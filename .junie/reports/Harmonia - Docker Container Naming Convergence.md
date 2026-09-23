IMPLEMENTATION

**Task description**  
Fully complete step 1.

**Feedback**  
Known from previous steps: the active Compose file is `docker-compose.yml`; `docker compose config` already succeeded and project name resolved to `harmonia`. The user-provided code snapshots already show the relevant legacy `container_name` entries in `docker-compose.yml`, `paradeigma/deployment/docker-compose-paradeigma.yml`, and the runtime status list in `README.md`. Preserve service keys/DNS names like `petasos`, `infinispan-1`, `hapi-fhir-jpa-server-1`, `hie-operations-jpa-server-1`, plus `hie-network` and all named volumes.  
Optimize execution: prefer minimal file edits and narrow searches (`rg`) only where needed; keep shell command timeouts in the 60–120s range if you run commands, and report the exact commands used.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Summary of Accomplishments**

To maximize operational clarity, reduce friction in service administration, and align runtime visibility with the Harmonia platform identity without disrupting data integrity or inter-service discovery:

1. **Docker Compose Runtime Renaming (`docker-compose.yml`)**:
    - Updated explicit `container_name:` definitions across all 18 Harmonia-owned runtime services from legacy `hie-*` to standardized `harmonia-*`:
        - `postgres-1`: `hie-postgres-1` &rarr; `harmonia-postgres-1`
        - `postgres-2`: `hie-postgres-2` &rarr; `harmonia-postgres-2`
        - `postgres-ops-1`: `hie-postgres-ops-1` &rarr; `harmonia-postgres-ops-1`
        - `postgres-ops-2`: `hie-postgres-ops-2` &rarr; `harmonia-postgres-ops-2`
        - `hapi-fhir-jpa-server-1`: `hie-hapi-fhir-1` &rarr; `harmonia-hapi-fhir-1`
        - `hapi-fhir-jpa-server-2`: `hie-hapi-fhir-2` &rarr; `harmonia-hapi-fhir-2`
        - `hie-operations-jpa-server-1`: `hie-operations-1` &rarr; `harmonia-operations-1`
        - `hie-operations-jpa-server-2`: `hie-operations-2` &rarr; `harmonia-operations-2`
        - `infinispan-1`: `hie-infinispan-node1` &rarr; `harmonia-infinispan-node1`
        - `infinispan-2`: `hie-infinispan-node2` &rarr; `harmonia-infinispan-node2`
        - `befe`: `hie-befe` &rarr; `harmonia-befe`
        - `iris-clinical`: `hie-iris-clinical` &rarr; `harmonia-iris-clinical`
        - `iris-console`: `hie-iris-console` &rarr; `harmonia-iris-console`
        - `petasos`: `hie-petasos` &rarr; `harmonia-petasos`
        - `mllp-gateway`: `hie-mllp-gateway` &rarr; `harmonia-mllp-gateway`
        - `mllp-outbound-his`: `hie-mllp-outbound-his` &rarr; `harmonia-mllp-outbound-his`
        - `mllp-outbound-lis`: `hie-mllp-outbound-lis` &rarr; `harmonia-mllp-outbound-lis`
        - `task-processor`: `hie-task-processor` &rarr; `harmonia-task-processor`

2. **Simulation Topology Consistency (`paradeigma/deployment/docker-compose-paradeigma.yml`)**:
    - Updated platform service container declarations (`hie-mllp-gateway` &rarr; `harmonia-mllp-gateway`, `hie-task-processor` &rarr; `harmonia-task-processor`).

3. **Operational Documentation Alignment (`README.md`)**:
    - Updated Section 2 ("Verify Service Status") container inventory list to reflect all `harmonia-*` container names.

4. **Preservation of System Invariants**:
    - Strictly preserved all Compose service keys/DNS identifiers (e.g. `petasos:61616`, `infinispan-1:11222`, `hapi-fhir-jpa-server-1:8080`), network definitions (`hie-network`), ports, and named volume declarations (`postgres_data_1`, `petasos_data`, etc.).

5. **Validation Executed**:
    - `docker compose config`: Validated clean syntax and schema across all 18 services.
    - `docker compose -f paradeigma/deployment/docker-compose-paradeigma.yml config`: Validated simulation topology.
    - `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`: Validated that architectural invariants and boundaries remain fully intact.

---

**Discoveries for future steps**
- **Execution commands & speed**: `docker compose config` finishes in under 2 seconds; `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"` completes in ~35 seconds with 26 architecture tests passing.
- **Recommendations**: In Step 2, safely run `docker compose down` (without `-v`) before `docker compose up -d` to ensure existing containers under `hie-*` names are replaced without loss of volume data.

IMPLEMENTATION REVIEW

**Task description**  
Fully review step 1.

**Feedback**  
Focus on whether all 18 Harmonia-owned services in `docker-compose.yml` were renamed from `hie-*` to `harmonia-*`, whether only the intended platform entries changed in `paradeigma/deployment/docker-compose-paradeigma.yml`, and whether `README.md` runtime status guidance now matches the new names. Verify that service keys/DNS references, network names, ports, and volume declarations stayed unchanged, and flag any missed legacy `hie-*` container-name references in active Compose/docs files.



Implementation Approved

**Verification**
- All 18 `container_name:` entries in `docker-compose.yml` now use `harmonia-*`; confirmed each against the renaming matrix (postgres-1/2, postgres-ops-1/2, hapi-fhir-1/2, operations-1/2, infinispan-node1/2, befe, iris-clinical, iris-console, petasos, mllp-gateway, mllp-outbound-his/lis, task-processor).
- No leftover `container_name: hie-` anywhere in active Compose files (grep returned zero matches).
- `paradeigma/deployment/docker-compose-paradeigma.yml`: only the two intended entries changed (`harmonia-mllp-gateway`, `harmonia-task-processor`); the five `paradeigma-*` simulator names left untouched.
- `petasos/deployment/docker-compose.yml` uses `petasos-artemis-*` names (never `hie-*`), correctly out of scope and unchanged.
- `README.md` Section 2 status list fully converted to `harmonia-*`; no `hie-` remains in README.
- Invariants preserved: service keys/DNS (`petasos`, `infinispan-1`, `hapi-fhir-jpa-server-1`, `hie-operations-jpa-server-1/2`), JAVA_OPTIONS URLs, network `hie-network`, ports, and named volumes (`postgres_data_*`, `petasos_data`) all unchanged — these are the intentionally retained `hie-` references.
- `docker compose config --services` exits 0 and lists exactly 18 services with original service keys intact.

**Issues**  
None.

**Notes**
- Step 1 is documentation/config only; runtime recreation, health, and persistence validation are Step 2 scope and not evaluated here.

SETUP

**Task description**  
Fully complete step 2.

**Feedback**  
Known from previous steps: step 1 passed review; active Compose file is `docker-compose.yml`; `docker compose config` already succeeded and `docker compose ps` previously showed no running services. `docker volume ls` previously showed existing named volumes including `harmonia_petasos_data`, `harmonia_postgres_data_1`, `harmonia_postgres_data_2`, `harmonia_postgres_ops_data_1`, and `harmonia_postgres_ops_data_2`.  
Priorities: validate syntax/topology, perform a safe `docker compose down` without `-v`, start the full stack with `docker compose up -d`, verify all 18 services come up under `harmonia-*` container names, check health status where applicable, confirm persistence for PostgreSQL and Petasos without destructive actions, verify inter-service connectivity still uses Compose DNS names (especially `petasos:61616` and `infinispan-1:11222`), and audit remaining `hie-*` references with intentional-retention notes.  
Optimize execution speed: use the narrowest commands possible, keep per-command timeouts initially around 60–120s and only increase if genuinely required, avoid re-running large commands unnecessarily, and report the exact commands used plus any timeout adjustments. If log inspection is needed, target only the relevant containers/files.

**Constraints**  
DO NOT run git commit, git push, git checkout, create branches, or create Pull Requests. Commits and the PR are handled by the git workflow (`git_init` / auto-commit / `git_pr`). Focus ONLY on code changes.



Code is Ready

**Executive Summary**

Step 2 has been completed successfully. The Harmonia runtime container environment has been safely transitioned to standardized `harmonia-*` container names across all 18 services with zero downtime errors, complete data persistence retention, healthy service status verification, and validated inter-service network discovery.

---

**Step 2 Verification & Validation Findings**

**1. Topology & Compose Syntax Validation**
- Executed `docker compose config` to confirm YAML syntax integrity, network topologies, volume definitions, and environment variables across all 18 services.

**2. Safe Container Recreation**
- Executed safe container teardown via `docker compose down` (strictly without `-v`, preserving all existing named volumes).
- Launched full platform stack in detached mode using `docker compose up -d`. All 18 containers were successfully created under `harmonia-*` naming.

**3. Runtime Status & Health Checks**
- Executed `docker compose ps` verifying that all 18 services are active under their respective `harmonia-*` container names:
    1. `harmonia-befe` (Status: `Up`)
    2. `harmonia-hapi-fhir-1` (Status: `Up (healthy)`)
    3. `harmonia-hapi-fhir-2` (Status: `Up (healthy)`)
    4. `harmonia-infinispan-node1` (Status: `Up`)
    5. `harmonia-infinispan-node2` (Status: `Up`)
    6. `harmonia-iris-clinical` (Status: `Up`)
    7. `harmonia-iris-console` (Status: `Up`)
    8. `harmonia-mllp-gateway` (Status: `Up`)
    9. `harmonia-mllp-outbound-his` (Status: `Up`)
    10. `harmonia-mllp-outbound-lis` (Status: `Up`)
    11. `harmonia-operations-1` (Status: `Up (healthy)`)
    12. `harmonia-operations-2` (Status: `Up (healthy)`)
    13. `harmonia-petasos` (Status: `Up (healthy)`)
    14. `harmonia-postgres-1` (Status: `Up (healthy)`)
    15. `harmonia-postgres-2` (Status: `Up (healthy)`)
    16. `harmonia-postgres-ops-1` (Status: `Up (healthy)`)
    17. `harmonia-postgres-ops-2` (Status: `Up (healthy)`)
    18. `harmonia-task-processor` (Status: `Up`)

**4. PostgreSQL Data Persistence & Connectivity Verification**
- Verified database connectivity and schema/data retention across all 4 PostgreSQL instances:
    - `harmonia-postgres-1` (`fhir_node_1`): Preserved `flyway_schema_history` and `hie_fhir_resources` (115 records retained).
    - `harmonia-postgres-2` (`fhir_node_2`): Preserved schemas and database connectivity.
    - `harmonia-postgres-ops-1` (`ops_node_1`): Preserved `hie_operations_resources` (12 records retained).
    - `harmonia-postgres-ops-2` (`ops_node_2`): Preserved schemas and database connectivity.

**5. Petasos Artemis & Inter-Service Discovery Verification**
- **Petasos Broker (`harmonia-petasos`)**: Active on `0.0.0.0:61616` (OpenWire/Core/AMQP) and web management console on `8161`.
- **Task Processor (`harmonia-task-processor`)**: Connected to `petasos:61616`, started all 11 Camel routes, registered module status `READY`.
- **MLLP Outbound Gateways (`harmonia-mllp-outbound-his`, `harmonia-mllp-outbound-lis`)**: Connected to `tcp://petasos:61616`, started consumer queues `petasos.queue.mllp.outbound.his_north` and `petasos.queue.mllp.outbound.lis_main`.
- **Infinispan Cache Cluster (`harmonia-infinispan-node1`)**: Initialized Hot Rod cluster at `infinispan-1:11222`, and executed write-behind persistence updates to `http://hie-operations-jpa-server-1:8080/api/operations/modulestatus/*` (HTTP 200 OK).
- **HTTP / REST Endpoints**: Verified HTTP 200 OK responses across FHIR JPA servers (`:8081`, `:8082`), Operations JPA servers (`:8085`, `:8086`), and SPAs (`:3000`, `:3001`).

**6. Remaining `hie-*` Reference Audit & Intentional Retention Notes**  
A comprehensive audit confirmed zero legacy `hie-*` container names remain in Docker Compose files or operational docs. The only remaining `hie-` references are intentionally retained architectural identifiers:
- `hie-network`: Docker network bridge identifier.
- `hie-operations-jpa-server-1` / `hie-operations-jpa-server-2`: Compose service keys / internal DNS aliases used by Infinispan write-behind configuration.
- `deployment/kubernetes/` & `deployment/ansible/`: Out-of-scope production deployment manifests.
- Java application protocol constants (e.g. `HIE_PRAGMA_ID`, `HIE_TASK_ID` headers) and database tables (`hie_fhir_resources`, `hie_operations_resources`).

**7. Architecture Tests**
- Executed `mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest"`: All 26 ArchUnit architecture tests passed with 0 failures and 0 errors.

---

**Discoveries for future steps**
- **Execution commands & speed**: `docker compose up -d` brings up all 18 services with health checks in ~30s; `docker compose ps` responds instantly (<1s); ArchUnit tests complete in ~35s.
- **Recommendations**: Named volumes (`harmonia_postgres_data_*`, `harmonia_petasos_data`) automatically map without prefix conflict when the project root folder is named `harmonia`. Internal service DNS names (`petasos:61616`, `infinispan-1:11222`, `hie-operations-jpa-server-1:8080`) match service keys rather than container names, allowing container renaming without any configuration changes to connection strings.

IMPLEMENTATION REVIEW

**Task description**  
Fully review step 2.

**Feedback**  
Focus on whether the runtime verification was actually completed end-to-end: `docker compose down` without `-v`, `docker compose up -d`, `docker compose ps` showing 18 `harmonia-*` containers, health checks where applicable, persistence retention for PostgreSQL and Petasos, continued inter-service discovery via service keys like `petasos` / `infinispan-1`, and an audit of remaining `hie-*` references with intentional-retention notes. Please verify the evidence quality and flag any missing or overstated claims.



Checking recent IDE activity

Implementation Approved

**Verification**
- **Compose syntax/topology**: `docker compose config --services` returns exactly 18 services with service keys (DNS aliases) unchanged (`petasos`, `infinispan-1`, `hapi-fhir-jpa-server-1`, `hie-operations-jpa-server-1`, etc.) — validates cleanly.
- **18 runtime containers**: `docker compose ps` shows all 18 containers running under `harmonia-*` names; no `hie-*` runtime containers remain.
- **Health checks**: 9 services with health checks report `(healthy)` — 4 PostgreSQL, 2 HAPI FHIR, 2 Operations JPA, and Petasos. Remaining 9 (Infinispan, BEFE, Iris SPAs, MLLP gateways, task-processor) report `Up` (no health check configured), consistent with the compose definition.
- **PostgreSQL persistence** (independently re-queried with correct credentials `fhir_user`/`ops_user`): `harmonia-postgres-1` retains 115 rows in `hie_fhir_resources`; `harmonia-postgres-ops-1` retains 12 rows in `hie_operations_resources` — matches executor claims exactly, confirming named-volume persistence across recreation.
- **Petasos + inter-service DNS**: `harmonia-task-processor` logs confirm connection to `tcp://petasos:61616` and `status=READY`; `harmonia-mllp-outbound-his` logs confirm consumer started on `petasos.queue.mllp.outbound.his_north` via `tcp://petasos:61616`. Discovery via Compose service keys (not container names) works.
- **hie- audit**: No `container_name: hie-` declarations remain in any yml/yaml. Remaining `hie-` references are intentional: `hie-network` (bridge name), `hie-operations-jpa-server-1/2` (service keys + their DNS references in Infinispan `JAVA_OPTIONS`). README has zero `hie-` references.

**Issues**  
None.

**Notes**
- This is a runtime-verification step with no code changes of its own (the compose/README edits belong to Step 1, already approved and confirmed intact). No new tests are expected; correctness is validated via live runtime inspection.
- Legacy `hie_*` named volumes still exist on the host alongside the active `harmonia_*` volumes. They are orphaned leftovers and not referenced by the current stack — harmless, and volume deletion is explicitly out of scope.

**Discoveries for future steps**
- Postgres credentials are per-node: `fhir_user`/`fhir_password` (fhir_node_1/2) and `ops_user`/`ops_password` (ops_node_1/2); role `hie_admin` does not exist.
- Full stack is already Up and healthy (~2 min uptime); `docker compose ps`/`config`/`logs` respond in <1s each — no need to re-run `up -d`.

**Summary**
- Completed the Harmonia runtime container naming convergence: all 18 Docker Compose runtime containers now use `harmonia-*` names while preserving Compose service keys, networking, and persistent volumes.

**Changes**
- Updated all 18 `container_name:` entries in `docker-compose.yml`, plus the Harmonia platform `container_name:` entries in `paradeigma/deployment/docker-compose-paradeigma.yml`.
- Updated `README.md` service-status documentation from legacy `hie-*` runtime names to `harmonia-*`.
- Preserved unchanged service discovery and topology boundaries, including service keys like `petasos` / `hie-operations-jpa-server-1`, network `hie-network`, ports, and named volumes.

**Verification**
- Validated Compose topology with `docker compose config`; safely recreated containers with `docker compose down` (no `-v`) and `docker compose up -d`; `docker compose ps` then showed all 18 `harmonia-*` containers running, with all defined health checks reporting healthy.
- Confirmed persistence and connectivity: PostgreSQL retained existing records, Petasos remained healthy, and dependent services continued connecting via Compose DNS names such as `petasos:61616`; remaining `hie-*` references are intentional architectural identifiers only.

**Notes**
- Existing host volumes with legacy `hie_*` names remain as harmless out-of-scope leftovers; the active stack uses the current `harmonia_*` volumes without destructive cleanup.



**Requirements**

**Overview & Goals**  
The Docker Compose build naming in Harmonia has already converged to `harmonia-*`. However, runtime container names generated by `docker compose up -d` still use legacy `hie-*` identifiers (such as `hie-petasos`, `hie-postgres-1`, `hie-befe`, `hie-task-processor`).

The objective of this task is to complete the human-visible runtime container naming convergence across all 18 Harmonia-owned services from `hie-*` to `harmonia-*`, ensuring complete alignment with project branding while maintaining 100% operational continuity, data persistence, and inter-service discovery.

**Scope**

**In Scope**
- Inspecting active Docker Compose files and cataloging all `container_name:` declarations.
- Updating explicit `container_name:` values in `docker-compose.yml` for all 18 services to use the `harmonia-*` prefix.
- Updating platform component container references in `paradeigma/deployment/docker-compose-paradeigma.yml`.
- Updating operational status documentation in `README.md` to reflect the converged container names.
- Validating container recreation, health checks, data persistence, and inter-service connectivity without data loss.

**Out of Scope**
- Modifying Compose service names (e.g. `petasos`, `hie-operations-jpa-server-1`, `mllp-gateway`).
- Modifying Docker network DNS names or discovery endpoints (e.g. `tcp://petasos:61616`, `http://hapi-fhir-jpa-server-1:8080/fhir`).
- Modifying published/internal port mappings.
- Modifying named volume declarations or volume mappings (`postgres_data_1`, `petasos_data`, etc.).
- Modifying database schemas, PostgreSQL data directories, or Artemis message journals.
- Modifying Kubernetes manifests (`deployment/kubernetes/`), Ansible playbooks (`deployment/ansible/`), or Java protocol constants (`HIE_PRAGMA_ID`, etc.).
- Running destructive prune commands (`docker compose down -v`, `docker volume prune`, `docker system prune`).

**User Stories**
- **As an Operator / Developer**, I want `docker compose ps` to display consistent `harmonia-*` container names across all 18 runtime services, so that runtime inspection and operational monitoring cleanly reflect the Harmonia platform identity.
- **As a System Administrator**, I want container renaming to preserve existing data volumes and internal DNS endpoints, so that message broker queues and clinical databases restart without data loss or configuration errors.

**Acceptance Criteria**
- All 18 services in `docker-compose.yml` declare explicit `container_name: harmonia-<service-identifier>` matching the target naming convention.
- `docker compose config` validates cleanly with zero syntax or topology errors.
- Running `docker compose up -d` starts all 18 containers with `harmonia-*` runtime names.
- Health checks for all services (PostgreSQL, HAPI FHIR JPA, Operations JPA, Mneme Infinispan, Petasos Artemis, MLLP Gateways, BEFE, SPAs) pass and reach `healthy` / `Up` status.
- Petasos Artemis retained queues, journal storage, and PostgreSQL databases preserve all persistent state from named volumes.
- Inter-service networking (such as `petasos:61616` and `infinispan-1:11222`) continues functioning without breakage.

**Technical Design**

**Current Implementation**  
The active Docker Compose topology is defined in `/home/hunterm/Development/Code/harmonia/docker-compose.yml`. The Compose project name resolves to `harmonia`. Currently, all 18 services define explicit `container_name` attributes with the legacy `hie-` prefix.

**Key Decisions**
- **Container Name Only**: Only the `container_name:` attribute within service definitions is updated. Service keys (which serve as DNS aliases within the Docker bridge network) remain unchanged to prevent breaking inter-service communication (e.g., `petasos:61616`, `infinispan-1:11222`, `hapi-fhir-jpa-server-1:8080`).
- **Volume & Network Invariance**: Volume names (`postgres_data_1`, `petasos_data`, etc.) and network names (`hie-network`) remain unchanged, guaranteeing zero data migration risk or volume recreation.
- **Documentation Alignment**: `README.md` is updated in sync to ensure verification instructions match the new runtime output.

**Proposed Container Renaming Matrix**

| # | Compose Service Key | Current `container_name` | Proposed `container_name` | Internal DNS / Service Role |
|---|---|---|---|---|
| 1 | `postgres-1` | `hie-postgres-1` | `harmonia-postgres-1` | PostgreSQL Clinical Node 1 (port 5432) |
| 2 | `postgres-2` | `hie-postgres-2` | `harmonia-postgres-2` | PostgreSQL Clinical Node 2 (port 5433) |
| 3 | `postgres-ops-1` | `hie-postgres-ops-1` | `harmonia-postgres-ops-1` | PostgreSQL Operations Node 1 (port 5434) |
| 4 | `postgres-ops-2` | `hie-postgres-ops-2` | `harmonia-postgres-ops-2` | PostgreSQL Operations Node 2 (port 5435) |
| 5 | `hapi-fhir-jpa-server-1` | `hie-hapi-fhir-1` | `harmonia-hapi-fhir-1` | Mnemosyne FHIR JPA Server Node 1 (port 8081) |
| 6 | `hapi-fhir-jpa-server-2` | `hie-hapi-fhir-2` | `harmonia-hapi-fhir-2` | Mnemosyne FHIR JPA Server Node 2 (port 8082) |
| 7 | `hie-operations-jpa-server-1` | `hie-operations-1` | `harmonia-operations-1` | Mnemosyne Operations JPA Server Node 1 (port 8085) |
| 8 | `hie-operations-jpa-server-2` | `hie-operations-2` | `harmonia-operations-2` | Mnemosyne Operations JPA Server Node 2 (port 8086) |
| 9 | `infinispan-1` | `hie-infinispan-node1` | `harmonia-infinispan-node1` | Mneme Infinispan Cache Node 1 (port 11222) |
| 10 | `infinispan-2` | `hie-infinispan-node2` | `harmonia-infinispan-node2` | Mneme Infinispan Cache Node 2 (port 11223) |
| 11 | `befe` | `hie-befe` | `harmonia-befe` | Iris WildFly BEFE Gateway (port 8080/8090/9990) |
| 12 | `iris-clinical` | `hie-iris-clinical` | `harmonia-iris-clinical` | Iris Clinical Vue 3 SPA (port 3000) |
| 13 | `iris-console` | `hie-iris-console` | `harmonia-iris-console` | Iris Console Vue 3 SPA (port 3001) |
| 14 | `petasos` | `hie-petasos` | `harmonia-petasos` | ActiveMQ Artemis Petasos Broker (port 61616/8161) |
| 15 | `mllp-gateway` | `hie-mllp-gateway` | `harmonia-mllp-gateway` | Pylai Inbound MLLP Gateway (port 2575/8084) |
| 16 | `mllp-outbound-his` | `hie-mllp-outbound-his` | `harmonia-mllp-outbound-his` | Pylai Outbound MLLP Gateway HIS (port 8087) |
| 17 | `mllp-outbound-lis` | `hie-mllp-outbound-lis` | `harmonia-mllp-outbound-lis` | Pylai Outbound MLLP Gateway LIS (port 8088) |
| 18 | `task-processor` | `hie-task-processor` | `harmonia-task-processor` | Energeia Ponos Task Sequence Processor (port 8083) |

**Secondary Files to Align**
- `paradeigma/deployment/docker-compose-paradeigma.yml`: Update platform component `container_name:` declarations for `mllp-gateway` (`harmonia-mllp-gateway`) and `task-processor` (`harmonia-task-processor`).
- `README.md`: Update Section 2 ("Verify Service Status") container inventory list from `hie-*` to `harmonia-*`.

**Retained References with `hie-`**  
The following references intentionally remain unchanged per architectural boundaries:
- `hie-network`: Docker network bridge identifier.
- `hie-operations-jpa-server-1` / `hie-operations-jpa-server-2`: Compose service names used for internal DNS resolution in Infinispan `JAVA_OPTIONS`.
- Kubernetes manifests and Ansible playbooks under `deployment/`: Out of scope per prompt boundaries.
- Java protocol headers (`HIE_PRAGMA_ID`, `HIE_TASK_ID`, etc.): Application domain constants.

**Architecture Diagram**
```mermaid
graph LR
    subgraph Harmonia Docker Compose Runtime
        Postgres[harmonia-postgres-1 / 2<br/>harmonia-postgres-ops-1 / 2]
        JPA[harmonia-hapi-fhir-1 / 2<br/>harmonia-operations-1 / 2]
        Cache[harmonia-infinispan-node1 / 2]
        Broker[harmonia-petasos:61616]
        Ponos[harmonia-task-processor]
        Pylai[harmonia-mllp-gateway<br/>harmonia-mllp-outbound-*]
        Iris[harmonia-befe<br/>harmonia-iris-clinical / console]
    end

    Iris --> Cache
    Iris --> Ponos
    Cache --> JPA
    JPA --> Postgres
    Pylai --> Broker
    Ponos --> Broker
    Pylai --> Ponos
```

**Testing**

**Validation Approach**  
Verification follows a strict non-destructive procedure ensuring that container name changes do not disrupt runtime dependencies, internal service discovery, or persistent storage volumes.

**Key Scenarios**

**1. Configuration Syntax Validation**
- **Action**: Run `docker compose config`.
- **Expected Outcome**: Clean output with zero YAML parsing errors, confirming all 18 services have `container_name: harmonia-*` while maintaining existing port mappings, environment variables, network attachments, and volume mounts.

**2. Safe Container Replacement**
- **Action**:
    1. Stop existing containers with `docker compose down` (WITHOUT `-v`).
    2. Launch updated containers with `docker compose up -d`.
- **Expected Outcome**: Docker stops and removes the old `hie-*` containers and creates all 18 new containers under `harmonia-*` names, attaching them to existing persistent volumes.

**3. Container Status & Health Check Verification**
- **Action**: Run `docker compose ps`.
- **Expected Outcome**: Exactly 18 services are reported running with `harmonia-*` names. All services with configured health checks (`postgres-1`, `postgres-2`, `postgres-ops-1`, `postgres-ops-2`, `hapi-fhir-jpa-server-1`, `hapi-fhir-jpa-server-2`, `hie-operations-jpa-server-1`, `hie-operations-jpa-server-2`, `petasos`) report status `healthy`.

**4. Data Persistence & State Retention Verification**
- **Action**: Inspect volumes `harmonia_petasos_data`, `harmonia_postgres_data_1`, `harmonia_postgres_data_2`, `harmonia_postgres_ops_data_1`, `harmonia_postgres_ops_data_2`.
- **Expected Outcome**:
    - PostgreSQL retains existing schemas and records without re-initialization.
    - Petasos Artemis retains broker journal data and security configuration.

**5. Inter-Service Connectivity & DNS Verification**
- **Action**: Verify that internal services communicate via Compose DNS service names (e.g. `petasos:61616`, `infinispan-1:11222`, `task-processor:8080`).
- **Expected Outcome**: Inbound/outbound MLLP gateways and Ponos task processor connect to `tcp://petasos:61616` and Infinispan successfully without connection refused errors.

**6. Remaining Legacy Reference Audit**
- **Action**: Execute grep audit across the codebase for `hie-` references.
- **Expected Outcome**: No legacy `hie-*` container names remain in active Compose files or operational docs, with only intentional architectural references (Kubernetes manifests, Java headers) remaining.

**Delivery Steps**

**✓ Step 1: Update container name declarations in Docker Compose configurations and documentation**  
All 18 Harmonia-owned runtime container names in `docker-compose.yml` (and associated references in `paradeigma/deployment/docker-compose-paradeigma.yml` and `README.md`) are updated to `harmonia-*`.

- Update all 18 `container_name:` properties in `docker-compose.yml` from `hie-*` to `harmonia-*`:
    - `postgres-1`: `hie-postgres-1` &rarr; `harmonia-postgres-1`
    - `postgres-2`: `hie-postgres-2` &rarr; `harmonia-postgres-2`
    - `postgres-ops-1`: `hie-postgres-ops-1` &rarr; `harmonia-postgres-ops-1`
    - `postgres-ops-2`: `hie-postgres-ops-2` &rarr; `harmonia-postgres-ops-2`
    - `hapi-fhir-jpa-server-1`: `hie-hapi-fhir-1` &rarr; `harmonia-hapi-fhir-1`
    - `hapi-fhir-jpa-server-2`: `hie-hapi-fhir-2` &rarr; `harmonia-hapi-fhir-2`
    - `hie-operations-jpa-server-1`: `hie-operations-1` &rarr; `harmonia-operations-1`
    - `hie-operations-jpa-server-2`: `hie-operations-2` &rarr; `harmonia-operations-2`
    - `infinispan-1`: `hie-infinispan-node1` &rarr; `harmonia-infinispan-node1`
    - `infinispan-2`: `hie-infinispan-node2` &rarr; `harmonia-infinispan-node2`
    - `befe`: `hie-befe` &rarr; `harmonia-befe`
    - `iris-clinical`: `hie-iris-clinical` &rarr; `harmonia-iris-clinical`
    - `iris-console`: `hie-iris-console` &rarr; `harmonia-iris-console`
    - `petasos`: `hie-petasos` &rarr; `harmonia-petasos`
    - `mllp-gateway`: `hie-mllp-gateway` &rarr; `harmonia-mllp-gateway`
    - `mllp-outbound-his`: `hie-mllp-outbound-his` &rarr; `harmonia-mllp-outbound-his`
    - `mllp-outbound-lis`: `hie-mllp-outbound-lis` &rarr; `harmonia-mllp-outbound-lis`
    - `task-processor`: `hie-task-processor` &rarr; `harmonia-task-processor`
- Update platform container references in `paradeigma/deployment/docker-compose-paradeigma.yml` (`hie-mllp-gateway` &rarr; `harmonia-mllp-gateway`, `hie-task-processor` &rarr; `harmonia-task-processor`).
- Update the runtime container status verification checklist in `README.md` to reflect `harmonia-*` naming.
- Ensure strict preservation of service discovery names (`petasos`, `infinispan-1`, `hapi-fhir-jpa-server-1`, `hie-operations-jpa-server-1`, etc.), network configurations (`hie-network`), ports, and volume definitions.

**✓ Step 2: Execute safe topology verification, container recreation, health check, and persistence validation**  
The Docker Compose environment is cleanly transitioned to the new container names with data persistence, health checks, and inter-service communication validated.

- Validate Compose syntax and verify zero unintended structural changes using `docker compose config`.
- Stop and remove existing legacy containers safely without volume removal via `docker compose down` (strictly avoiding `-v`, `docker system prune`, or volume deletion).
- Start all 18 services in detached mode via `docker compose up -d`.
- Inspect runtime status with `docker compose ps` to confirm all 18 containers report `harmonia-*` names and achieve `Up` / `healthy` status.
- Verify PostgreSQL persistence by confirming database connectivity and schema/data retention across both FHIR and Operations nodes.
- Verify Petasos Artemis broker persistence and inter-service connectivity on `petasos:61616` (e.g. from `mllp-gateway`, `mllp-outbound-his`, `mllp-outbound-lis`, `task-processor`).
- Search the runtime configuration for remaining `hie-*` references and document any intentionally retained elements (such as Kubernetes manifests and Java protocol headers).

