from flask import Flask, jsonify
import random

app = Flask(__name__)

POKEMON_DATA = {
    "pikachu": {
        "type": "electric",
        "abilities": ["thunder-shock", "quick-attack", "iron-tail"]
    },
    "bulbasaur": {
        "type": "grass/poison",
        "abilities": ["vine-whip", "tackle", "razor-leaf"]
    },
    "charmander": {
        "type": "fire",
        "abilities": ["ember", "scratch", "flamethrower"]
    }
}

ITEMS = [
    {"id": 1, "name": "potion", "effect": "heal 20 HP"},
    {"id": 2, "name": "super-potion", "effect": "heal 50 HP"},
    {"id": 3, "name": "poke-ball", "effect": "catch a Pokémon"}
]

@app.get("/health")
def health():
    return {"status": "ok"}, 200

@app.get("/pokemon/<name>")
def get_pokemon(name):
    name = name.lower()
    if name in POKEMON_DATA:
        return jsonify(POKEMON_DATA[name]), 200
    return jsonify({"error": "Pokémon not found"}), 404

@app.get("/pokemon/<name>/abilities")
def get_abilities(name):
    name = name.lower()
    if name in POKEMON_DATA:
        return jsonify(POKEMON_DATA[name]["abilities"]), 200
    return jsonify({"error": "Pokémon not found"}), 404

@app.get("/items")
def get_items():
    return jsonify(ITEMS), 200

@app.get("/pokemon/random")
def random_pokemon():
    name = random.choice(list(POKEMON_DATA.keys()))
    return jsonify({name: POKEMON_DATA[name]}), 200

if __name__ == "__main__":
    app.run(host="0.0.0.0", port=5000)