# ETAPE 1 : Construcción (Build)
# Utilización de una imagen Maven con JDK 21 para compilar el proyecto
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1) Optimización del cache Docker : Se copia primero los ficheross de configuración Maven
# Se incluye el POM padre y los POM de los módulos para adecuar el lanzameinteo a la estructure multi-módulos
COPY pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

# 2) Copia de la integralidad del código fuente
# Se necesita aquí porque Maven debe validar la coherencia entre los módulos
COPY backend ./backend
COPY frontend ./frontend

# 3) Compilación y empaquetado
# -pl backend : Se quiere realizar la operación solamente en el módule backend
# -am (Also Make) : Se compila igualmente las dependencias necesarias (por ej.: los módulos compartidos)
# -DskipTests : On ignore les tests unitaires pour accélérer le build en CI/CD
RUN mvn -q -DskipTests -pl backend -am package

# ETAPA 2 : Imagen final de producción (Runtime)
# Utilización de un JRE (Java Runtime Environment) ligero, suficiente para este tipo de ejecución
FROM eclipse-temurin:21-jre
WORKDIR /app

# Recuperación del artefacto compilado únicamente (.jar) desde la etapa del build
COPY --from=build /build/backend/target/backend-0.0.1-SNAPSHOT.jar app.jar

# Documentación del puerto de escucha (8080 para el backend Spring Boot)
EXPOSE 8080

# Comando de lanzamiento de la aplicación
ENTRYPOINT ["java","-jar","app.jar"]
