<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Current EA operational independence | Accepted |Implementation satisfies the authorised current-EA operational-independence change. Current EA operations are self-contained under enterprise-architecture/; Legacy-RAD is retained solely for historical/provenance purposes. Preservation and isolation evidence is sufficient for this change. Actual Archi execution/visual inspection is not required for acceptance of this tooling relocation and remains subject to normal future Archi round-trip activity. |
| [DR-2](#dr-2) | Bounded Minutiae and unresolved temporal provenance | Accepted | The Task issuance timestamp was unavailable to the executing agent and must not be reconstructed or inferred. The retained Task content, originating Git state, report-production timestamp and implementation evidence provide sufficient provenance for this change. No further remediation is required. |

# HCRL — current EA operational independence

Review basis: actual root `1.1.5` / `previous_version=1.1.4`, Git
`7fda4410a9d3583d674d9530710b86b4ec748c2a`; prospective tooling/governance
patch `1.1.6` / `previous_version=1.1.5`, iteration `001`. The operative
[issued Task](../../evidence/1.1.6/001/before/ai-agent-tasks/unresolved-ea-operational-independence.md)
authorises the bounded implementation. Existing RAD-1.1.5 holistic review is
still pending; this record establishes neither that review's acceptance nor a
completed transition. No separate earlier HCRL adjudication is invented.

<a id="dr-1"></a>
## DR-1 — Current EA operational independence

The task identifies improper current imports/configuration/schema/build and
governance dependencies on Legacy-RAD and explicitly authorises their removal.
The implementation promotes current entry points, their required shared
semantics, offline schemas, configuration, tests and maintained governance into
EA. Historical adoption, identity-registry machinery, RAD capture replay,
historical publication slices and source-preservation replay stay historical.
No `architecture_identity.py` successor is introduced.

Relevant axioms: AX-04 preserves semantics while reusing suitable current
machinery; AX-08/09 bound retained evidence and transient working material;
AX-14 preserves canonical/representation identity, standing, semantic/presentation
and validation/acceptance distinctions; AX-15/17 preserve uncertainty and require
actual authority. The approach retains canonical bytes, source-qualified standing,
shared algorithms and authored Views. Maintained governance successors carry
the existing rules, precedence and deferrals; no missing architecture is inferred.

Application: current tooling, schemas, build/tests, operational configuration and
required governance were promoted under EA. Required functions were extracted
without importing RAD adoption, identity-registry or historical publisher
subsystems. Legacy edits are additions-only standing notices and bounded
supersession wrappers; original legacy lines and historical machinery remain.
Canonical element files and authored Views are unchanged. One patch was prepared
at the root; current release is 1.1.6 with previous_version 1.1.5.

Validation: the documented current Makefile all target passed with 43 tests and
generation/staleness/schema/reference/naming/containment/link checks. Independent
generation, review, comparison and 42 tests passed with docs/, scripts/, Git and
historical configuration absent; six artifacts matched byte for byte and
canonical source hashes were unchanged. The final focused helper/link suite
passed eight tests after the last small edits. The offline Maven architecture
suite passed 90 tests in 11 classes, with no failures/errors/skips. Original and
promoted 1.1.5 projection bytes match; resulting 1.1.6 XML changes only the one
model release value. All five schemas match their originals. Browser DOM checks,
315 current governance link targets and git diff --check passed.

The implementation Report is retained as
[formal iteration evidence](../../evidence/1.1.6/001/after/ai-agent-reports/20261022T075014Z-ea-operational-independence.md) and at
[the requested report location](../../../../.junie/reports/2026-10-22-ea-operational-independence.md).
Selected verification evidence is
[operational independence](../../evidence/1.1.6/001/after/operational-independence.json),
[preservation](../../evidence/1.1.6/001/after/promotion-preservation.json),
[remaining historical references](../../evidence/1.1.6/001/after/legacy-reference-inventory.json),
[current build](../../evidence/1.1.6/001/after/current-build.json),
[links](../../evidence/1.1.6/001/after/current-links.json) and
[architecture tests](../../evidence/1.1.6/001/after/architecture-tests.json).

Limits: initial short-suite TIMEOUT and initial link/cache failures are disclosed
in the Report alongside the passing corrected checks. Actual Archi execution and
interactive visual inspection were not performed; synthetic canonical-source
review/application and raw comparison prove machinery only. Existing unresolved
source/naming properties retain their standing. Result confirmation and
architectural closure remain pending Solution Architect review.

<a id="dr-2"></a>
## DR-2 — Bounded Minutiae and provenance

The bounded supporting group comprises path/package/build changes, offline
schema promotion, current test relocation and regression evidence, mechanical
reference succession, historical-standing notices/supersession wrappers,
one release patch, and formal Task/Report correspondence. These implement the
operative task without new Harmonia architecture or governance decisions.

The Task's authoritative instruction-entry time and required filename prefix
are unavailable. They remain explicitly unresolved for Solution Architect
treatment; the clock is not substituted. Report emission has its own observable
event. Directory placement and automated checks supply no acceptance.

```text
Minutiae-Accepted: [awaiting Solution Architect entry: Yes | No]
Comment: [awaiting Solution Architect / ARB comment]
```
