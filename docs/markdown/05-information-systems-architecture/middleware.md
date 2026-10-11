<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Internal Information Systems Middleware

**Standing: Harmonia Candidate Information Systems Architecture.**
[Scope](README.md) applies. Internal middleware responsibilities are reconstructed
from documented collaboration intent, principally [L13](sources.md#l13),
[L29](sources.md#l29), [L01](sources.md#l01), [L14](sources.md#l14) and
[L19](sources.md#l19). External integration contracts and runtime products remain
separate architectural concerns.
The [U02–U06 adjudications](sources.md#u02) distinguish execution/activity/information,
semantic selection, persistent handoff and Journey observation. Specialisations
reuse common facilities; the [shared semantic view](pathways-journeys-and-handoff.md)
records detailed boundaries and retained uncertainty.

## Middleware responsibilities

| ID | Internal responsibility / candidate allocation | Information and collaboration contract | Current standing / limits |
| :--- | :--- | :--- | :--- |
| M01 | **Petasos shared persistent/recoverable messaging and handoff** | Persistently manages handoff obligations without information ownership. For Pragma processing, identity/reference plus handoff metadata allows consumer resolution through Mneme; Communication/Notification Events and other forms retain their own semantics | U05, AX-10. Semantic eligibility/consumer selection belongs to Mneme. No universal reference-only format or ActionableTask-only restriction. Acceptance, commit, undertaking completion and external ACK distinct; CDG-16/19/34/37. |
| M02 | **Recoverable backlog, redelivery, rejection, dead-letter and designated replay** | Durable backlog absorbs pressure; consumer acknowledgement/rejection and dead-letter observations support recovery. Deliberate replay starts a new linked attempt at a designated transition, preserving the original. | L13 ADR-015/017, L02/L29. Exact queue sizes/retry limits are implementation detail. Replay is not transport redelivery, arbitrary cache restart or redelivery of an established result, CDG-17. |
| M03 | **Ponos governed execution ingress, dispatch and progression** | Consumer resolves Pragma/context through Mneme and establishes its distinct FulfillmentTask for ActionableTask undertaking; Ponos manages execution and execution retry/re-attempts | U02/U05. Ponos does not convert an opaque transport payload into information it manages, match Subscriptions or own handoff dead-letter recovery. Detailed continuation/acknowledgement CDG-01/03/16/17. |
| M04 | **Ergo/Ergon activity contract and Erga Aggregation** | Context Loading/Unloading resolves/updates governed information through Mneme; bounded Ergon exposes ingress/egress interfaces; Erga aggregates assemblies/concurrent observations/libraries | U02/U04–U06. Associated Energeia responsibilities do not imply Composition. Energeia owns Praxis; Aggregation of Pathway [0..1] retained. Detailed activity/control contracts CDG-01/06. |
| M05 | **Mneme-facing governed access, observation and change coordination** | Read/change services combine governed access, active coordination, authoritative conditional commit and guarded post-commit convergence. Context and semantically distinct outcomes travel with the operation. | L14; A01 AX-05/07/14/15 and A02. Facade is an application access contract, not another persistence owner or mandatory network boundary. Tokens/versions/errors require distinctions, CDG-11/16/31. |
| M06 | **Governed definition/reference availability and Praxis management collaboration** | Energeia Praxis Management Services manage operational definition/configuration; Mneme manages governed representations and availability; Mnemosyne C&G supplies durability; Calliope reference definitions consumed locally | U06 extends L04/L19 historical loader/seeder descriptions without adopting reload/version algorithms. Publishing/Subscription/Pattern semantic management remains Mneme. Detailed definition/lifecycle contracts CDG-01/06/24/36. |
| M07 | **Security/context and meaningful evidence participation** | Trusted context reaches enforcing boundaries; Themis evaluates; significant evidence is preserved according to policy, distinct from transient metrics/checkpoints. Specialisations reuse these common facilities with Dokimasia terminal constraints | A01 AX-07–09, U02, L08/L13/L14. Dokimasia governed record/result is authoritative assurance-activity evidence; no separate own-execution AuditEvent. Evidence-access/representation detail CDG-08/09/13–15/30; no new context wrapper or automatic transport persistence. |
| M08 | **Agora collaboration handoff** | Collaboration requests/events move through Petasos; Agora identity/lifecycle/membership/transaction handling governs collaboration protocol projection. | AGENTS Invariant 10; L12. No direct Agora-to-Ponos dependency or raw Matrix type leakage; summary sources/partial transaction effects unresolved, CDG-19/21–23. |
| M09 | **Harmonia operational observation/monitoring/reporting and presentation mediation** | Energeia owns Harmonia operational management; Journey is common observed progression; Iris presents; component contributors retain their responsibilities; external IT/Ops manages technical platform | U06: R1.x/R2.x Observe → Monitor → Report; no autonomous adaptation. Journey, diagnostics, AuditEvent and findings distinct; detailed freshness/projection/reporting contracts CDG-28/38/39; terminal rules retained. |
| M10 | **Mneme Pragma Management collaboration** | PragmaFactory creates/initialises; Mneme manages Pragma/payload and Publishing/Subscription/Pattern semantic responsibilities; Ponos executes, Ergon behaves, Petasos effects persistent handoff | U02/U03/U05. Governed information state, handoff state, undertaking state and execution state remain distinct. Observation recording confers no delivery obligation ownership. Detail CDG-02/03/04/09/18/36/37. |
| M11 | **DigitalTwin entity-specific pending-work coordination** | DigitalTwin specialises Ponos with the per-Twin queue pattern and executes Behaviour specialising Ergon; uses governed entity context and candidate Stimulus/Effector Pragma information | U02 preserves A02. Only Twins with pending work become active, serviced by bounded execution resources; no thread per Twin. Scheduling, ordering/recovery and control detail remain CDG-07, without queue product/API or topology selection. |
| M12 | **Dokimasia independent assurance collaboration** | Ponos-specialised Dokimasia uses **Praxis as assurance basis and Journey as observed evidence subject**; executes Dokimasia Ergon / AI Assurance Ergon where selected; reuses common activity/information/platform contracts | U02/U06 preserve A04 independence and terminal rules: no recursive Dokimasia assessment or separate own-execution AuditEvent. Detailed independence/evidence access CDG-08/09; AI discovery CDG-35. |
| M13 | **Mneme Publishing / Subscription / Pattern selection** | Provider Pattern filtering governs semantic eligibility; consumer Pattern matching selects available Pragma and eligible recipient/consumer relationships; Petasos establishes/manages concrete persistent handoff to destination | U05/U06. Mneme manages information availability/selection; Petasos owns queue/destination mechanics/state; Ponos executes. Topic remains classification; Pattern/lifecycle CDG-36, fan-out detail CDG-37. |
| M14 | **Pathway-controlled progression** | Ergon PathwayEndpoints, inter-component Segments, encapsulated Traversals, Route = ordered Segment [1..N], Pathway interaction with available Routes; Praxis aggregates Pathway [0..1] | U04/U06. Distinct from Pub/Sub; Journey observes both under Energeia application ownership. Detailed control/definition contracts CDG-01/06; no routing technology selected. |
| M15 | **Journey management, evidence contribution and assurance use** | Energeia Journey Management Services aggregate/reference observations/information from Mneme, Petasos, Ponos/Ergon, DigitalTwin and other governed contributors; Mneme information management; Mnemosyne O&A durable responsibility | U06 resolves owner without owning all contributors or duplicating payloads. Journey evidence subject differs from Praxis assurance basis and Dokimasia inference/findings. Construction/reference/causal/correlation/retention contracts CDG-38; no schema/shard key or universal AuditEvents. |

| M16 | **Application management versus durable persistence collaboration** | Energeia owns Praxis/Journey application semantics; Mneme governs managed access/use; Mnemosyne C&G/O&A establish authoritative durable state under their domain guardrails | U06. C&G coherent governance authority; O&A horizontal partitionability preserves identity/provenance/relationships/meaning. Three semantic domains create no databases, services, network boundaries or transport APIs; physical design CDG-40. |

## Documented collaboration examples

These examples summarise source-described collaborations; they are not approved
end-to-end algorithms or new external contracts.

1. **ActionableTask Pub/Sub handoff:** provider egress presents managed Pragma
   through Mneme. Publishing may block availability; Subscription/Pattern
   matching determines eligible consumers. Petasos persists resulting identity/
   reference handoff obligations. Consumers resolve through Mneme and establish
   distinct FulfillmentTasks when undertaking work; Ponos executes Ergon through
   Ergo's governed context boundary. Exact fan-out contracts remain CDG-37.
2. **Pathway-controlled progression:** defined endpoint interaction and Route(s)
   provide controlled progression. Segment connects components; Traversal
   represents component passage without exposing internal logic. Journey records
   available observations of execution, just as for Pub/Sub or mixed progression.
   Praxis supplies the operational/assurance envelope; detailed control contracts
   remain CDG-01/06.
3. **Inbound acceptance / Registry change:** Pylai captures governed information
   and requests appropriate handoff. Pragma-based processing resolves information
   through Mneme; governed change uses Mnemosyne durable establishment, distinct
   from Petasos acceptance. REC-001 remains: downstream publish/cache failure
   must not yield AA. Exact acceptance, recovery, external status and historical
   direct-store conflicts remain CDG-11/16/32/34.
4. **Destination observations:** Petasos owns operational handoff obligations;
   Pylai contributes external delivery outcomes. Governed Task/Pragma information
   and Journey may represent observations with REC-002 granularity. Exact parent
   progression and observation relationships remain CDG-18; enqueue, send, ACK,
   information commit and work resolution are distinct.
5. **Other messaging / collaboration:** shared Petasos supports Communication/
   Notification Event forms alongside established Agora collaboration requests/
   events. Exact client/event-form relationships remain unadjudicated; their
   formats are not forced into the ActionableTask reference model. Partial publication,
   marker failure and uncertain effects remain CDG-19; no exactly-once contract
   is inferred. Observation may contribute to Journey without compulsory audit.

## Responsibility boundaries

| Concern | Domain05 meaning | Separate downstream concern |
| :--- | :--- | :--- |
| Internal messaging / handoff | Persistent obligation, delivery/recovery state, reference resolution for Pragma processing and broader event-form semantics | Broker product/API, queue bindings, wire format, journal flush/failover machinery; no universal reference-only format. |
| Internal activity collaboration | Execution/context/composition and continuing responsibility | Camel, direct/SEDA/JMS routes, threads, process/runtime topology. |
| Specialised execution/activity collaboration | DigitalTwin/Dokimasia reuse Ponos; Behaviour/Dokimasia Ergon reuse Ergo/Ergon; Erga supports assembly/concurrent activity; terminal assurance rules constrain inherited hooks | Implementation inheritance, Twin scheduling/queue bindings and AI service/model/provider architecture; no choice made here. |
| Managed information collaboration | Application-facing access, active coordination, durable establishment and truthful outcomes | Hot Rod, CAS/locking, HTTP/internal persistence transport, SQL/JPA and cache distribution. |
| External interaction | Execution requests an interaction; Pylai governs representation/conformance and non-destructive publication | Domain06 external/cross-boundary interfaces, standards interaction details and Domain07 connectivity/runtime realisation. |
| Matrix collaboration | Agora governs collaboration context and protocol encapsulation, using internal handoff | Matrix Client-Server/Admin/Application Service transports and homeserver deployment. |

The middleware product inventory in L17 is a technology/support catalogue. It
does not mean that all internal middleware belongs in Domain06 or that each
runtime library/product becomes an Information Systems component. Pylai's
standards-facing boundary is preserved without transferring all generic transport
machinery to it. Database/access and evidence/assurance seams remain separate;
technology availability cannot silently weaken those responsibilities.
