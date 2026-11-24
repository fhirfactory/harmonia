<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — Published ArchiMate release projection

Task-Issued-At: UNRESOLVED — the system instruction-entry timestamp is not exposed in this interaction.

Issuer: Mark Hunter, Solution Architect, through the operative user instruction.
Executor: Codex. HCRL: 1.1.12 iteration 001; immediate predecessor: accepted 1.1.11.
The unavailable event and filename prefix require Solution Architect treatment before
closure. Neither the current clock, file metadata nor commit time substitutes for
the instruction-entry event. The `unresolved-` prefix preserves that limitation.

## Originating state and authority

Task entry has a clean worktree at commit
`ed8121a` (Harmonia Architectural Governance Process (XII)(Continuum 1.1.11)).
Root `architecture-release.json` records 1.1.11 / previous 1.1.10.
The user-linked pending 1.1.11-002 HCRL is historical review input; the completed
`enterprise-architecture/design/register/1.1.11/hcrl-1.1.11-002.complete.md`
records actual acceptance and closure. Both remain unchanged.

Apply AGENTS.md, canonical AX-04/08/14/15/17, Architecture Governance,
Architecture Review Praxis, Architecture Change Provenance, release policy,
Continuum Representation and the existing specialised round-trip contracts.
This task authorises the bounded publication implementation and verification;
it does not authorise HCRL acceptance or round-trip redesign.

## Operative instruction (verbatim)

> # Goal
>
> Establish a durable published ArchiMate Open Exchange XML projection for each published Architecture Continuum release.
>
> Implement the initial published projection using the accepted **1.1.11 Architecture Continuum** as the baseline.
>
> This task addresses a publication omission only. It does not redesign the Architecture Archi-Roundtrip process.
>
> # Expected Outcome
>
> The repository contains a clear, durable location under the existing:
>
> `enterprise-architecture/design/archimate/`
>
> area for published ArchiMate Open Exchange projections.
>
> The accepted 1.1.11 Architecture Continuum has a corresponding published Open Exchange XML artefact suitable for import into Archi as the starting workbench representation of that release.
>
> The repository documentation clearly distinguishes:
>
> 1. the canonical Architecture Continuum;
> 2. the published Open Exchange XML projection of a released Continuum;
> 3. an architect-modified Open Exchange export subsequently produced from Archi.
>
> The published XML is a **release artefact**, not merely test or transition evidence.
>
> # Context
>
> Release 1.1.11 is accepted, committed and pushed.
>
> The current repository contains:
>
> `enterprise-architecture/design/archimate/`
>
> including the existing ArchiMate projection machinery such as:
>
> `projection-template.xml`
>
> and its supporting documentation.
>
> The 1.1.11 release process generated and successfully exercised an ArchiMate Open Exchange projection, including actual Archi import/save/export verification.
>
> However, the usable Open Exchange XML currently appears to be retained principally as release/evidence material rather than as an explicitly published architectural release artefact.
>
> That leaves no obvious permanent file for the Solution Architect to select when beginning subsequent work in Archi.
>
> # Architectural Authority
>
> The authority model remains unchanged:
>
> **Architecture Continuum = canonical architecture authority**
>
> **ArchiMate Open Exchange XML = derived projection/interchange representation**
>
> **Archi = architecture workbench**
>
> An Open Exchange XML file exported after an architect modifies the model in Archi is **not automatically authoritative architecture**.
>
> The future Architecture Archi-Roundtrip remains conceptually:
>
> `Published Continuum projection`
>
> → `Archi`
>
> → `Solution Architect changes`
>
> → `Archi Open Exchange export`
>
> → `Codex review`
>
> → `Solution Architect review/disposition`
>
> → `Codex applies accepted changes`
>
> → `Canonical Continuum`
>
> → `new published release/projections`
>
> Do not implement or redesign that Praxis in this task.
>
> # Required Published Structure
>
> Establish an appropriate durable publication structure beneath:
>
> `enterprise-architecture/design/archimate/`
>
> The expected conceptual form is:
>
> `enterprise-architecture/design/archimate/published/<release>/`
>
> For example:
>
> `enterprise-architecture/design/archimate/published/1.1.11/`
>
> containing the published Open Exchange XML for release 1.1.11.
>
> Use the existing Harmonia naming conventions and generator conventions to determine the appropriate XML filename.
>
> Do not create an arbitrary second naming convention where an established one already exists.
>
> # Published Artefact Semantics
>
> The XML published for 1.1.11 must represent:
>
> > The ArchiMate Open Exchange projection of the accepted Harmonia Architecture Continuum release 1.1.11.
>
> It must be generated from the accepted canonical Continuum using the maintained projection machinery.
>
> Do not simply copy an intermediate or post-Archi verification file into the publication location without establishing that it is the correct deterministic projection of the accepted Continuum.
>
> The published projection should be suitable for the Solution Architect to:
>
> - import into Archi;
> - inspect and navigate;
> - use as the starting workbench model for subsequent architectural changes.
>
> # Architect-Modified Exports
>
> Do **not** establish a permanent `ingest/` mechanism merely for symmetry.
>
> An architect-modified XML exported from Archi is a proposed change input to a subsequent `architecture archi-roundtrip`.
>
> It is not a published architecture release.
>
> Such an export should ultimately be retained as appropriate `before` evidence for the round-trip activity that reviews it.
>
> The exact future operational handling of that input belongs to the Architecture Archi-Roundtrip Praxis and is outside this task.
>
> # Documentation
>
> Update the existing ArchiMate design/round-trip documentation sufficiently to explain:
>
> ## Canonical authority
>
> The Architecture Continuum remains authoritative.
>
> ## Published projection
>
> `published/<release>/...xml` is the durable Open Exchange projection of that accepted Continuum release.
>
> It is the appropriate starting file for loading that release into Archi.
>
> ## Architect export
>
> An XML subsequently exported from an architect-modified Archi model is proposed input to an Architecture Archi-Roundtrip.
>
> It does not modify or supersede the Continuum merely by existing.
>
> ## Evidence copies
>
> Copies retained under governed evidence demonstrate generation, testing, transport or transition activity.
>
> They are evidence.
>
> They are not the normal operational location from which the Solution Architect should have to retrieve a published architecture release.
>
> # Constraints
>
> - Release 1.1.11 is accepted historical state.
> - Do not alter accepted 1.1.11 architecture.
> - Do not alter accepted 1.1.11 navigation.
> - Do not alter accepted 1.1.11 publications.
> - Do not alter accepted 1.1.11 evidence or HCRLs.
> - Do not change Canonical IDs.
> - Do not change names.
> - Do not change standing.
> - Do not change semantic relationships.
> - Do not change authored Views.
> - Do not redesign the Architecture Archi-Roundtrip Praxis.
> - Do not create an automatic Archi-ingest mechanism.
> - Do not create a permanent ingest directory unless an existing governed requirement already mandates one; if such a requirement exists, report it for Solution Architect disposition rather than silently expanding scope.
> - Do not introduce another architecture authority.
> - Do not duplicate projection-generation machinery.
> - Keep the publication deterministic and reproducible.
> - Do not perform unrelated repository cleanup.
>
> # Authorised Activity / Scope
>
> You are authorised to:
>
> - inspect the current ArchiMate projection machinery;
> - inspect the accepted 1.1.11 projection/evidence;
> - establish the durable `published/<release>/` structure beneath the existing ArchiMate design area;
> - generate the accepted 1.1.11 Open Exchange projection into that structure;
> - update relevant current documentation;
> - make the smallest necessary generator/publication changes so future accepted releases can publish their corresponding XML consistently;
> - add bounded regression tests for the publication behaviour;
> - verify the published XML through the maintained Archi/Open Exchange validation machinery;
> - create 1.1.12 Task, Report and supporting evidence.
>
> # Required Verification
>
> Verify at minimum:
>
> 1. the published 1.1.11 XML is derived from the accepted canonical 1.1.11 Continuum;
> 2. it is not an accidentally selected intermediate/test/returned Archi export;
> 3. it contains the expected canonical element population;
> 4. it contains the expected semantic relationship population;
> 5. it contains the expected authored Views;
> 6. canonical IDs are preserved;
> 7. navigation metadata required by the current projection is present;
> 8. the file is valid Open Exchange XML;
> 9. it is suitable for import into the supported Archi version;
> 10. generation is deterministic/reproducible;
> 11. existing current EA tests remain green;
> 12. existing Java architecture tests remain green;
> 13. accepted 1.1.11 artefacts remain unchanged.
>
> # Release Behaviour
>
> Determine the smallest appropriate mechanism by which future accepted Continuum releases publish their corresponding Open Exchange XML under:
>
> `enterprise-architecture/design/archimate/published/<release>/`
>
> Prefer extending the existing deterministic publication machinery over creating another parallel publication process.
>
> Do not broaden this into general release automation.
>
> # Report
>
> Create the formal 1.1.12 Task under:
>
> `enterprise-architecture/design/evidence/1.1.12/<iteration>/before/ai-agent-tasks/`
>
> Create the execution Report under:
>
> `enterprise-architecture/design/evidence/1.1.12/<iteration>/after/ai-agent-reports/`
>
> The Report must identify:
>
> - the final repository-relative publication path;
> - the published XML filename;
> - its relationship to the canonical Continuum;
> - how it was generated;
> - why it is the correct 1.1.11 projection rather than an evidence/intermediate file;
> - validation results;
> - reproducibility results;
> - Archi compatibility/transport results;
> - documentation changes;
> - any generator changes;
> - confirmation that accepted 1.1.11 architecture and evidence remain unchanged;
> - any issue requiring Solution Architect disposition.
>
> Prepare the appropriate pending 1.1.12 HCRL for Solution Architect review.
>
> Do not assert acceptance or close the HCRL.

The instruction links
`enterprise-architecture/design/review/1.1.11/hcrl-1.1.11-002.pending.md`.
