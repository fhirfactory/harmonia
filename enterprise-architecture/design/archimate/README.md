<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate projection and review workbench

The maintained source is the [individual-element continuum](../../README.md).
[projection-template.xml](projection-template.xml) owns existing model-level
properties, property definitions, navigation and **1,209 Views**, including their
geometry. Its element/relationship collections are empty. The current projector
fills those collections from canonical Markdown, preserves the Views and adds
navigation for newly represented elements.

Import the [RAD-1.1.5 Exchange model](../../continuum/documentation/published/1.1.5/Harmonia-Architecture.xml)
into Archi 5.10. A local native `.archimate` workspace is a human workbench;
exported submissions are review inputs. Neither overwrites the canonical source
or supplies acceptance. Preserve the exact baseline and submitted export under
the originating iteration's evidence contract when they materially support review.

```bash
python3 enterprise-architecture/tooling/continuum_workbench.py review \
  --baseline /path/to/exact-current-projection.xml \
  --submitted /path/to/complete-model-export.xml \
  --output /path/to/iteration/evidence/review.json
python3 enterprise-architecture/tooling/continuum_workbench.py apply --decision /path/to/reviewed-decision.json
```

Commands run from the repository root. Review correlates only valid canonicalId
claims. It reports native fields, property values, relationships and Views without
writing sources. The baseline must match the current projection byte for byte.
The [existing Round-Trip Praxis](../../governance/archimate-round-trip-engineering-praxis.md)
and common HCRL process govern adjudication; the JSON adapter does not replace them.

A decision names `authority` (repository-relative HCRL path and DR anchor), `review`
(repository-relative review JSON), its exact `reviewSha256`, a `dispositions` map,
and `replacements`. Every reviewed element uses its canonicalId as disposition key;
relationships/Views use `relationship:<identifier>` / `view:<identifier>`. Each
value records `action` (`apply`, `reject`, `defer`) and an attributable `reason`.
Each applied replacement supplies complete `metadata` and `body`. Existing identity,
native type and specialisation must remain unchanged in this bounded adapter.
The review hash, complete disposition coverage, exact baseline and source preimages
are checked before prospective validation and writes. The adapter validates the
presence of recorded authority, not its authenticity; agents must read the actual
Solution Architect disposition and obey its scope.

New elements, retirements, splits, merges and retypings require their separately
reviewed identity/lineage work under the incorporation Praxis. Relationship or View
application requires reviewed amendment to the owning canonical element or maintained
presentation template, followed by projection and verification. Record deferred
items explicitly. This adapter rejects an unsupported automatic `apply` rather
than silently dropping part of a submission. Multi-file writes are preflighted,
but no filesystem transaction or concurrent-writer guarantee is claimed.

The [actual Archi transport evidence](../evidence/1.1.5/002/after/archimate-validation.json)
distinguishes canonical semantics from raw XML transport. The no-edit return
preserves canonical values/owners, native relationships and Views. Archi adds
language attributes and restructures organization folders; whole-model raw
fidelity is not claimed. Preserve the presentation source and exact returned
control; do not silently replace the template with that return. The full raw
comparison can be regenerated with `python3 enterprise-architecture/tooling/exchange_compare.py compare --baseline <exact-XML> --submitted <returned-XML-or-gz> --report <report.json>` using the
published XML and retained `returned-model.xml.gz` (decompress first).
