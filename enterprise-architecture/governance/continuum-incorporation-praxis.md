<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architectural element incorporation Praxis

This Praxis operationalises the [continuum representation](continuum-representation.md)
under the common [Architecture Review Praxis](architecture-review-praxis.md).
The operative Task/HCRL determines creation, change and allocation authority.

For DesignNote release processing, apply the
[ARB assertion-disposition rules](continuum-representation.md#arb-semantic-reconciliation-follow-up).
Review individual normative assertions and their later adjudications, retaining
an explicit disposition and exact source correspondence for each. Use native
Constraint/Requirement or the governed CourseOfAction specialisation
ImplementationGuideline where that is what the assertion means. Existing canonical
assertions can represent the meaning without allocating duplicates. Preserve
genuine design deferrals and the DesignNote itself as attributable capture/evidence.

1. **Identify.** Read the actual source and its governing decisions. Establish the
   bounded architectural thing, full responsibility/definition, exclusions and
   assertion standing. Distinguish an element from a category, example, bundle,
   governance rule, implementation name or representation.
2. **Classify.** Use native ArchiMate semantics and the governed mapping contract.
   Apply Feature/Topic only with their explicit source evidence. A missing native
   mapping becomes an HCRL question for the affected content; continue other work.
3. **Select.** Read type-catalogue.json and generate the base template for the native
   type and applicable specialisation. Its directory is deterministic. Do not
   create a document category to accommodate a difficult source.
4. **Resolve identity.** Search canonical IDs, exact scoped source bindings,
   accepted reconciliation and retained history. Merge contributions only when
   evidence establishes the same instance. Preserve an existing ID. For a newly
   established, independently bounded construct with explicit allocation authority,
   allocate one UUIDv4 through `create`; persist it before any projection. A failed
   or interrupted creation requires inspecting the existing result before retrying.
5. **Populate.** Preserve the source definition, boundaries and material qualifiers
   in Markdown. Populate identity-independent names from governed lexical and
   contextual rules. Preserve missing formal vocabulary or ancestry explicitly.
   Materialise each independently established element in its own file; reference
   other elements. Do not derive native edges from headings or prose proximity.
6. **Record provenance and standing.** Record exact source sections/records, editions,
   authority and correspondence. Source control can preserve original bytes;
   uncommitted unique inputs require retained evidence. Semantic standing,
   currentness and placement remain independent. Do not infer acceptance from
   a successful transform, source location, canonicalId or generated diagram.
7. **Place.** Write to the profile-selected native/specialisation directory using
   the persisted canonicalId filename. Editing an existing element retains its
   identity. Retyping, semantic replacement, splits and merges require their actual
   architectural authority and lineage; the tool cannot decide them.
8. **Validate and exercise.** Run the continuum validator, meaningful mutation tests,
   projection schema/reference/identity checks and deterministic regeneration.
   Exercise the real changed elements and preserve affected Views. For a submitted
   Archi change, follow the existing exact-baseline, review and application gates;
   accepted reconciliation updates canonical Markdown, then reprojection verifies
   the result. Publication never allocates or self-accepts architecture.
9. **Retire incorporated carriers.** Account for each source section. Retire only
   successfully incorporated content with explicit replacement references and
   unchanged historical text. Retain and identify unresolved content. Do not leave
   a successfully ingested multi-element document as another active authority.
10. **Report.** Record actual application, validation, limitations and unresolved
    meaning in the applicable HCRL DR. Retain the required Task/Report and selected
    correspondence evidence. Submit the real result for SA/ARB review; completion
    follows the Solution Architect's confirmation.

For an authorised `Create Principle ABC ...` request, select Principle, use the
Principle template, establish the definition and standing, check existing exact
identity evidence, allocate/preserve the UUID, apply MOT naming where semantic
inputs are sufficient, and place the file under motivation/principle. For
`Create Feature XYZ ...`, select native Capability plus Feature and preserve its
actual CT3 framing and uncertainty. Neither request creates identity from its name.

Useful commands, from the repository root:

```bash
python3 scripts/architecture_continuum.py template --type Principle
python3 scripts/architecture_continuum.py create --spec /tmp/authorised-element.json
python3 scripts/architecture_continuum.py validate
python3 scripts/architecture_continuum.py project --output /tmp/harmonia.xml
python3 scripts/continuum_manifest.py
python3 scripts/continuum_manifest.py --check
```

The source-specific `ingest_rad_*` adapters implement the bounded RAD-1.1.5
migration. They are not prospective prose classifiers. Future agents use the
canonical sources and this Praxis; adding more ingestion rules requires actual
source/classification evidence and the operative task's authority.

For prospective names with sufficient governed inputs, retain `naming.inputs`
using the existing lexical/type descriptor contract and check the persisted names
against it. For example a local Principle may use `{"key":"task-scoped-input",
"type":"Principle","name":"ABC"}`. This yields `Abc` and `MOT-Abc` under the
existing Motivation rule. Retain any required spelling decision.
Do not manufacture ancestor descriptors to complete a name. The bounded migration
retains accepted naming expectations and explicitly unresolved inputs.

Run `make -C docs/archimate/framework all` for current publication, the root
[review manifest](../continuum/MANIFEST.md), mutation tests and staleness checks.
Regenerate the manifest after authorised element changes; edit canonical sources
through this Praxis rather than editing generated rows. The manifest preserves
source qualifications and supplies navigation, never architectural acceptance.
Run `verify_continuum_reproducibility.py --report <path>` to
prove maintained-input independence. Publication does not compile or ingest old
LaTeX workspaces. For a PDF catalogue, run XeLaTeX twice on the generated
`continuum-catalogue.tex` with a fixed `SOURCE_DATE_EPOCH` in a temporary output
directory, inspect the result, and retain the compiled PDF as a derived artefact.
