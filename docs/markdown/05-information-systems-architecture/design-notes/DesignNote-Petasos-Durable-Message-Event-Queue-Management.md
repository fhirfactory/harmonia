# DesignNote — Petasos Durable Message/Event Queue Management

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05 Candidate Architecture Step A  
**Subject:** Petasos purpose-defined durable queues, PetasosParcel, inter-Ergon information flow, event sharing, operational queue management, and component boundaries

## 1. Purpose

This DesignNote records the current architectural position for **Petasos**, Harmonia's durable message/event queue-management component.

It establishes the Petasos responsibility boundary and the conceptual semantics of its common queued datatype, **PetasosParcel**, without prematurely selecting queue technology, broker implementation, physical topology, delivery protocol, queue persistence mechanism, retry algorithm, ordering model, or detailed Parcel schema.

It also corrects any earlier architectural wording that characterised Petasos too narrowly as merely a Ponos messenger, an ActionableTask handoff mechanism, or a reference-only transport service.

## 2. Fundamental Responsibility

> **Petasos manages the creation and operational status of durable message/event queues used by Harmonia.**

Petasos queues are not generic arbitrary messaging queues. Each queue exists for a defined Harmonia functional purpose.

Petasos supports:

- durable inter-Ergon flow of information;
- defined Harmonia communication/event sharing;
- defined Harmonia notification/event sharing; and
- other well-defined Harmonia event-sharing services established by the architecture.

Conceptually:

```text
                         PETASOS
        Durable Message/Event Queue Management
                           |
          +----------------+----------------+
          |                |                |
     Queue Creation   Queue Operation   Queue Status
          |                |                |
          +----------------+----------------+
                           |
                           v
                 Purpose-defined Queue
                           |
                           | carries [0..*]
                           v
                    PetasosParcel
```

Petasos is therefore a first-class shared Harmonia messaging/event capability, not merely a delivery mechanism subordinate to Ponos.

## 3. Purpose-Defined Queues

A Petasos queue SHALL exist for a defined Harmonia functional purpose.

The architecture does not treat Petasos as a generic broker through which arbitrary modules create semantically ungoverned queues.

Illustratively:

```text
Petasos
  |
  +-- Inter-Ergon Information Flow Queue
  |
  +-- Communication Event Queue
  |
  +-- Notification Event Queue
  |
  `-- Other architecturally defined
      Harmonia event-sharing purpose
```

These examples do not establish the final queue taxonomy or queue names.

The important architectural property is:

> **Queue purpose is part of the Harmonia architecture; queue existence is not semantically arbitrary.**

The detailed mechanism by which queue purposes are defined, registered, instantiated or configured remains unresolved.

## 4. Common Queue Datatype — PetasosParcel

All Petasos queues carry a single common datatype:

> **PetasosParcel**

A PetasosParcel represents the queued Harmonia communication/event.

From a conceptual perspective, a PetasosParcel may be thought of as analogous to a **FHIR Communication** object.

This analogy establishes conceptual intent; it does not establish that PetasosParcel is literally a FHIR Communication resource or that its eventual schema must mirror FHIR Communication.

## 5. Pass-by-Value Parcel, Predominantly Pass-by-Reference Information

The PetasosParcel itself is **pass-by-value**.

The information associated with or referred to by that Parcel is expected to be predominantly **pass-by-reference**.

```text
Petasos Queue
      |
      | transports by value
      v
+--------------------------------+
|         PetasosParcel          |
|                                |
| parcel identity                |
| communication/event metadata   |
| source / origin                |
| destination / recipient        |
| purpose / event semantics      |
| timestamps / operational data  |
|                                |
| references                     |
|   +--> governed information    |
|   +--> Pragma                  |
|   +--> ActionableTask          |
|   +--> Identity                |
|   +--> Journey                 |
|   +--> other referenced things |
+--------------------------------+
```

The examples above are illustrative only and do not define the final PetasosParcel schema.

The architectural distinction is:

```text
PetasosParcel
    pass-by-value
    "the communication/event being queued"

Referenced Information
    predominantly pass-by-reference
    "the things that communication/event concerns"
```

Petasos SHALL NOT be described as transporting only references. It transports PetasosParcel instances by value.

Likewise, Petasos SHALL NOT be assumed to duplicate all information associated with a Parcel into the queue.

## 6. PetasosParcel Ownership

Petasos owns the messaging/event semantics and operational management of PetasosParcel within its queueing responsibility.

This does not transfer ownership of referenced information to Petasos.

For example:

```text
Mneme
  owns/manages governed information
             ^
             | reference
             |
      PetasosParcel
             |
             v
Petasos
  owns durable queueing and
  Parcel communication/event handling
```

A reference from a PetasosParcel to information managed by another Harmonia component does not make Petasos the information-management authority for the referenced information.

## 7. ActionableTask Handoff as a Petasos Use Case

ActionableTask processing is one Petasos use case; it does not define Petasos as a whole.

For an ActionableTask handoff:

```text
Mneme
  |
  | manages
  v
ActionableTask / Pragma
  ^
  | reference
  |
PetasosParcel
  |
  | passed by value
  v
Purpose-defined
ActionableTask Queue
  |
  v
Receiving Consumer
  |
  v
establishes its own
FulfillmentTask
```

The PetasosParcel may carry the identity/reference and communication metadata necessary to offer the ActionableTask to the destination.

Petasos does not transport another consumer's FulfillmentTask.

Each consumer that undertakes the ActionableTask establishes its own distinct FulfillmentTask according to the separately established ActionableTask/FulfillmentTask architecture.

The earlier statement that Petasos does not transport the ActionableTask's information payload remains valid for this use case.

It SHALL NOT be generalised into the incorrect proposition that Petasos transports no by-value data or that PetasosParcel itself is reference-only.

## 8. Broader Event and Communication Use

Petasos is not restricted to ActionableTask or Ponos-related handoff.

Its purpose-defined queues may support other defined Harmonia event-sharing needs, including communication and notification events.

```text
Inter-Ergon Flow Queue
       |
       v
PetasosParcel
       |
       `-- relevant inter-Ergon references/context


Notification Event Queue
       |
       v
PetasosParcel
       |
       `-- notification/event semantics


Communication Event Queue
       |
       v
PetasosParcel
       |
       `-- communication/event semantics
```

The exact event types and queue taxonomy remain deliberately unresolved.

## 9. Queue Operational Management

Petasos responsibility includes the **operational status** of the queues it manages.

Petasos is therefore more than an enqueue/dequeue API.

Conceptually its responsibility includes:

```text
Petasos
  |
  +-- Queue Purpose / Definition
  +-- Queue Creation
  +-- Queue Operational Availability
  +-- Queue Operational Status
  `-- Queue Management
```

Detailed operational metrics and state are not yet defined.

Potential downstream considerations may include queue availability, depth, backlog, consumer state, stalled processing, delivery state, failure state and health, but none are mandated as specific Petasos data structures by this DesignNote.

## 10. Relationship to Energeia

Petasos owns and manages the operational state of its purpose-defined queues.

Energeia owns Harmonia-level operational observation/reporting.

Therefore:

```text
Petasos
   owns queue operational state
          |
          | operational observations
          v
Energeia
   observes and reports Harmonia
   operational behaviour
```

Energeia observation/reporting does not transfer queue-management responsibility from Petasos.

For R1.x/R2.x, Energeia remains limited to the separately established **Observe -> Report** scope; it does not autonomously control Petasos queue behaviour.

## 11. Relationship to Themis

Where a Petasos operation constitutes a governed forwarding action, the applicable Policy Enforcement Point is local to the Petasos operation but is implemented through Themis-owned PEP capability.

Conceptually:

```text
Petasos proposed FORWARD
          |
          v
Themis-owned PEP
          |
          | evaluates applicable
          | PEP-Context Criteria
          v
     local decision
          |
       ALLOW / DENY
          |
          v
Petasos queue operation
```

Themis owns the Authorisation Model, PEP-Context Criteria and PEP enforcement semantics.

Petasos owns the actual queue operation and is therefore the location at which the forwarding action can be prevented.

The detailed interaction between Petasos queue purpose, destination semantics and Themis FORWARD Criteria remains downstream design.

## 12. Relationship to Mneme

Mneme manages governed information referenced by PetasosParcel where that information falls within Mneme's information-management responsibility.

Petasos does not become the owner of a Pragma, ActionableTask, Information Unit or other governed information merely because a Parcel references it.

```text
Mneme
    WHAT GOVERNED INFORMATION IS MANAGED?

Petasos
    WHAT DURABLE COMMUNICATION/EVENT IS QUEUED,
    FOR WHAT DEFINED PURPOSE, AND WHAT IS THE
    OPERATIONAL STATE OF THAT QUEUE?
```

This boundary SHALL be preserved.

## 13. Relationship to Ponos / Ergon

Petasos supports inter-Ergon information flow and may support execution-related communication used by Ponos.

Petasos is not subordinate to Ponos and SHALL NOT be characterised merely as the Ponos messaging subsystem.

```text
Ergon A
   |
   | PetasosParcel
   v
Purpose-defined Petasos Queue
   |
   v
Ergon B
```

Ponos owns execution-framework responsibility.

Ergon owns bounded activity logic.

Petasos owns the durable queueing/event-sharing mechanism used where applicable between those processing boundaries.

## 14. Architectural Guardrails

1. **Petasos is Harmonia's durable message/event queue-management component.**
2. **Petasos manages creation and operational status of its queues.**
3. **Petasos queues are purpose-defined Harmonia queues, not semantically arbitrary generic queues.**
4. **Inter-Ergon information flow is an important Petasos purpose but does not exhaust Petasos responsibility.**
5. **Petasos may support other defined Harmonia communication, notification and event-sharing services.**
6. **Every Petasos queue carries the single common datatype PetasosParcel.**
7. **PetasosParcel is passed by value.**
8. **Information associated with a PetasosParcel is predominantly represented by reference to information managed elsewhere.**
9. **PetasosParcel SHALL NOT be described as merely a pointer/reference record.**
10. **Petasos SHALL NOT be described as transporting the full referenced information payload by default.**
11. **PetasosParcel is conceptually analogous to FHIR Communication; literal FHIR equivalence is not established.**
12. **Petasos ownership of a Parcel does not transfer ownership of referenced governed information to Petasos.**
13. **ActionableTask handoff is a Petasos use case, not the definition of Petasos.**
14. **For ActionableTask handoff, Petasos does not transport a FulfillmentTask; each undertaking establishes its own FulfillmentTask.**
15. **Petasos is not merely a Ponos messenger.**
16. **Petasos owns queue operational state; Energeia may observe/report that state without assuming queue-management responsibility.**
17. **Applicable forwarding actions are enforced locally using Themis-owned PEP capability.**
18. **Detailed queue technology and physical topology remain downstream concerns.**

## 15. Deliberately Unresolved

This DesignNote does not determine:

- final queue taxonomy;
- final queue names;
- queue configuration/registration mechanism;
- physical broker/queue technology;
- physical persistence mechanism;
- queue topology;
- queue partitioning;
- ordering guarantees;
- delivery semantics;
- retry/recovery algorithm;
- dead-letter behaviour;
- consumer coordination;
- queue scaling;
- detailed queue-health model;
- detailed PetasosParcel schema;
- whether PetasosParcel is eventually represented using FHIR Communication or only remains conceptually analogous;
- detailed reference types carried by PetasosParcel;
- amount of embedded Parcel-specific content;
- exact Petasos/Themis enforcement interaction;
- exact Petasos/Kleio audit interaction;
- event correlation semantics;
- detailed Journey reconstruction from Petasos observations.

These are downstream design matters.

## 16. Summary

Petasos manages the creation and operational status of durable, purpose-defined Harmonia message/event queues.

Those queues support inter-Ergon information flow and other architecturally defined communication, notification and event-sharing services. Every Petasos queue carries one common datatype: **PetasosParcel**.

The PetasosParcel itself is passed by value. It represents the queued communication/event and carries its own identity and communication/operational metadata. The information associated with that communication/event is predominantly represented through references to governed information managed elsewhere.

Conceptually, PetasosParcel may be thought of as analogous to a FHIR Communication object.

**Petasos manages the durable queue and the Parcel; it does not acquire ownership of the information to which the Parcel refers.**
