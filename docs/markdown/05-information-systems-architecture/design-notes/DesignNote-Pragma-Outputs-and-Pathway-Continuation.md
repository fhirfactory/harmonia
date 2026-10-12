<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# DesignNote --- Pragma Outputs and Pathway Continuation

## Status

**Architecturally adjudicated DesignNote — 2026-10-12.**

**Architecture Layer:** Domain05 — Information Systems Layer.

This note records the explicit human architectural adjudication registered as
[U18](../sources.md#u18), reconciled into the Domain05 Candidate Architecture.
It does not accept the whole candidate, change A4 standing, commence Step B or
authorise implementation. The [Architectural Axioms](../../governance/architectural-axioms.md)
remain controlling; the [Domain05 scope](../README.md) applies.

## 1. Purpose

Distinguish Pragma independently resulting from Ergon execution from the Pragma
used to continue fulfilment of the current Praxis along its Pathway. Preserve
the corresponding boundary between Journey progression and complete Pragma
provenance without prescribing new components or representations.

## 2. Pathway and Praxis

**A Pathway defines the conceptual sequence of Ergo through which execution
progresses.** Established PathwayEndpoints, PathwaySegments and Routes express
the controlled interactions available along that sequence; their existing
meanings remain intact.

**A Praxis defines how that Pathway is to be fulfilled**, including:

- the Context definitions applicable to execution;
- expected criteria/outcomes associated with Pathway segments;
- the semantics required to process one segment's result into the input required
  by the next; and
- other Praxis-level housekeeping required for fulfilment.

Praxis manages the definition of Context required by the Ergo framework. Mneme
manages Context materialisation and use; the framework applies the applicable
definitions. The existing non-exhaustive likely resource set identified by an
Ergo and supported bounded Adhoc access remain compatible with that division.

This clarifies fulfilment semantics within Praxis's established operational and
assurance envelope. It preserves Praxis Aggregation of Pathway [0..1], including
the established possibility of Pub/Sub activity without a predefined Pathway.
It does not make Praxis an orchestrator, choreographer or execution engine.

## 3. Outputs and continuation

Execution of an Ergon may produce one or more outputs. Processing those outputs
may result in multiple Pragma. **Not every resulting Pragma implicitly progresses
the current Pathway.**

The **Pathway-continuation Pragma** represents the derived information required
to continue fulfilment of the current Praxis along its Pathway. Praxis defines
the semantics by which that continuation is derived. The execution framework
performs the applicable output processing and routing using those semantics.

Conceptually:

```text
Pragma X
    |
    v
  Ergo A
    |
    v
Pragma X.output
    |
    v
Process Pragma Output
    |
    +----> Independent Pragma P1 ---> Mneme Pub/Sub / other governed use
    |
    +----> Independent Pragma P2 ---> another Praxis / other consumer
    |
    +----> Pathway-continuation Pragma X'
                              |
                              v
                            Route
                              |
                              v
                            Ergo B
```

This is a conceptual distinction, not a processing algorithm, identity rule,
mandatory fan-out cardinality or physical pipeline. Other resulting Pragma are
independently available governed artefacts: they may participate in Mneme
Publish/Subscribe, another Praxis, other governed Harmonia processing, or no
further processing. Availability does not imply publication, selection, handoff
or execution, and does not bypass applicable governance.

## 4. Continuation is a role; representation is deferred

**Pathway-continuation is a role played by a Pragma.** This adjudication creates
no Pragma subtype or physical representation. In particular, `X'` in the diagram
denotes derived continuation information, not a mandated new object identity.

Future detailed modelling must determine whether:

1. a new Pragma is always constructed for continuation;
2. one of the resulting output Pragma may be designated as the continuation
   Pragma; or
3. another governed representation is appropriate.

That decision is explicitly deferred. No schema, designation mechanism, factory
extension, copy/version rule or persistence mechanism follows from this note.

## 5. Journey consequence

Journey represents meaningful observed progression in fulfilment of a Praxis
against its Pathway. The lineage of Pathway-continuation Pragma therefore
provides an important observable thread for Journey progression.

**Journey lineage is not equivalent to complete Pragma provenance.** Independent
Pragma created from the same Ergon outputs may establish their own subsequent
activity, Pub/Sub interactions, Praxes and Journeys. Their common origin alone
does not make all subsequent activity progression of the originating Praxis.

Mneme may manage richer Pragma relationships and provenance than the Journey
associated with the originating Praxis represents. This does not transfer
Journey management from Energeia or authoritative audit responsibility from Kleio.
Journey may reference relevant governed observations without becoming a complete
information-provenance graph, execution log, audit trail or Petasos queue history.
Existing Pub/Sub-only and mixed, potentially graph-shaped Journeys remain valid;
this note does not require every Journey to have a Pathway or prescribe correlation
or reconstruction algorithms.

## 6. Responsibility boundaries

| Responsibility | Established allocation |
| :--- | :--- |
| Praxis, Pathway and Journey management | Energeia retains established management responsibilities; Praxis defines fulfilment, applicable Context and continuation semantics. |
| Governed execution, output processing and routing | Ponos/Ergo execution framework applies those semantics; Ergon supplies bounded behaviour within the existing contract. Praxis does not orchestrate or choreograph execution. |
| Context and Pragma management; semantic Publish/Subscribe | Mneme retains governed information management, creation/initialisation through established PragmaFactory responsibility, selection and availability. |
| Durable physical handoff, where applicable | Petasos retains concrete persistent handoff obligations, queue operations and recovery; semantic selection remains Mneme's. |
| Authoritative durable information and audit | Mnemosyne establishes durable state; Kleio retains authoritative audit capture. Neither responsibility is replaced by continuation lineage or Journey. |

## 7. Release boundary

For **R1.x/R2.x, Context requirements remain concretely defined**. The existing
likely resource declarations and bounded runtime Adhoc acquisition do not
establish criteria-driven dynamic determination of Context.

**R3.x may introduce criteria-driven dynamic Context determination and dynamically
configured reusable/generic Ergo.** This is deferred architectural direction only.
No R3.x mechanism is designed and no AI-driven selection/configuration is
introduced into the current architecture by this adjudication.

## 8. Axiom consistency, reconciliation and retained conflicts

- **AX-14:** preserves the distinction between independently resulting Pragma
  and the continuation role without requiring a separate type.
- **AX-08/09/16:** preserves meaningful observed progression while keeping
  information provenance, audit and transient execution/handoff mechanics distinct.
- **AX-04/05/12/18:** preserves semantic definition, governed execution,
  information management, durable state and originating authority boundaries.
- **AX-07/15/17:** retains governed operations, uncertain outcomes and explicitly
  deferred representation and release decisions without inventing machinery.

The [shared semantic view](../pathways-journeys-and-handoff.md#pathway-continuation),
[Context/Pragma view](../context-and-persistence-management.md#pragma-output) and
affected registers incorporate this decision without reproducing the full note.
[Source reconciliation](../sources.md#u18) records the reviewed views and limits.
[CDG-53](../gaps.md#continuation-representation) retains the representation decision;
existing detailed lifecycle, routing and Journey contracts remain open.

U18 reported the repository
[AGENTS.md §2](../../../../AGENTS.md#2-system-taxonomy--module-hierarchy)
**“Workflow orchestration (Praxis)”** wording conflict without changing it.
[U19](../sources.md#u19) subsequently closes that conflict through the explicitly
authorised **Pathway fulfilment management** correction: Praxis defines Context,
expected criteria and housekeeping and is neither orchestrator nor choreographer.
Previously superseded historical Praxis engine/blueprint descriptions remain
historical; the continuation representation remains deferred.
