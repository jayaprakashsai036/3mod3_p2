from flask import Flask, jsonify, request
from flask_cors import CORS

app = Flask(__name__)
CORS(app)


@app.route("/")
def home():
    return jsonify({
        "status": "success",
        "message": "3mod3_p2 Python backend is running",
        "backend": "Python Flask",
        "frontend": "Kotlin"
    })


@app.route("/api/hello", methods=["GET"])
def hello():
    return jsonify({
        "status": "success",
        "message": "Hello from Python backend"
    })


@app.route("/api/user", methods=["POST"])
def user():
    data = request.get_json() or {}

    name = data.get("name", "Unknown")
    age = data.get("age", 0)

    return jsonify({
        "status": "success",
        "name": name,
        "age": age,
        "message": f"Welcome {name}"
    })


@app.route("/health")
def health():
    return jsonify({
        "status": "healthy"
    })


if __name__ == "__main__":
    app.run(
        host="0.0.0.0",
        port=5000,
        debug=True
    )
