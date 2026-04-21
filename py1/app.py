import time

from flask import Flask, jsonify, request
from sqlalchemy.exc import OperationalError

from db import SessionLocal, engine
from models import Pokemon, Base, Tool

app = Flask(__name__)


# --- INICIALIZACION DE LA BASE  ---
def init_db():
    retries = 5
    while retries > 0:
        try:
            # Crea las tablas si no existen
            Base.metadata.create_all(bind=engine)
            with SessionLocal() as s:
                # Se verifica si la tabla está vacía antes de insertar
                if s.query(Pokemon).count() == 0:
                    p1 = Pokemon(name="Pikachu", type="Electrico", level=25)
                    s.add(p1)
                    s.commit()  # El comit sirve para genera el ID de p1

                    t1 = Tool(name="Baya Aranja", description="Cura 10 PV", pokemon_id=p1.id)
                    s.add(t1)
                    s.commit()  # El comit sirve para genera el ID de p1
            print(">>> Base Pokemon y Tools inicializada.")
            break
        except OperationalError:
            retries -= 1
            print(f">>> Esperando a  MySQL... ({retries} intentos restantes)")
            time.sleep(5)  # Espera 5 segundos anted de voler a intentarlo


# --- LANZAMIENTO DE LA INICIALIZACION ---

init_db()


# --- RUTES API PARA JAVA ---

@app.get("/health")
def health():
    return {"status": "ok"}, 200


# --- OBTENCION DE LA LISTA DE POKEMONES ---

@app.get("/pokemons")
def list_pokemons():
    with SessionLocal() as s:
        # rows = s.execute(sa_select(Pokemon)).scalars().all()
        # Uso de la sesión para interrogar directamente al modelo
        rows = s.query(Pokemon).all()

        return jsonify([{"id": p.id, "name": p.name, "type": p.type, "level": p.level} for p in rows]), 200


@app.get("/pokemons/<int:pokemon_id>/tools")
def list_tools(pokemon_id):
    with SessionLocal() as s:
        # Recupera las herramientas ligadas a un Pokémon específico
        tools = s.query(Tool).filter(Tool.pokemon_id == pokemon_id).all()
        return jsonify([{"id": t.id, "name": t.name, "description": t.description} for t in tools]), 200


# --- CREACION DE UN POKEMON NUEVO ---

@app.route("/pokemons", methods=['POST'], strict_slashes=False)
def add_pokemon():
    # Recupera el JSON enviado por Java. Se fuerza a Flask a recuperar como Json
    data = request.get_json(force=True)

    # Vérification de sécurité minimale
    if not data or "name" not in data:
        return {"error": "El nombre de ese Pokémon no existe"}, 400

    with SessionLocal() as s:
        # Creación del objet Pokémon con datos
        new_p = Pokemon(
            name=data['name'],
            type=data.get('type', 'Inconnu'),
            level=data.get('level', 1)
        )
        s.add(new_p)
        s.commit()
        s.refresh(new_p)  # Recupera el ID generado

        print(f">>> Se ha añadido un Pokémon : {new_p.name}")
        return {"id": new_p.id, "status": "success"}, 201


# --- MODIFICACION ---
@app.route("/pokemons/<int:pokemon_id>", methods=['PUT'], strict_slashes=False)
def update_pokemon(pokemon_id):
    data = request.get_json(force=True)
    with SessionLocal() as s:
        p = s.get(Pokemon, pokemon_id)
        if not p:
            return {"error": "not found"}, 404
        p.name = data.get("name", p.name)
        p.type = data.get("type", p.type)
        p.level = data.get("level", p.level)
        s.commit()
        return {"status": "updated", "message": "updated"}, 200


# --- SUPRESION ---
@app.route("/pokemons/<int:pokemon_id>", methods=['DELETE'], strict_slashes=False)
def delete_pokemon(pokemon_id):
    with SessionLocal() as s:
        p = s.get(Pokemon, pokemon_id)
        if not p: return {"error": "not found"}, 404
        s.delete(p)
        s.commit()
        return {"status": "deleted"}, 200


# --- OBTENER UN POKEMON UNICO ---
@app.get("/pokemons/<int:pokemon_id>")
def get_pokemon(pokemon_id):
    with SessionLocal() as s:
        p = s.get(Pokemon, pokemon_id)
        if not p: return {"error": "Pokemon no encontrado"}, 404
        return jsonify({"id": p.id, "name": p.name, "type": p.type, "level": p.level}), 200
