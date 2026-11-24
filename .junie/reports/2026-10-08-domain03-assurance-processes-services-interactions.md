# Domain03 Health Service Assurance — Processes, Services and Interactions

**Date:** 2026-10-08

**Scope:** Bounded Business Architecture continuation using approved human-review decisions.

**Status:** Three Processes, three Services and three Interactions are documented with approved Role relationships and boundaries. Detailed progression, further exchanges, Collaborations and downstream architecture remain unresolved. No Domain03 freeze/refreeze is performed.

This report is execution history and handover only. It is **not architectural authority**. The [initial assurance report](2026-10-08-domain03-assurance-business-architecture.md) and [Roles/Functions report](2026-10-08-domain03-assurance-roles-functions.md) supplied context only and remain unchanged. The user's approved decisions and canonical architecture govern this task.

## 1. Approved Architecture Documented

Before editing, the affected canonical material was reviewed: [AX-14/AX-17 and relevant information/evidence/uncertainty axioms](../../docs/architectural-axioms.md), [REQ-FND-005](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity), [BC-18 and management-monitoring boundaries](../../docs/markdown/02-strategy/capabilities/business-capabilities.md#18-health-service-assurance), the [approved assurance Strategy derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md), [Assurance Design, Assurance Criteria Management and Governed Assurance](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities), [EC-02/EC-14](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md), and the current [Domain03 metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), Roles, assurance behaviour, Processes, Interactions, Collaborations, dependencies and information-responsibility material. Existing solution constructs supplied no derivation authority.

| Approved architecture | Documentary outcome and responsibility boundary |
| :--- | :--- |
| **Assurance Process Design — PR** | Owned by Assurance Design; principally performed through Service Assurance Modeller. Design Service Assurance and Manage Assurance Criteria participate; the latter retains its Assurance Criteria Management ownership. Conceptual output is Assurance Definition. No combined criteria lifecycle, approval/publication workflow or modeller approval authority is established. |
| **Assurance Process Execution — PR** | Owned by Governed Assurance; principally performed through Service Guardian. Uses an applicable Assurance Definition and identifiable governed subject; participates through the three established Guardian Functions to establish findings/conclusions from applicable trustworthy evidence. Function completion is not a state model; evidence insufficiency remains explicit. |
| **Assurance Process Reporting / Communication — PR** | Owned by Governed Assurance; communicates already established findings/conclusions through the approved Service/Interaction. Neither assesses/adjudicates nor alters/reinterprets the outcome. Documentary report production is not required. No additional reporting Function or recipient-response responsibility is created. |
| **Request Service Assurance — SV** | Exposed by Governed Assurance to authorised Care Coordinator, Service Coordinator, Regulator, Policy Authority and System Steward. Requesting does not alter Assurance Definition/Criteria or control evidence assessment, adjudication or conclusion. |
| **Request Service Assurance Status — SV** | Exposed by Governed Assurance exclusively to System Steward for business progression observation. It is not general requester tracking and communicates no provisional/predicted/unadjudicated finding or conclusion. No status taxonomy is invented. |
| **Communicate Service Assurance Outcome — SV** | Exposed by Governed Assurance to applicable authorised recipients among the five approved Roles. Outcome is already established; communication is not mandatory broadcast or responsibility for subsequent recipient action. Requester, status consumer and recipient need not coincide. |
| **Service Assurance Request — Business Interaction** | Service Guardian and one authorised requester from the five Roles; associated with Request Service Assurance. Participant variation does not create separate Interactions. |
| **Service Assurance Status — Business Interaction** | System Steward and Service Guardian; associated with Request Service Assurance Status. Progression observation SHALL NOT constitute, imply or expose an unadjudicated finding/conclusion. |
| **Service Assurance Outcome Communication — Business Interaction** | Service Guardian and one or more authorised recipients from the five Roles; associated with Communicate Service Assurance Outcome. Recipient interpretation and subsequent clinical, operational, policy/compliance or improvement action remain within the recipient's mandate. |
| **Process-family relationship** | Design → conceptual Assurance Definition → Execution → established Finding / Conclusion → Reporting / Communication is a semantic relationship. Definitions may be reused by multiple Execution instances. No mandatory design-per-execution, runtime orchestration, deployment sequence or message flow is prescribed. |
| **Consumer concerns** | Care Coordinator: clinical management; Service Coordinator: operational management and potentially task/people improvement; Regulator: service governance/compliance; Policy Authority: governance/compliance and policy effectiveness/improvement; System Steward: ICT process improvement and stewardship of assurance progression. These are non-exhaustive concerns within existing Role mandates. |

The [Process catalogue](../../docs/markdown/03-business-architecture/processes/business-processes.md#6-health-service-assurance-processes) contains exact approved purposes and two semantic diagrams. The [capability-scoped Service catalogue](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#5-approved-governed-assurance-business-services), [Interaction entries](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md#5-approved-service-assurance-interactions) and [dependency relationships](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md#4-assurance-dependency-derivation-boundary) document approved exposure/participation without allocating consuming Capabilities from Role names. Detailed internal Service-to-Function mappings remain unestablished; the five Functions are unchanged.

AX-14 is preserved by distinguishing modelling, progression observation, assessment, adjudication, communication and recipient action. AX-17 is preserved by documenting approved behaviour without speculative states, Functions, exchanges, ancestry or allocations. REQ-FND-005 control independence remains intact even when a requester performs/manages the subject or an Actor fulfils multiple Roles. Source-information ownership, governed access, meaningful/temporal evidence and evidence insufficiency remain unchanged under AX-06/07/08/09. Execution uncertainty under AX-15 is not collapsed into an assurance conclusion. No material upstream conflict was identified; the approved non-recursion boundary distinguishes execution integrity from governed-subject assurance without rewriting upstream authority.

## 2. Previous Unresolved Items Now Resolved

- The three Process names, purposes, bounded ownership, principal Design/Execution Roles and approved participating Functions are established. Detailed lifecycle states/transitions remain unresolved.
- The three Service names, behaviour, exposure owner and approved Role consumers/recipients are established. Detailed contracts and internal realisation remain unestablished.
- The three Interaction names, purposes, Role participants and associated Services are established. Detailed Actor eligibility and exchange mechanics remain unresolved.
- Assurance status concerns activity progression and exposes no unadjudicated finding/conclusion; established-outcome communication remains distinct from status, assessment/adjudication and subsequent response.
- Financial Governance's deliberate exclusion and assurance non-recursion are explicit settled boundaries. Neither is retained as an architectural gap.

The canonical [resolved/unresolved assessment](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#6-additional-business-elements-not-yet-established) is updated. Historical reports retain their earlier unresolved findings as execution history.

## 3. Deliberate Scope Exclusions

**Financial Governance Exclusion:** Financially relevant assurance information may be received by appropriately authorised external financial-governance activities without Harmonia acquiring financial-governance responsibility. No distinct Financial Governance / Service Governance (Financials) Role, Function, Capability, Service, Process or Collaboration is created. This exclusion is deliberate; the exact external recipients/mandates remain unresolved without creating a sixth approved Role relationship.

**Assurance non-recursion:** Governed Assurance does not recursively assure its own execution. Assurance activity execution integrity is provided by the established activity execution framework. Managed assurance progression does not make its execution another Service Assurance subject, and System Steward status observation is management monitoring/stewardship. No Assurance of Assurance, Guardian-of-Guardian, recursive Function, Process, Service or conclusion is created. The framework reference allocates no solution component.

## 4. Existing Architecture Clarified but Unchanged

- Service Assurance Modeller and Service Guardian retain their canonical names, definitions, Function responsibilities and clinical/operational/authority boundaries. Legal/personal-welfare Guardian remains intact. Existing Role definitions, six families and seven Actor categories are preserved.
- The five Functions and their entire capability-scoped responsibility section remain byte-identical. Evidence assembly remains within context establishment; conclusion establishment remains within adjudication. No reporting/status/request Function is added.
- Governance Authority remains exercised through existing mandated Roles. Modeller does not acquire approval authority; requester does not acquire assessment/adjudication control; communication does not transfer recipient response or source-information ownership to Service Guardian.
- Sixteen original Process definitions/state models remain exact. Catalogue navigation adds the three assurance Processes, making nineteen; it does not allocate structural IDs or manufacture assurance states.
- Ten primary Interaction categories, supplementary definitions and existing boundary matrix are preserved. Seven existing Collaborations and their memberships are unchanged; only assurance-boundary commentary references the newly approved Interactions.
- Existing dependency topology/Capability-consumption rows and the original Information Responsibility matrix remain intact. New dependency material records Role-level consumption/communication without assigning Role names to consuming Capabilities.
- Actor catalogue, metamodel and five original contextual behaviour documents remain byte-identical. CT1/CT2/CT3/FT, FN/SV/PR, established Feature spelling and qualified reference grammar are unchanged. Historical navigation anchors are retained without becoming aliases/IDs.
- Domains01/02/04, axioms, requirements, ADRs, Deferred Register, runtime plan, implementation, test definitions and both prior assurance reports remain unchanged. No convergence/runtime milestone is selected or advanced.

## 5. Remaining Unresolved Business Architecture Questions

| Decision required | Remaining boundary |
| :--- | :--- |
| **Detailed Process progression** | Lifecycle states/transitions, initiation mechanics, completion/disposition semantics and detailed criteria lifecycle/approval/publication workflows remain unestablished. Function completion supplies no lifecycle taxonomy. |
| **Further exposure and dependencies** | Detailed Service contracts, internal Service-to-Function mappings, consuming Capability allocations, exact evidence/criteria providers and additional Services require explicit decisions. |
| **Detailed information semantics** | Request/status/outcome meaning, formal Assurance Definition modelling, criteria catalogues, outcome taxonomy, confidence models, information authority/custody/lifecycle and retention remain unresolved. |
| **Evidence acquisition/exchange** | Evidence may be available through existing governed information access; contextual association does not establish an Evidence Contribution Interaction. Any explicit evidence exchange requires separate derivation. |
| **Modeller-to-Guardian exchange** | Applicable Assurance Definition use establishes a responsibility/information dependency only. No explicit Interaction or new Service between the Roles is established. |
| **Collaborations** | No enduring structured assurance collective or existing membership is approved. The three Interactions do not mechanically create a Collaboration. |
| **Actor eligibility, mandates and authority** | Detail beyond the approved Role relationships, assurance coverage/applicability and exact requirement/disposition/definition/criterion approval allocation remain unresolved. |
| **Findings-to-response mechanisms** | Outcome communication is established; mechanisms for recipient interpretation and subsequent operational, clinical, compliance, policy or improvement action are not allocated to assurance. |
| **External financially relevant receipt** | Exact authorised external financial-governance recipients and mandates remain unresolved. Financial-governance responsibility itself is deliberately excluded from Harmonia. |
| **Structural placement** | Capability Tier/CT ancestry, contextual-view placement, Feature association and structural Canonical IDs remain unresolved. Interaction identifier conventions are not expanded. |

## 6. Downstream Matters Deliberately Deferred

No formal Information Concept, Information Object or Data Object is created for Assurance Request, Status, Definition, Context, Finding, Conclusion, Report or communication. These remain conceptual Business Architecture descriptions. No schema, payload, FHIR representation, message/event model or persistence structure is derived; Domain04 is unchanged.

No Process, Service, Interaction or framework responsibility is allocated to Dokimasia, Ponos, Praxis, Pragma, Digital Twins, Mneme, Mnemosyne, Application Components, technical APIs, queues, topics, Kubernetes, Java classes or other solution constructs. Three Business Services do not imply three technical APIs. Diagrams do not define runtime orchestration. No implementation code or tests are modified.

## 7. Files Changed

Nine existing canonical Domain03 Markdown files are modified; this report is the only new file.

| File | Change |
| :--- | :--- |
| [Domain03 README](../../docs/markdown/03-business-architecture/README.md) | Catalogue counts, approved behaviour navigation and unresolved/scope boundaries. |
| [Role catalogue](../../docs/markdown/03-business-architecture/actors-roles/roles.md) | Approved Process/Interaction participation and consumer relationships; existing definitions/mandates preserved. |
| [Behaviour index](../../docs/markdown/03-business-architecture/behaviours/index.md) | Navigates approved Processes, Services and Interactions without assigning view/CT placement. |
| [Health Service Assurance view](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md) | Three capability-scoped Service entries, consumer concerns, status/outcome boundaries, deliberate exclusions and updated resolved/unresolved assessment. |
| [Process catalogue](../../docs/markdown/03-business-architecture/processes/business-processes.md) | Three approved assurance Processes, ownership/participation, semantic diagrams, reuse and non-recursion; original sixteen definitions retained. |
| [Interaction catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) | Three approved Interactions and participants; clinical, operational, exchange and Collaboration limits. |
| [Collaboration catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) | Clarifies that approved Interactions do not establish an enduring collective; no membership change. |
| [Dependency material](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) | Approved Role-level Service relationships without inventing consuming Capability or evidence-exchange edges. |
| [Information Responsibility material](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Conceptual request/status/outcome/communication distinctions and unchanged ownership; no formal information modelling. |
| [This report](2026-10-08-domain03-assurance-processes-services-interactions.md) | New non-authoritative execution, validation and handover record. |

## 8. Validation Performed and Results

### Required Repository Architecture Suite

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS — exit 0, BUILD SUCCESS, Maven elapsed 18.110 seconds.** Maven output and Surefire XML establish **90 tests across 11 architecture suites; zero failures, errors or skipped tests**. No timeout or stall occurred. These tests verify repository conformance, not architectural approval or completeness of the Business documentation.

### Documentation and Preservation Verification

Task-local read-only validators check the Domain03 corpus and this report; no repository validator/test code is added.

- **PASS — links/navigation:** 192 local links/anchors across all sixteen Domain03 Markdown files and this report, plus 409 inbound documentation/Junie references to edited sources. Zero missing targets/anchors, zero baseline defects in the checked source set and zero new defects. Historical anchors continue to resolve. External URLs were not network-tested.
- **PASS — documentation/semantics/preservation:** 220 checks, zero failures. Seventeen Markdown files parsed with `markdown_it`, including 15 tables and 27 closed fenced blocks. Final newlines, no new heading-spacing/trailing-whitespace defects and `git diff --check HEAD` are verified.
- **PASS — approved element counts and relationships:** Exactly three new assurance Processes, three Services and three Interactions with exact approved purposes/definitions, capability ownership, Role participants and Service associations. All five Roles may request/receive applicable outcomes; only System Steward consumes status. No mandatory broadcast, general requester tracking or requester control of assessment/adjudication is established.
- **PASS — boundaries and exclusions:** Status exposes no provisional/predicted/unadjudicated finding/conclusion; reporting starts from an established outcome without assessment/adjudication, alteration or recipient-action responsibility. Financial Governance is deliberately excluded; no new Financial Governance Role/Function is created. No Evidence Contribution Interaction, Modeller-to-Guardian Interaction, Collaboration, recursive assurance construct, formal Information Architecture or solution allocation is introduced.
- **PASS — diagram source review:** Execution diagram: 7 declared nodes and 6 semantic edges. Process-family diagram: 5 declared nodes and 4 semantic edges. Source structure and expected responsibility relationships are checked; no rendered-diagram verification is claimed. The two pre-existing assurance responsibility/contribution diagrams are unchanged.
- **PASS — existing architecture preservation:** Role tree, all original Role definitions and legal Guardian are intact; two assurance Role names/responsibilities remain unchanged. The entire five-Function responsibility section, sixteen original Process definitions/state models, existing Interaction definition lines/distinction matrix, all seven Collaboration definitions/memberships, dependency topology/Capability rows, original information matrix and assurance information-demarcation table are preserved. Actor catalogue, metamodel and all five original contextual behaviour documents are byte-identical.
- **PASS — scope/preservation:** SHA-256 comparison against the incoming 2,274 tracked-file baseline establishes 2,265 tracked files outside the allowed change set as byte-identical, including both historical assurance reports, Domains01/02/04, axioms, requirements, ADRs, Deferred Register, implementation and test definitions. The final worktree contains exactly nine modified Domain03 documents and this new report. No named solution construct is introduced in added canonical text.

Evidence is retained under `/tmp/harmonia-domain03-assurance-processes-services-interactions/`: incoming Domain03/prior-report snapshots, baseline hashes, architecture log/XML-derived results, documentation/link checks and final transaction diff. Temporary evidence is not canonical architecture.
