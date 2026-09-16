# Multi-stage Dockerfile for Paradeigma RIS-PAC Simulator
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl paradeigma/paradeigma-rispac -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/paradeigma/paradeigma-rispac/target/paradeigma-rispac-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8094 2203 2205
ENTRYPOINT ["java", "-jar", "app.jar"]
