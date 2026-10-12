# DesignNote — Mneme Publication/Subscription and Petasos Handoff Architecture

**Status:** Working Design Note  
**Scope:** Harmonia Information Systems Layer Architecture — candidate design discussion  
**Authority:** Conversational design input only. This note is not a RADS baseline and does not supersede canonical architecture.  
**Date:** 2026-10-11

## 1. Purpose

This DesignNote captures the current architectural reasoning for Harmonia publish/subscribe processing and its relationship to:

- Mneme;
- Pragma;
- InformationUnit;
- Publishing;
- Subscription;
- Pattern;
- Petasos;
- ActionableTask;
- FulfillmentTask;
- Ponos;
- Ergo / Ergon; and
- Journey.

It complements the separate DesignNote concerning Pathways, Journeys and assurance representation.

The principal architectural separation established here is:

> **Mneme manages the information and determines information eligibility, publication, subscription and selection. Petasos provides persistent/recoverable messaging and handoff services. Ponos manages execution.**

For ActionableTask processing specifically:

> **Petasos distributes the opportunity to undertake work, not the undertaking itself.**

This statement describes the ActionableTask handoff pattern and is not a complete definition of Petasos.

---

## 2. Pragma and its Payload

Every Pragma contains a payload, normally an InformationUnit or equivalent managed information construct.

Conceptually:

```text
Pragma
|
+-- Pragma Metadata
|     +-- origin
|     +-- targetDestination
|     +-- timestamps
|     +-- processing status
|     +-- other metadata
|
+-- Payload
      |
      +-- InformationUnit
            |
            +-- Container
            |     +-- Topic
            |     +-- source
            |     +-- other metadata
            |
            +-- Content
                  +-- Topic
                  +-- source
                  +-- other metadata
```

The exact metadata schemas remain to be finalised.

A significant semantic point is that Topic is not necessarily a singular property of a transported object. Topic and other selection metadata may exist independently on the InformationUnit container and content aspects.

Destination and Topic are not equivalent concepts.

---

## 3. Mneme Owns Pragma Management

Pragma is managed information and therefore belongs within Mneme's information-management responsibility.

Conceptually:

```text
Mneme
  |
  +-- Pragma Management
        |
        +-- PragmaFactory
        +-- Pragma
        +-- Publishing
        +-- Subscription
        +-- Pattern / Matching
```

Execution of, behaviour upon, or transport/handoff of a Pragma does not confer information-management ownership of that Pragma.

This is consistent with the previously established responsibility rule:

> **Execution of, behaviour upon, or transport of managed information does not confer information-management responsibility for that information.**

---

## 4. Pattern

Pattern is the common matching concept used to evaluate relevant characteristics of managed Pragma and their payload information.

A Pattern may potentially evaluate characteristics drawn from:

```text
Pattern
   |
   +-- Pragma metadata
   |
   +-- InformationUnit.Container metadata
   |       +-- Topic
   |       +-- source
   |       +-- ...
   |
   +-- InformationUnit.Content metadata
           +-- Topic
           +-- source
           +-- ...
```

The complete Pattern language, supported attributes, precedence, operators, matching implementation and performance model remain to be designed.

Pattern is an information-selection concept, not merely a messaging-broker topic expression.

---

## 5. Subscription

A Subscription allows a consumer to request that Pragma matching a Pattern be forwarded to its input queue.

Working definition:

> **Subscription is a governed consumer-side information-selection definition that requests handoff of Pragma matching its associated Pattern to the consumer's input queue or equivalent destination.**

Conceptually:

```text
Available Pragma
       |
       v
  Subscription
       |
   Pattern match?
    /        \
   no        yes
   |          |
   X          v
       Consumer Input Queue
```

Subscription therefore expresses:

> **What information/work is this consumer interested in receiving?**

Subscription matching is a Mneme information-management responsibility.

---

## 6. Publishing

Publishing provides the provider-side control over what egress content may be made available through publication.

A Publishing object contains or references Pattern-based filtering semantics.

Working definition:

> **Publishing is a governed provider-side publication definition that constrains whether egress Pragma may be made available for subscription-based handoff.**

Conceptually:

```text
Provider / Ergon
      |
      v
 Egress activity
      |
      v
   Publishing
      |
 Pattern / Filter
      |
   blocked?
   /     \
 yes     no
  |       |
  X       v
      available for
      subscription matching
```

Publishing does not determine the ultimate execution of a consumer. It governs provider-side information eligibility for publication.

---

## 7. Publishing and Subscription Are Deliberately Asymmetric

Publishing and Subscription use related Pattern semantics but represent different responsibilities.

```text
PROVIDER                                  CONSUMER

Publishing                                Subscription
    |                                         |
    v                                         v
"What may leave /                       "What do I
be made available?"                     want to receive?"
    |                                         |
    +----------------+   +--------------------+
                     |   |
                     v   v
                 eligible match
                       |
                       v
                handoff decision
```

Publishing constrains availability.

Subscription expresses interest.

Mneme evaluates both as part of governed information selection.

---

## 8. Mneme Performs Publication/Subscription Management

Mneme is responsible for the semantic management of Harmonia publication/subscription processing.

This includes, at the architectural level:

- management of Pragma;
- management of Publishing definitions;
- management of Subscription definitions;
- Pattern evaluation;
- information eligibility;
- matching and selection; and
- determination of eligible destination(s).

Conceptually:

```text
Ergon A
   |
   | produces / presents Pragma P
   v
Mneme
   |
   +-- apply Publishing constraints
   |
   +-- evaluate Subscriptions
   |
   +-- evaluate Patterns against
   |      +-- Pragma metadata
   |      +-- InformationUnit.Container metadata
   |      +-- InformationUnit.Content metadata
   |
   +-- determine eligible destination(s)
             |
             v
          Petasos
```

A useful boundary statement is:

> **Mneme coordinates the governed management, selection, publication, subscription and availability of information. It does not coordinate execution of the activities that consume that information.**

Execution responsibility remains with Ponos.

---

## 9. Petasos Does Not Own the Information Payload

For Pragma-based processing, Petasos does not transport the InformationUnit payload.

The Pragma and its payload remain managed through Mneme.

Petasos transports/manages the identity or reference of the relevant Pragma together with the metadata required to effect the handoff.

Conceptually:

```text
                         Mneme
                           |
                    manages Pragma P
                           |
          +----------------+----------------+
          |                |                |
       identity         metadata          payload
                                      InformationUnit
                                            |
                                      remains managed
                                        by Mneme
```

Petasos receives/manages something conceptually closer to:

```text
Handoff / Queue Entry
|
+-- Pragma identity / reference
+-- destination / queue information
+-- handoff metadata
+-- delivery / queue state
+-- other messaging metadata
```

rather than:

```text
Message
|
+-- metadata
+-- complete InformationUnit payload
```

The exact Petasos handoff structure remains to be designed.

---

## 10. Context is Resolved Through Mneme

Because the payload remains under Mneme management, the receiving execution environment resolves the relevant Pragma and processing context through Mneme.

Conceptually:

```text
Petasos
   |
   | identity / handoff metadata
   v
Task Receiver
   |
   v
Context Loader
   |
   +-----------------------> Mneme
   |                           |
   |                           +-- resolve Pragma
   |                           +-- load required context
   |
   v
Checkpoint
   |
   v
Ergon Business Logic
   |
   v
Checkpoint
   |
   v
Context Unloader
   |
   +-----------------------> Mneme
                               |
                               +-- persist/update
                                   governed information
```

This reinforces the separation between:

- information management — Mneme;
- messaging/handoff — Petasos;
- execution — Ponos; and
- bounded activity — Ergon.

---

## 11. Pub/Sub Fan-Out

A single Pragma may match multiple Subscriptions.

Conceptually:

```text
                        Mneme
                          |
                      Pragma P
                          |
                Subscription Matching
                          |
             +------------+------------+
             v            v            v
            S1           S2           S3
             |            |            |
             +------------+------------+
                          |
                          v
                       Petasos
                          |
             +------------+------------+
             v            v            v
         Queue/Handoff Queue/Handoff Queue/Handoff
            P->B         P->C         P->D
```

This does not inherently require duplication of the managed payload.

The common managed Pragma remains within Mneme while Petasos may manage multiple independent handoff obligations or queue entries referring to it.

The exact reference/copy/version semantics remain subject to detailed design where required.

---

## 12. ActionableTask and FulfillmentTask Handoff Semantics

A critical distinction exists between ActionableTask and FulfillmentTask.

```text
ActionableTask
    = work that can/should be undertaken

FulfillmentTask
    = a particular identifiable undertaking
      of that ActionableTask
```

For ActionableTask processing, Petasos does **not** distribute the details of a FulfillmentTask.

It hands off the ActionableTask identity/details required for a consumer to recognise and undertake the work.

Each consumer then copies/morphs the ActionableTask representation into its own distinct FulfillmentTask.

Conceptually:

```text
                  ActionableTask
               "this work is to be done"
                        |
                     Mneme
                        |
                     Petasos
             persistent/recoverable
             ActionableTask handoff
                        |
          +-------------+-------------+
          v             v             v
      Consumer A    Consumer B    Consumer C
          |             |             |
       undertake      undertake      undertake
          |             |             |
          v             v             v
   FulfillmentTask FulfillmentTask FulfillmentTask
        FT-A           FT-B           FT-C
```

This is consistent with the established Information Architecture relationship:

```text
ActionableTask
      |
      +---- FulfillmentTask [0..*]
```

Multiple consumers undertaking the same ActionableTask therefore naturally produce multiple FulfillmentTasks.

---

## 13. Petasos Distributes the Opportunity to Undertake Work

For the ActionableTask handoff pattern:

> **Petasos distributes the opportunity to undertake work, not the undertaking itself.**

The handoff may be understood conceptually as:

```text
Petasos Queue Entry
        |
        | identifies
        v
  ActionableTask AT-123
        |
        | resolved through Mneme
        v
      Mneme
        |
        v
ActionableTask representation
        |
        | consumer undertakes / morphs
        v
  FulfillmentTask FT-901
        |
        v
      Ponos
        |
        v
      Ergon
```

The resulting FulfillmentTask belongs to the particular undertaking and must not be confused with the common ActionableTask offered through Petasos.

---

## 14. Distinct State Responsibilities

The architecture should not collapse all processing/delivery/execution state into Pragma or ActionableTask metadata.

At minimum, conceptually distinct state exists for:

```text
ActionableTask state
    status of the work requirement itself

Petasos handoff state
    status of a particular queue/handoff obligation

FulfillmentTask state
    status of a particular undertaking

Ponos execution state
    runtime execution/progression state
```

The detailed state models remain unresolved.

This distinction is consistent with the established principle that information state, operational activity state and workflow/progression state are not equivalent.

---

## 15. Petasos Remains a General Harmonia Messaging/Handoff Subsystem

Petasos must **not** be architecturally reduced to a Ponos messenger or an ActionableTask delivery mechanism.

Petasos is a shared Harmonia messaging/handoff subsystem.

Working architectural responsibility:

> **Petasos provides persistent, recoverable messaging and handoff services between Harmonia processing boundaries without thereby acquiring ownership of the information or activity semantics that caused the handoff.**

Known or anticipated service patterns include:

```text
                         Petasos
                            |
          +-----------------+-----------------+
          |                 |                 |
          v                 v                 v
    Task Handoff      Communication       Notification
                        Events              Events
          |                 |                 |
          +-----------------+-----------------+
                            |
                       other future
                    messaging/handoff
                        patterns
```

For Pragma/ActionableTask handoff, identity/reference-based transfer is established as the candidate model.

The architecture does **not** yet assert that every Petasos interaction must be reference-only. Communication Events, Notification Events and other message forms require their own semantics.

Petasos therefore remains a first-class shared subsystem that may serve Ponos, Agora, Pylai and other Harmonia components where architecturally appropriate. Exact client relationships remain subject to adjudication.

---

## 16. Petasos Queue/Destination Semantics

Petasos requires persistent queue management and destination/routing mechanics.

The exact Petasos concept corresponding to a queue destination, channel or broker-style topic is not yet named or finalised.

Care must be taken not to confuse Petasos queue/destination classification with InformationUnit semantic Topic.

These are distinct concerns:

```text
Mneme / Information Semantics

InformationUnit.Container.Topic
InformationUnit.Content.Topic


Petasos / Messaging Semantics

Queue / Destination / Channel / equivalent
```

Even if a future implementation technology uses the word "topic" for both, the architecture should maintain the semantic distinction.

---

## 17. Relationship to Ponos and Ergon

The responsibility chain for Pragma-based ActionableTask processing is:

```text
Mneme
  |
  | manages information,
  | Publishing / Subscription,
  | Pattern matching and selection
  v
Petasos
  |
  | persistent/recoverable
  | handoff of identity/metadata
  v
Consumer Input Queue
  |
  v
Consumer undertakes ActionableTask
  |
  v
FulfillmentTask
  |
  v
Ponos
  |
  | manages execution
  v
Ergon
  |
  | Context Loader / Unloader
  | resolve/update through Mneme
  v
governed activity
```

Petasos is used by this chain but is not structurally owned by Ponos.

---

## 18. Relationship to Journey

The publication/subscription and handoff architecture provides rich evidence from which Journey can represent what occurred.

Different architectural components contribute different evidence.

```text
Mneme
  |
  +-- Publishing permitted / blocked
  +-- Subscription matched
  +-- Pattern reasoning
  +-- destination selected

Petasos
  |
  +-- handoff created
  +-- queue/destination
  +-- delivery/recovery state

Ponos / Ergon
  |
  +-- undertaking created
  +-- execution occurred
  +-- activity result / progression

              |
              v
            Journey
              |
              v
       Reporting / Assurance
```

This permits Journey and Dokimasia to distinguish, for example:

- a consumer was never selected by Mneme;
- a consumer was selected but Petasos failed to effect the handoff;
- Petasos effected the handoff but execution did not occur;
- execution occurred but the Ergon failed;
- execution completed but produced an unexpected result.

Journey remains the canonical observational representation of the activity, whether the activity arose through Pub/Sub or Pathway-controlled processing.

---

## 19. Relationship to Pathway Processing

Publish/Subscribe and Pathway processing remain distinct control/progression mechanisms.

```text
CONTROL / PROGRESSION                    OBSERVATION

Pathway-controlled --------+
                            |
                            +------------> Journey
                            |
Publish/Subscribe ----------+
```

Pub/Sub does not require a predefined end-to-end Pathway or Route.

Journey provides the common reporting and assurance representation across both mechanisms.

A later design activity may further establish how Pub/Sub activity is represented within Journey and how mixed Pathway/Pub/Sub activity is correlated.

---

## 20. Candidate Responsibility Summary

```text
MNEME
--------------------------------------------------
Information management
Pragma management
PragmaFactory
InformationUnit / payload management
Publishing definitions
Subscription definitions
Pattern semantics and evaluation
Information eligibility
Matching / selection
Determination of eligible destinations


PETASOS
--------------------------------------------------
Shared persistent/recoverable messaging and handoff
Queue management
Destination / channel mechanics
Handoff metadata
Handoff state
Retry / recovery
Duplicate handling
ActionableTask identity/reference handoff
Communication / Notification event support
Other messaging patterns as required


PONOS
--------------------------------------------------
Execution management
Scheduling
Concurrency
Progression
Execution failure handling
Invocation/management of bounded activity


ERGO / ERGON
--------------------------------------------------
Reusable activity/platform-integration contract
Bounded fine-grained activity
Context Loading / Unloading
Platform integration
Business/activity logic


JOURNEY
--------------------------------------------------
Canonical observed representation
Reporting
Monitoring
Analysis
Assurance substrate
```

---

## 21. Architectural Principles Established

### 21.1 Information ownership

> **Execution of, behaviour upon, or transport/handoff of managed information does not confer information-management responsibility for that information.**

### 21.2 Publication/subscription responsibility

> **Mneme determines whether and where managed information should be made available according to Publishing, Subscription and Pattern semantics.**

### 21.3 Handoff responsibility

> **Petasos provides the persistent/recoverable messaging and handoff services required to effect those decisions without assuming Mneme's information-management responsibility or Ponos's execution responsibility.**

### 21.4 ActionableTask handoff

> **For ActionableTask processing, Petasos distributes the opportunity to undertake work, not the undertaking itself.**

### 21.5 Fulfilment

> **A consumer undertaking an ActionableTask creates/morphs a distinct FulfillmentTask representing that particular undertaking.**

### 21.6 Payload locality

> **For Pragma-based ActionableTask handoff, the managed information payload remains under Mneme management; Petasos manages the identity/reference and handoff metadata required to cause subsequent processing.**

### 21.7 Petasos generality

> **Petasos is a shared Harmonia messaging/handoff subsystem and SHALL NOT be constrained to Ponos task messaging alone.**

---

## 22. Implementation Possibilities Are Deliberately Deferred

The architecture appears amenable to implementation using capabilities of Infinispan or equivalent distributed data-grid/cache technology for significant portions of:

- managed Pragma storage/access;
- indexing and Pattern matching;
- distributed event/listener behaviour;
- persistent/distributed structures;
- subscription evaluation; and
- related clustered processing.

However:

> **Implementation capability SHALL NOT be used to reverse-derive the Information Systems architecture.**

The architecture first establishes required semantics and responsibilities. Technology analysis can subsequently determine how much of that responsibility can be efficiently realised through Infinispan or other middleware/platform services.

---

## 23. Known Guardrail and Implementation Concerns

The following concerns are known but do not currently invalidate the architectural responsibility model established by this DesignNote:

- circular subscriptions;
- publication/subscription loops;
- recursive or uncontrolled fan-out;
- loop detection and termination;
- duplicate suppression;
- subscription explosion;
- Pattern evaluation performance;
- indexing strategy;
- queue sizing and back-pressure;
- delivery retry behaviour;
- stale subscriptions;
- destination availability;
- ordering guarantees;
- transaction boundaries;
- concurrency and race conditions;
- distributed consistency;
- cache/data-grid implementation mechanics.

These should be treated principally as later design, implementation and operational guardrail concerns unless further analysis exposes a genuine architectural requirement or contradiction.

The architecture should not prematurely embed implementation-specific solutions to these issues.

---

## 24. Not Yet Finalised

This DesignNote deliberately does **not** finalise:

- detailed Pragma metadata schema;
- detailed InformationUnit Container/Content Topic semantics;
- Pattern language or schema;
- Pattern precedence or evaluation algorithm;
- Publishing lifecycle;
- Subscription lifecycle;
- subscription registration/discovery mechanics;
- detailed Petasos handoff structure;
- Petasos queue/destination/channel naming;
- exact semantics of Communication Events;
- exact semantics of Notification Events;
- whether all Petasos message forms are reference-only;
- detailed ActionableTask-to-FulfillmentTask morph/copy mechanics;
- exact state models;
- ownership of Journey construction;
- Journey constituent model;
- detailed assurance evidence representation;
- implementation technology;
- Infinispan implementation design;
- loop/circular-subscription prevention mechanisms.

These require later Information Systems Layer, middleware, implementation and/or operational design as appropriate.

---

## 25. Working Architectural Summary

```text
                  ERGON / PROVIDER
                         |
                         v
                      PRAGMA
                         |
                         v
                       MNEME
                         |
              +----------+----------+
              |                     |
              v                     v
          Publishing           Subscription
           Pattern               Pattern
              |                     |
              +----------+----------+
                         |
                    Match / Select
                         |
                         v
                      PETASOS
          persistent/recoverable messaging
                 and handoff services
                         |
                identity / metadata
                         |
                         v
                  Consumer Queue
                         |
                         v
                  ActionableTask
                   is undertaken
                         |
                         v
                  FulfillmentTask
                         |
                         v
                       PONOS
                         |
                         v
                       ERGON
                         |
             Context via Mneme


          OBSERVED ACROSS THE ACTIVITY
                         |
                         v
                      JOURNEY
                         |
                         v
               REPORTING / ASSURANCE
```

The central architectural separation is:

> **Mneme knows what the information is and determines who is eligible to receive it. Petasos reliably manages the handoff. Ponos manages the resulting execution. Ergon performs the bounded activity. Journey represents what observably happened.**
