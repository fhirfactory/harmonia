# Harmonia Docker Compose Step 2 & 3 — Image Packaging & Clean Build Verification Report

**Report date:** 2026-09-29  
**Scope:** Step 2 & 3 — Establish active context hygiene, resolve Docker packaging discrepancies, perform clean-room `--no-cache` verification across all 13 built container services in `docker-compose.yml`, and verify architectural non-regression.  
**Environment:** `/srv/harmonia`  
**Compose topology:** 13 built service images across 9 active build contexts (excluding 5 upstream official images and non-Compose modules like `iris-administration`).

---

## 1. Full Stack Image Build Verification Results (`--no-cache`)

All 13 container images defined across the 9 active build contexts build cleanly, deterministically, and reproducibly in an end-to-end `docker compose build --no-cache` execution:

| Service Name | Docker Build Context | Base Image / Runtime | Target Artifact / Packaging Destination | Build Status |
|---|---|---|---|---|
| `hapi-fhir-jpa-server-1` | `./hestia/mnemosyne-clinical` | `eclipse-temurin:21-jre-jammy` | `target/mnemosyne-clinical-*-exec.jar` -> `/app/app.jar` | **SUCCESS** |
| `hapi-fhir-jpa-server-2` | `./hestia/mnemosyne-clinical` | `eclipse-temurin:21-jre-jammy` | `target/mnemosyne-clinical-*-exec.jar` -> `/app/app.jar` | **SUCCESS** |
| `operations-1` | `./hestia/mnemosyne-operations` | `eclipse-temurin:21-jre-jammy` | `target/mnemosyne-operations-*-exec.jar` -> `/app/app.jar` | **SUCCESS** |
| `operations-2` | `./hestia/mnemosyne-operations` | `eclipse-temurin:21-jre-jammy` | `target/mnemosyne-operations-*-exec.jar` -> `/app/app.jar` | **SUCCESS** |
| `infinispan-1` | `./hestia/mneme-cluster` | `quay.io/infinispan/server:15.0.3.Final` | `target/lib/*.jar` + `src/main/resources/{infinispan.xml,users.properties,groups.properties}` -> `/opt/infinispan/server/` | **SUCCESS** |
| `infinispan-2` | `./hestia/mneme-cluster` | `quay.io/infinispan/server:15.0.3.Final` | `target/lib/*.jar` + `src/main/resources/{infinispan.xml,users.properties,groups.properties}` -> `/opt/infinispan/server/` | **SUCCESS** |
| `befe` | `./iris/iris-befe` | `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` | `target/iris-befe.war` -> `/opt/jboss/wildfly/standalone/deployments/ROOT.war` | **SUCCESS** |
| `iris-clinical` | `./iris/iris-clinical` | Multi-stage: `node:20-alpine` -> `nginx:alpine` | Vite / `vue-tsc` build -> `/app/dist` -> `/usr/share/nginx/html` | **SUCCESS** |
| `iris-console` | `./iris` | Multi-stage: `node:20-alpine` -> `nginx:alpine` | `@harmonia/iris-befe-frontend` + Vite build -> `/app/iris-console/dist` -> `/usr/share/nginx/html` | **SUCCESS** |
| `mllp-gateway` | `./pylai/pylai-mllp-in` | `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` | `target/mllp-gateway.war` -> `/opt/jboss/wildfly/standalone/deployments/ROOT.war` | **SUCCESS** |
| `mllp-outbound-his` | `./pylai/pylai-mllp-out` | `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` | `target/mllp-gateway-out.war` -> `/opt/jboss/wildfly/standalone/deployments/ROOT.war` | **SUCCESS** |
| `mllp-outbound-lis` | `./pylai/pylai-mllp-out` | `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` | `target/mllp-gateway-out.war` -> `/opt/jboss/wildfly/standalone/deployments/ROOT.war` | **SUCCESS** |
| `task-processor` | `./energeia/ponos` | `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` | `target/task-sequence-processor.war` -> `/opt/jboss/wildfly/standalone/deployments/ROOT.war` | **SUCCESS** |

---

## 2. Packaging Discrepancies, Root Causes, and Applied Fixes

### 1. Missing Base Image Tag on Quay.io (`iris-befe`)
- **Problem / Root Cause:** `iris/iris-befe/Dockerfile` referenced `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`. Remote image resolution returned `404 Not Found` because tag `31.0.1.Final-jdk21` does not exist in the Quay.io registry.
- **File Modified:** `iris/iris-befe/Dockerfile`
- **Affected Image:** `harmonia-befe:latest`
- **Resolution:** Updated base image to `quay.io/wildfly/wildfly:32.0.1.Final-jdk21`, which exists in Quay.io, supports Java 21 / Jakarta EE 10, and resolves cleanly without cache.

### 2. Mutable Floating Base Image Tags (`latest-jdk21`) in WildFly Services
- **Problem / Root Cause:** `energeia/ponos/Dockerfile`, `pylai/pylai-mllp-in/Dockerfile`, and `pylai/pylai-mllp-out/Dockerfile` used the unpinned floating tag `quay.io/wildfly/wildfly:latest-jdk21`, which risks non-deterministic builds across environments.
- **Files Modified:**
  - `energeia/ponos/Dockerfile`
  - `pylai/pylai-mllp-in/Dockerfile`
  - `pylai/pylai-mllp-out/Dockerfile`
- **Affected Images:** `harmonia-task-processor`, `harmonia-mllp-gateway`, `harmonia-mllp-outbound-his`, `harmonia-mllp-outbound-lis`
- **Resolution:** Pinned immutable release tag `quay.io/wildfly/wildfly:32.0.1.Final-jdk21` across all three Dockerfiles.

### 3. Build Context Hygiene & Isolation
- **Problem / Root Cause:** Docker build contexts could copy host `node_modules`, `dist`, `.git`, test source trees, and temporary caches into Docker daemon contexts, degrading build performance and risking non-reproducible artifacts.
- **Files Established / Refined:**
  - `iris/.dockerignore`
  - `iris/iris-clinical/.dockerignore`
  - `iris/iris-befe/.dockerignore`
  - `energeia/ponos/.dockerignore`
  - `hestia/mneme-cluster/.dockerignore`
  - `hestia/mnemosyne-clinical/.dockerignore`
  - `hestia/mnemosyne-operations/.dockerignore`
  - `pylai/pylai-mllp-in/.dockerignore`
  - `pylai/pylai-mllp-out/.dockerignore`
- **Affected Images:** All 13 built images
- **Resolution:** Established and refined minimal `.dockerignore` files strictly at active build context roots. Excluded `.git`, test classes, node/npm artifacts, and non-target binaries without creating an unnecessary repository-root `.dockerignore`.

### 4. Scope Discipline & Source Reverts
- **Investigation / Action:** All out-of-scope Java source modifications in `pylai-mllp-base` (`FhirConfig.java`, `DefaultCommunicationService.java`, `DefaultProvenanceService.java`, `DefaultTaskService.java`) were reverted. Zero Java source files, Maven POMs, or application runtime behaviors are modified in Step 2/3. Prebuilt Step 1 Maven artifacts are preserved and packaged directly.

---

## 3. Execution Log & Verification Commands

1. **Compose Topology Sanity Check:**
   - Command: `docker compose config --quiet`
   - Result: Exit code 0 (valid Compose structure).
2. **End-to-End Clean Room Build:**
   - Command: `docker compose build --no-cache`
   - Result: Exit code 0, 13/13 images built and tagged successfully in Docker daemon.
3. **Architectural Guardrail Verification:**
   - Command: `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false`
   - Result: 84/84 ArchUnit tests passed with 0 failures, 0 errors, 0 skipped.

---

## 4. Architectural Non-Regression Summary

- **Invariant 1 (Paradeigma Production Isolation):** Confirmed no production Dockerfile or module imports or depends on Paradeigma simulation modules.
- **Invariant 2 (Petasos API Abstraction):** Confirmed `petasos-api` remains purely abstract with zero JMS/ActiveMQ leakage.
- **Invariant 3 (Iris Presentation Decoupling):** Confirmed Iris frontend and BEFE containers remain decoupled from internal databases/JPA.
- **Invariants 4-10 (Security, Ingress, Audit, and State Boundaries):** Confirmed zero changes to application logic, data contracts, or security policies. All 84 ArchUnit architecture tests continue to pass with 0 failures.
