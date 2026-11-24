<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 — Energeia, Praxis, Journey and Persistence Adjudication

**Date:** 2026-10-11. **Standing:** bounded Step A correction performed; A2 remains
in progress, A3 iterative, A4 pending and Step B not started. This execution
report is not architectural authority or whole-candidate acceptance.

## Task Goal

Correct the Domain05 Candidate Architecture to incorporate the explicitly
adjudicated Energeia operational-management boundary, Praxis/Journey ownership
and management services, assurance basis/evidence distinction, and Mnemosyne
Clinical/C&G/O&A semantic persistence responsibilities and scaling guardrails.
Refine residual Mneme eligibility-versus-concrete-destination wording while
preserving the previous Pathway/Journey/Publish-Subscribe/Petasos adjudication.

## Task Activity

Reviewed root [AGENTS.md](../../AGENTS.md), [docs instructions](../../docs/AGENTS.md),
the [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md)
and [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md).
This is Domain05 Step A architectural review/correction, outside convergence/runtime
implementation execution; no implementation-sequencing milestone was commenced.

Reviewed all nine Domain05 views and corrected them together:

- [README](../../docs/markdown/05-information-systems-architecture/README.md): U06 adjudication and exact Step-A standing.
- [System map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md): discrete Energeia, direct responsibilities, explicit Association/Aggregation/Specialisation/Realisation and collaboration diagrams.
- [Component register](../../docs/markdown/05-information-systems-architecture/component-register.md): new justified Energeia C39, revised Praxis C06, Journey C35, Mnemosyne C12 and assurance allocation.
- [Feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md): F11 Praxis Management Services, F38 Journey Management Services, F39 operational observation/monitoring/reporting, F40 deferred R3 direction; related allocations reconciled.
- [Data/Persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md): application ownership, three semantic domains, classification tests/pattern, C&G coherence and O&A partitionability/Journey locality.
- [Pathway/Journey/handoff](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md): retained canonical view, Praxis envelope, Journey ownership/reference boundary, assurance diagram and precise semantic-selection/concrete-handoff distinction.
- [Middleware](../../docs/markdown/05-information-systems-architecture/middleware.md): reconciled management, selection, handoff, execution, assurance and persistence collaborations; M16 records application-management/persistence seam without prescribing a transport.
- [Gaps](../../docs/markdown/05-information-systems-architecture/gaps.md): resolved high-level ownership/meaning versus retained detail; CDG-39 decomposition/possible upstream omission, CDG-40 downstream physical persistence and CDG-41 explicitly deferred R3.
- [Sources](../../docs/markdown/05-information-systems-architecture/sources.md): U06 provenance, current dispositions and precise legacy supersession assessment.

Relevant upstream review covered the Domain04 Task/Work model (including
ActionableTask, FulfillmentTask and TaskOutcome), Information Unit metamodel,
observable-information/domain-meaning guardrail, independent-assurance finding
and historical-truth/lifecycle principles, together with Strategy's logical
responsibility boundaries. These constrain the correction; no capability or
information realisation traceability was performed and no upstream files changed.

Read all five supplied DesignNotes: Energeia/DigitalTwin/Dokimasia specialisation;
Pragma Management responsibility; Pathways/Journeys/assurance representation;
Mneme publication/subscription/Petasos handoff; and the new Energeia operational
management/Praxis responsibility note. U06 records the new note **and explicit
task decisions**: Journey ownership and persistence-domain guardrails extend the
note's wording. Architectural meaning is incorporated into RADS; external note
access is not required to understand the corrected architecture. Notes unchanged.

### Axiom consistency

| Relevant authority | Consequence preserved by this correction |
| :--- | :--- |
| AX-01–04 | Harmonia operational semantics and native models precede technology; no engine, database, schema or deployment selection. |
| AX-05/11/12 | Energeia application responsibility uses Mneme governed management; Mnemosyne alone establishes durable state. No direct persistence application path or active-state promotion. |
| AX-06/07 | Persistence/observation do not establish originating authority; established security context and Themis remain controlling. Interface/Topic configuration grants no bypass. |
| AX-08/09 | Meaningful durable evidence is distinct from transient mechanics; O&A classification does not mandate persistence of every observation, diagnostic or runtime field. Kleio and Dokimasia terminal evidence rules remain intact. |
| AX-10/14–16 | Recoverable handoff, information/undertaking/execution states and observable progression remain distinct; O&A partitionability preserves identity/provenance/relationships/meaning; absence of observation does not prove non-occurrence. |
| AX-13 | Pylai retains external contract/publication responsibility; Praxis interface configuration does not extend Harmonia management beyond egress. |
| AX-17/18 | Candidate, unresolved and deferred matters remain explicit; application ownership, information management, persistence and originating domain authority are distinct. No inferred Composition or upstream requirement. |

No new axiom conflict was identified. Historical engine-only/two-domain/topology
claims are explicitly superseded or retained as historical/supporting conflicts,
without changing implementation. No executable implementation is claimed to
realise the new services or persistence guardrails merely because tests pass.

## Architectural Decisions Incorporated

1. **Energeia is a discrete Software Component**, responsible for Harmonia
   operational management, with its associated execution/activity constructs
   retained and their specialisation/reuse unchanged.
2. **Energeia owns Praxis**, the higher-order expected/permitted operational and
   assurance envelope, and provides candidate **Praxis Management Services**.
   Candidate responsibilities cover definition/registration, discovery/retrieval,
   operational/interface configuration, Topic inclusion/exclusion policy,
   Pathway aggregation, assurance-rule association and Praxis-wide constraints.
3. **Praxis aggregates Pathway [0..1]** and has a working Aggregation relationship
   to Journey with cardinalities unresolved. Praxis can govern Pub/Sub activity
   without a Pathway. Composition/lifecycle coupling is not inferred.
4. **Energeia owns Journey** and candidate **Journey Management Services**.
   Journey remains governed observed progression, aggregating/referencing
   information from Mneme, Petasos, Ponos, Ergon, DigitalTwin and other governed
   contributors without owning or indiscriminately duplicating their information.
5. **Praxis is the assurance basis; Journey is the evidence subject.** Applicable
   Pathway expectations contribute through Praxis. Dokimasia performs independent
   governed interpretation and produces findings/results; inference does not
   replace observations. Its two terminal-boundary rules are unchanged.
6. **Energeia observes/monitors/reports Harmonia as Harmonia**, including work-to-be-
   done/being-done/done, outstanding tasks/undertakings, Journey progression,
   stalled/incomplete activity, Praxis context and abnormal operational behaviour.
   External IT/Ops manages technical platform operation; Iris presents views.
7. **R1.x/R2.x: Observe → Monitor → Report**, without autonomous workload,
   route/Pathway/PubSub/Praxis, resilience or capacity adaptation from observations.
   Existing explicitly governed execution/recovery authority is retained.
8. **R3 Adaptive Load and Resilience Service** remains future/deferred direction:
   Observe → Analyse → Decide → Adapt with feedback. No adaptive-control design.
9. **Mneme determines semantic eligibility and eligible recipient/consumer
   relationships; Petasos establishes/manages concrete persistent handoff to the
   destination; Ponos manages execution.** Queue binding/destination mechanics
   and handoff state are not assigned to Mneme.
10. **Mnemosyne has Clinical Information, Harmonia Configuration & Governance
    (C&G), and Harmonia Operations & Assurance (O&A) persistence responsibility
    domains.** They are semantic responsibilities, not prescribed databases,
    services, schemas, workloads or products. Clinical architecture is retained.
11. **C&G presents coherent governed information without semantic sharding into
    independent/divergent authority.** Replication, caching, clustering, high
    availability, read replicas and distributed storage remain permissible without
    selecting mechanisms.
12. **O&A permits horizontal partitioning** without changing information identity,
    provenance, relationships or governed meaning. Partitionability is a property;
    no multi-shard R1.x/R2.x deployment or shard key is required/selected.
13. **Journey locality and efficient reconstruction/use are objectives**, with
    comparatively small discrete Journey information referencing substantial
    distributed activity/evidence, without prescribing duplication or algorithms.
14. **Application ownership versus persistence responsibility stays distinct**:
    Energeia owns Praxis/Journey management, Mneme manages governed information,
    and Mnemosyne C&G/O&A provides durability. Operational observation does not
    transfer ActionableTask/FulfillmentTask/Pragma information management.

The previous endpoint/element/segment/traversal/Route/Pathway meanings, Topic
classification, Publishing/Subscription/Pattern semantics, Petasos generality,
Ponos retry versus Petasos redelivery, four-state distinction, bounded fan-out
principle, Journey factual standing and Dokimasia terminal boundaries are retained.

## Retained Uncertainty

- Praxis lifecycle: versioning, activation/deactivation, replacement, retirement
  and effects on in-flight activity.
- Detailed Praxis representation, Topic inclusion necessity, interface model,
  assurance-rule attachment, definition-to-instance equivalences and detailed
  Praxis-to-activity/Pathway/Journey relationships beyond established Aggregation.
- Detailed Journey representation/construction, aggregation/reference schema,
  PathwayElement relationships, causal/correlation/evidence-reconstruction
  algorithms, inference qualification, lifecycle/retention and consumption.
- Detailed Energeia Feature/Service/internal-construct/subordinate-Component
  decomposition, monitoring thresholds and reporting contracts.
- C&G physical persistence implementation/topology; O&A physical partition/shard
  model; shard key; replication and consistency mechanisms.
- R3 adaptive-control architecture, policy model, algorithms, load/resilience
  mechanisms, infrastructure integration and AI decision mechanism — deferred.
- Existing detailed Pub/Sub fan-out reference/copy/version, identity/morph,
  targetDestination, per-consumer state/concurrency/isolation, lineage and Journey
  contracts; no copy-per-subscriber or shared mutable processing model selected.
- Existing unrelated documented gaps and upstream assurance-information deferrals.

Possible upstream omission of operational-housekeeping demand is recorded for
later review in CDG-39; it is not an established upstream defect or coverage gap.
No missing Business Architecture or capability requirement is manufactured.
Deliberately deferred physical/R3 design is not classified as missing R1.x/R2.x
high-level architecture.

## Legacy Documentation Assessment

### Canonical Documentation Assessment

Relevant legacy architectural material was found and reconciled within this
bounded Domain05 task. **11 targeted wrappers across seven files** preserve
original content and identify precise RADS replacements:

| Legacy document / affected section | RADS replacement |
| :--- | :--- |
| [Mnemosyne concept](../../docs/concepts/mnemosyne.md): opening definition and §2 Architectural Definition including two-service diagram | [Persistence domains](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#persistence-responsibility-domains), [C&G coherence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#cg-governance-space), [O&A partitionability](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#oa-partitionability). |
| [Hestia persistence](../../docs/concepts/hestia-persistence.md): §3.2 Operations Scope bullet | Same semantic domains/classification and scaling guardrails. Framework/database detail unchanged. |
| [Hestia module](../../docs/modules/hestia.md): opening two-domain description | Same semantic responsibility domains. Module/classes/configuration catalogue unchanged. |
| [Praxis concept](../../docs/concepts/praxis.md): §1 Architectural Rationale paragraph | [Praxis envelope/ownership](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md#praxis-operational-context) and retained detailed contracts. Existing wrappers unchanged. |
| [Energeia chapter](../../docs/latex-legacy/chapters/06-workflow-energeia.tex): opening engine/Pragma-ownership paragraph and Praxis sequence-engine definition | [Energeia C39](../../docs/markdown/05-information-systems-architecture/component-register.md#c39), [explicit relationships](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md#specialisation-and-reuse), Praxis envelope. |
| [Praxis appendix](../../docs/latex-legacy/chapters/appendix-praxis-workflow.tex): Overview opening allocation and Praxis Paradigm component-topology paragraphs | Energeia C39, Praxis envelope/ownership and retained detailed-contract uncertainty. |
| [Persistence chapter](../../docs/latex-legacy/chapters/10-postgresql-persistence.tex): opening two-module description and Database Separation lead-in | Three semantic persistence domains and coherent C&G/O&A partitionability guardrails. Physical catalogue untouched. |

No whole legacy document is marked superseded. Existing wrappers and enclosed
original content remain unchanged. Mixed Hestia topology/write-behind diagrams,
physical cluster/schema/catalogue descriptions, tutorials and dynamic-route
mechanisms retain historical/supporting or unresolved standing; they are not
adopted as current architecture. Simulator journey terminology concerns scenarios
and supplies no Journey ownership decision. Historical memory and execution
artefacts are navigation, not architectural authority.

Further legacy reconciliation remains separately bounded, including pre-existing
access/write-behind, schema/history/evidence and collaboration-store conflicts.
All architectural meaning adjudicated for this task is available in Domain05
RADS without the external DesignNotes; this is not a full legacy consolidation
or Domain Completion Gate assessment.

## Validation

### Architecture suite — PASS

Executed with **OpenJDK 21.0.12.1** and a 600-second command timeout:

```bash
JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH \
timeout --signal=TERM --kill-after=15s 600s \
mvn -o test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**90 tests across 11 architecture classes; 0 failures, 0 errors, 0 skips.
BUILD SUCCESS; elapsed 28.817 seconds.** Includes isolation/layering/security,
Provider Registry, Pylai publication, governed-write and Mnemosyne authoritative-
persistence tests. This verifies existing executable guardrails, not new
service implementations or architectural acceptance.

The initial online command stopped before test execution after 1.864 seconds:
Maven could not write `resolver-status.properties` in the read-only local
repository. This was a dependency-resolution environment problem, not a semantic
test failure, timeout or stall. Offline execution used the available cached
artifacts and completed the same suite. Logs were captured under `/tmp`.

### Documentation and preservation checks

| Check | Result and evidence |
| :--- | :--- |
| Local Markdown links and anchors, plus LaTeX wrapper references | **PASS** — 332 local references and 185 Markdown anchor targets across all nine Domain05 views, seven changed legacy files and this report; no broken targets or duplicate explicit anchors. |
| Register identifiers | **PASS** — unique definitions: 39 C, 40 F, 25 D, 16 M and 41 CDG identifiers. Existing identifiers retained; added C39, F39–40, M16, CDG-39–41 and U06 reflect justified responsibilities/deferrals. |
| Mermaid/text diagram consistency | **PASS** — four Mermaid blocks checked for structural delimiters/quotes and manually reviewed against text. Both collaboration/assurance diagrams use Praxis basis, Journey observed subject and Pathway Aggregation [0..1]. No Composition inferred. No rendered-layout validation was performed. |
| Legacy wrapper preservation | **PASS** — removing exactly the 11 new wrappers reconstructs all seven pre-task legacy files byte-for-byte against Git HEAD, including every existing wrapper and original enclosed section. |
| Previous adjudication preservation | **PASS** — detailed fan-out section and full Dokimasia terminal-boundary section remain byte-identical. Semantic review retains control constructs, Topic classification, four-state distinction, general Petasos messaging and execution-retry/redelivery separation. |
| Gap preservation | **PASS** — 31 unaffected pre-existing gap rows remain exactly unchanged; affected high-level uncertainties are narrowed rather than erased, with physical/R3 deferrals distinguished from missing architecture. |
| Scope | **PASS** — 16 tracked documentation files changed plus this new report; Domains01–04, governance/programme plans, source/implementation, tests, build/deployment configuration and external DesignNotes unchanged. |
| Excluded design/acceptance claims | **PASS** — no selected technology, physical schema/topology, partition/shard key, replication/consistency algorithm, R3 control design, Step-B realisation traceability, MVP/implementation plan or whole-candidate acceptance. Existing technology/schema names remain expressly historical/supporting. |
| Whitespace | **PASS** — `git diff --check`. |

### Semantic cross-view review — PASS

Component, feature, data, middleware, overview/system-map, semantic, source and
gap views were reconciled to the same allocations:

| Responsibility / construct | Consistent allocation and limit |
| :--- | :--- |
| Energeia | Discrete operational-management component; direct Praxis/Journey management plus R1.x/R2.x observation/monitoring/reporting. Associated execution/activity constructs retain their separate responsibilities. |
| Praxis | Energeia application ownership/Management Services; expected/permitted operational envelope and assurance basis; Mneme information management; Mnemosyne C&G durable responsibility. |
| Journey | Energeia application ownership/Management Services; governed observed progression and evidence subject; Mneme information management; Mnemosyne O&A durable responsibility; contributors retain referenced information ownership. |
| Mneme | Managed access/information, Pragma and Pub/Sub semantic eligibility/selection; no execution or concrete queue/destination/handoff ownership. |
| Petasos | Concrete persistent handoff obligations and destination/recovery mechanics/state; broader messaging role retained; no payload-information or execution ownership. |
| Ponos / Ergon / Erga | Execution/progression and retry; bounded behaviour under Ergo; meaningful activity Aggregation/assembly/observation respectively. None acquires Praxis/Journey application ownership through use. |
| Dokimasia | Independent governed assurance using Praxis basis and Journey observed evidence; findings/inference distinct from Journey facts; terminal rules preserved. |
| Mnemosyne | Sole authoritative durable establishment across Clinical/C&G/O&A semantic domains; application semantics stay with their owners; no inferred physical topology. |
| Iris / external IT/Ops | Iris presents governed operational views; external IT/Ops manages technical substrate; neither replaces Energeia Harmonia operational-management responsibility. |

No responsibility is allocated differently across the corrected views. Detailed
representation, lifecycle, decomposition and physical-realisation uncertainty
remains visible with consistent standing; passing checks does not accept the
whole Candidate Architecture.

## Target Outcome

> **Energeia is a discrete Harmonia Software Component responsible for Harmonia's operational management. It owns Praxis as the definition of expected/permitted operational context and Journey as the governed representation of observed operational progression, providing Praxis Management Services and Journey Management Services. Praxis provides the basis for assurance; Journey provides its observed evidence subject. For R1.x/R2.x Energeia observes, monitors and reports Harmonia operation without autonomous adaptation, while R3 retains a future Adaptive Load and Resilience direction. Mnemosyne provides distinct persistence responsibilities for Clinical Information, Harmonia Configuration & Governance, and Harmonia Operations & Assurance: C&G forms a coherent non-semantically-sharded governance information space, while O&A is designed to permit horizontal partitioning without altering information identity, provenance, relationships or governed meaning.**

```text
A1 — Candidate reconstruction                  COMPLETE
A2 — Architectural review/adjudication         IN PROGRESS
A3 — Candidate correction                      ITERATIVE
A4 — Candidate acceptance                      PENDING
Step B — Capability/Information traceability   NOT STARTED
```

This completes the authorised correction, not Domain05 Step A acceptance. No
Step B traceability, MVP analysis, implementation planning, source-code work,
physical database/shard-key design, deployment design or R3 algorithm work was
commenced. Domains01–04, axioms and programme plans were not modified.
