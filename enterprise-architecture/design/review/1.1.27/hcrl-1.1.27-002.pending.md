<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Retire the exploratory ArchUnit requirement | Amendment preparation authorised. Exact amendment pending review. | Preserve architectural invariants and prospective testing expectations. |
| [DR-2](#dr-2) | Release and verification treatment | Pending review | Target the next Architecture patch, independently of the software candidate. |
| [DR-3](#dr-3) | Bounded Minutiae | Not recorded | Review provenance and exact patch applicability. |

# HCRL 1.1.27 / 002 — ArchUnit verification requirement retirement

**Status** · PENDING SOLUTION ARCHITECT REVIEW.  
**Origin** · Accepted Architecture 1.1.26 at Git
`718fc278be5b89b6138d4341feddefce1269eaa6`.  
**Target** · Next proposed Architecture patch, 1.1.27, distinct from the
[cancelled non-release iteration 001][cancelled].  
**Authority** · [Amendment Task][task] and [software reset review][software].
The assigned action is preparation for Architecture review.

<a id="dr-1"></a>
## DR-1 — Retire the exploratory ArchUnit requirement

Approve the [exact five-file amendment][patch] for later application. It
removes the mandatory exploratory suite, the linked enforcement claims and
its current executable invocation in the agent and governance guides. It
preserves the invariants and the prospective Mneme/Mnemosyne and Pylai test
guidance. The common Architecture Review and Software Development Praxes
continue to govern applicable verification. The build and operations
documents receive historical notices around the old commands, with their
original text preserved. Structural tests enter in later authorised backlog
items.

AX-05 and AX-13 retain the boundaries formerly exercised by these tests.
AX-08 supports retention of meaningful review evidence. AX-17 requires
explicit authority for the normative change. The amendment has no
Continuum element or relationship edits. `git apply --check` passed against
the current policy and documentation files. [Preparation Report][report]

<a id="dr-2"></a>
## DR-2 — Release and verification treatment

Use 1.1.27 as the proposed resulting Architecture release under the
[Architecture Release Policy][release-policy]. The existing accepted release
remains 1.1.26. The pending proposal and the software reset allocate no accepted
Architecture release and involve no publication. Apply the normative amendment
and the single patch increment through the Architecture release process after
review and attributable authority. Verify the exact result against the
invariants and document any applicable release or publication conditions.

The linked [software reset][software] records the candidate-specific
verification scope and the pre-existing missing retained-input failure in
canonical validation. The validator observed the same missing path as the
earlier F-01 review. Establish treatment of this prerequisite before dependent
Architecture verification or closure.

<a id="dr-3"></a>
## DR-3 — Bounded Minutiae

The cancelled 001 and its evidence are preserved. The Tool captured the
Report production event. The authoritative instruction-entry event remains
unresolved.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```

[cancelled]: hcrl-1.1.27-001.pending.md
[task]: ../../evidence/1.1.27/002/before/ai-agent-tasks/unresolved-archunit-retirement-amendment.md
[report]: ../../evidence/1.1.27/002/after/ai-agent-reports/20261124T045809Z-archunit-retirement-amendment.md
[patch]: ../../evidence/1.1.27/002/after/proposed-archunit-retirement.patch
[software]: ../software-0.1.0/hcrl-software-0.1.0-009.pending.md
[release-policy]: ../../../governance/architecture-release-policy.md
