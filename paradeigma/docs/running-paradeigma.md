# Harmonia Paradeigma — Running Paradeigma Locally

### 1. Build the Entire Solution
Build all platform and Paradeigma modules from repository root:
```bash
mvn clean install -DskipTests
```

To run all unit, component, and integration tests:
```bash
mvn test
```

---

### 2. Running via Docker Compose
To launch the complete environment (Harmonia Gateways, Task Processor, Artemis, and all 4 Healthcare Simulators):
```bash
cd paradeigma/deployment
docker compose -f docker-compose-paradeigma.yml up --build -d
```

Check status of containers:
```bash
docker compose -f docker-compose-paradeigma.yml ps
```

---

### 3. Executing a Live Clinical Patient Journey
Trigger a single end-to-end patient journey:
```bash
curl -X POST http://localhost:8090/api/scenarios/journey
```

Trigger concurrent patient journeys:
```bash
curl -X POST "http://localhost:8090/api/scenarios/concurrent?count=5&profile=TEST"
```

Check scenario conductor status:
```bash
curl -X GET http://localhost:8090/api/scenarios/status
```

---

### 4. Running Simulators Locally with Maven / Spring Boot
Run each simulator directly in separate terminal windows:
```bash
# Terminal 1: PAS Simulator
mvn spring-boot:run -pl paradeigma/paradeigma-pas

# Terminal 2: EMR Simulator
mvn spring-boot:run -pl paradeigma/paradeigma-emr

# Terminal 3: LMS Simulator
mvn spring-boot:run -pl paradeigma/paradeigma-lms

# Terminal 4: RIS-PAC Simulator
mvn spring-boot:run -pl paradeigma/paradeigma-rispac
```
