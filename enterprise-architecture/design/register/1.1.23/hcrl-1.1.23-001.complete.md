<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | MP-01–MP-08 and five-tier incorporation | ACCEPTED | Exact incorporated Mnemosyne architecture confirmed. |
| [DR-2](#dr-2) | Existing hierarchy, Motivation and component boundaries | ACCEPTED | Reconciliation and preserved IU/component boundaries confirmed. |
| [DR-3](#dr-3) | Canonical, projection, transport and human review gates | ACCEPTED WITH QUALIFICATION | CR-001 Accepted — Qualified for 1.1.23; strict transport FAIL retained; human reviews accepted. |
| [DR-4](#dr-4) | Naming, contract details and temporal provenance | ACCEPTED WITH QUALIFICATION | Null names, deferred vocabulary and unavailable Task times remain explicit. |
| [DR-5](#dr-5) | Bounded supporting Minutiae | ACCEPTED | Minutiae-Accepted: Yes; original evidence preserved. |

# HCRL 1.1.23 / 001 — Mnemosyne authoritative persistence incorporation

**COMPLETE — accepted and published 1.1.23 baseline, with the exact retained qualifications. Terminal commit/push verification is recorded separately in closure evidence.**

**Final attributable acceptance:** Mark Hunter, Solution Architect, explicitly
accepts DR-1–DR-5 and the actual incorporation result through the
[final acceptance and closure Task](../../evidence/1.1.23/001/before/ai-agent-tasks/unresolved-mnemosyne-final-acceptance-and-release-closure.md).
CR-001 is **Accepted — Qualified**, specifically for release **1.1.23**, with
strict transport **FAIL** retained. Native Archi-tree and generated HTML-reader
visual reviews are completed and accepted. Commit/push to
`origin/Mneme-and-Mnemosyne-Build` is authorised after the release controls pass.
Unavailable instruction-entry times, including this closure Task's event/prefix,
remain explicitly unresolved under the accepted provenance qualification; no
observable event, name or vocabulary is fabricated.

The opening table and final dispositions supersede earlier pending/checkpoint
statements below, which remain **pre-acceptance decision history**. The
[exact reviewed HCRL snapshot](../../evidence/1.1.23/001/before/release-closure/hcrl-1.1.23-001.reviewed.md)
preserves the original record. There was one DR-3 row at entry; the normalised
current table has exactly one each of DR-1–DR-5. No decision or historical evidence
is deleted. Canonical source-stage standing remains unchanged; actual acceptance
is recorded here rather than rewriting the accepted candidate's source bytes.

**Pre-acceptance originating-state history follows.**

Immediate accepted predecessor: **1.1.22**, verified through root
`architecture-release.json` and the [completed predecessor HCRL](../../register/1.1.22/hcrl-1.1.22-001.complete.md).
Entry Git baseline: `fde98cde3c44b4f4b89e7f3a29765e1a44fca515`, branch
`Mneme-and-Mnemosyne-Build`. Proposed resulting release: **1.1.23**, the next patch
subject to expected-current recheck and final controls. Pending preparation consumes
no increment; root remains 1.1.22. No successor iteration is allocated.

**Authority:** Mark Hunter, Solution Architect, explicitly states in the
[operative Task](../../evidence/1.1.23/001/before/ai-agent-tasks/unresolved-mnemosyne-authoritative-persistence-incorporation.md)
that the complete Mnemosyne Final.001 DRP, including MP-01–MP-08, is reviewed and
accepted, and authorises this bounded governed ADD/reconciliation. The original
DRP's earlier review-stage wording remains unchanged as decision history; the
Task supplies the subsequent acceptance and application authority. The
[exact retained input](../../evidence/1.1.23/001/before/accepted-input/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.Final.001.md)
preserves the untracked source bytes, which are not recoverable from entry HEAD.
Task instruction-entry time/prefix remain **UNRESOLVED**, for Solution Architect
treatment before closure; no guessed time is substituted.

The completed [execution Report](../../evidence/1.1.23/001/after/ai-agent-reports/20261111T065144Z-mnemosyne-authoritative-persistence-incorporation.md)
records actual activity, correspondence, verification outcomes and resulting Git
state. Its actual Report-emission timestamp is preserved independently of the
unresolved Task event. The [review guide](../../evidence/1.1.23/001/after/mnemosyne-incorporation/review-guide.md)
identifies the exact candidate and outstanding human controls.

<a id="dr-1"></a>
## DR-1 — MP-01–MP-08 and five-tier incorporation

Preserve accepted responsibilities as logical Application behaviour, not mandatory
implementation types, modules, processes, protocols or deployment units. Represent
Mneme Adapter / Façade, InformationUnit Controller, InformationUnit Constructor /
Disassembler, GovernedPersistenceFramework and Persistence Adapter distinctly;
PersistenceService is externally usable behaviour jointly realised by GPF control
and adapter execution, never a sixth tier. The nine full/partial allocations and
capability-supported authoritative search retain the exact scope of DRP §3.1.

Represent Calliope-owned generic Outcome with closed `Success`, `Failure`,
`Indeterminate` status and common closed Reason vocabulary plus governed closed
domain extensions. Exact Reason members remain deferred. Unknown at reporting
time, native ACK, durable effect and Mneme's consumer disposition remain distinct.

MP-08 requires multiple instances with identical, overlapping or disjoint IU-type
support. Mnemosyne-side Façade registers through Topics; Mneme-side Mnemosyne
Adapter (Store) discovers via TopicMasks and selects eligible instances for high
availability/changeover and load balancing. Distinct IUs of one type may execute
concurrently. Mneme retains individual IU concurrency; every eligible instance
reaches the same logical authoritative engine for a given IU. Detection, routing,
coordination and changeover mechanisms remain solution design, not new architecture.

**Actual incorporation:** 11 new canonical identities (six ApplicationFunctions,
four ApplicationServices and one unspecialised logical DataObject), 62 new native
relationships and additive changes to 15 existing resources. The five Mnemosyne
functions are assigned to existing Mnemosyne; the distinct Store function is
assigned to existing Mneme. GPF and Persistence Adapter have explicitly qualified
joint-contribution Realization edges to PersistenceService. Outcome properties,
Reason members and responsibility rows are not separately instantiated elements.
The [source and MP correspondence](../../evidence/1.1.23/001/after/source-correspondence-and-MP-coverage.md)
records all eight adjudications, nine allocations, source succession and exclusions;
the [identity allocation](../../evidence/1.1.23/001/after/identity-allocation.json)
and [native correspondence](../../evidence/1.1.23/001/after/native-relationship-correspondence.json)
identify the exact resulting elements, new edges and selected reused edges.
Accepted input semantics are incorporated; Solution Architect confirmation of
this exact representation remains pending.

**Final DR-1 disposition:** ACCEPTED. The exact eleven-element/62-relationship incorporation and MP-01–MP-08 coverage are confirmed by the final Task; no semantic redesign or transport return is applied.

<a id="dr-2"></a>
## DR-2 — Existing hierarchy, Motivation and component boundaries

Reuse accepted CT1 → CT2 → CT3 → Feature identities and actual partial/one-to-many
contributions. Historical `DataManagement.InformationUnits.*` labels and proposed
Authoritative Persistence supply no authority for duplicate Features or reparenting.
Native realisation states architectural contribution only, never implementation
satisfaction or exclusive ownership. Requirements, Mandates, Axioms and assurance
retain their native classifications and inherited standing.

Applicable axioms: AX-03/04 preserve native Content and engine/semantic separation;
AX-05 preserves Mneme active generation versus Mnemosyne durable authoritative
version (partial all-information state responsibility does not weaken that durable
authority); AX-06/18 prevent custody or participation conferring domain authority;
AX-07 preserves Themis policy ownership and distributed PDP/PEP execution;
AX-08/09 preserve meaningful audit/provenance and transient-state boundaries;
AX-10/11 preserve distributed availability without weaker guarantees; AX-14/15
preserve stage, version, uncertainty and consumer-disposition distinctions;
AX-17 preserves absent authority, qualified mappings and human acceptance.

Retain the 1.1.22 IU identity, Metadata, exactly one InformationRepresentation,
Content, coordinates, version/generation and native-Content semantics. Preserve
Themis, Kleio, Dokimasia, Provenance and Calliope boundaries. Ordinary persistence
recoverability is partial; catastrophic recovery orchestration remains outside
Mnemosyne. Neighbouring interaction DRPs, implementation and authored Views are
outside modification scope.

**Actual reconciliation:** no Feature, Capability or Motivation identity was
created, reclassified or reparented. Scoped contribution Realizations reuse
existing persistence, authoritative outcome, custody and retrieval Features;
Mneme retains the individual-IU concurrency contribution. Eight existing Motivation
resources receive applicability Associations without satisfaction claims. Native
Assignment, Serving, Access, Flow and Realization express the corresponding
component, service, model and contribution semantics; the qualified Application
Home, model-definition ownership and applicability Associations have explicit
rationales in the native correspondence. Historical persistence/throughput labels
are partial source mappings, with no inferred identity equivalence or exclusive
ownership. All 6,546 predecessor identities and 9,314 predecessor relationship
records are preserved; the IU, Metadata, Representation, Topic, TopicMask,
Dokimasia and Provenance resources are byte-unchanged. Themis provision with
distributed PDP/PEP execution, Kleio audit authority and existing assurance and
provenance responsibilities remain explicit. Source succession is bounded to the
accepted DRP; neighbouring DRPs remain unchanged and outside incorporation scope.

**Final DR-2 disposition:** ACCEPTED. Existing Capability/Feature/Motivation identities and classifications, Information Unit Framework and cross-component responsibility boundaries are confirmed unchanged in their accepted scope.

<a id="dr-3"></a>
## DR-3 — Validation, transport and human review gates

Entry canonical validation **PASS**: 6,546 elements / 9,314 relationships.
Prepare evidence-local XML, reader and narrative candidates using explicit output
destinations. Never use live `project --publish` or Makefile `continuum` while root
still identifies the protected accepted predecessor. Candidate metadata may show
the unchanged root 1.1.22 identity; that does not make candidate bytes an accepted
1.1.22 publication. Required applicable checks include canonical/provenance/native
endpoints, naming/containment/navigation, offline XSD/references, deterministic
generation, exact no-edit review, View preservation, links, meaningful mutation
checks, repository architecture suite and actual supported-Archi transport.

The accepted predecessor retains a strict transport **FAIL**, with explicit
release-specific Solution Architect treatment, generated-folder/label transport
loss and connection-order/default-style qualifications. Those results are not
this candidate's transport result, and their treatment waives no new gate.
Human workbench-tree and reader structural review under Praxis §5 and actual
result confirmation under §6 remain pending. No commit, push, accepted publication,
register completion or release allocation is authorised by successful automation.

**Actual candidate and checks:** canonical validation **PASS**, with **6,557
elements / 9,376 relationships / 462 qualified source predicates**. All **1,304
authored Views** are preserved in the candidate. Canonical provenance, native
endpoints, naming with disclosed qualifications, containment/navigation, offline
XSD/references and exact no-edit workbench review **PASS**. Independent generation
without docs/scripts/Git/historical processing reproduces all 18 candidate
artifacts and the root Manifest exactly. The required Maven architecture suite
passes **90 tests, zero failures/errors/skips**; eight focused Continuum mutation,
projection and workbench tests and four navigation tests also pass. Reader DOM
contract and canonical-link checks pass; no visual acceptance or PDF compilation
is claimed. See the [verification summary](../../evidence/1.1.23/001/after/mnemosyne-incorporation/verification-summary.json)
and [preservation check](../../evidence/1.1.23/001/after/mnemosyne-incorporation/preservation-and-conformance.json).

The [exact evidence-local XML](../../evidence/1.1.23/001/after/mnemosyne-incorporation/candidate/reader/Harmonia-Architecture.xml)
has SHA-256 `eb4c340c25a2d8ee114053a445056b1afaf4b000e38b15c2f0259384e7f5f6a9`.
Reader and narrative candidates are linked from the
[review guide](../../evidence/1.1.23/001/after/mnemosyne-incorporation/review-guide.md).
Candidate metadata displays the unchanged root **1.1.22**, qualified as pending
evidence for the proposed **1.1.23** transition. All **311 protected accepted
publication/register files** and root release authority remain unchanged.

Actual supported **Archi 5.10 import → native save → export PASSed**. However,
**strict transport FAILS: Relationship/View transport drift**: 6,456 relationship
records and 15 View records differ. No element/relationship/View IDs were lost or
added; all element fields and four governed identity/name property values are
preserved. Raw comparison categories are observations, not accepted defect counts
or proof of harmlessness. Changed relationship/View ID sets equal the recorded
predecessor failing sets; only that bounded correspondence is established, not
identical transformations, pixel equivalence, losslessness or inherited waiver.
The [transport Change Review CR-001](../../evidence/1.1.23/001/after/mnemosyne-incorporation/change-review.md#cr-001)
is **On-Hold / Unresolved; reconciliation not authorised**. Returned/native models
and actual execution/comparison evidence are retained. The transport return was
not applied. Candidate preparation/reporting can conclude independently;
dependent finalisation remains stopped for this candidate's explicit Solution
Architect transport disposition, required human structural reviews and §6
exact-result confirmation.

### CR-001 bounded comparison and human-review acceptance

The subsequent [Solution Architect Task](../../evidence/1.1.23/001/before/ai-agent-tasks/unresolved-cr001-transport-reconciliation.md)
states that native Archi-tree and generated HTML-reader visual review for this
candidate are completed and accepted. This supersedes earlier pending statements
for those human reviews only; no CR-001 disposition or whole-HCRL/result confirmation
is supplied. Its scope is retained-evidence comparison within the same iteration,
without canonical, View, template, verifier or release changes.

The [bounded comparison](../../evidence/1.1.23/001/after/cr001-transport-reconciliation/README.md)
and [record evidence](../../evidence/1.1.23/001/after/cr001-transport-reconciliation/comparison.json)
establish that all 9,314 existing relationship source records and returned records
match across releases. All 62 new relationship records preserve checked fields/order
after exact Property-key resolution, with zero internal raw findings. All 1,304 View
records match across releases before and after transport, including the exact 238
style additions/native attributes and fifteen complete sequence transformations.
No additional graph/text/value or presentation-relevant XML transformation is found.

The same seven mechanisms account for all 151,621 raw findings, with zero new or
unaccounted patterns. The 272-observation increase comprises 110 language annotations
on eleven new elements, 73 reconstructible labels and their 73 child-list observations,
and sixteen additional generated source-folder IDs. All prior omitted-folder records
match; 2,553 generated folder IDs are omitted in total, none from authored View hierarchy.
This is actual representation loss with an increased derived-folder count. No
architectural information loss beyond predecessor categories is detected at the
checked dimensions; no lossless result is claimed.

CR-001 remains **On-Hold / Unresolved**. The recommendation is candidate-specific
acceptance with exact evidence and bounded generated-ID, implicit-style-state and
global-order qualifications; only the Solution Architect can disposition it.
Strict transport remains **FAIL**. Pixel equivalence, future platform/preference
effects and candidate second-pass stability are unproved. No architectural correction
is demonstrated as necessary; any required machinery remediation needs separate
authority. Five existing diagnostic boundary tests and the required 90-test
architecture suite **PASS**. No full predecessor investigation or Archi run is repeated.
The [formal comparison Report](../../evidence/1.1.23/001/after/ai-agent-reports/20261111T081809Z-cr001-transport-reconciliation.md)
retains actual activity/results and its independently captured Report-emission event.

**Final DR-3 disposition:** ACCEPTED WITH QUALIFICATION. CR-001 is **Accepted —
Qualified** for **1.1.23 only**. Strict transport remains **FAIL**, with all original
observations and seven mechanisms preserved. This includes 2,553 omitted generated
folder IDs, implicit style-selection state on 238 connections and original global
connection sequence in fifteen Views. No lossless transport, pixel equivalence,
future platform/preference equivalence or future exemption is claimed. Native-tree
and HTML-reader visual reviews and the bounded comparison are accepted. No verifier,
comparison policy, authored View or maintained template is changed, and the return
is never applied. Final publication checks follow the ordinary release controls.

### Verified release allocation and accepted publication

The existing `architecture_release.py bump --expect 1.1.22` mechanism allocated
**1.1.23 / previous 1.1.22** exactly once for this accepted change set. At preflight
all nineteen destination files were absent; no immutable publication was replaced.
All 311 protected predecessor publication/register files are byte-unchanged. The
historical 1.1.19/DR-20 release conflict is already closed in the 1.1.20/1.1.21
accepted successors; no dependent release-path decision remains here.

Ordinary `make continuum continuum-rad` and `make check browser-test` **PASS**:
canonical provenance/identities, containment/navigation, naming with qualifications,
offline XSD/references, root Manifest, both published Exchanges, reader/narrative
freshness, manifest hashes, links and browser DOM checks. The required architecture
suite **PASSes 90 tests**, zero failures/errors/skips. Sources remain **6,557 elements /
9,376 relationships / 1,304 authored Views / 462 qualified predicates**. No production
or application code, authored View, template or strict comparison change occurs.

Both published Exchange copies have SHA-256
`95d03fe1e5e3e9a9c3dc7af92b10277a7ac6ce44e94ad4cc63c5eabc5df06dc0`.
The entire parsed accepted candidate is preserved except its root release metadata
changing from 1.1.22 to 1.1.23. Reader/narrative correspondence verifies all remaining
model/content/fields/order, with only selected generated release metadata, derived
model hash and generated link targets changing. An initial auxiliary evidence check
incorrectly rebased fixed master-template links and omitted the expected model-hash
change; its FAIL is retained, and the corrected field-specific check passes without
changing publication bytes or production comparison rules. No new architectural
meaning or required-gate failure is demonstrated by those harness assumptions.

The [closure evidence index](../../evidence/1.1.23/001/after/release-closure/README.md)
links actual generation/check logs, source/preservation and publication integrity.
Accepted publications: [durable Archi Exchange](../../archimate/published/1.1.23/Harmonia-Architecture.xml),
[HTML reader](../../../documentation/continuum-manifest/published/1.1.23/continuum.html)
and [narrative source](../../../documentation/continuum-rad/published/1.1.23/continuum-rad.tex).
Optional PDFs were not compiled; no pixel equivalence or new actual transport run
is claimed. Strict transport remains **FAIL, explicitly accepted with qualification
for 1.1.23 only**. All original transport observations and prior evidence remain.

<a id="dr-4"></a>
## DR-4 — Naming, contract details and temporal provenance

Use native logical types without inventing formal naming qualifiers or promoting
logical Outcome into an implementation Class merely to fill a name. Preserve
existing unresolved properties and explicitly disclose any new dependent names.
Exact common/domain Reason members, proof thresholds, correlation envelopes,
provider capabilities, TopicMask dialect details and availability/changeover/load
balancing mechanisms remain with their owning detailed contracts and solution
design. No unspecified provider, registry or coordination framework is created.

The Task entry event is unavailable in this interaction's exposed metadata. Its
record/prefix remain unresolved under the Provenance contract; current-clock or
file metadata cannot supply it. Solution Architect treatment is required before
closure. Resulting Report emission will use the existing evidence tool's actual
completed-emission event capture.

**Actual qualifications:** the six new ApplicationFunctions, four new
ApplicationServices and generic Outcome carry explicit null formal canonical
names because no governed qualifier is established. Outcome contributes one new
information-name qualification, **17 total including 16 inherited**. This records
missing authority without inventing formal names or changing existing ones.
Accepted closed status semantics and closed common/domain Reason governance are
represented; enumeration members and detailed proof/correlation contracts remain
unallocated. These qualifications, the Task event/prefix and the exact resulting
representation require Solution Architect treatment before closure; no agent
waiver or implied acceptance is recorded.

The CR-001 follow-up Task's instruction-entry event/prefix is also unavailable and
explicitly unresolved. Its attributable human-review acceptance retains its stated
scope; no guessed Task or visual-review time is assigned.

**Final DR-4 disposition:** ACCEPTED WITH QUALIFICATION. Eleven new formal names
remain explicitly null/unresolved; Outcome adds one information-name qualification,
17 total including sixteen inherited. Detailed Reason members, proof/correlation,
provider and TopicMask/selection contracts remain deferred to their owners. All
unavailable Task instruction-entry events/prefixes remain visibly unresolved,
including the closure instruction, under this Task's explicit provenance treatment.
Acceptance neither supplies missing timestamps/naming authority nor claims full
temporal conformance. No remaining qualification requires another architectural
adjudication absent a new material discrepancy.

<a id="dr-5"></a>
## DR-5 — Bounded supporting Minutiae

Disclosed group: exact operative Task and accepted input; baseline/preservation
pins; source/element/relationship and MP correspondence; purpose-selected validation,
generation and transport evidence; formal Report; generated root Manifest and
evidence-local projections; iteration-local review links. Substantive semantics
remain DR-1/DR-2 and qualifications/acceptance limits DR-3/DR-4.

The only maintained test-source change updates the expected canonical population
from `(6546, 9314, 462)` to `(6557, 9376, 462)` in
`enterprise-architecture/tooling/tests/test_continuum.py`; tests and strict
comparison policy are not weakened. No production tooling or application code is
modified. Evidence-local projections and navigation/Manifest regeneration are
supporting output, not accepted-release publication. Git remains uncommitted on
the entry branch/HEAD, with the original untracked accepted source preserved.
No release allocation, register completion, commit or push was performed.

The CR-001 follow-up adds its formal Task/Report, before-review snapshots,
input/preservation pins, candidate diagnostic output, bounded cross-release/per-record
correspondence and tests within **1.1.23/001**. Prior incorporation Reports and every
original transport outcome/input remain unchanged. Review cross-references/status
record actual findings and explicit human-review acceptance. No DR/iteration or
release allocation is introduced; substantive conclusions remain in DR-3/DR-4.

```text
Minutiae-Accepted: Yes
Comment: Explicitly accepted in final closure Task; preserve evidence and qualifications.
```

**Final DR-5 disposition:** ACCEPTED, including the disclosed supporting Minutiae and required closure evidence/provenance maintenance within this same 1.1.23/001 boundary.

**Registration and Git state:** This HCRL is registered as complete at
`design/register/1.1.23/hcrl-1.1.23-001.complete.md`; the retained review-history copy
is [here](../../review/1.1.23/hcrl-1.1.23-001.pending.md). At registration the accepted
change set is uncommitted on `Mneme-and-Mnemosyne-Build`, entry HEAD
`fde98cde3c44b4f4b89e7f3a29765e1a44fca515`. Commit/push is expressly authorised after
these passed controls, and terminal actual SHA/remote/clean-tree facts are retained
in the closure Report and Git receipt linked from the closure evidence index.
Registration consumes no further release; no successor iteration is created.
The completed register remains immutable through that provenance maintenance.
