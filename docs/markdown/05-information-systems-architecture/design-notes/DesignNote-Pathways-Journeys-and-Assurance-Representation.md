# DesignNote — Pathways, Journeys and Assurance Representation

**Status:** Working Design Note  
**Scope:** Harmonia Information Systems Layer Architecture — candidate design discussion  
**Authority:** Conversational design input only. This note is not a RADS baseline and does not supersede canonical architecture.  
**Date:** 2026-10-11

## 1. Purpose

This DesignNote captures the current architectural reasoning for how information and activity pass through Harmonia, how defined routing differs from publish/subscribe processing, and how both are represented consistently for monitoring, reporting and assurance.

The central distinction is between:

- **Pathway** — a defined mechanism for controlling/routing information passage;
- **Publish/Subscribe** — dynamic, non-Pathway-directed processing;
- **Journey** — the governed observational representation of what actually occurred, regardless of the mechanism that caused it.

Journey is intended to become the fundamental activity representation consumed by Harmonia assurance capabilities, including AI-based assurance activity.

---

## 2. Two Primary Information-Passage Mechanisms

Information passes through the general Harmonia framework through two primary mechanisms:

1. **Defined Pathways**
2. **Publish/Subscribe processing**

These mechanisms differ in how progression is controlled, but both ultimately produce observable activity that can be represented as a Journey.

```text
                    Harmonia Activity
                           |
              +------------+------------+
              |                         |
              v                         v
       Defined Pathway              Publish/Subscribe
       controlled routing           dynamic processing
              |                         |
              +------------+------------+
                           |
                           v
                        Journey
                           |
                    +------+------+
                    |             |
                    v             v
                 Reporting     Assurance
```

---

## 3. PathwayEndpoint

**PathwayEndpoint** is an interface boundary through which information enters or leaves a Harmonia component.

Conceptually:

> **PathwayEndpoint ≈ ArchiMate ApplicationInterface**

Every Ergon exposes two PathwayEndpoints:

- `IngressEndpoint`
- `EgressEndpoint`

Conceptually:

```text
                         Ergon
             +---------------------------+
             |                           |
     IngressEndpoint              EgressEndpoint
     ApplicationInterface         ApplicationInterface
             |                           |
             +---- Ergon activity -------+
```

The endpoints form stable interaction surfaces. They do not expose the internal processing performed by the Ergon.

---

## 4. PathwayElement

**PathwayElement** is the general representation of flow of information/activity between or across Harmonia component boundaries.

It provides a common abstraction for visualisation, reporting, observability and assurance without forcing routing/control semantics onto every observed movement.

Two specialisations are currently identified:

```text
                     PathwayElement
                           |
              +------------+------------+
              |                         |
              v                         v
       PathwaySegment           PathwayTraversal
       inter-component          intra-component
```

---

## 5. PathwaySegment

**PathwaySegment** is a specialisation of PathwayElement representing an inter-component flow.

Its normal form is:

```text
Ergon A.EgressEndpoint
          |
          | PathwaySegment
          v
Ergon B.IngressEndpoint
```

Conceptually, PathwaySegment represents the defined interaction between two adjacent component interfaces.

A PathwaySegment has routing/control significance.

---

## 6. PathwayTraversal

**PathwayTraversal** is a specialisation of PathwayElement representing intra-component passage from a component's ingress interface to its egress interface.

```text
Ergon B.IngressEndpoint
          |
          | PathwayTraversal
          v
Ergon B.EgressEndpoint
```

A PathwayTraversal records that information/activity traversed the component. It does **not** describe or expose the component's internal processing.

This preserves Ergon encapsulation while allowing a continuous representation of passage through Harmonia.

An observed sequence may therefore appear as:

```text
Ingress A
    |
    | PathwayTraversal A
    v
Egress A
    |
    | PathwaySegment AB
    v
Ingress B
    |
    | PathwayTraversal B
    v
Egress B
    |
    | PathwaySegment BC
    v
Ingress C
```

This produces the useful repeating structure:

```text
Traversal -> Segment -> Traversal -> Segment -> Traversal
```

Most visualisation, assurance and reporting may operate against the common PathwayElement abstraction, while actual routing and control remain principally associated with PathwaySegment.

---

## 7. Route

**Route** is an ordered sequence of PathwaySegments.

```text
Route = PathwaySegment [1..N]
```

A Route defines one available traversal between the relevant PathwayEndpoints.

Where multiple valid traversals exist, multiple Routes may be available within a Pathway.

Example:

```text
Pathway A -> D

Route 1: A -> B -> D
Route 2: A -> C -> D
```

The Route is fundamentally a routing/control construct. The architecture should not redefine Route merely to simplify reporting or observation; PathwayElement and Journey provide the richer observational abstractions.

---

## 8. Pathway

**Pathway** defines a controlled interaction between PathwayEndpoints, with the available traversal(s) defined by one or more Routes.

Conceptually:

> **Pathway ≈ an interaction between ApplicationInterfaces whose available traversals are defined through Route.**

A Praxis may have **zero or one Pathway**:

```text
Praxis
   |
   +-- Pathway [0..1]
```

Praxis is therefore not equivalent to Pathway, Route or a sequence of Ergon. The exact broader semantics of Praxis remain subject to separate architectural adjudication.

---

## 9. Publish/Subscribe Processing

Publish/Subscribe provides the second primary mechanism for information passage through Harmonia.

Unlike Pathway processing, Pub/Sub does not require a predefined end-to-end Route.

A simplified topology may be:

```text
Publisher
    |
    v
  Topic X
    |
    +----> Ergon B ---- publishes ----> Topic Y ----> Ergon D
    |
    +----> Ergon C ---- publishes ----> Topic Z ----> Ergon E
```

The design may be simple, but runtime activity can rapidly become difficult for humans to debug, monitor, report upon and assure.

Pub/Sub progression can branch, converge and create further publications dynamically. Its runtime topology can therefore form a graph rather than a predefined linear route.

Harmonia SHALL NOT require humans to reason directly from raw Pub/Sub topology and telemetry for normal reporting and assurance purposes.

Instead, observed Pub/Sub activity is represented through Journey.

---

## 10. Journey

**Journey** is the governed representation of Harmonia's observed progression of information and activity.

Working definition:

> **Journey is the governed representation of Harmonia's observed progression of information and activity, reconstructed from available execution evidence and forming the primary subject of reporting, monitoring, analysis and assurance, irrespective of whether that progression arose through Pathway-controlled routing, Publish/Subscribe processing, or a combination of both.**

Journey is therefore not merely a Pub/Sub convenience.

The reporting and assurance representation of activity within Harmonia is a Journey **even when that Journey resulted from a Pathway-controlled sequence**.

Conceptually:

```text
DESIGN / CONTROL             EXECUTION                 OBSERVATION

Pathway                      Ponos                     Journey
  |                            |                         |
  +-- Route                   Ergon                      |
  |                            |                         |
  +-- PathwayElement           |                         |
                               |                         |
Publish/Subscribe ------------+                         |
                               |                         |
                               +------------------------>|
                                                         v
                                               Reporting / Assurance
```

---

## 11. Pathway and Journey Are Different Concerns

A Pathway describes defined/permitted information passage.

A Journey represents observed information/activity progression.

A Journey does not imply that a Pathway or Route existed beforehand.

For Pathway-controlled execution:

```text
Defined Pathway / Route
          |
          v
       Execution
          |
          v
        Journey
```

The Journey can subsequently be assessed against the Pathway and Route that controlled or constrained execution.

For Pub/Sub processing:

```text
Publication / Subscription
          |
          v
       Execution
          |
          v
        Journey
```

There may be no corresponding predefined Pathway.

The Journey remains the primary human-consumable and assurance-consumable representation.

---

## 12. Journey as the Assurance Subject

Journey is intended to be the fundamental building block for Harmonia assurance activity.

Rather than requiring assurance logic to independently understand every execution mechanism, assurance can principally ask:

> **What Journey occurred?**

Where a Pathway exists, it provides normative context against which the Journey can be assessed.

```text
                 Journey
                    |
                    | assessed against
                    v
              Pathway / Route
              where applicable
                    |
                    v
                Assurance
```

Where no Pathway exists, the Journey may instead be assessed against other expected behaviours, policies, constraints, architecture, information rules or assurance criteria.

This permits a common assurance model across:

- Pathway-controlled activity;
- Publish/Subscribe activity;
- mixed Pathway and Pub/Sub activity;
- ordinary Ergon activity;
- Digital Twin activity;
- other Harmonia execution patterns capable of producing observable Journey evidence.

---

## 13. Journey and AI-Based Assurance

Journey is expected to be a principal subject of AI-assisted interpretation within Dokimasia assurance activity.

AI may assist in interpreting:

- observed progression;
- branching and convergence;
- likely causal relationships;
- conformance to expected Pathways or Routes;
- unexpected or anomalous activity;
- missing or failed expected activity;
- information exposure;
- security or policy anomalies;
- relationships between apparently separate activities;
- significance of observed activity within the relevant assurance context.

Conceptually:

```text
Observed Evidence
       |
       v
     Journey
       |
       v
AI / Deterministic Interpretation
       |
       v
Dokimasia Assurance Activity
       |
       v
Assurance Result / Finding
```

AI-based assurance activity is expected to occur as an explicit governed activity within Dokimasia workflows where appropriate, rather than merely as an opaque internal service call from an unrelated Ergon.

Detailed AI architecture remains subject to separate Information Systems Layer Architecture discovery and adjudication.

---

## 14. Observation Must Remain Distinct from Interpretation

Journey must not simply become "what the AI thinks happened."

The architecture must preserve a distinction between:

```text
WHAT HARMONIA OBSERVED
          |
          v
        Journey
          |
          v
WHAT HARMONIA CAN REASON
FROM THOSE OBSERVATIONS
          |
          v
Dokimasia / AI Assurance
          |
          v
WHAT HARMONIA CONCLUDES
          |
          v
Assurance Finding / Result
```

Journey is factual with respect to the observations available to Harmonia.

Interpretative relationships may be derived from those observations, including through AI, but inferred relationships and conclusions must remain distinguishable from directly observed evidence.

This is consistent with the broader Harmonia architectural position that Harmonia's concept of reality is bounded by its observability of that reality through the information available to it.

---

## 15. Journey May Be Graph-Shaped

Journey SHALL NOT be assumed to be linear.

Pub/Sub processing can naturally fan out, converge or produce further activity:

```text
                       A
                       |
                 publication X
                       |
          +------------+------------+
          v            v            v
          B            C            D
          |                         |
          v                         v
          E                         F
```

Where observable evidence establishes these activities as part of the same progression, the Journey may represent that branching structure.

The detailed Journey information model, including its constituent elements, relationships, evidence references, causal semantics and cardinalities, remains to be designed.

---

## 16. Relationship to Domain04 Observability

Journey should remain consistent with the established Information Architecture principle:

> **Harmonia's concept of reality is bounded by its observability of that reality through the information available to it.**

A Journey therefore represents the progression Harmonia can establish from available evidence. It does not claim independent knowledge of objective reality beyond that evidence.

This distinction is especially important where AI is used to interpret incomplete, ambiguous or probabilistic relationships.

---

## 17. Candidate Architectural Consequences

The current discussion suggests the following candidate consequences for Domain05:

1. PathwayEndpoint is an application-interface-style construct associated with Harmonia component ingress/egress boundaries.
2. Every Ergon exposes an IngressEndpoint and EgressEndpoint.
3. PathwayElement provides the common abstraction for passage between/across component boundaries.
4. PathwaySegment represents inter-component passage and carries routing/control significance.
5. PathwayTraversal represents intra-component passage without exposing internal component processing.
6. Route remains an ordered sequence of PathwaySegments.
7. Pathway defines controlled routing using available Route(s).
8. Praxis may have zero or one Pathway.
9. Pub/Sub remains a distinct dynamic processing mechanism and is not required to have a predefined Pathway.
10. Journey is the canonical observational representation of Harmonia activity irrespective of its controlling mechanism.
11. Reporting, monitoring, analysis and assurance should principally consume Journey representations.
12. Journey may be graph-shaped.
13. Pathway-controlled Journeys can be assessed against their governing Pathway/Route.
14. Pub/Sub Journeys can be assessed against other applicable architecture, policy, behavioural and assurance expectations.
15. AI interpretation of Journey is expected to be an important Dokimasia assurance capability.
16. Observed Journey evidence must remain distinguishable from AI inference and assurance conclusions.

---

## 18. Not Yet Finalised

This DesignNote deliberately does **not** finalise:

- the complete semantics of Praxis;
- the detailed Journey information model;
- whether Journey has specialised JourneyElement types;
- the exact relationship between Journey elements and PathwayElement;
- causal-link representation;
- correlation identifiers;
- evidence/reference structures;
- confidence or uncertainty representation;
- how inferred Journey relationships are represented;
- Journey lifecycle and persistence;
- which component owns Journey construction;
- which component owns Journey information management;
- detailed Dokimasia consumption contracts;
- detailed AI assurance architecture;
- exact Pub/Sub topic/subscription information models;
- detailed Petasos relationships;
- implementation classes, inheritance or technology choices.

These require further Information Systems Layer Architecture discovery and adjudication.

---

## 19. Working Architectural Summary

```text
DEFINED CONTROL                               DYNAMIC CONTROL

Pathway                                      Publish / Subscribe
  |
  +-- Route [1..*]
        |
        +-- PathwaySegment [1..*]

PathwayElement
  |
  +-- PathwaySegment
  |     EgressEndpoint -> IngressEndpoint
  |
  +-- PathwayTraversal
        IngressEndpoint -> EgressEndpoint


                         EXECUTION
                            |
                            v
                         JOURNEY
                            |
                +-----------+-----------+
                |                       |
                v                       v
             Reporting              Assurance
                                        |
                                        v
                                   Dokimasia
                                        |
                           +------------+------------+
                           |                         |
                           v                         v
                    Deterministic                AI-based
                     Assurance                   Assurance
```

The key architectural principle is:

> **Pathway describes controlled passage; Publish/Subscribe enables dynamic passage; Journey describes observed passage. Journey is the common representation through which Harmonia reports, analyses and assures what occurred.**
