<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

| Decision Review | Subject | Solution Architect Disposition | Comment |
| --- | --- | --- | --- |
| [DR-1](#dr-1) | Current Capability navigation and preserved history | Accepted | The remediated current Capability navigation and treatment of preserved historical Capability identities are accepted. The reader-facing Strategy hierarchy has been reviewed and is structurally acceptable. Historical/superseded identities remain appropriately available through reference/history navigation without obscuring the current Capability taxonomy. |
| [DR-2](#dr-2) | Complete Business Behaviour capability context and canonical references | Accepted | The remediated Business Behaviour contextual hierarchy and canonical Strategy references are accepted. The generated reader-facing result has been inspected and the CT1 → CT2 → CT3 contextual structure, associated behaviour and canonical Capability references are structurally acceptable. Some naming is not preferred, but no further naming change is requested or required for 1.1.11 acceptance. |
| [DR-3](#dr-3) | Contextual Stakeholder associations and separate Stakeholder Index | Accepted | The contextual Stakeholder presentation and separate R.1 Stakeholder Index are accepted. The generated Motivation and cross-cutting reference navigation, including representative contextual Stakeholder associations, has been inspected and is structurally acceptable. |
| [DR-4](#dr-4) | Reported duplicate I.6 | Accepted as recorded | The previously reported duplicate `I.6` remains not reproduced. No corrective action is required and no numbering change is authorised for that unconfirmed observation. |
| [DR-5](#dr-5) | Solution Architect reader-facing structural review checkpoint | Accepted and satisfied | The required Solution Architect reader-facing structural review has now been performed. The generated result is accepted as structurally fit for 1.1.11. Some Business Behaviour and Application naming is not preferred, but no further change is requested and those observations are not acceptance blockers. This review satisfies the newly established bounded human structural-review checkpoint. Do not create additional remediation activity from the non-blocking naming observations. |
| [DR-6](#dr-6) | Bounded Minutiae and temporal provenance | Accepted | The bounded supporting Minutiae and formal provenance are accepted. The Task instruction-entry timestamp and sortable Task filename prefix were unavailable to the executing agent and were correctly retained as unresolved rather than inferred or substituted. The available governed provenance, implementation evidence, mechanical verification, reader-inspection evidence and subsequent Solution Architect structural review are sufficient for this transition. No further remediation is required. |

# HCRL — RAD-1.1.10 → RAD-1.1.11 navigation remediation

**Standing: completed and closed iteration 002**, the authoritative semantic
record for **1.1.10 → 1.1.11**. Mark Hunter, Solution Architect, has reviewed
and accepted the generated 1.1.11 reader-facing architectural result, accepted
DR-1–DR-6 as recorded here, set `Minutiae-Accepted: Yes`, and expressly instructed
completion and closure. The full attributable acceptance instruction is retained
in the [formal closure Task][closure-task]. Codex records that human decision;
acceptance is not inferred from agent inspection or successful automated checks.

The [pending review input](../../review/1.1.11/hcrl-1.1.11-002.pending.md) remains
byte-for-byte unchanged as historical review provenance; its pending wording
records the pre-acceptance state. Its SHA-256 is
`e69abba3511715f5bd9b06fc49e87b75d57c6451acef7df8a9710d3cec298174`.
The dispositions above supersede its then-pending result confirmation and human
review requirements. Original unavailable Task event times/prefixes remain
unavailable, with the accepted treatment in DR-6; no event time is manufactured.

Closure records decisions and provenance only. Architecture, navigation, naming,
tooling, governance guidance and publications are unchanged. Non-blocking naming
observations authorise no additional remediation. Completion belongs to the same
accepted change set and consumes no additional release: root
`architecture-release.json` remains **1.1.11**, previous **1.1.10**.
The [closure preservation check](../../evidence/1.1.11/002/after/closure-check.json)
records the bounded content-preservation verification. This completed register
record is immutable under common governance.

**Review/application history:** Mark Hunter, Solution Architect, issued the [operative corrective Task][task]
following the accepted [diagnostic Report][diagnostic] in iteration 001.
Its complete wording is retained, including all five dispositions, constraints,
required verification, reader inspection and the explicit prohibition on completing
HCRL before Solution Architect review of the generated result. There was no
repository-held 1.1.11 HCRL at task entry; the issued dispositions supply the
bounded application authority recorded here. No missing earlier HCRL is invented.
Accepted 1.1.10 history and diagnostic iteration 001 remain unchanged.

Relevant canonical axioms: AX-04 keeps machinery subordinate to architecture;
AX-08 retains meaningful transition evidence; AX-14 distinguishes identity,
standing, historical correspondence and contextual occurrence; AX-15 preserves
uncertain outcomes; AX-17 prohibits filling architecture gaps through inference.
The implementation changes navigation/publication only. Canonical element
Markdown, native types, standing, semantic relationships and authored Views must
remain unchanged. The common Architecture Governance, Review Praxis and Change
Provenance contracts control this transition.

<a id="dr-1"></a>
## DR-1 — Current Capability navigation and preserved history

**Solution Architect disposition: Accepted.** The reviewed Strategy hierarchy
and preserved historical/reference treatment are structurally acceptable.

The accepted diagnostic found 212 same-name historical/successor groups and one
distinct BC/BEC same-name case. The issued disposition requires the governed
successor to occupy primary Strategy navigation, preserving historical identities
and correspondence. Identity is never inferred from label equality.

Application relocates all 214 explicitly governed reserved predecessor identities
to R.2 Historical Capability Identities, including the two differently named
correspondences. Current subjects link to their historical predecessors; historical
entries link to their governed successors with actual disposition and standing.
The BC Health Information Exchange remains under Business Capabilities. The
historical BEC Health Information Exchange refers to Health Information Exchange
Enablement under Business Enabling Capabilities. These concepts are not merged.
The 32 other superseded source Capability records have no such correspondence;
their recorded hierarchy, including 24 qualified pending descendants, is preserved
and visibly qualified. No reallocation or residual adjudication is supplied.

Validation establishes that all 214 correspondence pairs remain linked in both
directions, including 212 same-name pairs (209 direct and three evolved) and the
two differently named evolved correspondences. No canonical element Markdown
changed. All 1,602 IDs, 1,651 semantic relationships and 1,209 authored Views are
preserved. The BC/BEC exception is independently checked by ID and governed scope.

<a id="dr-2"></a>
## DR-2 — Business Behaviour contextual hierarchy and canonical references

**Solution Architect disposition: Accepted.** The generated contextual
hierarchy, associated behaviour and canonical Capability references have been
inspected and are structurally acceptable. No further naming change is requested
or required for 1.1.11 acceptance.

The issued disposition requires the complete relevant recorded ancestry under
Business Behaviour, as contextual references to Strategy. Application retains
the 72 existing populated contexts and adds 17 required ancestors, yielding 89
capability contextual occurrences. CT3 and Feature paths include CT1 → CT2 → CT3;
direct CT2 behaviour remains at CT2. The 323 represented behaviours retain their
original contexts: 142 Services, 160 Functions and 21 Processes. No BusinessEvents
are represented and none are invented. The 69 unallocated BusinessInteractions
remain Unclassified Business with unchanged uncertainty.

Canonical metadata identifies each contextual occurrence by its reference role
and existing `contextCanonicalId`. Derived projections expose the contextual
coordinate, canonical Strategy coordinate, identity, link and standing. Exchange
navigation item documentation carries these references without semantic edges;
derived element navigation properties preserve full context paths through Archi's
non-element-folder limitations. No authored View is reorganised for navigation.

The [bounded verification](../../evidence/1.1.11/002/after/verification.json)
confirms all 89 contextual paths exactly follow recorded Strategy ancestry and
all 72 original behaviour allocations are preserved. The generated Manifest,
browser, RAD and Exchange use the same 1,968-node current traversal. Actual Archi
transport preserves all 89 Capability-context folder labels and every native
element's explicit navigation metadata. The four leaf Stakeholder contextual
items do not survive as organisation labels; they remain explicit in transported
properties and their existing semantic Associations. This is a recorded native
representation limit, not authority to add navigation relationships or rewrite
the canonical source from Archi's return.

<a id="dr-3"></a>
## DR-3 — Stakeholder context and non-decompositional index

**Solution Architect disposition: Accepted.** The contextual Stakeholder
presentation and separate R.1 Stakeholder Index, including representative
associations, have been inspected and are structurally acceptable.

Motivation retains its eight architectural subject groups. The ten canonical
Stakeholders move to R.1 Stakeholder Index under the separate R Cross-Cutting
References area, outside Parts I–VI architectural decomposition. R is reference
navigation, not a new architectural domain, native type or Motivation concept.
Index entries remain qualified and linked to canonical sources.

Four contextual occurrences cite only the actual existing canonical Association
IDs: Care Team and Health Network against Clinical Interoperability; Healthcare
Provider against 24/7 Availability; Patient against Audit & PHI Privacy. The RAD
exposes these links despite their historical standing. The six Stakeholders with
no represented association remain discoverable and explicitly unassociated in
the index; no inferred association is introduced.

All four existing native associations are visible beneath the actually associated
Motivation material in the generated RAD and are checked against canonical edge
IDs. All ten Stakeholders remain in the index. No additional association exists
in the canonical source or Exchange; source prose about stakeholders/concerns
retains its source meaning and does not establish another native edge.

<a id="dr-4"></a>
## DR-4 — Reported duplicate I.6 not reproduced

**Solution Architect disposition: Accepted as recorded.** The observation
remains not reproduced; no corrective action or numbering change is authorised
for it.

The diagnostic could not reproduce the reported additional I.6 in either retained
PDF or inspected source projections. No remediation or speculative numbering
change is applied. The existing one-based shared traversal remains the coordinate
rule. Authorised relocation and added contextual/reference nodes legitimately
change current coordinates; historical coordinates retain their release context.
Any independently established collision would require a new finding.

There are 1,968 unique current coordinates and distinct PDF navigation destinations.
The authorised remap changes 1,066 canonical element coordinates and 547 name-paths;
the [identity-keyed correspondence](../../evidence/1.1.11/002/after/navigation-correspondence.json)
retains exact before/after locations. No numbering algorithm repair was made.

<a id="dr-5"></a>
## DR-5 — Human structural review before publication acceptance

**Solution Architect disposition: Accepted and satisfied.**

Application adds the issued safeguard to the existing common Review Praxis.
Where navigation/publication structure materially changes, the Solution Architect
must structurally review representative generated portions before HCRL acceptance:
Motivation, Strategy, Business Behaviour, Application, references, history and deep
numbering as applicable. Automation and agent inspection assist but do not replace
this human review. No new Praxis, subsystem, exhaustive page review or automated
semantic acceptance mechanism is created.

The required human checkpoint has now been performed by Mark Hunter, Solution
Architect. His review included:

- Motivation and R.1 Stakeholder Index;
- contextual Stakeholder associations;
- Strategy Business Capabilities;
- Strategy Business Enabling Capabilities;
- R.2 Historical Capability Identities;
- Business Behaviour contextual navigation;
- Application navigation.

The generated result is accepted as structurally fit for 1.1.11. This satisfies
the newly established bounded human structural-review checkpoint. Some Business
Behaviour and Application naming is not preferred, but no further change is
requested and those observations are not acceptance blockers. They create no
additional remediation activity. The full review/acceptance wording is retained
in the [closure Task][closure-task].

The [reader inspection pack](../../evidence/1.1.11/002/after/reader-inspection/README.md)
retains 14 representative rendered pages, selected contextual navigation and the
full PDF outline inventory. It includes Motivation/Stakeholders, both capability
scopes, history and the BC/BEC exception, complete CT1 → CT2 → CT3 context,
behaviour, Application and an eight-component deep coordinate. Actual contextual
PDF links resolve to their canonical Strategy destinations. The 550-page PDF's
clean fixed-input build reproduces identical bytes; agent inspection is not
the required human architectural review.

<a id="dr-6"></a>
## DR-6 — Bounded supporting evidence and temporal provenance

**Solution Architect disposition: Accepted.** The bounded Minutiae and formal
provenance are sufficient for this transition; no further remediation is required.

The disclosed supporting group comprises formal Task/Report correspondence,
purpose-selected before/after inventories, bounded regression tests, deterministic
derived publication metadata, preservation/reproducibility checks, actual Archi
transport evidence and representative reader-facing inspection. It authorises no
cleanup, retrospective rewriting or historical migration.

The Task instruction-entry timestamp was not exposed in this interaction. Its
`Task-Issued-At` and required sortable filename prefix remain explicitly unresolved
as facts; the Solution Architect has now accepted that treatment for this
transition and confirmed that it does not require further remediation. Current clock/capture time is
not substituted. Diagnostic iteration 001 retains its own unresolved time unchanged.
The execution Report must retain its actual observable production timestamp.

The maintained EA suite passes all 60 tests in the isolated current-input tree;
the final creation-placement compatibility refinement passes all nine navigation
regression tests. Java 21 architecture verification passes 90 tests with no
failures, errors or skips. All 19 derived source/metadata artifacts reproduce
without Legacy-RAD, historical processing, `docs/`, `scripts/` or Git metadata;
current staleness, link and browser DOM checks pass. Actual Archi no-edit review
reports zero canonical element, relationship or View changes. The
[test results](../../evidence/1.1.11/002/after/test-results.json),
[reproducibility](../../evidence/1.1.11/002/after/reproducibility.json),
[PDF check](../../evidence/1.1.11/002/after/pdf-reproducibility.json) and
[actual transport](../../evidence/1.1.11/002/after/archi-transport.json) retain
bounded outcomes and representation/environment limits, including earlier
interrupted or unsuccessful attempts and their resolutions. Successful
verification does not recover the unavailable Task event time. Acceptance is
supplied by the subsequent Solution Architect instruction, not by those checks.
The closure follow-up likewise preserves its unavailable instruction-entry event
and prefix without substitution or a claim of full temporal conformance; the
same provenance limitation is disclosed in its formal Task.

```text
Minutiae-Accepted: Yes
Comment: Mark Hunter, Solution Architect, accepts the bounded supporting Minutiae and formal provenance, including the explicitly unavailable Task event/prefix. The governed evidence and subsequent human structural review are sufficient for this transition; no further remediation is required.
```

[task]: ../../evidence/1.1.11/002/before/ai-agent-tasks/unresolved-continuum-navigation-remediation.md
[closure-task]: ../../evidence/1.1.11/002/before/ai-agent-tasks/unresolved-1.1.11-hcrl-closure.md
[diagnostic]: ../../evidence/1.1.11/001/after/ai-agent-reports/20261023T003913Z-1.1.10-navigation-diagnostic.md

The [completed execution Report](../../evidence/1.1.11/002/after/ai-agent-reports/20261023T021431Z-continuum-navigation-remediation.md)
records the actual incorporation and bounded results at its original observable
production event, `2026-10-23T02:14:31.832630Z`, and is preserved unchanged. Its
then-pending acceptance wording remains historical. The subsequent Solution
Architect review, result confirmation and closure instruction are recorded above
and in the formal closure Task. **HCRL 1.1.11-002 is complete and closed.**
