<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->


# Harmonia Architectural Axioms

## 1. Purpose

This document defines the Architectural Axioms of the Harmonia Health
Integration Environment (HIE). The axioms describe the fundamental
architectural truths upon which Harmonia is designed. They establish the
boundaries within which architecture decisions, subsystem designs,
implementation patterns and technology choices are made. The axioms are
intentionally more stable than individual Architecture Decision Records
(ADRs), requirements, design contracts or implementation decisions. The
architectural authority hierarchy is:

`</> plain text       Harmonia Vision and Purpose                     │                     ▼           Architectural Axioms                     │                     ▼         Architecture Decisions                     │                     ▼    Architectural Invariants and Guardrails                     │                     ▼     Design Contracts and Requirements                     │                     ▼                Implementation                     │                     ▼         Automated Conformance Tests`
An ADR, requirement, design or implementation SHALL NOT knowingly
contradict an Architectural Axiom.

Where existing implementation or documentation conflicts with an axiom,
the conflict SHALL be identified and resolved explicitly. Existing
implementation is not, by itself, evidence of architectural intent.

The keywords SHALL, SHALL NOT, SHOULD, SHOULD NOT and MAY are used
normatively throughout this document.

## 2. Architectural Axioms

### AX-01 --- Harmonia Is Health-Information Centric

#### Axiom

Harmonia is a distributed health-information management and
interoperability framework.

Its primary purpose is the ingestion, validation, transformation,
governance, processing, persistence, distribution and exposure of
health-related information.

Harmonia SHALL support applicable health-information standards and
models while retaining the ability to manage information according to
Harmonia's own internal operational requirements.

#### Rationale

Harmonia is not intended to become a generic distributed application
framework to which healthcare happens to be attached.

Its architecture exists to solve health-information problems:
interoperability, information authority, provenance, security,
terminology, workflow, persistence, distribution, resilience and
longitudinal information management.

#### Consequences

Health-information semantics take precedence over generic infrastructure
convenience.

Standards such as HL7 FHIR and HL7 v2 are first-class architectural
concerns.

Generic infrastructure capabilities SHOULD be used where useful, but
SHALL NOT dictate Harmonia's health-information semantics.

**This does not mean** \* Harmonia is a FHIR server. \* Harmonia is
restricted to FHIR information. \* Every internal Harmonia object must
correspond to a healthcare interoperability standard.

### AX-02 --- Standards at the Boundary; Harmonia Within the Boundary

#### Axiom

Harmonia SHALL ingest and expose health information using the standards,
information models and interoperability protocols applicable to an
external interface contract.

Within Harmonia, information MAY be augmented, represented, indexed,
cached, distributed, governed, versioned, correlated and persisted using
Harmonia-specific semantics and metadata necessary to satisfy Harmonia's
operational requirements.

Harmonia's internal operational semantics are private to Harmonia.
External systems SHALL NOT be required to understand, preserve,
reproduce or participate in them.

#### Rationale

Interoperability requires agreement at system boundaries. It does not
require participating systems to share an internal architecture.

Allowing external contracts to dictate Harmonia's internal
implementation would couple Harmonia to the assumptions and limitations
of external systems.

#### Consequences

Pylai acts as an interoperability membrane, not merely a protocol
adapter.

External representations SHALL conform to their applicable interface
contracts.

Harmonia-private operational metadata SHALL NOT leak into an external
interoperability contract.

#### Maxim

Standards at the boundary. Harmonia within the boundary.

### AX-03 --- Native Standards Models Remain Native

#### Axiom

Standards-defined information objects SHALL retain their
standards-defined representation within Harmonia wherever practicable.

Harmonia SHALL NOT introduce parallel or derivative representations
solely to accommodate internal management concerns where the standard
representation provides an appropriate extensibility mechanism.

#### Rationale

Parallel representations introduce mapping complexity, semantic drift
and unnecessary ownership questions.

For FHIR information, the FHIR resource is already an extensible
information model.

#### Consequences

FHIR R5 and FHIR R4 AU resources SHOULD remain native FHIR resources
represented using the applicable HAPI FHIR models.

Where appropriate, Harmonia-specific durable metadata MAY be represented
using private FHIR extensions.

Jurisdictional and implementation-guide extensions, such as extensions
required for Australian FHIR conformance, remain part of the
standards-based representation and SHALL NOT be confused with
Harmonia-private operational extensions.

Non-FHIR managed objects MAY use representation-appropriate Harmonia
metadata mechanisms.

**This does not mean:** \* All Harmonia metadata must be represented as
FHIR extensions. \* Cache-specific state must be embedded into FHIR
resources. \* Non-FHIR Harmonia objects must be converted into FHIR.

### AX-04 --- Harmonia Owns the Semantics; Engines Provide the Machinery

#### Axiom

Harmonia owns the architectural semantics governing information
authority, security, lifecycle, concurrency, provenance, auditability,
resilience and operational integrity.

Harmonia SHALL preferentially use capabilities supplied by underlying
technology engines where those capabilities satisfy Harmonia's
architectural invariants rather than reproduce equivalent functionality.

#### Rationale

Harmonia should own its meaning without unnecessarily owning commodity
infrastructure machinery.

#### Consequences

HAPI FHIR MAY provide FHIR persistence, indexing, versioning and search.

Infinispan MAY provide distributed caching, entry metadata and atomic
coordination primitives.

ActiveMQ Artemis MAY provide durable messaging.

PostgreSQL MAY provide transactional relational persistence.

These technologies implement Harmonia capabilities. They do not define
Harmonia's architectural semantics.

#### Maxim

Use the machinery. Own the semantics.

**This does not mean:** \* Harmonia should wrap every engine capability
behind a bespoke implementation. \* An engine's native behaviour
automatically satisfies a Harmonia invariant. \* Using HAPI FHIR makes
Harmonia a HAPI FHIR application.

### AX-05 --- Active State and Authoritative Durable State Are Distinct

#### Axiom

Mneme owns Harmonia's application-facing access to managed information
and the distributed active-state representation, observation and
coordination required to use that information safely.

Mnemosyne owns Harmonia's authoritative durable representation of
managed information. It atomically establishes authoritative state and
authoritative version progression and persists the durable management
metadata required to interpret that state.

Mneme manages active use; Mnemosyne establishes durable truth.

#### Rationale

Distributed availability and authoritative persistence are different
responsibilities.

Conflating them makes cache state authoritative, persistence state
application-facing, or both.

#### Consequences

Mneme MAY reject or coordinate a proposed state progression before
persistence.

Only Mnemosyne can establish a new authoritative durable state.

Following authoritative commit, Mneme SHALL converge its active
representation toward the authoritative state.

Loss of Mneme SHALL NOT cause process-local or cached state to become
authoritative.

Loss of Mnemosyne SHALL NOT cause Mneme to promote its active
representation into authoritative durable state.

Mneme active state SHALL be reconstructable from authoritative state
where applicable.

**This does not mean:** \* Every Mneme operation requires a database
operation. \* Mnemosyne is exposed as an application database API. \*
Mneme and Mnemosyne must use the same version or concurrency mechanism.

Mneme active-state generation SHALL represent successfully established active state. A valid active-state token SHALL identify an observed generation of that state.

Failed or degraded convergence following authoritative state progression SHALL NOT establish or advance a valid active-state generation. Where the active representation cannot be successfully converged, its coordination state SHALL be treated as untrusted until reconciled with authoritative state.

Mneme active-state generation and Mnemosyne authoritative version SHALL remain distinct concurrency domains. Neither SHALL be inferred from, substituted for, or treated as an alias of the other.

### AX-06 --- Information Authority Is Explicit

#### Axiom

Harmonia SHALL represent the authority and credibility of managed
information independently of its technical transport, cache state,
persistence state or concurrency state.

Harmonia SHALL support governed information-authority concepts
including: \* Authoritative, \* Informational, and \* Anecdotal.

#### Rationale

Technical correctness and information credibility answer different
questions.

A successfully persisted assertion is not necessarily authoritative. A
stale assertion is not necessarily false. A technically reliable source
is not necessarily the authoritative source for every fact it supplies.

#### Consequences

Concurrency determines whether information has changed.

Semantic analysis determines what changed.

Information authority and assurance describe the standing of competing
assertions.

Policy determines what Harmonia does about them.

Calliope governs the semantics of information authority. Appropriate
Harmonia processes and policies apply those semantics.

**This does not mean:** \* Authoritative information automatically wins
every conflict. \* Anecdotal information is discarded. \* Persistence
success establishes information authority.

### AX-07 --- Security Is Intrinsic to Managed Operations

#### Axiom

Every governed operation SHALL execute within an established security
context and SHALL be subject to Harmonia security policy.

Security SHALL be enforced by framework and platform boundaries where
practicable and SHALL NOT depend solely upon developer knowledge, coding
convention or voluntary caller behaviour.

#### Rationale

Security is part of the semantics of performing an operation, not an
optional service invoked by well-behaved code.

#### Consequences

Themis owns Harmonia security policy evaluation.

Trusted identity and security context SHALL be established before
governed actions are performed.

Security context is operational context and SHALL NOT automatically
become persistent information content.

Security-significant events MAY become durable audit evidence according
to policy.

**This does not mean:** \* Every security decision belongs in a FHIR
resource. \* Every authorization evaluation belongs in the audit
repository. \* Internal security context should be exposed to external
systems.

### AX-08 --- Evidence Records Meaning, Not Machinery

#### Axiom

Provenance and audit evidence SHALL describe information-significant,
security-significant and business-significant events, assertions and
decisions.

Internal implementation mechanics SHALL NOT ordinarily become provenance
or audit evidence merely because they occurred.

#### Rationale

A durable evidence repository should explain what happened to
information and why, rather than become a permanent distributed debug
log.

#### Consequences

Authoritative state acceptance, significant information transformations,
stewardship decisions and security-significant actions MAY require
durable evidence.

Cache misses, cache rebalancing, CAS retries, connection retries,
reconstruction and ordinary infrastructure mechanics normally belong in
logging, metrics or transient diagnostics.

Kleio preserves durable evidence.

#### Maxim

Preserve the meaningful fact, not every mechanism that produced it.

### AX-09 --- Transient Operational State Is Ephemeral by Default

#### Axiom

Harmonia MAY maintain transient operational state associated with
managed information where required for caching, persistence,
concurrency, security, routing, resilience, diagnostics and processing
integrity.

Such state SHALL be retained only for as long as required by its
operational purpose and SHALL normally disappear when that purpose has
been satisfied.

Transient operational state SHALL NOT automatically become provenance or
audit evidence.

#### Diagnostic observability

Harmonia SHALL provide controlled mechanisms by which designated
transient operational state MAY be observed or reported when diagnostics
are explicitly enabled at framework, subsystem or component scope.

Where transient state directly contributes to an information-significant
decision, assertion or evidentiary fact, the meaningful information
SHALL be promoted into the appropriate durable provenance or audit
representation.

#### Rationale

Distributed systems require substantial operational state. Persisting
all of it indefinitely would confuse implementation mechanics with
information history.

Conversely, making operational state completely invisible would make the
framework extremely difficult to diagnose.

**This does not mean:** \* Transient state is unimportant. \* Transient
state cannot be logged or inspected. \* Diagnostic traces should
automatically become permanent evidence.

### AX-10 --- Distribution, Load and Failure Are Normal Operating Conditions

#### Axiom

Harmonia SHALL be designed on the assumption of distributed deployment,
sustained processing load and failure or degradation of individual
runtime components.

Loss or degradation of implementation machinery SHALL NOT silently alter
the authoritative, security, governance or information-authority
semantics of managed information.

#### Rationale

Distribution and failure are not exceptional architectural cases for
Harmonia. They are part of its normal operating environment.

#### Consequences

Work that has crossed an explicit durable processing boundary SHALL be
recoverable according to that boundary's contract.

Processing pressure SHOULD preferentially be represented as bounded,
durable backlog rather than unbounded growth of JVM heap, threads,
database connections or volatile work.

Failure SHALL be visible rather than silently replaced by semantically
weaker local behaviour.

**This does not mean:** \* Every component must be independently
distributed. \* Every operation must be asynchronous. \* Every internal
Java boundary should become a network boundary.

### AX-11 --- Managed Information Access Is Highly Available and Responsive

#### Axiom

Mneme SHALL provide highly available, responsive and load-tolerant
access to Harmonia-managed active information.

Its architecture SHALL favour bounded resource consumption, distributed
workload, reconstructable active state and graceful expression of
processing pressure.

Availability SHALL NOT be achieved by weakening authoritative-state,
concurrency, security, governance or information-authority guarantees.

#### Rationale

Mneme exists partly to prevent every application interaction from
becoming an authoritative persistence interaction while still preserving
Harmonia's correctness guarantees.

#### Consequences

Different categories of Mneme state MAY use different distribution and
consistency strategies.

Small coordination state need not have the same topology as large
resource caches.

Cache topology SHALL be selected according to the semantics and
operational requirements of the managed state rather than applied
universally.

**This does not mean:** \* Every cache should use REPL_SYNC. \* Every
managed resource must always reside in memory. \* Cache availability is
equivalent to authoritative information availability.

### AX-12 --- Hide Plumbing, Not Information

#### Axiom

Developers implementing Harmonia work units SHALL have direct and fluent
access to applicable standards-defined health-information models and
Harmonia's managed-information capabilities.

Framework plumbing SHALL provide security, governance, authority,
persistence, concurrency, provenance, messaging and operational services
without unnecessarily obscuring or replacing the underlying information
models. \#### Rationale Ergon and other Harmonia developers should
implement health-information behaviour, not repeatedly reconstruct the
framework's infrastructure contracts.

At the same time, hiding the actual information model behind generic
wrappers makes health semantics harder rather than easier to understand.

#### Consequences

A developer working with a FHIR Practitioner, Observation,
DiagnosticReport or other resource SHOULD be able to work with the real
standards model.

Harmonia SHALL make the safe and governed path the normal and convenient
path.

Raw infrastructure mechanisms SHOULD NOT become the ordinary application
programming model.

#### Maxim

Hide plumbing, not information.

**This does not mean:** \* Every capability must be exposed through one
universal API. \* Standards models should contain Harmonia
infrastructure logic. \* Developers should directly manipulate
RemoteCache, EntityManager, JMS or other infrastructure merely because
Harmonia uses those technologies internally.

### AX-13 --- Harmonia Management Has an Explicit Boundary

#### Axiom

Ingress establishes Harmonia management. Internal processing preserves
Harmonia management. Egress terminates Harmonia management of the
emitted representation.

Harmonia MAY retain evidence describing the information from which an
external representation was derived, the transformation applied, the
transmission performed and the externally observable outcome.

Harmonia SHALL NOT attribute its internal authority, governance,
concurrency, security, availability or operational guarantees to an
emitted representation after that representation crosses an external
egress boundary.

#### Rationale

Harmonia can govern information while it is under Harmonia's control. It
cannot govern a copy after that copy has been transferred to another
system.

Provenance can describe what Harmonia emitted; it cannot extend
Harmonia's operational control into another system.

#### Consequences

An HL7 v2 message ceases to be a Harmonia-managed representation when
emitted through its external egress boundary.

A FHIR resource returned through REST similarly becomes an externally
held representation after transmission.

A subsequent FHIR PUT, HL7 v2 message or other submission represents a
new ingress interaction and is assessed accordingly.

An HTTP ETag, FHIR meta.versionId, HL7 acknowledgement or equivalent
interoperability mechanism MAY communicate externally meaningful
transaction or version semantics. It SHALL NOT be interpreted as
extending Harmonia's internal operational control beyond its boundary.

#### Maxim

Provenance may describe or cross the boundary; operational control does
not.

**This does not mean:** \* The underlying information inside Harmonia
ceases to be managed when a representation is published. \* Harmonia
forgets what it transmitted. \* Acknowledgements and transaction
outcomes are irrelevant to audit or provenance.

### AX-14 — Semantic Distinctions Are Preserved
#### Axiom
Harmonia SHALL preserve meaningful distinctions between states, outcomes and concepts throughout its internal processing and across subsystem boundaries.

A distinction that is significant to information meaning, authority, security, concurrency, persistence, processing outcome or operational correctness SHALL NOT be collapsed merely because an underlying technology, transport, API or implementation abstraction does not represent that distinction directly.

Where an external interface contract intentionally presents a simpler or different semantic model, Harmonia MAY project its internal semantics into that contract explicitly at the applicable boundary.

#### Rationale
Loss of semantic distinction causes information about what Harmonia knows, what occurred and what may safely happen next to be lost.

Two conditions may produce similar implementation behaviour while having materially different meanings. Resource absence, access denial, persistence failure and indeterminate outcome may all prevent information from being returned, but they do not describe the same state.

Likewise, persistence version, active-state generation, interoperability version and information authority describe different properties even where an implementation happens to correlate them.

Implementation convenience SHALL NOT redefine those meanings.

#### Consequences
Semantically distinct outcomes SHALL remain distinguishable through Harmonia's internal contracts.

Security denial SHALL NOT be represented as resource absence merely because both prevent access to information.

A failure to establish authoritative state SHALL NOT be represented as authoritative absence.

An indeterminate outcome SHALL NOT be represented as either success or failure unless subsequent processing establishes that outcome.

ActiveStateToken, AuthoritativeVersion, externally meaningful version identifiers and standards-defined version metadata SHALL remain distinct where they represent different semantic domains.

Where an underlying engine or protocol lacks a distinction required by Harmonia, Harmonia SHALL introduce an appropriate semantic representation rather than infer meaning from incidental implementation details such as exception classes, message text or correlated version values.

Boundary projection MAY deliberately reduce or transform semantic distinctions where required by an external contract or policy, but such projection SHALL be explicit and SHALL NOT redefine Harmonia's internal semantics.

#### Maxim
Preserve semantics internally; project deliberately at boundaries.

This does not mean: Harmonia must expose every internal distinction externally. Similar outcomes can never share implementation machinery. Every semantic distinction requires a separate Java type. External standards or interface contracts may be ignored in favour of Harmonia's internal model.

### AX-15 — Uncertainty Is Preserved Until Resolved
#### Axiom
Where Harmonia cannot establish the state, outcome or effect of an operation with the certainty required by its governing contract, that uncertainty SHALL be represented explicitly.

Harmonia SHALL NOT infer, manufacture or assume a more certain outcome merely to simplify processing, recovery or application behaviour.

Uncertainty MAY be resolved through authoritative observation, reconciliation or other governed evidence capable of establishing the required state.

#### Rationale
Distributed operations can fail at points where the observable failure does not establish whether the requested operation occurred.

For example, loss of a connection after transmitting a request does not establish whether the receiving subsystem processed or committed that request. Treating such an outcome as either success or failure introduces information that Harmonia does not possess.

Preserving uncertainty allows subsequent processing to determine what actually occurred without compounding an unknown outcome through unsafe assumptions or repeated actions.

#### Consequences
An indeterminate operation outcome SHALL remain distinguishable from both confirmed success and confirmed non-occurrence.

Harmonia SHALL NOT automatically retry a state-changing operation where the previous attempt may have succeeded unless the operation's contract makes that retry demonstrably safe.

Recovery from an uncertain outcome SHOULD preferentially establish the current authoritative state and reconcile against it before further state-changing action is attempted.

Timeouts, connection failures and similar technical events SHALL NOT, by themselves, determine semantic outcome. Their interpretation SHALL depend upon what can be established about the operation's progress and the governing boundary contract.

Where subsequent authoritative observation or other governed evidence resolves an uncertainty, Harmonia MAY continue processing from the newly established state.

Uncertainty SHALL NOT cause transient, cached or process-local state to be promoted into authoritative state.

#### Maxim
__Do not turn “unknown” into “yes” or “no”.__

__This does not mean:__ Harmonia must retain uncertainty indefinitely. Every technical failure produces an uncertain outcome. Operations known not to have crossed the relevant state-changing boundary cannot be safely retried. Reconciliation must always require human intervention.

### AX-17 — Architectural Authority and Explicit Uncertainty

#### Axiom

Documented architectural decisions, definitions, relationships, boundaries
and constraints are authoritative and SHALL NOT be silently reinterpreted,
replaced, bypassed or contradicted by downstream architecture or
implementation.

Where the architecture does not establish a fact, relationship,
responsibility or decision, that absence SHALL be preserved explicitly
rather than completed through inference, convention, structural
convenience, lexical similarity or anticipated implementation.

Unresolved architecture MAY be accompanied by clearly identified
candidates, suggestions or recommendations, but these SHALL remain
explicitly non-authoritative until explicitly accepted into the
architecture.

Where evidence indicates that authoritative architecture may be incorrect,
incomplete or internally inconsistent, the conflict SHALL be raised
explicitly for architectural review. The existing architecture remains
authoritative until an approved architectural change is made. That
approved change becomes the new authoritative architecture.

#### Rationale

Downstream derivation must distinguish established architectural knowledge
from gaps and proposed resolutions. A plausible relationship supplies no
authority merely because it makes a model or implementation appear
complete. Absence of an established relationship is architecturally
meaningful information.

Repeated architectural review during Domain04 Package2 G1 confirmed this
need across different traceability relationships:

- K9: unresolved Capability hierarchy must not be completed through
  invented ancestry.
- K10: Behaviour / Process traceability must not be completed through
  invented stage equivalence.
- K11: Function / Service traceability must not be completed through
  unsupported Feature association.
- K12: cross-capability dependency traceability must not be completed
  through invented Service ownership or consumption.
- K13: downstream representation traceability must not be completed through
  anticipated FHIR, application or technology mappings.

These examples justify an architecture-wide rule; its application does
not depend on Domain04 or G1 terminology.

#### Consequences

Human architects, human developers and AI development agents SHALL apply
this axiom to architecture derivation, documentation generation and
implementation.

Architectural traceability SHALL represent established relationships and
explicitly preserve unresolved or unestablished relationships.
Completeness SHALL NOT be manufactured through:

- inferred hierarchy;
- structural convenience;
- lexical similarity;
- conventional modelling patterns;
- assumed equivalence;
- anticipated implementation;
- familiar standards;
- probable application design.

Candidates, suggestions and recommendations SHALL be distinguishable from
documented, approved architecture. A proposed resolution or apparently
better alternative SHALL NOT acquire authority through downstream use.

AI development agents SHOULD challenge architecture when evidence
warrants architectural review. They SHALL NOT silently bypass it because
they recognise a familiar destination.

This axiom applies the authority and explicit-review obligations in
[Purpose](#1-purpose), [Relationship to Architecture Decisions](#6-relationship-to-architecture-decisions)
and [Architectural Review Rule](#7-architectural-review-rule) to downstream
derivation and implementation.

[AX-14](#ax-14--semantic-distinctions-are-preserved) preserves semantic
distinctions; this axiom preserves the distinction between established,
unresolved and proposed architecture.
[AX-15](#ax-15--uncertainty-is-preserved-until-resolved) governs uncertainty
about operational state, outcome or effect; this axiom governs uncertainty
about the architecture itself. Neither form of uncertainty may be silently
resolved by assumption.

[AX-02](#ax-02-----standards-at-the-boundary-harmonia-within-the-boundary)
and [AX-04](#ax-04-----harmonia-owns-the-semantics-engines-provide-the-machinery)
preserve Harmonia's internal semantics from external standards and engine
assumptions. Familiar standards or technology patterns do not establish
otherwise undocumented architectural relationships.
[AX-06](#ax-06-----information-authority-is-explicit) governs the authority
of managed information; architectural authority remains a distinct concern.

#### Maxim

**Traceability must be truthful, not artificially complete.**

## 3. Subsystem Responsibilities Derived from the Axioms

The axioms establish the following high-level responsibilities: \*
Themis controls who may act. \* Calliope governs what information means
and its information authority. \* Mneme manages active distributed
information. \* Mnemosyne establishes authoritative durable state. \*
Petasos makes distributed work recoverable. \* Kleio preserves evidence.
\* Pylai makes health information interoperable without exposing
Harmonia's operational machinery.

These statements describe architectural responsibilities rather than
deployment boundaries. They SHALL NOT be interpreted as requiring a
separate process, network service or runtime for every subsystem.

## 4. FHIR and Harmonia

Harmonia exposes standards-compliant FHIR interfaces according to its
supported interoperability contracts, including FHIR R5 and applicable
FHIR R4 AU interfaces.

Harmonia MAY define and use Harmonia-specific FHIR extensions, value
sets and other metadata required for governance, information authority,
distributed resilience, concurrency, high availability and low-latency
management.

HAPI FHIR SHOULD be used for FHIR persistence, indexing, validation and
search capabilities where those capabilities satisfy Harmonia
requirements.

Harmonia-specific operational extensions, value sets and metadata SHALL
NOT be exposed to external FHIR consumers.

Jurisdictional, implementation-guide and interoperability extensions
required by the exposed FHIR contract SHALL be retained.

Pylai SHALL construct an externally publishable projection without
destructively modifying Harmonia's managed representation.

Publication SHALL be fail-closed: only information, profiles, extensions
and metadata permitted by the applicable external interoperability
contract SHALL be exposed. Harmonia-private operational metadata SHALL
remain internal.

External FHIR clients SHALL NOT be required to understand Harmonia's
internal distributed-operational semantics.

For REST interactions, externally observable operational behaviour SHALL
be expressed through the applicable FHIR contract and standard HTTP
semantics.

**Pylai hides Harmonia's implementation state, not FHIR's REST state.**

## 5. Architectural Enforcement

The axioms are not intended to remain documentation-only aspirations.

Where practicable, consequences of an axiom SHALL be enforced through: -
module and dependency boundaries; - framework APIs; - default-deny
behaviour; - architecture tests; - integration and behavioural tests; -
interoperability-boundary tests; and - build-time conformance checks.

Correctness SHALL NOT depend solely upon developer knowledge or
voluntary use of the correct implementation pattern.

> Make the safe thing the easy thing, and make the unsafe thing
> difficult or impossible.

Not every axiom can or should be directly represented by a single
automated test. Tests SHOULD enforce concrete architectural consequences
rather than attempt to encode architectural philosophy.

## 6. Relationship to Architecture Decisions

Architecture Decision Records explain consequential design choices made
within the constraints established by these axioms.

An ADR MAY refine how an axiom is realised.

An ADR SHALL NOT silently override an axiom.

Where Harmonia's architectural principles genuinely change, the affected
axiom SHALL be explicitly reconsidered and the consequences assessed
across existing ADRs, requirements, documentation, implementation and
tests. Changes to an axiom therefore require a higher level of
architectural consideration than ordinary implementation or design
changes.

## 7. Architectural Review Rule

Before proposing a material architectural change, the following
questions SHALL be answered: 1. Which axioms apply? 2. Does the proposed
change preserve them? 3. Does an existing implementation conflict with
them? 4. Can the required behaviour be provided by existing Harmonia or
underlying platform capabilities rather than introducing another
mechanism? 5. Can the resulting architectural constraint be mechanically
enforced?

Where the answer reveals an architectural conflict, the conflict SHALL
be resolved explicitly before implementation proceeds.
