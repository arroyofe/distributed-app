# Etapa 1: Compilación del JAR con  Maven
FROM maven:3.9.9-eclipse-temurin-21 AS builder

WORKDIR /build

COPY pom.xml .
COPY backend/pom.xml backend/
COPY frontend/pom.xml frontend/
COPY websocket-server/pom.xml websocket-server/

COPY backend backend
COPY frontend frontend
COPY websocket-server websocket-server

RUN mvn -q -DskipTests clean install

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /build/websocket-server/target/websocket-server-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8090

# --- SOLUCIÓN SONAR COMPATIBLE CON TEMURIN ---

# 1. Creamos las carpetas necesarias como root
RUN mkdir -p /app/tmp /app/logs

# 2. Cambiamos el dueño del conjunto al ID 1000 (usuario estándar no-root)
# Usamos el ID directamente para evitar el error de "user not found" o "already exists"
RUN chown -R 1000:0 /app && \
    chmod -R 775 /app

# 3. Cambiamos al usuario 1000
USER 1000

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
