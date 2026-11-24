# DesignNote --- Agora Controlled Collaboration and Omilia Realisation

**Status:** Working Architecture DesignNote\
**Architecture Layer:** Domain05 --- Information Systems Layer\
**Purpose:** Record the current adjudicated Candidate Architecture for
Harmonia.Agora managed collaboration and its realisation through the
Omilia collaboration subsystem.\
**Authority:** Working design record only. This DesignNote is not RADS
authority until reconciled into the canonical `/docs/markdown`
architecture.

------------------------------------------------------------------------

## 1. Purpose

This DesignNote records the current architectural decisions concerning:

-   Harmonia.Agora;
-   Agora-managed Rooms/Spaces;
-   Room/Space membership control;
-   general collaboration and Teams/Slack-like functionality;
-   the distinction between Agora-native collaboration and
    clinical/operational messaging conveyed through Agora;
-   the capture of defined user interactions from managed Rooms into
    Harmonia processing;
-   the relationship between Agora and the collaboration-platform
    worker;
-   the Harmonia-specific collaboration subsystem **Omilia**;
-   the intended derivation of Omilia.Server from Matrix Synapse;
-   the intended derivation of Omilia.Client from Element;
-   the boundary between collaboration control, collaboration operations
    and Harmonia application processing.

This note intentionally does not complete detailed Agora information
persistence/custody design or implementation design.

------------------------------------------------------------------------

## 2. Architectural Position

### 2.1 Agora is Harmonia's Collaboration Control Component

Harmonia.Agora provides the Harmonia application-level semantics and
control for collaboration.

Agora supports native collaboration functionality comparable in broad
intent to Teams/Slack, including Rooms/Spaces, dialogue and
collaboration media.

For Rooms/Spaces that Harmonia manages, Agora controls:

-   Room/Space creation and lifecycle intent;
-   participant membership intent;
-   addition and removal of members;
-   information published by Harmonia into the Room/Space;
-   observation of dialogue and activity occurring within the
    Room/Space;
-   capture of defined dialogue/actions for introduction into wider
    Harmonia processing.

Agora does not itself perform the underlying collaboration-platform
operations. Those operations are delegated to Omilia.Server through its
Application Services API.

The governing distinction is:

> **Agora controls; Omilia operates.**

------------------------------------------------------------------------

## 3. Omilia

### 3.1 Naming

The Harmonia-specific collaboration subsystem is named:

-   **Harmonia.Omilia.Server**
-   **Harmonia.Omilia.Client**

`Omilia` (Ομιλία) conveys conversation, speech or discourse and
identifies the collaboration subsystem independently of the upstream
products from which it is derived.

### 3.2 Provenance

The intended software lineage is:

-   **Harmonia.Omilia.Server** --- cloned and modified from Matrix
    Synapse.
-   **Harmonia.Omilia.Client** --- cloned and modified from Element.

The upstream lineage is important for technical, licensing and
provenance purposes but does not define the architectural identity of
the Harmonia components.

Conceptually:

``` text
Harmonia.Agora
      |
      | Application Services API
      v
Harmonia.Omilia.Server
      |
      | Matrix protocol
      v
Harmonia.Omilia.Client
```

### 3.3 Omilia.Server Responsibility

Omilia.Server owns the operational mechanics of the collaboration
environment.

This includes the underlying operations for:

-   all Rooms/Spaces;
-   all user activity;
-   all membership activity;
-   dialogue/message operations;
-   collaboration-platform events;
-   applicable media operations;
-   exposure of collaboration operations and events through the
    Application Services API.

Omilia.Server is therefore the operational worker used by Agora.

This does not make Omilia.Server the owner of Harmonia collaboration
policy, semantics or control decisions.

### 3.4 Omilia.Client Responsibility

Omilia.Client is the Harmonia-specific collaboration client derived from
Element.

It provides the collaboration user experience associated with the Omilia
collaboration environment.

[U19](../sources.md#u19) establishes that **Iris does not directly interact with
Agora or Omilia**. Detailed Omilia.Client responsibilities and human-experience
integration remain later design matters within that boundary. Administrative
intent through Omilia → Agora proceeds to an Administrative Function implemented
by specialised Administrative Ergon; Iris uses its separate Iris-BackEnd path.
Neither presentation/collaboration channel directly resumes or manipulates a
Digital Twin. Normal authorisation, Context, execution, Petasos handoff and Kleio
audit controls apply; this adds no direct Agora-to-Ponos dependency.

------------------------------------------------------------------------

## 4. Application Services API

Omilia.Server exposes an **Application Services API** used by
Harmonia.Agora.

Through this interface Agora can instruct Omilia.Server to perform
collaboration operations and can receive dialogue/activity occurring
within Agora-managed Rooms/Spaces.

Conceptually:

``` text
Agora decides                         Omilia.Server performs
----------------------------------------------------------------
Create managed Room/Space     -----> create Room/Space
Change managed Room/Space     -----> modify Room/Space
Add participant               -----> perform membership addition
Remove participant            -----> perform membership removal
Publish information           -----> publish into Room/Space
Listen to dialogue            <----- expose Room/Space activity
```

The Application Services API is the architectural boundary between:

-   **Harmonia collaboration control and semantics**, owned by Agora;
    and
-   **collaboration-platform operations**, owned by Omilia.Server.

The exact API technology and contract are deferred design.

------------------------------------------------------------------------

## 5. Rooms and Spaces

### 5.1 General Collaboration

Agora owns Harmonia's native collaboration behaviour.

General-purpose collaboration is therefore within Agora's scope rather
than being treated as an external capability.

This may include:

-   direct dialogue;
-   group dialogue;
-   Rooms/Spaces;
-   collaborative discussion;
-   collaboration media;
-   participant/member management;
-   collaboration state.

The exact Room/Space taxonomy remains open. `Room` and `Space` are
retained as working concepts pending detailed design.

### 5.2 Agora-Managed Rooms/Spaces

A subset of Rooms/Spaces are **Agora-managed**.

These Rooms/Spaces may be established in response to Harmonia
representations and operational contexts including, for example:

-   Patient;
-   Practitioner;
-   Location;
-   Service;
-   other governed Harmonia entities or contexts where later
    architecture establishes a requirement.

For these managed Rooms/Spaces, Agora controls the Harmonia intent while
Omilia.Server performs the underlying collaboration operations.

------------------------------------------------------------------------

## 6. Membership Control

Agora controls membership of its managed Rooms/Spaces.

Omilia.Server owns and performs the underlying user and membership
operations.

Therefore:

> **Agora determines who should be added to or removed from an
> Agora-managed Room/Space; Omilia.Server performs the corresponding
> collaboration-platform membership operation.**

Conceptually:

``` text
Operational / Harmonia context
            |
            v
          Agora
      membership control
            |
            | Application Services API
            v
      Omilia.Server
      membership operation
            |
            v
      Managed Room/Space
```

### 6.1 Roster Boundary

Roster Management is a **Separately Provided Capability (SEP)**.

For R1.x/R2.x Harmonia requires only the applicable:

-   Shift Start; and
-   Shift End

periods needed to establish relevant operational context.

Roster information may therefore inform Agora's membership decisions,
but the roster capability does not control Agora-managed Room
membership.

Agora remains responsible for the Harmonia membership-control decision.

No R1.x/R2.x requirement is established here for Harmonia to own:

-   roster construction;
-   scheduling;
-   workforce allocation;
-   shift management;
-   substitution management; or
-   general workforce availability management.

------------------------------------------------------------------------

## 7. Agora as Collaboration Owner and Harmonia Conduit

Two information behaviours must remain distinct.

### 7.1 Agora-Native Collaboration

Where information is created as part of native Agora collaboration,
Agora owns/manages the collaboration state and collaboration record.

Examples include:

-   Room dialogue;
-   collaborative discussion;
-   native collaboration messages;
-   Room/Space state;
-   collaboration membership state;
-   native collaboration media.

### 7.2 Harmonia Information Conveyed Through Agora

Agora may also act as a conduit between other Harmonia components and
users.

Examples may include:

-   clinical notifications;
-   practitioner operational messages;
-   workflow/task notifications;
-   escalations;
-   approvals;
-   diagnostic-result availability notifications;
-   other Harmonia-managed information intended for a practitioner or
    team.

Where such information originates elsewhere in Harmonia:

> **Presentation or publication through Agora does not transfer
> authoritative ownership of the originating information to Agora.**

Agora owns the collaboration representation/activity necessary to convey
the information, while the originating Harmonia responsibility retains
ownership of the underlying clinical, operational, workflow or other
governed information.

------------------------------------------------------------------------

## 8. Managed Rooms as Harmonia Interaction Surfaces

Agora-managed Rooms are not merely passive display surfaces.

Agora listens to dialogue and activity occurring within its managed
Rooms through Omilia.Server acting as the collaboration worker.

Conceptually:

``` text
               Agora-Managed Room
                       |
          +------------+-------------+
          |                          |
          v                          v
 Harmonia publishes           Practitioner/User
 information into Room        dialogue or action
          |                          |
          |                          v
          |                    Omilia.Server
          |                          |
          |                Application Services API
          |                          |
          +------------------------> Agora
                                     |
                              observe / capture
                                     |
                         +-----------+-----------+
                         |                       |
                         v                       v
                  collaboration           defined meaningful
                     record                  interaction
                                                 |
                                                 v
                                      Communication Information
                                      Unit and/or Pragma
                                                 |
                                                 v
                                         Harmonia processing
```

------------------------------------------------------------------------

## 9. Canonical Example --- Diagnostic Report Review

A canonical example is:

1.  Harmonia determines that a new Diagnostic Report is available.

2.  The applicable Harmonia component causes information to be published
    into the relevant Patient managed Room through Agora.

3.  Agora instructs Omilia.Server to perform the publication.

4.  The Room displays a message such as:

    **New Diagnostic Report Available**

5.  A Practitioner interacts in the Room, for example by entering text
    or activating a defined action corresponding to:

    **Report Reviewed**

6.  Omilia.Server exposes that Room activity to Agora through the
    Application Services API.

7.  Agora recognises/captures the defined interaction.

8.  The captured interaction is introduced into Harmonia as applicable
    Communication Information Unit and/or Pragma.

9.  The established Harmonia execution framework performs the resulting
    governed processing.

10. Applicable logging and audit responsibilities are exercised.

The Diagnostic Report does not become Agora-owned merely because
notification of it was published into the Room.

Likewise, the original Agora dialogue/action is not transferred out of
Agora merely because it results in new Harmonia-managed information or
activity.

------------------------------------------------------------------------

## 10. Capture versus Ownership Transfer

The architectural model is **capture/derivation**, not ownership
transfer.

> **Agora retains authoritative responsibility for its native
> collaboration record. Defined interactions occurring within managed
> Agora Rooms may additionally produce new Harmonia-managed information
> or activity. Such capture does not transform or transfer ownership of
> the originating Agora dialogue; it creates a governed Harmonia
> representation of the captured interaction.**

Conceptually:

``` text
Agora dialogue/action
        |
        | capture
        v
new Harmonia information/activity
        |
        !=
ownership transfer of Agora record
```

This avoids requiring a generic mechanism by which an Agora conversation
is "promoted" into a formal clinical record.

Where an Agora interaction causes formal clinical or operational
information to be created, that new information is created through the
appropriate governed Harmonia processing and information-management
responsibility.

Provenance/reference between the collaboration record and resulting
Harmonia information may be required, but its detailed representation is
deferred.

------------------------------------------------------------------------

## 11. Dialogue Capture, Logging and Audit

Dialogue within Agora-managed Rooms is captured/retained as part of
Agora's collaboration responsibility.

Activity within managed Rooms is logged.

Certain defined activities are additionally auditable according to
applicable Harmonia audit policy.

The following must remain distinct:

``` text
Captured dialogue  != AuditEvent
Logged activity    != AuditEvent
Audit-worthy event -> Kleio -> authoritative audit information
```

Agora does not become the authoritative audit manager.

Where an activity is auditable, Agora reports the applicable occurrence
and Kleio retains its established responsibility for authoritative audit
capture.

The exact set of auditable Agora activities is not established by this
DesignNote.

------------------------------------------------------------------------

## 12. Semantic Interpretation Boundary

Agora's observation of Room dialogue does not imply that every utterance
automatically becomes Harmonia work.

In particular:

> **Not every message, keystroke or utterance in a managed Room becomes
> a Pragma.**

Agora may capture **defined meaningful interactions** for introduction
into Harmonia processing.

These may include explicitly defined user actions such as:

-   Report Reviewed;
-   Acknowledge;
-   Accept;
-   Escalate;
-   Request Review;

or other interactions defined through later architecture/design.

The mechanism by which free text may be recognised as a defined
interaction is not established here.

Agora should not acquire unconstrained clinical semantic interpretation
merely because it observes collaboration dialogue.

------------------------------------------------------------------------

## 13. Responsibility Model

The current responsibility boundary can be summarised as:

  -----------------------------------------------------------------------
  Responsibility                      Owner
  ----------------------------------- -----------------------------------
  Harmonia collaboration              Agora
  semantics/control                   

  Control of Agora-managed Room/Space Agora
  lifecycle intent                    

  Control of membership intent for    Agora
  Agora-managed Rooms/Spaces          

  Control of Harmonia publication     Agora
  into managed Rooms/Spaces           

  Listening/capture policy for        Agora
  managed Rooms/Spaces                

  Native Agora collaboration          Agora
  information/state                   

  Room/Space operational mechanics    Omilia.Server

  User/membership operational         Omilia.Server
  mechanics                           

  Dialogue/event delivery mechanics   Omilia.Server

  Collaboration client                Omilia.Client

  Matrix protocol/platform            Omilia subsystem
  realisation                         

  Roster Management                   SEP

  R1.x/R2.x roster input              Shift Start / Shift End only

  Underlying clinical/operational     Originating Harmonia responsibility
  information published through Agora 

  Processing of captured              Applicable Harmonia execution
  Communication Information / Pragma  framework

  Authoritative audit information     Kleio
  -----------------------------------------------------------------------

------------------------------------------------------------------------

## 14. Architectural Invariants

The following Candidate Architecture invariants are established:

1.  **Agora controls; Omilia operates.**
2.  Agora owns Harmonia collaboration semantics and control.
3.  Omilia.Server owns the underlying collaboration-platform operations.
4.  Omilia.Server exposes an Application Services API consumed by Agora.
5.  Agora controls the lifecycle intent of its managed Rooms/Spaces.
6.  Agora controls membership intent for its managed Rooms/Spaces.
7.  Omilia.Server performs the corresponding Room/Space and membership
    operations.
8.  Roster Management is SEP.
9.  For R1.x/R2.x, Harmonia requires Shift Start/End periods from the
    roster capability as applicable operational context.
10. Roster-derived context may inform Agora membership control but does
    not own that control.
11. Agora provides native Teams/Slack-like collaboration capability.
12. Agora owns/manages its native collaboration state and record.
13. Agora may also act as a conduit for clinical, operational and other
    Harmonia-managed information.
14. Publication through Agora does not transfer ownership of underlying
    source information to Agora.
15. Agora listens to dialogue/activity in all Agora-managed Rooms
    through Omilia.Server.
16. Not every Room utterance becomes Harmonia work.
17. Defined Room interactions may be captured and introduced into
    Harmonia as Communication Information Units and/or Pragma.
18. Capture creates new governed Harmonia information/activity; it does
    not transfer ownership of the original Agora collaboration record.
19. Captured dialogue, logging and authoritative audit are distinct
    concerns.
20. Kleio remains responsible for authoritative audit information.
21. Omilia.Server is derived from Matrix Synapse but has Harmonia
    architectural identity.
22. Omilia.Client is derived from Element but has Harmonia architectural
    identity.

------------------------------------------------------------------------

## 15. Deferred Design / Discovery

The following are intentionally unresolved:

-   final terminology and taxonomy for Room versus Space;
-   exact criteria for creation of entity/context-associated managed
    Rooms;
-   detailed lifecycle rules for managed Rooms;
-   detailed membership rules and membership derivation;
-   exact use of PractitionerCapacity in Agora membership decisions;
-   exact use of Shift Start/End in membership decisions;
-   detailed Application Services API contract;
-   technology-specific API mechanism;
-   exact Omilia.Server modifications relative to Synapse;
-   exact Omilia.Client modifications relative to Element;
-   detailed Omilia.Client / Iris human-experience relationship, preserving
    U19's no-direct-interaction boundary;
-   detailed storage/persistence architecture for Agora-native
    collaboration records;
-   detailed binary/media custody and persistence;
-   retention policies for collaboration dialogue/media;
-   provenance representation between captured Agora interactions and
    resulting Harmonia information;
-   exact Communication Information Unit representation for captured
    interactions;
-   exact Pragma specialisation(s) used for captured interactions;
-   rules determining which dialogue/actions constitute defined
    meaningful interactions;
-   any NLP/AI interpretation of free-text dialogue;
-   exact set of auditable Agora activities;
-   detailed logging architecture;
-   detailed security/privacy controls for Room content and membership;
-   detailed external/federated Matrix interaction;
-   detailed notification/escalation mechanics.

These matters SHALL NOT be inferred merely from Synapse, Element,
Matrix, Teams, Slack or other collaboration implementations.

------------------------------------------------------------------------

## 16. Candidate Architecture Status

The following are considered sufficiently adjudicated for Candidate
Architecture purposes:

-   Agora owns Harmonia collaboration control and semantics.
-   Omilia.Server is the operational collaboration worker.
-   Omilia.Client is the Harmonia collaboration client.
-   Agora controls managed Room/Space lifecycle and membership intent.
-   Omilia.Server performs the underlying Room/Space and membership
    operations.
-   Agora may provide native general collaboration while also conveying
    information originating elsewhere in Harmonia.
-   Agora listens to all dialogue/activity in its managed Rooms via
    Omilia.Server.
-   Defined managed-Room interactions may be captured into wider
    Harmonia processing.
-   Capture does not transfer ownership of the original Agora
    collaboration record.
-   Roster Management is SEP; R1.x/R2.x requires Shift Start/End only.

Detailed persistence, media custody, retention, security mechanics and
interaction representations remain design/discovery matters unless
subsequent architecture adjudication determines that one is necessary to
close the Candidate Architecture.

------------------------------------------------------------------------

## 17. Concise Architecture Statement

> **Harmonia.Agora is Harmonia's collaboration control component. It
> owns Harmonia collaboration semantics, controls designated
> Rooms/Spaces and their membership, publishes Harmonia information into
> those collaboration contexts, and observes their dialogue for defined
> interactions that may be captured into governed Harmonia processing.
> Harmonia.Omilia.Server, derived from Matrix Synapse, acts as the
> collaboration worker and performs Room/Space, user, membership,
> messaging and event operations through an Application Services API.
> Harmonia.Omilia.Client, derived from Element, provides the associated
> collaboration client. Agora owns its native collaboration record but
> does not acquire ownership of clinical or operational information
> merely because that information is conveyed through an Agora-managed
> Room.**
