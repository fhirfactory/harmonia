<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — post-acceptance diagnostic of RAD-1.1.10 navigation

Task-Issued-At: UNRESOLVED

The attributable system timestamp of the operative user instruction entering this
interaction is unavailable to this agent. The environment supplies date context
2026-10-23, not that event time. Neither a later clock reading, recording time,
filesystem time nor Git time supplies it. The required UTC filename prefix is
therefore also unresolved and requires Solution Architect treatment before any
future closure. The accepted treatment of the separate 1.1.10 Task does not extend
to this Task automatically.

**Issuer:** Mark Hunter, Solution Architect, through the operative user message in
this Codex interaction. **Executor:** Codex. **Activity:** diagnostic review only.
**Evidence boundary:** prospective 1.1.11, iteration 001, the first unused iteration;
no HCRL is created or completed by assigning this evidence location. The current
accepted release remains 1.1.10. No release allocation or acceptance is asserted.

**Starting state:** clean worktree at
`d89ce08` (full commit recorded in the diagnostic source inventory);
root `architecture-release.json`: version 1.1.10, previous_version 1.1.9.
The user explicitly attaches
`enterprise-architecture/design/review/1.1.10/hcrl-1.1.10-001.pending.md`.
The completed register and exact pending input are to be read together.

## Operative instruction and correspondence

The following verbatim operative excerpts preserve purpose, authority, restrictions
and required decisions. The detailed requested analyses are retained immediately
below as an attributed task specification; they do not substitute an inferred
architecture decision for the user's instruction.

> Perform a bounded diagnostic review of the reader-facing architectural navigation produced and accepted in Harmonia release **1.1.10**.
>
> The review was triggered by subsequent Solution Architect inspection of the generated Continuum RAD/PDF, which identified apparent structural defects that were not detected during the 1.1.10 acceptance review.
>
> This task is **diagnostic only**.
>
> Do not remediate, restructure, rename, move, merge, delete or create architectural content or navigation structures as part of this task.

> 1.1.10 remains accepted historical state.
>
> The 1.1.11 review should explicitly acknowledge that the Solution Architect's 1.1.10 acceptance review was insufficiently thorough with respect to the semantic usefulness and reader-facing structure of the generated navigation.
>
> This does not invalidate the mechanical verification performed in 1.1.10. Rather, it identifies a review dimension that those gates did not establish.

> Repeating:
>
> `CT1 → CT2 → CT3`
>
> under Business Behaviour for navigation and architectural context is **authorised and desirable**.
>
> Duplicating the actual Capability architecture or creating new Capability instances is not.
>
> The Business occurrence is a **contextual reference/preamble** to the canonical Strategy Capability.
>
> No new Composition, Aggregation, ownership, allocation or other semantic relationship should be inferred merely because a contextual navigation path is shown.

> Contextual repetition is legitimate where the relationship to the canonical subject is explicit.

> Do not respond by proposing a large new governance framework.
>
> Identify the **smallest useful review safeguard** that would have made these defects visible before 1.1.10 acceptance.
>
> Any proposed safeguard is advisory only and requires Solution Architect disposition.

> Do not create a completed 1.1.11 HCRL or imply Solution Architect acceptance.
>
> The purpose of this task is to establish what went wrong and why before any corrective change is authorised.

## Detailed issued scope

The user's message requires an explanation of the following, based on accepted
source and publication evidence, with uncertainty preserved:

1. Apparently repeated CT1 Strategy subjects, including Care Access & Coordination;
   Clinical Quality, Safety & Improvement; Community Health & Wellbeing; Connected
   Health Services; Corporate Resources & Enterprise Services; Diagnostic,
   Therapeutic & Clinical Support Services; Health Information & Knowledge
   Management; Health Products & Clinical Technology; Health Research & Innovation;
   Health Rights, Advocacy & Participation; Health Service Direction & Stewardship;
   Health Service Governance; Individual Care Delivery; Population Health &
   Health-System Planning; Security, Privacy & Digital Trust; Standards, Semantics &
   Reference Governance; Workforce & Organisational Capability. Business Enabling
   examples include Clinical Service Dependency Management, Entity Information
   Enablement, Health Informatics Management and Independent Assurance Enablement.
   Investigate representative examples and the full affected population. Distinguish
   multiple IDs, standing/provenance variants, multiple context paths, reconciliation
   artefacts and generation effects; do not assume duplicate records. Establish why
   each exists and is emitted, and whether its distinction is architecturally
   meaningful or remains unresolved.
2. All immediate Motivation entries, including the reported second `I.6` associated
   with Constraints/Motivation. Trace canonical navigation, Manifest, Exchange,
   LaTeX, PDF outline and rendered text separately. Determine actual coordinate
   collisions versus stale output, references, hierarchy or publication effects.
   Explain the ten numbered Stakeholder profiles against cross-cutting treatment;
   do not invent associations for unconnected Stakeholders.
3. Every Business Behaviour capability context: presence and correct CT1/CT2/CT3
   ancestry against Strategy, flattened/omitted levels, explicit canonical Strategy
   references, reader distinction between contextual nodes and actual elements,
   Business Services/Functions/Processes/Events and other behaviour populations,
   empty contexts, and preservation of unallocated behaviour without inferred
   lexical matching. A repeated capability name in Business is not itself a defect.
4. Bounded complete-hierarchy inspection for analogous same-level subjects,
   coordinate collisions, provenance/standing distinctions leaking into taxonomy,
   ambiguous contextual/reference presentation, global native-type catalogues,
   missing contextual ancestry, wrong taxonomic levels/order, implementation/source
   organisation leakage, empty/misleading nodes and cross-projection disagreement.
   This is not a general architecture review.
5. Root causes separated between canonical architectural records, canonical
   navigation/derivation and particular publication/transport projections. Preserve
   native semantics and historical acceptance, and identify limits of mechanical
   verification compared with architectural navigation review.

## Required deliverables

- Formal Task here and formal Report under this iteration's
  `after/ai-agent-reports/`, with report production event and matching UTC filename.
- Machine-readable evidence where practical and human-readable summaries.
- Capability duplicate table for every affected subject/ID: displayed/friendly/
  canonical names, ID, coordinate, scope, CT level, native type/specialisation,
  standing/currentness, parent/owner, original organisation, relevant relationships,
  existence/emission causes and diagnostic classification.
- Motivation table for every immediate entry: coordinate, label, element versus
  navigation-only node, ID/type, role, projection behaviour and finding.
- Business Behaviour table for every context: coordinate, CT level/label, Strategy
  ID/coordinate, contextual parent, completeness of ancestry, reader-explicit
  canonical reference, service/function/process/event/other counts, presence of
  behaviour, emission reason and unambiguous contextual distinction.
- Report explicitly acknowledges post-acceptance Solution Architect visual findings
  and insufficient thoroughness of the 1.1.10 acceptance review in this dimension,
  without rewriting accepted evidence or invalidating its mechanical checks.
- Report ends with bounded alternatives/consequences and explicit Solution Architect
  decision questions on same-name/distinct-ID Strategy material; cross-cutting
  Stakeholders; exact CT1/CT2/CT3 contextual preambles; canonical Strategy references;
  coordinate correction if established; analogous defects; and the smallest useful
  reader-facing review safeguard. Alternatives are advisory, not selected because
  of implementation convenience and not application authority.

## Authorised inspection and exclusions

The user authorises inspecting accepted 1.1.10 Continuum/navigation, Manifest,
Exchange, RAD/LaTeX/PDF, current canonical records/relationships, 1.1.10
Task/Report/HCRL/mapping/verification evidence and generator code as necessary;
read-only diagnostic scripts; diagnostic evidence in 1.1.11; and candidate
remediation alternatives for Solution Architect consideration.

Do not alter `navigation.json`, canonical files/IDs/names/standing/relationships,
accepted 1.1.10 evidence or completed governance records, publication generators,
capability allocations, Stakeholder associations or information classifications.
Do not merge, move, delete, promote/demote, restructure, clean up, introduce a Praxis
or architecture-factory mechanism, or implement alternatives. No new semantic
edge follows from a contextual path. Treat PDF as an architectural review surface.

## Governing context

Read the common Architecture Review Praxis, Architecture Change Provenance,
Architecture Governance, release policy and Continuum Representation. Follow
`architectural-axioms.md` to the canonical Principles: materially relevant AX-04
(semantics/machinery), AX-08 (meaningful evidence), AX-14 (semantic distinctions),
AX-15 (uncertainty), AX-17 (authority), with AX-03 where native types are discussed.
This task changes no architecture; candidate alternatives must preserve identity,
native semantics, separate standing, and decision versus diagnosis boundaries.
Historical 1.1.10 instruction/acceptance is evidence of its own bounded decision;
the present contextual-preamble requirement must not be backdated into it.
