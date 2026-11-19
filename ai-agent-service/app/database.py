from sqlalchemy import create_engine
from sqlalchemy.orm import declarative_base, sessionmaker
from app.config import settings

# Motor de conexión con pool de alta resiliencia
engine = create_engine(
    settings.database_url,
    pool_pre_ping=True,
    pool_size=10,
    max_overflow=20
)

SessionLocal = sessionmaker(autocommit=False, autoflush=False, bind=engine)
Base = declarative_base()


def get_db():
    """Inyector de dependencias para sesiones de base de datos."""
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()
        