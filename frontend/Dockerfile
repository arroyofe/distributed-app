# ETAPA 1: Construcción (Build)
# Se utiliza una imagen de Maven con JDK 21 para compilar el módulo frontend.
FROM maven:3.9.9-eclipse-temurin-21 AS build
WORKDIR /build

# 1) Preparación de la estructura multi-módulo.
# Se copian los archivos de configuración (POM) para resolver dependencias entre módulos.
COPY pom.xml ./pom.xml
COPY backend/pom.xml backend/pom.xml
COPY frontend/pom.xml frontend/pom.xml

# 2) Copia del código fuente de ambos módulos.
# Necesario para que Maven valide las referencias cruzadas durante el empaquetado.
COPY backend ./backend
COPY frontend ./frontend

# 3) Compilación específica del frontend.
# -pl frontend: Indica a Maven que solo empaquete el módulo de interfaz.
# -am: Asegura que se construyan las dependencias necesarias del proyecto raíz.
RUN mvn -q -DskipTests -pl frontend -am package

# ETAPA 2: Entorno de Ejecución (Runtime)
# Imagen optimizada con JRE para minimizar el tamaño del contenedor final.
FROM eclipse-temurin:21-jre
WORKDIR /app

# Se recupera el artefacto generado en la etapa anterior.
# El uso de *.jar facilita el mantenimiento si cambia la versión del proyecto.
COPY --from=build /build/frontend/target/*.jar app.jar

# Exposición del puerto 8081 (puerto configurado para el frontend en docker-compose).
EXPOSE 8081

# Ejecución de la aplicación Spring Boot.
ENTRYPOINT ["java","-jar","app.jar"]
