# DesignNote — Pragma Management Responsibility and Energeia Boundary

**Status:** Architectural adjudication / working design note  
**Context:** Domain05 Candidate Information Systems Architecture review  
**Date:** 2026-10-11

## Context

During review of the reconstructed Domain05 Candidate System Map, the placement of `Pragma` beneath `Energeia` was challenged.

The initial line of reasoning considered introducing a peer runtime framework beneath Energeia, analogous to Petasos for message management, responsible for management of Pragma. Further analysis identified a more fundamental responsibility distinction: Pragma is not primarily an execution or transport construct. It is a managed Application Data Object / runtime information construct representing governed work and activity information.

The architectural question therefore becomes one of **information-management responsibility**, rather than proximity to execution.

## Adjudicated Direction

Pragma management belongs with **Mneme**, rather than Ponos, Petasos, or Energeia merely because those runtime constructs use it.

Conceptually:

```text
Mneme
  └── Pragma Management
        ├── PragmaFactory
        └── Pragma
```

Pragma is then consumed or acted upon by other runtime responsibilities:

```text
Mneme / Pragma Management
        │
        │ manages
        ▼
      Pragma
        │
        ├──── used/progressed by ────> Ponos
        │
        ├──── acted upon by ─────────> Ergon
        │
        └──── handed off via ────────> Petasos
```

The key architectural principle is:

> **Execution of, behaviour upon, or transport of managed information does not confer information-management responsibility for that information.**

## Responsibility Separation

The current conceptual separation is:

```text
Pragma
    = managed information / Application Data Object

Mneme / Pragma Management
    = governed information-management responsibility

Ponos
    = activity / execution responsibility

Petasos
    = message / recoverable-handoff responsibility

Ergon
    = behavioural construct acting upon or generating governed information
```

This is consistent with the broader Domain05 distinction between Information Responsibility, execution/activity responsibility, middleware/handoff responsibility, and persistence responsibility.

Persistence responsibility remains separate and is not adjudicated by this note.

## PragmaFactory

This placement also provides a coherent architectural home for `PragmaFactory`.

The current conceptual relationship remains:

```text
ActionableTaskArchetype
        │
        │ logically embodied through
        ▼
   PragmaFactory
        │
        │ creates / initialises
        ▼
      Pragma
        │
        ├── may represent ActionableTask
        └── may represent FulfillmentTask
```

`ActionableTaskArchetype` remains a Domain04 meta-concept and is not thereby turned into an instantiated Information Asset.

PragmaFactory is therefore better understood as part of the governed Pragma-management responsibility than as an execution responsibility of Ponos or Praxis.

## Implication for the Reconstructed Candidate System Map

The current reconstructed Candidate System Map places `Pragma — task/context/progression carrier construct` beneath `Energeia`.

That structural placement is now considered suspect and should be corrected during Candidate Architecture adjudication.

Pragma may be central to Energeia activity without belonging structurally to Energeia.

Likewise, Ponos, Petasos and Ergon may use, progress, act upon, or transfer Pragma-associated work without acquiring information-management ownership of Pragma.

## Implication for Energeia

This decision does **not** yet settle the complete Energeia hierarchy.

In particular, this DesignNote does not adjudicate:

- the precise architectural nature of Energeia;
- the final relationship between Ponos and Energeia;
- whether Petasos is structurally within or merely collaborates with Energeia;
- the precise relationship between Praxis, Ergo, Ergon and Erga;
- the complete Pragma-management feature set;
- detailed Pragma lifecycle semantics;
- persistence responsibility for Pragma;
- TaskOutcome persistence or management responsibility.

Those matters remain for subsequent Candidate Architecture review.

## Working Boundary

The emerging responsibility boundary can be expressed as:

```text
Mneme
    WHAT INFORMATION IS MANAGED?
              │
              │ Pragma
              ▼
Energeia / Ponos
    WHAT ACTIVITY IS EXECUTED?
              │
              ▼
          Ergo / Ergon

Petasos
    HOW IS WORK RELIABLY HANDED
    BETWEEN EXECUTION BOUNDARIES?
```

This is a working architectural view intended to preserve the adjudicated responsibility separation while avoiding premature completion of the remaining Energeia design.
