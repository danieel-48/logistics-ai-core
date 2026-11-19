from fastapi import APIRouter, HTTPException, status
from app.schemas.logistics_agent import LogisticsChatRequest, LogisticsChatResponse
from app.services.logistics_agent import logistics_agent_service

router = APIRouter(prefix="/logistics", tags=["Logistics & Import Agent"])


@router.post(
    "/chat",
    response_model=LogisticsChatResponse,
    summary="Consultar estado de importaciones y contenedores",
    description="Permite a vendedores y directores consultar en lenguaje natural en qué contenedor vienen los productos y su estado en aduanas."
)
async def chat_with_logistics_agent(payload: LogisticsChatRequest) -> LogisticsChatResponse:
    try:
        return await logistics_agent_service.run_agent(user_prompt=payload.prompt)
    except Exception as e:
        raise HTTPException(
            status_code=status.HTTP_500_INTERNAL_SERVER_ERROR,
            detail=f"Error en el agente de logística: {str(e)}"
        )