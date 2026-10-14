<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# R1/R2 Business Capability Reconciliation — Execution Report

Date: 2026-10-14. Repository: `fhirfactory/harmonia`, branch
`Harmonia-GovernedAccess`. This is execution evidence, not architectural authority.
The canonical definitions, parent/tier assertions, classifications and boundaries
are in [business-capabilities.md](../../docs/markdown/02-strategy/capabilities/business-capabilities.md).

The established portions of DN-BC-001–037 have been reconciled. The naming and
Maintenance-membership points below are stopped as unresolved; this report does
not declare those conflicts adjudicated or the whole architecture programme
complete. The scratchpad remains non-authoritative and unchanged by this task.
The user's staged mindmap is neither altered nor used to supply missing ancestry.

## 1. Files Changed

- `docs/markdown/02-strategy/README.md`
- `docs/markdown/02-strategy/capabilities/business-capabilities.md`
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
- `docs/markdown/02-strategy/capabilities/index.md`
- `docs/markdown/02-strategy/capability-maps/capability-tier-model.md`
- `docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md`
- `docs/markdown/02-strategy/capability-maps/index.md`
- `docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md`
- `docs/markdown/02-strategy/reviews/fixed-capability-decomposition-decision.md`
- `docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md`
- `docs/markdown/03-business-architecture/reference-model/README.md`
- `docs/markdown/03-business-architecture/reference-model/business-architecture.json`
- `docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md`
- `scripts/rads_business_architecture.py`
- `scripts/tests/test_rads_business_architecture.py`
- `.junie/reports/2026-10-14-business-capability-r1-r2-reconciliation.md`

The BEC Markdown change only corrects its cross-reference from the BC's old L2
presentation to the now-established CT2s. B2.1 only qualifies its existing BC
decomposition/Feature wording; its seven support assertions remain unchanged and
Domain05 B2.2 is not commenced. The historical five-region and fixed-decomposition
records retain their original text with dated current-authority references.
No production code, runtime/build/deployment configuration, axioms, Motivation
requirements or Domain04 source is changed. The JSON Schema is unchanged.

## 2. Final Reconciled BC Hierarchy

**190 established BCs: 18 CT1 roots, 39 CT2s, 133 CT3s.** Explicit canonical
parent statements establish this hierarchy; catalogue indentation does not.
All Canonical IDs/local codes remain unallocated. The tree does not exhaust
the healthcare enterprise. A stopping point does not deny other business ability
or assign it to an existing child. The five natural regions remain contextual
groupings rather than ancestors. Health Workforce Management and Healthcare
Organisation & Service Structure Management retain Workforce & Organisational
Capability as their CT1 frame.

```text
CT1 Individual Care Delivery
  CT2 Individual Health Assessment
    CT3 Health History Assessment
    CT3 Clinical Examination
    CT3 Health Status Measurement
  CT2 Clinical Determination [no adjudicated CT3 set]
  CT2 Individual Care Planning [no adjudicated CT3 set]
  CT2 Clinical Intervention [CT3 candidate remains provisional]
  CT2 Individual Health Monitoring
    CT3 Health State Monitoring
    CT3 Care Response Monitoring
  CT2 Individual Care Review
    CT3 Care Appropriateness Review
    CT3 Care Effectiveness Review
CT1 Care Access & Coordination
  CT2 Care Access Management
    CT3 Care Intake Management
    CT3 Care Referral Management
    CT3 Care Eligibility Determination
  CT2 Care Scheduling & Capacity Management
    CT3 Care Appointment Management
    CT3 Care-Place Allocation
    CT3 Care-Place Readiness Management
    CT3 Care Resource Deployment
    CT3 Operational Care Flow Coordination
    CT3 Operational Care Capacity Management
  CT2 Care Coordination & Transition
    CT3 Care-Team Coordination
    CT3 Care Responsibility Transition
    CT3 Care Discharge Coordination
  CT2 Care Support Logistics Management
    CT3 Patient Movement Coordination
    CT3 Clinical Material Logistics Coordination
    CT3 Care Support Work Coordination
CT1 Health Rights, Advocacy & Participation
CT1 Diagnostic, Therapeutic & Clinical Support Services
  CT2 Diagnostic Support Provision
    CT3 Diagnostic Service Appointment Management
    CT3 Diagnostic Service Delivery
    CT3 Diagnostic Specimen Management
    CT3 Diagnostic Data Management
    CT3 Diagnostic Interpretation
    CT3 Diagnostic Report Management
    CT3 Diagnostic Report Distribution
  CT2 Medication & Pharmacy Services
    CT3 Prescription Medication Dispensing
    CT3 Non-Prescription Medication Provision
    CT3 Medication Advice
    CT3 Medication Dose Administration Aid Provision
CT1 Health Products & Clinical Technology
  CT2 Clinical Technology Management
    CT3 Clinical Device Inventory Management
    CT3 Clinical Device Feature & Release Management
    CT3 Clinical Device Loan & Deployment Management
    CT3 Clinical Device Utilisation Support
    CT3 Clinical Device Integration Provision
    CT3 Clinical Device Data Integration
    CT3 Clinical Device Lifecycle Management
CT1 Clinical Quality, Safety & Improvement
  CT2 Clinical Safety Management
    CT3 Clinical Risk Identification
    CT3 Clinical Risk Mitigation Management
    CT3 Clinical Risk Response Management
  CT2 Clinical Quality Management
    CT3 Clinical Quality Criteria Management
    CT3 Clinical Quality Evidence Management
    CT3 Clinical Quality Review & Assessment Management
    CT3 Clinical Quality Reporting
CT1 Community Health & Wellbeing
  CT2 Preventative Health
    CT3 Preventative Health Problem Identification
    CT3 Preventative Health Programme Management
    CT3 Preventative Health Education Services
    CT3 Preventative Health Programme Monitoring
    CT3 Preventative Health Reporting
    CT3 Preventative Health Cohort Management
CT1 Population Health & Health-System Planning
  CT2 Population Health Surveillance
    CT3 Population Health Longitudinal Assessment
    CT3 Population Health Surveillance Management
    CT3 Population Health Event Identification
    CT3 Notifiable Health Event Management
    CT3 Population Health Reporting
  CT2 Health-System Planning
    CT3 Health Service Demand Assessment
    CT3 Health Service Capacity Planning
    CT3 Health Service Distribution Planning
CT1 Health Information & Knowledge Management
  CT2 Health Information Management
    CT3 Health Information Data Management
    CT3 Health Information Structure Management
    CT3 Health Information Lifecycle Management
    CT3 Health Information Catalogue and Index Management
    CT3 Health Information Compartmentalisation
    CT3 Health Information Data Correlation
    CT3 Health Information Data Retrieval
  CT2 Clinical Knowledge Management
    CT3 Clinical Knowledge Context Identification
    CT3 Clinical Knowledge Context Extraction
    CT3 Clinical Knowledge Information Formulation
    CT3 Clinical Knowledge Concept Association
    CT3 Clinical Knowledge Review & Approval Management
    CT3 Clinical Knowledge Publication & Sharing
CT1 Standards, Semantics & Reference Governance
  CT2 Health Information Standards Governance
    CT3 Information Model Discovery
    CT3 Information Model Adoption
    CT3 Information Model Education
    CT3 Information Model Compliance
    CT3 Information Model Governance Oversight
    CT3 Standards Adoption
    CT3 Standards Impact Identification
  CT2 Clinical Semantics Governance
    CT3 Terminology Governance
    CT3 Terminology Utilisation Oversight
    CT3 Terminology Localisation
    CT3 Classification Governance
    CT3 Classification Utilisation Oversight
    CT3 Semantic Mapping Governance
CT1 Connected Health Services
  CT2 Health Information Exchange [no adjudicated CT3 set]
  CT2 Digital Communication Channel [no adjudicated CT3 set]
CT1 Security, Privacy & Digital Trust
  CT2 Digital Identity & Access Governance
    CT3 Identity Assurance
    CT3 Identity Modelling & Interoperability
    CT3 Identity Qualification Control
    CT3 Identity Identification & Authentication
    CT3 Access Control Context Management
    CT3 Access Control Assurance & Reporting
    CT3 Access Control Collaboration
    CT3 Access Control Enforcement
  CT2 Health Information Protection
    CT3 Health Privacy Safeguard Management
    CT3 Health Information Safeguarding
    CT3 Health Information Access Monitoring
    CT3 Health Information Compartment Assurance
    CT3 Health Information Classification Assurance
  CT2 Digital Accountability & Trust
    CT3 Governed Activity Attribution
    CT3 Security & Information-Use Accountability
CT1 Health Research & Innovation
  CT2 Research Information Support
    CT3 Governed Research Information Provision
  CT2 Health Innovation
    CT3 Health Innovation Evaluation
CT1 Health Service Direction & Stewardship
  CT2 Health Service Modelling
    CT3 Health Service Need Identification
    CT3 Health Service Capability Identification
    CT3 Health Service Implementation Support
    CT3 Health Service Continuous Improvement
  CT2 Health Service Alignment
    CT3 Health Service Co-Dependency Management
    CT3 Health Service Resource Identification
    CT3 Health Service Financial Management
    CT3 Health Service Implementation Planning
CT1 Workforce & Organisational Capability
  CT2 Health Workforce Management
    CT3 Practitioner Qualification Management
    CT3 Practitioner Engagement & Privilege Management
    CT3 Workforce Service Coverage Management
  CT2 Healthcare Organisation & Service Structure Management
    CT3 Healthcare Organisation Identity & Structure Management
    CT3 Healthcare Location Definition Management
    CT3 Healthcare Service Offering Management
CT1 Corporate Resources & Enterprise Services
  CT2 Financial & Commercial Services
    CT3 Clinical Activity Charging Information Provision
  CT2 Corporate Support Services
    CT3 Back-Office Services
    CT3 Facilities Management
    CT3 IT Infrastructure Management
    CT3 Communications & Publicity
    CT3 People Management
    CT3 Operational Oversight
CT1 Health Service Governance
  CT2 Governance Framework Management
    CT3 Policy / Obligation Governance
    CT3 Authority & Constraint Governance
  CT2 Compliance Governance
    CT3 Compliance Assessment Definition
    CT3 Governance Reporting & Checkpoint Definition
CT1 Health Service Assurance
  CT2 Health Service Assurance Specification
    CT3 Assurance Approach Definition
    CT3 Assurance Evaluation Criteria Governance
  CT2 Assurance Evaluation
    CT3 Assurance Evidence Assessment
    CT3 Assurance Conclusion Determination
```

Outside that established tree, three identified/named candidate concerns retain
unresolved tier and placement: Health Service Operational Readiness, Clinical
Device Maintenance Management and Health Information Sharing Programme Management.
No CT1/CT2 parent or region is supplied for them. Medication compounding is not
created even as a candidate CT3 record: its very need for separate Capability
identity remains OI-BC-001's open question.

## 3. Classification Reconciliation

The existing four-value taxonomy is unchanged. Descendant classifications are
not inherited from CT1; contextual consumption alone does not establish Core.

| CT1 | Before → after | Established semantic rationale |
| :--- | :--- | :--- |
| 05 Health Products & Clinical Technology | Reference → Harmonia-Relevant | Device integration/data and service-relevant context materially frame exchange/care enablement, while biomedical/supply/maintenance authority remains external. |
| 07 Community Health & Wellbeing | Reference → Harmonia-Relevant | Bounded preventative programme/cohort information and notifications materially support organised health activity; broader community/public-health operation is not assigned to Harmonia. |
| 08 Population Health & Health-System Planning | Adjacent → Harmonia-Relevant | Formal notification and governed population information materially support activity; externally governed demand/capacity planning is not made Core by context consumption. |
| 13 Health Research & Innovation | Adjacent → Harmonia-Relevant | Authorised provision of existing information is substantive research support; trials, genomic research and specialist research governance remain intentionally unmodelled. |
| 14 Health Service Direction & Stewardship | Reference → Harmonia-Relevant | Service need, viability, resource/dependency and alignment facts materially frame service-information derivation. No service catalogue BEC or executive management authority is invented. |
| 15 Workforce & Organisational Capability | Harmonia-Relevant retained | Qualification/privilege/coverage, organisational/location and offering facts constrain care enablement; their business authority is not acquired through directories. |
| 16 Corporate Resources & Enterprise Services | Reference → Harmonia-Relevant | Narrow clinical activity/utilisation evidence provision materially supports financial/charging purposes. Corporate support context matters without making each support ability Relevant/Core. |
| 17 Health Service Governance | Unresolved retained | What/how adherence is governed is established, but platform participation/enablement is not sufficiently assigned to settle Adjacent versus Relevant/Core. |
| 18 Health Service Assurance | Unresolved → Harmonia-Relevant | Previously approved independent governed assurance materially supports confidence; Harmonia does not own the enterprise's macro-level clinical/service delivery assurance. |
| 01–04, 06, 09–12 | Assignments retained | Existing material/direct platform responsibilities remain bounded by organisational and originating authority. |

## 4. Assurance Independence

The macro-level BC is the organisational ability to establish and maintain
justified confidence that health services can be, and are being, delivered
within applicable expectations, obligations, constraints and performance levels.
Independent governed evaluation is a required **property of applicable assurance
approaches/evaluations**, rather than the exhaustive definition of every
enterprise assurance activity.

This conclusion follows REQ-FND-005's bounded applicability: Harmonia must support
independent governed assurance **where assurance is required**, without requiring
assurance of every activity or prescribing all organisational assurance methods.
The old independent-evaluation content is retained as that mandatory obligation,
with the approved Strategy/Business responsibilities unchanged. Assurance Approach
Definition cannot weaken it. Subject performers/managers may supply evidence but
must not solely determine, suppress, manufacture or retrospectively alter their
assurance outcome. Independence is responsibility/conclusion control, not an
external auditor or physical separation.

The macro-level capability does not split into security, information, clinical,
technology or process assurance trees merely because those concerns contribute
criteria/evidence. The protection/access QA CT3s retain their subject-specific
purposes. Governance establishes what must be done and how adherence is determined
and governed; Assurance establishes what sufficient evidence legitimately supports.
Harmonia does not acquire clinical-adequacy judgement, subject progression,
remediation or service-management authority. Evidence insufficiency under
REQ-FND-005 remains distinct from operational uncertainty under AX-15/REQ-FND-004.

## 5. OI-BC-001 and OI-BC-002

- **OI-BC-001: UNRESOLVED.** Medication compounding/preparation is neither accepted
  nor rejected, subsumed, excluded or allocated elsewhere. The four established
  pharmacy CT3s remain, with the open issue explicit in Markdown/RADS uncertainty.
- **OI-BC-002: UNRESOLVED.** Collective service/site readiness retains its semantic
  identity: whether the offering can actually be delivered here from the combined
  supporting conditions. Resource readiness, offering availability and execution
  remain distinct. No CT1→CT2→CT3 ancestry is asserted; no convenient parent,
  region or local code is allocated. A session exercise is not a second capability.

## 6. BC Assertions Newly Captured in RADS

The reference candidate now captures explicit BC tiers, root standing, the stated
CT2/CT3 parents, definitions/boundary evidence and reconciled CT1 relevance.
Existing 24 BC record identities retain their keys; **166 new established BC
identities** and **three unplaced candidate concerns** are captured separately.
There are **172 BC parent edges**, including the six previously captured clinical
edges: **166 additional established decomposition edges**. None is a BC↔BEC edge.
BC names/tier/parents are backed by exact canonical definition, explicit parent
and decomposition assertions, not guessed from document layout.

The final inventory is 269 Capability records (193 BC, 76 BEC), 137 Features,
147 Functions, 133 Services and 19 Processes. BC's 193 includes 190 established
and three candidate concerns. All existing non-decomposition edges, BEC/behaviour
responsibility and catalogue-description semantics are preserved. Stored source
fingerprints/locators are updated only after that semantic review.

Incoming fingerprints already drifted for Business, enabling, metamodel, tier
model, K9, assurance Strategy and B2.1. The prior fixed-decomposition changes
clarified target Feature ancestry while preserving existing identity/ownership;
K9's historical supersession and B2.1's bounded support did not allocate BEC tiers.
Their unchanged evidence text was relocated, not rewritten to conceal drift.
The B2.1 source reference was reconciled to the catalogue's retained bounded trace.

## 7. Remaining Unresolved RADS Dimensions

- All complete Canonical IDs and new local Capability codes remain unallocated.
- BEC CT1/CT2/CT3 ancestry, root standing and Feature CT3 frames remain unresolved;
  all 76 BEC tier values are null. The 137 established FT Features retain their
  names, local spellings, owners and unresolved ancestry.
- All 69 unestablished enabling descriptions remain descriptions, not Capabilities.
- BC↔BEC traceability beyond previously approved support remains unestablished.
- BC descendant relevance is unallocated; BC-17 relevance/enablement remain open.
- OI-BC-001/002, the Maintenance set discrepancy, Sharing Programme placement,
  Clinical Intervention's provisional taxonomy and missing clinical CT3 sets
  remain explicit. External monitoring providers/general improvement placement,
  possible identity-assurance representation and presentation enablement remain parked.
- Canonical BC evidence/context/consumption relationships retain their exact
  business meanings in Markdown. The existing governed RADS vocabulary cannot
  express all those semantics. `semantic_relationship_representation` records
  that capture limitation rather than substituting runtime/structural/Service edges.
- Existing unestablished Function/Feature, Service/Function, consumer/exposure
  and other associations remain unchanged, including the clinical-handover source
  conflict already recorded by the reference candidate.

The validator reports **2467 unresolved dimensions/relationships**, not 2467
elements. PASS establishes representation integrity with uncertainty, not
architectural completeness or global name-uniqueness conformance.

## 8. Validator, Schema and Representation Changes

No schema migration was needed: the existing category/tier/parent/status/source
and uncertainty properties correctly represent the BC assertions and unplaced
concerns. Internal record keys are navigation references, not Canonical IDs or
architectural code allocations.

The validator's identifier grammar now requires CT1→CT2→CT3 before a Feature
token, including a Function/Service/Process beneath that Feature. Complete
Feature ancestry also checks immediate established **BEC CT3** ownership even
without allocated IDs. BC ownership cannot frame Harmonia Features. Same-category
Capability parents preserve the separate hierarchies. Functions/Services/Processes
may still anchor directly at CT1, CT2, CT3 or established Features; no Service or
Process is generated for every Function. Incomplete historical BEC Feature
responsibility remains valid with explicit unresolved ancestry.

Both distinct Exchange records carry explicit `name_uniqueness` uncertainty;
only a real cross-category collision reported by both records is retained as
unresolved. Unreported duplicates or false conflict markers remain INVALID.
This preserves a known conflict; it does not change CM-R05 or approve an exception.
Bare Exchange lookup requires a record key because it is ambiguous.

Regression tests cover valid BEC CT3 Feature frames, rejection of complete
CT1/CT2 frames with and without IDs, rejected BC framing, preserved direct
behaviour anchoring, unresolved historical ancestry, collision uncertainty and
the updated BC inventory/open-issue boundaries.

## 9. Scope and Authority Preservation

**No BEC ancestry, Feature framing, new BC↔BEC relationship, Canonical ID or local
Capability code was manufactured.** The 69 descriptions and 137 Features were
not reconciled into new owners. Existing BC support relationships were preserved
without distributing them among the new descendants. Domain05 B2.2 is untouched.
No convergence/runtime milestone or subsequent architecture phase was started.

AX-14 governs semantic distinctions, including two hierarchies, organisational
versus enabling ability, availability/deployment/work, evidence/conclusion and
assurance/management. AX-17 governs explicit established/provisional/unresolved
standing and stopped conflict points. AX-15's operational uncertainty is not
misused to invent architecture or equate evidence insufficiency with execution
failure. AX-18 prevents information/coordination from conferring domain authority.

## 10. Points Requiring Architectural Adjudication

1. **Exchange name collision.** DN-BC-027's BC name duplicates the existing BEC
   Health Information Exchange, conflicting with CM-R05 and DN-BC-023's naming
   boundary. Both meanings are retained separately; no rename, alias, merger,
   hierarchy or mapping is decided. This point remains stopped for adjudication.
2. **Clinical Device Maintenance membership.** DN-BC-015 separately defines
   maintenance but explicitly lists/closes with seven CT3s omitting it. The
   defined maintenance semantics are preserved outside the established tree;
   no eighth child is assumed. Inclusion/tier/ancestry remains stopped.
3. **Insufficient supplied assertions.** No precise Sharing Programme parent,
   final Clinical Intervention taxonomy or CT3 sets for Clinical Determination
   and Individual Care Planning are supplied. They are not completed from a
   mindmap candidate or a conventional clinical taxonomy. Generic improvement
   placement is not inferred from the separately established service-wide
   Continuous Improvement CT3.
4. **BC-17 classification.** Definition/compliance scope is established, but
   material platform participation is insufficiently allocated to select a
   relevance value confidently. Existing uncertainty is retained.

The historical five-region review's pre-existing Value Stream `BC-*` reference
discrepancy also remains recorded; ordinals are not substituted as IDs. No newly
discovered information is copied from legacy documentation into RADS.

## 11. Validation

| Verification | Result |
| :--- | :--- |
| All offline architecture tests, Java 21 | **PASS** — 90 tests in 11 suites, 0 failures/errors/skips; BUILD SUCCESS, 17.938 seconds. |
| RADS reference/documentation regressions | **PASS** — 40 Python tests, including schema, source fingerprint/locator, inventory, uncertainty, ownership and grammar checks. |
| RADS validator | **PASS** — 0 invalid assertions; 2467 unresolved dimensions/relationships preserved separately. |
| Markdown rendering, fences, explicit anchors and local/inbound links | **PASS** — final counts in the verification result; no introduced or current checked link defects. |
| Semantic preservation assertions | **PASS** — BEC/Feature/behaviour assertions, 69 contexts and existing non-decomposition edges preserved; no IDs/codes; scratchpad unchanged. |
| `git diff --check` | **PASS** — final task verification. |

Commands (all bounded; no timeout/stall occurred):

```bash
timeout --signal=TERM --kill-after=10s 180s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 PATH=/usr/lib/jvm/java-21-openjdk-amd64/bin:$PATH mvn -o -B test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false -Dsurefire.forkedProcessTimeoutInSeconds=120
timeout 60s env PYTHONDONTWRITEBYTECODE=1 python3 -m unittest discover -s scripts/tests -p 'test_rads_business_architecture.py' -v
timeout 60s python3 scripts/rads_business_architecture.py validate
timeout 60s python3 /tmp/harmonia-bc-doc-check.py
git diff --check
```

No dedicated committed Markdown renderer/link suite was found; the bounded
temporary MarkdownIt check covers edited RADS documents and inbound references
to them. External links and Mermaid renderer output are not network/render tested.
The initial temporary preservation check incorrectly compared relocated source
line numbers as catalogue-context semantic changes, then expected one H1 in the
pre-existing multi-H1 BEC catalogue. Both harness assumptions were corrected
without altering BEC scope. Two real removed inbound anchors were restored.

Logs: `/tmp/harmonia-bc-architecture-tests.log`,
`/tmp/harmonia-bc-reference-tests.log`, `/tmp/harmonia-bc-doc-check.json`.
Temporary evidence is execution support, not architectural authority.
