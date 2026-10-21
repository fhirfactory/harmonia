<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | `<matter requiring adjudication>` | Not recorded | — |
| [DR-2](#dr-2) | Bounded Minutiae | Not recorded | — |

# HCRL — `<transition subject>`

**TEMPLATE ONLY.** No placeholder is a decision or executable instruction.
Apply [Architecture Governance](../architecture-governance.md) and
[Architecture Change Provenance](../architecture-change-provenance.md).
Follow the common [Architecture Review Praxis](../architecture-review-praxis.md)
for every change; review effort and evidence are proportionate to scope,
complexity, uncertainty and materiality identified through review. Implementation
classification does not itself reduce or bypass the Praxis.
Replace/remove example matters and renumber actual top-level DRs consecutively.
There is one opening Decision Review table. Add a subordinate DR only where
independently adjudicable, using one level such as `DR-1.01`; a matter needing
deeper decomposition becomes another top-level DR. Keep table/section references
and attributable dispositions/comments consistent. Do not create parallel
knowledge, action, uncertainty or minutiae decision taxonomies.

**Review basis.** `<actor/date; target and iteration; originating immediate
governed release/state and source locator; proposed/actual result distinguished;
exact supplied decision/input provenance; conditions, exclusions and evidence
limits; final-confirmation standing>`.

<a id="dr-1"></a>
## DR-1 — `<matter requiring adjudication>`

Explain the matter in enough context/detail for ARB adjudication. Distinguish
established authority and observed evidence from the proposed meaning. Include
the relevant consequences, uncertainty, dependencies, implementation implications,
source correspondence and supporting provenance within this DR. Record the
Solution Architect's attributable disposition, comment and material revisions;
do not invent missing decisions or infer authority from placeholders.

When authorised application occurs, record what actually changed, required
before/after evidence or adequate source-control locators, validation conclusions
and limits, remaining explicit deferrals/exclusions, and the Solution Architect's
confirmation status. For formal AI-agent Tasks directing execution of an
identifiable adjudicated/authorised change, retain Task Details at
`enterprise-architecture/design/evidence/<resulting-version>/before/ai-agent-tasks/`
and corresponding Task Reports at `after/ai-agent-reports/`, irrespective of
architectural materiality. Reference the actual records and preserve correspondence
adequate to reconstruct the transition. These are task-input/result roles, not
claims about artefact presence in predecessor/successor Git states. Material
content and validation conclusions belong in the applicable DR; a report does
not replace it. Do not require duplicate exact-byte archives or universal
hashing. Do not claim uncommitted material is recoverable from Git history.
Successful checks do not establish architectural completion.

Execution Tasks/Reports require authoritative issuance/production ISO 8601 event
timestamps with `Z` or an explicit offset and event meaning/source, plus filenames
beginning with filesystem-safe sortable UTC timestamps derived from those same
events, followed by descriptive names. The attributable system timestamp recording
operative instruction entry into the agent interaction is Task-Issued-At; that
recording completed Report emission is Report-Produced-At. These observable events
assert no earlier human composition or internal model-generation time. Preserve
subsequent Report amendments as subsequent provenance without replacing original
production time. Separate later receipt/capture or recording does not replace the
governed instruction-entry event; production remains distinct from execution,
validation, later formalisation, review or acceptance. Recover existing times only
from established source evidence; preserve known precision and any unresolved
event times/filename prefixes for Solution Architect treatment before closure.
Do not guess from file metadata,
commit time, the current clock or inferred order.

Investigation, analysis, candidate preparation and pending-HCRL preparation/revision
are Architecture Review provenance. Preserve relevant instructions, findings and
preparation facts in the HCRL lifecycle and applicable DRs/Minutiae; do not create
formal execution records or a new formal review Task/Report convention for them.

<a id="dr-2"></a>
## DR-2 — Bounded Minutiae

Disclose the bounded supporting/minor group here, subject to ARB/SA review.
An unnumbered list may describe its members; it is not another decision hierarchy.
Material architectural meaning belongs in its applicable DR. Task Details and
Task Reports normally support transition provenance unless materially architectural.
Formal AI-agent execution Task/Report retention remains required irrespective of
that classification; disclose their supporting/minor treatment within this bounded
group without treating substantive decision content as accepted minutiae.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

This control and the table describe the same bounded group decision. Silence,
agent judgement and tool success supply no acceptance. Yes accepts only the
disclosed group; No identifies correction/escalation/reclassification. Carry
adjudication into subsequent iterations. Do not use a per-item
`ACCEPTED_WITHOUT_COMMENT` disposition.
