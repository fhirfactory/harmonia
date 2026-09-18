# ==============================================================================
# Harmonia :: Agora - Matrix/Synapse Collaboration Gateway Microservice
# Multi-stage production container build
# ==============================================================================

# --- Stage 1: Build & Layer Extraction ---
FROM eclipse-temurin:21-jdk-jammy AS builder
WORKDIR /workspace
ARG JAR_FILE=target/agora-service-*-exec.jar
COPY ${JAR_FILE} app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# --- Stage 2: Hardened Runtime Container ---
FROM eclipse-temurin:21-jre-jammy AS runtime
WORKDIR /app

# Non-root user and group for platform security
RUN groupadd -r harmonia && useradd -r -g harmonia -s /bin/false harmonia

# Copy extracted Spring Boot layers
COPY --from=builder /workspace/dependencies/ ./
COPY --from=builder /workspace/spring-boot-loader/ ./
COPY --from=builder /workspace/snapshot-dependencies/ ./
COPY --from=builder /workspace/application/ ./

USER harmonia:harmonia

# Expose Matrix Application Service (8092) and Spring Boot Actuator/Management (9992)
EXPOSE 8092 9992

ENV SPRING_PROFILES_ACTIVE=postgres
ENV SERVER_PORT=8092
ENV MANAGEMENT_SERVER_PORT=9992

ENTRYPOINT ["java", "org.springframework.boot.loader.launch.JarLauncher"]
