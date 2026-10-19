<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Correction audit — reclassify internal 1.0.14 evidence

The Solution Architect's 2026-10-19 instruction removes internal governance/tooling
1.0.14 from the RAD architecture publication namespace. This is a location and
classification correction. It introduces no architecture change, new RAD release,
renumbered historical evidence or Information Architecture work. The existing
uncommitted RAD-1.0.9 change set retains its root release authority and architecture.
AX-14 preserves the distinction between architecture publication and internal
evidence; AX-17 preserves original meaning and provenance.

The destination and affected live references were identified before mutation.
The [pre-mutation reference inventory](affected-references-before.json),
[working-tree status](git-status-before.txt),
[171 protected-file hashes](protected-before.json) and
[original reference consumers](reference-consumers-before/) retain that basis.
Their [snapshot hash inventory](reference-consumers-before.json) identifies the
original consumer files independently of the updated tools and documentation.

| Original namespace | New non-RAD namespace | Original files |
| --- | --- | ---: |
| `docs/archimate/exchange/publish/1.0.14/` | `docs/archimate/governance-tooling/1.0.14/exchange/` | 1 |
| `docs/archimate/processing/publishing/1.0.14/` | `docs/archimate/governance-tooling/1.0.14/processing/` | 9 |

The [relocation manifest](../../1.0.14/manifest.json) records all original/current
paths, SHA-256 hashes, sizes and original link bases, with
`classification=governance-tooling-evidence` and `rad_architecture_release=null`.
Every original file is byte-identical. Original names, embedded release metadata,
historical Dispositions/Outcomes and recorded commands remain unchanged.
Neither original publication directory remains, including aliases or symlinks.

The [historical location map](../../../framework/historical-locations.json) adds
ten exact file mappings and two directory mappings. Its `non_rad_evidence` entry
classifies 1.0.14 independently of the RAD release authority. Frozen reports,
ledgers and reviews retain their original locator strings; the map resolves them
to the unchanged evidence. Relative links are resolved using the original link
base recorded by the manifest. Current navigation is provided in the
[archive index](../../1.0.14/README.md). The complete
[historical link-resolution inventory](historical-link-resolution.json) records
each frozen report's original target and its existing current file.

Live references changed in the exchange publication index, framework/workspace
documentation and historical location map. Publication path selection now
rejects an internal evidence identifier and rejects reintroduced directories or
symlinks under either RAD publication root. Current report selection no longer
compares RAD versions with the Home migration's internal governance version.
Four regression tests cover path selection, directory/symlink reinsertion,
exact historical navigation, evidence mutation and incorrect RAD classification.

Validation results and actual command logs are recorded in
[verification.json](verification.json). The applicable ArchiMate, governance and
Java architecture suites, publication/report checks, canonical identity validation
and independent current-output reproduction were run after relocation. The
current 15 generated RAD outputs reproduce byte-for-byte; the preserved 1.0.14
normalisation XML also reproduces byte-for-byte in memory using its original
literal model metadata. No internal-evidence reproduction writes to a RAD
publication directory or changes release authority.

The preserved reconciliation verifier contains a literal old hash-ledger path.
The current [evidence verifier](../../../framework/tools/verify_governance_evidence.py)
runs that unchanged historical verifier with only its hash-path lookup resolved
through the explicit location map. Its resulting audit equals the original
RAD-1.0.9 preservation record. All 171 protected files, including governed baseline
sources, ingestion/review history, RAD-1.0.9 model and publication evidence, retain
their original bytes. The RAD model SHA-256 remains
`9f120d6fc36d7e659f3799b0af5f7c1d4544b6591abd31e45ccb7393fb0162af`.

The full historical 1.0.14 report check already had stale live review dependencies
after later adjudication; that pre-existing limitation and its
[failed preflight evidence](../../../processing/ingestion/1.0.8/run-001/reconciliation/execution-001/reproduction.log.gz)
remain intact. This correction does not refresh historical attestations or claim
their original 14 outputs were produced for RAD-1.0.9. Relocation adds no loss
of evidence, model reproducibility or audit provenance. Human rendering acceptance
for RAD-1.0.9 retains its previous pending state.

To repeat the bounded checks from the repository root:

```bash
timeout --signal=TERM --kill-after=5s 30s python3 -B docs/archimate/framework/tools/verify_governance_evidence.py
timeout --signal=TERM --kill-after=5s 30s python3 -B docs/archimate/governance-tooling/migrations/2026-10-19-reclassify-1.0.14/reproduce-normalisation.py
PYTHONDONTWRITEBYTECODE=1 make -C docs/archimate/framework baseline-check baseline-report baseline-independent
timeout --signal=TERM --kill-after=5s 300s python3 -B -m unittest discover -s docs/archimate/framework/tests -v
timeout --signal=TERM --kill-after=5s 120s python3 -B -m unittest discover -s scripts/tests -v
```

The resulting repository structure is:

```text
docs/archimate/
├── exchange/publish/                 governed RAD exchange packages
│   └── 1.0.9/                        unchanged current model
├── processing/publishing/            governed RAD publication evidence
│   └── 1.0.9/                        unchanged current evidence
└── governance-tooling/               explicitly non-RAD
    ├── 1.0.14/
    │   ├── manifest.json
    │   ├── exchange/                 unchanged original XML
    │   └── processing/               unchanged original reports and evidence
    └── migrations/
        └── 2026-10-19-reclassify-1.0.14/
```

Existing genuine historical architecture publications remain in their original
release directories. Work stops after this correction and its validation.
