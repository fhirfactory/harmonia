<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Release verification diagnostics

The initial sandboxed Archi invocation failed before import at GTK display
initialisation. Its execution record and log remain under `../roundtrip/` with
the `sandboxed` suffix. The retry with local display access used the same
read-only publication and isolated temporary configuration/workspace/keyring;
it imported, exported, schema-validated and saved the native model successfully.

The first independent-publication comparison failed on validation JSON, because
the older `synchronise_baseline.py` entry point added its own validation field
and encoding after the new adapter returned. The current-release command now
delegates to the release adapter, preserving the established historical path.
The initial log is retained in `independent-publication-reproduction-attempt-001.log`.
The model XML hash did not change. The corrected independent comparison passes
for all nine publication/report outputs.

The first framework suite reported a setup error in the historical workspace
fixture: it invoked Run-002 generation against root authority 1.1.0 and hit the
correct unchanged-1.0.10 guard. The fixture now hash-checks the preserved outputs
and exercises the current shared naming/projector read-only, requiring exact
candidate and plan reproduction. The run-generation guard and all frozen run
evidence remain unchanged. The original result remains in
`framework-tests-attempt-001.log`; the final suite is recorded separately.

The frozen isolated Run-002 replay was executed successfully before advancing
root release authority; `frozen-run-002-replay.log` preserves that result. It
creates no new run. The final integrity audit independently checks all frozen
files after promotion. The original reconciliation plan retains its 1.0.10
authority binding; the release adapter validates that historical binding and
the separately authorised 1.1.0 transition without rewriting the plan.

The initial final-audit attempt reached the report link check before its own
JSON result existed. Recording now permits that exact pending self-reference;
the subsequent read-only check requires the recorded file and verifies it.
No model or publication output changed. The initial audit log is retained as
`independent-audit-attempt-001.log`.
