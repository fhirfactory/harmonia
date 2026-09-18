# Concept: Iris `[IMPLEMENTED]`

Iris is Harmonia's presentation tier, providing decoupled, modern browser user interfaces and a specialized WildFly 31 Backend-For-Frontend (BEFE) REST gateway for clinical exploration, operational monitoring, and provider administration.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Ἶρις* (Iris)
- **Etymology**: Ancient Greek noun referring to the rainbow, or the colored circle around the eye.
- **Mythological Context**: In Greek mythology, Iris is the goddess of the rainbow and the personal swift-winged messenger of the gods (particularly Hera and Zeus). With golden wings and a pitcher of sacred water from the River Styx, Iris travelled with the speed of the wind across the sky, into the depths of the ocean, and into the underworld to deliver divine messages directly to mortals. The rainbow was seen as her multicolored path bridging the celestial realm of Olympus to the human world below.
- **Architectural Rationale**: Iris bridges the deep, complex backend machinery of Harmonia (caches, queues, databases) into human-readable visual spectrums. Just as the rainbow reveals white light split into clear, vibrant colors, Iris translates high-throughput binary messages and JSON payloads into clean, intuitive, and responsive web user interfaces.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Iris separates the presentation tier into a dedicated Jakarta EE 10 BEFE gateway and three decoupled TypeScript / Vue 3 Single Page Applications (SPAs):

```
+---------------------------------------------------------------------------------------+
|                                     IRIS PRESENTATION                                 |
+---------------------------------------------------------------------------------------+
|                                                                                       |
|   +--------------------------------------------------------------------------------+  |
|   |                       TYPESCRIPT / VUE 3 FRONTEND SPAS                         |  |
|   |                                                                                |  |
|   |   +--------------------+  +--------------------+  +------------------------+   |  |
|   |   |   iris-clinical    |  |    iris-console    |  |  iris-administration   |   |  |
|   |   | (Port 3000 / Nginx)|  | (Port 3001 / Nginx)|  |   (Port 3002 / Nginx)  |   |  |
|   |   | * Clinical Viewer  |  | * Ops & Telemetry  |  | * Provider Registry UI |   |  |
|   |   | * FHIR Timeline    |  | * Artemis Queues   |  | * Practitioner Search  |   |  |
|   |   +--------------------+  +--------------------+  +------------------------+   |  |
|   +--------------------------------------------------------------------------------+  |
|                                         | HTTP REST Calls                             |
|                                         v                                             |
|   +--------------------------------------------------------------------------------+  |
|   |                  iris-befe (WildFly 31.0.1 Jakarta EE Gateway)                 |  |
|   |                                                                                |  |
|   |   - Port 8080: /api/fhir/* (Clinical REST endpoints)                           |  |
|   |   - Port 8090: /api/operations/* (Operational telemetry endpoints)             |  |
|   |   - Port 9990: WildFly Management Console                                      |  |
|   |   - Connects to Mneme distributed cache grid via Hot Rod binary RPC (:11222)   |  |
|   |   - Evaluates caller context via Themis API                                    |  |
|   +--------------------------------------------------------------------------------+  |
|                                                                                       |
+---------------------------------------------------------------------------------------+
```

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Iris Owns
- WildFly 31.0.1 Jakarta EE 10 Backend-For-Frontend gateway deployment descriptor and REST resource classes (`iris-befe`).
- Dual-port separated REST API routing (`8080` for clinical, `8090` for operations).
- Hot Rod cache client connections to the Mneme cluster.
- Vue 3 / Vite Single Page Application source trees (`iris-clinical`, `iris-console`, `iris-administration`).
- Client-side routing (Vue Router), state stores (Pinia), and responsive UX components.
- Nginx container hosting and static asset bundling.

### What Iris Explicitly Does NOT Own (Anti-Responsibilities)
- Direct JDBC connection pools, PostgreSQL drivers, or JPA entities (strictly forbidden under Invariant 3).
- Server-side Provider Registry validation state machines, database tables, or referential integrity (owned server-side by `mnemosyne-clinical`, `erga`, and `themis-core`).
- Message queue listening or background worker processing (owned by Petasos / Energeia).
- MLLP socket listening (owned by Pylai).

---

## 4. Key Architectural Invariants `[IMPLEMENTED]`

### Invariant 3: Iris Presentation Decoupling `[IMPLEMENTED]`
- Iris modules must **never** declare dependencies on or import `jakarta.persistence..`, `org.hibernate..`, or `org.postgresql..`.
- All data access must route exclusively through Hot Rod cache RPC or upstream REST APIs.
- Continuously verified by `IrisDecouplingArchitectureTest`.

### Provider Registry Decoupling `[IMPLEMENTED]`
- `iris-administration` is strictly a presentation-tier consumer of FHIR REST endpoints (`/fhir/r5/Practitioner`, `/fhir/r5/Organization`).
- It does not own backend persistence, master file validation, or change approval state machines.
- Continuously verified by `ProviderRegistryArchitectureTest`.

### Terminology Standardization `[IMPLEMENTED]`
- The operations monitoring SPA is canonically named **`iris-console`**. Legacy references to `iris-monitor` in older drafts are deprecated.

---

## 5. Key Modules & Ports `[IMPLEMENTED]`

| Component | Module Name | Technology | Ports | Endpoints | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Iris BEFE** | `iris-befe` | WildFly 31.0.1, Jakarta REST | `8080`, `8090`, `9990` | `/api/fhir/*`, `/api/operations/*` | `[IMPLEMENTED]` |
| **Iris Clinical** | `iris-clinical` | Vue 3, Vite, TypeScript, Nginx | `3000` (container: `80`) | `/` | `[IMPLEMENTED]` |
| **Iris Console** | `iris-console` | Vue 3, Vite, TypeScript, Nginx | `3001` (container: `80`) | `/console/` | `[IMPLEMENTED]` |
| **Iris Admin** | `iris-administration` | Vue 3, Vite, TypeScript, Nginx | `3002` (container: `80`) | `/admin/` | `[IMPLEMENTED]` |
