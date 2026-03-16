from flask import jsonify

def register_routes(app):

    @app.get("/healthz")
    def healthz():
        return jsonify({"status": "ok"}), 200

    @app.get("/info")
    def info():
        return jsonify({"service": "py2", "version": "0.1.0"}), 200