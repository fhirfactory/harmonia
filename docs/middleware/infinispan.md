# Infinispan 15.0.3 Distributed Cache Grid Topology `[IMPLEMENTED]`

Infinispan 15.0.3 powers Harmonia's in-memory data grid (**Mneme**), providing low-latency distributed caching and operational state synchronization.

---

## 1. Clustered StatefulSet Topology

- **Workload**: StatefulSets `infinispan-1` and `infinispan-2` in the `harmonia` namespace.
- **Discovery**: JGroups TCP discovery over port `7800` using the configured `infinispan-service` Kubernetes Service in the `harmonia` namespace.
- **Client Protocol**: Hot Rod binary RPC protocol on port `11222`.

```
[ Iris BEFE / Ponos / Gateways ]
               │
               ▼  Hot Rod Protocol (11222)
┌──────────────────────────────────────────────┐
│  Infinispan Node 1  ◄──JGroups (7800)──►  Infinispan Node 2  │
│  (infinispan-1)                           (infinispan-2)     │
└──────────────────────────────────────────────┘
```

---

## 2. Configured Cache Definitions (`infinispan.xml`)

Mneme defines 17 replicated caches, all configured with synchronous replication (`REPL_SYNC`), `text/plain` encoding, and write-behind persistence stores:

| Cache Name | Cache Mode | Encoding | Store Binding | Purpose |
| :--- | :--- | :--- | :--- | :--- |
| `person-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Person resources (clinical) |
| `relatedperson-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR RelatedPerson resources (clinical) |
| `practitioner-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Practitioner resources (provider registry) |
| `practitionerrole-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR PractitionerRole resources (provider registry) |
| `organization-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Organization resources (provider registry) |
| `location-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Location resources (provider registry) |
| `healthcareservice-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR HealthcareService resources (provider registry) |
| `group-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Group resources (care teams) |
| `provenance-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Provenance resources (audit lineage) |
| `auditevent-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR AuditEvent resources (security audit) |
| `consent-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Consent resources (privacy governance) |
| `task-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Task resources (workflow state) |
| `communication-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR Communication resources (message payloads) |
| `documentreference-cache` | `REPL_SYNC` | `text/plain` | `FhirRestCacheStore` | FHIR DocumentReference resources (clinical documents) |
| `tasksequence-cache` | `REPL_SYNC` | `text/plain` | `OperationsRestCacheStore` | Praxis TaskSequence workflow definitions (operations) |
| `messagequeue-cache` | `REPL_SYNC` | `text/plain` | `OperationsRestCacheStore` | Petasos message queue state (operations) |
| `modulestatus-cache` | `REPL_SYNC` | `text/plain` | `OperationsRestCacheStore` | Module health & status telemetry (operations) |

**Key Configuration Details**:
- **Replication Mode**: All caches use `mode="SYNC"` for synchronous replication across cluster nodes.
- **Encoding**: All caches use `text/plain` for both keys and values (no Protobuf or binary serialization).
- **Write-Behind Stores**: Each cache is backed by a `write-behind` store with `modification-queue-size="1024"`:
  - **Clinical Caches** (person, relatedperson, practitioner, practitionerrole, organization, location, healthcareservice, group, provenance, auditevent, consent, task, communication, documentreference): Flush to `FhirRestCacheStore` → `http://mnemosyne-clinical-1:8080/fhir/r5` (FHIR REST endpoint).
  - **Operations Caches** (tasksequence, messagequeue, modulestatus): Flush to `OperationsRestCacheStore` → `http://mnemosyne-operations-1:8080/api/operations` (Operations REST endpoint).
- **No TTL/Lifespan**: Caches are configured without explicit expiration; entries persist until explicitly evicted or the cluster is restarted.
- **No Distributed Mode**: All caches are replicated (not distributed); every node holds a complete copy of all entries.

---

## 3. Write-Behind CacheStore SPI Integration

Mneme utilizes a custom `NonBlockingStore` SPI (`mneme-persistence`):
- Cache mutations trigger an asynchronous write-behind buffer.
- Buffer flushes asynchronously post FHIR/Operations JSON payloads to Mnemosyne REST endpoints (`http://mnemosyne-clinical:8080/fhir/r5` and `http://mnemosyne-operations:8080/api/operations`).
- Isolates high-frequency in-memory reads and writes from relational disk I/O latency.

---

## 4. Middleware Capabilities: Used vs. Avoided

| Capability Dimension | Used / Relied Upon in Harmonia | Avoided / Excluded in Harmonia | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **Data Synchronization** | Synchronous replicated caches (`REPL_SYNC`) | Asynchronous replication (`REPL_ASYNC`) without acknowledgement or distributed mode | Synchronous replication keeps the complete cache copy consistent across nodes. |
| **Transport Protocol** | Hot Rod Binary Protocol (port `11222`) | REST, Memcached, WebSocket protocols | Hot Rod binary protocol minimizes serialization latency and payload size. |
| **Clustering Discovery** | JGroups TCP discovery (port `7800`) over K8s headless service | JGroups UDP multicast | UDP multicast is unreliable or prohibited in Kubernetes container networks. |
| **Query & Indexing** | Key-based lookups over text payloads | Protobuf-specific lookup and embedded Lucene / Hibernate Search | The configured cache encoding is `text/plain`; search indexing is not configured in Mneme. |
| **Transactions** | Non-transactional caches with write-behind SPI | 2-Phase Commit (2PC) / XA distributed transactions | Eliminates 2PC distributed locking overhead; relies on idempotent workflows. |
| **Cross-Site Replication** | Multi-node local Kubernetes cluster | Cross-site replication (x-site across data centers) | Cross-site replication is not configured; local cluster replication handles node recovery. |

---

## 5. Operational Verification

```bash
# 1. Verify Infinispan cluster health and member nodes
kubectl exec -it infinispan-1-0 -n harmonia -- \
  /opt/infinispan/bin/cli.sh --connect --username admin --password $INFINISPAN_PASSWORD \
  "describe cluster"

# 2. Inspect cache status and entry counts
kubectl exec -it infinispan-1-0 -n harmonia -- \
  /opt/infinispan/bin/cli.sh --connect --username admin --password $INFINISPAN_PASSWORD \
  "cache task-cache"

# 3. Check memory consumption and JVM stats
kubectl exec -it infinispan-1-0 -n harmonia -- \
  /opt/infinispan/bin/cli.sh --connect --username admin --password $INFINISPAN_PASSWORD \
  "stats"
```
