FROM eclipse-temurin:21-jdk-alpine AS build

WORKDIR /workspace

COPY . .

RUN sed -i 's/\r$//' mvnw \
    && chmod +x mvnw \
    && ./mvnw -B -ntp -pl importer -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

RUN addgroup -S app && adduser -S -G app app

COPY --from=build --chown=app:app /workspace/importer/target/*.jar app.jar

USER app

ENTRYPOINT ["java", "-jar", "/app/app.jar"]