# Harmonia Implementation Guide
## Engineering Philosophy, Defensive Programming and Educational Documentation

**Status:** Draft 001 — Solution Architect Review  
**Scope:** All Harmonia software development  
**Applicability:** Human developers, Codex, Junie and other implementation agents  
**Proposed location:** `docs/implementation/Harmonia-ImplementationGuide.md`

---

## 1. Purpose

Harmonia is, at its heart, a vehicle for learning about software architecture and software development.

While Harmonia implements a sophisticated Health Information Exchange architecture, its purpose extends beyond the delivery of functional software.

Harmonia provides a practical environment in which developers can explore, understand, implement and evaluate software engineering concepts, architectural patterns, integration mechanisms, information governance and distributed-system behaviours.

**The educational value of the implementation is an explicit engineering objective.**

Software developed for Harmonia must therefore be understandable, explainable, testable and sufficiently documented to help developers unfamiliar with the codebase learn from its implementation.

This objective complements, rather than replaces, Harmonia's functional, security, resilience and other non-functional requirements.

## 2. Core Engineering Principles

### 2.1 Learning Through Implementation

Harmonia should demonstrate how architectural concepts are realised through working software.

Developers exploring the system should be able to understand:

- What each component is responsible for.
- How a component relates to the wider architecture.
- Why significant implementation decisions were made.
- How information passes between components.
- How processing state is maintained.
- How failures are detected and handled.
- How recovery and consistency are achieved.
- How the implementation conforms to established architectural contracts.

Where useful, the implementation should demonstrate the rationale behind an engineering pattern rather than merely use that pattern.

Harmonia should remain approachable to a newcomer without unnecessarily simplifying the architecture or concealing important engineering complexity.

### 2.2 Defensive Programming

**Defensive programming is Harmonia's default software development methodology.**

The implementation must consider more than the expected successful execution of an operation.

Developers must systematically consider conditions under which an operation cannot proceed as expected.

These include, as applicable:

- Invalid, incomplete or inconsistent input.
- Missing or unavailable dependencies.
- Network interruption and timeouts.
- Partial execution or partial failure.
- Concurrent operations and race conditions.
- Duplicate, delayed or out-of-order messages.
- Stale or inconsistent information.
- Resource exhaustion.
- Interrupted execution.
- Repeated requests and retries.
- Recovery following component or platform failure.
- Authorisation failures.
- Unexpected state transitions.

Defensive programming does not mean indiscriminately catching exceptions, ignoring errors or silently introducing default values.

It means explicitly understanding operational assumptions, validating those assumptions where necessary, and providing deliberate behaviour when they are violated.

Unhappy paths are an integral part of the implementation, not incidental complications added after happy-path development.

### 2.3 The 95/5 Principle

Conventional 80/20 approaches often prioritise the most common cases while leaving less common scenarios incompletely supported.

Harmonia adopts a **95/5 engineering philosophy** for supported operational scenarios.

The objective is to provide meaningful support for a substantially broader range of legitimate execution circumstances, particularly those involving exceptional conditions and recovery.

The principle directs engineering effort toward identifying and handling realistic unhappy paths rather than concentrating almost exclusively on successful execution.

The 95/5 principle is a design and development objective. It is not an assertion that exactly 95% of every theoretically possible execution path is implemented or tested.

The remaining 5% must not be interpreted as permission to disregard applicable safety, security, information-integrity or accepted non-functional requirements.

Where a scenario is unsupported, the limitation should be deliberate, understood and appropriately documented.

**An unsupported operation must fail in a defined manner rather than produce a misleading appearance of success.**

## 3. Failure and Recovery Design

Every substantial processing component should define its expected behaviour during abnormal operation.

Where applicable, developers should consider the following progression:

1. Identify the failure or exceptional condition.
2. Determine whether the operation can safely continue.
3. Preserve information and processing state where required.
4. Determine whether retry, recovery, rejection or escalation is appropriate.
5. Ensure subsequent processing does not violate consistency or integrity requirements.
6. Make the outcome observable and diagnosable.

A failure is not adequately handled merely because an exception has been caught.

The implementation must consider the consequences of that failure for the wider system.

For example, a timeout after submitting a request does not necessarily mean the requested operation failed. It may mean the operation completed but its acknowledgement was lost.

Similarly, repeating an operation may introduce duplicates unless the relevant interaction is idempotent or otherwise protected.

These distinctions should be reflected in implementation logic and tests.

Recovery behaviour must respect Harmonia's accepted durability, acknowledgement, security and information-governance contracts.

## 4. Source Code as Educational Documentation

### 4.1 Class-Level Documentation

**Every Harmonia class must contain sufficient documentation to enable a newcomer to understand its purpose and follow its logic.**

The amount of detail should reflect the complexity and responsibility of the class.

At minimum, class documentation should explain its purpose and architectural role. Where relevant, it should also describe:

- Its responsibilities and boundaries.
- The architectural component or service to which it belongs.
- Important collaborators and dependencies.
- The information it consumes, produces or maintains.
- Significant processing flows.
- Lifecycle and state-management behaviour.
- Concurrency or threading considerations.
- Important assumptions and invariants.
- Expected failures and recovery behaviour.
- Relevant interfaces, contracts and architectural references.

The documentation should provide enough context to make the implementation intelligible without requiring the reader to reverse-engineer the entire surrounding codebase.

Small value objects and straightforward utility classes need not contain extensive narratives. Their documentation should nevertheless make their purpose clear.

### 4.2 Method-Level Documentation

Methods should be documented where their behaviour, contract or implementation is not immediately obvious.

Particular attention should be given to:

- Public service interfaces.
- Preconditions and postconditions.
- Significant state changes.
- Error and exception contracts.
- Retry and recovery behaviour.
- Concurrency-sensitive operations.
- Methods implementing non-obvious algorithms.
- Methods interacting with external systems.

Documentation must describe important behaviour rather than simply repeat the method name or restate the source code.

### 4.3 Implementation Commentary

Comments within methods should explain significant decisions, assumptions and unusual processing.

For example, comments should explain why a retry is safe, why an acknowledgement occurs at a particular point, or why an operation deliberately preserves an apparently redundant state transition.

Comments should not merely translate individual programming statements into English.

The objective is to communicate **why the implementation behaves as it does**, particularly where that behaviour is not obvious.

### 4.4 Architectural Traceability

Where a class or service implements an architecturally significant contract, its documentation should identify the relevant architectural concept or governed contract.

Developers should be able to navigate from an architectural responsibility to its implementation and, conversely, understand the architectural context of code they are inspecting.

Such references should remain useful and maintainable rather than introducing fragile dependencies on generated documentation locations.

### 4.5 Documentation Accuracy

Documentation is part of the maintained implementation.

When code changes materially alter behaviour, corresponding documentation must be reviewed and updated.

Outdated documentation that misrepresents implementation behaviour is a defect.

## 5. Testing Philosophy

Testing should reflect Harmonia's defensive programming philosophy.

Tests must not concentrate exclusively on expected successful behaviour.

For each significant operation, developers should consider:

- Successful execution.
- Input validation and rejection.
- Expected failure conditions.
- Partial failure.
- Dependency unavailability.
- Retry behaviour.
- Duplicate or repeated processing.
- Interrupted execution.
- Recovery.
- Relevant concurrency and timing conditions.

Tests should make the expected behaviour of an operation understandable.

Where possible, a newcomer should be able to read a test and understand the behaviour it is intended to establish.

Test names, setup and assertions should make the scenario and expected outcome explicit.

The 95/5 principle does not prescribe a particular numeric code-coverage threshold. Code coverage may provide useful evidence, but it is not a substitute for meaningful scenario coverage.

Testing must continue to satisfy all accepted architectural and non-functional requirements.

## 6. Observability and Diagnosability

Defensive implementation also requires sufficient information to understand what happened when processing does not proceed as expected.

Components should provide meaningful operational information using Harmonia's established logging, metrics, audit and assurance facilities.

Diagnostic information should permit developers and operators to investigate:

- The operation being performed.
- Its processing context.
- The relevant component or service.
- Significant state transitions.
- The failure or exceptional condition.
- Recovery actions and outcomes.
- Relevant correlation identifiers.

Observability must respect information-security and confidentiality requirements.

Diagnostic logging must not become an uncontrolled mechanism for exposing sensitive information.

The established distinctions between operational LogEvents, AuditEvents and Provenance remain applicable.

## 7. Engineering Clarity and Maintainability

The educational objective does not imply that implementations should be unnecessarily elaborate.

Harmonia should favour:

- Clear responsibilities and explicit contracts.
- Understandable processing flows.
- Meaningful names.
- Appropriate separation of concerns.
- Deliberate state management.
- Explicit handling of meaningful errors.
- Reuse of established frameworks where appropriate.
- Explanations of non-obvious decisions.

Excessive abstraction, unnecessary indirection and elaborate defensive mechanisms without realistic justification may make an implementation harder to understand rather than more robust.

The objective is **engineering clarity, not complexity for its own sake**.

Where a technically sophisticated mechanism is necessary, its purpose, operation and trade-offs should be explained.

## 8. Applicability Across Harmonia

These principles apply to all implementation technologies and components, including:

- Java and Jakarta EE services.
- Integration frameworks and adapters.
- Messaging and asynchronous processing.
- Persistence and information-management services.
- Vue/TypeScript user interfaces.
- Platform and infrastructure management integrations.
- Testing and automation tooling.
- Python-based services, Matrix/Synapse integrations, AI and agent-related components, and supporting automation and tooling.

Language-specific documentation conventions may differ.

For example, Java implementations will generally use Javadoc, while TypeScript implementations may use TSDoc-compatible documentation.

The underlying expectation is the same: **the code should be comprehensible, its contracts explicit, and its failure behaviour deliberate**.

## 9. AI-Assisted Development

The implementation principles apply equally to software written by human developers and AI implementation agents.

Codex, Junie and other agents must not treat successful compilation or passing happy-path tests as sufficient evidence of implementation completeness.

Implementation work should consider the applicable failure scenarios, error handling, operational visibility and documentation requirements.

Agents should produce code that can be understood and maintained by developers unfamiliar with the implementation.

The use of AI does not diminish the requirement for understandable code or appropriate architectural conformance.

These expectations complement Harmonia's existing architecture governance and implementation review procedures.

## 10. Implementation Review Expectations

Implementation reviews should consider three complementary dimensions.

### 10.1 Functional Correctness

Does the implementation satisfy the intended functional and architectural contracts?

### 10.2 Defensive Correctness

Does it behave appropriately when operations fail, are interrupted or encounter adverse conditions?

Are important unhappy paths covered by meaningful tests?

### 10.3 Educational Quality

Can a newcomer understand what the code does, why it does it, how it fits into Harmonia and what happens when things go wrong?

These dimensions are complementary. An implementation that satisfies only one or two may require further refinement.

Review effort should remain proportionate to complexity, risk and architectural significance.

## 11. Relationship to Architecture and Governance

This Implementation Guide establishes repository-wide software engineering expectations.

It does not replace:

- The Harmonia Architecture Continuum.
- Accepted architectural requirements and contracts.
- Architectural Axioms.
- Architecture governance and review processes.
- Repository agent instructions.
- Established security and information-governance obligations.

Where this guide reveals a genuine architecture or requirements gap, that gap must be addressed through the established governance process.

The guide must not be used to justify unauthorised changes to architectural responsibilities or accepted system behaviour.

## 12. Closing Principle

Harmonia is intended to be both a functioning system and an accessible body of software engineering knowledge.

Its implementation should demonstrate not only how software performs its intended work, but also how well-engineered software behaves when circumstances are less than ideal.

**Harmonia should be built so that a developer can learn from its architecture, understand its implementation, examine its failure handling, and develop the confidence to extend it.**

That expectation applies throughout the solution, not merely to selected examples or educational components.

---

**Decision:** Pending Solution Architect review and acceptance.

**Document standing:** Proposed repository-wide Implementation Guide. No repository changes have been made.