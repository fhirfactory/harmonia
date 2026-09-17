# Getting Started: Terminology & Etymology Reference

Harmonia uses classical Greek naming metaphors to describe its architectural components. This guide demystifies these names, mapping each term to its classical etymology, architectural role in Harmonia, and familiar enterprise/healthcare equivalent.

---

## 1. Core Greek Metaphor Register `[IMPLEMENTED]`

| Concept Name | Classical Etymology | Mythological / Classical Role | Harmonia Architectural Responsibility | Industry Equivalent | Status |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Harmonia** | *ἁρμονία* (agreement, concord) | Immortal goddess of harmony, order, and bringing discordant elements together. | The overarching Health Integration Environment (HIE) platform uniting all subsystems. | Enterprise Integration Engine / Interoperability Hub | `[IMPLEMENTED]` |
| **Hestia** | *Ἑστία* (hearth, fireside) | Virgin goddess of the hearth, family stability, and the enduring sacred flame. | Foundation subsystem governing both ephemeral caching (Mneme) and durable relational persistence (Mnemosyne). | Data Layer / Persistence Subsystem | `[IMPLEMENTED]` |
| **Pylai** | *Πύλαι* (gates, portals) | The great ceremonial and defensive city gates controlling entry and egress. | Ingress and egress protocol boundary gateways (inbound/outbound MLLP, FHIR REST Registry). | Protocol Adapters / Boundary Gateways | `[IMPLEMENTED]` |
| **Petasos** | *πέτασος* (winged sun hat) | The broad-brimmed winged hat worn by Hermes, messenger god of safe swift travel. | Resilient messaging abstraction layer isolating broker clients behind pure transport interfaces. | Message Transport / Event Bus (JMS / ActiveMQ Artemis) | `[IMPLEMENTED]` |
| **Energeia** | *ἐνέργεια* (actuality, action) | Aristotelian philosophical concept of activity, energy, and bringing potential into reality. | Task execution and workflow orchestration subsystem comprising workers, activity units, and blueprints. | Workflow Engine / Orchestration Layer | `[IMPLEMENTED]` |
| **Ponos** | *Πόνος* (toil, hard labor) | Personification of strenuous work, heavy physical labor, and ceaseless exertion. | High-concurrency WorkEngine daemon consuming tasks from message queues and invoking activities. | Background Task Worker / Consumer Daemon | `[IMPLEMENTED]` |
| **Ergon** | *ἔργον* (work, deed, artifact) | A discrete, purposeful piece of work or single finished deed. | Atomic, single-responsibility activity execution unit (e.g., parsing ADT, mapping FHIR, fan-out dispatch). | Pipeline Step / Task Processor / Command Handler | `[IMPLEMENTED]` |
| **Praxis** | *πρᾶξις* (practical action) | Purposeful, practical human action guided by reason; deliberate execution of a plan. | Workflow blueprint definition governing multi-step sequential or parallel task execution stages. | Workflow Definition / DAG / Pipeline Spec | `[IMPLEMENTED]` |
| **Pragma** | *πρᾶγμα* (matter, affair, thing done) | A tangible thing done, a matter of fact, or a concrete transaction. | Canonical task execution state envelope carrying metadata, checkpoints, security context, and payload references. | Correlation Envelope / Execution Context | `[IMPLEMENTED]` |
| **Mneme** | *Μνήμη* (memory, recollection) | One of the original three Boeotian Muses, personifying short-term memory and recall. | In-memory distributed cache grid (Infinispan 15.0.3 Hot Rod) providing sub-millisecond task coordination. | Distributed In-Memory Cache (L1/L2 Grid) | `[IMPLEMENTED]` |
| **Mnemosyne**| *Μνημοσύνη* (remembrance) | Titaness of memory and mother of the nine Muses; personifies permanent historical memory. | Authoritative relational persistence tier (HAPI FHIR R5 JPA and Spring Data JPA on PostgreSQL 16). | System of Record / Relational Database (SQL) | `[IMPLEMENTED]` |
| **Calliope** | *Καλλιόπη* (beautiful-voiced) | Eldest and chief of the nine Muses, presiding over eloquence and epic poetry. | Canonical data models, shared DTOs, HL7 v2-to-FHIR converters, and standard event envelopes. | Canonical Data Model (CDM) Library | `[IMPLEMENTED]` |
| **Themis** | *Θέμις* (divine law, custom) | Titaness of divine order, justice, and the unwritten cosmic law. | Default-deny policy evaluation engine, role-to-authority mappings, and non-PHI security audit logging. | Policy Decision Point (PDP) / ABAC/RBAC Engine | `[IMPLEMENTED]` |
| **Iris** | *Ἶρις* (rainbow messenger) | Goddess of the rainbow and swift winged messenger connecting sea, sky, and mortals. | Presentation tier delivering the WildFly BEFE REST gateway and three decoupled Vue 3 web SPAs. | Presentation Layer / Web Consoles & BFF | `[IMPLEMENTED]` |
| **Agora** | *ἀγορά* (assembly, marketplace) | Ancient Greek public gathering place for debate, civic assembly, and commerce. | Collaboration gateway projecting clinical events into Matrix Synapse Spaces, rooms, and care team chats. | Collaboration Gateway / Chat Ops Integration | `[IMPLEMENTED]` |
| **Paradeigma**| *παράδειγμα* (pattern, model) | A model, archetype, or pattern used to demonstrate a principle or test a hypothesis. | Synthetic clinical simulation framework generating personas, encounters, and multi-system scenarios. | Synthetic Testbed / Digital Twin / Simulator | `[IMPLEMENTED]` |

---

## 2. Healthcare & Integration Domain Terminology `[IMPLEMENTED]`

| Term / Acronym | Full Name / Expansion | Definition in Harmonia Context |
| :--- | :--- | :--- |
| **HIE** | Health Integration Environment | The comprehensive enterprise software platform facilitating secure, standardized health data exchange. |
| **HL7 v2.x** | Health Level Seven Version 2 | Pipe-delimited messaging standard used by legacy PAS, LMS, and EMR systems (e.g., ADT^A01, ORU^R01, ORM^O01). |
| **FHIR R5** | Fast Healthcare Interoperability Resources (Release 5) | Modern RESTful, JSON-based healthcare data standard utilized for Harmonia's internal canonical structures. |
| **MLLP** | Minimal Lower Layer Protocol | Lightweight framing protocol over TCP (Start Block `0x0B`, End Block `0x1C 0x0D`) used to transport HL7 v2 messages. |
| **PHI** | Protected Health Information | Individually identifiable health information (names, MRNs, dates of birth, diagnoses) protected under HIPAA/GDPR. |
| **Zero-PHI Logging** | Non-Clinical Operational Logging | Architectural invariant ensuring operational logs (`INFO`/`WARN`/`ERROR`) contain zero patient identifiers. |
| **PAS** | Patient Administration System | Hospital administrative system managing patient registration, admissions, transfers, and discharges. |
| **EMR / EHR** | Electronic Medical Record | Clinical software storing comprehensive patient charts, progress notes, and orders. |
| **LMS / LIS** | Laboratory Management / Information System | Specialized laboratory system managing specimen collection, pathology tests, and analytical results. |
| **RIS / PACS** | Radiology Information System / Picture Archiving | Systems managing medical imaging orders, examinations, and DICOM metadata. |
| **Hot Rod** | Infinispan Hot Rod Protocol | High-performance, binary, topology-aware client-server protocol used to communicate with Mneme cache clusters. |
| **JPA** | Jakarta Persistence API | Standard Java ORM framework utilized by Mnemosyne to manage relational persistence in PostgreSQL. |
| **AS** | Application Service (Matrix) | Privileged integration service connecting Harmonia's Agora subsystem to the Matrix Synapse homeserver. |
| **REC-001** | Ingress Dual-Write Safety | Architecture rule: gateways must guarantee Petasos queue acceptance before returning an `AA` acknowledgment over MLLP. |
| **REC-002** | Granular Fan-Out Tracking | Architecture rule: outbound dispatch activities must record destination-specific delivery checkpoints in `Task.output`. |
