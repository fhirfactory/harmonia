# Health Service Assurance — Bounded Business Architecture Derivation

## 1. Authority and Derivation Scope

This view carries the [approved Domain02 Health Service Assurance derivation](../../02-strategy/capability-maps/health-service-assurance-derivation.md) into Domain03. Its upstream responsibility chain is [REQ-FND-005 — Independent Assurance of Governed Activity](../../01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity) → [BC-18 Health Service Assurance](../../02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) → **Assurance Design**, **Assurance Criteria Management** and **Governed Assurance**, with the approved collaborative Enterprise Capability contributions including **EC-14 Service Guardian**.

[AX-14](../../../architectural-axioms.md#ax-14--semantic-distinctions-are-preserved) requires the responsibility distinctions below to remain explicit. [AX-17](../../../architectural-axioms.md#ax-17--architectural-authority-and-explicit-uncertainty) and the [Domain03 metamodel](../metamodel/business-architecture-metamodel.md) require established responsibility to be distinguished from unestablished naming, decomposition, exposure, progression and authority relationships. AX-06, AX-07, AX-08 and AX-09 continue to govern information authority, governed access, meaningful evidence and contextual evidence selection. AX-15 governs operational uncertainty without making it equivalent to insufficient assurance evidence.

The approved human review decisions on 2026-10-08 extend the previous bounded derivation with **[Service Assurance Modeller](../actors-roles/roles.md#service-assurance-modeller)** and **[Service Guardian](../actors-roles/roles.md#service-guardian)** Business Roles and exactly five Business Functions: **Design Service Assurance**, **Manage Assurance Criteria**, **Establish Assurance Context**, **Assess Assurance Evidence** and **Adjudicate Assurance Assessment**. Their capability ownership and Role performance are documented in §3. The assurance Role previously named Guardian is now Service Guardian; legal/personal-welfare Guardian remains unchanged. These naming and Function-decomposition decisions are resolved; further decomposition is not approved.

The next approved human review decisions on 2026-10-08 establish exactly three [Business Processes](../processes/business-processes.md#6-health-service-assurance-processes), three [Business Services](#5-approved-governed-assurance-business-services) and three [Business Interactions](../collaborations-interactions/interactions.md#5-approved-service-assurance-interactions), with the approved consumer/participant relationships. They also establish the [Financial Governance deliberate exclusion](#financial-governance-exclusion) and [assurance non-recursion boundary](#assurance-non-recursion-boundary). The five approved Functions are preserved without reinterpretation or further decomposition.

The three assurance capabilities remain established responsibility contexts with unresolved contextual-view placement, Capability Tier, complete ancestry, Feature decomposition and structural Canonical IDs. This document is a capability-scoped view, not a sixth contextual view or an identifier ancestor. Specific Actors and Collaborations remain unallocated; detailed Process states/transitions, information models and implementation remain unresolved.

**EC-14 Service Guardian is an Enterprise Capability. Service Guardian is also the intentional canonical name of a distinct Business Role.** Shared terminology does not equate their architectural types or make either a software component. The approved human review decisions supply the Business Architecture relationships; they are not inferred from the EC contribution model or its explanatory sequence. Previous completion reports supply execution context only and are not architectural authority.

## 2. Governance, Management, Guardianship and Clinical Authority

```text
Governance defines
       ↓
Management performs
       ↓
Guardianship assures
```

This expresses semantic responsibilities, not organisational structure, a compulsory execution sequence or technical topology.

| Responsibility | Business meaning | Preserved boundary |
| :--- | :--- | :--- |
| **Governance** | Defines applicable requirements, obligations, authority, constraints, controls, reporting requirements and checkpoints. | Establishes the normative envelope without taking over the operational means or progression of the governed activity. Subject-specific governance remains intrinsic to its established subject capability. |
| **Operational management** | Performs or manages the subject activity within its constraints, including monitoring for progression, assignment, delegation, operational escalation, response, remediation and expected failure/recovery. | Monitoring for operational completion or deciding what happens next does not establish independent assurance. |
| **Service Assurance modelling** | Service Assurance Modeller determines and models how satisfaction of governed requirements, constraints and expected behaviours is to be assured within Assurance Design and Assurance Criteria Management. | Modelling does not inherently approve the governing requirement, Assurance Definition or Assurance Criteria, perform independent assurance, or manage the subject. |
| **Guardianship / governed assurance** | Service Guardian independently evaluates governed activity, information, state or outcomes against applicable criteria and establishes findings and conclusions from sufficient trustworthy evidence. | Service Guardian may manage its own assurance activity; it does not thereby perform or manage the subject, direct its operational response or confer clinical authority. |
| **Clinical review / clinical assurance** | Exercises the applicable clinical process and authority's responsibility for clinical evaluation and professional judgement. | Neither assurance Role establishes clinical adequacy, clinical correctness or professional clinical judgement. Clinical quality, safety and improvement retain their existing responsibilities. |

**Governance Authority establishes what is authoritative. Service Assurance Modeller models how satisfaction will be assured. Service Guardian independently establishes what the applicable evidence demonstrates.** Governance Authority references the [existing mandated authority Roles](../actors-roles/roles.md#37-governance-authority-and-assurance-modelling), including Policy Authority for policy and Regulator for statutory authority, with existing steward mandates preserved. No new Governance Authority Role or extension of those mandates is established. Modelling does not grant approval authority for the governing requirement, Assurance Definition or Assurance Criteria; exact approval allocation/workflows remain unresolved.

```mermaid
graph TD
    GOV["Governance Authority<br/>Existing mandated authority Roles"] -->|Establishes authoritative basis| MOD["Business Role<br/>Service Assurance Modeller"]
    MOD -->|Performs| DESIGN["Function: Design Service Assurance<br/>Owner: Assurance Design"]
    MOD -->|Performs| CRITERIA["Function: Manage Assurance Criteria<br/>Owner: Assurance Criteria Management"]
    MOD -->|Models| DEF["Assurance Definition<br/>Conceptual modelling output"]
    DEF -->|Modelling boundary for assurance| GUARD["Business Role<br/>Service Guardian"]
    GUARD -->|Performs| CONTEXT["Function: Establish Assurance Context<br/>Owner: Governed Assurance"]
    GUARD -->|Performs| ASSESS["Function: Assess Assurance Evidence<br/>Owner: Governed Assurance"]
    GUARD -->|Performs| ADJ["Function: Adjudicate Assurance Assessment<br/>Owner: Governed Assurance"]
    ADJ -->|Establishes| FINDING["Assurance Finding / Conclusion"]
```

This is a responsibility/semantic model, not mandatory execution topology, organisational or application structure, deployment topology or a complete Business Process. The output labels do not introduce Information Objects or a formal information model. The diagram distinguishes performance through a Role from Function ownership by a Capability and does not define process ordering or an assurance initiation/approval exchange.

A real-world Actor may fulfil different Roles in different contexts where governance permits. That possibility does not relax REQ-FND-005: assurance progression and conclusion must remain independently governed rather than controlled by the performer or manager of the subject. The subject may contribute evidence but must not solely determine, suppress, manufacture or retrospectively alter its assurance conclusion. Evidence dependency and control dependency remain distinct.

Neither Service Assurance Modeller nor Service Guardian inherently possesses clinical authority or replaces professional clinical judgement, clinical peer review or clinical governance. Information and behaviour concerning clinical activity may be subject to governed assurance where an explicit applicable requirement establishes that concern. This does not constitute **Clinical Services Delivery Assurance** or transfer clinical-care responsibility to Guardianship or Harmonia. The [approved clinical boundary](../../02-strategy/capabilities/business-enabling-capabilities.md#clinical-services-delivery-assurance-boundary), [Service Delivery care-enablement principle](03-service-delivery.md#the-fundamental-care-enablement-principle), and existing clinical review responsibilities remain controlling. No generic clinical-assurance authority Role is derived.

Neither assurance Role manages the subject merely because it designs or performs assurance. Service Guardian may manage progression of its own assurance activity without assuming progression of its subject. Findings may cause another responsible party to initiate management, escalation, remediation or other operational action; no such response responsibility is allocated to Service Guardian.

## 3. Direct Capability-Scoped Responsibility Consequences

The following five Functions are approved Business Architecture. Each is behaviour within its established owning Capability, performed through the stated Business Role. **Element Type: Business Function (FN)** applies to each. **Canonical IDs remain unresolved** because Capability Tier, complete ancestry and local identifier allocation are unestablished. **Feature association is not established** for any of these Functions; no Feature or structural ID is invented to anchor them. Function ownership does not transfer source-information ownership to an assurance Role or Capability.

### 3.1 Assurance Design — Service Assurance Modeller

- **Owning Capability**: [Assurance Design](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-design).
- **Performing Business Role**: [Service Assurance Modeller](../actors-roles/roles.md#service-assurance-modeller).

<a id="design-service-assurance"></a>

#### Design Service Assurance

- **Canonical Name**: `Design Service Assurance`.
- **Definition**: Defines how satisfaction of a governed requirement, constraint or expected behaviour is to be independently established, including the applicable assurance disposition, assurance criteria, required evidence and evaluation expectations.
- **Boundary**: Concerns assurance design. It does not establish the underlying governance requirement, grant authority to it, perform the resulting assurance, manage the subject activity or prescribe application or implementation mechanisms. Modelling the resulting Assurance Definition does not grant approval authority. The Strategy peer relationship to behaviour design and failure/recovery design creates no additional Capability or Function.

### 3.2 Assurance Criteria Management — Service Assurance Modeller

- **Owning Capability**: [Assurance Criteria Management](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-criteria-management).
- **Performing Business Role**: [Service Assurance Modeller](../actors-roles/roles.md#service-assurance-modeller).

<a id="manage-assurance-criteria"></a>

#### Manage Assurance Criteria

- **Canonical Name**: `Manage Assurance Criteria`.
- **Definition**: Establishes and manages reusable, governed and temporally identifiable assurance criteria through which governed subjects may be evaluated.
- **Scope**: Criteria may express governance rule boundaries, quality constraints, performance expectations, compliance criteria, temporal criteria, evidence requirements or completeness expectations. These examples are not an exhaustive taxonomy. Criteria may have lifecycle, version and temporal applicability.
- **Boundary**: Managing a criterion does not transfer authority for the governing requirement from which it may derive. Criteria may operationalise a requirement without silently altering, weakening, strengthening or replacing it; not every criterion must derive from formal Governance. Modelling/managing criteria does not inherently confer approval authority. Detailed criterion lifecycle states and approval workflows remain unresolved.

### 3.3 Governed Assurance — Service Guardian

- **Owning Capability**: [Governed Assurance](../../02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance).
- **Performing Business Role**: [Service Guardian](../actors-roles/roles.md#service-guardian).

<a id="establish-assurance-context"></a>

#### Establish Assurance Context

- **Canonical Name**: `Establish Assurance Context`.
- **Definition**: Establishes the governed subject, applicable assurance purpose and criteria, relevant temporal context, and contextual association of sufficient trustworthy source information as evidence required for an assurance activity.
- **Scope**: Includes the evidence-assembly responsibility previously described as Assurance Evidence Assembly; no separate evidence-assembly Function is established. Sufficiency is required for conclusion, not assumed merely because context is established; unavailable or insufficient evidence remains explicit.
- **Information Boundary**: Source information remains owned by its responsible source Capability. Its use as evidence does not transfer ownership or originating authority. Governed Assurance owns the contextual association that particular information constitutes evidence for a particular assurance purpose. Evidence association and applicable criteria may each be temporal and version-specific; association distinguishes the information state relevant to assurance from its current state.
- **Realisation Boundary**: Uses generic EC-02 context-management capability together with EC-14 assurance-specific semantics as documented in [§4.1](#41-context-management-contribution). Establishment of Assurance Context prescribes no copying, persistence, aggregation, caching or other technical mechanism.

<a id="assess-assurance-evidence"></a>

#### Assess Assurance Evidence

- **Canonical Name**: `Assess Assurance Evidence`.
- **Definition**: Evaluates the available trustworthy evidence against the applicable assurance criteria and establishes what that evidence demonstrates for each relevant criterion or assessment concern.
- **Boundary**: Assessment remains distinct from adjudication. It may identify satisfaction, exception, insufficiency or other criterion-level results where applicable; no universal result taxonomy is established. Quantitative, qualitative, compound, temporal and confidence-bearing assessment remain possible. **Insufficient evidence is not equivalent to either satisfaction or non-satisfaction.**

<a id="adjudicate-assurance-assessment"></a>

#### Adjudicate Assurance Assessment

- **Canonical Name**: `Adjudicate Assurance Assessment`.
- **Definition**: Determines the assurance finding or conclusion that follows from the assessed evidence and applicable assurance criteria.
- **Boundary**: Establishment of the finding/conclusion is part of adjudication. It remains distinct from evidence assessment, operational response, remediation, escalation, workflow progression and clinical judgement. No separate conclusion-establishment or reporting Function is introduced merely because a conclusion is established or may be communicated.

### 3.4 Preserved Assurance Obligations

The approved [assurance dispositions](../../02-strategy/capabilities/business-enabling-capabilities.md#assurance-disposition) remain explicit governed decisions. **Not Assurable** does not remove the requirement; **Not Worth Assuring** does not make it optional. Disposition does not require specific assurance of every individual activity, information item or outcome.

The approved [generic processing assurance obligation](../../02-strategy/capabilities/business-enabling-capabilities.md#generic-processing-assurance-and-system-operations) constrains Harmonia-managed workflow behaviour. Evaluating business progression, completion, timeliness, pathways, failure/recovery and outcomes for assurance is distinct from monitoring to progress that work. The existing operational Processes and Functions retain their owners; neither successful completion nor an operationally healthy environment establishes an assurance conclusion. No final criterion catalogue or applicability model is supplied by these examples.

Assessment asks what the evidence demonstrates against applicable criteria; adjudication establishes what finding or conclusion follows. The approved decisions now establish these as distinct Functions performed through Service Guardian; they do not require separate Roles or process stages. Assurance is not universally reducible to PASS / FAIL / UNKNOWN. An assurance activity may execute correctly while lacking enough evidence to conclude. That insufficiency is distinct from its own execution failure or indeterminate execution outcome and must not imply satisfaction or non-satisfaction.

## 4. Information and Dependency Consequences

The [Business Information Responsibility material](../information-responsibility/information-responsibility.md#4-assurance-related-business-information-responsibilities) records the directly supported demarcations: Assurance Design defines how satisfaction will be assured; Assurance Criteria Management manages criteria; Governed Assurance owns contextual evidentiary association and its findings/conclusions. Underlying source information remains owned and managed by its established capability. Evaluating it does not acquire its originating authority.

Evidence association must distinguish the relevant information state from its current state where temporal/version-specific inclusion matters. Evaluation must use temporally appropriate evidence and criteria. Evidence collection, provenance, audit information and operational completion are not themselves assurance conclusions. No assurance Information Object, outcome taxonomy, representation, custody, retention rule or persistence structure is defined here.

An evidence need does not identify a Business Service/provider or authorise a new consumption edge. The [dependency assessment](../dependencies/cross-capability-dependencies.md#4-assurance-dependency-derivation-boundary) preserves that distinction, including the prohibition on subject control of assurance progression or conclusion. The approved Enterprise Capability contribution matrix remains a Strategy contribution model, not a Business Service consumption matrix.

### 4.1 Context Management Contribution

[EC-02 Context Management](../../02-strategy/capabilities/enterprise-capabilities.md#ec-02-context-management) supplies generic establishment, validation, propagation and preservation of meaningful context. [EC-14 Service Guardian](../../02-strategy/capabilities/enterprise-capabilities.md#ec-14-service-guardian) supplies assurance-specific subject/purpose, criterion applicability and contextual evidentiary-association semantics. Their contributions together support **Establish Assurance Context**, owned by Governed Assurance and performed through the Service Guardian Business Role.

```mermaid
graph TD
    CONTEXT["Enterprise Capability<br/>EC-02 Context Management"] -->|Generic context semantics| FN["Business Function<br/>Establish Assurance Context<br/>Owner: Governed Assurance"]
    ASSURANCE["Enterprise Capability<br/>EC-14 Service Guardian"] -->|Assurance-specific semantics| FN
```

This documents the approved semantic contribution relationship. In the [Domain02 collaborative contribution matrix](../../02-strategy/capability-maps/health-service-assurance-derivation.md#collaborative-enterprise-capability-contribution-matrix), EC-02 and EC-14 each contribute directly to Governed Assurance. Neither is a sole realiser or Function owner; the other approved capability contributions remain intact. This view is not an exhaustive Function-to-EC allocation matrix, a new Business Service consumption edge or an application/runtime allocation. How context establishment is realised belongs to later architecture; no loader/unloader mechanism or implementation pattern is derived.

### 4.2 Assurance Definition — Conceptual Modelling Output

**Assurance Definition** describes the conceptual output/boundary between Service Assurance modelling and governed assurance: how satisfaction is to be assured, including assurance disposition, applicable criteria, required evidence and evaluation expectations. This output description follows directly from **Design Service Assurance** within **Assurance Design**, performed through **Service Assurance Modeller**; governed assurance uses that modelling boundary without transferring governance authority or establishing an exposed Service or approval exchange.

The existing Domain03 rules permit documenting this business output and its responsibility context. They do not establish its formal information structure, identity, lifecycle, representation or approval model. The term therefore remains a conceptual responsibility/output description, not a newly allocated formal Business Information element, Information Object or Data Object. Formal information modelling is deferred. Modelling an Assurance Definition does not make it authoritative or grant its modeller approval authority; applicable governance authority must establish that standing.

## 5. Approved Governed Assurance Business Services

Exactly three assurance Services are established below. **Element Type: Business Service (SV)** applies to each; each is owned/exposed by **[Governed Assurance](../../02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance)** with Service Guardian participating through its associated Business Interaction. Identifiable consumers/recipients outside that responsibility boundary justify the exposure under the metamodel. **Feature association is not established** and **Canonical IDs remain unresolved**; no Capability Tier, ancestry or local code is invented.

The Services expose approved request, activity-progression observation and established-outcome communication behaviour. They do not mechanically correspond one-to-one with the five Functions, nor introduce new request, status or reporting Functions. Detailed internal Service-to-Function mappings and contracts beyond the approved behaviour remain unestablished. Status observation concerns assurance's own permitted activity progression, not assessment of the subject or a new status Process.

<a id="request-service-assurance"></a>

### 5.1 Request Service Assurance

- **Canonical Name**: `Request Service Assurance`.
- **Definition**: Enables an authorised consumer to request that Service Assurance be performed for an applicable governed subject in accordance with an established Assurance Definition.
- **Approved Consumers**: `Care Coordinator`, `Service Coordinator`, `Regulator`, `Policy Authority`, `System Steward`.
- **Associated Interaction**: [Service Assurance Request](../collaborations-interactions/interactions.md#service-assurance-request), with Service Guardian and one authorised requesting Role.
- **Process Relationship**: Requests [Assurance Process Execution](../processes/business-processes.md#assurance-process-execution) under an applicable established Assurance Definition. It does not require a new Assurance Process Design instance for each request.
- **Independence Boundary**: The requesting Role identifies the assurance need within its responsibility and authority. Requesting does not grant authority to define or alter the Assurance Definition, alter Assurance Criteria, direct evidence assessment, determine adjudication or control the resulting Assurance Finding / Conclusion. REQ-FND-005 control independence remains applicable, including where a requester performs or manages the subject. Detailed payloads, contracts, initiation mechanics and states remain unresolved.

<a id="request-service-assurance-status"></a>

### 5.2 Request Service Assurance Status

- **Canonical Name**: `Request Service Assurance Status`.
- **Definition**: Enables the System Steward to obtain the current business progression state of an identifiable Service Assurance activity for stewardship purposes.
- **Only Approved Consumer**: `System Steward`.
- **Associated Interaction**: [Service Assurance Status](../collaborations-interactions/interactions.md#service-assurance-status), between System Steward and Service Guardian.
- **Status Boundary**: Concerns **activity progression state**. It does not communicate provisional findings, incomplete assessment results as conclusions, predicted outcomes or unadjudicated conclusions. Other requesting Roles do not gain status access merely because they requested assurance. No detailed activity-status taxonomy is established.

**Service Assurance Status answers "where is the assurance activity in its progression?" It does not answer "what has the assurance established?"** The latter requires an adjudicated Assurance Finding / Conclusion and subsequent outcome communication; neither status nor execution completion establishes that conclusion.

**Management Monitoring evaluates activity state in order to progress or manage activity. Assurance evaluates activity against governing criteria in order to establish an assurance conclusion.** System Steward's use of this Service is stewardship / management observation of assurance progression, not itself assurance or recursive assurance. The non-recursion boundary in §7.2 applies.

<a id="communicate-service-assurance-outcome"></a>

### 5.3 Communicate Service Assurance Outcome

- **Canonical Name**: `Communicate Service Assurance Outcome`.
- **Definition**: Communicates applicable established Assurance Findings and Conclusions to authorised recipients according to their responsibilities, authority and information requirements.
- **Approved Potential Recipients**: `Care Coordinator`, `Service Coordinator`, `Regulator`, `Policy Authority`, `System Steward`.
- **Associated Interaction**: [Service Assurance Outcome Communication](../collaborations-interactions/interactions.md#service-assurance-outcome-communication), with Service Guardian and one or more authorised recipient Roles.
- **Process Relationship**: Exposes the communication behaviour of [Assurance Process Reporting / Communication](../processes/business-processes.md#assurance-process-reporting-communication), beginning from an already established finding/conclusion. It does not perform assessment or adjudication or alter/reinterpret the established outcome. A documentary report is not required.
- **Recipient Boundary**: No mandatory broadcast is established. A particular outcome may be communicated to one, some or all legitimate recipients according to applicability, authority, responsibility and information requirements. Requester, status consumer and outcome recipient need not be the same Role. Communication does not transfer responsibility for recipients' interpretation within their authority, operational/clinical management, remediation, escalation, policy/compliance response, process improvement or other subsequent action to Service Guardian.

### 5.4 Approved Consumer and Recipient Concerns

The [existing Role definitions and mandates](../actors-roles/roles.md) remain controlling. These are approved consumer/recipient concerns, not exhaustive Role responsibilities or new Roles. Each use requires applicable authority; Role eligibility alone does not grant unrestricted information access.

| Business Role | Request Service Assurance | Request Service Assurance Status | Potential recipient of Communicate Service Assurance Outcome | Principal assurance concern |
| :--- | :--- | :--- | :--- | :--- |
| **Care Coordinator** | Approved within authority | Not approved | Approved where applicable/authorised | Clinical management |
| **Service Coordinator** | Approved within authority | Not approved | Approved where applicable/authorised | Operational management and potentially task / people process improvement |
| **Regulator** | Approved within authority | Not approved | Approved where applicable/authorised | Service governance / compliance |
| **Policy Authority** | Approved within authority | Not approved | Approved where applicable/authorised | Service governance / compliance and policy effectiveness / improvement |
| **System Steward** | Approved within authority | Only approved consumer | Approved where applicable/authorised | ICT process improvement and stewardship of assurance activity progression |

Care Coordinator's request or receipt may inform Clinical Management; clinical interpretation, judgement and action remain with the applicable clinical authority. Service Coordinator retains operational management/improvement responsibilities; System Steward's status access does not make Service Guardian responsible for task progression, assignment, delegation, operational escalation, remediation or process improvement. Regulator and Policy Authority retain their established governance/compliance mandates. None of these consumer relationships transfers source-information ownership, governance authority or control of assurance assessment/adjudication.

<a id="5-additional-business-elements-not-yet-established"></a>

## 6. Additional Business Elements Not Yet Established

The two assurance Roles, five Functions, three Processes, three Services, three Interactions and approved Role relationships are established. The following further concerns remain explicitly **unresolved and non-authoritative**; their inclusion does not add them to a catalogue. Financial Governance's scope exclusion and assurance non-recursion are settled boundaries, not gaps.

| Candidate concern assessed | What approved architecture establishes | Architectural decision required before adding an element |
| :--- | :--- | :--- |
| **Further Function decomposition / Feature association** | Exactly the five approved Functions in §3, including evidence assembly within context establishment and conclusion establishment within adjudication. | Additional Functions and Feature associations require a further decision. No separate applicability/criteria-resolution, evidence-assembly, conclusion-establishment, reporting, remediation or escalation Function is approved. |
| **Further Business Services / contracts** | Exactly the three Services in §5 with their approved consumers/recipients, owner and behaviour. | Additional criteria/evidence Services, exact evidence/criteria providers, detailed Service contracts and internal Service-to-Function mappings are unestablished. No extra Service is inferred from reportability, reuse or evidence need. |
| **Detailed Process progression** | Assurance Process Design, Execution and Reporting / Communication have established purposes, participating Functions where approved, conceptual boundaries and ownership. | Detailed lifecycle states/transitions, initiation mechanics, completion/disposition semantics, criteria lifecycle and approval/publication workflows remain unresolved. Function completion, EC-14's sequence and existing coordination lifecycles do not supply assurance states. |
| **Further Business Interactions** | Service Assurance Request, Status and Outcome Communication with approved Role participants and associated Services. | Evidence acquisition/exchange Interactions remain unresolved; source-owned information may be available through existing governed information access. No Evidence Contribution or Modeller-to-Guardian Interaction is created from evidence association or use of an Assurance Definition. Further exchange commitments require separate approval. |
| **Assurance Role collaborations** | Roles participate in the three approved Interactions; capability realisation is collaborative. | Establish an enduring, structured Actor/Role association with a defined purpose and permitted control relationships. Interaction does not mechanically imply Collaboration; no existing membership or new assurance collective is established. |
| **Further responsibility relationships / dependencies** | Approved request/status/outcome Role relationships, governance mandates and independence; source ownership, operational management and clinical authority remain separate. | Detailed Actor eligibility/mandates beyond approved Roles, coverage/applicability, initiation mechanics, exact approval allocation, evidence/criteria providers, findings-to-operational-response mechanisms and exact authorised external financial-governance recipients remain unresolved. Financial governance responsibility itself is deliberately excluded. |
| **Further Business Information responsibilities** | Responsibility demarcations and contextual/temporal evidence obligations in §4; conceptual Assurance Definition; progression status distinct from established findings/conclusions and their communication. | Detailed request/status/outcome information semantics, formal Assurance Definition modelling, further information concepts/asset identity, custody/lifecycle, criterion catalogues, outcome taxonomies, confidence models and retention remain unresolved. No formal information element or source-information reassignment is inferred. |

<a id="51-previous-questions-resolved-by-approved-human-review"></a>

### 6.1 Previous Questions Resolved by Approved Human Review

| Previous question | Approved resolution |
| :--- | :--- |
| Legal Guardian / assurance Guardian naming collision | **Resolved:** legal/personal-welfare Guardian remains intact; governed assurance uses the unambiguous **Service Guardian** Business Role. EC-14 retains the intentional shared name as a distinct Enterprise Capability. |
| Who models how satisfaction will be assured? | **Resolved:** **Service Assurance Modeller** performs Design Service Assurance within Assurance Design and Manage Assurance Criteria within Assurance Criteria Management, without inherent governance/approval authority. |
| Function names and bounded decomposition | **Resolved for this stage:** exactly five Functions are established in §3. Evidence assembly is included in Establish Assurance Context; finding/conclusion establishment is included in Adjudicate Assurance Assessment. No separate reporting Function is established. |
| Generic context management / assurance-specific context relationship | **Resolved to the approved extent:** EC-02 and EC-14 contribute together to Establish Assurance Context; sole realisation, Service exposure and implementation allocation are not implied. |
| Assurance Process names and purposes | **Resolved:** exactly Assurance Process Design, Assurance Process Execution and Assurance Process Reporting / Communication; detailed states/transitions remain unresolved. |
| Assurance Business Services and consumers | **Resolved:** Request Service Assurance for the five approved Roles; Request Service Assurance Status exclusively for System Steward; Communicate Service Assurance Outcome for applicable authorised recipients from the five Roles. |
| Assurance Business Interactions and participants | **Resolved:** Service Assurance Request, Service Assurance Status and Service Assurance Outcome Communication, with Service Guardian and the approved requesting/status/recipient Roles. |
| Assurance status versus outcome | **Resolved:** status concerns business progression only and exposes no unadjudicated finding/conclusion. Established-outcome communication remains separate and is not mandatory broadcast or responsibility for recipient action. |
| Financial Governance scope | **Deliberately excluded:** no distinct Harmonia financial-governance Role, Function, Capability, Service, Process or Collaboration. Financial relevance may justify authorised external receipt without responsibility transfer; exact external recipients remain unresolved. |
| Assurance recursion | **Resolved boundary:** Governed Assurance does not recursively assure its own execution; execution integrity belongs to the established activity execution framework without solution allocation. |

Capability Tier, complete ancestry, contextual-view placement, Feature placement and structural Canonical IDs remain unresolved. Assurance Definition's formal information modelling remains deferred; documenting its conceptual output does not resolve those decisions. These bounded approvals do not complete Business Architecture or freeze/refreeze Domain03.

## 7. Deliberate Scope Boundaries

<a id="financial-governance-exclusion"></a>

### 7.1 Financial Governance Exclusion

> **Financial Governance Exclusion** — Service Assurance findings may contain information that has financial relevance or may be consumed by external financial-governance activities. Harmonia does not establish a distinct Service Governance (Financials) Role, Function, Process or responsibility because financial governance is outside the presently established Harmonia business scope. Where financially relevant assurance outcomes are produced, they may be communicated to an appropriately authorised external recipient without Harmonia assuming financial-governance responsibility.

This is a deliberate scope exclusion, not an unresolved architectural gap. No Financial Governance Role, Function, Capability, Service, Process or Collaboration is created for symmetry. The allowance for authorised external receipt establishes no additional consumer Role or allocation in the approved Service/Interaction catalogues; exact external recipients and their mandates remain unresolved. Existing business responsibilities are preserved.

<a id="assurance-non-recursion-boundary"></a>

### 7.2 Assurance Non-Recursion Boundary

> **Governed Assurance does not recursively assure its own execution. Assurance activity execution integrity is provided by the established activity execution framework.**

This approved boundary distinguishes assurance execution integrity from independent evaluation of a governed subject. Managed execution/progression of an assurance activity does not make that execution another Service Assurance subject. No Assurance of Assurance, Guardian-of-Guardian, recursive Process, Function, Service or conclusion is introduced. System Steward's progression observation through Service Assurance Status is stewardship / management monitoring, not recursive assurance. The activity execution framework reference is solely a Business Architecture boundary and establishes no application/runtime responsibility allocation or implementation pattern.

<a id="6-bounded-outcome-and-navigation"></a>

## 8. Bounded Outcome and Navigation

The two assurance Roles and exactly five Functions, three Processes, three Services and three Interactions are established with approved Role relationships and capability, authority, clinical, operational and information boundaries. Further Business elements and detail remain unresolved as recorded above; Financial Governance is deliberately excluded and assurance non-recursion is established. Existing metamodel abbreviations **CT1 / CT2 / CT3 / FT** and **FN / SV / PR**, established Feature identifiers and the Qualified Architectural Reference Grammar remain unchanged. No unresolved tier, ancestry, Feature placement or structural identifier is filled to accommodate assurance.

This derivation stops in Business Architecture. Information Architecture and all application, runtime, execution, representation, persistence and deployment allocation remain downstream. The completion report is execution evidence and confers no architectural authority.

- [Domain03 orientation](../README.md)
- [Capability-scoped behaviour index](index.md)
- [Service Assurance Modeller Role](../actors-roles/roles.md#service-assurance-modeller)
- [Service Guardian Role and boundaries](../actors-roles/roles.md#service-guardian)
- [Collaboration derivation boundary](../collaborations-interactions/collaborations.md#44-assurance-role-collaboration-derivation-boundary)
- [Approved assurance Services and consumer concerns](#5-approved-governed-assurance-business-services)
- [Approved assurance Interactions](../collaborations-interactions/interactions.md#5-approved-service-assurance-interactions)
- [Approved assurance Processes](../processes/business-processes.md#6-health-service-assurance-processes)
- [Financial Governance scope exclusion](#financial-governance-exclusion)
- [Assurance non-recursion boundary](#assurance-non-recursion-boundary)
