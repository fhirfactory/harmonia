<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Partial section succession; unwrapped scope/governance and unresolved material retain their recorded roles. See the [canonical continuum](../../../../enterprise-architecture/continuum/documentation/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

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

<a id="2-the-canonical-r1-business-interaction-catalogue"></a>

## 2. The Canonical R1.x/R2.x Business Interaction Catalogue

Harmonia defines an authoritative catalogue of Business Interactions structured into ten primary functional categories. The [three approved Service Assurance Interactions](#5-approved-service-assurance-interactions) supplement the existing catalogue without reclassifying its established categories or deriving an additional category through symmetry:

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
├── Supplementary Categories:
│   ├── Service Offering & Catalogue Interactions
│   └── Incident Management Interactions
└── Approved Service Assurance Interactions:
    ├── Service Assurance Request
    ├── Service Assurance Status
    └── Service Assurance Outcome Communication
```

---

### 2.1 Clinical Information Interactions
Interactions concerning the routine exchange, query, review, and collaborative discussion of patient clinical records and diagnostic documentation.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Supply](../../../../enterprise-architecture/continuum/business/business-interaction/a6e2525ea3294ee3ab9a4ec3dd4dfe9f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Information Supply**: Routine, authorised transmission or contribution of clinical information, discharge summaries, or diagnostic results into the integration environment.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Provision](../../../../enterprise-architecture/continuum/business/business-interaction/4d34b3705cb0405a910ddcacfdd6b6d3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Information Provision**: The delivery of requested clinical record content to an authorised clinician or system.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Query](../../../../enterprise-architecture/continuum/business/business-interaction/8aabfdf9c83c4de486cf5f50c41c0d5e.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Information Query**: A targeted request to discover or search for clinical records, observations, or history for a specific healthcare subject.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Review](../../../../enterprise-architecture/continuum/business/business-interaction/06dbfdb6b8a84bd29f67ca955ba23006.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Information Review**: The structured examination, validation, or clinical evaluation of clinical record content by a practitioner.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Collaboration](../../../../enterprise-architecture/continuum/business/business-interaction/f54492b30639431e80cd0723217d6be8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Collaboration**: Structured, multi-party dialogue, handover messaging, or clinical commentary among care team members.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Information Notification](../../../../enterprise-architecture/continuum/business/business-interaction/31e03beac32946a09985ad52e0f9cce4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Information Notification**: An automated or operational notice informing participants that new or updated clinical information is now available.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.2 Service Delivery Interactions
Interactions governing the request, referral, coordination, progression, and outcome reporting of clinical services.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Referral](../../../../enterprise-architecture/continuum/business/business-interaction/b7ea2864b6c044c1a9b7763ee6428d7c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Referral**: A formal request for a specialist, service provider, or clinical department to assess, manage, or assume care responsibility for a patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Request](../../../../enterprise-architecture/continuum/business/business-interaction/be3915edc56d4020969b7e1f4837f112.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Request**: A formal order placed for a specific diagnostic test, therapeutic procedure, or clinical service.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Response](../../../../enterprise-architecture/continuum/business/business-interaction/6bc0e6df5ba3405a9037e34f9810aefc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Response**: A communication indicating the status, triage decision, scheduled timing, or acceptance disposition of a referral or request.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Outcome](../../../../enterprise-architecture/continuum/business/business-interaction/e52b7159e28441769fcf3214cc6be6a6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Outcome**: The formal clinical finding, procedural report, or discharge disposition resulting from completed service delivery.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Transfer of Care](../../../../enterprise-architecture/continuum/business/business-interaction/8bb02a34807f4bb183bb42a722f4509b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Transfer of Care**: The formal transition of clinical and medico-legal accountability for a patient between care teams, wards, or organisations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Coordination](../../../../enterprise-architecture/continuum/business/business-interaction/c00bba65740947fea75542c13563553c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Service Coordination**: Dialogue aimed at synchronising multiple care delivery activities across clinical specialties or care settings.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Coordination Outcome](../../../../enterprise-architecture/continuum/business/business-interaction/abb4947b64944790b20fd52524f0babc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Coordination Outcome**: A recorded summary of coordinated care decisions, agreed milestones, or integrated care plans.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.3 Operational Logistics & Work Interactions
Interactions governing facility operations, bed assignments, portering, specimen logistics, and staff task dispatch.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Coordination](../../../../enterprise-architecture/continuum/business/business-interaction/cdb5d645d3984eaa8ad5e56a81023c59.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Operational Coordination**: Tactical alignment regarding resource allocation, bed distribution, queue balancing, and facility throughput.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resource Reporting](../../../../enterprise-architecture/continuum/business/business-interaction/05bf8e557ce14f0eb1c869d07c5a93ca.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Resource Reporting**: Telemetry or status reporting on the availability, capacity, or status of physical beds, theatres, or clinical equipment.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Resource Allocation](../../../../enterprise-architecture/continuum/business/business-interaction/8479c1d4b2304d9dabc3716c6cb20e83.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Resource Allocation**: The formal assignment or reservation of a physical care-place, bed, theatre slot, or device to a patient.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Assignment](../../../../enterprise-architecture/continuum/business/business-interaction/c5611769a52d44aebeef55bd48e1f433.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Work Assignment**: The formal dispatch or allocation of an operational task (e.g., patient transfer, cleaning, specimen delivery) to a staff member or role.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Reassignment](../../../../enterprise-architecture/continuum/business/business-interaction/d4fb961b6148436686632668c98a6139.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Reassignment**: The redirection of an assigned operational task to an alternative worker or unit due to operational constraints.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Acceptance](../../../../enterprise-architecture/continuum/business/business-interaction/64d7dfac2f5f4b61998df127b44c0521.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Work Acceptance**: Acknowledgement and commitment by an operational worker to execute an assigned task.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Progress](../../../../enterprise-architecture/continuum/business/business-interaction/a687efed1f7e456bba53e356b157f0d8.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Work Progress**: Milestone updates regarding the active execution status of an in-flight operational task.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Work Outcome](../../../../enterprise-architecture/continuum/business/business-interaction/a950de4051d04cfa8bb180f8193f7495.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Work Outcome**: The formal completion report and outcome disposition of an operational task.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Coordination Outcome](../../../../enterprise-architecture/continuum/business/business-interaction/d40c5ee0dbaa450dab073103f25ded6d.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Operational Coordination Outcome**: Recorded agreements from operational huddles or bed-meeting decisions.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.4 Information Governance & Control Interactions
Interactions governing the access control, privacy enforcement, correction, and publication of health information assets.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Access](../../../../enterprise-architecture/continuum/business/business-interaction/b2624626d06044fb9767a96045cd0302.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Access**: An authorised consumer exercising legitimate authority to retrieve or inspect governed health information.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Disclosure](../../../../enterprise-architecture/continuum/business/business-interaction/47536a5d81aa4d8eb2d13054b864d1bf.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Disclosure**: An information custodian or system exercising authority or legal obligation to release records to an external entity.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Control](../../../../enterprise-architecture/continuum/business/business-interaction/773133f23f7540eb9b90d39ca872aebd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Control**: The assertion or enforcement of confidentiality restrictions, consent directives, or masking rules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Challenge](../../../../enterprise-architecture/continuum/business/business-interaction/44d71c390a8f435cbb596601e627bdfa.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Challenge**: A formal dispute or query raised regarding the accuracy, completeness, or attribution of a health record.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Correction](../../../../enterprise-architecture/continuum/business/business-interaction/3f1db661c97b4fa692e3d70278a40226.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Correction**: The governed rectification, amendment, or retraction of erroneous or misattributed health information.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Submission](../../../../enterprise-architecture/continuum/business/business-interaction/d3186ad3c8a74e92a80cb4c1f8f32fb6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Submission**: The formal submission of clinical documents or datasets for governance validation, indexing, or archival.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Qualification](../../../../enterprise-architecture/continuum/business/business-interaction/6b606df4675f48b3baba03f4b2ca7602.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Qualification**: The formal annotation, verification stamp, or clinical validation applied to an information asset.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Information Publishing](../../../../enterprise-architecture/continuum/business/business-interaction/6fb6f66531874c2d983071a04b5934c2.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Information Publishing**: The distribution of governed, approved health information into public or shared access registries.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.5 Authority, Statutory & Regulatory Interactions
Interactions governing statutory compliance, regulatory supervision, patient consent, and policy mandates.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Policy Direction](../../../../enterprise-architecture/continuum/business/business-interaction/b2c573b855ad4c7abc4f02b2ae484a65.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Policy Direction**: An enterprise or jurisdictional directive establishing mandatory clinical or operational policy rules.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Regulatory Direction](../../../../enterprise-architecture/continuum/business/business-interaction/baf4be48ac11472aa130c71e6474d9a4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Regulatory Direction**: A legally binding order or compliance requirement issued by a statutory regulatory body.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [FOI Request](../../../../enterprise-architecture/continuum/business/business-interaction/2c7ca48559fb4a72b4d9c250df4015ce.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **FOI Request**: A formal statutory request for access to records under Freedom of Information or privacy legislation.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [FOI Response](../../../../enterprise-architecture/continuum/business/business-interaction/909faede10b5452cbee2d7d052635f47.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **FOI Response**: The governed disclosure, redaction, or refusal response to a statutory access request.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Authorisation — Governance](../../../../enterprise-architecture/continuum/business/business-interaction/a1a6a8eeb4d54fa9ac184198a506d318.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Authorisation — Governance**: The formal granting of administrative, system, or operational access privileges based on organizational authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Authorisation — Consent](../../../../enterprise-architecture/continuum/business/business-interaction/8c8a371f6a324d908656c3c935c7656f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Authorisation — Consent**: The explicit granting or revocation of authority by a patient or substitute decision-maker to collect, use, or disclose health data.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Complaint](../../../../enterprise-architecture/continuum/business/business-interaction/29f2a74c8234411c9ac15458e66ca4bc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Complaint**: A formal grievance lodged regarding clinical care, privacy breaches, or operational conduct.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Sanction](../../../../enterprise-architecture/continuum/business/business-interaction/f4dc8ed1c034473784a5c8ec4227c7d6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Sanction**: A formal disciplinary, regulatory, or administrative restriction imposed upon a practitioner, organisation, or system.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.6 Identity Management Interactions
Interactions governing the assertion, resolution, and verification of person, provider, and device identities.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Identity Assertion](../../../../enterprise-architecture/continuum/business/business-interaction/47b9abea0964451eb0fa419b464b4505.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Identity Assertion**: A claim presented by an actor regarding their identity or an identifier representing them.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Identity Query](../../../../enterprise-architecture/continuum/business/business-interaction/d119aa62e95f4d7aa01822401ca4203c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Identity Query**: A search or lookup request to discover or disambiguate identity records matching supplied demographic traits.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Identity Establishment](../../../../enterprise-architecture/continuum/business/business-interaction/3d3459edfa884ef0abdcfd3772b538e4.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Identity Establishment**: The governed recording and official recognition of a newly identified entity in an authoritative registry.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Identity Verification](../../../../enterprise-architecture/continuum/business/business-interaction/1a56f778873a4094b5a0f84cae842d35.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Identity Verification**: The formal validation of an asserted identity against trusted credentials, identity documents, or biometric proofs.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Identity Reconciliation](../../../../enterprise-architecture/continuum/business/business-interaction/332cdc30ae0346c6b86233db71caf683.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Identity Reconciliation**: The governed merging, unlinking, or cross-authority correlation of duplicate or fragmented identity records.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.7 Relationship Management Interactions
Interactions governing relationships between patients, carers, practitioners, and healthcare organisations.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Relationship Assertion](../../../../enterprise-architecture/continuum/business/business-interaction/9a53f10eac7748a2b4a9d88da75f8814.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Relationship Assertion**: A claim that a specific relationship exists between two entities (e.g., claiming to be a patient's carer).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Relationship Query](../../../../enterprise-architecture/continuum/business/business-interaction/b30441d9b44a4bc29771e8f6e94f6b5a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Relationship Query**: An inquiry to discover existing care relationships, nominated representatives, or practitioner affiliations.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Relationship Establishment](../../../../enterprise-architecture/continuum/business/business-interaction/f451d56104944498909549ab7f864003.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Relationship Establishment**: The governed recording and formal activation of an acknowledged relationship in an authoritative registry.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Relationship Verification](../../../../enterprise-architecture/continuum/business/business-interaction/c71453e1b0ff46a7b91e27a1745d842b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Relationship Verification**: The validation of legal or clinical authority supporting an asserted relationship (e.g., verifying power of attorney).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Relationship Termination](../../../../enterprise-architecture/continuum/business/business-interaction/e536ab3a1bbe4a36a99635458ba208f6.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Relationship Termination**: The formal dissolution, expiry, or revocation of an established relationship.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.8 Event & Alert Notification Interactions
Interactions delivering real-time telemetry, clinical alarms, and operational notifications.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Event Notification](../../../../enterprise-architecture/continuum/business/business-interaction/d2153d1057d740fea1665d71ff66dd1c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Event Notification**: An automated notice indicating that a clinically significant event has occurred (e.g., patient admission, vital sign threshold breached).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Clinical Alert](../../../../enterprise-architecture/continuum/business/business-interaction/8666d4168fe545fd9ce222217887bd61.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Clinical Alert**: An urgent, interruptive warning requiring prompt clinical attention (e.g., critical lab value, severe drug interaction).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Event Notification](../../../../enterprise-architecture/continuum/business/business-interaction/7a2b6e8802e24e018af89f265271f5ca.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Operational Event Notification**: A notice indicating a facility or logistics state change (e.g., bed vacated, cleaning completed).


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Operational Alert](../../../../enterprise-architecture/continuum/business/business-interaction/3697066c0d634076be5a8d745e3fdd91.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Operational Alert**: An urgent operational warning indicating capacity exhaustion, system degradation, or logistics bottlenecks.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


### 2.9 Review & Clinical Governance Interactions
Interactions governing clinical quality audits, mortality reviews, and peer evaluation.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Review Request](../../../../enterprise-architecture/continuum/business/business-interaction/480556d544ad4dc18f6945f213044e9b.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Review Request**: A formal submission requesting clinical peer review, adverse event evaluation, or diagnostic second opinion.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Review Outcome](../../../../enterprise-architecture/continuum/business/business-interaction/45bf28eb44e64554b568db48a30ac43c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Review Outcome**: The documented findings, consensus decisions, and quality recommendations resulting from a clinical review.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


These retain their clinical-review purpose and applicable clinical authorities. A clinical-quality audit label alone does not establish independent-assurance exchange semantics. The separately [approved assurance Interactions](#5-approved-service-assurance-interactions) preserve the assurance boundaries and are not inferred from Review Request/Outcome. See the [Guardianship boundary](#4-guardianship-and-clinical-review-boundary).

### 2.10 Participation & Alignment Interactions
Interactions governing an entity's formal participation in care programmes, registries, or distribution lists.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Participation Registration](../../../../enterprise-architecture/continuum/business/business-interaction/906c61eb392f4b538f6d0a79917d21cc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Participation Registration**: The formal enrolment or addition of an actor into a multidisciplinary programme, registry, or care collective.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Participation Query](../../../../enterprise-architecture/continuum/business/business-interaction/1ce0ba25408c47f9bd82953fba605c40.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Participation Query**: A lookup to determine whether an actor is actively enrolled in a specific care programme or team.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Participation Withdrawal](../../../../enterprise-architecture/continuum/business/business-interaction/ce1901f036364b7291451bfbd4f25c00.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Participation Withdrawal**: The formal removal or resignation of an actor from a participation context.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


---

### 2.11 Supplementary Interaction Categories

#### Service Offering, Catalogue & Eligibility Interactions
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceOffering Query](../../../../enterprise-architecture/continuum/business/business-interaction/40105b22c930413692cc64826d3ad32f.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceOffering Query**: Discovering services offered by a facility or provider.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceOffering Selection](../../../../enterprise-architecture/continuum/business/business-interaction/dbb4668e39994da6bd7ec0251210d4bc.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceOffering Selection**: Choosing an appropriate service offering for a referral or order.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceCatalogue Request](../../../../enterprise-architecture/continuum/business/business-interaction/d6c56d2bb1274fc68cb0ed6c3b8a84b9.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceCatalogue Request**: Requesting published service catalogue specifications.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceCatalogue Update](../../../../enterprise-architecture/continuum/business/business-interaction/c15051a06c1a46b1acb75d18a5e728dd.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceCatalogue Update**: Modifying service definitions, operating hours, or delivery modalities.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceCatalogue Publish](../../../../enterprise-architecture/continuum/business/business-interaction/397a4200bb894479a54a03dc84451efb.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceCatalogue Publish**: Releasing updated service directory definitions to consumers.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceEligibility Assessment Request](../../../../enterprise-architecture/continuum/business/business-interaction/1508d1e33b264534bbcde367eb3dc6a3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceEligibility Assessment Request**: Evaluating whether a patient meets clinical or funding eligibility criteria.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [ServiceEligibility Assessment Report](../../../../enterprise-architecture/continuum/business/business-interaction/db44999c87d545618969319d3f63725a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **ServiceEligibility Assessment Report**: The formal determination of patient eligibility for a service.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


#### Incident Management Interactions
<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Incident Registration](../../../../enterprise-architecture/continuum/business/business-interaction/126276873bde4105a08e21bb3187f5b3.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Incident Registration**: The formal recording of a clinical, operational, or safety event into a governed incident management framework.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Incident Report](../../../../enterprise-architecture/continuum/business/business-interaction/7cb1eb055be44eefb7092356510acc77.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

- **Incident Report**: The formal communication and investigation findings regarding an incident.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


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

The approved human review now establishes exactly the three Interactions below with their associated Services and Role participants. Detailed Actor eligibility, initiation mechanics, information semantics and further exchange commitments remain [unresolved](../behaviours/health-service-assurance.md#6-additional-business-elements-not-yet-established). Findings may cause another responsible party to initiate operational response without giving Service Guardian authority to assign, delegate, remediate or operationally escalate the subject. Neither assurance Role acquires that management responsibility. Care Coordinator may request/receive applicable assurance, but clinical interpretation, professional judgement, clinical adequacy and subsequent clinical management/action remain with the applicable clinical authority. Service Coordinator's request/receipt and System Steward's status observation do not transfer operational management, task progression or improvement responsibility. The ten existing categories and supplementary Interaction definitions are preserved.

## 5. Approved Service Assurance Interactions

Exactly three new assurance Interactions are established. **Element Type: Business Interaction** applies to each. **Canonical IDs remain unresolved**; existing Domain03 Interaction conventions are retained without inventing identifier tokens, Actor instances or a new namespace. The Service Guardian Role participates in Governed Assurance; the other participants act within their existing mandates. Services remain owned by Governed Assurance. Role participation is not Collaboration membership or an extension of clinical, operational or governance authority.

<a id="service-assurance-request"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Assurance Request](../../../../enterprise-architecture/continuum/business/business-interaction/dd80af764f88480ea8b8b5898bb15ce7.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

### 5.1 Service Assurance Request

- **Canonical Name**: `Service Assurance Request`.
- **Participants**: `Service Guardian` and **one authorised requesting Role** from `Care Coordinator`, `Service Coordinator`, `Regulator`, `Policy Authority`, `System Steward`.
- **Purpose**: Establishes a request by an authorised Business Role for Service Assurance to be performed against an identifiable governed subject under an applicable assurance basis.
- **Associated Business Service**: [Request Service Assurance](../behaviours/health-service-assurance.md#request-service-assurance).
- **Boundary**: The requesting Role identifies the need within its responsibility/authority; it does not control assurance evidence assessment, adjudication or the resulting finding/conclusion, nor acquire authority to define/alter the Assurance Definition or Assurance Criteria. REQ-FND-005 independence remains controlling. Participant variation does not create five separate Interactions. Detailed request information and initiation mechanics remain unresolved.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="service-assurance-status"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Assurance Status](../../../../enterprise-architecture/continuum/business/business-interaction/6bad04918445465cb350c3252af73377.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

### 5.2 Service Assurance Status

- **Canonical Name**: `Service Assurance Status`.
- **Participants**: `System Steward`, `Service Guardian`.
- **Purpose**: Enables the System Steward to obtain the current business progression state of an identifiable Service Assurance activity for stewardship purposes.
- **Associated Business Service**: [Request Service Assurance Status](../behaviours/health-service-assurance.md#request-service-assurance-status).
- **Boundary**: **Service Assurance Status concerns progression of the assurance activity and SHALL NOT constitute, imply or expose an unadjudicated Assurance Finding or Conclusion.** It supplies no provisional/predicted finding or incomplete assessment presented as a conclusion. Only System Steward consumes the associated Service; requesting assurance does not grant another Role access to status. Status taxonomy remains unresolved.

This Interaction is stewardship / management observation of assurance activity progression, not recursive assurance. Management Monitoring observes activity state to progress/manage activity; assurance evaluates a governed subject against governing criteria to establish a conclusion. Obtaining status is not itself assurance. The [non-recursion boundary](../behaviours/health-service-assurance.md#assurance-non-recursion-boundary) applies.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->


<a id="service-assurance-outcome-communication"></a>

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [Service Assurance Outcome Communication](../../../../enterprise-architecture/continuum/business/business-interaction/73b6d1ab8ecc48c4bdea98f013288c9c.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

### 5.3 Service Assurance Outcome Communication

- **Canonical Name**: `Service Assurance Outcome Communication`.
- **Participants**: `Service Guardian` and **one or more authorised recipient Roles** from `Care Coordinator`, `Service Coordinator`, `Regulator`, `Policy Authority`, `System Steward`.
- **Purpose**: Communicates an established Assurance Finding or Conclusion to authorised Business Roles for use within their respective responsibilities.
- **Associated Business Service**: [Communicate Service Assurance Outcome](../behaviours/health-service-assurance.md#communicate-service-assurance-outcome).
- **Boundary**: The outcome is already established; communication does not assess/adjudicate, reinterpret or alter it. This Interaction is not necessarily a broadcast: one, some or all legitimate recipients may receive the applicable outcome according to authority, responsibility and information requirements. The requesting Role, status consumer and outcome recipient need not coincide. Communication need not require a documentary report.

Service Guardian does not become responsible for the recipient's interpretation within its own authority, management action, remediation, escalation, policy change, process improvement, clinical action or other subsequent response. Financial relevance does not establish Harmonia financial-governance responsibility; the [deliberate Financial Governance exclusion](../behaviours/health-service-assurance.md#financial-governance-exclusion) applies, with exact external recipients still unresolved.



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

### 5.4 Further Exchange and Collaboration Boundaries

Establish Assurance Context associates source-owned information as evidence; that does not establish that evidence arrives through a new explicit Role-to-Role exchange. Evidence may be available through existing governed information access. Existing Information Access and other authoritative Interactions retain their own meanings and are not reinterpreted as assurance evidence exchanges. Evidence acquisition/exchange Interaction requirements remain unresolved; no Assurance Evidence Contribution, Submit/Provide Assurance Evidence or Evidence Exchange Interaction is introduced.

Use of an Assurance Definition establishes a responsibility/information dependency, not a Modeller-to-Guardian Interaction. No explicit exchange between those Roles is approved here. The three approved Interactions do not establish an enduring structured collective, new Service Assurance Collaboration or membership of an existing Collaboration. [Collaboration decisions remain unresolved](collaborations.md#44-assurance-role-collaboration-derivation-boundary).
