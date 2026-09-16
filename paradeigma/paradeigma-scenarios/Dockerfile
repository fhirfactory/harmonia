# Multi-stage Dockerfile for Paradeigma Scenario Engine
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl paradeigma/paradeigma-scenarios -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/paradeigma/paradeigma-scenarios/target/paradeigma-scenarios-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8090
ENTRYPOINT ["java", "-jar", "app.jar"]
