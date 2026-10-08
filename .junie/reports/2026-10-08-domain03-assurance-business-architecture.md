# Domain03 — Bounded Health Service Assurance Business Architecture Derivation

**Date:** 2026-10-08. **Outcome:** Bounded Business Architecture documentation completed; additional architecture and the Guardian canonical-name collision remain explicitly unresolved for human review. No later-domain derivation or implementation work commenced.

This report records execution, verification and handover. It is **not architectural authority**. The task authorises the Guardian Role and directly supported consequences; canonical Domain01/02 architecture and Domain03 rules govern the derivation. This report does not accept candidate architecture or freeze/refreeze a domain.

## 1. Applicable Architecture Read and Authority Applied

The incoming Git worktree was clean. Inspection covered all fifteen incoming Domain03 Markdown documents before editing, plus the applicable upstream architecture and repository instructions.

| Material inspected | Purpose |
| :--- | :--- |
| [Root AGENTS.md](../../AGENTS.md), [documentation AGENTS.md](../../docs/AGENTS.md), [central axioms](../../docs/architectural-axioms.md), [memory-recovery context](../../docs/memory-recovery.md) | Architectural authority, scope, evidence and bounded execution. Memory recovery supplied context only; it did not override canonical architecture. |
| [Domain01 orientation](../../docs/markdown/01-motivation/README.md), [foundational requirements](../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md), [independent-assurance reconciliation](../../docs/markdown/01-motivation/reviews/independent-assurance-reconciliation.md), applicable [stakeholder](../../docs/markdown/01-motivation/stakeholders/enterprise-stakeholders.md), [outcome](../../docs/markdown/01-motivation/goals-outcomes/business-outcomes.md), [constraint](../../docs/markdown/01-motivation/requirements-constraints/external-constraints.md) and principle material | Confirm approved REQ-FND-005, sufficient trustworthy evidence, independent conclusion control, operational-management separation, evidence insufficiency and unchanged Motivation authority. |
| [Domain02 orientation](../../docs/markdown/02-strategy/README.md), [approved assurance derivation](../../docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md), relevant [Business Capability definitions](../../docs/markdown/02-strategy/capabilities/business-capabilities.md), [assurance Business Enabling definitions/principles](../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md#health-service-assurance-approved-business-enabling-capabilities), [EC-12/EC-14 definitions](../../docs/markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-12-operational-assurance), [five-region reconciliation](../../docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md) | Confirm BC-18, the three assurance capabilities, collaborative EC contributions, clinical boundaries, contextual evidence ownership, temporal applicability, assessment/adjudication and unresolved downstream allocation. |
| [Domain03 orientation](../../docs/markdown/03-business-architecture/README.md), [metamodel](../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md), [Actors](../../docs/markdown/03-business-architecture/actors-roles/actors.md), [Roles](../../docs/markdown/03-business-architecture/actors-roles/roles.md) | Apply current actor/role semantics, owning-capability anchoring, Function/Service/Process thresholds, qualified references and canonical abbreviations. Identify the pre-existing legal Guardian name. |
| [Collaborations](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md), [Interactions](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md), [behaviour index](../../docs/markdown/03-business-architecture/behaviours/index.md) and all five behaviour documents, [Processes](../../docs/markdown/03-business-architecture/processes/business-processes.md), [dependencies](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md), [Information Responsibilities](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Review all requested Business Architecture categories and existing clinical, operational, information and coordination demarcations before deriving consequences. |
| [Master documentation index](../../docs/README.md), relevant [deferred register boundaries](../../docs/deferred-document-register.md#item-04--capability-modelling-metamodel-and-identifier-conventions), validation-file inventory and the existing task-local link validator | Confirm navigation impact and verification method; do not reconcile recovered metamodel conventions. Historical completion reports informed verification conventions only. |

The task is outside the convergence/runtime implementation programme; no runtime milestone or implementation step was selected, commenced or updated.

### Axiom Conformance

| Relevant authority | How the change preserves it |
| :--- | :--- |
| **AX-14** | Governance definition, operational management, independent Guardianship and clinical review/clinical assurance remain different responsibilities. Evidence, assessment, adjudication, conclusion, workflow completion and operational response remain distinct. Legal representation and governed-assurance Guardian are not conflated. |
| **AX-17** | Derivation uses approved responsibility rather than names or solution familiarity. Missing Function names/decomposition, Service exposure, Process transitions, Interaction semantics, collaboration structure and authority allocations are recorded as unresolved. Existing Role naming is not silently changed. |
| **REQ-FND-005** | Guardian evaluates and concludes from sufficient trustworthy evidence; assurance progression/conclusion remain independently governed. The subject may supply evidence but cannot solely determine, suppress, manufacture or retrospectively alter its conclusion. Guardian may manage assurance's own activity without managing the subject. |
| **AX-06 / AX-07** | Source-information ownership/originating authority remain with the source capability; a Role does not grant information-access authority. Existing governed-access and security boundaries remain intact. |
| **AX-08 / AX-09** | Contextual evidence association does not turn every operational observation into permanent evidence or equate audit/provenance collection with assurance. No new retention/preservation mechanism is derived. |
| **AX-15 / REQ-FND-004** | Execution failure or indeterminate execution outcome remain distinct from an assurance activity that executes correctly but lacks sufficient subject evidence. No binary assurance taxonomy is invented. |
| **AX-05 and existing responsibility invariants** | No active/durable-state, persistence, application-access or execution allocation is made. Established source-information ownership and operational management remain intact. |

The incoming Guardian name collision is the material architectural issue discovered. No further conflict was identified that prevents documenting the authorised Role responsibility and direct boundaries. Unresolved matters below remain unresolved; this is not a claim of complete Business Architecture coverage.

## 2. Architecture Directly Derived and Documented

| Direct consequence | Canonical documentary result and limit |
| :--- | :--- |
| **Guardian Business Role** | Added to the existing Governance / Authority family with the task's exact definition. The family placement expresses independently governed assurance responsibility without granting governance-defining authority. No seventh family or actor category is created. Canonical Role ID remains unresolved under the existing metamodel; the incoming legal Guardian name remains unchanged pending disambiguation. |
| **Responsibility separation** | Documented Governance defines → Management performs → Guardianship assures as semantics, not topology or compulsory execution order. Possible fulfilment of different Roles by one Actor remains subject to REQ-FND-005 control independence. Guardian's evaluation/conclusion responsibility is within Governed Assurance; design, criterion approval, operational response and clinical review are not automatically assigned to it. |
| **Clinical demarcation** | Guardian confers no clinical authority or judgement of clinical adequacy/correctness. Existing clinical review and clinical assurance remain with applicable clinical processes/authorities. Explicitly required assurance of facts concerning clinical activity does not constitute Clinical Services Delivery Assurance. |
| **Capability-scoped behaviour consequences** | Added a bounded assurance view using the three established capability definitions. It documents design/disposition, criteria management and independent evaluation/conclusion responsibilities without naming or adding discrete Functions, Services or Processes. The new file is not a sixth contextual view, placement decision or structural ancestor. |
| **Assurance-related Business Information responsibilities** | Added a responsibility-demarcation table: Assurance Design defines how satisfaction is assured; Assurance Criteria Management manages governed temporally identifiable criteria; Governed Assurance owns contextual evidentiary association and establishes its assessments/findings/conclusions. Source-information ownership and governing-requirement authority remain distinct. No named asset catalogue, Information Object or representation is created. |
| **Temporal evidence and conclusion limits** | Carried forward relevant-state evidence association, temporally appropriate criteria, assessment/adjudication distinction and explicit evidence insufficiency. No taxonomy, confidence algorithm, retention rule or storage mechanism is added. |
| **Catalogue/dependency consequences** | Documented why EC contributions, evidence needs, reportability and generic coordination do not establish Business Service consumption, new Interactions, Guardian collaboration membership or an assurance Process. Existing clinical and operational definitions are clarified at their catalogue boundaries. |
| **Navigation** | Added the assurance page to the Domain03 tree/reading path and behaviour index. Domain03 owns this navigation; the master documentation index has no affected detailed Domain03 entries and required no edit. |

The candidate assessment is canonical in [the bounded derivation's unresolved-elements table](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md#5-additional-business-elements-not-yet-established). The report does not supply additional architecture.

## 3. Existing Architecture Clarified but Not Changed

- Seven Actor categories, six Role families and all existing Role definitions remain; the Governance / Authority family's description is extended to include the new independently governed responsibility. Care-support Guardian retains legal custody/personal-welfare meaning.
- Clinical Information Review, Review Request/Outcome, Practitioner Collaboration and clinical collaboration responsibilities remain clinical. An audit/review label does not establish independent-assurance semantics. No generic clinical-assurance authority Role is created.
- All five existing behaviour catalogues and their Functions, Services, owning contexts, Feature associations and recorded uncertainties are byte-identical to the incoming baseline.
- Seven Business Collaborations, ten primary Interaction categories plus supplementary categories, sixteen Process definitions/state models, existing dependency edges/rows and the original Information Responsibility matrix are preserved.
- Work allocation, coordination, operational escalation, remediation and expected failure/recovery remain with their established subject responsibilities. No Guardian control or assurance execution allocation is inferred from generic activity coordination.
- Source-information ownership, originating clinical authority, legal mandate/consent authority, governance requirements and existing access-control responsibilities are preserved.
- CT1 / CT2 / CT3 / FT terminology, FN / SV / PR identifiers, established FEAT spelling, qualified reference grammar and unresolved ancestry/structural IDs are preserved. Deferred recovered conventions are not substituted.
- Domain01, Domain02, Domain04, central axioms, requirements, ADRs, implementation and test definitions are unchanged. The approved AX-05 responsibility separation remains intact.

## 4. Unresolved Business Architecture Decisions Requiring Human Review

These are questions requiring decisions, not accepted candidate elements.

| Decision needed | Why approved architecture is insufficient |
| :--- | :--- |
| **Guardian canonical-name/reference disambiguation** | The incoming Care Support → Representative Role is already named Guardian. The requested governed-assurance Guardian has a different responsibility. Bare `-as-Guardian` is ambiguous in the existing grammar. Human review must decide canonical names/references; no rename, alias or grammar/namespace workaround is approved here. A clarification was raised during execution; absent an explicit decision, both meanings and this unresolved collision are preserved. |
| **Business Function names and decomposition** | Capability responsibilities do not settle Function granularity, names, separate assessment/adjudication/reporting Functions or Feature associations. Approve these within the established owners before adding entries. |
| **Exposed Business Services** | Identify behaviour actually exposed, owning Function/capability, an identifiable outside consumer and service contract. Reusable criteria and reportable conclusions are insufficient by themselves. |
| **Assurance/criteria Processes** | Establish bounded activity instances, initiation/completion, lifecycle states, transitions, dispositions and insufficiency treatment. EC-14's explanatory sequence is explicitly not a Process specification; generic operational lifecycles supply no equivalence. |
| **Business Interactions** | Establish participating Actors/Roles, purpose, commitments and initiation/response/outcome semantics before naming exchanges for evidence contribution, assurance initiation or findings. Clinical Review Request/Outcome cannot supply assurance semantics through similarity. |
| **Guardian collaborations** | Establish an enduring structured Actor/Role relationship, purpose and independently governed control relationships. Neither an interaction nor collaborative EC realisation meets that threshold automatically. No membership of an existing collaboration is assigned. |
| **Further responsibility allocation** | Decide eligible Actors, mandates, coverage/applicability, disposition authority, criterion approval, additional Role responsibilities and findings-to-response handover. Guardian's evaluation/conclusion responsibility does not decide these. |
| **Further Business Information responsibilities** | Decide additional asset names/decomposition, information authority/approval, custody, detailed lifecycle, retention and conclusion revision responsibilities. The direct responsibility demarcations do not establish a complete information catalogue. |
| **Concrete dependencies** | Identify exact evidence/criteria providers, exposed Business Services and consumers. The approved EC contribution matrix supplies no consumption edges; evidence provision must not transfer control of assurance. |

Capability Tier, complete CT ancestry, Feature placement and structural Canonical IDs remain unresolved. This derivation does not resolve them or require their speculative completion to document an established responsibility.

## 5. Downstream Matters Deliberately Not Addressed

No Domain04 Information Architecture derivation, assurance Information Concept/model, Information Object allocation, outcome taxonomy or representation is performed. Domain04 content is unchanged; automated inbound-link inspection is not downstream architectural derivation.

No Application Component, runtime service, implementation class, persistence structure, FHIR resource or binding, Pragma, Praxis, Ponos, Dokimasia, Digital Twin, Mneme, Mnemosyne or other solution allocation is made. No clinical decision/assurance engine, deployment topology or organisational separation is prescribed. EC-14 remains an Enterprise Capability; no allocation to an existing or anticipated solution is established.

No criterion catalogue, applicability/confidence model, exhaustive coverage obligation, conventional assurance programme, generic clinical-assurance authority, recovered metamodel reconciliation or identifier migration is invented. Existing upstream historical statements and unrelated deferred architecture are not rewritten. No implementation code or test definitions are modified.

## 6. Files Changed

| File | Change |
| :--- | :--- |
| [Domain03 README](../../docs/markdown/03-business-architecture/README.md) | Orientation, navigation and assurance derivation scope. |
| [Role catalogue](../../docs/markdown/03-business-architecture/actors-roles/roles.md) | Guardian definition/boundaries, family presentation, independence and explicit legal-role naming collision. |
| [Behaviour index](../../docs/markdown/03-business-architecture/behaviours/index.md) | Navigate to the bounded assurance view without assigning contextual placement. |
| [Health Service Assurance view](../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md) | **New** canonical Business Architecture derivation and unresolved-elements assessment. |
| [Collaboration catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/collaborations.md) | Guardian collaboration threshold and preserved clinical/compliance-monitoring boundaries. |
| [Interaction catalogue](../../docs/markdown/03-business-architecture/collaborations-interactions/interactions.md) | Clinical-review clarification and assurance Interaction derivation limits. |
| [Process catalogue](../../docs/markdown/03-business-architecture/processes/business-processes.md) | Assurance Process derivation threshold, generic-coordination and clinical/operational boundaries. |
| [Dependency material](../../docs/markdown/03-business-architecture/dependencies/cross-capability-dependencies.md) | Distinguish Strategy contributions/evidence needs from approved Service consumption. |
| [Information Responsibility material](../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) | Direct assurance-related responsibility demarcations and contextual/temporal evidence limits. |
| [This report](2026-10-08-domain03-assurance-business-architecture.md) | **New** non-authoritative execution, verification and human-review handover. |

## 7. Verification Actually Executed

### Required Repository Architecture Suite

```bash
timeout --signal=TERM --kill-after=10s 180s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**PASS — exit 0, BUILD SUCCESS, Maven elapsed 18.007 seconds.** Maven output and Surefire XML establish **90 tests in 11 architecture suites; zero failures, errors or skipped tests**. All required architecture classes and the additional governed-write/authoritative-persistence architecture suites ran. No stall or timeout occurred. Architecture tests verify repository conformance, not architectural approval or completeness of the new Business documentation.

### Documentation and Preservation Verification

Documentation checks are task-local read-only validators; no repository test/validator code is added.

- **PASS — local links/anchors:** 111 local links across all sixteen final Domain03 Markdown files and this report, plus 307 inbound references from documentation and Junie Markdown to affected sources. Zero missing targets/anchors, zero pre-existing link defects in the checked source set and zero newly introduced defects. External URLs were not network-tested.
- **PASS — structure and scoped preservation:** 98 checks, zero failures. Seventeen Markdown files parsed with `markdown_it`, including 15 tables and 23 closed fenced blocks. Final newlines are present; no new heading-spacing or trailing-whitespace defects. `git diff --check` passed.
- **PASS — existing architecture preservation:** Actor catalogue, metamodel and all five existing behaviour documents are byte-identical. Original Collaboration, Process, dependency and Information Responsibility content remains an exact prefix of the updated files. All existing Role and Interaction definition lines are preserved exactly. Seven Collaborations, ten primary Interaction categories and sixteen Processes retain their definitions and counts.
- **PASS — scope/preservation:** SHA-256 comparison confirms 2,263 pre-existing tracked files outside the allowed change set are byte-identical, including Domains01/02/04, axioms, requirements, ADRs, deferred register, implementation and test definitions. The final worktree contains exactly the ten Markdown changes listed above: eight modified existing files and two new files.
- **PASS — semantic/exclusion review:** Exact requested Guardian definition, legal Guardian meaning, unresolved naming collision, clinical/operational/independence boundaries, all seven requested candidate categories, EC-14/Role distinction and lack of speculative allocation are checked. No solution construct name is introduced in added canonical Domain03 text. No new Function/Service/Process catalogue entry or Feature/structural ID is assigned.

The initial **overbroad heading-spacing probe returned FAIL** because it checked the entire incoming corpus against a formatting preference rather than limiting itself to this change; it reported 123 existing heading-spacing occurrences. Inspection established these as baseline formatting, not an implementation or new documentation failure. The validator was corrected to compare each file with its incoming snapshot; the final scoped comparison found no new defects. Existing architecture was preserved rather than reformatted to satisfy the probe. This harness correction is retained in the initial/final validation evidence.

Task-local evidence is retained under `/tmp/harmonia-domain03-assurance/`: baseline hashes and incoming Domain03 snapshot, architecture test log and XML-derived summary, link/structure/preservation validation and final transaction diff. Temporary evidence is not canonical architecture.
