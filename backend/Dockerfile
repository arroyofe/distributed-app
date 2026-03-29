# backend/Dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1) Copiar los POM padre y los hijos para asegurar el comportamiento del flujo
COPY pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

# 2) Copiar TODAS las fuentes antes de lanzar Maven
COPY backend ./backend
COPY frontend ./frontend

# 3) Construir unicamente el módulo backend (+ y sus dependencias  via -am)
#    (con esto se evita el go-offline y permite evitar el parse en contexto parcial)
#RUN echo "=== Listing /build ===" && ls -la && echo "=== Recursive ===" && ls -R
RUN mvn -q -DskipTests -pl backend -am package

# ---------- Runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/backend/target/backend-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]