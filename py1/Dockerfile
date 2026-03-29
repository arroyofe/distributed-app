FROM python:3.12-slim

# OS deps
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential libpq-dev gcc && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Se copia todos los ficheros Python
COPY . .

EXPOSE 5000
# Se ha forzado un único worker para evitar los conflictos de creación de tablas al arranque
# No es la solución recomendable para un uso real, debe quedar limitado a este proyecto académico
CMD ["gunicorn", "--workers", "1", "--bind", "0.0.0.0:5000", "app:app"]