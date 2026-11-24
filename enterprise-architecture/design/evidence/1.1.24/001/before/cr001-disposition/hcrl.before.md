<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Five-contract interaction and accepted decision preservation | Complete source DRPs approved; ADD authorised | Actual incorporated result awaits confirmation. |
| [DR-2](#dr-2) | Identity, information, Feature and native relationship reconciliation | Deterministic incorporation authorised | Result and explicit naming qualifications await review. |
| [DR-3](#dr-3) | Two-cache architecture and MC-01–MC-07 | Complete source DRP approved with its qualifications | Technical/deployment qualifications remain; no weaker durability contract. |
| [DR-4](#dr-4) | Validation, projections, transport and human checkpoints | Not recorded for resulting candidate | Actual Archi import/save/export PASS; strict transport FAIL — CR-001 On-Hold/Unresolved. |
| [DR-5](#dr-5) | Detailed-contract deferrals and temporal provenance | Source-defined detail boundaries approved | Unavailable Task event/prefix requires treatment before closure. |
| [DR-6](#dr-6) | Bounded supporting Minutiae | Not recorded | Supporting evidence and generated Manifest only. |

# HCRL 1.1.24 / 001 — Mneme–Mnemosyne interaction and Mneme cache architecture

**PENDING — authorised governed ADD candidate; no final result acceptance, release allocation, accepted publication, commit or push.**

Mark Hunter, Solution Architect, explicitly approves both complete Final.001 DRPs
and directs this incorporation in the [operative issued Task](../../evidence/1.1.24/001/before/ai-agent-tasks/unresolved-mneme-interaction-cache-add.md).
Their internal pre-approval wording is historical. The source files remain unchanged;
this subsequent approval governs their semantic use. Approval of inputs does not
confirm the representation produced by this activity.

Actual originating branch: `Mneme-and-Mnemosyne-Build`; HEAD:
`8204fd49a4d380c068a03acdf30c17bb06e5d953`; clean working tree at entry.
Root release: **1.1.23**, previous **1.1.22**, consistent with both completed HCRLs.
Target **1.1.24**, iteration **001**, consumes no release allocation during pending
preparation. [Before-state pins](../../evidence/1.1.24/001/before/baseline.json)
identify exact recoverable source bytes at entry HEAD. The DRPs' historical commit
identifiers do not select this baseline. Accepted publications and registers remain
immutable. The next accepted substantive change receives one patch allocation,
subject to expected-current recheck and actual-result acceptance.

The completed [formal Report](../../evidence/1.1.24/001/after/ai-agent-reports/20261114T045751Z-mneme-interaction-cache-add.md)
records actual activity, resulting Git state, validation and unresolved qualifications.
Its Report-Produced-At event and corresponding filename are captured by the maintained
evidence emitter after completed initial emission. Mechanical evidence checks and
cross-link synchronisation remain within this same provenance boundary.

<a id="dr-1"></a>
## DR-1 — Five-contract interaction and accepted decision preservation

Incorporate exactly `GovernedSummaryReader`, `GovernedSummaryWriter(CREATE, RETIRE)`,
`GovernedSummarySearch`, `GovernedReader`, `GovernedWriter`. Search is Summary-only
in R1/R2 for predicates, matching, indexing domain and result disclosure. There is
no sixth interaction method. Summary excludes exactly InformationRepresentation.Content;
the Content reference is in ContentMetadata, opaque to Mneme. Full reads attempt
materialisation subject to the established large-binary exception. Full writes
establish coherent Content, reference and Summary as the same authoritative IU/version.
Summary CREATE registers external Content without retrieving/validating it; external
custody, integrity, availability and lifecycle remain SEP. RETIRE preserves history
and does not delete the external referent; Summary UPDATE is absent.

Preserve AV-01–AV-05, FC-01–FC-05, MI-05, MI-06 and SearchResult refinement. Explicit
version reads never substitute; directed writes may return a higher actual version
while preserving immutable history. All authoritative mutation Success requires
multi-site durability through PersistenceService. Timeout does not determine factual
Outcome. Authoritative `GovernedReader(version=*)` may reconcile uncertain writes
only with sufficient correspondence/durability evidence. Complete required IU-type
coverage uses the union of eligible available supported criteria, with shortfall alarms;
per-request matching and Themis remain separate. Configured identities/sites remain
deterministic. Continuations remain bound to the originating Mnemosyne instance.

SearchResult.itemList contains IU Summary projections with size equal to item count,
paginationSize, persistence-extraction timestamp and nextSetReference. Mneme's Search
Result Set remains IU-version references. Session MI-07 maps to original **MI-03**;
original **MI-07 RETIRE/DELETE** is preserved. No new search-result model is implied.

**Actual incorporation:** accepted definitions and all AV/FC/MI/SearchResult decisions
are represented in the five owning services, Store/Façade and cache/execution contexts.
The [complete source/decision correspondence](../../evidence/1.1.24/001/after/source-correspondence.md)
accounts for every source section, approved decision, preserved boundary and detail
deferral. Both approved DRPs remain byte-unchanged and now serve as input/decision
provenance rather than parallel active multi-element definitions. No new semantic
design question was required for deterministic incorporation.

<a id="dr-2"></a>
## DR-2 — Identity, information, Feature and native relationship reconciliation

Preserve Reader `410a938f42964474b7cf5655815dc830`, Writer
`105f6ebe61f74bc6b51938ba550bc1a7`, and Search `dcb31810ae134818adeeba9b27bd2f30`.
Refine/rename Search to GovernedSummarySearch: same authoritative-search responsibility,
provider, consumer, Home and relationships; narrower accepted search domain. Preserve
old names/source wording as history, never as an exposed sixth method. Two missing
Summary service contracts justify two new ApplicationServices. Search Request and
SearchResult are logical contract structures within the service, not automatic new
IUs, Topics, Classes or DTO models.

Use native behaviour/service/data/technology relationships with explicit scope.
Reuse the accepted CT1→CT2→CT3→Feature hierarchy, Motivation, Business IU and Search
Result Set, Calliope IU/metadata/Topic/TopicMask/Outcome, and existing Store/Façade.
No new Feature, Requirement, Business activity or Interaction metamodel is needed.
Logical cache responsibilities and their selected technology realisations are distinct;
runtime multiplicity creates no additional canonical identities.

**Actual delta:** six new elements — two ApplicationServices, two ApplicationFunctions
and two SystemSoftware configurations — and **51** new native relationships. All
**6,557 predecessor identities**, **9,376 predecessor relationship records** and
**1,304 authored Views** remain preserved. The sole intentional existing-name change
is Search → SummarySearch, retaining its ID, previous names and bounded superseded
prose. Existing Strategy and Business definitions remain unchanged. The
[identity allocation](../../evidence/1.1.24/001/after/identity-allocation.json),
[native correspondence](../../evidence/1.1.24/001/after/native-relationship-correspondence.json),
[Feature ancestry](../../evidence/1.1.24/001/after/feature-correspondence.md) and
[independent preservation](../../evidence/1.1.24/001/after/preservation.json)
establish actual classification, Home and contribution relationships. Ten selected
contributions follow real CT1→CT2→CT3→Feature Composition paths; no ancestry is
inferred from a friendly name, folder or historical Feature label.

AX-03/04 preserve native Content and machinery/semantics; AX-05/06 distinguish active,
durable and information authority; AX-07 preserves Themis/disclosure and Kleio mandatory
audit authority; AX-08/09 preserve meaningful evidence and transient caches; AX-10/11
preserve distribution and availability without weaker guarantees; AX-12/13/18 preserve
governed access and external/domain boundaries; AX-14/15/17 preserve versions, outcomes,
uncertainty and accepted-input versus pending-result standing. No Java implementation
is used as authority or changed. Native valid edges and independent semantic checks
test the representation; technical deployment conformance remains future evidence.

<a id="dr-3"></a>
## DR-3 — Two-cache architecture and MC-01–MC-07

Mneme-Standard-Cache provides distributed lightweight IU Summary/reference and
operational state using Infinispan Server/Hot Rod. Synchronous distribution is the
selected direction, qualified by topology/concurrency verification. Mneme-Workflow-Cache
provides transient materialised processing information using embedded Infinispan LOCAL
within each applicable Ponos, DigitalTwin and Dokimasia JVM. No inter-JVM Workflow-Cache
replication, L1 hybrid or independent authoritative cache IU is introduced.

Common Ergon owns context loading/unloading, checkpointing and recovery; specialisations
reuse it. Recovery reconstructs required context from authoritative information and
durable work at the existing execution boundary, without resuming inside a failed Ergon.
Immutable input may have independent working copies; new authoritative versions do not
retroactively mutate an executing input. Existing DigitalTwin serialisation remains.

MC-01/02 retain unproved topology, owner/site placement, concurrency, partition and
site-loss behaviour. No native linearizability or universal replication is claimed.
MC-03 retains resource/security/lifetime policy. MC-04 retains visibility/freshness
under governing read/wait/persistence contracts. **MC-05:** two sites minus one cannot
establish new multi-site durable write Success without another eligible site or explicit
contract disposition; no N+1 write-continuity claim is made. MC-06 is deterministic
five-service reconciliation; MC-07 is existing Feature mapping plus deferred physical
implementation allocation. These are retained qualifications, not newly invented mechanisms
or requests to reopen 1.1.22/1.1.23. Any later deployment claiming incompatible guarantees
requires owning NFR/persistence adjudication before that claim/application.

<a id="dr-4"></a>
## DR-4 — Validation, projections, transport and human checkpoints

Entry canonical validation PASS: **6,557 elements / 9,376 relationships / 462 qualified
source predicates**. Generate candidate-only XML/reader/narrative through maintained
tooling with explicit evidence-local destinations. Root release remains 1.1.23; generated
metadata showing it does not publish this candidate as accepted 1.1.23 architecture.
No live `project --publish` or Makefile `continuum` operation is authorised here.

**Actual automated outcomes:**

- Canonical/native/naming/navigation and offline XML schema/reference validation
  **PASS**: **6,563 elements / 9,427 relationships / 462 qualified predicates**,
  8,446 source bindings. Source standing/name qualifications remain explicit.
- Independent preservation **PASS**; all 6,913 pinned predecessor paths are either
  unchanged or within the authorised canonical delta. Both source DRPs, root release,
  accepted registers/publications and maintained View template are unchanged.
- [Independent reproduction](../../evidence/1.1.24/001/after/reproducibility.json)
  **PASS**: **19** candidate/Manifest outputs identical in an isolated maintained-input
  tree without Legacy-RAD, scripts, Git or undeclared processing inputs.
- Exact-candidate no-edit workbench review **PASS**: zero element changes,
  relationship/View changes or unresolved matters. Reader/Manifest/narrative
  staleness **PASS**; 3,045 rendered current source links and reader DOM smoke **PASS**.
- Eight focused Continuum and four navigation tests **PASS**. Required Maven
  architecture suite **PASS: 90 tests**, zero failures/errors/skips; Java unchanged.
- Actual installed Archi **5.10.0.202605271610** import/native-save/export **PASS**.
  Initial pre-import sandbox display failures are preserved; the same isolated
  check completed with local-display access.
- Unmodified strict transport verifier **FAIL: Relationship/View transport drift**.
  All 6,563 element IDs/types/specialisations/names/documentation and governed
  identity/name property values/owners survive; no relationship/View ID lost or
  added. **6,507 of 9,427 relationship records** and **15 of 1,304 Views** differ.

The exact [candidate/review guide](../../evidence/1.1.24/001/after/review-guide.md),
[verification summary](../../evidence/1.1.24/001/after/verification-summary.json),
[transport summary](../../evidence/1.1.24/001/after/transport-summary.json) and
[CR-001](../../evidence/1.1.24/001/after/change-review.md#cr-001) preserve inspectable
projections, raw classifications and actual execution. Candidate XML SHA-256:
`6f6432a979a92a3adbf0261ba4bc761d092f87eb55fd4e5d7968f597e4d64d3a`.
Generator reports saying `actualArchiTransport: NOT_PERFORMED` describe generation's
own scope; the separately retained actual Archi run establishes transport execution.

Bounded actual-record correspondence establishes all 9,376 predecessor relationship
records and 1,304 Views match across releases both before and after transport.
The 51 new-relationship raw findings are exclusively absent-to-`en` documentation
annotations. Raw categories, strict FAIL, presentation/default/order uncertainty
and candidate-specific disposition remain distinct; no harmlessness or pixel
equivalence is inferred. **CR-001 remains On-Hold / Unresolved.** Prior 1.1.23
qualified acceptance supplies no waiver for 1.1.24. Dependent finalisation stops
until this candidate's gate receives explicit Solution Architect treatment.

Solution Architect native-tree/reader structural review and exact-result confirmation
under Review Praxis §§5–6 remain pending. The imported native model and HTML reader
are retained for those checkpoints. This activity ends at the reviewable pending
candidate; no closure, release allocation or accepted publication occurs.

<a id="dr-5"></a>
## DR-5 — Detailed-contract deferrals and temporal provenance

Exact DTOs/signatures, Reason members/stage vocabulary, reference/cursor encoding,
provider proof/correlation thresholds, native RETIRE/DELETE/retention mapping,
TopicMask coverage proof, probes/selection, resource bounds and WildFly/container
allocation remain with owning detailed contracts, within accepted semantics.
Mandatory operation-specific audit/provenance catalogues are not invented from
historical candidate lists. Existing source-near evidence, Themis, Kleio and
Dokimasia/Provenance obligations remain; where mandatory Kleio capture cannot be
performed, AX-07 fail-closed behaviour remains binding.

The final source filenames provide the exact approved input locators; competing
historical draft identifiers remain provenance aliases, not new canonical IDs.
No neighbouring DRP is accepted or incorporated automatically. Common Ergon scheduling
discussion remains boundary context rather than a new scheduling API specification.

Task instruction-entry timestamp is unavailable in exposed interaction metadata.
**Task-Issued-At and required filename prefix remain UNRESOLVED** for Solution Architect
treatment before closure under the Provenance contract; current-clock capture is not
substituted. Report production uses the evidence tool's completed-emission event.
Null formal names remain explicit where governed type vocabulary is absent; no invented
qualifier, Class or Topic is introduced merely to satisfy naming.

The six additions retain explicit null formal-name qualifications. The existing
information-name check still reports its inherited 17 unresolved information cases;
no duplicate model or invented qualifier is used to suppress them. Technical/detail
deferrals are carried with their actual status, not presented as new architectural
questions. Unavailable original Task time/prefix remains an explicit closure matter.

<a id="dr-6"></a>
## DR-6 — Bounded supporting Minutiae

Disclosed group: issued Task/Report, exact source/baseline and preservation pins,
source/element/relationship/decision correspondence, validation and reproduction
evidence, candidate-only projections, generated root Manifest and iteration review
links. Two maintained test population expectations are updated: 6,563/9,427/462 and
the two explicit selected Technology IDs in place of the formerly empty domain.
No comparator, assertion strength or validation rule is weakened. Initial test
invocation/import/class-name errors and the stale empty-Technology assertion are
retained separately from the corrected passing checks. Evidence-harness path and
declared-provenance input corrections were recovered before successful independent
verification; they did not change architecture or allocate identities. Material
meaning remains DR-1–DR-5.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect review of this bounded group]
```
