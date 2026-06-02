# syntax=docker/dockerfile:1

# Build stage uses the full JDK because Maven needs compilation tools.
FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

# Copy Maven metadata first so dependency downloads can be cached between source changes.
COPY .mvn .mvn
COPY mvnw .
COPY pom.xml .

RUN chmod +x mvnw
RUN ./mvnw dependency:go-offline

COPY src src

# Tests run in CI and locally; skipping here keeps image builds fast and repeatable.
RUN ./mvnw clean package -DskipTests


# Runtime stage uses the smaller JRE because the packaged Spring Boot jar is already compiled.
FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
