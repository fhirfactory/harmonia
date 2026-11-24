<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Current EA operational independence — implementation report

Report-Produced-At: 2026-10-22T07:50:14Z

Event: completed Report emission in this Codex interaction; attributable UTC system
clock captured at emission. This is distinct from Task issuance, execution,
publication and architectural acceptance.

Task: `enterprise-architecture/design/evidence/1.1.6/001/before/ai-agent-tasks/unresolved-ea-operational-independence.md`.
Semantic review record: `enterprise-architecture/design/review/1.1.6/hcrl-1.1.6-001.pending.md`.
Originating Git state: `7fda4410a9d3583d674d9530710b86b4ec748c2a`.
Root release prepared once: **1.1.5 → 1.1.6**. RAD-1.1.5 holistic review remains
pending; neither this patch nor successful verification supplies its acceptance.

## Result and changes

Current EA operations use `enterprise-architecture/tooling/` and maintained
Continuum, governance and design inputs. Current operations no longer import,
execute or read Legacy-RAD code, schemas, configuration, baseline captures,
identity registries, location mappings or operational governance. Historical
source locators remain source/provenance, with explicit maintained successors
for the bounded promoted governance references.

The normal build is `make -C enterprise-architecture/tooling all`.
The independent proof is `make -C enterprise-architecture/tooling verify-independent`.
The tooling README documents installation, creation, validation, review/apply,
raw comparison, attributed transport evidence, publication, standing checks
and the optional PDF procedure.

| Required capability | Current implementation |
| --- | --- |
| Governed creation, validation, templates and Open Exchange projection | `architecture_continuum.py`; maintained catalogue/schema/applicability/identity boundary configuration |
| Canonical-source Archi review and adjudicated application | `continuum_workbench.py`; exact baseline, review hash, disposition coverage, source preimages and prospective validation retained |
| Current lexical/type naming and native containment | Extracted `governed_naming.py`, `home_relationships.py`, `relationship_state.py`, `containment.py`, `information_naming.py` |
| Secure XML, offline validation and raw transport comparison | `exchange_xml.py`, `exchange_compare.py`, `exchange_signatures.py`, `element_correlation.py`; five byte-preserving offline schema promotions |
| Manifest and publication | `continuum_manifest.py`, `publish_continuum.py`; HTML, Markdown index, LaTeX source and publication manifest |
| Release, formal evidence and current standing checks | `architecture_release.py`, `architecture_review_evidence.py`, canonical validation/projection, `verify_continuum_links.py` and mutation tests |
| Independent regeneration and regression proof | `verify_continuum_reproducibility.py`, current tests, Makefile and declared Python dependencies |

Current specialised governance is promoted under `enterprise-architecture/governance/`:
release policy, identity/naming, modelling strategy, authoring, round-trip,
Change Review/template, publication semantics and navigation/View rules. The
required native mapping and Capability/Feature framing rules are bounded current
profiles; the historical RAD identity/adoption/publication subsystem is excluded.
Still-active axiom Purpose, decision-relationship and review governance is
promoted alongside the existing individual-Principle index. Current common
governance, agent instructions, catalogue authority and operational links point
to maintained successors. Existing precedence, qualifications and unresolved
matters are retained.

Legacy edits consist solely of historical-standing notices and section-appropriate
supersession wrappers. Original legacy lines are preserved; no historical test
or evidence was repaired. The scripts directory explicitly identifies its EA
editions as historical, with no current fallback or second supported tooling
location. The historical framework Makefile remains unchanged apart from its
historical-standing comment.

## Dependencies eliminated and preservation

The old dependency chain through `governed_metadata`, `generate_views`,
`diagnose_roundtrip`, `change_review`, `home_relationships` and
`workspace_paths` is replaced by only the required current functions and shared
rules. Historical capture replay, establishment/bootstrap, adoption/identity
registry, source-ingestion/reconciliation adapters and old publisher dispatch
are excluded. **`architecture_identity.py` is unchanged and has no current
successor.** Current governance no longer obtains operative rules from legacy
governance carriers.

All **1,602 canonical element files are unchanged**. The generated root manifest
changes only its current command/generator references. Projection at release
1.1.5 before and after extraction is byte-identical:
`440ed943908b375b8d66818ceaa341969925db51a859a67c6f365d0461ffb28b`.
At 1.1.6 the parsed complete XML differs only in its single model release value.
The 1,651 relationships, 462 qualified source predicates, identities, source
standing, unresolved naming and authored Views are preserved. Five promoted
schemas match the originals byte for byte.

The generated 1.1.6 XML, HTML, index, catalogue source and publication manifest
are derived review outputs. Earlier published releases and PDFs remain intact.
Generation does not assert acceptance; no new PDF or actual Archi execution is
claimed.

## Remaining Legacy-RAD references

The bounded `legacy-reference-inventory.json` enumerates every remaining legacy
link in the maintained governance carriers inspected here: **12 link occurrences**
and **three explicit historical release/run literals**, plus the explicit
governance-successor keys. Their roles are:

| Legacy target | Historical/provenance purpose |
| --- | --- |
| Original architectural-axioms register | Original wording and prior section succession; current Principles/governance are under EA |
| Pre-adoption architecture-change-provenance contract | Superseded adoption input; current evidence rules are under EA |
| `baseline/documentation-classification.json` | Fixed historical text-classification evidence; not a current rendering input |
| `architecture-identity/adoption-1.0.3.md` | Provenance of old EC bindings; not a current identity mechanism |
| Original 1.0.8 submitted XML | Empirical Home interchange evidence; current semantics are in maintained contracts |
| `baseline/business-homes.json` | One-time historical migration assessments; no current inference or replay |
| `baseline/home-derived-metadata.json` (two links) | Historical resource-amendment and previous-name provenance; current persisted naming inputs are canonical |
| 1.0.9 Home completion evidence | Prior implementation/validation outcome; not a generation prerequisite |
| 1.0.10 canonical-name completion evidence | Prior naming adjudication/completion provenance; not a naming input |
| 1.0.8/run-001 Change Review (two anchors) | Original disposition/outcome and shared-metamodel decision context; maintained rules are promoted |
| Three 1.0.10 release/run literals | Explicitly bounded historical proving-cycle account; not current release/run discovery |
| Old paths in `authority-successors.json` | Explicit succession keys read from current configuration; legacy files are never opened |

Unchanged canonical source provenance, earlier design/evidence, released
publications and historical scripts preserve their originating locators and
standing. No corpus-wide reference rewrite or retrospective evidence migration
was performed. Current rendering resolves the bounded promoted authority links
from maintained configuration; other archival links remain locators.

Current link verification reports **1,143 current EA targets checked**, five
promoted authority links, **354 historical source/provenance links** and two
repository-instruction source citations. Historical files and the cited copy
of root AGENTS.md are not read by that check. Root AGENTS.md continues to govern
agent activity independently. Missing current targets/anchors still fail, with
a focused regression proving that distinction.

## Validation performed

- **PASS:** final documented `make -C enterprise-architecture/tooling all`;
  generation, **43 current tests**, release/staleness/projection/publication/link checks.
- **PASS:** isolated current pipeline with no `docs/`, `scripts/`, Git metadata,
  historical location configuration or historical processors. All 11 stages
  passed, including exact-baseline review, raw XML comparison and **42 tests**.
  Six generated artifacts matched the workspace byte for byte. Canonical hashes
  remained unchanged in both trees. The later focused helper/link suite
  passed all eight tests after the final small edits.
- **PASS:** browser DOM smoke test for 1,602 elements, identity search, selection,
  Feature filtering and combined currentness filtering.
- **PASS:** required Maven ArchUnit suite using offline dependencies:
  **90 tests, zero failures/errors/skips**, across 11 architecture test classes.
- **PASS:** original/promoted projector parity, single-release-value XML
  comparison, unchanged canonical sources, byte-identical schemas, additions-only
  legacy edits and `git diff --check`.
- **UNRESOLVED:** iteration evidence timestamp check, solely because the original
  Task issuance event/prefix is unavailable; formal Task/Report records are retained.

The first 120-second Python-suite attempt was **TIMEOUT** during the final
workbench test with continued progress. Repository-sized bounded runs passed.
Initial isolated link checks exposed the missing Kleio axiom-section alias and
repository-instruction source-citation distinction; both were corrected and
rechecked. The initial default Maven attempt failed before tests because it tried
to update a read-only dependency-cache status file; the offline invocation passed.
Missing Python dependencies were installed only into a temporary directory and
are now explicitly declared by the current tooling requirements.

Selected evidence is under
`enterprise-architecture/design/evidence/1.1.6/001/after/`:
`operational-independence.json`, `promotion-preservation.json`,
`legacy-reference-inventory.json`, `current-links.json`,
`architecture-tests.json` and `current-build.json`. Raw working logs and temporary
dependency/bootstrap material are not permanent architectural evidence.

## Matters for Solution Architect review

The original Task instruction-entry timestamp is not available to this agent;
the Task time and required filename prefix remain explicitly unresolved for
Solution Architect treatment before closure. No time was guessed from the clock,
filesystem or commit. Report production has its separately recorded event.

No additional architectural meaning or governance decision was invented.
The bounded HCRL records actual implementation/verification facts and awaits
Solution Architect result confirmation. Existing pending holistic review and
source-qualified unresolved properties retain their prior standing. Actual
Archi import/export and interactive visual inspection were not performed in this
tooling relocation; synthetic workbench tests and the raw comparator do not
supply those outcomes or architectural acceptance.

