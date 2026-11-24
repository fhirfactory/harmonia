<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Task Detail — HCRL 1.1.4-004 Praxis implementation

**Task-Issued-At: 2026-10-21T06:43:22.583Z.** Event: the operative user instruction entered
the agent interaction, recorded in system `response_item` / `message`, role
`user`, line 9 of `/home/hunterm/.codex/sessions/2026/10/21/rollout-2026-10-21T17-43-20-01a15d33-374c-7952-84af-4ea7d32c2480.jsonl`.
The established millisecond precision is preserved; the UTC filename prefix
comes from this same event to seconds. This is not human composition time or
later capture time. The operative text is retained below so the local log is
not a continuing dependency. Its SHA-256 (UTF-8, without added terminal newline)
is `d7810d898c0c3404d07b9abc9b23c04792442828085e5d33686955e6a5018204`.

Correspondence: [pending HCRL 004](../../../../../review/1.1.4/hcrl-1.1.4-004.pending.md),
[demonstrated 003 baseline](../../../../../review/1.1.4/hcrl-1.1.4-003.pending.md).
The supplied UI attachment is the existing [001 pending HCRL](../../../../../review/1.1.4/hcrl-1.1.4-001.pending.md);
the system-recorded message body below contains no rendered attachment suffix.
The corresponding Report belongs under `004/after/ai-agent-reports/`.
The user explicitly authorises prospective Praxis/governance implementation,
carrying 003 forward and reserving substantive migration/adjudication for 005.

<!-- ORIGINAL MESSAGE START -->
For 004, I would now make Codex implement the Praxis that 003 has demonstrated, rather than revisit the repository refactoring. The key is that 004 carries 003 forward and turns the demonstrated structure into governed process.
Goal
Create and apply HCRL 1.1.4-004 to implement the Architecture Review, HCRL and evidence Praxis demonstrated by 1.1.4-003.
Carry forward all applicable 003 decisions and standing material.
This task changes the governed Praxis and supporting governance documentation/machinery. It does not continue or adjudicate the substantive RAD-1.1.4 repository migration. That resumes in 1.1.4-005.
Expected Outcome
The governed Architecture Review Praxis SHALL explicitly establish the following model:
Architecture Review activity
        │
        ├── transient working material
        │       └── not retained merely because it existed
        │
        ├── durable evidence
        │       └── design/evidence/<RAD-version>/<HCRL-iteration>/
        │               ├── before/
        │               └── after/
        │
        └── HCRL
                └── governed semantic change/decision record

The evidence structure SHALL therefore be:
enterprise-architecture/design/evidence/
└── <RAD-version>/
    └── <HCRL-iteration>/
        ├── before/
        └── after/

For substantive AI-agent activity associated with an HCRL iteration:
before/
└── ai-agent-tasks/
    └── <timestamp>-<task>.md

after/
└── ai-agent-reports/
    └── <timestamp>-<report>.md

The iteration is the provenance boundary. Do not create different provenance models for review activity versus authorised-change execution.
The governed semantics SHALL distinguish:
- Working material — transient material used while performing an activity. It is discardable and is not governed evidence merely because it existed.
- Evidence — durable inputs, outputs or supporting material retained because they materially support, explain, reproduce or verify the governed activity associated with an HCRL iteration.
- HCRL — the authoritative semantic change/decision record for the governed RAD transition. It is not the Architecture Review working document and is not a substitute for detailed supporting evidence.
Context
1.1.4-003 demonstrated the corrected structure against real repository activity.
The previous Praxis inherited from RAD-1.1.3 distinguished substantive AI-agent review/preparation activity from authorised execution activity and retained formal Task/Report provenance only for the latter.
That distinction has proved too narrow.
RAD-1.1.4 demonstrated that substantive Architecture Review activity can itself produce material evidence whose provenance must be durable and reproducible.
The corrected model is therefore based upon the HCRL iteration, not upon an artificial review-versus-execution provenance distinction.
For example:
design/evidence/1.1.4/
├── 001/
│   ├── before/
│   └── after/
├── 002/
│   ├── before/
│   └── after/
├── 003/
│   ├── before/
│   └── after/
└── 004/
    ├── before/
    └── after/

Each iteration records the durable evidence associated with the governed activity represented by that iteration.
1.1.4-003 has already refactored the existing 001/002/003 evidence into this structure and recovered attributable historical AI-agent Task/Report provenance where available.
Treat the actual 003 repository result as the demonstrated baseline for this Praxis implementation.
Constraints
Carry forward the applicable 003 decisions and standing material.
Do not rewrite the history of 001, 002 or 003.
Do not imply that the corrected Praxis existed before 004. 003 demonstrated and refactored the structure; 004 governs it prospectively.
Do not resume, execute or adjudicate the substantive documentation/repository migration. That is reserved for 1.1.4-005.
Do not alter substantive migration mappings, classifications, findings or recommendations carried through from 002/003.
Do not introduce separate evidence taxonomies for “review” and “execution”. The HCRL iteration is the provenance boundary.
Do not require retention of every intermediate artefact created by an agent. Retention is based on whether material constitutes durable evidence, not merely whether it was produced.
Do not turn the HCRL into a manifest of every evidence file or into the Architecture Review working area. References to evidence should be sufficient to establish traceability without reproducing its detailed content.
Do not invent historical provenance.
Preserve the existing requirements for attributable AI-agent Task and Report timestamps. Adapt them to the iteration-scoped model rather than discarding them.
Preserve the existing Solution Architect authority and Architecture Review governance established by RAD-1.1.2 and subsequent accepted changes.
Where existing governance text conflicts with the newly authorised iteration-scoped evidence model, update it explicitly rather than leaving contradictory rules in place.
Authorised Activity / Scope
You are authorised to:
1. Inspect the governance and Praxis documents affected by the 003 decision.
2. Create hcrl-1.1.4-004.pending.md, carrying forward applicable 003 material and recording the Praxis implementation decisions.
3. Retain this formal 004 Task under:
enterprise-architecture/design/evidence/1.1.4/004/before/ai-agent-tasks/

4. Update the governed Architecture Review Praxis to implement the iteration-scoped evidence model.
5. Update architecture-governance.md, architecture-change-provenance.md, templates, README/navigation material, tests and supporting governance machinery where required for consistency.
6. Remove or supersede the RAD-1.1.3 execution-only Task/Report rule where it conflicts with the new model, while preserving historical truth about what 1.1.3 governed at that time.
7. Ensure the governed process recognises that substantive AI-agent activity may occur during investigation, review, corrective work, application or verification and that the associated iteration provides its provenance boundary.
8. Ensure durable review outputs such as mappings, reference inventories, checks and similar supporting artefacts are treated as evidence where they materially support the review.
9. Ensure transient working material is not required to be retained.
10. Update automated checks/tests where necessary to enforce the new governed structure.
11. Run the complete relevant architecture/governance test suite.
12. Produce the formal 004 Report under:
enterprise-architecture/design/evidence/1.1.4/004/after/ai-agent-reports/

Report
The formal Report SHALL identify:
- governance/Praxis documents changed;
- previous rules superseded or amended;
- resulting normative definition of working material, evidence and HCRL;
- resulting iteration-scoped evidence structure;
- treatment of substantive AI-agent Tasks and Reports;
- changes to templates, navigation, validation or tests;
- tests/checks executed and their results;
- any inconsistency or ambiguity requiring Solution Architect adjudication;
- confirmation that applicable 003 decisions were carried forward; and
- explicit confirmation that substantive RAD-1.1.4 repository migration/adjudication was not resumed.
Where a governance conflict cannot be resolved without changing an architectural decision beyond the authority already established by 003, surface it for Solution Architect adjudication rather than inventing a rule.
Stop after implementing the 004 Praxis, producing the 004 Report, and updating hcrl-1.1.4-004.pending.md. Do not proceed to the substantive migration decisions intended for 1.1.4-005.
<!-- ORIGINAL MESSAGE FINISH -->
