FROM python:3.12-slim

WORKDIR /app
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1

COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

#  copier l’ENSEMBLE des fichiers Python (y compris app.py)
COPY . .

EXPOSE 5000
CMD ["gunicorn", "-c", "gunicorn.conf.py", "app:app"]