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
  DOMAIN04 SEMANTIC BASELINE
════════════════════════════════
    ↓
Information Systems Layer Architecture
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
Implementation / Java decision
    ↑
Application responsibility
    ↑
Information Concept
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

## 3. R1.x/R2.x Completion Sequence

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

## 4. Current Programme Position

**2026-10-11 — Domain05 candidate reconstruction authorised.** Domain05 is now
**Information Systems Layer Architecture**, approached as Solution Architecture
within the established Domains01–04 context. The
[initial Harmonia Candidate Information Systems Architecture](05-information-systems-architecture/README.md)
reconstructs the already documented solution rather than re-deriving it from
upstream architecture. Candidate inclusion is not approval of R1.x/R2.x necessity.

The authorised Domain05 approach is: (1) document the Candidate Architecture;
(2) map upstream capability/information architecture and establish traceability;
(3) identify genuine architecture gaps; (4) identify excess candidate features
and establish the MVP Candidate Architecture; (5) establish the implementation
plan. This task performs **Step 1 only**. The initial record retains documentation
and component-feature gaps, not upstream coverage findings. Steps 2–5 and the
final Domain05 completion/baseline remain future separately authorised work.
The earlier Application Architecture assessment and its Q01–Q05 recommendation
are navigation evidence, not the current work sequence. General derivation and
completion gates remain applicable to the eventual validated solution baseline.

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

**The Architecture Completion Programme may proceed to Domain05 — Application
Architecture.** This is the next stage and requires a separately bounded task;
no Domain05 reconciliation or derivation is undertaken by this completion action.
Domain05 may determine how the established Information Architecture is realised
and SHALL preserve its information meaning. Genuine contradictions or missing
upstream semantic requirements SHALL be raised through architecture governance
under AX-17 rather than silently resolved within downstream design.

| Domain | Current programme status |
|---|---|
| Domain03 — Business Architecture | Semantically complete for the agreed R1.x/R2.x scope — 2026-10-09 |
| Domain04 — Information Architecture | Semantically complete for the agreed R1.x/R2.x scope — 2026-10-10 |
| Domain05 — Information Systems Layer Architecture | Initial Candidate Architecture reconstruction — Step 1; upstream mapping, validation against requirements, MVP and implementation planning remain future work |

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
