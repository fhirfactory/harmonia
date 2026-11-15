<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Exact baseline, accepted inputs and source-stage qualifications | ACCEPTED — subject to exact reader/native-model confirmation | Source bytes and qualifications preserved; final human review pending. |
| [DR-2](#dr-2) | InformationUnit, itinerary and peer OperationalMetadata reconciliation | ACCEPTED — subject to exact reader/native-model confirmation | Nine-element refinement satisfactory; no further architectural redesign authorised. |
| [DR-3](#dr-3) | Immutable Topic/TopicMask model and neutral selectors | ACCEPTED — subject to exact reader/native-model confirmation | Preserve approved semantics, current Topic identities and relationships. |
| [DR-4](#dr-4) | Revisions, portable Content and accepted contract preservation | ACCEPTED — subject to exact reader/native-model confirmation | Preserve IU-01–IU-13, five contracts, MI-05 full coverage and all safeguards. |
| [DR-5](#dr-5) | Exact-candidate validation, strict transport and outstanding checks | QUALIFIED — CR-001 unresolved; human review pending | Strict FAIL retained; inherited test FAIL and incomplete TIMEOUT qualified; Java checks deferred for this architecture-only incorporation. |
| [DR-6](#dr-6) | Detailed deferrals and provenance qualifications | ACCEPTED WITH QUALIFICATIONS | Original time/prefix and Topic.java before-hash remain unavailable; detailed decisions and operational 0.5 Petasos gate deferred. |
| [DR-7](#dr-7) | Bounded supporting Minutiae | Minutiae-Accepted: Yes | Bounded supporting evidence/corrections accepted; no CR-001 waiver or final exact-result acceptance. |

# HCRL 1.1.25 / 001 — InformationUnit Final.002 governed reconciliation

**PENDING — architectural reconciliation accepted in principle with the recorded
qualifications; explicit native-tree/reader review and final exact-result acceptance
remain outstanding. CR-001 remains FAIL — QUALIFIED / UNRESOLVED. No release allocated.**

**Current attributable review:** Mark Hunter, Solution Architect, issued the
[review disposition](../../evidence/1.1.25/001/before/ai-agent-tasks/unresolved-solution-architect-review-disposition.md)
after reviewing this HCRL and the reconciliation Report. DR-1–DR-4 are accepted
subject to exact reader/native-model confirmation; DR-5/DR-6 have the bounded
treatments recorded below; DR-7 is accepted. No further architectural redesign is
authorised. The [exact reviewed HCRL](../../evidence/1.1.25/001/before/review-disposition/hcrl-1.1.25-001.reviewed.md)
preserves the prior checkpoint. Preparation-stage statements below about required
dispositions describe that history; each current disposition block governs the
present standing without changing historical evidence or actual validation outcomes.
The follow-up instruction-entry timestamp is unavailable and is not reconstructed;
its content and attribution are retained separately from the evidence-capture time.

The [current review checkpoint](../../evidence/1.1.25/001/after/review-disposition/review-checkpoint.md)
identifies the unchanged Exchange, reader and retained native model. Recording this
disposition does not advance the iteration or supply final exact-result acceptance.
The Solution Architect explicitly directs a stop before publication, release
increment, HCRL completion, commit or push.

Review basis: Mark Hunter, Solution Architect, issued the [operative Task](../../evidence/1.1.25/001/before/ai-agent-tasks/unresolved-information-unit-final002-reconciliation.md)
to reconcile accepted conceptual inputs into canonical Markdown and stop at the
appropriate acceptance gate. Entry and current branch are
`Mneme-and-Mnemosyne-Build`; HEAD is
`626d72fdb2c26768114a6c7f0a2f5845875e5b2f`. The worktree was dirty, including
Java work and untracked DRPs; no reset, discard, code edit or accepted-input rewrite occurred.
Root `architecture-release.json` is **1.1.24 / previous 1.1.23**, corroborated by the
[completed 1.1.24 HCRL](../../register/1.1.24/hcrl-1.1.24-001.complete.md).
Proposed **1.1.24 → 1.1.25** is the ordinary next patch review identity, not an
allocation or reservation. Recheck the accepted predecessor at any later acceptance.

The [source reconciliation](../../evidence/1.1.25/001/after/source-reconciliation.md)
provides exact sources, assertion/decision correspondence, selected canonical
inventory, qualifications and deferrals. The [review guide](../../evidence/1.1.25/001/after/review-guide.md)
identifies the exact candidate and inspectable source/projection/native model.
Formal Task/Report records and selected evidence belong to **1.1.25/001**.
The completed [formal Report](../../evidence/1.1.25/001/after/ai-agent-reports/20261115T050805Z-information-unit-final002-reconciliation.md)
records this bounded activity; its completion does not accept the architecture.
Directory placement, successful tooling and approved inputs do not accept this result.

<a id="dr-1"></a>
## DR-1 — Exact baseline, accepted inputs and source-stage qualifications

The Task explicitly identifies accepted IU Final.001 and newly accepted Final.002,
approved Topic/TopicMask Final.001 and explicitly approved OM-06–OM-10, with the
accepted 1.1.24 Interaction/Cache and earlier relevant DRPs. Final.002 was already
at the requested offline-design path; its predecessor was not overwritten.
The Topic/TopicMask source was absent at initial inventory, then became observable
at the exact file locator supplied by the user during preflight. It is now retained
with the other untracked accepted inputs. The requested OM draft.002 filename was
absent; actual successor is OperationalMetadata Final.001. No missing draft is invented.

Whole-source acceptance of IU Final.001 is established by completed HCRL 1.1.22;
Mnemosyne Final.001 by completed 1.1.23; Interaction/Cache by completed 1.1.24.
Current Task authority supplies the bounded new conceptual approval. Topic source
text still says pending complete approval; OM Final.001 header says approved but its
register/conclusion still request approval. These facts and their unchanged bytes
are preserved, not repaired through filenames or retrospective rewriting. The
current instruction's accepted semantic scope controls the bounded incorporation.
Final.002's internal proposed-amendment wording does not reopen its explicit approved
scope or accept the actual resulting representation.

The PetasosParcel file is **not approved**, despite a Final.001 filename. It was
neither incorporated nor treated as an accepted source. Mark-ChatGPT-Context was
not found in the repository or exposed current agent workspace, including hidden
files; its optional absence was not replaced by remembered architecture.

**Actual application:** before-state source/user/protected-file pins and three unique
untracked accepted inputs are preserved. [Preservation](../../evidence/1.1.25/001/after/preservation.json)
confirms all 29 pinned user files and 321 protected publication/register/release/
navigation files unchanged, with the same HEAD. This HCRL and candidate remain
uncommitted and are not recoverable from that predecessor commit.

**Current Solution Architect disposition — DR-1:** **ACCEPTED**, subject to
confirmation of the exact candidate through reader/native-model review. Preserve
the accepted source decisions, identities and qualifications. No source-stage
wording or missing provenance is retrospectively repaired.

<a id="dr-2"></a>
## DR-2 — InformationUnit, itinerary and peer OperationalMetadata reconciliation

Apply the approved conceptual allocation without introducing a ManagedIU, nested IU,
wrapper, new superclass or one-Class-per-box design. The existing Business IU and
logical Calliope InformationUnit Class remain two architectural views of the same
governed atomic IU, with one IU UUID and one InformationRepresentation. IU Topic
UUID remains distinct. Four InformationCoordinates remain **PersistenceGateway,
GatewayEndpoint, InformationLocation, InformationOwner**, independent of security.

InformationItinerary is now explicit as a **distinct conceptual information concern**
in the existing Metadata definition: actual/intended participation and passage,
separate from coordinates, origin, business Journey, provenance evidence and an
individual handoff. Participant names are illustrative conceptual candidates.
Each relevant parcel carries its own complete itinerary instance for its handoff;
complete structure does not imply complete knowledge, nor a shared mutable object.
This is solely the approved common boundary; no Petasos/Parcel element or framework
design is changed. Historical/intended/update details remain bounded design.

OM-06–OM-08 place Mneme operational revision, per-manifestation Active Generation,
cache/working facts and Mneme–Mnemosyne operation/handshake facts conceptually in
**associated peer OperationalMetadata**. Association is relative to the IU,
manifestation or operation as appropriate, not automatic IU constituency or physical
co-persistence. OM-09/10 retain IU authority/identity, logical ownership/location,
IU-required security, optional supplied relationships, governed information lifecycle,
representation metadata and Content. Existing façade projections remain supported;
no accepted field, visibility, concurrency, durability, recovery or retention assurance
is removed. The same Features keep their responsibilities.

**Actual application:** nine existing elements refined; **zero new elements or
relationships**. Existing Metadata/Representation Composition and every native
relationship remain byte-for-field unchanged. New concerns are represented within
the existing subject definitions and responsibility boundaries; standalone logical
Class/association allocation remains open rather than manufactured. The existing
Feature ancestry is inventoried from actual Composition edges and reused unchanged.
The three-version/four-context and Topic-renaming predecessor excerpts affected by
new accepted meaning remain unchanged inside scoped supersession wrappers.

AX-03/04 preserve native models, portability and reuse; AX-05/06 distinguish active,
durable and information authority; AX-07 preserves security/audit; AX-08/09 preserve
meaningful evidence/operational retention; AX-10/11 preserve distributed correctness
without weaker guarantees; AX-12/13 preserve governed access and boundaries;
AX-14/15/17 preserve revisions, outcomes, uncertainty and accepted-input versus
pending-result distinctions; AX-18 prevents routing/observation from conferring
domain authority. No axiom or capability allocation changes.

**Current Solution Architect disposition — DR-2:** **ACCEPTED**, subject to
confirmation of the exact candidate through reader/native-model review. The
nine-element refinement is satisfactory. Preserve existing identities/relationships,
the four InformationCoordinates, InformationItinerary distinctions and peer
OperationalMetadata allocations. No further architectural redesign is authorised.

<a id="dr-3"></a>
## DR-3 — Immutable Topic/TopicMask model and neutral selectors

Reuse existing common Topic/TopicMask DataObjects and their identity/relationships.
Topics are immutable, with a full UUID, explicitly sequenced root-to-leaf typed
qualifier/name entries and derived names. Reject gaps/duplicates/ambiguous ordering;
normalise absent qualifier by absolute position. Preserve distinct runtime field
roles rather than silently equating them to Continuum canonicalId/displayName.
Substantive replacement creates a different Topic UUID; **no Topic.Version**.
All existing specialised Topic files/IDs/names/relationships remain unchanged.

TopicMasks are immutable with a separate UUID independent of matching; each has
exactly one basis, name-based or canonical qualifier/name-based. Regex is element-wise.
Exact requires matching depth; Inclusive admits descendant levels. Default unrestricted
intent is retained with exact root/interchange and cross-language regex conventions
explicitly deferred. No versionRegex or whole-path regex is introduced.

InformationUnitMask, OperationalMetadataMask and PetasosParcelMask remain separable
neutral selectors, composing/referencing TopicMasks for Topic-valued criteria.
No unapproved field/operator/cardinality/absence/snapshot catalogue, subscription
implementation or Passport subclass is introduced. Matching grants no disclosure,
execution, publication, persistence or routing authority; Themis remains controlling.

**Actual application:** the two existing common models were refined; earlier bounded
renaming wording is retained as superseded source history. No registry service,
Topic-instance migration, inheritance edge or alternative selector taxonomy is added.

**Current Solution Architect disposition — DR-3:** **ACCEPTED**, subject to
confirmation of the exact candidate through reader/native-model review. Preserve
the accepted Topic/TopicMask and neutral-selector semantics and existing identities
and relationships. No additional model or implementation work is authorised.

<a id="dr-4"></a>
## DR-4 — Revisions, portable Content and accepted contract preservation

The authoritative persisted IU revision is Mnemosyne-established/returned and stays
with the IU, distinct from Mneme operational revision, Content Structure Version,
optional native Content instance version and the Active Generation manifestation
identity. The predecessor's three-version wording is not an exhaustive denial of
the already accepted authoritative revision role. No new counter, native version
equivalence or payload mutation is fabricated. Historical accepted DRPs are unchanged.

InformationRepresentation retains ContainerMetadata, ContentMetadata and portable
textual/binary/by-reference Content, with no mandatory Container.Id/Content.Id UUIDs.
Java objects may be working manifestations, not the sole interchange/persistence
contract. Native semantics, accepted large-binary reference exception and new IU for
a governed transformation remain. No wire format or physical schema is selected.

All IU-01–IU-13 remain effective, with only the specifically approved semantic
relocations. The five logical Mneme–Mnemosyne contracts and their IDs/definitions
are byte-unchanged. Summary still excludes exactly Content, its opaque reference
remains in ContentMetadata, and accepted operational façade views/guarantees remain.
No arbitrary operational observation is silently added to authoritative search.
MI-05 remains **complete required coverage by the union of eligible available
supported criteria**, not overlap. Preserve exact-version access, higher actual
authoritative version returns, full/summary authorisation, no-loss durable work,
multi-site mutation Success, factual Indeterminate, Kleio audit/Provenance and all
AV/FC/MI/MC qualifications. Neither cache nor transient operational status proves
durability or removes evidence-retention obligations.

**Actual application:** current definitions and explicit Store/Façade/cache compatibility
statements express these boundaries. Independent preservation confirms the full
predecessor graph and all service sources unchanged. No Java files were edited,
Junie task dispatched, persistence schema changed or deferred Petasos work initiated.

**Current Solution Architect disposition — DR-4:** **ACCEPTED**, subject to
confirmation of the exact candidate through reader/native-model review. Preserve
all accepted semantic decisions and Mneme–Mnemosyne contracts, including the
identity, revision, portability, security, durability and provenance safeguards above.

<a id="dr-5"></a>
## DR-5 — Exact-candidate validation, strict transport and outstanding checks

Current exact candidate is [candidate-v2 Exchange](../../evidence/1.1.25/001/after/candidate-v2/reader/Harmonia-Architecture.xml),
SHA-256 **`7d1fe1f3d23aa39d87cedb0779c88772b4e04dafa5b8e32efd1c72863dae50c8`**.
Original candidate and its checks remain separate. V2 only separates two supersession
markers from preceding prose with blank lines; no semantic/graph change. Both use
root metadata **1.1.24**, which does not make them accepted 1.1.24 publications.
All generation uses explicit evidence-local destinations. No `--publish`, accepted
publication, release bump, HCRL completion, commit or push occurred.

**PASS:** canonical identity/provenance/native endpoint/naming/containment/navigation
and offline XML schema/reference validation, 6,563 elements / 9,427 relationships /
462 qualified predicates / 8,459 source bindings; deterministic Manifest and candidate
reader/narrative freshness; exact no-edit workbench review with zero element,
relationship/View or unresolved deltas; current rendered source links and reader DOM
smoke. Independent reproduction produces **18 identical artifacts** (Manifest plus
five reader and twelve narrative outputs) without Legacy-RAD, scripts or Git.
Independent comparison against accepted 1.1.24 preserves every relationship,
all **1,304 authored Views**, organisations, IDs and names; exactly nine element
records differ. Existing source naming qualifications remain unchanged.

**Actual Archi import → native save → export PASS.** An initial sandbox SWT display
initialisation failure is preserved separately; a fresh isolated config/workspace/home
run with local display access completed for exact candidate-v2. No interactive human
inspection or returned-model application occurred. **Unmodified strict transport
verification FAILS: Relationship/View transport drift.** All 6,563 element identifiers,
types, specialisations, names, documentation and four governed identity/name property
values survive; no relationship/View identity is lost/added. **6,507 relationship
records and 15 View records differ.** Raw classifications and exact input/return/log/
native model are retained in [transport evidence](../../evidence/1.1.25/001/after/transport-summary.json).
No lossless or pixel-equivalent transport is claimed. Prior 1.1.24 qualified acceptance
supplies no waiver for this exact candidate. Required disposition is recorded as
On-Hold/Unresolved in the [transport Change Review](../../evidence/1.1.25/001/after/change-review.md).

The broader existing Python Continuum test command reached its **240-second TIMEOUT**
during the deterministic projection/publication test after positive completed tests;
it is not reported as suite PASS or implementation FAIL. Narrow corruption tests
exclude the expensive projection/publication scenarios already exercised by direct
generation/reproduction. Their exact result is in the verification summary; they
do not erase the broader TIMEOUT. No production code is changed to address a harness
budget limit.

The Task excludes Java/build activity. AGENTS.md §4 ordinarily requires the Maven
architecture suite for repository modifications; it is **NOT PERFORMED** here.
This candidate supplies no waiver or claim of current Java-test conformance, especially
with pre-existing uncommitted user Java work. Explicit Solution Architect treatment
of that check/scope boundary is needed before closure; no affected acceptance or
build proceeds by assumption.

**Focused checks: FAIL — seven passed, one failed.** The reserved-historical-identity
mutation is rejected, but its test expects error text `identity correspondence`
instead of the actual `Inconsistent predecessor identity lineage:
5f6d333e94634420b8a8bbf8b9cb6e2a`. An isolated control restores the exact accepted
canonical sources from entry HEAD with unchanged maintained tooling: the same test
**FAILS identically**. [Accepted-baseline control](../../evidence/1.1.25/001/after/accepted-baseline-test-control.json)
and both logs preserve that evidence. This establishes an inherited diagnostic-text
expectation failure, not a new Final.002 semantic regression or a passing test.
No validator/test change is authorised or made. The broader suite had completed
18 positive tests before TIMEOUT and had not reached this alphabetically later test.

[Bounded actual-record correspondence](../../evidence/1.1.25/001/after/predecessor-transport-correspondence.json)
also establishes all 9,427 relationship and 1,304 View records equal to accepted
1.1.24 both before and after actual transport. This bounds the current failure but
does not accept it or replace raw classifications. Installed editor bundle version
is **5.10.0.202608252016**, established from the current installation's manifest.

**Preparation-stage gate (retained history):** disposition this candidate's strict FAIL and test/check limits;
perform/confirm any applicable §5 human native-tree/reader structural review and §6
exact-result acceptance, or give explicit bounded treatment. Graph/navigation structure
is unchanged and inspectable evidence is supplied; agent inspection cannot self-confirm
the human checkpoint. No finalisation occurs while these remain untreated.

**Current Solution Architect disposition — DR-5:**

1. **Strict transport: FAIL — QUALIFIED / UNRESOLVED (CR-001).** The independent
   predecessor-correspondence evidence is accepted as evidence bounding the issue,
   not proof of successful strict transport. Preserve the actual FAIL and raw
   findings; do not weaken the verifier or reclassify the result. The historical
   transport Change Review and validation evidence remain unchanged. No returned
   model application or CR-001 waiver is authorised.
2. **Focused corruption test: bounded qualification accepted.** Retain actual
   **FAIL** and the accepted-baseline reproduction proving the inherited
   diagnostic-text expectation mismatch. Seven focused passes do not erase it.
3. **Python suite: TIMEOUT accepted as incomplete execution.** Separate successful
   direct generation/reproduction checks support this treatment; the suite is not
   a complete PASS.
4. **Java ArchUnit/Maven: bounded deferral authorised** for this architecture-only
   incorporation. Actual outcome remains **NOT PERFORMED**. No Java changes or
   implementation acceptance are authorised; preserve pre-existing user Java work.
5. **Human review: PENDING.** Native-tree and reader-facing structural review must
   be explicitly confirmed by the Solution Architect. DR-1–DR-4 acceptance remains
   subject to this checkpoint and final exact-result confirmation under Praxis §6.

**Actual disposition-recording verification:** the canonical diff still matches the
retained reviewed patch. Exchange SHA-256 remains
`7d1fe1f3d23aa39d87cedb0779c88772b4e04dafa5b8e32efd1c72863dae50c8`;
reader SHA-256 remains
`34375a900311a30cc18fc415589fc03d2744157c155e0dc338215eeab27847b3`.
The retained native review artifact is `after/archi-transport-v2/candidate.archimate`,
currently pinned as
`3d201b3a5905456698863c423376f6fbf362c0d11eb7e6936317d4c35012f9cf`.
The [checkpoint and verification](../../evidence/1.1.25/001/after/review-disposition/review-checkpoint.md)
record exact correspondence, unchanged historical evidence and the remaining human
gate. No candidate regeneration, canonical modification or human inspection occurred.

<a id="dr-6"></a>
## DR-6 — Detailed deferrals and provenance qualifications

Preserve detailed design boundaries: exact standalone logical concept/association
allocation, interface projections and physical metadata housing/migration; historical
versus intended itinerary/update mechanics; Origin/itinerary/Provenance evidential
relationships; higher-mask field/operator/cardinality/absence/snapshot semantics;
regex dialect/root/interchange encoding; provider layouts and version algorithms.
No universal mandatory fields, new capability, audit ledger or native version
equivalence is inferred. PetasosParcel/Framework remains deferred until operational
Mneme/Mnemosyne **0.5** build quality, except the approved complete-instance itinerary
cross-reference. All inherited qualifications retain their standing.

The operative Task and source-locator follow-up instruction-entry event times are
**UNRESOLVED**, including the Task filename prefix, because the exposed interaction
has no attributable original timestamps. Current time, file/commit metadata and later
capture are not substitutes. Preserve this condition for explicit Solution Architect
treatment before closure. Report production is captured separately by the maintained
emitter at actual completed Report emission. Earlier releases' temporal qualifications
do not automatically accept this iteration's unresolved time.

**Actual evidence check:** [iteration layout/event-prefix check](../../evidence/1.1.25/001/after/iteration-evidence-check.json)
returns **UNRESOLVED (exit 2)** with no structural errors, solely for the original
Task time/prefix. The Report has an actual production time and matching UTC filename.
This is not a full temporal-conformance PASS or a closure disposition.

**Preservation-evidence limit:** the original dirty inventory records tracked
`Topic.java` as modified, but the original hash capture omitted that first path
after stripping a leading porcelain-status space. All 29 pinned user files remain
hash-identical; independent before/after byte proof for this additional tracked file
is unavailable. No Java edit was performed. [Exact qualification](../../evidence/1.1.25/001/after/user-work-preservation-qualification.json)
and [final scope check](../../evidence/1.1.25/001/after/final-git-state.json) preserve
the limit without changing before-state evidence or manufacturing a missing hash.
Include this limitation in exact-result/provenance acceptance.

**Current Solution Architect disposition — DR-6:** **ACCEPTED WITH QUALIFICATIONS.**
Accept the recorded unavailable original Task/follow-up instruction-entry times and
Task filename prefix; the missing original before-state hash for already-modified
`Topic.java`; and the remaining detailed implementation/modelling decisions.
PetasosParcel/Framework work remains deferred until Mneme/Mnemosyne reaches
operational **0.5** build quality. These are bounded qualifications, not restored
evidence or completed detailed design. Do not reconstruct missing provenance or
represent incomplete evidence as verified. Retain all original results and source
limits; exact-result human acceptance remains separate and pending.

<a id="dr-7"></a>
## DR-7 — Bounded supporting Minutiae

The disclosed group is the formal operative Task/Report, before-state/user/source/
protected-file pins, retained unique accepted inputs, selected inventory and assertion
correspondence, preservation/reproduction/check results, raw failed transport and
timeout evidence, review guide and generated root Manifest/candidate artifacts.
An initial duplicate source-binding selector was rejected by the validator and
corrected by explicitly scoping each contribution to its actual existing subject;
no architectural identity or semantics changed. The two supersession-marker spacing
corrections explain candidate-v2. Substantive interpretation remains DR-1–DR-6.

```text
Minutiae-Accepted: Yes
Comment: Mark Hunter, Solution Architect, accepts the disclosed supporting evidence and mechanical corrections within the stated bounded scope, in the retained current review disposition.
```

**Current Solution Architect disposition — DR-7:** this disclosed evidence group is
accepted within its stated bounded scope. It does not waive CR-001, authorise
architectural changes outside the agreed scope or override the separate exact-result
human-review requirement. It supplies no release allocation, HCRL completion,
publication, commit or push authority.
