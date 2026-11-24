# DesignNote — Mneme Context and Pragma Management

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05 Candidate Architecture Step A  
**Subject:** Mneme Context Management, Pragma Management, Context lifecycle, Adhoc access, and Resource Harmonisation Management

## 1. Purpose

This DesignNote records the current architectural position for **Mneme**.

Mneme is not principally an information repository and does not exist merely to provide generic CRUD access to persisted resources.

Mneme manages the **information context of Harmonia activity**, principally through **Pragma** and **Context**. It provides distributed, concurrent, managed, assured, regulated and controlled seamless access to and update of Context within the Harmonia framework, while managing the information consequences and outputs of Harmonia activity.

## 2. Fundamental Responsibility

> **Mneme manages the information context of Harmonia activity — principally through Pragma and Context — providing controlled, concurrent and seamless access to existing resource values and management of the new Content produced by that activity.**

Conceptually:

```text
                         MNEME
            Context and Pragma Management
                           |
          +----------------+----------------+
          |                                 |
          v                                 v
       Pragma                            Context
       Management                       Management
          |                                 |
     Input / Output                 Load / Save lifecycle
          |                                 |
          +----------------+----------------+
                           |
                           v
                    Harmonia Activity
```

Mneme's role is deliberately different from Mnemosyne.

> **Mneme manages the information context of Harmonia activity. Mnemosyne manages and authoritatively reports persistence.**

## 3. Principal Exposed Mneme Services

Mneme exposes four principal services:

```text
Mneme
|
+-- Context Load
|
+-- Context Save
|
+-- Adhoc Resource Access
|
`-- Adhoc Search
```

These four services form the principal Mneme-facing information interaction surface.

Other Mneme responsibilities described in this note are supporting/internal responsibilities and SHALL NOT automatically be interpreted as additional exposed services.

## 4. Context

A **Context** is a versioned, temporal load-snapshot composed from resource **values**, not merely references.

> **At Context Load, a Context is a versioned temporal snapshot composed from the authoritative resource values materialised for a Harmonia activity's anticipated information requirements.**

Each loaded resource value represents a defined authoritative resource state/version at the point at which it is incorporated into the Context.

Conceptually:

```text
Context
|
+-- Context Identity
+-- Context Version
+-- Temporal Load Basis
+-- Authoritative State/Basis
|
`-- Resource Bucket                 [COMPOSITION]
      |
      +-- Resource Value A @ authoritative state/version
      +-- Resource Value B @ authoritative state/version
      +-- Resource Value C @ authoritative state/version
      `-- ...
```

The resource bucket is a **composition**, not an aggregation.

The authoritative resource itself is not existentially owned by the Context. Rather, the **loaded resource value** forms part of the Context snapshot.

```text
Authoritative Resource A
        |
        | load
        v
Resource A Value @ State/Version
        |
        | composed into
        v
     Context
```

A Context does not imply that the entire distributed Harmonia information environment was transactionally frozen at one universal instant. It records the authoritative states/versions actually materialised into that Context.

Context version and constituent resource versions are distinct concepts.

## 5. Context as the Ergon Working Information Environment

Context exists so that Ergon Business Logic can primarily **read and act upon values**, rather than concern itself with the mechanics required to locate, retrieve and materialise those values.

```text
Ergo Definition
      |
      | identifies expected resource set
      v
Mneme Context Load
      |
      | locate / acquire / map /
      | version / compose / control
      v
Loaded Context
      |
      v
Ergon Business Logic
      |
      | read values
      | act upon values
      | produce outputs
      v
Resulting activity state
```

The preferred developer experience is:

> Declare what the Ergo is most likely to require; when the Ergon executes, those values are already available within its Context.

The loaded Context is the Ergon's **starting managed information environment**, not a closed universe of everything the Ergon may legitimately access or produce.

## 6. Ergo Definition and Expected Resource Set

During Ergo development/definition, the Ergo identifies the resource set it is **most likely to access**.

This declaration enables the Ergo framework, working with Mneme, to establish the execution Context efficiently.

```text
DEFINITION

Ergo
  |
  | identifies
  v
Expected Resource Set
  |
  v
Ergo Definition


RUNTIME

Ergo Framework
  |
  | Context Load request
  v
Mneme
  |
  | materialises anticipated values
  v
Context
  |
  v
Ergon
```

The expected resource set is intentionally **not required to be exhaustive**.

Attempting to preload every resource an Ergon could conceivably access would undermine the performance purpose of Context.

## 7. Context Load — Preferred and Efficient Path

**Context Load is the normal and performance-optimised information-access path.**

Mneme SHALL support efficient materialisation of the anticipated working resource set declared by the Ergo definition.

Context Load may require substantial internal activity, including resource location, retrieval, mapping, version/state determination, control application and composition of values into the Context bucket.

These mechanics SHALL be substantially insulated from Ergon Business Logic.

```text
Expected Resource Set
        |
        v
    Context Load
        |
        +-- locate
        +-- retrieve
        +-- map
        +-- establish state/version
        +-- apply required controls
        `-- compose values
                |
                v
             Context
```

The architectural requirement is efficient Context establishment; this note does not prescribe caching, prefetching, batching, replication, locality, distributed-cache technology or other implementation mechanisms.

## 8. Context Loader and Context Unloader

The **Ergo Context Loader** is distinct from **Mneme Context Load**.

By the time the Ergo Context Loader participates, Mneme has already performed the information acquisition required to establish the Context.

```text
Mneme Context Load
    = materialise the managed Context

Ergo Context Loader
    = expose/make the already-loaded Context
      available to Ergon Business Logic
```

Likewise, the **Context Unloader** is not a persistence mechanism.

It brings the result of bounded Ergon processing back across the business-logic boundary so that Mneme can continue managing the resulting Context and Pragma information consequences.

```text
Context Loader
      |
      v
Ergon Business Logic
      |
      v
Context Unloader
      |
      v
Mneme Context Save / Pragma Management
```

An Ergon SHALL NOT be required to understand persistence mechanics merely because its activity changes Context values or produces new Content.

## 9. Adhoc Resource Access and Adhoc Search

The loaded Context is the preferred information-access mechanism, but it is not a closed-world constraint.

An Ergon may legitimately require information that could not reasonably have been anticipated at definition time.

Mneme therefore exposes:

- **Adhoc Resource Access**
- **Adhoc Search**

These services support cases including:

1. interaction with external systems where information may arrive or be discovered dynamically;
2. deep interrogation of state outside known or reasonably pre-loadable boundaries; and
3. activities whose information requirements emerge from runtime processing.

```text
Ergon
 |
 +-- Loaded Context -------------> preferred / efficient
 |
 +-- Adhoc Resource Access ------> supported / potentially expensive
 |
 `-- Adhoc Search ---------------> supported / potentially expensive
```

Adhoc access may be materially more expensive than Context Load.

It is therefore supplementary, not an equivalent substitute for good Ergo Context definition.

> **Harmonia optimises the expected case without constraining the legitimate exceptional case.**

Repeated predictable Adhoc access may indicate that an Ergo's expected resource-set definition should be reconsidered, but Adhoc access is not itself an architectural error.

## 10. Pragma Management

Mneme manages **Pragma**.

Pragma provides the information/task envelope through which Harmonia activity receives inputs and expresses outputs.

```text
                         Pragma
                +----------+----------+
                |                     |
             Input(s)              Output(s)
                |                     ^
                v                     |
              Mneme                   |
                |                     |
           Context Load               |
                |                     |
                v                     |
             Context                  |
                |                     |
                v                     |
              Ergon ------------------+
```

Context and Pragma are related but distinct.

Context is the managed, value-based working information environment for the activity.

Pragma expresses the activity information envelope, including its inputs and outputs.

## 11. New Content and Pragma Output

An Ergon may:

1. read resource values from its Context;
2. act upon/change values within its working Context; and
3. generate **new Content**.

New Content generated by an Ergon is expressed as **Pragma Output**.

```text
READ

Context.ResourceValue
        |
        v
      Ergon


ACT UPON

Context.ResourceValue
        |
        v
      Ergon
        |
        v
Changed Working Value


PRODUCE

Context + Business Logic
        |
        v
      Ergon
        |
        v
   Pragma.Output
        |
        v
    New Content
```

New Content is therefore not merely an incidental mutation of the loaded Context.

> **Context represents the value-based working information environment provided to an Ergon. New Content produced by an Ergon is expressed as Pragma Output.**

Mneme manages that output and its subsequent information consequences.

This is particularly important where an Ergon:

- receives or produces information through interaction with an external system;
- performs deep runtime interrogation; or
- generates genuinely new Content as the result of its bounded activity.

## 12. Context Save

Context Save is the principal Mneme service through which the resulting Context state is returned to Mneme management.

Context Save is not equivalent to "write this database record".

Mneme determines the appropriate subsequent information handling.

```text
Ergon
  |
  v
Context Unloader
  |
  +-- resulting Context state
  `-- Pragma Output(s)
             |
             v
           Mneme
             |
       information handling
             |
       +-----+------+
       |            |
       v            v
   normal path   unhappy path
       |            |
       v            v
   persistence    Resource
   as required    Harmonisation
                  Management
```

The Ergon is not responsible for database selection, shard selection, persistence transactions, persistence tagging, persistence indexing or other persistence mechanics.

## 13. Concurrency

Mneme operates in a distributed and concurrent environment.

A Context is a temporal working snapshot. The authoritative state of constituent resources may change while an Ergon is operating.

Mneme therefore requires sufficient state/version information to recognise when resulting resource delivery cannot safely proceed through the normal path.

However:

> **Managing concurrent Contexts does not require the originating Ergon to resolve generic resource conflicts.**

The originating Ergon performed its bounded business activity against the Context supplied to it.

Where conflicting, discordant, malformed or otherwise unsuitable resource state requires further work, that condition is managed through the Resource Harmonisation Manager and appropriate specialised Ergon activity.

Detailed concurrency algorithms, locking models, MVCC mechanisms, merge semantics and persistence transaction mechanisms remain unresolved.

## 14. Resource Harmonisation Manager

Mneme contains an internal **Resource Harmonisation Manager (RHM)**.

The RHM manages the exceptional resource-delivery path where resulting information cannot safely or appropriately proceed through normal handling.

```text
                  Mneme
                    |
              Resource Delivery
                    |
          +---------+---------+
          |                   |
       ~95%+                <= ~5%
    normal path          unhappy path
          |                   |
          v                   v
      Mnemosyne          Resource
                        Harmonisation
                          Manager
                             |
                             v
                       appropriate
                    Harmonisation Ergon
```

The percentages represent a design expectation rather than a hard SLA: Resource Harmonisation is expected to be an exceptional path, approximately **5% or less at worst**, rather than the normal execution path.

The normal path SHALL NOT be unnecessarily burdened with expensive harmonisation machinery solely because the exceptional path exists.

## 15. Resource Harmonisation Responsibility

The Resource Harmonisation Manager does **not** itself perform domain harmonisation.

It identifies and manages conditions that cannot proceed normally and causes the appropriate bounded harmonisation activity to occur.

```text
Problematic Resource Delivery
            |
            v
Resource Harmonisation Manager
            |
            +-- characterise condition
            +-- preserve relevant state/context
            +-- identify required activity
            `-- initiate appropriate work
                         |
                         v
                 Harmonisation Ergon
                         |
                         v
                  resulting state
                         |
                         v
                 Resource Delivery
```

Harmonisation may be required for conflicting work, poor/discordant data or other exceptional information states.

The exact harmonisation categories remain deliberately unresolved.

## 16. Reuse of Ponos and Ergo

Resource Harmonisation may require workflow, human interaction, audit, assurance and common Harmonia execution capabilities.

The RHM SHALL NOT duplicate or embed a second execution framework within Mneme.

Actual harmonisation activity is undertaken through appropriate, explicitly defined Ergon executed using the common Ponos execution capability.

```text
Resource Harmonisation Manager
             |
             | establishes required work
             v
      Harmonia Execution Contract
             |
             v
           Ponos
             |
             v
     Harmonisation Ergon
```

The cross-component dependency SHALL remain narrow and architectural rather than coupling Mneme to Ponos implementation internals.

A reusable **Ergo Library** may provide common Harmonia activity patterns and integration for workflow, context, audit, assurance and other recurring execution concerns.

Detailed UI architecture, reusable forms and presentation mechanisms are explicitly outside the scope of this DesignNote.

## 17. Harmonisation as New Work

Harmonisation SHALL NOT be implemented by resuming inside the originating Ergon.

Where Resource Harmonisation is required, it represents new governed work.

```text
Original FulfillmentTask
        |
        v
   Original Ergon
        |
        v
   Context Save
        |
        X cannot proceed normally
        |
        v
Resource Harmonisation Manager
        |
        v
Harmonisation ActionableTask
        |
        v
New FulfillmentTask
        |
        v
Harmonisation Ergon
```

This preserves the existing Harmonia rule that execution does not resume inside an Ergon.

Terminal/escalation handling for pathological repeated harmonisation remains unresolved.

## 18. Mneme / Mnemosyne Boundary

Mneme and Mnemosyne have complementary but distinct responsibilities.

```text
Mneme
  Context and Pragma Management
  |
  | persistence/search requirement
  v
Mnemosyne
  Persistence Manager
  |
  v
Persistent Services
```

Mneme manages Context, Pragma, resource interaction and resulting information consequences.

Mnemosyne manages authoritative persistence, persistence-service oversight, shard management, persistence search execution, persistence-boundary compartment validation/reapplication, persistence logging and authoritative persistence reporting.

Mneme SHALL NOT require Ergon Business Logic to understand those persistence mechanics.

## 19. Architectural Guardrails

1. **Mneme manages the information context of Harmonia activity, principally through Pragma and Context.**
2. Mneme's principal exposed services are Context Load, Context Save, Adhoc Resource Access and Adhoc Search.
3. A Context is a versioned temporal load-snapshot composed from resource values rather than merely references.
4. Loaded resource values are composed into the Context bucket.
5. Context version and constituent resource authoritative state/version are distinct.
6. An Ergo definition identifies the resource set it is most likely to access.
7. Context Load SHALL be the preferred and performance-optimised mechanism for anticipated information access.
8. The expected resource set need not be exhaustive.
9. Ergon Business Logic SHOULD primarily read and act upon values already provided in its Context.
10. Ergon Business Logic MAY use Adhoc Resource Access or Adhoc Search where its bounded activity requires information outside the loaded Context.
11. Adhoc Resource Access and Adhoc Search may be materially more expensive than Context Load.
12. The Ergo Context Loader is not responsible for performing Mneme's resource acquisition mechanics.
13. The Context Unloader is not a persistence mechanism.
14. Mneme manages Pragma.
15. New Content generated by an Ergon is expressed as Pragma Output.
16. Changed Context values and newly generated Pragma Outputs SHALL remain semantically distinguishable.
17. Mneme manages the subsequent information consequences of Pragma Output.
18. An Ergon SHALL NOT be required to understand persistence mechanics merely because it changes values or produces Content.
19. Mneme SHALL support distributed and concurrent Context operation.
20. Generic resource conflict/harmonisation SHALL NOT be pushed into the originating Ergon.
21. Resource Harmonisation Manager is an internal Mneme exception-path capability.
22. Resource Harmonisation is expected to represent approximately 5% or less of resource-delivery activity at worst; this is a design expectation, not a hard SLA.
23. The normal resource-delivery path SHALL NOT be unnecessarily burdened by exceptional harmonisation processing.
24. Resource Harmonisation Manager manages the unhappy path but does not itself perform domain harmonisation.
25. Required harmonisation SHALL be performed by an appropriate, explicitly defined Ergon.
26. Harmonisation is new governed work and SHALL NOT resume inside the originating Ergon.
27. Resource Harmonisation Manager SHALL reuse common Harmonia/Ponos execution capability rather than duplicating execution-framework responsibilities.
28. Mneme SHALL remain insulated from Ponos implementation internals through an appropriate architectural execution/work contract.
29. Mneme manages Context and Pragma; Mnemosyne manages and authoritatively reports persistence.
30. Detailed UI, reusable-form and presentation architecture is deferred.

## 20. Deliberately Unresolved

This DesignNote does not determine:

- physical Context representation;
- Context identity/version implementation;
- exact Context bucket schema;
- detailed Information Unit to Context mapping;
- exact resource/data mapping framework;
- Context serialisation;
- Context caching;
- Context distribution/replication implementation;
- prefetch/batching strategy;
- detailed concurrency algorithm;
- locking/MVCC implementation;
- conflict-detection algorithm;
- Resource Harmonisation categories;
- Harmonisation Ergon catalogue;
- terminal/escalation handling for repeated harmonisation;
- detailed Ergo Library structure;
- human-interaction/UI implementation;
- reusable forms;
- physical persistence mechanisms;
- shard technology;
- detailed Adhoc Search query model;
- detailed Adhoc Resource Access API;
- exact Pragma Output representation;
- downstream routing of newly generated Content.

## 21. Summary

Mneme is Harmonia's **Context and Pragma Manager**.

It provides four principal exposed services: **Context Load, Context Save, Adhoc Resource Access and Adhoc Search**.

Context Load efficiently materialises a versioned, temporal, value-based working snapshot based principally on the resource set anticipated by the Ergo definition. Ergon Business Logic can then read and act upon those values without normally concerning itself with resource-loading or persistence mechanics.

Where runtime activity requires information outside the anticipated Context, Adhoc Resource Access and Adhoc Search provide deliberate, potentially more expensive escape paths.

Mneme also manages Pragma. New Content produced by an Ergon is expressed as **Pragma Output** and managed by Mneme rather than being treated merely as an incidental Context mutation.

Normal resource delivery is designed to remain efficient. Exceptional resource conditions are managed internally by the **Resource Harmonisation Manager**, expected to be required for approximately 5% or less of activity at worst. Actual harmonisation is undertaken as new governed work through appropriate Harmonisation Ergon using the common Harmonia execution framework.

> **Mneme makes a distributed, concurrent and controlled information environment available to Harmonia activity as manageable Context, while managing the Pragma inputs and outputs through which that activity consumes and produces information.**
