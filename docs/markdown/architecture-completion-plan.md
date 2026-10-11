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

# Harmonia R1.x/R2.x Architecture Completion Plan

## 1. Purpose and Authority

This canonical governance plan establishes the agreed programme for
completing, reconciling and baselining Harmonia's R1.x/R2.x architecture
documentation. The documentation serves three related purposes:

1. **Engineering input:** provide traceable design input and guardrails for
   implementation without prematurely prescribing implementation choices.
2. **Control framework for AI-assisted development:** human and AI developers
   SHALL reason from architecture rather than infer it from code, framework
   conventions or implementation familiarity. Architecture SHOULD be
   challenged where evidence warrants review, but SHALL NOT be silently
   bypassed, reinterpreted or completed through inference.
3. **Educational and demonstrative artefact:** demonstrate pragmatic use of
   architecture to support design, implementation, assurance, evolution,
   quality, maintainability, efficiency and cost optimisation.

The derivation between architectural layers is itself part of the deliverable.
The [Architectural Axioms](governance/architectural-axioms.md), applicable repository
`AGENTS.md` instructions and authoritative upstream architecture govern this
programme. In particular,
[AX-17 — Architectural Authority and Explicit Uncertainty](governance/architectural-axioms.md#ax-17)
requires established, unresolved and proposed architecture to remain distinct.

The canonical-corpus goal below does not relocate or override current
architectural authority. Outstanding dependencies on non-canonical
documentation must be explicit and addressed through separately authorised
tasks. `.junie/plans` and `.junie/reports` are execution records, not
architectural authority. The [Deferred Document Register](../deferred-document-register.md)
provides scope and deferral awareness; Item 04 SHALL NOT be used as authority
for the Business Architecture metamodel or identifiers.

## 2. Architecture Derivation

The intended derivation sequence is:

```text
Motivation
    ↓
Strategy
    ↓
Business Architecture
    ↓
Information Architecture
    ↓
════════════════════════════════
  DOMAINS01–04 UPSTREAM CONTEXT
  DOMAIN04 SEMANTIC BASELINE
  SOLUTION ARCHITECTURE HANDOVER
════════════════════════════════
    ↓
Domain05 — Information Systems Layer Architecture
  Candidate Architecture → validation and refinement
    ↓
Integration Architecture
    ↓
Technology Architecture
    ↓
Implementation
```

The current [Domain 01](01-motivation/README.md),
[Domain 02](02-strategy/README.md), [Domain 03](03-business-architecture/README.md)
and [Domain 04](04-information-architecture/README.md) indexes describe these
upstream responsibilities. Downstream architecture SHALL be demonstrably
derived from upstream architectural responsibility rather than reconstructed
from the existing implementation.

Where applicable, traceability should support reasoning such as:

```text
Implementation / Technology decision
    ↑
Information Systems responsibility
    ↑
Information Concept / Business requirement
    ↑
Business Function / Interaction
    ↑
Business Capability
    ↑
Strategic requirement
    ↑
Architectural motivation
```

This is illustrative traceability. It does not require every element to have
a relationship at every layer or establish relationships by itself. Under
AX-17, traceability SHALL be truthful rather than artificially complete;
unestablished relationships SHALL remain explicit.

### Solution Architecture Handover

Domains01–04 establish the upstream Enterprise, Business, Business Enabling and
Information Architecture: architectural intent, capabilities, semantics,
responsibilities, requirements, constraints and guardrails. Domain04 is formally
baselined as semantically complete for the agreed R1.x/R2.x scope. Domain05
represents the handover into **Solution Architecture** within that context.

Domain05 begins with **an established Candidate Information Systems Architecture
that must be documented, tested, reconciled and refined against the upstream
architecture**. Harmonia has accumulated substantial solution architecture over
many years. An experienced Solution Architect can recognise architectural
patterns and form a credible candidate from the upstream context; in Harmonia's
case the candidate already exists across accumulated design and architectural
material. The activity is neither a blank-sheet exercise nor a mechanical
derivation of a solution from Domains01–04.

> **Architecture constrains and validates the solution; it does not eliminate
> Solution Architecture design judgement.**

This complements the derivation obligation above. Design judgement operates
within established authority; familiar patterns do not establish missing facts
or relationships. Architecture SHALL NOT be silently reverse-engineered from
implementation mechanisms, frameworks, code structure or familiar technology
patterns. Capturing a candidate makes its intended architecture assessable;
it does not prove that the solution satisfies upstream architecture.

## 3. R1.x/R2.x Completion Sequence

The established programme stage numbering is retained for history and existing
references. Domains01–04 now provide the established/baselined upstream context;
the active stage is Domain05. Earlier stage intent is not retrospectively
rewritten by the following elaboration of stage 4.

1. **Domain 02 — Strategy Cleanup**
2. **Domain 03 — Business Architecture Completion**
3. **Domain 04 — Information Architecture Reconciliation**
4. **Domain 05 — Information Systems Layer Architecture Completion**
5. **External `/docs` Architectural Content Consolidation**
6. **R1.x/R2.x Architecture Consistency and Traceability Review**
7. **R1.x/R2.x Architecture Baseline**

Each stage requires bounded task authorisation and completion evidence against
the applicable gates. Existing documentation does not establish completion of
a future stage. Current completion status is recorded in section 4; the
sequence alone does not establish it.

The active forward programme is:

```text
Domains01–04 — established / baselined upstream architecture
    ↓
Domain05 — Information Systems Layer Architecture Completion
    A. Candidate Architecture Capture
    B. Capability and Information Mapping / Traceability
    C. Architecture Gap Analysis
    D. Architecture Excess Analysis and MVP Candidate Baseline
    E. Implementation Planning
    ↓
Subsequent Integration / Technology / implementation work
    as governed by the Completion Programme and applicable plans
    ↓
Residual consolidation, consistency / traceability review and final baseline
    (programme stages 5–7)
```

This sequence does not authorise or redesign Domain06, Technology Architecture
or implementation execution. Each remains separately bounded and governed.
Domain05's completion method is **Document → Map → Challenge → Minimise → Plan**.
The stages organise completion evidence; they do not prohibit iteration between
Information Systems views or review of the candidate as new evidence emerges.

### 3.1 Domain05 Scope and Candidate Architecture

**Domain05 — Information Systems Layer Architecture** is broader than an
Application Component Architecture. Its working scope is:

```text
Information Systems Layer
 |
 |-- Enabling Capability Layer + Enterprise Capability Layer
 |       (upstream demand referenced and mapped, not redesigned)
 |-- Logical Data Layer
 |-- Application Feature Map
 |-- Application Component Map
 |       |-- Data Objects
 |       |-- Information Responsibility
 |       `-- Persistence Responsibility
 |-- Middleware Map
 `-- Data / Persistence Layer
         `-- Persistence Data Structures
```

The **Harmonia Candidate Information Systems Architecture** represents the current
Solution Architecture candidate intended to satisfy the established upstream
architecture: an architectural hypothesis to be reviewed, tested and refined.
It is the current Solution Architecture baseline candidate, represented canonically
in RADS for rigorous assessment, rather than an ungoverned design suggestion.
Its candidate standing remains explicit under AX-17; existing approved
architectural constraints and decisions retain their authority.

Recording the candidate reconstruction in RADS establishes its canonical location;
it does not constitute architectural acceptance of its meaning. Existence in
RADS does not initially establish that every candidate component or feature is
required or traceable, that all upstream requirements are satisfied,
that the candidate is the minimum viable R1.x/R2.x solution, or that it is ready
for implementation. The candidate is the subject of Domain05 validation and
completion, not evidence that those activities are already complete.

### 3.2 Mutually Constraining Information Systems Views

> **Logical Data, Application Features and Application Components SHALL be
> developed and reconciled together as mutually constraining views of the
> Information Systems Layer.**

These views are architecturally distinct but SHALL normally be analysed and
reconciled together: **Logical Data ↔ Application Features ↔ Application
Components**. No view must be completely finished before the others can be
considered. A Component without a requiring Feature, a Feature without
architectural purpose or information interaction, or a Data Object without a
requiring Feature or Component warrants investigation. Such cases may expose
unjustified scope or implementation detail leaking upward. Conversely, a Feature
may reveal a previously implicit Data Object, and a Component boundary may reveal
an unclear information-management responsibility. This is iterative Solution
Architecture reasoning, not automatic proof of a gap or excess.

The Enabling Capability Layer and Enterprise Capability Layer establish upstream
architectural demand. They are outside this core co-design triad. Domain05 SHALL
reference and map the established capability models, not recreate or redesign
them.

**Middleware** is a distinct supporting Information Systems view: internal
collaboration, messaging, handoff and supporting mechanisms between Harmonia
Information Systems components. **Domain06 Integration Architecture** concerns
external/cross-boundary integration semantics, interfaces and interactions.
Messaging and middleware therefore do not all belong to Domain06. Middleware
may be analysed somewhat separately, but remains constrained by, and may feed
constraints back into, the core views. Products, protocols and broker
implementations SHALL NOT become Domain05 requirements without architectural
justification.

**Data / Persistence** is another distinct supporting Information Systems view.
The completion method SHALL preserve the following distinctions:

```text
Logical Data
    | represented by
    v
Application Data Objects
    | persisted through
    v
Persistence Data Structures
    | hosted / implemented upon
    v
Technology Platforms
```

**Information Responsibility** identifies which application component manages
information within the Information Systems Layer. **Data Persistence
Responsibility** identifies which application component provides durable
persistence, retrieval and preservation. Persistence responsibility does not
automatically confer information-management authority; persistence technology
does not define information semantics or application responsibility. This
preserves AX-04/05/06/14/18 without allocating components here. Detailed
responsibility allocation belongs in Domain05.

### 3.3 Domain05 Five-Stage Completion Method

#### Step A — Candidate Architecture Capture

Document the existing Harmonia Candidate Information Systems Architecture in
RADS. Reconstruct and make explicit its system/component hierarchy, application
features, components and behaviours, logical/application Data Objects,
information-management and persistence responsibilities, internal middleware,
Data/Persistence architecture, significant collaborations and responsibility
boundaries. Capture the architecture Harmonia currently believes it has within
existing authority and legacy reconciliation rules. This does not yet prove
upstream coverage.

Step A includes reconstruction, architectural review/adjudication, iterative
correction/reconciliation as required, and acceptance of the reconstruction as
sufficiently faithful to the intended Harmonia Candidate Architecture. Its current
activities are:

| Step-A activity | Current status |
|---|---|
| A1 — Reconstruct and document Candidate Architecture | COMPLETE |
| A2 — Architectural review and adjudication | IN PROGRESS |
| A3 — Correct/reconcile Candidate Architecture as required | IN PROGRESS; performed iteratively with A2 |
| A4 — Accept Candidate Architecture as sufficiently faithful | PENDING |

**Step A is complete when the reconstructed Candidate Architecture has been
architecturally reviewed and is considered a sufficiently faithful representation
of the intended Harmonia solution architecture to serve as the subject of Step B
capability and information traceability.** Formal Step B SHALL begin only after
that acceptance.

“Sufficiently faithful” does not require every documentation gap or uncertainty
to be resolved, every candidate construct to be finally accepted, upstream
coverage to be demonstrated, the Candidate Architecture to be proven correct,
or MVP to be established. Explicit unresolved Candidate Architecture matters may
remain under AX-17 where they do not prevent meaningful Step-B analysis.

Step-A review asks: **Is this a sufficiently faithful representation of the
Harmonia Candidate Architecture we intend to test?** Whether that candidate
satisfies all upstream architecture belongs principally to Steps B and C.

#### Step B — Capability and Information Mapping / Traceability

Map the established upstream architecture onto the candidate. Trace relevant
Enterprise, Business and Business Enabling Capabilities, Business responsibilities
where necessary, Information Architecture concepts and obligations, and
architectural requirements, constraints and guardrails through appropriate
Information Systems Features, Components, Data Objects and responsibilities.
Traceability SHALL work in both directions where useful, establishing which
candidate elements realise established architectural demand. Relationships SHALL
remain truthful under AX-17; no exhaustive or artificial mapping is required.

#### Step C — Architecture Gap Analysis

Identify established upstream requirements that the candidate cannot presently
satisfy or cannot satisfy without additional architectural definition. A genuine
solution gap connects a required Capability or Information Obligation through a
required Information Systems Feature to **no adequate candidate realisation or
owner**. Documentation ambiguity, incomplete component definition and unresolved
legacy contradiction are not, by themselves, proven upstream architecture gaps.

Determine whether each discovered gap belongs to Domain05 or exposes a precise
upstream defect. The latter requires explicit review and reopening of the relevant
earlier architectural decision under AX-17. Domain05 SHALL NOT silently repair
or redefine upstream architecture.

#### Step D — Architecture Excess Analysis and MVP Candidate Baseline

Perform reverse traceability over significant Candidate Features, Components
and supporting responsibilities. Determine whether each is justified by an
upstream capability, information requirement, architectural constraint, necessary
supporting Information Systems responsibility or another justified dependency.
An apparently excess construct may be unnecessary scope, future architecture,
a missing trace, a missing upstream requirement or an enabling responsibility
requiring explicit justification. Absence of a trace does not automatically
authorise deletion or manufacture an upstream requirement.

Establish the **MVP Candidate Information Systems Architecture** as:

> **The smallest coherent subset of the Candidate Architecture capable of
> satisfying the established Harmonia R1.x/R2.x architectural requirements and
> constraints.**

MVP is not merely the smallest feature set that can be shipped. Valid candidate
architecture not required for R1.x/R2.x may be explicitly retained as future or
deferred architecture. Establishing the MVP baseline does not require destroying
that knowledge.

#### Step E — Implementation Planning

Only after the candidate has been mapped, challenged, gap-corrected and reduced /
baselined for MVP should the programme establish the implementation plan. Planning
may address sequencing, increments, dependencies, migration/convergence, technical
realisation, deployment, verification, transition from existing implementation and
delivery. **Architecture completion**, **implementation planning** and
**implementation execution** remain distinct. Recording this stage neither
establishes an implementation plan nor authorises its execution, and does not
waive subsequent Integration or Technology Architecture obligations.

## 4. Current Programme Position

**2026-10-11 — Domain05 Step A: initial Candidate Architecture reconstruction
completed; architectural review/adjudication in progress.** The
[Harmonia Candidate Information Systems Architecture](05-information-systems-architecture/README.md)
is now represented in `docs/markdown/05-information-systems-architecture/`.
Codex has reconstructed and documented the candidate, but that reconstruction has
not yet been architecturally accepted. The completed reconstruction/documentation
activity (A1) is recorded as Step 1 in the reconstruction documents; **Step A as
a whole is not yet complete**.

Repository evidence verifies eight RADS documents, including the
[system/component map](05-information-systems-architecture/candidate-system-map.md),
[22 component/construct entries](05-information-systems-architecture/component-register.md),
[34 candidate features](05-information-systems-architecture/candidate-feature-map.md),
[Data/Persistence](05-information-systems-architecture/data-and-persistence.md),
[Middleware](05-information-systems-architecture/middleware.md) and
[source traceability](05-information-systems-architecture/sources.md).

The [34 documentation/component-feature gaps](05-information-systems-architecture/gaps.md)
are candidate-definition issues, not established upstream coverage gaps.
Source traceability records reconstruction provenance, not Step B capability or
information realisation mappings. The reconstruction applied 19 section-level
legacy supersession wrappers across 13 legacy files. Preservation validation
confirmed unchanged enclosed sections and original files after wrapper removal;
the recorded architecture verification passed 90 tests across 11 classes, with
no failures, errors or skips. The
[reconstruction completion report](../../.junie/reports/2026-10-11-domain05-candidate-information-systems-architecture.md)
records execution evidence, not architectural authority or semantic approval.
These remain evidence that A1 reconstruction/documentation is complete, not that
the reconstructed architectural meaning has been accepted.

**The current programme activity is architectural review/adjudication of the
reconstructed Candidate Architecture**, followed formally by Step B only once
it is accepted as sufficiently faithful to the intended Harmonia solution under
the Step-A completion condition. No Step B upstream mapping/coverage,
Step C architecture gap analysis, Step D excess/MVP baseline or Step E
implementation plan is established by initial capture. Domain05 completion and
the final programme baseline remain future work subject to the completion gates
and separately bounded tasks.

The [2026-10-10 Application Architecture assessment](../../.junie/reports/2026-10-10-domain05-application-architecture-assessment.md)
remains useful non-authoritative discovery/reconciliation evidence. Its
Application Architecture framing, Q01–Q05 as a work sequence, and recommendation
to begin with Q03 are superseded by the current Domain05 definition and method.
Its inventories and findings may inform candidate review and subsequent gap
analysis; the historical report remains unchanged.

| Domain | Current programme status |
|---|---|
| Domains01–02 — Motivation / Strategy | Established upstream intent and capability context; earlier programme history retained below |
| Domain03 — Business Architecture | Semantically complete for the agreed R1.x/R2.x scope — 2026-10-09 |
| Domain04 — Information Architecture | Semantically complete for the agreed R1.x/R2.x scope — 2026-10-10 |
| Domain05 — Information Systems Layer Architecture | Step A remains open — A1 reconstruction/documentation complete (2026-10-11); A2 architectural review/adjudication and A3 bounded correction/reconciliation in progress; A4 candidate acceptance pending; Step B capability + information traceability NOT STARTED and follows acceptance; Steps B–E remain unperformed and the Domain Completion Gate is not yet satisfied |

### Established upstream completion and earlier Domain05 transition

The authorised Domain 03 **Step 3 — Residual Semantic Adjudication and Capacity Management Pattern Review** is complete. The 2026-10-09 assessment establishes **semantic completion for the agreed R1.x/R2.x Business Architecture scope** against the Domain Completion Gate. The [Domain 03 metamodel](03-business-architecture/metamodel/business-architecture-metamodel.md#9-r1xr2x-semantic-sufficiency-boundary), [residual Service Delivery decisions](03-business-architecture/behaviours/03-service-delivery.md#residual-feature-sufficiency) and [HSO capacity responsibilities](03-business-architecture/behaviours/04-health-service-operations.md#capacity-management-responsibility-boundary) record the authoritative outcomes; the [Domain 03 completion boundary](03-business-architecture/README.md#5-completion-boundary-and-retained-uncertainty) retains downstream, governance and deliberately unestablished matters. Completion requires semantic sufficiency, not graph density. No freeze/refreeze or final R1.x/R2.x programme baseline is declared.

**2026-10-10 — Domain04 Information Architecture completion recorded.**
[Domain04 is semantically complete for the agreed Harmonia R1.x/R2.x scope](04-information-architecture/README.md#completion-status-and-semantic-baseline).
Its authoritative information meaning is sufficient for downstream derivation
without requiring downstream domains to invent Information Architecture
semantics. The **Domain Completion Gate is SATISFIED** on the basis of the
[residual semantic-gap assessment](../../.junie/reports/2026-10-10-domain04-residual-semantic-gap-assessment.md)
and subsequent [search-semantics reconciliation / gate reassessment](../../.junie/reports/2026-10-10-domain04-search-semantics-reconciliation.md#6-domain-completion-gate-reassessment).
The former sole Category A finding, **R01 — search-returned information
management — is resolved**; no Category A Domain04 semantic blocker remains.

Known retained uncertainties and explicit deferrals remain valid, as referenced
in the [Domain04 baseline](04-information-architecture/README.md#completion-status-and-semantic-baseline).
This is semantic completion for the agreed scope, not exhaustive modelling or
immutability. Future evolution remains subject to normal architecture
governance. No final R1.x/R2.x programme baseline is declared.

**Historical transition statement — 2026-10-10.** The wording below records the
earlier Application Architecture framing and authorisation boundary. Its Domain05
framing is superseded by the Information Systems Layer Architecture definition
and method above; its upstream preservation and AX-17 obligations remain valid.

**The Architecture Completion Programme may proceed to Domain05 — Application
Architecture.** This is the next stage and requires a separately bounded task;
no Domain05 reconciliation or derivation is undertaken by this completion action.
Domain05 may determine how the established Information Architecture is realised
and SHALL preserve its information meaning. Genuine contradictions or missing
upstream semantic requirements SHALL be raised through architecture governance
under AX-17 rather than silently resolved within downstream design.

### Canonical Architectural Axioms Migration — Completed

The separately authorised 2026-10-09 migration is complete. The single maintained
axiom authority is [the canonical governance register](governance/architectural-axioms.md).
AX-01 through AX-17 and their supporting normative material are semantically
preserved. [AX-18](governance/architectural-axioms.md#ax-18) is an approved new
human architectural decision established at this migration boundary.

Motivation now provides orientation/navigation, and the former external register
is a non-authoritative compatibility pointer. Semantic, authority, Markdown/link,
whitespace and architecture validation passed; the
[Step 2 execution report](../../.junie/reports/2026-10-09-canonical-architectural-axioms-migration-step2.md)
records the evidence. At that migration boundary, Domain04 changes were link-only;
its semantic reconciliation remained the next separately authorised stage.

This completes canonicalisation of the axiom knowledge, not overall corpus
consolidation. Accepted ADRs, execution-model material and other external
architectural dependencies still require separately authorised assessment and
consolidation under this plan.

### Earlier Recorded Programme Position

The following records the programme position previously captured by this plan, rather than a prohibition on subsequent bounded authorisation:

- Domain 02 Strategy Cleanup Step 1 assessment has been completed.
- Its findings have subsequently been architecturally adjudicated.
- Domain 02 cleanup implementation remains a separately authorised task.
- This Completion Plan does not itself perform or authorise that cleanup.

The finding register and individual adjudication decisions belong to the
bounded Domain 02 cleanup task and its completion evidence. They are not
reproduced or encoded here.

#### Domain04 Position after Step 5 — Historical, 2026-10-09

The following preserves the earlier Step 5 programme record unchanged. Its
semantic-sufficiency NO and task-authorisation limits applied at that boundary;
the current Domain04 baseline and Domain05 transition are recorded in section 4.

The current domain stage is **Domain 04 — Information Architecture Reconciliation**. The separately authorised [Step 1 assessment](../../.junie/reports/2026-10-09-domain04-information-architecture-reconciliation-step1.md), [Step 2 established responsibility/trace corrections](../../.junie/reports/2026-10-09-domain04-information-architecture-reconciliation-step2.md), [Step 3 Information Unit documentation/integration](../../.junie/reports/2026-10-09-domain04-information-unit-step3.md), [Step 4 Version / Active Generation semantics](../../.junie/reports/2026-10-09-domain04-version-active-generation-step4.md) and [Step 5 Active Generation establishment/failure boundary](../../.junie/reports/2026-10-09-domain04-active-generation-establishment-step5.md) are complete. The [canonical Domain04 metamodel](04-information-architecture/metamodel/information-architecture-metamodel.md#5-harmonia-managed-information) now establishes Harmonia-managed information, the conceptual Information Unit boundary, release-bound model and platform/framework obligations, together with the [Version / Active Generation distinction](04-information-architecture/metamodel/information-architecture-metamodel.md#651-version-and-active-generation) and its [successful establishment / operational failure boundary](04-information-architecture/metamodel/information-architecture-metamodel.md#656-establishment-and-semantic-indivisibility); detailed structures and remaining adjudications are deferred. The current Version / Active Generation discussion is closed at this semantic boundary; manifestation mechanics remain downstream. Semantic sufficiency remains **NO**. The next Information Architecture question requires separate bounded human adjudication; Domain04 completion and Domain05 are not authorised by this step. The proposed HSO-16 Strategy name correction remains a separately controlled follow-on task. Neither that correction nor the axiom migration was performed as part of the Domain03 reconciliation.

## 5. Canonical Architecture Corpus

> `/docs/markdown` is the Reference Architecture Documentation Set (RADS), the
> canonical Harmonia architecture documentation corpus authoritative for the
> current Harmonia architecture.
>
> No architectural knowledge required to understand, derive, govern or
> implement Harmonia SHALL depend upon documentation outside `/docs/markdown`.
>
> Architectural content found elsewhere under `/docs` SHALL be treated as
> migration input. It must be assessed and semantically incorporated into the
> appropriate architectural domain rather than mechanically copied.

The standing repository [RADS and legacy documentation rules](../../AGENTS.md#12-rads-and-legacy-documentation-governance)
govern reconciliation and preservation throughout the repository, including
documentation outside `/docs`. Required architectural dependencies still held
in legacy-doco remain explicit pending separately authorised reconciliation;
their discovery or reference does not establish incorporation into RADS.

The requirement concerns architectural knowledge. It does not require every
file under `/docs` to move. Implementation guidance, developer documentation,
historical records, generated material and other supporting documentation may
remain outside the canonical architecture corpus. Supporting material SHALL
NOT be the sole source of required architectural knowledge.

## 6. Semantic Migration Rule

Migration into the canonical corpus SHALL be semantic, not structural. For
architectural material found outside `/docs/markdown`, determine:

1. what architectural knowledge it contains;
2. whether that knowledge remains valid;
3. whether it conflicts with current authoritative architecture;
4. which TOGAF/ArchiMate architecture domain or governance artefact owns it;
5. whether it is already represented canonically;
6. whether it should be incorporated, reconciled, retained as supporting
   material, or have its architectural meaning retired.

When authoritative RADS content replaces a legacy section's architectural
meaning, apply the standing section-supersession rule: preserve the original
content unchanged and surround only the affected section with a wrapper
referencing the RADS replacement. Retirement of architectural meaning does not
authorise deletion or rewriting of the legacy information.

Migration SHALL NOT confer architectural authority merely because material
exists elsewhere under `/docs`. AX-17 applies to all reconciliation: preserve
unresolved matters explicitly, raise conflicts for architectural review and
keep candidates, suggestions and recommendations non-authoritative until
explicitly accepted. Historical identifiers or relationships SHALL NOT be
mechanically transferred into incompatible current models.

## 7. Continuous Assessment and Migration

During completion of Domains 02–05, relevant architectural material elsewhere
under `/docs` SHOULD be identified and assessed when relevant to the domain
currently being worked on. Architectural-content consolidation is not deferred
entirely until programme Step 5.

Discovery does not authorise migration. Non-canonical material does not
automatically become authority. Migration or reconciliation occurs only where
the bounded task explicitly authorises it; otherwise, record the required
future work and any unresolved dependency.

Programme Step 5 is the final completeness audit and residual
migration/reconciliation pass, not the first consideration of external
documentation.

## 8. Task Control

The Architecture Completion Plan establishes programme sequence,
documentation invariants and completion criteria. It does not itself authorise
architectural changes or migration.

Each architectural modification remains subject to a bounded task, applicable
upstream architectural authority, AX-17 and explicit treatment of unresolved
architecture. Candidates, suggestions and recommendations remain
non-authoritative until explicitly accepted. Missing architecture SHALL NOT
be completed through inference.

Creating this plan does not perform Domain 02 Step 2 cleanup, change Domains
01–04, derive Domain 05, reconcile AX-16, amend axioms or the deferred register,
change capabilities, Strategic Value Streams or Courses of Action, allocate
EC-14, derive Dokimasia architecture, migrate external `/docs` content, migrate
identifiers, or change implementation code. It does not declare the R1.x/R2.x
architecture complete or frozen.

### 8.1 AI Context Independence and Progressive Authority Loading

**AI Context Independence:** Each substantive architecture or implementation
task SHOULD be executable from a fresh AI context using repository-held
architectural authority. Prior conversational context, AI-session state,
model memory, generated summaries or other AI-derived representations SHALL
NOT be required to correctly interpret Harmonia architecture.

AI agents SHOULD initially load the minimum authoritative context sufficient
for the bounded task and progressively expand that context by following
architectural dependencies, references and traceability. Repository navigation
aids MAY identify authoritative sources and dependency paths but SHALL NOT
substitute summaries, previous-session context or AI-derived representations
for the authoritative architecture.

Where sufficient architectural authority cannot be located, or available
authority is ambiguous or contradictory, the uncertainty SHALL be made
explicit in accordance with
[AX-17](governance/architectural-axioms.md#ax-17)
rather than resolved from prior conversational context, model memory,
convention or inference. A plausible relationship does not become an
authoritative relationship through navigation assistance or prior AI
interpretation.

#### Fresh-Context Default

Fresh AI context is the preferred default for a new substantive bounded
architecture or implementation task. A session restart is not required for
direct clarification of the current task, correction of a mechanical error
within that task, review of immediately completed work before closure, or
other tightly coupled activity within the same authorised task boundary.
The purpose is to prevent previous AI interpretation from becoming implicit
architectural authority, not to require a restart after every interaction.

#### Progressive Authority Loading

A task normally develops sufficient authoritative context through the
following activities:

1. Start with repository operating and governance instructions.
2. Read the architecture-completion programme context where applicable.
3. Read the bounded task's target architecture or domain material.
4. Read relevant upstream architectural authority required to establish
   derivation.
5. Follow explicit references, dependencies and traceability where needed.
6. Expand into broader repository material only when required to resolve a
   dependency, validate a relationship, investigate a conflict or establish
   sufficient authority.

This is guidance for progressive loading, not a fixed mandatory file-reading
sequence. Required task inputs and repository instructions still apply. The
amount of context depends on the task; this guardrail imposes no token,
document-count or arbitrary context budgets. The objective is **minimum
sufficient authoritative context, progressively expanded when required**.

#### Repository Authority

This distinction applies the existing authority rules in section 1 and
applicable repository instructions; it does not create another hierarchy or
relocate current authority.

Authoritative material includes:

- canonical architecture documentation;
- applicable repository governance and instructions;
- explicitly authorised task decisions;
- other repository artefacts where the architecture explicitly assigns
  them authority.

The following are non-authoritative by themselves:

- previous AI conversation context and AI-session state;
- model memory;
- compacted conversation summaries and other generated summaries;
- previous-session generated explanations and other AI-derived
  representations;
- `.junie/plans` and `.junie/reports`;
- implementation structure;
- framework conventions;
- lexical similarity;
- historical documentation whose authority has been superseded.

Non-authoritative material MAY assist discovery or investigation. It SHALL
NOT establish architecture merely because it is available to an agent.
Missing authority remains missing; ambiguity and unresolved relationships
remain explicit under AX-17.

## 9. Completion Gates

### Domain Completion Gate

A domain SHALL be declared complete only when:

1. the architecture required for the agreed scope is sufficiently established;
2. known contradictions affecting that domain have been reconciled or
   explicitly retained as unresolved;
3. relevant traceability to authoritative upstream architecture is truthful;
4. known architectural knowledge relevant to that domain outside
   `/docs/markdown` has been semantically incorporated into the canonical
   corpus, reconciled and deliberately retained as supporting material, or
   explicitly identified as unresolved/deferred;
5. no architectural knowledge required to understand that domain depends
   solely upon non-canonical documentation.

Explicitly recording unresolved/deferred material does not waive the fifth
condition where required knowledge remains solely non-canonical. Completion
does not require artificial relationships, decomposition or allocation where
the architecture has not established them.

#### Application to Domain05

An initial Candidate Architecture in RADS does not satisfy this gate. For the
agreed R1.x/R2.x scope, Domain05 SHALL demonstrate sufficient candidate capture;
truthful realisation/traceability of relevant upstream capability and information
requirements; resolution of genuine solution gaps or explicit retention only
where permitted by the gate; assessment of significant candidate excess; and an
established MVP Candidate Information Systems Architecture.

Required Domain05 architectural knowledge SHALL be canonical in RADS. Known
relevant legacy architectural knowledge SHALL be incorporated, reconciled and
deliberately retained, or explicitly deferred under the conditions above. No
required Domain05 knowledge may remain solely outside RADS. Candidate
documentation gaps SHALL remain distinguishable from proven upstream coverage
gaps, and traceability SHALL NOT be manufactured to satisfy the gate. Step E
planning remains a distinct subsequent activity, not a substitute for these
architecture-completion conditions.

### Final R1.x/R2.x Completion Gate

The programme SHALL NOT declare the R1.x/R2.x architecture baseline complete
until:

> No architectural knowledge required to understand, derive, govern or
> implement Harmonia depends upon reading documentation outside the canonical
> `/docs/markdown` architecture corpus.

The final consistency and traceability review SHALL also establish that:

- downstream architecture does not silently contradict upstream authority;
- unresolved architecture remains explicit;
- historical identifiers or relationships have not been mechanically migrated
  into incompatible current models;
- implementation constructs have not silently become architecture authority;
- architecture-domain boundaries remain coherent;
- traceability is supported by actual semantic relationships rather than
  lexical or identifier similarity.

## 10. Standard Context for Subsequent Completion Tasks

Use the following reusable task preamble:

```markdown
### Architecture Completion Programme Context

This task forms part of the Harmonia R1.x/R2.x Architecture Completion
Programme defined in:

`docs/markdown/architecture-completion-plan.md`

Read that document before commencing this task.

The Completion Plan establishes programme sequence, canonical documentation
goals and completion criteria. It does NOT expand the authorised scope of
this task.

If architectural material relevant to the current task is discovered elsewhere
under `/docs`, assess it against the canonical architecture.

Do not silently treat non-canonical material as architectural authority.

Where appropriate, identify material that should subsequently be migrated
into `/docs/markdown`.

Do not perform migration unless explicitly authorised by this task.
```

## 11. Standard Completion-Report Requirement

Include the following reusable section in subsequent completion reports:

```markdown
### Canonical Documentation Assessment

Report:

1. whether relevant architectural material was found outside `/docs/markdown`;
2. the source document(s);
3. what architectural knowledge they contain;
4. the canonical architecture domain into which that knowledge belongs;
5. whether it was incorporated by this authorised task;
6. if not incorporated, what future migration/reconciliation is required.

Do not declare a domain complete while known required architectural knowledge
for that domain remains solely outside `/docs/markdown`.
```
