# WildFly 31.0.1 Runtime Profile & BEFE Architecture `[IMPLEMENTED]`

WildFly 31.0.1.Final (Jakarta EE 10) hosts Harmonia's Backend-For-Frontend gateway (**Iris BEFE**) and workflow execution daemon (**Ponos WorkEngine**).

---

## 1. Runtime Profile & Subsystems `[IMPLEMENTED]`

Harmonia adopts WildFly as a lightweight, modular Jakarta EE 10 application container configured with a stripped-down profile:

```mermaid
graph TD
    subgraph IrisBefe ["Iris BEFE / Ponos WorkEngine (WildFly 31)"]
        JAXRS["Jakarta RESTful Web Services 3.1 (JAX-RS)"]
        CDI["Jakarta Contexts & Dependency Injection 4.0 (CDI)"]
        JSON["Jakarta JSON Processing (JSON-P) & JSON-B"]
        HOTROD["Embedded Infinispan Hot Rod Client"]
        MANAGEMENT["WildFly Management Interface (:9990)"]
    end

    subgraph PresentationTier ["Presentation Tier (Vue 3 SPAs)"]
        VUE_CLIN["iris-clinical"]
        VUE_CONS["iris-console"]
        VUE_ADMIN["iris-administration"]
    end

    subgraph DataGrid ["Mneme Cache Grid"]
        ISPN["Infinispan 15.0.3 (:11222)"]
    end

    VUE_CLIN -->|HTTP :8080 (/api/fhir/*)| JAXRS
    VUE_CONS -->|HTTP :8090 (/api/operations/*)| JAXRS
    VUE_ADMIN -->|HTTP :8080 (/api/fhir/*)| JAXRS

    JAXRS --> CDI
    CDI --> HOTROD
    HOTROD -->|Binary RPC| ISPN
```

### Core Jakarta EE APIs Utilized
- **Jakarta RESTful Web Services 3.1**: Implements `/api/fhir/*` and `/api/operations/*` endpoints.
- **Jakarta CDI 4.0**: Enforces dependency injection (`@ApplicationScoped`, `@Dependent`, `@Inject`) across services.
- **Jakarta JSON-P / JSON-B**: Native streaming JSON parsing and serialization.
- **Hot Rod Client**: Low-latency binary RPC client connecting directly to the Mneme Infinispan cluster.

---

## 2. Port Separation & Interface Binding `[CONFIGURED]`

WildFly is configured with strict port separation to isolate clinical queries from administrative operations:

| Interface / Subsystem | Port Number | Bind Address | Purpose | Security Domain |
| :--- | :--- | :--- | :--- | :--- |
| **Clinical FHIR Interface** | `8080` | `0.0.0.0` | JAX-RS endpoints for clinical FHIR resource browsing (`/api/fhir/*`). | Governed by Themis RBAC |
| **Operations Interface** | `8090` | `0.0.0.0` | JAX-RS endpoints for operational telemetry, queues, and task lineage (`/api/operations/*`). | Operations Admin Only |
| **WildFly Management** | `9990` | `0.0.0.0` | Container health probes, metrics, and administrative CLI. | Host / Kubelet Only |

---

## 3. Iris Presentation Decoupling (Invariant 3) `[IMPLEMENTED]`

WildFly strictly enforces the architectural boundary defined in Invariant 3:
- **Zero JPA / Hibernate**: Iris BEFE contains zero dependencies on `jakarta.persistence.*`, `org.hibernate.*`, or PostgreSQL drivers.
- **Zero Local Database Access**: All queries are fulfilled exclusively via Hot Rod binary RPC to Mneme or HTTP proxying to Mnemosyne REST.
- **State Statelessness**: HTTP sessions are completely stateless; client authentication tokens are passed in each request and validated by Themis.

---

## 4. Deep Capability Exploitation: Used vs. Avoided `[IMPLEMENTED]`

| Capability Dimension | Used / Relied Upon in Harmonia | Avoided / Excluded in Harmonia | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **Component Model** | Jakarta RESTful Web Services 3.1 & CDI 4.0 | Stateful Enterprise Java Beans (EJB) & JMS MDBs | Stateless HTTP REST and Hot Rod binary clients prevent session stickiness. |
| **Presentation Gateway** | Decoupled BEFE proxying Mneme | Direct JPA / Hibernate relational persistence | Strictly enforces Invariant 3 (Iris Presentation Decoupling). |
| **Clustering Architecture**| Standalone container instances in Kubernetes | WildFly Domain Mode / mod_cluster multicast | Kubernetes handles lifecycle, rolling updates, and liveness/readiness probes. |
| **Messaging Subsystem** | External ActiveMQ Artemis cluster via Hot Rod / REST | Embedded WildFly Artemis messaging broker subsystem | Prevents broker memory exhaustion and JVM GC pauses from disrupting HTTP gateway responsiveness. |
| **Security Governance** | Themis REST security context propagation | Container-managed JAAS realms with fixed database auth | Centralizes default-deny authorization via the unified Themis policy engine. |

---

## 5. Operational Verification `[IMPLEMENTED]`

```bash
# 1. Query server state and uptime via jboss-cli
kubectl exec -it iris-befe-0 -n harmonia -- \
  /opt/wildfly/bin/jboss-cli.sh --connect --command=":read-attribute(name=server-state)"

# 2. Check active HTTP listener metrics
kubectl exec -it iris-befe-0 -n harmonia -- \
  /opt/wildfly/bin/jboss-cli.sh --connect --command="/subsystem=undertow/server=default-server/http-listener=default:read-resource(include-runtime=true)"

# 3. Verify deployed WAR archives
kubectl exec -it iris-befe-0 -n harmonia -- \
  /opt/wildfly/bin/jboss-cli.sh --connect --command="deployment-info"
```
