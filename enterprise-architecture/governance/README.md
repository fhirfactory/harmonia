<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture governance

Current element authoring follows [Continuum Representation](continuum-representation.md) and the [Incorporation Praxis](continuum-incorporation-praxis.md). The [individual Architectural Axioms](architectural-axioms.md) retain highest authority. [RAD-1.1.5 HCRL 002](../design/review/1.1.5/hcrl-1.1.5-002.pending.md) presents the incorporated result for holistic review; the historical adoption accounts below retain their original scope.

[Architecture Governance](architecture-governance.md) defines the common
Architecture Review, Solution Architect authority, HCRL Decision Review Log,
repository structure, governance/architecture distinction and prospective
adoption. [Architecture Change Provenance](architecture-change-provenance.md)
defines required transition evidence and faithful recovery. The
[Architecture Review Praxis](architecture-review-praxis.md) gives the common
operational workflow for every Harmonia change, including required formal
iteration-scoped substantive AI-agent Task/Report evidence. The
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
and Apply instruction established the common Praxis and required formal records
under `design/evidence/<resulting-version>/before/ai-agent-tasks/` and
`after/ai-agent-reports/`. RAD-1.1.3 is their first prospective use.
[Application iteration 002](../design/review/1.1.3/hcrl-1.1.3-002.pending.md)
preserves initial incorporation and verification facts. The Solution Architect's
[reviewed iteration 003](../design/review/1.1.3/hcrl-1.1.3-003.pending.md), DR-6/DR-7,
then limited execution provenance to Tasks directing an identifiable
adjudicated/authorised change and corresponding execution Reports, requiring
authoritative issuance/production timestamps and filenames beginning with sortable
UTC timestamps from those events. Investigation, analysis and pending-HCRL
preparation/revision remained Architecture Review provenance within the HCRL
lifecycle; no formal review Task/Report convention was created at that time.
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

[HCRL 1.1.4 iteration 004](../design/review/1.1.4/hcrl-1.1.4-004.pending.md)
implements the model demonstrated by [003](../design/review/1.1.4/hcrl-1.1.4-003.pending.md)
prospectively. It supersedes that execution-only/release-level rule with one
boundary: `design/evidence/<RAD-version>/<HCRL-iteration>/before/` and `after/`.
Substantive AI-agent investigation, review, correction, application and verification
retain formal Tasks in `before/ai-agent-tasks/` and Reports in
`after/ai-agent-reports/`, with the existing event timestamps and UTC prefixes.
The [provenance definitions](architecture-change-provenance.md#working-material-evidence-and-hcrl)
distinguish transient working material, durable evidence and the HCRL semantic
record. Material mappings, reference inventories and checks can be evidence;
every intermediate artefact need not be retained. HCRL provides decisions and
traceability, not an evidence manifest or review workspace. Earlier records stay
unchanged; substantive repository migration/adjudication remains 005 work.
Mark Hunter, Solution Architect, subsequently confirmed the bounded 004 result
on 2026-10-21: “result confirmed, complete!” The [completed HCRL 004](../design/register/1.1.4/hcrl-1.1.4-004.complete.md)
records that confirmation with its attributable event and explicit migration
deferrals. Completion consumes no additional release; the substantive migration
remains reserved for 005.

The read-only conventional Markdown evidence check is:

```bash
python3 scripts/architecture_review_evidence.py --version 1.1.4 --iteration 004
```

It checks only the selected iteration's layout and recorded timestamp/prefix
correspondence, returning UNRESOLVED for unavailable original times. It does not
prove attributable events, semantic completeness, substantive activity coverage
or architectural acceptance. Run the complete relevant suites from the repository root:

```bash
timeout --signal=TERM --kill-after=5s 420s python3 -B -m unittest discover -s scripts/tests -v
timeout --signal=TERM --kill-after=5s 420s python3 -B -m unittest discover -s docs/archimate/framework/tests -v
timeout --signal=TERM --kill-after=5s 240s mvn test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

Governance machinery, rules and context belong here. They do not acquire
ArchiMate classification or require continuum entries merely through durability.
The [existing governance/specialised-contract index](../../docs/markdown/governance/README.md)
remains available. Existing files, architecture, models and publications have not
been migrated; migration requires its own bounded mapping and authorisation.
