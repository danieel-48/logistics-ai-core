from pydantic import BaseModel, Field
from typing import Any

class LogisticsActionStep(BaseModel):
    tool_name: str
    arguments: dict[str, Any]
    output: Any

class LogisticsChatResponse(BaseModel):
    prompt: str
    answer: str
    steps_executed: list[LogisticsActionStep] = []

class LogisticsChatRequest (BaseModel):
    """Payload de entrada que enviará el frontend en Angular o el backend de Java."""

    prompt: str = Field(..., min_length=2, description="Consulta en lenguaje natural sobre contenedores o productos")

class LogisticsActionsStep(BaseModel):
    """Registro de trazabilidad de cada herramienta de base de datos invocada."""
    
    tool_name: str
    arguments: dict[str, Any]
    output: Any 

class LogisticsChatResponse(BaseModel):
     """Respuesta consolidada para el usuario con auditoría de herramientas."""

     prompt: str
     answer: str
     steps_executed: list[LogisticsActionsStep] = Field(
         default_factory=list,
         description="Herramientas ejecutadas para responder a la consulta"
     )

