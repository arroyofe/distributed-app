import os
from sqlalchemy import create_engine, text
from sqlalchemy.exc import SQLAlchemyError

def _env(name, default=None):
    v = os.getenv(name, default)
    if v is None:
        raise RuntimeError(f"Missing environment variable: {name}")
    return v

def init_engine():
    user = _env("DB_USER", "appuser")
    pwd  = _env("DB_PASSWORD", "password")
    host = _env("DB_HOST", "localhost")
    port = _env("DB_PORT", "3306")
    name = _env("DB_NAME", "appdb")

    # Choisis l’un des deux (décommente la ligne correspondante) :
    # url = f"mysql+mysqldb://{user}:{pwd}@{host}:{port}/{name}?charset=utf8mb4"
    url = f"mysql+pymysql://{user}:{pwd}@{host}:{port}/{name}?charset=utf8mb4"

    engine = create_engine(
        url,
        pool_pre_ping=True,
        pool_size=5,
        max_overflow=5
    )
    return engine

def health_check_db(engine):
    try:
        with engine.connect() as conn:
            conn.execute(text("SELECT 1"))
        return True, None
    except SQLAlchemyError as e:
        return False, str(e)


def list_items(engine, limit: int = 50, offset: int = 0):
    sql = text("""
               SELECT id, name, description, created_at, updated_at
               FROM demo_item
               ORDER BY id ASC
               LIMIT :limit OFFSET :offset
               """)
    with engine.connect() as conn:
        rows = conn.execute(sql, {"limit": limit, "offset": offset}).mappings().all()
        return [dict(r) for r in rows]
