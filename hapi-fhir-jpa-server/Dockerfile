FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY target/hapi-fhir-jpa-server-*.jar app.jar
EXPOSE 8080
ENV SPRING_PROFILES_ACTIVE=postgres
ENTRYPOINT ["java", "-jar", "app.jar"]
