Task-Issued-At: 2026-11-24T09:06:42.557Z

Event source: Codex session 01a20cce-a661-7381-ac8c-d6a611826bd0, attributable response_item user input event for this operative instruction

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Independent Codex Naming implementation review Task

**Standing** · Operative user assignment for independent implementation and architectural review, Software 0.1.0-SNAPSHOT, `software-0.1.0/010`. Review and evidence authority only.

**Assignee** · Codex.

**Backlog** · [Item 0003 revision 000.003](../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md), SHA-256 `6c5b7bf1612105d8043e62becadb8923d66d74cbf9151130567dd030dba5947b`.

**Candidate locator** · [Implementation Report](../../after/ai-agent-reports/unresolved-naming-0003-implementation-report.md) and [exact patch](../../after/naming-0003-candidate.patch), SHA-256 bound by the issued instruction. Source commit `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`, tree `1846199b36bbfb07c87746c1367c059698b7120d`.

## Operative instruction as issued

```
# Harmonia — Independent Naming Implementation Review

**Software:** `0.1.0-SNAPSHOT`  
**Backlog:** 0003, revision 000.003  
**Iteration:** `software-0.1.0/010`  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`

## Objective

Conduct an independent engineering, object-oriented design and architectural review of Junie's completed Naming implementation.

The implementation is a candidate, not accepted software.

Review the exact candidate identified by the Implementation Report and candidate patch SHA-256:

`70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`

Use the accepted Backlog Item 0003, the qualified Naming DRP, the Harmonia Implementation Guide and the applicable architectural dispositions as review authority.

Do not treat the superseded exploratory Java implementation as authoritative.

The review must assess not only functional correctness but also **object cohesion, package containment, encapsulation, API exposure and complexity proportionality**.

A successful Maven build and passing tests are necessary evidence, not sufficient grounds for acceptance.

## 1. Candidate integrity

Verify:

- Current branch and Git state.
- Starting commit and tree.
- Candidate patch integrity.
- Actual production and test changes.
- Maven dependency scope.
- Implementation Report correspondence.
- Preservation of unrelated work.

Identify any discrepancy between the candidate reviewed and the implementation evidence.

Do not modify the implementation during this review.

## 2. Functional correctness

Independently examine conformance with AC-1 through AC-13.

Pay particular attention to:

- Topic identity, equality, hashing and ordering.
- Canonical naming and escaping.
- Version preservation and absence semantics.
- TopicMask equality and matching.
- Exact and inclusive containment.
- Explicit universal matching.
- Builder behaviour, validation and recovery.
- XML serialization and reconstruction.
- Malformed and adversarial input handling.
- Resource limits and bounded evaluation.
- Concurrency and immutable state.
- Safe diagnostic behaviour.

Examine actual test assertions, not merely test counts.

Identify missing, weak, misleading or insufficient tests.

Do not infer correctness solely from successful test execution.

## 3. Public API and Encapsulation

Perform a specific review of the externally accessible Naming API.

For every public production class, interface, enumeration, constructor and method, determine whether its visibility is justified.

Classify exposed types as:

1. Accepted public Naming contract.
2. Necessary supporting public contract.
3. Internal implementation exposed for technical reasons.
4. Unnecessarily exposed implementation detail.

Examine public method signatures for leakage of internal types.

Prefer private and package-private visibility wherever practical.

Do not assume that a public Java declaration automatically constitutes an authorised Harmonia API.

Pay particular attention to:

- Diagnostic types and enumerations.
- Parsers and validation helpers.
- Regex evaluators and safeguards.
- XML serialization collaborators.
- Internal factories and construction helpers.

Minimising the externally accessible API surface is an explicit architectural objective.

This applies to internal Harmonia interfaces as well as externally exposed interfaces.

Do not introduce elaborate access-control frameworks merely to reduce visibility.

## 4. Package Structure, Cohesion and Containment

Perform an independent review of the complete Naming package hierarchy.

The review must assess whether every production class, interface and enumeration is located in the most appropriate package based on its responsibilities, dependencies, intended consumers and visibility requirements.

**Being located somewhere under `model.core.naming` does not establish correct package containment.**

### 4.1 Package responsibilities

For each package:

1. Identify its intended responsibility.
2. Identify the classes, interfaces and enumerations it contains.
3. Assess whether those elements form a cohesive group.
4. Identify classes that logically belong in another package.
5. Identify packages whose responsibilities overlap unnecessarily.
6. Identify packages that exist solely for organisational convenience without a meaningful responsibility boundary.
7. Identify dependencies that undermine the intended package boundaries.

Pay particular attention to the existing:

- `naming`
- `naming.datatypes`
- `naming.valuesets`

And any additional subpackages introduced during implementation.

The `valuesets` package name is not itself a review defect. Assess its actual cohesion and responsibility.

Do not assume that the existing directory structure is architecturally correct merely because it compiles.

### 4.2 Encapsulation and visibility

Assess package organisation together with Java visibility.

Specifically examine:

- Public classes that could be package-private if related collaborators were colocated.
- Public constructors and methods exposed solely to cross-package collaborators.
- Internal implementation types referenced by public API signatures.
- Unnecessary dependencies between Naming subpackages.
- Package organisation that forces implementation details to become externally accessible.
- Opportunities to make internal collaborators inaccessible to downstream Harmonia modules.

Remember that Java package-private visibility does not extend to subpackages.

Where package placement forces unnecessary public exposure, determine whether restructuring would improve both cohesion and encapsulation.

Do not recommend restructuring merely to reduce the number of public declarations if it would damage otherwise appropriate responsibility boundaries.

### 4.3 Domain model containment

Verify that the six principal Naming abstractions remain discoverable and coherent:

- `Topic`
- `TopicEntry`
- `TopicBuilder`
- `TopicMask`
- `TopicMaskEntry`
- `TopicMaskBuilder`

Evaluate whether their supporting collaborators belong alongside them or within subordinate packages.

This includes:

- Canonical parsers.
- Mask-expression parsers.
- Field and mask evaluators.
- Validation and resource-limit helpers.
- XML interchange codecs.
- Diagnostic models and enumerations.

Do not prescribe arbitrary subpackages or mechanically distribute classes.

A flat package is acceptable where it improves cohesion and encapsulation.

A subordinate package is justified when it establishes a meaningful responsibility boundary.

### 4.4 Public API minimisation

For every public production type, establish whether its exposure is:

- Required by the accepted Naming API.
- Necessary for a deliberately supported consumer contract.
- Technically necessary because of package organisation.
- Unnecessary and correctable.

Also examine public methods and constructors on otherwise justified public types.

A public class may be necessary while some of its operations should remain internal.

Review whether implementation types are unnecessarily exposed through public signatures, return types, parameters or exceptions.

**Public Java visibility is not, by itself, evidence that an operation should be part of Harmonia's supported software contract.**

### 4.5 Required package-containment evidence

Produce a concise package-containment assessment containing:

1. The actual production package hierarchy.
2. Each package's intended responsibility.
3. The classes and interfaces assigned to each package.
4. Any misplaced classes or inappropriate dependencies.
5. Unnecessarily public implementation types or operations.
6. Recommended package or visibility corrections.
7. The rationale for retaining the existing structure where appropriate.

Classify material package-containment or API-exposure defects as findings requiring disposition before implementation acceptance.

Do not perform restructuring during the independent review.

## 5. Complexity Proportionality

Assess whether implementation complexity is justified by the accepted requirements.

Review:

- Class responsibilities and cohesion.
- Package organisation.
- Unnecessary abstractions.
- Duplicated validation or evaluation logic.
- Excessive indirection.
- Testability and maintainability.
- Separation between domain semantics and implementation machinery.

For each potentially unnecessary abstraction, identify the specific simpler alternative and explain why it would be preferable.

Do not penalise legitimate complexity required for correctness, security or robustness.

Do not recommend reducing safeguards merely to decrease class count.

The objective is the **smallest cohesive implementation that fully satisfies the accepted requirements**.

## 6. RE2/J and Defensive Behaviour

Verify the approved dependency and resource-limit profile.

Examine whether pre-compilation safeguards remain simple and bounded.

Confirm that the implementation does not construct a second regex parser or engine.

Review:

- Input-size limits.
- Group-depth and repetition safeguards.
- Compiled-program instruction limits.
- Unicode handling.
- Rejection of malformed expressions.
- Fail-closed matching.
- Diagnostic confidentiality.
- Concurrency safety.

Test representative adversarial and boundary cases independently where useful.

Do not silently relax accepted limits.

## 7. XML Interchange

Review the actual encoding and decoding implementation.

Verify:

- Deterministic representation.
- Lossless round trips.
- Correct handling of absent and present-empty fields.
- Strict structural and field validation.
- Bounded input.
- Safe XML processing.
- Rejection of DTD and external entity processing.
- Correct reconstruction of original regex expressions.
- No unintended exposure of internal implementation representations.

Do not assume that a successful encode/decode cycle alone establishes correctness.

Use independent expected representations and field assertions.

## 8. Verification and Test Quality

Review Junie's reported verification results:

- 36 Calliope tests passed.
- 72 tests passed across the complete 50-module reactor.
- Zero reported failures, errors or skips.

Reproduce relevant tests independently.

Examine the assertions and coverage represented by those tests.

Identify:

- Missing positive cases.
- Missing negative and boundary cases.
- Weak or misleading assertions.
- Unverified failure handling.
- Untested resource-limit behaviour.
- Missing concurrency scenarios.
- Missing serialization or reconstruction cases.
- Unnecessary dependence on implementation details.

Distinguish functional tests from structural and conformance tests.

Identify any missing integration or architecture verification required for acceptance.

The retired ArchUnit suite remains absent under the accepted qualification.

Do not reconstruct obsolete tests.

## 9. Documentation

Verify compliance with the Harmonia Implementation Guide.

Review:

- Class and interface purposes.
- Public and protected constructors and methods.
- Non-obvious implementation logic.
- Failure behaviour.
- Assumptions and limits.
- Educational documentation.
- The 500-physical-line production-class limit.

Documentation must accurately describe implemented behaviour.

Identify documentation that exposes unnecessary implementation detail as an apparent public contract.

## 10. Review Evidence and Findings

Produce the independent review findings and supporting evidence through the existing Software Development Praxis.

Classify findings by severity and required disposition.

Distinguish:

- Functional correctness defects.
- Architectural conformance defects.
- Public API and encapsulation violations.
- Package containment and cohesion defects.
- Security and robustness defects.
- Maintainability and complexity concerns.
- Test coverage deficiencies.
- Documentation defects.
- Optional improvements.

For each substantive finding, provide:

1. The affected class, package or interface.
2. The applicable requirement or engineering principle.
3. The observed problem.
4. Its consequence.
5. The recommended correction or required Solution Architect decision.

Avoid speculative findings without evidence.

Do not automatically implement corrections.

Do not introduce unrelated architecture work.

## 11. Completion

Return:

1. Candidate integrity assessment.
2. AC-1–AC-13 conformity assessment.
3. Public API and encapsulation findings.
4. Package-containment assessment.
5. Complexity proportionality assessment.
6. RE2/J and defensive behaviour assessment.
7. XML interchange assessment.
8. Test and verification findings.
9. Documentation findings.
10. Required corrections or decisions.
11. Recommendation concerning implementation acceptance.

Do not modify the implementation candidate.

Do not commit, push, merge, tag, deploy or publish.

The objective is an independent, evidence-based assessment of whether the Naming implementation is suitable to become the first accepted canonical model increment of Harmonia Software 0.1.0.

**Functional correctness, object cohesion, appropriate package containment and minimal public API exposure are all acceptance considerations.**
```
