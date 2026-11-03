<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# HCRL lifecycle commands

Implemented shared definitions in [AGENTS.md](../../AGENTS.md).
Codex discovers them through root instructions; [Junie guidelines](../guidelines.md)
points to the same authority.

| Command | Operations and outcome |
| --- | --- |
| HCRL Updated | Apply authorised iteration work and checks; preserve candidate/evidence and decision history; carry unresolved matters forward; establish exactly the next active pending iteration. No release or accepted publication. |
| HCRL Finalised | Verify history/state, obligations and SA confirmation; establish a valid release plan; use existing allocation/publication/registration machinery and complete only the authorised Git transition. |

Both commands define inputs, discovery, retry handling and fail-closed stops.
Existing review/evidence, validation, workbench, candidate-generation, release and
publication tools are reused. Iteration candidates use explicit evidence-local
outputs; Makefile `continuum` is reserved for terminal publication because it
includes `--publish`. No new tooling or release-identity semantics were introduced.

Verification: local links and shared discovery, authoritative Task event/prefix,
manual command/closure/retry/release semantics review, append-only HCRL history,
130 protected pre-existing file hashes, and bounded diff/whitespace/file checks.
No live Junie session or end-to-end lifecycle execution was performed.

Orchestration is supplied by agent instructions. DR-20 remains UNRESOLVED;
HCRL 1.1.19-001 remains pending. Neither command ran. Release remains 1.1.19 /
predecessor 1.1.18; no publication, iteration advance, allocation, commit or push.

Changed files (six): AGENTS.md; .junie/guidelines.md; an appended factual note in
existing DR-20; the required formal Task and Report; and this working report.
Exact paths, authority, review and limitations are in the
[formal Report](../../enterprise-architecture/design/evidence/1.1.19/001/after/ai-agent-reports/20261103T032921Z-hcrl-lifecycle-command-definitions.md) and its linked Task.
