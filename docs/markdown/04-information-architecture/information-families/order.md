# Order Information Family

**Status: G2 CANDIDATE — Block 1; not approved, CLOSED or FROZEN.**

[G2 categories A–D](../reviews/package2-g2-block1-review.md#2-review-standing) describe derivation/review standing, not approval. [G1 remains CLOSED](../reviews/package2-g1-review.md#16-controlled-g1-reconciliation-and-closure).

## 1. Purpose & Semantic Boundary

Order Administration needs to understand an attributable request/direction for bounded clinical activity or service fulfilment, determine its destination, coordinate routing, track a closed loop, coordinate requested modification/cancellation and associate returned outcomes. Diagnostic, medication and procedural orders remain in scope; the local diagnostic Process does not delimit the entire family.

```text
Order ≠ Referral ≠ HealthcareServiceDelivery ≠ ServiceOutcome
Dispatch ≠ receipt ≠ acknowledgement ≠ acceptance
Modification/cancellation requested ≠ modification/cancellation effected
Order closure / requester acknowledgement ≠ diagnostic-content verification
```

Reuse the [Healthcare Service semantic chain](healthcare-service.md#4-associated-request-mechanism-order): DeliverableHealthcareService → optional Order → HealthcareServiceDelivery → ServiceOutcome. These arrows distinguish meanings and evidenced associations, not mandatory states of a single lifecycle. Delivery may occur without an Order; an Order may remain unfulfilled.

## 2. Domain 03 Responsibility & Traceability

Use [Strategy FEAT-SA-11–14](../../02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core), [Order behaviour](../../03-business-architecture/behaviours/02-service-administration.md#6-order-administration), [Closed-Loop Order Progression Process](../../03-business-architecture/processes/business-processes.md#35-closed-loop-order-progression-process), and [Clinical Order & Closed-Loop Matrix](../../03-business-architecture/information-responsibility/information-responsibility.md#2-canonical-business-information-ownership-matrix). Supporting Clinical Order Master Record, Closed-Loop Tracking Ledger and Order-Result Correlation Matrix retain their G1 K3 meaning; they are not three new ownership-matrix entries.

[Service Request, Service Response and Service Outcome Interactions](../../03-business-architecture/collaborations-interactions/interactions.md#22-service-delivery-interactions) carry requested work, reported dispositions and returned outcomes. [Service-Delivery Collaboration](../../03-business-architecture/collaborations-interactions/collaborations.md#35-service-delivery-collaboration) supports closed-loop fulfilment where such a collaboration exists, but every Order does not create an enduring collaboration. [Requester, Prescriber, Performer and Service Provider Roles](../../03-business-architecture/actors-roles/roles.md#22-care-delivery-roles) remain distinct from local requesting/performing participation relationships and from Referrer.

Local keys below allocate no architectural Canonical IDs or ancestry. Outcome association derives from established Order Administration responsibility, not an invented Strategy Feature.

| Requirement | Established owner / Feature → Function / exposed Service / Process | Interaction → information need → candidate semantics |
| :--- | :--- | :--- |
| OR-IR1 | Order Administration / FEAT-SA-11 Order Request Ingestion → Receive Order Request / Order Requisition Ingress | Service Request → know the requested bounded work/service, subject, requesting participant and indication/specification → reuse Order, subject and qualified requester/service relationships. |
| OR-IR2 | Order Administration / FEAT-SA-12 Order Destination Resolution & Routing → Resolve Order Destination / Order Dispatch Service | Service Request transmission and Service Response → distinguish intended/resolved performing destination, routing direction and actual dispatch → reuse Order Routing Directive; qualified destination/service/endpoint associations and progression facts. |
| OR-IR3 | Order Administration / FEAT-SA-13 Order Closed-Loop Progression Tracking → Manage Order Progression / Order Status & Tracking Query / Closed-Loop Order Progression Process | Service Response / Service Outcome → correlate receipt, technical or business acknowledgement, acceptance, execution, completion and closure evidence with the originating Order → Order Progression Event and existing relationship pattern. Absence of a report does not establish failure or successful completion. |
| OR-IR4 | Order Administration / FEAT-SA-14 Order Cancellation & Modification Coordination → Coordinate Order Modification / Cancellation / Order Cancellation Service | Changed Service Request and coordination/response → distinguish request to change or cancel from performing-system response and established effect → Order Change Request; correlated progression facts. A control instruction is not the fact that the clinical work stopped. |
| OR-IR5 | Order Administration, Feature association not established → Associate Order Outcome / Order Outcome Notification | Service Outcome / Clinical Information Notification → associate returned report/observation information with its originating request and notify the requester → reuse Order-Result Correlation Link; contextual ServiceOutcome relationship. Correlation does not adjudicate conflicting results. |
| OR-IR6 | Order Administration → consumed Service Provision Resolution; [dependency matrix](../../03-business-architecture/dependencies/cross-capability-dependencies.md#2-cross-capability-service-consumption-matrix). Practitioner Role Resolution consumption is [withdrawn](../../03-business-architecture/dependencies/cross-capability-dependencies.md#3-dependency-governance-rules); precise privilege-related Service consumption remains unestablished. | Service destination resolution → understand performing service/provider context → reuse DeliverableHealthcareService and destination Endpoint where supplied. Requesting participation and applicable Clinical Privilege / Operational Privilege remain meaningful context without an inferred Service dependency. Practitioner Role information establishes neither privilege; validation does not confer substantive clinical authority. |

## 3. Principal Information Concepts

### 3.1 Order — A; existing Concept reused

- **Semantic Classification**: Assertion / Request / Direction.
- **Definition**: The independently identifiable clinical requisition, directive or prescription requesting bounded activity/service fulfilment for a stated subject and purpose.
- **Necessity**: OR-IR1 requires the requested work to be understood and OR-IR3 requires returned facts to correlate with the same originating Order. Package1 already establishes Order as an associated direction mechanism; this family elaborates its information responsibility rather than creates a duplicate request Concept.
- **Boundary**: The Order's directive standing is attributable to its originator/context; ingestion does not certify that every submitted Order is clinically valid. Order does not establish receipt, acceptance, execution or delivery. Requested clinical responsibility for bounded work does not by itself distinguish every assessment Order from Referral; G1 U1 remains.

### 3.2 Order Routing Directive — A; Foundation Concept reused

- **Semantic Classification**: Assertion / Instruction.
- **Definition**: The attributable routing direction that relates an Order to the resolved performing destination/service context.
- **Necessity**: OR-IR2 explicitly requires destination resolution and routing directives. Reuse [Foundation canonical Order derivation](../traceability/domain03-traceability.md#23-representative-example-3-order-administration).
- **Boundary**: A resolved destination, routing instruction and dispatch occurrence are distinct facts. Destination is represented through existing provider/service/Endpoint relationships, not a new destination entity or universal routing-rule Definition. A receiving endpoint is not itself a clinician, Service Provider or accepting party.

### 3.3 Order Progression Event — A

- **Semantic Classification**: Event.
- **Definition**: Information concerning a material occurrence or reported progression fact in the closed loop for an Order, qualified by source, destination, evidence and time.
- **Necessity**: OR-IR3 needs distinguishable dispatch, receipt, acknowledgement, acceptance, execution, completion and closure facts; OR-IR4 needs the response/effect of changes to remain distinguishable from the change request.
- **Boundary**: Event kind, reporting source and qualification express these distinctions without creating a Concept for every status. Acknowledgement must retain what it acknowledges. Technical receiving-boundary acceptance does not establish business acceptance; requester result acknowledgement is distinct again. No mandatory linear sequence is derived for all kinds of Order.

### 3.4 Order Change Request — A

- **Semantic Classification**: Assertion / Requested Direction Change.
- **Definition**: An attributable request to modify a particular Order's clinical details or cancel its unexecuted requested work, retaining the original Order association and the requested change's meaning.
- **Necessity**: OR-IR4 establishes modification/cancellation coordination even though the illustrative Process omits these checkpoints. This Concept captures requested effect rather than silently rewriting an Order as though the effect already occurred.
- **Boundary**: Modification and cancellation remain different requested effects, expressed through qualification; no new universal generic request family is introduced. Authority to request a change, acceptance by the performing system and actual effect require their own evidence. Exact race/eligibility/effective-change rules are not established.

### 3.5 Order-Result Correlation Link — A; Foundation relationship reused

- **Semantic Classification**: Relationship / Correlation.
- **Definition**: The qualified association of returned result/report information with its originating Order, preserving source authority, correlation evidence and qualification.
- **Necessity**: OR-IR5 and the Order-Result Correlation Matrix require this association. Reuse the Foundation name and [common relationship structure](../patterns/information-relationships.md); do not create a redundant correlation Concept for each outcome type.
- **Boundary**: Result correlation is not result production, diagnostic interpretation, conflict adjudication or verification. Association of broader ServiceOutcome kinds is supported by Package1's chain, with exact kind/authority semantics unresolved under G1 U4. No later clinical family is derived here.

### 3.6 Order Context Assembly — B; existing candidate reused

- **Semantic Classification**: Assembly.
- **Definition**: A governed contextual composition linking an Order, its routing/progression/change evidence and separately meaningful fulfilment/outcome information when established.
- **Necessity**: OR-IR3 and OR-IR5 support coherent tracking/query/notification. Package1 already identifies [Order Context Assembly](healthcare-service.md#8-candidate-information-assembly-participation). Its precise inclusion and temporal composition boundary require [G2-Q06](../reviews/package2-g2-block1-review.md#g2-q06).
- **Boundary**: An assembly may contain an unfulfilled Order with no delivery/outcome evidence. It does not require a new Collection, acquire source authority or become an order-entry workflow.

## 4. Key Information Relationships

| Source → Target | Type / Qualification | Meaning-bearing properties | Standing / applicability |
| :--- | :--- | :--- | :--- |
| Order → Healthcare Subject Context / requesting Practitioner Role Binding | Concerns subject / originated by requester participation | Source-qualified subject/order identity, request authority/context, supplied indication and credential-resolution evidence | A — required clinical-order meaning. Reuse [subject](person-healthcare-subject.md) and [practitioner](practitioner.md) semantics. Relationship Roles do not establish a new Requester entity or equate it to Referrer. |
| Order → DeliverableHealthcareService / requested work specification | Requests / directs bounded fulfilment | Identified service binding and request scope where resolved; source indication/specification | A — conditional contextual service binding from OR-IR2/6 and Package1. Order is not mandatory for service delivery, and not every intake request has a fully resolved binding. |
| Order Routing Directive → Order / resolved service/provider/Endpoint | Routes request to destination | Resolution evidence, routing direction, destination qualification and decision time | A — required routing meaning; directive does not establish dispatch, receipt or acceptance. Reuse existing n-ary context rather than disconnected claims of universal service provision. |
| Order Progression Event → Order / relevant destination or request | Reports closed-loop occurrence/disposition | Fact kind and scope, source/reporting authority, occurrence/recorded time, correlation evidence and acknowledged subject | A — required tracking. Multiple facts in a local checkpoint stay distinct; destination qualification is not a fan-out algorithm or cardinality requirement. |
| Order Change Request → Order | Requests modification / cancellation | Requested effect, requesting authority, issue/received time, target Order context and response/effect evidence when returned | A — required coordination; exact authoritative effect rules are C under G2-Q07. |
| Order → HealthcareServiceDelivery | Requests work / is associated with evidenced fulfilment | Request reference versus evidence that identified delivery occurred; performer/source and temporal context | A — conditional Package1 association. A request for delivery is not the fact of fulfilment. |
| Order → ServiceOutcome / result information | Order-Result Correlation Link; broader outcome association when evidenced | Outcome source/kind, qualification, correlation basis, association time and correction lineage | A — required association capability, conditional on supplied outcome. No universal outcome qualification or ownership is established. |
| Order Context Assembly → Order / routing / progression / change / delivery / outcome | Assembles tracking context | Inclusion purpose, evidence references and composition provenance | B — G2-Q06; missing delivery/outcome remains absent evidence, not an invented failed or completed disposition. |

## 5. Authority, Responsibility & Provenance

| Concept | Originating authority | Harmonia responsibility | Provenance characteristics |
| :--- | :--- | :--- | :--- |
| Order | Requesting clinician/clinical agent or prescriber within the evidenced request context; exhaustive authority criteria not supplied | Order Administration governs request information, intake, routing, tracking and exposure; does not prescribe or perform the work | Originator versus transmitting system, request identity/namespace, subject/requester context, supplied indication/specification and amendment attribution. Locally required signatures remain local. |
| Order Routing Directive | Governed destination-resolution/routing responsibility; provider catalogue authority remains Health Service Administration's context | Own/govern routing direction under Order Administration | Resolution source/basis, chosen service/provider/Endpoint context and routing assertion time; no new routing-rule authority model. |
| Order Progression Event | Dispatcher for dispatch facts; receiver/performer for their reported receipt/acceptance/execution; requester for result acknowledgement | Correlate/preserve/expose the closed loop; originate only evidenced internal coordination facts | Source, fact/acknowledgement scope, destination/request correlation, occurrence/assertion/recorded time and correction evidence. Unknown effect stays unknown under AX-15. |
| Order Change Request | Participant submitting the requested change; exact authorisation/eligibility remains contextual | Govern coordination of the request, response and established effect, without substituting a directive for actual cancellation | Originator, reason/requested change, original Order association and response/effect lineage. |
| Order-Result Correlation Link | Order Administration for the correlation assertion; result/report originator retains substantive authority | Own/govern association and notification; consume qualification without defining it | Source result/request references, correlation evidence/context, assertion time, qualification and correction/rebinding lineage. |
| Order Context Assembly | Each constituent originator; Order Administration for composition | Govern tracking-context assembly/provision only | Inclusion/composition context, generating time and constituent lineage; no originating authority acquired by aggregation or review. |

## 6. Temporal & Lifecycle Characteristics

Closed-loop progression requires requested/issued and received time where available, dispatch time, recipient-reported occurrence/disposition time, association time and Harmonia recorded time to be distinguished. Requested timing, imaging scheduled timing and actual execution timing apply only where supplied/evidenced. A cancellation/modification request's time is distinct from an established effect time; a response delay does not determine whether the effect occurred.

The local diagnostic/procedural Process preserves signed requisition, specimen/scheduling, preliminary/final result binding and requester acknowledgement/closure requirements. It does not establish a universal signature state, specimen stage or result lifecycle for medication or every Order. Its Order Dispatched compound checkpoint contains routing and recipient acceptance facts; Behaviour Received is not mapped to it without evidence (G1 U8).

Foundation's illustrative independent Order states remain guidance, not an exhaustive universal state machine. Order information progression, actual service execution, returned-information lifecycle and Process checkpoint progression remain distinct. Closed / Verified concerns requester acknowledgement and Order closure; it does not establish diagnostic-content verification, approval or incorporation. G1 U2's signing/finality/legal criteria and U5's business-acceptance criteria remain unresolved.

Foundation correction/provenance principles apply; the Closed-Loop Tracking Ledger label does not independently establish exhaustive retention policy. No cancellation race-resolution, retry protocol, order-entry workflow or universal completion rule is added.

## 7. Assembly Participation & Other Families

Reuse Package1 service definition/context/fulfilment/outcome distinctions, Practitioner Role Binding/Clinical Privilege/Endpoint, subject context and the existing Order Context Assembly candidate. Diagnostic information is an externally attributable boundary input to outcome association in this block; medication Order scope is retained without deriving Medication. Broader outcome-kind semantics and complete Delivery/Outcome owner traces remain G1 U3/U4.

The imaging scheduling checkpoint supports a conditional Order–Appointment relationship, with scheduler authority external. Encounter Process context supports contextual occurrence of Orders during care but supplies no universal Order–Encounter relationship/cardinality. Referral–Order conversion/causation is not established. See [cross-family analysis](../reviews/package2-g2-block1-review.md#4-cross-family-semantic-analysis).

## 8. Explicit Boundaries, Unresolved Semantics & Downstream Realisation

G2-Q07 retains who may request/authorise changes, the meaning of performing-system response and when modification/cancellation takes effect. Candidate assembly boundaries remain G2-Q06. Exact diagnostic authority/finality/legal criteria are consumed only within established local requirements, never resolved by Order Administration.

No Order subtype taxonomy, universal healthcare request superclass, order-entry system, clinical verification system or unsupported Service dependency is designed. Order Outcome association has no allocated Feature identity. The assessment Referral/bounded Order semantic boundary remains unresolved rather than merged.

No FHIR/resource/profile mapping, Java/application representation, schema, persistence/cache model, API, integration message, topic or implementation allocation is created. [AX-17](../../governance/architectural-axioms.md#ax-17) governs all candidate and unestablished derivation links.
