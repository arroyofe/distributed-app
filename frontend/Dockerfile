# frontend/Dockerfile
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

COPY pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

COPY backend ./backend
COPY frontend ./frontend

RUN mvn -q -DskipTests -pl frontend -am package

FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /build/frontend/target/*.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java","-jar","app.jar"]