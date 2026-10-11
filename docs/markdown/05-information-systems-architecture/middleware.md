<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Candidate Internal Information Systems Middleware

**Standing: Harmonia Candidate Information Systems Architecture.**
[Scope](README.md) applies. Internal middleware responsibilities are reconstructed
from documented collaboration intent, principally [L13](sources.md#l13),
[L29](sources.md#l29), [L01](sources.md#l01), [L14](sources.md#l14) and
[L19](sources.md#l19). External integration contracts and runtime products remain
separate architectural concerns.
The [U02–U11 adjudications](sources.md#u02) distinguish execution/activity/information,
semantic selection, persistent handoff and Journey observation. Specialisations
reuse common facilities; the [shared semantic view](pathways-journeys-and-handoff.md)
records detailed boundaries and retained uncertainty.

## Middleware responsibilities

| ID | Internal responsibility / candidate allocation | Information and collaboration contract | Current standing / limits |
| :--- | :--- | :--- | :--- |
| M01 | **Petasos durable message/event queue management** | Creation, operation and operational status of purpose-defined queues for inter-Ergon flow and other architecturally established communication/notification/event sharing; every queue carries PetasosParcel by value, associated information predominantly by reference | U05/U09, AX-10. First-class shared capability; Mneme selects information and retains ActionableTask/Pragma management. Petasos Parcel reference supports own distinct consumer FulfillmentTask, not transported other-consumer undertaking. Queue state Petasos-owned; Energeia observes/reports. Detail CDG-16/19/34/37/44. |
| M02 | **Recoverable backlog, redelivery, rejection, dead-letter and designated replay** | Durable backlog absorbs pressure; consumer acknowledgement/rejection and dead-letter observations support recovery. Deliberate replay starts a new linked attempt at a designated transition, preserving the original. | L13 ADR-015/017, L02/L29. Exact queue sizes/retry limits are implementation detail. Replay is not transport redelivery, arbitrary cache restart or redelivery of an established result, CDG-17. |
| M03 | **Ponos governed execution ingress, dispatch and progression** | Consumer resolves Pragma/context through Mneme and establishes its distinct FulfillmentTask for ActionableTask undertaking; Ponos manages execution and execution retry/re-attempts | U02/U05. Ponos does not convert an opaque transport payload into information it manages, match Subscriptions or own handoff dead-letter recovery. Detailed continuation/acknowledgement CDG-01/03/16/17. |
| M04 | **Ergo/Ergon Context Loader/Unloader and Erga Aggregation** | Ergo declares likely non-exhaustive set; Mneme Context Load materialises; Ergo Loader exposes values; bounded logic acts/produces Pragma Output; Unloader returns results; Mneme Context Save manages consequences | U10 refines U02/U04–U06; Loader/Unloader differ from Mneme services and Unloader is not persistence. Bounded Adhoc acquisition supported; Erga collection and Praxis Pathway Aggregation unchanged. Detail CDG-45/01/06. |
| M05 | **Mneme Context/Pragma-facing four-service surface and supporting coordination** | Context Load, Context Save, Adhoc Resource Access, Adhoc Search; controlled concurrent active use, Mnemosyne authoritative persistence outcomes and post-commit convergence | U10/U11 and AX-05/07/11/12/14/15. C11 facade/carrier intent is supporting, not extra exposed services/network boundary; Context/resource versions and active generation distinct. Detail CDG-31/45/47/48. |
| M06 | **Governed definition/reference availability and Praxis management collaboration** | Energeia Praxis Management Services manage operational definition/configuration; Mneme manages governed representations and availability; Mnemosyne C&G supplies durability; Calliope reference definitions consumed locally | U06 extends L04/L19 historical loader/seeder descriptions without adopting reload/version algorithms. Publishing/Subscription/Pattern semantic management remains Mneme. Detailed definition/lifecycle contracts CDG-01/06/24/36. |
| M07 | **Themis distributed enforcement and Kleio Audit Capture** | Themis PAP/PRP/PDP manages/reasons over Authorisation Model; PEP-Context supplies Identity-specific Criteria to Themis-owned local PEP at action-controlling host. Significant client occurrences use Kleio contract; Kleio constructs immutable FHIR AuditEvent | U07/U08, AX-07–09/14. ACCESS/CHANGE/FORWARD distinct; context projection is not complete policy distribution. Valid sufficient context may continue during PDP loss; required new context must wait, unavailability is not denial. Required durable audit capture essential; no Journey prerequisite. Dokimasia terminal rules retained. Detail CDG-42/43. |
| M08 | **Agora collaboration handoff** | Collaboration requests/events move through Petasos; Agora identity/lifecycle/membership/transaction handling governs collaboration protocol projection. | AGENTS Invariant 10; L12. No direct Agora-to-Ponos dependency or raw Matrix type leakage; summary sources/partial transaction effects unresolved, CDG-19/21–23. |
| M09 | **Harmonia operational observation/reporting and presentation mediation** | Energeia owns Harmonia operational management; Journey is common observed progression; Iris presents; component contributors retain their responsibilities; external IT/Ops manages technical platform | U06: R1.x/R2.x Observe → Report; no autonomous adaptation. Journey, diagnostics, AuditEvent and findings distinct; detailed freshness/projection/reporting contracts CDG-28/38/39; terminal rules retained. |
| M10 | **Mneme Pragma Management and new Content collaboration** | Factory initialises; Pragma expresses inputs/outputs; Ergon changes Context values and produces new Content as Pragma Output; Mneme manages resulting consequences and retained Pub/Sub semantics | U10 extends U02/U03/U05. Outputs and changed Context values distinct; Ponos executes, Petasos hands off; recording observations does not own handoff obligations. Output detail CDG-45, existing state/fan-out CDG-03/18/37. |
| M11 | **DigitalTwin entity-specific pending-work coordination** | DigitalTwin specialises Ponos with the per-Twin queue pattern and executes Behaviour specialising Ergon; uses governed entity context and candidate Stimulus/Effector Pragma information | U02 preserves A02. Only Twins with pending work become active, serviced by bounded execution resources; no thread per Twin. Scheduling, ordering/recovery and control detail remain CDG-07, without queue product/API or topology selection. |
| M12 | **Dokimasia independent assurance collaboration** | Ponos-specialised Dokimasia uses **Praxis as assurance basis and Journey as observed evidence subject**; executes Dokimasia Ergon / AI Assurance Ergon where selected; reuses common activity/information/platform contracts | U02/U06 preserve A04 independence and terminal rules: no recursive Dokimasia assessment or separate own-execution AuditEvent. Detailed independence/evidence access CDG-08/09; AI discovery CDG-35. |
| M13 | **Mneme Publishing / Subscription / Pattern selection** | Provider Pattern filtering governs semantic eligibility; consumer Pattern matching selects available Pragma and eligible recipient/consumer relationships; Petasos establishes/manages concrete persistent handoff to destination | U05/U06. Mneme manages Context/Pragma availability/selection; Petasos owns queue/destination mechanics/state; Ponos executes. Topic remains classification; Pattern/lifecycle CDG-36, fan-out detail CDG-37. |
| M14 | **Pathway-controlled progression** | Ergon PathwayEndpoints, inter-component Segments, encapsulated Traversals, Route = ordered Segment [1..N], Pathway interaction with available Routes; Praxis aggregates Pathway [0..1] | U04/U06. Distinct from Pub/Sub; Journey observes both under Energeia application ownership. Detailed control/definition contracts CDG-01/06; no routing technology selected. |
| M15 | **Journey management, evidence contribution and assurance use** | Energeia Journey Management Services aggregate/reference observations/information from Mneme, Petasos, Ponos/Ergon, DigitalTwin and other governed contributors; Mneme Context/Pragma management; Mnemosyne O&A durable responsibility | U06 resolves owner without owning all contributors or duplicating payloads. Journey evidence subject differs from Praxis assurance basis and Dokimasia inference/findings. Construction/reference/causal/correlation/retention contracts CDG-38; no schema/shard key or universal AuditEvents. |
| M16 | **Application management versus durable persistence collaboration** | Energeia owns Praxis/Journey; Themis owns Authorisation Model/PRP semantics; Kleio owns Audit Information Management. Mneme provides governed Context/Pragma access; Mnemosyne C&G/O&A supplies authoritative durable state | U06–U08. Themis PRP may use Mneme/C&G; Kleio uses Mneme/O&A with independent authoritative audit targets permitted and R1.x/R2.x harmonisation SEP. C&G coherent authority and O&A partitionability create no prescribed databases/services/topology. Physical design CDG-40; audit mechanisms CDG-43. |
| M17 | **Exceptional resource delivery through common execution** | Internal Mneme Resource Harmonisation Manager causes appropriate Harmonisation Ergon work through established work/execution contract and common Ponos | U10. New governed work, no resumed originating Ergon or duplicate execution; approximately 5% or less at worst is design expectation, not SLA; normal path efficient; detail CDG-46. |
| M18 | **Mnemosyne persistence oversight and local enforcement** | Transaction/service oversight, shard management, compartment validation/reapplication, persistence logging and authoritative reporting; Mneme receives established/uncertain outcomes; Themis owns hosted PEP | U11. Persistence API response not automatically Harmonia outcome; semantic compartment/sharding constraints retained; Energeia R1.x/R2.x Observe → Report receives state without management transfer; detail CDG-47. |
| M19 | **Adhoc Search / authoritative Search Management / Research Metadata** | Mneme provides activity-facing search; Mnemosyne searches authoritative managed persistence, optionally using capability and distinct AI-derived Research Metadata; derivation outside Mnemosyne through appropriate Harmonia execution | Explicit U11 task decisions. Source Content unchanged; cache not complete searchable space; provenance/confidence/schema/query/index detail unresolved CDG-48. No physical technology or deriving-component selection. |

## Documented collaboration examples

These examples summarise source-described collaborations; they are not approved
end-to-end algorithms or new external contracts.

1. **ActionableTask Pub/Sub handoff:** provider egress presents managed Pragma
   through Mneme. Publishing may block availability; Subscription/Pattern
   matching determines eligible consumers. Petasos queues by-value PetasosParcels
   carrying ActionableTask references and communication/handoff metadata for the
   resulting persistent obligations. Consumers resolve through Mneme and establish
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
   events. All queues use the common by-value PetasosParcel datatype. Associated
   information is predominantly by reference; event-purpose/schema relationships
   remain unresolved without imposing the ActionableTask no-payload-copy rule. Partial publication,
   marker failure and uncertain effects remain CDG-19; no exactly-once contract
   is inferred. Observation may contribute to Journey without making every observation an
   AuditEvent; required significant audit information must nevertheless be captured.

## Context lifecycle and persistence collaboration

The [preferred activity sequence](context-and-persistence-management.md#ergon-information-access)
keeps Mneme Context Load/Save distinct from Ergo Loader/Unloader. Business logic
primarily uses materialised values, may obtain runtime information through the two
Adhoc services, and returns changed working values separately from new Content as
Pragma Output. Unloading is not persistence. Mneme manages subsequent consequences;
Mnemosyne establishes/reports authoritative persistence outcomes.

Exceptional resource delivery causes **new governed Harmonisation Ergon work**
through the existing work/execution contract and common Ponos capability. The
internal manager performs no domain harmonisation and embeds no execution framework.
Search uses authoritative persistence; AI-derived Research Metadata is managed by
Mnemosyne but derived outside it. These logical collaborations select no runtime
boundary, physical transaction, query/index or AI technology.

## Local forwarding enforcement and essential audit capture

Petasos controls its queue operation and therefore hosts enforcement when that
operation is a governed **FORWARD**. Its applicable local decision SHALL use
Themis-owned PEP capability and relevant PEP-Context Criteria. Destination/context
may affect FORWARD independently of ACCESS. No Petasos/Themis protocol is selected.
Queue status stays Petasos-owned while Energeia observes/reports it.

Kleio's contract accepts client-reported significant occurrences and constructs
authoritative FHIR AuditEvent; clients do not have to construct resources. Journey
and Dokimasia outputs may be audit sources and Kleio may supply historical context
to Dokimasia, preserving evidence/view distinctions and terminal rules. Journey
participation is not required. **Harmonia SHALL NOT perform an auditable activity
unless its required audit information can be authoritatively and durably captured.**
If Kleio cannot capture it, Harmonia is DOWN. One configured independent audit
target capable of establishing the required information suffices in R1.x/R2.x;
none means fail closed. Capture cannot be deferred for reconstruction from logs/
Journey. Transaction/retry/failover and target selection remain unresolved.

## Responsibility boundaries

| Concern | Domain05 meaning | Separate downstream concern |
| :--- | :--- | :--- |
| Internal messaging / handoff | Purpose-defined durable queues and operational status; common by-value PetasosParcel with predominantly referenced information; handoff/recovery and ActionableTask resolution through Mneme | Broker product/API, queue bindings/taxonomy, Parcel/wire schema, journal/failover and delivery mechanisms; no reference-only transport or generic broker-derived semantics. |
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
