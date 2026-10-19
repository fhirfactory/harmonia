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
the fixed accepted Business Home register. It infers no Home.

------- Legacy Content - Superseded ------- Start ------
Superseded by: [Home-derived naming/navigation](../../markdown/governance/architecture-modelling-strategy.md#home-derived-naming-and-navigation)

Added Home edges
are applied after legacy layout generation, so this change prescribes no new
folder/View placement.

------- Legacy Content - Superseded ------- Finish ----

The historical Home migration's bounded publication
validation and report remain in `governance-tooling/1.0.14/processing/`;
historical report renderers retain their original release scope and 14-output set.
The [non-RAD evidence manifest](../governance-tooling/1.0.14/manifest.json) and
exact historical location map preserve the original paths and hashes. The
publisher rejects this internal identifier as a RAD release and rejects its
reappearance in either architecture publication namespace. RAD report selection
does not compare architecture versions with internal governance/tooling versions.

```bash
python3 -B docs/archimate/framework/tools/verify_governance_evidence.py
```

Run this from the repository root to verify the relocated evidence and protected
RAD-1.0.9 bytes, and replay the preserved reconciliation audit using only the
explicit historical locator map. Frozen ledgers and reports retain their original
text and original link bases; they are not refreshed as RAD-1.0.9 evidence.

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

The RAD-1.0.9 completion adds `home_context.py` after accepted-instance replay.
It uses explicit Home and governed semantic ancestry for both qualified names and
principal resource navigation. The current resource fields and prior-name provenance
are persisted in `docs/markdown/governance/baseline/home-derived-metadata.json`;
ordinary publication validates them and allocates no identities. The original
captures, Home register, ingestion/audit records and pre-completion candidate remain
preserved. Relationship references follow each source's resulting location.
Generated View presentation is unchanged.

```bash
python3 -B docs/archimate/framework/tools/home_context.py audit
python3 -B docs/archimate/processing/publishing/1.0.9/home-context-completion/roundtrip/verify-transport.py
```

The [completion evidence](../processing/publishing/1.0.9/home-context-completion/README.md)
records the full 212-element population, all six named examples, 111 untouched
unresolved Home cases and the new actual Archi transport. It completes the unsigned
RAD-1.0.9 candidate without a further release increment. The original
`reconciliation-verification.json` remains historical; the current report reads
`home-context-completion/verification.json`. The governance evidence verifier
replays historical resulting-state signatures against the preserved original
candidate, separately from current Home-projection validation.
