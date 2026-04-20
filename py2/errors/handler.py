from flask import Flask, render_template
import traceback, json

from errors.classifier import classify_error

app = Flask(__name__)

@app.errorhandler(Exception)
def handle_exception(e):
    info = {
        "type": type(e).__name__,
        "message": str(e),
        "category": classify_error(e),
        "stacktrace": traceback.format_exc()
    }

    return render_template("error.html", errorJson=json.dumps(info)), 500
