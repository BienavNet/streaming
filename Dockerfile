# syntax=docker/dockerfile:1

# ---------- Etapa 1: build ----------
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app

# Copiamos primero lo mínimo para cachear las dependencias de Maven
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
# Evita errores si mvnw se guardó con saltos de línea CRLF (Windows)
RUN sed -i 's/\r$//' mvnw && chmod +x mvnw \
    && ./mvnw -B -q dependency:go-offline

# Ahora el código fuente
COPY src ./src
RUN ./mvnw -B -q clean package -DskipTests

# ---------- Etapa 2: runtime ----------
FROM eclipse-temurin:25-jre
WORKDIR /app

# Usuario sin privilegios
RUN groupadd --system app && useradd --system --gid app --no-create-home app

COPY --from=build /app/target/*.jar app.jar

USER app
EXPOSE 8080

ENTRYPOINT ["java", "-XX:MaxRAMPercentage=75.0", "-jar", "app.jar"]
