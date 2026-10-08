# Business Interaction Catalogue & Boundary Rules

## 1. Definition & Architectural Purpose

In the Harmonia Business Architecture, a **Business Interaction** represents a discrete, observable, and purposeful exchange of healthcare information, directives, requests, or commitments between Business Actors acting in defined Business Roles.

An Interaction answers the architectural question:
> **What specific business dialogue, notification, directive, or exchange is taking place between participants?**

A Business Interaction is **not** synonymous with a Business Service:
- A **Business Service** is behaviour exposed across a capability boundary.
- A **Business Interaction** is the formal exchange pattern through which participants communicate.
- A single Business Service may participate in, initiate, or support one or more Business Interactions.

---

## 2. The Canonical R1 Business Interaction Catalogue

Harmonia defines an authoritative catalogue of Business Interactions structured into ten primary functional categories:

```text
Business Interaction Catalogue
├── 1. Clinical Information Interactions
├── 2. Service Delivery Interactions
├── 3. Operational Logistics & Work Interactions
├── 4. Information Governance & Control Interactions
├── 5. Authority, Statutory & Regulatory Interactions
├── 6. Identity Management Interactions
├── 7. Relationship Management Interactions
├── 8. Event & Alert Notification Interactions
├── 9. Review & Clinical Governance Interactions
├── 10. Participation & Alignment Interactions
└── Supplementary Categories:
    ├── Service Offering & Catalogue Interactions
    └── Incident Management Interactions
```

---

### 2.1 Clinical Information Interactions
Interactions concerning the routine exchange, query, review, and collaborative discussion of patient clinical records and diagnostic documentation.

- **Clinical Information Supply**: Routine, authorised transmission or contribution of clinical information, discharge summaries, or diagnostic results into the integration environment.
- **Clinical Information Provision**: The delivery of requested clinical record content to an authorised clinician or system.
- **Clinical Information Query**: A targeted request to discover or search for clinical records, observations, or history for a specific healthcare subject.
- **Clinical Information Review**: The structured examination, validation, or clinical evaluation of clinical record content by a practitioner.
- **Clinical Collaboration**: Structured, multi-party dialogue, handover messaging, or clinical commentary among care team members.
- **Clinical Information Notification**: An automated or operational notice informing participants that new or updated clinical information is now available.

### 2.2 Service Delivery Interactions
Interactions governing the request, referral, coordination, progression, and outcome reporting of clinical services.

- **Service Referral**: A formal request for a specialist, service provider, or clinical department to assess, manage, or assume care responsibility for a patient.
- **Service Request**: A formal order placed for a specific diagnostic test, therapeutic procedure, or clinical service.
- **Service Response**: A communication indicating the status, triage decision, scheduled timing, or acceptance disposition of a referral or request.
- **Service Outcome**: The formal clinical finding, procedural report, or discharge disposition resulting from completed service delivery.
- **Transfer of Care**: The formal transition of clinical and medico-legal accountability for a patient between care teams, wards, or organisations.
- **Service Coordination**: Dialogue aimed at synchronising multiple care delivery activities across clinical specialties or care settings.
- **Coordination Outcome**: A recorded summary of coordinated care decisions, agreed milestones, or integrated care plans.

### 2.3 Operational Logistics & Work Interactions
Interactions governing facility operations, bed assignments, portering, specimen logistics, and staff task dispatch.

- **Operational Coordination**: Tactical alignment regarding resource allocation, bed distribution, queue balancing, and facility throughput.
- **Resource Reporting**: Telemetry or status reporting on the availability, capacity, or status of physical beds, theatres, or clinical equipment.
- **Resource Allocation**: The formal assignment or reservation of a physical care-place, bed, theatre slot, or device to a patient.
- **Work Assignment**: The formal dispatch or allocation of an operational task (e.g., patient transfer, cleaning, specimen delivery) to a staff member or role.
- **Reassignment**: The redirection of an assigned operational task to an alternative worker or unit due to operational constraints.
- **Work Acceptance**: Acknowledgement and commitment by an operational worker to execute an assigned task.
- **Work Progress**: Milestone updates regarding the active execution status of an in-flight operational task.
- **Work Outcome**: The formal completion report and outcome disposition of an operational task.
- **Operational Coordination Outcome**: Recorded agreements from operational huddles or bed-meeting decisions.

### 2.4 Information Governance & Control Interactions
Interactions governing the access control, privacy enforcement, correction, and publication of health information assets.

- **Information Access**: An authorised consumer exercising legitimate authority to retrieve or inspect governed health information.
- **Information Disclosure**: An information custodian or system exercising authority or legal obligation to release records to an external entity.
- **Information Control**: The assertion or enforcement of confidentiality restrictions, consent directives, or masking rules.
- **Information Challenge**: A formal dispute or query raised regarding the accuracy, completeness, or attribution of a health record.
- **Information Correction**: The governed rectification, amendment, or retraction of erroneous or misattributed health information.
- **Information Submission**: The formal submission of clinical documents or datasets for governance validation, indexing, or archival.
- **Information Qualification**: The formal annotation, verification stamp, or clinical validation applied to an information asset.
- **Information Publishing**: The distribution of governed, approved health information into public or shared access registries.

### 2.5 Authority, Statutory & Regulatory Interactions
Interactions governing statutory compliance, regulatory supervision, patient consent, and policy mandates.

- **Policy Direction**: An enterprise or jurisdictional directive establishing mandatory clinical or operational policy rules.
- **Regulatory Direction**: A legally binding order or compliance requirement issued by a statutory regulatory body.
- **FOI Request**: A formal statutory request for access to records under Freedom of Information or privacy legislation.
- **FOI Response**: The governed disclosure, redaction, or refusal response to a statutory access request.
- **Authorisation — Governance**: The formal granting of administrative, system, or operational access privileges based on organizational authority.
- **Authorisation — Consent**: The explicit granting or revocation of authority by a patient or substitute decision-maker to collect, use, or disclose health data.
- **Complaint**: A formal grievance lodged regarding clinical care, privacy breaches, or operational conduct.
- **Sanction**: A formal disciplinary, regulatory, or administrative restriction imposed upon a practitioner, organisation, or system.

### 2.6 Identity Management Interactions
Interactions governing the assertion, resolution, and verification of person, provider, and device identities.

- **Identity Assertion**: A claim presented by an actor regarding their identity or an identifier representing them.
- **Identity Query**: A search or lookup request to discover or disambiguate identity records matching supplied demographic traits.
- **Identity Establishment**: The governed recording and official recognition of a newly identified entity in an authoritative registry.
- **Identity Verification**: The formal validation of an asserted identity against trusted credentials, identity documents, or biometric proofs.
- **Identity Reconciliation**: The governed merging, unlinking, or cross-authority correlation of duplicate or fragmented identity records.

### 2.7 Relationship Management Interactions
Interactions governing relationships between patients, carers, practitioners, and healthcare organisations.

- **Relationship Assertion**: A claim that a specific relationship exists between two entities (e.g., claiming to be a patient's carer).
- **Relationship Query**: An inquiry to discover existing care relationships, nominated representatives, or practitioner affiliations.
- **Relationship Establishment**: The governed recording and formal activation of an acknowledged relationship in an authoritative registry.
- **Relationship Verification**: The validation of legal or clinical authority supporting an asserted relationship (e.g., verifying power of attorney).
- **Relationship Termination**: The formal dissolution, expiry, or revocation of an established relationship.

### 2.8 Event & Alert Notification Interactions
Interactions delivering real-time telemetry, clinical alarms, and operational notifications.

- **Clinical Event Notification**: An automated notice indicating that a clinically significant event has occurred (e.g., patient admission, vital sign threshold breached).
- **Clinical Alert**: An urgent, interruptive warning requiring prompt clinical attention (e.g., critical lab value, severe drug interaction).
- **Operational Event Notification**: A notice indicating a facility or logistics state change (e.g., bed vacated, cleaning completed).
- **Operational Alert**: An urgent operational warning indicating capacity exhaustion, system degradation, or logistics bottlenecks.

### 2.9 Review & Clinical Governance Interactions
Interactions governing clinical quality audits, mortality reviews, and peer evaluation.

- **Review Request**: A formal submission requesting clinical peer review, adverse event evaluation, or diagnostic second opinion.
- **Review Outcome**: The documented findings, consensus decisions, and quality recommendations resulting from a clinical review.

These retain their clinical-review purpose and applicable clinical authorities. A clinical-quality audit label alone does not establish independent-assurance exchange semantics. Independent evaluation of explicitly governed facts concerning clinical activity must preserve the assurance boundaries; its Interaction definition remains unresolved rather than being inferred from Review Request/Outcome. See the [Guardianship boundary](#4-guardianship-and-clinical-review-boundary).

### 2.10 Participation & Alignment Interactions
Interactions governing an entity's formal participation in care programmes, registries, or distribution lists.

- **Participation Registration**: The formal enrolment or addition of an actor into a multidisciplinary programme, registry, or care collective.
- **Participation Query**: A lookup to determine whether an actor is actively enrolled in a specific care programme or team.
- **Participation Withdrawal**: The formal removal or resignation of an actor from a participation context.

---

### 2.11 Supplementary Interaction Categories

#### Service Offering, Catalogue & Eligibility Interactions
- **ServiceOffering Query**: Discovering services offered by a facility or provider.
- **ServiceOffering Selection**: Choosing an appropriate service offering for a referral or order.
- **ServiceCatalogue Request**: Requesting published service catalogue specifications.
- **ServiceCatalogue Update**: Modifying service definitions, operating hours, or delivery modalities.
- **ServiceCatalogue Publish**: Releasing updated service directory definitions to consumers.
- **ServiceEligibility Assessment Request**: Evaluating whether a patient meets clinical or funding eligibility criteria.
- **ServiceEligibility Assessment Report**: The formal determination of patient eligibility for a service.

#### Incident Management Interactions
- **Incident Registration**: The formal recording of a clinical, operational, or safety event into a governed incident management framework.
- **Incident Report**: The formal communication and investigation findings regarding an incident.

---

## 3. Interaction Boundary Rules & Semantic Distinctions

Harmonia enforces strict semantic boundary rules to prevent misclassification of interactions:

| Distinction | Concept A | Concept B | Architectural Guardrail |
| :--- | :--- | :--- | :--- |
| **Information vs. Event Notification** | **Clinical Information Notification** | **Clinical Event Notification** | Information notification concerns *new/updated information assets becoming available*. Event notification concerns a *clinically significant occurrence happening in the physical/operational world*. |
| **Routine Supply vs. Formal Submission** | **Clinical Information Supply** | **Information Submission** | Supply is *routine operational contribution* of data. Submission is *deliberate submission of a complete asset for formal review, governance, or legal archiving*. |
| **Access vs. Disclosure** | **Information Access** | **Information Disclosure** | Access is *the consumer exercising pull authority*. Disclosure is *the custodian exercising push release or statutory duty*. |
| **Assertion vs. Establishment vs. Verification** | **Identity / Relationship Assertion** | **Identity / Relationship Establishment** | Assertion is a *claim*. Establishment is *official registry recording*. Verification is *proof against trusted validation criteria*. Neither assertion nor establishment implies verification. |
| **Participation vs. Relationship** | **Participation Registration** | **Relationship Establishment** | Participation concerns *enrolment in a programme or activity*. Relationship establishes *legal/clinical ties between specific entities*. |
| **Response vs. Outcome** | **Service Response** | **Service Outcome** | Response communicates *triage status or acceptance disposition*. Outcome reports *clinical findings and results after delivery*. |
| **Service vs. Operational Coordination** | **Service Coordination** | **Operational Coordination** | Service coordination focuses on *clinical care continuity*. Operational coordination focuses on *logistics, beds, and staff dispatch*. |
| **Work Progress vs. Work Outcome** | **Work Progress** | **Work Outcome** | Progress conveys *interim state during active execution*. Outcome conveys *final completion and task disposition*. |
| **Incident Registration vs. Report** | **Incident Registration** | **Incident Report** | Registration is the *initial call to action bringing an event under governance*. Report is the *investigative communication and findings*. |
| **Governance vs. Consent Authorisation** | **Authorisation — Governance** | **Authorisation — Consent** | Governance derives from *organisational or statutory mandate*. Consent derives from the *individual patient or legal representative*. |

## 4. Guardianship and Clinical Review Boundary

[Service Guardian](../actors-roles/roles.md#service-guardian) independently evaluates a governed subject against applicable assurance criteria; [Service Assurance Modeller](../actors-roles/roles.md#service-assurance-modeller) models how satisfaction will be assured. Neither Role confers clinical-review authority, replaces clinical peer review or clinical governance, or assumes Clinical Services Delivery Assurance. Clinical Information Review and Review Request/Outcome retain their clinical evaluation and professional-judgement semantics. Information Qualification, Policy Direction, operational alerts, Work Progress/Outcome and Incident Registration/Report also retain their defined purposes; none is automatically an assurance conclusion or an assurance-Role interaction.

The approved architecture permits evidence contribution and establishes reportable assurance findings/conclusions. The five approved Functions, modelling output and responsibility diagram do not establish participating Actors, exchange-specific Role participation, request/response commitments, initiation semantics, exchange semantics or canonical names needed to add assurance Interactions. Those decisions remain [unresolved](../behaviours/health-service-assurance.md#5-additional-business-elements-not-yet-established). Findings may cause another responsible party to initiate operational response without giving Service Guardian authority to assign, delegate, remediate or operationally escalate the subject. Neither assurance Role acquires that management responsibility. Explicitly required assurance concerning clinical activity does not transfer clinical responsibility. The ten existing categories and supplementary Interaction definitions are preserved.
