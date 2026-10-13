<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# RADS Business Architecture reference catalogue reconciliation

**Date:** 2026-10-13. **Branch:** `Harmonia-GovernedAccess`.
**Outcome:** Reference candidate capture completed; validation PASS.

This report is execution/reconciliation evidence, not architectural authority.
The [reference model documentation](../../docs/markdown/03-business-architecture/reference-model/README.md)
explains the representation and its limits. Source architectural definitions,
decomposition and decisions were not changed. No Capability Model redesign,
structural identifier allocation, new Feature/Function or downstream runtime
allocation was performed.

## Authority and existing RADS inspection

The repository was clean on the requested branch at task start. The JSON records
the exact starting commit and source-document SHA-256 fingerprints.

RADS is the existing `/docs/markdown` corpus. Inspection found Markdown
catalogues, explicit source references, embedded Mermaid/ASCII diagrams and
metamodel generation intent, but **no existing machine-readable RADS catalogue,
schema, model implementation or RADS generator**. Repository Python tooling and
the legacy LaTeX/LibreOffice build were also inspected for reuse. The latter
generates legacy documentation, not this RADS model, and supplies no architectural
authority. No legacy architectural content was migrated or superseded.

The bounded extension is therefore a JSON representation and JSON Schema within
Domain03, plus standard-library validation/navigation tooling under `scripts`.
It uses the existing metamodel, catalogues, authority hierarchy and
established/candidate/unresolved distinctions. It creates no parallel application
catalogue, production model, module, dependency or approval workflow.

Materially relevant axioms: AX-01/AX-04 preserve Business meaning; AX-14 preserves
Capability/Feature/behaviour and contextual/ancestry distinctions; AX-15 and
AX-17 preserve uncertainty and truthful traceability. AX-18 prevents information
integration/coordination from conferring clinical-work authority. The explicit
source conflict below is retained under AX-17. This task is outside the
convergence/runtime implementation programme and declares no domain/programme
completion or freeze.

## Files inspected

Repository governance and orientation:

- `AGENTS.md` and `docs/AGENTS.md`.
- `docs/markdown/governance/architectural-axioms.md`.
- `docs/markdown/architecture-completion-plan.md`, particularly context loading,
  candidate standing and completion gates.
- `docs/markdown/02-strategy/capabilities/index.md`.
- `docs/markdown/03-business-architecture/README.md`.
- `docs/markdown/03-business-architecture/behaviours/index.md`.

Authoritative capture/reconciliation sources; all fifteen are fingerprinted in
the model:

- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`.
- `docs/markdown/02-strategy/capabilities/business-capabilities.md`.
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`.
- `docs/markdown/02-strategy/capability-maps/capability-tier-model.md`.
- `docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md`.
- `docs/markdown/04-information-architecture/reviews/package2-g1-review.md`,
  especially K5 and K9.
- `docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md`.
- `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`.
- `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`.
- `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`.
- `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`.
- `docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md`.
- `docs/markdown/03-business-architecture/behaviours/health-service-assurance.md`.
- `docs/markdown/03-business-architecture/processes/business-processes.md`.
- `docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md`.

Tooling/navigation inspection additionally covered `scripts/test_runner.py`,
`docs/libreoffice/Makefile`, relevant navigation in `docs/README.md`, repository
file inventories and the architecture-test inventory. Legacy material was used
only to locate tooling, not to establish captured architectural semantics.

## Files changed

| File | Change |
| :--- | :--- |
| `docs/markdown/03-business-architecture/reference-model/business-architecture.json` | New source-evidenced reference candidate capture. |
| `docs/markdown/03-business-architecture/reference-model/business-architecture.schema.json` | New JSON Schema 2020-12 representation contract. |
| `docs/markdown/03-business-architecture/reference-model/README.md` | Representation, uncertainty, ownership, contextual views, identity, aliases, validation and maintenance documentation. |
| `docs/markdown/03-business-architecture/README.md` | Navigation link and candidate-standing qualification only. |
| `scripts/rads_business_architecture.py` | Schema/graph/evidence validation, listing, lookup, uncertainty inventory, change-impact queries and contextual Mermaid generation. |
| `scripts/tests/test_rads_business_architecture.py` | Validator rejection/acceptance tests and source-capture reconciliation regressions. |
| `.junie/reports/2026-10-13-rads-business-architecture-reference-catalogue.md` | This completion report. |

The existing test runner also refreshes ignored `test-execution.log` and
`test-summary.txt` as execution artefacts; these are not architecture/model
changes. No production Java, POM, Strategy catalogue, metamodel, axiom or accepted
ADR was changed.

## Model/schema changes and captured inventory

The schema separates architectural elements, contextual views, named catalogue
decomposition descriptions and typed relationships. Each behavioural element has
an explicit established Capability/Feature owner. Internal record keys are
references, not Canonical IDs or aliases. `canonical_id: null`, unresolved tier
and partial ancestry are independently represented. Source fingerprints and
exact line/text/anchor locators make assertions reviewable and expose drift.

| Captured construct | Count | Interpretation |
| :--- | ---: | :--- |
| Business Capabilities | **24** | The 18 catalogue entries plus the six agreed Individual Care Delivery children. |
| Established Business Enabling Capability contexts | **76** | 68 primary Strategy entries; four independently evidenced Entity Management owners; three assurance Capabilities; one distinct Domain03 operational Credential Management owner. |
| Named enabling decomposition contexts | **73** | Four resolve to established Capabilities; **69** have unresolved standalone Architectural Element identity. They are not counted as approved Capabilities. |
| Features | **137** | All existing FT identities, names and local identifiers retained. |
| Business Functions | **147** | Explicit source Functions; no Function mechanically created for a Feature. |
| Business Services | **133** | Existing named exposed Services, with their owners. |
| Business Processes | **19** | Sixteen pre-existing and three approved assurance Processes. |
| Contextual views/regions | **10** | Five enabling contextual views and five natural Business regions. |
| Confirmed relationships | **274** | Bounded source assertions, not an exhaustive dependency matrix. |
| Specifically unestablished Feature associations | **22** | Eleven Function and eleven Service association statements preserved separately from the approved graph. |

Behaviour counts by source context:

| Context | Established enabling owners | Functions | Services |
| :--- | ---: | ---: | ---: |
| Entity Management | 11 | 31 | 30 |
| Service Administration | 13 | 32 | 32 |
| Service Delivery | 16 | 20 | 19 |
| Health Service Operations | 21 | 26 | 17 |
| Intrinsic / Shared Enablement | 12 | 33 | 32 |
| Assurance, view placement unresolved | 3 | 5 | 3 |
| **Total** | **76** | **147** | **133** |

The 69 unestablished enabling descriptions plus 76 independently established
owners provide a faithful reference to **145 named enabling entries/contexts**;
this does not assert that all 145 are Architectural Capability elements.

## Established identifiers and aliases

**Complete structural Canonical IDs captured: 0.** No authoritative complete
path/code allocation was found for these captured elements. All 536 element
records therefore retain explicit unresolved complete Canonical IDs.

**Established local Feature identifiers captured: 137**, exactly as spelled:

| Family | Retained identifiers | Count |
| :--- | :--- | ---: |
| Entity Management | `FEAT-EM-01` through `FEAT-EM-31` | 31 |
| Service Administration | `FEAT-SA-01` through `FEAT-SA-29` | 29 |
| Service Delivery | `FEAT-SD-01` through `FEAT-SD-20` | 20 |
| Health Service Operations | `FEAT-HSO-01` through `FEAT-HSO-29` | 29 |
| Intrinsic / Shared Enablement | `FEAT-ISE-01` through `FEAT-ISE-28` | 28 |

`FEAT-EM-01` is not rewritten as `FEAT-EM01` and is not represented as a complete
structural Canonical ID. Catalogue numbers, BC reference/display labels and
metamodel syntax examples are not allocated structural identifiers or ID Aliases.

Three explicit Name Aliases are retained:

| Current element | Retained Name Alias |
| :--- | :--- |
| Clinical Qualification Management | Clinical Credential Management |
| Manage Ward Capacity | Coordinate Ward Operational State |
| Manage Outpatient Capacity | Coordinate Outpatient Session Utilisation |

**ID Aliases captured: 0.** No actual historical structural ID allocation for a
captured element is established by these sources. Syntax examples are not alias
allocations. Alias collision/direct-resolution handling is nevertheless tested
using artificial fixtures that are never written to RADS.

## Unresolved identity, ancestry and responsibility cases

- **100 Capability tier assignments/root statuses remain explicitly unresolved.**
  FT classification is established for all 137 Features. The capture preserves
  the Business catalogue's eighteen explicit `L1` labels and six explicit `L2`
  labels separately. The canonical decomposition model/K9 and Business
  reconciliation do not authorise mechanical CT ancestry/root allocation from
  those labels. Their deprecated/current-CT reconciliation is deliberately
  deferred, without losing the explicit six-part Business decomposition.
- **All 536 complete ancestry/Canonical ID assertions remain unresolved**;
  **399** elements also lack an allocated local code. The 137 Feature local
  tokens remain established despite unresolved complete ancestry.
- `Client Administration → Person Identity → Identifier Resolution [FT /
  FEAT-EM-01]` is retained as the explicit partial parent chain and Feature owner.
  No tier, root or missing intermediate ancestor is manufactured.
- Individual Care Delivery retains exactly Individual Health Assessment,
  Clinical Determination, Individual Care Planning, Clinical Intervention,
  Individual Health Monitoring and Individual Care Review, including their
  source definitions. These are actual agreed decomposition relationships.
  Care settings, illustrative activities and explanatory detail create no CT3
  elements. The seven explicit Health Information Access enablement relationships
  and three BC-18 assurance enablement relationships remain typed enablement,
  not ancestry.
- The three assurance enabling Capabilities and their behaviour have unresolved
  contextual-view placement. No sixth view or Feature is invented. BC-17 enabling
  derivation, BC-17/BC-18 relevance and other unallocated relevance remain explicit.
- All Functions/Services/Processes remain owned even without an established
  Feature association. Explicitly rejected associations stay unresolved rather
  than being assigned a replacement Feature. Composition sufficiency does not
  create new Functions or mappings.

The **69 named descriptions with unresolved standalone Capability identity** are:

| View | Count | Preserved descriptions |
| :--- | ---: | :--- |
| Entity Management | 21 | Practitioner; Practitioner Role; Provider Relationship; Provider Engagement; Healthcare Organisation; Organisational Structure; Organisation Contact; Healthcare Location; Location Structure; Care-Place Definition; Healthcare Service Definition; Service Provision Relationship; Service Availability Definition; Service Eligibility Definition; Health Product Definition; Product Formulary & Catalogue; Product Batch & Serial Tracking; Device Definition; Device Instance; Device Association; Device Connectivity. |
| Service Administration | 11 | Client Intake & Demographic Update; Referral Intake & Progression; Appointment & Booking Administration; Encounter Lifecycle Management; Closed-Loop Order Progression; Diagnostic Workflow Management; Medication Order & Dispense Management; Procedure & Surgical Administration; Clinical Documentation Lifecycle; Care Plan Coordination; Secure Clinical Communication. |
| Service Delivery | 10 | Primary Care Coordination; Acute Care Monitoring & Event Coordination; Emergency Care Progression; Multidisciplinary Inpatient Care Coordination; Diagnostic Service Integration; Medication Therapy Provision and Monitoring; Screening and Immunisation Enablement; Community Care Coordination; Outreach Care Continuity; Remote Patient Monitoring. |
| Health Service Operations | 15 | Session & Queue Coordination; Ward Logistics Coordination; Perioperative Logistics; ED Operational Coordination; Outpatient Flow Enablement; Bed State & Turnover Management; Specialized Equipment Coordination; Facility Demand & Capacity Telemetry; Mobile Worker Dispatch & Safety; On-Call Directory Resolution; Operational Work Assignment & Supervision; Credential Verification Checkpoint; Patient Transfer Coordination; Specimen & Supply Courier Tracking; Discharge Progression & Coordination. |
| Intrinsic / Shared Enablement | 12 | Longitudinal Clinical Record Governance; Multi-Model Information Exchange; Governed Query & Search; Boundary Protocol Mediation; Policy & Security Governance; Terminology & Reference Mapping; LHR-Aware Multidisciplinary Collaboration; Operational Messaging; Multi-Work-Unit Orchestration; Operational & Clinical Calendar Sync; Decoupled Human Interaction & Dashboards; Canonical Semantics & Schema Governance. |

Their source containment and Feature associations are preserved as catalogue
context. Each Feature remains anchored to an independently established owning
Capability. K9 does not validate every description's identity as a separate
Capability merely because a named bullet appears in a decomposition catalogue.

## Documentation inconsistencies and deliberately deferred decisions

| Finding | Capture/reconciliation disposition |
| :--- | :--- |
| **Clinical-handover conflict:** `Coordinate Care-Team Collaboration` describes managing communication spaces, memberships **and clinical handovers**. Metamodel §3.7 explicitly excludes clinical-handover management, consistent with AX-18. | Preserve the existing Function name/description and governing boundary, attach both source assertions and flag `semantic_boundary_conflict` unresolved. No silent correction or clinical authority allocation. |
| Legacy L1/L2 Business terminology versus current CT terminology and unresolved allocation boundaries. | Preserve explicit source labels/decomposition independently from CT tier and structural identity. No numerical conversion. Subsequent review can adjudicate their exact CT standing. |
| Domain03 `Credential Management` has no matching Strategy catalogue entry. | Preserve its independently established operational badge/access responsibility, view and Function/Service. Record unresolved Strategy correspondence. Do not equate it with qualification/competency management or `FEAT-HSO-23`. |
| `FEAT-HSO-16` has a proposed new name, while its current name remains `Mobile Worker Task Dispatch`. | Preserve its current name, ID and approved integration qualification. The proposal is not a Name Alias or new Feature. |
| `Health Information Communication → Boundary Protocol Mediation` may require semantic review. | Preserve exactly the current catalogue context and existing Feature/Function/Service responsibility. No reassignment/redesign. |
| SD-03/SD-08/SD-14 composition decisions and other explicitly unsupported Feature associations. | Retain Features and established Functions/Services/owners separately. Do not turn composition sufficiency or shared terminology into a whole-Feature realisation edge. |
| Access filtering/security propagation responsibility exists alongside explicitly unestablished Feature associations. | Keep the source-owned Functions and explicit unresolved mappings; do not add an apparently obvious association. |
| Ambulatory Session Status Query lacks an identified outside consumer/mandate; Security Context Validation Service lacks a precise internal Function mapping. | Preserve named Services and owners; mark exposure/mapping uncertainty. No invented consumer or one-to-one Service/Function mapping. |
| Earlier Business reconciliation records incompatible strategic Value Stream `BC-*` labels. | No ordinal translation, alias or exhaustive traceability is inferred. Those unrelated mappings remain outside this capture. |

Further tier/code allocations, standalone decomposition-entry identity, assurance
Feature/view placement, proposed renames, precise Service contracts, exhaustive
dependency/participation matrices, lifecycle states and downstream implementation
remain separate architecture activities. The candidate capture is a baseline for
that review, not its adjudication.

## Validation added and execution results

Validation distinguishes **INVALID representation** from **UNRESOLVED BY
ARCHITECTURAL DECISION**. Permitted architectural uncertainty does not fail
validation. Source drift is a capture-evidence error requiring reconciliation,
not proof of architectural invalidity.

Checks cover closed schema structure, duplicate JSON properties/record keys,
Canonical ID/ID Alias uniqueness and collisions, correct terminal tokens,
complete established ancestor paths when asserted, valid owners, FT typing,
source/model/context references, ancestry cycles, scoped name uniqueness where
the CT1 root is established, direct alias resolution and explicit reasons for
unestablished relationships. Unknown CT1 Feature-name scope remains unresolved.
Historical aliases do not require their former ancestors to exist in the current
graph. Source qualifiers are retained as supporting evidence without expanding
Feature scope or creating new relationships.

| Verification | Result |
| :--- | :--- |
| `python3 scripts/rads_business_architecture.py validate` | **PASS**, 0 invalid assertions. **2015 unresolved dimensions/relationships** reported separately; this is not a count of unique elements. |
| `timeout 60s env PYTHONDONTWRITEBYTECODE=1 python3 -m unittest discover -s scripts/tests -p 'test_rads_business_architecture.py' -v` | **PASS**, 32 tests, approximately 1.4 seconds. Artificial fixtures exercise valid full IDs, aliases, collisions, invalid token types, missing ancestors, orphans, unresolved architecture, cycles, cross-owner Feature mappings and source drift. |
| CLI summary/list/contexts/show/impact/diagram smoke checks | **PASS**: 24 Business entries, 11 Entity Management owners, 25 Entity Management catalogue contexts, exact `FEAT-EM-01` lookup and 14 affected Person Identity elements, including its evidenced partial parent Client Administration. Diagram output retains visible uncertainty. |
| Normal required Maven architecture command through `scripts/test_runner.py`, bounded at 600 seconds overall / 120 seconds silence | **UNRESOLVED verification due to environment**, stopped in about 3 seconds before any tests: Maven could not update read-only `/home/mhunter/.m2/repository/com/sun/mail/jakarta.mail/resolver-status.properties`. No production-code change was made. |
| `timeout 120s mvn -o -B test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false` | **PASS**, **90 tests**, 0 failures, 0 errors, 0 skipped; Maven BUILD SUCCESS in 28.278 seconds. Offline mode avoided the metadata write and used cached dependencies. |
| New documentation links/anchors and task whitespace checks | **PASS**; details verified after the report was written. |

A preliminary test-runner invocation forwarded a literal `--` to Maven and was
rejected before tests; the invocation was corrected. This was a command harness
error, not a semantic test failure. No operation timed out or stalled.

Temporary detailed verification artefacts are
`/tmp/harmonia-business-reference-model-tests.log`,
`/tmp/harmonia-business-reference-architecture-tests.log`,
`/tmp/harmonia-business-reference-architecture-offline.log` and
`/tmp/harmonia-business-reference-service-delivery.mmd`.

The focused suite, model evidence checks and documentation checks establish this
capture's integrity. They do not approve unresolved architectural decomposition
or promise exhaustive traceability. Subsequent source changes must be reconciled
semantically before updating model evidence and fingerprints.
