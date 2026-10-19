<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Harmonia ArchiMate framework

Reusable generation, validation, testing, comparison and recovery tools live here,
with schemas, projection configuration and framework baselines. `baseline/`
contains pinned historical XML and semantic/navigation locks for tests and replay.
`documentation-map.json` supports the projection. Architectural authority remains
in the [Markdown RAD](../../markdown/).

Requirements: Python 3.9+ and `lxml` from `requirements.txt`. From the repository root:

```bash
PYTHONDONTWRITEBYTECODE=1 make -C docs/archimate/framework all
python3 -B docs/archimate/framework/tools/governed_metadata.py validate
python3 -B docs/archimate/framework/tools/recover_v02.py --check
python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py diagnose --baseline-context
```

`make all` generates the current publication, runs publication/recovery,
identity/naming and round-trip tests, checks saved outputs/reports and independently
reproduces all **15** current generated outputs byte-for-byte (14 for the
historical publication set). Generation reads the root
release authority and allocates no Canonical IDs. The XML goes to
`../exchange/publish/<release>/`. Validation, report and guide go to
`../processing/publishing/<release>/`. Eleven current textual/JSON projection outputs remain
in governed Markdown RADS. Complete 1.0.8 report generation requires its
hash-pinned `workspace-verification-1.0.8.json`; use
`python3 -B docs/archimate/framework/tools/report_baseline.py` to render it after
verification. The report checker does not manufacture passing evidence.

The existing Increment 1 fidelity diagnostic accepts explicit input paths.
A bare `--incoming` filename resolves inside `exchange/ingest/`:

```bash
python3 -B docs/archimate/framework/tools/diagnose_roundtrip.py compare \
  --published docs/archimate/exchange/publish/1.0.8/Harmonia-Architecture-1.0.8.xml \
  --incoming proposed-export.xml \
  --output /tmp/harmonia-transport-diagnostic.json
```

This diagnostic does not implement Increment 2 architectural comparison or
reconciliation. An absent export remains an awaiting-input result. Explicit
relative and absolute input paths continue to work. Diagnostic outputs remain
restricted to `.junie/reports/` or `/tmp` by the existing Increment 1 protection.
The new `processing/ingestion/` boundary reserves future working state and evidence;
this task adds no workflow or new comparator normalisation.

The subsequently governed [Ingestion Change Review contract](../../markdown/governance/archimate-ingestion-change-review.md)
and [run convention](../processing/ingestion/README.md#run-layout) define review
records and executable authorisation. They add no comparator or reconciliation
implementation and do not relax the Increment 1 diagnostic output restriction.

`recovery-check` replays the pinned v0.2 snapshot with its 1.0.5 release fixture.
Earlier report renderers and `legacy-all` are historical replay machinery; they
require their corresponding historical release/verification fixtures and do not
replace current gates. Historical templates, manifest hash keys, UUID namespaces
and stable identity keys deliberately retain their original spelling.

`workspace_paths.py` resolves current framework, publishing/ingestion processing,
publish and ingest locations relative to the framework. Its explicit
`historical-locations.json` map resolves relocated historical inputs without
changing historical text or expected hashes. No obsolete umbrella directory or
compatibility wrapper remains. See the [workspace](../README.md), preserved
[Increment 1 procedure](roundtrip-increment-1.md) and [former README](historical-workspace-readme.md).

The [Architecture Modelling Strategy](../../markdown/governance/architecture-modelling-strategy.md)
governs explicit Home independently of ingestion. `home_relationships.py` reads
Archi's explicit specialisation through its property definition and replays only
the fixed accepted Business Home register. It infers no Home. Added Home edges
are applied after legacy layout generation, so this change prescribes no new
folder/View placement. The historical Home migration's bounded publication
validation and release report remain in `processing/publishing/1.0.14/`;
historical report renderers retain their original release scope and 14-output set.

The authorised 1.0.8 reconciliation produces **RAD-1.0.9**, independently of the
preserved governance/tooling sequence. `reconcile_108.py` replays the immutable
[accepted resource capture](../../markdown/governance/baseline/accepted-ingestion-1.0.8.json)
after the existing projection. The capture persists nine allocated identities;
publication allocates none. Explicit Home remains independent of Realization.
The 93 unestablished and 18 ambiguous baseline Home cases remain unresolved.
The accepted Product catalogue is the fifteenth output. `report_baseline.py`
dispatches the current evidence-backed reconciliation report and inspection guide
in `processing/publishing/1.0.9/`. Run `verify_baseline_outputs.py` for independent
reproduction of all 15 outputs, including the report and guide. The real Archi
no-edit transport evidence and pending human rendering gate are recorded there.
