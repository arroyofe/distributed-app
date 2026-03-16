from flask import jsonify, request
from .errors import make_error
from .db import health_check_db, list_items

def register_routes(app):

    @app.get("/healthz")
    def healthz():
        ok, err = health_check_db(app.config["DB_ENGINE"])
        if ok:
            return jsonify({"status": "ok"}), 200
        return make_error(500, "Database Error", err)

    @app.get("/pokemon/<name>")
    def get_pokemon(name: str):
        if name.lower() == "pikachu":
            return jsonify({"name": "Pikachu", "type": "electric"}), 200
        return make_error(404, "Not Found", f"Pokemon {name} introuvable")

    @app.get("/items")
    def get_items():
        # Paramètres de pagination ?limit=&offset=
        try:
            limit = int(request.args.get("limit", 50))
            offset = int(request.args.get("offset", 0))
        except ValueError:
            return make_error(400, "Bad Request", "Paramètres limit/offset invalides")

        try:
            items = list_items(app.config["DB_ENGINE"], limit=limit, offset=offset)
            return jsonify({"items": items, "count": len(items)}), 200
        except Exception as e:
            # DB inconnue/non migrée → 500
            return make_error(500, "Database Error", str(e))