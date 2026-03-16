from flask import Flask, jsonify

app = Flask(__name__)

@app.get("/healthz")
def healthz():
    return {"status": "ok"}, 200

# ✅ alias de santé générique
@app.get("/health")
def health():
    return {"status": "ok"}, 200

@app.get("/api/hello")  # <-- utilisé par le backend
def hello():
    return jsonify(message="Hello from py2"), 200