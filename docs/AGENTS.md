```{=html}
<!--
  Copyright (c) 2026 Mark Hunter

  This program is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program. If not, see <https://www.gnu.org/licenses/>.
-->
```
# Documentation Agent Instructions

All work under docs/ inherits the repository [AGENTS.md](../AGENTS.md) in full.
That root file is the single maintained instruction set for architectural authority,
fresh-context and progressive authority loading, bounded command execution,
architectural guardrails, documentation governance and required verification.
Read and apply it before work, including the standing
[RADS and legacy-doco rules](../AGENTS.md#12-rads-and-legacy-documentation-governance)
for reconciliation, original-content preservation and section supersession.

For all architecture-related work, load and apply the same
[Architecture Change Provenance contract](../enterprise-architecture/governance/architecture-change-provenance.md)
required by root instructions for Codex, Junie and other agents. This is a
navigation reference to the normative contract, not a separate provenance rule.

Apply the common [Architecture Review and HCRL governance](../enterprise-architecture/governance/architecture-governance.md).
Load and follow the common
[Architecture Review Praxis](../enterprise-architecture/governance/architecture-review-praxis.md)
for every change, with review activity/evidence proportionate to scope,
complexity, uncertainty and materiality identified through review. Classification
as implementation or similar does not itself reduce or bypass the Praxis.
For substantive AI-agent activity associated with an HCRL iteration, formal Tasks
and corresponding Reports MUST be retained at
`enterprise-architecture/design/evidence/<RAD-version>/<HCRL-iteration>/before/ai-agent-tasks/`
and that iteration's `after/ai-agent-reports/`, under the common provenance
contract, irrespective of architectural materiality. Investigation, analysis,
review, candidate/pending-HCRL preparation or revision, corrective work, application
and verification use the same model; the iteration is the provenance boundary.
Material meaning and conclusions belong in the applicable HCRL DR with sufficient
traceability references. HCRL is the semantic change/decision record, not the
Architecture Review working document, an evidence-file manifest or a substitute
for detailed supporting evidence. Durable mappings, inventories and checks are
evidence when they materially support the activity. Transient working material
is discardable when its purpose ends and is not retained merely because it existed;
preserve active recovery, validation and unresolved dependencies. Apply the provenance
contract's authoritative issuance/production timestamps, mandatory sortable UTC
filename prefixes and unresolved-original-time treatment. The attributable system
timestamps of operative instruction entry into the agent interaction and completed
Report emission are the governed Task-Issued-At and Report-Produced-At events.
Preserve subsequent Report amendments without replacing original production time;
these events assert no earlier composition or internal model-generation time.
RAD-1.1.3 first required execution-only records at release level; 1.1.4-003
demonstrated/refactored the iteration structure, and 1.1.4-004 implements it
prospectively, superseding the narrower rule. No retrospective compliance,
historical rewrite, migration or unrelated disposal authority is supplied.

Every change receives review; material implementation discoveries require
adjudication. The accepted target structure does not authorise repository
migration. Existing documentation remains at its current paths until bounded
mapping and application are separately authorised.

For architecture-completion tasks, also follow the
[Architecture Completion Plan](markdown/architecture-completion-plan.md),
including its context-loading guidance and completion gates. This scoped file
adds no architectural rules or exceptions and does not duplicate the root policy.
