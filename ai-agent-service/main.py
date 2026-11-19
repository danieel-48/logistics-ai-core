from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from app.routers.logistics_router import router as logistics_router

app = FastAPI(
    title="Logistics AI Core - Microservicio de Importaciones",
    description="Microservicio de IA para el rastreo inteligente de contenedores marítimos y órdenes de compra de cosméticos.",
    version="1.0.0"
)

# CORS habilitado para el frontend de Angular (puerto 4200) y consumo desde Java
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

app.include_router(logistics_router, prefix="/api/v1")


@app.get("/health", tags=["System"])
async def health_check():
    return {"status": "ok", "service": "Logistics AI Core Service"}