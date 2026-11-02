Task-Issued-At: 2026-11-02T00:02:47.861Z

Event source: /home/mhunter/.codex/sessions/2026/11/02/rollout-2026-11-02T11-02-45-01a19990-cc5f-7e61-ac01-0392657c18ca.jsonl; response_item role=user containing the operative Views reconciliation instruction; system instruction-entry timestamp, not capture time.

# ArchiMate Views reconciliation — formal Task

Issuer: Mark Hunter, Solution Architect, user instruction in this Codex interaction.
HCRL boundary: 1.1.18 / 001. Instruction preserved verbatim below.

```
# Harmonia — ArchiMate Views Reconciliation
## Release 1.1.18 / Iteration 001

### Goal

Restore the authored ArchiMate Views and organisation hierarchy from the Solution Architect's incoming export, while preserving the accepted architectural incorporation already completed under HCRL 1.1.18/001.

Produce a reconciled ArchiMate Open Exchange XML suitable for import into Archi and visual verification by the Solution Architect.

This is a **bounded presentation reconciliation and round-trip correction**, not an additional architectural design or incorporation activity.

### Expected Outcome

The resulting ArchiMate XML faithfully reproduces the Solution Architect's authored Views and presentation organisation from:

`enterprise-architecture/design/evidence/1.1.18/001/before/Harmonia-Continuum-1.1.18-001-004.xml`

while retaining the accepted canonical architectural changes already incorporated into the Continuum.

The reconciled projection must preserve:

- The complete authored Views folder hierarchy, including empty folders.
- Existing and newly created Views.
- View membership and referenced architectural elements.
- View layout, geometry, connections and presentation properties.
- Folder names, nesting and ordering.
- The accepted G18.03 spelling correction.

Unrelated architectural changes in the incoming XML must not be silently incorporated.

The resulting XML must be suitable for import into a new Archi model for Solution Architect inspection.

### Context

The previous incorporation successfully created:

- 32 native Grouping Subjects.
- 27 Subject classification Composition relationships.
- The accepted G18.03 presentation correction.

Codex generated:

`enterprise-architecture/design/evidence/1.1.18/001/after/subject-incorporation/Harmonia-Subjects-unreleased.xml`

However, the generated XML did not faithfully reproduce the Solution Architect's authored Views hierarchy from the incoming `004.xml`.

The previous execution report confirms that only a minimal G18.03 presentation path was added to the maintained projection.

The Solution Architect has confirmed that the original Views remain intact in `004.xml`.

The canonical architecture resides under:

`enterprise-architecture/continuum/`

The maintained ArchiMate projection must remain consistent with that authority.

### Constraints

**Architectural authority**

The canonical Continuum remains authoritative for architectural elements, relationships, identities and semantics.

The incoming `004.xml` is authoritative input for this bounded reconciliation of the Solution Architect's authored presentation changes, not for unrelated architectural changes.

Do not promote architectural elements or relationships from the incoming XML into the Continuum without separate authorisation.

**Preservation**

Preserve:

- All existing canonical architectural resources and identities.
- The 32 newly incorporated Subjects.
- The 27 accepted Composition relationships.
- Existing G01–G13 architecture.
- Existing accepted architectural relationships.
- The 784 OpenSpec Motivation candidates and their assessments.
- Existing HCRL decisions, Change Reviews and provenance.

Do not discard or overwrite authored Views merely because their contents reference elements not yet incorporated into the canonical architecture.

Such references must be reconciled or explicitly reported as unresolved.

**Round-trip fidelity**

Treat the authored Views hierarchy as presentation information requiring faithful preservation.

Do not reduce the reconciliation to the previously accepted G18.03 correction.

Do not assume that preserving existing Views is equivalent to preserving the incoming authored presentation.

Do not replace the complete maintained projection with the incoming XML.

**Scope**

Do not undertake additional Subject, Capability, Feature, Motivation or requirements modelling.

Do not resolve DR-3–DR-9.

Do not change the architecture release number.

Do not overwrite the protected published 1.1.18 XML.

Do not close HCRL 001.

### Authorised Activity / Scope

#### 1. Establish the presentation reconciliation baseline

Compare the authored presentation structures in `004.xml` against:

- The maintained ArchiMate projection template.
- The existing published 1.1.18 XML.
- The unreleased Subject incorporation projection.

Identify the presentation changes required to reproduce the Solution Architect's authored Views.

Distinguish presentation changes from architectural semantic changes.

Use bounded comparison and evidence techniques consistent with the existing Round-Trip Praxis.

#### 2. Reconcile the maintained presentation

Incorporate the authored Views organisation and presentation changes into the maintained projection.

Preserve existing canonical element and relationship identities.

Resolve references using established canonical-to-Exchange identity mappings.

Where a View references an unresolved architectural element or relationship, preserve the evidence and report the conflict rather than inventing an architectural mapping.

Retain the accepted G18.03 spelling correction.

Do not introduce unrelated architectural changes.

#### 3. Generate a reconciled verification XML

Generate a new, unreleased ArchiMate Open Exchange XML from the canonical Continuum and reconciled maintained presentation.

Preserve the existing verification projection and the original `004.xml` unchanged.

The new XML must contain:

- The accepted 32 Subjects.
- The accepted 27 Composition relationships.
- The reconciled authored Views hierarchy.
- All previously accepted architectural content.

Do not replace the existing published release.

#### 4. Verify round-trip fidelity

Perform the applicable governed validation.

Verify:

1. The authored Views folder hierarchy is faithfully represented, including empty folders.
2. Existing and new Views are preserved.
3. View membership, layout, geometry and presentation properties are preserved.
4. All references resolve to valid projected architectural identities.
5. The accepted Subject incorporation remains intact.
6. No unrelated architectural changes have been introduced.
7. The resulting XML conforms to the applicable Exchange schema.

Produce a bounded comparison of the incoming authored presentation and the reconciled projection.

Distinguish exact matches, intentional transformations and unresolved discrepancies.

Do not claim successful Archi import/export verification unless it has actually been performed.

### Report

Retain the formal Task and supporting input evidence under:

`enterprise-architecture/design/evidence/1.1.18/001/before/`

Retain the execution Report, reconciliation evidence and verification results under:

`enterprise-architecture/design/evidence/1.1.18/001/after/`

Follow the repository's established evidence naming, provenance and validation requirements.

The Report must identify:

1. Presentation changes discovered and incorporated.
2. Authored Views and organisation structures preserved.
3. Any unresolved references or presentation differences.
4. Architectural elements and relationships confirmed unchanged.
5. Verification outcomes and limitations.
6. The exact path of the newly generated XML.
7. Any remaining requirements for Solution Architect visual acceptance.

### Governance Boundary

This task authorises **presentation reconciliation only**.

It does not authorise additional architectural incorporation, unrelated XML changes, HCRL closure, release transition, commit or push.

The resulting XML is a verification candidate for Solution Architect import into Archi.

**Stop after generating and validating the reconciled XML.**

The Solution Architect will import it into a separate Archi model and assess whether the authored Views have been faithfully reproduced before authorising any subsequent governance activity.
```
