# Harmonia — Requirements and Architectural Motivation Register

## Architectural element specialisations

The following Harmonia specialisations define the ArchiMate element types used in this register. The specialisation name and base element are distinct; unqualified `Principle` and `Goal` denote native ArchiMate elements.

| Harmonia element | ArchiMate base element | Meaning |
|---|---|---|
| `Axiom` | `Principle` | Prescriptive governing architectural rule; deviations require governance. |
| `ArchitecturalAxiom` | `Principle` | Prescriptive governing architectural direction for architecture and implementations. |
| `ImplementationGuideline` | `CourseOfAction` | Preferred implementation approach or strategy, not inherently a binding mandate. |
| `Mandate` | `Constraint` | Binding architectural constraint or obligation, including positive obligations. |
| `FunctionalRequirement` | `Requirement` | Required functionality or behaviour. |
| `NonFunctionalRequirement` | `Requirement` | Required quality or operational characteristic. |
| `InformationRequirement` | `Requirement` | Required information semantics, integrity, availability or lifecycle characteristics. |
| `TransitionRequirement` | `Requirement` | Required conditions when moving between architecture or solution states. |
| `AssuranceRequirement` | `Requirement` | Required demonstrable evidence or verification outcome. |

Native `Principle` expresses an aspirational architectural direction; native `Goal` expresses a desired outcome. `DerivedRequirement` denotes provenance rather than a separate requirement specialisation. The distinct Axiom and ArchitecturalAxiom specialisations are retained without asserting a specialisation relationship between them.

## Subject groups and containment hierarchy

The following subject groups and subgroups provide the hierarchical containment organisation for this register. Each entry has one primary subject subgroup; that placement does not exclude other architectural relationships. The hierarchy is a subject organisation, not an assertion of component ownership or implementation containment.

### G01 — Architectural Governance

| Subgroup | Name | Concern |
|---|---|---|
| `G01.01` | Architecture Process | How architecture is developed, reviewed, governed and maintained. |
| `G01.02` | Architecture Vision | The intended architectural direction, objectives and desired outcomes. |
| `G01.03` | Implementation Strategy | How architectural direction guides solution design and implementation choices. |
| `G01.04` | Architecture Conformance | How implementations are assessed against the governed architecture, including deviations and their resolution. |

### G02 — Information Management

| Subgroup | Name | Concern |
|---|---|---|
| `G02.01` | Information Identity | — |
| `G02.02` | Information Integrity | — |
| `G02.03` | Information Lifecycle | — |
| `G02.04` | Information Persistence | — |
| `G02.05` | Information Sovereignty and Residency | — |

### G03 — Security and Authorisation

| Subgroup | Name | Concern |
|---|---|---|
| `G03.01` | Authentication | Establishing and verifying identity. |
| `G03.02` | Authorisation | Determining what an authenticated entity is permitted to do, under which conditions and within which scope. |
| `G03.03` | Authorisation Enforcement | Ensuring that activities and information access comply with applicable authorisation decisions. |
| `G03.04` | Information Protection | Confidentiality, encryption and protection of sensitive information. |
| `G03.05` | Security Administration | Credential lifecycle, revocation, security configuration and operational isolation. |
| `G03.06` | Security Monitoring | Detection and reporting of security-related events and conditions. |

### G04 — Audit and Accountability

| Subgroup | Name | Concern |
|---|---|---|
| `G04.01` | Audit Generation | Creation of audit records for activities subject to audit obligations. |
| `G04.02` | Audit Integrity | Protection of audit records against unauthorised alteration, deletion or corruption. |
| `G04.03` | Audit Management | Retention, retrieval, access and lifecycle management of audit information. |
| `G04.04` | Accountability and Traceability | Establishing who or what performed an activity, under which authority, and its relationship to other activities. |

### G05 — Interoperability and Integration

| Subgroup | Name | Concern |
|---|---|---|
| `G05.01` | External Connectivity | Establishing, configuring and managing connections with external systems. |
| `G05.02` | Interoperability Standards | Conformance with applicable exchange protocols, formats and interoperability standards. |
| `G05.03` | Information Exchange | Inbound and outbound information exchange, including transformation and interpretation. |
| `G05.04` | Interaction Contracts | Definition, compatibility and evolution of external interaction contracts. |
| `G05.05` | Interaction Reliability | Handling failed, delayed or repeated external interactions. |

### G06 — Processing and Workflow

| Subgroup | Name | Concern |
|---|---|---|
| `G06.01` | Task Management | Creation, assignment, execution and completion of processing activities. |
| `G06.02` | Task Completion | Coordination, scheduling and orchestration of processing activities to achieve their defined completion outcomes. |
| `G06.03` | Concurrency Management | Concurrent execution, coordination and processing consistency. |
| `G06.04` | Message Processing | Internal message handling, sequencing, deduplication and processing semantics. |
| `G06.05` | Processing State Management | Preservation, transition and recovery of processing state. |
| `G06.06` | Processing Outcomes | Establishment, reporting and verification of processing results. |

### G07 — Resilience and Recovery

| Subgroup | Name | Concern |
|---|---|---|
| `G07.01` | Fault Tolerance | Continuing acceptable operation despite component or dependency failures. |
| `G07.02` | Failure Detection and Handling | Recognising failures and responding appropriately to prevent uncontrolled consequences. |
| `G07.03` | Operational Recovery | Restoring components and processing activities following recoverable failures. |
| `G07.04` | Disaster Recovery | Recovering the solution to a known, operationally consistent state following catastrophic failures or information corruption. |
| `G07.05` | Continuity of Operations | Preserving accepted work and maintaining essential capabilities during disruptions, restarts and recovery activities. |

### G08 — Performance and Capacity

| Subgroup | Name | Concern |
|---|---|---|
| `G08.01` | Throughput | Processing volumes and rates under defined operating conditions. |
| `G08.02` | Responsiveness | Response times and latency experienced by users and interacting systems. |
| `G08.03` | Scalability | Ability to accommodate increasing workloads through changes in processing capacity. |
| `G08.04` | Resource Management | Resource consumption, contention, allocation and capacity limits. |
| `G08.05` | Capacity Planning | Establishing, monitoring and forecasting the capacity required to support expected workloads. |

### G09 — Operations and Assurance

| Subgroup | Name | Concern |
|---|---|---|
| `G09.01` | Operational Monitoring | Observing component, subsystem and overall solution health and behaviour. |
| `G09.02` | Operational Diagnostics | Collecting and interpreting information to investigate failures and abnormal behaviour. |
| `G09.03` | Operational Control | Authorised administrative intervention, configuration and operational state management. |
| `G09.04` | Operational Reporting | Reporting operational status, performance, dependencies and significant conditions. |
| `G09.05` | Operational Information Management | Governance, retention and management of operational information. |
| `G09.06` | Operational Assurance | Assessing operational readiness, health and conformance with defined operational expectations. |

### G10 — Deployment and Release Management

| Subgroup | Name | Concern |
|---|---|---|
| `G10.01` | Build Management | Controlled, repeatable construction and packaging of solution artefacts. |
| `G10.02` | Deployment Management | Deployment processes, automation and infrastructure integration. |
| `G10.03` | Configuration Management | Management of environment-specific configuration and deployment parameters. |
| `G10.04` | Release Management | Release preparation, approval, publication and lifecycle governance. |
| `G10.05` | Release Compatibility | Compatibility and concurrent operation of adjacent solution releases. |
| `G10.06` | Deployment Environments | Supported platforms, runtime environments and deployment constraints. |

### G11 — Verification and Testing

| Subgroup | Name | Concern |
|---|---|---|
| `G11.01` | Functional Verification | Verifying that implemented capabilities satisfy their functional requirements. |
| `G11.02` | Integration Verification | Verifying internal interactions and conformance with external interaction contracts. |
| `G11.03` | Regression Testing | Detecting unintended changes in previously established solution behaviour. |
| `G11.04` | Performance and Capacity Testing | Evaluating performance and capacity against representative workloads and conditions. |
| `G11.05` | Resilience Testing | Simulating failures and verifying fault tolerance and recovery behaviour. |
| `G11.06` | Test Governance | Management of the reference test harness, test evidence and release verification obligations. |

### G12 — User Experience

| Subgroup | Name | Concern |
|---|---|---|
| `G12.01` | Usability | Ease of use, clarity and effectiveness of user interactions. |
| `G12.02` | Accessibility | Accessibility requirements and accommodation of differing user needs. |
| `G12.03` | User Interface Responsiveness | Responsiveness of user interactions and interface behaviour. |
| `G12.04` | Navigation and Interaction | Navigation, contextual interaction and user workflow support. |
| `G12.05` | Localisation and Internationalisation | Language, locale and regional presentation requirements. |

### G13 — Distributed Systems and Temporal Management

| Subgroup | Name | Concern |
|---|---|---|
| `G13.01` | Distributed Coordination | Coordination of activities across distributed operational domains. |
| `G13.02` | Temporal Consistency | Consistent interpretation of temporal information across distributed environments. |
| `G13.03` | Event Ordering and Causality | Establishing ordering and causal relationships between distributed events. |
| `G13.04` | Event Horizon Management | Establishing and managing temporal boundaries within distributed operational domains. |
| `G13.05` | Distributed State Consistency | Maintaining appropriate consistency of operational state across distributed components. |
| `G13.06` | Task Ordering | Establishing and enforcing execution-order constraints between distributed processing activities. |

## Architectural statements

The register identifier is stable and does not determine element type. `ArchiMate Element` identifies the actual base element and, where applicable, its Harmonia specialisation. The `Domain` column identifies the primary subject subgroup in the hierarchy above.

| ID | Name | ArchiMate Element | Description | Domain |
|---|---|---|---|---|
| `NFR-001` | Conformance to Governed Architecture | `Principle` «ArchitecturalAxiom» | Conform to governed Harmonia architecture | `G01.02` — Architecture Vision |
| `NFR-002` | Architectural Decision Traceability | `Constraint` «Mandate» | Trace architectural decisions to governing principles, constraints and requirements | `G01.01` — Architecture Process |
| `NFR-003` | Implementation Reconciliation | `Constraint` «Mandate» | Reconcile implementation changes against governed architecture | `G01.01` — Architecture Process |
| `NFR-004` | Stable Architectural Identity | `Principle` «ArchitecturalAxiom» | Preserve stable architectural identities across transformations and round trips | `G01.03` — Implementation Strategy |
| `NFR-005` | Authoritative Projection Semantics | `Principle` «ArchitecturalAxiom» | Preserve authoritative semantics in architectural projections | `G01.03` — Implementation Strategy |
| `NFR-006` | Governed Architecture Review | `Constraint` «Mandate» | Subject architectural changes to governed review and acceptance | `G01.04` — Architecture Conformance |
| `NFR-007` | Independent Architecture Verification | `Principle` «ArchitecturalAxiom» | Independently verify implementations against architectural contracts | `G01.02` — Architecture Vision |
| `NFR-008` | Defined Service Contracts | `Principle` «ArchitecturalAxiom» | Expose defined service contracts | `G01.03` — Implementation Strategy |
| `NFR-009` | Service Boundary Encapsulation | `Principle` «ArchitecturalAxiom» | Prevent implementation-specific technology leakage across service boundaries | `G01.03` — Implementation Strategy |
| `NFR-010` | Component Substitutability | `CourseOfAction` «ImplementationGuideline» | Allow component replacement without consumer changes when contracts remain satisfied | `G01.03` — Implementation Strategy |
| `NFR-011` | Independent Component Evolution | `CourseOfAction` «ImplementationGuideline» | Support independent component evolution within defined boundaries | `G01.03` — Implementation Strategy |
| `NFR-012` | Explicit Component Dependencies | `CourseOfAction` «ImplementationGuideline» | Make component dependencies explicit and governed | `G01.03` — Implementation Strategy |
| `NFR-013` | Separation of Responsibilities | `CourseOfAction` «ImplementationGuideline» | Maintain separation of component responsibilities | `G01.03` — Implementation Strategy |
| `NFR-014` | Governed Information Identity | `Principle` «ArchitecturalAxiom» | Governed information elements shall possess well-defined, traceable identities that remain consistent across their points of utilisation. | `G02.01` — Information Identity |
| `NFR-015` | Governed Information Versioning | `Principle` «ArchitecturalAxiom» | Governed information shall support versioning, enabling individual versions to be identified, accessed and referenced unambiguously. | `G02.03` — Information Lifecycle |
| `NFR-016` | Designated Information Immutability | `CourseOfAction` «ImplementationGuideline» | Keep information designated immutable unchanged during the applicable execution context | `G02.04` — Information Persistence |
| `NFR-017` | Governed Information Modification | `CourseOfAction` «ImplementationGuideline» | Preserve governed integrity during information modification | `G02.04` — Information Persistence |
| `NFR-018` | Concurrent Information Consistency | `CourseOfAction` «ImplementationGuideline» | Prevent inconsistent information states from concurrent operations | `G02.04` — Information Persistence |
| `NFR-019` | Information Provenance | `Requirement` «AssuranceRequirement» | Preserve information provenance where required | `G02.02` — Information Integrity |
| `NFR-020` | Transformation Semantic Integrity | `CourseOfAction` «ImplementationGuideline» | Guides implementation of information transformations to avoid semantic loss. | `G02.04` — Information Persistence |
| `NFR-021` | Information Reference Resolution | `CourseOfAction` «ImplementationGuideline» | Keep information references resolvable within lifecycle and retention boundaries | `G02.04` — Information Persistence |
| `NFR-022` | Distinct Execution Contexts | `CourseOfAction` «ImplementationGuideline» | Maintain distinct identifiable contexts for concurrent execution instances | `G02.04` — Information Persistence |
| `NFR-023` | Immutable Information Protection | `Constraint` «Mandate» | Information designated as immutable shall not be modified under any circumstances within the scope of its immutability designation. | `G02.02` — Information Integrity |
| `NFR-024` | Ergon Execution Isolation | `CourseOfAction` «ImplementationGuideline» | Isolate execution state between independently operating Ergon instances | `G06.03` — Concurrency Management |
| `NFR-025` | Concurrency Policy Conformance | `Principle` «ArchitecturalAxiom» | Concurrent activities shall produce outcomes consistent with their defined concurrency policies and applicable architectural and operational constraints. | `G06.03` — Concurrency Management |
| `NFR-026` | Execution Outcome Attribution | `Requirement` «AssuranceRequirement» | Attribute outcomes to originating execution contexts | `G06.06` — Processing Outcomes |
| `NFR-027` | Task Independence | `CourseOfAction` «ImplementationGuideline» | Avoid unintended dependencies between independent tasks | `G06.05` — Processing State Management |
| `NFR-028` | Concurrent Task State Integrity | `Principle` «ArchitecturalAxiom» | Preserve integrity of task state transitions under concurrency | `G07.01` — Fault Tolerance |
| `NFR-029` | Accepted Work Durability | `Constraint` «Mandate» | Recoverable processing failures shall not result in the loss of accepted governed work. | `G07.05` — Continuity of Operations |
| `NFR-030` | Controlled Processing Recovery | `Constraint` «Mandate» | Support controlled recovery or retry of failed operations | `G07.03` — Operational Recovery |
| `NFR-031` | Repeated Execution Safety | `CourseOfAction` «ImplementationGuideline» | Prevent unintended duplicate effects on repeated execution | `G07.03` — Operational Recovery |
| `NFR-032` | Failure Containment | `CourseOfAction` «ImplementationGuideline» | Contain component failures across independent processing contexts | `G07.02` — Failure Detection and Handling |
| `NFR-033` | Interrupted Processing Recovery | `CourseOfAction` «ImplementationGuideline» | Recover interrupted processing from identifiable execution state where supported | `G07.02` — Failure Detection and Handling |
| `NFR-034` | Failure Observability | `Principle` «ArchitecturalAxiom» | Make processing failures observable and attributable | `G09.02` — Operational Diagnostics |
| `NFR-035` | Governed Information Authorisation | `Constraint` «Mandate» | Access to governed information shall be subject to authorisation. | `G03.03` — Authorisation Enforcement |
| `NFR-036` | Authorisation Compartment Assessment | `CourseOfAction` «ImplementationGuideline» | Consider applicable action and information compartment requirements in authorisation | `G03.01` — Authentication |
| `NFR-037` | Authenticated Identity | `Constraint` «Mandate» | Establish authenticated identity suitable for authorisation | `G03.03` — Authorisation Enforcement |
| `NFR-038` | Authorisation Boundary Enforcement | `Constraint` «Mandate» | Enforce authorisation at defined architectural boundaries | `G03.04` — Information Protection |
| `NFR-039` | Auditable Security Decisions | `Requirement` «AssuranceRequirement» | Make security decisions independently auditable | `G03.06` — Security Monitoring |
| `NFR-040` | Provider-Side Security Enforcement | `Constraint` «Mandate» | Do not rely solely on consumer-side security enforcement | `G03.04` — Information Protection |
| `NFR-041` | Execution Context Confidentiality | `CourseOfAction` «ImplementationGuideline» | Prevent disclosure to unauthorised execution contexts | `G03.04` — Information Protection |
| `NFR-042` | Security Information Integrity | `Principle` «ArchitecturalAxiom» | Preserve integrity of security-relevant information across interactions | `G03.04` — Information Protection |
| `NFR-043` | Governed Audit Generation | `Constraint` «Mandate» | All activities subject to Harmonia's governed audit obligations shall generate appropriate audit records. | `G04.01` — Audit Generation |
| `NFR-044` | Audit Context Identification | `Requirement` «AssuranceRequirement» | Identify relevant operation, subject and execution context in audit records | `G04.02` — Audit Integrity |
| `NFR-045` | Audit Record Protection | `Constraint` «Mandate» | Protect audit records against unauthorised modification | `G04.03` — Audit Management |
| `NFR-046` | Audit Retention | `Requirement` «AssuranceRequirement» | Retain audit information for its defined retention period | `G04.04` — Accountability and Traceability |
| `NFR-047` | Cross-Component Execution Traceability | `Requirement` «AssuranceRequirement» | Trace execution across participating components | `G04.04` — Accountability and Traceability |
| `NFR-048` | Audit Noninterference | `Requirement` «AssuranceRequirement» | Ensure audit recording does not compromise audited operation integrity | `G04.01` — Audit Generation |
| `NFR-049` | Published Interaction Contracts | `CourseOfAction` «ImplementationGuideline» | Conform service interactions to published contracts | `G05.04` — Interaction Contracts |
| `NFR-050` | Exchange Semantic Preservation | `Principle` «ArchitecturalAxiom» | Preserve semantics of exchanged information | `G05.02` — Interoperability Standards |
| `NFR-051` | Interoperability Encapsulation | `Principle` «ArchitecturalAxiom» | Avoid unnecessary exposure of implementation technology in interoperability mechanisms | `G05.03` — Information Exchange |
| `NFR-052` | Governed Exchange Representation | `Constraint` «Mandate» | Information exchanged with Harmonia shall either be encapsulated within a governed information structure or mapped/transformed into a governed information representation. | `G05.03` — Information Exchange |
| `NFR-053` | Exchange Identity and Provenance | `Requirement` «AssuranceRequirement» | Preserve required identity and provenance in exchange | `G05.04` — Interaction Contracts |
| `NFR-054` | Contract Evolution Compatibility | `Principle` | Minimise disruption to consumers when contracts evolve | `G05.04` — Interaction Contracts |
| `NFR-055` | Native Operational Observability | `Constraint` «Mandate» | Observability is a fundamental Harmonia capability. Every component shall expose sufficient operational information to enable effective monitoring, diagnosis and assessment of its operational state. | `G09.01` — Operational Monitoring |
| `NFR-056` | Dependency Health Monitoring | `Constraint` «Mandate» | Component deployment and execution dependencies shall be explicitly identified and monitored, with their operational state incorporated into solution operational readiness reporting. | `G09.01` — Operational Monitoring |
| `NFR-057` | Operational Failure Diagnostics | `Requirement` «AssuranceRequirement» | Diagnose operational failures without direct access to implementation internals | `G09.02` — Operational Diagnostics |
| `NFR-058` | Cross-Service Activity Correlation | `Requirement` «AssuranceRequirement» | Correlate processing activity across services | `G09.04` — Operational Reporting |
| `NFR-059` | Component and Dependency Health Distinction | `Requirement` «AssuranceRequirement» | Distinguish component health from dependency health | `G09.06` — Operational Assurance |
| `NFR-060` | Concurrent Execution Units | `Constraint` «Mandate» | Where practical, Harmonia processing activities shall be encapsulated within independently managed execution units that support concurrent operation. | `G06.03` — Concurrency Management |
| `NFR-061` | Elastic Processing Capacity | `Constraint` «Mandate» | Harmonia's workflow, message-processing and Digital Twin capabilities shall support the addition and removal of processing resources, where practical, without requiring fundamental changes to their operational behaviour. | `G08.03` — Scalability |
| `NFR-062` | Concurrent Service Processing | `Constraint` «Mandate» | Harmonia services responsible for receiving, processing or routing information, or modifying persisted information, shall support appropriate concurrent execution mechanisms to minimise processing bottlenecks and avoid single points of failure. | `G06.03` — Concurrency Management |
| `NFR-063` | Event Ingestion Capacity | `Requirement` «NonFunctionalRequirement» | R1.x accepts 100,000 incoming messages/PUTs/POSTs over 8 hours (12,500/hour; approximately 3.47/second average). This workload is processed together with the NFR-066, NFR-068 and NFR-069 obligations; the 8-hour incoming workload is fully processed by hour 24 while 100 practitioners and 10 OA&M users remain active. | `G08.01` — Throughput |
| `NFR-064` | Resource Contention Integrity | `Principle` «ArchitecturalAxiom» | Prevent resource contention from compromising integrity | `G08.04` — Resource Management |
| `NFR-065` | Governed Search Performance | `Requirement` «NonFunctionalRequirement» | Harmonia's governed search capabilities shall satisfy defined performance requirements independently of create, read, update and delete operations | `G08.02` — Responsiveness |
| `NFR-066` | Event Processing Completion | `Requirement` «NonFunctionalRequirement» | Complete processing of the 100,000 events received during an 8-hour window within 24 hours of the start of that window (16 additional hours). | `G08.01` — Throughput |
| `NFR-067` | Accepted Activity Durable Recording | `Principle` «ArchitecturalAxiom» | Accepted processing activities shall be durably recorded before acknowledgement and retained until their associated processing obligations have been discharged. | `G07.05` — Continuity of Operations |
| `NFR-068` | Concurrent Practitioner Workload | `Requirement` «NonFunctionalRequirement» | Support 100 concurrent practitioners across all non-OA&M user interfaces while ingesting and processing the specified 100,000-event workload. | `G08.04` — Resource Management |
| `NFR-069` | Concurrent OA&M Workload | `Requirement` «NonFunctionalRequirement» | Support 10 concurrent OA&M users simultaneously with the practitioner and event-processing workloads. | `G08.04` — Resource Management |
| `NFR-070` | R1.x Accessibility Scope | `Requirement` «FunctionalRequirement» | R1.x user interfaces are not required to conform to WCAG accessibility standards. | `G12.02` — Accessibility |
| `NFR-071` | R1.x User Authentication | `Requirement` «FunctionalRequirement» | R1.x: unauthenticated users shall not access Harmonia functionality; system-to-system identities remain a separate consideration. | `G03.01` — Authentication |
| `NFR-072` | Software-Defined Deployment | `CourseOfAction` «ImplementationGuideline» | Support software-defined infrastructure, automated configuration, installation and deployment using Ansible or equivalent scripts. | `G10.02` — Deployment Management |
| `NFR-073` | R1.x Container Deployment | `CourseOfAction` «ImplementationGuideline» | R1.x: container-based Harmonia deployment on Ubuntu Server; VMs establish macro-level infrastructure; software-defined networking/configuration supported. | `G10.06` — Deployment Environments |
| `NFR-074` | R2.x Deployment Platforms | `CourseOfAction` «ImplementationGuideline» | R2.x: support both Ubuntu Server and Red Hat OpenShift container infrastructure; OpenShift is preferred, not mandatory. | `G10.06` — Deployment Environments |
| `NFR-075` | Native Operational Logging | `Principle` «ArchitecturalAxiom» | Provide a distinct, native capability for operational log capture and display, without dependency on external logging services. | `G09.05` — Operational Information Management |
| `NFR-076` | Operational Log PHI Protection | `Constraint` «Mandate» | Exclude PHI from operational logs unless its inclusion complies with the governed Harmonia Logging Strategy. | `G03.04` — Information Protection |
| `NFR-077` | Encryption at Rest | `Constraint` «Mandate» | Encrypt all Harmonia-managed data at rest. | `G03.04` — Information Protection |
| `NFR-078` | Internal Encryption in Transit | `Constraint` «Mandate» | Encrypt data in transit between internal Harmonia components. | `G03.04` — Information Protection |
| `NFR-079` | Standard Key Management | `CourseOfAction` «ImplementationGuideline» | Use industry-standard cryptographic key-management services; do not build a Harmonia-specific proprietary key-management service. | `G03.05` — Security Administration |
| `NFR-080` | Responsive Interface Layouts | `Principle` | Provide responsive UI layouts adapting to supported device display characteristics and capabilities. | `G12.01` — Usability |
| `NFR-081` | User Interaction Responsiveness | `Requirement` «NonFunctionalRequirement» | Provide prompt interaction responses and appropriate feedback for operations that cannot complete immediately; response-time thresholds TBD. | `G12.03` — User Interface Responsiveness |
| `NFR-082` | Contextual Navigation | `Principle` | Core UIs should support contextual navigation where practical, preserving relevant operational context and authorisation. | `G12.04` — Navigation and Interaction |
| `NFR-083` | Hard Shell Hard Body Security | `Constraint` «Mandate» | Apply a 'Hard Shell, Hard Body' security profile: enforce security at the perimeter and throughout internal components and interactions. | `G03.04` — Information Protection |
| `NFR-084` | Source-Proximate Audit Generation | `CourseOfAction` «ImplementationGuideline» | Generate audit information as close to the source activity/event as practical. | `G04.01` — Audit Generation |
| `NFR-085` | Reference-Based Audit Records | `Principle` «ArchitecturalAxiom» | Avoid duplicating persisted information in audit records where references suffice; exceptions when information itself causes/is the subject of the audit or no persisted instance exists (e.g. transformations). | `G04.03` — Audit Management |
| `NFR-086` | Independent Audit Service | `Principle` «ArchitecturalAxiom» | Audit Service must not depend on external event capture services for core audit generation, capture or management. | `G04.01` — Audit Generation |
| `NFR-087` | Large Information Object Storage | `Principle` «ArchitecturalAxiom» | Information objects exceeding a defined size threshold shall be persisted in fit-for-purpose storage within the Harmonia ecosystem and exchanged by reference. | `G02.04` — Information Persistence |
| `NFR-088` | Configurable MLLP Timeouts | `CourseOfAction` «ImplementationGuideline» | MLLP timeouts shall be configurable (connection, I/O and idle where applicable). | `G05.01` — External Connectivity |
| `NFR-089` | Configurable MLLP Acknowledgements | `CourseOfAction` «ImplementationGuideline» | HL7 v2 over MLLP acknowledgement behaviour shall be configurable within applicable standards, including original/enhanced modes where applicable. | `G05.02` — Interoperability Standards |
| `NFR-090` | Robust MLLP Connection Management | `Principle` | MLLP connection management shall tolerate poorly behaving clients, including slow closure and repeated/multiple connection establishment, without compromising integrity. | `G05.01` — External Connectivity |
| `NFR-091` | Governed HL7 v2 Tolerance | `Principle` | Assess and report inbound HL7 v2.x structural conformance; support governed, configurable sending-system-specific tolerance of nonconformance when reliable interpretation is possible. Tolerated messages remain nonconformant. | `G05.03` — Information Exchange |
| `NFR-092` | External API Infrastructure Assumptions | `CourseOfAction` «ImplementationGuideline» | API servers may assume API management and load balancers are provided by supporting infrastructure for endpoint connectivity; internal API traversal scope remains open. | `G05.01` — External Connectivity |
| `NFR-093` | Operational State Reporting | `Principle` «ArchitecturalAxiom» | Track/report subsystem, workflow, queue, persistence-service and audit-service state. Logging and OA&M monitoring services require only basic health and availability monitoring. | `G09.01` — Operational Monitoring |
| `NFR-094` | Single-Machine Failure Recovery | `Requirement` «AssuranceRequirement» | Harmonia shall demonstrate recovery from the failure of any single physical or virtual machine without loss of accepted governed work or the need for manual intervention. | `G07.03` — Operational Recovery |
| `NFR-095` | Timezone Correctness | `Principle` | Support concurrent timezones; preserve required timezone detail for local temporal values, distinguish absolute instants and handle daylight saving correctly. | `G12.05` — Localisation and Internationalisation |
| `NFR-096` | Target User Expertise | `Principle` | R1.x/R2.x UIs target technically proficient and/or clinically trained users; novice/nontechnical/nonclinical user support is not required. | `G12.01` — Usability |
| `NFR-097` | Coherent Workflow Scheduler | `Principle` «ArchitecturalAxiom» | Use one coherent logically centralised (not necessarily physically centralised) scheduler for workflow activity. | `G06.02` — Task Completion |
| `NFR-098` | Coherent Calendar Service | `Principle` «ArchitecturalAxiom» | Where needed, use one coherent logically centralised calendar for appointments, calendar scheduling and alarms; calendar and workflow scheduler are almost entirely independent. | `G06.02` — Task Completion |
| `NFR-099` | Structured Information Encapsulation | `Principle` | Prefer XML for structured information encapsulation where practical; allow JSON for UI interactions and other formats required by interoperability/contracts. | `G05.03` — Information Exchange |
| `NFR-100` | Harmonia FOSS Licensing | `Constraint` «Mandate» | Harmonia itself shall be Free and Open Source Software (FOSS). | `G01.03` — Implementation Strategy |
| `NFR-101` | FOSS Technology Dependencies | `Constraint` «Mandate» | Use FOSS technologies/dependencies; proprietary or closed-source software requires explicit HCRL approval. | `G01.03` — Implementation Strategy |
| `NFR-102` | Jakarta EE Preference | `CourseOfAction` «ImplementationGuideline» | Prefer Jakarta EE for applicable application/service implementations, where practical, subject to FOSS rules. | `G01.03` — Implementation Strategy |
| `NFR-103` | Unbounded Clinical Information Retention | `Principle` «ArchitecturalAxiom» | Impose no inherent temporal ceiling on patient/practitioner information retention; supporting infrastructure capacity and applicable governance still apply. | `G02.03` — Information Lifecycle |
| `NFR-104` | R1.x Internationalisation | `Principle` | R1.x must be architecturally multilingual (i18n), but only Australian English (en-AU) resources/content need be populated and delivered. | `G12.05` — Localisation and Internationalisation |
| `NFR-105` | Operational Availability | `Requirement` «NonFunctionalRequirement» | Meet a defined operational availability target | `G07.05` — Continuity of Operations |
| `NFR-106` | Disaster Recovery Consistency | `Constraint` «Mandate» | Harmonia shall support recovery to a known, operationally consistent state following catastrophic infrastructure failure or information corruption. | `G07.04` — Disaster Recovery |
| `NFR-107` | Component Recovery Consistency | `Principle` «ArchitecturalAxiom» | Individual Harmonia components shall, where practical, support recovery to a known, operationally consistent state following failure. | `G07.03` — Operational Recovery |
| `NFR-108` | Backup and Replication | `Constraint` «Mandate» | Governed information and recoverable operational state shall be protected through appropriate backup and/or replication mechanisms, consistent with applicable information retention, integrity and recovery obligations. | `G07.04` — Disaster Recovery |
| `NFR-109` | Planned Restart Preservation | `Principle` «ArchitecturalAxiom» | Planned system restarts and deployments shall preserve accepted governed work and its recoverable processing state. | `G07.03` — Operational Recovery |
| `NFR-110` | Degraded Mode Continuity | `Principle` | Harmonia should, where practical, continue providing unaffected capabilities when non-essential components or services become unavailable. | `G07.05` — Continuity of Operations |
| `NFR-111` | Controlled Capacity Admission | `Constraint` «Mandate» | Harmonia shall manage increasing workloads within its operational capacity and, where defined capacity thresholds are reached, shall apply controlled admission or rejection of incoming activity and report the associated capacity or performance conditions. | `G08.04` — Resource Management |
| `NFR-112` | Processing Resource Isolation | `Principle` | Harmonia should, where practical, prevent resource exhaustion or excessive resource consumption within one processing context from adversely affecting the operation of unrelated processing contexts. | `G08.04` — Resource Management |
| `NFR-113` | Configurable Resource Limits | `CourseOfAction` «ImplementationGuideline» | Where practical and supported by the underlying infrastructure or middleware, resource utilisation limits and/or guidelines should be configurable at the individual component, component-type or subsystem level. | `G08.04` — Resource Management |
| `NFR-114` | Detailed Performance Metrics | `CourseOfAction` «ImplementationGuideline» | Where practical and supported by the underlying infrastructure and middleware, detailed performance metrics for Harmonia components, modules and subsystems should be exposed and made available for consumption by OA&M monitoring tools. | `G09.01` — Operational Monitoring |
| `NFR-115` | Cross-Component Resource Contention | `Principle` | Harmonia shall endeavour to minimise cross-component resource contention and its impact on workflow execution, recognising that distributed architectures and non-real-time operating environments limit the ability to reliably enforce workload prioritisation. | `G08.04` — Resource Management |
| `NFR-117` | Information Archival and Retrieval | `Principle` «ArchitecturalAxiom» | Harmonia shall support a governed framework for information archival and retrieval. | `G02.03` — Information Lifecycle |
| `NFR-118` | Configurable Retention and Disposal | `Principle` | Harmonia should support configurable information retention and disposal policies, avoiding arbitrary application-imposed limitations where practical and within the capabilities of the underlying infrastructure and middleware. | `G02.03` — Information Lifecycle |
| `NFR-119` | Information Integrity Assessment | `Constraint` «Mandate» | Harmonia's governed information framework shall support the assessment and reporting of the integrity of individual governed information items. | `G02.02` — Information Integrity |
| `NFR-121` | Migration Information Integrity | `Constraint` «Mandate» | Any migration of governed information shall preserve information integrity, including the identities of information items and their associated references, across the migration boundary. | `G02.02` — Information Integrity |
| `NFR-122` | Information Model Migration Planning | `CourseOfAction` «ImplementationGuideline» | Evolution of governed information models should be accompanied by an appropriate migration process or plan addressing the governed information sets potentially affected by the change. | `G02.03` — Information Lifecycle |
| `NFR-123` | Cryptographic Key Rotation | `Principle` «ArchitecturalAxiom» | Harmonia's cryptographic services shall support the rotation and replacement of cryptographic keys and certificates to maintain the security, integrity and trustworthiness of cryptographically protected information and communications. | `G03.05` — Security Administration |
| `NFR-124` | Credential and Grant Revocation | `Principle` «ArchitecturalAxiom» | Harmonia shall support governed revocation of authentication credentials and authorisation grants, and operational isolation of components, modules and subsystems through centrally governed enforcement of their access to Harmonia's information services. | `G03.03` — Authorisation Enforcement |
| `NFR-125` | Secrets Protection | `Constraint` «Mandate» | Harmonia shall protect security-sensitive configuration information, credentials, cryptographic keys and other secrets against unauthorised access, disclosure and modification. | `G03.04` — Information Protection |
| `NFR-126` | Configurable Session Timeouts | `CourseOfAction` «ImplementationGuideline» | Where supported by the underlying security framework and middleware, Harmonia should provide configurable session duration and inactivity timeouts appropriate to the security requirements of the relevant interaction context. | `G03.01` — Authentication |
| `NFR-127` | Security Anomaly Reporting | `Principle` «ArchitecturalAxiom» | Harmonia shall report detectable security-related failures, violations and anomalous conditions through its governed operational monitoring and reporting capabilities. | `G03.06` — Security Monitoring |
| `NFR-129` | Governed DevSecOps Deployment | `Constraint` «Mandate» | Harmonia shall adopt a defined DevSecOps and software-defined deployment model, with software builds, deployment definitions, configuration changes and release activities governed, version-controlled and traceable through an established change-management framework. | `G10.02` — Deployment Management |
| `NFR-130` | Environment-Specific Configuration | `Principle` | Harmonia should, where practical, support environment-specific configuration without requiring modification or rebuilding of application artefacts. | `G10.03` — Configuration Management |
| `NFR-131` | Adjacent Release Coexistence | `Principle` «ArchitecturalAxiom» | Harmonia shall support the concurrent operation of two adjacent solution releases against the same governed information environment, enabling controlled transition between releases without requiring in-place upgrades. Red/Blue deployment: two adjacent release generations can run concurrently against the same governed information environment, with controlled activity transition; in-place upgrade not required. | `G10.05` — Release Compatibility |
| `NFR-132` | Three-Generation Release Compatibility | `Principle` «ArchitecturalAxiom» | Harmonia release N shall support concurrent operation with either release N−1 or release N+1 against the same governed information environment.Three-generation compatibility window N−1/N/N+1; N runs concurrently with either N+1 or N−1, not necessarily all three. General-purpose rollback and reversible database migration not required. | `G10.05` — Release Compatibility |
| `NFR-133` | Representative Deployment Test Harness | `CourseOfAction` «ImplementationGuideline» | Harmonia should provide a well-defined deployment and testing harness capable of reproducing, where practical, the configuration, infrastructure dependencies and operational characteristics of its target deployment environments, enabling validation of solution behaviour prior to deployment. | `G11.06` — Test Governance |
| `NFR-135` | Governed External Interaction Retry | `Principle` «ArchitecturalAxiom» | Harmonia shall support governed retry of failed external interactions where recovery through repetition is appropriate, consistent with the interaction's defined failure-handling, retry and idempotency policies. | `G05.05` — Interaction Reliability |
| `NFR-136` | Duplicate Message Handling | `Requirement` «FunctionalRequirement» | Harmonia's message-processing capabilities shall support the identification and governed handling of duplicate incoming messages, consistent with the applicable message-processing policies. | `G06.04` — Message Processing |
| `NFR-137` | Message Processing Ordering | `Requirement` «FunctionalRequirement» | Harmonia's message-processing capabilities shall support the preservation and enforcement of message-processing order where required by the applicable message-processing policies and interaction contracts. | `G13.06` — Task Ordering |
| `NFR-138` | Interaction Contract Versioning | `CourseOfAction` «ImplementationGuideline» | Harmonia service interfaces and interaction contracts should, where practical, support explicit versioning to facilitate controlled evolution and compatibility between service consumers and providers. | `G05.04` — Interaction Contracts |
| `NFR-139` | External Connection Configuration | `Principle` «ArchitecturalAxiom» | Harmonia shall support the configuration of external-system connections independently of the implementation of the components responsible for those interactions. | `G05.01` — External Connectivity |
| `NFR-141` | Configurable Operational Alert Thresholds | `CourseOfAction` «ImplementationGuideline» | Harmonia's operational monitoring capabilities should, where practical, support configurable thresholds for alarms, performance indicators, resource utilisation and other operational metrics, enabling the identification and reporting of conditions that warrant operational attention. | `G09.01` — Operational Monitoring |
| `NFR-142` | Governed OA&M Information | `Principle` «ArchitecturalAxiom» | Harmonia's operational administration, monitoring and management (OA&M) information shall constitute a governed information set, subject to the applicable information governance principles, mandates and requirements, irrespective of the technologies or mechanisms used for its collection, persistence and management. | `G09.05` — Operational Information Management |
| `NFR-143` | Manual Processing Intervention | `Requirement` «FunctionalRequirement» | Harmonia shall support authorised manual intervention in stalled, failed or otherwise exceptional processing activities, consistent with the applicable workflow governance and operational control policies. | `G06.02` — Task Completion |
| `NFR-145` | Root Cause Investigation | `Principle` | Harmonia should support the investigation and analysis of operational failures, degraded conditions and abnormal behaviour to assist in identifying their underlying causes, potentially incorporating automated and AI-assisted analysis within governed Assurance workflows. | `G09.02` — Operational Diagnostics |
| `NFR-146` | Controlled Component Shutdown and Restart | `Constraint` «Mandate» | Harmonia shall support controlled shutdown and restart of its operational components, modules and subsystems, preserving accepted governed work and recoverable processing state throughout the transition. | `G09.03` — Operational Control |
| `NFR-147` | Automated Interaction Contract Verification | `CourseOfAction` «ImplementationGuideline» | Harmonia should, where practical, support automated verification of its implemented interaction contracts, encompassing internal component interoperability and conformance of Harmonia's external interaction interfaces to their applicable specifications. | `G11.02` — Integration Verification |
| `NFR-148` | Governed Reference Test Harness | `Constraint` «Mandate» | Harmonia shall maintain a governed reference test harness that is reviewed, updated and executed as part of each solution release, providing regression verification of the solution's functional and non-functional behaviour against its applicable requirements and architectural contracts. | `G11.06` — Test Governance |
| `NFR-149` | Repeatable Performance Testing | `Principle` | Harmonia should, where practical, support repeatable performance and capacity testing using representative workloads and operational conditions, enabling meaningful comparison of results across solution releases. | `G11.04` — Performance and Capacity Testing |
| `NFR-150` | Controlled Resilience Testing | `Constraint` «Mandate» | Harmonia's governed reference test harness shall support the controlled simulation of representative infrastructure and component failures to verify solution resilience, failure handling and recovery behaviour. | `G11.05` — Resilience Testing |
| `NFR-151` | Processing Outcome Evidence | `Requirement` «AssuranceRequirement» | Harmonia shall provide sufficient diagnostic and execution evidence to enable verification of processing outcomes against their defined expectations and applicable processing obligations. | `G06.06` — Processing Outcomes |
| `NFR-152` | Architecture-Implementation Alignment | `Goal` | Maintain demonstrable alignment between Harmonia's governed architecture and its implemented solutions throughout the solution lifecycle. | `G01.04` — Architecture Conformance |
| `NFR-153` | Distributed Temporal Consistency | `Principle` «ArchitecturalAxiom» | Harmonia shall support the consistent representation, interpretation and management of temporal information across distributed operational domains, including the identification and management of event horizons, temporal ordering and temporal uncertainty where relevant to governed processing activities. | `G13.02` — Temporal Consistency |
| `NFR-154` | External Information Sovereignty | `Principle` «ArchitecturalAxiom» | Harmonia shall preserve the authority of external information sources by supporting governed references to externally managed information and enabling its retrieval through Pyria when required, without requiring that information to be incorporated into Harmonia's persistent information holdings. | `G02.05` — Information Sovereignty and Residency |
| `NFR-155` | Information Residency Governance | `Principle` «ArchitecturalAxiom» | Harmonia shall support the governance of information residency through its persistence framework, enabling the physical storage location of governed information to be determined and controlled by the applicable Mnemosyne PersistenceService, consistent with relevant information governance and deployment requirements. | `G02.05` — Information Sovereignty and Residency |
| `NFR-156` | External Dependency Resilience | `Goal` | Harmonia aims to explicitly identify and document its external service dependencies for each solution release and, where practical, support continued operation during the unavailability of external services for a defined period, with resilience objectives established according to the requirements and characteristics of each deployment. | `G07.05` — Continuity of Operations |

## Identifier reservations

The identifiers `NFR-116`, `NFR-120`, `NFR-128`, `NFR-134`, `NFR-140` and `NFR-144` are reserved and have no active architectural statement in this register.

## Architectural semantics

- A subject subgroup is the primary containment classification for a statement; classification and subject are orthogonal.
- Composition denotes actual containment. Aggregation, Specialisation, Realisation, Association and Grouping retain their distinct ArchiMate semantics. Grouping alone does not imply ownership or containment.
- Realisation does not establish logical home.
- Conformance direction (`ArchitecturalAxiom`), conformance assurance strategy (`CourseOfAction`) and release conformance evidence (`AssuranceRequirement`) are distinct concepts.
- Information and processing obligations retain their stated release scope, applicability qualifiers and unresolved thresholds.
