from flask import jsonify, request
from werkzeug.exceptions import HTTPException
from datetime import datetime, timezone

def make_error(status: int, error: str, message: str):
    body = {
        "timestamp": datetime.now(timezone.utc).isoformat(),
        "status": status,
        "error": error,
        "message": message or "",
        "path": request.path
    }
    return jsonify(body), status

def register_error_handlers(app):
    @app.errorhandler(HTTPException)
    def handle_http(e: HTTPException):
        return make_error(e.code, e.name, e.description)

    @app.errorhandler(404)
    def not_found(e):
        return make_error(404, "Not Found", "Page non trouvée")

    @app.errorhandler(Exception)
    def handle_generic(e: Exception):
        return make_error(500, "Internal Error", str(e))