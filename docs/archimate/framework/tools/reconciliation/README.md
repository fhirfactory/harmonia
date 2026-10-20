<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Accepted information reconciliation adapter

`information.py` executes an agent-reviewed, hash-pinned plan under the
[Round-Trip Praxis](../../../../markdown/governance/archimate-round-trip-engineering-praxis.md).
The agent must first interpret the explicit human adjudication and issue precise
decision-linked instructions. This adapter does not interpret prose or decide
architectural acceptance. It supports bounded information additions, retypings,
renames, new semantic relationships and authored View additions/revisions.

`--capture` validates exhaustive scope and canonical correlation against the exact
baseline/control, derives naming through shared containment, allocates only the
explicitly approved absent element identities, and preflights the complete result.
It then persists resource-owned records in RADS before rendering from those records.
Existing source/output paths refuse capture. An interrupted operation requires
inspection of the persisted source and replay of its IDs; never delete the source
to allocate replacements. No relationships or presentation objects receive IDs.

`--replay` produces a new processing output directory from persisted records;
pass the original output's `source-manifest.json` as `--established-manifest`.
`--check` reconstructs outputs in memory and compares them without writes or
allocation. Both verify the established source hash (including all allocated IDs),
pinned input, decision and issued instruction hashes and
unchanged root release authority. They validate persisted identity and naming
claims rather than repair them. Shared projectors generate all Element/Relationship
navigation, prove independence from absent/misleading submitted folders, and
preserve every authored View and its navigation exactly. No publication is made.

From the repository root, the accepted Run-002 result is checked with:

```bash
timeout --signal=TERM --kill-after=5s 180s python3 -B \
  docs/archimate/framework/tools/reconciliation/information.py \
  --plan docs/archimate/processing/ingestion/1.0.10/run-002/reconciliation/execution-001/plan.json \
  --source docs/markdown/governance/baseline/accepted-ingestion-1.0.10.json \
  --output docs/archimate/processing/ingestion/1.0.10/run-002/reconciliation/execution-001/artifacts \
  --check
```

Actual final-model Archi transport and publication gates remain separate. The
original machinery-validation candidate and its replay remain frozen inputs.
