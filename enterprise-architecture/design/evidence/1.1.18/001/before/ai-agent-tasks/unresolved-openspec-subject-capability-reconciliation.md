Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Task — OpenSpec and Subject reconciliation preparation

Actor: User instructing Codex; Solution Architect context supplied in instruction.
Boundary: 1.1.18 / 001. Review preparation only. No incorporation acceptance,
commit, push, governance closure or release transition authorised.
Task-Issued-At and required timestamp filename prefix remain UNRESOLVED:
no attributable system instruction-entry timestamp is exposed to this agent.
Do not substitute the current clock, filesystem metadata or commit time.

## Operative issued instruction (verbatim)

### Goal

Reconcile the Archi working-model changes in `Harmonia-Continuum-1.1.18-001-004.xml` with the governed Architecture Continuum, then prepare the incorporation of the OpenSpec UserStory and UseCase candidates and their Capability Model associations.

### Expected Outcome

1. G14–G18 Subjects and their subgroups reconciled with the governed Subject hierarchy.
2. Existing architectural identities, relationships and history preserved.
3. All 784 candidate Motivation elements assessed for incorporation.
4. Candidate relationships to existing CT3 Capabilities and Features assessed.
5. Discrepancies, missing capabilities, ambiguous mappings and proposed architectural changes identified for Solution Architect review.
6. Complete governed evidence produced before any acceptance or release transition.

### Context

The Solution Architect has extended the Subject hierarchy in Archi to accommodate business-functional Motivation elements.

The existing G01–G13 hierarchy remains applicable. G14–G18 extend that framework.

Subject classification and Capability alignment serve different purposes:

- **Motivation → Subject:** architectural classification and governance.
- **Motivation → Capability/Feature:** capability coverage and development traceability.

The authoritative architecture resides under:

`enterprise-architecture/continuum/`

The incoming Archi export is:

`enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-Continuum-1.1.18-001-004.xml`

The candidate mapping is:

`Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md`

This mapping must be supplied to Codex in the repository workspace.

### Constraints

- Follow the repository's established architecture governance, reconciliation and evidence procedures.
- Treat Markdown RAD as governed authority and ArchiMate XML as an incoming working-model projection.
- Preserve existing canonical identities and architectural relationships.
- Do not create duplicate Subjects, Requirements, Capabilities or Features.
- Do not infer architectural relationships solely from matching names.
- Do not automatically equate a Subject with a Capability.
- Do not assume that a candidate Capability association establishes satisfaction or implementation responsibility.
- Preserve OpenSpec source identifiers, provenance and original UserStory wording.
- Preserve distinctions between current and future release scope.
- Report unresolved conflicts rather than making unsupported architectural decisions.
- Do not commit, push, close governance records or transition releases without the required authorisation.

### Authorised Activity / Scope

**Phase 1 — Subject reconciliation**

Inspect the incoming Archi export and identify additions or changes to G14–G18.

Compare these against the candidate Subject hierarchy and the governed Continuum.

Reconcile identities, names, specialisations, containment and relationships using the established governance process.

Report discrepancies requiring architectural judgement.

**Phase 2 — Motivation reconciliation**

Inspect the OpenSpec candidate mapping and its source material.

Identify existing Motivation elements before proposing additions.

Prepare the incorporation of the 363 UseCase and 421 UserStory candidates, preserving their source identities and provenance.

Reconcile their proposed Subject assignments against the actual Subject hierarchy.

**Phase 3 — Capability alignment**

Reconcile the candidate CT3 and Feature associations against the governed Capability Model.

Prefer the most specific appropriate existing Feature.

Where no suitable Feature exists, identify the applicable CT3 Capability and report the potential Feature gap.

Identify ambiguous associations, multiple relevant capabilities and requirements without defensible capability coverage.

Do not invent new Capabilities or Features.

### Report

Produce a comprehensive report under `.junie/reports`, following repository conventions.

Include:

- Input versions and provenance.
- Subject reconciliation results.
- Motivation element counts and reconciliation outcomes.
- Existing versus proposed element identification.
- Capability and Feature alignment results.
- Duplicate and identity-conflict findings.
- Architectural gaps and unresolved decisions.
- Verification results and evidence locations.
- Explicit recommendations requiring Solution Architect authorisation.

Preserve all governed evidence under the appropriate `enterprise-architecture/design/evidence/1.1.18/001/` structure.

**Stop at the required governance review boundary. Do not treat preparation or technical validation as Solution Architect acceptance.**

## Material follow-up instruction (verbatim)

> Not that the Harmonia-OpenSpec-Expanded-Subject-Capability-Mapping.md document is located at /enterprise-architecture/design/archimate/working/1.1.18/

Follow-up instruction-entry timestamp: UNRESOLVED; not exposed in this
interaction. This confirms the mapping locator already discovered and used.
It does not answer the separately pending bounded export-interpretation question.
