# Getting Started: Build & Compilation Guide

This guide describes how to set up your local development environment and compile Harmonia from source code.

---

## 1. Prerequisites `[CONFIGURED]`

To build the entire Harmonia platform, ensure the following software packages are installed:

| Component | Minimum Version | Recommended | Verification Command | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| **Java Development Kit (JDK)** | 21 | Eclipse Temurin 21 (LTS) | `java -version` | Core platform compilation (Java 21 bytecode) |
| **Apache Maven** | 3.9.0 | 3.9.6+ | `mvn -version` | Multi-module reactor build system |
| **Node.js** | v20.12.2 | v20.12.2 (LTS) | `node -v` | Iris Vue 3 web SPAs compilation |
| **npm** | 10.5.0 | 10.5.0+ | `npm -v` | Frontend dependency management |
| **Docker** / Podman | 24.0+ | 26.0+ | `docker --version` | Container packaging and local integration testing |

Ensure `JAVA_HOME` points to your JDK 21 installation:
```bash
export JAVA_HOME=/usr/lib/jvm/temurin-21-jdk-amd64
export PATH=$JAVA_HOME/bin:$PATH
```

---

## 2. Repository Maven Structure `[IMPLEMENTED]`

Harmonia is organized as a multi-module Maven project with 9 core subprojects and 37 leaf modules governed by the root `pom.xml`:

```
harmonia/
├── pom.xml                   # Root parent POM (dependency management & properties)
├── calliope/                 # Canonical schemas & converters (1 leaf module)
├── themis/                   # Security & default-deny authorization (3 leaf modules)
├── hestia/                   # Persistence & caching (5 leaf modules)
├── petasos/                  # Resilient messaging abstraction (4 leaf modules)
├── energeia/                 # Task execution & workflows (4 leaf modules)
├── pylai/                    # Ingress/Egress protocol gateways (5 leaf modules)
├── iris/                     # Presentation tier (4 leaf modules: BEFE + 3 Vue SPAs)
├── agora/                    # Matrix collaboration gateway (4 leaf modules)
└── paradeigma/               # Synthetic clinical simulation (7 leaf modules)
```

---

## 3. Compilation Commands `[IMPLEMENTED]`

### Fast Compilation (Skip Tests)
To verify compilation across all Java modules without executing tests:
```bash
mvn clean test-compile -DskipTests
```

### Full Standard Build
To compile and execute unit tests across the entire repository:
```bash
mvn clean install
```

### Targeted Subsystem Builds
You can compile specific subprojects and automatically resolve required upstream dependencies using `-pl` (project list) and `-am` (also-make):

- **Build Calliope (Canonical Foundation)**:
  ```bash
  mvn clean install -pl calliope
  ```

- **Build Themis (Security Engine)**:
  ```bash
  mvn clean install -pl themis -am
  ```

- **Build Hestia (Persistence & Cache Grid)**:
  ```bash
  mvn clean install -pl hestia -am
  ```

- **Build Petasos (Messaging Backbone)**:
  ```bash
  mvn clean install -pl petasos -am
  ```

- **Build Energeia (Workflow Engine)**:
  ```bash
  mvn clean install -pl energeia -am
  ```

- **Build Pylai (Protocol Gateways)**:
  ```bash
  mvn clean install -pl pylai -am
  ```

- **Build Agora (Matrix Collaboration Gateway)**:
  ```bash
  mvn clean install -pl agora -am
  ```

- **Build Iris (BEFE & Frontend SPAs)**:
  ```bash
  mvn clean install -pl iris -am
  ```

---

## 4. Architecture Verification Tests `[IMPLEMENTED]`

Harmonia uses ArchUnit to enforce architectural invariants across all modules at compile time.

To run the full architecture validation suite:
```bash
mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false
```

Key architectural rules verified include:
1. **`ParadeigmaIsolationArchitectureTest`**: Verifies that production code never imports or depends on Paradeigma simulation packages.
2. **`AgoraIsolationArchitectureTest`**: Verifies that Agora does not depend on Ponos directly and encapsulates all Matrix DTOs.
3. **`PetasosApiIsolationArchitectureTest`**: Verifies that `petasos-api` has strictly zero JMS or ActiveMQ imports.
4. **`IrisDecouplingArchitectureTest`**: Verifies that Iris has zero dependencies on JPA, Hibernate, or PostgreSQL.
5. **`ProviderRegistryArchitectureTest`**: Verifies that `iris-administration` does not implement backend persistence.

---

## 5. Frontend SPA Builds (Iris) `[IMPLEMENTED]`

The `iris` subproject uses `frontend-maven-plugin` to automatically download Node.js and npm during the standard Maven build. You can also build the SPAs manually:

```bash
# Build Iris Clinical SPA
cd iris/iris-clinical
npm install
npm run build

# Build Iris Console SPA
cd ../iris-console
npm install
npm run build

# Build Iris Administration SPA
cd ../iris-administration
npm install
npm run build
```

---

## 6. Common Build Troubleshooting `[EXAMPLE/REFERENCE]`

- **Issue: Out of Memory during Maven compilation**
  - *Fix*: Increase JVM heap memory in `MAVEN_OPTS`:
    ```bash
    export MAVEN_OPTS="-Xmx2048m -XX:MaxMetaspaceSize=512m"
    ```
- **Issue: Node / npm download failure behind corporate proxy**
  - *Fix*: Configure Maven proxy settings in `~/.m2/settings.xml` or pre-install Node.js v20.12.2 locally.
- **Issue: ArchUnit test failure on new dependency**
  - *Fix*: Check the dependency against `AGENTS.md`. Ensure you are not importing higher-layer classes from lower-layer modules.
