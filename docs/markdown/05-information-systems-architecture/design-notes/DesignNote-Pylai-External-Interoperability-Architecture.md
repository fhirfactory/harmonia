# DesignNote — Pylai External Interoperability Architecture

**Status:** Working Design Note — substantial architecture established; further nuances remain to be adjudicated  
**Date:** 2026-10-11  
**Scope:** Candidate Information Systems Layer Architecture — Pylai  
**Purpose:** Preserve the architectural reasoning and decisions established during the Pylai review before formal RADS consolidation.

> **Important:** This note is deliberately not a declaration that Pylai is architecturally complete. The core model described below has been substantially clarified, but additional Pylai nuances remain for later discussion when Mark is fresh. Those matters must not be silently inferred or completed from implementation convention.

## 1. Fundamental Responsibility

**Pylai is Harmonia's External Interoperability Service.**

Pylai is responsible for enabling and managing system-to-system communication between Harmonia and external systems and clients, irrespective of communication style, protocol, standard or technology.

Its responsibility is broader than standards-based interoperability. It must support whatever external communication Harmonia requires, including asynchronous message-oriented communication, synchronous and asynchronous Remote Method Invocation (RMI), APIs, file-based interaction, polling, externally initiated interaction, message brokers, and other mechanisms supported by the selected implementation technology.

`RMI` is an architectural interaction pattern meaning Remote Method Invocation; it does not imply Java RMI.

Apache Camel is the current candidate technology for Pylai's extensible external endpoint/connector capability. Pylai is not architecturally synonymous with Apache Camel.

## 2. Principal Interaction Semantics

```text
Pylai
 ├── Asynchronous Messaging
 ├── Synchronous RMI
 └── Asynchronous RMI
```

Whether Harmonia exposes or consumes a capability is a role/direction concern rather than a separate fundamental interaction semantic. External protocol complexity should terminate at Pylai rather than leak unnecessarily into the rest of Harmonia.

## 3. Information Direction Is Not Transport Role

Pylai ingress and egress are defined by the direction in which **information crosses the Harmonia boundary**, not by client/server role, connection initiator, push/pull behaviour, producer/consumer terminology, or endpoint technology.

An ingress Terma may actively poll an external system. An egress Terma may expose information through a file share rather than actively send it to a recipient.

## 4. Pylai.Terma

`Pylai.Terma` is the logical external interoperability interface/boundary through which information crosses between Pylai/Harmonia and the external environment.

Directional terminology may be used where useful:
- `Pylai.IngressTerma`
- `Pylai.EgressTerma`

A Terma owns the external interaction behaviour required for its interface. Terma I/O logic may therefore handle listening or polling, connection/session behaviour, protocol interaction, API/RMI mechanics, file interaction, segmentation/reassembly, protocol acknowledgements, final acknowledgements where applicable, retry behaviour, carriage of external identity, and other communication-specific behaviour.

A Terma may be realised through one or more technical endpoints/connectors, including Apache Camel endpoints.

### Security boundary

Pylai carries external identity into Harmonia. Pylai does **not** invent or determine Harmonia authority, roles, permissions or Criteria. Those remain responsibilities of Themis and the wider Harmonia security architecture.

## 5. Pylai.Diadromí

`Pylai.Diadromí` is a **transient representation of an interoperability interaction** during the period in which information is crossing a Pylai boundary but the defined logical handover condition has not yet been established.

A Diadromí is not a durable configuration object and is not the component performing protocol logic.

**B1 reconciliation, 2026-10-13:** Pylai.Diadromí **realises**
[Metabolē](../../04-information-architecture/patterns/metabole.md#2-architectural-realisation)
for transient information progression through Pylai interoperability activity.
This is architectural Realisation in the ArchiMate/TOGAF sense, not analogy,
synonymy or inheritance from Pragma. Pylai responsibility and the
[consolidated non-recoverability contract](../pylai-external-interoperability.md#diadromi-recoverability)
remain intact. The concept introduces no common manager, persistence model or
lifecycle with Pragma.

> **Terma acts; Diadromí represents transient interaction state.**

Events, alarms and performance metrics may be associated with a Diadromí. It represents a period partly outside Harmonia-controlled business logic where external interaction state may be observable without yet being certain.

A Diadromí may span multiple protocol exchanges, intermediate acknowledgements, retries, multiple message segments, and other protocol-specific interaction necessary to complete one logical interoperability transaction. Protocol-level acknowledgements may update Diadromí state without satisfying its logical handover condition.

## 6. Praxis.Horos and the Pylai Boundary

`Praxis.Horos` is the logical boundary of governed Praxis activity. Directional forms may be used where useful:
- `Praxis.IngressHoros`
- `Praxis.EgressHoros`

This terminology supersedes earlier use of `Praxis.Endpoint` where `Endpoint` meant the logical Praxis boundary. It does **not** replace legitimate technical/network/Camel endpoint terminology.

A Praxis starts and finishes normally. A Horos is associated with the appropriate Ergo boundary, normally an `Ergo.Ingress` or `Ergo.Egress`.

For a Praxis originating from Pylai ingress:

```text
External World
      │
      ▼
Pylai.IngressTerma
      │
      ▼
Pylai.Diadromí
      │
      ▼
Praxis.IngressHoros
      │
      ▼
Ergo.Ingress
```

The Praxis is not structurally redefined merely because its origin is Pylai. Terma and Diadromí feed the normal `Ergo.Ingress` boundary.

## 7. Communication Information Unit

For incoming information, Pylai encapsulates the external interaction into a **Communication Information Unit** before handing it to `Ergo.Ingress`.

The Communication Information Unit preserves the received communication rather than prematurely interpreting it. Conceptually it includes:

```text
Communication Information Unit
    ├── raw payload
    ├── associated API/message/communication metadata
    └── external identity information as applicable
```

The exact schema remains to be designed/adjudicated.

The important separation is:

```text
What Pylai actually received
        │
        ▼
Communication Information Unit
        │
        ▼
What interface-specific business logic
interprets / derives / constructs
        │
        ▼
Task.output()
```

## 8. Special Pylai Ingress Ergo Behaviour

The normal Ergo execution form is:

```text
Ergo.Ingress
      ↓
Ergo.ContextLoader
      ↓
Ergo.BusinessLogicFeeder
      ↓
[ Ergo Business Code ]
      ↓
Ergo.BusinessLogicCapturer
      ↓
Ergo.ContextUnloader
      ↓
Ergo.Egress
```

A Pylai-originating ingress Ergo preserves this structure but has specialised context-establishment behaviour.

For an ordinary Ergo, `Ergo.ContextLoader` loads the Context required to execute the Task. For a Pylai ingress Ergo, the incoming Communication Information Unit already carries the information from which the initial execution Context must be established.

The Pylai ingress `Ergo.ContextLoader` therefore:
1. receives the Communication Information Unit from `Ergo.Ingress`;
2. encapsulates it into a Task;
3. places the incoming Communication Information Unit in `Task.input()`;
4. establishes the required initial Context;
5. requests necessary authoritative persistence; and
6. releases the Task into `Ergo.BusinessLogicFeeder` when applicable persistence/handover requirements are satisfied.

It must **not** perform interface-specific semantic interpretation or transformation of the raw payload. That belongs to the paired developer-written `Ergo.BusinessLogic`.

## 9. Terma-Paired Ergo.BusinessLogic

Every `Pylai.Terma` has an `Ergo.BusinessLogic` paired with it.

That business logic is written by a developer who understands what the particular interface is intended to accomplish. It processes the Communication Information Unit supplied through `Task.input()` and produces the appropriate `Task.output()` for subsequent Harmonia processing.

Depending on the interface, that may involve interpretation, validation, mapping, transformation, enrichment, decomposition, construction of appropriate Harmonia representations, or other interface-specific processing needed to make the information "Harmonia digestible".

Once produced, `Task.output()` is handled using normal Harmonia Ergo/Praxis semantics. It is not treated specially merely because its source was an external Pylai interaction.

## 10. Pylai Specialisation Model

A Pylai specialisation consists principally of **two separate but paired aspects**:

```text
                Pylai Specialisation
                        │
            ┌───────────┴───────────┐
            ▼                       ▼
    Pylai.Terma I/O          Ergo.BusinessLogic
         Logic                    Logic
            │                       │
   "How do I interact         "What do I do
    with this external         with the raw
    system?"                   information?"
```

### Terma I/O specialisation
The Terma-specific logic handles the communication mechanics necessary to interact with the external system. It may be highly specific to the external system or protocol.

### Ergo Business Logic specialisation
The paired Ergo business logic handles whatever must be done to the received raw information to make it suitable for subsequent Harmonia processing. It is intentionally interface-specific and developer-defined.

### Architectural separation
The two concerns remain distinct. Vendor-specific communication behaviour does not contaminate Harmonia business processing, and interface-specific semantic transformation does not contaminate Pylai's generic interoperability framework.

The Communication Information Unit provides the stable information boundary between these concerns.

## 11. Petasos Linking and Praxis Pathway

For a Praxis originating at a Pylai point, the Petasos queue is not merely an implementation destination selected by the Pylai Ergo.

The applicable `Petasos.Queue` is an already-defined `Praxis.PathwaySegment`.

> **The Ergo.Egress of the Pylai-originating ingress Ergo feeds directly into the Petasos.Queue associated with the originating Pylai.Terma.**

The queue is named after the applicable `Pylai.Terma`.

```text
Pylai.IngressTerma
      │
Pylai.Diadromí
      │
Praxis.IngressHoros
      │
Ergo.Ingress
      │
specialised Context establishment
      │
Ergo.BusinessLogicFeeder
      │
developer-defined Ergo.BusinessLogic
      │
Ergo.BusinessLogicCapturer
      │
Ergo.ContextUnloader
      │
Ergo.Egress
      │
      ▼
Petasos.Queue[TermaName]
      │
      └── Praxis.PathwaySegment
```

Pylai does not need to know which later Harmonia processing ultimately consumes the queue. Petasos provides the durable queue/handoff mechanism and the Praxis Pathway defines its architectural role.

## 12. Acknowledgement and Handover Semantics

Acknowledgement semantics distinguish protocol progress from Harmonia acceptance.

Protocol-level acknowledgements are Terma responsibilities and may occur while the Diadromí remains active. They may acknowledge an individual message, one segment of a larger logical communication, protocol sequencing, or another intermediate condition. Such acknowledgement does **not** necessarily mean Harmonia has accepted responsibility for the complete logical information.

Where an acknowledgement represents Harmonia's durable acceptance of the logical incoming information:

> **That acknowledgement may occur only when the corresponding Pragma/Task information has been authoritatively persisted according to the defined handover semantics.**

This is distinct from later completion of Ergo Business Logic or Context Unload. Exact handover conditions may vary by interaction and must not be reduced to the assumption that every protocol ACK means successful Harmonia handover.

## 13. Journey, Audit and Operational Observation

**Journey:** records architecturally meaningful observed progression. At the Pylai boundary it may report successful business transaction progression at the applicable logical boundary, or a technology failure that prevented progression. Journey is factual observation, not AI interpretation.

**Audit:** every architecturally meaningful interoperability **transaction** is auditable. This does not mean every low-level network or Camel activity becomes an AuditEvent. Pylai identifies/reports the auditable transaction; Kleio constructs and manages the authoritative AuditEvent.

**Technical failures:** detailed Pylai communication failures and diagnostics are principally IT/Operations concerns. They may nevertheless provide evidence relevant to Dokimasia assurance activity.

## 14. Current Architectural Model

```text
External System
      │
      ▼
Pylai.Terma
  [external I/O behaviour]
      │
      ▼
Pylai.Diadromí
  [transient interaction state]
      │
      ▼
Communication Information Unit
  [raw payload + communication metadata]
      │
      ▼
Praxis.Horos / Ergo.Ingress
      │
      ▼
Specialised Pylai Context Establishment
  [Task + Task.input() + persistence]
      │
      ▼
Terma-paired Ergo.BusinessLogic
  [developer-defined interpretation/
   transformation]
      │
      ▼
Task.output()
      │
      ▼
Normal Ergo completion
      │
      ▼
Ergo.Egress
      │
      ▼
Petasos.Queue[TermaName]
  [Praxis.PathwaySegment]
      │
      ▼
Subsequent Harmonia processing
```

## 15. Matters Deliberately Left for Further Pylai Discussion

**Pylai is not yet declared complete.**

There are additional nuances Mark wishes to explore when fresh. These must remain explicit rather than being silently filled in by implementation assumptions, framework conventions or AI inference.

Potentially affected areas include, without asserting that each requires additional architecture:

- Pylai/Terma lifecycle and management semantics;
- Diadromí behaviour and its relationship to handover observation;
- ingress versus egress asymmetries;
- complex multi-message or multi-segment interactions;
- synchronous and asynchronous RMI behaviour;
- precise acknowledgement, persistence and handover semantics for different interaction forms;
- Communication Information Unit structure and lifecycle;
- context establishment and persistence sequencing;
- Pylai specialisation families;
- Journey/assurance observations at the Terma/Horos boundary;
- operational management and telemetry; and
- additional Pylai constructs or constraints exposed through continued review.

> **Guardrail:** Do not infer missing Pylai architecture from Apache Camel, conventional integration-engine design, REST/FHIR conventions, message-broker conventions, or existing implementation code. Resume architectural adjudication with Mark before formalising unresolved behaviour.

## 16. Working Summary

> **Pylai manages Harmonia's external interoperability boundary. A Terma manages the mechanics of interaction with an external system and represents the received raw payload and associated communication metadata as a Communication Information Unit. A transient Diadromí represents the interaction while its logical handover remains in progress. At the Praxis Horos, the Communication Information Unit enters a normal Ergo through Ergo.Ingress. The Pylai-specialised ContextLoader establishes and persists the initial Task/Context with the Communication Information Unit as Task.input(). Developer-defined Ergo.BusinessLogic paired with the Terma then interprets and transforms that information into appropriate Task.output(). The Ergo subsequently completes normally, with its Egress feeding the Terma-associated Petasos.Queue that forms the defined Praxis.PathwaySegment.**

This is the current working Candidate Architecture and is suitable as the basis for later RADS consolidation **after the remaining Pylai nuances have been discussed and adjudicated**.
