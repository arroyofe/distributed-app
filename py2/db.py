import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker


def _dsn() -> str:
    user = os.getenv("DB_USER", "appuser")
    pwd = os.getenv("DB_PASSWORD")  # se enmascara el valor por confidencialidad
    host = os.getenv("DB_HOST", "db")
    port = os.getenv("DB_PORT", "3306")
    name = os.getenv("DB_NAME", "appdb")

    if not pwd:
        raise RuntimeError("DB_PASSWORD environment variable must be set")

    return f"mysql+pymysql://{user}:{pwd}@{host}:{port}/{name}?charset=utf8mb4"


engine = create_engine(_dsn(), pool_pre_ping=True, future=True)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False, future=True)
