# Domain03 Health Service Assurance — Approved Roles and Functions

**Date:** 2026-10-08

**Scope:** Bounded Business Architecture continuation following approved human review.

**Status:** The approved Role names and five Functions are documented; remaining architecture is explicitly unresolved. Domain03 has not been frozen/refrozen.

This report is execution history and handover only. It is **not architectural authority**. The [previous derivation report](2026-10-08-domain03-assurance-business-architecture.md) supplied execution context and remains unchanged. Authority for the new decisions is the user's approved human-review instruction, interpreted consistently with canonical upstream architecture; neither report supplies additional architecture.

## 1. Approved Architecture Documented

The affected Domain01, Domain02 and Domain03 material was reviewed before editing: [AX-14 and AX-17](../../docs/architectural-axioms.md), [REQ-FND-005](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md#req-fnd-005-independent-assurance-of-governed-activity), [BC-18](../../docs/markdown/02-strategy/capabilities/business-capabilities.md#18-health-service-assurance), the [approved assurance Strategy derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md), [Assurance Design, Assurance Criteria Management and Governed Assurance](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities), [EC-02](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-02-context-management), [EC-14](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-14-service-guardian), and the current [Domain03 metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), Role, behaviour, Collaboration, Interaction, Process, dependency and information-responsibility catalogues. Memory-recovery material was treated as context, not as authority for recovered conventions or solution allocation.

| Approved element or relationship | Canonical documentary outcome |
| :--- | :--- |
| **Service Assurance Modeller — Business Role** | Determines and models how satisfaction of a governed requirement, constraint or expected behaviour is to be assured, within Assurance Design and Assurance Criteria Management. Modelling does not inherently approve the requirement, Assurance Definition or Assurance Criteria. |
| **Service Guardian — Business Role** | Independently evaluates governed activity, information, state or outcomes against applicable assurance criteria and establishes findings/conclusions from sufficient trustworthy evidence. Replaces the previous assurance-role name Guardian; legal/personal-welfare Guardian is preserved. EC-14 intentionally shares the name as a distinct Enterprise Capability. |
| **Design Service Assurance — FN** | Owned by Assurance Design, performed through Service Assurance Modeller. Defines how satisfaction will be independently established, including disposition, criteria, evidence and evaluation expectations, without granting governance authority or performing the resulting assurance. |
| **Manage Assurance Criteria — FN** | Owned by Assurance Criteria Management, performed through Service Assurance Modeller. Manages reusable, governed, temporally identifiable criteria without transferring governing-requirement authority or inventing lifecycle states/approval workflows. |
| **Establish Assurance Context — FN** | Owned by Governed Assurance, performed through Service Guardian. Establishes subject, purpose, applicable criteria, temporal context and contextual evidence association. Includes evidence assembly; source-information ownership remains with the source Capability. |
| **Assess Assurance Evidence — FN** | Owned by Governed Assurance, performed through Service Guardian. Establishes what available trustworthy evidence demonstrates against each applicable criterion/assessment concern. Assessment remains distinct from adjudication; insufficient evidence implies neither satisfaction nor non-satisfaction. |
| **Adjudicate Assurance Assessment — FN** | Owned by Governed Assurance, performed through Service Guardian. Determines the finding/conclusion following from assessed evidence and applicable criteria. Includes conclusion establishment, without operational response, remediation, escalation, workflow progression or clinical judgement. |
| **Governance Authority / modelling / Guardianship responsibility model** | Reuses established Policy Authority, Regulator and existing steward mandates rather than creating a new authority Role. Authority establishes what is authoritative; Modeller models how satisfaction will be assured; Service Guardian independently establishes what the evidence demonstrates. Function ownership remains with Capabilities, distinct from Role performance. |
| **EC-02 + EC-14 context contribution** | Generic context-management capability and assurance-specific semantics together support Establish Assurance Context. Neither EC is the sole realiser or Function owner. No exposed Service, consumption edge or loader/unloader mechanism is inferred. |
| **Assurance Definition** | Documented as the conceptual modelling output/boundary of Design Service Assurance within Assurance Design. Formal Business Information element identity, information structure, lifecycle, approval and representation remain deferred. |

The [canonical assurance view](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md) contains the exact approved Function definitions, capability ownership, performing Roles and two responsibility/contribution diagrams. These are semantic models, not complete Processes or compulsory execution, organisational, application or deployment topology.

AX-14 is preserved by separating governance authority, assurance modelling, evidence association, assessment, adjudication, operational management and clinical authority. AX-17 is preserved by documenting only the approved bounded decisions and retaining uncertainty elsewhere. REQ-FND-005 independence continues to apply even where a real-world Actor fulfils different Roles: evidence contribution does not grant control of assurance progression or conclusion. AX-06/07 information authority/access, AX-08/09 contextual evidence and AX-15 execution uncertainty remain distinct from assurance conclusions and evidence insufficiency. No material conflict with these authorities was identified.

## 2. Previous Unresolved Items Now Resolved

| Previous question | Resolution and limit |
| :--- | :--- |
| **Guardian naming collision / qualified Role reference** | Resolved as Service Guardian for governed assurance. Guardian continues to identify legal custody/personal welfare. EC-14 remains Service Guardian. The existing reference grammar is preserved; historical document anchors are retained solely for navigation, not as Role names, aliases or Canonical IDs. |
| **Responsibility for assurance modelling** | Service Assurance Modeller is established, with the two approved modelling/criteria Functions. Existing governance-authority mandates are referenced without adding approval powers. |
| **Function naming and bounded decomposition** | Exactly five Functions are established. Evidence assembly is included in context establishment; finding/conclusion establishment is included in adjudication. No additional applicability-resolution, criteria-resolution, evidence-assembly, conclusion-establishment, reporting, remediation or escalation Function is created. Feature associations remain unestablished. |
| **Context Management relationship** | EC-02 generic context semantics and EC-14 assurance-specific semantics contribute together, consistently with their approved collaborative contributions to Governed Assurance. Detailed realisation remains downstream. |

Documenting Assurance Definition's conceptual output resolves how the approved responsibility boundary is described here; it does **not** resolve formal information modelling, approval allocation or representation.

## 3. Existing Architecture Clarified but Unchanged

- The legal/personal-welfare Guardian definition and tree entry remain intact. All other pre-existing Role definitions, six Role families and seven Actor categories retain their established meaning. No generic Clinical Assurance Authority or new Governance Authority Role is created.
- Neither assurance Role inherently possesses clinical authority, determines clinical correctness/adequacy, replaces professional clinical judgement, peer review or clinical governance, or assumes Clinical Services Delivery Assurance. Explicitly governed assurance concerning clinical information/behaviour does not transfer clinical responsibility.
- Neither Role manages the subject merely by designing or performing assurance. Service Guardian may manage its own assurance progression; subject progression, assignment, delegation, remediation and operational escalation remain with the responsible subject capability/authority.
- Source information retains its owning Capability and originating authority. Governed Assurance owns the contextual association that particular information constitutes evidence for a particular purpose, potentially temporal/version-specific. Evidence association does not prescribe copying, persistence, aggregation or caching.
- Existing seven Collaborations, ten primary Interaction categories and supplementary definitions, sixteen Processes/state models, dependency rows/edges and the original Information Responsibility matrix remain unchanged. Their assurance-boundary commentary is updated without creating elements or memberships.
- The Actor catalogue, metamodel and all five original contextual behaviour documents remain byte-identical. CT1/CT2/CT3/FT, FN/SV/PR, established Feature spelling, reference grammar and unresolved structural identifiers are preserved. The Deferred Register is not reconciled.
- Domains01/02/04, axioms, requirements, ADRs, implementation and test definitions, the runtime implementation plan and the previous assurance report remain unchanged. This task is outside the convergence/runtime implementation programme.

## 4. Remaining Unresolved Business Architecture Questions

These remain decision requests, not newly approved elements. Their canonical record is the [unresolved-elements assessment](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#5-additional-business-elements-not-yet-established).

| Decision required | Remaining question |
| :--- | :--- |
| **Business Services and consumers** | What behaviour is actually exposed, by which owner, under what contract, and to which identifiable outside consumer? Five Functions, reusable criteria and reportable conclusions do not mechanically create Services. |
| **Business Processes** | What bounded activity instances, initiation/completion conditions, lifecycle states, transitions, dispositions and evidence-insufficiency semantics are required for assurance and criteria management? Neither responsibility diagrams nor EC-14's sequence settle them. |
| **Business Interactions** | Which exchange-specific participants, commitments, initiation/response/outcome semantics and names govern evidence contribution, initiation, approval or findings communication? |
| **Collaborations** | Which enduring structured Actor/Role associations, purposes and permitted control relationships are justified for either assurance Role? No existing Collaboration membership is assigned. |
| **Actor eligibility, mandates and initiation** | Who may fulfil either Role, with what assurance coverage/applicability and initiation responsibility, while preserving control independence? |
| **Governance/approval allocation** | Which applicable mandates approve requirements, dispositions, Assurance Definitions and criteria, and through which workflows? Reusing existing authority Roles does not settle those exact allocations. |
| **Findings-to-operational-response handover** | Which responsible party receives findings, initiates response and owns any management, remediation or escalation? No response responsibility is assigned to Service Guardian. |
| **Further information responsibilities** | Formal Assurance Definition modelling/representation, detailed assurance concepts and asset identities, criterion catalogues, information authority/custody, lifecycle, conclusion approval/revision, outcome taxonomies, confidence models and retention remain unresolved. |
| **Concrete dependencies** | Exact evidence/criteria providers, exposed Services and consumers require explicit decisions. The EC contribution model does not establish consumption edges. |
| **Further Functions and structural placement** | Additional decomposition, Capability Tier, CT ancestry, contextual-view placement, Feature association/placement and structural Canonical IDs remain unresolved. No IDs or new namespaces are allocated. |

## 5. Downstream Matters Deliberately Deferred

No Domain04 derivation, Information Object, Data Object, schema, FHIR representation or persistence model is created. Assurance Definition remains conceptual. Reading existing links during consistency checks does not commence Information Architecture work.

No Application Component, runtime service, implementation class, context loader/unloader, persistence structure, execution/deployment allocation or implementation pattern is derived. No allocation is made to Dokimasia, Ponos, Praxis, Pragma, Digital Twins, Mneme, Mnemosyne or other existing/anticipated solution constructs. Existing implementation is not used as evidence of upstream authority.

No implementation code or test definition is modified. No later architecture domain, runtime milestone, upstream catalogue reconciliation or Domain03 freeze/refreeze is undertaken.

## 6. Files Changed

Nine existing canonical Domain03 Markdown files are modified; this report is the only new file.

| File | Change |
| :--- | :--- |
| [Domain03 README](../../docs/markdown/03-business-architecture/README.md) | Updated orientation, navigation and bounded Role/Function scope. |
| [Role catalogue](../../docs/markdown/03-business-architecture/actors-roles/roles.md) | Service Guardian naming resolution, Service Assurance Modeller, Function references and authority/boundary separation. |
| [Behaviour index](../../docs/markdown/03-business-architecture/behaviours/index.md) | Updated assurance view description without allocating contextual placement. |
| [Health Service Assurance view](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md) | Five approved Functions, responsibility/contribution models, conceptual output and resolved/unresolved assessments. |
| [Collaboration catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) | Both assurance Roles' derivation boundary; existing memberships preserved. |
| [Interaction catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) | Updated Role names and exchange/clinical/operational boundaries. |
| [Process catalogue](../../docs/markdown/03-business-architecture/processes/business-processes.md) | Clarified that approved Functions/responsibility models do not establish Process lifecycle semantics. |
| [Dependency material](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) | EC-02/EC-14 contribution relationship without Service consumption or implementation edges. |
| [Information Responsibility material](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Function/Role references, conceptual Assurance Definition, contextual evidence/source ownership and deferred formal modelling. |
| [This report](2026-10-08-domain03-assurance-roles-functions.md) | New non-authoritative execution, validation and handover record. |

## 7. Validation Performed and Results

### Required Repository Architecture Suite

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS — exit 0, BUILD SUCCESS, Maven elapsed 17.338 seconds.** Maven output and Surefire XML establish **90 tests across 11 architecture suites; zero failures, errors or skipped tests**. No timeout or stall occurred. These tests verify repository conformance; they do not approve or establish completeness of Business Architecture.

### Documentation and Preservation Checks

Task-local read-only validators check the final Domain03 corpus and this report without adding repository validator/test code.

- **PASS — links/navigation:** 125 local links/anchors across all sixteen Domain03 Markdown files and this report, plus 343 inbound documentation/Junie references to affected sources. Zero missing targets/anchors, zero baseline link defects in the checked source set and zero new defects. Historical anchors continue to resolve. External URLs were not network-tested.
- **PASS — documentation/semantics/preservation:** 142 checks, zero failures. Seventeen Markdown files parsed with `markdown_it`, including 14 tables and 25 closed fenced blocks. Final newlines, no newly introduced heading-spacing/trailing-whitespace defects and `git diff --check HEAD` are verified.
- **PASS — approved semantic boundaries:** Exact two assurance Role names/responsibilities and exactly five Function definitions; owning Capability/performing Role relationships; resolved naming/decomposition; separate assessment/adjudication; evidence insufficiency; temporal evidence association without source-ownership transfer; clinical, operational, approval and independence boundaries; EC-02/EC-14 contributions without sole ownership or implementation allocation; conceptual Assurance Definition and remaining decision scopes are verified.
- **PASS — diagram source review:** The responsibility diagram has 10 declared nodes and 9 edges; the context contribution diagram has 3 declared nodes and 2 edges. Source structure and intended responsibility/contribution edges are checked. No rendered-diagram verification is claimed.
- **PASS — existing architecture preservation:** Legal Guardian definition and Care Support tree are intact; all original non-assurance Role definitions remain exact. Actor catalogue, metamodel and five original behaviour documents are byte-identical. Existing Collaboration, Interaction, Process, dependency and information matrices/catalogues preceding the assurance-boundary sections are unchanged. Seven Collaborations, ten primary Interaction categories and sixteen Processes retain their established definitions/counts.
- **PASS — authorised change scope:** SHA-256 comparison against the incoming 2,273 tracked-file baseline establishes 2,264 tracked files outside the allowed change set as byte-identical, including the previous assurance report and upstream/downstream architecture, implementation and test definitions. The final worktree contains exactly nine modified Domain03 Markdown documents and this new report. No solution construct name is introduced in added canonical text.

The initial Care Support tree-preservation probe returned **FAIL** because it delimited the section using a nonexistent Administrative Roles heading and inadvertently included the approved Governance / Authority edits. The validator was corrected to use the existing Operational Roles boundary; the final comparison passes. No architectural content was changed to satisfy that probe. Initial/final evidence is retained to distinguish this harness defect from a documentation or architecture failure.

Task-local evidence is retained under `/tmp/harmonia-domain03-assurance-roles-functions/`: incoming snapshots, baseline hashes, architecture log/XML-derived results, documentation/link checks and final transaction diff. Temporary evidence is not canonical architecture.
