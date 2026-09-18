# Multi-stage Dockerfile for Paradeigma LMS Simulator
FROM maven:3.9.6-eclipse-temurin-21 AS build
WORKDIR /app
COPY . .
RUN mvn clean package -pl paradeigma/paradeigma-lms -am -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /app/paradeigma/paradeigma-lms/target/paradeigma-lms-1.0.0-SNAPSHOT.jar app.jar
EXPOSE 8093 2202 2204
ENTRYPOINT ["java", "-jar", "app.jar"]
