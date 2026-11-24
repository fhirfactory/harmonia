<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# ArchiMate / Markdown baseline synchronisation — 2026-10-17

## Final human-inspection correction — same uncommitted Release 1.0.6

Ready for Mark's final import and inspection in **Archi 5.10.0**. No commit,
release increment, semantic reconciliation or native GUI acceptance is claimed.
Import [Harmonia-Architecture-Baseline.xml](Harmonia-Architecture-Baseline.xml)
and follow the [inspection guide](archi-inspection-guide.md).

This pass started from the current accepted uncommitted candidate, SHA-256
`8dc2a377f080c7083811c0849bce66166b740f8c8321bb009eb3a8da3d78eb0b`. The
[previous completion report](pre-human-inspection-synchronisation-report.md),
[previous verification](pre-human-inspection-verification.json), and earlier
Documentation and projection preservation proofs remain historical stage evidence.
Their 1,012-View preservation statements describe that earlier stage; the View
presentation changes and new landscapes below are explicitly authorised here.
No earlier baseline was used to replace the starting candidate.

## Authority and unchanged semantic architecture

The Markdown RAD remains the governed source for architecture, design, motivation,
implementation, decisions, governance, traceability and rationale. ArchiMate remains
the governed architectural projection and human workbench; Open Exchange XML is
interchange. Archi changes are inputs for reconciliation into Markdown RAD and/or
governed projection metadata. LaTeX remains downstream:
`Markdown RAD → LaTeX RAD → publication output`.

AX-04 keeps semantic architecture separate from navigation/layout machinery;
AX-14 preserves identity, type, standing and lifecycle distinctions; AX-17 forbids
invented ancestry or associations. The exact governed capture, metamodel,
reconciliation and crosswalk supply keys and assertions. Names, proximity,
implementation structure and chat supply no architectural authority.

| Object | Starting candidate | Final candidate | Result |
| --- | ---: | ---: | --- |
| Semantic elements | 1,299 | 1,299 | Exact IDs, Names, types, Documentation and Properties unchanged |
| Relationships | 852 | 852 | Exact IDs, endpoints, standing, Documentation and Properties unchanged |
| Existing Views | 1,012 | 1,012 retained | All existing View IDs retained |
| New landscape Views | 0 | 184 | Deterministic context/projection IDs |
| Total Views | 1,012 | 1,196 | No existing View removed |
| Property definitions | 55 | 55 | Exact content unchanged |
| Persistent Architecture Identities allocated | 0 | 0 | All 14 adopted bindings unchanged |
| Current governed Features | 157 | 157 | All complete CT1 → CT2 → CT3 → Feature paths retained |

The [new starting proof](human-inspection-start-preservation.json) checks exact
semantic and relationship signatures against the actual starting candidate.
Unrelated Views remain exact. 186 existing immediate-child
Views change only their presentation Name to identify their landscape purpose.
61 existing Business Views receive generic
Feature layout and association endpoint completion. Their existing node/connection
IDs, styling, View Documentation and metadata remain protected. Added visual
appearances use deterministic IDs; changed connection endpoints still reference
the same semantic relationship endpoints. The validator limits these exceptions
to declared landscape Names and Business visual presentation.

Source current/pending/superseded populations remain 845 / 69 / 32; source
Canonical/Proposed/Candidate populations remain 716 / 227 / 3. The 462 metadata-only
relationship assertions, deferred relationships, 214 unproven historical 1.0.4
crosswalks and all unresolved identity states remain unchanged.

Documentation remains **396 entries / 675 current explanatory fields**, with
**212 fields across 98 entries historical**. Every semantic Documentation entry
is identical to the starting candidate. Current explanation stays before
`---` / `## History`; uncertain historical explanation stays below it. Structured
metadata remains Properties. The classification manifest and authority/publication
lifecycle are unchanged.

## Generator and projection changes

`feature_projection.py` supplies one exact assertion index shared by Business
resource navigation, Feature-specific View navigation and diagram layout.
`realises_feature`, `associated_feature` and `contributes_feature` endpoints are
used only when current and not unresolved, with current endpoint records.
Recorded Proposed relationships remain Proposed; their display is not acceptance.
The explicit Function `governed_process` path can place that Process in the
Function's Feature context. This is navigation through existing assertions,
**not** an invented direct Process→Feature semantic relationship. Participation,
Service use, common ownership and lexical similarity are not traversed.

**137 Features** have recorded Business
behaviour associations. Unique associations group **118
Functions, 8 Processes and
101 Services** beneath Feature resource contexts.
All **153 existing Feature-specific
realisation Views** move beneath their exact Feature subjects; standing breakdown:
`{'current / Proposed': 17, 'current / established': 118, 'preserved historical assertion / Proposed': 11, 'preserved historical assertion / established': 7}`. Historical subjects use qualified
historical folders. CT3/Capability-wide Behaviour Views retain their Capability
contexts. There is no CT3-level generic Feature Realisations bucket.

Business diagrams use a Capability context above Feature columns, with associated
behaviour beneath each Feature. Shared behaviour has multiple visual appearances
of its single semantic ID. A separate lower band holds unassociated behaviour.
Existing page populations are retained and completed with missing associated
endpoints so paging cannot hide an association. Native edges are drawn only from
existing governed native mappings; no Function→Process→Service chain is implied.
Coordinates, columns, absence of rectangle overlap and semantic connection
endpoints are mechanically checked. Crossing minimisation follows same-column
realisation routing; perceived readability and native rendering require Mark's
inspection. Person Identity Administration is the representative regression,
not a coordinate special case.

## Landscape coverage and ordering

There are **370 landscape Views**: **186
reused** and **184 new**. The child-level totals are
**5 CT1 landscapes**, **51 CT2 landscapes**,
**182 CT3 landscapes** and **132 Feature landscapes**.
These names describe the child population: a CT2 landscape belongs to CT1;
a CT3 landscape belongs to CT2. Counts include independent Strategy and Business
projections where applicable; Enterprise has Strategy landscapes only.

| Taxonomy | Root → CT1 | CT1 → CT2 | CT2 → CT3 | CT3 → Feature |
| --- | ---: | ---: | ---: | ---: |
| Business Capabilities | 2 | 34 | 76 | 0 |
| Business Enabling Capabilities | 2 | 14 | 102 | 132 |
| Enterprise Capabilities | 1 | 3 | 4 | 0 |

BC/BEC rows each include both Strategy and Business Views. Source context counts
are half those row counts. Enterprise is the already represented independent
catalogue, with no Application architecture expansion. BC contains **zero Features**;
there is no BC Feature landscape fixture to manufacture. Natural terminal
contexts retain their existing anchor/context Views. Each landscape contains only
the context (where there is a semantic context) and its **immediate** governed
children. Root landscapes contain CT1 only. No generic Landscapes folder exists.
A landscape is first among serialized View references in its context. Its View
display Name has a local `0-` prefix: installed Archi 5.10.0 comparator bytecode
confirms that tree Views sort by display Name independently of XML item order.
Validation also checks name ordering. This View-only prefix does not change
semantic element Names or resource numbering. Native GUI rendering remains a
human check. See the recorded comparator inspection log.

View-tree numbering remains `0-Motivation`, `1-Strategy`, `2-BusinessLayer`,
`3-ApplicationLayer`, `4-TechnologyLayer`, `5-Implementation`. Native resource roots
remain unnumbered, with human-readable navigation labels. Redundant resource
constructs such as `Strategy / 1-Strategy`, `Motivation / 0-Motivation` and
`Business / 2-BusinessLayer` remain absent. Semantic Names remain unprefixed.

## Explicit exceptions and uncertainty

The governed single canonical resource-home convention cannot assign the following
**13 shared elements** to every
Feature without multiple resource homes or an arbitrary preferred association.
Their affected resource relocation is deliberately stopped: existing Capability
homes remain. No relationship is discarded. Their multiple Feature contexts are
exposed by realisation Views where native mappings exist and by repeated visual
appearances in Business diagrams. This is a navigation constraint, not a claim
that a semantic association is unresolved. No preferred Feature was inferred.

- `service:practitioner-directory-query` → `feature:practitioner-profile-governance`, `proposal:feature-provider-directory-discovery-resolution`
- `function:manage-ward-capacity` → `feature:ward-patient-flow-coordination`, `proposal:feature-ward-care-capacity-management`
- `function:manage-theatre-case-progression` → `feature:operating-theatre-case-progression`, `feature:perioperative-resource-notification`
- `function:coordinate-ed-departmental-capacity` → `feature:ed-bed--bay-tracking`, `proposal:feature-emergency-treatment-capacity-management`
- `function:manage-outpatient-capacity` → `feature:outpatient-arrival-registration`, `proposal:feature-outpatient-appointment-capacity-management`
- `function:manage-bed-turnover-progression` → `feature:bed-readiness-notification`, `feature:bed-state-progression`
- `function:dispatch-operational-work-order` → `feature:work-dispatch-delivery`, `feature:work-item-instantiation`, `feature:worker-matching--allocation`
- `function:manage-patient-transport-progression` → `feature:transport-dispatch--progress-tracking`, `feature:transport-request-ingestion`
- `function:manage-discharge-coordination-progression` → `feature:discharge-milestone-tracking`, `feature:discharge-notification-dispatch`, `feature:discharge-readiness--coordination-oversight`
- `process:theatre-case-progression-process` → `feature:operating-theatre-case-progression`, `feature:perioperative-resource-notification`
- `process:bed-turnover-process` → `feature:bed-readiness-notification`, `feature:bed-state-progression`
- `process:patient-transport-process` → `feature:transport-dispatch--progress-tracking`, `feature:transport-request-ingestion`
- `process:discharge-progression-process` → `feature:discharge-milestone-tracking`, `feature:discharge-notification-dispatch`, `feature:discharge-readiness--coordination-oversight`

The following **42 current Business
elements** have no usable recorded Feature association and retain the finest
established Capability resource context (which can be broader than CT3):

- `service:order-outcome-notification` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Order Administration / Services`
- `function:associate-care-plan-context` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / Functions`
- `service:care-plan-context-query` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / Services`
- `function:distribute-care-plan-change` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / Functions`
- `service:care-plan-change-notification` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / Services`
- `function:coordinate-multi-agency-chronic-care-plan` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Patient Clinical Record / [CT3] Primary Care Coordination / Functions`
- `service:chronic-care-timeline-query` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Patient Clinical Record / [CT3] Primary Care Coordination / Services`
- `function:route-acute-critical-result-alert` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Episode & Encounter Administration / [CT3] Acute Care Monitoring & Event Coordination / Functions`
- `service:acute-clinical-alert-dispatch` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Episode & Encounter Administration / [CT3] Acute Care Monitoring & Event Coordination / Services`
- `function:assemble-inpatient-discharge-dossier` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Ward Operations / [CT3] Multidisciplinary Inpatient Care Coordination / Functions`
- `service:discharge-dossier-packaging-service` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Ward Operations / [CT3] Multidisciplinary Inpatient Care Coordination / Services`
- `function:receive-diagnostic-requisition-feed` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Diagnostic Administration / [CT3] Diagnostic Service Integration / Functions`
- `service:modality-worklist-distribution` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Diagnostic Administration / [CT3] Diagnostic Service Integration / Services`
- `function:publish-governed-diagnostic-report` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Diagnostic Administration / [CT3] Diagnostic Service Integration / Functions`
- `service:diagnostic-publication-service` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Diagnostic Administration / [CT3] Diagnostic Service Integration / Services`
- `function:correlate-adverse-reaction--allergy-profile` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Medication Administration / [CT3] Medication Therapy Provision and Monitoring / Functions`
- `service:allergy--adverse-reaction-query` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Medication Administration / [CT3] Medication Therapy Provision and Monitoring / Services`
- `function:project-screening-eligibility-timeline` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Client-Centred Administration / [CT3] Screening and Immunisation Enablement / Functions`
- `service:preventive-screening-status-query` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Client-Centred Administration / [CT3] Screening and Immunisation Enablement / Services`
- `function:bind-community-support-network-context` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / [CT3] Community Care Coordination / Functions`
- `service:community-care-team-query` — `Business / Business Enabling Capabilities / [CT1] Care Administration Enablement / [CT2] Care Plan Administration / [CT3] Community Care Coordination / Services`
- `service:ward-operational-summary-query` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Ward Operations / Services`
- `service:ed-operational-status-query` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Emergency Department Operations / Services`
- `service:ambulatory-session-status-query` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Outpatient Operations / Services`
- `function:track-operational-staff-presence` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Mobile Staff Management / Functions`
- `service:staff-presence-telemetry-query` — `Business / Business Enabling Capabilities / [CT1] Health Service Operations Enablement / [CT2] Mobile Staff Management / Services`
- `function:retrieve-health-information` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Access / Functions`
- `service:clinical-resource-retrieval-service` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Access / Services`
- `function:apply-access-qualified-result-filtering` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Access / Functions`
- `function:evaluate-consent-constraint` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Functions`
- `service:consent-enforcement-service` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Services`
- `function:propagate-security-context` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Functions`
- `service:security-context-validation-service` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Services`
- `function:record-security-significant-activity` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Functions`
- `service:security-audit-ingress` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Health Information Control / Services`
- `service:information-dictionary-query` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Information Design Governance / Services`
- `service:terminology-binding-specification-lookup` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Information Design Governance / Services`
- `service:exchange-profile-specification-query` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Information Design Governance / Services`
- `process:assurance-process-design` — `Business / Business Enabling Capabilities / [CT1] Independent Assurance Enablement / [CT2] Assurance Specification / [CT3] Assurance Design / Processes`
- `proposal:service-associated-binary-content-retrieval` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Patient Clinical Record / [CT3] Linked Information Curation / Services`
- `proposal:service-governed-reference-dataset-content-query` — `Business / Business Enabling Capabilities / [CT1] Shared Information & Coordination Enablement / [CT2] Clinical Knowledge Services / [CT3] Governed Reference Dataset Administration / Services`
- `proposal:service-authorised-research-information-export` — `Business / Business Enabling Capabilities / [CT1] Health Informatics Management / [CT2] Research Information Curation / Services`

There are **20 unresolved current Feature
assertions**. They remain metadata/findings and are excluded from Feature grouping;
no replacement association is inferred:

- Assertion 246: `function:coordinate-multi-agency-chronic-care-plan` → `feature:primary-care-notification-dispatch`; unresolved.
- Assertion 247: `service:chronic-care-timeline-query` → `feature:primary-care-notification-dispatch`; unresolved.
- Assertion 248: `function:route-acute-critical-result-alert` → `feature:acute-clinical-state-event-capture`; unresolved.
- Assertion 249: `service:acute-clinical-alert-dispatch` → `feature:acute-clinical-state-event-capture`; unresolved.
- Assertion 250: `function:assemble-inpatient-discharge-dossier` → `feature:inpatient-clinical-progression-tracking`; unresolved.
- Assertion 251: `service:discharge-dossier-packaging-service` → `feature:inpatient-clinical-progression-tracking`; unresolved.
- Assertion 252: `function:receive-diagnostic-requisition-feed` → `feature:laboratory--imaging-result-distribution`; unresolved.
- Assertion 253: `service:modality-worklist-distribution` → `feature:laboratory--imaging-result-distribution`; unresolved.
- Assertion 254: `function:publish-governed-diagnostic-report` → `feature:diagnostic-history-consolidation`; unresolved.
- Assertion 255: `service:diagnostic-publication-service` → `feature:diagnostic-history-consolidation`; unresolved.
- Assertion 256: `function:correlate-adverse-reaction--allergy-profile` → `feature:medication-administration-tracking`; unresolved.
- Assertion 257: `service:allergy--adverse-reaction-query` → `feature:medication-administration-tracking`; unresolved.
- Assertion 258: `function:project-screening-eligibility-timeline` → `feature:screening-recall-notification-distribution`; unresolved.
- Assertion 259: `service:preventive-screening-status-query` → `feature:screening-recall-notification-distribution`; unresolved.
- Assertion 260: `function:bind-community-support-network-context` → `feature:community-care-plan-synchronization`; unresolved.
- Assertion 261: `service:community-care-team-query` → `feature:community-care-plan-synchronization`; unresolved.
- Assertion 262: `function:track-operational-staff-presence` → `feature:mobile-worker-task-dispatch`; unresolved.
- Assertion 263: `service:staff-presence-telemetry-query` → `feature:mobile-worker-task-dispatch`; unresolved.
- Assertion 264: `function:retrieve-health-information` → `feature:access-controlled-information-filtering`; unresolved.
- Assertion 265: `service:clinical-resource-retrieval-service` → `feature:access-controlled-information-filtering`; unresolved.

No current Feature ancestry is unresolved: all 157 recorded paths remain complete.
Pending and superseded records, unresolved identity and definition cases remain
available for inspection. This task makes no architectural adjudication.

## Workbench, schema and recovery

**Archi 5.10.0** remains the supported human workbench. The existing namespace,
schemaLocation and all bundled schema bytes are unchanged. The previous pass
proved all five installed Archi 5.10.0 schemas byte-identical to the repository
schemas. This pass validates against those schemas. The earlier native CLI import
could not initialise GTK against the available display; it was not retried
unchanged and no successful native GUI import is claimed. Mark performs final
import, rendering, ordering, navigation and layout acceptance.

Immutable 1.0.5A recovery replay passes byte-for-byte, including model hash
`f85be36ff6edb3c6491dd526c42d4207db9214154da437941b709de34dc106a1`.
Its complete file boundary, historical reports and release fixture are protected.
Root Architecture Release remains **1.0.6**, previous **1.0.5**; generation reads it
and never increments it. No commit has been made.

## Verification and generated-output set

The final generated-output set contains **14 files**. Earlier references to
12 counted only core generator outputs; 14 includes the completion report and
inspection guide. New landscapes are inside XML and add no output files.
Preservation proofs, verification logs and frozen prior reports are audit inputs
or evidence, not generator outputs. Same-process regeneration checks core
generator determinism. Independent processes reproduce and compare the complete
14-file output set byte-for-byte, including the report and guide.

- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/discrepancies.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/motivation.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/relationships.md`
- `docs/markdown/governance/baseline/services.md`

| Check | Result | Evidence | Log |
| --- | --- | --- | --- |
| generation | PASS | XSD, exact preservation, ancestry, four-projection Feature reachability, association navigation, landscape coverage, layout and deterministic regeneration PASS | [verification/human-inspection/generation.log](verification/human-inspection/generation.log) |
| publication | PASS | 102 publication tests PASS, including 31 baseline continuity/rejection tests and Feature/landscape/layout regressions | [verification/human-inspection/publication.log](verification/human-inspection/publication.log) |
| scripts | PASS | 68 repository script/unit tests PASS | [verification/human-inspection/scripts.log](verification/human-inspection/scripts.log) |
| architecture | PASS | 90 architecture tests PASS, zero failures/errors/skips; BUILD SUCCESS | [verification/human-inspection/architecture.log](verification/human-inspection/architecture.log) |
| rad | PASS | RAD validation PASS; 2,467 unresolved source-era findings preserved | [verification/human-inspection/rad.log](verification/human-inspection/rad.log) |
| identity | PASS | Identity registry validation PASS; no new allocations | [verification/human-inspection/identity.log](verification/human-inspection/identity.log) |
| bindings | PASS | Exact representation bindings/crosswalk validation PASS | [verification/human-inspection/bindings.log](verification/human-inspection/bindings.log) |
| release | PASS | Release check PASS; 1.0.6 unchanged | [verification/human-inspection/release.log](verification/human-inspection/release.log) |
| recovery | PASS | Immutable 1.0.5A recovery replay PASS, byte-for-byte | [verification/human-inspection/recovery.log](verification/human-inspection/recovery.log) |
| snapshot_tests | PASS | 17 immutable source snapshot tests PASS | [verification/human-inspection/snapshot_tests.log](verification/human-inspection/snapshot_tests.log) |
| snapshot | PASS | Read-only frozen capability/identity/ancestry/source navigation checks PASS | [verification/human-inspection/snapshot.log](verification/human-inspection/snapshot.log) |
| audit | PASS | Direct starting-candidate audit PASS; unchanged semantics, Documentation, Properties, all 1,012 View IDs and release; 247 authorised View presentation changes, 765 unchanged existing Views | [verification/human-inspection/audit.log](verification/human-inspection/audit.log) |
| archi_sort | PASS | Installed Archi 5.10.0 comparator inspection confirms display-name sorting; local View-only 0- landscape prefix is mechanically validated, without claiming GUI acceptance | [verification/human-inspection/archi_sort.log](verification/human-inspection/archi_sort.log) |
| check | PASS | Read-only generated baseline divergence check PASS | [verification/human-inspection/check.log](verification/human-inspection/check.log) |
| final_workflow | PASS | Generated baseline and report checks PASS; all 14 complete outputs independently regenerate byte-for-byte; release unchanged | [verification/human-inspection/final-workflow.log](verification/human-inspection/final-workflow.log) |

The publication gate includes XSD, exact crosswalk/identity continuity, capability
ancestry, independent BC/BEC Feature reachability, Feature-centred resources and
Views, landscapes, layout, Documentation publication and rejection regressions.
Exact commands:

- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s docs/archimate/capability-architecture/tests -v`
- `timeout --signal=TERM --kill-after=5s 120s python3 -m unittest discover -s scripts/tests -v`
- `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 timeout --signal=TERM --kill-after=5s 180s mvn -o test -pl paradeigma/paradeigma-test -am '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/rads_business_architecture.py validate`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_identity.py validate`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_identity.py bindings --check`
- `timeout --signal=TERM --kill-after=5s 60s python3 scripts/architecture_release.py check`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/recover_v02.py --check`
- `timeout --signal=TERM --kill-after=5s 60s python3 -m unittest discover -s docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/tools -p 'test_*.py' -v`
- `timeout --signal=TERM --kill-after=5s 60s python3 docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/snapshot-check.py`
- `timeout --signal=TERM --kill-after=5s 60s python3 docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/starting-candidate-audit.py`
- `javap -classpath /usr/vm/devel/Tools/Archi/plugins/com.archimatetool.editor_5.10.0.202608252016/com.archimatetool.editor.jar -c -p 'com.archimatetool.editor.views.tree.TreeModelViewer$1'`
- `timeout --signal=TERM --kill-after=5s 90s python3 docs/archimate/capability-architecture/tools/synchronise_baseline.py --check`
- `PYTHONDONTWRITEBYTECODE=1 timeout --signal=TERM --kill-after=5s 180s make -C docs/archimate/capability-architecture baseline-check baseline-report baseline-independent`

## Files changed in this bounded correction

- `docs/archimate/capability-architecture/Makefile`
- `docs/archimate/capability-architecture/README.md`
- `docs/archimate/capability-architecture/tools/synchronise_baseline.py`
- `docs/archimate/capability-architecture/tools/feature_projection.py`
- `docs/archimate/capability-architecture/tools/report_baseline.py`
- `docs/archimate/capability-architecture/tools/verify_baseline_outputs.py`
- `docs/archimate/capability-architecture/tests/test_baseline.py`
- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-Baseline.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/validation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/human-inspection-start-preservation.json`
- `docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-synchronisation-report.md`
- `docs/archimate/capability-architecture/synchronised-baseline/pre-human-inspection-verification.json`
- `docs/archimate/capability-architecture/synchronised-baseline/2026-10-17-archimate-markdown-baseline-synchronisation.md`
- `docs/archimate/capability-architecture/synchronised-baseline/archi-inspection-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/verification/human-inspection/`
- `docs/markdown/governance/archi-navigation-and-view-convention.md`
- `docs/markdown/governance/baseline/correspondence.json`

Generated catalogues that reproduce unchanged remain in the output manifest above.
The earlier uncommitted candidate's other modifications are retained; no rollback
or restart from a prior release occurred.

## Remaining human acceptance

Mark should complete every point in the [Archi 5.10.0 guide](archi-inspection-guide.md),
including immediate-child landscapes and ordering, Person Identity Feature resource
and View contexts, its CT3-wide Behaviour View and visual grouping, shared-resource
exceptions, terminal contexts, numbering, Documentation and visible pending cases.
The 13 shared-resource exceptions and 20 unresolved assertions are explicit review
items; this task does not resolve them through preferred ownership or new edges.

Final model SHA-256: `d57a5adeeef6b0906b30a02c3ea80cc6ff6308988775e7e3a619db01564ca52a`.
