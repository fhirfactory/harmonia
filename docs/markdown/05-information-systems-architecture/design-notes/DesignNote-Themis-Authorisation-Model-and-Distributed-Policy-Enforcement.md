# DesignNote — Themis Authorisation Model and Distributed Policy Enforcement

**Status:** Architectural Design Note  
**Scope:** Harmonia Information Systems Layer Architecture / Domain05 Candidate Architecture Step A  
**Subject:** Themis Authorisation Model, PAP/PRP/PDP responsibilities, PEP-Context, distributed Themis-owned Policy Enforcement Points, availability and delegation

## 1. Purpose

This DesignNote records the current architectural position for **Themis**, Harmonia's Authorisation Model management, policy reasoning and distributed policy-enforcement architecture.

It establishes the component boundary and principal responsibilities needed for Domain05 Candidate Architecture Step A without prematurely selecting a policy language, Criteria representation, cache mechanism, validity/invalidation model, delegation mechanism, physical persistence topology, or implementation framework.

## 2. Fundamental Responsibility

**Themis owns and manages Harmonia's Authorisation Model.**

The Authorisation Model is:

- administered through the **Policy Administration Point (PAP)**;
- maintained as authoritative policy through the **Policy Repository Point (PRP)**;
- evaluated and reasoned over by the **Policy Decision Point (PDP)**; and
- expressed to distributed Policy Enforcement Points through a **PEP-Context** containing the Criteria applicable to an authenticated Identity.

```text
                    THEMIS
        Authorisation Model Management
                       |
                       v
              +-----------------+
              |       PAP       |
              | define / manage |
              | Authorisation   |
              | Model           |
              +--------+--------+
                       |
                       v
              +-----------------+
              |       PRP       |
              | authoritative   |
              | repository      |
              +--------+--------+
                       |
                       v
              +-----------------+
              |       PDP       |
              | policy +        |
              | contextual      |
              | reasoning       |
              +--------+--------+
                       |
                       v
                  PEP-Context
                       |
                       v
              Distributed PEPs
```

Themis is therefore not merely a generic security component. Its architectural purpose is to provide consistent, governed authorisation policy management, reasoning and enforcement semantics across Harmonia.

## 3. Governed Actions

Harmonia components use Themis policy services when they need to determine whether a governed information action may occur.

The principal questions currently established are:

1. **Can I ACCESS XYZ?**
2. **Can I CHANGE ABC?**
3. **Can I FORWARD DEF?**

These verbs SHALL NOT be reduced to CRUD semantics.

```text
ACCESS
    May this authenticated Identity obtain/use
    the governed information?

CHANGE
    May this authenticated Identity alter
    the governed information?

FORWARD
    May this authenticated Identity disclose,
    transmit or hand the governed information
    onward to the relevant destination/context?
```

The same Identity may legitimately receive different outcomes for these different actions.

Likewise, FORWARD may produce different outcomes for different destinations or circumstances.

## 4. Policy Administration Point — PAP

The **PAP** provides the Themis responsibility through which the Harmonia Authorisation Model is defined, administered and governed.

Conceptually, PAP responsibility includes management of authorisation policy and its lifecycle.

The detailed policy-authoring language, governance workflow, approval mechanism, versioning scheme, administration interface and change process are deliberately unresolved.

## 5. Policy Repository Point — PRP

The **PRP** provides the authoritative repository responsibility for the Harmonia Authorisation Model.

Themis owns the repository semantics and authority of the Authorisation Model.

This does not require Themis to own a dedicated physical database or persistence engine.

Durable persistence may be delegated through Harmonia information-management and persistence services:

```text
Themis PRP
    owns authoritative Authorisation Model
    repository responsibility
              |
              | durable persistence may
              | be delegated
              v
            Mneme
              |
              v
 Mnemosyne — Harmonia Configuration
             & Governance
```

The Authorisation Model aligns naturally with the **Harmonia Configuration & Governance (C&G)** persistence responsibility domain.

Persistence responsibility does not transfer ownership of the Authorisation Model from Themis.

## 6. Policy Decision Point — PDP

The **PDP** reasons over the applicable Authorisation Model, authenticated Identity and relevant context to establish the policy information required for enforcement.

The PDP is not restricted to producing a simple YES/NO response.

A PDP result may comprise:

- an immediate decision; and/or
- reusable **Criteria** applicable to the authenticated Identity and relevant context.

The principal reusable product of successful PDP reasoning is a **PEP-Context**.

## 7. PEP-Context

> **PEP-Context is the Themis-derived policy enforcement context established for an authenticated Identity. It contains the Criteria applicable to that Identity that a Themis Policy Enforcement Point requires to make local policy enforcement decisions.**

A PEP-Context is a contextual projection of the Authorisation Model rather than a distribution of the complete Authorisation Model itself.

```text
Authorisation Model
        |
        | PDP reasoning for
        | authenticated Identity
        v
    PEP-Context
        |
        +-- authenticated Identity
        +-- applicable Criteria
        +-- contextual metadata
        +-- relevant provenance
        `-- other required enforcement context
```

The exact PEP-Context schema is deliberately unresolved.

The same authenticated Identity may possess different PEP-Contexts where different relevant contexts require different Criteria.

Criteria belong to the PEP-Context; the PEP-Context applies to the authenticated Identity.

## 8. Policy Enforcement Point — PEP

Policy Enforcement is distributed across Harmonia, because enforcement must occur at the point where the proposed governed action can actually be prevented.

However, the distributed PEP is **Themis-derived, Themis-owned and Themis-built capability**.

A PEP may therefore be realised as a Themis-provided library/framework element embedded within another Harmonia module.

```text
                         THEMIS
             +--------------------------+
             | PAP                      |
             | PRP                      |
             | PDP                      |
             |                          |
             | PEP Library / Framework |
             +------------+-------------+
                          |
                  distributed into
          +---------------+---------------+
          v               v               v
        Mneme           Petasos         Energeia
      [Themis PEP]     [Themis PEP]    [Themis PEP]
```

The host component controls the actual operation. The Themis-owned PEP applies Themis Criteria and provides consistent local enforcement semantics.

> **The PEP instance is distributed; PEP capability and enforcement semantics remain Themis-owned.**

A Harmonia component SHALL NOT implement an independent interpretation of Themis policy in place of the Themis PEP.

## 9. Policy, Criteria and Enforcement Decision

Three concepts SHALL remain distinct:

```text
POLICY
    Authorisation Model managed by Themis
          |
          v
CRITERIA
    PDP-derived contextual expression
    within a PEP-Context
          |
          v
ENFORCEMENT DECISION
    local determination by the PEP
    for a particular proposed action
```

The PDP owns interpretation/reasoning over the Authorisation Model.

The PEP does not require direct access to the PRP or the complete Authorisation Model. It applies the Criteria contained within an applicable PEP-Context.

This prevents each embedded PEP from becoming an independent miniature PDP.

## 10. Themis Availability and In-Flight Activity

Themis PDP availability SHALL NOT, by itself, interrupt an in-flight activity that already possesses a valid PEP-Context sufficient for the policy enforcement decisions required by that activity.

```text
Existing valid PEP-Context
          |
          v
Themis-owned local PEP
          |
          +-- ACCESS?
          +-- CHANGE?
          `-- FORWARD?
          |
          v
Local enforcement continues
even if the PDP is temporarily unavailable
```

Where an activity requires a **new, renewed or replacement PEP-Context**, the PDP must be available to establish that context.

If the PDP cannot establish the required new PEP-Context, the activity SHALL NOT proceed beyond the point requiring that context and must wait until authoritative policy context can be established.

```text
Need new / renewed PEP-Context
             |
             v
       PDP available?
        /          \
      YES           NO
       |             |
       v             v
 establish          WAIT
 PEP-Context
```

PDP unavailability is therefore not itself a policy denial.

The following concepts SHALL remain distinct:

- policy permits;
- policy denies;
- authoritative policy determination cannot presently be established.

The detailed semantics for waiting, timeout, retry, user notification or failure escalation remain downstream concerns.

## 11. PEP-Context Validity

The architecture requires the concept of a **valid PEP-Context** but does not currently define the detailed validity model.

Potential future considerations may include:

- temporal validity;
- policy change;
- Identity change;
- contextual change;
- Praxis change;
- revocation;
- delegation change;
- renewal requirements.

No specific mechanism or trigger is selected by this DesignNote.

## 12. PEP-Context Delegation

A PEP-Context **MAY be delegatable**.

> **A delegated PEP-Context derives its authority from an existing PEP-Context and establishes the Criteria applicable to the authenticated Identity receiving that delegated authority.**

Conceptually:

```text
Authenticated Identity A
        |
        v
   PEP-Context A
        |
        | governed delegation
        | mechanism
        v
   PEP-Context B
        |
        +-- authenticated Identity B
        +-- derived Criteria
        `-- delegation provenance
```

Delegation SHALL remain within the Themis policy architecture and SHALL preserve sufficient provenance to establish the source of delegated authority.

The conditions under which delegation is permitted are deliberately deferred to **detailed implementation design and governance review**.

This DesignNote does not determine:

- whether delegated Criteria may only narrow authority;
- whether authority may be transformed or broadened;
- who may delegate;
- whether delegation requires PDP interaction;
- whether delegation may be performed locally;
- delegation depth or chaining;
- delegation expiry;
- revocation semantics;
- approval requirements;
- representation of delegation provenance.

The architectural capability and provenance requirement are established; the governing rules are not.

## 13. Relationship to Praxis

Praxis and Themis address different concerns.

```text
Praxis
    establishes expected/permitted operational
    context and assurance basis

Themis
    establishes authorisation Criteria applicable
    to authenticated Identities and governed actions
```

A Praxis may provide relevant context to Themis policy reasoning, but Praxis is not the Authorisation Model and does not replace Themis.

Likewise, a Themis authorisation decision does not establish that an observed activity conforms to its Praxis.

## 14. Relationship to Dokimasia

Themis and Dokimasia remain distinct:

```text
Themis
    "May this authenticated Identity perform
     this governed action under the applicable
     Authorisation Model?"

Dokimasia
    "Did Harmonia's observed activity conform
     to the applicable Praxis and assurance rules?"
```

Themis provides authorisation policy reasoning and enforcement context.

Dokimasia provides governed assurance assessment.

## 15. Relationship to Kleio

Themis owns authorisation policy decisions and their reasoning.

Kleio owns authoritative Audit Information Management.

Significant Themis policy activity and enforcement outcomes may constitute auditable occurrences and be reported through Kleio Audit Capture.

```text
Themis / PEP
      |
      | auditable occurrence
      v
    Kleio
      |
      v
Authoritative FHIR AuditEvent
```

Kleio does not become part of Themis policy decision-making merely because it records an auditable occurrence.

Kleio's authoritative audit-persistence stream is a specific exception to the general requirement for components to obtain Themis authorisation before governed information actions, because audit capture must remain capable of authoritatively recording the relevant occurrence.

The exact audit/reporting semantics for Themis decisions and PEP enforcement are deliberately unresolved.

## 16. Architectural Guardrails

1. **Themis owns and manages Harmonia's Authorisation Model.**
2. **Themis provides PAP, PRP and PDP responsibilities.**
3. **The Authorisation Model is administered through the PAP.**
4. **The Authorisation Model is maintained authoritatively through the PRP.**
5. **The PDP reasons over the Authorisation Model, authenticated Identity and relevant context.**
6. **The principal reusable PDP product is a PEP-Context containing applicable Criteria.**
7. **PEP-Context is established for an authenticated Identity within a relevant context.**
8. **The complete Authorisation Model need not be distributed to PEPs.**
9. **Policy, Criteria and local enforcement decision SHALL remain distinct concepts.**
10. **Policy Enforcement occurs locally at the component controlling the governed action.**
11. **Distributed PEP capability, implementation and enforcement semantics are Themis-owned.**
12. **Host components SHALL NOT independently reinterpret Themis policy in place of the Themis PEP.**
13. **ACCESS, CHANGE and FORWARD are distinct governed actions and SHALL NOT be collapsed into CRUD semantics.**
14. **An existing valid PEP-Context is sufficient for continued local policy enforcement during temporary PDP unavailability.**
15. **A new, renewed or replacement PEP-Context requires successful authoritative PDP processing.**
16. **PDP unavailability SHALL NOT be represented as a policy denial.**
17. **PEP-Context MAY be delegatable.**
18. **Delegation SHALL remain within Themis policy architecture and preserve delegation provenance.**
19. **Detailed delegation conditions and mechanisms are deferred to implementation design and governance review.**
20. **Themis application ownership is distinct from Mneme/Mnemosyne information-management and persistence responsibilities.**
21. **Praxis operational permission/expectation is distinct from Themis authorisation.**
22. **Dokimasia assurance is distinct from Themis policy decision-making.**
23. **Kleio audit management is distinct from Themis authorisation, even where Themis decisions are audited.**

## 17. Deliberately Unresolved

This DesignNote does not determine:

- Authorisation Model schema;
- policy language;
- PAP user interface or governance workflow;
- policy approval/change process;
- Criteria schema;
- PEP-Context physical representation;
- whether PEP-Context is represented as a particular Information Unit or Application Data Object;
- PEP-Context transport/distribution mechanism;
- validity duration;
- invalidation and revocation mechanism;
- Criteria refresh semantics;
- policy-change propagation;
- PEP library implementation technology;
- decision algorithm;
- caching implementation;
- detailed delegation rules;
- delegation implementation;
- detailed Themis/Kleio audit interaction;
- detailed persistence topology;
- failure timeout/retry/user-experience behaviour.

These are downstream design and governance matters.

## 18. Summary

Themis is Harmonia's Authorisation Model management, policy reasoning and distributed policy-enforcement architecture.

Themis manages the Authorisation Model through the PAP, maintains its authoritative repository through the PRP, reasons over the model through the PDP, and expresses applicable authorisation Criteria as a PEP-Context established for an authenticated Identity.

Themis-owned PEP capability is distributed into Harmonia components so enforcement occurs at the point capable of preventing the governed action while retaining common Themis enforcement semantics.

Existing valid PEP-Contexts allow in-flight activity to continue during temporary PDP unavailability. Activities requiring a new or replacement PEP-Context must wait until the PDP can authoritatively establish one.

PEP-Contexts may be delegatable, but the conditions and mechanisms governing delegation are deliberately deferred to implementation design and governance review.

**Themis owns the Authorisation Model; the PDP derives Criteria; the PEP-Context carries Criteria; the distributed Themis PEP enforces them.**
