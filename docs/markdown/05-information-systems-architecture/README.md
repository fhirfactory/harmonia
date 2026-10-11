<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->

# Domain05 — Information Systems Layer Architecture

**Standing: initial Harmonia Candidate Information Systems Architecture,
2026-10-11.** This is the canonical RADS record of the reconstructed candidate,
not the final Solution Architecture baseline. Inclusion does not establish
that a component or feature is required for R1.x/R2.x. Absence of upstream
traceability at this stage does not invalidate a documented candidate.

## Purpose and completion boundary

Harmonia already has substantial solution design. This first Domain05 activity
reconstructs that design from repository documentation, preserving established
RADS boundaries and explicitly retaining competing or incomplete candidate
meanings. It does not derive another solution from Domains01–04 or use source
code, Maven modules, classes, deployment units or technology products to supply
missing architecture.

The authorised Domain05 approach is:

1. Document the Candidate Architecture — **this initial reconstruction**.
2. Map upstream capability and information architecture and establish traceability.
3. Identify genuine architecture gaps against that upstream architecture.
4. Identify excess candidate features/components and establish the MVP Candidate Architecture.
5. Establish the implementation plan.

Only Step 1 has been performed. Capability mapping, Information Architecture
coverage, upstream requirements gap analysis, excess-feature analysis, MVP
determination, final baselining and implementation planning remain future work.
The [documentation gap register](gaps.md) records insufficiencies within the
documented candidate, not unsupported upstream requirements. This initial
record does not declare the Domain Completion Gate satisfied.

## Read the candidate

| View | Contents |
| :--- | :--- |
| [System map](candidate-system-map.md) | Responsibility-based software hierarchy; separate collaboration map; construct standing. |
| [Component register](component-register.md) | Roles, features, significant internal constructs, information, persistence, collaborators and exclusions. |
| [Candidate feature map](candidate-feature-map.md) | Documented solution features and allocations, including retained proposals and unclear owners. |
| [Data and persistence](data-and-persistence.md) | Coupled logical/application Data Objects; information-management and persistence responsibilities; competing persistence structures. |
| [Internal middleware](middleware.md) | Internal messaging, recoverable handoff, dispatch, access and context collaboration, separated from external integration and products. |
| [Documentation/component-feature gaps](gaps.md) | Evidence, incomplete meaning and possible adjudication for each gap. |
| [Sources and reconciliation](sources.md) | Authority, legacy provenance, reviewed scope, disposition and section-specific supersession. |

The emerging Information Systems Layer model is preserved as follows:

```text
Information Systems Layer
  Enabling Capability Layer + Enterprise Capability Layer — mapping deferred
  Logical Data Layer                         ┐
  Application Feature Map                    ├ analysed together where documented
  Application Component Map                  ┘
    Data Objects / Information Responsibility / Persistence Responsibility
  Middleware Map — internal component collaboration
  Data / Persistence Layer — persistence structures, without selecting platforms
```

## Authority and interpretation

The [Architectural Axioms](../governance/architectural-axioms.md), repository
[AGENTS.md](../../../AGENTS.md), [documentation instructions](../../AGENTS.md)
and [Completion Plan](../architecture-completion-plan.md) govern. Established
RADS decisions constrain reconstruction; legacy material supplies candidate
solution meaning. A historical `[IMPLEMENTED]`, `Accepted` or `authoritative`
label is not a new Domain05 approval. Accepted ADRs retain their governance role
subject to the axioms; recording their meaning here does not revoke a decision.

Material constraints are AX-01–04 (health-information purpose, native models,
private internal semantics and technology independence), AX-05/11/12 (governed
active access, durable establishment and developer support), AX-06–09
(information authority, security, meaningful evidence and transient state),
AX-10/14/15/16 (recoverable collaboration, distinct outcomes, uncertainty and
observable entity/activity progression), AX-13 (non-destructive fail-closed
publication), and AX-17/18 (explicit architectural uncertainty and no inferred
domain authority). Each view identifies the relevant consequences. This is
reconstruction within those boundaries, not a material redesign.

Source identifiers are local documentary keys defined in [Sources](sources.md).
Register identifiers C, F, D, M and CDG support navigation only; they are not
upstream capability, requirement or Information Concept identifiers. Unknown
means the inspected documentation does not establish the detail. It does not
mean the feature cannot exist or has been rejected.

<a id="task-decisions"></a>

## Task-specific candidate decisions

The explicit 2026-10-11 task instruction establishes the following candidate
solution meanings, recorded as **U01** for traceability:

- `ActionableTaskArchetype` is an Information Architecture meta-concept, not
  necessarily an individually realised Information Asset. Its current candidate
  logical embodiment is `PragmaFactory`; the factory's detailed contract is unknown.
- Both `ActionableTask` and `FulfillmentTask` may be represented through `Pragma`,
  with `WorkOrder`, `ToDo`, `Stimulus` and `Effector` candidate forms. Sharing a
  representation preserves the distinct work-instance and undertaking meanings.
  The forms are not four new Information Resources or an inheritance decision.
- **Ergon is an Application Architecture behavioural construct. It has no
  discrete Information Architecture representation merely by virtue of being
  an Ergon. Its information significance is expressed through the information
  resources it acts upon or generates.** No `Ergon` Information Resource is created.
- Praxis definition/composition/instance questions remain open wherever source
  meanings conflict. `ReportedTask` remains deferred under Domain04.

These decisions extend the recorded candidate posture; they do not claim that
legacy sources already specify them or complete Information Architecture
traceability. The [Domain04 Task model](../04-information-architecture/information-families/task-work.md)
and [Dokimasia finding](../04-information-architecture/reviews/dokimasia-assurance-architectural-finding.md)
remain controlling for their established meanings and deferrals.
