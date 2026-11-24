Task-Issued-At: 2026-11-02T00:29:14.357Z

Event source: /home/mhunter/.codex/sessions/2026/11/02/rollout-2026-11-02T11-02-45-01a19990-cc5f-7e61-ac01-0392657c18ca.jsonl; response_item role=user containing this instruction, attributable system instruction-entry event.

# Subject Grouping hierarchy incorporation — formal Task

Issuer: Mark Hunter, Solution Architect. HCRL 1.1.18 / 001.
The complete operative instruction including spelling and existing Home authorisation is retained verbatim.

```
# Harmonia — Subject Grouping Hierarchy Incorporation
## Release 1.1.18 / Iteration 001

### Goal

Establish the governed Subject Grouping hierarchy represented by the Solution Architect's authored `1. Subject-Based-Views` folder structure.

Incorporate the 120 existing native Groupings into that hierarchy, establishing both Composition and Home relationships.

This is an explicitly authorised architectural incorporation activity.

### Expected Outcome

The canonical Architecture Continuum contains:

1. A native ArchiMate Grouping element corresponding to every folder beneath `1. Subject-Based-Views`.
2. The appropriate Composition and Home relationships for the resulting Subject hierarchy.
3. The 120 native Groupings from the incoming Archi export, reconciled into their appropriate Subject Groupings.
4. Preserved canonical identities and relationships for existing architectural elements.
5. A regenerated ArchiMate projection containing the complete authored Views hierarchy.

The resulting XML must be suitable for import into Archi for Solution Architect review.

### Context

The authoritative authored presentation input is:

`enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-Continuum-1.1.18-001-004.xml`

The previous Views reconciliation established that:

- All 1,285 Views were preserved in staging.
- All 1,183 folders, including 42 empty folders, were preserved.
- 120 native Groupings remain unincorporated.
- Those Groupings are referenced by 127 nodes across 57 new Views.
- The unresolved references prevent generation of a schema-valid XML.

The staged presentation and its reconciliation evidence are available under:

`enterprise-architecture/design/evidence/1.1.18/001/after/`

The canonical architecture is maintained under:

`enterprise-architecture/continuum/`

The accepted 32 Subjects and 27 Composition relationships must be preserved.

### Constraints

**Folder-derived Subjects**

For every folder beneath `1. Subject-Based-Views`, establish a corresponding native Grouping.

Remove the numeric navigation prefix from the folder name when determining the architectural Grouping name.

For example:

`03. Directory-Information-Maintenance`

becomes:

`Directory-Information-Maintenance`

Preserve the remaining authored naming unless an existing governed naming rule requires otherwise.

The `1. Subject-Based-Views` folder itself is a presentation root and does not require a new architectural Grouping.

**Identity reconciliation**

Before creating any Grouping:

- Search for an existing canonical Grouping representing the same Subject.
- Reuse established canonical identities where appropriate.
- Do not create duplicate architectural concepts.
- Preserve existing canonical identities and provenance.
- Report unresolved identity or naming ambiguities.

**Relationships**

For each parent–child Grouping relationship, establish:

- Composition from parent to child.
- Home association identifying the parent as the child's Home.

Apply the established Harmonia semantics and relationship representation conventions.

Do not duplicate existing equivalent relationships.

For the 120 native Groupings, establish Composition and Home relationships with their appropriate containing Subject Groupings.

**Architectural authority**

The incoming XML provides the authorised source hierarchy and native Grouping population for this bounded incorporation.

Do not incorporate unrelated architectural elements, relationships or properties.

Preserve all existing accepted architecture.

Do not modify the 784 OpenSpec Motivation candidates or their assessments.

### Authorised Activity / Scope

#### 1. Reconcile the Subject folder hierarchy

Inspect the authored `1. Subject-Based-Views` hierarchy.

Build a complete inventory of folders, their normalised architectural names, parent folders and corresponding canonical Groupings.

Identify existing Groupings that can be reused.

Create missing native Groupings through the governed Continuum mechanisms.

#### 2. Establish Subject relationships

For each nested Subject Grouping:

- Establish the appropriate Composition relationship.
- Establish the corresponding Home association.

Preserve the existing accepted Subject relationships and avoid duplicate assertions.

#### 3. Incorporate the 120 native Groupings

Reconcile the 120 unincorporated native Groupings against the Subject hierarchy.

Preserve their authored names and identity evidence.

Assign each to its appropriate Subject Grouping based on the authored structural context and available architectural evidence.

Create the required Composition and Home relationships.

Where placement cannot be established unambiguously, record the unresolved case rather than inventing a classification.

#### 4. Reconcile the Views

Apply the previously staged authored presentation to the maintained projection.

Preserve:

- All 1,285 Views.
- All 1,183 folders.
- All 42 empty folders.
- View identities, membership, geometry, connections and styling.
- The accepted G18.03 spelling correction.
- The documented Property-reference rebindings.

Ensure the View references resolve against the incorporated canonical Groupings.

#### 5. Generate and validate the projection

Generate a new unreleased verification XML.

Verify:

- Canonical identity consistency.
- Subject hierarchy completeness.
- Composition and Home relationship consistency.
- Resolution of all 127 previously missing element references.
- Preservation of accepted architectural content.
- Presentation fidelity against the authored input.
- Exchange schema validity.

Do not claim actual Archi import or visual acceptance.

### Report

Retain the Task and inputs under:

`enterprise-architecture/design/evidence/1.1.18/001/before/`

Retain the Report and verification evidence under:

`enterprise-architecture/design/evidence/1.1.18/001/after/`

Report:

1. Folder-derived Groupings created or reused.
2. Existing canonical Subjects reconciled.
3. Composition relationships created or reused.
4. Home associations created or reused.
5. The disposition of all 120 native Groupings.
6. Unresolved naming, identity or placement conflicts.
7. Presentation preservation results.
8. Validation results.
9. The exact path of the generated verification XML.

### Governance Boundary

This task authorises the specified Subject Grouping incorporation and associated presentation reconciliation.

It does not authorise unrelated architectural modifications, release transition, protected publication replacement, HCRL closure, commit or push.

Stop after producing the validated verification XML and Report.

The Solution Architect will perform Archi import and visual acceptance before authorising closure.
```
Spelling and Naming Corrections
Codex is explicitly authorised to identify and correct spelling and obvious typographical errors encountered within the scope of this activity without requesting separate Solution Architect approval.

This authorisation applies to:
- Grouping names and descriptions.
- Subject folder names.
- View names and presentation labels.
- Corresponding canonical names and derived identifiers, where applicable.
- References and navigation structures affected by those corrections.
  Constraints:
- Preserve existing canonical identities (UUIDs).
- Apply corrections consistently across the Continuum and its projections.
- Do not change architectural meaning, classification or relationships under the guise of spelling correction.
- Do not silently rename established architectural terminology where the intended correction is ambiguous.
- Record corrections in the execution report for traceability.
- Do not create separate Change Requests solely for unambiguous spelling corrections.
  For example, Achitecture-Based-Views → Architecture-Based-Views should be corrected automatically.

Similarly, the previously identified Directory-Informaton-Maintenance → Directory-Information-Maintenance correction remains applicable.

The governing principle is that correcting an obvious spelling mistake is maintenance, not a design decision.

Additional authorised activity — Existing Subject Home associations
Codex shall reconcile the existing Subject hierarchy (G01–G17), together with the newly incorporated G18 hierarchy, to ensure that every contained Subject Grouping has both:
1. Composition — from its containing Subject Grouping to the contained Subject Grouping.
2. Home association — identifying that same containing Subject Grouping as the contained Grouping's Home.
   The following rules apply:
- Reuse existing Composition and Home relationships where present.
- Create missing Home associations without requiring separate approval.
- Preserve all existing canonical identities.
- Do not duplicate relationships.
- Do not change existing Subject classifications or containment merely to establish Home.
- Where a Subject has multiple relationships, distinguish its architectural Home from other associations.
- Do not infer a Home from Aggregation, Realization, Specialisation or View membership alone.
- Report any ambiguous or conflicting Home assignments rather than choosing arbitrarily.
  Scope: G01–G18, including all nested Subject Groupings.
  One architectural distinction
  I would retain the semantics we've established:

Composition defines containment; Home identifies the authoritative architectural location.

Although the two will normally point to the same parent within this Subject hierarchy, they remain distinct relationships.

That also means Codex should not create a second Home association simply because a Subject appears in multiple Views.
Resulting scope
The consolidated Codex activity now covers:
1. Creating or reconciling Groupings for the Subject-Based-Views folder hierarchy.
2. Incorporating the 120 additional Groupings into their appropriate Subjects.
3. Establishing Composition and Home for the new Groupings.
4. Completing missing Home associations throughout G01–G18.
5. Automatically correcting unambiguous spelling mistakes.
6. Restoring the complete authored Views and generating a validated XML for Archi.
   This gives us a consistent Subject hierarchy rather than two generations of Subject modelling with different relationship conventions.
