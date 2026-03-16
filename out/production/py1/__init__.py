from flask import Flask
from .main import register_routes
from .errors import register_error_handlers
from .db import init_engine

def create_app():
    app = Flask(__name__)

    # DB Engine prêt (optionnel si tu n'utilises pas encore la DB)
    app.config["DB_ENGINE"] = init_engine()

    # Routes & erreurs
    register_routes(app)
    register_error_handlers(app)

    return app

# WSGI pour Gunicorn
app = create_app()