<!-- continuum-source-succession -->
> **RAD-1.1.5 source succession:** Historical source and navigation; all architectural sections incorporated. See the [canonical continuum](../../../../enterprise-architecture/documentation/continuum-manifest/published/1.1.5/README.md) and [section correspondence](../../../../enterprise-architecture/design/evidence/1.1.5/002/after/source-succession.json). Original source wording and standing below are preserved.

<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Metabolē — Bounded Information Progression

**Standing: adjudicated conceptual Information Architecture extension,
2026-10-13, recorded for Domain05 B1 reconciliation.** This is the canonical
definition of Metabolē. Recording the supplied decisions does not declare B1
PASS or commence B2 capability mapping.

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[BusinessObject] Metabolē](../../../../enterprise-architecture/continuum/business/business-object/c34b4929f71946708d37dbe6c1da920a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## 1. Concept and information boundary

> **Metabolē** is a conceptual information construct representing the bounded
> progression of one or more Information Units through a change or activity.
> A Metabolē incorporates the input information, applicable Context and
> provenance/derivation associated with that progression and accumulates the
> resulting output information.

```text
Metabolē
 ├── Input       [IU 1..*]
 ├── Context
 │    ├── ActivityContext
 │    └── other applicable Context
 ├── Provenance / Derivation
 └── Output      [IU 0..*]
```

The [Information Unit](../metamodel/information-architecture-metamodel.md#6-information-unit)
remains the governed information boundary. Metabolē describes the bounded
progression involving Units; it does not replace their identities, originating
authority, Management Context or lifecycle. Its conceptual partitions do not
prescribe nesting, copying, reference structures, physical representation or a
new universal lifecycle.

Metabolē is conceptual. It is not an application subsystem, persistence service,
execution component or runtime manager. No `MetaboleManager` or equivalent
application responsibility SHALL be inferred.



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[BusinessObject] Metabolē](../../../../enterprise-architecture/continuum/business/business-object/c34b4929f71946708d37dbe6c1da920a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## 2. Architectural Realisation

**Pragma realises Metabolē** for governed Harmonia information/activity
progression. **Pylai.Diadromí realises Metabolē** for transient information
progression through Pylai interoperability activity.

```text
                    Metabolē
               «conceptual construct»
                       ▲
                       │
                   Realisation
                ┌──────┴──────┐
                │             │
             Pragma     Pylai.Diadromí
```

These are genuine architectural **Realisation** relationships in the
architectural/ArchiMate/TOGAF sense: a concrete, domain-specific construct
realises the conceptual construct. They are not analogy, synonymy or merely
conceptual similarity. A realisation may introduce domain-specific lifecycle,
semantics, structure and management responsibility.

Realisation SHALL NOT imply that Pragma and Diadromí inherit from each other,
share a runtime type or application manager, or have identical physical
representations, persistence requirements or lifecycles.

| Realisation | Preserved application responsibility and boundary |
| :--- | :--- |
| [Pragma](../../05-information-systems-architecture/context-and-persistence-management.md#pragma-output) | Mneme retains Context and Pragma management. Execution does not acquire information-management responsibility. Mnemosyne alone establishes authoritative durable state where required. |
| [Pylai.Diadromí](../../05-information-systems-architecture/pylai-external-interoperability.md#terma-and-diadromi) | Pylai retains responsibility for transient interoperability progression. Diadromí remains non-recoverable after loss/termination; appropriate client retry creates a new interaction. Realisation does not make it a Pragma, Task, Ergon or durable handoff. |



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[BusinessObject] Metabolē](../../../../enterprise-architecture/continuum/business/business-object/c34b4929f71946708d37dbe6c1da920a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## 3. Context and ActivityContext

`Metabolē.Context.ActivityContext` is a conceptual semantic classification
describing the broad class of activity represented by the Metabolē. It answers:

> **What broad class of activity is this information progression undertaking?**

The initial architectural vocabulary is:

```text
INFORMATION_REQUEST
INFORMATION_CREATE
INFORMATION_UPDATE

ACTIVITY_INITIATION
ACTIVITY_UPDATE
ACTIVITY_STOP
```

These are initial examples, not an exhaustive enumeration or an implementation
enum. No further values are established merely for symmetry.

ActivityContext is distinct from a specific request action:

```text
Metabolē.Context.ActivityContext = INFORMATION_REQUEST
request.action = SEARCH
request.content = <search criteria/content>
```

The path expresses conceptual semantics, not a required field layout.
ActivityContext neither grants authority nor establishes the policy permission
for a specific action. Its shared model, definition and nomenclature semantics
are governed consistently with
[Calliope](../../05-information-systems-architecture/calliope-models-and-datasets.md);
runtime policy resolution, authorisation decisions and enforcement remain with
Themis. No physical Models/DataSets allocation or new runtime vocabulary service
is decided here.

Metabolē's applicable Context, the Information Unit's
[Management Context](../metamodel/information-architecture-metamodel.md#65-context),
Mneme's [value-based activity Context](../../05-information-systems-architecture/context-and-persistence-management.md#context-values)
and Themis PEP-Context retain their distinct meanings. This classification
does not equate those Contexts or decide their detailed representation/mapping.



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[BusinessObject] Metabolē](../../../../enterprise-architecture/continuum/business/business-object/c34b4929f71946708d37dbe6c1da920a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## 4. Additive progression and information accountability

Service fulfilment may progress through a Metabolē carrying the governing
Context and accumulated information associated with the Service Request.
Functions act upon the information relevant to their defined state transition
and add resulting information to the progressing Metabolē.

The originating request and information established by preceding Functions
remain available to subsequent Functions, subject to applicable
information-management, security and lifecycle constraints. Additive progression
does not require unrestricted visibility, indefinite retention, automatic durable
persistence, or publication of all accumulated information to the client.

**Additive progression does not mean every Function consumes everything already
present within the Metabolē.** Carriage alone establishes neither a
`requires`/`consumes` relationship nor Function accountability. The
[Step B relationship rules](../../05-information-systems-architecture/traceability/step-b-method.md#b1-r03)
identify only relationships material to the Function's state transition.



------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->

<!-- continuum-supersession:start -->
------- Legacy Content - Superseded ------- Start ------
Superseded by canonical continuum element(s), RAD-1.1.5 authorised ingestion:
- [[BusinessObject] Metabolē](../../../../enterprise-architecture/continuum/business/business-object/c34b4929f71946708d37dbe6c1da920a.md)
Original source standing and unresolved qualifications are preserved.
---------------------------------------------------------

## 5. Provenance, evidence and architectural consistency

A progressing Metabolē SHALL retain sufficient provenance/derivation information
to establish how accumulated information was derived and how it relates to the
originating activity/request.

```text
Provenance / derivation ≠ Audit Evidence ≠ Assurance Evidence
```

This is an information obligation, not a new evidence repository or manager.
The [provenance framework](../governance/authority-custody-provenance.md#4-provenance--lineage-framework)
retains derivation meaning; Kleio retains audit information responsibility and
Dokimasia retains assurance responsibility. Transient operational mechanics do
not automatically become durable evidence.

The material [Architectural Axioms](../../governance/architectural-axioms.md) are
AX-01/04 (information meaning independent of machinery), AX-05 (active/durable
separation), AX-07 (intrinsic security), AX-08/09 (meaningful evidence versus
transient state), AX-13 (external management boundary), AX-14 (distinct concepts)
and AX-17/18 (explicit uncertainty and no inferred authority). The conceptual
definition and domain-specific realisations preserve these boundaries. Detailed
schemas, lifecycle/retention mechanisms and application contracts remain
downstream; the concept establishes no new persistence or management authority.


------- Legacy Content - Superseded ------- Finish ----
<!-- continuum-supersession:end -->
