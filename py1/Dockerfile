# Utilización de une image Python ligera basada enDebian Bookworm
FROM python:3.12-slim

# Instalación de las dependencias sistema necesarias para compilar ciertos módulos  Python (ej.: conectores DB)
# La limpieza de las listas apt permite la reducción del tamaño final de la imagen.
RUN apt-get update && apt-get install -y --no-install-recommends \
    build-essential libpq-dev gcc && rm -rf /var/lib/apt/lists/*

WORKDIR /app

# Configuración del entorno Python :
# PYTHONDONTWRITEBYTECODE : Impide la creación de ficheros .pyc (mejor para uso en contenedores)
# PYTHONUNBUFFERED : Fuerza la presentación inmediata de los logs en la consola Docker (indispensable para el débug)
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1

# Instalación de dependencias Python
# Se copia primero el fichero requirements para optimizar el cache de las capas Docker
COPY py1/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia del código fuente del microservicio py1
COPY py1/ ./py1/

# Documentación del puerto interno utilizado por Flask/Gunicorn
EXPOSE 5000

# --- SOLUCIÓN SONAR: Usuario no-root ---

# 1. Aseguramos que la carpeta /app pertenece al ID 1000 (usuario estándar)
# El grupo 0 (root) es una práctica recomendada en contenedores para permisos.
RUN chown -R 1000:0 /app && chmod -R 775 /app

# 2. Cambiamos al usuario por su ID
USER 1000

# Lanzamiento via Gunicorn (servidor WSGI de produccióon)
# NOTA : Se usa un worker único para garantizar la coherencia de las operaciones en la base
# de datos compartidos en el momento de la inicialización del proyecto.
CMD ["gunicorn", "--chdir", "/app/py1", "--bind", "0.0.0.0:5000", "--workers", "1", "-c", "/app/py1/gunicorn.conf.py", "app:app"]