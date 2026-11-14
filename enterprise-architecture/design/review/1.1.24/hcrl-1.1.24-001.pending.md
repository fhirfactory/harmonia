<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Five-contract interaction and accepted decision preservation | ACCEPTED — complete incorporated result | Final Solution Architect Task, 2026-11-14T09:38:10.697Z. |
| [DR-2](#dr-2) | Identity, information, Feature and native relationship reconciliation | ACCEPTED — actual reconciliation confirmed | Alternative Feature/Function/Interface encapsulation accepted; no remediation. |
| [DR-3](#dr-3) | Two-cache architecture and MC-01–MC-07 | ACCEPTED WITH QUALIFICATIONS | MC-01–MC-07 and technical/deployment limitations retained. |
| [DR-4](#dr-4) | Validation, projections, transport and human checkpoints | ACCEPTED WITH QUALIFICATION — exact 1.1.24 candidate CR-001 | Strict FAIL retained; native Archi-tree/HTML-reader reviews completed and accepted; closure authorised. |
| [DR-5](#dr-5) | Detailed-contract deferrals and temporal provenance | ACCEPTED WITH QUALIFICATION | Original Task time and filename prefix remain explicitly UNRESOLVED; no substitution. |
| [DR-6](#dr-6) | Bounded supporting Minutiae | ACCEPTED — Minutiae-Accepted: Yes | Preserve supporting evidence, intermediate failures and all qualifications. |

# HCRL 1.1.24 / 001 — Mneme–Mnemosyne interaction and Mneme cache architecture

**COMPLETE — accepted and published 1.1.24 baseline, with the exact retained qualifications. Terminal commit/push verification is recorded separately in closure evidence.**
This retained review-history copy is closed. The authoritative [completed register](../../register/1.1.24/hcrl-1.1.24-001.complete.md) records the same accepted transition; there is no successor iteration.

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

**Final attributable acceptance:** the [Solution Architect closure Task](../../evidence/1.1.24/001/before/ai-agent-tasks/20261114T093810Z-final-acceptance-and-release-closure.md), instruction-entry **2026-11-14T09:38:10.697Z**, expressly accepts the complete 1.1.24 incorporated result, DR-1–DR-6 and their recorded qualifications. Native Archi-tree and HTML-reader structural reviews are complete and accepted; the observed alternative Feature/Function/Interface encapsulation is acceptable without remediation. The [exact reviewed HCRL](../../evidence/1.1.24/001/before/release-closure/hcrl-1.1.24-001.reviewed.md) preserves the prior checkpoint. Earlier statements below about pending review, unavailable authority and unperformed release/Git operations describe that pre-acceptance history; final dispositions and actual closure facts govern current status.

The final Task authorises one expected-current **1.1.23 → 1.1.24** patch allocation, validation/publication, complete registration, commit and push to `origin/Mneme-and-Mnemosyne-Build`, followed by remote/release/clean-tree verification. **Strict transport remains FAIL** for the exact accepted candidate `6f6432a979a92a3adbf0261ba4bc761d092f87eb55fd4e5d7968f597e4d64d3a`; no lossless transport or pixel equivalence is asserted. Original ADD Task instruction-entry time/prefix remain unresolved under explicit DR-5 treatment; this final Task's own attributable event does not replace them. No new design, implementation, returned-model application or comparison-policy change is authorised.

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

**Final DR-1 disposition:** ACCEPTED. The complete incorporated five-contract interaction and recorded AV/FC/MI/SearchResult decisions are confirmed by the final Task; no additional architectural change is made.

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

**Final DR-2 disposition:** ACCEPTED. Identity, information, Feature and native relationship reconciliation is confirmed. The alternative Feature/Function/Interface encapsulation observed during structural review is accepted and requires no remediation.

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

**Final DR-3 disposition:** ACCEPTED WITH QUALIFICATIONS. The two-cache architecture and **MC-01–MC-07** are accepted with all recorded topology, concurrency, resource, visibility, durability and deployment limits. No technical qualification is silently resolved or weaker multi-site durability guarantee inferred.

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
equivalence is inferred. At the initial checkpoint, **CR-001 was On-Hold / Unresolved**;
the [pre-disposition HCRL](../../evidence/1.1.24/001/before/cr001-disposition/hcrl.before.md)
preserves that reviewed state. Prior 1.1.23 qualified acceptance supplied no waiver
for this candidate.

**Subsequent Solution Architect disposition — CR-001: ACCEPTED WITH QUALIFICATION
(1.1.24 only).** Mark Hunter expressly accepts the bounded transport exception for
the exact candidate SHA-256 above in the
[verbatim issued follow-up](../../evidence/1.1.24/001/before/cr001-solution-architect-disposition.md).
Native CR Disposition is **Accept**, Outcome **Resolved**, by this governing decision.
The supplied correspondence establishes preservation of all predecessor relationship
and View records across releases; all element/relationship/View identities survive
actual Archi transport; the 51 new-relationship differences are limited to
documentation-language annotations. **Strict verification remains FAIL**, with its
original result and raw evidence retained byte-unchanged. Previously qualified Archi
representation/presentation limitations remain; acceptance establishes no lossless
transport, pixel equivalence or general equivalence of all raw differences.

The returned Exchange model remains unauthorised for application to canonical
Markdown or maintained Views. No comparator weakening, template alteration or
architectural remediation is authorised. This decision applies only to the exact
1.1.24 candidate and establishes no waiver for subsequent releases. Recording it
is provenance maintenance within this iteration; no iteration advance or release
allocation follows. The instruction-entry time is unavailable in exposed interaction
metadata; the separately recorded capture clock reading is not substituted.

Solution Architect native-tree/reader structural review and exact-result confirmation
under Review Praxis §§5–6 remain pending. The imported native model and HTML reader
are retained for those checkpoints. This activity ends at the reviewable pending
candidate; no closure, release allocation or accepted publication occurs.

**Final DR-4 disposition:** ACCEPTED WITH QUALIFICATION. CR-001 is accepted for the exact 1.1.24 candidate only; **strict Archi verification remains FAIL**. Native Archi-tree and HTML-reader structural reviews are completed and accepted, as is the whole incorporated result. The ordinary release-closure checks and deterministic release-metadata correspondence remain required; no return/model/template/comparator alteration is authorised.

### Verified release allocation and accepted publication

The governed `architecture_release.py bump --expect 1.1.23` mechanism allocated **1.1.24 / previous 1.1.23** exactly once for this accepted change set. At preflight all nineteen publication destinations and the completed-register destination were absent; no immutable predecessor is overwritten. No iteration advance or further release allocation occurs.

The maintained `make continuum continuum-rad` **PASSed** in 48.225 seconds, followed by ordinary `make check browser-test` **PASS** in 50.225 seconds: canonical provenance/identities, containment/navigation, naming with explicit qualifications, offline XSD/references, release/Manifest, both Exchange copies, reader/narrative freshness, local links and DOM contracts. The fresh required Maven architecture suite **PASSed 90 tests**, zero failures/errors/skips. Sources remain **6,563 elements / 9,427 relationships / 1,304 authored Views / 462 qualified predicates / 8,446 source bindings**. Existing focused Continuum/navigation and independent maintained-input reproduction evidence remains intact and applicable to the unchanged incorporated sources.

Both published Exchange copies have SHA-256 **`9f2e2c7b663423218fd03e6a1c26cd2ada1444b1690f7015e2ec022b29968dc4`**. Whole parsed model comparison preserves the exact accepted candidate **`6f6432a979a92a3adbf0261ba4bc761d092f87eb55fd4e5d7968f597e4d64d3a`** except its root release property changing **1.1.23 → 1.1.24**. Reader/narrative correspondence preserves every remaining model/content/field/order, with only selected generated release metadata, independently verified derived model hash and generated link targets changing. Original candidate bytes remain intact. This is the deterministic release projection authorised by the final Task; it supplies no new transport PASS or waiver beyond exact CR-001.

[The closure evidence index](../../evidence/1.1.24/001/after/release-closure/README.md) links full commands, checks, [all nineteen publication hashes](../../evidence/1.1.24/001/after/release-closure/publication-integrity.json), actual limits and preservation. Accepted products are the [durable Archi Exchange](../../archimate/published/1.1.24/Harmonia-Architecture.xml), [HTML reader](../../../documentation/continuum-manifest/published/1.1.24/continuum.html) and [narrative source](../../../documentation/continuum-rad/published/1.1.24/continuum-rad.tex). Optional PDFs were not compiled or published.

All **6,563 canonical element files and navigation**, **75 pre-existing iteration evidence files**, **299 prior publication/register files**, **71 maintained inputs** and **four approved source DRPs** retain exact entry bytes. The generated root Manifest changes only its release title; reversing that single heading restores its entry hash. The root release changes only through the authorised one-patch mechanism. No production comparator, application implementation, template, returned model or architectural definition is changed during closure. Supporting checker fixture/Manifest assumptions were corrected after exact diagnosis; their initial failures remain supporting evidence, not suppressed or reclassified successful executions.

**Strict transport remains FAIL**, including **6,507 relationship records and 15 Views**; CR-001 treatment remains specific to the accepted 1.1.24 candidate. Native-tree/reader human reviews are explicitly accepted. The evidence-layout checker remains **UNRESOLVED (exit 2)** solely for the original ADD Task time/prefix, with explicit final DR-5 treatment; it is not reported as full temporal conformance. All MC-01–MC-07, six new formal-name qualifications, inherited information-name/standing and detailed-contract qualifications remain. No lossless transport, pixel equivalence, topology/deployment proof or weaker durability contract is inferred.

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

**Final DR-5 disposition:** ACCEPTED WITH QUALIFICATION. The unavailable **original ADD Task instruction-entry timestamp and filename prefix remain explicitly UNRESOLVED** for this release. Neither current-clock capture nor the final acceptance Task event is substituted. Six new formal-name qualifications, 17 inherited information-name cases and owning-contract/detail deferrals remain explicit. This is accepted bounded provenance treatment, not a fully conformant original timestamp claim.

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
Minutiae-Accepted: Yes
Comment: Mark Hunter, Solution Architect, explicitly accepts the disclosed group in the final Task, 2026-11-14T09:38:10.697Z. Preserve all supporting evidence, intermediate failures and qualifications.
```

**Final DR-6 disposition:** ACCEPTED, including the disclosed supporting Minutiae and required release-closure evidence/provenance maintenance within this same 1.1.24/001 boundary. Original records and intermediate failures remain intact.

**Registration and Git boundary:** this record completes HCRL **1.1.24/001** on the final Solution Architect confirmation. The retained review copy preserves history and refers to this immutable register. No successor iteration is allocated. Commit/push to `origin/Mneme-and-Mnemosyne-Build` is expressly authorised after the passed release controls. Terminal actual SHA, remote equality, root release and clean-tree facts are recorded separately in the closure Report/Git receipt, without amending this completed HCRL or allocating another patch.
