# backend/Dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1) Copier POM parent + modules
COPY pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

# 2) Copier TOUTES les sources avant de lancer Maven (réacteur satisfait)
COPY backend ./backend
COPY frontend ./frontend

# 3) Construire uniquement le module backend (+ ses deps via -am)
#    (on évite go-offline pour contourner le parse en contexte partiel)
#RUN echo "=== Listing /build ===" && ls -la && echo "=== Recursive ===" && ls -R
RUN mvn -q -DskipTests -pl backend -am package

# ---------- Runtime ----------
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/backend/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","app.jar"]