Task-Issued-At: 2026-11-10T10:43:52.224Z

Event source: Codex response_item user instruction-entry event; session 01a1c50e-2b7e-7741-b3e6-92831595f5fd, JSONL event line 24; operative instruction preserved verbatim

```
# Harmonia 1.1.21 — Stakeholder, Journey, UseCase and UserStory Incorporation

## Goal

Perform the governed ADD operation for Harmonia 1.1.21, incorporating the accepted Stakeholder, Journey, UseCase and UserStory architectural semantics into the Architecture Continuum.

This is substantive architectural incorporation, not preliminary discovery or another Capability Model consolidation.

## Expected Outcome

An integrated, governed architectural model containing:

1. The accepted Stakeholder catalogue and definitions.
2. Reusable, recursively structured Journeys.
3. Stakeholder participation in Journeys.
4. UseCases and UserStories with their accepted independent semantics.
5. Meaningful relationships among Journeys, Stakeholders, Requirements, Capabilities, Features and existing architectural elements.
6. Appropriate ArchiMate representations, navigable views, documentation and traceability.
7. Validation and execution evidence sufficient for Solution Architect review.

The outcome must preserve the accepted 1.1.20 architecture and its established identities.

## Context

The accepted predecessor is 1.1.20.

HCRL 1.1.21/001 has already established the development version and inherited architectural context. Its initialisation is complete; substantive modelling has not commenced.

The Architecture Continuum, its governed Markdown RAD, current repository governance and accepted decisions remain authoritative.

The 1.1.20 Capability Model, including its Business and Enterprise Capability hierarchies, is the accepted starting point.

The existing 784-candidate UseCase/UserStory assessment and associated source contributions are available for reconciliation. Preserve applicable prior identity and provenance decisions.

### Accepted Stakeholder catalogue

The agreed catalogue contains 16 perspectives:

1. Participant
2. Patient
3. Practitioner
4. Allied Health
5. Nurse & Support
6. Diagnostics
7. Operations (Facility)
8. Administrators (Facility)
9. Executive
10. Regulatory / Government
11. Researchers
12. Operations (System)
13. Administrators (System)
14. Responsible Person
15. External Organisation
16. Autonomous System

The final three were added during the 1.1.21 architectural discussion.

**Responsible Person** represents recognised responsibility for, or representation of, a Patient or Participant. It does not imply automatic authorisation.

**External Organisation** represents an organisation participating in its own capacity, independently of a particular individual.

**Autonomous System** represents independently meaningful participation by software, AI agents, Digital Twins or other computational entities. Ordinary automated processing does not automatically qualify as Stakeholder participation.

Family/Carer is not a separate Stakeholder perspective. Participant and Responsible Person accommodate those contexts.

Stakeholders represent architectural perspectives, not a Person superclass, persona catalogue or organisational role hierarchy.

### Stakeholder participation

The accepted contextual participation characteristics are:

- Initiator
- Participant
- Beneficiary
- Observer

Characteristics are optional, non-exclusive and dependent upon the Journey context.

Accountability is a separate governance concern, not a Journey participation characteristic.

Stakeholder participation and Journey progression are distinct architectural concerns.

### Journey semantics

A Journey is a reusable specialisation of an ArchiMate Value Stream.

Journey identity is governed primarily by meaningful progression and intended outcome.

A Journey does not necessarily deliver direct Stakeholder value. It may deliver value, enable another outcome, fulfil a clinical or regulatory obligation, or represent necessary operational work.

Do not manufacture Stakeholder value to justify a Journey.

A Journey may contain other Journeys. A **Journey Stage is a constituent Journey**, not a separate element type or specialisation.

Journey decomposition is recursive, without prescribed depth.

Constituent Journeys retain independent identity and may be reused. Use Composition or Aggregation according to the actual structural semantics; do not mechanically apply Composition to independently shared elements.

Journey definitions may express:

- Intended and alternative outcomes.
- Conditional and optional progression.
- Repetition.
- Concurrent progression.
- Asynchronous or long-running progression.
- Dependencies and relevant events.

These characteristics do not prescribe execution orchestration.

Distinguish:

- Structural composition.
- Meaningful progression and dependencies.
- Runtime execution and orchestration.

Actual outcomes and runtime states belong to particular occurrences, not reusable Journey definitions.

### UseCase semantics

UseCase is an accepted Requirement specialisation.

UseCases are primarily interaction-oriented and describe recognisable interactions directed towards a result.

A UseCase:

- Has independent architectural identity.
- Does **not** need to belong to a Journey.
- May have no Journey association.
- May contribute to multiple Journeys.
- May address technical, operational, administrative or clinical interactions.

A Journey may involve zero, one or many UseCases.

Absence of Journey association is not inherently a coverage defect.

### UserStory semantics

UserStory is an accepted Requirement specialisation.

UserStories express stakeholder needs, expectations or desired outcomes.

A UserStory may span:

- Multiple interactions and UseCases.
- System and non-system elements.
- Human, organisational and technical activities.
- Multiple Journeys.
- Functional and non-functional concerns.

A UserStory is not merely a stakeholder-specific restatement of a UseCase.

Successful execution of associated UseCases does not necessarily satisfy the broader UserStory.

UserStories retain independent identities and may exist without associated UseCases or Journeys.

### Architectural relationships

Preserve the following accepted semantics:

**Journey ↔ Capability/Feature**

Many-to-many across Business and Enterprise Capabilities.

A capability may be required by or contribute to a Journey. These distinctions are optional and contextual.

**Journey ↔ UseCase**

Optional, many-to-many. Neither construct requires the other.

**Journey ↔ UserStory**

Optional, many-to-many. Neither construct requires the other.

**UseCase ↔ UserStory**

Optional, many-to-many. A UserStory may encompass several UseCases, and a UseCase may contribute to multiple UserStories.

**Functional Requirement → Journey**

Applicable Functional Requirements may influence Journey behaviour, progression and outcomes.

**Non-Functional Requirement → Journey**

Use native ArchiMate Influence relationships where the NFR meaningfully influences a Journey.

NFRs may influence multiple Journeys, and Journeys may be influenced by multiple NFRs.

NFRs remain independently identifiable and may also constrain Capabilities, Services, Processes and other architectural elements.

Not all requirements need Journey relationships.

**Stakeholder ↔ Journey**

Represent meaningful Stakeholder participation using appropriate native ArchiMate semantics and contextual participation characteristics.

Do not invent new kernel element types or relationship types where accepted ArchiMate constructs suffice.

## Constraints

1. Preserve all accepted 1.1.20 architecture, identities and standing qualifications.
2. Preserve existing 1.1.21 initialisation and HCRL authority.
3. Do not repeat the 1.1.20 Capability Model consolidation.
4. Reconcile existing architectural identities and source contributions before introducing new elements.
5. Reuse genuinely equivalent elements; preserve meaningful distinctions.
6. Do not introduce artificial one-to-one relationships or forced traceability.
7. Do not require every Journey to identify a beneficiary or value proposition.
8. Do not require every UseCase, UserStory, Functional Requirement or NFR to associate with a Journey.
9. Do not confuse Journey architecture with executable workflows.
10. Do not introduce a Person superclass, Actor hierarchy, persona catalogue or separate Journey Stage element type.
11. Preserve architectural distinctions between Business, Information, Application, Integration, Technology and implementation concerns.
12. Use the repository's established governance, evidence, validation, publication and review mechanisms.
13. Do not silently alter accepted decisions or introduce new architectural authority.

## Authorised Activity / Scope

Perform the substantive 1.1.21 ADD operation.

Inspect and reconcile the relevant authoritative material, including existing requirements, stakeholder material, Journey and Value Stream structures, capability relationships, source contributions and the prior UseCase/UserStory assessment.

This investigation is integral to the authorised ADD operation. It is not a separate discovery engagement.

Develop and incorporate meaningful architectural instances, definitions, relationships and views under the accepted semantics.

Exercise independent architectural judgement in identifying appropriate Journeys, reusable constituent Journeys, interactions, requirements and relationships.

Derive architectural content from established evidence and accepted semantics. Do not generate speculative instances simply to achieve numerical coverage.

Prefer resolving ordinary ambiguity through available architectural evidence and sound semantic interpretation.

Escalate only genuine unresolved architectural conflicts requiring Solution Architect adjudication. Provide a preferred resolution and rationale wherever possible.

The review should be **exceptionally exceptional exception-based**. Codex should do its best to resolve ordinary modelling and reconciliation questions without requiring manual review of thousands of elements or relationships.

Do not treat this instruction as authorisation to bypass mandatory release acceptance, commit or publication controls.

## Report

Record the formal Task and authoritative inputs in the established:

`enterprise-architecture/design/evidence/1.1.21/<iteration>/before/`

Record the execution report and supporting validation evidence in:

`enterprise-architecture/design/evidence/1.1.21/<iteration>/after/`

Use the repository's actual governed subdirectories.

Do not place governed execution reports in `.junie/`.

Report:

- Architectural content incorporated.
- Stakeholder and Journey catalogue results.
- UseCase/UserStory incorporation and reconciliation.
- New and reused element identities.
- Relationship coverage and semantic integrity.
- ArchiMate projection and view results.
- Validation and assurance outcomes.
- Preserved qualifications.
- Genuine unresolved exceptions requiring Solution Architect disposition.
- Current HCRL and release state.

Distinguish completed work, qualifications and remaining work explicitly.

Do not claim architectural acceptance, human visual review or immutable publication without the corresponding evidence and authorisation.
```

## Solution Architect follow-up — native NFR relationship disposition

Follow-up-Issued-At: 2026-11-10T10:47:35.537Z

Source: same Codex session, attributable user response_item at event line 215.

Question: The governed native endpoint table allows only Association from Requirement to ValueStream. Native Influence in that direction is invalid. The preferred correction was directed Association documenting the NFR influence on Journey behaviour, preserving native Influence wherever its endpoints are valid.

Verbatim response:

```text
<send_user_message_question_reply>
[{"answer":"Use directed Association with explicit constraint meaning (recommended)","question":"One native ArchiMate conflict needs Solution Architect disposition: the governed endpoint table permits only Association from Requirement (including NFR) to ValueStream (Journey); native Influence runs toward Motivation elements. Your requested NFR → Journey Influence would fail mandatory validation. Under AGENTS.md §1.5 and the modelling strategy’s native-compatibility rule, may I represent these constraints using directed Association, documenting their influence on Journey behaviour, while retaining nativ","questionItemId":"[\"request_user_input_async\",\"call_p2VF6B1ifIIDEc6bINIMDJyR\",0]"}]
</send_user_message_question_reply>
```
