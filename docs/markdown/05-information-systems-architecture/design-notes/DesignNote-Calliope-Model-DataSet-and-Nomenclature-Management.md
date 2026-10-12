# DesignNote --- Calliope Model, DataSet and Nomenclature Management

## Status

**Architecturally adjudicated working DesignNote**

This DesignNote records the current architectural intent for Calliope
within the Harmonia Information Systems Layer. It is intended to support
subsequent consolidation into the Reference Architecture Documentation
Set (RADS).

This note does not constitute Domain05 Step A acceptance and does not
begin capability/information traceability.

------------------------------------------------------------------------

## 1. Purpose

Calliope is Harmonia's component for the management of foundational
models and governed definition/data sets used throughout the Harmonia
platform.

Calliope is deliberately divided into two discrete sub-modules with
different lifecycle, authority and dependency characteristics:

1.  **Calliope-Models** --- a design/build-time library essential to the
    development and operation of most Harmonia components.
2.  **Calliope-DataSets** --- a runtime repository and management
    capability for governed mutable datasets whose authoritative content
    may change while Harmonia is operating.

The distinction is fundamental.

Calliope-Models establishes the software-visible structures, definitions
and development representations through which Harmonia software
understands and accesses governed concepts.

Calliope-DataSets establishes and manages the authoritative runtime
content of mutable datasets governed through those structures.

------------------------------------------------------------------------

## 2. Architectural Decomposition

``` text
                         CALLIOPE
                            │
             ┌──────────────┴──────────────┐
             │                             │
             ▼                             ▼
      Calliope-Models              Calliope-DataSets
      DESIGN / BUILD TIME             RUN TIME
             │                             │
             │                             │
   Foundational models             Value-Sets
   Metadata Types                  Code-Sets
   Criteria Definitions            Structured Metadata
   Topics                          Mutable governed datasets
   Filter Types                    Reviewed ontology-derived
   Filter Elements                   content / sets
   Core Harmonia Elements
             │                             │
             ▼                             ▼
      DevOps lifecycle           Operational governance
```

The two modules collaborate but SHALL NOT be treated as having a single
lifecycle.

------------------------------------------------------------------------

## 3. Calliope-Models

### 3.1 Responsibility

**Calliope-Models is Harmonia's design/build-time library of core
models, metadata types, Criteria definitions, Topic definitions, Filter
types and elements, and other foundational reusable Harmonia model
elements.**

Calliope-Models provides the structural definitions and software
representations required by Harmonia components.

Its lifecycle is governed through the software engineering and DevOps
lifecycle:

``` text
Design → Code → Build → Test → Release → Deploy
```

Calliope-Models is not a runtime administration mechanism for changing
software models.

### 3.2 Foundational Dependency

Calliope-Models is a foundational shared software dependency used by
much of Harmonia.

Conceptually:

``` text
Calliope-Models
      │
      ├────► Mneme
      ├────► Mnemosyne
      ├────► Petasos
      ├────► Energeia / Ponos
      ├────► Themis
      ├────► Kleio
      └────► other Harmonia components
```

The exact physical packaging and dependency mechanism remain
implementation concerns.

### 3.3 Criteria, Topics and Filters

Criteria definitions, Topics, Filter types and Filter elements are
explicitly **design/build-time Calliope-Models concerns**.

Their definitions are not runtime-modifiable Calliope-DataSets merely
because instances or uses of those definitions participate in runtime
processing.

Examples of the distinction include:

``` text
Calliope-Models
      │
      └── Topic Definition
               │
               ▼
        Topic metadata used
        by runtime information
```

``` text
Calliope-Models
      │
      └── Criteria Definition
               │
               ▼
        Criteria used by Themis
        and PEP-Context activity
```

``` text
Calliope-Models
      │
      └── Filter Type / Elements
               │
               ▼
        Publishing / Subscription
        Pattern definitions and use
```

Definition ownership does not confer runtime operational ownership.

------------------------------------------------------------------------

## 4. Calliope-DataSets

### 4.1 Responsibility

**Calliope-DataSets is Harmonia's runtime repository and management
capability for governed mutable datasets, including ValueSets, CodeSets
and structured metadata whose authoritative content may change while
Harmonia is operating.**

These datasets are code-supported but runtime authoritative.

### 4.2 Runtime Governance

Calliope-DataSets may support governed activities including:

-   update request;
-   review;
-   approval;
-   authoritative update;
-   update enforcement;
-   tagging and classification;
-   authoritative review of applicable ontology sets or ontology-derived
    content; and
-   making approved content available for runtime use.

The exact lifecycle/state machine is not established by this DesignNote.

Conceptually:

``` text
Current Dataset
      │
      ▼
Update Request
      │
      ▼
Review / Assessment
      │
      ▼
Approval
      │
      ▼
Authoritative Update
      │
      ▼
Runtime Availability
```

Tagging, ontology review and other governance activities may participate
where applicable.

### 4.3 Workflow and Human Interaction

Calliope-DataSets owns the application responsibility for the governed
lifecycle of its mutable datasets.

It does not follow that Calliope must implement a separate workflow
engine or user-interface framework.

Calliope-DataSets is expected to reuse common Harmonia capabilities for
execution/workflow and human interaction.

In particular, it is heavily dependent upon:

-   **Mneme** for Context, Pragma and governed runtime information
    interaction;
-   **Mnemosyne** for authoritative persistence;
-   **Ponos / Ergo / Ergon** for execution of governed management
    workflows; and
-   a Harmonia human-interaction capability for activities requiring
    review, approval or other human action.

Detailed Iris, reusable-form and user-interface architecture is
explicitly deferred.

------------------------------------------------------------------------

## 5. Build-Time Knowledge and Runtime Authority

Harmonia development must not require developers to abandon normal
code-assisted programming merely because a ValueSet, CodeSet or DataSet
is runtime mutable.

Calliope therefore deliberately supports both:

1.  rich development-time knowledge of known datasets and values; and
2.  runtime authoritative resolution of mutable dataset content.

Conceptually:

``` text
              Calliope-Models
                    │
        development representation
                    │
          ┌─────────┴─────────┐
          ▼                   ▼
     IDE assistance       AI assistance
     type safety          model knowledge
     autocomplete         semantic context
     documentation        code generation
          │                   │
          └─────────┬─────────┘
                    ▼
               Developer
                    │
                    ▼
            Ergon / Behaviour
                    │
                    │ stable code-supported
                    │ identity / contract
                    ▼
             ValueSet / CodeSet
                    │
                    │ runtime resolution
                    ▼
           Calliope-DataSets
                    │
                    ▼
       Authoritative Runtime Content
```

### 5.1 Development-Time Representation

For known runtime-managed DataSets, CodeSets and ValueSets,
Calliope-Models should provide development representations sufficient to
support efficient:

-   human software development;
-   IDE code assistance and completion;
-   static analysis where appropriate;
-   documentation;
-   testing;
-   AI-assisted development;
-   AI code generation and reasoning about Harmonia software.

Such representations may expose, as appropriate:

-   stable identity;
-   type;
-   intended meaning and usage;
-   metadata;
-   known build-time content;
-   constraints;
-   relationships;
-   runtime mutability; and
-   the mechanism/contract through which runtime authoritative content
    is resolved.

### 5.2 Runtime Mutability

Build-time knowledge is not equivalent to runtime authority.

A dataset may contain values known when software is built while
subsequently gaining, losing or changing governed runtime content.

Software built against an earlier representation SHALL NOT automatically
treat a runtime value as invalid merely because that value was unknown
at build time.

Conversely, software written before a new value existed cannot be
expected to contain explicit business behaviour written specifically for
that later value.

### 5.3 Architectural Rule

> **Build-time Calliope knowledge assists software development; runtime
> Calliope authority determines current governed dataset content and
> membership.**

And:

> **Calliope SHALL provide development-time representations of known
> runtime-managed DataSets, CodeSets and ValueSets sufficient to support
> efficient human, IDE and AI-assisted software development, while
> preserving runtime resolution against authoritative Calliope-DataSets
> state.**

Development-time representation SHALL NOT require developers to
sacrifice normal type-safe/code-assisted programming merely because the
represented dataset is runtime mutable.

Development-time knowledge SHALL NOT be treated as authoritative
evidence of current runtime membership or content.

------------------------------------------------------------------------

## 6. Managing the Apparent Circular Dependency

Calliope-DataSets has significant runtime dependencies upon Mneme,
Mnemosyne, Ponos and human-interaction capability.

Those components may themselves use datasets governed by
Calliope-DataSets.

This creates an apparent circular dependency.

The architectural response is not to eliminate collaboration but to
separate **foundational structural dependency** from **runtime
operational collaboration**.

``` text
FOUNDATIONAL / STATIC

Calliope-Models
       ↓
core Harmonia components
Mneme / Ponos / etc.


RUNTIME / DYNAMIC

Calliope-DataSets
       ↓ ↑
Mneme / Ponos / human interaction
       ↓
Mnemosyne
```

The structural contracts necessary to access and manage
Calliope-DataSets are established through Calliope-Models and the
software build/deployment lifecycle.

Runtime Calliope-DataSets provides the authoritative mutable content
governed by those contracts.

### 6.1 Bootstrap Guardrail

> **The structural contracts necessary to access and manage
> Calliope-DataSets SHALL be established through Calliope-Models and the
> software build/deployment lifecycle. Runtime Calliope-DataSets SHALL
> provide the authoritative mutable content governed by those contracts
> and SHALL NOT be required to establish the structural contracts
> necessary for its own access.**

This prevents a runtime bootstrap dependency in which Harmonia must
successfully query Calliope-DataSets merely to understand how to access
Calliope-DataSets.

------------------------------------------------------------------------

## 7. Persistence Boundary

Calliope-Models and Calliope-DataSets do not share the same persistence
characteristics.

``` text
Calliope-Models
      ↓
software build / packaging / deployment


Calliope-DataSets
      ↓
runtime managed datasets
      ↓
Mneme
      ↓
Mnemosyne
      ↓
authoritative persistence
```

Calliope-DataSets owns the management responsibility for its governed
datasets.

It does not thereby become Harmonia's persistence manager.

Mnemosyne retains authoritative persistence responsibility.

The exact interaction with Mneme Context and Pragma management remains
subject to the established Mneme architecture.

------------------------------------------------------------------------

## 8. Security and Assurance Nomenclature

### 8.1 Fundamental Responsibility

Calliope is explicitly responsible for Harmonia's **security and
assurance nomenclature**.

This is a first-class architectural responsibility and SHALL NOT be
reduced to the incidental storage of security-related ValueSets.

> **Calliope is responsible for the definition, governance and
> authoritative representation of the shared nomenclature used by
> Harmonia to express security, privacy, policy, audit and assurance
> concepts.**

This nomenclature provides a common governed language across Harmonia.

Conceptually:

``` text
                    CALLIOPE
                       │
              defines / governs
                       ▼
        Security & Assurance Nomenclature
                       │
          ┌────────────┼────────────┐
          ▼            ▼            ▼
       Terms        Code-Sets    Value-Sets
       Types        Metadata     Classifications
          │            │            │
          └────────────┼────────────┘
                       ▼
             shared nomenclature
                       │
        ┌──────────────┼──────────────┐
        ▼              ▼              ▼
      Themis        Dokimasia       Kleio
     security       assurance        audit
     policy         assessment      evidence
```

Other Harmonia components may also consume this nomenclature.

### 8.2 Models and DataSets

Security and assurance nomenclature may span both Calliope sub-modules.

Structural nomenclature belongs in **Calliope-Models**.

Examples may include structural types or definitions for:

-   security classification;
-   assurance findings;
-   Criteria;
-   compartment metadata;
-   decision/reason representation; and
-   other security/assurance model elements.

Mutable controlled vocabularies belong in **Calliope-DataSets**.

Examples may include governed runtime:

-   security classification ValueSets;
-   compartment CodeSets;
-   assurance finding categories;
-   disclosure-purpose CodeSets;
-   assurance severity ValueSets; and
-   other mutable security/assurance nomenclature.

The examples above are illustrative and do not establish a final
nomenclature catalogue.

### 8.3 Responsibility Boundaries

Calliope's responsibility for nomenclature does **not** confer
responsibility for the operational functions that use that nomenclature.

In particular:

-   **Themis** retains responsibility for the Authorisation Model,
    policy reasoning and policy enforcement.
-   **Dokimasia** retains responsibility for assurance activity and
    assessment.
-   **Kleio** retains responsibility for Audit Information Management.
-   other Harmonia components retain their established responsibilities.

The distinction is:

``` text
Calliope
    establishes and governs
    the common language

Themis
    applies security policy
    using that language

Dokimasia
    performs assurance
    using that language

Kleio
    manages authoritative audit
    information expressed using
    that language
```

### 8.4 Architectural Guardrail

> **Responsibility for security and assurance nomenclature does not
> confer responsibility for security policy determination, policy
> enforcement, audit management or assurance assessment. Those
> responsibilities remain with their respective Harmonia components.**

------------------------------------------------------------------------

## 9. Ontology Review

Calliope has responsibility for authoritative review of ontology sets
applicable to Harmonia use.

This does not imply that Harmonia becomes the originating authority for
externally governed ontologies or terminology systems.

Calliope may assess externally governed material and establish its
accepted, constrained or otherwise governed use within Harmonia.

The detailed ontology-review lifecycle, terminology authority
relationships, versioning rules and update mechanisms remain unresolved.

------------------------------------------------------------------------

## 10. Current Architectural Boundaries

The current Calliope architecture establishes the following principal
separation:

``` text
Calliope-Models
    "What kind of thing is this,
     how is it structurally represented,
     and how does Harmonia software
     work with it?"

Calliope-DataSets
    "What is the currently governed
     runtime content of this mutable
     dataset?"
```

Calliope as a whole provides Harmonia with a coherent relationship
between:

-   foundational software models;
-   code-supported development representations;
-   runtime mutable governed datasets;
-   common security and assurance nomenclature; and
-   the authoritative runtime content required by Harmonia activities.

------------------------------------------------------------------------

## 11. Explicitly Unresolved

This DesignNote deliberately does not establish:

-   the exact Calliope-DataSets lifecycle/state machine;
-   detailed dataset version/effective-date semantics;
-   historical-version retention rules;
-   exact runtime API shapes;
-   exact generated-code representation;
-   exact IDE integration;
-   exact AI development-tool integration;
-   physical packaging of Calliope-Models;
-   physical datastore or persistence technology;
-   detailed ontology-review workflow;
-   detailed terminology/ontology synchronisation;
-   exact UI responsibility or reusable-form architecture;
-   detailed Ponos/Ergo workflows for dataset governance;
-   exact caching strategy;
-   exact interaction between historical Context and later dataset
    versions;
-   the final catalogue of security and assurance nomenclature.

These remain matters for subsequent architectural adjudication or
implementation design as appropriate.

------------------------------------------------------------------------

## 12. Summary

Calliope consists of two discrete but complementary sub-modules.

**Calliope-Models** is the foundational design/build-time library
through which Harmonia software understands models, metadata types,
Criteria, Topics, Filters and other core Harmonia definitions. It also
provides code-supported representations of known runtime-managed
datasets to enable effective human, IDE and AI-assisted development.

**Calliope-DataSets** is the runtime management capability for mutable
governed ValueSets, CodeSets, structured metadata and related datasets.
Its authoritative content may change while Harmonia is operating and is
managed through governed runtime processes using common Harmonia
Context, persistence, execution and human-interaction capabilities.

Calliope is additionally and explicitly responsible for Harmonia's
**security and assurance nomenclature**, providing the common governed
language used by Themis, Dokimasia, Kleio and other Harmonia components
without assuming those components' policy, assurance or audit
responsibilities.

The central architectural principle is:

> **Build-time Calliope knowledge assists software development; runtime
> Calliope authority determines current governed dataset content and
> membership.**
