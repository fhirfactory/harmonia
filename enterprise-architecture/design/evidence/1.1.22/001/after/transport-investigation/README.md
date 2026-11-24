<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Archi transport investigation — 1.1.22/001

The strict failure reflects repeated serialization differences already present
in 1.1.21. No IU graph/text defect or comparison-algorithm defect was demonstrated.
**Strict verification remains FAIL.** Original global connection order and
unspecified style state require presentation-fidelity treatment; no exception is
applied. The candidate and canonical incorporation are unchanged.

[Formal investigation Report](../ai-agent-reports/20261111T034947Z-archi-transport-failure-investigation.md)
records the root causes, exact counts, baseline comparison, remaining risks and
verification outcomes.

| Evidence | Purpose |
| --- | --- |
| [Candidate exhaustive analysis](candidate-v2-analysis/analysis.json) | Every raw category and representative differences; per-ID graph/text, navigation and View comparisons |
| [1.1.21 exhaustive analysis](baseline-1.1.21-analysis/analysis.json) | Equivalent accepted-predecessor control and compressed raw comparison |
| [Cross-release checks](cross-release-checks.json) | Same relationship/View changes; second-pass byte equality; exact count deltas |
| [1.1.21 execution](baseline-1.1.21-control/execution.json) | Actual Archi command, hashes and native/returned files |
| [Returned-candidate control](candidate-return-control/execution.json) | Actual second pass; returned Exchange identical to its input |
| [Strict replays](strict-verification-replays.json) | Both original-source strict verifications still FAIL |
| [Installed plugin bytecode](installed-archi-bytecode-evidence.txt) | Plugin hash and relevant exporter/importer method evidence |
| [Preservation](preservation-check.json) | 6,553 pinned input files unchanged; production comparison machinery unchanged |
| [Diagnostic negative tests](diagnostic-boundary-tests.txt) | Changes to meaning, geometry, routing, language and metadata remain detectable |

`analyse-transport.py` is a read-only evidence diagnostic. It inventories each
excluded dimension separately; it is not a new verification policy or acceptance
mechanism. `run-archi-control.py` uses isolated temporary configuration/workspace,
bounded execution and fresh output destinations. All exact original incorporation
transport evidence remains in the adjacent `information-unit-incorporation` directory.

Review [HCRL DR-3](../../../../../review/1.1.22/hcrl-1.1.22-001.pending.md#dr-3)
for the semantic conclusions and unresolved presentation treatment. No publication,
completion, iteration/release allocation, commit or push occurred.
