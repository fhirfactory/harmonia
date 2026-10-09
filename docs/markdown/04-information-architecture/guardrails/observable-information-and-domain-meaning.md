# Observable Information and Encapsulated Domain Meaning

## 1. Authority and Architectural Boundary

The authorised 2026-10-10 Domain04 reconciliation establishes this
semantic-agnosticism boundary and incorporates the general runtime-AI adjunct
position previously held in legacy execution-model documentation. It defines
the relationship between governed information, developer-defined behaviour
and domain interpretation. It establishes no new Application Architecture,
execution engine, AI orchestration, governance mechanism or implementation.

[AX-01](../../governance/architectural-axioms.md#ax-01) retains Harmonia's
health-information purpose. [AX-04](../../governance/architectural-axioms.md#ax-04)
retains its own architectural semantics;
[AX-12](../../governance/architectural-axioms.md#ax-12) supports developer access
to governed information and models. [AX-06](../../governance/architectural-axioms.md#ax-06),
[AX-07](../../governance/architectural-axioms.md#ax-07) and
[AX-08](../../governance/architectural-axioms.md#ax-08) preserve authority,
security and meaningful evidence. [AX-14](../../governance/architectural-axioms.md#ax-14)
preserves the distinctions below; [AX-17](../../governance/architectural-axioms.md#ax-17)
prohibits inferred architecture, and
[AX-18](../../governance/architectural-axioms.md#ax-18) prevents observation or
participation from conferring domain authority.

## 2. Observability and Encapsulated Domain Meaning

Harmonia's concept of reality is bounded by its observability of that reality
through the information available to it. Observation and management of
information do not imply semantic comprehension of all encapsulated domain
content. Missing observation supplies neither a domain fact nor a certain
operational outcome; [AX-15](../../governance/architectural-axioms.md#ax-15)
continues to govern operational uncertainty.

Harmonia may observe, carry, associate, apply, append, transform, persist,
route, forward and respond to information according to explicitly established
models, rules, Behaviours, Praxis and developer-defined logic without
independently deriving the domain meaning of that information.

**Harmonia is not intrinsically an inferential agent.** Meaning acted upon is
supplied through established models, explicit rules/behaviour,
developer-defined processing or intentionally invoked services. The Harmonia
Platform does not independently infer that meaning, nor intrinsically require
comprehension of encapsulated business, clinical or other domain content.

This boundary does not remove Harmonia's own semantics. Harmonia necessarily
understands the governed architectural semantics required to manage its own
information and execution constructs, including authority, lifecycle,
security, provenance and work/undertaking distinctions. Established semantic
models, terminology rules and conformance responsibilities remain applicable;
their existence does not make the Platform intrinsically comprehend every
content item it carries.

For the [bounded Task model](../information-families/task-work.md), an
`ActionableTaskArchetype` can establish content expectations without specifying
the content's structural representation or requiring domain comprehension.
`TaskOutcome` can encapsulate semantically opaque content while its established
Task information relationships remain governed.

## 3. Governed Ergo Logic Boundary

The Harmonia Platform is responsible for the governed execution boundary
around developer-defined Ergo business logic:

```text
Context Loading
      |
      v
Ergo Logic Process
      |
      v
Context Unloading
```

This is a conceptual boundary, not a new component allocation, interface,
runtime topology or definition of loading/unloading machinery. It does not
redesign Ergo/Ergon, Praxis, Pragma, Ponos or Digital Twin.

Developer-defined Ergo logic may understand or deliberately interpret known
content sufficiently to perform its intended function. It may inspect
content, apply rules, transform or map information, append information to a
model, invoke terminology or other domain services, invoke an AI agent/service,
calculate results and produce outputs. Such intentional interpretation belongs
to that defined logic or invoked service; it does not imply intrinsic domain
comprehension by the Harmonia Platform. Platform governance and the applicable
work, information and domain-authority boundaries remain in force.

## 4. Runtime AI as an Optional Adjunct

**Runtime AI is an adjunct execution capability intentionally invoked by
developer-defined Ergo business logic.** It is optional: Ergo execution may
use deterministic logic, rules/algorithms and governed information/services
without AI. Invocation does not make AI intrinsic to Harmonia or imply that
the Platform independently derives domain meaning.

Using an AI agent/service does not alter Ergo/Praxis/Digital Twin
responsibility, authority, governance or execution semantics. Architectural
responsibility remains with the governed Harmonia construct invoking the
capability. Invocation alone does not make the AI a Digital Twin, Business
Role, Service Guardian, governance authority, independent workflow authority
or owner of business responsibility, and transfers no architectural authority
to it. AI and assurance do not define Twin identity; the existing
[Strategy Digital Twin boundary](../../02-strategy/strategic-views/logical-component-responsibilities.md#component-7-digital-twin-entity-centred-operational-coordination-construct)
remains controlling.

This general position does not allocate an AI-backed assurance activity or a
Dokimasia execution engine, or establish new AI governance, orchestration or
implementation. Assurance-specific uses and downstream execution allocation
are outside this bounded reconciliation; unestablished matters remain explicit
under AX-17.

### 4.1 Runtime AI and AI-Assisted Development

**AI-assisted development** uses AI tooling to design, document, analyse, test
or implement Harmonia. Using such tooling to build Harmonia does not make it a
participant in runtime business execution.

**Runtime AI** is an AI agent/service intentionally invoked by runtime Ergo
business logic to contribute to governed execution. The distinction concerns
the purpose of invocation, not the product name. Neither development-time use
nor runtime invocation establishes intrinsic Platform intelligence.
