# Dokimasia — Assurance Framework Orientation

Dokimasia is Harmonia's independent assurance framework responsible for coordinating governed assurance activity over a subject in accordance with the authoritative assurance architecture. It provides an orientation for downstream realisation of Health Service Assurance; detailed solution architecture and responsibility allocation remain to be derived.

This page is an orientation/navigation artefact. Motivation, Strategy, the approved Business Architecture and later approved architecture remain authoritative over it. Under [AX-17](../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty), a conceptual framework is not an established Application Component, deployable module or implementation allocation.

## Canonical Assurance Architecture

The [approved Health Service Assurance Strategy](../markdown/02-strategy/capability-maps/health-service-assurance-derivation.md) traces the Motivation basis and capability realisation. The [approved Business Architecture](../markdown/03-business-architecture/behaviours/health-service-assurance.md) defines the assurance semantics. Navigate there for:

- [Service Assurance Modeller](../markdown/03-business-architecture/actors-roles/roles.md#service-assurance-modeller) and [Service Guardian](../markdown/03-business-architecture/actors-roles/roles.md#service-guardian).
- [Approved assurance Functions](../markdown/03-business-architecture/behaviours/health-service-assurance.md#3-direct-capability-scoped-responsibility-consequences).
- [Assurance Process Design, Assurance Process Execution and Assurance Process Reporting / Communication](../markdown/03-business-architecture/processes/business-processes.md#6-health-service-assurance-processes).
- [Approved assurance Services](../markdown/03-business-architecture/behaviours/health-service-assurance.md#5-approved-governed-assurance-business-services) and [Interactions](../markdown/03-business-architecture/collaborations-interactions/interactions.md#5-approved-service-assurance-interactions).

Dokimasia is associated with the downstream realisation of this architecture. This does not establish that Dokimasia owns or implements every Role, Function, Process, Service or Interaction. It may coordinate its own assurance activity without acquiring operational management of the subject being assured; the [approved conceptual finding](../markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md) records that boundary. Current Strategy and Business Architecture govern the subsequently reconciled assurance semantics.

## Digital Twins and Execution

Dokimasia is associated with Harmonia's assurance / Digital Twin design space. The [general Digital Twin definition](../markdown/02-strategy/strategic-views/logical-component-responsibilities.md#component-7-digital-twin-entity-centred-operational-coordination-construct) remains independent of AI, assurance, Dokimasia and any particular implementation mechanism. The [approved finding](../markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md#27-relationship-to-digital-twin) explicitly does not establish Dokimasia as a Digital Twin or subtype. Detailed Dokimasia/Digital-Twin allocation remains to be derived.

[Praxis](../concepts/praxis.md), [Ergon / Ergo](../concepts/ergon.md) and [Ponos](../concepts/ponos.md) document general execution concepts. The [Dokimasia / Assurance Praxis / Ponos execution relationship](../markdown/04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md#28-execution-relationship-with-ponos) remains architecturally unresolved; these links supply no execution allocation.

## Runtime AI

Dokimasia is expected to be a major consumer of Harmonia runtime AI because assurance commonly requires reasoning over criteria, patterns, context and evidence. The [canonical runtime-AI execution position](../architecture/execution-model.md#6-runtime-ai-as-adjunct-ergo-execution-capability) treats AI as adjunct capability, distinct from AI-assisted development. AI is not intrinsic to Dokimasia's identity: governed execution must remain possible regardless of whether a particular assurance activity uses AI.

AI does not become Service Guardian or independently acquire assurance authority. Potential uses are examples, not prescriptions or approval of assessment/adjudication allocation. The [assurance non-recursion boundary](../markdown/03-business-architecture/behaviours/health-service-assurance.md#assurance-non-recursion-boundary) remains controlling; AI use introduces no recursive assurance obligation.

## Open Downstream Architecture

Detailed responsibility allocation, the Digital Twin relationship, execution independence and the AI execution model remain downstream architecture. Application components, APIs, information models, persistence, AI orchestration and deployment are not defined here. Any AI-specific governance, safety, provenance or controls require separate derivation if needed.
