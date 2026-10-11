<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 — Kleio, Themis and Petasos Step-A Adjudication

**Date:** 2026-10-11. **Branch:** `Harmonia-GovernedAccess`.
**Result:** bounded Candidate Architecture correction applied. Step A remains
open; candidate acceptance and Step B remain unstarted. This report is execution
evidence, not architectural authority.

## Authority, scope and axiom consistency

The [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md),
[repository instructions](../../AGENTS.md), [documentation instructions](../../docs/AGENTS.md)
and [Completion Plan](../../docs/markdown/architecture-completion-plan.md) govern.
Current Strategy responsibility/state/transport seams, Domain04 Task/Information
Unit meaning and independent-assurance boundaries were reviewed as constraints;
no upstream capability/information coverage mapping was performed.

The three supplied DesignNotes were available outside the repository in Downloads
and read in full. Their filenames, scope and incorporated dispositions are
recorded as [U07–U09](../../docs/markdown/05-information-systems-architecture/sources.md#u07).
The controlling task requirements and incorporated decisions are self-contained
in RADS. No implementation structure or historical plan supplied missing authority.

| Relevant axioms | Consistency of this correction |
| :--- | :--- |
| AX-01–04 / AX-12 | FHIR AuditEvent remains the audit-event representation; supporting audit structures need not be FHIR resources. Parcel/FHIR Communication analogy supplies no schema equivalence. Safe client contracts preserve semantics without selecting technology. |
| AX-05 / AX-06 / AX-11 | Mneme governed active management differs from Mnemosyne durable establishment. Kleio/Themis application ownership survives persistence delegation; references transfer no source authority. |
| AX-07 | Themis owns policy reasoning/enforcement semantics, with authenticated Identity and local Themis PEP at action-controlling hosts. Default-deny remains; the note's proposed audit exception is raised explicitly below. |
| AX-08 / AX-09 | Significant audit evidence differs from UI telemetry, diagnostics, Journey observations, prepared context and assurance findings. Dokimasia terminal constraints remain unchanged. |
| AX-10 / AX-14 / AX-15 | Durable purpose-defined queues, essential audit capture and valid-context availability preserve failure semantics. Policy denial differs from unavailable determination; uncertain effects remain uncertain. No retry/failover mechanism is inferred. |
| AX-13 / AX-16 / AX-18 | Existing publication, observable progression and non-transfer of domain authority remain; queue/control/audit/assurance roles do not create clinical-work authority. |
| AX-17 | Detailed APIs, schemas, mechanisms and the explicit authorisation exception conflict remain unresolved; whole-candidate acceptance and traceability are not implied. |

## RADS files changed

All nine Domain05 views were reviewed and corrected where necessary:

| File | Correction |
| :--- | :--- |
| [README](../../docs/markdown/05-information-systems-architecture/README.md) | U07–U09 orientation, core decisions, release boundaries and explicit A1–A4/Step-B status. |
| [System map](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Kleio capture/history, Themis PAP/PRP/PDP/context/local PEP and Petasos purpose-defined queues/common Parcel; collaboration arrows preserve application/persistence and source ownership. |
| [Component register](../../docs/markdown/05-information-systems-architecture/component-register.md) | Detailed C02/C03/C10 adjudication; corrected C39/Journey observation/reporting and C12 persistence alignment. |
| [Feature map](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | F03 model/enforcement, F13 durable queue management, F15 authoritative audit capture/history and F19 semantic persistence; F27/38/39 Observe → Report alignment. |
| [Data/persistence](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | D03 Authorisation Model/PEP-Context, D11 by-value Parcel, D12 AuditEvent/supporting evidence; C&G/O&A ownership and independent audit targets without physical topology. |
| [Middleware](../../docs/markdown/05-information-systems-architecture/middleware.md) | M01 queue/Parcel, M07 local enforcement/capture, M09 observation/reporting and M16 semantic persistence; corrected handoff examples and essential-audit boundary. |
| [Gaps](../../docs/markdown/05-information-systems-architecture/gaps.md) | Reclassified CDG-13/14/30 as partially resolved; added detailed Themis/Kleio/Petasos contracts CDG-42–44 and explicit AX-07 conflict; corrected Energeia wording. |
| [Sources](../../docs/markdown/05-information-systems-architecture/sources.md) | DesignNote provenance/dispositions, reconciliation limits and exact legacy supersession scope. |
| [Pathways/Journeys/handoff](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md) | By-value Parcel versus referenced ActionableTask, distinct consumer undertaking, local FORWARD enforcement and Journey-independent audit boundary; Observe → Report alignment. |

The [Completion Plan](../../docs/markdown/architecture-completion-plan.md#33-domain05-five-stage-completion-method)
received only the requested A2/A3 in-progress and Step-B-not-started status
maintenance. No milestone or architecture-completion gate was declared complete.

## Kleio corrections

Kleio owns authoritative Audit Information Management: Capture, audit-event
creation/management, canonical FHIR AuditEvent, immutable history preservation/
retrieval, significant user/system audit activity and historical-context provision/
preparation. Clients report occurrences through Kleio's contract/library/API;
Kleio constructs authoritative resources. No detailed API or mapping was selected.

Existing AuditEvents cannot be corrected, amended, overwritten or replaced.
Subsequent correction/qualification creates a new event referring back as
appropriate while preserving the earlier assertion. Supporting indexes/correlation/
prepared historical views need not be AuditEvents and remain distinguishable from
authoritative evidence. Significant user activity differs from UI clickstream;
UI security governance/assurance is SEP for Kleio. Audit is neither Governance
nor Assurance.

Journey is an important audit source, not the audit boundary. One Journey may
relate to many AuditEvents; audit may occur without Journey participation.
Kleio may consume Journey/Dokimasia outputs and supply historical audit context
without becoming an assurance evaluator or creating Dokimasia own-execution
AuditEvents. Existing terminal rules remain intact.

Source history is referenced by default with sufficient identity, version,
provenance and temporal detail. Only when the source cannot guarantee retrieval
of its audited historical state may necessary evidence be replicated within or
in association with AuditEvent. No FHIR attachment/reference mechanism was chosen.

Required authoritative durable audit capture is a precondition of operation:
if Kleio cannot capture it, Harmonia is DOWN/fails closed. Capture is not best-effort
or reconstructable later from logs/Journey/incidental state. R1.x/R2.x permits
multiple independent authoritative audit persistence targets through Mneme/O&A;
one configured target capable of establishing the required information suffices.
No target is assumed a replica, complete corpus or identical history. Cross-target
harmonisation is SEP, with no convergence, reconnection-reconciliation or unified
history guarantee and no future mechanism selected.

## Themis corrections

Themis owns the Authorisation Model: PAP administers its lifecycle; PRP owns
authoritative repository semantics; PDP reasons over model, authenticated Identity
and context. PRP durability may be delegated through Mneme/Mnemosyne C&G without
a dedicated Themis database or ownership transfer.

ACCESS, CHANGE and FORWARD remain distinct, with destination/context independently
relevant to FORWARD. PDP may produce immediate decisions and/or reusable Criteria.
PEP-Context is the contextual model projection for authenticated Identity and
contains Criteria required by Themis PEP for local decisions; Policy, Criteria
and enforcement decision remain distinct. Complete policy is not distributed
merely for local enforcement. PEP capability/implementation/semantics are
Themis-owned, embedded at action-controlling hosts where appropriate; no central
enforcement component or independent host policy interpretation is introduced.

Valid sufficient context may support in-flight enforcement during PDP loss.
New/renewed/replacement context requires PDP establishment; activity must wait
at that boundary. Unavailable determination is neither permit nor denial.
Delegation may derive authority from existing context for a receiving authenticated
Identity, within Themis policy and with source provenance. Detailed rules and
mechanisms remain deferred to implementation design/governance review.

## Petasos and Energeia corrections

Petasos is first-class durable message/event queue management, owning creation,
operation and status of architecturally purpose-defined queues. Inter-Ergon flow,
communication, notification and other established event sharing are purposes,
without a final taxonomy or broker topology. Every queue carries **PetasosParcel
by value**; associated information is predominantly referenced to governed
information elsewhere. FHIR Communication is conceptual analogy only.

ActionableTask/Pragma stays Mneme-managed; its Parcel may reference it. Petasos
does not transport another consumer's FulfillmentTask and each undertaking
establishes its own distinct FulfillmentTask. The no-payload-copy special case
does not make Parcel reference-only or mandate duplication of all associated
information. Queue state stays Petasos-owned; Energeia observes/reports. Governed
FORWARD uses Themis-owned local PEP and relevant PEP-Context Criteria at Petasos.

R1.x/R2.x Energeia wording is **Observe → Report**, limited to observation and
reporting. Monitor is no separate architectural responsibility; possible later
monitoring mechanisms require justified implementation/feature decomposition.
**R3 Adaptive Load and Resilience Service** remains future direction only.

## Persistence alignment and preserved decisions

Clinical Information, Harmonia C&G and Harmonia O&A remain semantic responsibility
domains. Themis Authorisation Model aligns with C&G; Kleio Audit Information
aligns with O&A. C&G remains coherent without divergent governance authority;
O&A remains horizontally partitionable without a selected key or topology.
Independent audit targets establish no general O&A topology or C&G sharding.

Previously adjudicated Energeia specialisation/reuse, Praxis ownership/envelope,
Pathway [0..1] Aggregation, Journey ownership/evidence role, Mneme management/
PubSub selection, Mnemosyne durable authority, Ponos execution, Ergo/Ergon/Erga,
DigitalTwin, Dokimasia independence/terminal rules and distinct work/undertaking/
handoff/execution states remain. Detailed fan-out identities/lifecycles remain
open; no downstream capability, Information Resource or Step-B coverage is invented.

## Legacy supersession and unresolved matters

**15 section-level wrappers across eight legacy files** were added:

| File | Sections preserved inside new wrappers |
| :--- | :--- |
| [Themis concept](../../docs/concepts/themis.md) | Opening definition; §2 Architectural Definition/fixed gates. |
| [Themis security concept](../../docs/concepts/themis-security.md) | Opening definition; §1 fixed-gate model; §4 decision auditing. |
| [Decision audit](../../docs/security/audit.md) | §1 Overview. |
| [Themis module reference](../../docs/modules/themis.md) | Opening definition; §3 positive ownership. |
| [Petasos messaging concept](../../docs/concepts/petasos-messaging.md) | Opening definition; §1 generic-broker introductory paragraph. |
| [Petasos module reference](../../docs/modules/petasos.md) | Opening definition; §3 positive ownership. |
| [Root messaging architecture](../../docs/architecture.md) | Opening mandate; Standard Message Envelope. |
| [Architecture decisions](../../docs/architecture-decisions.md) | ADR-020 Audit and Provenance subsection. |

Existing wrappers remain unchanged and resolve to corrected RADS. No entire
legacy document is superseded. Implementation catalogues, original tables,
schemas, diagrams and surrounding historical content remain unchanged.

**Explicit retained conflict:** Themis DesignNote §15 proposes an audit-persistence
authorisation exception that conflicts with AX-07's governed-operation security
requirement and is not adjudicated by the controlling task requirements.
[CDG-42](../../docs/markdown/05-information-systems-architecture/gaps.md#themis-detailed-contracts)
records it without adopting a bypass; essential audit capture and default-deny
remain in force. Further bounded adjudication is needed before designing the
detailed audit/authorisation interaction. No upstream correction was made.

Detailed APIs/FHIR mapping/attachments, retention/query/correlation/context
preparation, originating-activity transaction coordination, audit target selection/
write strategy/retry/failover/readiness, policy language/schema/PAP process,
PEP-Context validity/distribution/delegation, Parcel schema/carrier relationships,
queue taxonomy/topology/technology/metrics/delivery/recovery and detailed protocols
remain unresolved in CDG-42–44. Harmonisation is excluded R1.x/R2.x scope, not a
new design obligation. No future reconciliation or R3 adaptation was designed.

## Validation and outcome

**PASS — required architecture suite**, Java 21.0.12.1, offline, five-minute bound:

```bash
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH \
  timeout --signal=TERM --kill-after=10s 300s \
  mvn -o -B test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**BUILD SUCCESS; 90 tests across 11 architecture classes, zero failures/errors/
skips; 27.598 seconds; exit 0.** No timeout/stall. These tests verify existing
executable guardrails; they do not validate runtime implementations of the newly
documented architecture or accept the candidate.

Documentation validation follows the established temporary CommonMark/GFM
process: local links/anchors, table widths, fence closure, explicit-anchor and
register uniqueness, Mermaid delimiter/relationship checks, inbound references,
scope/hash checks, legacy byte preservation and `git diff --check`.

| Verification | Result |
| :--- | :--- |
| All nine Domain05 views, Completion Plan, report and changed legacy Markdown | PASS — CommonMark/GFM parse/render, links/anchors, fences/tables and unique anchors/register IDs. |
| Inbound Markdown/LaTeX references to changed documents | PASS — existing replacement/navigation references resolve; no broken local targets/anchors. |
| Legacy content preservation | PASS — removal of only 15 new wrappers restores all eight files byte-for-byte to HEAD, including existing wrappers and original whitespace. |
| Domains01–04 | PASS — all 76 tracked upstream RADS files retain their baseline SHA-256 values. |
| Implementation/source, tests, configuration and other tracked files | PASS — baseline/scope comparison permits only nine Domain05 views, Completion Plan status, eight legacy wrapper files and this report; no implementation/source modification. |
| Earlier adjudications | PASS — defined-control/selection, execution/activity/information constructs and Dokimasia terminal sections retained; 34 unaffected gap rows remain byte-identical. Fan-out changes clarify Parcel transport without resolving deferred identity/lifecycle. |
| Energeia and release limits | PASS — no old three-verb sequence or observation/monitoring/reporting responsibility remains in Domain05; terminal prohibitions and possible future implementation mechanisms are distinguished. R3 stays deferred. |
| Whitespace and diff scope | PASS — `git diff --check`; only the authorised documentation/report paths changed. |

Final documentation checks cover **434 local references plus 237 inbound
references, 360 anchor references, 38 tables, 117 closed fences and four Domain05
Mermaid blocks checked structurally/textually**. Register IDs remain unique:
C01–C39, F01–F40, D01–D25, M01–M16 and CDG-01–CDG-44.

The first documentation check caught wrappers splitting an existing ASCII
diagram/table; wrapper boundaries were corrected and preservation revalidated.
Five pre-existing trailing-whitespace lines in the legacy ADR file were confirmed
against HEAD and preserved unchanged. No new whitespace defect is permitted.
No rendered-layout or LaTeX publication-build validation is claimed; no dedicated
repository documentation test command was identified in validation discovery.

Evidence is held in `/tmp/domain05-kleio-themis-petasos-20261011/`: architecture
log, baseline hashes, wrapper manifest, validation harness and results. Original
DesignNotes were not modified.

| Completion activity | Final status |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | COMPLETE |
| A2 — Architectural review/adjudication | IN PROGRESS |
| A3 — Bounded correction/reconciliation | IN PROGRESS |
| A4 — Candidate acceptance | PENDING |
| Step B — Capability + Information traceability | NOT STARTED |

Step A remains open. No candidate baseline acceptance, traceability proof,
MVP selection, implementation plan or later-step work is established.
