# Harmonia Architecture Design Notes

> **Standing:** Architectural scratchpad / working memory only.
>
> This document captures design discussion, emerging decisions, semantic
> distinctions, rationale, interpretive warnings, and unresolved
> questions that may otherwise be lost between working sessions. It is
> **not architectural authority**.
>
> Canonical architecture remains in the applicable authoritative
> Markdown documents under `/docs/markdown`. Material recorded here
> becomes authoritative only when it is explicitly adjudicated and
> incorporated into the appropriate canonical architecture source.
>
> Scratchpad material MUST NOT be used to silently resolve uncertainty,
> establish identifiers, allocate ancestry, create traceability
> relationships, or override canonical architecture.

## Purpose

This scratchpad exists to preserve the architectural reasoning that sits
between conversational discussion and formal architectural
documentation.

In particular, capture material where retaining only the final decision
would lose an important semantic boundary or make a later
misinterpretation likely.

Useful scratchpad material includes:

-   candidate architectural decisions;
-   intended meanings of ambiguous terms;
-   semantic distinctions;
-   responsibility boundaries;
-   rationale for placement or decomposition;
-   interpretive warnings;
-   deliberately rejected interpretations;
-   unresolved questions;
-   assumptions that still require adjudication; and
-   observations that should influence later canonical documentation.

A successful build, implementation pattern, catalogue shape, or
convenient structural mapping does not promote scratchpad material into
architectural authority.

## Working Practice

During Harmonia design discussions, record a note when a decision,
distinction, boundary, rationale, warning, or unresolved issue could
reasonably be lost through conversation compaction or a later session
restart.

Where useful, notes should distinguish:

-   **Context** --- what part of the architecture is being discussed.
-   **Working Decision** --- what has presently been agreed.
-   **Intended Meaning** --- what the term or construct is intended to
    mean.
-   **Boundary** --- what it explicitly does not mean or own.
-   **Reasoning** --- why the decision was made.
-   **Interpretive Warning** --- likely future misreadings that should
    be prevented.
-   **Open Issue** --- anything deliberately left unresolved.
-   **Promotion Target** --- likely canonical document(s), if known.

Scratchpad notes do not need every heading where the meaning is obvious.

------------------------------------------------------------------------

### Decision and Open-Issue Discipline

The scratchpad SHALL distinguish established working decisions from deliberately unresolved architectural issues.

- `DN-<area>-<number>` identifies a **Design Note** recording a working decision that has been explicitly agreed during architectural discussion. A Design Note may record its intended meaning, rationale, boundaries, semantic distinctions, interpretive warnings, and promotion considerations.
- `OI-<area>-<number>` identifies an **Open Issue** recording a question or architectural matter that remains deliberately unresolved and requires later adjudication.

A Design Note SHALL record only what has actually been established. It MUST NOT silently resolve, absorb, or imply resolution of an Open Issue.

An Open Issue may reference the Design Note or architectural context that exposed it, but its unresolved standing MUST remain explicit until separately adjudicated.

When an Open Issue is subsequently resolved:

1. record the resulting decision in the appropriate Design Note;
2. mark the Open Issue as resolved and reference the resulting Design Note; and
3. subsequently promote the established architecture into the appropriate canonical source when authorised.

> **Decisions record what we have established. Open Issues record what we have deliberately not established.**

This discipline preserves architectural uncertainty until it is explicitly resolved and prevents provisional discussion from acquiring accidental architectural authority.

------------------------------------------------------------------------

## CT3 Capability Boundaries

The CT3 decomposition of **Diagnostic Support Provision** separates materially different business responsibilities associated with providing a diagnostic service.

Each CT3 is bounded by the business outcome it is responsible for establishing. Information or activity may pass between these capabilities without transferring responsibility for those outcomes.

### Diagnostic Service Appointment Management

**CT3 Boundary:**

Owns the ability to establish and manage the **scheduled opportunity for diagnostic service delivery**, including diagnostic-service-specific scheduling constraints.

It begins where a requirement for diagnostic service scheduling exists and ends when the applicable diagnostic appointment state has been established or managed.

It does **not** own:

- the clinical decision or order requesting the diagnostic service;
- performance of the diagnostic service;
- generic enterprise appointment-management capability;
- diagnostic evidence produced during the service; or
- interpretation or reporting of diagnostic results.

**Boundary test:**

> If the concern is **when and under what diagnostic-service-specific scheduling conditions the service is to occur**, it belongs here. If the concern is **performing the service**, it has crossed into Diagnostic Service Delivery.

---

### Diagnostic Service Delivery

**CT3 Boundary:**

Owns the ability to **perform the diagnostic service** through which diagnostic evidence is obtained or produced.

It encompasses the conduct of the diagnostic activity itself and ends where the resulting specimen, data, observation or other diagnostic evidence requires management or interpretation as an established diagnostic artefact.

It does **not** own:

- scheduling the service;
- longitudinal management of specimens;
- management of acquired diagnostic data as information;
- professional interpretation of the resulting evidence;
- establishment of the authoritative diagnostic report; or
- distribution of that report.

**Boundary test:**

> If the concern is **performing the diagnostic activity that obtains or produces evidence**, it belongs here. Once the concern becomes **managing or interpreting the resulting evidence**, it has crossed into another CT3.

---

### Diagnostic Specimen Management

**CT3 Boundary:**

Owns the ability to maintain the **identity, integrity, suitability, custody and applicable lifecycle context of a diagnostic specimen**.

The Capability is centred on the specimen as a managed diagnostic entity.

It does **not** own:

- the broader diagnostic service within which the specimen may be collected;
- interpretation of evidence derived from the specimen;
- general inventory or logistics management;
- diagnostic data produced by analysing the specimen; or
- diagnostic reporting.

**Boundary test:**

> If the concern is **the specimen itself and whether its identity, integrity, suitability or custody is correctly maintained**, it belongs here. Once the concern becomes **data or meaning derived from the specimen**, it has crossed the boundary.

---

### Diagnostic Data Management

**CT3 Boundary:**

Owns the ability to manage **diagnostic data as diagnostic information**, including its identity, association, integrity, organisation, availability and applicable lifecycle.

Its subject is the acquired or produced diagnostic data rather than the physical specimen, the clinical interpretation of that data, or the final diagnostic report.

It does **not** own:

- physical specimen management;
- performance of the diagnostic service;
- professional interpretation of diagnostic evidence;
- patient-level Clinical Determination;
- authoritative diagnostic report management; or
- report distribution.

**Boundary test:**

> If the concern is **preserving and managing diagnostic evidence as data**, it belongs here. If the concern is **determining what that evidence means**, it has crossed into Diagnostic Interpretation.

---

### Diagnostic Interpretation

**CT3 Boundary:**

Owns the ability to establish **diagnostic meaning from diagnostic evidence** within the professional scope of the diagnostic service.

Its outcome is an interpreted diagnostic finding, conclusion or meaning derived from the applicable diagnostic evidence.

It does **not** own:

- acquisition or management of the underlying diagnostic data;
- patient-level synthesis of diagnostic evidence with the broader clinical context;
- establishment of diagnoses, problems, prognosis or risk belonging to Clinical Determination;
- lifecycle management of the authoritative diagnostic report; or
- distribution of that report.

**Boundary test:**

> If the concern is **what the diagnostic evidence demonstrates**, it belongs here. If the concern is **what the total clinical evidence means for the Individual**, it has crossed into Clinical Determination.

---

### Diagnostic Report Management

**CT3 Boundary:**

Owns the ability to establish and manage the **diagnostic report as the governed and authoritative expression of diagnostic-service findings and conclusions**.

Its scope includes the report lifecycle necessary to establish and maintain its applicable status and authority, including creation, review, amendment, verification, finalisation and signing/authorisation where applicable.

It does **not** own:

- the underlying diagnostic interpretation itself;
- management of raw/acquired diagnostic data;
- patient-level Clinical Determination;
- communication/distribution of the established report; or
- downstream use of the report by recipients.

**Boundary test:**

> If the concern is **what constitutes the authoritative diagnostic report and its governed state**, it belongs here. Once the concern becomes **getting that established report to an authorised consumer**, it has crossed into Diagnostic Report Distribution.

---

### Diagnostic Report Distribution

**CT3 Boundary:**

Owns the ability to **distribute or provide an established diagnostic report to applicable authorised recipients, destinations or consuming contexts**.

Its subject is the movement or provision of the established report beyond its authoritative reporting context.

It does **not** own:

- creation or alteration of the diagnostic interpretation;
- establishment, signing or finalisation of the authoritative report;
- determination of the clinical meaning of the report for the Individual;
- generic health-information access or communication controls; or
- authority to bypass privacy, security or information-governance constraints.

Diagnostic Report Distribution operates subject to the applicable Health Information Access, Health Information Communication, Health Information Control and security/privacy capabilities.

**Boundary test:**

> If the concern is **providing the established report to an authorised destination or consumer**, it belongs here. If the concern is **establishing or changing the authoritative report**, it belongs in Diagnostic Report Management.

---

### Cross-Capability Boundary Summary

The principal semantic boundaries are therefore:

```text
Appointment Management
    = when / under what scheduling constraints

Service Delivery
    = perform the diagnostic service

Specimen Management
    = manage the physical diagnostic specimen

Diagnostic Data Management
    = manage diagnostic evidence as information

Diagnostic Interpretation
    = determine what the diagnostic evidence demonstrates

Diagnostic Report Management
    = establish and govern the authoritative report

Diagnostic Report Distribution
    = provide the established report to authorised consumers
```

These boundaries are semantic responsibility boundaries, not mandatory workflow stages.

A diagnostic service may exercise only a subset of these CT3 Capabilities, and activities across them may occur concurrently, iteratively or in discipline-specific patterns.

------------------------------------------------------------------------

# Business Capability MVP Reconciliation

## DN-BC-001 --- MVP Business Capability Modelling Scope

**Context:** Business Capability CT1 → CT2 → CT3 reconciliation.

**Working Decision:**

The Business Capability model is being developed as the minimum coherent
business architecture necessary to establish Harmonia's architectural
context and justify its Business Enabling Capabilities.

It is not intended to become an exhaustive Enterprise Health Capability
Model.

Capability decomposition should therefore be established where a
distinction is necessary to explain, derive, constrain, or govern
Harmonia responsibilities, or where sufficient adjacent business context
is required to make those responsibilities intelligible.

**Working test:**

> Would distinguishing these business abilities cause Harmonia to
> behave, govern, represent, exchange, secure, coordinate, or trace
> something materially differently?

If yes, further Capability distinction may be justified.

If no, additional decomposition is probably unnecessary explanatory
enterprise detail.

**Boundary:**

The established Business Enabling Capability landscape may be used as
evidence that business context is required. It MUST NOT be used to
mechanically reverse-engineer Business Capability identities or create
one-to-one Business Capability ↔ Business Enabling Capability mappings.

------------------------------------------------------------------------

## DN-BC-002 --- Fixed Capability Decomposition and Incomplete Branches

**Context:** Capability metamodel.

**Working Decision:**

Harmonia uses the fixed decomposition:

`CT1 → CT2 → CT3 → Feature (FT)`

A Feature must be framed by a meaningful CT3 Capability. CT3 must have a
CT2 parent and CT2 must have a CT1 parent.

A Feature cannot compensate for incomplete Capability decomposition.

If Harmonia requires a Feature and a meaningful CT3 cannot yet be
identified, this indicates incomplete upstream Capability modelling. It
is not permission to skip CT3 or manufacture a meaningless CT3 for
structural symmetry.

**Interpretive Warning:**

A CT1 or CT2 branch may remain intentionally undecomposed in the MVP
where no Feature currently requires a lower frame. This does not relax
the fixed hierarchy for future Features.

------------------------------------------------------------------------

## DN-BC-003 --- Individual Health Assessment CT3 Decomposition

**Context:**

`Individual Care Delivery → Individual Health Assessment`

**Working Decision:**

Individual Health Assessment decomposes according to the distinct means
by which knowledge about the individual's health state is established,
not according to care setting, profession, clinical specialty, workflow,
or information type.

Current agreed CT3s:

-   Health History Assessment
-   Clinical Examination
-   Health Status Measurement

**Boundary:**

Functional, nutritional, cognitive, developmental, psychosocial,
mental-health, frailty and similar assessment labels are generally
assessment domains rather than distinct means of establishing knowledge.

Health Risk Assessment crosses into Clinical Determination where
evidence is interpreted into a clinically meaningful conclusion.

------------------------------------------------------------------------

## DN-BC-004 --- Care Access & Coordination MVP Decomposition

**Context:** Review of the candidate Business Capability decomposition
against established Harmonia Business Enabling responsibilities.

**Working Decision:**

The expanded Care Access & Coordination structure proposed during review
is accepted as a useful MVP business frame, subject to the definitions
and boundaries recorded below.

Current working structure:

``` text
Care Access & Coordination

  Care Access Management
    Care Intake Management
    Care Referral Management
    Care Eligibility Determination

  Care Scheduling & Capacity Management
    Care Appointment Management
    Care-Place Allocation
    Care-Place Readiness Management
    Care Resource Deployment
    Operational Care Flow Coordination
    Operational Care Capacity Management

  Care Coordination & Transition
    Care-Team Coordination
    Care Responsibility Transition
    Care Discharge Coordination

  Care Support Logistics Management
    Patient Movement Coordination
    Clinical Material Logistics Coordination
    Care Support Work Coordination
```

**Reasoning:**

The original scheduling/allocation model was too thin to explain
established Harmonia-relevant business context around operational flow,
care-place readiness, service capacity, roster/resource deployment, work
coordination, patient transport, and clinical-material logistics.

The additional structure is intended to provide sufficient business
context without turning Harmonia into a PAS, workforce-management
system, bed-management product, or hospital logistics platform.

------------------------------------------------------------------------

## DN-BC-005 --- Care Resource Deployment Means Roster-Supported Operational Deployment

**Context:**

`Care Access & Coordination → Care Scheduling & Capacity Management → Care Resource Deployment`

**Working Decision:**

Retain **Care Resource Deployment** as a CT3 beneath **Care Scheduling &
Capacity Management**.

**Intended Meaning:**

Care Resource Deployment concerns the operational deployment of
available and appropriately authorised resources to meet care demand.

A particularly important use is **roster support**: the intersection of
roster/on-call context, availability, role/qualification, resource
suitability, and the operational care requirement.

It answers, in business terms:

> Of the resources legitimately available to provide coverage, which
> resources are operationally deployed to meet this care requirement?

**Boundary:**

Care Resource Deployment does **not** establish Harmonia as:

-   the authoritative rostering system;
-   the workforce-management system;
-   the authority that establishes workforce positions or employment;
-   the authority that determines clinical work;
-   a hospital-wide resource-logistics system; or
-   the authoritative source of all resource availability.

**Related distinction:**

-   **Workforce Service Coverage Management** establishes
    roster/on-call/service-coverage context: *who is rostered, on-call,
    or otherwise available to provide coverage?*
-   **Care Resource Deployment** applies available and authorised
    resource context to operational care demand: *who or what is
    actually deployed to support this care requirement?*
-   **Care Support Work Coordination** coordinates the resulting
    operational support work: *what work needs doing, by whom, and how
    is its progression/completion coordinated?*

These responsibilities may consume overlapping information but are not
interchangeable.

**Interpretive Warning:**

The name **Care Resource Deployment** can reasonably be read more
broadly than its intended Harmonia meaning.

Future architecture MUST NOT infer responsibility for hospital-wide
resource logistics merely from the Capability name. Its roster-support
and care-demand context must be retained explicitly in the canonical
Capability definition when this working decision is promoted.

------------------------------------------------------------------------

## DN-BC-006 --- Care Capacity, Resource Readiness and Resource Deployment Are Distinct

**Context:** Care Scheduling & Capacity Management.

**Working Decision:**

Operational care capacity, resource readiness/availability, and resource
deployment are related but semantically distinct.

-   **Operational Care Capacity Management** concerns the ability of a
    service to accommodate care in the current operational context.
-   **Care-Place Readiness Management** concerns whether a care place is
    actually ready for allocation or use.
-   **Care Resource Deployment** concerns application of
    available/authorised resources to care demand.

Possession of available staff, equipment, or an unoccupied bed does not
by itself establish service capacity.

Likewise, service capacity does not prove that a particular resource has
been deployed.

**Boundary:**

Operational service capacity is distinct from strategic or regional
capacity planning.

------------------------------------------------------------------------

## DN-BC-007 --- Physical Patient Movement Is Not Transfer of Care Responsibility

**Context:** Care Access & Coordination.

**Working Decision:**

Physical movement of a patient and transition of care responsibility are
separate business meanings.

-   **Patient Movement Coordination** concerns physical transit and
    applicable mobility constraints.
-   **Care Responsibility Transition** concerns
    establishment/coordination of who has actually assumed applicable
    care responsibility.

**Semantic guardrail:**

> Patient moved ≠ care responsibility transferred.

Arrival at a destination, referral submission, message receipt,
discharge readiness, or physical departure does not by itself establish
that another clinician, team, or service has accepted care
responsibility.

This distinction must be preserved in information semantics, workflow
interpretation, traceability, and any later implementation allocation.

------------------------------------------------------------------------

## DN-BC-008 --- Care Support Logistics Is Bounded Operational Context

**Context:**

`Care Access & Coordination → Care Support Logistics Management`

**Working Decision:**

Care Support Logistics Management is retained as a CT2 containing:

-   Patient Movement Coordination
-   Clinical Material Logistics Coordination
-   Care Support Work Coordination

**Intended Meaning:**

The branch supplies business context for operational support necessary
to enable care access and fulfilment, including physical patient
movement, clinically relevant material movement/custody, and authorised
support work.

**Boundary:**

It does not establish a general hospital logistics, supply-chain, ERP,
facilities-management, or workforce-management architecture.

Clinical Material Logistics Coordination should remain concerned with
materials whose movement, custody, availability, or disposition is
materially relevant to care or diagnostic fulfilment.

Care Support Work Coordination must not be interpreted as authority to
determine clinical work.

------------------------------------------------------------------------

## DN-BC-009 — Care Resource Deployment and Roster Support

**Context:**

`Care Access & Coordination → Care Scheduling & Capacity Management → Care Resource Deployment`

**Working Decision:**

**Care Resource Deployment** is retained beneath **Care Scheduling & Capacity Management**.

The Capability should be understood particularly in the context of **roster support and operational care resourcing**, rather than as a general hospital resource-management or logistics capability.

**Intended Meaning:**

Care Resource Deployment concerns applying knowledge of available and appropriately authorised resources to an operational care requirement.

For workforce resources, this includes the intersection of:

- roster and on-call context;
- current or expected availability;
- practitioner role and engagement;
- qualification, competency and applicable privilege;
- service coverage;
- operational care demand; and
- the resource actually deployed to support that demand.

The Capability therefore helps answer:

> Given the resources legitimately available to provide service coverage, which resource is operationally deployed to support this care requirement?

The same conceptual distinction may apply to non-workforce care resources where their availability and deployment materially affect care delivery, without establishing general asset-management responsibility.

**Relationship to Workforce Service Coverage Management:**

**Workforce Service Coverage Management** and **Care Resource Deployment** are deliberately distinct.

`Workforce Service Coverage Management` establishes the roster/on-call/service-coverage context:

> Who is rostered, on-call, or otherwise expected and authorised to provide service coverage?

`Care Resource Deployment` applies that available-resource context to current or planned care demand:

> Which available and appropriate resource is actually deployed to meet this care requirement?

Being rostered or available does not establish that a practitioner or other resource has been deployed to a particular care requirement.

Likewise, deployment does not alter the authoritative roster, qualification, privilege, employment or workforce record.

**Relationship to Care Support Work Coordination:**

**Care Support Work Coordination** concerns the operational work resulting from the care requirement:

> What support work needs to occur, who is to perform it, and how is its progression, completion or escalation coordinated?

Resource deployment and work coordination may interact, but they are not equivalent.

Assignment or dispatch of operational work does not itself establish workforce availability, qualification, privilege or clinical authority.

**Boundary:**

Care Resource Deployment does **not** make Harmonia responsible for:

- authoritative workforce rostering;
- workforce establishment or employment management;
- practitioner qualification or credential ownership;
- clinical privilege determination;
- determining what clinical work should be performed;
- enterprise asset management;
- general hospital resource logistics; or
- authoritative management of every resource's availability.

Harmonia may consume and correlate information from the authoritative systems responsible for those concerns where that information is required to support operational care coordination.

**Interpretive Warning:**

The phrase **Resource Deployment** is broader than the intended Harmonia responsibility and can easily be misread as hospital-wide workforce, equipment or logistics management.

The eventual canonical Capability definition MUST therefore retain the **care-demand and roster-support context**.

Future architecture MUST NOT infer broad resource-management ownership from the Capability name alone.

In particular:

> **Available for service coverage ≠ deployed to a care requirement ≠ assigned operational work.**

These are related but semantically distinct business states and responsibilities.

**Canonicalisation Note:**

When this working decision is promoted into the Business Capability catalogue, the Capability definition should explicitly preserve:

1. the roster/service-coverage relationship;
2. the care-demand context;
3. the distinction between availability and deployment;
4. the distinction between deployment and work coordination; and
5. the exclusion of authoritative rostering, workforce management and hospital-wide resource logistics.

------------------------------------------------------------------------

## DN-BC-010 — Decomposition Depth Must Not Distort Peer Completeness

**Context:**

Business Capability decomposition, identified while reviewing:

`Individual Care Delivery → Clinical Intervention`

The candidate decomposition currently proposes:

- Therapy & Counselling Intervention
- Medication Intervention
- Procedural Intervention

**Working Decision:**

Harmonia relevance may legitimately determine **whether** a Business Capability needs to be decomposed further for the Harmonia Business Architecture.

However, once a Capability is decomposed into peer capabilities representing distinct types of business activity, Harmonia relevance MUST NOT determine which materially distinct peers are represented.

In particular:

> **Where a Business Capability is decomposed into peer capabilities representing distinct types of business activity, the decomposition SHALL represent the materially distinct peer types required for a coherent business model. Harmonia relevance may determine whether decomposition is necessary, but SHALL NOT determine which peers exist once that decomposition is undertaken.**

**Reasoning:**

The Business Architecture describes business capability, not the subset of healthcare activity implemented or directly supported by Harmonia.

Selective decomposition based on Harmonia's technical interests would cause the Business Capability model to inherit solution boundaries.

For example, if Harmonia has significant interest in medication and procedural activity but little interest in other intervention types, this does not make medication and procedural intervention the only meaningful children of Clinical Intervention.

Once Clinical Intervention is decomposed by intervention type, the decomposition must remain coherent from the business perspective.

**Boundary:**

This rule does **not** require exhaustive decomposition of every Business Capability.

It remains valid to stop at a higher Capability where additional decomposition would not materially improve Harmonia's architectural context, derivation, governance or traceability.

The distinction is:

> **Scope may determine decomposition depth. Scope must not distort decomposition semantics.**

Therefore, for a Capability `X`, either:

1. retain `X` without subordinate decomposition where further distinction is unnecessary for the Harmonia architecture; or
2. if `X` is decomposed, identify the materially meaningful peer capabilities independently of which peers Harmonia happens to support.

**Application to Clinical Intervention:**

The current candidate:

```text
Clinical Intervention
  Therapy & Counselling Intervention
  Medication Intervention
  Procedural Intervention
```

is **not yet accepted as the final CT3 decomposition**.

`Medication Intervention` and `Procedural Intervention` appear to represent meaningful intervention classes.

However, `Therapy & Counselling Intervention` may be an artificial aggregation of otherwise distinct intervention classes and may function as a residual category for interventions that are neither medication nor procedural.

The appropriate intervention taxonomy therefore requires further architectural examination.

Potential intervention classes MUST NOT be created merely from examples or implementation concerns. They should be identified according to meaningful business distinctions between forms of clinical intervention.

**Interpretive Warning:**

Do not infer that every peer Capability must have:

- a Harmonia Feature;
- a Business Enabling Capability relationship;
- a Harmonia Service;
- a Harmonia component;
- equivalent implementation depth; or
- equivalent traceability.

A Capability may legitimately exist in the Business Architecture to make the business decomposition coherent while having little or no downstream Harmonia responsibility.

Conversely, absence of Harmonia implementation interest is not sufficient reason to omit a materially distinct peer once that part of the Business Capability model has been decomposed.

**Open Issue:**

Determine the coherent CT3 intervention taxonomy beneath **Clinical Intervention**.

The current three-child candidate remains provisional until that taxonomy has been examined and adjudicated.

------------------------------------------------------------------------

## DN-BC-011 — Individual Health Monitoring Decomposition

**Context:**

`Individual Care Delivery → Individual Health Monitoring`

The previous Business Capability candidate deliberately left the CT3 decomposition beneath **Individual Health Monitoring** unestablished, while identifying treatment response and deterioration as relevant concerns.

**Working Decision:**

**Individual Health Monitoring** decomposes into two CT3 Business Capabilities:

```text
Individual Health Monitoring
  Health State Monitoring
  Care Response Monitoring
```

These capabilities represent different scopes of health monitoring rather than different monitoring technologies, clinical specialties, observation types, or care settings.

---

### Health State Monitoring

**Intended Meaning:**

**Health State Monitoring** establishes an aggregate view of the health of an Individual, as ascertained from monitored data, at a given temporal setting.

The Capability is concerned with establishing the Individual's health state from the applicable monitored information available for that temporal context.

The resulting health-state view may incorporate multiple observations, measurements or other monitored indicators and is not limited to a particular problem, condition, treatment or episode of care.

**Boundary:**

Health State Monitoring is distinct from **Health Status Measurement**.

`Health Status Measurement` establishes individual observations or measurements concerning the Individual's health.

`Health State Monitoring` uses applicable monitored data to establish an aggregate, temporally situated view of the Individual's health state.

Accordingly:

> **Measurement contributes observations about health; monitoring establishes a view of health state from monitored data.**

Health State Monitoring does not, by itself, establish a diagnosis, prognosis, clinical risk determination or other clinical interpretation belonging to **Clinical Determination**.

---

### Care Response Monitoring

**Intended Meaning:**

**Care Response Monitoring** establishes a scoped view of the health status of an Individual within the context of:

- a particular problem or condition being treated; and
- the treatment being applied specifically to that problem or condition.

Its concern is therefore not the Individual's aggregate health state in isolation, but the Individual's health state as it relates to the problem/condition and associated treatment whose response is being monitored.

**Service Relationship:**

Care Response Monitoring **consumes services provided by Health State Monitoring**.

Conceptually:

```text
Monitored Data
      |
      v
Health State Monitoring
      |
      | Health-state services
      v
Care Response Monitoring
      |
      +-- Problem / Condition Context
      |
      +-- Treatment Context
```

Care Response Monitoring does not independently recreate the aggregate Health State.

It applies the relevant problem/condition and treatment context to the health-state information established through Health State Monitoring.

Accordingly:

> **Health State Monitoring establishes the aggregate temporally situated health-state view; Care Response Monitoring scopes that view to a particular problem/condition and its associated treatment.**

---

### Deterioration and Improvement

**Working Decision:**

**Deterioration Monitoring** is not presently established as a separate peer CT3 Capability.

Deterioration, improvement, stability, unexpected change, or lack of expected response represent states or changes that may be identified through monitoring rather than necessarily distinct monitoring capabilities.

Deterioration may be apparent:

- within the aggregate health state established through **Health State Monitoring**; or
- within the problem/treatment context established through **Care Response Monitoring**.

The relevant monitoring context determines the architectural meaning.

This same reasoning applies to improvement and other changes in health state.

---

### Relationship to Clinical Determination

Monitoring and clinical determination remain distinct.

Health State Monitoring and Care Response Monitoring establish the applicable monitored view of the Individual.

Where that information is subsequently interpreted to establish a clinical finding, diagnosis, problem determination, risk, prognosis or other clinical conclusion, that responsibility belongs to the applicable **Clinical Determination** Capability.

Therefore:

> **Observed or monitored change ≠ clinical interpretation of that change.**

The fact that monitored information demonstrates a change does not, by itself, establish the clinical meaning of that change.

---

### Modelling Guardrail

The CT3 decomposition of Individual Health Monitoring SHALL NOT be based upon:

- care setting;
- clinical specialty;
- profession;
- monitoring technology;
- individual observation or measurement type;
- physiological system; or
- particular positive or adverse monitoring outcome.

The distinction between the two CT3 Capabilities is principally one of **scope and clinical context**:

- **Health State Monitoring** — aggregate Individual health state at a temporal setting.
- **Care Response Monitoring** — problem/condition-and-treatment-scoped view of that health state.

---

### Future Traceability Warning

The statement that **Care Response Monitoring consumes services from Health State Monitoring** is an architectural semantic decision but does not, by itself, establish the final Feature, Function, Service, component, interface, runtime invocation, or RADS relationship required to realise that consumption.

Those relationships must be established through the applicable downstream architecture and traceability process.

Future modelling MUST preserve the semantic dependency without prematurely converting it into an implementation dependency.

------------------------------------------------------------------------

## DN-BC-012 — Individual Care Review Decomposition

**Context:**

`Individual Care Delivery → Individual Care Review`

The candidate Business Capability decomposition proposed:

- Care Intent Appropriateness Review
- Care Effectiveness Review

The underlying distinction was accepted, but **Care Intent Appropriateness Review** was considered unnecessarily awkward and potentially misleading terminology.

**Working Decision:**

**Individual Care Review** decomposes into:

```text
Individual Care Review
  Care Appropriateness Review
  Care Effectiveness Review
```

The two CT3 Capabilities represent two materially different review questions:

> **Care Appropriateness Review — Are we still pursuing the right care?**

> **Care Effectiveness Review — Is the care being provided achieving its intended outcomes?**

---

### Care Appropriateness Review

**Intended Meaning:**

**Care Appropriateness Review** evaluates whether the current intended care remains appropriate for the Individual in light of the applicable current context.

That context may include:

- current health state;
- clinical findings and determinations;
- identified problems or conditions;
- care goals;
- planned care responses;
- interventions being undertaken;
- response to care; and
- other circumstances materially affecting the suitability of the intended care.

The Capability is concerned with reviewing the continuing suitability of the care being pursued.

**Boundary with Individual Care Planning:**

Care Appropriateness Review does not itself establish the replacement care plan.

It may determine that existing care remains appropriate or that reconsideration or replanning is required.

Where new or changed care goals, responses or interventions are subsequently established, that responsibility belongs to **Individual Care Planning**.

Accordingly:

> **Determining that care should be reconsidered ≠ performing the resulting care planning.**

---

### Care Effectiveness Review

**Intended Meaning:**

**Care Effectiveness Review** evaluates whether delivered care is achieving, or has achieved, its intended goals and outcomes.

It considers applicable evidence concerning the Individual's response to care, including information established through **Care Response Monitoring**.

Its concern is therefore the effectiveness of the care provided relative to what that care was intended to achieve.

**Boundary with Care Response Monitoring:**

Care Response Monitoring establishes the problem/condition-and-treatment-scoped view of the Individual's health state.

Care Effectiveness Review uses applicable response information to evaluate the effectiveness of the care.

Accordingly:

> **Monitoring the response to care ≠ evaluating whether the care is effective.**

Care Response Monitoring provides relevant evidence; Care Effectiveness Review applies the review judgement.

---

### Relationship Between Appropriateness and Effectiveness

Appropriateness and effectiveness are related but not equivalent.

Care may:

- remain appropriate while not yet having produced the intended outcome;
- prove ineffective and consequently require reconsideration;
- produce an outcome while no longer being appropriate to continue;
- remain both appropriate and effective; or
- become inappropriate because the Individual's health state, circumstances or clinical understanding has changed.

An effectiveness finding may therefore inform an appropriateness review, but it does not replace it.

---

### Relationship to the Broader Individual Care Model

The currently established Business Capabilities form a coherent set of care responsibilities:

```text
Clinical Determination
        |
        v
Individual Care Planning
        |
        v
Clinical Intervention
        |
        v
Individual Health Monitoring
        |
        v
Individual Care Review
        |
        +----> reconsideration / replanning where required
```

**Interpretive Warning:**

This representation describes meaningful relationships between Business Capabilities.

It MUST NOT be interpreted as establishing a mandatory linear workflow, state machine, clinical pathway, runtime orchestration sequence, or required system interaction.

Actual care may be iterative, concurrent, recursive, interrupted, reordered, or may exercise only some of these Capabilities in a particular context.

The Business Capability model describes **what the business is able to do**, not a prescribed sequence in which those abilities must be exercised.

---

### Modelling Guardrail

The distinction beneath Individual Care Review is:

- **Care Appropriateness Review** — whether the intended/current care remains suitable.
- **Care Effectiveness Review** — whether delivered care is achieving its intended outcomes.

Future architecture MUST preserve the distinction between:

- monitoring health or treatment response;
- reviewing effectiveness;
- reviewing continuing appropriateness; and
- establishing revised care through planning.

These concerns may exchange information and influence one another but remain semantically distinct Business Capabilities.

------------------------------------------------------------------------

## DN-BC-013 — Medication & Pharmacy Services Decomposition

**Context:**

`Diagnostic, Therapeutic & Clinical Support Services → Medication & Pharmacy Services`

The candidate Business Capability decomposition contained only:

- Medication Dispensing

This was considered insufficient once **Medication & Pharmacy Services** had been deliberately decomposed to CT3.

In accordance with `DN-BC-010`, Harmonia relevance may determine whether decomposition is required, but MUST NOT distort the meaningful peer capabilities once decomposition occurs.

**Working Decision:**

The current working CT3 decomposition is:

```text
Medication & Pharmacy Services
  Prescription Medication Dispensing
  Non-Prescription Medication Provision
  Medication Advice
  Medication Dose Administration Aid Provision
```

These represent materially different pharmacy-service responsibilities rather than stages of a single medication workflow.

---

### Prescription Medication Dispensing

**Intended Meaning:**

**Prescription Medication Dispensing** concerns the professional preparation and provision of medication in accordance with an applicable authorised prescription or medication order.

Its context includes the controls necessary to ensure that the medication supplied corresponds to the authorised medication request and applicable dispensing requirements.

**Boundary:**

Prescription Medication Dispensing does not establish the clinical decision to prescribe the medication.

Prescribing and dispensing are distinct business responsibilities.

Accordingly:

> **Authorising medication therapy ≠ dispensing the authorised medication.**

The existence of a prescription or medication order provides an input to dispensing; the dispensing capability does not create the underlying clinical authority for medication treatment.

---

### Non-Prescription Medication Provision

**Intended Meaning:**

**Non-Prescription Medication Provision** concerns provision of medication that does not require an applicable prescription, subject to relevant professional, regulatory and safety controls.

The term **Provision** is deliberately used rather than **Dispensing** to preserve the distinction between prescription-based dispensing and non-prescription medication supply.

**Boundary:**

Non-Prescription Medication Provision does not imply unrestricted retail supply.

Applicable professional assessment, advice, regulation, restrictions or other controls may still govern whether and how a non-prescription medication is provided.

---

### Medication Advice

**Intended Meaning:**

**Medication Advice** concerns provision of professional advice regarding medication and its appropriate use.

This may include advice relevant to matters such as:

- medication use;
- administration;
- precautions;
- interactions;
- adherence;
- safe handling;
- expected effects; and
- other medication-specific considerations.

The exact scope may require further refinement during canonicalisation.

**Boundary:**

Medication Advice does not automatically establish:

- a diagnosis;
- a prescription;
- a medication treatment decision;
- authority to alter an established treatment plan; or
- a general Clinical Determination capability.

Medication advice may inform clinical decisions, and clinical information may inform medication advice, without making those responsibilities equivalent.

Accordingly:

> **Providing medication advice ≠ determining or prescribing medication treatment.**

---

### Medication Dose Administration Aid Provision

**Intended Meaning:**

**Medication Dose Administration Aid Provision** concerns preparation and provision of medication organised into a form that supports administration according to an established medication regimen.

This includes the business capability represented by commonly used medication packaging arrangements such as a **Webster-pak**, without making a particular commercial product or packaging implementation part of the Capability definition.

The Capability concerns the organised preparation/provision of medication doses to support correct administration of an established regimen.

**Terminology Decision:**

`Webster Pack` / `Webster-pak` MUST NOT be used as the Business Capability name.

It represents a familiar implementation/product example of the broader **Dose Administration Aid (DAA)** concept.

The Business Capability therefore remains:

**Medication Dose Administration Aid Provision**

**Boundary:**

This Capability does not establish the medication regimen.

The regimen is an input to preparation of the dose administration aid.

Accordingly:

> **Organising medication according to a regimen ≠ establishing the regimen.**

---

### Relationship to Clinical Intervention

Medication & Pharmacy Services and medication-related Clinical Intervention are related but distinct.

**Medication & Pharmacy Services** concerns pharmacy-service responsibilities such as medication dispensing, provision, advice and dose-administration-aid provision.

**Medication Intervention** concerns medication as an intervention applied in the care of the Individual.

The same medication information may therefore participate in multiple business contexts without collapsing those Business Capabilities into one another.

In particular:

> **Medication prescribed ≠ medication dispensed ≠ medication provided ≠ medication administered.**

These may be related events or states, but they represent materially different business responsibilities.

---


### Modelling Guardrail

The Medication & Pharmacy Services decomposition describes distinct business abilities.

It MUST NOT be interpreted as a medication workflow or mandatory sequence.

For example, the model does not imply:

```text
Prescription
    → Dispensing
    → Advice
    → Dose Administration Aid
    → Administration
```

Any such sequence is contextual and belongs to applicable care processes/workflows rather than being established by the Capability hierarchy.

The Capability decomposition establishes **what the business can do**, not the order in which those abilities must be exercised.

------------------------------------------------------------------------

## DN-BC-014 — Diagnostic Support Provision Decomposition

**Context:**

`Diagnostic, Therapeutic & Clinical Support Services → Diagnostic Support Provision`

The previous candidate decomposition proposed only:

- Diagnostic Investigation Performance
- Diagnostic Reporting

This was considered insufficient to represent the materially distinct business abilities involved in providing a diagnostic service.

In accordance with `DN-BC-010`, once Diagnostic Support Provision is decomposed, the decomposition should represent a coherent set of materially distinct peer capabilities rather than only those of immediate interest to Harmonia.

**Working Decision:**

**Diagnostic Support Provision** decomposes into:

```text
Diagnostic Support Provision
  Diagnostic Service Appointment Management
  Diagnostic Service Delivery
  Diagnostic Specimen Management
  Diagnostic Data Management
  Diagnostic Interpretation
  Diagnostic Report Management
  Diagnostic Report Distribution
```

These capabilities represent distinct responsibilities involved in the provision of diagnostic services. They do not establish a mandatory diagnostic workflow.

---

### Diagnostic Service Appointment Management

**Intended Meaning:**

**Diagnostic Service Appointment Management** concerns management of appointments associated with delivery of a diagnostic service.

Diagnostic appointments may be subject to requirements specific to the diagnostic service, including service or modality availability, preparation requirements, sequencing, duration, urgency and other constraints affecting when and how the diagnostic service can be delivered.

**Boundary:**

Diagnostic Service Appointment Management does not replace the broader **Care Appointment Management** capability.

It represents the diagnostic-service-specific business context in which appointment-management capabilities are applied.

Accordingly:

> **Generic appointment management ≠ management of the diagnostic-service-specific appointment context.**

The relationship between these capabilities may later be expressed through Business Enabling Capability and service consumption relationships rather than by duplicating generic scheduling behaviour.

---

### Diagnostic Service Delivery

**Intended Meaning:**

**Diagnostic Service Delivery** concerns performance of the diagnostic service required to obtain diagnostic evidence concerning the Individual.

The term deliberately accommodates different forms of diagnostic service rather than assuming all diagnostic services operate through an identical investigation model.

Diagnostic Service Delivery may result in specimens, acquired diagnostic data, measurements, observations or other diagnostic evidence according to the nature of the service.

**Boundary:**

Delivery of the diagnostic service does not itself imply that the resulting evidence has been interpreted or that an authoritative diagnostic report has been established.

> **Diagnostic service performed ≠ diagnostic meaning established.**

---

### Diagnostic Specimen Management

**Intended Meaning:**

**Diagnostic Specimen Management** concerns management of specimens associated with diagnostic service delivery.

Its scope includes the business responsibilities necessary to maintain the identity, integrity, suitability and applicable custody/context of a diagnostic specimen throughout its relevant lifecycle.

This may include concerns associated with collection, identification, handling, custody, movement, suitability and disposition.

**Boundary:**

Managing the specimen is distinct from analysing or interpreting the diagnostic evidence derived from it.

> **Specimen managed ≠ specimen evidence interpreted.**

Diagnostic Specimen Management also does not establish a general-purpose logistics or inventory-management capability.

---

### Diagnostic Data Management

**Intended Meaning:**

**Diagnostic Data Management** concerns management of data acquired or produced through diagnostic service delivery.

Diagnostic data may include, according to the diagnostic service, such material as:

- images;
- waveforms;
- measurements;
- analyser output;
- observations;
- derived diagnostic data; and
- other evidence produced through diagnostic activity.

Diagnostic data may exist independently of the eventual diagnostic interpretation or diagnostic report.

**Boundary:**

Diagnostic Data Management does not itself establish the clinical meaning of the data.

Accordingly:

> **Diagnostic data acquired or managed ≠ diagnostic data interpreted.**

It also does not imply that all diagnostic data must be incorporated directly into the final diagnostic report.

---

### Diagnostic Interpretation

**Intended Meaning:**

**Diagnostic Interpretation** concerns establishing diagnostic meaning from applicable diagnostic evidence and data.

It represents the professional interpretive activity through which diagnostic evidence is assessed and relevant diagnostic findings or conclusions are established.

**Boundary with Clinical Determination:**

Diagnostic Interpretation is distinct from the broader patient-level **Clinical Determination** capabilities.

A diagnostic service may establish what its diagnostic evidence demonstrates. A clinician responsible for the Individual may subsequently combine that interpretation with other clinical information, health-state information and care context to establish a diagnosis, problem, risk, prognosis or other patient-level clinical determination.

Accordingly:

> **Interpretation of diagnostic evidence ≠ patient-level Clinical Determination.**

The same information may participate in both contexts without making the two business responsibilities equivalent.

---

### Diagnostic Report Management

**Intended Meaning:**

**Diagnostic Report Management** concerns establishment and management of the diagnostic report as the authoritative expression of the applicable diagnostic-service findings and conclusions.

Its responsibilities include management of the report through the states necessary to establish its applicable authoritative status.

This may include behaviours such as:

- report creation;
- review;
- amendment;
- verification;
- finalisation;
- authorisation or signing; and
- management of report status.

The exact lifecycle and authority rules may vary according to diagnostic discipline and applicable governance.

**Terminology Decision:**

Signing, verification and finalisation are behaviours associated with **Diagnostic Report Management** and do not require separate Capability names merely because particular diagnostic workflows expose them as discrete activities.

**Boundary:**

A diagnostic interpretation and an authoritative diagnostic report are related but distinct.

> **Diagnostic meaning established ≠ authoritative diagnostic report established.**

Likewise, establishment of an authoritative report does not mean that the report has been distributed to all applicable recipients.

---

### Diagnostic Report Distribution

**Intended Meaning:**

**Diagnostic Report Distribution** concerns authorised distribution or provision of an established diagnostic report to applicable recipients, destinations or consuming contexts.

Its concern is making the report available beyond the diagnostic reporting context in accordance with applicable information-access, privacy, security and communication controls.

**Boundary:**

Diagnostic Report Distribution does not establish or alter the diagnostic interpretation or authoritative content of the report.

> **Diagnostic report established ≠ diagnostic report distributed.**

Distribution also does not bypass applicable Health Information Access, Health Information Communication, Health Information Control, privacy, security or other platform governance responsibilities.

---

### Important Semantic Distinctions

The decomposition deliberately preserves several states and responsibilities that future architecture MUST NOT collapse:

> **Diagnostic service scheduled ≠ diagnostic service delivered.**

> **Diagnostic service delivered ≠ diagnostic evidence interpreted.**

> **Specimen managed ≠ diagnostic evidence interpreted.**

> **Diagnostic data managed ≠ diagnostic data interpreted.**

> **Diagnostic evidence interpreted ≠ patient-level Clinical Determination.**

> **Diagnostic interpretation established ≠ authoritative diagnostic report established.**

> **Diagnostic report established ≠ diagnostic report distributed.**

These distinctions are expected to become important when later deriving Features, Functions, Services, information-state semantics and traceability.

---

### Relationship to Diagnostic Ordering

**Working Boundary:**

**Diagnostic Order Management** is not established as a subordinate CT3 Capability of Diagnostic Support Provision by this decision.

The request/order for a diagnostic service represents the demand/request context through which diagnostic service provision is initiated, rather than necessarily an intrinsic subordinate capability of the diagnostic provider.

Existing Order Management and care-requesting contexts should therefore be considered during later enabling-capability and service modelling.

This decision does not establish the eventual Feature, Function, Service or component relationships through which diagnostic orders are managed or communicated.

---

### Modelling Guardrail

The CT3 decomposition describes distinct business abilities involved in diagnostic-service provision.

It MUST NOT be interpreted as establishing a mandatory sequence such as:

```text
Appointment
    → Service Delivery
    → Specimen
    → Data
    → Interpretation
    → Report
    → Distribution
```

Different diagnostic services may exercise different combinations of these capabilities and may exercise them concurrently, iteratively or in a different order.

For example, some diagnostic services may not involve a specimen at all.

The Business Capability model establishes **what the business is able to do**, not a prescribed diagnostic workflow.

------------------------------------------------------------------------

## DN-BC-015 — Clinical Technology Management Decomposition

**Context:**

`Health Products & Clinical Technology → Clinical Technology Management`

The previous candidate decomposition identified only:

- Clinical Device Identification & Specification
- Clinical Device Use Attribution

This was considered insufficient because it primarily described information Harmonia might maintain about a Clinical Device rather than the materially distinct business abilities required to manage clinical technology.

In accordance with `DN-BC-010`, decomposition of Clinical Technology Management should represent the materially distinct peer capabilities necessary for a coherent Business Capability model.

## Working Decision

**Clinical Technology Management** decomposes into:

```text id="0a9z9s"
Clinical Technology Management
  Clinical Device Inventory Management
  Clinical Device Feature & Release Management
  Clinical Device Loan & Deployment Management
  Clinical Device Utilisation Support
  Clinical Device Integration Provision
  Clinical Device Data Integration
  Clinical Device Lifecycle Management
```

These are distinct business abilities and SHALL NOT be interpreted as mandatory sequential lifecycle stages.

---

## CT3 Capability Boundaries

### Clinical Device Inventory Management

Owns the ability to establish and maintain **what Clinical Devices exist and the inventory context in which they are managed**.

This includes device identity, device type/model/specification, ownership or custodianship, inventory status and other information required to distinguish and manage devices as clinical technology assets.

It does **not** own:

- deployment or loan of the device;
- enabled software/features/releases;
- device integration;
- user enablement;
- maintenance or lifecycle activity; or
- clinical activity performed using the device.

**Boundary test:**

> If the concern is **what device exists and what managed clinical-technology asset it represents**, it belongs here. If the concern is **what is currently being done with that device**, another CT3 is responsible.

---

### Clinical Device Feature & Release Management

Owns the ability to establish and manage the **functional and release state of a Clinical Device**, including applicable software, firmware, configuration, enabled features and other managed characteristics that materially determine device capability.

It does **not** own:

- basic device inventory identity;
- deployment of the device;
- establishment of integration connectivity;
- training or utilisation support resulting from a change; or
- lifecycle maintenance of the physical device.

A feature or release change may create requirements for integration changes, renewed utilisation support or lifecycle activity without transferring responsibility for those activities to Feature & Release Management.

**Boundary test:**

> If the concern is **what functional/release capability the device currently provides**, it belongs here.

---

### Clinical Device Loan & Deployment Management

Owns the ability to manage **loan, issue, deployment, assignment, return and applicable operational placement of Clinical Devices**.

Its concern is making a managed device available within an applicable operational context.

It does **not** own:

- inventory definition;
- workforce rostering;
- user competency;
- clinical activity performed with the device;
- device integration; or
- general clinical logistics.

**Boundary test:**

> If the concern is **where, to whom or into what operational context the device has been loaned, issued or deployed**, it belongs here.

---

### Clinical Device Utilisation Support

Owns the ability to provide **device-specific enablement, guidance and operational support necessary for appropriately authorised users to use a Clinical Device safely and effectively**.

This may include device familiarisation, device-specific operating guidance, assistance, competency-supporting activity and renewed enablement following material changes to device features or operation.

It does **not** establish:

- professional qualification;
- clinical credentialing;
- clinical privileges;
- workforce authority;
- authority to undertake the clinical activity; or
- the clinical activity performed using the device.

Those constraints are established externally and may be consumed by Clinical Device Utilisation Support.

**Boundary test:**

> If the concern is **ensuring an otherwise appropriately authorised user is enabled and supported to use this particular Clinical Device**, it belongs here.

The following distinction SHALL be preserved:

> **Qualified or authorised to perform a clinical activity ≠ enabled to use a particular Clinical Device ≠ actually using that device in care.**

---

### Clinical Device Integration Provision

Owns the ability to establish and maintain the **technical integration capability through which a Clinical Device can participate in applicable information exchanges**.

This may include applicable connectivity, interface, endpoint, protocol and integration configuration concerns.

It does **not** own:

- the clinical data subsequently exchanged;
- interpretation of device-produced data;
- generic enterprise integration infrastructure; or
- the clinical activity performed by the device.

**Boundary test:**

> If the concern is **making the device capable and correctly configured to participate in an integration**, it belongs here.

---

### Clinical Device Data Integration

Owns the ability to **acquire, exchange or convey Clinical Device data through established integration capability**.

Its concern is the operational integration of device-produced or device-consumed data rather than establishment of the underlying connectivity itself.

It does **not** own:

- provisioning the integration;
- determining clinical meaning from the exchanged data;
- general management of the Clinical Device; or
- authoritative management of downstream clinical information.

**Boundary test:**

> If the concern is **device data actually being exchanged through an established integration**, it belongs here.

Accordingly:

> **Device capable/configured for integration ≠ device data actually integrated.**

---

### Clinical Device Maintenance Management

Owns the ability to manage the **maintenance, testing, calibration, inspection, fault remediation and repair activities necessary to maintain or restore a Clinical Device to an acceptable operational condition**.

It may consume externally provided device-monitoring information, fault information, utilisation information or other evidence indicating that maintenance attention is required.

It does **not** own:

- the overall lifecycle disposition of the device;
- inventory identity;
- loan or deployment;
- device feature/release management;
- technical monitoring itself; or
- decisions concerning acquisition, replacement, withdrawal or retirement except where maintenance findings provide evidence for those decisions.

**Boundary test:**

> If the concern is **maintaining or restoring the device to an acceptable operational condition**, it belongs here. If the concern is **whether the device should continue to exist or remain in service as a managed clinical technology asset**, it has crossed into Clinical Device Lifecycle Management.

---

### Clinical Device Lifecycle Management

Owns the ability to manage the **Clinical Device through its overall managed lifecycle**, including its progression between applicable lifecycle states and decisions concerning its continuing place within the clinical technology estate.

This includes lifecycle concerns such as introduction into service, continued service, lifecycle review, replacement, withdrawal, decommissioning and retirement.

Clinical Device Lifecycle Management may consume evidence from:

- Clinical Device Maintenance Management;
- externally provided device monitoring;
- utilisation information;
- device Feature & Release Management;
- safety or assurance activities; and
- other applicable clinical-technology management capabilities.

It does **not** itself perform maintenance, repair, calibration, technical monitoring or routine operational deployment.

**Boundary test:**

> If the concern is **what lifecycle state the device is in and whether it should enter, remain in, transition within or leave clinical service**, it belongs here.

---

## Maintenance, Monitoring and Lifecycle Boundary

The following three responsibilities SHALL remain distinct:

> **Clinical Device Monitoring** provides evidence about device state, condition or performance.

> **Clinical Device Maintenance Management** manages activity intended to maintain or restore acceptable device condition.

> **Clinical Device Lifecycle Management** governs the device's progression through and disposition within its overall managed lifecycle.

Accordingly:

> **Device monitored ≠ device maintained ≠ device lifecycle managed.**

Monitoring remains externally provided enabling capability under this decision.

Maintenance findings may cause a lifecycle decision—for example, replacement or retirement—but this does not make maintenance and lifecycle management the same capability.

Likewise, a lifecycle decision may require maintenance activity—for example, inspection before return to service—without transferring lifecycle responsibility to Clinical Device Maintenance Management.
---

## External Monitoring Boundary

**Clinical Device Monitoring is not established as a separate CT3 Capability under Clinical Technology Management.**

Where monitoring of device technical or operational state is required, that monitoring is provided through an applicable external/enabling capability and consumed by Clinical Device Lifecycle Management.

The provider of that monitoring capability is **not established by this decision** and SHALL be resolved during subsequent Business Enabling Capability reconciliation.

Accordingly:

> **Monitoring the state of a Clinical Device ≠ managing the Clinical Device in response to that state.**

This decision MUST NOT be interpreted as establishing a new Business Enabling Capability, Feature, Function, Service, component or integration mechanism.

---

## Important Semantic Distinctions

The decomposition deliberately preserves the following distinctions:

> **Device identity/inventory ≠ device functional/release state.**

> **Device available in inventory ≠ device loaned or deployed.**

> **Device deployed ≠ user enabled to use the device.**

> **User enabled to use device ≠ user professionally authorised to perform the clinical activity.**

> **Device integration provisioned ≠ device data integrated.**

> **Device monitored ≠ device lifecycle managed.**

> **Device managed for clinical use ≠ device actually used in an instance of care.**

These distinctions are expected to be material when Features, Functions, Services, information responsibilities and Business Enabling Capability relationships are subsequently derived.

## Modelling Guardrail

The seven CT3 Capabilities represent distinct business abilities within Clinical Technology Management.

They SHALL NOT be interpreted as a mandatory device lifecycle, workflow or implementation architecture.

In particular, this decision does not establish ownership by Harmonia, system/component boundaries, runtime sequencing, information-system authority, or a one-to-one mapping between these Business Capabilities and future Business Enabling Capabilities, Features, Functions or Services.

------------------------------------------------------------------------

## DN-BC-016 — Clinical Safety Management Decomposition

**Context:**

`Clinical Quality, Safety & Improvement → Clinical Safety Management`

The previous candidate decomposition identified:

- Clinical Safety Concern & Response Management
- Clinical Safety Threat Monitoring

This was considered insufficiently precise. In particular, `Clinical Safety Concern & Response Management` combined materially different responsibilities associated with identifying clinical risk, managing mitigation of that risk, and responding when a risk has materialised or requires active intervention.

The Clinical Safety domain is substantially broader than the scope required to establish the Harmonia R1/R2 architecture. The decomposition therefore establishes the materially relevant capability distinctions while deliberately avoiding exhaustive modelling of the specialist Clinical Safety domain.

## Working Decision

**Clinical Safety Management** decomposes for the Harmonia R1/R2 architectural scope into:

```text id="ht16t9"
Clinical Safety Management
  Clinical Risk Identification
  Clinical Risk Mitigation Management
  Clinical Risk Response Management
```

This decomposition is intentionally non-exhaustive beyond the architectural needs of Harmonia R1/R2.

---

## CT3 Capability Boundaries

### Clinical Risk Identification

Owns the ability to **identify and characterise a clinical safety risk requiring recognition within the applicable clinical safety context**.

Clinical risk may be identified through incidents, concerns, observations, reported information, monitoring, assurance activity or other applicable evidence.

Its responsibility is establishing that the risk exists and sufficiently characterising it for subsequent management.

It does **not** own:

- implementation or management of risk mitigation;
- active response to a materialised clinical risk;
- the underlying monitoring capability through which risk evidence may be detected;
- generic incident-management capability; or
- specialist clinical-safety functions beyond the required Harmonia architectural scope.

**Boundary test:**

> If the concern is **recognising and characterising a clinical safety risk**, it belongs here. Once the concern becomes **what should be done to reduce or respond to that risk**, responsibility has crossed into another CT3.

---

### Clinical Risk Mitigation Management

Owns the ability to manage **measures intended to reduce the likelihood, exposure or consequence associated with an identified clinical risk**.

Its primary concern is prospective or preventative management of established clinical risk.

Mitigation may consume information from Clinical Risk Identification, clinical governance, assurance, operational capabilities and other relevant sources.

It does **not** own:

- identification of the underlying clinical risk;
- active response to a risk that has materialised or requires immediate intervention;
- generic control implementation capabilities; or
- ownership of every operational capability through which a mitigation may ultimately be implemented.

**Boundary test:**

> If the concern is **what should be established or changed to reduce an identified clinical risk before or independently of active risk response**, it belongs here.

---

### Clinical Risk Response Management

Owns the ability to manage the **active response to a clinical safety risk that has materialised, is emerging, or otherwise requires active intervention**.

Its concern includes coordination of the applicable response, containment, remediation and other actions required to manage the manifested or actively emerging clinical safety concern.

It does **not** own:

- initial identification of the risk;
- prospective mitigation management;
- every operational capability used to execute a response; or
- the entirety of clinical incident, quality or safety governance.

**Boundary test:**

> If the concern is **managing what must now be done because the clinical risk has materialised, is emerging or requires active intervention**, it belongs here.

---

## Important Semantic Distinctions

The following distinctions SHALL be preserved:

> **Clinical risk identified ≠ clinical risk mitigated ≠ clinical risk actively responded to.**

Identification establishes knowledge of the risk.

Mitigation manages measures intended to reduce its likelihood, exposure or consequence.

Response manages active intervention where the risk has materialised, is emerging or otherwise requires an active response.

These capabilities may interact iteratively and SHALL NOT be interpreted as establishing a mandatory sequential workflow.

---

## Monitoring Boundary

**Clinical Safety Threat Monitoring is not established as a separate CT3 Capability by this decision.**

Clinical Risk Identification may consume evidence produced by monitoring capabilities, but this does not make the underlying monitoring activity an intrinsic subordinate capability of Clinical Safety Management.

The applicable monitoring capability or capabilities are not established by this decision and may subsequently be identified through Business Enabling Capability reconciliation.

Accordingly:

> **Monitoring for evidence of clinical risk ≠ identifying and characterising the clinical risk demonstrated by that evidence.**

This decision does not establish a new Business Enabling Capability, Feature, Function, Service, component or runtime dependency.

---

## R1/R2 Scope Boundary

Clinical Safety Management is a substantial specialist business domain.

The three CT3 Capabilities established here are **not asserted to constitute an exhaustive enterprise decomposition of Clinical Safety Management**.

They represent the level of distinction presently required to:

- establish coherent Harmonia architectural context;
- derive and constrain Harmonia responsibilities;
- support later Business Enabling Capability and Feature traceability; and
- prevent materially different clinical-risk responsibilities from being collapsed into a single capability.

Additional specialist Clinical Safety capabilities SHALL NOT be introduced merely to produce an apparently complete healthcare-enterprise capability model.

Further decomposition is warranted only where an additional distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

Accordingly:

> **The existence of additional real-world Clinical Safety responsibilities does not create an obligation for Harmonia to model them for R1/R2.**

This is an intentional architecture scope boundary, not an assertion that no additional Clinical Safety capabilities exist.

---

## Modelling Guardrail

The R1/R2 stopping point SHALL NOT be interpreted as permission to omit a materially distinct peer capability where subsequent Harmonia requirements demonstrate that the distinction matters.

Consistent with `DN-BC-010`:

> **Scope may determine decomposition depth. Scope must not distort decomposition semantics.**

Where future Harmonia requirements expose an additional materially relevant Clinical Safety capability, the Business Capability model may be extended through explicit architectural adjudication rather than by silently expanding the meaning of one of the three established CT3 Capabilities.

## Decision Consequences

Establishing **Clinical Risk Identification**, **Clinical Risk Mitigation Management** and **Clinical Risk Response Management** as distinct CT3 Capabilities has the following architectural consequences.

### DC-01 — Risk Responsibilities SHALL Remain Distinct

Subsequent architecture SHALL preserve the distinction between:

- identifying and characterising clinical risk;
- managing measures intended to mitigate clinical risk; and
- managing active response to materialised or emerging clinical risk.

A downstream Feature, Function, Service or implementation component MAY support more than one of these responsibilities, but such implementation reuse SHALL NOT collapse their Business Capability semantics.

---

### DC-02 — Monitoring SHALL NOT Be Mistaken for Risk Identification

The presence of monitoring, alerting, surveillance or event-detection functionality SHALL NOT by itself establish that **Clinical Risk Identification** has occurred.

Monitoring may provide evidence or stimuli from which clinical risk is identified.

Accordingly:

```text id="vyljhw"
Monitoring / Observation
        ↓ evidence
Clinical Risk Identification
```

The eventual architecture SHALL preserve the distinction between **detecting something** and **establishing that what has been detected constitutes a clinical risk**.

---

### DC-03 — Mitigation SHALL NOT Absorb Operational Execution

Clinical Risk Mitigation Management may determine, coordinate or govern mitigation measures whose implementation requires other Business or Business Enabling Capabilities.

This does not transfer ownership of those underlying capabilities into Clinical Safety Management.

For example, a mitigation may require changes to:

- clinical workflow;
- Clinical Device configuration;
- Health Information controls;
- workforce practice;
- clinical guidance;
- service-delivery arrangements; or
- another operational capability.

The capability responsible for implementing the change remains semantically distinct from the capability responsible for managing the clinical-risk mitigation.

---

### DC-04 — Response SHALL NOT Become a Generic Incident-Management Capability

Clinical Risk Response Management is bounded to management of active response to clinical safety risk.

It SHALL NOT become a generic container for:

- enterprise incident management;
- emergency management;
- operational issue management;
- technical fault management;
- complaint management; or
- other unrelated response activity.

Where Clinical Risk Response Management consumes those capabilities, their existing responsibilities remain intact.

---

### DC-05 — R1/R2 Scope SHALL Remain Deliberately Bounded

Future modelling SHALL NOT infer missing Clinical Safety CT3 Capabilities merely to make the Clinical Safety domain appear enterprise-complete.

Additional decomposition requires explicit architectural justification demonstrating that the distinction materially affects Harmonia.

Conversely, the R1/R2 scope boundary SHALL NOT prevent later introduction of additional Clinical Safety Capabilities where such evidence exists.

---

### DC-06 — Clinical Safety State SHALL Not Be Reduced to Workflow State

Risk identification, mitigation and response may be iterative, concurrent and mutually informative.

The decomposition SHALL NOT be interpreted as requiring:

```text id="92wjoi"
Identify → Mitigate → Respond
```

as a mandatory lifecycle or workflow.

A response may reveal additional risk; mitigation may expose new evidence; monitoring may continue throughout; and multiple risks may be managed independently.

The Capability model therefore establishes **business responsibility**, not process sequencing.

---

## Traceability Requirements

This decision establishes architectural traceability obligations for subsequent Business Enabling Capability, Feature, Function, Service and implementation modelling.

It does **not** establish those downstream elements or relationships in advance.

### TR-01 — CT3 Identity SHALL Be Preserved

Later traceability SHALL retain the three CT3 identities:

```text id="3sx0ms"
Clinical Safety Management
  Clinical Risk Identification
  Clinical Risk Mitigation Management
  Clinical Risk Response Management
```

A downstream element supporting multiple CT3s SHALL trace explicitly to each applicable capability rather than relying upon an ambiguous relationship to `Clinical Safety Management` as a whole.

---

### TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with the fixed Capability hierarchy:

```text id="lyd3o6"
CT1 → CT2 → CT3 → Feature
```

any Feature derived from Clinical Safety Management SHALL be framed by the CT3 whose business responsibility requires that Feature.

Features SHALL NOT be attached directly to `Clinical Safety Management` merely because their more precise CT3 allocation is inconvenient.

Where Feature responsibility genuinely spans more than one CT3, that relationship SHALL be represented explicitly rather than by weakening the CT3 boundaries.

---

### TR-03 — Monitoring Traceability SHALL Preserve the Evidence Boundary

Where monitoring or surveillance capabilities provide information used by Clinical Risk Identification, subsequent traceability SHOULD express the relationship as provision or consumption of evidence rather than treating monitoring as synonymous with Clinical Risk Identification.

Conceptually:

```text id="om4cps"
Monitoring Capability
       │
       │ evidence / observation
       ▼
Clinical Risk Identification
```

The specific Business Enabling Capability, Feature, Function, Service or component providing monitoring is intentionally unresolved by this decision.

---

### TR-04 — Mitigation Traceability SHALL Preserve Execution Boundaries

Where Clinical Risk Mitigation Management requires action through another capability, traceability SHALL distinguish:

```text id="j4ol52"
Clinical Risk Mitigation Management
        │
        │ mitigation requirement / direction
        ▼
Applicable Operational or Enabling Capability
        │
        ▼
Mitigation implemented
```

The existence of this relationship SHALL NOT transfer accountability for the underlying operational capability into Clinical Safety Management.

---

### TR-05 — Response Traceability SHALL Preserve External Capability Boundaries

Clinical Risk Response Management may coordinate or consume capabilities required to implement an active response.

Later traceability SHALL distinguish the **management of the clinical-risk response** from the capabilities through which particular response actions are performed.

This prevents response coordination from becoming an architectural justification for duplicating workforce, communication, workflow, clinical, operational or technology capabilities within Clinical Safety Management.

---

### TR-06 — Information Traceability SHALL Preserve Semantic State

Where subsequent Information Architecture represents clinical-risk information, it SHOULD preserve sufficient semantic distinction to determine whether information represents:

- evidence potentially indicative of clinical risk;
- an identified or characterised clinical risk;
- a mitigation associated with that risk;
- an active response associated with that risk; or
- an outcome or subsequent state resulting from mitigation or response.

This requirement does not prescribe a particular information model, FHIR resource, data structure or persistence mechanism.

---

### TR-07 — Implementation SHALL Not Redefine the Capability Model

A Harmonia component, service or implementation mechanism MAY support multiple Clinical Safety capabilities.

Such consolidation is an implementation decision and SHALL NOT be used retrospectively to redefine the Business Capability boundaries.

Traceability therefore proceeds:

```text id="86j4wp"
Business Capability
        ↓
Business Enabling Capability
        ↓
Feature
        ↓
Function / Service
        ↓
Application Responsibility
        ↓
Implementation
```

and not in reverse.

Where implementation convenience conflicts with the established Business Capability distinction, the Business Capability distinction remains authoritative unless explicitly reconsidered through architecture governance.

---

### TR-08 — Unmodelled Clinical Safety Scope SHALL Remain Explicitly Unmodelled

Absence of a Clinical Safety capability from the R1/R2 model SHALL NOT be interpreted as:

- evidence that the real-world capability does not exist;
- allocation of that responsibility to one of the three established CT3s;
- allocation of that responsibility to Harmonia; or
- permission for implementation to silently absorb the responsibility.

Where subsequent requirements encounter such a responsibility, it SHALL be returned to Business Architecture for explicit adjudication.

This preserves the architectural distinction between:

> **not required to be modelled yet**

and

> **modelled as belonging somewhere else.**

------------------------------------------------------------------------

## DN-BC-017 — Clinical Quality Management Decomposition

**Context:**

`Clinical Quality, Safety & Improvement → Clinical Quality Management`

The previous candidate decomposition identified:

- Clinical Quality Performance Monitoring
- Continuous Improvement

This was considered insufficiently precise.

Clinical Quality Management requires distinction between the criteria against which quality is considered, the evidence available to support consideration of quality, the review and assessment through which quality conclusions are established, and the reporting of those conclusions.

At the same time, Clinical Quality Management is a substantial specialist business domain whose complete decomposition is unnecessary for the Harmonia R1/R2 architecture.

## Working Decision

**Clinical Quality Management** decomposes for the Harmonia R1/R2 architectural scope into:

```text id="8uk9zj"
Clinical Quality Management
  Clinical Quality Criteria Management
  Clinical Quality Evidence Management
  Clinical Quality Review & Assessment Management
  Clinical Quality Reporting
```

This decomposition is intentionally non-exhaustive beyond the architectural needs of Harmonia R1/R2.

`Continuous Improvement` is not retained as a subordinate CT3 of Clinical Quality Management by this decision.

---

## CT3 Capability Boundaries

### Clinical Quality Criteria Management

Owns the ability to establish and manage the **criteria against which clinical quality may be reviewed, assessed or reported**.

This may include applicable:

- quality measures;
- indicators;
- expectations;
- thresholds;
- assessment criteria; and
- other governed definitions necessary to evaluate clinical quality.

It does **not** own:

- production or collection of quality evidence;
- performance of quality assessment;
- reporting of established quality findings;
- generic governance or assurance criteria; or
- the operational capabilities whose performance may subsequently be assessed.

**Boundary test:**

> If the concern is **what constitutes the applicable basis for assessing clinical quality**, it belongs here. If the concern is evidence against those criteria or the conclusions reached from that evidence, responsibility has crossed into another CT3.

---

### Clinical Quality Evidence Management

Owns the ability to establish and manage the **body of evidence used to support clinical-quality review and assessment**.

Evidence may originate from clinical information, operational activity, monitoring, audit, safety activity, outcome information, service-performance information or other applicable sources.

Clinical Quality Evidence Management does not necessarily own the capabilities through which that evidence was originally produced.

It does **not** own:

- definition of quality criteria;
- the source clinical or operational activity;
- technical monitoring capabilities;
- quality assessment itself; or
- reporting of established quality conclusions.

**Boundary test:**

> If the concern is **what evidence is available and managed for consideration of clinical quality**, it belongs here. If the concern is **what that evidence means when evaluated against applicable criteria**, it has crossed into Clinical Quality Review & Assessment Management.

---

### Clinical Quality Review & Assessment Management

Owns the ability to **review applicable clinical-quality evidence against applicable clinical-quality criteria and establish a clinical-quality assessment, finding or conclusion**.

This represents the evaluative responsibility within Clinical Quality Management.

It may consume criteria, evidence and other applicable information without assuming ownership of the capabilities through which those inputs were established.

It does **not** own:

- definition of the applicable criteria;
- production or management of source evidence;
- communication of established quality conclusions;
- implementation of subsequent improvement activity; or
- generic assurance activity outside the clinical-quality context.

**Boundary test:**

> If the concern is **what the applicable evidence demonstrates about clinical quality when considered against the applicable criteria**, it belongs here.

---

### Clinical Quality Reporting

Owns the ability to **communicate established clinical-quality measures, findings, assessments and applicable conclusions to authorised governance, clinical, operational or other consumers**.

It concerns reporting established clinical-quality information rather than establishing the underlying quality conclusion.

It does **not** own:

- establishment of quality criteria;
- collection or management of source evidence;
- performance of the quality assessment;
- implementation of improvement activity; or
- generic information-distribution capability.

**Boundary test:**

> If the concern is **communicating an established clinical-quality measure, finding, assessment or conclusion to an applicable consumer**, it belongs here. If the concern is **establishing that conclusion**, it belongs in Clinical Quality Review & Assessment Management.

---

## Important Semantic Distinctions

The following distinctions SHALL be preserved:

> **Clinical quality criteria ≠ clinical quality evidence.**

> **Clinical quality evidence ≠ clinical quality assessment.**

> **Clinical quality assessment ≠ clinical quality reporting.**

> **Evidence source ≠ management of that evidence for clinical-quality purposes.**

> **Clinical quality assessment ≠ subsequent improvement activity.**

These distinctions represent business responsibility boundaries and SHALL NOT be interpreted as establishing a mandatory workflow.

---

## Monitoring Boundary

**Clinical Quality Performance Monitoring is not established as a separate CT3 Capability by this decision.**

Monitoring may produce observations, measures or other evidence consumed by Clinical Quality Evidence Management.

The capability responsible for producing that monitoring evidence remains external to this decomposition unless subsequently established through explicit architectural adjudication.

Accordingly:

> **Monitoring clinical performance ≠ managing clinical-quality evidence ≠ assessing clinical quality.**

The provider of applicable monitoring capability is intentionally unresolved by this decision.

---

## Continuous Improvement Boundary

`Continuous Improvement` is not retained as a subordinate CT3 Capability of Clinical Quality Management.

Clinical-quality findings and assessments may identify opportunities or requirements for improvement, and subsequent improvement activity may consume those findings.

However:

> **Identification of a clinical-quality issue ≠ management of the improvement undertaken in response to that issue.**

Continuous Improvement may apply across clinical, operational, workforce, technology, administrative and other domains.

Its appropriate canonical placement, if required within the Harmonia Business Capability model, is not established by this decision.

This decision SHALL NOT be interpreted as asserting that Continuous Improvement is unnecessary or does not exist.

---

## R1/R2 Scope Boundary

Clinical Quality Management is a substantial specialist business domain.

The four CT3 Capabilities established here are **not asserted to constitute an exhaustive enterprise decomposition of Clinical Quality Management**.

They establish the distinctions presently required to:

- provide coherent Business Architecture context for Harmonia;
- support derivation of Harmonia responsibilities;
- preserve important information-semantic boundaries;
- support later Business Enabling Capability and Feature traceability; and
- prevent materially different quality-management responsibilities from being collapsed into a single capability.

Additional specialist Clinical Quality capabilities SHALL NOT be introduced merely to make the Business Capability model appear enterprise-complete.

Further decomposition requires evidence that the distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

Accordingly:

> **The existence of additional real-world Clinical Quality responsibilities does not create an obligation for Harmonia to model them for R1/R2.**

This is an intentional architecture scope boundary, not an assertion that no additional Clinical Quality capabilities exist.

---

## Decision Consequences

### DC-01 — Criteria, Evidence, Assessment and Reporting SHALL Remain Distinct

Subsequent architecture SHALL preserve the distinction between:

- establishing clinical-quality criteria;
- managing clinical-quality evidence;
- reviewing and assessing quality;
- reporting established quality findings and conclusions.

A downstream element MAY support more than one of these responsibilities, but implementation reuse SHALL NOT collapse their Business Capability semantics.

---

### DC-02 — Evidence Management SHALL Not Absorb Evidence Production

Clinical Quality Evidence Management may consume information generated through many other capabilities.

The fact that information becomes clinical-quality evidence does not transfer responsibility for its original production into Clinical Quality Management.

For example:

```text id="6h3tx5"
Clinical / Operational / Monitoring Activity
              │
              │ produces information
              ▼
Clinical Quality Evidence Management
              │
              │ provides evidence
              ▼
Clinical Quality Review & Assessment Management
```

The source capability remains authoritative for its own business responsibility.

---

### DC-03 — Assessment SHALL Not Be Reduced to Monitoring

Observation of a measure or threshold does not itself establish a clinical-quality assessment.

Automated or manual monitoring may provide evidence, but the architecture SHALL preserve the distinction between:

> **observing performance**

and

> **establishing what that performance means in the applicable clinical-quality context.**

---

### DC-04 — Reporting SHALL Not Establish the Conclusion It Reports

Clinical Quality Reporting communicates established quality information.

Publication, transmission, visualisation or presentation of a quality measure or conclusion SHALL NOT be treated as the architectural point at which that conclusion becomes established.

---

### DC-05 — Clinical Quality Management SHALL Not Absorb Continuous Improvement

Improvement activity arising from a clinical-quality assessment remains semantically distinct from the assessment itself.

Future architecture SHALL NOT expand Clinical Quality Review & Assessment Management or Clinical Quality Reporting merely to provide a convenient home for improvement planning, implementation or change management.

---

### DC-06 — R1/R2 Scope SHALL Remain Deliberately Bounded

The absence of additional specialist Clinical Quality capabilities from the R1/R2 model SHALL NOT be treated as evidence that those capabilities do not exist.

Likewise, future modelling SHALL NOT manufacture additional CT3s merely to create an apparently complete Clinical Quality taxonomy.

---

## Traceability Requirements

### TR-01 — CT3 Identity SHALL Be Preserved

Later traceability SHALL preserve:

```text id="1lv94a"
Clinical Quality Management
  Clinical Quality Criteria Management
  Clinical Quality Evidence Management
  Clinical Quality Review & Assessment Management
  Clinical Quality Reporting
```

Downstream relationships SHOULD identify the applicable CT3 rather than relying upon an ambiguous relationship to `Clinical Quality Management` as a whole.

---

### TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text id="3vx0o1"
CT1 → CT2 → CT3 → Feature
```

Features derived from Clinical Quality Management SHALL be framed by the applicable CT3.

A Feature SHALL NOT be attached directly to Clinical Quality Management merely because precise CT3 allocation is inconvenient.

---

### TR-03 — Criteria Traceability SHALL Preserve Authority

Where Clinical Quality Criteria Management consumes externally governed standards, obligations, definitions or criteria, traceability SHALL preserve the distinction between:

- the authority establishing the underlying requirement; and
- management of that requirement as applicable clinical-quality criteria.

Use of an external criterion does not transfer authority for that criterion to Clinical Quality Management.

---

### TR-04 — Evidence Traceability SHALL Preserve Provenance

Where information is used as Clinical Quality evidence, subsequent Information and Application Architecture SHOULD retain sufficient provenance to identify:

- the source of the evidence;
- the context in which it was established;
- the applicable quality purpose for which it is being used; and
- where material, the relationship between the evidence and applicable quality criteria or assessment.

This requirement does not prescribe a particular information model, persistence mechanism or technology.

---

### TR-05 — Assessment Traceability SHALL Preserve Criteria and Evidence Relationships

Where an established clinical-quality assessment is represented downstream, the architecture SHOULD permit traceability to the applicable:

```text id="yzmnfz"
Clinical Quality Criteria
        +
Clinical Quality Evidence
        ↓
Clinical Quality Assessment / Finding
```

This is a semantic traceability requirement and does not establish a mandatory runtime processing sequence.

---

### TR-06 — Reporting Traceability SHALL Preserve Assessment Authority

Where Clinical Quality Reporting communicates an assessment or finding, downstream architecture SHALL preserve the distinction between:

- the authoritative quality assessment/finding; and
- representations, reports, messages, dashboards or other mechanisms through which that information is communicated.

A reporting artefact SHALL NOT silently become the authoritative source of the underlying quality conclusion merely because it presents that conclusion.

---

### TR-07 — Improvement Activity SHALL Trace Separately

Where a clinical-quality assessment results in improvement activity, later traceability SHALL represent that improvement responsibility separately from Clinical Quality Management unless explicitly adjudicated otherwise.

Conceptually:

```text id="2jln6x"
Clinical Quality Review & Assessment
              │
              │ finding / improvement need
              ▼
External Improvement Capability
```

The identity and canonical placement of that improvement capability are not established by this decision.

---

### TR-08 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component, service or implementation mechanism MAY support multiple Clinical Quality capabilities.

Such consolidation SHALL NOT be used retrospectively to redefine the Business Capability model.

Traceability proceeds from established architectural responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

### TR-09 — Unmodelled Clinical Quality Scope SHALL Remain Explicitly Unmodelled

Absence of a specialist Clinical Quality responsibility from the R1/R2 Capability model SHALL NOT be interpreted as:

- evidence that the responsibility does not exist;
- allocation of that responsibility to one of the four established CT3s;
- allocation of that responsibility to Harmonia; or
- permission for implementation to silently absorb the responsibility.

Where a subsequent requirement exposes such a responsibility, it SHALL return to Business Architecture for explicit adjudication.

The architecture SHALL preserve the distinction between:

> **not presently modelled because Harmonia does not require the distinction**

and

> **modelled as the responsibility of an existing Capability.**

------------------------------------------------------------------------

## DN-BC-018 — Preventative Health Decomposition

**Context:**

`Community Health & Wellbeing → Preventative Health`

The previous candidate decomposition identified:

- Preventive Screening Management
- Immunisation Programme Management

This was considered an unsuitable decomposition axis because screening and immunisation represent **types of preventative-health programmes or interventions**, rather than a coherent set of peer Business Capabilities.

Once Preventative Health is decomposed, the model should represent the materially distinct business responsibilities involved in preventative-health activity rather than enumerate particular programme types.

## Working Decision

**Preventative Health** decomposes for the Harmonia R1/R2 architectural scope into:

```text
Preventative Health
  Preventative Health Problem Identification
  Preventative Health Program Management
  Preventative Health Education Services
  Preventative Health Program Monitoring
  Preventative Health Reporting
  Preventative Health Cohort Management
```

Screening, immunisation and similar preventative activities are not established as separate CT3 Capabilities merely because they represent recognisable types of preventative-health programme.

---

## CT3 Capability Boundaries

### Preventative Health Problem Identification

Owns the ability to **identify and characterise a preventative-health problem that warrants preventative action within a population, community or other applicable health context**.

The identified problem may arise from population-health information, surveillance, clinical evidence, research, service information or other applicable evidence.

It does **not** own:

- patient-level Clinical Determination;
- establishment or management of the target cohort;
- design or operation of the preventative-health programme;
- programme monitoring; or
- implementation of the underlying evidence-producing capabilities.

**Boundary test:**

> If the concern is **what preventative-health problem warrants organised preventative attention**, it belongs here.

An identified preventative-health problem may inform individual care, but identification of a population or community preventative-health problem does not itself establish a diagnosis, problem or clinical determination for an Individual.

---

### Preventative Health Program Management

Owns the ability to establish and manage an **organised preventative-health programme intended to address an identified preventative-health need or objective**.

This may include programmes involving:

- screening;
- immunisation;
- preventative intervention;
- education;
- outreach; or
- other organised preventative-health activity.

It does **not** imply that each type of programme constitutes a separate Business Capability.

It does **not** own:

- identification of the underlying preventative-health problem;
- management of the target cohort as a distinct responsibility;
- every operational capability through which programme activities are delivered;
- generic project/programme management; or
- the clinical capabilities exercised where programme activity results in individual care.

**Boundary test:**

> If the concern is **establishing and managing the organised preventative response to an identified health problem or objective**, it belongs here.

---

### Preventative Health Education Services

Owns the ability to provide **education intended to prevent illness, injury, deterioration or other adverse health outcomes and to promote preventative-health behaviour**.

Preventative-health education may form part of a broader preventative-health programme or may itself constitute the principal preventative intervention.

It does **not** own:

- generic communication capability;
- generic education or workforce training;
- clinical counselling undertaken as part of individual care;
- programme management merely because education forms part of a programme; or
- technical mechanisms used to distribute educational information.

**Boundary test:**

> If the business outcome being delivered is **preventative-health education intended to influence health knowledge, behaviour or preventative action**, it belongs here.

Accordingly:

> **Communicating information in support of a preventative programme ≠ delivering Preventative Health Education Services.**

The educational purpose is material to the capability boundary.

---

### Preventative Health Program Monitoring

Owns the ability to monitor the **operation, reach, participation, coverage, progress and applicable performance of a preventative-health programme**.

Unlike generic technical or clinical monitoring, the subject of this capability is specifically the preventative-health programme and its operation.

It may consume information produced through other operational, clinical, population-health or information capabilities.

It does **not** own:

- generic monitoring infrastructure;
- patient-level clinical monitoring;
- identification of the original preventative-health problem;
- management of programme cohorts; or
- establishment of conclusions that belong to a separate review, assurance or quality capability.

**Boundary test:**

> If the concern is **how the preventative-health programme is operating, progressing, reaching its intended population or performing against its applicable programme objectives**, it belongs here.

---

### Preventative Health Reporting

Owns the ability to **report established information concerning preventative-health problems, programmes, cohorts, activity, coverage, performance and applicable outcomes to authorised consumers**.

It does not establish the underlying facts merely by reporting them.

It does **not** own:

- source information production;
- programme monitoring;
- generic information distribution;
- clinical-quality assessment;
- population-health surveillance; or
- the technical mechanisms used to communicate the report.

**Boundary test:**

> If the concern is **communicating established preventative-health information to an applicable consumer for preventative-health purposes**, it belongs here.

Accordingly:

> **Preventative-health information established ≠ preventative-health information reported.**

---

### Preventative Health Cohort Management

Owns the ability to establish and manage the **population or cohort to which preventative-health activity or a preventative-health programme applies**.

This may include management of:

- cohort definition;
- eligibility or inclusion criteria;
- exclusion criteria;
- cohort membership;
- applicable stratification;
- entry and exit; and
- other context necessary to manage the target population.

It does **not** own:

- patient-level diagnosis or Clinical Determination;
- identification of the underlying preventative-health problem;
- generic identity management;
- delivery of the preventative intervention; or
- management of the programme itself.

**Boundary test:**

> If the concern is **which population or group is in scope for the preventative-health activity and how that population is managed as a cohort**, it belongs here.

The following distinction SHALL be preserved:

> **Population at risk ≠ defined preventative-health cohort ≠ individual clinical problem.**

---

## Programme-Type Boundary

Screening, immunisation and similar activities SHALL NOT automatically be modelled as CT3 Capabilities beneath Preventative Health.

For example:

```text
Immunisation Programme
        │
        ├── exercises Preventative Health Program Management
        ├── may require Preventative Health Cohort Management
        ├── may use Preventative Health Education Services
        ├── exercises Preventative Health Program Monitoring
        └── may produce Preventative Health Reporting
```

Likewise, a screening programme may exercise the same Business Capabilities while producing different activities, information and clinical consequences.

Accordingly:

> **Programme type ≠ Business Capability.**

A particular preventative-health programme type warrants separate Capability treatment only where future architectural evidence demonstrates a materially distinct business ability that cannot coherently be represented through the established CT3s.

---

## Important Semantic Distinctions

The decomposition SHALL preserve:

> **Preventative-health problem identified ≠ preventative-health cohort established.**

> **Cohort established ≠ preventative-health programme managed.**

> **Programme managed ≠ preventative-health intervention delivered.**

> **Programme activity ≠ programme monitoring.**

> **Programme monitoring ≠ preventative-health reporting.**

> **Preventative-health education ≠ generic communication.**

> **Population preventative-health problem ≠ Individual Clinical Determination.**

These are responsibility boundaries rather than mandatory process stages.

---

## R1/R2 Scope Boundary

Preventative Health is a substantial specialist health-service domain.

The six CT3 Capabilities established here are sufficient to provide the presently required Business Architecture context for Harmonia R1/R2.

They are not asserted to constitute an exhaustive enterprise decomposition of Preventative Health.

Additional specialist capabilities SHALL NOT be introduced merely to enumerate known preventative-health programmes, interventions or professional activities.

Further decomposition requires evidence that the distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

---

## Decision Consequences

### DC-01 — Programme Types SHALL Not Become Capabilities by Default

Screening, immunisation, outreach and other recognised programme types SHALL NOT automatically produce corresponding CT3 Capabilities.

Future modelling SHALL first determine whether the proposed capability represents a materially distinct business ability or merely a specialised exercise of the established Preventative Health capabilities.

---

### DC-02 — Cohort Management SHALL Remain Distinct from Individual Care

Management of a preventative-health cohort SHALL NOT be represented as management of a collection of Individual Clinical Determinations.

Cohort membership may be derived from individual information, but the cohort exists for a distinct preventative-health business purpose.

---

### DC-03 — Programme Management SHALL Not Absorb Delivery Capabilities

Preventative Health Program Management may coordinate activities performed through clinical, operational, communication, scheduling or other capabilities.

This does not transfer ownership of those capabilities into Preventative Health Program Management.

---

### DC-04 — Education SHALL Remain Purpose-Specific

Preventative Health Education Services SHALL be distinguished from generic information communication based upon the preventative-health purpose of the educational activity.

The use of a generic communication mechanism does not remove the Business Capability responsibility for providing preventative-health education.

---

### DC-05 — Program Monitoring SHALL Not Become Generic Monitoring

Preventative Health Program Monitoring is bounded to monitoring the operation and applicable performance of preventative-health programmes.

It SHALL NOT become a general monitoring capability for patients, clinical services, technology, population-health surveillance or enterprise performance.

---

### DC-06 — R1/R2 Scope SHALL Remain Deliberately Bounded

The model SHALL NOT attempt to capture the complete specialist Preventative Health domain unless subsequent Harmonia requirements demonstrate that additional distinctions are architecturally material.

---

## Traceability Requirements

### TR-01 — CT3 Identity SHALL Be Preserved

Subsequent traceability SHALL preserve:

```text
Preventative Health
  Preventative Health Problem Identification
  Preventative Health Program Management
  Preventative Health Education Services
  Preventative Health Program Monitoring
  Preventative Health Reporting
  Preventative Health Cohort Management
```

Downstream elements SHOULD trace to the applicable CT3 rather than ambiguously to Preventative Health as a whole.

---

### TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text
CT1 → CT2 → CT3 → Feature
```

Features derived from Preventative Health SHALL be framed by the applicable CT3.

Programme-specific implementation shall not be used to bypass this hierarchy.

---

### TR-03 — Problem Identification SHALL Preserve Evidence Provenance

Where Preventative Health Problem Identification consumes population, clinical, surveillance, research or other evidence, downstream architecture SHOULD preserve sufficient provenance to identify the applicable source and context.

Use of evidence for preventative-health purposes does not transfer authority for the source information to Preventative Health.

---

### TR-04 — Cohort Traceability SHALL Preserve Membership Reasoning

Where Harmonia represents or manages preventative-health cohort membership, subsequent architecture SHOULD preserve sufficient information to establish:

- the cohort concerned;
- applicable inclusion or eligibility criteria;
- applicable exclusions;
- the basis upon which membership was established; and
- where material, applicable entry, exit or stratification context.

This requirement does not prescribe a particular information model or implementation mechanism.

---

### TR-05 — Programme Traceability SHALL Not Become Programme-Specific Capability Duplication

Where screening, immunisation or another programme type requires Features, Functions or Services, those elements SHALL trace to the established Business Capabilities that provide their business context.

The existence of programme-specific implementation does not itself establish a new Business Capability.

---

### TR-06 — Monitoring and Reporting SHALL Remain Distinct

Where monitoring information subsequently appears in a report, traceability SHALL preserve the distinction between:

- information established through Preventative Health Program Monitoring; and
- the report or communication through which that information is provided to a consumer.

Reporting SHALL NOT silently become the authoritative source of the underlying programme state.

---

### TR-07 — External Delivery Capabilities SHALL Remain External

Where a preventative-health programme requires appointment management, clinical intervention, diagnostic services, medication services, communication, workflow or other capabilities, later traceability SHALL represent those relationships without absorbing those capabilities into Preventative Health Program Management.

---

### TR-08 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component or service MAY support multiple Preventative Health CT3s.

Implementation consolidation SHALL NOT retrospectively collapse the established Business Capability distinctions.

Architecture traceability proceeds from business responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

### TR-09 — Unmodelled Preventative Health Scope SHALL Remain Explicitly Unmodelled

Absence of a specialist Preventative Health responsibility from the R1/R2 model SHALL NOT imply:

- that the responsibility does not exist;
- that it belongs to one of the six established CT3s;
- that Harmonia owns it; or
- that implementation may silently absorb it.

Where a future requirement exposes a materially distinct responsibility, it SHALL return to Business Architecture for explicit adjudication.

## Terminology Convention

Harmonia uses Australian/British English terminology within the Business Capability model.

Accordingly, **Programme** is used where the term refers to an organised set of activities directed toward a business or health objective.

The canonical Capability names are therefore:

```text
Preventative Health
  Preventative Health Problem Identification
  Preventative Health Programme Management
  Preventative Health Education Services
  Preventative Health Programme Monitoring
  Preventative Health Reporting
  Preventative Health Cohort Management
```

`Program` SHALL NOT be used as an alternative spelling for `Programme` within Business Capability names or descriptions, except where referring to a technical/software program or where reproducing an externally defined term.

------------------------------------------------------------------------

## DN-BC-019 — Population Health Surveillance Decomposition

**Context:**

`Population Health & Health-System Planning → Population Health Surveillance`

The previous candidate decomposition identified:

- Notifiable Health Event Reporting
- Population Health Monitoring

This was considered insufficiently precise.

`Population Health Monitoring` combined several materially different responsibilities associated with longitudinal understanding of population health, management of surveillance activity, and identification of population-health events.

Likewise, `Notifiable Health Event Reporting` represented the communication aspect of a broader responsibility associated with management of formally notifiable health events.

The decomposition must provide sufficient Population Health context for Harmonia R1/R2 without attempting to model the complete specialist public-health or epidemiological domain.

## Working Decision

**Population Health Surveillance** decomposes for the Harmonia R1/R2 architectural scope into:

```text id="zh7x2w"
Population Health Surveillance
  Population Health Longitudinal Assessment
  Population Health Surveillance Management
  Population Health Event Identification
  Notifiable Health Event Management
  Population Health Reporting
```

These represent distinct business responsibilities and SHALL NOT be interpreted as mandatory stages in a surveillance workflow.

---

## CT3 Capability Boundaries

### Population Health Longitudinal Assessment

Owns the ability to establish and maintain an understanding of **population health state, patterns and change over time** using applicable population-level health evidence.

Its concern is the longitudinal assessment of health across a population rather than the health state of a particular Individual.

This may include identification and assessment of:

- trends;
- changes in health state;
- patterns across time;
- variation within or between populations;
- emerging changes in population-health characteristics; and
- other longitudinal population-health observations.

It does **not** own:

- Individual Health Monitoring;
- management of surveillance programmes or regimes;
- determination that an observed pattern constitutes a population-health event;
- management of formal notification obligations;
- Preventative Health Problem Identification; or
- generic data acquisition and information-management mechanisms.

**Boundary test:**

> If the concern is **what is happening to the health of a population over time**, it belongs here.

The following distinction SHALL be preserved:

> **Individual Health Monitoring ≠ Population Health Longitudinal Assessment.**

---

### Population Health Surveillance Management

Owns the ability to establish and manage **organised population-health surveillance activity directed toward defined population-health concerns, conditions, risks or other surveillance objectives**.

Its concern includes the managed surveillance context: what is being surveilled, for what population-health purpose, and under what applicable surveillance regime or arrangement.

It does **not** own:

- the underlying source systems or evidence-producing capabilities;
- longitudinal population-health assessment itself;
- determination that observed evidence constitutes a population-health event;
- management of formal notification obligations; or
- generic technical monitoring capability.

**Boundary test:**

> If the concern is **what population-health concerns are deliberately being surveilled and how that surveillance activity is managed**, it belongs here.

Accordingly:

> **Population-health evidence available ≠ organised population-health surveillance established.**

---

### Population Health Event Identification

Owns the ability to identify and characterise a **population-health event or pattern of sufficient significance to warrant recognition within the applicable population-health context**.

An event may be identified from longitudinal assessment, organised surveillance, notifications or other applicable evidence.

It does **not** own:

- management of the surveillance activity;
- management of statutory or formal notification obligations;
- patient-level Clinical Determination;
- preventative-health programme management; or
- the operational response to the identified population-health event.

**Boundary test:**

> If the concern is **whether available population-health evidence demonstrates a significant population-health event or pattern**, it belongs here.

Accordingly:

> **Observed population-health change ≠ identified population-health event.**

---

### Notifiable Health Event Management

Owns the ability to manage **population-health events for which formal notification obligations apply**.

Its responsibility extends beyond merely transmitting a report and may include management of:

- applicability of notification requirements;
- required notification information;
- notification status;
- applicable authority or recipient;
- submission or provision of the notification;
- correction or supplementation of notification information; and
- applicable follow-up associated with the notification obligation.

It does **not** own:

- identification of every population-health event;
- establishment of the underlying notification obligation;
- generic communication mechanisms;
- population-health reporting generally; or
- the operational response to the health event.

**Boundary test:**

> If the concern is **managing the formal notification responsibility associated with a notifiable health event**, it belongs here.

Accordingly:

> **Population-health event identified ≠ event determined to be notifiable ≠ notification obligation discharged.**

---

### Population Health Reporting

Owns the ability to **report established population-health assessments, surveillance information, events, trends and other applicable population-health findings to authorised consumers**.

Its responsibility is communication of established population-health information for population-health purposes.

It does **not** own:

- establishment of the underlying assessment or finding;
- surveillance management;
- event identification;
- management of formal notification obligations;
- generic information distribution; or
- Preventative Health Reporting.

**Boundary test:**

> If the concern is **communicating established population-health information to an applicable consumer for population-health purposes**, it belongs here.

Accordingly:

> **Population-health information established ≠ population-health information reported.**

---

## Boundary with Preventative Health

Population Health Surveillance and Preventative Health are related but semantically distinct.

**Population Health Longitudinal Assessment** establishes understanding of:

> **What is happening to the health of this population over time?**

**Population Health Event Identification** establishes:

> **Has a population-health event or significant pattern emerged?**

**Preventative Health Problem Identification** establishes:

> **What preventative-health problem warrants organised preventative action?**

Population Health capabilities may therefore provide evidence to Preventative Health without assuming responsibility for the preventative response.

Conceptually:

```text id="imn79h"
Population Health Longitudinal Assessment
                 │
                 ├──────┐
                 ▼      │
Population Health Event │
     Identification     │
                 │      │
                 └──┬───┘
                    │ evidence
                    ▼
Preventative Health Problem Identification
```

This relationship is semantic and SHALL NOT be interpreted as establishing mandatory runtime sequencing.

---

## Boundary with Individual Health

Population Health Surveillance operates at a population-health level.

Information concerning Individuals may contribute to population-health evidence, but this does not convert Population Health Surveillance into Individual Care Delivery.

The architecture SHALL preserve:

> **Individual health state ≠ population health state.**

> **Individual clinical finding ≠ population-health pattern.**

> **Individual Clinical Determination ≠ Population Health Event Identification.**

Likewise, identification of a population-health event does not establish a diagnosis or problem for every Individual associated with the relevant population.

---

## Important Semantic Distinctions

The decomposition SHALL preserve:

> **Population-health evidence ≠ Population Health Longitudinal Assessment.**

> **Longitudinal population-health change ≠ identified population-health event.**

> **Population-health surveillance ≠ generic monitoring.**

> **Population-health event identified ≠ event determined to be notifiable.**

> **Notifiable event established ≠ notification obligation discharged.**

> **Formal health-event notification ≠ general Population Health Reporting.**

> **Population-health problem or event ≠ preventative-health response.**

These are responsibility boundaries rather than mandatory process stages.

---

## R1/R2 Scope Boundary

Population Health Surveillance is a substantial specialist public-health and epidemiological domain.

The five CT3 Capabilities established here are not asserted to constitute an exhaustive enterprise decomposition of Population Health Surveillance.

They establish the distinctions presently required to:

- provide coherent Business Architecture context for Harmonia;
- support Harmonia information and interoperability responsibilities;
- preserve Individual versus population-health semantics;
- support applicable notification and reporting contexts; and
- support later Business Enabling Capability and Feature traceability.

Additional specialist surveillance, epidemiological, outbreak-management or public-health capabilities SHALL NOT be introduced merely to make the model appear enterprise-complete.

Further decomposition requires evidence that the distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

---

## Decision Consequences

### DC-01 — Population and Individual Health Semantics SHALL Remain Distinct

Subsequent architecture SHALL NOT treat aggregation of Individual health information as sufficient, by itself, to establish Population Health Longitudinal Assessment.

Likewise, a population-health conclusion SHALL NOT silently become an Individual Clinical Determination.

---

### DC-02 — Surveillance SHALL Not Become Generic Monitoring

Population Health Surveillance Management is bounded to organised surveillance undertaken for population-health purposes.

It SHALL NOT absorb generic monitoring, clinical monitoring, technical monitoring, programme monitoring or operational monitoring capabilities.

---

### DC-03 — Event Identification SHALL Remain Distinct from Observation

Detection of an unusual value, trend or pattern does not necessarily establish a Population Health Event.

Subsequent architecture SHALL preserve the distinction between:

```text id="al4x6a"
Observation / Evidence
        ↓
Longitudinal Assessment / Surveillance
        ↓
Population Health Event Identification
```

without requiring this as a mandatory processing sequence.

---

### DC-04 — Notification SHALL Remain Distinct from General Reporting

Notifiable Health Event Management represents management of a specific formal notification responsibility.

Population Health Reporting represents broader communication of established population-health information.

The existence of a generic reporting or communication mechanism SHALL NOT collapse these responsibilities.

---

### DC-05 — Population Health SHALL Not Absorb Preventative Health

Population-health findings may trigger or inform Preventative Health activity.

This relationship SHALL NOT transfer responsibility for Preventative Health Problem Identification, Cohort Management, Programme Management or intervention into Population Health Surveillance.

---

### DC-06 — R1/R2 Scope SHALL Remain Deliberately Bounded

The model SHALL NOT attempt to capture the complete public-health, epidemiological, outbreak-management or surveillance domain unless subsequent Harmonia requirements demonstrate that additional distinctions are architecturally material.

---

## Traceability Requirements

### TR-01 — CT3 Identity SHALL Be Preserved

Subsequent traceability SHALL preserve:

```text id="0e7u70"
Population Health Surveillance
  Population Health Longitudinal Assessment
  Population Health Surveillance Management
  Population Health Event Identification
  Notifiable Health Event Management
  Population Health Reporting
```

Downstream elements SHOULD trace to the applicable CT3 rather than ambiguously to Population Health Surveillance as a whole.

---

### TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text id="p9i2t2"
CT1 → CT2 → CT3 → Feature
```

Features derived from Population Health Surveillance SHALL be framed by the applicable CT3.

Features SHALL NOT be attached directly to Population Health Surveillance merely because precise CT3 allocation is inconvenient.

---

### TR-03 — Longitudinal Assessment SHALL Preserve Evidence Provenance

Where Population Health Longitudinal Assessment consumes clinical, operational, demographic, surveillance or other information, downstream architecture SHOULD preserve sufficient provenance to identify the applicable source and context.

Use of information in a population-health assessment does not transfer authority for the source information to Population Health Surveillance.

---

### TR-04 — Population Context SHALL Be Traceable

Where Harmonia represents a population-health assessment, surveillance activity or event, the architecture SHOULD preserve sufficient context to identify the population, cohort, geography, temporal period or other population boundary to which the information applies where materially relevant.

This requirement does not prescribe a particular information model or implementation mechanism.

---

### TR-05 — Event Identification SHALL Preserve Supporting Evidence

Where a Population Health Event is established, subsequent architecture SHOULD permit traceability to the applicable evidence, assessment or surveillance context supporting that identification.

This is a semantic traceability requirement and does not establish mandatory automated event-detection behaviour.

---

### TR-06 — Notifiable Event Traceability SHALL Preserve Obligation Context

Where a health event is managed as notifiable, downstream architecture SHOULD preserve sufficient information to establish:

- the event concerned;
- the applicable notification requirement;
- the authority or recipient to which notification applies;
- applicable notification status; and
- where material, the relationship between the notification and its supporting population-health evidence.

This decision does not establish the source or governance mechanism for notification obligations.

---

### TR-07 — Reporting SHALL Preserve Source Authority

Population Health Reporting SHALL preserve the distinction between an authoritative population-health assessment, event or finding and the report, message, dashboard or other representation through which it is communicated.

Reporting SHALL NOT silently become the authoritative source of the underlying population-health conclusion.

---

### TR-08 — Preventative Health Relationships SHALL Remain Explicit

Where Population Health information informs:

- Preventative Health Problem Identification;
- Preventative Health Cohort Management;
- Preventative Health Programme Management; or
- another Preventative Health capability,

later traceability SHALL represent that relationship explicitly rather than merging the Business Capabilities.

---

### TR-09 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component or service MAY support multiple Population Health Surveillance CT3s.

Implementation consolidation SHALL NOT retrospectively collapse the established Business Capability distinctions.

Architecture traceability proceeds from business responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

### TR-10 — Unmodelled Population Health Scope SHALL Remain Explicitly Unmodelled

Absence of a specialist Population Health responsibility from the R1/R2 model SHALL NOT imply:

- that the responsibility does not exist;
- that it belongs to one of the five established CT3s;
- that Harmonia owns it; or
- that implementation may silently absorb it.

Where a subsequent requirement exposes a materially distinct responsibility, it SHALL return to Business Architecture for explicit adjudication.

The architecture SHALL preserve the distinction between:

> **not presently modelled because Harmonia does not require the distinction**

and

> **modelled as the responsibility of an existing Capability.**

## Capability Relationship Notation Legend

The capability-to-capability relationship summaries in this Design Note use a small set of controlled relationship expressions.

These expressions describe **semantic relationships between Business Capabilities**. They identify what one Capability establishes or makes available for use within the responsibility of another Capability.

They do not, by themselves, establish runtime, process, application or implementation relationships.

| Relationship Expression | Meaning |
|---|---|
| **provides evidence to** | The source Capability establishes information, observations, findings or other evidence that may be considered by the target Capability in exercising its own responsibility. |
| **provides context to** | The source Capability establishes contextual information that may influence how the target Capability understands or exercises its responsibility, without establishing the target Capability's outcome. |
| **provides surveillance context to** | A specialised form of `provides context to` in which the source establishes the purpose, population, concern or other managed context within which surveillance-related activity occurs. |
| **provides identified event to** | The source Capability establishes the identity and relevant characterisation of an event that may subsequently be acted upon or managed by the target Capability. |
| **provides established information to** | The source Capability establishes information within its own responsibility that may subsequently be used, communicated or otherwise consumed by the target Capability. |
| **provides notification information to** | The source Capability establishes information concerning a formal notification responsibility, its execution or its state that may subsequently be used by the target Capability. |
| **provides population-health evidence to** | A specialised form of `provides evidence to` in which the evidence has been established within a population-health context. |
| **provides population characteristics to** | The source Capability establishes characteristics, patterns or other information concerning a population that may inform definition or management of a population or cohort by the target Capability. |
| **may provide context to** | The source Capability can provide relevant context to the target Capability, but such a relationship is conditional and SHALL NOT imply that the source is required for every exercise of the target Capability. |
| **consumes** | The target Capability uses an outcome, information, service or other established result provided through another Capability while retaining responsibility for its own business outcome. Where used, `consumes` SHALL NOT imply transfer of ownership or accountability. |

### Interpretation of Direction

Relationship notation is read from **source to target**:

```text id="5kw2tr"
Capability A
    │
    │ provides evidence to
    ▼
Capability B
```

means:

> **Capability A establishes evidence that Capability B may use while exercising Capability B's own responsibility.**

It does **not** mean that Capability A performs any part of Capability B.

Where the relationship is described from the consumer perspective:

```text id="g16md5"
Capability B
    │
    │ consumes
    ▼
Outcome provided through Capability A
```

the same separation of responsibility applies.

---

### Strength of Relationship

The notation deliberately distinguishes between an established semantic relationship and mandatory dependency.

Unless explicitly qualified otherwise:

**`provides … to`** means that the source Capability produces something semantically relevant to the target Capability.

It does not mean that every exercise of the source produces that outcome for the target, nor that every exercise of the target requires the source.

**`may provide … to`** makes this conditionality explicit where the relationship is particularly contextual or optional.

**`consumes`** means that, where the applicable relationship is exercised, the consuming Capability uses an externally established outcome while retaining its own business responsibility.

---

### Relationship Semantics Are Not Process Semantics

Capability relationships SHALL NOT be interpreted as process-flow notation.

For example:

```text id="3bfk9q"
Population Health Longitudinal Assessment
        │
        │ provides evidence to
        ▼
Population Health Event Identification
```

does **not** establish:

```text id="5xlhzj"
STEP 1: Perform Longitudinal Assessment
STEP 2: Perform Event Identification
```

It establishes only that longitudinal assessment may produce evidence relevant to event identification.

The Capabilities may be exercised independently, concurrently, repeatedly or within different business processes.

---

### Relationship Semantics Are Not Structural Semantics

The relationship notation SHALL NOT establish Capability decomposition, ancestry or specialisation.

For example:

```text id="e5i1w6"
Population Health Event Identification
        │
        │ provides identified event to
        ▼
Notifiable Health Event Management
```

does not make Notifiable Health Event Management a child, subtype or component of Population Health Event Identification.

Capability structure remains established separately through the governed:

```text id="dnmnvp"
CT1 → CT2 → CT3
```

Capability hierarchy.

---

### Relationship Semantics Are Not Implementation Semantics

The notation SHALL NOT, without subsequent architectural derivation, be interpreted as establishing:

```text id="dppz8m"
Business Capability Relationship
        ≠
Application Dependency
        ≠
Service Invocation
        ≠
API Call
        ≠
Message Flow
        ≠
Event Subscription
        ≠
Database Relationship
        ≠
Runtime Sequence
```

A later architecture may implement a capability relationship using one or more of these mechanisms, but the implementation mechanism is not determined by the Business Capability relationship.

---

### Information Authority Is Preserved

Where Capability A provides information, evidence or context to Capability B:

> **Use by Capability B does not transfer authority for the source information from Capability A to Capability B.**

Capability B may establish a new business conclusion within its own responsibility using that information.

For example:

```text id="qovl09"
Population Health Longitudinal Assessment
        │
        │ provides population-health evidence to
        ▼
Preventative Health Problem Identification
```

means that Population Health Longitudinal Assessment remains authoritative for the population-health assessment it establishes.

Preventative Health Problem Identification may use that evidence to establish a separate preventative-health conclusion.

Accordingly:

> **Information consumed ≠ information ownership transferred.**

> **Evidence consumed ≠ source conclusion re-authored.**

> **Context provided ≠ target outcome established.**

---

### RADS Relationship Guardrail

The expressions in this Design Note are **Business Architecture semantic notation**, not automatically RADS relationship types.

When these relationships are subsequently represented within RADS:

1. the applicable governed RADS relationship type SHALL be selected;
2. the semantic meaning established by this Design Note SHALL be preserved;
3. no stronger dependency SHALL be inferred merely from the directional notation used here; and
4. where no existing RADS relationship type accurately preserves the required semantics, the relationship SHALL remain unresolved until the metamodel is explicitly adjudicated.

Accordingly:

> **A convenient available relationship type SHALL NOT be used where doing so changes the architectural meaning of the relationship.**

## Capability-to-Capability Relationship Summary

The CT3 Capabilities within Population Health Surveillance are semantically related but remain independently accountable Business Capabilities.

The following relationships describe the principal **business-information and responsibility relationships** established by this decision. They SHALL NOT be interpreted as mandatory workflow sequencing, runtime invocation, system integration, or implementation dependency.

### Internal Population Health Surveillance Relationships


| Source Capability | Relationship | Target Capability | Meaning |
|---|---|---|---|
| Population Health Longitudinal Assessment | **provides evidence to** | Population Health Event Identification | Longitudinal patterns, trends and changes may provide evidence from which a significant population-health event is identified. |
| Population Health Surveillance Management | **provides surveillance context to** | Population Health Longitudinal Assessment | Managed surveillance establishes population-health concerns and contexts within which longitudinal assessment may be undertaken. |
| Population Health Surveillance Management | **provides surveillance context to** | Population Health Event Identification | An identified event may be interpreted within the purpose, population and concern established by organised surveillance. |
| Population Health Event Identification | **provides identified event to** | Notifiable Health Event Management | An identified population-health event may subsequently be determined to fall within an applicable formal notification obligation. |
| Population Health Longitudinal Assessment | **provides established population-health information to** | Population Health Reporting | Established longitudinal population-health assessments may be communicated through Population Health Reporting. |
| Population Health Surveillance Management | **provides surveillance information to** | Population Health Reporting | Established information concerning surveillance activity may be communicated through Population Health Reporting. |
| Population Health Event Identification | **provides established event information to** | Population Health Reporting | Identified population-health events may be communicated through broader Population Health Reporting independently of formal notification obligations. |
| Notifiable Health Event Management | **provides notification information to** | Population Health Reporting | Information concerning notifiable events and notification activity may contribute to broader population-health reporting where applicable. |

These relationships do not require every instance of the source Capability to result in exercise of the target Capability.

For example:

> A longitudinal assessment does not necessarily identify a population-health event.

> An identified population-health event is not necessarily notifiable.

> A notifiable event may require formal notification independently of whether it appears in broader Population Health Reporting.

---

### Relationship to Preventative Health

Population Health Surveillance may establish evidence that informs Preventative Health responsibilities without assuming ownership of the preventative response.

| Source Capability | Relationship | Target Capability | Meaning |
|---|---|---|---|
| Population Health Longitudinal Assessment | **provides population-health evidence to** | Preventative Health Problem Identification | Population trends and changes may provide evidence that a preventative-health problem exists or is emerging. |
| Population Health Event Identification | **provides identified population-health concern to** | Preventative Health Problem Identification | An identified event may establish evidence requiring consideration as a preventative-health problem. |
| Population Health Longitudinal Assessment | **provides population characteristics to** | Preventative Health Cohort Management | Population assessment may provide evidence useful in defining, stratifying or reviewing a preventative-health cohort. |
| Population Health Event Identification | **may provide context to** | Preventative Health Cohort Management | The characteristics of an identified event may contribute to determining the population requiring preventative attention. |

The principal boundary remains:

```text id="gndcmh"
Population Health Surveillance
        │
        │ establishes population-health evidence
        ▼
Preventative Health
        │
        │ establishes and manages preventative response
        ▼
Preventative activity
```

Accordingly:

> **Evidence that preventative action may be required ≠ establishment or management of that preventative action.**

---

### Relationship to Individual Care Delivery

Individual-level health information may contribute evidence to Population Health Surveillance, while population-health findings may subsequently provide context relevant to Individual Care Delivery.

Neither direction transfers responsibility between the capability domains.

Conceptually:

```text id="e8cfln"
Individual Care Delivery
        │
        │ individual health information
        ▼
Population Health Surveillance
        │
        │ population-health knowledge / context
        ▼
Individual Care Delivery
```

This SHALL NOT be interpreted as a circular workflow.

It represents distinct uses of information under different business responsibilities.

In particular:

> **Individual observations may contribute to population-health evidence without becoming population-health conclusions.**

and:

> **Population-health findings may inform individual care without becoming Individual Clinical Determinations.**

---

### Relationship to Preventative Health Programme Monitoring

**Population Health Longitudinal Assessment** and **Preventative Health Programme Monitoring** SHALL remain distinct.

```text id="d8k4vl"
Population Health Longitudinal Assessment
    = What is happening to the health of the population over time?

Preventative Health Programme Monitoring
    = How is the preventative-health programme operating,
      reaching its population and performing?
```

A preventative-health programme may affect population-health outcomes observed through Population Health Longitudinal Assessment.

Likewise, population-health assessment may provide context for evaluating the continuing relevance of a preventative-health programme.

Neither capability substitutes for the other.

---

### Relationship to Clinical Quality and Clinical Safety

Population Health Surveillance information may provide evidence relevant to **Clinical Quality Management** or **Clinical Safety Management** where population-level patterns expose concerns relevant to those domains.

Likewise, information established through Clinical Quality or Clinical Safety activities may contribute evidence to Population Health Surveillance.

Such information relationships SHALL NOT collapse the capability boundaries.

For example:

> **A population pattern suggesting a clinical safety concern ≠ Clinical Risk Identification.**

Clinical Risk Identification remains responsible for establishing the clinical-safety risk within its applicable business context.

Similarly:

> **Population-health evidence concerning service outcomes ≠ Clinical Quality Review & Assessment.**

Clinical Quality Review & Assessment Management remains responsible for establishing the applicable clinical-quality assessment.

---

## Relationship Interpretation Guardrail

The relationships established above describe **semantic business relationships between independently accountable Capabilities**.

Unless subsequently established through explicit architectural adjudication, a relationship in this summary SHALL NOT be interpreted as:

- a mandatory invocation;
- a mandatory workflow transition;
- temporal sequencing;
- a runtime dependency;
- a system interface;
- a Feature relationship;
- a Function relationship;
- a Service relationship;
- an application-component dependency;
- an information ownership transfer; or
- evidence that one Capability is subordinate to another.

In particular:

> **Capability A provides information or context to Capability B**

does not mean:

> **Capability A performs Capability B**

and does not mean:

> **Capability B cannot operate without Capability A.**

Any later RADS relationship derived from these Business Capability relationships SHALL preserve the semantic meaning established here and SHALL use the applicable governed relationship type rather than inferring implementation dependency from this summary.

------------------------------------------------------------------------

# DN-BC-020 — Health-System Planning Decomposition

## Context

`Population Health & Health-System Planning → Health-System Planning`

Health-System Planning represents the business ability to assess expected health-service demand and determine the capacity and distribution of health services required to respond to that demand.

For Harmonia R1/R2, the architecture requires sufficient decomposition to distinguish:

- assessment of health-service demand;
- planning of required health-service capacity; and
- planning of the distribution of that capacity across the health-service footprint.

A more extensive enterprise decomposition of health-system strategy, infrastructure planning, commissioning, funding, workforce planning or service-network planning is not required for Harmonia R1/R2.

The decomposition therefore applies the established MVP stopping test:

> **Would distinguishing additional capabilities cause Harmonia to behave, govern, represent, exchange, secure, coordinate or trace something materially differently?**

Where the answer is no, further decomposition is not presently required.

---

## Working Decision

**Health-System Planning** decomposes for Harmonia R1/R2 into:

```text id="5e5bx6"
Health-System Planning
  Health Service Demand Assessment
  Health Service Capacity Planning
  Health Service Distribution Planning
```

These are distinct Business Capabilities.

They SHALL NOT be interpreted as mandatory stages in a planning process or workflow.

---

## CT3 Capability Boundaries

### Health Service Demand Assessment

Owns the ability to establish and assess **current and expected demand for health services across an applicable population, geography, service domain or health-system context**.

Its concern is the nature, scale, distribution and expected evolution of demand for health-service capability.

Applicable evidence may include:

- population-health information;
- demographic information;
- service utilisation;
- epidemiological information;
- historical demand;
- projected population or health changes;
- unmet or emerging demand; and
- other applicable health-system planning evidence.

It does **not** own:

- Individual care demand;
- referral management;
- appointment or scheduling demand;
- current operational workload or queues;
- operational care capacity;
- establishment of population-health findings; or
- determination of the health-service capacity required to address the assessed demand.

**Boundary test:**

> If the concern is **what health-service demand exists or is expected across the applicable population or health-system context**, it belongs here.

Accordingly:

> **Individual demand for care ≠ health-service demand across a population or health system.**

---

### Health Service Capacity Planning

Owns the ability to determine **what health-service capacity should exist to address assessed current and expected health-service demand**.

Its concern is planned capacity at a health-system or service-planning level rather than the management of immediately usable operational care capacity.

Capacity may concern the scale, type or mix of health-service capability required to respond to assessed demand.

It does **not** own:

- assessment of the underlying health-service demand;
- management of current operational care capacity;
- Care-Place Readiness Management;
- Care Resource Deployment;
- workforce roster management;
- individual appointment or care-place allocation;
- ownership or management of the underlying resources; or
- determination of where planned capacity should be distributed.

**Boundary test:**

> If the concern is **what health-service capacity needs to exist to address assessed demand**, it belongs here.

The following distinction SHALL be preserved:

> **Health Service Capacity Planning ≠ Operational Care Capacity Management.**

Health Service Capacity Planning concerns **required health-system/service capacity**.

Operational Care Capacity Management concerns **usable operational capacity within the care-delivery environment**.

Accordingly:

> **Capacity required ≠ capacity established ≠ capacity operationally available.**

---

### Health Service Distribution Planning

Owns the ability to determine **how planned health-service capacity should be distributed across the applicable health-service footprint**.

Its concern may include the geographical, organisational, site, service-network or other applicable distribution of planned health-service capacity.

It does **not** own:

- definition of the health service itself;
- management of healthcare organisations or locations;
- operational deployment of resources;
- Care-Place Allocation;
- Care-Place Readiness Management;
- workforce deployment;
- management of current operational capacity; or
- physical implementation of the planned service distribution.

**Boundary test:**

> If the concern is **where and through what health-service footprint planned capacity should be made available**, it belongs here.

Accordingly:

> **Planned distribution of service capacity ≠ operational deployment of care resources.**

---

## Important Semantic Distinctions

The decomposition SHALL preserve:

> **Health-service demand ≠ individual demand for care.**

> **Demand assessed ≠ capacity required.**

> **Capacity required ≠ capacity operationally available.**

> **Capacity planned ≠ capacity deployed.**

> **Service distribution planned ≠ healthcare location managed.**

> **Service distribution planned ≠ resource deployed.**

> **Strategic or tactical health-service capacity ≠ operational care capacity.**

These are business-responsibility boundaries rather than mandatory process stages.

---

## Relationship to Population Health Surveillance

Population Health Surveillance may provide evidence used in Health-System Planning.

In particular:

**Population Health Longitudinal Assessment** may provide evidence concerning population-health state, trends and change relevant to Health Service Demand Assessment.

**Population Health Event Identification** may identify emerging or changed population-health circumstances that materially affect expected health-service demand.

The relationship does not transfer responsibility.

Population Health Surveillance remains responsible for establishing the applicable population-health assessment or event.

Health Service Demand Assessment remains responsible for determining what that evidence means for health-service demand.

Accordingly:

> **Population-health change identified ≠ health-service demand established.**

---

## Relationship to Operational Care Capacity Management

Health Service Capacity Planning and Operational Care Capacity Management operate at different levels of business responsibility.

```text id="07f0tv"
Health Service Capacity Planning
    = What health-service capacity needs to exist?

Operational Care Capacity Management
    = What usable care capacity is operationally available?
```

Health Service Capacity Planning may establish planning expectations or capacity requirements that provide context for downstream operational capabilities.

Operational Care Capacity Management may provide operational evidence that informs future planning.

Neither Capability substitutes for the other.

In particular:

> **A planned capacity requirement does not establish current operational capacity.**

and:

> **Current operational capacity does not, by itself, establish the appropriate future health-system capacity requirement.**

---

## Relationship to Healthcare Service Offering Management

Health Service Distribution Planning may determine where particular categories or forms of health-service capacity should be available.

This SHALL remain distinct from **Healthcare Service Offering Management**, which establishes and manages the identifiable health services an organisation offers.

Accordingly:

> **Planning that a service capability should exist within a location or service footprint ≠ establishing and managing the identifiable Healthcare Service Offering.**

The capabilities may inform one another without transferring responsibility.

---

## R1/R2 Scope Boundary

Health-System Planning is a substantial enterprise and health-system management domain.

The three CT3 Capabilities established here are **not asserted to constitute an exhaustive decomposition of health-system planning**.

They establish the distinctions required for Harmonia R1/R2 to represent:

- health-service demand;
- planned health-service capacity;
- distribution of planned service capacity;
- population-health evidence relevant to service planning; and
- the distinction between planned and operational capacity.

Additional decomposition covering matters such as:

- infrastructure planning;
- capital planning;
- detailed workforce planning;
- service commissioning;
- funding allocation;
- organisational planning;
- detailed service-network design; or
- broader health-system strategy

SHALL NOT be introduced merely to make the Business Capability model appear enterprise-complete.

Such decomposition requires evidence that the distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

---

## Decision Consequences

### DC-01 — Strategic/Tactical Capacity SHALL Remain Distinct from Operational Capacity

Subsequent architecture SHALL preserve the distinction between:

```text id="7cjwlx"
capacity required
capacity planned
capacity established
capacity operationally available
capacity operationally allocated
```

These states SHALL NOT be collapsed merely because an implementation represents several of them using related information.

---

### DC-02 — Demand Assessment SHALL Not Become Individual Care Demand Management

Health Service Demand Assessment operates at the health-system/service-planning level.

It SHALL NOT absorb:

- referrals;
- appointments;
- individual service requests;
- current care queues;
- individual care requirements; or
- other care-recipient-specific operational demand.

---

### DC-03 — Capacity Planning SHALL Not Own Underlying Resources

Health Service Capacity Planning may determine that particular capacity is required.

This does not transfer ownership of:

- workforce;
- facilities;
- care places;
- Clinical Devices;
- Health Products;
- ICT;
- supporting services; or
- other resources

to Health-System Planning.

---

### DC-04 — Distribution Planning SHALL Not Become Operational Deployment

Health Service Distribution Planning determines the intended distribution of service capacity.

It SHALL NOT absorb operational:

- Care Resource Deployment;
- Care-Place Allocation;
- workforce allocation;
- device deployment;
- material logistics; or
- work allocation and dispatch.

---

### DC-05 — Population Health Evidence SHALL Not Predetermine Planning Conclusions

Population Health Surveillance may provide evidence to Health-System Planning.

The planning Capability remains responsible for establishing the applicable demand, capacity or distribution conclusion.

Accordingly:

> **Evidence supplied ≠ planning conclusion established.**

---

### DC-06 — R1/R2 Scope SHALL Remain Deliberately Bounded

Further decomposition SHALL NOT be undertaken solely because additional planning disciplines exist within a healthcare enterprise.

Scope determines the required decomposition depth while preserving coherent peer semantics.

---

## Capability-to-Capability Relationship Summary

Relationship expressions in this section use the capability relationship semantics established by **DN-BC-019**.

They describe semantic Business Capability relationships and SHALL NOT, by themselves, establish workflow sequencing, runtime dependency, system integration or implementation behaviour.

| Source Capability | Relationship | Target Capability | Meaning |
|---|---|---|---|
| Population Health Longitudinal Assessment | **provides population-health evidence to** | Health Service Demand Assessment | Population-health state, patterns and longitudinal change may provide evidence relevant to assessment of current and expected health-service demand. |
| Population Health Event Identification | **provides evidence to** | Health Service Demand Assessment | An identified population-health event may materially affect expected health-service demand. |
| Health Service Demand Assessment | **provides demand evidence to** | Health Service Capacity Planning | Established health-service demand provides evidence from which required health-service capacity may be planned. |
| Health Service Capacity Planning | **provides planned capacity context to** | Health Service Distribution Planning | Established capacity requirements provide context for determining how required service capacity should be distributed. |
| Operational Care Capacity Management | **may provide evidence to** | Health Service Demand Assessment | Operational capacity pressures and utilisation may contribute evidence relevant to understanding health-service demand. |
| Operational Care Capacity Management | **may provide evidence to** | Health Service Capacity Planning | Operational capacity experience may contribute evidence to future health-service capacity planning. |
| Health Service Distribution Planning | **may provide context to** | Healthcare Service Offering Management | Planned service distribution may provide context for decisions concerning identifiable Healthcare Service Offerings. |

These relationships do not establish a mandatory:

```text id="iqxldm"
Demand
  ↓
Capacity
  ↓
Distribution
```

planning workflow.

The three Capabilities may be exercised iteratively, concurrently and recursively as planning assumptions and evidence change.

---

## Traceability Requirements

### TR-01 — CT3 Identity SHALL Be Preserved

Subsequent traceability SHALL preserve:

```text id="phq69k"
Health-System Planning
  Health Service Demand Assessment
  Health Service Capacity Planning
  Health Service Distribution Planning
```

Downstream architectural elements SHOULD trace to the applicable CT3 rather than ambiguously to Health-System Planning as a whole.

---

### TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text id="69ik48"
CT1 → CT2 → CT3 → Feature
```

Features derived from Health-System Planning SHALL be framed by the applicable CT3.

A Feature SHALL NOT be attached directly to Health-System Planning merely because allocation to the appropriate CT3 is inconvenient.

---

### TR-03 — Demand Assessment SHALL Preserve Evidence Context

Where Health Service Demand Assessment consumes population-health, demographic, operational, utilisation or other planning evidence, downstream architecture SHOULD preserve sufficient provenance and context to identify the basis upon which the demand assessment was established.

Use of evidence does not transfer authority for its source information.

---

### TR-04 — Capacity Planning SHALL Trace to Applicable Demand

Where Harmonia represents a planned health-service capacity requirement, the architecture SHOULD permit traceability to the applicable demand assessment or other planning basis supporting that requirement.

This does not require a one-to-one relationship between a demand assessment and a capacity-planning outcome.

---

### TR-05 — Distribution Planning SHALL Trace to Applicable Capacity Context

Where Harmonia represents planned distribution of health-service capacity, the architecture SHOULD preserve sufficient context to identify the capacity requirement, population, geography, service domain or other planning context to which that distribution applies where materially relevant.

---

### TR-06 — Planned and Operational Capacity SHALL Remain Distinguishable

Information architecture and subsequent implementation SHALL preserve sufficient semantics to distinguish:

- planned capacity;
- established capacity;
- operationally available capacity; and
- allocated or deployed operational capacity

where these states are represented by Harmonia.

One SHALL NOT silently substitute for another.

---

### TR-07 — Population Health Evidence Relationships SHALL Remain Explicit

Where Population Health Surveillance informs Health-System Planning, subsequent traceability SHALL preserve the relationship between the established population-health information and the planning conclusion derived from it.

The planning conclusion SHALL NOT overwrite or redefine the authoritative population-health assessment.

---

### TR-08 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component or service MAY support more than one Health-System Planning CT3.

Implementation consolidation SHALL NOT retrospectively collapse:

- demand assessment;
- capacity planning; and
- distribution planning

into a single Business Capability.

Architecture traceability proceeds from business responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

### TR-09 — Unmodelled Health-System Planning Scope SHALL Remain Explicitly Unmodelled

Absence of a health-system planning responsibility from the R1/R2 decomposition SHALL NOT imply:

- that the responsibility does not exist;
- that it belongs to one of the three established CT3s;
- that Harmonia owns it; or
- that implementation may silently absorb it.

Where a subsequent Harmonia requirement exposes a materially distinct planning responsibility, that responsibility SHALL return to Business Architecture for explicit adjudication.

The architecture SHALL preserve the distinction between:

> **not presently modelled because Harmonia does not require the distinction**

and

> **modelled as the responsibility of an existing Capability.**

------------------------------------------------------------------------

# DN-BC-021 — Health Information Management Decomposition

## Context

`Health Information & Knowledge Management → Health Information Management`

Health Information Management is a Harmonia-Core Business Capability concerned with the management of health information as governed, semantically meaningful information.

The previous candidate decomposition identified:

- Clinical Record Management; and
- Health Information Discovery & Retrieval.

This was considered insufficient for Harmonia.

`Clinical Record Management` conflated management of health-information **content** with management of the **structures and semantic bindings through which that content acquires unambiguous meaning**.

Likewise, `Health Information Discovery & Retrieval` conflated catalogue/index responsibilities with retrieval of identified health information.

Harmonia additionally requires explicit business-capability distinctions for:

- health-information lifecycle;
- catalogue and index management;
- compartmentalisation;
- correlation; and
- retrieval.

These distinctions are architecturally material to Harmonia and therefore warrant explicit CT3 representation.

---

## Working Decision

**Health Information Management** decomposes for Harmonia R1/R2 into:

```text id="qmbk3n"
Health Information Management
  Health Information Data Management
  Health Information Structure Management
  Health Information Lifecycle Management
  Health Information Catalogue and Index Management
  Health Information Compartmentalisation
  Health Information Data Correlation
  Health Information Data Retrieval
```

Historical/conceptual aliases may be understood as:

```text id="qhy6re"
Health Information Data Management
    ≈ Clinical Record Management — Content

Health Information Structure Management
    ≈ Clinical Record Management — Model
```

These aliases are explanatory only and SHALL NOT replace the canonical Capability names.

---

# CT3 Capability Boundaries

## Health Information Data Management

Owns the ability to manage the **actual health-information content** represented and maintained within the health-information environment.

Its concern is the managed instance information: clinical and health facts, assertions, observations, relationships and other health-information content established through applicable business activity.

Health Information Data Management manages that content according to applicable governed health-information structures.

It does not independently establish the structural or semantic definitions through which the content acquires meaning.

It does **not** own:

- information-model definition;
- terminology or value-set governance;
- structural definition;
- semantic binding definition;
- access authorisation;
- communication of health information;
- clinical interpretation;
- catalogue/index management; or
- information retrieval.

**Boundary test:**

> If the concern is **managing the actual health-information content**, it belongs here.

Accordingly:

> **Health-information content ≠ the structure through which that content is interpreted.**

---

## Health Information Structure Management

Owns the ability to manage the **information structures and semantic bindings through which health-information content acquires consistent and unambiguous meaning**.

Its concern includes applicable:

- information structures and models;
- element definitions;
- relationships between information elements;
- datatypes;
- cardinality constraints;
- reference definitions;
- permitted reference targets;
- terminology bindings;
- value-set bindings;
- structural constraints;
- semantic constraints expressed through the information structure; and
- other definitional references and linkages necessary to interpret managed health information.

Health Information Structure Management therefore concerns more than physical or syntactic schema.

It establishes and manages the structural and semantic context required for health-information content to be interpreted unambiguously.

Conceptually:

```text id="3yb86a"
Health Information Structure
  ├─ Element Definition
  ├─ Datatype
  ├─ Cardinality
  ├─ Reference Definition
  ├─ Reference Target
  ├─ Terminology Binding
  │     └─ Value Set Reference
  ├─ Structural Constraint
  └─ Semantic Constraint
```

It does **not** necessarily own the semantic artefacts to which those structures refer.

For example, the Capability may establish that:

```text id="f5p28l"
Information Element X
    binds to
Value Set Y
```

without assuming responsibility for governance of `Value Set Y` itself.

That authority remains with the applicable Standards, Semantics & Reference Governance capability.

**Boundary test:**

> If the concern is **what structure, references, constraints and semantic bindings are necessary for health-information content to have unambiguous meaning**, it belongs here.

Accordingly:

> **Information structure ≠ semantic authority.**

and:

> **Semantic binding ≠ ownership of the semantic artefact being bound.**

---

## Content and Structure Relationship

Health Information Data Management and Health Information Structure Management are distinct but strongly related Capabilities.

The fundamental relationship is:

```text id="k7pxk8"
Health Information Structure Management
        │
        │ provides semantic structure to
        ▼
Health Information Data Management
```

Health Information Data Management manages content **within the meaning established through the applicable Health Information Structure**.

Accordingly:

> **A stored value does not acquire unambiguous health-information meaning merely because it has been persisted.**

Its meaning depends upon the applicable information structure and the references, relationships, constraints and semantic bindings associated with that structure.

For example:

```text id="ymh7vv"
"value = 120"
```

does not, by itself, establish whether `120` represents:

- systolic blood pressure;
- heart rate;
- body weight;
- a score;
- a quantity;
- a coded concept;
- or something else entirely.

Meaning arises from the governed information context within which the value occurs.

The architecture SHALL therefore preserve:

> **Data value ≠ information meaning.**

> **Information content ≠ information structure.**

> **Information structure provides meaning to content without becoming the content.**

---

## Relationship to Standards, Semantics & Reference Governance

The architecture SHALL distinguish:

```text id="3p3mh5"
Standards / Semantics / Reference Governance
        │
        │ establishes governed semantic artefacts
        ▼
Health Information Structure Management
        │
        │ binds and references those artefacts
        │ within health-information structures
        ▼
Health Information Data Management
        │
        │ manages content according to
        │ those structures
        ▼
Semantically interpretable health information
```

This is a semantic relationship and SHALL NOT be interpreted as a mandatory workflow or runtime sequence.

The distinction can be expressed as:

> **Semantic authority ≠ semantic binding ≠ information content.**

For example:

- Terminology Governance may govern a terminology or Value Set.
- Health Information Structure Management may bind an information element to that Value Set.
- Health Information Data Management may manage an instance containing a code conforming to that binding.

These are distinct responsibilities.

---

## Health Information Lifecycle Management

Owns the ability to manage health information through its applicable **information lifecycle**, while preserving the required integrity, provenance, status and historical meaning of that information.

Its concern may include applicable lifecycle states and transitions associated with:

- creation or establishment;
- amendment;
- correction;
- supersession;
- versioning;
- currency;
- historical retention;
- archival;
- withdrawal;
- logical deletion or disposition; and
- other governed health-information lifecycle states.

This Capability does not imply that Health Information Management originates every item of health information.

The business activity responsible for establishing a clinical or health fact remains responsible for that fact.

Health Information Lifecycle Management governs the lifecycle of the resulting information once it exists within the health-information environment.

**Boundary test:**

> If the concern is **the governed state and history of health information over time**, it belongs here.

Accordingly:

> **Current truth ≠ historical truth.**

> **Information amended ≠ previous information never existed.**

> **Information superseded ≠ information provenance erased.**

---

## Health Information Catalogue and Index Management

Owns the ability to manage the **catalogues, indexes and associated descriptive information through which available health information can be identified and located**.

Its concern is knowledge that health information exists and sufficient indexing/cataloguing context to locate or discover that information.

It does **not** own:

- the underlying health-information content;
- authority to access that content;
- retrieval of the content itself;
- interpretation of the content; or
- communication of the retrieved information.

**Boundary test:**

> If the concern is **knowing what health information exists and how it may be located**, it belongs here.

Accordingly:

> **Information catalogued ≠ information retrieved.**

> **Information discoverable ≠ information accessible.**

> **Information indexed ≠ access authorised.**

---

## Health Information Compartmentalisation

Owns the ability to manage the **logical association, partitioning and compartmental context of health information** where health information must be organised into meaningful bounded information groupings.

Compartmentalisation may establish that particular information belongs within, relates to, or is associated with an applicable information compartment.

It does **not**, by itself, determine whether a particular actor is authorised to access that information.

Accordingly, compartmentalisation is distinct from security and access-control decision-making.

**Boundary test:**

> If the concern is **the information compartment to which health information belongs or is associated**, it belongs here.

The architecture SHALL preserve:

> **Information compartmentalised ≠ access authorised.**

> **Information compartment membership may inform access control without itself being the access-control decision.**

This distinction is particularly important where compartment membership is used as input to security, privacy or information-control capabilities.

---

## Health Information Data Correlation

Owns the ability to establish and manage **meaningful correlations between independently managed items or sets of health information**.

Its concern is identifying that health-information elements have a relevant relationship, correspondence or association that is meaningful for subsequent information use.

Correlation may involve information originating from:

- different source systems;
- different encounters;
- different episodes;
- different information structures;
- different temporal contexts;
- different clinical domains; or
- other independently managed health-information contexts.

Health Information Data Correlation does **not**, by itself:

- establish clinical meaning from the correlated information;
- make a Clinical Determination;
- establish Individual identity;
- perform identity matching;
- establish that two independently represented entities are the same entity;
- authorise access; or
- alter source-information authority.

**Boundary test:**

> If the concern is **establishing a meaningful relationship between independently managed health-information data**, it belongs here.

Accordingly:

> **Information correlated ≠ information clinically interpreted.**

> **Information correlated ≠ entity identity resolved.**

> **Information correlated ≠ source authority transferred.**

---

## Health Information Data Retrieval

Owns the ability to retrieve **identified health-information content** according to an applicable information request and context.

Its concern is obtaining health information that has been identified as relevant to the retrieval request.

It does **not** own:

- catalogue/index management;
- authority to access the requested information;
- clinical interpretation;
- communication to another party;
- presentation of the retrieved information; or
- establishment of the underlying information.

Health Information Data Retrieval may therefore consume outcomes from:

- Health Information Catalogue and Index Management;
- Health Information Compartmentalisation;
- Health Information Data Correlation;
- applicable Health Information Control capabilities; and
- other relevant capabilities,

without assuming their responsibilities.

**Boundary test:**

> If the concern is **obtaining identified health-information content for an applicable authorised information purpose**, it belongs here.

Accordingly:

> **Information identified ≠ information retrieved.**

> **Information retrieved ≠ information communicated.**

> **Information retrieved ≠ information presented.**

---

# Important Semantic Distinctions

The decomposition SHALL preserve:

> **Information content ≠ information structure.**

> **Semantic authority ≠ semantic binding ≠ information content.**

> **Data value ≠ information meaning.**

> **Current information ≠ historical information.**

> **Information catalogued ≠ information retrieved.**

> **Information discoverable ≠ information accessible.**

> **Information compartmentalised ≠ access authorised.**

> **Information correlated ≠ information clinically interpreted.**

> **Information correlated ≠ entity identity resolved.**

> **Information retrieved ≠ information communicated.**

> **Information retrieved ≠ information presented.**

These distinctions are Business Capability boundaries and SHALL remain visible through subsequent architectural derivation.

---

# Decision Consequences

## DC-01 — Health Information SHALL Not Be Treated as Semantically Self-Describing Data

Subsequent architecture SHALL NOT assume that an information instance carries sufficient meaning merely through the values physically represented within that instance.

Applicable structure, references, bindings and semantic context form part of the information meaning.

---

## DC-02 — Content and Model Responsibilities SHALL Remain Distinct

Health Information Data Management SHALL NOT absorb Health Information Structure Management.

Likewise, Health Information Structure Management SHALL NOT become the repository or owner of health-information instance content.

---

## DC-03 — Semantic Binding SHALL Not Transfer Semantic Authority

Reference from a Health Information Structure to a terminology, Value Set, classification, reference model or other governed semantic artefact SHALL NOT transfer governance authority for that artefact to Health Information Structure Management.

---

## DC-04 — Catalogue and Retrieval SHALL Remain Distinct

Catalogue/index mechanisms may enable discovery and location of health information.

They SHALL NOT be treated as equivalent to retrieving the health information itself.

---

## DC-05 — Compartmentalisation SHALL Not Become Access Control

Health Information Compartmentalisation may provide information used by security, privacy or information-control capabilities.

It SHALL NOT independently establish access authorisation.

---

## DC-06 — Correlation SHALL Not Become Clinical Interpretation

Health Information Data Correlation may establish meaningful relationships between health-information data.

It SHALL NOT establish clinical conclusions merely because related information has been brought together.

Clinical interpretation remains with the applicable Business Capability.

---

## DC-07 — Correlation SHALL Not Become Identity Resolution

Correlation of health-information data SHALL NOT silently establish that independently represented persons, practitioners, organisations, devices or other entities are the same real-world entity.

Identity resolution remains a separate responsibility.

---

## DC-08 — Retrieval SHALL Not Become Communication or Presentation

Retrieving health information establishes availability of the applicable information to the requesting context.

It does not, by itself, establish:

- communication to another party;
- presentation to a user;
- disclosure;
- publication; or
- clinical interpretation.

---

## DC-09 — Lifecycle Management SHALL Preserve Historical Meaning

Subsequent architecture SHALL preserve sufficient lifecycle context to distinguish current information from historical, amended, corrected, superseded or otherwise lifecycle-qualified information where materially relevant.

---

# Capability-to-Capability Relationship Summary

Relationship expressions in this section use the semantic notation established by **DN-BC-019**.

They SHALL NOT, by themselves, establish workflow, runtime invocation, implementation dependency or system integration.

| Source Capability | Relationship | Target Capability | Meaning |
|---|---|---|---|
| Standards, Semantics & Reference Governance capabilities | **provide governed semantic artefacts to** | Health Information Structure Management | Governed models, terminology, Value Sets, classifications and other semantic artefacts may be referenced or bound within health-information structures. |
| Health Information Structure Management | **provides semantic structure to** | Health Information Data Management | Information structures, references, constraints and bindings provide the context through which managed health-information content acquires unambiguous meaning. |
| Health Information Data Management | **provides managed health-information content to** | Health Information Lifecycle Management | Managed information is subject to applicable lifecycle governance and state management. |
| Health Information Data Management | **provides information context to** | Health Information Catalogue and Index Management | Managed health information provides the subject from which catalogue/index entries and location metadata may be established. |
| Health Information Data Management | **provides information context to** | Health Information Compartmentalisation | Managed health information may be associated with applicable information compartments. |
| Health Information Data Management | **provides health-information data to** | Health Information Data Correlation | Independently managed health-information content may be correlated where meaningful relationships exist. |
| Health Information Catalogue and Index Management | **provides location and discovery context to** | Health Information Data Retrieval | Catalogue/index information may identify what health information exists and where applicable information can be located. |
| Health Information Compartmentalisation | **provides compartment context to** | Health Information Data Retrieval | Compartment association may provide context relevant to determining applicable retrieval scope. |
| Health Information Data Correlation | **may provide context to** | Health Information Data Retrieval | Established correlations may inform identification of related health information relevant to a retrieval request. |
| Health Information Structure Management | **provides interpretation context to** | Health Information Data Retrieval | Retrieved content remains dependent upon its applicable information structure for unambiguous interpretation. |
| Health Information Lifecycle Management | **provides lifecycle context to** | Health Information Data Retrieval | Retrieval may need to distinguish current, historical, superseded or otherwise lifecycle-qualified information. |

These relationships SHALL NOT be interpreted as a mandatory:

```text id="u92t3e"
Structure
   ↓
Data
   ↓
Catalogue
   ↓
Correlation
   ↓
Retrieval
```

processing sequence.

The Capabilities are independently accountable and may operate concurrently, recursively or in different business contexts.

---

# Traceability Requirements

## TR-01 — CT3 Identity SHALL Be Preserved

Subsequent architecture SHALL preserve:

```text id="m8u83x"
Health Information Management
  Health Information Data Management
  Health Information Structure Management
  Health Information Lifecycle Management
  Health Information Catalogue and Index Management
  Health Information Compartmentalisation
  Health Information Data Correlation
  Health Information Data Retrieval
```

---

## TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text id="akq1g4"
CT1 → CT2 → CT3 → Feature
```

Health Information Management Features SHALL be framed by the applicable CT3.

A Feature SHALL NOT compensate for failure to preserve these Business Capability distinctions.

---

## TR-03 — Content SHALL Be Traceable to Applicable Structure

Where Harmonia manages health-information content, subsequent architecture SHOULD preserve sufficient traceability to determine the applicable information structure under which that content is interpreted.

This requirement does not prescribe a particular runtime representation or implementation mechanism.

---

## TR-04 — Semantic Bindings SHALL Preserve Authority

Where an information structure references:

- terminology;
- Value Sets;
- classifications;
- reference models;
- external standards; or
- other governed semantic artefacts,

traceability SHOULD preserve the identity and applicable authority/version of the referenced artefact where materially relevant.

Binding an artefact SHALL NOT silently duplicate or transfer its governance authority.

---

## TR-05 — Lifecycle State SHALL Remain Traceable

Where lifecycle state materially affects interpretation or use of health information, subsequent architecture SHOULD preserve sufficient information to establish that state and relevant historical relationship.

---

## TR-06 — Catalogue Entries SHALL Remain Distinguishable from Information Content

A catalogue or index entry SHALL NOT silently become the authoritative health-information content to which it refers.

Traceability between index/catalogue information and managed health-information content SHOULD be preserved.

---

## TR-07 — Compartment Membership SHALL Remain Distinguishable from Access Decision

Where compartment information contributes to an access decision, subsequent traceability SHOULD preserve:

```text id="xaxjtb"
Health Information
      │
      │ associated with
      ▼
Information Compartment
      │
      │ considered by
      ▼
Information Control / Access Decision
```

without treating compartment membership as the authorisation decision itself.

---

## TR-08 — Correlation SHALL Preserve Source Provenance

Where independently managed health information is correlated, subsequent architecture SHOULD preserve sufficient provenance to identify the contributing information and its authoritative sources.

Correlation SHALL NOT erase the independence or provenance of the source information.

---

## TR-09 — Retrieval SHALL Preserve Information Context

Retrieved health information SHOULD retain sufficient:

- structural;
- semantic;
- lifecycle;
- provenance;
- compartment; and
- authority

context to permit correct interpretation where those dimensions are materially relevant.

Retrieval SHALL NOT reduce governed health information to semantically context-free data.

---

## TR-10 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component or service MAY support multiple Health Information Management CT3s.

For example, a persistence or API capability may technically participate in:

- Data Management;
- Lifecycle Management;
- Catalogue and Index Management;
- Correlation; and
- Retrieval.

Such implementation consolidation SHALL NOT retrospectively collapse the Business Capability distinctions.

Architecture traceability proceeds from business responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

## TR-11 — Information Models SHALL Not Be Equated with FHIR Resources

Where FHIR is used as an information representation or interoperability mechanism, subsequent architecture SHALL preserve the distinction between:

- the governed health-information meaning;
- the applicable Health Information Structure;
- the FHIR representation of that information;
- applicable profiles, terminology bindings and constraints; and
- individual FHIR resource instances.

A FHIR resource instance SHALL NOT, merely by existing, be assumed to constitute the complete semantic authority for the health information it represents.

---

## TR-12 — Unmodelled Health Information Management Scope SHALL Remain Explicitly Unmodelled

Absence of an information-management responsibility from this R1/R2 decomposition SHALL NOT imply:

- that the responsibility does not exist;
- that it belongs to one of the seven established CT3s;
- that Harmonia owns it; or
- that implementation may silently absorb it.

Where a subsequent Harmonia requirement exposes a materially distinct Health Information Management responsibility, it SHALL return to Business Architecture for explicit adjudication.

The architecture SHALL preserve the distinction between:

> **not presently modelled because Harmonia does not require the distinction**

and

> **modelled as the responsibility of an existing Capability.**

------------------------------------------------------------------------

# DN-BC-022 — Clinical Knowledge Management Decomposition

## Context

`Health Information & Knowledge Management → Clinical Knowledge Management`

Clinical Knowledge Management represents the business ability to identify, extract, formulate, associate, govern and share reusable clinical knowledge.

The previous candidate decomposition identified only:

- Clinical Knowledge Curation; and
- Clinical Knowledge Access.

This was considered insufficient for Harmonia.

Those capabilities obscured materially distinct responsibilities associated with:

- identifying contexts containing potential clinical knowledge;
- extracting knowledge-relevant content from those contexts;
- formulating extracted material into explicit clinical knowledge;
- associating clinical knowledge with applicable clinical concepts;
- reviewing and approving clinical knowledge for governed use; and
- publishing and sharing approved clinical knowledge.

For Harmonia R1/R2, these distinctions are architecturally material because they preserve the boundaries between:

- source context and extracted knowledge;
- extracted material and formulated clinical knowledge;
- clinical knowledge and governed clinical concepts;
- knowledge formulation and knowledge authority;
- approved knowledge and publication; and
- reusable clinical knowledge and its application to an Individual.

---

## Working Decision

**Clinical Knowledge Management** decomposes for Harmonia R1/R2 into:

```text id="j7xj22"
Clinical Knowledge Management
  Clinical Knowledge Context Identification
  Clinical Knowledge Context Extraction
  Clinical Knowledge Information Formulation
  Clinical Knowledge Concept Association
  Clinical Knowledge Review & Approval Management
  Clinical Knowledge Publication & Sharing
```

These are independently accountable Business Capabilities.

Although meaningful relationships exist between them, the decomposition SHALL NOT be interpreted as establishing a mandatory knowledge-management workflow.

---

# CT3 Capability Boundaries

## Clinical Knowledge Context Identification

Owns the ability to identify **information, material, evidence or other contexts containing content potentially relevant to clinical knowledge**.

Its concern is recognising the applicable knowledge-bearing context.

Such contexts may include, where applicable:

- clinical literature;
- clinical guidelines;
- policies;
- protocols;
- standards;
- research outputs;
- established clinical evidence;
- health-information collections;
- professional knowledge sources; and
- other material from which reusable clinical knowledge may potentially be derived.

Identification does not establish that all content within the identified context constitutes valid or approved clinical knowledge.

It does **not** own:

- extraction of knowledge from the identified context;
- formulation of clinical knowledge;
- approval of clinical knowledge;
- semantic governance of concepts contained within the source;
- publication of clinical knowledge; or
- application of knowledge to an Individual.

**Boundary test:**

> If the concern is **identifying a context that may contain clinically relevant reusable knowledge**, it belongs here.

Accordingly:

> **Knowledge-bearing context identified ≠ clinical knowledge extracted.**

---

## Clinical Knowledge Context Extraction

Owns the ability to extract **knowledge-relevant content from an identified clinical knowledge context**.

Its concern is separating, capturing or deriving material relevant to clinical knowledge from the broader context in which that material occurs.

Extraction preserves sufficient relationship to the source context to support subsequent interpretation, provenance and governance.

Extracted material is not necessarily a governed clinical-knowledge artefact.

It does **not** own:

- identification of the original knowledge-bearing context;
- formulation of the extracted material into governed clinical knowledge;
- approval of the resulting knowledge;
- definition of clinical concepts;
- publication of knowledge; or
- application of the extracted material to an Individual.

**Boundary test:**

> If the concern is **obtaining knowledge-relevant material from an identified source or context**, it belongs here.

Accordingly:

> **Source identified ≠ relevant content extracted.**

and:

> **Content extracted ≠ clinical knowledge formulated.**

---

## Clinical Knowledge Information Formulation

Owns the ability to formulate extracted or otherwise applicable knowledge-relevant material into **explicit, structured and reusable clinical knowledge information**.

Its concern is establishing a coherent clinical-knowledge representation suitable for subsequent association, review, approval, publication and use.

Formulation may establish applicable:

- knowledge statements;
- relationships;
- conditions;
- applicability;
- qualifications;
- supporting rationale;
- evidence references;
- contextual constraints; and
- other information necessary to represent the clinical knowledge clearly.

Clinical Knowledge Information Formulation does not, merely through formulation, establish that the knowledge has been approved for clinical use.

It does **not** own:

- governance of the underlying clinical concepts;
- formal approval of the knowledge;
- publication or sharing;
- clinical determination for an Individual; or
- Health Information Structure Management.

**Boundary test:**

> If the concern is **turning knowledge-relevant material into an explicit and reusable clinical-knowledge representation**, it belongs here.

Accordingly:

> **Extracted clinical material ≠ formulated clinical knowledge.**

> **Clinical knowledge formulated ≠ clinical knowledge approved.**

---

## Clinical Knowledge Concept Association

Owns the ability to establish and manage **associations between clinical knowledge and the clinical concepts to which that knowledge relates**.

Its concern is the semantic relationship between the knowledge and applicable governed concepts.

For example, clinical knowledge may be associated with concepts concerning:

- conditions;
- findings;
- procedures;
- medications;
- observations;
- anatomical structures;
- risks;
- interventions;
- clinical circumstances; or
- other governed clinical concepts.

Clinical Knowledge Concept Association does **not** establish semantic authority over the concepts themselves.

Where a concept, terminology, classification, ontology or other semantic artefact is governed elsewhere, that governance authority remains external to Clinical Knowledge Management.

**Boundary test:**

> If the concern is **which governed clinical concepts the clinical knowledge concerns or is meaningfully associated with**, it belongs here.

Accordingly:

> **Clinical concept governed ≠ clinical knowledge associated with that concept.**

and:

> **Knowledge-to-concept association ≠ definition of the concept.**

---

## Clinical Knowledge Review & Approval Management

Owns the ability to manage the **review and approval of formulated clinical knowledge for its intended governed use**.

Its concern may include:

- review status;
- reviewer participation;
- applicable review criteria;
- clinical or professional endorsement;
- approval authority;
- approval status;
- conditions or limitations upon approval;
- rejection;
- revision requirements;
- re-review requirements; and
- applicable approval history.

Review and approval establish the governed standing of clinical knowledge for its intended use.

They do not themselves publish or distribute that knowledge.

**Boundary test:**

> If the concern is **whether formulated clinical knowledge has been appropriately reviewed and approved for its intended use**, it belongs here.

Accordingly:

> **Knowledge formulated ≠ knowledge reviewed.**

> **Knowledge reviewed ≠ knowledge approved.**

> **Knowledge approved ≠ knowledge published.**

---

## Clinical Knowledge Publication & Sharing

Owns the ability to make **approved clinical knowledge available to applicable consumers, communities or use contexts**.

Its concern includes governed publication and sharing of clinical knowledge after the applicable knowledge authority has been established.

Publication and sharing may make clinical knowledge available to:

- clinicians;
- clinical teams;
- services;
- applications;
- decision-support capabilities;
- knowledge services;
- other authorised consumers; or
- other applicable clinical contexts.

It does **not** own:

- formulation of the underlying knowledge;
- approval of the knowledge;
- definition of associated clinical concepts;
- access-control decisions;
- clinical application of the knowledge to an Individual; or
- conclusions reached through use of the published knowledge.

**Boundary test:**

> If the concern is **making governed clinical knowledge available for appropriate use**, it belongs here.

Accordingly:

> **Knowledge approved ≠ knowledge published or shared.**

> **Knowledge published ≠ knowledge applied.**

---

# Relationship to Standards, Semantics & Reference Governance

Clinical Knowledge Management may associate clinical knowledge with semantic artefacts governed through Standards, Semantics & Reference Governance.

The architecture SHALL preserve:

```text id="ahqkfc"
Clinical Semantics / Terminology Governance
        │
        │ establishes governed clinical concepts
        ▼
Clinical Knowledge Concept Association
        │
        │ associates clinical knowledge with
        │ applicable governed concepts
        ▼
Clinical Knowledge
```

This does not transfer semantic authority.

For example, Clinical Knowledge Concept Association may establish:

```text id="fdd20w"
Clinical Knowledge Artefact A
        │
        │ associated with
        ▼
Clinical Concept X
```

without redefining `Clinical Concept X`.

Accordingly:

> **Semantic authority ≠ knowledge-to-concept association.**

> **Concept governance ≠ knowledge governance.**

---

# Relationship to Health Information Management

Clinical Knowledge Management and Health Information Management are distinct.

Health Information Management concerns governed **health information**, including information content concerning Individuals and other health-domain subjects.

Clinical Knowledge Management concerns reusable **clinical knowledge** that may be applied across applicable clinical contexts.

The architecture SHALL preserve:

> **Health information ≠ clinical knowledge.**

> **Information about an Individual ≠ reusable clinical knowledge.**

> **Clinical knowledge derived from health information ≠ the source health information.**

Where health information contributes to Clinical Knowledge Context Identification or Extraction, source authority and provenance SHALL remain distinguishable from the resulting clinical knowledge.

Likewise:

> **Clinical Knowledge Information Formulation ≠ Health Information Structure Management.**

Health Information Structure Management establishes structures and semantic bindings through which health-information content acquires unambiguous meaning.

Clinical Knowledge Information Formulation establishes explicit reusable clinical knowledge.

---

# Relationship to Individual Care Delivery

Clinical knowledge may inform Individual Care Delivery.

It does not perform Individual Care Delivery.

Conceptually:

```text id="2xvnij"
Clinical Knowledge Management
        │
        │ provides applicable clinical knowledge
        ▼
Individual Care Capability
        │
        │ applies knowledge within
        │ individual clinical context
        ▼
Individual clinical conclusion or action
```

This relationship SHALL NOT be interpreted as an automated or mandatory clinical decision path.

The architecture SHALL preserve:

> **General clinical knowledge ≠ Individual clinical fact.**

> **Knowledge applicable to a condition ≠ determination that an Individual has that condition.**

> **Knowledge describing a risk ≠ determination that an Individual has that risk.**

> **Knowledge describing an intervention ≠ decision that the intervention should be provided to an Individual.**

Application of clinical knowledge within an Individual's clinical context remains the responsibility of the applicable Individual Care capability.

---

# Important Semantic Distinctions

The decomposition SHALL preserve:

> **Knowledge-bearing context identified ≠ knowledge extracted.**

> **Knowledge extracted ≠ clinical knowledge formulated.**

> **Clinical knowledge formulated ≠ clinical knowledge reviewed.**

> **Clinical knowledge reviewed ≠ clinical knowledge approved.**

> **Clinical knowledge approved ≠ clinical knowledge published/shared.**

> **Clinical knowledge published/shared ≠ clinical knowledge applied.**

> **Clinical concept governed ≠ clinical knowledge associated with that concept.**

> **Clinical knowledge ≠ health information.**

> **Clinical knowledge ≠ Individual Clinical Determination.**

> **Knowledge applicability ≠ clinical conclusion.**

These are responsibility and semantic boundaries rather than mandatory process stages.

---

# R1/R2 Scope Boundary

Clinical Knowledge Management is a substantial specialist domain.

The six CT3 Capabilities established here are not asserted to constitute an exhaustive enterprise decomposition of knowledge management, evidence management, clinical decision support or knowledge engineering.

They establish the distinctions presently required to support Harmonia responsibilities concerning:

- identification of clinical knowledge-bearing contexts;
- extraction of knowledge-relevant material;
- formulation of reusable clinical knowledge;
- association of knowledge with governed clinical concepts;
- governed review and approval; and
- publication and sharing.

Additional decomposition SHALL NOT be introduced merely to represent every possible clinical knowledge-management discipline.

Further decomposition requires evidence that the distinction materially affects Harmonia responsibility, information semantics, interoperability, security, workflow enablement, governance, assurance or architectural traceability.

---

# Decision Consequences

## DC-01 — Source Context and Clinical Knowledge SHALL Remain Distinct

Identification or extraction of source material SHALL NOT silently convert that material into governed clinical knowledge.

Source provenance and the resulting clinical-knowledge artefact remain distinguishable.

---

## DC-02 — Formulation SHALL Not Establish Approval

A technically valid or well-structured clinical-knowledge representation SHALL NOT be treated as approved merely because it has been formulated.

Review and approval remain explicit responsibilities.

---

## DC-03 — Concept Association SHALL Not Transfer Semantic Authority

Association of clinical knowledge with a terminology, concept, Value Set, classification, ontology or other semantic artefact SHALL NOT transfer governance authority for that artefact to Clinical Knowledge Management.

---

## DC-04 — Review and Approval SHALL Remain Distinct from Publication

Approval establishes governed standing for the intended use.

Publication and sharing make the approved knowledge available.

One SHALL NOT be used as evidence that the other has occurred unless explicitly established.

---

## DC-05 — Publication SHALL Not Establish Clinical Applicability to an Individual

Publication or sharing of clinical knowledge SHALL NOT establish that the knowledge applies to a particular Individual.

The applicable clinical capability remains responsible for determining relevance within the Individual clinical context.

---

## DC-06 — Clinical Knowledge SHALL Not Become Clinical Decision-Making

Clinical Knowledge Management SHALL NOT absorb:

- Clinical Determination;
- Individual Care Planning;
- prescribing;
- diagnostic interpretation;
- clinical risk determination;
- treatment selection; or
- other Individual clinical decision responsibilities.

It may provide knowledge used by those capabilities without assuming their accountability.

---

## DC-07 — Clinical Knowledge SHALL Remain Distinct from Health Information

Where health information contributes evidence to clinical knowledge, the resulting knowledge SHALL NOT overwrite, redefine or assume authority for the source health information.

Likewise, reusable clinical knowledge SHALL NOT be treated as an Individual health record merely because it is used in an Individual clinical context.

---

# Capability-to-Capability Relationship Summary

Relationship expressions in this section use the capability relationship semantics established by **DN-BC-019**.

They describe semantic Business Capability relationships and SHALL NOT, by themselves, establish workflow sequencing, runtime dependency, system integration or implementation behaviour.

| Source Capability | Relationship | Target Capability | Meaning |
|---|---|---|---|
| Clinical Knowledge Context Identification | **provides identified knowledge context to** | Clinical Knowledge Context Extraction | Identified contexts provide the source context from which clinically relevant knowledge material may be extracted. |
| Clinical Knowledge Context Extraction | **provides extracted knowledge material to** | Clinical Knowledge Information Formulation | Extracted material may provide the basis for formulation of explicit reusable clinical knowledge. |
| Clinical Knowledge Information Formulation | **provides formulated clinical knowledge to** | Clinical Knowledge Concept Association | Formulated knowledge provides the knowledge artefact against which applicable clinical-concept associations may be established. |
| Standards, Semantics & Reference Governance capabilities | **provide governed semantic artefacts to** | Clinical Knowledge Concept Association | Governed clinical concepts and other semantic artefacts may be associated with clinical knowledge without transferring semantic authority. |
| Clinical Knowledge Information Formulation | **provides formulated clinical knowledge to** | Clinical Knowledge Review & Approval Management | Formulated knowledge may be submitted for applicable review and approval. |
| Clinical Knowledge Concept Association | **provides semantic association context to** | Clinical Knowledge Review & Approval Management | Concept associations may provide semantic context relevant to review of the formulated clinical knowledge. |
| Clinical Knowledge Review & Approval Management | **provides approved clinical knowledge to** | Clinical Knowledge Publication & Sharing | Knowledge with appropriate governed approval may be made available to applicable consumers. |
| Clinical Knowledge Publication & Sharing | **provides clinical knowledge to** | Individual Care capabilities | Published/shared knowledge may inform applicable clinical activity without establishing the Individual clinical conclusion or action. |
| Health Information Management capabilities | **may provide source information to** | Clinical Knowledge Context Identification | Governed health information may, where appropriate, provide a context from which reusable clinical knowledge may be identified. |

These relationships SHALL NOT be interpreted as requiring:

```text id="3q48bw"
Identify
   ↓
Extract
   ↓
Formulate
   ↓
Associate
   ↓
Review / Approve
   ↓
Publish / Share
```

as a mandatory workflow.

Knowledge may be revised, re-extracted, re-formulated, reassociated, re-reviewed, withdrawn, republished or otherwise managed according to applicable governance.

The relationships describe responsibility boundaries, not a prescribed process.

---

# Traceability Requirements

## TR-01 — CT3 Identity SHALL Be Preserved

Subsequent architecture SHALL preserve:

```text id="ejnkyk"
Clinical Knowledge Management
  Clinical Knowledge Context Identification
  Clinical Knowledge Context Extraction
  Clinical Knowledge Information Formulation
  Clinical Knowledge Concept Association
  Clinical Knowledge Review & Approval Management
  Clinical Knowledge Publication & Sharing
```

Downstream architectural elements SHOULD trace to the applicable CT3 rather than ambiguously to Clinical Knowledge Management as a whole.

---

## TR-02 — Features SHALL Be Framed by the Applicable CT3

Consistent with:

```text id="3yxw82"
CT1 → CT2 → CT3 → Feature
```

Clinical Knowledge Management Features SHALL be framed by the applicable CT3.

Features SHALL NOT compensate for incomplete Capability decomposition.

---

## TR-03 — Extracted Knowledge SHALL Preserve Source Provenance

Where knowledge-relevant material is extracted from an identified context, subsequent architecture SHOULD preserve sufficient provenance to identify the applicable source and extraction context.

Extraction SHALL NOT erase source authority.

---

## TR-04 — Formulated Knowledge SHALL Remain Traceable to Supporting Material

Where clinically material, formulated clinical knowledge SHOULD remain traceable to the source material, evidence or context upon which it is based.

This requirement does not prescribe a particular evidence-management or knowledge-representation technology.

---

## TR-05 — Concept Associations SHALL Preserve Semantic Authority

Where clinical knowledge is associated with governed clinical concepts, subsequent architecture SHOULD preserve sufficient information to identify the applicable concept and its semantic authority.

The association SHALL NOT create an uncontrolled duplicate definition of the concept.

---

## TR-06 — Review and Approval State SHALL Be Traceable

Where Harmonia manages governed clinical knowledge, subsequent architecture SHOULD preserve applicable:

- review status;
- approval status;
- authority;
- version;
- conditions or limitations;
- effective period; and
- applicable review history

where materially relevant to safe and governed use.

---

## TR-07 — Publication SHALL Preserve Knowledge Standing

Published or shared clinical knowledge SHOULD retain sufficient context to determine its applicable governed standing.

Publication SHALL NOT strip away information necessary to distinguish, where relevant:

- approved knowledge;
- draft knowledge;
- superseded knowledge;
- withdrawn knowledge; or
- knowledge whose applicability has otherwise changed.

---

## TR-08 — Knowledge Use SHALL Remain Distinguishable from Knowledge Authority

Where an Individual Care capability consumes clinical knowledge, subsequent architecture SHOULD preserve the distinction between:

```text id="7k26tr"
Clinical Knowledge
        │
        │ informs
        ▼
Individual Clinical Context
        │
        │ contributes to
        ▼
Clinical Conclusion / Action
```

The resulting Individual conclusion or action SHALL NOT be represented as though it were authored by the Clinical Knowledge Management capability.

---

## TR-09 — Health Information and Clinical Knowledge Provenance SHALL Remain Distinguishable

Where health information contributes to clinical knowledge, downstream architecture SHOULD preserve sufficient provenance to distinguish:

- source health information;
- extracted knowledge material;
- formulated clinical knowledge; and
- subsequent clinical application.

These SHALL NOT collapse into a single undifferentiated information object merely because an implementation stores them together.

---

## TR-10 — Implementation SHALL Not Redefine Capability Boundaries

A Harmonia component or service MAY support multiple Clinical Knowledge Management CT3s.

Implementation consolidation SHALL NOT retrospectively collapse:

- identification;
- extraction;
- formulation;
- concept association;
- review and approval; or
- publication and sharing

into a single Business Capability.

Architecture traceability proceeds from business responsibility toward implementation, not from implementation convenience toward reconstructed Business Capability semantics.

---

## TR-11 — Unmodelled Clinical Knowledge Scope SHALL Remain Explicitly Unmodelled

Absence of a specialist Clinical Knowledge Management responsibility from the R1/R2 decomposition SHALL NOT imply:

- that the responsibility does not exist;
- that it belongs to one of the six established CT3s;
- that Harmonia owns it; or
- that implementation may silently absorb it.

Where a subsequent Harmonia requirement exposes a materially distinct Clinical Knowledge Management responsibility, it SHALL return to Business Architecture for explicit adjudication.

The architecture SHALL preserve the distinction between:

> **not presently modelled because Harmonia does not require the distinction**

and

> **modelled as the responsibility of an existing Capability.**

------------------------------------------------------------------------

------------------------------------------------------------------------

# DN-BC-023 — Business Capability and Business Enabling Capability Distinction

## Context

Current Business Capability reconciliation: organisational abilities and
the reusable system-enabled abilities through which Harmonia enables them.

## Working Decision

A **Business Capability** describes what the health enterprise or
organisation must be capable of doing.

A **Business Enabling Capability** describes a reusable system-enabled
ability Harmonia provides to enable Business Capabilities.

A Business Enabling Capability may concern the same conceptual subject
as a Business Capability without being semantically equivalent to it.
The relationship is enablement, not decomposition and not implementation
equivalence.

A Business Enabling Capability does not assume accountability for the
underlying business activity merely because it represents, records or
provides information used by that activity.

## Naming Guardrail

> Where a Business Capability and Business Enabling Capability concern
> the same business concept, their names SHALL distinguish the
> organisational ability from the system-enabled ability. A Business
> Enabling Capability SHALL NOT reuse a Business Capability name where
> doing so implies semantic equivalence or ownership of the underlying
> business activity.

## Identity Assurance Example

The Business Capability **Identity Assurance** concerns the organisational
activity of establishing sufficient confidence that a person is who
they claim to be using appropriate supporting evidence and processes.

A future Harmonia Business Enabling Capability may represent the
established assurance or quality of an identity in computable form.
That future Business Enabling Capability must therefore use a distinct
name rather than also being called **Identity Assurance**.

## Open Issue — Future Enabling Capability Name

**Identity Assurance Representation** was discussed as a possible
Business Enabling Capability name but has **not been formally
established**. This note does not establish that name or the future
Business Enabling Capability.

------------------------------------------------------------------------

# DN-BC-024 — Business Capability Interpretation Guardrail

## Context

Interpretation of Business Capability descriptions and examples during
the current reconciliation.

## Working Decision

> Business Capabilities SHALL be interpreted as organisational abilities.
> References to digital technologies, information systems,
> infrastructure, physical mechanisms or other implementation mechanisms
> within their descriptions or examples do not imply that the Business
> Capability is itself a software or technology capability.

Such examples illustrate how a Business Capability may be exercised or
enabled; they do not redefine the organisational ability.

This guardrail applies, for example, to **Digital Communication Channel**,
**Health Information Exchange**, **Access Control Enforcement** and
**Identity Assurance** without adding semantics to those capabilities.

## Interpretive Test

> If interpretation of a Business Capability requires identifying a
> particular piece of software in order to explain what the capability
> means, the interpretation should be treated as suspect and the intended
> business ability re-examined.

------------------------------------------------------------------------

# DN-BC-025 — Health Information Standards Governance — Oversight and Impact Boundaries

## Context

Current reconciliation of Health Information Standards Governance.

## Working Decision — Information Model Governance Rename

Rename **Information Model Governance** to **Information Model Governance
Oversight**.

**Information Model Governance Oversight** oversees and evaluates the
effectiveness, consistency and appropriate operation of the
organisation's information-model governance activities.

It does not perform the peer capabilities such as **Information Model
Discovery**, **Information Model Adoption**, **Information Model
Education** or **Information Model Compliance**.

## Information Models and Standards — Impact Boundary

For Information Models, impact assessment is intrinsic to **Information
Model Adoption**.

For Standards, impacts can extend beyond information-system
implementation into clinical practice, work practices, permitted
medications, operating procedures, governance obligations, reporting,
safety, policy and other organisational concerns.

Therefore **Standards Impact Identification** is legitimately a separate
capability from **Standards Adoption**.

## Interpretive Warning

Conceptual sequences associated with these capabilities SHALL NOT be
interpreted as mandatory workflows.

------------------------------------------------------------------------

# DN-BC-026 — Clinical Semantics Governance — Current CT3 Set and Semantic Boundaries

## Context

Current reconciliation of Clinical Semantics Governance.

## Working Decision — Current CT3 Set

- Terminology Governance
- Terminology Utilisation Oversight
- Terminology Localisation
- Classification Governance
- Classification Utilisation Oversight
- Semantic Mapping Governance

## Terminology Boundaries

**Terminology Governance** manages the lifecycle and organisational
standing of governed terminology/reference-set information, including
acceptance, introduction, update, supersession/retirement, release
handling and coordination with terminology authorities/providers.

**Terminology Utilisation Oversight** oversees how terminology/reference
sets are actually used by people, applications and systems throughout
the organisation and identifies inappropriate, inconsistent or drifting
semantic usage.

**Terminology Localisation** supports controlled organisational responses
where adopted terminology does not adequately meet legitimate local
semantic requirements.

**Classification Localisation** has not been established. It SHALL NOT
be created merely for structural symmetry with Terminology Localisation.

## Orthogonality Guardrail

> Information context, information sensitivity and information meaning
> are orthogonal concerns. Compartmentalisation, classification and
> terminology SHALL therefore remain distinct architectural problem
> domains even where implementations represent them through related
> metadata or apply them concurrently.

The conceptual distinctions are:

- **Compartmentalisation** — how information relates to the business
  context, client, practitioners, roles and other relevant contextual
  relationships.
- **Classification** — how exposure or mishandling of information could
  materially affect the organisation, practitioner, client or other
  relevant party.
- **Terminology** — how the meaning of information/data is defined
  consistently and unambiguously.

The people, responsibilities and organisational processes associated
with these domains may be different even at Business Architecture level.

------------------------------------------------------------------------

# DN-BC-027 — Connected Health Services — Naming and Communication Boundaries

## Context

Current reconciliation of Connected Health Services.

## Working Decision — CT2 Renames

- **Health Information Connectivity** → **Health Information Exchange**
- **Digital Health Interaction** → **Digital Communication Channel**

**Digital Communication Channel (Patient/Practitioner)** was considered,
but the agreed name is **Digital Communication Channel**. Participant
scope is expressed through its definition rather than parentheses in the
capability name.

## Health Information Exchange Boundary

**Health Information Exchange** concerns the organisational ability to
exchange health information appropriately between participating parties,
systems and services.

It is not synonymous with a particular interoperability platform, FHIR
server, interface engine, protocol or implementation.

## Digital Communication Channel Boundary

**Digital Communication Channel** provides digitally mediated
communication, notification and collaborative interaction between
patients, practitioners and other participants in health-service
delivery.

It includes concerns such as participant collaboration and notifications
associated with appointments, pharmacy queries, results and similar
health-service interactions.

> Digital Communication Channel SHALL NOT be treated as the general
> capability for presenting Harmonia functionality through user
> interfaces. A user interface through which a person exercises another
> Business or Business Enabling Capability does not, by that fact alone,
> constitute use of the Digital Communication Channel.

## Open Issue — General UI/Presentation Enablement

The general UI/presentation enabling concern remains **parked** for later
Business Enabling Capability reconciliation, particularly against the
existing **Presentation** enabling capability. This note does not resolve
that concern.

## Working Decision — Programme Spelling

Correct **Health Information Sharing Program Management** to **Health
Information Sharing Programme Management**.

------------------------------------------------------------------------

# DN-BC-028 — Digital Identity & Access Governance — Current CT3 Set and Business Boundaries

## Context

Current reconciliation of Digital Identity & Access Governance.

## Working Decision — Current CT3 Set

- Identity Assurance
- Identity Modelling & Interoperability
- Identity Qualification Control
- Identity Identification & Authentication
- Access Control Context Management
- Access Control Assurance & Reporting
- Access Control Collaboration
- Access Control Enforcement

## Identity Boundaries

**Identity Assurance** is the organisational ability to establish
sufficient confidence that a person is who they claim to be using
appropriate supporting evidence and processes, such as passports, birth
certificates, referees or other applicable identity evidence.

It is not itself a software capability. The distinction from a future
Business Enabling Capability representing established assurance is
recorded in **DN-BC-023**.

**Identity Identification & Authentication** concerns identifying an
actor and authenticating the asserted identity.

**Identity Qualification Control** determines whether the characteristics,
provenance and assurance quality of the identity being presented are
sufficient for the context in which it is to be used.

Federation and Single Sign-On are relevant examples but are not
equivalent concepts and do not themselves establish that an identity is
of sufficient quality for every purpose.

The shorthand distinction is:

- **Identification & Authentication** — Who are you, and can the
  asserted identity be authenticated?
- **Identity Assurance** — How confident are we that this identity
  corresponds to the claimed person/entity?
- **Identity Qualification Control** — Is the identity presented of
  sufficient quality for what is being attempted in this context?

## Access Control Context Management

**Access Control Context Management** is the organisational capability
for determining what information, functions, activities or other things
require control, under what circumstances, and how those control
requirements should be modelled.

It is not merely runtime assembly of attributes for an authorisation
decision.

## Access Control Enforcement

**Access Control Enforcement** ensures that the different mechanisms and
frameworks implementing access controls work coherently to impose the
intended controls.

Those mechanisms may be physical, procedural or digital, including
locked doors, keyed cupboards, passwords, authorisation services and
other mechanisms.

These examples illustrate how the organisational capability may be
exercised or enabled. It is not synonymous with a software authorisation
engine.

## Access Control Collaboration

**Access Control Collaboration** coordinates access-control requirements
and arrangements where protected information, functions, activities,
resources or environments span independently managed control domains,
authorities, organisations or parties.

> Strong local control does not imply effective end-to-end control.

Independent parties may retain control ownership while requiring
collaboration so that one party's arrangements do not undermine the
intended collective protection.

## Access Control Assurance & Reporting

**Access Control Assurance & Reporting** provides organisational assurance
and evidence concerning whether access-control arrangements are
appropriately established and operating.

------------------------------------------------------------------------

# DN-BC-029 — Health Information Protection — Current CT3 Set and Assurance Boundaries

## Context

Current reconciliation of Health Information Protection.

## Working Decision — Current CT3 Set

- Health Privacy Safeguard Management
- Health Information Safeguarding
- Health Information Access Monitoring
- Health Information Compartment Assurance
- Health Information Classification Assurance

## Privacy and Safeguarding Boundaries

**Health Privacy Safeguard Management** concerns the privacy consequences
of supplying, exposing or otherwise using information concerning an
individual or other relevant entity. It establishes organisational
guidelines, guardrails and assurance processes around information sets
whose exposure or use may adversely affect privacy.

**Health Information Safeguarding** concerns whether the health-information
estate and its operational environment are adequately protected and
resilient.

Relevant concerns may include storage safety, infrastructure access,
backup, off-site storage, disaster recovery, operational practices,
authorisation arrangements, access controls, testing and assurance.

These examples illustrate how the organisational capability may be
exercised or enabled; they do not make it a technology capability.

> Privacy safeguarding concerns the consequences of information
> exposure or use. Health Information Safeguarding concerns whether the
> information and the environment in which it is held are adequately
> protected against loss, compromise, inappropriate access, operational
> failure and related threats.

## Health Information Access Monitoring

**Health Information Access Monitoring** is an organisational QA/oversight
capability concerning the organisation's actual practices for accessing
and handling health information.

It may evaluate matters including:

- application of access controls;
- accessibility practices;
- compartmentalisation;
- classification;
- physical and printed information handling; and
- other relevant information-access and protection practices.

It is not merely monitoring software access events.

Health Information Access Monitoring may consume evidence supplied by
enabling capabilities such as IT access monitoring, audit/event
monitoring, physical/building access monitoring and other monitoring
mechanisms.

> The enabling capability observes and supplies evidence. The Business
> Capability evaluates organisational practice using that evidence.

Supplying monitoring evidence does not transfer business accountability
for Health Information Access Monitoring to the enabling capability.

## Compartment and Classification Assurance

**Health Information Compartment Assurance** assures that
compartmentalisation is being established and applied appropriately. It
does not establish the underlying access decision.

**Health Information Classification Assurance** assures that information
classification is being established and applied appropriately according
to the sensitivity and potential consequences of information exposure
or mishandling.

------------------------------------------------------------------------

# DN-BC-030 — Digital Accountability & Trust — Attribution and Accountability Boundaries

## Context

Current reconciliation of Digital Accountability & Trust.

## Working Decision — Current CT3 Set

- Governed Activity Attribution
- Security & Information-Use Accountability

## Governed Activity Attribution

**Governed Activity Attribution** is the organisational ability to
establish and preserve the attributable relationship between:

- who participated in an activity, including applicable person, role,
  business unit, organisation or other accountable organisational
  context;
- what was done; and
- the applicable authority, permission or constraint context governing
  what those actors were permitted to do.

The following distinction SHALL be preserved:

> Actor identified ≠ activity attributed ≠ activity permitted.

Attribution without applicable authority/constraint context may
establish who did what but is insufficient to establish whether they
were entitled to do it.

Governed Activity Attribution is therefore richer than simple audit
logging or provenance.

## Security & Information-Use Accountability

**Security & Information-Use Accountability** is the organisational
ability to evaluate whether security-relevant activity and use of
information were appropriate within applicable authority, policy,
obligation and constraint context, and to establish accountability for
that activity or use.

The shorthand distinction is:

- **Governed Activity Attribution** — Who did what, in what attributable
  capacity, and what were they permitted to do?
- **Security & Information-Use Accountability** — Given that attribution
  and authority context, was what they actually did appropriate, and can
  they be held to account?

## Enablement Boundary

Expected consumption of attribution and authority context by Service
Governance, Service Assurance or other enabling capabilities SHALL NOT
establish a structural Business Capability → Business Enabling Capability
relationship. Those relationships remain for later explicit
reconciliation.

------------------------------------------------------------------------

# DN-BC-031 — Health Research & Innovation — R1/R2 Bounded Decomposition

## Context

Current reconciliation of Health Research & Innovation for Harmonia
R1/R2.

## Working Decision — Current Structure

```text
Health Research & Innovation
  Research Information Support
    Governed Research Information Provision
  Health Innovation
    Health Innovation Evaluation
```

This structure is deliberately under-decomposed for R1/R2.

## Research Information Support

**Research Information Support** is presently limited to the
research-information responsibility required by Harmonia: provision of
existing health information for authorised research purposes.

**Governed Research Information Provision** is the organisational ability
to provide health information for an authorised research purpose subject
to applicable governance, ethics, privacy, consent, security,
information-use and other constraints.

## R1/R2 Scope Boundary

This decomposition SHALL NOT be interpreted as the complete enterprise
research capability model.

> The R1/R2 decomposition does not assert that governed information
> provision is the only capability required to support health research.
> It establishes only the research-information capability presently
> required to frame Harmonia responsibilities. Additional
> research-information capabilities may be introduced when a future
> Harmonia requirement makes the distinction architecturally material.

Research Assessment and Governance, Research/Trials Oversight and other
research capabilities remain intentionally unmodelled unless later
Harmonia requirements make them material. Their absence does not allocate
their responsibilities to Governed Research Information Provision.

## Health Innovation Evaluation

**Health Innovation Evaluation** is the organisational ability to
evaluate proposed or emerging health-service or clinical innovation for
suitability, value, implications and potential adoption.

------------------------------------------------------------------------

# DN-BC-032 — Health Service Direction & Stewardship — Modelling and Alignment

## Context

Current reconciliation of Health Service Direction & Stewardship.

## Working Decision — Current Structure

```text
Health Service Direction & Stewardship

  Health Service Modelling
    Health Service Need Identification
    Health Service Capability Identification
    Health Service Implementation Support
    Health Service Continuous Improvement

  Health Service Alignment
    Health Service Co-Dependency Management
    Health Service Resource Identification
    Health Service Financial Management
    Health Service Implementation Planning
```

## Health Service Modelling

The semantic progression is:

> Identification → Requirements to Fulfil → Support Fulfilment → Check
> and Repeat

This is a semantic relationship, not a mandatory sequential workflow.

**Health Service Need Identification** establishes an unmet
health-service need requiring response.

**Health Service Capability Identification** establishes the lower-case
abilities necessary for a service to satisfy that need, including
applicable clinical, workforce, facility, technology, information and
support abilities. “Capability” here is ordinary business meaning and
MUST NOT be interpreted as Harmonia Capability-tier decomposition.

**Health Service Implementation Support** supports establishment of the
framed service, including applicable facility change, recruitment,
training, equipment, technology, organisational preparation and
programmes of work. It does not absorb HR, facilities, ICT, procurement
or other responsible capabilities.

**Health Service Continuous Improvement** evaluates the established
service, identifies correction or improvement requirements, and may
feed those requirements back into need/capability identification and
implementation support.

The boundary is:

> Health Service Modelling establishes what service should exist and
> what it needs to become viable.

## Health Service Alignment

**Health Service Alignment** concerns how a health service fits within
and interacts with the wider service ecosystem.

**Health Service Co-Dependency Management** identifies and manages
bidirectional dependencies between a health service and other services:
what this service requires from others and what other services require
from it.

**Health Service Resource Identification** identifies workforce,
facilities, equipment, clinical technology, ICT, administration,
supporting services and other resources required to support the service.
It does not own those resources.

**Health Service Financial Management** establishes how the service fits
within applicable funding, financial and charging arrangements without
absorbing enterprise finance/accounting responsibilities.

**Health Service Implementation Planning** establishes and coordinates
the surrounding organisational/service changes necessary to introduce
or materially change the service.

The shorthand distinction is:

> Health Service Modelling: What service do we need, what must it be
> capable of, and how do we establish/improve it?

> Health Service Alignment: How does that service fit into, depend upon,
> and affect the wider health-service environment?

The Service Catalogue boundary SHALL remain explicit:

> Health Service Alignment does not establish the authoritative service
> catalogue. It ensures that a new or changed health service is coherently
> positioned within the service environment represented by that catalogue.

## Service Catalogue Derivation Rationale

A major Harmonia relevance for these Business Capabilities is the
expected semantic richness of the future **Service Catalogue Management
Business Enabling Capability**.

The Business Capabilities establish and govern facts concerning health
services. A future Service Catalogue Management Business Enabling
Capability may provide reusable system-enabled representation of
applicable service facts.

Derivation proceeds from the business abilities and facts toward their
system-enabled representation. Expected Service Catalogue complexity
is evidence of why these business concepts matter to Harmonia; it does
not define the Business Capabilities or establish a one-to-one
Business Capability ↔ Business Enabling Capability mapping.

This note does not establish the Service Catalogue Management Business
Enabling Capability structure or any Feature mapping.

------------------------------------------------------------------------

# DN-BC-033 — Health Workforce Management — Qualification, Engagement and Coverage

## Context

Current reconciliation of Health Workforce Management.

## Working Decision — Current Structure

```text
Health Workforce Management
  Practitioner Qualification Management
  Practitioner Engagement & Privilege Management
  Workforce Service Coverage Management
```

## Practitioner Qualification Management

**Practitioner Qualification Management** extends beyond ordinary HR
credential recording and concerns organisational governance of
practitioner professional/clinical standing and qualifications,
including applicable review, escalation, complaints, penalties/issues,
suspension and revocation.

## Practitioner Engagement & Privilege Management

**Practitioner Engagement & Privilege Management** concerns the
macro-level engagement and ongoing management of practitioners as
participants in health-service delivery, including applicable
roles/capacities, privileges and qualification context. It sits above
ordinary HR employment administration.

The following distinction SHALL be preserved:

> Practitioner qualified ≠ practitioner engaged ≠ practitioner privileged
> to undertake a particular activity.

## Workforce Service Coverage Management

**Workforce Service Coverage Management** is the whole-of-enterprise
capability for observing whether required work/service obligations can
be fulfilled by available workforce, identifying coverage gaps and
risks, and managing/shuffling workforce coverage in response.

It is not merely rostering.

The distinction from care-demand deployment and particular operational
work SHALL be preserved, retaining the boundaries in **DN-BC-005** and
**DN-BC-009**:

- **Workforce Service Coverage Management** — Can required enterprise
  work be covered, where are the gaps/risks, and how should workforce
  coverage be adjusted?
- **Care Resource Deployment** — Given legitimately available resources,
  which appropriate resource is deployed to this care requirement?
- **Care Support Work Coordination** — What particular operational work
  needs doing, who is to perform it, and how is its completion coordinated?

------------------------------------------------------------------------

# DN-BC-034 — Healthcare Organisation & Service Structure Management

## Context

Current reconciliation of Healthcare Organisation & Service Structure
Management.

## Working Decision — Current Structure

```text
Healthcare Organisation & Service Structure Management
  Healthcare Organisation Identity & Structure Management
  Healthcare Location Definition Management
  Healthcare Service Offering Management
```

## Organisation and Location Boundaries

**Healthcare Organisation Identity & Structure Management** establishes
and manages the identity and structural relationships of organisations
and organisational units participating in health-service delivery.

**Healthcare Location Definition Management** establishes and manages
recognised locations in which health services and related activity may
occur, including applicable business characteristics and relationships.
It is not Facilities Management.

## Healthcare Service Offering Management

**Healthcare Service Offering Management** establishes and manages
identifiable health services offered by an organisation, including:

- what service is offered;
- by whom;
- where;
- when; and
- under what applicable conditions or constraints the service is available.

Temporal and conditional availability are material.

For example, a walk-in service may be offered at one location from
06:00–22:00 while another service/pathway provides coverage outside
those hours.

The following distinction SHALL be preserved:

> Service exists ≠ service offered at this location ≠ service available
> at this time/context.

## Operational Readiness Boundary

> Healthcare Service Offering Management establishes the circumstances
> under which an identifiable health service is offered; it does not
> itself establish that the resources necessary to deliver that offering
> are presently operationally ready.

**OI-BC-002 — Health Service Operational Readiness Placement** remains
unresolved. This note does not establish its CT2 parent or canonical
ancestry.

## Distinction from Health Service Direction & Stewardship

The distinction from **DN-BC-032** SHALL remain explicit:

- **Health Service Modelling** establishes what service is needed and
  what it must be capable of.
- **Health Service Alignment** establishes how it fits within the wider
  service ecosystem.
- **Healthcare Service Offering Management** establishes/manages it as
  an identifiable organisational service offering.

------------------------------------------------------------------------

# DN-BC-035 — Corporate Resources & Enterprise Services — Harmonia-Relevant Context

## Context

Current reconciliation of Corporate Resources & Enterprise Services for
Harmonia R1/R2.

## Working Decision — Current Structure

```text
Corporate Resources & Enterprise Services

  Financial & Commercial Services
    Clinical Activity Charging Information Provision

  Corporate Support Services
    Back-Office Services
    Facilities Management
    IT Infrastructure Management
    Communications & Publicity
    People Management
    Operational Oversight
```

## Clinical Activity Charging Information Provision

**Clinical Activity Charging Information Provision** is the
organisational ability to identify and provide information about
chargeable or cost-relevant clinical and health-service activity where
such information is required for applicable financial, funding or
charging purposes.

This is deliberately narrow for R1/R2.

Harmonia may need to capture/provide utilisation evidence such as
referral forwarding, diagnostic requests, applicable
referral/diagnostic/service type, quantities or other contextual usage
information.

The boundary SHALL be preserved:

> Harmonia may provide evidence of relevant activity and utilisation;
> it does not thereby become responsible for pricing, billing, claiming,
> accounting or revenue management.

Not every Harmonia-mediated activity is presumed chargeable.

This note does not establish comprehensive Medicare,
private-health-insurance, activity-based-funding, billing, claims or
revenue-cycle architecture.

## Corporate Support Services

These are enterprise capabilities whose resources, availability, state
or constraints may materially affect Harmonia-enabled service activity.
They SHALL NOT be dismissed as merely irrelevant background capabilities.

- **Back-Office Services** — administrative/support services upon which
  health-service activity may depend.
- **Facilities Management** — physical facilities and facility services
  affecting delivery, including applicable location/resource/availability
  context such as printers, service-delivery centres and facility state.
- **IT Infrastructure Management** — operation and availability of
  underlying technology infrastructure upon which Harmonia or
  health-service activity may depend.
- **Communications & Publicity** — organisational communication/distribution
  arrangements that may affect how information is published, distributed,
  forwarded or otherwise made available.
- **People Management** — broader organisational management of people
  upon whom services depend, distinct from the clinical/practitioner-specific
  Health Workforce Management capabilities.
- **Operational Oversight** — observation of broader operational
  conditions where those conditions affect organisational ability to
  perform work or deliver services.

The Harmonia relevance boundary is:

> Harmonia does not need to own a Business Capability for that capability
> to be relevant to Harmonia. Harmonia may need to consume information
> about the capability's resources, state, availability or constraints
> in order to make or support an operational decision.

These CT3s SHALL NOT be decomposed further for R1/R2 unless a later
requirement makes such decomposition architecturally material. This
working note does not change canonical relevance classifications or
resolve **OI-BC-002**.

------------------------------------------------------------------------

# DN-BC-036 — Health Service Governance — Framework and Compliance

## Context

Current reconciliation of Health Service Governance.

## Working Decision — Current Structure

```text
Health Service Governance

  Governance Framework Management
    Policy / Obligation Governance
    Authority & Constraint Governance

  Compliance Governance
    Compliance Assessment Definition
    Governance Reporting & Checkpoint Definition
```

The primary distinction is:

> Governance Framework Management = establishing the WHAT.

> Compliance Governance = making sure the WHAT is followed and reported on.

## Governance Framework Management

**Policy / Obligation Governance** is the organisational ability to
identify, interpret, establish and maintain authoritative obligations
arising from applicable legislation, regulation, policy, standards,
guidelines, contractual requirements and other governing requirements.

The obligation definitions are critical organisational accountability
artefacts and must remain governed, attributable and traceable to their
applicable sources/authority.

**Authority & Constraint Governance** establishes and maintains who is
authorised to make, approve or govern particular organisational decisions
and artefacts, together with the constraints under which that authority
may legitimately be exercised.

This includes applicable engagement with regulators, standards bodies,
compliance bodies and other external authorities to establish legitimate
working parameters.

The shorthand distinction is:

- **Obligation** — What are we required to do or satisfy?
- **Authority** — Who may decide or approve?
- **Constraint** — Within what bounds may that authority be exercised?

## Compliance Governance

Rename the previous candidate **Compliance Obligation Definition** to
**Compliance Assessment Definition**.

**Compliance Assessment Definition** is the organisational ability to
define how compliance with established obligations, authorities and
constraints will be evaluated, including the evidence, measures,
criteria, controls and assessment arrangements necessary to determine
whether organisational activity sufficiently satisfies those requirements.

The distinction SHALL be preserved:

> Obligation definition establishes what must be satisfied; Compliance
> Assessment Definition establishes what constitutes sufficient basis
> for determining that it is being satisfied.

**Governance Reporting & Checkpoint Definition** establishes where/how
applicable compliance is checked and what governance/compliance
information must be reported.

Compliance Governance SHALL NOT be collapsed into Assurance.

------------------------------------------------------------------------

# DN-BC-037 — Health Service Assurance — Specification and Evaluation

## Context

Current reconciliation of Health Service Assurance at Business Capability
level.

## Working Decision — Current Structure

```text
Health Service Assurance

  Health Service Assurance Specification
    Assurance Approach Definition
    Assurance Evaluation Criteria Governance

  Assurance Evaluation
    Assurance Evidence Assessment
    Assurance Conclusion Determination
```

**Health Service Assurance** is the organisational ability to establish
and maintain justified confidence that health services can be, and are
being, delivered in accordance with applicable expectations, obligations,
constraints and required levels of performance.

The macro-level subject is the organisation's ability to deliver health
services.

## Health Service Assurance Specification

**Assurance Approach Definition** establishes how service-delivery
assurance will be undertaken, including applicable scope, evidence
expectations, responsibilities, methods, degree of independence and
timing/frequency.

**Assurance Evaluation Criteria Governance** establishes and maintains
the criteria against which sufficient assurance can be determined.

Those criteria may draw upon any materially relevant concern, including
workforce, facilities, clinical practice, information, technology,
security, privacy, governance, dependencies and operational state.

## Assurance Evaluation

**Assurance Evidence Assessment** evaluates available evidence against
the established assurance approach and criteria.

**Assurance Conclusion Determination** establishes the assurance
conclusion legitimately supported by that evidence, including applicable
insufficiency, qualification or unresolved concerns.

## Governance and Assurance Boundary

The distinction from **DN-BC-036** SHALL remain explicit:

> Health Service Governance establishes what the organisation must do
> and how adherence will be determined and governed.

> Health Service Assurance establishes whether sufficient evidence exists
> to give justified confidence in the organisation's ability to deliver
> its health services appropriately.

## Modelling and Derivation Guardrails

At Business Capability level, assurance SHALL NOT be split into separate
security, information, process, technology or clinical assurance
capability trees merely because those concerns may contribute evidence.
Drawing criteria or evidence from those concerns does not create separate
Business Assurance Capabilities.

This structure SHALL NOT prematurely derive Business Enabling Capability
or component ownership.

## Canonical Reconciliation Consideration

The existing [canonical Health Service Assurance definition](../02-strategy/capabilities/business-capabilities.md#18-health-service-assurance)
defines independent evaluation of governed subjects, while this working
definition frames justified confidence in the organisation's ability to
deliver health services. That difference requires explicit reconciliation
before promotion, including the canonical independence and Clinical
Services Delivery Assurance boundaries.

This note does not replace the canonical definition, vary
[REQ-FND-005's independence requirement](../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity),
transfer clinical-adequacy responsibility to Harmonia, or remap the
existing approved Assurance Design, Assurance Criteria Management and
Governed Assurance enabling responsibilities.

------------------------------------------------------------------------

# Open Adjudication Queue

The v0.2 Business Capability pass has now been substantially adjudicated
as approved **working decisions**, captured through **DN-BC-037**.
This is not canonical promotion: the scratchpad remains non-authoritative
working architectural memory.

**OI-BC-001** and **OI-BC-002** remain unresolved. Outstanding open issues,
provisional candidates and parked matters in earlier notes are not closed
by this housekeeping update; they require their own explicit adjudication.

The next major activity is the following reconciliation sequence:

1.  Reconcile the approved working Business Capability decisions into
    canonical Markdown, preserving the definitions, boundaries,
    interpretive warnings and unresolved issues. Reconcile differences
    with existing canonical scope and check global Capability-name
    uniqueness as part of that work.
2.  Reconcile the Business Capability representation in RADS only after
    canonical documentation is coherent.
3.  Then construct/reconcile the Business Enabling Capability
    CT1 → CT2 → CT3 hierarchy.
4.  Then frame the established Features beneath their owning Business
    Enabling Capability CT3s.
5.  Then establish Business Capability ↔ Business Enabling Capability
    enablement relationships through explicit architectural reconciliation.
6.  Domain05 B2.2 remains parked until that reconciliation is sufficiently
    complete.

This queue records future work. It does not authorise canonical edits,
RADS JSON/schema changes, enabling-capability ancestry, Feature
ownership/framing, Canonical IDs or Domain05 traceability in this capture.

------------------------------------------------------------------------

# Promotion / Reconciliation Reminder

Before material from this scratchpad is promoted:

1.  Re-read the applicable canonical Markdown.
2.  Confirm that the working decision has actually been adjudicated.
3.  Preserve unresolved uncertainty rather than silently filling gaps.
4.  Incorporate the decision, definition, boundary and any material
    interpretive warning into the appropriate canonical source.
5.  Update RADS only as part of an explicitly authorised semantic
    reconciliation.
6.  Do not allocate Canonical IDs merely because the conceptual tree has
    been agreed.
7.  Do not infer Business Capability ↔ Business Enabling Capability
    relationships from contextual justification alone.
8.  Do not resume Domain05 traceability merely because a candidate
    Capability name exists.

Once promoted, this scratchpad may retain the note as historical
reasoning, but the canonical Markdown becomes the architectural
authority.

-----------------------------------------------------------------------
# Open Issues

-----------------------------------------------------------------------

## OI-BC-001 — Medication Compounding and Preparation

**Context:**

`Diagnostic, Therapeutic & Clinical Support Services → Medication & Pharmacy Services`

**Related Decision:**

`DN-BC-013 — Medication & Pharmacy Services Decomposition`

**Question:**

Does medication compounding and/or specialised medication preparation represent a materially distinct CT3 Business Capability beneath **Medication & Pharmacy Services**?

**Current Standing:**

**Unresolved.**

Medication compounding or specialised preparation has neither been established nor rejected as a CT3 Business Capability.

Its absence from the currently agreed decomposition MUST NOT be interpreted as a decision that it is:

- subsumed by Prescription Medication Dispensing;
- irrelevant to the Business Architecture;
- outside Medication & Pharmacy Services; or
- represented by another established Capability.

**Required Adjudication:**

Determine whether medication compounding/preparation represents a sufficiently distinct business ability to require its own CT3 Capability at the chosen Harmonia Business Architecture scope.

Any eventual decision should comply with `DN-BC-010 — Decomposition Depth Must Not Distort Peer Completeness`.

-----------------------------------------------------------------------

## OI-BC-002 — Health Service Operational Readiness Placement

### Issue

A distinct Business Capability has been identified concerning the **operational readiness of a Health Service Offering for delivery from a particular location or operational context**.

The capability is provisionally named:

**Health Service Operational Readiness**

Its semantic identity is sufficiently understood to preserve it within the working architecture; however, its appropriate CT2 parent and therefore its canonical Capability ancestry have **not yet been established**.

It SHALL NOT be forced beneath an existing CT2 merely to complete the current decomposition.

---

## Candidate Capability Definition

**Health Service Operational Readiness** concerns the operational oversight necessary to determine whether a defined Health Service Offering can actually be delivered within a particular site, location or operational context.

It considers whether the combination of resources, supporting services and environmental conditions necessary for delivery are available and suitable.

These may include:

- workforce coverage;
- administrative and front-of-house support;
- rooms and other physical spaces;
- furniture and general equipment;
- Clinical Devices;
- clinical materials and supplies;
- utilities;
- ICT and communications;
- supporting operational services; and
- other dependencies required for delivery of the Health Service Offering.

The Capability does **not** own or directly manage each of these underlying resources.

Instead, it consumes the outcomes of the applicable workforce, facilities, utilities, technology, logistics, Clinical Technology and other supporting capabilities in order to establish and maintain operational readiness for Health Service delivery.

---

## CT3 Boundary

The semantic responsibility is:

> **Can this Health Service Offering actually be delivered from this operational context?**

The capability owns the collective operational-readiness concern.

It does **not** own:

- definition of the Health Service Offering;
- workforce administration or rostering;
- facilities management;
- utilities management;
- Clinical Device management;
- inventory or logistics management;
- ICT service management;
- individual Care-Place Readiness; or
- the clinical activity subsequently performed through the Health Service.

Those capabilities provide conditions or resources upon which Health Service Operational Readiness depends.

---

## Ongoing and Instance Readiness

The capability primarily concerns the **ongoing ability of a site or operational context to provide a Health Service Offering**.

For example:

> Can Site X operationally support delivery of Service Y?

The same capability may also be exercised in relation to a particular service-delivery instance or session:

> Are the necessary operational conditions satisfied for today's delivery of Service Y?

The latter is presently considered an **instance-level exercise of the same capability**, rather than evidence of a separate Business Capability.

This remains subject to later architectural evidence.

---

## Important Semantic Distinctions

The following distinctions SHALL be preserved:

> **Health Service Offering defined ≠ Health Service operationally ready for delivery.**

> **Individual supporting resource available ≠ Health Service collectively ready for delivery.**

> **Ongoing service/site readiness ≠ execution of a particular clinical activity.**

> **Operational readiness oversight ≠ ownership of the resources upon which readiness depends.**

> **Care-Place Readiness ≠ Health Service Operational Readiness.**

A room, device, workforce resource or other individual dependency may be ready while the Health Service as a whole remains unable to operate.

Conversely, Health Service Operational Readiness depends upon other capabilities without absorbing their responsibilities.

---

## Relationship to Healthcare Service Offering Management

**Healthcare Service Offering Management** answers principally:

> **What identifiable Health Service does the organisation offer, by whom,
> where, when, and under what applicable conditions or constraints?**

As recorded in **DN-BC-034**, temporal and conditional availability are
material to the offering:

> **Service exists ≠ service offered at this location ≠ service available
> at this time/context.**

**Health Service Operational Readiness** answers principally:

> **Can that Health Service Offering actually be delivered here under the applicable operational conditions?**

The existence or applicable temporal/conditional availability of a defined
Health Service Offering therefore does not establish that the resources
necessary to deliver it are presently operationally ready.

---

## Relationship to Health Service Alignment and Corporate Support Services

**Health Service Alignment**, as recorded in **DN-BC-032**, concerns how
a health service fits within and interacts with the wider service
ecosystem, including co-dependencies, required resources, financial
arrangements and surrounding implementation changes. It coherently
positions a new or changed service within the service environment
represented by the catalogue; it does not establish the authoritative
service catalogue or, by that positioning alone, present operational
readiness.

**Corporate Support Services**, as recorded in **DN-BC-035**, provide
enterprise context whose resources, availability, state or constraints
may materially affect service delivery. Health Service Operational
Readiness may consume such context without owning those supporting
capabilities or their resources.

Neither service alignment nor relevance of corporate support establishes
the CT2 parent of Health Service Operational Readiness. Its placement
remains unresolved and SHALL NOT be inferred from these relationships.

---

## Placement Unresolved

The appropriate CT2 parent for Health Service Operational Readiness is deliberately unresolved.

Potential placement MUST be considered against the completed Business Capability model rather than inferred from the capability currently under review.

In particular, this capability SHALL NOT presently be asserted as subordinate to **Health Product Management** merely because it was identified while reviewing that branch.

Likewise, similarity to existing Care Access & Coordination, Care-Place Readiness, Health Service Operations or service-administration concepts SHALL NOT establish ancestry without explicit adjudication.

### Resolution Required

During subsequent Business Capability reconciliation:

1. identify the CT2 whose semantic responsibility properly contains Health Service Operational Readiness;
2. determine whether an existing CT2 provides that parent or whether the model exposes a genuine structural gap;
3. verify that placement does not duplicate Care-Place Readiness, Care Resource Deployment, Operational Care Capacity Management or Healthcare Service Offering Management; and
4. only then establish its canonical CT1 → CT2 → CT3 ancestry.

Until that occurs:

> **Health Service Operational Readiness is an identified candidate Business Capability with unresolved canonical placement.**
