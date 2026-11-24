<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Enterprise Architecture documentation publications

Task-Issued-At: UNRESOLVED

The operative instruction entered this Codex interaction on 2026-10-22
(interaction date). Its attributable system instruction-entry timestamp is not
exposed to the agent. The required timestamp filename prefix also remains
unresolved; neither the current clock nor filesystem/Git metadata substitutes
for that event. Solution Architect treatment is required before closure.

Issuer: Mark Hunter, Solution Architect (repository owner and interaction user).
Agent: Codex. Resulting release: 1.1.8; predecessor: 1.1.7; HCRL iteration: 001.
Originating HEAD: `ca7c471d6a2cc65b5bb2c52a031d9fde27b7f474`; entry worktree clean;
root release `1.1.7`, previous `1.1.6`. The linked 1.1.7 pending HCRL is retained
unchanged; its completed register record establishes the accepted predecessor.

## Operative user instruction (verbatim)

## Goal

Restructure the Enterprise Architecture documentation so that documentation is a first-class concern under `enterprise-architecture/`, and establish the Architecture Continuum RAD as the human-readable narrative publication of the canonical Architecture Continuum.

Produce the change as the next governed architecture release: **1.1.8**, with previous version **1.1.7**.

## Expected Outcome

The current:

`enterprise-architecture/continuum/documentation/`

is relocated to:

`enterprise-architecture/documentation/continuum-manifest/`

and all current tooling, references and navigation are updated accordingly.

A second documentation product is established at:

`enterprise-architecture/documentation/continuum-rad/`

The resulting structure is approximately:

```text
enterprise-architecture/
├── continuum/
├── documentation/
│   ├── continuum-manifest/
│   └── continuum-rad/
├── governance/
├── design/
└── tooling/
```

`continuum-manifest/` remains the structural/catalogue-style publication of the Continuum.

`continuum-rad/` becomes the navigable, human-readable LaTeX architectural document derived from the Architecture Continuum.

The existing LaTeX RAD material is the starting point for the Continuum RAD, but the resulting publication must reflect the current Architecture Continuum and Enterprise Architecture model rather than perpetuating the superseded RAD as architectural authority.

## Context

Release 1.1.7 formally accepted the reconciled Architecture Continuum as the canonical architectural baseline.

The documentation currently located beneath `continuum/` should no longer imply that documentation is intrinsic canonical Continuum state. Documentation is a publication of that state and belongs alongside the other first-class Enterprise Architecture concerns.

There are two deliberately different documentation products.

### Continuum Manifest

`documentation/continuum-manifest/`

This is the existing generated structural/catalogue representation of the Architecture Continuum.

Its relocation should preserve its current purpose and behaviour.

### Continuum RAD

`documentation/continuum-rad/`

This is the narrative architectural publication.

Its document structure should approximately — around 95% — reflect the organisation of the Enterprise Architecture / ArchiMate model.

For example:

```text
Part I      Motivation

Part II     Strategy
  Chapter ... Business Capabilities
  Chapter ... Business Enabling Capabilities
  ...

Part III    Business
  ...

Part IV     Application
  ...

Part V      Technology
  ...

Part ...    other applicable EA model domains
```

The actual current EA model determines the publication hierarchy. Do not invent an independent RAD taxonomy where the governed model already supplies the organisation.

The remaining publication-specific structure may accommodate normal document concerns such as front matter, introductions, indexes, appendices and publication metadata.

The Continuum RAD may include explanatory narrative and diagrams that make the existing architecture understandable to a human reader.

AI-assisted documentation may infer **how to explain and present existing architecture**. It is not authorised to infer new architecture.

The governing rule for this work is:

> **Document the Continuum. Do not improve the Continuum while documenting it.**

## Constraints

Do **not** modify canonical Continuum architectural content as part of this work, except release metadata required by the normal 1.1.7 → 1.1.8 release process.

Do **not** introduce new architectural elements, relationships, requirements, constraints, principles, standing decisions or architectural assertions.

Do **not** use documentation generation as an architecture-discovery or architecture-improvement process.

If existing architecture appears incomplete, inconsistent, difficult to explain or potentially capable of improvement, leave the Continuum unchanged. Record only a genuine blocker if one prevents publication.

Do **not** redesign the Enterprise Architecture governance framework.

Do **not** create a new general-purpose documentation framework or “documentation factory”.

Do **not** reopen the RAD → Continuum migration.

Do **not** treat the old RAD as current architectural authority.

Do **not** require manually authored Archi views merely because a useful publication diagram does not already exist.

AI-generated explanatory text and diagrams must be grounded in existing governed Continuum content. They may select, summarise, organise and explain existing architectural facts; they must not silently manufacture new architectural facts.

Use the existing publication/tooling machinery wherever practical. Modify or extend it only as necessary to implement this bounded documentation change.

## Authorised Activity / Scope

You are authorised to:

- relocate `enterprise-architecture/continuum/documentation/` to `enterprise-architecture/documentation/continuum-manifest/`;
- update current EA tooling, Makefiles, scripts, links, references and navigation affected by that relocation;
- establish `enterprise-architecture/documentation/continuum-rad/`;
- inspect and reuse the existing LaTeX RAD publication material as the starting point for the Continuum RAD;
- adapt the LaTeX document hierarchy so that it substantially reflects the current EA/ArchiMate model structure;
- update terminology and authority statements necessary to reflect the Architecture Continuum as canonical authority;
- generate explanatory narrative from existing Continuum content where appropriate;
- generate useful explanatory diagrams from existing Continuum elements and relationships where appropriate;
- preserve publication-specific front matter, indexes, appendices and similar document concerns where useful;
- update release metadata from 1.1.7 to 1.1.8;
- perform the existing deterministic validation, publication and release checks appropriate to the change;
- update current navigation/register material required by the existing governance process.

The Continuum RAD should be useful to an architect or developer trying to understand Harmonia, rather than merely being a textual serialization of the model.

However, when choosing between improving the architecture and faithfully documenting the architecture, choose faithful documentation.

If implementation requires an architectural decision rather than a documentation/presentation decision, stop and report that matter for Solution Architect review.

## Report

Retain the formal Task and Report as governed **1.1.8 evidence** using the current Enterprise Architecture evidence conventions:

- Task under the appropriate `enterprise-architecture/design/evidence/1.1.8/.../before/ai-agent-tasks/` location.
- Report under the corresponding `enterprise-architecture/design/evidence/1.1.8/.../after/ai-agent-reports/` location.

Agent-specific working locations such as `.junie/tasks/` or `.junie/reports/`, if used, are transient working state and are not substitutes for governed evidence.

The Report should describe:

- relocation of the Continuum Manifest;
- establishment and structure of the Continuum RAD;
- existing LaTeX material reused or adapted;
- publication/tooling/reference changes required;
- how correspondence with the EA model structure was achieved;
- AI-generated narrative or diagram mechanisms introduced or used;
- validation/publication checks performed;
- confirmation that canonical Continuum architectural content was not altered by the documentation activity;
- any genuine issue requiring Solution Architect review.

Do not produce another general analysis of the Continuum, migration, governance framework or development factory.

Attached repository input:
`enterprise-architecture/design/review/1.1.7/hcrl-1.1.7-001.pending.md`.

## Applicable authority and bounded review

The issued instruction supplies explicit relocation/application authority and
the 1.1.8 release allocation. The Architecture Review Praxis and Change Provenance
contract govern this iteration. AX-04 (existing publication machinery), AX-08/09
(purpose-selected evidence), AX-14 (publication/authority/standing distinctions)
and AX-17 (no invented architectural facts) apply. No runtime/module architecture
changes are needed. Pending/qualified content remains qualified. Only presentation,
links, tooling, release metadata and this iteration's review/evidence may change.

## Operative hierarchy clarification (verbatim)

The user supplied this steering instruction during the same authorised task.
Its attributable instruction-entry timestamp is also unavailable; the later clock
query is not substituted. This clarification governs the final implementation.

> Do not invent the RAD document hierarchy. Derive the RAD hierarchy from the existing Enterprise Architecture model hierarchy. ArchiMate/EA domains become LaTeX Parts; the governed organisational structure within each domain becomes Chapters and subordinate sections. Publication-only material is the exceptional ~5%, not the organising structure.

Attached repository input:
`enterprise-architecture/design/review/1.1.7/hcrl-1.1.7-001.pending.md`.

## Operative publication-path correction (verbatim)

This follow-up belongs to the same authorised 1.1.8 iteration, before acceptance.
Its attributable instruction-entry event is **UNRESOLVED** because the system
does not expose that message timestamp. A later clock reading is not substituted.

> One final publication correction before acceptance: rename the generated published/<release>/chapters/ directory to published/<release>/parts/ and update all generator paths, LaTeX includes, publication metadata, tests and reproducibility expectations accordingly. The files in this directory represent the EA-domain/LaTeX Part level (Motivation, Strategy, Business, Application), not Chapters. Do not otherwise change the RAD structure or content. Re-run only the checks required to establish the corrected publication and update the pending HCRL/Report evidence accordingly.

Attached repository input:
`enterprise-architecture/design/review/1.1.8/hcrl-1.1.8-001.pending.md`.

## Operative completion instruction (verbatim)

Issuer: Mark Hunter, Solution Architect, in the same Codex interaction.
Instruction-entry event: **UNRESOLVED**; the message timestamp is not exposed to
the agent. No current clock or file/Git time is substituted. This follow-up
retains its actual authority and does not create another release or iteration.

> hcrl-1.1.8-001.pending.md updated. complete

Attached/reviewed input:
`enterprise-architecture/design/review/1.1.8/hcrl-1.1.8-001.pending.md`.
The opening table records the Solution Architect's **Accept** dispositions for
DR-1–DR-3. DR-3 expressly accepts the disclosed temporal provenance and states
that no further remediation is required. The completion instruction confirms the
reviewed result; earlier architectural qualifications remain unchanged. The exact
reviewed-input hash is recorded in the completed register and closure checks.
