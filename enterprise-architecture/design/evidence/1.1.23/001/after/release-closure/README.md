<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# 1.1.23/001 — accepted release closure evidence

The [Solution Architect Task](../../before/ai-agent-tasks/unresolved-mnemosyne-final-acceptance-and-release-closure.md)
accepts DR-1–DR-5, supporting Minutiae, the actual Mnemosyne incorporation result,
native Archi-tree/HTML-reader visual reviews and CR-001 **Accepted — Qualified**,
specifically for **1.1.23**. Strict transport remains **FAIL**. Naming, deferred
vocabulary, generated-folder IDs, implicit style/global ordering, unproved pixel/
future-platform effects and unavailable Task event/prefix qualifications remain.

[Reviewed HCRL](../../before/release-closure/hcrl-1.1.23-001.reviewed.md),
[CR-001 snapshot](../../before/release-closure/change-review.reviewed.md) and
[input pins](../../before/release-closure/input-pins.json) preserve the originating
review state and authority correspondence. The final decision table has one row
each for DR-1–DR-5; the entry snapshot already had one DR-3 row.

| Evidence | Actual purpose/result |
| --- | --- |
| [Preflight](preflight.json) | Accepted 1.1.22 predecessor, exact candidate reproduced, 19 absent publication destinations; historical release-path conflict already closed in accepted successors |
| [Release metadata correspondence](release-metadata-correspondence.json) | Entire Exchange unchanged except root release 1.1.22 → 1.1.23 |
| [Canonical validation](resulting-candidate-validation.json) | PASS: 6,557 elements / 9,376 relationships / 1,304 preserved authored Views |
| [Generation log](publication-execution.txt) | PASS: ordinary `continuum continuum-rad` targets, no immutable predecessor overwritten |
| [Publication checks](publication-checks.txt) | PASS: release authority, root Manifest, both Exchange destinations, reader/narrative freshness, links and browser DOM contracts |
| [Candidate/publication correspondence](candidate-publication-correspondence.json) | PASS: selected generated release fields, independently verified model hash and generated links only |
| [Initial diagnostic](initial-candidate-publication-correspondence.json) | Retained evidence-harness FAIL; fixed template links were incorrectly evidence-rebased and expected derived model hash omitted; no publication/production change |
| [Integrity inventory](publication-integrity.json) | All 19 published paths, byte counts and hashes; manifests verified and both Exchange copies equal |
| [Verification summary](verification-summary.json) | Required 90-test architecture PASS; accepted strict FAIL and explicit temporal/naming qualifications |

Optional PDFs were not compiled. The narrative publication consists of LaTeX
source/parts and complete structure/source correspondence. No canonical, authored
View, maintained template, application or strict verifier changes occur in closure.
Only the existing expected-current patch mechanism allocates **1.1.23 from 1.1.22**;
registration and terminal provenance maintenance consume no additional release.

The formal closure Report and final Git receipt are linked here as they are
emitted. The accepted architecture commit and its actual push verification are
recorded before the final provenance receipt commit; that receipt's final SHA is
externally verified and reported without a self-referential commit identifier.

[Formal closure Report](../ai-agent-reports/20261111T083155Z-mnemosyne-final-acceptance-and-release-closure.md) · [Git transition receipt](git-transition.json).

Accepted release commit: `d3eee757244f3a185c8d8a3273180e4e7e78d6fa`; clean tree
verified at that commit. HTTPS push failed for unavailable terminal credentials.
The user explicitly elected to push the commits locally. Remote remained at
`fde98cde3c44b4f4b89e7f3a29765e1a44fca515` on observation. The local receipt records
this handoff; overall release closure awaits push and remote equality verification.
