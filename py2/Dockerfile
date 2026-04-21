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
COPY requirements.txt .
RUN pip install --no-cache-dir -r requirements.txt

# Copia de la totalidad de las fuentes del módulo  py2 (scripts, logica y configuración)
COPY . .

# Port d'écoute interne du service d'items
EXPOSE 5000

# Lancement du serveur WSGI de production Gunicorn.
# L'option "-c" délègue la configuration (workers, timeouts, logs) au fichier 
# externe gunicorn.conf.py, assurant une gestion plus granulaire et lisible.
CMD ["gunicorn", "-c", "gunicorn.conf.py", "app:app"]
