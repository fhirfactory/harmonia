# Domain 04 — Entity, Identity, and Service Information Families Completion Report

**Date**: 2026-10-06  
**Status**: Completed  
**Domain**: Domain 04 (Information Architecture)

---

## 1. Overview & Purpose

This task established the first substantive package of detailed Information Architecture specifications for **Domain 04 — Information Architecture**, formalising the foundational **Entity, Identity, and Service Information Families**:

1. **Information Families Overview & Navigation** (`information-families/README.md`)
2. **Person and Healthcare Subject** (`information-families/person-healthcare-subject.md`)
3. **Practitioner** (`information-families/practitioner.md`)
4. **Organisation** (`information-families/organisation.md`)
5. **Healthcare Location / Care Place** (`information-families/healthcare-location.md`)
6. **Healthcare Service** (`information-families/healthcare-service.md`)
7. **Device** (`information-families/device.md`)

These models define the semantic entities, master identities, service definitions, contextual service bindings, and devices upon which subsequent clinical, diagnostic, encounter, workflow, and operational information families depend.

---

## 2. Files Reviewed

Authoritative upstream baselines reviewed and treated as strictly CLOSED and FROZEN:
- `docs/markdown/01-motivation/README.md`
- `docs/markdown/01-motivation/principles/architectural-axioms.md`
- `docs/markdown/02-strategy/README.md`
- `docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md`
- `docs/markdown/03-business-architecture/README.md`
- `docs/markdown/03-business-architecture/behaviours/01-entity-management.md`
- `docs/markdown/03-business-architecture/behaviours/02-service-administration.md`
- `docs/markdown/03-business-architecture/behaviours/03-service-delivery.md`
- `docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md`
- `docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md`
- `docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md`
- `docs/markdown/04-information-architecture/patterns/information-relationships.md`
- `docs/markdown/04-information-architecture/patterns/containment-and-collections.md`
- `docs/markdown/04-information-architecture/patterns/definition-to-accountability.md`
- `docs/markdown/04-information-architecture/governance/authority-custody-provenance.md`
- `docs/markdown/04-information-architecture/guardrails/modelling-guardrails.md`
- `docs/markdown/04-information-architecture/traceability/domain03-traceability.md`

---

## 3. Files Created

The canonical documentation structure was created under `docs/markdown/04-information-architecture/information-families/`:

1. `docs/markdown/04-information-architecture/information-families/README.md`: Overview, pedagogical navigation, structural rules, and authority demarcations for detailed information families.
2. `docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md`: Pure semantic model for `Person`, `Person Identity`, `Identifier`, `Identity Alias`, `Identity Correlation Graph`, `Identity Correction`, `Healthcare Subject Context`, and personal support/representation networks.
3. `docs/markdown/04-information-architecture/information-families/practitioner.md`: Workforce models for `Practitioner`, `Practitioner Identity`, `Practitioner Identifier`, `Professional Registration`, `Practitioner Profile`, `Clinical Privilege`, `Practitioner Role Binding`, and `Endpoint`.
4. `docs/markdown/04-information-architecture/information-families/organisation.md`: Models for `Healthcare Organisation`, `Organisation Identity`, `Organisation Identifier`, `Organisation Profile`, `Organisation Classification`, `Organisation Contact Information`, `Organisational Unit`, and recursive administrative containment.
5. `docs/markdown/04-information-architecture/information-families/healthcare-location.md`: Models for `Healthcare Location`, `Location Identity`, `Location Classification`, `Geospatial Address`, `Care-Place Definition`, and recursive physical containment.
6. `docs/markdown/04-information-architecture/information-families/healthcare-service.md`: Models for the 5-stage progression (`OfferedHealthcareService` → `DeliverableHealthcareService` → `HealthcareServiceDelivery` → `ServiceOutcome` → `AssuredHealthcareService`), `Order` as an associated direction mechanism, and the Service Provision evaluation.
7. `docs/markdown/04-information-architecture/information-families/device.md`: Models for `Device Definition` vs. `Device Instance`, dynamic temporal associations, and communication endpoints.
8. `.junie/reports/2026-10-06-domain04-entity-identity-service-information-families.md`: This completion report.

---

## 4. Files Modified

1. `docs/markdown/04-information-architecture/README.md`: Navigation-only modification updating the repository directory tree and reading path list to index the new `information-families/` package.
2. `.junie/plans/domain04-entity-identity-service-information-families.md`: Plan execution step status updates.

---

## 5. Information Concepts Established

| Information Family | Established Information Concepts | Semantic Metamodel Category |
| :--- | :--- | :--- |
| **Person & Healthcare Subject** | `Person`<br/>`Person Identity`<br/>`Identifier` (`Identifier Value` + `Identifier Authority`)<br/>`Identity Alias`<br/>`Identity Correlation Graph`<br/>`Identity Correction`<br/>`Healthcare Subject Context`<br/>`Family Relationship`<br/>`Carer Relationship`<br/>`Legal Representation` | Entity<br/>Assertion / Identity Bundle<br/>Assertion<br/>Assertion<br/>Relationship / Correlation Graph<br/>Activity / Governance Record<br/>Contextual Binding<br/>Relationship<br/>Relationship<br/>Relationship |
| **Practitioner** | `Practitioner`<br/>`Practitioner Identity`<br/>`Practitioner Identifier`<br/>`Professional Registration`<br/>`Professional Profile`<br/>`Clinical Privilege`<br/>`Practitioner Role Binding`<br/>`Endpoint` | Entity<br/>Assertion / Identity Bundle<br/>Assertion<br/>Assertion / Governed Relationship<br/>Assertion / Profile Bundle<br/>Assertion / Entitlement Directive<br/>Contextual Binding<br/>Assertion / Coordinate |
| **Organisation** | `Healthcare Organisation`<br/>`Organisation Identity`<br/>`Organisation Identifier`<br/>`Organisation Profile`<br/>`Organisation Classification`<br/>`Organisation Contact Information`<br/>`Organisational Unit` | Entity<br/>Assertion / Identity Bundle<br/>Assertion<br/>Assertion / Profile Bundle<br/>Assertion / Classification<br/>Assertion / Contact Bundle<br/>Entity / Administrative Subdivision |
| **Healthcare Location** | `Healthcare Location`<br/>`Location Identity`<br/>`Location Classification`<br/>`Geospatial Address`<br/>`Care-Place Definition` | Entity<br/>Assertion / Identity Bundle<br/>Assertion / Classification<br/>Assertion / Spatial Coordinate<br/>Definition |
| **Healthcare Service** | `OfferedHealthcareService`<br/>`DeliverableHealthcareService`<br/>`Order`<br/>`HealthcareServiceDelivery`<br/>`ServiceOutcome`<br/>`AssuredHealthcareService` | Definition (Stage 1)<br/>Contextual Binding (Stage 2)<br/>Assertion / Request Mechanism<br/>Activity / Fulfilment (Stage 3)<br/>Outcome (Stage 4)<br/>Accountability / Audit Record (Stage 5) |
| **Device** | `Device Definition`<br/>`Device Instance`<br/>`Device Association`<br/>`Endpoint` | Definition<br/>Entity<br/>Contextual / Temporal Relationship<br/>Assertion / Coordinate |

---

## 6. Significant Information Relationships Established

1. **Person Identity & Support**:
   - `Person.identifiedBy(PersonIdentity)`
   - `PersonIdentity.correlatedVia(IdentityCorrelationGraph)`
   - `Person.participatesAs(HealthcareSubjectContext)`
   - `Person.familyRelationshipWith(Person)`
   - `Person.carerFor(Person)`
   - `Person.authorisedRepresentativeFor(Person)`
2. **Practitioner & Professional Roles**:
   - `Practitioner.identifiedBy(PractitionerIdentity)`
   - `Practitioner.holds(ProfessionalRegistration)`
   - `Practitioner.granted(ClinicalPrivilege)`
   - `Practitioner.fulfills(PractitionerRoleBinding)`
   - `PractitionerRoleBinding.affiliatedWith(HealthcareOrganisation)`
   - `PractitionerRoleBinding.participatesIn(DeliverableHealthcareService)`
   - `PractitionerRoleBinding.reachesVia(Endpoint)`
3. **Organisation Hierarchies**:
   - `Organisation.contains(OrganisationalUnit)` (forward recursive containment)
   - `OrganisationalUnit.contains(OrganisationalUnit)`
4. **Location & Care-Place Containment**:
   - `HealthcareLocation.contains(HealthcareLocation)` (forward recursive spatial containment: `Campus` → `Building` → `Floor` → `Ward` → `Room` → `Bed`)
   - `CarePlace.governedBy(CarePlaceDefinition)`
5. **Healthcare Service Progression**:
   - `OfferedHealthcareService.contains(OfferedHealthcareService)` (recursive service composition)
   - `OfferedHealthcareService.contextualisedAs(DeliverableHealthcareService)`
   - `DeliverableHealthcareService.fulfilledBy(HealthcareServiceDelivery)`
   - `Order.directs(HealthcareServiceDelivery)`
   - `HealthcareServiceDelivery.produces(ServiceOutcome)`
   - `HealthcareServiceDelivery.evaluatedBy(AssuredHealthcareService)`
6. **Device Temporal Associations**:
   - `DeviceDefinition.instantiatedAs(DeviceInstance)`
   - `DeviceInstance.associatedWith(HealthcareSubjectContext)` (temporal)
   - `DeviceInstance.locatedAt(HealthcareLocation)` (temporal)
   - `DeviceInstance.participatesIn(HealthcareService)` (temporal)
   - `DeviceInstance.communicatesVia(Endpoint)`

---

## 7. Domain 03 Responsibility Traceability Matrix

All realised Information Concepts trace directly to owning Domain 03 Business Information Responsibilities:

| Domain 03 Capability | Domain 03 Information Responsibility | Realised Domain 04 Concepts |
| :--- | :--- | :--- |
| **`L2: Person Identity`** | `Person Identity & Identifier Correlation Graph`, `Identity Aliases`, `Identity Merge/Split Audit Log` | `Person`, `Person Identity`, `Identifier`, `Identity Alias`, `Identity Correlation Graph`, `Identity Correction` |
| **`L2: Healthcare Subject Context`** | `Healthcare Subject Profile`, `Demographic History`, `Communication Preferences` | `Healthcare Subject Context`, `Demographic Trait Assertion`, `Communication Preference` |
| **`L2: Client Relationships & Support Network`** | `Client Support Network & Legal Mandates`, `Representative Legal Mandate`, `Carer Contact Directory` | `Family Relationship`, `Carer Relationship`, `Legal Representation`, `Support Person Relationship` |
| **`L1: Provider Administration`** | `Practitioner Registry & Role Bindings`, `Professional Registration Status`, `Practitioner Role Bindings`, `Electronic Communication Endpoints`, `Scope of Practice Privileges` | `Practitioner`, `Practitioner Identity`, `Practitioner Identifier`, `Professional Registration`, `Professional Profile`, `Clinical Privilege`, `Practitioner Role Binding`, `Endpoint` |
| **`L1: Organisation Administration`** | `Healthcare Organisation Registry`, `National Facility Identifier Bindings`, `Department Hierarchy Graph`, `Organisation Contact Directory` | `Healthcare Organisation`, `Organisation Identity`, `Organisation Identifier`, `Organisation Profile`, `Organisation Classification`, `Organisation Contact Information`, `Organisational Unit` |
| **`L1: Location Administration`** | `Location & Care-Place Definitions`, `Physical Hierarchy Graph`, `Care-Place Specifications` | `Healthcare Location`, `Location Identity`, `Location Classification`, `Geospatial Address`, `Care-Place Definition` |
| **`L1: Health Service Administration`** | `Service Catalogue & Service/Location/Provider Map`, `Service Availability Schedules`, `Service Eligibility Rule Sets` | `OfferedHealthcareService`, `DeliverableHealthcareService`, `Availability Schedule`, `Eligibility Rule Set` |
| **`L1: Clinical Device Administration`** | `Device Registry & Association Ledger`, `Medical Device Type Registry`, `Physical Device Instance Directory`, `Device Association Ledger`, `Device Communication Endpoint Map` | `Device Definition`, `Device Instance`, `Device Association`, `Endpoint` |
| **`L1: Order Administration`** | `Clinical Order Master Record`, `Closed-Loop Tracking Ledger`, `Order-Result Correlation Matrix` | `Order` *(Associated Request / Direction Mechanism)* |

---

## 8. Authority & Provenance Findings

1. **Assertion-Level Provenance**: Authority, validity, and evidence attach directly to individual assertions (e.g. identifiers, alias names, qualifications, privileges), enabling composite multi-source records without merging or transferring originating authority.
2. **Federated External Authorities**: National registration boards, statutory identifier authorities, and external regulatory bodies retain originating authority. Harmonia acts as custodian or consumer and records verification provenance.
3. **Non-Destructive History**: Identity corrections (merges/splits) and profile/location reconfigurations preserve complete historical audit logs without destructively rewriting historical assertions.

---

## 9. Service Provision Modelling Evaluation & Decision

### Semantic Evaluation
Derived from the Domain 03 responsibility `Maintain Service / Location / Provider Map` (`Service / Location / Provider Mapping Matrix`), Service Provision was evaluated across structural alternatives:
- **Option A (Pure Binary Links)**: Disconnected binary links (`Service-Location`, `Service-Provider`, `Provider-Location`) were rejected because Service Provision is inherently multi-dimensional. Collapsing it into binary links loses the essential context that Provider A offers Service X *only* at Location Y under Schedule Z with Practitioner Role R (violating Guardrail 15).
- **Option B (Separate Reified Concept alongside Service Model)**: Introducing a separate `ServiceProvisionMapping` entity alongside `DeliverableHealthcareService` was rejected as redundant structural duplication.
- **Option C (Unified Contextual Binding via `DeliverableHealthcareService`)**: **Adopted**. `DeliverableHealthcareService` natively serves as Stage 2 (Contextualisation / Binding) in the 5-stage metamodel pattern and embodies the reified Service Provision mapping binding:
  - `OfferedHealthcareService` (Definition)
  - `Healthcare Organisation` (fulfilling the Domain 03 Business Role `Service Provider`)
  - `Healthcare Location` (Delivery Site)
  - `Practitioner Role Binding` (Participating Workforce Roles)
  - `Availability Schedule` (Operating Hours)
  - `Eligibility Rule Set` (Catchment & Clinical Indications)

---

## 10. Candidate Information Assembly Participation

The established foundational concepts participate in candidate Information Assemblies without transferring originating authority:
1. **Healthcare Subject Context Assembly**: Aggregates `Person Identity`, `Healthcare Subject Context`, active `Identity Aliases`, and communication preferences.
2. **Client Support Network Assembly**: Aggregates verified `Carer Relationships`, `Legal Representation` mandates, and emergency contacts.
3. **Practitioner Context Assembly**: Aggregates `Practitioner`, active `Professional Registration`, verified `Clinical Privileges`, active `Practitioner Role Bindings`, and secure `Endpoints`.
4. **Service Provision Context Assembly**: Aggregates `DeliverableHealthcareService`, `OfferedHealthcareService`, delivering `Healthcare Organisation`, `Healthcare Location`, `Practitioner Role Bindings`, and availability schedules.
5. **Encounter Context Assembly**: Aggregates `Healthcare Subject Context`, attending `Practitioner Role Bindings`, service delivery `Healthcare Location`, and delivered `Healthcare Services`.
6. **Bed / Ward Operational Context Assembly**: Aggregates `Healthcare Location` (`Care-Place Definition`) with dynamic operational occupancy and telemetry devices.

---

## 11. Semantic Hygiene Audit & Corrections Applied

A rigorous semantic hygiene review was executed across all seven information-family documents with the following confirmed corrections:

1. **Generic Semantic Terminology in Normative Definitions**:
   - Replaced implementation-specific and jurisdiction-specific terms in normative definitions with generic concepts (`Device Identifier`, `Organisation Identifier`, `External Directory Identifier`, `Device Version / Configuration Assertion`, `Registration Authority`, `Regulatory Classification`).
   - Specific real-world schemes (UDI, asset tags, AHPRA, HPI-I) are retained exclusively as non-normative illustrative examples.
2. **Purged Non-Canonical Business Roles**:
   - Verified zero occurrences of the invented Business Role "Healthcare Professional". All workforce roles strictly use canonical Domain 03 Business Roles (`Practitioner`, `Clinician`, `Performer`, `Prescriber`, `Requester`, `Care Coordinator`, `Service Provider`).
3. **Canonical Order Administration Traceability**:
   - Replaced all occurrences of `Clinical Order & Closed-Loop Matrix` with the exact canonical Domain 03 Information Responsibilities: `Clinical Order Master Record`, `Closed-Loop Tracking Ledger`, `Order-Result Correlation Matrix`.
4. **Governed Person Identity Semantics**:
   - Purged all "master / golden record" terminology from normative definitions.
   - Defined `Person Identity` as governed identity information concerning a `Person`. Explicitly confirmed that cross-authority correlation and identity correction preserve source authorities and provenance without single-record destructive consolidation.
5. **Direct Service Provision Semantics**:
   - Replaced `Service Provision Stewardship` and `Service Provision Mandate` with direct `DeliverableHealthcareService provided-by Healthcare Organisation` where the organisation fulfils the Domain 03 `Service Provider` Business Role.
6. **Canonical Terminology Audit**:
   - Automated scans confirmed zero remaining non-canonical Domain 03 terms across all files under `information-families/`.

---

## 12. Issues or Ambiguities Identified

- No blocking issues or contradictions were identified.
- Upstream baselines and the Domain 04 foundational metamodel provided clear and unambiguous guidance.

---

## 13. Explicit Confirmations

- **Upstream Immutability**: Confirmed that **Domain 01 (Motivation)**, **Domain 02 (Strategy)**, and **Domain 03 (Business Architecture)** were **NOT modified** in any way.
- **Foundation Immutability**: Confirmed that the frozen Domain 04 Foundation (`metamodel/`, `patterns/`, `governance/`, `assemblies-views/`, `guardrails/`, `traceability/`) was **NOT modified**.
- **Navigation-Only Root Update**: Confirmed that `docs/markdown/04-information-architecture/README.md` was modified strictly with navigation links indexing the new `information-families/` section.
- **Scope Discipline**: Confirmed that no FHIR resource definitions, Java classes, JPA entities, database DDL, API endpoints, or downstream implementation constructs were introduced.
- **Jurisdiction Agnosticism**: Confirmed that canonical conceptual models use generic semantic terminology rather than jurisdiction-specific schemes.
