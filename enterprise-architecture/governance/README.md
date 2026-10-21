<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture governance

[Architecture Governance](architecture-governance.md) defines the common
Architecture Review, Solution Architect authority, HCRL Decision Review Log,
repository structure, governance/architecture distinction and prospective
adoption. [Architecture Change Provenance](architecture-change-provenance.md)
defines required transition evidence and faithful recovery. The
[Architecture Review Praxis](architecture-review-praxis.md) gives the common
operational workflow for every Harmonia change, including required formal
AI-agent Task/Report execution provenance. The
[HCRL template](templates/hcrl-template.pending.md) supports submissions without
establishing approval or execution authority.

The adoption transition is recorded in the
[completed HCRL 1.1.2 iteration 003](../design/register/1.1.2/hcrl-1.1.2-003.complete.md),
confirmed by Mark Hunter, Solution Architect, on 2026-10-21 with the instruction
“result confirmed - complete”. The issued decisions remain preserved in
[reviewed iteration 002](../design/review/1.1.2/hcrl-1.1.2-002.pending.md). The
[Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md)
and [release policy](../../docs/markdown/governance/architecture-release-policy.md)
remain at their current paths. Root [architecture-release.json](../../architecture-release.json)
remains the single current-release authority.

The [reviewed RAD-1.1.3 HCRL](../design/review/1.1.3/hcrl-1.1.3-001.pending.md)
and Apply instruction establish the common Praxis and required formal records
under `design/evidence/<resulting-version>/before/ai-agent-tasks/` and
`after/ai-agent-reports/`. RAD-1.1.3 is their first prospective use.
[Application iteration 002](../design/review/1.1.3/hcrl-1.1.3-002.pending.md)
preserves initial incorporation and verification facts. The Solution Architect's
[reviewed iteration 003](../design/review/1.1.3/hcrl-1.1.3-003.pending.md), DR-6/DR-7,
limits execution provenance to Tasks directing an identifiable
adjudicated/authorised change and corresponding execution Reports, requiring
authoritative issuance/production timestamps and filenames beginning with sortable
UTC timestamps from those events. Investigation, analysis and pending-HCRL
preparation/revision remain Architecture Review provenance within the HCRL
lifecycle; no formal review Task/Report convention is created.
[Application iteration 004](../design/review/1.1.3/hcrl-1.1.3-004.pending.md)
records the corrections and then-unresolved original times/prefixes. Its updated
Solution Architect table DR-9 expressly clarifies DR-7: attributable system
timestamps of operative instruction entry into the agent interaction and completed
Report emission are the governed issuance/production events. Subsequent amendments
preserve the original Report production time. [Application iteration 005](../design/review/1.1.3/hcrl-1.1.3-005.pending.md)
records recovery and filename mappings. Mark Hunter, Solution Architect,
confirmed the incorporated result on 2026-10-21: “result confirmed. Complete!”
The [completed HCRL iteration 005](../design/register/1.1.3/hcrl-1.1.3-005.complete.md)
records the accepted RAD-1.1.2 → RAD-1.1.3 transition. Completion consumes no
additional release; the pending review inputs remain preserved.

Governance machinery, rules and context belong here. They do not acquire
ArchiMate classification or require continuum entries merely through durability.
The [existing governance/specialised-contract index](../../docs/markdown/governance/README.md)
remains available. Existing files, architecture, models and publications have not
been migrated; migration requires its own bounded mapping and authorisation.
