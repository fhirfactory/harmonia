# DesignNote — Energeia Operational Management and Praxis Responsibility

**Status:** Working architectural adjudication  
**Scope:** Harmonia Domain05 — Information Systems Layer Architecture  
**Purpose:** Preserve the current architectural reasoning concerning Energeia as a discrete Software Component, its responsibility for Praxis, and its R1.x/R2.x operational-management boundary.  
**Authority:** DesignNote only. This note records agreed working architecture for subsequent reconciliation into RADS; it does not itself supersede canonical repository documentation.

## 1. Context

Energeia had previously been useful as an architectural boundary around Harmonia execution constructs including Ponos, DigitalTwin, Dokimasia, Ergo, Ergon and Erga. That alone did not provide a sufficiently strong reason for Energeia to exist as a discrete Software Component rather than merely as a grouping or boundary.

The adjudication of Praxis provides that missing responsibility.

Praxis is the higher-order operational construct above Pathway and Journey. Responsibility for Praxis does not belong to Mneme, Ponos or Dokimasia. Praxis establishes operational and assurance context for Harmonia activity and therefore provides Energeia with a distinct application responsibility of its own.

This, in turn, exposes a broader Energeia responsibility: management of the operation of the Harmonia framework itself, distinct from management of the technical infrastructure upon which Harmonia runs.

## 2. Architectural Vocabulary

At this level of architecture, relationship terminology SHALL be explicit.

- **Aggregation** means architectural aggregation.
- **Composition** means architectural composition and SHALL NOT be inferred merely because one construct is described as including, having or using another.
- **Association** indicates relationship without implying aggregation or composition.
- The terms “contain” and “contains” SHOULD be avoided where they would leave the intended relationship ambiguous.

## 3. Praxis

Praxis is a **“super Pathway/Journey”** construct. It provides an operational envelope that sits above the individual Pathway, Publish/Subscribe and Journey representations used within Harmonia.

Praxis has two principal roles.

### 3.1 Operational envelope

Praxis carries operational information that is not naturally a property of an individual Pathway or of the Publish/Subscribe model. This includes, at least conceptually:

- externally visible interface details;
- overall Topic exclusions;
- potentially overall Topic inclusions;
- other Praxis-wide operational detail and constraints;
- aggregation/association of relevant Pathway and Journey constructs.

The exact Praxis information model, lifecycle, versioning semantics, activation/retirement semantics and cardinalities remain to be designed.

A Praxis may have **zero or one Pathway**:

```text
Praxis
  └── Pathway [0..1]
```

The absence of a Pathway does not imply the absence of Praxis. Activity governed by a Praxis may progress through Publish/Subscribe mechanisms without requiring a defined Pathway.

### 3.2 Assurance basis

Praxis is the construct **against which assurance rules are written**.

A useful working distinction is:

- **Praxis** provides the assurance basis.
- **Journey** provides the observed evidence subject.
- **Dokimasia** performs governed assurance activity against that basis and evidence.

Journey SHALL remain a representation of what Harmonia observed. Assurance interpretation, including AI-assisted interpretation, SHALL NOT rewrite Journey into a representation of what an assurance process inferred happened.

## 4. Praxis Ownership

Praxis responsibility belongs to **Energeia**.

It does **not** belong to Mneme merely because Praxis and its associated constructs require governed information representations.

The distinction is:

- Mneme manages governed information required by Harmonia.
- Energeia owns the operational responsibility represented by Praxis.
- Ponos consumes Praxis in support of execution.
- Dokimasia consumes Praxis as an assurance basis.

This establishes a concrete responsibility for Energeia independently of its subordinate or associated execution constructs.

## 5. Energeia as a Discrete Software Component

Energeia SHALL be treated as a discrete Harmonia Software Component rather than solely as a grouping or architectural boundary around Ponos, DigitalTwin, Dokimasia, Ergo, Ergon and Erga.

Its emerging responsibility is:

> **Energeia is responsible for management of Harmonia's operational activity.**

Conceptually:

```text
Energeia
│
├── Praxis Management Services
├── Harmonia Operational Observation
├── Harmonia Operational Monitoring
├── Harmonia Operational Reporting
├── Ponos
│     ├── DigitalTwin
│     └── Dokimasia
├── Ergo / Ergon / Erga
└── future operational-management capabilities
```

This diagram does not assert Composition relationships. The precise Application Component, Application Feature, Application Service and relationship model remains subject to Domain05 reconciliation.

## 6. Praxis Management Services

Energeia requires explicit **Praxis Management Services**.

The precise decomposition remains to be designed, but candidate responsibilities include:

- Praxis definition and registration;
- Praxis discovery and retrieval;
- management of Praxis operational configuration;
- management of externally visible interface information;
- management of Praxis-level Topic inclusion/exclusion rules;
- management of Pathway relationship/aggregation;
- association of applicable assurance rules;
- management of other Praxis-wide operational constraints.

Praxis lifecycle management is recognised as a required concern, but its exact semantics are unresolved. This DesignNote does not establish versioning, activation/deactivation, replacement, retirement, treatment of in-flight activity when a Praxis changes, or whether all candidate responsibilities above are separate Application Features or Services.

## 7. Harmonia Operational Management

There are two distinct operational-management concerns.

### 7.1 IT / Operations management

The external IT/Ops framework manages the technical substrate on which Harmonia executes: Kubernetes/container/platform health, JVM/runtime health, database/platform health, infrastructure capacity, network health, technical telemetry and underlying platform resilience.

This answers:

> **Is the technical platform on which Harmonia runs operating correctly?**

### 7.2 Harmonia operational management

Energeia manages the operational activity of **Harmonia as Harmonia**.

Relevant observations include:

- work-to-be-done;
- work-being-done;
- work-done;
- outstanding ActionableTasks;
- relationship between ActionableTasks and their undertakings;
- stalled or incomplete activity;
- Journey progression;
- Praxis-related operational observations;
- unexpected or anomalous operational behaviour;
- operational information required for reporting and assurance.

This answers:

> **Is Harmonia itself operating as expected?**

## 8. Work-to-be-done and Work-done

Energeia's operational-management responsibility requires visibility of both the obligation to perform work and the observed result/progression of that work.

This does not transfer information-management responsibility for ActionableTask or FulfillmentTask from Mneme to Energeia.

The responsibility distinction remains:

```text
Mneme       manages governed work information
Energeia    observes/reports Harmonia operational activity
Ponos       manages execution/progression
Ergon       performs bounded activity
Journey     represents observed progression
Dokimasia   performs governed assurance activity
```

## 9. R1.x / R2.x Boundary

For R1.x and R2.x, Energeia's Harmonia operational-management responsibility is deliberately limited to **observation, monitoring and reporting**:

```text
Observe → Monitor → Report
```

R1.x/R2.x SHALL NOT introduce autonomous adaptation merely because Energeia can observe Harmonia operation.

Operational observations SHALL NOT automatically confer authority to autonomously alter execution behaviour, workload distribution, routing, platform capacity, resilience behaviour, Praxis, Pathway or Publish/Subscribe semantics.

## 10. R3 Direction — Adaptive Load and Resilience

R3 is expected to introduce an **Adaptive Load and Resilience Service**.

This is a future architectural direction, not an R1.x/R2.x requirement and not yet a detailed design.

```text
R1.x / R2.x:
Observe → Monitor → Report

R3:
Observe → Analyse → Decide → Adapt
   ▲                         │
   └─────────────────────────┘
```

The R1.x/R2.x observability foundation should support later adaptive capability without embedding R3 autonomous behaviour prematurely.

A future adaptive service SHALL operate from governed Harmonia operational observations and explicitly defined control policies. The ability to observe execution SHALL NOT itself confer arbitrary authority to modify execution.

## 11. Relationship to Dokimasia

Energeia operational management and Dokimasia assurance are related but are not synonymous:

```text
IT/Ops
  Is the technical platform operating correctly?

Energeia Operational Management
  Is Harmonia operating as expected?

Dokimasia
  Does observed Harmonia activity conform to
  the applicable Praxis and assurance rules?
```

Energeia operational observation may identify activity warranting assurance examination. Dokimasia remains the specialised governed assurance execution construct.

## 12. Emerging Energeia Feature Set

The following are candidate Domain05 features/responsibilities requiring reconciliation against the Candidate Architecture:

1. Praxis Management Services.
2. Harmonia Operational Observation.
3. Harmonia Operational Monitoring.
4. Harmonia Operational Reporting.
5. Existing Ponos execution responsibilities.
6. Existing DigitalTwin specialised execution responsibilities.
7. Existing Dokimasia specialised assurance execution responsibilities.
8. Existing Ergo / Ergon / Erga activity-framework responsibilities.
9. R3 Adaptive Load and Resilience Service — future/deferred.

This list does not require each item to become a separate Software Component.

## 13. Guardrails

1. **Energeia is not merely a namespace or grouping.** It has discrete operational-management responsibilities.
2. **Praxis responsibility belongs to Energeia, not Mneme.**
3. **Information management and operational management remain distinct.**
4. **Ponos does not own Praxis merely because it uses Praxis during execution.**
5. **Dokimasia does not own Praxis merely because assurance rules are written against Praxis.**
6. **Journey remains observational.** Assurance interpretation does not rewrite observed evidence.
7. **R1.x/R2.x operational management is observational/reporting-oriented, not autonomously adaptive.**
8. **R3 adaptive capability is acknowledged but deferred.**
9. **Do not manufacture Business Architecture retrospectively merely to justify these housekeeping responsibilities.** The absence of explicit upstream Business Architecture should be recorded honestly and reviewed later if necessary.
10. **Do not infer Composition where only Aggregation or Association has been established.**

## 14. Unresolved Questions

The following remain deliberately unresolved:

- exact Praxis lifecycle and versioning semantics;
- exact Praxis data/information representation;
- exact Praxis-to-Journey cardinalities;
- precise Aggregation/Association/Composition relationships among Praxis, Pathway, Journey and related constructs;
- detailed externally visible interface model;
- whether Topic inclusion is required in addition to Topic exclusion;
- detailed assurance-rule representation and attachment mechanism;
- detailed operational monitoring model and thresholds;
- which Energeia responsibilities are Application Features versus Application Services versus subordinate Software Components;
- detailed R3 Adaptive Load and Resilience architecture;
- additional Energeia functions that may naturally belong to its newly established operational-management responsibility.

These questions SHALL remain visible rather than being completed from implementation convention.

## 15. Working Summary

> **Energeia is a discrete Harmonia Software Component responsible for management of Harmonia's operational activity. It owns Praxis responsibility and provides Praxis Management Services. Praxis establishes the operational and assurance envelope above Pathway, Publish/Subscribe activity and Journey. For R1.x/R2.x, Energeia observes, monitors and reports Harmonia operation, including work-to-be-done and work-done, without autonomously adapting execution. R3 is expected to extend this foundation with an Adaptive Load and Resilience Service.**
