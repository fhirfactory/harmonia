<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Continuum architectural taxonomy and publication remap

Task-Issued-At: UNRESOLVED. The operative user instruction entered this
Codex interaction on the environment's 2026-10-23 Australia/Sydney date; no
attributable instruction-entry timestamp is exposed to the executing agent.
Neither the current clock nor the first tool invocation substitutes for that
event. The required UTC filename prefix is correspondingly unresolved for
Solution Architect treatment before closure.

Issuer: Mark Hunter, Solution Architect (operative user message).
Executor: Codex. Resulting release: 1.1.10; HCRL iteration: 001.
Originating HEAD: `b8c14272f0ab0a8a452d904a8c4fa608f65b620b`.
Originating working tree: clean. Root release: 1.1.9, previous 1.1.8.
The user links the reviewed 1.1.9 pending HCRL; its completed register confirms
the immediate predecessor. Those historical records remain unchanged.

This is direct authorisation to implement the bounded organisation/navigation
remap and regenerate publications. Final architectural acceptance remains with
the Solution Architect under the common Review Praxis. AX-03, AX-04, AX-08,
AX-14, AX-15 and AX-17 apply: preserve native semantics, canonical authority,
identity/standing distinctions, meaningful evidence and explicit uncertainty.

The issued wording follows verbatim (the attachment is represented by its
repository locator after the quoted instruction).

---

# Goal

Restructure the Harmonia Architecture Continuum and regenerate the Continuum RAD so that both expose a coherent, navigable architecture taxonomy aligned with the agreed Harmonia interpretation of ArchiMate.

This is release **1.1.10**.

The work is primarily an **architectural organisation and navigation remap**. It is not an exercise in redesigning the architecture, inventing missing architecture, changing architectural meaning, or re-identifying existing architectural elements.

# Expected Outcome

At completion:

1. The Architecture Continuum is organised according to the agreed Harmonia architectural taxonomy described below.
2. Existing architectural content is remapped into that taxonomy according to its existing architectural semantics.
3. The Continuum RAD is regenerated from the remapped Continuum and follows substantially the same hierarchy.
4. The Continuum Manifest reflects the remapped Continuum.
5. The Archi/Open Exchange projection exposes the same navigable architectural organisation as far as the projection permits.
6. Every node exposed in the navigable architectural hierarchy has a hierarchical navigation number down to the lowest published level.
7. Navigation numbering is consistent across the Continuum and its projections/publications.
8. Existing Canonical IDs remain the identity of architectural elements.
9. Existing architectural meaning, relationships and standing are preserved unless a change is explicitly required to perform the authorised organisational remap.
10. No new architectural content is invented merely to make the hierarchy look complete or symmetrical.
11. The resulting Continuum RAD is readable as an architecture document rather than as a literal dump of repository/model implementation structure.

# Context

The Architecture Continuum is the canonical architecture authority.

Archi/Open Exchange is a projection, workbench and interchange representation.

The Continuum Manifest is a structural/catalogue publication of the Continuum.

The Continuum RAD is the human-readable narrative publication/explanation of the Continuum.

Neither the Manifest nor the RAD is an independent architecture authority.

The previous 1.1.8 RAD generation demonstrated that mechanically projecting existing model organisation into LaTeX produces an undesirable reading structure. Model artefact/navigation names such as `Continuum additions`, `Unreconciled 1.0.4 Reference`, `BusinessObjectMap`, `InformationMap`, `Application Layer`, `Business Layer`, and similar implementation/history-oriented organisation must not automatically become reader-facing architectural chapters.

For 1.1.10, the Continuum organisation itself is authorised to be remapped so that the canonical architecture has an intelligible architectural taxonomy.

ArchiMate determines architectural semantics but does not dictate Harmonia's document/navigation taxonomy.

The Continuum hierarchy represents architectural organisation and navigation.

ArchiMate relationships represent architectural semantics.

Do not manufacture containment merely to represent a semantic relationship.

Architectural standing — current, pending, proposed, historical, superseded, etc. — is orthogonal to taxonomy. Do not create separate architectural branches merely because material has a particular standing.

`Unclassified <Domain>` is a legitimate location for material known to belong to an architectural domain but whose correct architectural classification has not yet been agreed.

# Navigation Numbering

Apply hierarchical navigation numbering to **every node exposed in the navigable architectural hierarchy, down to the lowest published level**.

The numbering is a navigation coordinate, not architectural identity.

Use:

- Governance: `G.x`
- Motivation: `I.x`
- Strategy: `II.x`
- Business: `III.x`
- Application: `IV.x`
- Technology: `V.x`
- Implementation & Migration: `VI.x`

Continue hierarchical numbering through all exposed descendants, including populated architecture.

For example:

`IV.2.3.1.1.4`

is a valid navigation coordinate.

The semantic rule is:

- **Canonical ID = immutable architectural identity**
- **Name = architectural meaning**
- **Hierarchy number = current navigation/location**

If an existing architectural element moves to a different location as a result of this authorised remap, its Canonical ID must remain unchanged while its navigation number changes to reflect its new location.

Do not create a second identity scheme from navigation numbers.

The same navigation coordinate should be exposed consistently through the Continuum, Archi projection, Continuum Manifest and Continuum RAD/PDF wherever technically applicable.

# Target Architecture Taxonomy

## Architecture Governance

Architecture Governance is not an architectural Part. It governs the architecture and uses the separate `G.x` namespace.

Target structure:

- Governance Model
  - Architecture Authority
  - Decision Rights
  - Roles and Responsibilities
- Architecture Evidence
  - Evidence before change
  - Evidence after change
- Architecture Review
  - Review records
  - Pending architectural decisions/dispositions
- Architecture Register
  - Accepted/completed governance records
- Architecture Governance Processes
  - Governed architecture Praxes
- Architecture Principles and Compliance
  - Application of canonical Architecture Principles
  - Compliance/conformance
  - Exceptions where applicable
- Architecture Repository and Authority
  - Architecture Continuum
  - Architecture publications and projections
  - Evidence
  - Review
  - Register

The existing `evidence`, `review` and `register` structures are the basis for this material. Do not invent an alternative governance repository.

Architecture Principles/Axioms remain normatively defined by their canonical Continuum Principle elements. Governance may reference and explain their application but must not duplicate their normative definitions.

## Part I — Motivation

Target structure:

- Drivers
- Strategic Goals
- Business Outcomes
- Assessments
- Requirements
- Constraints
- Architecture Axioms
- Unclassified Motivation

Stakeholders are a **cross-cutting Motivation perspective**, associated with relevant Drivers, Goals, Outcomes, Assessments, Requirements, Constraints and other Motivation material.

Do not create a peer Stakeholders chapter merely to create a type bucket.

## Part II — Strategy

Target structure:

- Capabilities
  - Enterprise Capabilities
    - existing governed capability hierarchy
  - Business Capabilities
    - CT1
      - CT2
        - CT3
          - Feature
  - Business Enabling Capabilities
    - CT1
      - CT2
        - CT3
          - Feature
- Courses of Action
  - Implementation Guardrails
  - Architecture Patterns
- Unclassified Strategy

The Enterprise / Business / Business Enabling headings provide meaningful **capability scope/context**.

Within those contexts, preserve and expose the existing governed capability decomposition.

Do not flatten the capability model.

Do not reinterpret CT1 → CT2 → CT3 → Feature.

Resources are a valid ArchiMate Strategy concept but must not receive a structural heading unless substantive Harmonia Resource architecture currently warrants it.

## Part III — Business

Target structure:

- Business Information Architecture
- Products and Value Streams
  - Products
  - Value Streams
- Organisation Architecture
  - Actors
  - Roles
  - Organisational Structures
- Business Behaviour
  - organise within relevant capability context
    - Business Services
    - Business Functions
    - Business Processes
    - Business Events
- Contracts
- Unclassified Business

The capability hierarchy remains authoritative in Strategy.

Do not duplicate the complete capability hierarchy in Business merely for navigation.

Business Behaviour should be organised in meaningful capability context rather than as enormous global ArchiMate-type buckets.

## Part IV — Application

Target structure:

- Information Architecture
  - Topics
  - Reference Data
  - Reusable Information Concepts
  - other existing logical information architecture where applicable

- System-of-Systems Architecture
  - System Map
  - System Responsibility Map

  - Integration Architecture
    - Interactions
      - `<Interaction>`
        - Interaction Overview
        - Interaction Events
        - Interaction Information Flow
        - Integration Data Flow
        - Integration Endpoints
    - Information Architecture
      - Classes
      - other integration-specific information structures
    - Interfaces
    - Shared Services and Interoperability

  - Middleware Architecture
    - `<Middleware System>`
      - Component Architecture
      - Information Architecture
        - Classes
      - Services
      - Interfaces
      - other middleware-specific architecture where applicable

- Applications
  - `<Application>`
    - Application Overview
    - Component Architecture
      - `<Component>`
        - component decomposition as applicable
    - Information Architecture
      - Classes
      - other application/component-specific information structures
    - Services
    - Interfaces
    - other application-specific architectural subjects where genuinely required

- Unclassified Application

Important semantic distinction:

Top-level Application Information Architecture is the **logical information model** and is predominantly **Topic-oriented**.

Integration-, application- and component-scoped Information Architecture represents implementation-specific information structures and is predominantly **Class-oriented**.

A Topic is not a Class.

One Topic may be represented by multiple Classes.

One Class may project or combine multiple Topics.

Do not promote implementation Classes into logical Topics merely because they exist.

Middleware Architecture is part of Application/System-of-Systems architecture, not Technology architecture.

Services and Interfaces are independent peer architectural dimensions. Do not manufacture containment between them merely because they are related.

Applications should be organised by actual application — for example Ponos, Mneme, Calliope, Themis, Mnemosyne, Pylai where these exist in the current model — rather than by global ArchiMate element type.

Do not force every application to have an identical internal structure when its actual architecture does not warrant it.

## Part V — Technology

Target structure:

### Data Architecture

- Data Technology Architecture
- Data Platforms and Services
- Data Storage and Processing
- Data Movement
- Data Standards and Reference Architectures

Data Architecture concerns technological realisation, persistence, processing and movement of data.

Do not confuse it with Business Information Architecture or logical Application Information Architecture.

### Platform Architecture

- Operating System Architecture
- Runtime Environment Architecture
  - Physical / Bare-Metal Runtime
  - Virtual Machine Runtime
  - other runtime forms where required
- Container and Orchestration Architecture
- Platform Management Architecture
  - Container Management
  - Kubernetes Management
  - API Management
  - Serverless / Function Platform Management
  - other platform-management architecture where required
- Platform Services
- Platform Standards and Reference Architectures

Runtime Environment Architecture represents the execution environment presented to software.

Physical compute belongs to Infrastructure.

Physical/Bare-Metal Runtime belongs to Platform.

Virtualisation infrastructure belongs to Infrastructure.

Virtual Machine Runtime belongs to Platform.

Do not create a separate Deployment Architecture merely to duplicate Container and Orchestration Architecture.

Do not create a Technology-level Middleware Architecture. Middleware systems are classified according to their Harmonia architectural role, with middleware architecture currently belonging under Application/System-of-Systems.

### Infrastructure Architecture

- Compute Architecture
  - Physical Compute
  - Virtualisation Infrastructure
  - specialised compute where required
- Network Architecture
  - Network Topology
  - Network Segmentation
  - Connectivity
  - Network Services
  - other existing network concerns
- Storage Architecture
  - Block Storage
  - File Storage
  - Object Storage
  - other storage concerns
- Device Architecture
  - only where actual architecture warrants it
- Infrastructure Standards and Reference Architectures
- Infrastructure Services
  - only where substantive architecture warrants it

Do not create empty architectural sections merely for symmetry.

Then:

- Unclassified Technology

## Part VI — Implementation & Migration

Target structure:

- Release Strategy
  - R1.x
    - architectural scope/target state
    - architectural transition material where applicable
  - R2.x
    - architectural scope/target state
    - architectural transition material where applicable
  - R3.x
    - as applicable
  - subsequent release families where they actually exist

- Unclassified Implementation & Migration

This Part should remain small.

Its purpose is to describe how the **architecture itself evolves through release families**.

Do not populate it with project-management or architecture-governance machinery such as:

- Codex/Junie tasks
- HCRLs
- CI/CD
- delivery schedules
- project milestones
- generic work packages
- Solution Build/Ingest/Release operational Praxis records

unless an item is genuinely architectural Implementation & Migration content.

# Continuum RAD Publication Structure

The generated Continuum RAD should have the broad reading structure:

- Front Matter
- Architecture Governance
- Part I — Motivation
- Part II — Strategy
- Part III — Business
- Part IV — Application
- Part V — Technology
- Part VI — Implementation & Migration
- Architectural Name Index
- Publication appendices only where genuinely required

The RAD should follow the remapped Continuum hierarchy closely.

Do not recreate the 1.1.8 behaviour where repository/model organisation mechanically became reader-facing document chapters.

The RAD may contain AI-assisted or generated explanatory prose and diagrams only where they are grounded in existing architecture.

The governing rule remains:

> **Document the Continuum. Do not improve the Continuum while documenting it.**

Do not use RAD generation as an opportunity to invent, complete, repair or redesign architecture.

If existing architecture is incomplete or its correct classification cannot be determined from authoritative evidence, retain that uncertainty and use the appropriate `Unclassified <Domain>` location where necessary.

# Constraints

- Preserve existing Canonical IDs.
- Preserve existing architectural semantics.
- Preserve existing ArchiMate relationships unless a structural/projection adjustment is necessary to represent the authorised taxonomy.
- Do not invent semantic containment relationships to reproduce directory structure.
- Preserve architectural standing.
- Do not turn historical/superseded material into a separate architecture merely because of standing.
- Do not manufacture missing architecture.
- Do not silently resolve architectural ambiguity.
- Do not create empty headings merely for visual symmetry.
- Do not re-establish Legacy-RAD as an operational dependency.
- Legacy-RAD remains historical/archaeological only.
- Do not modify historical evidence to make current results appear cleaner.
- Do not change Canonical IDs to reflect navigation numbering.
- Navigation numbers are derived location coordinates and may change when architectural location changes.
- Maintain deterministic/reproducible generation.
- Existing release and architectural validation mechanisms must continue to pass.
- Where an item cannot be classified confidently from current architectural authority, record the issue rather than guessing.

# Authorised Activity / Scope

You are authorised to:

- inspect the current Architecture Continuum and its existing organisational metadata;
- inspect current EA governance, tooling and publication machinery;
- restructure the current Continuum organisational/navigation hierarchy;
- move existing architectural elements within that hierarchy without changing their Canonical IDs;
- introduce the agreed taxonomy/navigation nodes necessary to implement the structure above;
- derive and assign hierarchical navigation numbers;
- update tooling necessary to generate and maintain the new organisation;
- update the Archi/Open Exchange projection so the agreed navigation organisation is exposed appropriately;
- update the Continuum Manifest generation;
- update the Continuum RAD generation and LaTeX structure;
- regenerate derived publications;
- add or update bounded tests necessary to protect the agreed taxonomy, identity preservation and deterministic generation;
- record genuine classification uncertainties for Solution Architect review.

You are **not** authorised to:

- redesign Harmonia architecture;
- invent missing architecture;
- resolve semantic architectural questions not required by this remap;
- alter Canonical IDs;
- create arbitrary ArchiMate relationships merely to represent navigation;
- reinterpret capability decomposition;
- convert Classes into Topics or Topics into Classes merely for structural convenience;
- rewrite historical evidence;
- introduce new architecture-factory concepts or Praxes;
- broaden this task into general repository cleanup.

# Execution Approach

First inspect the current Continuum population and map existing organisational nodes and architectural elements against the target taxonomy.

Where classification is clear from existing architectural semantics, perform the remap.

Where classification is not clear, preserve the architecture and place it in the relevant `Unclassified <Domain>` location or record it for review rather than guessing.

Do not ask the Solution Architect to manually classify thousands of straightforward elements that can be deterministically classified from existing governed architecture.

Conversely, do not use automated classification confidence as authority for genuinely ambiguous architectural meaning.

Before finalising, verify that the resulting hierarchy is sensible when viewed:

- in the canonical Continuum;
- through Archi/Open Exchange;
- through the Continuum Manifest; and
- as the Continuum RAD/PDF.

# Report

Create the formal Task evidence for release 1.1.10 in the appropriate governed:

`enterprise-architecture/design/evidence/1.1.10/<iteration>/before/ai-agent-tasks/`

and the execution Report under:

`enterprise-architecture/design/evidence/1.1.10/<iteration>/after/ai-agent-reports/`

If transient Junie locations are used, they remain working state only and are not the governed evidence.

The Report must include at least:

- summary of the remap performed;
- before/after organisational structure;
- number of existing elements remapped;
- confirmation of Canonical ID preservation;
- navigation-number generation approach;
- treatment of relationships versus hierarchy;
- treatment of architectural standing;
- classification rules actually applied;
- list/count of material placed into each `Unclassified <Domain>` branch;
- any genuinely ambiguous classifications requiring Solution Architect review;
- changes to Continuum tooling;
- changes to Archi/Open Exchange projection;
- changes to Manifest generation;
- changes to Continuum RAD/LaTeX generation;
- resulting RAD/PDF structure and size;
- validation/test results;
- reproducibility results;
- confirmation that Legacy-RAD is not an operational dependency;
- confirmation that no architecture was invented solely for publication;
- any proposed HCRL dispositions required to accept 1.1.10.

Do not mark architectural ambiguities as resolved unless they were already resolvable from authoritative current architecture.

---

Attached reference: `enterprise-architecture/design/review/1.1.9/hcrl-1.1.9-001.pending.md`.


## Completion follow-up — 2026-10-23 interaction date

Follow-up Task-Issued-At: UNRESOLVED

Mark Hunter, Solution Architect, supplied the following operative instruction,
linked to the updated [HCRL 1.1.10-001](../../../../../review/1.1.10/hcrl-1.1.10-001.pending.md):

> hcrl-1.1.10-001.pending.md updated. Complete!

Attachment: `enterprise-architecture/design/review/1.1.10/hcrl-1.1.10-001.pending.md`.
The exact reviewed pending input has SHA-256 `2eadf2bd67d2225317dee62646c96a63ba04c7c49f3b62779384418d6ec152c4`.
DR-1–DR-4 are Accept. DR-2 accepts explicit classification deferrals and absent
architecture; DR-3 accepts the documented projection/inspection limits; DR-4
accepts the unavailable original Task event/prefix as an acknowledged provenance
limitation that does not prevent closure. This instruction confirms the
incorporated result and directs completion within the same 1.1.10 change set.
Its instruction-entry timestamp is also unavailable and is not inferred from
the current clock or filesystem/commit metadata. No different transition's
temporal treatment is extended here.

Completion includes current register/acceptance navigation and the lifecycle-only
refresh of G.3.2's governed-source link and description after confirmation. Its
coordinates, taxonomy and architectural elements remain unchanged. The reviewed
navigation and PDF are retained as bounded completion inputs; original execution
evidence remains attributable to the reviewed result.
