# Concept: Harmonia `[IMPLEMENTED]`

Harmonia is the overarching Health Integration Environment (HIE) platform, providing the structural cohesion, lifecycle management, and architectural invariants that unify all subsystems into a single resilient, fault-tolerant healthcare integration ecosystem.

---

## 1. Classical Metaphor & Etymology `[IMPLEMENTED]`

- **Greek Term**: *Ἁρμονία* (Harmonia)
- **Etymology**: Derived from *ἁρμός* (joint, fastening) and the Proto-Indo-European root *\*ar-* (to fit together). In classical Greek, it signifies an agreement, a concord of sounds, or the harmonious fitting together of disparate parts.
- **Mythological Context**: Harmonia is the immortal goddess of harmony and concord, the daughter of Ares (god of war) and Aphrodite (goddess of love). Her union represents the cosmic balancing of opposites—dynamic energy and orderly grace—uniting discordant elements into peaceful, productive alignment.
- **Architectural Rationale**: In modern healthcare IT, hospital systems are notoriously discordant: legacy HL7 v2 engines, modern FHIR APIs, disparate vendor databases, and high-concurrency event brokers operate under conflicting paradigms. Harmonia serves as the unifying platform, harmonizing communication protocols, data schemas, security policies, and workflow lifecycles across the clinical enterprise.

---

## 2. Architectural Definition `[IMPLEMENTED]`

Architecturally, Harmonia represents the umbrella integration runtime, dependency container, and governance boundary for healthcare-grade data exchange. It defines:
1. The global 5-tier topology: Presentation (Iris), Gateways (Pylai), Execution (Energeia), Messaging (Petasos), and Persistence (Hestia: Mneme & Mnemosyne).
2. Cross-cutting governance: Default-deny security (Themis), canonical schemas (Calliope), collaboration (Agora), and synthetic clinical simulation (Paradeigma).
3. System-wide architectural invariants, including Ingress Dual-Write Safety (REC-001), Destination Fan-Out Telemetry (REC-002), and Zero-PHI Diagnostic Logging.

---

## 3. Ownership Boundaries `[IMPLEMENTED]`

### What Harmonia Owns
- Platform-wide parent POM configuration, build plugin orchestration, and dependency management (`pom.xml`).
- Unified container and MicroK8s deployment descriptors (`docker-compose.yml`, `deployment/kubernetes/base/`).
- System-level architectural invariants enforced via continuous ArchUnit verification.
- Global lifecycle coordination and graceful startup/shutdown sequencing across all tiers.
- Cross-subsystem correlation conventions (`TaskSequenceId`, `PragmaId`, `MessageId`).

### What Harmonia Explicitly Does NOT Own (Anti-Responsibilities)
- Direct protocol socket termination (delegated entirely to Pylai).
- Raw message broker client connections (delegated entirely to Petasos).
- Relational schema tables or ORM mapping logic (delegated entirely to Mnemosyne in Hestia).
- Specific clinical activity business logic (delegated entirely to Erga in Energeia).
- Direct browser UI rendering (delegated entirely to Iris).

---

## 4. Key Contracts & Identifiers `[IMPLEMENTED]`

- **Root Reactor**: `net.fhirfactory.harmonia:harmonia-parent:1.0.0-SNAPSHOT`
- **Correlation Keys**:
  - `TaskSequenceId`: Globally unique UUID identifying an end-to-end multi-step clinical integration workflow.
  - `PragmaId`: Globally unique UUID tracking a discrete activity checkpoint.
  - `MessageId`: Transport-level message identifier generated at ingress.
- **System Topologies**:
  - Local containerized environment: Docker Compose topology.
  - Production environment: MicroK8s Kubernetes cluster topology.

---

## 5. Subsystem Taxonomy `[IMPLEMENTED]`

```
+---------------------------------------------------------------------------------------+
|                                    HARMONIA HIE                                       |
+---------------------------------------------------------------------------------------+
| Presentation:        Iris (iris-befe, iris-clinical, iris-console, iris-admin)        |
| Perimeter Gateways:  Pylai (pylai-mllp-in, pylai-mllp-out, pylai-fhir-registry)       |
| Workflow Engine:     Energeia (ponos, erga, praxis, pragma)                           |
| Message Transport:   Petasos (petasos-api, petasos-core, petasos-artemis)             |
| Persistence & Cache: Hestia (mneme-cluster, mnemosyne-clinical, mnemosyne-operations) |
| Security Engine:     Themis (themis-api, themis-core, themis-audit)                   |
| Canonical Modeling:  Calliope (canonical schemas, event envelopes, converters)        |
| Collaboration:       Agora (agora-api, agora-matrix, agora-core, agora-service)       |
| Simulation Testbed:  Paradeigma (paradeigma-common, simulators, scenarios, arch tests)|
+---------------------------------------------------------------------------------------+
```
