Working report copy. The authoritative governed Report is [20261103T011842Z-mneme-post-create-availability-amendment.md](../../enterprise-architecture/design/evidence/1.1.19/001/after/ai-agent-reports/20261103T011842Z-mneme-post-create-availability-amendment.md). This copy supplies no architectural authority.

Report-Produced-At: 2026-11-03T01:18:42.457126Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# DR-18/DR-19 — bounded Mneme availability amendment Report

**Outcome:** the reviewed ADD candidate is refined with
`Cache{InformationUnit}.postCreateAvailability` in Mneme's existing canonical
component definition. Exactly three permitted values and the agreed invariants
are represented. **No elements or relationships are added, removed or reallocated.**
The other five reviewed definitions, all existing candidate contributions and
Mnemosyne's persistence boundary remain unchanged. The amended candidate is ready
for **consolidated Solution Architect review**, with the qualifications below;
this is not formal candidate acceptance or DR/HCRL closure.

Corresponding [follow-up Task](../../enterprise-architecture/design/evidence/1.1.19/001/before/ai-agent-tasks/unresolved-mneme-post-create-availability-amendment.md)
preserves the operative issued wording and review recommendations. Original Task
instruction-entry timestamp/filename prefix remain UNRESOLVED for Solution Architect
treatment before closure. Report production is independently captured by the maintained
emission machinery. The [earlier ADD Report](../../enterprise-architecture/design/evidence/1.1.19/001/after/ai-agent-reports/20261103T000246Z-dokimasia-provenance-add.md)
and all earlier evidence/production times remain unchanged.

## Six reviewed definitions and dispositions

These are the attributable Task's review recommendations, **not formal Solution
Architect candidate acceptance**:

| Definition | Review disposition | Actual treatment |
| --- | --- | --- |
| Provenance — DataObject | Recommended acceptance unchanged | Byte-identical, including all six original unresolved design questions. |
| Provenance Client Library — ApplicationComponent | Recommended acceptance unchanged | Byte-identical; no contract, API, deployment or selected policy added. |
| Dokimasia — ApplicationComponent | Recommended acceptance unchanged | Byte-identical; Provenance framework/lifecycle and independent Assurance intact. |
| Kleio — ApplicationComponent | Recommended acceptance unchanged | Byte-identical; Audit management/historical evidence intact. |
| Mneme — ApplicationComponent | Amendment required | All original body text preserved with an inserted candidate policy subsection and one appended provenance contribution. |
| Mnemosyne — ApplicationComponent | Recommended acceptance unchanged | Byte-identical; persistence, versioning, outcomes and Search Management intact. |

HCRL DR-18 records the bounded amendment and actual result; DR-19 records the review
recommendations and preserved Assurance qualifications. No new DR is allocated.
DR-1–DR-17 text/dispositions and the prior DR-18/19 review history remain unchanged.

## Exact Mneme amendment

Mneme SHALL interpret and apply `Cache{InformationUnit}.postCreateAvailability`
within its managed cache environment. The property governs required post-creation
InformationUnit availability, with precisely these values:

| Value | Architectural semantics |
| --- | --- |
| `Immediate-And-Distributed` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache and proactively distributed across the applicable cache environment. |
| `Immediate` | Following creation, the InformationUnit is immediately materialised in the originating Mneme cache without requiring proactive distribution. |
| `OnRequest` | The InformationUnit is durably persisted without requiring proactive cache materialisation or distribution. Materialisation occurs on demand through governed retrieval. |

Every value preserves Mnemosyne-established/reported authoritative durable persistence,
existing governed retrieval/search, separation of cache and durable authority,
InformationUnit identity/version/lifecycle/governance and existing durability,
reliability, recovery, security and authorisation obligations. **OnRequest means
on-demand materialisation, not delayed persistence or delayed accessibility.**
Mnemosyne's existing Search Management already searches authoritative managed
persistence independently of complete transient cache population. No new service,
persistence mechanism, store or technology is introduced.

**Representation:** the existing component Markdown body carries this normative
architectural requirement, permitted-value domain, invariants and unknowns, consistent
with Continuum Representation and Incorporation Praxis. `Cache{InformationUnit}`
is the Task's architectural notation; no new Class, enum element, Requirement
element, specialisation, graph edge or static/runtime selected Property value is
needed. No new canonical/Exchange identity is allocated. This stays within Task
§6.2's existing-construct authority; no additional representation adjudication is
required by this treatment. Formal naming/schema contracts are unchanged.

The [reconciliation](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/reconciliation.md) records
source discovery, the six recommendations, representation rationale and consistency
with canonical AX-05/10/11/14/15/17. AX-05 retains distinct active/durable domains;
AX-11 does not require every resource to reside in cache; governed accessibility
is preserved. Historical Cache Transparency and Sub-ms Cache Access material remain
historical, without promoting their standing or deriving a timing requirement.
No authoritative architectural conflict was identified within this bounded review.

## Originating and preserved state

Origin is the current **uncommitted** DR-18/19 candidate, not an accepted baseline
transition. Its XML SHA-256 is `13b0c5cf52d092e70a9f57e6d95a8ebc1d3fd712dfb7c60dfb92b6c38d7ecebf`.
The [retained before state](../../enterprise-architecture/design/evidence/1.1.19/001/before/mneme-post-create-availability/source-state.json),
[exact Mneme preimage](../../enterprise-architecture/design/evidence/1.1.19/001/before/mneme-post-create-availability/Mneme-before.md) and
[exact HCRL preimage](../../enterprise-architecture/design/evidence/1.1.19/001/before/mneme-post-create-availability/hcrl-before.pending.md)
preserve unique input that the earlier Git HEAD does not contain. Current canonical
Markdown remains authority; the XML supports exact correspondence only.

[Preservation evidence](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/preservation.json) confirms:

- **1,996 existing identities**, classifications, specialisations, names, standing,
  bindings and original contributions retained; no element added/removed/retyped.
- **2,246 native/projected relationships and 1,288 Views**, plus catalogue folders,
  exact and unchanged; all ten original ADD relationships preserved.
- All **1,995 other canonical element files** byte-identical. Mneme's prior body is
  recovered exactly by removing only the inserted policy subsection; its prior metadata is unchanged except one appended
  attributable candidate-provenance record.
- Mnemosyne and the other four unchanged reviewed definitions byte-identical;
  authoritative persistence/version/outcome/Search Management boundaries are unchanged.
- Root Manifest, navigation, maintained View template, prior candidate/evidence,
  governance/tooling and 204 protected publication
  files unchanged. No validator/comparator/test fixture or implementation is modified.

New derived review evidence includes the [amended XML](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/Harmonia-Provenance-availability-amended-candidate.xml),
[reader](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/reader-candidate/continuum.html) and narrative
LaTeX source. Candidate population stays **1,996 elements / 2,246 relationships /
1,288 Views**, SHA-256 `ca70443bf8bec1e4546b45c9fb454c9925b87325a79532b2145e14256d79bb7b`.
Only Mneme differs in the semantic projection; older candidate artifacts are not
overwritten. Root architecture version remains **1.1.19 / predecessor 1.1.18**.
No accepted-baseline publication is generated or replaced.

## Preserved and additional unresolved matters

All six original DR-18/19 questions remain recorded and unresolved: original versus
linked retry Pragma; detailed execution attempts; synchronous/asynchronous Provenance
publication; acknowledgement/recovery; schemas/cardinalities/lifecycle states;
detailed client-library contracts/deployment. The amendment resolves none of them.
Existing independent Assurance, Praxis basis/Journey observed subject, finite
Dokimasia and no separate own-AuditEvent qualifications remain intact.

The mechanism determining the policy remains unresolved. Per-creation supplied
value and governing metadata are possibilities; neither is selected and no precedence
is implied. No default, additional value, timing threshold, replication topology,
cache implementation, API or deployment is introduced. Provenance is a natural
`OnRequest` candidate, not a mandated value for every instance. Creation/publication
ordering, durable acknowledgement and recovery contracts are not decided by cache
materialisation wording. No additional unestablished architectural representation
issue was discovered. Missing Task event times/prefixes remain a separate governance
qualification for treatment before closure; no historical time is reconstructed.

## Verification results and limits

| Check | Result |
| --- | --- |
| Canonical schema, identity, naming, standing, provenance, bindings, navigation | PASS; population unchanged. |
| Native endpoint applicability, containment, offline XML schema/references | PASS; zero identity allocation. |
| Deterministic projection; reader/narrative source generation and staleness | PASS; protected publications unchanged. |
| Isolated maintained-input generation | PASS; 19 identical artifacts without historical docs/scripts/Git/processing. |
| Root Manifest staleness, canonical links and browser DOM | PASS; Manifest bytes unchanged. |
| No-edit canonical workbench self-review | PASS; no semantic/graph/View amendments from identical exact candidate. |
| Exact prior-state/component/graph/View/folder preservation | PASS; only Mneme content/provenance amended. |
| Full architecture-tooling mutation/conformance suite | PASS; 87 tests in 365.903 seconds, exit 0, before final subsection placement; relevant final checks rerun below. |
| Final focused publication/projection/round-trip tests | PASS; 4 tests in 129.339 seconds, exit 0. |
| Java ArchitectureTest suite | PASS; 90 tests, 0 failures/errors/skips, exit 0. |
| Real installed Archi 5.10 import/native save/export | PASS, exit 0; schema-valid output. |
| Strict Archi transport verifier | **FAIL — inherited xml:lang qualification retained**. |
| Task event/prefix | **UNRESOLVED**; original event unavailable. |
| Consolidated Solution Architect review/acceptance | **PENDING**. |

The [transport evidence](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/archi-transport/summary.json)
retains exact native/returned files, execution and strict/raw/normalised comparison.
All 1,996 canonical element fields/Property values and all 2,246 native relationship
identities/types/endpoints/text/Property values are preserved. No relationships or
Views are missing/added; zero View records change. The strict verifier still FAILs
on **the exact same 575 relationship IDs / 1741
absent-to-English attributes** as the prior candidate. This is inherited, not a
new policy regression. Raw generated-label/organisation-ID omissions and navigation
normalisation remain explicit; neither semantic preservation checks nor successful
Archi execution claim raw fidelity, accept losses or weaken guards. Returned XML
SHA-256: `9e6bed3d01eadc7a8ef9242f0a4ea857b3be219607b093f37013ce4d0e198682`.

**Projection-placement correction:** Verification established that the narrative
renderer reads only the main Architectural content section. The initially appended
policy block was therefore placed as a candidate subsection inside that existing
section, without changing any prior text or renderer. Final XML/reader/narrative
checks now expose the property and all three values. The full tooling suite passed
before this placement refinement; focused final projection/publication/round-trip
checks are separately retained in the check summary. No remaining representation
mismatch is asserted.

Actual Archi execution used fresh temporary configuration/workspace/Java user.home
and reviewed local-display access, with outputs under `/tmp`. It does not constitute
Solution Architect inspection. Returned files never replace canonical sources,
maintained Views or release publications. [Check evidence](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/checks/summary.json)
records finite commands/results. No runtime cache policy, implementation, enum class,
latency measurement or replication mechanism is implemented/tested by this task.

## Files, review readiness and terminal boundary

This amendment changes only the existing Mneme canonical component resource
`enterprise-architecture/continuum/application/application-component/1af8a56358f7429fabfb57be66542a99.md`
and appends bounded DR-18/19 findings/results to the existing pending HCRL. It adds
this Task/Report, before preimages/state, selected after candidate/evidence under
`mneme-post-create-availability/`, and the requested working report
`.junie/reports/dokimasia-mneme-post-create-availability-amendment.md`.
The [changed-file inventory](../../enterprise-architecture/design/evidence/1.1.19/001/after/mneme-post-create-availability/changed-files.json)
distinguishes this amendment from already-existing uncommitted ADD changes.

**Scope deviations:** none. No new architectural decision, identity, relationship,
specialisation, principal service, storage subsystem, implementation or unrelated
change. Prior review recommendations remain recommendations. DR-1, DR-18, DR-19
and HCRL 1.1.19-001 stay open. No version advance, accepted publication, staging,
commit or push occurred. Existing Git HEAD does not contain the new evidence.

**Ready for consolidated Solution Architect review:** the single required Mneme
amendment is represented and verified; the other five definitions and Assurance/
Audit/Provenance responsibilities remain as reviewed. The exact candidate,
reconciliation, preserved unknowns and inherited verification qualification are
presented for explicit disposition. This report supplies no acceptance or closure.
