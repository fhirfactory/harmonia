<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — sever current EA operational dependencies on Legacy-RAD

Task-Issued-At: UNRESOLVED

The operative user instruction entered this Codex interaction on 2026-10-22
(environment date). Its attributable instruction-entry timestamp is not exposed
to this agent. Neither the current clock nor file/commit metadata substitutes
for that event. The required timestamp filename prefix is consequently unresolved
for Solution Architect treatment before closure. The instruction wording below
is the available attributable interaction source. Repository instructions are
`AGENTS.md`; originating Git state is
`7fda4410a9d3583d674d9530710b86b4ec748c2a`, root release `1.1.5`, predecessor
`1.1.4`. The existing RAD-1.1.5 holistic review remains pending; this task does
not supply its acceptance.

Correspondence: [HCRL 1.1.6-001](../../../../../review/1.1.6/hcrl-1.1.6-001.pending.md),
DR-1 and DR-2. This is direct implementation authority from the operative user
instruction, not inferred acceptance of an agent proposal.

## Operative issued instruction

## Goal

Sever all **current Enterprise Architecture operational dependencies** on Legacy-RAD.

`enterprise-architecture/` must become the self-contained home of current EA architecture, governance, design/evidence, and operational tooling.

## Expected Outcome

Current EA operations work entirely from:

- `enterprise-architecture/continuum/`
- `enterprise-architecture/governance/`
- `enterprise-architecture/design/`
- `enterprise-architecture/tooling/`

Move/promote/reimplement current EA tooling, schemas, configuration, build entry points, validation, projection, Archi round-trip, publication, and standing verification as necessary.

Legacy-RAD remains intact for archaeology, historical evidence, and reproducibility, but supplies **no current operational behaviour, semantics, configuration, schema, or authority**.

Historical provenance references into Legacy-RAD remain valid.

## Context

The Solution Architect has already determined that current Continuum operations improperly depend on `docs/archimate/framework/` and current governance still references material under `docs/markdown/governance/`.

Known examples include `architecture_continuum.py`, `continuum_workbench.py`, their direct/transitive helper dependencies, Open Exchange schemas, and the current Continuum build targets presently hosted by the historical framework Makefile.

`architecture_identity.py` is RAD-era migration/adoption machinery and is **not** to be modernised into the current environment.

## Constraints

Use your engineering judgement to determine the smallest coherent implementation.

Do **not** load or analyse the whole Harmonia architecture, Continuum corpus, Legacy-RAD corpus, historical releases, or governance corpus.

Inspect files **on demand as concrete dependencies require**. Expand scope only where dependency evidence demonstrates it is necessary.

Do not redesign Harmonia architecture or governance, semantically modify Continuum content, modernise historical machinery, repair historical tests/evidence, or perform opportunistic cleanup.

Promote **required current capability**, not historical subsystems merely because current code imports them.

Current governance authority belongs under `enterprise-architecture/governance/`. Historical copies remain historical.

All EA-specific operational tooling belongs under `enterprise-architecture/tooling/`; do not leave `scripts/` as a second current EA tooling location.

## Authorised Activity / Scope

You may inspect and modify current EA tooling, its demonstrated direct/transitive dependencies, current EA governance references, required schemas/configuration, tests, and build entry points.

Legacy files may be inspected selectively to understand capability being severed. Modify Legacy-RAD only if strictly necessary to mark/reference its historical standing; otherwise preserve it.

You are authorised to make sensible implementation and packaging decisions within these boundaries without seeking approval for each file.

If you discover a matter requiring an architectural/governance decision rather than an implementation decision, **do not invent the decision**. Record it for Solution Architect review.

Acceptance criterion:

> Normal current EA operations must succeed without executable, configuration, schema, semantic, or authority dependency on Legacy-RAD.

This includes Continuum validation, governed creation, Open Exchange projection, Archi review/apply, manifest/publication generation, and current standing verification.

## Report

Write the implementation report under `.junie/reports/`.

Report:
- changes made;
- current capabilities promoted/reimplemented;
- dependencies eliminated;
- remaining references to Legacy-RAD and why each is historical/provenance-only;
- validation performed;
- any unresolved matters requiring Solution Architect review.

Do not broaden the task to produce another general governance analysis.
