# Harmonia Implementation Guide
## Engineering Philosophy, Defensive Programming and Educational Documentation

**Status:** Draft 001 — Solution Architect Review  
**Scope:** All Harmonia software development  
**Applicability:** Human developers, Codex, Junie and other implementation agents  
**Proposed location:** `docs/implementation/Harmonia-Implementation-Guide-Philosophy.md`

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

All public and protected methods and constructors MUST be documented. Private methods MUST also be documented where their purpose, semantics, constraints or implementation decisions are not immediately obvious. No method is exempt from the requirement for understandable and appropriately documented code merely because its implementation appears straightforward.

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

## 7. Object-Oriented Engineering and Code Organisation

### 7.1 Object-Oriented Design Is Fundamental

**Object-oriented design is the fundamental software engineering methodology for Harmonia Java development.**

Java implementations MUST be structured around cohesive objects with identifiable responsibilities, well-defined contracts and meaningful encapsulation.

Classes must represent coherent domain concepts, information structures, behaviours or supporting responsibilities. Collaboration between objects must be preferred over concentrating unrelated functionality within large, multifunctional classes.

The existence of an established class or package structure does not establish that the structure represents good object-oriented design.

Implementation agents MUST critically assess existing structures against the accepted architecture and these engineering principles rather than reproduce historical arrangements by default.

Object-oriented design is not synonymous with inheritance. Composition, delegation and appropriately encapsulated collaboration should be preferred where inheritance does not represent genuine shared structure or behaviour.

### 7.2 Java Package Organisation

**A Java package represents a cohesive collection of objects that collectively provide identifiable value.**

Package organisation MUST reflect the logical organisation of Harmonia's information and software contracts, rather than incidental implementation history, individual development tasks or the identity of consuming components.

Calliope is Harmonia's shared core data and information model. Its packages should therefore be organised around cohesive information concepts and their supporting objects.

For example:

```text
net.fhirfactory.harmonia.model.core.naming
    Topic
    TopicMask

net.fhirfactory.harmonia.model.core.information
    InformationUnit
    InformationUnitMask

net.fhirfactory.harmonia.model.core.messaging
    Petasos...
```

These names illustrate the intended organisational methodology; they do not independently authorise changes to the accepted Architecture Continuum.

Within a cohesive package, the following supporting subpackages SHOULD be used where applicable:

- **`datatypes`** — lower-level supporting data structures, constituent objects, metadata and associated types.
- **`valuesets`** — enumerations, constants and defined sets of values.
- **`factory`** — object construction, assembly, conversion and related factory responsibilities where these warrant separate encapsulation.
- **`base`** — shared base classes or abstractions, **if and only if** multiple classes within the relevant scope exhibit genuine commonality that justifies such an abstraction.

These subpackages MUST NOT be created merely to satisfy a template. Their existence must be justified by the objects and responsibilities they contain.

Primary domain objects should remain readily discoverable at the root of their logical package. Supporting implementation detail should not obscure the principal model.

### 7.3 Separation of Information Models, Interfaces and Implementations

Information models, logical interfaces and component implementations represent different engineering concerns.

Calliope MUST NOT be organised as a replication of Harmonia's implementation-component hierarchy.

An information object used by Ponos, Petasos, Mneme or another Harmonia component does not automatically belong in a package named after that consuming component.

Shared information objects should be located according to the information concepts they represent.

Interfaces SHOULD be grouped according to their logical contractual responsibility, independently of the implementation that happens to fulfil the contract.

For example:

```text
net.fhirfactory.harmonia.audit.interfaces.AuditSatelliteAdapter
```

The logical ownership of the contract determines its placement; the implementation technology or consuming component does not.

An interface's package location must not, by itself, be interpreted as assigning its implementation responsibility.

### 7.4 Class Size and Responsibility

**A Java source file longer than 500 lines is an engineering antipattern.**

This threshold applies to production Java source files, including documentation and supporting declarations.

A source file approaching this threshold SHOULD prompt examination of its responsibilities and opportunities for improved object-oriented decomposition.

A source file exceeding 500 lines MUST be treated as an implementation-review exception. It MUST be decomposed into appropriately encapsulated collaborating classes unless the Solution Architect explicitly accepts a justified exception.

Appropriate decomposition may include:

- Extraction of constituent data structures or metadata objects.
- Separation of factory and construction responsibilities.
- Extraction of conversion, mapping or translation responsibilities.
- Delegation of independently meaningful algorithms or operations.
- Identification of genuine shared abstractions where multiple classes exhibit commonality.

Decomposition MUST preserve the intended semantic responsibilities, object invariants and architectural contracts.

Artificial fragmentation, unnecessary inheritance and collections of unrelated static helper methods are not acceptable substitutes for object-oriented design.

**Passing unit tests does not establish that an oversized or poorly encapsulated class is appropriately designed.**

### 7.5 Source Code Documentation Is Mandatory

**NO CODE IS SELF-DOCUMENTING.**

Meaningful identifiers, clear control flow and well-structured classes improve code readability, but they do not replace documentation.

Every production Java class and interface MUST contain documentation explaining its purpose, responsibilities and intended architectural role.

Public and protected operations MUST be documented sufficiently to establish their contracts, including applicable parameters, return values, preconditions, exceptional outcomes and side effects.

Implementation commentary MUST explain significant decisions, assumptions, invariants and non-obvious processing behaviour.

Documentation should explain **why** an implementation behaves as it does, not merely restate the Java statements.

Where applicable, documentation MUST identify relevant architectural concepts and accepted design contracts.

Documentation is a maintained part of the implementation. Changes to behaviour, contracts or architectural responsibilities MUST include corresponding documentation review and updates.

The presence of Javadoc alone does not establish documentation adequacy. Documentation must be meaningful, accurate and useful to developers seeking to understand or maintain the software.

### 7.6 Engineering Agent Responsibilities

These requirements apply equally to human developers and automated implementation agents.

Before implementing or materially modifying Java code, an agent MUST:

1. Identify the relevant architectural concepts, their logical ownership and their relationships.
2. Determine an appropriate object-oriented decomposition and package organisation.
3. Reconcile existing implementation structures against the accepted architectural model.
4. Identify responsibilities that warrant supporting data types, value sets, factories or shared abstractions.
5. Ensure that the proposed design does not introduce unnecessarily oversized classes.
6. Establish the documentation required to explain the resulting implementation.

Where the accepted architecture and existing code disagree, the agent MUST identify and report the discrepancy.

An agent MUST NOT silently resolve such a discrepancy by treating existing code as architectural authority.

Where an alternative design may improve the architecture, the agent MAY propose it, but MUST distinguish the proposal from the currently accepted design and MUST NOT implement an architectural change without the necessary authorisation.

Implementation completion reports MUST identify any unresolved violations or explicitly accepted exceptions to these principles.

**Architectural fidelity, object-oriented design quality, documentation and functional correctness are all required outcomes of implementation.**

## 8. Engineering Clarity and Maintainability

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

Engineering clarity includes the quality of object-oriented decomposition, package cohesion, encapsulation and documented design intent. Functional correctness and successful automated tests do not excuse violations of the object-oriented engineering principles defined in §7.

## 9. Applicability Across Harmonia

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

## 10. AI-Assisted Development

The implementation principles apply equally to software written by human developers and AI implementation agents.

Codex, Junie and other agents must not treat successful compilation or passing happy-path tests as sufficient evidence of implementation completeness.

Implementation work should consider the applicable failure scenarios, error handling, operational visibility and documentation requirements.

Agents should produce code that can be understood and maintained by developers unfamiliar with the implementation.

The use of AI does not diminish the requirement for understandable code or appropriate architectural conformance.

These expectations complement Harmonia's existing architecture governance and implementation review procedures.

## 11. Implementation Review Expectations

Implementation reviews should consider three complementary dimensions.

### 11.1 Functional Correctness

Does the implementation satisfy the intended functional and architectural contracts?

### 11.2 Defensive Correctness

Does it behave appropriately when operations fail, are interrupted or encounter adverse conditions?

Are important unhappy paths covered by meaningful tests?

### 11.3 Educational Quality

Can a newcomer understand what the code does, why it does it, how it fits into Harmonia and what happens when things go wrong?

These dimensions are complementary. An implementation that satisfies only one or two may require further refinement.

Review effort should remain proportionate to complexity, risk and architectural significance.

## 12. Relationship to Architecture and Governance

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

## 13. Closing Principle

Harmonia is intended to be both a functioning system and an accessible body of software engineering knowledge.

Its implementation should demonstrate not only how software performs its intended work, but also how well-engineered software behaves when circumstances are less than ideal.

**Harmonia should be built so that a developer can learn from its architecture, understand its implementation, examine its failure handling, and develop the confidence to extend it.**

That expectation applies throughout the solution, not merely to selected examples or educational components.

---

**Decision:** Pending Solution Architect review and acceptance.

**Document standing:** Proposed repository-wide Implementation Guide. No repository changes have been made.