<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

# Entity Management — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

The named owning Capabilities and established Features retain their responsibilities. Affected Capability Tier, complete ancestry, root status and structural Canonical IDs remain unresolved under [approved G1 K9](../../04-information-architecture/reviews/package2-g1-review.md#11-k9--capability-metamodel-typing); document grouping and numbering do not establish architectural identity or hierarchy.

Entity Management establishes the canonical master data governance, identity correlation, and structural definitions for all core business entities participating in healthcare delivery: Clients/Patients, Healthcare Practitioners, Healthcare Organisations, Locations, Health Services, and Clinical Devices.

---

## 2. Client Administration

Client Administration governs the identity, demographic context, relationship associations, and privacy directives of persons receiving care.

### 2.1 Person Identity
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Person Identity Administration](../../../../enterprise-architecture/continuum/strategy/capability/6c3de7750ef1436cad6a9533731782b6.md)
- [[CT2] Client Administration](../../../../enterprise-architecture/continuum/strategy/capability/f0885472cced4bccbcce1568bf0360dc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Person Identity` (under `Client Administration`)


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Identifier Resolution**: Asks for authoritative detail and verification regarding a specific person identifier within a single domain namespace.
  - **Identifier Correlation**: Ingests and maintains cross-authority associations between identifier namespaces where the governing relationship is established by authoritative information.
  - **Identity Alias**: Contextual, legal, or alternative identity expressions (e.g., preferred names, maiden names, emergency aliases) by which a person is known. It is conceptually distinct from cross-domain correlation.
  - **R1.x/R2.x authority boundary**: Harmonia ingests and maintains authoritative identifiers, aliases and identity relationships, processes externally authoritative corrections/merge outcomes and propagates managed-state consequences. It does not originate probabilistic matching, golden-record determination, authoritative master-person selection or person-merge decisions. [REQ-FND-003](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-003-subject-referential-integrity) and the [Strategy scope statement](../../02-strategy/capabilities/business-enabling-capabilities.md#architectural-scope-statement-harmonia-1x2x-vs-3x-roadmap) govern this boundary.
- **Functions & Exposed Services**:
  - **Feature: Identifier Resolution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resolve Person Identifier](../../../../enterprise-architecture/continuum/business/business-function/2f128e1d8de642478c18b7a0ccb8e083.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Resolve Person Identifier` — Validates and retrieves the authoritative demographic record bound to a specific identifier.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Person Identifier Resolution](../../../../enterprise-architecture/continuum/business/business-service/a533cf0d23364f398f2a71843cce98c4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Person Identifier Resolution` — Exposed to Service Administration and Service Delivery to disambiguate client identities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Cross-Authority Identifier Correlation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Correlate Person Identifiers](../../../../enterprise-architecture/continuum/business/business-function/3050366e2e1d47eeaa4cfd6db92c0a56.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Correlate Person Identifiers` — Correlates local identifiers (e.g., Hospital MRN, National IHI, Pathology ID) using relationships established by authoritative information, retaining their source and namespace.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Person Identifier Correlation](../../../../enterprise-architecture/continuum/business/business-service/71220901361e42f686faf7d852f57e45.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Person Identifier Correlation` — Exposed across the HIE to establish patient cross-reference mappings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Identity Alias Association**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Person Identity Aliases](../../../../enterprise-architecture/continuum/business/business-function/9db2528e180a418eacbad3acf86b3956.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Person Identity Aliases` — Records and qualifies alias names, titles, and emergency aliases for a governed person identity.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

    - *Exposed Service*: *(Internal Function — accessed via Person Identifier Resolution)*.
  - **Feature: Governed Identity Correction**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Apply Governed Person Identity Correction](../../../../enterprise-architecture/continuum/business/business-function/844b9abe470d4f4684f35adddf1b880d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Apply Governed Person Identity Correction` — Processes externally authoritative corrections and merge/unlink outcomes, updates governed identity associations and propagates resulting managed-state consequences with audit history. Harmonia does not adjudicate the originating person-merge decision.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Person Identity Correction](../../../../enterprise-architecture/continuum/business/business-service/dd5ed5ba58f94ecbbdac913c88d2b7b6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Apply Governed Person Identity Correction](../../../../enterprise-architecture/continuum/business/business-function/844b9abe470d4f4684f35adddf1b880d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Governed Person Identity Correction Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Person Identity Record, Identifier Namespace Bindings, Cross-Authority Correlation Graph, Identity Aliases, Identity Merge/Split Audit Log.

---

### 2.2 Healthcare Subject
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Healthcare Subject Profile Administration](../../../../enterprise-architecture/continuum/strategy/capability/df214b5499694b768eb4a7a175a9d471.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Healthcare Subject`.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Owner derivation**: Strategy Healthcare Subject owns Subject Context Binding and Demographic Context Governance; the Functions below discharge those same responsibilities. “Healthcare Subject Context” describes information/behaviour context, not another Capability or alias.
- **Functions & Exposed Services**:
  - **Feature: Subject Context Binding**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Establish Healthcare Subject Context](../../../../enterprise-architecture/continuum/business/business-function/f445f86f9de24461aabee527e5b41287.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Establish Healthcare Subject Context` — Binds demographic, cultural, and communication requirements to a patient record.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Healthcare Subject Context Resolution](../../../../enterprise-architecture/continuum/business/business-service/99a739efc7be4d7b92d90181ecd69d15.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Healthcare Subject Context Resolution` — Provides comprehensive demographic, language, and indigenous status context to clinical applications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Demographic Context Governance**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Subject Demographic Context](../../../../enterprise-architecture/continuum/business/business-function/76288dacb9034e0bbf75b43d8afe59d2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Subject Demographic Context` — Applies validation rules to address changes, date of birth verification, and death notifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Demographic Change Distribution](../../../../enterprise-architecture/continuum/business/business-service/2c4a8c1251294885922f8ccdc3d7b069.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Demographic Change Distribution` — Emits governed subject demographic updates to subscribed clinical systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Healthcare Subject Profile, Demographic History, Communication Preferences.

---

### 2.3 Client Relationship
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Client Relationship Administration](../../../../enterprise-architecture/continuum/strategy/capability/d682c11d3eb04770b1f20be478fd475e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Client Relationship`.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Owner derivation**: Strategy Client Relationship owns Next-of-Kin & Guardian Association and Relationship Validity Tracking. Support-network wording describes those responsibilities without establishing a separate Capability, alias or specialisation.
- **Functions & Exposed Services**:
  - **Feature: Next-of-Kin & Guardian Association**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Client Relationships](../../../../enterprise-architecture/continuum/business/business-function/1b2ce4a240384672b78976904b83a011.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Client Relationships` — Records links between patients and carers, next of kin, nominated representatives, and legal guardians.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Client Relationship Query](../../../../enterprise-architecture/continuum/business/business-service/7b3eb2e91baa4e9c887927433979a8f6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Client Relationship Query` — Discloses verified support network members and contact details to clinical teams.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Relationship Validity Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Client Relationship Validity](../../../../enterprise-architecture/continuum/business/business-function/dc87537df6254fc5bdeeca0ccc011679.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Client Relationship Validity` — Verifies power-of-attorney documents, guardianship orders, and proxy validity periods.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Representative Authority Verification](../../../../enterprise-architecture/continuum/business/business-service/2d3205c785ba4db5af5868ebb6ab1495.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Representative Authority Verification` — Confirms legal decision-making authority for substitute consent.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Client Relationship Record, Representative Legal Mandate, Carer Contact Directory.

---

### 2.4 Client Privacy
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT3] Client Privacy Guardrail Management](../../../../enterprise-architecture/continuum/strategy/capability/7577a6e1815243648dd5356c21cca151.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Client Privacy`.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Owner derivation**: Strategy Client Privacy owns Consent Directive Evaluation and Confidentiality Flag Enforcement. Consent-directive wording describes that responsibility without creating a separate Capability.
- **Functions & Exposed Services**:
  - **Feature: Consent Directive Evaluation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Evaluate Client Consent](../../../../enterprise-architecture/continuum/business/business-function/b9e978a651cf4ff3bb690af50b8528cd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Evaluate Client Consent` — Evaluates patient opt-in, opt-out, and general information sharing preferences against proposed exchanges.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Consent Decision Service](../../../../enterprise-architecture/continuum/business/business-service/e1bebefbdbef4a1981ec31341eb71dfd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Confidentiality Flag Enforcement**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Evaluate Client Confidentiality Restrictions](../../../../enterprise-architecture/continuum/business/business-function/a61324c3ef734910af5886f048cd3bf5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Evaluate Client Confidentiality Restrictions` — Evaluates sensitive record flags (e.g., VIP masking, restricted department exclusions).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Confidentiality Restriction Evaluation](../../../../enterprise-architecture/continuum/business/business-service/87cfcfb20b7a4570b502b4a9a53803ad.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Confidentiality Restriction Evaluation` — Enforces record-level masking directives across the HIE.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Client Consent Directive, Record Masking Rule, Disclosure Restriction Register.

---

## 3. Provider Administration

Provider Administration governs individual healthcare practitioners, their professional qualifications, clinical roles, electronic communication endpoints, and organizational affiliations.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Provider Administration](../../../../enterprise-architecture/continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Provider Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Practitioner $\neq$ Service Provider**: A *Practitioner* is an individual professional. A *Service Provider* is an organisation or facility offering health services. A Practitioner may act in the *Service Provider* role in private practice.
  - **Role and privilege**: Practitioner Role Resolution supplies role/capacity information; it does not establish Clinical Privilege or Operational Privilege. Clinical Privilege Verification retains its clinical scope. An activity may require both distinct privilege determinations; neither follows from Role information alone.
- **Functions & Exposed Services**:
  - **Feature: National Practitioner Verification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Practitioner Registration](../../../../enterprise-architecture/continuum/business/business-function/a9f4fe6090bf4a4b946ae7064213945c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Verify Practitioner Registration` — Validates national registration, specialty endorsements, and disciplinary sanctions against regulatory registries (e.g., AHPRA).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner Verification Service](../../../../enterprise-architecture/continuum/business/business-service/adf039a87e9243a1b82612a4919f6f14.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Practitioner Verification Service` — Discloses verified registration and status to credentialing and clinical systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Practitioner Registration](../../../../enterprise-architecture/continuum/business/business-function/a9f4fe6090bf4a4b946ae7064213945c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Governed Process*: **Practitioner Verification Process**.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Practitioner Profile Governance**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Practitioner Profile](../../../../enterprise-architecture/continuum/business/business-function/a9a1a2c5ab544c029ba0c362a305eaa5.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Practitioner Profile` — Maintains official names, professional identifiers (e.g., HPI-I, Prescriber Number), and clinical qualifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner Directory Query](../../../../enterprise-architecture/continuum/business/business-service/ed8e6a3d891340fe92fc3201abd15c46.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Practitioner Directory Query` — Provides provider directory lookups across the healthcare network.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Role & Specialty Modeling**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Practitioner Roles](../../../../enterprise-architecture/continuum/business/business-function/08a269d84dad46a58d6b9181b465ddc4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Practitioner Roles` — Assigns and maintains clinical roles (e.g., Specialist Physician, Registrar, General Practitioner) fulfilled by a practitioner.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner Role Resolution](../../../../enterprise-architecture/continuum/business/business-service/c3e5c04e2a194b4a8d49b8776fce2c51.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Practitioner Role Resolution` — Resolves the specific clinical capacities of a practitioner.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Electronic Endpoint Binding**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Bind Practitioner Electronic Endpoints](../../../../enterprise-architecture/continuum/business/business-function/11962189d1734cca840842209613c8d1.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Bind Practitioner Electronic Endpoints` — Associates secure messaging addresses, direct communication endpoints, and notification preferences to practitioner roles.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner Endpoint Lookup](../../../../enterprise-architecture/continuum/business/business-service/2efb21629acf41e9b4f5c59db00721d0.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Practitioner Endpoint Lookup` — Provides secure message routing destinations for clinical communication.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Practitioner-Organisation Affiliation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Practitioner Affiliations](../../../../enterprise-architecture/continuum/business/business-function/7dc829546e99424dbbf8f19b4565d3fc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Practitioner Affiliations` — Tracks employment, admitting rights, and consulting appointments at specific healthcare facilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Practitioner Affiliation Resolution](../../../../enterprise-architecture/continuum/business/business-service/65baca2a21f040d49be74fab1727f96d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Practitioner Affiliation Resolution` — Discloses organizational ties and practice locations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Clinical Privileging Record**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Clinical Privileges](../../../../enterprise-architecture/continuum/business/business-function/4965ae4538cc42cd8af1bd9a1905b108.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Clinical Privileges` — Records approved clinical scopes of practice, procedural authorizations, and prescribing privileges.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Privilege Verification](../../../../enterprise-architecture/continuum/business/business-service/f6599b4283194577a977bfc86ea854c2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Clinical Privilege Verification` — Verifies procedural and prescribing authority for clinical ordering systems.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Practitioner Master Record, Professional Registration Status, Practitioner Role Bindings, Electronic Communication Endpoints, Scope of Practice Privileges.

---

## 4. Organisation Administration

Organisation Administration maintains the authoritative registry of healthcare organisations, networks, directorates, and administrative subdivisions.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Organisation Administration](../../../../enterprise-architecture/continuum/strategy/capability/14fbdc5ce3dd4c2092c5397dab3ed743.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Organisation Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: National Organisation Verification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Verify Healthcare Organisation](../../../../enterprise-architecture/continuum/business/business-function/c39236ca89864f089684242df5647c9d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Verify Healthcare Organisation` — Validates enterprise legal identity, national facility identifiers (e.g., HPI-O), and accreditation status.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Organisation Verification Service](../../../../enterprise-architecture/continuum/business/business-service/223b2cbe762b40efb9f05bb5cdd66bcb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Organisation Verification Service` — Provides verified organisation credentials to external and internal partners.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Organisation Profile Management**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Govern Organisation Profile](../../../../enterprise-architecture/continuum/business/business-function/08990a2aa346403f8dd7d716acf188bf.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Govern Organisation Profile` — Maintains official trading names, enterprise registration numbers, and governing bodies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Organisation Directory Query](../../../../enterprise-architecture/continuum/business/business-service/c5acf1dc97874d0a8264f9a5c1b735fa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Organisation Directory Query` — Discloses healthcare organisation profiles across the region.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Hierarchical Structure Navigation**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Organisational Structure](../../../../enterprise-architecture/continuum/business/business-function/5ae4edf0813f4a738fdb3160f3e77b0d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Organisational Structure` — Models parent-subsidiary relationships, clinical directorates, departments, and operational divisions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Organisational Hierarchy Resolution](../../../../enterprise-architecture/continuum/business/business-service/c9187704dd7f49f69f9e674c16e9181b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Organisational Hierarchy Resolution` — Resolves department nesting and reporting hierarchies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Directory Contact Management**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Organisation Contacts](../../../../enterprise-architecture/continuum/business/business-function/4584f74d08834bc2bc595463ad51bd37.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Organisation Contacts` — Manages administrative, clinical, and emergency contact details for facilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Organisation Contact Lookup](../../../../enterprise-architecture/continuum/business/business-service/551f1e73fb73449a83f3155cb8158f30.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Organisation Contact Lookup` — Provides official operational contact details.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Healthcare Organisation Registry, National Facility Identifier Bindings, Department Hierarchy Graph, Organisation Contact Directory.

---

## 5. Location Administration

Location Administration defines the physical, functional, and geospatial structure of healthcare environments, including sites, buildings, floors, wards, rooms, bays, and bed care-places.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Location Administration](../../../../enterprise-architecture/continuum/strategy/capability/c410005667444f99a221483bf577fad2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Location Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Definition vs. Operational State**: *Location Administration* defines what a physical location or care-place **IS** (its static attributes, type, capacity, hierarchy). *Health Service Operations* owns what is **operationally happening** to that care-place (occupancy, cleaning status, isolation locks, turnover).
- **Functions & Exposed Services**:
  - **Feature: Location Geospatial & Address Resolution**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Healthcare Location](../../../../enterprise-architecture/continuum/business/business-function/448f1c9317914498ab246f74d4ce2bd6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Healthcare Location` — Records physical sites, campuses, outpatient clinics, and community health centres.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Location Directory Query](../../../../enterprise-architecture/continuum/business/business-service/5ef66362eb674794b335c435227b0c95.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Location Directory Query` — Exposes location profiles and geospatial addresses.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Physical Hierarchy Modeling**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Physical Location Hierarchy](../../../../enterprise-architecture/continuum/business/business-function/d217a5cd3a3d40e9a89068d1680ec45f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Physical Location Hierarchy` — Models the nested structural containment tree: `Campus` $\to$ `Building` $\to$ `Floor` $\to$ `Ward/Suite` $\to$ `Room/Bay` $\to$ `Bed Care-Place`.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Location Hierarchy Resolution](../../../../enterprise-architecture/continuum/business/business-service/389af7aaa9d74ab8b42e55f0fa72ef99.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Location Hierarchy Resolution` — Discloses physical containment and navigation paths for clinical facilities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Atomic Care-Place Modeling**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Care-Place Definition](../../../../enterprise-architecture/continuum/business/business-function/ad9aa4eeecf84eb08460234ed0dc4ecc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Care-Place Definition` — Defines static bed bays, treatment chairs, surgical tables, and emergency resuscitation cubicles, including equipment capabilities and oxygen/telemetry infrastructure.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Care-Place Specification Lookup](../../../../enterprise-architecture/continuum/business/business-service/43b011b0c233408eaf6e555ecbf05d9e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Care-Place Specification Lookup` — Discloses care-place physical capabilities to Bed & Care-Place Management.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Location Master Registry, Physical Facility Hierarchy Tree, Care-Place Structural Definitions.

---

## 6. Health Service Administration

Health Service Administration governs the catalogue of clinical services offered across the healthcare network and resolves the critical multi-dimensional mapping between Services, Providers, Locations, and Practitioners.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Health Service Administration](../../../../enterprise-architecture/continuum/strategy/capability/ba68a52913a0499d8377553bddfa74a4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Health Service Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Key Architectural Semantics**:
  - **Service / Location / Provider Map**: This capability models the multi-way relationship:
    $$\text{Healthcare Service} \longleftrightarrow \text{Service Provider} \longleftrightarrow \text{Healthcare Location} \longleftrightarrow \text{Practitioner Role}$$
  - **Ownership Invariant**: The mapping capability owns the **relationships and association rules**. It does **not** acquire ownership of the Service, Provider, Location, or Practitioner entities being mapped.
  - **Consumer Resolution Scenarios**: Enables clinical and administrative consumers to unambiguously resolve:
    1. *Who provides Service X?*
    2. *Where does Provider Y provide Service X?*
    3. *What Services are delivered at Location Z?*
    4. *Where can Service X be obtained?*
    5. *Which Practitioner Roles participate in Service X?*
- **Functions & Exposed Services**:
  - **Feature: Service Catalogue Publishing**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Healthcare Service Definition](../../../../enterprise-architecture/continuum/business/business-function/833a98c58dae416da783ef6e77290576.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Healthcare Service Definition` — Defines clinical service offerings (e.g., Cardiology Outpatient Clinic, MRI Imaging, Acute Stroke Care), specialty classifications, and clinical scope.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Healthcare Service Directory Query](../../../../enterprise-architecture/continuum/business/business-service/c7bf105e1217441a9423706b1481d153.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Healthcare Service Directory Query` — Exposes published clinical service offerings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Service-Location-Provider Binding**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Service / Location / Provider Map](../../../../enterprise-architecture/continuum/business/business-function/ea06c77ea3c841cc94cb1686295322b4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Service / Location / Provider Map` — Binds health service definitions to delivering organisations, physical clinic locations, and participating practitioner roles.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Provision Resolution](../../../../enterprise-architecture/continuum/business/business-service/e54866d88b1e442f8ea170f06f98028b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Service Provision Resolution` — Exposes multi-dimensional resolution queries answering who, where, and when healthcare services are delivered.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Operating Schedule Specification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Service Availability](../../../../enterprise-architecture/continuum/business/business-function/38321dd2581b46a182dcdf52865d44be.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Service Availability` — Governs regular operating hours, emergency intake availability, and seasonal clinic schedules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Availability Query](../../../../enterprise-architecture/continuum/business/business-service/a3846cb2960a4000a5db9ef18942ee20.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Service Availability Query` — Discloses operational intake hours and open status.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Eligibility & Catchment Specification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Service Eligibility Rules](../../../../enterprise-architecture/continuum/business/business-function/e1e1cf2aa30840368d107ab9dfd0257f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Service Eligibility Rules` — Configures catchment criteria, age thresholds, referral requirements, and clinical indications for service access.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Eligibility Evaluation](../../../../enterprise-architecture/continuum/business/business-service/7e005ce374f347088ae854442fc3edef.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Service Eligibility Evaluation` — Evaluates referral eligibility for intake coordinators.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Healthcare Service Catalogue, Service / Location / Provider Mapping Matrix, Service Availability Schedules, Service Eligibility Rule Sets.

---

## 7. Clinical Device Administration

Clinical Device Administration governs the master definition, instance identification, and electronic communication endpoints of medical equipment and point-of-care appliances.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[CT2] Clinical Device Administration](../../../../enterprise-architecture/continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Owning Capability**: `Clinical Device Administration`


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Functions & Exposed Services**:
  - **Feature: Device Type Specification**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Device Definition](../../../../enterprise-architecture/continuum/business/business-function/d44083e67fb540579737337cabf2c227.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Device Definition` — Models medical device models, manufacturers, regulatory classifications (e.g., TGA/FDA classes), and calibration specifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Device Definition Query](../../../../enterprise-architecture/continuum/business/business-service/49325c789401465aa565fd7fa7109070.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Device Definition Query` — Discloses approved medical device models and technical parameters.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Device Serial Tracking**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Device Instance Identity](../../../../enterprise-architecture/continuum/business/business-function/927796d77f5e4f61835bd2a7bcceb681.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Device Instance Identity` — Tracks physical serial numbers, Unique Device Identifiers (UDI), asset tags, and firmware versions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Device Instance Resolution](../../../../enterprise-architecture/continuum/business/business-service/14fb1e3f17dd40d88087a00c9707578d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Device Instance Resolution` — Resolves physical device instances from scanned barcodes or UDIs.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Device-Patient/Location Assignment**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Device Association](../../../../enterprise-architecture/continuum/business/business-function/fc0f4d9c781241359d783d26f038b7ff.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Device Association` — Records active associations between clinical devices, patient encounters, and specific care-places (e.g., Bed 4 Vital Signs Monitor).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Device Association Query](../../../../enterprise-architecture/continuum/business/business-service/01637c40914945d68d261c2094b86904.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Device Association Query` — Discloses active device-to-patient and device-to-location bindings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

  - **Feature: Device Endpoint Registration**:
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Maintain Device Communication Endpoints](../../../../enterprise-architecture/continuum/business/business-function/ab5a7538fbd449ae8214d584b7d53a43.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Function*: `Maintain Device Communication Endpoints` — Manages network telemetry addresses, gateway ports, and protocol bindings for medical devices.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Device Endpoint Resolution](../../../../enterprise-architecture/continuum/business/business-service/5806d6e030b543d0867a862d5b6d8fad.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

    - *Exposed Service*: `Device Endpoint Resolution` — Exposes network addresses for telemetry routing.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

- **Information Responsibility**: Medical Device Type Registry, Physical Device Instance Directory, Device Association Ledger, Device Communication Endpoint Map.
