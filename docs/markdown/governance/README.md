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

# Cross-Domain Architecture Governance

The current common [Architecture Governance](../../../enterprise-architecture/governance/architecture-governance.md)
and [Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md)
contracts implement the reviewed RAD-1.1.2 adoption. HCRL is the common Decision
Review Log; specialised contracts below retain their specialised purpose and
feed relevant material into HCRL without competing final acceptance records.
Governance knowledge does not require ArchiMate elements or continuum entries.
Existing source paths and specialised semantics remain in place: repository
migration requires a separately established bounded mapping and authorisation.

This location contains cross-domain architecture governance material applying
across the canonical Harmonia architecture corpus. It is not a numbered
architecture domain and does not replace Motivation, Strategy or another domain.

[Architectural Axioms](architectural-axioms.md) is the single maintained
authoritative architectural axiom register for the whole Harmonia architecture.

The repository [RADS and legacy documentation rules](../../../AGENTS.md#12-rads-and-legacy-documentation-governance)
define `/docs/markdown` as the Reference Architecture Documentation Set (RADS),
its authority for current architecture, review and adjudication before legacy
incorporation, and preservation of original legacy sections inside supersession
wrappers. The root `AGENTS.md` is the single maintained source of these standing
rules for human and AI documentation work throughout the repository.

The [Architecture Completion Plan](../architecture-completion-plan.md) records
the wider canonical-corpus programme and its outstanding consolidation work.

The [RAD / Architecture release policy](architecture-release-policy.md) defines
patch increments for every accepted repository change and the single root
[machine-readable release authority](../../../architecture-release.json).

[Architecture Change Provenance](../../../enterprise-architecture/governance/architecture-change-provenance.md) is the
governed, representation-neutral definition of evidence for authorised
architecture transitions, applicable to Markdown/RADS, ArchiMate and future
representations. Codex, Junie, other agents, Praxes, templates and machinery
reference this one contract. Its current adoption supersedes the mandatory
three-file package and uses required evidence without duplicate exact-byte
archives or universal hashing. The preserved 2026-10-20 contract reconciled former
permanent processing-retention requirements, preserves active recovery and
migration dependencies, and establishes eventual independence from historical
processing. Installation authorises no evidence migration or disposal.

The [Architecture Modelling Strategy](architecture-modelling-strategy.md) governs
semantic compatibility of Harmonia specialisations and explicit logical
architectural Home. Directed Association specialised as `Home` is independent
of Realization, structural containment, ownership, naming and navigation.
The [fixed Business Home migration register](baseline/business-homes.json)
records the accepted one-time baseline normalisation and unresolved cases;
prospective Home requires explicit authoring, never contextual inference.

The [RAD → ArchiMate model mapping](rad-archimate-model-mapping.md),
[mapping catalogue](rad-archimate-mapping-catalogue.md),
[coverage assessment](rad-archimate-coverage-assessment.md) and
[incremental publication plan](rad-archimate-publication-plan.md) govern the
common model's progressive publication. Their unresolved findings and candidate
mapping decisions remain explicit; installation does not authorise later slices.

The [ArchiMate Ingestion Change Review contract](archimate-ingestion-change-review.md)
governs independently adjudicable incoming changes, Solution Architect decisions,
executable reconciliation instructions and active processing records. Durable
transition evidence follows Architecture Change Provenance above.
Disposition records the Solution Architect's call to action; separate
[Outcome and Outcome Reason](archimate-ingestion-change-review.md#outcome-and-outcome-reason)
record the resulting CR state and why, including resolution without reconciliation.
Its
[governed template](templates/archimate-ingestion-change-review-template.md)
supports review preparation; it accepts no incoming architecture. Reconciliation
machinery and the blind-test proposals remain outside this documentation increment.

The [Increment 2B ingestion/publication semantics](archimate-ingestion-publication-semantics.md)
govern native Name/displayName equality, authorised qualifiers, semantic naming
context, machine metadata, relationship recognition/reuse, source-based homes,
accepted authored Views and the deterministic 1–20-child Containment View.
The [Business metamodel Composition rule](../03-business-architecture/metamodel/business-architecture-metamodel.md#bec-composition-hierarchy), accepted by the Solution
Architect on 2026-10-19, establishes what an accepted BEC CT3 → Feature Composition
means; it accepts no submitted instance. The explicit Home strategy resolves
Realization/context inference negatively: Realization by itself establishes
none of Home, containment, parentage, ownership, naming or navigation context.
Presentation beyond 20 children remains explicitly unresolved. This contract
implements no reconciliation or layout engine and accepts no blind-test proposal.

The [Increment 2C Architecture Authoring Praxis](architecture-authoring-praxis.md)
governs the serial published-baseline, authoring, submission, review,
reconciliation, validation and publication lifecycle. Submission closes an
Authoring Iteration; further governed authoring starts from the next applicable
publication. [Working on the Harmonia architecture](../../archimate/README.md)
is the practical entry point for architects. The Praxis changes no 2A disposition
or 2B semantic rule and introduces no branching, merge or reconciliation engine.

The [Governed Architecture Identity model](architecture-identity/model.md),
[identity inventory and collision report](architecture-identity/inventory.md)
and [common identity register contract](architecture-identity/registry.schema.json)
establish the cross-domain identity mechanism for review before allocation.
The register adopts only the 14 proven EC identities under the
[Release 1.0.3 decision](architecture-identity/adoption-1.0.3.md); existing semantic
locks and unresolved crosswalks remain intact. See the
[identity release report](architecture-identity/release-report.md).

The [Harmonia Archi Navigation & View Convention](archi-navigation-and-view-convention.md)
governs canonical element/relationship navigation homes, independent subject hierarchies and
progressive Views. The [Release 1.0.4 report](../../archimate/processing/publishing/1.0.4/navigation-release-report.md)
records its navigation-only application to existing capabilities and the human
Archi inspection gate. The 1.0.3 identity report above remains historical.

The [Harmonia ArchiMate Round-Trip Engineering Praxis](archimate-round-trip-engineering-praxis.md)
is the two-trigger processing entry for Codex and Junie. It identifies the Change
Review and directive/application checkpoints, reusable tools, current containment
rules, navigation regeneration, View preservation and run/release boundaries.
