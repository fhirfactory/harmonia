<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia Architectural Axioms

The Architecture Continuum Principle elements linked below are the sole current
normative definitions of Harmonia's architectural axioms and retain the highest
architectural authority. Their canonical meaning, wording and identity are unchanged.
This page provides navigation and governance context; it does not define the axioms.

**One definition; many references.** Current governance, guidance, documentation
and tooling reference these Principles rather than maintain independent normative
copies. Context about their application does not redefine them. Derived publications
remain reading projections of canonical content, with no independent authority.
Historical registers, release publications and evidence retain their historical roles.

The [original register](../../docs/markdown/governance/architectural-axioms.md) preserves unchanged source sections and supersession correspondence. The [representation contract](continuum-representation.md) and [HCRL 002](../design/review/1.1.5/hcrl-1.1.5-002.pending.md) record application and historical review standing; [RAD-1.1.7 acceptance](../design/register/1.1.7/hcrl-1.1.7-001.complete.md) records prospective holistic acceptance with individual standing and residual matters preserved.

<a id="ax-01"></a>
- [AX-01 Harmonia Is Health-Information Centric](../continuum/motivation/principle/e9bd61acaaea4577bc4086a5639e52c3.md)
<a id="ax-02"></a>
- [AX-02 Standards at the Boundary; Harmonia Within the Boundary](../continuum/motivation/principle/eee8c3783d964d18a7f9dcea85fcd4cf.md)
<a id="ax-03"></a>
- [AX-03 Native Standards Models Remain Native](../continuum/motivation/principle/e99a4e2bf99f476d876394300d6b9c3d.md)
<a id="ax-04"></a>
- [AX-04 Harmonia Owns the Semantics; Engines Provide the Machinery](../continuum/motivation/principle/54508cda80484a6caf95b901780eaf7d.md)
<a id="ax-05"></a>
- [AX-05 Active State and Authoritative Durable State Are Distinct](../continuum/motivation/principle/0412a2084f0145babc4f022c71fea65f.md)
<a id="ax-06"></a>
- [AX-06 Information Authority Is Explicit](../continuum/motivation/principle/952c3b6148ce4fce94b69f99a0c2fec7.md)
<a id="ax-07"></a>
<a id="kleio-mandatory-capture-authority"></a>
- [AX-07 Security Is Intrinsic to Managed Operations](../continuum/motivation/principle/5de2ae2c3b794c2d9f3a14efd51d564f.md)
<a id="ax-08"></a>
- [AX-08 Evidence Records Meaning, Not Machinery](../continuum/motivation/principle/21d6864657964ba5b03e3f6b5eb29cd5.md)
<a id="ax-09"></a>
- [AX-09 Transient Operational State Is Ephemeral by Default](../continuum/motivation/principle/9a5cb9e8876844e58e660f909f7df9fc.md)
<a id="ax-10"></a>
- [AX-10 Distribution, Load and Failure Are Normal Operating Conditions](../continuum/motivation/principle/0d9673833eb840eab03804aa076c1b0c.md)
<a id="ax-11"></a>
- [AX-11 Managed Information Access Is Highly Available and Responsive](../continuum/motivation/principle/8a1e995f16954484a78ae1c5c114d546.md)
<a id="ax-12"></a>
- [AX-12 Hide Plumbing, Not Information](../continuum/motivation/principle/ce85889bd9b34f79a53d4c907ceb19d4.md)
<a id="ax-13"></a>
- [AX-13 Harmonia Management Has an Explicit Boundary](../continuum/motivation/principle/b6a55decfeec4f1bb3d7f18534a2ffea.md)
<a id="ax-14"></a>
- [AX-14 Semantic Distinctions Are Preserved](../continuum/motivation/principle/d3fc6025dc7646d1b2020b51f7a44bfa.md)
<a id="ax-15"></a>
- [AX-15 Uncertainty Is Preserved Until Resolved](../continuum/motivation/principle/ef8abbaec37e4890ad78b9f263e67906.md)
<a id="ax-16"></a>
- [AX-16 Operational Activity & Entity State Progress Together](../continuum/motivation/principle/de7e90f46127455a9ec9ad3b9b48ba54.md)
<a id="ax-17"></a>
- [AX-17 Architectural Authority and Explicit Uncertainty](../continuum/motivation/principle/8882573df7ce43c6bc0f66da0a676139.md)
<a id="ax-18"></a>
- [AX-18 Integration and Coordination Do Not Confer Domain Authority](../continuum/motivation/principle/b8cafe3cf5904d2bb9a9767260fddf78.md)

## 1. Purpose

The individual Principles linked above are the maintained axiom definitions.

The linked Continuum Principles define the Architectural Axioms of the Harmonia
Health Integration Environment (HIE). The axioms describe the fundamental
architectural truths upon which Harmonia is designed. They establish the
boundaries within which architecture decisions, subsystem designs,
implementation patterns and technology choices are made. The axioms are
intentionally more stable than individual Architecture Decision Records
(ADRs), requirements, design contracts or implementation decisions. The
architectural authority hierarchy is:

```text
Harmonia Vision and Purpose
    │
    ▼
Architectural Axioms
    │
    ▼
Architecture Decisions
    │
    ▼
Architectural Invariants and Guardrails
    │
    ▼
Design Contracts and Requirements
    │
    ▼
Implementation
    │
    ▼
Automated Conformance Tests
```

An ADR, requirement, design or implementation SHALL NOT knowingly
contradict an Architectural Axiom.

Where existing implementation or documentation conflicts with an axiom,
the conflict SHALL be identified and resolved explicitly. Existing
implementation is not, by itself, evidence of architectural intent.

The governance and review guidance on this page applies the linked Principles;
it supplies no alternative axiom definitions. The keywords SHALL, SHALL NOT,
SHOULD, SHOULD NOT and MAY govern that application context.

## 6. Relationship to Architecture Decisions

Architecture Decision Records explain consequential design choices made
within the constraints established by these axioms.

An ADR MAY refine how an axiom is realised.

An ADR SHALL NOT silently override an axiom.

Where Harmonia's architectural principles genuinely change, the affected
axiom SHALL be explicitly reconsidered and the consequences assessed
across existing ADRs, requirements, documentation, implementation and
tests. Changes to an axiom therefore require a higher level of
architectural consideration than ordinary implementation or design
changes.

## 7. Architectural Review Rule

Before proposing a material architectural change, the following
questions SHALL be answered:

1. Which axioms apply?
2. Does the proposed change preserve them?
3. Does an existing implementation conflict with them?
4. Can the required behaviour be provided by existing Harmonia or underlying platform capabilities rather than introducing another mechanism?
5. Can the resulting architectural constraint be mechanically enforced?

Where the answer reveals an architectural conflict, the conflict SHALL
be resolved explicitly before implementation proceeds.
