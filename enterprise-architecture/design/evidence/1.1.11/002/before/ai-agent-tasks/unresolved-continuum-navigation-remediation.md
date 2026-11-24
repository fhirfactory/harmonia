<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task — 1.1.11 reader-facing Continuum navigation remediation

Task-Issued-At: UNRESOLVED

The operative user instruction has no attributable instruction-entry timestamp
exposed to this agent. The current clock and file creation time do not establish
that event. Its timestamp and required filename prefix remain unresolved for
Solution Architect treatment before closure, under Architecture Change Provenance.
Issuer: Mark Hunter, Solution Architect, through the operative user instruction.
Executor: Codex. Evidence boundary: 1.1.11 / 002. The preceding diagnostic at
1.1.11 / 001 is retained unchanged. Starting release: 1.1.10, predecessor 1.1.9;
starting Git HEAD: d89ce08dc56206cb31c68120b4effaaaa30295e7.

The following preserves the operative issued wording. Its dispositions authorise
implementation; they do not accept the resulting publication or complete HCRL.

---

# Goal

Remediate the reader-facing Architecture Continuum navigation defects identified by the accepted 1.1.11 diagnostic review of release 1.1.10.

This is the corrective implementation activity for **release 1.1.11**.

Implement only the Solution Architect dispositions defined below.

Do not reopen the broader 1.1.10 taxonomy design and do not use this task as an opportunity for general architecture, navigation, publication or repository cleanup.

# Expected Outcome

At completion:

1. Strategy presents current capability architecture without historical/superseded identities appearing as duplicate peer architectural subjects.
2. Historical/superseded Capability identities remain preserved and navigable as architectural history/provenance.
3. Distinct same-name capabilities remain distinct where the governed architecture establishes that they are different architectural concepts.
4. Business Behaviour uses the relevant complete CT1 → CT2 → CT3 capability hierarchy as contextual navigation.
5. Every Business Behaviour capability-context occurrence clearly references its corresponding canonical Strategy Capability.
6. Business Services, Functions, Processes and Events are presented within their appropriate capability context.
7. Stakeholders no longer appear as peer Motivation decomposition subjects.
8. Existing Stakeholder associations are represented contextually against the Motivation material to which they are actually associated.
9. All canonical Stakeholders remain discoverable through a clearly non-decompositional Stakeholder Index.
10. The Continuum, Manifest, RAD and Exchange projection expose these semantics consistently within the technical limits of each representation.
11. Existing Canonical IDs, architectural semantics, relationships and standing remain unchanged except for authorised navigation/publication organisation.
12. No architecture is invented to make the resulting publication look complete.

# Context

Release 1.1.10 remains accepted historical state.

Subsequent Solution Architect inspection of its generated RAD identified reader-facing architectural-navigation defects.

The 1.1.11 diagnostic established:

- **213 same-name Capability groups**;
- **212** are historical/successor pairs;
- **one** same-name case represents genuinely distinct Business Capability / Business Enabling Capability responsibilities;
- Business Behaviour contains **72 contextual capability references**;
- all 72 Business Behaviour contexts currently sit directly beneath Business Behaviour and omit their CT1 ancestry;
- canonical Strategy references exist in the RAD body for those contexts but are not consistently exposed through the other contextual projections;
- Stakeholders currently appear as peer Motivation navigation entries;
- the RAD suppresses four existing Stakeholder associations;
- the previously suspected duplicate `I.6` navigation coordinate could not be reproduced in the retained accepted PDFs or inspected source projections.

The purpose of 1.1.11 is to correct the diagnosed navigation/presentation defects without changing the underlying architecture merely to simplify publication.

# Solution Architect Disposition 1 — Capability History and Primary Strategy Navigation

The primary Strategy capability hierarchy should present the **current architectural capability subject**.

Where the governed architecture establishes a historical/superseded Capability identity and its current/successor Capability identity, the historical identity must not appear as another peer architectural subject merely because it has the same architectural name.

Conceptually, do not present:

`[CT1] Care Access & Coordination`

`[CT1] Care Access & Coordination`

as indistinguishable peer entries when one is a historical/superseded identity and the other is its governed current/successor identity.

Instead:

- the current/successor Capability occupies the primary Strategy taxonomy position;
- historical/superseded identities remain preserved;
- historical identities remain discoverable through the current subject's history/provenance or another clearly historical/reference presentation;
- standing and historical correspondence remain explicit;
- Canonical IDs remain unchanged.

Do **not** deduplicate by name.

The diagnostic identified one important exception demonstrating why name-based deduplication is prohibited: `Health Information Exchange` spans distinct Business and Business Enabling responsibilities, while the historical BEC identity corresponds to the differently named `Health Information Exchange Enablement` successor.

Use governed identity, historical/successor correspondence, scope and architectural semantics — never label equality alone — to determine presentation.

# Solution Architect Disposition 2 — Business Behaviour Capability Context

Repeating capability structure beneath Business Behaviour is **intentional and required**.

The Business Behaviour navigation should provide the relevant complete contextual capability preamble:

`Business Behaviour → CT1 → CT2 → CT3 → Business Behaviour`

where the final behaviour is organised through the applicable:

- Business Services;
- Business Functions;
- Business Processes;
- Business Events.

The CT1/CT2/CT3 occurrences beneath Business Behaviour are **contextual references**, not additional Capability instances and not a second authoritative Capability model.

The authoritative Capability decomposition remains under Strategy.

## Contextual hierarchy

Where a CT3 has represented Business Behaviour, provide its complete governed ancestry:

`CT1 → CT2 → CT3`

Do not flatten the contextual hierarchy merely to the CT2 or CT3 that directly owns behaviour.

The contextual hierarchy should allow a reader to understand where the behaviour belongs within the broader capability decomposition without leaving the Business section.

## Canonical Capability reference

Every contextual CT1, CT2 and CT3 occurrence must clearly reference the corresponding canonical Strategy Capability.

The reader should be able to distinguish:

- the Business Behaviour navigation coordinate;
- the capability being used as context;
- the canonical Strategy navigation coordinate;
- the canonical Capability identity;
- the behaviour associated with that context.

Conceptually:

`III.x.x [CT3] Encounter Administration`

`Capability: II.x.x.x Encounter Administration`

followed by:

- Business Services
- Business Functions
- Business Processes
- Business Events

as applicable.

The precise visual syntax may follow existing publication conventions, but the distinction must be explicit.

## Cross-projection behaviour

The canonical Strategy reference must not exist only as explanatory prose in the RAD.

Expose the contextual-to-canonical reference consistently in:

- canonical Continuum navigation metadata;
- Manifest/browser;
- RAD;
- Exchange projection where technically representable without inventing an ArchiMate semantic relationship.

Do not create Composition, Aggregation, Serving, Assignment or another semantic relationship merely to implement contextual navigation.

## Empty contexts

Do not populate the entire capability hierarchy under Business Behaviour merely for symmetry.

Include the ancestry required to contextualise represented Business Behaviour.

If an intermediate CT1/CT2 context is required to reach populated descendants, it is legitimate contextual navigation even if it has no behaviour directly attached to itself.

Do not manufacture Business Behaviour to populate a context.

# Solution Architect Disposition 3 — Stakeholders as Cross-Cutting Motivation

Stakeholders are a **cross-cutting Motivation perspective**.

They are not peer architectural decomposition categories alongside:

- Drivers;
- Strategic Goals;
- Business Outcomes;
- Assessments;
- Requirements;
- Constraints;
- Architecture Axioms.

Therefore individual Stakeholders must no longer appear as additional peer Motivation subjects after those architectural subjects.

## Contextual Stakeholder presentation

Where an actual canonical relationship associates a Stakeholder with Motivation material, expose the Stakeholder contextually against that material.

For example:

`Requirement`

`    <Requirement>`

`        Stakeholders`

`            <Stakeholder reference>`

or the equivalent appropriate reader-facing representation.

The contextual occurrence references the canonical Stakeholder identity.

It does not create another Stakeholder.

Only expose associations that actually exist in the canonical architecture.

Do not infer associations from:

- names;
- descriptions;
- apparent relevance;
- role;
- domain;
- common sense;
- publication convenience.

The four existing Stakeholder associations identified by the diagnostic must no longer be suppressed by the RAD publication.

## Stakeholder Index

Provide a **Stakeholder Index** as cross-cutting navigation/reference material.

The Stakeholder Index must expose all canonical Stakeholders, including those for which no canonical Motivation association is currently represented.

This index is not another Motivation architectural decomposition category.

Its presentation must make that distinction clear.

Conceptually:

`Part I — Motivation`

`    Drivers`

`    Strategic Goals`

`    Business Outcomes`

`    Assessments`

`    Requirements`

`    Constraints`

`    Architecture Axioms`

`    Unclassified Motivation`

and separately as cross-cutting/reference navigation:

`    Cross-Cutting References`

`        Stakeholder Index`

The exact physical representation may be adapted to the existing navigation/publication machinery, but it must not make `Cross-Cutting References` or `Stakeholder Index` appear to be another architectural Motivation concept equivalent to Drivers, Requirements or Constraints.

The Stakeholder Index provides navigation/discovery.

Canonical relationships provide architectural association.

Do not confuse those functions.

# Solution Architect Disposition 4 — Reported Duplicate I.6

No remediation is authorised for the previously reported second `I.6`.

The 1.1.11 diagnostic could not reproduce the defect in:

- the accepted final PDF;
- the retained pre-completion PDF;
- or the inspected source projections.

Retain the diagnostic finding as **not reproduced**.

Do not modify numbering logic merely to address this unconfirmed observation.

If a reproducible coordinate collision is independently discovered during this implementation, record it as a new finding rather than silently repairing it.

# Solution Architect Disposition 5 — Reader-Facing Review Safeguard

The 1.1.10 mechanical validation established preservation, completeness, transport, reproducibility and publication integrity but did not establish that the resulting architectural hierarchy was semantically useful to a reader.

Do not create a new architecture Praxis, factory subsystem or elaborate automated semantic-review mechanism in response.

Introduce the smallest useful governance safeguard:

> **A generated reader-facing architecture/navigation publication must receive Solution Architect structural review before HCRL acceptance when a change materially alters architectural navigation or publication structure.**

The review should inspect representative and structurally significant portions of the generated hierarchy, including at minimum where applicable:

- Motivation;
- Strategy capability hierarchy;
- Business Behaviour contextual hierarchy;
- Application hierarchy;
- cross-cutting/reference presentation;
- historical/superseded presentation;
- deep navigation numbering.

The safeguard is a human architectural review checkpoint.

Automation may assist by identifying suspicious patterns, but automated checks do not replace Solution Architect review.

Do not require exhaustive page-by-page review of a publication merely because it is large.

# Navigation Semantics

Retain the 1.1.10 navigation principle:

- **Canonical ID = immutable architectural identity**
- **Name = architectural meaning**
- **Navigation coordinate = current architectural/navigation location**

Navigation coordinates may change as a result of this authorised remediation.

That is expected.

Do not preserve an incorrect 1.1.10 coordinate merely for numeric stability.

Historical references to 1.1.10 coordinates remain valid within the historical 1.1.10 evidence/publication context.

# Architectural Versus Contextual Navigation

The implementation must make this distinction explicit:

## Architectural occurrence

Represents the canonical architectural subject in its authoritative taxonomy location.

Example:

`Strategy → Capabilities → Business Capabilities → CT1 → CT2 → CT3`

## Contextual occurrence

References an existing canonical architectural subject to provide orientation elsewhere.

Example:

`Business → Business Behaviour → CT1 context → CT2 context → CT3 context`

or a Stakeholder reference beneath associated Motivation material.

A contextual occurrence:

- may have its own navigation coordinate;
- may repeat the canonical subject's display name;
- must identify/reference the canonical architectural subject;
- does not allocate another Canonical ID;
- does not imply another architectural instance;
- does not create a semantic relationship unless that relationship already exists canonically.

This distinction must be understandable to a reader and representable in the generated navigation metadata.

# Constraints

- Preserve accepted 1.1.10 evidence and completed governance records unchanged.
- Preserve Canonical IDs.
- Preserve native ArchiMate types and specialisations.
- Preserve existing semantic relationships.
- Preserve architectural standing.
- Preserve governed historical/successor correspondence.
- Do not deduplicate by name.
- Do not merge genuinely distinct architectural concepts.
- Do not create semantic relationships merely for navigation.
- Do not invent Stakeholder associations.
- Do not invent Business Behaviour.
- Do not manufacture capability allocation.
- Do not manufacture Technology or Implementation & Migration architecture.
- Do not import Legacy-RAD material to fill gaps.
- Do not alter information classification merely for publication.
- Do not broaden this work into architecture redesign.
- Do not introduce new Praxes or factory mechanisms.
- Do not perform unrelated repository cleanup.
- Keep generation deterministic and reproducible.
- Continue to treat the Continuum as architecture authority and publications/projections as derived representations.

# Authorised Activity / Scope

You are authorised to:

- modify current 1.1.11 Continuum navigation metadata;
- modify navigation generation logic;
- modify Manifest/browser generation;
- modify Continuum RAD/LaTeX generation;
- modify Exchange navigation/projection organisation where required;
- add contextual-reference metadata that does not constitute architectural semantics;
- restructure reader-facing navigation according to the dispositions above;
- suppress historical/superseded peer navigation where governed successor correspondence exists while preserving historical accessibility;
- restore publication of existing Stakeholder relationships;
- introduce the cross-cutting Stakeholder Index;
- restore CT1 → CT2 → CT3 ancestry under Business Behaviour;
- expose canonical Strategy references from Business Behaviour contextual nodes;
- add bounded validation/tests protecting these behaviours;
- update current governance guidance with the bounded Solution Architect reader-facing structural review checkpoint;
- regenerate the Manifest, Exchange projection and Continuum RAD/PDF;
- perform actual Archi transport verification;
- generate formal 1.1.11 evidence.

You are not authorised to:

- alter the architectural meaning of the underlying canonical elements;
- allocate replacement identities;
- delete historical architectural identities;
- resolve unrelated architectural residuals;
- create new semantic architecture to improve presentation;
- modify accepted 1.1.10 evidence.

# Required Verification

Verify at minimum:

1. all existing canonical element identities remain preserved;
2. all existing semantic relationships remain preserved;
3. all authored Views remain preserved unless an explicitly authorised projection-only change requires otherwise;
4. current Strategy capability navigation no longer presents governed historical/successor pairs as indistinguishable peer subjects;
5. genuinely distinct same-name capabilities remain distinct;
6. historical/superseded identities remain discoverable;
7. Business Behaviour contextual paths contain the required CT1 → CT2 → CT3 ancestry;
8. every Business Behaviour contextual capability occurrence references its canonical Strategy Capability;
9. contextual occurrences are distinguishable from canonical architectural occurrences;
10. represented Stakeholder associations appear contextually in the RAD;
11. no Stakeholder association is invented;
12. all canonical Stakeholders are discoverable through the Stakeholder Index;
13. Stakeholders no longer appear as peer Motivation decomposition subjects;
14. navigation coordinates are unique within the current hierarchy;
15. Manifest, RAD and Exchange consume the same current navigation authority within their documented representation limits;
16. generated artifacts are reproducible;
17. maintained EA tests pass;
18. maintained Java architecture tests pass;
19. Legacy-RAD remains operationally unnecessary.

# Reader-Facing Inspection

In addition to automated verification, inspect the generated result as a reader.

At minimum inspect and record representative rendered/navigation evidence for:

- Motivation and the Stakeholder Index;
- one Motivation element with an actual Stakeholder association;
- Strategy Business Capabilities;
- Strategy Business Enabling Capabilities;
- at least one remediated historical/successor Capability case;
- the exceptional distinct same-name Capability case;
- Business Behaviour showing a complete CT1 → CT2 → CT3 contextual path;
- the canonical Strategy reference from that Business Behaviour context;
- behaviour beneath that context;
- at least one deep navigation coordinate.

This inspection is evidence for Solution Architect review.

It is not agent acceptance.

# Report

Create the formal 1.1.11 remediation Task evidence under:

`enterprise-architecture/design/evidence/1.1.11/<iteration>/before/ai-agent-tasks/`

Create the execution Report under:

`enterprise-architecture/design/evidence/1.1.11/<iteration>/after/ai-agent-reports/`

Retain the diagnostic evidence from the preceding 1.1.11 diagnostic iteration unchanged.

The Report must include:

- implemented dispositions;
- before/after navigation structure;
- treatment of the 212 historical/successor same-name Capability groups;
- treatment of the exceptional distinct same-name Capability case;
- historical Capability accessibility;
- Business Behaviour contextual-navigation implementation;
- CT1/CT2/CT3 ancestry verification;
- canonical Strategy-reference implementation;
- Stakeholder contextual-association implementation;
- Stakeholder Index implementation;
- confirmation that no Stakeholder associations were invented;
- navigation-coordinate changes;
- Continuum changes;
- Manifest/browser changes;
- RAD/LaTeX changes;
- Exchange changes and representation limitations;
- reader-facing inspection evidence;
- identity/relationship/View preservation results;
- test results;
- reproducibility results;
- Archi transport results;
- Legacy-RAD isolation result;
- any newly discovered ambiguity or defect requiring Solution Architect disposition;
- proposed HCRL decision items.

Do not claim Solution Architect acceptance.

Do not complete the 1.1.11 HCRL until the Solution Architect has reviewed the generated reader-facing result.
