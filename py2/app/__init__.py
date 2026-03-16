from flask import Flask
from .main import register_routes
from .errors import register_error_handlers

def create_app():
    app = Flask(__name__)
    register_routes(app)
    register_error_handlers(app)
    return app

app = create_app()