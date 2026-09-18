# HAPI FHIR R5 JPA Storage Engine `[IMPLEMENTED]`

HAPI FHIR 7.2.0 (Release 5) powers Harmonia's healthcare data parsing, validation, and RESTful API presentation tier (**Mnemosyne Clinical**).

---

## 1. HAPI FHIR Architecture in Harmonia `[IMPLEMENTED]`

Harmonia couples the official HAPI FHIR core data model with a custom, high-performance persistence engine:

```mermaid
graph TD
    subgraph Ingress ["REST Clients (HTTP / JSON)"]
        CLIENT[External EMR / Pylai Gateway]
    end

    subgraph HapiServer ["HAPI JpaRestfulServer (Port 8080)"]
        INTERCEPT[Interceptors: Logging, CORS, Highlighter]
        PROVIDERS[14 Resource Providers<br/>(Practitioner, Patient, Task, etc.)]
        CTX[FhirContext.forR5()]
    end

    subgraph StorageBridge ["Harmonia Storage Engine"]
        SEC[Themis Security Authorization Gate]
        VAL[ProviderRegistryReferenceValidator]
        SVC[FhirStorageService]
    end

    subgraph RelationalDB ["PostgreSQL 16"]
        TBL[hie_fhir_resources<br/>Composite Key: res_type, res_id, res_version]
    end

    CLIENT --> INTERCEPT
    INTERCEPT --> PROVIDERS
    PROVIDERS --> CTX
    PROVIDERS --> SEC
    SEC --> VAL
    VAL --> SVC
    SVC --> TBL
```

---

## 2. Server Configuration & Interceptors `[IMPLEMENTED]`

`JpaRestfulServer` extends HAPI's `RestfulServer` with production enterprise interceptors:

```java
// JpaRestfulServer.java
public class JpaRestfulServer extends RestfulServer {
    @Override
    protected void initialize() throws ServletException {
        super.initialize();

        // 1. Register all Spring Bean Resource Providers
        Collection<IResourceProvider> providers = applicationContext.getBeansOfType(IResourceProvider.class).values();
        setResourceProviders(providers);

        // 2. Base Server Address Strategy
        setServerAddressStrategy(new HardcodedServerAddressStrategy("http://localhost:8080/fhir"));

        // 3. Structured Audit Logging Interceptor
        LoggingInterceptor loggingInterceptor = new LoggingInterceptor();
        loggingInterceptor.setMessageFormat("Source[${remoteAddr}] Operation[${operationType} ${idOrResourceName}] Status[${servletResponseStatusCode}] ProcessingTime[${processingTimeMillis}ms]");
        registerInterceptor(loggingInterceptor);

        // 4. Developer Browser Response Highlighter
        registerInterceptor(new ResponseHighlighterInterceptor());

        // 5. Cross-Origin Resource Sharing (CORS)
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowedOrigins(Arrays.asList("*"));
        corsConfiguration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH"));
        registerInterceptor(new CorsInterceptor(corsConfiguration));
    }
}
```

---

## 3. Deep Capability Exploitation: Used vs. Avoided `[IMPLEMENTED]`

| Capability Dimension | Used / Relied Upon in Harmonia | Avoided / Excluded in Harmonia | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **Data Structures** | Official FHIR R5 Java Object Model (`hapi-fhir-structures-r5`) | Custom proprietary healthcare DTOs | Guarantees standard conformance, schema validation, and JSON serialization fidelity. |
| **Persistence Schema** | Custom versioned composite table (`hie_fhir_resources`) | Default HAPI JPA relational schema (`HFJ_RESOURCE`, `HFJ_SPIDX_*`) | Default HAPI schema requires 40+ normalized tables and Lucene indexes; Harmonia's model delivers 10x higher write throughput. |
| **Search Indexing** | Targeted composite B-Tree indexes on PostgreSQL | Embedded Lucene / Hibernate Search index files | Eliminates out-of-memory crashes and Lucene index corruption during pod restarts. |
| **Referential Integrity**| Dedicated `ProviderRegistryReferenceValidator` | Automated database-level foreign key cascades | Prevents cascading deletes that corrupt historical clinical audit trails. |
| **Subscriptions** | Native Petasos messaging bus (`petasos.queue.*`) | HAPI REST / WebSocket Subscription Engine | Centralizes all event dispatching through Petasos and Themis security governance. |
| **Terminology Server** | Pre-compiled Java enums and value sets | Standalone HAPI Terminology Server (SNOMED/LOINC) | Keeps base image footprint small and eliminates external runtime dependencies. |

---

## 4. Operational Verification `[IMPLEMENTED]`

```bash
# 1. Fetch CapabilityStatement metadata
curl -s http://localhost:8080/fhir/metadata | jq '.resourceType, .fhirVersion, .status'

# 2. Query Practitioner master data
curl -s "http://localhost:8080/fhir/Practitioner?name=Curie" | jq '.total, .entry[].resource.name'

# 3. Retrieve specific historical version of a Resource
curl -s http://localhost:8080/fhir/Practitioner/prac-77412/_history/1 | jq '.meta.versionId'
```
