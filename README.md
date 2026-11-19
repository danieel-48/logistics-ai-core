# Logistics AI Core

Plataforma para consultar y monitorear importaciones, contenedores marítimos, productos cosméticos, estados aduaneros y órdenes de compra mediante una interfaz web y un agente conversacional de IA.

## Arquitectura

El proyecto está compuesto por cuatro servicios:

Angular :4200
    ↓
Spring Boot :8085
    ├── PostgreSQL :5432
    └── Python AI Agent :8000
             ├── LangGraph
             ├── Google Gemini
             └── PostgreSQL

## Funcionamiento
Angular muestra los contenedores obtenidos desde Java.
El usuario consulta información al chat.
Java envía la pregunta al microservicio Python.
LangGraph determina qué herramienta debe ejecutar.
La herramienta consulta PostgreSQL.
Gemini genera una respuesta utilizando los resultados obtenidos.
Java devuelve la respuesta y las herramientas ejecutadas al frontend.
Angular muestra una auditoría de las consultas realizadas por el agente.

El agente dispone de las siguientes herramientas:

buscar_producto_en_transito
consultar_estado_contenedor
listar_contenedores_en_transito

El proyecto utiliza Tool Calling con LangGraph y consultas SQL mediante SQLAlchemy. No implementa RAG ni búsqueda vectorial.

## 🚀 Despliegue Rápido en 1 Comando (Docker Compose)

### Requisitos previos:
* Docker & Docker Desktop instalado.
* Obtener una API Key de [Google AI Studio](https://aistudio.google.com/).
### Pasos:
1. Clonar el repositorio:
   ```bash
   git clone https://github.com/TU_USUARIO/logistics-ai-core.git
   cd logistics-ai-core
2. Configurar la clave de IA:
cp ai-agent-service/.env.example ai-agent-service/.env
# Edita ai-agent-service/.env y pega tu OPENAI_API_KEY
3. Levantar toda la arquitectura con Docker Compose:
docker compose up -d --build

### Puntos de Acceso:
Frontend Dashboard & Chatbot: http://localhost:4200
Swagger UI (Java Core Backend): http://localhost:8085/swagger-ui.html
Swagger UI (Python AI Agent): http://localhost:8000/docs
Base de Datos PostgreSQL: localhost:5432 (db: logistics_db)

### Tecnologias y Patrones 
Clean Architecture & SOLID: Dominio agnóstico a frameworks, separación estricta de puertos y adaptadores.
Java 21, Spring Boot 3.3.5: Uso intensivo de records nativos sin dependencias de terceros (cero Lombok).
LangGraph: Máquina de estados cognitivos con tolerancia a fallos de red (max_retries=3) y auditoría auditable de herramientas (steps_executed).
Angular 22 Signals: Gestión de estado reactiva síncrona sin subscripciones manuales de RxJS.
SQL Tolerante: Consultas a PostgreSQL mediante operadores regex POSIX (~*) agnósticos a acentos y plurales.

## Estructura del Proyecto
logistics-ai-core/
├── ai-agent-service/
│   ├── app/
│   │   ├── models/
│   │   ├── routers/
│   │   ├── schemas/
│   │   └── services/
│   ├── main.py
│   └── pyproject.toml
├── core-backend-service/
│   ├── src/
│   └── pom.xml
├── frontend-angular/
│   ├── src/
│   ├── angular.json
│   └── package.json
├── init_logistics.sql
└── docker-compose.yml