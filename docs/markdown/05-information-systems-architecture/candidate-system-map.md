<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Harmonia Candidate Information Systems Architecture — System Map

**Standing: reconstructed and adjudicated candidate; [Step-A boundary](README.md) applies.**
The map groups documented responsibilities and preserves named constructs. It
does not prescribe deployment, map upstream capabilities or select an MVP.
Components are detailed in the [register](component-register.md); source keys
resolve through [Sources](sources.md).

## Structural hierarchy

The following is a software responsibility map. “Grouping” identifies a
documented solution grouping without assigning it additional semantic or
operational authority. “Slice” denotes a documented responsibility area within
a component, not an approved separate process or service. Leaves called
“construct” or “Data Object” are significant contained constructs, not all
independently executable components.

```text
Harmonia Information System — candidate software solution
  Calliope — semantic definitions, shared models and conversion support [C01]
    Canonical model / schema / terminology-binding support
    Event, payload, reason and topic definitions (Data Objects)
    HL7/FHIR converter support
  Themis — policy and authorization [C02]
    Policy evaluation and security contracts
    Security-decision evidence production (not durable evidence ownership)
  Kleio — meaningful audit and provenance evidence [C03]
    Detailed internal decomposition unresolved
  Hestia — documented information-management/preservation grouping
    Mneme — governed active information access and coordination [C09]
      Governed read/write and observation/coordination services [C11]
      Pragma Management — managed task/context/progression information [C28]
        PragmaFactory — creates/initialises Pragma; archetype embodiment [C08]
        Pragma — managed Application Data Object [C07]
      Active representations and working definitions (Data Objects)
    Mnemosyne — authoritative durable establishment/preservation [C12]
      Clinical/FHIR persistence responsibility slice
      Non-FHIR operational persistence responsibility slice
  Energeia — activity/execution architectural subsystem
    Ponos — reusable execution framework/component pattern [C04]
      Ingress dispatch / sequence resolution / execution control
    Ergo — reusable activity pattern and platform-integration contract [C05]
    Ergon — bounded fine-grained task-completion software component [C23]
    Erga — meaningful Ergon collections, assembly, observation and libraries [C24]
    DigitalTwin — Ponos-specialised entity-coordination execution component [C19]
    Behaviour — Ergon-specialised DigitalTwin activity component [C25]
    Dokimasia — Ponos-specialised independent assurance execution component [C20]
    Dokimasia Ergon — Ergon-specialised bounded assurance activity [C26]
    AI Assurance Ergon — candidate Dokimasia Ergon specialisation [C27]
    Praxis — workflow definition/composition/execution design area [C06]
      Definition loader / seeder / composition / checkpoint support
      Exact definition-to-instance model unresolved
  Petasos — internal messaging and recoverable handoff [C10]
    Producer/consumer/destination contracts
    Opaque envelope, duplicate detection and transport observation support
  Pylai — standards-facing interoperability/publication membrane [C14]
    Inbound HL7 interaction responsibility slice
    Outbound HL7 publication/delivery-observation slice
    FHIR registry interaction responsibility slice
  Iris — contextual human interaction [C15]
    Clinical viewer [C16]
    Operations console [C16]
    Administration / provider self-service views [C16]
    Backend-for-frontend mediation [C17]
    Shared presentation/design-system support
  Agora — governed collaboration [C18]
    Identity and collaboration-context mapping
    Space/room lifecycle and membership reconciliation
    Collaboration event/transaction ingress and messaging handoff
    Protocol adapter encapsulation (implementation support)
```

Energeia responsibilities and Mneme-associated Pragma Management follow
[U02](sources.md#u02), correcting the historical grouping in L01–L05/L23.
The Energeia entries are distinct members of the subsystem responsibility
family; their specialisation/contract relationships are shown separately below.
Ponos does not contain Ergo, Ergon, DigitalTwin or Dokimasia. Hestia grouping
is described in [L09/L10/L23](sources.md#l09), not inferred from Maven parents.
Clinical and
operations persistence slices follow documented information responsibilities
in L09/L10, not the database topology. Iris's presentation/mediation distinction
is documented in [L11/L23](sources.md#l11). Agora's functional slices follow
[L12](sources.md#l12), not a promotion of its four Maven modules.

The following do not have a supported structural parent in that tree:

| Construct / solution area | Representation and standing |
| :--- | :--- |
| **Provider Registry [C13]** | Documented cross-component application collaboration spanning Pylai, Themis, Petasos, Ponos/Praxis/Erga, Mneme/Mnemosyne and Iris; not automatically a new enclosing subsystem. [L15](sources.md#l15). |
| **Operator/developer support [C22]** | Documented CLI, activity/sequence authoring, shared design support and telemetry contracts. Tools retain association with their target components; no invented common platform subsystem. [L19/L21/L23](sources.md#l19). |
| **Media Server / runtime terminology service** | No sufficiently defined component boundary found. DocumentReference and binary payloads, compiled bindings and proposed terminology services are retained in the data/features views; named server allocation remains unknown. [L17/L19/L27](sources.md#l17). |

**Paradeigma [C21]** is associated simulation/verification support, outside the
production collaboration hierarchy. Its documented PAS, EMR, LMS, RIS-PAC,
scenario, synthetic-generator/fault-injection and verification responsibilities
are retained. It exercises production interfaces; production components must
not depend on it. Its “digital twins of external applications” wording does not
establish Harmonia Digital Twin archetypes. [L22](sources.md#l22), AGENTS Invariant 1.

<a id="specialisation-and-reuse"></a>

## Specialisation and reuse — three related axes

This view is separate from the containment tree. Axis boxes group kinds of
architectural responsibility; they are not runtime containers. Dashed labelled
arrows run from specialised/reusing construct to its base contract. Ergo is a
conceptual pattern realised/reused by Ergon, rather than a Ponos child or a
second execution framework. Literal programming-language inheritance is not
required. [U02](sources.md#u02) and the
[common specialisation principle](README.md#specialisation-principle) govern.

```mermaid
flowchart LR
    subgraph EX["EXECUTION axis"]
        Ponos["Ponos"]
        Twin["DigitalTwin"]
        Dokimasia["Dokimasia"]
        Twin -.->|specialises / reuses| Ponos
        Dokimasia -.->|specialises / reuses| Ponos
    end
    subgraph AC["ACTIVITY axis"]
        Ergo["Ergo: pattern / contract"]
        Ergon["Ergon: bounded component"]
        Behaviour["Behaviour"]
        DokErgon["Dokimasia Ergon"]
        AI["AI Assurance Ergon: candidate"]
        Ergon -.->|conforms to / reuses contract| Ergo
        Behaviour -.->|specialises / reuses| Ergon
        DokErgon -.->|specialises / reuses| Ergon
        AI -.->|specialises / reuses| DokErgon
    end
    subgraph IN["INFORMATION axis: managed through Mneme"]
        Pragma["Pragma"]
        Stimulus["Stimulus: candidate"]
        Effector["Effector: candidate"]
        Request["Assurance request/input role: candidate"]
        Result["Assurance result/finding role: candidate"]
        Stimulus -.->|specialises / reuses| Pragma
        Effector -.->|specialises / reuses| Pragma
        Request -.->|requires specialisation semantics| Pragma
        Result -.->|requires specialisation semantics| Pragma
    end
```

Assurance request/input and result/finding are candidate semantic roles, not
final names, schemas, cardinalities or lifecycles. Erga's collection/assembly
relationship is distinct from the activity specialisation axis. The key
relationship meanings are:

| Relationship kind | Candidate meaning |
| :--- | :--- |
| **Containment / responsibility family** | Energeia includes distinct execution/activity responsibilities. Mneme includes Pragma Management, which contains PragmaFactory and Pragma. No deployment boundary follows. |
| **Specialisation / reuse** | DigitalTwin and Dokimasia specialise Ponos; Behaviour and Dokimasia Ergon specialise Ergon; AI Assurance Ergon specialises Dokimasia Ergon; Stimulus/Effector are candidate Pragma specialisations. |
| **Activity contract** | Ergon conforms to/reuses Ergo's loading/logic/unloading and platform-integration contract; specialisations retain those facilities. |
| **Collection / assembly** | Erga collates Ergon into executable assemblies/hubs, manages/observes concurrent activity instances and supports coherent libraries/change management; not merely an informal plural. |
| **Execution** | Ponos executes/manages Ergon. DigitalTwin executes specialised Behaviour; Dokimasia executes its assurance Ergon, including explicit AI activity where selected. Execution does not confer information management. |
| **Management** | Mneme / Pragma Management manages Pragma and its specialised representations; Mnemosyne establishes durable state. PragmaFactory creates/initialises representations associated with the archetype meta-concept. |
| **Collaboration** | Tightly coupled Ponos and Ergo/Ergon responsibilities collaborate with Mneme, Petasos, Themis and platform evidence/reporting contracts; they are peers within Energeia. |

DigitalTwin owns the per-Twin queue pattern as a specialised responsibility;
only Twins with pending work become active and are serviced by bounded execution
resources. Many Twins do not require a thread per Twin. Behaviour and specialised
Pragma retain common process-management, assurance, reporting and integration
instead of introducing DigitalTwin equivalents. Dokimasia follows the same
reuse rule subject to its [two terminal constraints](component-register.md#dokimasia-terminal-boundaries).
AI component existence/responsibility is established; detailed architecture is
subject to [CDG-35 discovery](gaps.md#dokimasia-ai-discovery).

## Collaboration map

Arrows describe responsibilities crossing a seam, not containment, mandatory
network hops, exact APIs, or one synchronous sequence. Dotted arrows are
explicitly incomplete candidate relationships. Themis and Kleio participation
applies across governed boundaries; the diagram does not require all activity
to pass through one central runtime service.

```mermaid
flowchart LR
    Iris["Iris: contextual interaction"] -->|governed information access| Mneme["Mneme: active information"]
    Iris -->|governed activity request| Ponos["Ponos: activity execution"]
    Mneme -->|durable establishment and recovery| Mnemosyne["Mnemosyne: durable state"]
    Ponos -->|requests governed information use and change| Mneme
    Mneme -->|provides Pragma information management| PM["Pragma Management"]
    PM -->|manages| Pragma["Pragma and payload information"]
    Twin["DigitalTwin: specialised execution"] -->|uses governed entity context| Mneme
    Twin -->|executes| Behaviour["Behaviour: specialised Ergon"]
    Behaviour -->|uses / produces through governed management| Pragma
    Praxis["Praxis: definition and composition"] -->|supplies sequence intent; model unresolved| Ponos
    Ponos -->|executes / manages| Ergon["Ergon: conforms to Ergo"]
    Erga["Erga: assembly / concurrent observation / libraries"] -->|collects activity components| Ergon
    Ergon -->|uses / produces through governed management| Pragma
    Ponos -->|durable work transfer| Petasos["Petasos: internal handoff"]
    Petasos -->|delivers work| Ponos
    Pylai["Pylai: standards membrane"] -->|internal information requests| Mneme
    Pylai -->|accepted work handoff| Petasos
    Petasos -->|publication request| Pylai
    Agora["Agora: collaboration"] -->|collaboration events| Petasos
    Petasos -->|collaboration requests| Agora
    Calliope["Calliope: semantic reference"] -->|definitions consumed locally| Ergon
    Themis["Themis: policy"] -->|governs operations| Ponos
    Themis -->|governs access| Mneme
    Themis -->|governs collaboration| Agora
    Ponos -->|meaningful evidence under policy and terminal constraints| Kleio["Kleio: evidence"]
    Themis -->|security-significant evidence according to policy| Kleio
    Dokimasia["Dokimasia: specialised independent assurance execution"] -.->|evidence access contract unresolved| Kleio
    Dokimasia -->|executes| DokErgon["Dokimasia Ergon"]
    Dokimasia -->|executes explicit AI activity where selected| AI["AI Assurance Ergon: candidate"]
    DokErgon -->|uses / produces through governed management| Pragma
    AI -->|uses / produces through governed management| Pragma
    Dokimasia -->|uses common Pragma management| PM
    Dokimasia -->|authoritative evidence of assurance activity; no separate own-execution AuditEvent| AssuranceRecord["Governed assurance record/result: detail unresolved"]
```

| Relationship | Documentary basis and limit |
| :--- | :--- |
| Mneme persists through / recovers from Mnemosyne | A01 AX-05, A02 Seam 1; L13 ADR-018/019. Active state is reconstructable; cache acceptance is not durable establishment. |
| Ponos executes Ergon within Ergo; Erga collects; Praxis relationship remains open | U02 corrects L01–L05/L18/L19. Execution, activity contract, assembly and composition are distinct; legacy “four tiers” is not containment. |
| Mneme Pragma Management contains PragmaFactory and Pragma | U01/U02. Factory creates/initialises candidate representations associated with the meta-concept, not an instantiated archetype Information Asset. Detailed contracts/persistence remain unknown; no automatic Praxis equivalence. |
| DigitalTwin specialises Ponos and uses governed entity information | U02 and A02 Seams 2–3. Per-Twin queues are specialised responsibility; bounded resources service pending work. Detailed control/state contract remains unknown. |
| Dokimasia specialises Ponos and executes assurance Ergon | U02 preserves A04 independence; reuse does not give the assured activity control of assurance determination. AI activity boundary is established; detailed architecture CDG-35 remains open. |
| Components use Themis and meaningful evidence/reporting contracts | A01 AX-07–09; L08/L13 ADR-013/016; U02 Dokimasia terminal rules. No separate Dokimasia own-execution AuditEvent or recursive Dokimasia monitoring/assessment; governance and controlled observability remain. |
| Ingress / execution / publication use Petasos handoffs | L13 ADR-014/017, L29/L30. Messaging is opaque; work acceptance, state commit, activity completion and external acknowledgement remain distinct. |
| Agora coordinates with execution through Petasos | AGENTS Invariant 10; L12 §3. No direct Agora-to-Ponos dependency. A detailed information-summary/provider contract is not established. |
| Pylai publishes governed information externally | A01 AX-02/13 and A02 Seams 4/G4. Projection is non-destructive and fail-closed; egress ends management of the emitted representation. Transport packaging does not transfer logical ownership. |

## Preserved model boundaries

The [Task/Pragma candidate](README.md#task-decisions) preserves work versus
undertaking even where both use Pragma. Ergon is behaviour and acquires no
discrete Information Resource. Blueprint, composed behaviour and actual Praxis
execution remain distinguishable where their relationship is unknown. Evidence
does not itself constitute assurance; Dokimasia does not manage its operational
subject. Information custody, active management, durable persistence and domain
authority are not aliases. These limits apply to every source-derived feature
and relationship, with unresolved cases recorded in [CDG](gaps.md).
