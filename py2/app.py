from flask import Flask, jsonify, request
from flask import render_template, redirect, url_for
from sqlalchemy import select
from sqlalchemy.exc import SQLAlchemyError

from db import SessionLocal
from models import Item

app = Flask(__name__)

# ====== constante para Sonar =======
APY = "/api3"


# ========== INTERFACE HTML ==========

@app.get("/items/web")
def web_list_items():
    with SessionLocal() as s:
        rows = s.execute(select(Item)).scalars().all()
        return render_template("list.html", items=rows)


@app.get("/items/web/new")
def web_new_item_form():
    return render_template("new.html")


@app.post("/items/web/new")
def web_new_item_submit():
    data = request.form
    with SessionLocal() as s:
        it = Item(name=data["name"], description=data.get("description"))
        s.add(it)
        s.commit()
        return redirect(APY + url_for("web_list_items"))


@app.get("/items/web/<int:item_id>/edit")
def web_edit_item_form(item_id):
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        return render_template("edit.html", item=it)


@app.post("/items/web/<int:item_id>/edit")
def web_edit_item_submit(item_id):
    data = request.form
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        it.name = data.get("name", it.name)
        it.description = data.get("description", it.description)
        s.commit()
        return redirect("/api3" + url_for("web_list_items"))


@app.post("/items/web/<int:item_id>/delete")
def web_delete_item(item_id):
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        s.delete(it)
        s.commit()
        return redirect(APY + url_for("web_list_items"))


# --- HEALTHCHECK ---
@app.get("/health")
def health():
    try:
        with SessionLocal() as s:
            s.execute(select(1))
        return {"status": "ok", "db": "up"}, 200
    except SQLAlchemyError as e:
        return {"status": "degraded", "db": "down", "error": str(e)}, 200


# --- LIST ITEMS ---
@app.get("/items")
def list_items():
    with SessionLocal() as s:
        rows = s.execute(select(Item)).scalars().all()
        return jsonify([
            {"id": x.id, "name": x.name, "description": x.description}
            for x in rows
        ]), 200


# --- ADD ITEM ---
@app.post("/items")
def add_item():
    data = request.get_json(force=True)
    # Verificación por si el JSON no es válido o faltan campos
    if not data or "name" not in data:
        return {"error": "Missing name"}, 400

    with SessionLocal() as s:
        it = Item(
            name=data["name"],
            description=data.get("description")
        )
        s.add(it)
        s.commit()
        s.refresh(it)
        return {"id": it.id}, 201


# --- UPDATE ITEM ---
@app.route("/items/<int:item_id>", methods=['PUT', 'POST'])  # Autorise les deux
def update_item(item_id: int):
    data = request.get_json(force=True)
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        if not it:
            return {"error": "not found"}, 404

        it.name = data.get("name", it.name)
        it.description = data.get("description", it.description)
        s.commit()

        return {"status": "updated"}, 200


# --- GET SINGLE ITEM (Llamado por Spring Boot para la edición) ---
@app.get("/items/<int:item_id>")
def get_single_item(item_id: int):
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        if not it:
            return {"error": "Item not found"}, 404
        # On renvoie le JSON pur attendu par le ItemDto Java
        return {
            "id": it.id,
            "name": it.name,
            "description": it.description
        }, 200


# --- DELETE ITEM ---
@app.delete("/items/<int:item_id>")
def delete_item(item_id: int):
    with SessionLocal() as s:
        it = s.get(Item, item_id)
        if not it:
            return {"error": "not found"}, 404

        s.delete(it)
        s.commit()

        return {"status": "deleted"}, 200
