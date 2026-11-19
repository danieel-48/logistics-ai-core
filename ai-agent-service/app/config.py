from pydantic_settings import BaseSettings, SettingsConfigDict
from pydantic import Field

class Settings(BaseSettings):
    model_config = SettingsConfigDict(
        env_file=".env",
        env_file_encoding="utf-8",
        extra="ignore"
        )


    database_url: str = Field(..., description="URL de conexión a la base de datos")
    openai_api_key: str = Field(..., description="API LLM")
    openai_base_url: str = Field(..., description="URL del LLM")
    model_name: str = Field(..., description="Nombre del modelo LLM")

settings = Settings()

