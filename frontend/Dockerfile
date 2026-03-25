# Frontend Dockerfile
# --- Etapa de constucción ---------------------------------------------------------------
FROM maven:3.9-eclipse-temurin-21 AS build
WORKDIR /build

# Copiar  el proyecto entreo
# COPY .. . VERSION ANTIGUA
# Copiar solamente lo necesario test de recuperación
COPY ../pom.xml .
COPY ../frontend ./frontend
COPY ../backend ./backend


# Construir el módulo frontend y sus dependencias
RUN mvn -q -DskipTests -pl frontend -am package

# --- Etapa de lanzamiento -------------------------------------------------------------
FROM eclipse-temurin:21-jre
WORKDIR /app

# Instalación de curl para el healthchecker
RUN apt-get update && apt-get install -y curl && rm -rf /var/lib/apt/lists/*

COPY --from=build /build/frontend/target/*.jar app.jar

EXPOSE 8081
ENTRYPOINT ["java", "-jar", "/app/app.jar"]