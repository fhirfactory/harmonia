<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | MP-01–MP-08 and five-tier incorporation | Accepted source semantics; ADD/reconciliation authorised | Current operative Task explicitly accepts the complete Final.001 DRP. Actual incorporated candidate confirmation pending. |
| [DR-2](#dr-2) | Existing hierarchy, Motivation and component boundaries | Reconciliation authorised within issued scope | Exact mapping and resulting relationships require review. |
| [DR-3](#dr-3) | Canonical, projection, transport and human review gates | Not recorded for resulting candidate | Prior release qualifications supply no new waiver. |
| [DR-4](#dr-4) | Naming, contract details and temporal provenance | Not recorded for resulting candidate qualifications | Preserve missing vocabulary, deferred mechanisms and unavailable Task event explicitly. |
| [DR-5](#dr-5) | Bounded supporting Minutiae | Not recorded | Supporting evidence and generated projections only. |

# HCRL 1.1.23 / 001 — Mnemosyne authoritative persistence incorporation

**PENDING — authorised incorporation candidate; resulting-state acceptance, release allocation and publication pending.**

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

```text
Minutiae-Accepted: [awaiting Solution Architect: Yes | No]
Comment: Solution Architect disposition not yet recorded.
```
