FROM python:3.13-slim

# OS deps
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential libpq-dev gcc && rm -rf /var/lib/apt/lists/*

WORKDIR /app
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# 👉 Se copia todos los ficheros Python
COPY . .

EXPOSE 5000
CMD ["gunicorn", "-c", "gunicorn.conf.py", "app:app"]