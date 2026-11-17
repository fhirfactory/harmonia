<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia — Software Implementation Task Details

- **Task Identifier:** `Task-Calliope-Model-Engineering-002`
- **Task-Issued-At:** `2026-11-17T09:22:56Z`
- **Task-Issued-At source:** The timestamp recorded for the operative issue in the user-supplied execution plan and consistent with the issue's supplied local date/time; it denotes instruction entry, not earlier composition.
- **Implementation Agent:** `Junie`
- **Target Software Release:** `0.1.0`
- **Accepted Architecture Baseline:** `1.1.26` (`architecture-release.json`)
- **Branch:** `feature/BL-CALLIOPE-OO-01`
- **Starting Git HEAD:** `cd492cf1c8de3ea649aece412d1826da3ee6f6d1`
- **Starting worktree:** The prior Calliope candidate was already present as related tracked changes when this task began; the worktree was not clean.
- **Associated HCRL:** [`hcrl-software-0.1.0-001.pending.md`](../../../../../review/software-0.1.0/hcrl-software-0.1.0-001.pending.md)
- **Accepted Backlog Item:** [`BL-CALLIOPE-OO-01`, revision 1](../../../../../../../docs/backlog/calliope-oo-01-topic-models.md); its historical acceptance and original narrow specification remain unchanged.
- **Assignment scope source:** [`BacklogItem-0001-Calliope-Model-Restructure.001.md`](../../../../../../../docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md), header ID `00001`, revision `001`.
- **Scope authority:** The Solution Architect's explicit disposition below accepts the whole-Calliope content of the assignment scope source for this assignment only. This does not revise or replace the accepted standing of `BL-CALLIOPE-OO-01` revision 1, nor accept the software candidate.
- **Related prior Task:** [`Task-Calliope-Model-Restructure-001`](20261116T055100Z-calliope-model-restructure-task.md).
- **Related prior Report:** [`Report-Calliope-Model-Restructure-001`](../../after/ai-agent-reports/20261116T083000Z-calliope-model-restructure-report.md); historical results are not treated as proof of this candidate's engineering quality.

---

## 1. Operative Assignment

The following is the operative assignment issued in this interaction. The applicable repository instructions and governance contracts remain in force.

### Issue Task

**Model:** Claude Opus 5.5 — High Effort
**Branch:** `feature/BL-CALLIOPE-OO-01`
**Backlog Item:** BL-CALLIOPE-OO-01
**Target Software Release:** 0.1.0

#### Goal

Complete the engineering implementation of the Calliope Model Restructure backlog item. The existing implementation was produced by an earlier agent. Although it compiles and reportedly passes its tests, the Solution Architect's initial code review identified concerns about object-oriented design quality, encapsulation, construction mechanisms and package organisation. Review, correct and complete the implementation to professional Java software engineering standards; this is an implementation assignment, not a design proposal.

#### Expected Outcome

A complete, cohesive, maintainable and properly tested Calliope Java model that implements the accepted Design Note, satisfies the governing backlog scope and acceptance criteria, conforms to the Harmonia Implementation Guide, preserves accepted architectural semantics and information contracts, demonstrates sound object-oriented engineering throughout Calliope, and integrates with affected Harmonia modules.

#### Context and Review Direction

Read and apply the current repository versions of:

1. `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`.
2. The Calliope Model Restructure backlog scope source at `docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md`, as accepted for this assignment by the disposition in §2 below.
3. `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`.
4. Applicable repository `AGENTS.md` instructions.
5. The Software Development Praxis.
6. Applicable accepted Architecture Continuum elements.

Use the Architecture Continuum as the authoritative reference for architectural meaning. Do not process the entire RAD unnecessarily. The earlier Implementation Report and HCRL are historical evidence, not proof of engineering quality.

Review object responsibilities and construction patterns, package organisation below the functional hierarchy, information metadata and operational-context handling, Jackson and Java serialization, binary and by-reference round-tripping, builder preservation, duplicate JSON properties, diagnostic PHI exposure, and whole-model engineering quality. This defect list is not exhaustive.

#### Constraints and Authority

The Solution Architect authorizes ordinary software-engineering choices within the whole-Calliope assignment scope. Package/class restructuring, removal of obsolete interfaces, construction changes, consumer migrations, tests and documentation are permitted. Preserve accepted architectural meaning and genuine persistence, interchange, identity and external-integration contracts. Material architectural conflicts must be escalated rather than silently resolved. Do not modify `mainline` or `master`, merge, declare a release, or claim Solution Architect acceptance.

#### Implementation and Verification

Apply the Harmonia Implementation Guide in full: cohesive encapsulation, appropriate collaborators, no redundant factories/builders/static facades, educational documentation, defensive programming and meaningful unhappy-path behaviour. Test normal, invalid, boundary and failure scenarios, including serialization round trips and consumer regressions. Run appropriate component, architecture/conformance, integration/regression and full Maven reactor compilation checks with bounded waits; investigate implementation-attributable failures and explicitly report unavailable or incomplete verification.

#### Commit and Candidate Submission

Complete the whole-of-Calliope work in the authorized scope, test and self-review it, commit the implementation and relevant tests/documentation on `feature/BL-CALLIOPE-OO-01`, and verify the resulting working tree. Prepare an Implementation Report with an **Implementation Guide Conformance** section addressing functional correctness and tests, failure paths, educational documentation, architectural/security conformity, and known limitations or untested scenarios, all supported by concrete evidence. Update the existing software HCRL to identify the committed candidate and request Solution Architect code review. Do not claim acceptance or merge to `mainline`.

### User-Directed Execution Plan

The user directed that the supplied implementation plan be followed: preserve the prior candidate, correct information model and collaborating-object design, migrate related Calliope consumers, add focused tests, execute the listed solution-wide checks, and submit the exact committed candidate with Task/Report/HCRL traceability. The plan is execution direction, not independent architectural authority.

---

## 2. Solution Architect Disposition — Assignment Scope

The following disposition was supplied as a later explicit follow-up in the current interaction. The interface did not expose a distinct authoritative event timestamp for this follow-up; its event time is therefore **UNRESOLVED** and has not been inferred from a later clock reading.

> **Solution Architect Disposition — BL-CALLIOPE-OO-01**
>
> I explicitly authorise the whole-of-Calliope implementation scope defined in:
>
> `docs/backlog/BacklogItem-0001-Calliope-Model-Restructure.001.md`
>
> This authorisation supersedes the narrower scope of `BL-CALLIOPE-OO-01` revision 1 **for this implementation assignment only**. The historical acceptance of revision 1 remains unchanged.
>
> The broader backlog document is accepted as the governing implementation scope for the current assignment, notwithstanding its existing proposed status and different identifier.
>
> Record this explicit Solution Architect disposition and reconcile the documentation identifiers accordingly, preserving historical traceability.
>
> Proceed with the full Calliope engineering work under the accepted Design Note, Implementation Guide and Architecture Continuum.
>
> Complete the implementation, verify it, commit to `feature/BL-CALLIOPE-OO-01`, and submit the committed candidate for Solution Architect code review.
>
> Do not merge into `mainline`.

This disposition authorizes implementation scope only. It does not change Continuum elements, allocate a release, accept the implementation, authorize merge/push/tag/deployment, or alter the historical acceptance of `BL-CALLIOPE-OO-01` revision 1.

---

## 3. Governing References and Boundaries

- Architectural authority: `enterprise-architecture/governance/architectural-axioms.md` and the specifically applicable current Continuum elements, including Calliope-Models, Information Unit Metadata, InformationRepresentation, Topic and TopicMask.
- Common governance: `enterprise-architecture/governance/architecture-governance.md`, `architecture-review-praxis.md`, and `architecture-change-provenance.md`.
- Software-specific governance: `enterprise-architecture/governance/software-development-praxis.md`.
- Design hierarchy: `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`.
- Implementation standards: `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md` and repository `AGENTS.md`.
- Preserve as an unresolved design boundary, unless an applicable authority is found: the Task plan's identified `PragmaFhirSecurityConverter` construction of security context from Task extensions. Do not independently redefine accepted Themis security-context authority.
- Preserve the canonical Content distinctions: one InformationRepresentation per IU; distinct ContainerMetadata, ContentMetadata and Content; textual/binary/by-reference content; and distinct IU authoritative, Mneme operational, Content Structure, and optional native Content versions.
- Do not equate physical persistence operations with managed-information lifecycle semantics or introduce new runtime boundaries.

---

## 4. Assignment Summary

The following is a navigation summary. The complete original assignment and the user-provided execution plan are reproduced in §§6–7; the backlog scope source remains the work specification.

```text
# Junie Assignment — Calliope Model Engineering

**Model:** Claude Opus 5.5 — High Effort
**Branch:** `feature/BL-CALLIOPE-OO-01`
**Backlog Item:** BL-CALLIOPE-OO-01
**Target Software Release:** 0.1.0

## Goal

Complete the engineering implementation of the Calliope Model Restructure backlog item.

The existing implementation was produced by an earlier agent. Although it compiles and reportedly passes its tests, the Solution Architect's initial code review identified concerns about object-oriented design quality, encapsulation, construction mechanisms and package organisation.

You are responsible for reviewing, correcting and completing the implementation to professional Java software engineering standards.

This is an implementation assignment, not a request for another design proposal.

## Expected Outcome

A complete, cohesive, maintainable and properly tested Calliope Java model that:

- Implements the accepted Design Note.
- Satisfies the Backlog Item and its acceptance criteria.
- Conforms to the Harmonia Implementation Guide.
- Preserves accepted architectural semantics and information contracts.
- Demonstrates sound object-oriented engineering throughout Calliope.
- Integrates correctly with affected Harmonia modules.

## Context

Read and apply the current repository versions of:

1. `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`
2. The Calliope Model Restructure Backlog Item under `docs/backlog/`.
3. `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`
4. Applicable repository `AGENTS.md` instructions.
5. The Software Development Praxis.
6. The accepted Architecture Continuum under `enterprise-architecture/continuum/`.

Use the Architecture Continuum as the authoritative reference for architectural meaning.

Do not load or process the entire RAD documentation unnecessarily. Inspect relevant architectural elements as required.

The existing Implementation Report and HCRL are useful historical evidence of the previous implementation, not proof that its engineering quality is acceptable.

## Previous Code Review Observations

The Solution Architect and ChatGPT identified several concerns in the existing implementation.

These are observations to investigate, not prescribed solutions.

### Object-oriented design

The current `InformationUnit` implementation exposes numerous delegated accessors, multiple construction mechanisms and dependencies on its own factory.

Assess whether these responsibilities belong in the object itself or in appropriately designed collaborating objects.

In particular, examine the use of static construction methods on domain objects that delegate to separate factory classes.

### Package organisation

The current implementation appears to have established the upper-level functional hierarchy without consistently applying cohesive object-oriented organisation beneath it.

Assess the complete package structure, including constituent data types, value sets, factories, builders and supporting objects.

### Information model correctness

Previous investigation identified potential or demonstrated problems involving:

- `InformationMetadata` and `InformationUnitMetadata`.
- Management and operational context handling.
- Jackson serialization/deserialization.
- Binary and by-reference content round-tripping.
- Builder behaviour and preservation of information.
- Duplicate or inconsistent JSON properties.
- Potential PHI exposure through diagnostic methods.

Determine the actual defects and correct them consistently with the accepted architecture.

### Engineering quality

Several classes appear to have been reduced to just below the 500-line limit without necessarily achieving appropriate responsibility decomposition.

Assess the quality of the object model, not merely numerical compliance.

These observations are not an exhaustive defect list.

Exercise independent engineering judgement across the whole Calliope subproject.

## Constraints

You have full engineering authority within the scope of this backlog item and its feature branch.

You may:

- Restructure packages and classes.
- Remove obsolete or redundant interfaces.
- Replace inappropriate construction mechanisms.
- Introduce justified collaborating objects.
- Refactor existing implementations.
- Update affected consumers.
- Add or revise tests.
- Commit changes incrementally.

You do not need Solution Architect approval for ordinary software engineering decisions within the accepted backlog scope.

However:

- Do not silently change accepted architectural meaning.
- Preserve genuine persistence, interchange, identity and external integration contracts.
- Escalate material architectural conflicts requiring Solution Architect decisions.
- Do not modify `mainline` or `master`.
- Do not merge or declare a release.

The feature branch is your development sandbox.

## Implementation Requirements

Apply the Harmonia Implementation Guide in full.

In particular:

- Use cohesive object-oriented design and meaningful encapsulation.
- Separate responsibilities appropriately.
- Prefer understandable collaborating objects over monolithic classes.
- Avoid redundant factories, builders, static convenience interfaces and unnecessary compatibility façades.
- Provide educational class and method documentation.
- Apply defensive programming and the 95/5 philosophy.
- Test meaningful normal, invalid, boundary and failure scenarios.
- Preserve architectural and security invariants.

Do not mistake passing tests, file-length compliance or package-name conformity for complete engineering quality.

## Engineering Execution

You are responsible for determining the appropriate engineering approach.

Inspect the existing implementation, identify genuine deficiencies, correct them and verify the result.

You may work incrementally and commit intermediate changes to the feature branch.

Do not abandon the assignment after addressing only the initially identified examples.

Do not repeatedly restart the assignment or discard valid work without a concrete engineering reason.

Complete the whole-of-Calliope restructuring required by the backlog item.

## Verification

Execute the appropriate:

- Unit tests.
- Negative and boundary-condition tests.
- Serialization/deserialization round-trip tests.
- Consumer regression tests.
- Architectural conformance tests.
- Relevant integration tests.
- Full Maven reactor compilation.

Investigate and resolve failures attributable to the implementation.

Explicitly identify any verification that cannot be completed.

## Commit and Code Review

Once you are satisfied that the implementation meets the Backlog Item, Design Note, Implementation Guide and applicable Architecture Continuum requirements:

1. Confirm the feature branch contains the completed implementation.
2. Commit all relevant implementation, test and documentation changes.
3. Verify the working tree is clean.
4. Prepare a concise Implementation Report describing the engineering outcome, significant decisions and verification evidence.
5. Update the existing HCRL to identify the committed candidate and request Solution Architect code review.

The Implementation Report must include an Implementation Guide Conformance section covering functional correctness and tests, unhappy/failure-path treatment, educational documentation, architectural/security conformity, and known limitations or untested scenarios, supported by concrete evidence.

Do not claim Solution Architect acceptance.

Do not merge into `mainline`.

The Solution Architect will review the committed code and determine whether it is suitable for promotion.

## Final Instruction

You are the software engineer responsible for completing this backlog item.

The Architecture Team has already established the architectural direction, expected outcome and engineering principles.

We expect you to exercise independent professional engineering judgement.

Engineer the solution. Test it. Review your own work. Commit it. Submit it for code review.
```

---

## 5. User-Directed Plan Instruction

```text
### IMPLEMENT FROM CREATE_PLAN
The plan above is the source of truth for this task.

DO:
- Execute the plan step by step (touch points → approach → tests).
- Reuse plan facts; open/search code only for a concrete gap or failed assumption.
- If the plan lists created artifacts (repro/scripts), reuse those paths — do not recreate.
- On a real blocker (missing symbol, red expected test, conflict with guidelines/issue): fix locally or do a targeted lookup. Stay on the same plan.

DO NOT:
- Re-map / broadly re-explore the repo "from scratch".
- Invent a competing private plan that replaces the user's plan.
- Call create_plan again for this task.
- Deviate from the plan without a concrete blocker.

Next action: start implementing from the plan (edits/tests as it says).
```

The detailed plan was supplied in the task interaction, not as a repository file. Its code touch points, implementation sequence, test scope and noted architectural boundaries are incorporated as execution direction by this Task. The plan does not supersede the accepted Continuum, repository governance, or the explicit Solution Architect scope disposition in §2.

---

## 6. Complete Original Assignment (issued wording)

```text
# Junie Assignment — Calliope Model Engineering

**Model:** Claude Opus 5.5 — High Effort
**Branch:** `feature/BL-CALLIOPE-OO-01`
**Backlog Item:** BL-CALLIOPE-OO-01
**Target Software Release:** 0.1.0

## Goal

Complete the engineering implementation of the Calliope Model Restructure backlog item.

The existing implementation was produced by an earlier agent. Although it compiles and reportedly passes its tests, the Solution Architect's initial code review identified concerns about object-oriented design quality, encapsulation, construction mechanisms and package organisation.

You are responsible for reviewing, correcting and completing the implementation to professional Java software engineering standards.

This is an implementation assignment, not a request for another design proposal.

## Expected Outcome

A complete, cohesive, maintainable and properly tested Calliope Java model that:

- Implements the accepted Design Note.
- Satisfies the Backlog Item and its acceptance criteria.
- Conforms to the Harmonia Implementation Guide.
- Preserves accepted architectural semantics and information contracts.
- Demonstrates sound object-oriented engineering throughout Calliope.
- Integrates correctly with affected Harmonia modules.

## Context

Read and apply the current repository versions of:

1. `docs/design/DesignNote-Calliope-Initial-Model-Architecture.md`
2. The Calliope Model Restructure Backlog Item under `docs/backlog/`.
3. `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`
4. Applicable repository `AGENTS.md` instructions.
5. The Software Development Praxis.
6. The accepted Architecture Continuum under `enterprise-architecture/continuum/`.

Use the Architecture Continuum as the authoritative reference for architectural meaning.

Do not load or process the entire RAD documentation unnecessarily. Inspect relevant architectural elements as required.

The existing Implementation Report and HCRL are useful historical evidence of the previous implementation, not proof that its engineering quality is acceptable.

## Previous Code Review Observations

The Solution Architect and ChatGPT identified several concerns in the existing implementation.

These are observations to investigate, not prescribed solutions.

### Object-oriented design

The current `InformationUnit` implementation exposes numerous delegated accessors, multiple construction mechanisms and dependencies on its own factory.

Assess whether these responsibilities belong in the object itself or in appropriately designed collaborating objects.

In particular, examine the use of static construction methods on domain objects that delegate to separate factory classes.

### Package organisation

The current implementation appears to have established the upper-level functional hierarchy without consistently applying cohesive object-oriented organisation beneath it.

Assess the complete package structure, including constituent data types, value sets, factories, builders and supporting objects.

### Information model correctness

Previous investigation identified potential or demonstrated problems involving:

- `InformationMetadata` and `InformationUnitMetadata`.
- Management and operational context handling.
- Jackson serialization/deserialization.
- Binary and by-reference content round-tripping.
- Builder behaviour and preservation of information.
- Duplicate or inconsistent JSON properties.
- Potential PHI exposure through diagnostic methods.

Determine the actual defects and correct them consistently with the accepted architecture.

### Engineering quality

Several classes appear to have been reduced to just below the 500-line limit without necessarily achieving appropriate responsibility decomposition.

Assess the quality of the object model, not merely numerical compliance.

These observations are not an exhaustive defect list.

Exercise independent engineering judgement across the whole Calliope subproject.

## Constraints

You have full engineering authority within the scope of this backlog item and its feature branch.

You may:

- Restructure packages and classes.
- Remove obsolete or redundant interfaces.
- Replace inappropriate construction mechanisms.
- Introduce justified collaborating objects.
- Refactor existing implementations.
- Update affected consumers.
- Add or revise tests.
- Commit changes incrementally.

You do not need Solution Architect approval for ordinary software engineering decisions within the accepted backlog scope.

However:

- Do not silently change accepted architectural meaning.
- Preserve genuine persistence, interchange, identity and external integration contracts.
- Escalate material architectural conflicts requiring Solution Architect decisions.
- Do not modify `mainline` or `master`.
- Do not merge or declare a release.

The feature branch is your development sandbox.

## Implementation Requirements

Apply the Harmonia Implementation Guide in full.

In particular:

- Use cohesive object-oriented design and meaningful encapsulation.
- Separate responsibilities appropriately.
- Prefer understandable collaborating objects over monolithic classes.
- Avoid redundant factories, builders, static convenience interfaces and unnecessary compatibility façades.
- Provide educational class and method documentation.
- Apply defensive programming and the 95/5 philosophy.
- Test meaningful normal, invalid, boundary and failure scenarios.
- Preserve architectural and security invariants.

Do not mistake passing tests, file-length compliance or package-name conformity for complete engineering quality.

## Engineering Execution

You are responsible for determining the appropriate engineering approach.

Inspect the existing implementation, identify genuine deficiencies, correct them and verify the result.

You may work incrementally and commit intermediate changes to the feature branch.

Do not abandon the assignment after addressing only the initially identified examples.

Do not repeatedly restart the assignment or discard valid work without a concrete engineering reason.

Complete the whole-of-Calliope restructuring required by the backlog item.

## Verification

Execute the appropriate:

- Unit tests.
- Negative and boundary-condition tests.
- Serialization/deserialization round-trip tests.
- Consumer regression tests.
- Architectural conformance tests.
- Relevant integration tests.
- Full Maven reactor compilation.

Investigate and resolve failures attributable to the implementation.

Explicitly identify any verification that cannot be completed.

## Commit and Code Review

Once you are satisfied that the implementation meets the Backlog Item, Design Note, Implementation Guide and applicable Architecture Continuum requirements:

1. Confirm the feature branch contains the completed implementation.
2. Commit all relevant implementation, test and documentation changes.
3. Verify the working tree is clean.
4. Prepare a concise Implementation Report describing the engineering outcome, significant decisions and verification evidence.
5. Update the existing HCRL to identify the committed candidate and request Solution Architect code review.

The Implementation Report must include an Implementation Guide Conformance section covering functional correctness and tests, unhappy/failure-path treatment, educational documentation, architectural/security conformity, and known limitations or untested scenarios, supported by concrete evidence.

Do not claim Solution Architect acceptance.

Do not merge into `mainline`.

The Solution Architect will review the committed code and determine whether it is suitable for promotion.

## Final Instruction

You are the software engineer responsible for completing this backlog item.

The Architecture Team has already established the architectural direction, expected outcome and engineering principles.

We expect you to exercise independent professional engineering judgement.

Engineer the solution. Test it. Review your own work. Commit it. Submit it for code review.
```

---

## 7. User-Directed Execution Plan (complete plan content)

```text
## Goal
Bring the uncommitted Calliope candidate (HEAD cd492cf1 plus 312 working-tree changes) up to sound OO quality, consistent with the Design Note, Backlog Item 0001, the Implementation Guide and the Continuum. Fix the real defects, migrate consumers, verify across the solution, commit on feature/BL-CALLIOPE-OO-01, and amend HCRL software-0.1.0-001 with a new Task and Report.

## Scope
- In: the whole Calliope model, consumer migration (agora, pylai, energeia, hestia, iris, paradeigma), tests, ArchUnit references, Task/Report/HCRL.
- Out: changing Pragma's mutable-bean interchange contract (JSON aliases); redesigning PragmaFhirSecurityConverter (escalate under AX-17); new capabilities; merging to mainline.

## Touch points (verified)
- information/entity: InformationUnit, InformationMetadata, InformationUnitMetadata, InformationRepresentation, Content, ContentMetadata, ContainerMetadata, InformationCoordinates, InformationItinerary, InformationSecurityContext, InformationUnitRelationship, LifecycleContext, InformationUnitIdentifier, InformationUnitMask; entity/factory: InformationUnitBuilder, InformationUnitFactory, InformationUnitValidator, ContainerMetadataBuilder, OperationalMetadataBuilder.
  - Only Calliope tests use these, so they can be rewritten freely.
- information/operations: ManagementContext. Move OperationalMetadata, OperationalMetadataMask, OperationalRevision and OperationalMetadataBuilder here, since they are peer operational concerns, not IU constituents.
- naming: Topic and TopicMask static delegates and nested compatibility subclasses (Entry, Builder, EntryPattern). Migrate Topic.fromName (148 uses in 22 files) and other callers to TopicFactory/TopicBuilder.
- workflow/tasks: Pragma (remove static newDraft/newRequested/builder and the nested Builder) and ErgonPayload (remove static from* that duplicate ErgonPayloadFactory or ErgonPayloadFhirConverter). Organise into valuesets (PragmaStatus, ErgonReasonEnum, ErgonPayloadConstants), datatypes (PragmaCheckpoint) and factory (PragmaBuilder, ErgonPayloadFactory).
- workflow/orchestration: remove PraxisDefinition.builder if it is a delegate; move PraxisDefinitionBuilder to factory.
- security: move the enums into valuesets. Make ErgonSecurityDefinition permits* default-deny on null or empty (Invariant 6; only tests call it).
- domain/fhir: move ProviderRegistryConstants and PragmaFhirConstants into valuesets.
- ArchUnit tests in paradeigma-test/arch that reference model.core.information.access/persistence, domain.fhir and security.FhirSecurityTagManager need updating if those classes move.

## Approach
1. Information model redesign:
   - Single InformationMetadata (coordinates, itinerary, origin, security, lifecycle, relationships); delete InformationUnitMetadata, InformationUnitFactory and InformationUnitValidator.
   - InformationUnit holds identifier, version, metadata, representation, plus an optional ManagementContext as an embedded facade of peer operational metadata (Continuum c05b7e37: embedded views remain valid; no ManagedIU or superclass).
   - One JSON creator constructor; no static create/random/builder; delegate accessors removed; validation in the constructor.
   - Content becomes a typed, mode-tagged JSON value (text/binary/reference). WORKING mode fails closed on JSON and Java serialisation.
   - InformationRepresentation takes a Content.
   - ContainerMetadata and ContentMetadata become Topic-only, without duplicate String/`type` properties.
   - `isEmpty`/derived accessors get `@JsonIgnore`.
   - ManagementContext is typed OperationalRevision only, with no AuthoritativeVersion conflation.
   - Relationship constructors are typed; the Object constructor goes.
   - Value-set constants move to `entity/valuesets`; constituents go to `entity/datatypes`; builders to `entity/factory`.
   - toString methods emit no PHI (counts and identifiers only).
2. OperationalMetadata composes ManagementContext plus typed fields, with no Map<String,Object>, so it round-trips.
3. Naming, workflow, security and domain clean-up, using sed-driven import migration across modules.
4. Rewrite and extend the Calliope tests: JSON round-trip for text, binary, reference and summary content and ManagementContext; duplicate-property absence; PHI-free toString; invalid input; default-deny.
5. Run the full reactor test-compile, then module tests (calliope, agora, pylai-mllp-in/out, energeia, hestia, iris-befe), the ArchUnit suite and the full `mvn test` (bounded).
6. Write the Task record (issued 2026-11-17T09:22:56Z) and the Report, amend the HCRL (also fixing its backlog link), commit and verify the tree is clean.

## Tests
- Existing: calliope (234 pass at baseline), arch suite `*ArchitectureTest`, and the module suites listed in AGENTS.md §5.
- Add: InformationUnit serialisation round-trip and negative cases, Content mode tests, ManagementContext and OperationalMetadata JSON, toString PHI tests, ErgonSecurityDefinition deny tests.

## Corner cases & risks
- Pragma and ErgonPayload JSON is an inter-module interchange or persistence contract, so keep its property names and aliases.
- The Topic JSON shape is used widely, so don't change it.
- PragmaFhirSecurityConverter rebuilds the security context from Task extensions (Invariant 6.2 conflict). Report it for SA disposition rather than redesign it.
- PersistenceOperationType.DELETE versus the no-physical-DELETE rule: report it.
- The LifecycleContext handshake fields overlap OperationalMetadata; keep them as the facade the Continuum requires and report the overlap.
- Builds must use JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 and `mvn -o`.
```

The plan text above is preserved as execution direction, not independent architectural authority. Actual repository authority and the explicit Solution Architect scope disposition in §2 govern any conflict; actual outcomes are recorded in the corresponding Implementation Report and HCRL.
