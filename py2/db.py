import os
from sqlalchemy import create_engine
from sqlalchemy.orm import sessionmaker

def _dsn() -> str:
    user = os.getenv("DB_USER", "appuser")
    pwd  = os.getenv("DB_PASSWORD", "password")
    host = os.getenv("DB_HOST", "db")
    port = os.getenv("DB_PORT", "3306")
    name = os.getenv("DB_NAME", "appdb")
    # charset utf8mb4 conseillé
    return f"mysql+pymysql://{user}:{pwd}@{host}:{port}/{name}?charset=utf8mb4"

engine = create_engine(_dsn(), pool_pre_ping=True, future=True)
SessionLocal = sessionmaker(bind=engine, autoflush=False, autocommit=False, future=True)
