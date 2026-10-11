<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Domain05 Mneme / Mnemosyne Architectural Adjudication and Correction

**Date:** 2026-10-11. **Scope:** Domain05 Step A — bounded A2 architectural
review/adjudication and A3 correction. This report records execution evidence;
it is not architectural authority or Candidate Architecture acceptance.

## Outcome and programme boundary

The canonical Domain05 candidate now represents **Mneme as Context and Pragma
Manager**, **Mnemosyne as Persistence Manager**, their activity/persistence/search
boundary, first-class Search and Research Metadata Management, and Energeia
R1.x/R2.x **Observe → Report**. Incorporated architectural meaning is self-contained
in RADS; external DesignNotes remain provenance.

| Activity | Status after this bounded correction |
| :--- | :--- |
| A1 — Candidate reconstruction/documentation | COMPLETE; earlier outcome preserved |
| **Step A overall** | **OPEN** |
| A2 — Architectural review/adjudication | IN PROGRESS |
| A3 — Bounded correction/reconciliation | IN PROGRESS |
| A4 — Candidate acceptance | PENDING; not performed |
| **Step B — Capability / Information traceability** | **NOT STARTED** |

No upstream coverage, gap/excess/MVP analysis, final baseline, implementation
planning or implementation change is established. The Architecture Completion
Plan's existing programme status remains unchanged.

## Authority and axiom consistency

Read repository `AGENTS.md`, `docs/AGENTS.md`, the Architecture Completion Plan,
Architectural Axioms and all current Domain05 RADS views. The two supplied
DesignNotes were read in full and are recorded as **U10/U11** in the
[source register](../../docs/markdown/05-information-systems-architecture/sources.md#u10).
Existing repository authority, including the Domain04 governed logic boundary
and Management Context/version distinctions, constrained the correction;
implementation was not used to derive architecture.

- **AX-05/11/12:** Context/Pragma active use, efficient anticipated-value loading
  and developer insulation preserve Mnemosyne durable-state authority. Unloading
  does not persist; cache or business-logic success cannot establish durable truth.
- **AX-04/10:** exceptional harmonisation reuses common execution through the
  established work contract; no execution framework, runtime topology or technology
  is invented. Persistent services retain their machinery beneath Harmonia semantics.
- **AX-07/08/09:** Themis owns hosted enforcement, compartments remain governed,
  persistence logging is distinct from Kleio evidence and diagnostic PHI controls
  remain. No audit-authorisation exception is adopted.
- **AX-14/15:** Context version, resource authoritative state/version and active
  generation remain distinct; working changes versus new Outputs, derived metadata
  versus source Content, and confirmed versus indeterminate outcomes remain distinct.
- **AX-17/18:** detailed unknowns are explicit; search, persistence, AI derivation
  and observation confer no originating domain authority.

The existing **Themis audit-persistence authorisation proposal versus AX-07
conflict remains unresolved**. Its detailed CDG-42 section is byte-identical to
the task baseline. AX-07 was not changed or weakened.

## RADS files changed

All paths below are within `docs/markdown/05-information-systems-architecture/`.

| File | Correction |
| :--- | :--- |
| [context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md) — new | Self-contained Mneme services, Context lifecycle, Pragma Output, harmonisation, persistence oversight, Search and Research Metadata meaning. |
| [README.md](../../docs/markdown/05-information-systems-architecture/README.md#mneme-mnemosyne-adjudication) | Orientation, new view and U10/U11 adjudication; residual Energeia wording removed; A2/A3/A4/Step-B boundaries retained. |
| [component-register.md](../../docs/markdown/05-information-systems-architecture/component-register.md#c09) | C09/C11/C12 responsibilities, Ergo/Ergon and Pragma boundaries; internal RHM C40. Existing registry/preservation responsibilities retained. |
| [candidate-system-map.md](../../docs/markdown/05-information-systems-architecture/candidate-system-map.md) | Responsibility hierarchy and collaboration: Context value Composition, common execution, persistence outcome/reporting, search, Research Metadata and hosted PEP. |
| [candidate-feature-map.md](../../docs/markdown/05-information-systems-architecture/candidate-feature-map.md) | Four-service surface and supporting state progression; internal RHM F41, persistence oversight F42, Search F43, Research Metadata F44. |
| [data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md) | Context D26, expected set D27, Output/new Content D28, search metadata D29, Research Metadata D30; ownership/state/persistence boundaries. |
| [middleware.md](../../docs/markdown/05-information-systems-architecture/middleware.md) | Loader/Unloader versus Load/Save, harmonisation work M17, persistence oversight M18, search/Research Metadata collaboration M19. |
| [pathways-journeys-and-handoff.md](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md) | Activity Context/Output seam, narrow harmonisation execution reuse, retained selection/handoff/Journey ownership and explicit Observe → Report label. |
| [gaps.md](../../docs/markdown/05-information-systems-architecture/gaps.md#context-and-pragma-contracts) | CDG-27/31/40 refined; CDG-45–48 separate settled responsibilities from unresolved detailed contracts. Existing conflict/deferrals retained. |
| [sources.md](../../docs/markdown/05-information-systems-architecture/sources.md#u10) | DesignNote/task provenance, reconciliation and precise legacy supersession scope. |

Registers now contain 40 component/construct entries, 44 features, 30 data entries,
19 middleware entries and 48 candidate-definition gaps. These are local navigation
keys, not Step-B traceability or upstream coverage claims.

## Architectural meanings incorporated

Mneme's four principal exposed services are exactly **Context Load, Context Save,
Adhoc Resource Access and Adhoc Search**. Resource Harmonisation Manager is internal.
Historical governed read/change carriers remain supporting intent with unresolved
mapping to that surface; they do not create additional exposed services.

Context contains authoritative loaded **values**, composed into its bucket by
**Composition**. It is a versioned temporal load-snapshot, with Context version
distinct from constituent resource state/version. No globally transactionally
consistent distributed snapshot is inferred. An Ergo declares the resources it
is most likely to access, deliberately non-exhaustively; efficient Context Load
precedes the Loader exposing those values to business logic. Adhoc runtime access/
search remains legitimate for external interaction, deep interrogation and emerging
requirements, and may cost materially more than Context Load.

The Context Unloader returns bounded results across the logic boundary and is not
persistence. Changed working Context values and **new Content as Pragma Output**
remain distinct; Mneme manages their subsequent information consequences. Originating
Ergon persistence, database/shard selection, transactions and generic conflict
resolution responsibilities are prohibited.

Internal Resource Harmonisation Manager manages exceptional resource delivery,
expected at approximately **5% or less at worst**, a design expectation rather than
an SLA. It causes appropriate bounded Harmonisation Ergon activity as **new governed
work** using Ponos through the established work/execution contract. It does not
perform domain harmonisation, resume inside the originating Ergon or duplicate/
embed execution-framework responsibility. Ergo Library remains candidate reuse.
No Iris, forms or UI design was performed. Kleio's separate cross-target audit
harmonisation SEP remains intact.

Mnemosyne manages authoritative persistence through transaction oversight,
persistent-service oversight/reporting, shard management, authoritative reporting,
persistence logging, compartment validation/reapplication and Themis-owned PEP
hosting. Persistence API success alone is not Harmonia's authoritative outcome.
Semantic compartment applicability and sharding safety are not independently
determined by Mnemosyne. Clinical/C&G/O&A domains and their scaling constraints
remain unchanged; no physical technology/topology is selected.

**Search Management** searches authoritative managed persistence behind Mneme Adhoc
Search; transient cache is not assumed complete. Mnemosyne may manage persistence-
domain, shard, persistent-service and search/index capability metadata.
**Research Metadata Management** is distinct and usable by search: Mnemosyne captures,
persists, manages and utilises AI-derived meaning/relationships based on proximity.
Authoritative source Content remains unchanged; derived metadata remains distinct
from source Content/metadata. AI derivation occurs outside Mnemosyne through an
appropriate Harmonia behavioural/execution construct.

The Mnemosyne DesignNote itself does not elaborate Research Metadata. Its first-class
responsibility and Search elaboration come from the **explicit task adjudication**;
this distinction is recorded in U11 rather than attributed to absent note wording.

## Energeia wording and preserved adjudications

The exact stale three-stage sequence was already absent at the clean task baseline.
The README still allowed an intermediate architectural responsibility to return as
future feature detail; that sentence was removed and replaced with the established
observation/reporting boundary. System and Journey collaboration labels now explicitly
state **R1.x/R2.x Observe → Report**. README, register, feature map, middleware and
gaps consistently preserve that meaning. Legitimate Dokimasia monitoring restrictions
and historical provenance were retained.

R3 **Observe → Analyse → Decide → Adapt** remains separately deferred. Kleio capture/
immutability/availability, Themis enforcement/default-deny, Petasos queue/Parcel/
handoff, Energeia Praxis/Journey ownership, DigitalTwin/Dokimasia reuse/terminal rules,
Pathway control and Journey evidence distinctions remain intact. No Identity
architecture was reopened.

## Unresolved matters retained

CDG-45–48 preserve physical Context/identity/version/bucket/Information Unit mapping,
access/search/Output representation, concurrency/conflict detection, harmonisation
categories/catalogue/algorithms/repeated-work escalation, exact narrow work contracts,
persistence transaction/outcome/rollback/retry/service/shard/compartment/PEP/logging
detail, search query/result/completeness/consistency, and Research Metadata
**provenance, confidence and schema**. The deriving behavioural construct is not
allocated by inference. No AI framework/model/provider/vector/embedding/proximity
algorithm or physical index is selected. Existing CDG-42 Themis/AX-07 uncertainty
and all unrelated gaps remain.

## Legacy supersession and preservation

**Six section-level wrappers across three legacy files**, with authoritative RADS
replacement links:

| Legacy file | Newly wrapped content |
| :--- | :--- |
| [Mneme concept](../../docs/concepts/mneme.md) | Opening definition; §2 introductory definition paragraph; §3 positive ownership heading/list. |
| [Hestia concept](../../docs/concepts/hestia.md) | Opening definition; §2 dual-state account including its diagram and three explanatory items. |
| [Mnemosyne concept](../../docs/concepts/mnemosyne.md) | §3 positive ownership heading/list. |

**PASS:** every enclosed original section is byte-preserved. Removing only the
six new wrappers recovers each entire baseline file byte-for-byte, including the
two existing Mnemosyne wrappers. All other legacy files/wrappers are unchanged.
Whole documents are not superseded. Both external DesignNotes and the supplied
legacy Themis concept remain unchanged. The initial structure check caught a
wrapper boundary inside the Hestia ASCII diagram; it was corrected to the actual
section separator, and final structure/preservation validation passed.

## Validation results

**PASS — architecture suite**, using the repository's Java 21 compiler requirement,
cached dependencies/offline Maven and a five-minute command bound:

```bash
env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH \
  timeout --signal=TERM --kill-after=10s 300s \
  mvn -o -B test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**BUILD SUCCESS; exit 0; 90 tests across 11 classes; zero failures, errors or
skips; 28.561 seconds; OpenJDK/ArchUnit 21.0.12.1.** No timeout or stall.
These executable guardrails do not implement or accept the new documented design.

**PASS — documentation:** bounded CommonMark/GFM parsing/rendering; table widths
and parsed rows; closed fences; unique anchors/register IDs; local file/fragment
links and inbound references; Mermaid delimiter/relationship review; exactly four
principal Mneme services; no residual stale Energeia sequence or generic Mneme
Information Manager role. Dokimasia terminal rules, Themis/AX-07 conflict and R3
deferral sections were checked against the baseline. `git diff --check` passed.

**PASS — scope:** SHA-256 baseline over 2,319 tracked files verifies all **76
Domains01–04 files unchanged**, and **2,307 tracked files outside the permitted
Domain05/legacy scope unchanged**. Implementation/source/configuration, governance
instructions, axioms, completion/runtime plans and unrelated documentation are
unchanged. Only the ten Domain05 views, three targeted legacy documentation files
and this report are changed/new. Test build outputs are generated verification
artefacts, not source/configuration edits.

Temporary validation scripts, baseline hashes, wrapper manifest and logs are under
`/tmp/domain05-mneme-mnemosyne-20261011/`. The final `validation.json` records the
documentation, preservation, scope and architecture results. They are execution
evidence, not architectural authority.

## Canonical documentation assessment

Relevant non-RADS material comprises the two supplied DesignNotes and the inspected
legacy Mneme/Mnemosyne/Hestia concepts, with existing Pragma/Ergon and governed-write
material providing reconciliation context. The authorised architectural meaning
belongs in Domain05 and is incorporated into its management, component, feature,
data, middleware and uncertainty views. Legacy original content is preserved with
targeted wrappers; external notes remain unchanged provenance. Remaining historical
technology/implementation catalogues and unresolved detailed contracts were not
broadly migrated. Required incorporated meaning no longer depends on Downloads.

**Step A remains OPEN; A2/A3 remain IN PROGRESS; A4 remains PENDING; Step B has
NOT started.** This bounded correction does not complete Domain05.
