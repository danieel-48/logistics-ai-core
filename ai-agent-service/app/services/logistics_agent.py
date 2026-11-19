from langchain_core.messages import SystemMessage, HumanMessage
from langchain_google_genai import ChatGoogleGenerativeAI
from langgraph.graph import StateGraph, MessagesState, START, END
from langgraph.prebuilt import ToolNode, tools_condition
from langgraph.errors import GraphRecursionError

from app.config import settings
from app.schemas.logistics_agent import LogisticsActionsStep, LogisticsChatResponse
from app.services.logistics_tools import (
    buscar_producto_en_transito,
    consultar_estado_contenedor,
    listar_contenedores_en_transito
)

# Lista de herramientas conectadas al ToolNode
tools = [
    buscar_producto_en_transito,
    consultar_estado_contenedor,
    listar_contenedores_en_transito
]

class LogisticsAgentService:
    def __init__(self):
        # 1. Enlazamos las herramientas directamente al LLM (Tool Calling nativo)
        self.llm = ChatGoogleGenerativeAI(
            model=settings.model_name,
            google_api_key=settings.openai_api_key,
            temperature=0.0,
            max_retries=3 
        ).bind_tools(tools)

        # 2. Construimos la Máquina de Estados (StateGraph)
        workflow = StateGraph(MessagesState)

        # 3. Agregamos los Nodos del Grafo
        workflow.add_node("agent", self._call_model)
        workflow.add_node("tools", ToolNode(tools))

        # 4. Definimos el Flujo (Aristas)
        workflow.add_edge(START, "agent")

        # tools_condition evalúa automáticamente si el LLM pidió una Tool o si terminó
        workflow.add_conditional_edges("agent", tools_condition)

        # Del nodo 'tools' regresamos siempre al 'agent' para que procese el resultado
        workflow.add_edge("tools", "agent")

        # 5. Compilamos el grafo ejecutable
        self.app = workflow.compile()

    def _call_model(self, state: MessagesState):
        """Nodo que invoca al modelo con los mensajes acumulados."""
        messages = state["messages"]
        response = self.llm.invoke(messages)
        return {"messages": [response]}

    async def run_agent(self, user_prompt: str) -> LogisticsChatResponse:
        
        system_instruction = SystemMessage(
            content=(
                """
                Eres el Asistente Experto en Comercio Exterior y Logística de Importaciones de una empresa de cosméticos.
                Tu objetivo es dar respuestas claras, profesionales y exhaustivas sobre el estado de la mercancía en tránsito.
                SIEMPRE debes invocar tus herramientas disponibles para consultar la base de datos real.

                DIRECTIVA DE EXHAUSTIVIDAD (MÚLTIPLES RESULTADOS):
                - Si la consulta a la base de datos devuelve una lista con más de un resultado o variante, DEBES desglosar y detallar TODOS los registros encontrados sin omitir ninguno.
                - Para cada registro, informa: nombre exacto del producto, SKU, número de contenedor, unidades en camino, estado actual y fecha estimada de llegada (ETA).
                - Si algún contenedor presenta novedades aduaneras (ej. inspección sanitaria INVIMA o aforo DIAN), explica el motivo con claridad.
                - Nunca inventes números de contenedores ni fechas de llegada."""
            )
        )

        inputs = {
            "messages": [system_instruction, HumanMessage(content=user_prompt)]
        }
        try:
            final_state = await self.app.ainvoke(
                inputs,
                config={"recursion_limit": 10}
            )
        except GraphRecursionError:
            # 2. Respuesta controlada si el modelo supera el límite de intentos
            return LogisticsChatResponse(
                prompt=user_prompt,
                answer="Se alcanzó el límite máximo de consultas internas sin poder resolver la solicitud. Por favor, intenta reformular tu pregunta con datos más específicos.",
                steps_executed=[]
            )

         # Mapeamos las respuestas reales devueltas por cada herramienta (ToolMessage)
        tool_outputs = {}
        for msg in final_state["messages"]:
            if hasattr(msg, "tool_call_id") and msg.tool_call_id:
                tool_outputs[msg.tool_call_id] = msg.content
        # Auditamos las herramientas con sus datos reales para Swagger / Frontend
        steps_executed: list[LogisticsActionsStep] = []
        for msg in final_state["messages"]:
            if hasattr(msg, "tool_calls") and msg.tool_calls:
                for tc in msg.tool_calls:
                    call_id = tc.get("id")
                    steps_executed.append(
                        LogisticsActionsStep(
                            tool_name=tc["name"],
                            arguments=tc["args"],
                            output=tool_outputs.get(call_id, "Ejecutado por LangGraph ToolNode")
                        )
                    )

        last_message = final_state["messages"][-1].content
        if isinstance(last_message, list):
            clean_text = "".join(part.get("text", "") for part in last_message if isinstance(part, dict))
        else:
            clean_text = str(last_message)


        return LogisticsChatResponse(
            prompt=user_prompt,
            answer=clean_text,
            steps_executed=steps_executed
        )


logistics_agent_service = LogisticsAgentService()