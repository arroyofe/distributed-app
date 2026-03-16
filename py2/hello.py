
@app.get("/api/hello")
def hello():
    return {"message": "hello from py2"}, 200
