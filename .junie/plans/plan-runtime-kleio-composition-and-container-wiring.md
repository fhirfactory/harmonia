---
sessionId: session-260924-145006-5h8z
---

# Requirements

### Overview & Goals
The goal of Step 04.2 is to compose and wire the existing durable Kleio audit persistence capability into the Iris BEFE (Backend-For-Frontend) runtime as a container-managed CDI service. 

At the completion of this step, the Iris BEFE runtime container will be able to resolve:
```
AuditService
    -> DurableAuditService
        -> AppendOnlyAuditEventRepository
            -> JdbcAppendOnlyAuditEventRepository
                -> @KleioAudit DataSource
                    -> PostgreSQL audit persistence target (hie_fhir_resources)
```
This is achieved strictly through container CDI wiring, explicit WildFly DataSource configuration (`java:jboss/datasources/KleioAuditDS`), and deployable DataSource binding without introducing a new persistence architecture, altering legacy AuditEvent REST endpoints, or violating any architectural guardrails.

### Scope

#### In Scope
- **Iris BEFE Maven Configuration**: Add minimum required Maven dependency (`kleio-persistence`) to `iris/iris-befe/pom.xml` and include the `datasources` layer in `wildfly-jar-maven-plugin`.
- **Runtime DataSource Configuration**: Declare `@DataSourceDefinition` for `java:jboss/datasources/KleioAuditDS` in Iris BEFE with externalized environment variable configuration targeting `jdbc:postgresql://postgres-1:5432/fhir_node_1`.
- **Composition Root Producer**: Implement `AuditDataSourceProducer` in `net.fhirfactory.harmonia.befe.config` exposing `@Produces @KleioAudit @ApplicationScoped DataSource` bound to the container-managed DataSource via `@Resource(lookup = "java:jboss/datasources/KleioAuditDS")`.
- **CDI Discovery & Resolution**: Verify unique, unambiguous CDI resolution of `AuditService`, `DurableAuditService`, `AppendOnlyAuditEventRepository`, `JdbcAppendOnlyAuditEventRepository`, `HarmoniaAuditEventMapper`, and `FhirContext`.
- **Composition Verification**: Implement a focused CDI graph verification test `KleioRuntimeCompositionTest` in `iris-befe` verifying CDI bean resolution, scoping, qualifier satisfaction, and append/read execution against a test DataSource.
- **Architecture Guardrail Verification**: Confirm zero violations of `IrisDecouplingArchitectureTest`, `PackageLayeringArchitectureTest`, and `SecurityEnforcementArchitectureTest`.

#### Out of Scope (Deferred to Steps 04.3+)
- **AuditEvent REST Endpoints**: No modifications to `AuditEventResource` (POST/PUT/DELETE remain untouched until Step 04.4).
- **Themis Authorization & Security Domains**: No changes to `ThemisClinicalAuthorizationFilter`, `AuditReadPolicy`, or `ClinicalAuthorizationPolicy` (Step 04.3).
- **HAPI FHIR Storage & Cache Mutation**: No changes to `AuditEventResourceProvider`, `FhirStorageService`, `FhirCacheService`, or Infinispan `auditevent-cache` (Step 04.5).
- **Iris UI**: No changes to `AuditEventView.vue` or `securityStore.ts` (Step 04.6).
- **Synthetic Callers**: No synthetic production callers in Themis, Pylai, or Ponos merely to generate events (composition test is the proof).
- **Physical Relocation (AUDIT-BL-02/03)**: No table migrations, no DDL modifications, no WORM storage implementation.

### User Stories
- **As an Iris BEFE Developer/Service**, I want `AuditService` available as an injectable `@Inject` CDI service backed by `DurableAuditService` and PostgreSQL persistence, so that future BEFE endpoints and filters can record immutable append-only audit evidence directly into the authoritative store.
- **As a Security & Compliance Architect**, I want the presentation tier decoupled from concrete JDBC/JPA implementations and database drivers, preserving the architectural invariant that Iris communicates persistence needs only via qualified abstractions (`@KleioAudit DataSource` producer).

### Functional Requirements
- **FR-01**: Iris BEFE CDI context must discover and register `DurableAuditService` as the implementation for `AuditService`.
- **FR-02**: `JdbcAppendOnlyAuditEventRepository` must receive the container-managed DataSource via `@KleioAudit` qualifier.
- **FR-03**: `FhirContextProducer` in `kleio-persistence` must satisfy dependencies for `FhirContext` (R5) and `HarmoniaAuditEventMapper`.
- **FR-04**: `InMemoryAuditService` must NOT be registered as a CDI production bean, preventing any ambiguous dependency resolution.
- **FR-05**: Iris BEFE source code must NOT import `org.postgresql.*`, `jakarta.persistence.*`, `org.hibernate.*`, or `ca.uhn.fhir.jpa.*`.
- **FR-06**: `AuditDataSourceProducer` must inject the container-managed DataSource using the explicit JNDI resource `java:jboss/datasources/KleioAuditDS`.

### Non-Functional Requirements
- **NFR-01 (Durability & Transactionality)**: Append operations on `DurableAuditService` execute with `@Transactional(TxType.REQUIRED)` under WildFly JTA, ensuring evidence is committed to the database before returning.
- **NFR-02 (Zero Direct DB Driver Leakage)**: Iris BEFE source code interacts with `javax.sql.DataSource` and `AuditService` interfaces only; database drivers remain runtime-scoped.
- **NFR-03 (Immutability & Idempotence)**: Preserves existing Kleio append-only semantics (`insertIfAbsent`, duplicate replay idempotency, and integrity check on divergent event payloads).
- **NFR-04 (Relocatable Architecture)**: Decouples the `@KleioAudit` semantic qualifier from physical database JNDI configuration, enabling AUDIT-BL-02 to relocate the physical database solely through composition/runtime configuration without modifying `kleio-persistence`.

# Technical Design

### Current Implementation & Investigation Findings

#### 1. WildFly DataSource Binding Investigation
- **A. Is `java:jboss/datasources/KleioAuditDS` already configured to target `jdbc:postgresql://postgres-1:5432/fhir_node_1`?**
  - **No.** Iris BEFE currently has no pre-existing PostgreSQL datasource configured for the FHIR database.
- **B. Repository Configuration Analysis**:
  - `iris/iris-befe/pom.xml` configures `wildfly-jar-maven-plugin:11.0.1.Final` with provisioning layers `jaxrs`, `cdi`, `jsonb`, `transactions`, `management`.
  - `iris/iris-befe/Dockerfile` builds from `quay.io/wildfly/wildfly:31.0.1.Final-jdk21`.
  - `deployment/kubernetes/base/iris/iris-befe.yaml` and `docker-compose.yml` provide environment variables for Infinispan and OIDC.
  - `docs/middleware/wildfly.md` (Section 3) documents Iris Presentation Decoupling: zero direct JPA/Hibernate, zero direct database access, relying on Hot Rod for caching.
- **C. Composition-Root / Runtime Configuration**:
  - In `iris/iris-befe/pom.xml`, add `<layer>datasources</layer>` to `wildfly-jar-maven-plugin` layers.
  - In Iris BEFE composition root, declare standard Jakarta EE `@DataSourceDefinition` on `AuditDataSourceProducer` binding `java:jboss/datasources/KleioAuditDS` to `org.postgresql.ds.PGSimpleDataSource` with externalized environment variables (`FHIR_DB_URL`, `FHIR_DB_USER`, `FHIR_DB_PASSWORD`).
  - In `deployment/kubernetes/base/iris/iris-befe.yaml` and `docker-compose.yml`, supply `FHIR_DB_URL`, `FHIR_DB_USER`, `FHIR_DB_PASSWORD` pointing to `postgres-1:5432/fhir_node_1`.
- **D. Exact JNDI Name for `AuditDataSourceProducer`**:
  - `java:jboss/datasources/KleioAuditDS` (explicit composition-root JNDI identity).

#### 2. PostgreSQL Driver Ownership & Packaging
- `kleio-persistence/pom.xml` declares `org.postgresql:postgresql` with scope `runtime`.
- In `iris-befe/pom.xml`, adding `kleio-persistence` (scope `compile`) transitively includes the PostgreSQL JDBC driver as a `runtime` dependency, packaging `postgresql-42.7.2.jar` in `iris-befe.war!/WEB-INF/lib/`.
- `IrisDecouplingArchitectureTest` checks that no POM under `iris/**/pom.xml` directly declares `<artifactId>postgresql</artifactId>` and that `iris-befe/src/main/java` contains no `import org.postgresql.*`.
- Iris BEFE source code imports only `javax.sql.DataSource` and `@KleioAudit`. Container-managed DataSource ownership is preserved; no direct PostgreSQL driver dependency is added to `iris-befe/pom.xml`.

### Key Decisions
1. **DataSource Ownership & Semantic Separation**:
   - `kleio-persistence` owns the semantic dependency `@KleioAudit DataSource`.
   - `iris-befe` (the composition root) owns the physical binding by providing a `@Produces @KleioAudit @ApplicationScoped DataSource` method that injects `@Resource(lookup = "java:jboss/datasources/KleioAuditDS")`.
   - *Rationale*: Distinct JNDI identity `java:jboss/datasources/KleioAuditDS` decouples audit persistence from default container datasources and enables AUDIT-BL-02 to relocate the physical database solely through composition/runtime configuration without modifying `kleio-persistence`.
2. **Minimum Maven Dependency Selection**:
   - `iris-befe/pom.xml` adds only `net.fhirfactory.harmonia:kleio-persistence`.
   - `wildfly-jar-maven-plugin` adds `<layer>datasources</layer>`.
   - *Rationale*: Adding `kleio-persistence` automatically pulls `kleio-core` and `kleio-fhir` transitively, includes `META-INF/beans.xml` for Weld scanning, and keeps POM configuration minimal without adding duplicate direct dependencies.
3. **Strict Interface Consumption in BEFE**:
   - Iris BEFE source code will reference only `AuditService` and `@KleioAudit`. It will not reference `JdbcAppendOnlyAuditEventRepository` or database-specific types.
   - *Rationale*: Preserves Iris presentation decoupling and enforces architectural layering rules.

### CDI Dependency Graph & Wiring

| Injection Target | Injected Type / Qualifier | Provided By | Scope |
| :--- | :--- | :--- | :--- |
| Any BEFE Caller | `AuditService` | `DurableAuditService` (`kleio-persistence`) | `@ApplicationScoped` |
| `DurableAuditService` | `AppendOnlyAuditEventRepository` | `JdbcAppendOnlyAuditEventRepository` (`kleio-persistence`) | `@ApplicationScoped` |
| `DurableAuditService` | `HarmoniaAuditEventMapper` | `FhirContextProducer.produceMapper()` (`kleio-persistence`) | `@ApplicationScoped` |
| `DurableAuditService` | `FhirContext` | `FhirContextProducer.produceFhirContext()` (`kleio-persistence`) | `@ApplicationScoped` |
| `JdbcAppendOnlyAuditEventRepository` | `@KleioAudit DataSource` | `AuditDataSourceProducer.produceAuditDataSource()` (`iris-befe`) | `@ApplicationScoped` |

### Producer & DataSource Definition
```java
package net.fhirfactory.harmonia.befe.config;

import jakarta.annotation.Resource;
import jakarta.annotation.sql.DataSourceDefinition;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit;

import javax.sql.DataSource;

@ApplicationScoped
@DataSourceDefinition(
        name = "java:jboss/datasources/KleioAuditDS",
        className = "org.postgresql.ds.PGSimpleDataSource",
        url = "${env.FHIR_DB_URL:jdbc:postgresql://postgres-1:5432/fhir_node_1}",
        user = "${env.FHIR_DB_USER:fhir_user}",
        password = "${env.FHIR_DB_PASSWORD:fhir_password}",
        properties = {
                "serverName=${env.FHIR_DB_HOST:postgres-1}",
                "portNumber=${env.FHIR_DB_PORT:5432}",
                "databaseName=${env.FHIR_DB_NAME:fhir_node_1}"
        }
)
public class AuditDataSourceProducer {

    @Resource(lookup = "java:jboss/datasources/KleioAuditDS")
    private DataSource dataSource;

    @Produces
    @KleioAudit
    @ApplicationScoped
    public DataSource produceAuditDataSource() {
        return dataSource;
    }
}
```

### Architecture Diagram
```mermaid
graph TD
    subgraph IrisBEFE [Iris BEFE Composition Root]
        DSD[@DataSourceDefinition: java:jboss/datasources/KleioAuditDS]
        ADSP[AuditDataSourceProducer]
        KLDS[Resource: java:jboss/datasources/KleioAuditDS]
        DSD -->|provisions| KLDS
        KLDS -->|injected into| ADSP
    end

    subgraph KleioPersistence [Kleio Persistence Subsystem]
        DAS[DurableAuditService]
        JAER[JdbcAppendOnlyAuditEventRepository]
        FCP[FhirContextProducer]
        FC[FhirContext R5]
        HAEM[HarmoniaAuditEventMapper]

        FCP -->|produces| FC
        FCP -->|produces| HAEM
        ADSP -->|@Produces @KleioAudit| JAER
        JAER -->|injected into| DAS
        FC -->|injected into| DAS
        HAEM -->|injected into| DAS
    end

    subgraph Storage [PostgreSQL Database]
        PG[(hie_fhir_resources table)]
        JAER -->|JDBC Insert / Select| PG
    end

    BEFECALLER[BEFE Consumers] -->|@Inject AuditService| DAS
```

### Guardrail Compliance & Risks
- **`IrisDecouplingArchitectureTest`**: Iris BEFE POM does not declare `postgresql` or `hibernate-core` direct dependencies. Source code does not import `jakarta.persistence.*`, `org.hibernate.*`, `org.postgresql.*`, or `ca.uhn.fhir.jpa.*`. Only `javax.sql.DataSource` is used.
- **`PackageLayeringArchitectureTest`**: Unidirectional layering is respected (`iris-befe -> kleio-persistence -> kleio-fhir/kleio-core -> calliope/themis-api`).
- **`SecurityEnforcementArchitectureTest`**: Audit model packages and Themis contracts remain untouched and strictly enforced.

# Testing

### Validation Approach
Verification of Step 04.2 explicitly separates:
1. **CDI Graph Verification**: Booting a standalone Weld SE context in unit/integration tests (`KleioRuntimeCompositionTest`) to verify bean discovery, injection point satisfaction, qualifier matching, and ambiguity-free resolution of `AuditService -> DurableAuditService -> JdbcAppendOnlyAuditEventRepository`.
2. **WildFly Runtime / Container Verification**: Validating WAR packaging and deployment readiness under WildFly's JTA, `@DataSourceDefinition`, and `@Resource` container-managed lifecycle.
3. **Architecture Guardrail Verification**: Running ArchUnit tests to ensure zero architectural boundary violations.

### Key Scenarios

#### Scenario 1: CDI Graph Verification & Unambiguous Bean Resolution
- **Test**: `testCdiBeanResolution()` in `KleioRuntimeCompositionTest`.
- **Expected Outcome**:
  - `CDI.current().select(AuditService.class).get()` returns an instance of `DurableAuditService`.
  - `CDI.current().select(AppendOnlyAuditEventRepository.class).get()` returns an instance of `JdbcAppendOnlyAuditEventRepository`.
  - `CDI.current().select(DataSource.class, new KleioAuditLiteral()).get()` returns the configured DataSource.
  - `CDI.current().select(FhirContext.class).get()` and `CDI.current().select(HarmoniaAuditEventMapper.class).get()` are non-null and valid.

#### Scenario 2: Immutable Append-Only Audit Evidence Execution via Composed Service
- **Test**: `testAuditServiceAppendAndFindThroughCdi()` in `KleioRuntimeCompositionTest`.
- **Expected Outcome**:
  - Calling `auditService.append(event)` records a valid `HarmoniaAuditEvent` into the database table `hie_fhir_resources`.
  - Calling `auditService.findById(eventId)` recovers the exact event canonically.
  - Appending identical event produces idempotent success; appending divergent payload with identical ID throws `AuditIntegrityException`.

#### Scenario 3: Architecture Test Suite Verification
- **Test**: Run all ArchUnit architecture tests.
- **Expected Outcome**:
  - `IrisDecouplingArchitectureTest` passes with zero violations.
  - `PackageLayeringArchitectureTest` passes with zero violations.
  - `SecurityEnforcementArchitectureTest` passes with zero violations.

### Verification Commands
```bash

# 1. Run Iris BEFE tests including CDI composition test

mvn test -pl iris/iris-befe

# 2. Run Architecture Test Suite

mvn test -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false

# 3. Run Kleio Persistence and Iris BEFE together

mvn test -pl kleio/kleio-persistence,iris/iris-befe
```

# Delivery Steps

### ✓ Step 1: Configure Maven dependencies, WildFly provisioning layers, and implement the Iris BEFE Kleio Audit DataSource producer
Add the `kleio-persistence` dependency and `datasources` WildFly layer to `iris/iris-befe/pom.xml`, and create the CDI producer `AuditDataSourceProducer` in Iris BEFE configured with `@DataSourceDefinition` and `@Resource(lookup = "java:jboss/datasources/KleioAuditDS")`.

- Update `iris/iris-befe/pom.xml` to add `net.fhirfactory.harmonia:kleio-persistence` as a direct dependency (which transitively includes `kleio-core`, `kleio-fhir`, and runtime PostgreSQL drivers).
- Update `iris/iris-befe/pom.xml` `wildfly-jar-maven-plugin` configuration to include `<layer>datasources</layer>` in the provisioned layers.
- Create `iris/iris-befe/src/main/java/net/fhirfactory/harmonia/befe/config/AuditDataSourceProducer.java` annotated with `@ApplicationScoped` and `@DataSourceDefinition` targeting `java:jboss/datasources/KleioAuditDS` with externalized environment variables.
- Inject the container-managed DataSource via `@Resource(lookup = "java:jboss/datasources/KleioAuditDS")`.
- Implement `@Produces @KleioAudit @ApplicationScoped public DataSource produceAuditDataSource()` to expose the DataSource to Kleio repository beans.
- Update deployment environment configuration in `deployment/kubernetes/base/iris/iris-befe.yaml` and `docker-compose.yml` to supply `FHIR_DB_URL`, `FHIR_DB_USER`, `FHIR_DB_PASSWORD`.
- Ensure Iris BEFE source code imports only `javax.sql.DataSource`, `jakarta.annotation.sql.DataSourceDefinition`, and `net.fhirfactory.harmonia.kleio.persistence.qualifier.KleioAudit`, avoiding direct imports of PostgreSQL driver, JPA, Hibernate, or repository internal classes.

### ✓ Step 2: Implement the Kleio CDI runtime composition test and verify architecture guardrails
Create a dedicated composition test verifying CDI resolution of the Kleio service graph in Iris BEFE and validate all repository architecture guardrails.

- Add test dependency `org.jboss.weld.se:weld-se-core` (scope `test`) in `iris/iris-befe/pom.xml` to enable standalone CDI container testing.
- Create `iris/iris-befe/src/test/java/net/fhirfactory/harmonia/befe/config/KleioRuntimeCompositionTest.java` to boot the CDI container and verify:
  - `AuditService` resolves unambiguously to `DurableAuditService`.
  - `AppendOnlyAuditEventRepository` resolves unambiguously to `JdbcAppendOnlyAuditEventRepository`.
  - `@KleioAudit DataSource`, `HarmoniaAuditEventMapper`, and `FhirContext` resolve and inject successfully.
  - Test append and point read operations execute successfully against a test-configured DataSource verifying immutable append-only audit evidence semantics.
- Execute full ArchUnit test suite including `IrisDecouplingArchitectureTest`, `PackageLayeringArchitectureTest`, and `SecurityEnforcementArchitectureTest` to confirm compliance.