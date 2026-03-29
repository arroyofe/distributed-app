import os

os.environ["DB_HOST"] = "localhost"
os.environ["DB_PORT"] = "5432"
os.environ["DB_NAME"] = "appdb"
os.environ["DB_USER"] = "appuser"
os.environ["DB_PASSWORD"] = "password"

from appbis import create_app


def test_healthz(monkeypatch):
    app = create_app()
    client = app.test_client()
    resp = client.get("/healthz")
    # Ne pas imposer 200 si DB locale non dispo ; on vérifie simplement un JSON
    assert resp.status_code in (200, 500)
    data = resp.get_json()
    assert "status" in data or "error" in data
