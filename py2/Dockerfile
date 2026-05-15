# Utilización de la imagen Python 3.12 version "slim" para obtener un conteneur ligero y securizado
FROM python:3.12-slim

WORKDIR /app

# Configuración de las variables de entorno de Python :
# PYTHONDONTWRITEBYTECODE : Evita la generación de ficheros caché .pyc que no son útiles en Docker.
# PYTHONUNBUFFERED : Asegura que los logs de la aplicación Flask se transmiten en tiempo real.
ENV PYTHONDONTWRITEBYTECODE=1 PYTHONUNBUFFERED=1

# Gestión de las dependencias
# La copia aislada del fichero requirements permite no tener que reinstalar los paquetes
# en caso de cambie solamente el código guente de la application (optimiZA el cache Docker).
COPY py2/requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia de la totalidad de las fuentes del módulo  py2 (scripts, logica y configuración)
COPY py2/ ./py2/

# Puerto de escucha interna dek servicio
EXPOSE 5000

# --- SOLUCIÓN SONAR: Usuario no-root ---
# 1. Aseguramos que la carpeta /app pertenece al ID 1000 (usuario estándar)
# El grupo 0 (root) es una práctica recomendada en contenedores para permisos.
RUN chown -R 1000:0 /app && chmod -R 775 /app

# 2. Cambiamos al usuario por su ID
USER 1000

# Lanzamiento via Gunicorn (servidor WSGI de produccióon)
CMD ["gunicorn", "--chdir", "/app/py2", "--bind", "0.0.0.0:5000", "-c", "/app/py2/gunicorn.conf.py", "app:app"]
