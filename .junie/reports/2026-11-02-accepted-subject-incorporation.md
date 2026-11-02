Report-Produced-At: 2026-11-01T23:25:58.703019Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

# Accepted G14–G18 Subject incorporation — execution report

**The accepted 32 Subjects and 27 classification relationships are incorporated,
and the maintained G18.03 presentation path has the corrected spelling. The result
is presented for Solution Architect review; the HCRL remains pending.**

Boundary: 1.1.18 / 001. Authority is the retained Solution Architect
[acceptance and incorporation Task](../../enterprise-architecture/design/evidence/1.1.18/001/before/ai-agent-tasks/unresolved-accepted-subject-incorporation.md),
accepting DR-2, DR-10 and CR-001–CR-060 only. The Task retains the complete issued
wording. Its instruction-entry event time remains UNRESOLVED because that event
timestamp is not available in the interaction; no later clock was substituted.
This Report has its separately captured completed-emission event.

## Architecture incorporated

The maintained governed `architecture_continuum.create` operation created five
top-level and 27 subordinate **native Grouping Subjects, with no specialisation**.
Before creation, the current Continuum was checked again for their source-qualified
codes, source bindings and Grouping name collisions. None represented an existing
G14–G18 instance. Each canonical UUIDv4 was allocated once by the governed operation,
persisted in its resource, and recorded alongside its distinct established Exchange
identifier. No ID was derived from a Subject code, name, folder or source path.

| Group | Subgroups | Subject CRs | Composition CRs |
| --- | --- | --- | --- |
| G14 Patient Identity and Demographics | G14.01–G14.06 | CR-001–CR-007 | CR-033–CR-038 |
| G15 Provider and Workforce Management | G15.01–G15.05 | CR-008–CR-013 | CR-039–CR-043 |
| G16 Consent and Information Sharing | G16.01–G16.06 | CR-014–CR-020 | CR-044–CR-049 |
| G17 Terminology and Reference Data | G17.01–G17.05 | CR-021–CR-026 | CR-050–CR-054 |
| G18 Provider Directory | G18.01–G18.05 | CR-027–CR-032 | CR-055–CR-059 |

| Subject | Accepted display name | Canonical identity / resource |
| --- | --- | --- |
| G14 | Patient Identity and Demographics | [a9deca9adbb443208d6f08dbe4505717](../../enterprise-architecture/continuum/grouping/a9deca9adbb443208d6f08dbe4505717.md) |
| G14.01 | Identity Registration and Verification | [5fcada54e6df40a8983335a90e4a9f02](../../enterprise-architecture/continuum/grouping/5fcada54e6df40a8983335a90e4a9f02.md) |
| G14.02 | Identity Matching and Reconciliation | [b91ec2110aaf4d44bd043bb787fbd910](../../enterprise-architecture/continuum/grouping/b91ec2110aaf4d44bd043bb787fbd910.md) |
| G14.03 | Identity Maintenance and Status | [801386f27cda458b8ce811f133d917e9](../../enterprise-architecture/continuum/grouping/801386f27cda458b8ce811f133d917e9.md) |
| G14.04 | Demographic and Contact Management | [58c5443983854dd98cd72f2dd795c063](../../enterprise-architecture/continuum/grouping/58c5443983854dd98cd72f2dd795c063.md) |
| G14.05 | Identity Access and Disclosure | [2f97190ae5734e8caee48d581e1a6ab2](../../enterprise-architecture/continuum/grouping/2f97190ae5734e8caee48d581e1a6ab2.md) |
| G14.06 | Identity Quality and Oversight | [ba2e95cc11374757bd0945148da43b07](../../enterprise-architecture/continuum/grouping/ba2e95cc11374757bd0945148da43b07.md) |
| G15 | Provider and Workforce Management | [fd9d0fa7117a4dc3b45cfed0e7be614b](../../enterprise-architecture/continuum/grouping/fd9d0fa7117a4dc3b45cfed0e7be614b.md) |
| G15.01 | Provider Identity and Registration | [a58e87c95bd24c16a1006b217aeac691](../../enterprise-architecture/continuum/grouping/a58e87c95bd24c16a1006b217aeac691.md) |
| G15.02 | Credentials and Professional Standing | [2ad6333e5858456dba8aefeec71773a5](../../enterprise-architecture/continuum/grouping/2ad6333e5858456dba8aefeec71773a5.md) |
| G15.03 | Affiliations, Roles and Practice Locations | [25a15d4bea2d418d918e3a7e01d9fcbf](../../enterprise-architecture/continuum/grouping/25a15d4bea2d418d918e3a7e01d9fcbf.md) |
| G15.04 | Provider Profile Maintenance | [a47808600e8f4d7c83756f4ce77b498e](../../enterprise-architecture/continuum/grouping/a47808600e8f4d7c83756f4ce77b498e.md) |
| G15.05 | Provider Lifecycle and Administration | [ad16265c76114f4eaddf75b0be2d4755](../../enterprise-architecture/continuum/grouping/ad16265c76114f4eaddf75b0be2d4755.md) |
| G16 | Consent and Information Sharing | [abca473311ea40aca58756d859077ff7](../../enterprise-architecture/continuum/grouping/abca473311ea40aca58756d859077ff7.md) |
| G16.01 | Consent Capture and Maintenance | [884f06591f6c4dcf8e7f94e4b31269c1](../../enterprise-architecture/continuum/grouping/884f06591f6c4dcf8e7f94e4b31269c1.md) |
| G16.02 | Consent Scope and Preferences | [e48346674b0740c19f4a13cc50a7ae66](../../enterprise-architecture/continuum/grouping/e48346674b0740c19f4a13cc50a7ae66.md) |
| G16.03 | Consent Verification and Decisions | [3d87dbf35e0a43a3b5e12cbc6b947763](../../enterprise-architecture/continuum/grouping/3d87dbf35e0a43a3b5e12cbc6b947763.md) |
| G16.04 | Representatives and Delegated Consent | [392c07b6a5ba4fc7bd5c19910437da98](../../enterprise-architecture/continuum/grouping/392c07b6a5ba4fc7bd5c19910437da98.md) |
| G16.05 | Consent Communication and History | [52508b8b7aec4b1cbaa570576442d4c9](../../enterprise-architecture/continuum/grouping/52508b8b7aec4b1cbaa570576442d4c9.md) |
| G16.06 | Exceptional and Emergency Consent | [25bc65e773d64ecf9b71ae4f92ed57e1](../../enterprise-architecture/continuum/grouping/25bc65e773d64ecf9b71ae4f92ed57e1.md) |
| G17 | Terminology and Reference Data | [8b27323cbaba400698842f77e0d28c0d](../../enterprise-architecture/continuum/grouping/8b27323cbaba400698842f77e0d28c0d.md) |
| G17.01 | Terminology Definition and Maintenance | [b83d63bb553c4642b26bbc41f66445b3](../../enterprise-architecture/continuum/grouping/b83d63bb553c4642b26bbc41f66445b3.md) |
| G17.02 | Terminology Mapping and Validation | [8655d81cd41647449c3b1a10ffd453b3](../../enterprise-architecture/continuum/grouping/8655d81cd41647449c3b1a10ffd453b3.md) |
| G17.03 | Terminology Release and Versioning | [4e0bcac2a001411c8802b5df30e6bb38](../../enterprise-architecture/continuum/grouping/4e0bcac2a001411c8802b5df30e6bb38.md) |
| G17.04 | Reference Data Management | [ba0e7f67e2e7496d9df8f6617ee39ec3](../../enterprise-architecture/continuum/grouping/ba0e7f67e2e7496d9df8f6617ee39ec3.md) |
| G17.05 | Reference Data Quality and Governance | [400c5c54a23a44588cc473ed10ead4a8](../../enterprise-architecture/continuum/grouping/400c5c54a23a44588cc473ed10ead4a8.md) |
| G18 | Provider Directory | [0233415953ed487fbbe407ebb469e7bc](../../enterprise-architecture/continuum/grouping/0233415953ed487fbbe407ebb469e7bc.md) |
| G18.01 | Provider Directory Discovery | [c445fc55a6e547ae9b1a5bb43cad1a8e](../../enterprise-architecture/continuum/grouping/c445fc55a6e547ae9b1a5bb43cad1a8e.md) |
| G18.02 | Directory Publication and Visibility | [b6a20afe29624f73906e5e852d6f2fbf](../../enterprise-architecture/continuum/grouping/b6a20afe29624f73906e5e852d6f2fbf.md) |
| G18.03 | Directory Information Maintenance | [d2876dff4f5444e3b71bdeba487b2d6b](../../enterprise-architecture/continuum/grouping/d2876dff4f5444e3b71bdeba487b2d6b.md) |
| G18.04 | Directory Verification and Quality | [7b2cabb4089d414d8ad700ef5f7932b7](../../enterprise-architecture/continuum/grouping/7b2cabb4089d414d8ad700ef5f7932b7.md) |
| G18.05 | Directory Synchronisation | [e1ed88f315a84116a77ead21d8fe1792](../../enterprise-architecture/continuum/grouping/e1ed88f315a84116a77ead21d8fe1792.md) |

The [Subject identity manifest](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/incorporated-subjects.csv)
also records every accepted CR, canonical path, display/friendly name, Exchange
binding, operation and independent standing. All source-qualified classification
codes remain references rather than canonical identities.

The 27 accepted **Composition** assertions are stored under their five source
Subject resources. They have separate persisted relationship representation IDs,
explicit accepted standing, currentness and decision provenance; relationships
do not receive canonical UUIDs. Every target is the accepted subordinate Subject,
and each subgroup has exactly its accepted parent in this classification.
The [relationship manifest](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/incorporated-classification-relationships.csv)
identifies each actual relationship ID, CR, source/target canonical and Exchange
identities and classification codes.

Composition denotes **Subject classification containment only**. No Capability
decomposition, Component ownership, logical Home, implementation responsibility,
Motivation allocation, new specialisation or semantic parent above G14–G18 was
introduced. Existing G01–G13 remains applicable.

## Names, provenance and standing

Accepted display names and local friendly names are preserved, including
`Affiliations, Roles and Practice Locations` and `Directory Information Maintenance`.
The shared governed lexical function verifies friendly names. All 32 canonicalName
values remain **null**, with an explicit unresolved-property reason for the
unestablished unqualified-Grouping formal naming token. No qualifier or ancestor
qualification was manufactured.

New Subjects and relationships explicitly record Solution Architect acceptance
of their individual CR and pending incorporated-result review, independently of
their currentness. They cite the maintained HCRL, stable Change Review anchors,
the exact retained acceptance Task and the accepted source definitions. The
mapping and incoming XML retain their evidence roles; they are not elevated to
independent architectural authority. Current retained source bytes are preserved
and hash-pinned. No missing Git history, original timestamp or architectural
acceptance is invented.

The accepted 32 local concepts use the existing native storage and Architectural
Subjects reading context. Canonical `navigation.json` and its generated manifest
were updated through governed creation/projection operations. Removing only the
32 newly allocated identities from the navigation document reproduces its entire
pre-incorporation JSON structure. No unrelated navigation meaning was changed.

## Maintained presentation and governed projection

CR-060 is applied in `design/archimate/projection-template.xml`. The old maintained
tree had no `1. Subject-Based-Views` branch. To provide the accepted correction's
target without replacing that tree, only this necessary empty presentation path
was added:

`Views / 1. Subject-Based-Views / 18. Provider Directory / 03. Directory-Information-Maintenance`

That is **three empty organisation items**; no other incoming G14–G18 View folders,
Views, properties, layout or navigation changes were adopted. The original
`Directory-Informaton-Maintenance` spelling remains unchanged in export 004 and
the original review evidence. Removing the added path reproduces the original
template's complete parsed signature. All 1,216 existing View contents,
memberships, navigation items, order, geometry and styles remain preserved.

The maintained projector generated the
[unreleased verification projection](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/Harmonia-Subjects-unreleased.xml).
It contains **1,874 elements and 1,909 relationships**, exactly the original
1,842 / 1,882 plus the accepted 32 / 27. Its SHA-256 is
`aa899767de1b146feb27fe08b7f40fafa1c7f691178643e7f921de285040b631`.
Every Subject appears once under the native Grouping catalogue; every new
relationship appears once directly under its explicit source catalogue entry.
The corrected empty presentation path is present. Generation allocated no IDs.

The root release still reads **1.1.18**, previous **1.1.17**. The immutable published
1.1.18 baseline remains byte-identical to SHA-256
`7df92528a09f9a765ddebec105643bae08ac92583cad4e8901b3135d49db2c2e`.
No release transition is authorised, so its published bytes were not replaced.
The new XML is explicitly an unreleased evidence projection of the current
working canonical state. Accepted-change release accounting and publication must
be handled through a separately authorised transition; the release policy was
not waived. A normal current-release publication staleness check is not claimed
to pass against the intentionally preserved predecessor projection.

## Verification

The [verification record](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/verification.json)
and [projection validation](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/projection-validation.json)
retain the actual scope and consistency checks:

- **PASS:** canonical schema, provenance, UUID uniqueness, native relationship
  applicability, navigation, containment, information naming, offline XSD and
  Exchange identifiers/references.
- **PASS:** all 32 accepted Subjects and 27 exact classification edges resolve
  correctly, with accepted source-qualified standing and no invented naming values.
- **PASS:** all **1,842 existing canonical element files** remain byte-identical,
  including all 82 G01–G13 Subjects and all existing Capability/Feature resources.
- **PASS:** every old projected element and all 1,882 old projected relationships
  have unchanged semantic signatures. The only native additions are the accepted
  Grouping and Composition sets.
- **PASS:** all 1,216 Views and existing authored View navigation are unchanged;
  only the necessary three-item empty CR-060 path is added.
- **PASS:** the original **784 Motivation candidates**, their assessments, source
  wording/identities, proposed mappings, duplicate findings and earlier Reports
  remain unchanged. No UseCase/UserStory, Motivation association, Capability or
  Feature was created, changed, replaced or retired.
- **PASS:** protected baseline/export/working files and the user's pre-existing
  publication edits are preserved. No unrelated oversized-export difference was
  incorporated or assumed to be an accepted round-trip effect.

**PASS: the final standard tooling suite ran 85 tests in 314.190 seconds, with zero errors or failures.** The [final log](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/final-tooling-tests.log) and [structured results](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/final-test-results.json) retain the unmodified standard invocation.

The initial standard run is retained as **FAIL**, with six temporary-fixture missing-input errors. The supplemental diagnostic run is retained as **FAIL**, with one predecessor-population assertion failure across 85 tests. Those outcomes were not rewritten; the declared retained inputs and directly affected test-only baseline/fixtures were corrected, then the complete standard suite passed.

The test-only changes support the accepted addition: a shared fixture helper
copies exact declared retained provenance into temporary source trees, following
the maintained independence verifier's existing behaviour; three affected test
fixtures use that helper; and the explicit population assertion is now
1,874 / 1,909 / 462. The 462 qualified source predicates are unchanged.
Production validators and test assertions were not disabled or weakened.
The [test adjustment record](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/test-adjustments.json)
identifies the original tracked source revision and affected test files.

**Remaining verification limits:** actual Archi import/export transport and
Solution Architect workbench inspection are not performed or claimed. They remain
required gates before any applicable publication/HCRL acceptance. Earlier Java
architecture tests remain **UNRESOLVED** because Maven attempted to write to a
read-only local dependency cache before test execution; the unchanged invocation
was not retried. Task instruction-entry times remain explicitly **UNRESOLVED**
for governance treatment before closure. These limits do not become successful
results through source or XML validation.

## Review history, repository state and boundary

The existing [HCRL](../../enterprise-architecture/design/review/1.1.18/hcrl-1.1.18-001.pending.md)
records acceptance and actual incorporation in DR-2 and DR-10. The existing
[Change Review](../../enterprise-architecture/design/review/1.1.18/change-review.md)
records **Accept** for all 60 CRs and the actual identity/presentation processing
facts. Their **Unresolved** Outcomes retain the pending incorporated-result review
and applicable human Archi/publication gates; they do not reverse acceptance.
The prior review documents are retained unchanged under
`before/subject-incorporation/`. Original observations, dependencies, scope
qualifications and decision history remain distinguishable from application facts.
**DR-3–DR-9 are unchanged and unresolved.**

The [incorporation ledger](../../enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/incorporation-results.json)
preserves identity allocations, source paths, operation outcomes, relationship
endpoints and presentation treatment. It supports recovery without rerunning
UUID allocation blindly. Canonical resources are the maintained architecture;
these records are evidence, not alternative source authority or a new processor.

The resulting repository state is **uncommitted incorporated work awaiting review**:
32 new canonical Grouping files, 27 assertions under their five source owners,
updated reading navigation/manifest, the minimal maintained presentation path,
bounded review/evidence records and directly related test-fixture/baseline updates.
Existing canonical resources and unrelated user edits are preserved.
No commit, push, HCRL closure, resolution of DR-3–DR-9, architecture release
transition or published-baseline replacement occurred. The work stops for Solution
Architect review of the incorporated result and verification evidence.
