<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# Architecture identity inventory and collision report

Release 1.0.4 (previous 1.0.3); values read from
the root release authority. **0 existing identities adopted;
0 new identities allocated in this release.**
This is an evidence inventory, not an architecture model.

The [machine inventory](inventory.json) contains every inspected record, source
locator, classification, reason and collision evidence. Its populations overlap:
the same EC appears in its RAD definition and Exchange projection. Do not sum these
counts into an architectural element total. Source/JSON pointers are audit locators.

| Population | Architectural construct / record type | Classification | Records |
| --- | --- | --- | ---: |
| Actor categories | Actor category | documentary/non-architectural and therefore does not require canonical architecture identity | 7 |
| Business assertion | TraceabilityRelationship | requires architectural adjudication | 22 |
| Business assertion | TraceabilityRelationship | requires identity allocation | 440 |
| Business capture | Capability | identity conflict | 2 |
| Business capture | Capability | requires architectural adjudication | 3 |
| Business capture | Capability | requires identity allocation | 264 |
| Business capture | Feature | requires identity allocation | 137 |
| Business capture | Function | requires identity allocation | 147 |
| Business capture | Process | requires identity allocation | 19 |
| Business capture | Service | requires identity allocation | 133 |
| Business participants | BusinessCollaboration | requires identity allocation | 7 |
| Business participants | BusinessInteraction | requires identity allocation | 69 |
| Business participants | BusinessRole | requires identity allocation | 34 |
| Candidate requirement navigation | Motivation candidate/bundle | requires architectural adjudication | 9 |
| Catalogue context | Responsibility description | documentary/non-architectural and therefore does not require canonical architecture identity | 4 |
| Catalogue context | Responsibility description | requires architectural adjudication | 69 |
| Domain05 register | ApplicationCollaboration | requires identity allocation | 1 |
| Domain05 register | ApplicationComponent | requires identity allocation | 23 |
| Domain05 register | ApplicationComponent (logical library responsibility) | requires identity allocation | 1 |
| Domain05 register | ApplicationFunction | requires identity allocation | 2 |
| Domain05 register | ApplicationFunction (proposed) | requires architectural adjudication | 1 |
| Domain05 register | ApplicationInteraction (proposed) | requires architectural adjudication | 1 |
| Domain05 register | ApplicationInterface | requires identity allocation | 1 |
| Domain05 register | DataObject | requires identity allocation | 4 |
| Domain05 register | DataObject (proposed) | requires architectural adjudication | 2 |
| Domain05 register | Mixed/documentary bundle | documentary/non-architectural and therefore does not require canonical architecture identity | 67 |
| Domain05 register | Mixed/documentary bundle | identity conflict | 2 |
| Domain05 register | Mixed/documentary bundle | requires architectural adjudication | 41 |
| Governance navigation | Decision/source evidence | documentary/non-architectural and therefore does not require canonical architecture identity | 20 |
| Governance navigation | Gap navigation | documentary/non-architectural and therefore does not require canonical architecture identity | 55 |
| Information patterns/metamodel | Information definition/pattern | requires architectural adjudication | 2 |
| Information patterns/metamodel | Information definition/pattern | requires identity allocation | 1 |
| Information review candidates | Information/Data candidate | requires architectural adjudication | 57 |
| Motivation definition | Assessment | requires identity allocation | 5 |
| Motivation definition | Driver | requires identity allocation | 7 |
| Motivation definition | Goal | requires identity allocation | 6 |
| Motivation definition | Outcome | requires identity allocation | 3 |
| Motivation definition | Stakeholder | requires identity allocation | 6 |
| Motivation definition | Stakeholder (governing-body facet) | requires identity allocation | 5 |
| Native governed definitions | Capability/Enterprise | identity already governed | 14 |
| Native governed definitions | Motivation/Constraint | identity already governed | 3 |
| Native governed definitions | Motivation/Principle | identity already governed | 18 |
| Native governed definitions | Motivation/Requirement | identity already governed | 5 |
| Native governed definitions | Strategy/CourseOfAction | identity already governed | 6 |
| Native governed definitions | Strategy/Resource | identity already governed | 3 |
| Native governed definitions | Strategy/ValueStream | identity already governed | 4 |
| Presentation | Presentation grouping | documentary/non-architectural and therefore does not require canonical architecture identity | 10 |
| Proven Exchange | Capability publication | requires architectural adjudication | 214 |
| Proven Exchange | Capability publication | safe deterministic migration | 14 |
| Proven Exchange | Composition publication | requires architectural adjudication | 189 |

## Collisions and migration candidates

- F46 identifies both Omilia operations/client experience and Shared Security and
  Assurance Nomenclature in the same navigation table. This is an unresolved source
  identity collision, not authority to merge two architectural constructs.
- CM-R05 still requires global Capability name uniqueness. Health Information
  Exchange is used for separate BC/BEC responsibilities. Both keys/meanings remain;
  namespace qualification cannot waive the naming rule. Name equality never proves
  identity. The existing RAD validator retains its explicit uncertainty.
- The 14 EC Reference-ID/Exchange UUID crosswalks are the proven adoption population;
  [Release 1.0.3](adoption-1.0.3.md) adopts them into the common register with their
  exact native IDs and locked semantic bindings. They are not new allocations.
  Other native AX/REQ-FND/CST-EXT/COA/SR/VS IDs
  already govern their definitions; publication mappings still need individual review.
- 214 baseline Capabilities and all 189 baseline Composition assertion crosswalks
  remain unresolved. Semantic XML IDs are locked and retained, not promoted to RAD IDs.
- All 705 Business full structural Canonical IDs remain unallocated. The 137 local
  FEAT tokens retain spelling/owner evidence; neither their missing CT3 frames nor
  full ancestry is filled. Missing ancestry does not erase established responsibility.
- C/F/D/M and CDG keys remain navigation. Qualified source bindings may locate
  evidence after approved allocation, but must not become canonical IDs automatically.
- Repeated IDs in references, historical source wrappers and scratchpad examples
  are indexed as observations, not counted as collisions between constructs.
  Candidate requirements and ranges in the master navigation catalogue require
  owning-domain review; a range is not a set of allocated individual identities.
- Information headings, type categories, implementation names, model/FHIR instance
  identifiers and local review/question keys are not automatically architecture IDs.
  Domain05 candidate standing/deferrals and all 2,467 unresolved RAD dimensions are
  retained. Technology/Migration counts remain unestablished, not zero architecture.

## Scope limits and recommendation

This bounded census reuses the reviewed Business capture and definition selectors,
then indexes ID-shaped occurrences across RADS. It is not an exhaustive semantic
interpretation of every prose statement. Mixed Information and C/D rows remain
adjudication candidates; named roles are definitions, not inferred Actor instances.
The token index also exposes scoped rule/decision/review IDs (e.g. DN-BC, CM-R,
B1/B2, K, CDG and candidate requirement keys) as documentary evidence, without
mechanically publishing them as Motivation or Migration elements.

The [adoption decision](adoption-1.0.3.md) bounded Release 1.0.3 to the 14 existing
EC identities. Its historical admission/publication proof remains frozen against
the [1.0.2 evidence](adoption-baseline-1.0.2.json). Current binding checks compare
those same identities and definitions with the present semantic model, independently
of authorised navigation/View changes. The classifications above
describe the source evidence; safe crosswalks do not authorise additional adoption.
Stop for human review. The 214 baseline crosswalks require separately authorised
batches before expanding publication. No bulk allocation or additional domain
publication is authorised here.
