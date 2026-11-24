<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Architecture Continuum 1.1.24 candidate — review guide

**PENDING:** complete source inputs approved; actual incorporation awaits Solution
Architect review. Start with the [HCRL Decision Review Log](../../../../review/1.1.24/hcrl-1.1.24-001.pending.md).
Root release remains **1.1.23**; candidate-generated release metadata therefore
displays 1.1.23. Evidence placement identifies the prospective **1.1.24/001**
candidate and does not create an accepted 1.1.23 publication or allocate 1.1.24.

## Reviewable projections

- [HTML Continuum reader](candidate/reader/continuum.html)
- [Open Exchange XML](candidate/reader/Harmonia-Architecture.xml)
- [Actual Archi imported native model](archi-transport/candidate.archimate)
- [Narrative index](candidate/narrative/README.md) and [structure](candidate/narrative/structure.json)
- [Canonical Manifest](../../../../../continuum/MANIFEST.md)
- [Source/decision correspondence](source-correspondence.md) and [actual Feature ancestry](feature-correspondence.md)
- [Formal activity Report](ai-agent-reports/20261114T045751Z-mneme-interaction-cache-add.md)

Candidate XML SHA-256:
`6f6432a979a92a3adbf0261ba4bc761d092f87eb55fd4e5d7968f597e4d64d3a`.
The native model is the exact successfully imported candidate. Its exported no-edit
return is transport evidence, not canonical architecture. PDF compilation and visual
layout acceptance are not asserted; HTML, XML and generated narrative sources are
the review projections delivered here.

## Incorporated delta

Exactly five upper contracts: Summary Reader, Summary Writer (CREATE/RETIRE only),
sole Summary Search, full Reader and full Writer. Historical Search identity
`dcb31810ae134818adeeba9b27bd2f30` is retained as GovernedSummarySearch; Reader,
Writer, Store and Façade identities and prior edges remain. Summary is IU minus
Content; full operations preserve coherent Content/reference/Summary, versions and
multi-site durable Success. All AV/FC/MI/SearchResult decisions are retained.

Six justified additions: two Summary services, two Mneme cache functions and two
selected Infinispan technology configurations. There are **51** new native edges.
No new Feature, Requirement, Business entity/activity, IU/representation, Topic,
SearchResult model or execution/recovery framework is introduced.

Standard Cache is distributed Server/Hot Rod and reference-based. Workflow Cache
is embedded LOCAL, materialised and resident in applicable Ponos/DigitalTwin/
Dokimasia JVMs. Common Ergon recovery remains. MC-01–MC-07 retain topology,
concurrency, resource, visibility and deployment qualifications; two sites minus
one cannot establish new multi-site durable write Success.

## Verification and exceptions

| Check | Result |
| --- | --- |
| Canonical/native/naming/navigation/offline XML schema/reference checks | PASS — 6,563 elements / 9,427 relationships / 462 qualified predicates |
| Source/identity/old-relationship/authored-View preservation | PASS — both DRPs, root release and accepted publications/registers unchanged |
| Independent maintained-input reproduction | PASS — 19 candidate/Manifest outputs identical |
| Exact-candidate no-edit workbench review | PASS — zero element, relationship/View or unresolved changes |
| Focused tooling tests | PASS — eight Continuum and four navigation tests |
| Required Java architecture suite | PASS — 90 tests; Java source unchanged |
| Manifest/publication staleness, source links, reader DOM smoke | PASS |
| Actual Archi import/native save/export | PASS |
| Strict transport comparison | **FAIL retained** — [CR-001](change-review.md#cr-001) ACCEPTED WITH QUALIFICATION for this exact 1.1.24 candidate only |
| Human structural review and exact-result acceptance | PENDING — Solution Architect checkpoint |

The Solution Architect's [issued CR-001 disposition](../before/cr001-solution-architect-disposition.md)
resolves the bounded transport exception for this exact candidate. Original strict
FAIL, raw comparison results and representation/presentation qualifications remain;
there is no model-application/remediation authority or subsequent-release waiver.
Historical transport-summary and initial Report statements that acceptance was
pending describe their production checkpoint; current CR/HCRL records supply the
subsequent disposition. Native-tree/HTML-reader structural review, whole-result
acceptance, unavailable Task event/prefix treatment and bounded Minutiae review remain
outstanding before closure. Formal naming qualifications and detailed technical/
deployment choices remain explicit deferrals; no new semantic design question was
needed to incorporate the approved inputs.

For representative structural review, inspect Motivation and existing CT1–CT3/
Features, unchanged Business IU/Search Result Set context, Mnemosyne's five service
participants, Mneme's two internal cache functions, the three existing executor
contexts, new Technology entries, historical Search supersession and deep reader
numbering. The imported native tree and HTML reader must receive the required
Solution Architect review before acceptance; automated checks do not replace it.

No HCRL closure, release allocation, accepted publication, commit, push or Mneme/
Mnemosyne Java implementation occurred.
