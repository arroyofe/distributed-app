from sqlalchemy import Column, Integer, String, ForeignKey
from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy.orm import relationship

from db import engine, SessionLocal

Base = declarative_base()


class Pokemon(Base):
    __tablename__ = "pokemons"
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    type = Column(String(50))
    level = Column(Integer)
    # Enlace con tools
    tools = relationship("Tool", backref="owner")


class Tool(Base):
    __tablename__ = "tools"
    id = Column(Integer, primary_key=True, autoincrement=True)
    name = Column(String(100), nullable=False)
    description = Column(String(255))
    pokemon_id = Column(Integer, ForeignKey("pokemons.id"))


def init_db():
    # 1. Cr/eéación de las tablas (si no existen)
    Base.metadata.create_all(bind=engine)

    # 2. Inicialización de la base (Solo si la tabla está vacía)
    with SessionLocal() as s:
        if s.query(Pokemon).count() == 0:
            p1 = Pokemon(name="Pikachu", type="Électrique")
            s.add(p1)
            s.commit()  # On commit pour avoir l'ID de Pikachu

            t1 = Tool(name="Baya Aranja", description="Restaure 10 PV", pokemon_id=p1.id)
            s.add(t1)
            s.commit()
            print("Base Pokémon initialisée avec succès !")
