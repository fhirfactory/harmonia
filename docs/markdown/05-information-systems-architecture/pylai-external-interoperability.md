<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Pylai External Interoperability and Praxis.Horos Architecture

**Standing: Domain05 Step A adjudicated Candidate Architecture, 2026-10-12.**
[U14](sources.md#u14) records the supplied working Pylai DesignNote and the
additional explicit task adjudications, including Broker, Server, Controller,
non-recoverability and EgressHoros. [Scope](README.md) applies. AX-02/03/04/05/07–15/17/18
preserve boundary contracts, native models, security, evidence, transient versus
durable state, distinct outcomes and architectural uncertainty. No implementation
design or whole-Domain05 acceptance is established.

<a id="pylai-responsibility"></a>

## External Interoperability Service

> **Pylai is Harmonia's External Interoperability Service, responsible for enabling
> and managing system-to-system communication between Harmonia and external systems
> and clients, irrespective of communication style, protocol, standard or technology.**

Pylai may support asynchronous messaging, synchronous or asynchronous remote
method invocation (RMI), APIs, files, polling and other external interoperability
mechanisms. RMI is an architectural interaction concept; it does not mean Java RMI.
HL7/FHIR gateway slices are examples, not the complete Pylai architecture.
Apache Camel or another integration framework may realise external connectors/
technical endpoints; it is a candidate implementation mechanism, not Pylai itself.

External representations conform to their applicable interoperability contracts.
Publication SHALL be non-destructive and fail-closed: only permitted information/
extensions may leave. Harmonia-private authority, security-context, cache,
persistence, governance and concurrency metadata are not automatically exportable.
Egress terminates management of the emitted representation (AX-02/13); retained
provenance does not extend control over the external copy.

The [Strategy/Terma reconciliation](gaps.md#pylai-strategy-conflict) distinguishes
external communication mechanics from Harmonia application meaning and generic
network/infrastructure management.

<a id="terma-and-diadromi"></a>

## Terma and Diadromí

**Pylai.Terma** is the logical external interoperability interface/boundary.
`Pylai.IngressTerma` and `Pylai.EgressTerma` specialise it where useful. Terma owns
external communication mechanics: connection/session handling, listening, polling,
transport/protocol interaction, framing, segmentation/reassembly, protocol
acknowledgements, API/RMI/file interaction, interaction-local communication retry
and external identity carriage. One Terma may use one or more technical endpoints/connectors;
it is not reducible to a socket, URL, Camel endpoint or deployed adapter.

> **Pylai.Terma manages the communication stack; the applicable Harmonia
> execution framework manages the application meaning.**

Terma owns the mechanics necessary to establish, conduct and complete an external
communication interaction. It does not own the Harmonia application semantics
of the information communicated. Application interpretation and processing use
the applicable Harmonia execution framework, principally Praxis/Ergo/Ergon,
except where an explicitly defined interaction pattern provides otherwise.
The [Server READ/SEARCH pattern](#pylai-server-controller) is such a defined
pattern; it grants Terma no application/business interpretation responsibility.

```text
External System → Pylai.Terma → Pylai.Diadromí → Praxis / Ergo / Ergon
                  communication mechanics       application interpretation,
                                                transformation / processing
```

Ingress/Egress describe **information direction**, irrespective of client/server
role, who opened the connection or push/pull behaviour. An IngressTerma may poll;
an EgressTerma may expose an external file interaction.

> **Pylai.Diadromí is the transient carriage and interaction context through which
> a Pylai.Terma exchanges communication with its Harmonia-side participant for the
> duration of an external interoperability interaction.**

> **Terma acts; Diadromí carries/represents transient interaction.**

A Diadromí may span multiple protocol exchanges, acknowledgements, retries within
the active interaction and segmented communication. An intermediate protocol
acknowledgement need not establish logical handover or authoritative Harmonia
acceptance. Diadromí is neither an Ergon, Task/Pragma alias nor a durable queue.
Its exact representation is unresolved.

**Pylai.Diadromí realises
[Metabolē](../04-information-architecture/patterns/metabole.md#2-architectural-realisation)**
for transient information progression through Pylai interoperability activity.
This is genuine architectural Realisation, not analogy, synonymy with Metabolē
or inheritance from Pragma. Pylai retains responsibility; Diadromí's transient,
non-recoverable lifecycle does not acquire Pragma persistence or Mneme management
requirements. Conceptual ActivityContext does not replace a specific interaction
action or grant authorisation.

<a id="diadromi-recoverability"></a>

## Transience, loss and recoverability

> **Pylai.Diadromí-Controller SHALL NOT maintain recoverable interaction state.
> A Diadromí represents a transient interoperability interaction and, if that
> interaction is lost or cannot be completed, recovery SHALL NOT resume the
> previous Diadromí. Where retry is appropriate, client-side activity must initiate
> a new interaction and therefore a new Diadromí.**

```text
active external interaction → active Diadromí → communication retry
                              ↑__________________________|
                              same active interaction

failed / lost Diadromí → interaction terminates
                       → NO RESUMPTION
appropriate client retry → NEW interaction → NEW Diadromí
```

Transient state may exist for the active communication's lifetime. Active protocol
retries differ from resuming a lost interaction. Terma may retry communication
only while that interaction and its Diadromí remain active. Terma, Diadromí and
its Controller provide no durable application recovery. Petasos and the established
Harmonia execution architecture retain their durable messaging/recovery
responsibilities; no lost Diadromí is resumed through them.
Audit, Journey and operational evidence about the failed interaction may remain:
**not recoverable does not mean not observable**.

Loss of interaction does not prove failure/non-occurrence of a previously requested
state change. AX-15 uncertainty and safe-retry constraints still apply. A new
Diadromí supplies no automatic permission to repeat an uncertain mutation.

<a id="pylai-patterns"></a>

## Three architectural interaction patterns

```text
Pylai
  +-- PylaiPathway: external interaction ↔ governed Praxis / Ergo work
  +-- PylaiBroker:  executing Ergo.BusinessLogic ↔ external communication
  `-- PylaiServer
        +-- CREATE / UPDATE / DELETE → PylaiPathway
        `-- READ / SEARCH            → Diadromí-Controller pattern
```

These are interaction patterns, not mandatory classes, Camel routes, processes or
deployment units. The Controller is not required for every Diadromí. External
DELETE is a governed state-changing request; it does not introduce physical DELETE
as Harmonia managed-information lifecycle semantics.

<a id="praxis-horos"></a>

## Praxis.Horos: logical Praxis boundary

**Praxis.Horos** represents a logical Praxis boundary, with
**Praxis.IngressHoros** and **Praxis.EgressHoros** specialisations.
This supersedes “Praxis Endpoint” where it meant that logical boundary. Horos
is not a technical/network endpoint and Terma is not an Ergo.

The existing [Ergon PathwayEndpoint / IngressEndpoint / EgressEndpoint](pathways-journeys-and-handoff.md#pathway-model)
Application Interfaces retain their U04 meaning; this task does not rename every
interface to Horos or equate every Ergon boundary with a Praxis boundary.
FHIR Endpoint resources, technical endpoints and Camel endpoints retain their
names. A Pylai-originating Praxis uses the normal Ergo boundaries, with the
specialised ingress Context establishment and egress ordering below.

<a id="pylai-pathway-ingress"></a>

## PylaiPathway ingress and initial Context establishment

```text
External System
      ↓
Pylai.IngressTerma
      ↓
Pylai.Diadromí
      ↓
Praxis.IngressHoros
      ↓
Ergo.Ingress
      ↓
special Ergo.ContextLoader: initial Context establishment
      ↓
Ergo.BusinessLogicFeeder
      ↓
Terma-paired developer Ergo.BusinessLogic
      ↓
Ergo.BusinessLogicCapturer
      ↓
Ergo.ContextUnloader
      ↓
Ergo.Egress
      ↓
Petasos.Queue[TermaName] = Praxis.PathwaySegment
```

Incoming raw payload, communication metadata and applicable external identity
constitute **Communication Information**. Pylai encapsulates what it received in
a **Communication Information Unit**. Its exact schema remains unresolved; the
name does not establish a new Domain04 Information Concept/Family, FHIR resource
equivalence or automatic persistence of all external identity/security material.

The specialised ingress ContextLoader receives that Unit, wraps/associates it
with the applicable Task, places it in `Task.input()`, establishes initial Context,
requests authoritative persistence as required and proceeds to the
BusinessLogicFeeder when applicable persistence/handover requirements are satisfied.
This initial establishment differs from ordinary loading of existing Context;
the [Mneme/Ergo service boundary](context-and-persistence-management.md#ergon-information-access)
remains intact. The Loader SHALL NOT perform semantic mapping/transformation.
The Terma-paired developer BusinessLogic interprets/maps/transforms the communication
into applicable `Task.output()` information. This pairing concerns the Pathway
specialisation and does not impose Ergon execution on Server READ/SEARCH.

The completed ingress Ergo feeds the Petasos queue named for its originating
Terma. That queue is the defined Praxis.PathwaySegment, not an arbitrary destination
selected by Pylai. This is the transition from transient external interaction into
established durable Harmonia work/message architecture. Petasos retains queue
state, by-value PetasosParcel handoff, redelivery, recovery and dead-letter handling;
referenced Pragma/Task information remains managed through Mneme/Mnemosyne. No
queue/Parcel schema or exact atomic handover mechanism is selected.

<a id="acceptance-and-acknowledgement"></a>

### Protocol progress and authoritative Harmonia acceptance

Where an acknowledgement represents **durable Harmonia acceptance**, it SHALL NOT
be issued until the applicable Pragma/Task information has been authoritatively
persisted through established Mneme/Mnemosyne responsibilities. Earlier
protocol-level/intermediate acknowledgements may occur where the protocol
requires them. Their occurrence does not imply authoritative acceptance, later
BusinessLogic completion or Context Unload completion.

REC-001 remains a minimum safety rule: downstream queue/cache failure cannot yield
AA. Queue/cache success alone does not establish the stronger durable-acceptance
condition. ADR-018's general durable work-acceptance possibility is not permission
to replace this explicitly adjudicated Pylai Pragma/Task persistence condition.
Protocol success, persistence-engine success and authoritative Harmonia persistence
outcome remain distinct. Protocol-specific acknowledgement/handover contracts and
uncertain outcomes remain [CDG-50](gaps.md#pylai-contracts); no two-phase transaction
or universal ACK algorithm is invented.

<a id="praxis-egress-horos"></a>

## Praxis.EgressHoros: handover out and handover back

> **Praxis.EgressHoros is a specialised Praxis.Horos that governs the handover
> from Praxis-controlled activity into a Pylai egress interaction and the return
> of the resulting interaction state/outcome into that Praxis activity.**

```text
Ergo.BusinessLogic
      ↓
Ergo.BusinessLogicCapturer
      ↓
Praxis.EgressHoros
      │ HANDOVER OUT
      ▼
Pylai.Diadromí
      ↓
Pylai.EgressTerma
      ↓
External System
      ↓
Pylai.EgressTerma
      ↓
Pylai.Diadromí
      │ HANDOVER BACK
      ▼
Praxis.EgressHoros
      ↓
Ergo.ContextUnloader
      ↓
Ergo.Egress
```

The resulting communication state/outcome must be available **before
Ergo.ContextUnloader completes**. Result handling SHALL NOT be moved after
Context unloading. An uncertain outcome remains uncertain; this ordering requires
neither a successful external reply nor a particular blocking thread, class,
transport or runtime handover mechanism. Mneme Context Save and Mnemosyne durable
establishment retain their separate responsibilities.

<a id="pylai-broker"></a>

## PylaiBroker: communication from executing BusinessLogic

```text
Existing Ergon
      ↓
Ergo.BusinessLogic ↔ PylaiBroker ↔ Pylai.Diadromí ↔ Pylai.Terma ↔ External System
```

PylaiBroker supports external communication initiated by already executing
Ergo.BusinessLogic. It SHALL NOT manufacture a new ActionableTask, fake Praxis,
fake Ergon or new Pathway merely to communicate. Applicable Themis enforcement,
Kleio audit, Dokimasia assurance and Journey participation still apply. Operating
outside a Pathway does not remove governance. No Controller-mediated Broker
architecture is required by the current candidate.

<a id="pylai-server-controller"></a>

## PylaiServer and Pylai.Diadromí-Controller

PylaiServer supports externally initiated server/API interaction. CREATE, UPDATE
and DELETE use PylaiPathway and governed Praxis/Ergo work. READ and SEARCH use
the following pattern and SHALL NOT manufacture ActionableTasks, Praxes or Erga
merely to satisfy the work-execution pattern.

> **Pylai.Diadromí-Controller manages the Harmonia-side progression and governance
> of a Diadromí where the external interaction is not governed through an
> executing Praxis/Ergon.**

```text
External Client
      ↓
PylaiServer / Terma
      ↓
Pylai.Diadromí
      ↓
Pylai.Diadromí-Controller
      ├── Themis participation
      ├── Kleio participation
      ├── Dokimasia participation as applicable
      ↓
Mneme
      ├── Adhoc Resource Access ← READ
      └── Adhoc Search          ← SEARCH
      ↓
Mnemosyne as needed
      ↓
result → Diadromí-Controller → same active Diadromí → Terma → External Client
```

The Controller coordinates established responsibilities. It SHALL NOT become
another Mneme, Themis, Kleio, Dokimasia, Ponos or Petasos, and SHALL NOT retain
recoverable interaction state. Mneme remains **Harmonia's Context and Pragma
Manager**, exposing exactly Context Load, Context Save, Adhoc Resource Access and
Adhoc Search. The Controller uses the applicable Adhoc service; it adds no Mneme
service, raw-cache/database path or persistence manager. Mnemosyne remains the
Persistence Manager and supplies authoritative search/persistence as needed.

“Same Diadromí” applies to the still-active interaction only. If lost, it ends;
an appropriate client retry creates a new Diadromí. Routing additional interaction
forms through the Controller is a possible future implementation consideration,
**not a current architectural requirement** or a generalisation to all Diadromí.

<a id="pylai-governance"></a>

## Identity, authorisation, audit, assurance and Journey

**Pylai carries external identity into Harmonia.** Trusted external Identity/context
is interpreted through [Identity.Authentication and Identity.Authorisation](component-register.md#identity-authentication).
Pylai SHALL NOT independently derive Harmonia authority, AuthorisationRoles,
Function permissions, Function.Scope or PEP-Criteria from claims. Untrusted
caller claims grant no authority. Authentication remains SEP; Themis owns
AuthorisationRoles, runtime Criteria, PEP-Criteria, policy resolution and the local
enforcement framework. Pylai hosts applicable Themis enforcement at operations
it controls, including Controller interactions. Detailed token/claim carriage
remains deferred; operational security context does not automatically become
persisted resource content or an externally publishable representation.

Architecturally meaningful interoperability transactions are **auditable**.
Pylai reports applicable transaction occurrence through Kleio's contract; Kleio
creates/manages authoritative audit information/AuditEvent. Low-level network
events do not each require an AuditEvent. Required authoritative durable capture
and [Kleio availability constraints](component-register.md#auditability-and-availability)
remain in force, independent of Journey participation.

Communication failures supply operational evidence and may be relevant to
Dokimasia. Pylai is neither the audit repository/policy manager nor the assurance
engine. **Audit is separate; audits are not governance.** Kleio performs mandatory
capture under [its governed intrinsic platform authority](component-register.md#kleio-capture-authority).
The external/acting Identity remains audit evidence, not the authority persisting
that evidence. This establishes no equivalent platform authority for Pylai.

| Construct | Meaning at this boundary |
| :--- | :--- |
| Pathway | Governed interaction structure; PylaiPathway uses it, Broker/Server reads do not manufacture it. |
| Praxis | Expected/permitted governing operational and assurance envelope; a Diadromí is not automatically a Praxis. |
| Journey | Canonical governed representation of observed/actual progression. Where applicable to the governed Journey it may record successful logical business interaction or technology/interoperability failure, without claiming unknown effects occurred or did not occur. |
| Audit | Significant auditable occurrence reported to Kleio; Journey may be a source but is not the audit boundary. |
| Assurance | Dokimasia may assess Journey against Praxis; evidence contribution does not make Pylai the assessor. |

Ponos remains the execution framework; Ergo the reusable conceptual activity
pattern/platform integration contract; Ergon the bounded fine-grained task-completion
component; Erga meaningful collections/assemblies/concurrent activity groupings.
Ergon remains Application Architecture behaviour with no discrete Information
Architecture representation merely by being an Ergon. Dokimasia's terminal
constraints remain intact. Pylai does not acquire these responsibilities through
participation in governed activity.

<a id="pylai-deferred"></a>

## Deferred design

The exact Communication Information Unit schema/lifecycle, Terma lifecycle and
technology/connector realisation, Diadromí representation, protocol-specific
ACK/persistence/handover behaviour (including segmented interactions and RMI),
detailed PylaiServer APIs, token/claim carriage, Journey/assurance observations,
operational telemetry, Horos representation and detailed runtime handover mechanics
remain unresolved. Possible broader Controller use stays an implementation
consideration. [CDG-50](gaps.md#pylai-contracts) retains these deferrals;
[CDG-51](gaps.md#pylai-strategy-conflict) records the resolved Strategy boundary.
No Java structure, Camel route, topology, protocol, persistence
schema, PEP-Criteria representation or complete endpoint/API design is selected.
