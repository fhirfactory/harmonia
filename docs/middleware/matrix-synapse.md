# Matrix Synapse Homeserver Middleware Topology `[CONFIGURED]`

Matrix Synapse (`matrixdotorg/synapse:v1.120.0`) serves as Harmonia's collaboration middleware, providing private chat, clinical task notification threads, and multidisciplinary coordination surfaces for **Agora**.

---

## 1. Homeserver Instance Topology `[CONFIGURED]`

| Workload / Service | Container Image | Port(s) | Database Name / Target | Default User | Persistent Storage | Domain Scope |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| `synapse` | `matrixdotorg/synapse:v1.120.0` | `8008` (HTTP) | `synapse_db` on `postgres-synapse:5436` | `synapse_user` | StatefulSet PVC (`synapse-data`, 5Gi) | Matrix Collaboration Homeserver |
| `postgres-synapse` | `postgres:16-alpine` | `5436` (Service), `5432` (Native) | `synapse_db` | `synapse_user` | StatefulSet PVC (`postgres-synapse-data`, 2Gi) | Synapse Relational Storage |

```mermaid
graph LR
    subgraph AgoraSubsystem ["Agora Collaboration Subsystem"]
        AGORA["agora-service<br/>Port 8092"]
    end

    subgraph SynapseHomeserver ["Matrix Synapse (:8008)"]
        AS_EP["AS Transaction Dispatcher"]
        ADMIN_API["Synapse Admin API"]
        ROOM_MGR["Room & Space Engine"]
    end

    subgraph SynapseStorage ["Dedicated Relational Storage"]
        PG_SYN["postgres-synapse (:5436)<br/>Database: synapse_db"]
    end

    AGORA -->|PUT /_matrix/app/v1/transactions/{txnId}| AS_EP
    AGORA -->|Admin User Provisioning| ADMIN_API
    AS_EP --> ROOM_MGR
    ROOM_MGR --> PG_SYN
```

---

## 2. Homeserver Configuration & Architectural Boundary `[CONFIGURED]`

Synapse is configured strictly as an internal, closed collaboration engine (`deployment/kubernetes/base/synapse-configmap.yaml`):

```yaml
server_name: "harmonia.local"
report_stats: false

# Federation & Registration Perimeter
federation:
  enabled: false
enable_registration: false
allow_guest_access: false

# Message Retention Policy (AGORA-ADR-004)
retention:
  enabled: true
  default_policy:
    min_lifetime: 1d
    max_lifetime: 90d
  allowed_lifetime_min: 1d
  allowed_lifetime_max: 365d

# Application Service Registration
app_service_config_files:
  - /etc/matrix-synapse/conf.d/appservice-agora.yaml
```

---

## 3. Application Service Registration (`appservice-agora.yaml`) `[CONFIGURED]`

Agora registers with Synapse as a privileged Application Service (`synapse-appservice-registration.yaml`):

- **Service ID**: `harmonia-agora`
- **URL**: `http://agora.harmonia.svc.cluster.local:8092`
- **Tokens**: `as_token` (used by Agora to access Synapse) and `hs_token` (used by Synapse to send transactions to Agora)
- **Namespaces**:
  - `users`: Exclusive regex `@_harmonia_.*:harmonia\.local`
  - `aliases`: Exclusive regex `#_harmonia_.*:harmonia\.local`
  - `rooms`: Non-exclusive wildcard access for managed collaboration spaces

---

## 4. Deep Capability Exploitation: Used vs. Avoided `[IMPLEMENTED]`

| Capability Dimension | Used / Relied Upon in Harmonia | Avoided / Excluded in Harmonia | Architectural Rationale |
| :--- | :--- | :--- | :--- |
| **Federation** | Closed, non-federated single-domain topology (`federation.enabled: false`) | Inter-server federation over port 8448 | Eliminates exposure to external Matrix homeservers; preserves strict healthcare data boundary (AGORA-ADR-006). |
| **Room Encryption** | Transport TLS 1.3 encryption across all network hops; non-E2EE rooms | Olm/Megolm end-to-end encryption (E2EE) | Allows server-side governance, automated auditing, message deduplication, and Petasos workflow dispatch without key-sharing complexity (AGORA-ADR-002). |
| **User Identity** | Deterministic local user accounts (`@_harmonia_p_<uuid>:synapse`) provisioned via Synapse Admin API | Open registration, public email/MSISDN 3PID discovery | Restricts collaboration participants to authenticated Harmonia principals governed by Themis (AGORA-ADR-001). |
| **Room Discovery** | Invite-only private Spaces and child rooms | Public room directory and world-readable aliases | Prevents unindexed access or accidental visibility of clinical collaboration contexts (AGORA-ADR-005). |
| **Event Retention** | Automated 90-day retention cleanup policy | Permanent unbounded homeserver message retention | Matrix is an ephemeral projection; authoritative clinical data is persisted in Mnemosyne FHIR storage (AGORA-ADR-004). |
| **Transaction Processing** | Durable AS transaction deduplication in PostgreSQL (`agora_as_transactions`) | Stateless transaction ingestion | Guarantees idempotent event processing without duplicate Petasos messaging dispatches (AGORA-ADR-007). |
| **Room Retirement** | Soft archival (`m.room.power_levels` read-only, kick members, status `ARCHIVED`) | Destructive room purge | Preserves historical traceability while revoking active interaction permissions (AGORA-ADR-008). |

---

## 5. Matrix Synapse & Admin REST API Interaction Catalogue `[IMPLEMENTED]`

Agora interacts with Synapse across three distinct API surfaces:

### 5.1 Synapse Administration REST API (Port 8008)
Executed via `SynapseAdministrationGateway` with admin Bearer token authentication:
- `PUT /_synapse/admin/v2/users/{userId}`: Deterministically provisions local user accounts (`@_harmonia_p_<uuid>:synapse`) with display names and randomized credentials.
- `POST /_synapse/admin/v1/reset_password/{userId}`: Programmatically rotates user credentials.
- `GET /_synapse/admin/v1/rooms?limit={limit}`: Queries homeserver-wide room inventory for operational auditing.

### 5.2 Matrix Client-Server REST API v3 (Port 8008)
Executed via `MatrixClientAdapter` on behalf of the AS bot (`@_harmonia_bot:synapse`):
- `POST /_matrix/client/v3/createRoom`: Provisions private Spaces (`creation_content: {type: "m.space"}`) and typed child rooms (Statistics, Tasks, Discussion, Diagnostics).
- `PUT /_matrix/client/v3/rooms/{roomId}/state/m.space.child/{childRoomId}`: Establishes parent-to-child links in Space hierarchies.
- `PUT /_matrix/client/v3/rooms/{childRoomId}/state/m.space.parent/{spaceRoomId}`: Establishes child-to-parent canonical references.
- `PUT /_matrix/client/v3/rooms/{roomId}/state/m.room.power_levels`: Sets moderation rules and enforces read-only power levels upon encounter archival.
- `GET /_matrix/client/v3/rooms/{roomId}/members`: Retrieves active membership rosters for care team reconciliation.
- `POST /_matrix/client/v3/rooms/{roomId}/invite`: Issues invitations to missing care team members.
- `POST /_matrix/client/v3/rooms/{roomId}/kick`: Ejects unauthorized participants no longer listed on authoritative rosters.
- `POST /_matrix/client/v3/rooms/{roomId}/send/m.room.message`: Emits clinical notifications, vitals alerts, and task updates.

### 5.3 Matrix Application Service Ingress (Port 8092)
Invoked by Synapse pushing event transactions to `ApplicationServiceTransactionEndpoint`:
- `PUT /_matrix/app/v1/transactions/{txnId}`: Receives batches of room events, validates Bearer `hs_token`, performs durable deduplication against `agora_as_transactions`, authorizes events via Themis, and dispatches them to Petasos queue `petasos.queue.agora.inbound`.

---

## 6. Operational Verification `[IMPLEMENTED]`

```bash
# 1. Verify Synapse health endpoint
curl -s http://synapse.harmonia.svc.cluster.local:8008/health | jq .

# 2. Check Application Service transaction registration
kubectl logs -l app=synapse -n harmonia | grep "Loaded application service"

# 3. Query active rooms via Synapse Admin API
curl -s -H "Authorization: Bearer $SYNAPSE_ADMIN_TOKEN" \
  "http://synapse.harmonia.svc.cluster.local:8008/_synapse/admin/v1/rooms?limit=10" | jq .

# 4. Verify Agora Application Service Transaction Receiver
curl -s http://agora.harmonia.svc.cluster.local:9992/actuator/health | jq .
```
