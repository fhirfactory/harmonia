Report-Produced-At: 2026-11-03T00:02:46.592034Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Dokimasia Provenance Management and Assurance — candidate Report

**Outcome:** governed ADD candidate prepared for Solution Architect review under
HCRL 1.1.19-001 **DR-18 (Provenance Management)** and **DR-19 (Provenance and
Assurance)**. Neither DR nor the candidate is accepted or closed. DR-1 retains
its exact scope/status; DR-17's explicit acceptance and all inherited dispositions
remain intact. There is no release transition, accepted-baseline publication,
staging, commit or push.

Corresponding [operative Task](../../before/ai-agent-tasks/unresolved-dokimasia-provenance-add.md)
preserves the issued wording. Its instruction-entry timestamp and filename prefix
remain UNRESOLVED; no current/capture/Report time is substituted. The Task is the
available attributable account of Solution Architect intent from the DR-1 discussion;
no earlier conversation or historical decision is reconstructed. The Report's
production event is independently captured by the maintained emission machinery.

## Candidate-change summary for the Solution Architect

Dokimasia explicitly owns the Provenance Management framework and lifecycle and
provides a reusable client library through which originating components/activities
create, reference and report/publish Provenance. Provenance is governed information
managed through Mneme and durably persisted through Mnemosyne using existing mechanisms.
It references existing information/activity/context identities without requiring
embedded contents. Provenance is foundational evidence for independent Assurance;
Praxis remains the basis, Journey the observed evidence subject, and assessment/
findings/outcomes remain distinct from that evidence. Kleio retains Audit ownership
and historical audit evidence. No transport, API, implementation class, deployment,
new information family or separate execution/persistence framework is introduced.

Two distinct missing concepts and ten native relationships are added. Four existing
components receive append-only candidate responsibility clarifications; their original
definitions and source-qualified contributions remain unchanged. The resulting
projection has **1,996 elements / 2,246 relationships / 1,288 Views**, SHA-256
`13b0c5cf52d092e70a9f57e6d95a8ebc1d3fd712dfb7c60dfb92b6c38d7ecebf`. Inspect the
[candidate XML](../dokimasia-provenance-add/Harmonia-Provenance-candidate.xml),
[reader](../dokimasia-provenance-add/reader-candidate/continuum.html),
[reconciliation](../dokimasia-provenance-add/reconciliation.md) and
[validation summary](../dokimasia-provenance-add/checks/summary.json).

## Governance and architecture examined

The common Governance, Review Praxis, Change Provenance, release policy,
Continuum Representation/Incorporation Praxis, identity/naming and workbench/
Round-Trip contracts governed the work. DR-18/19 were registered before canonical
mutation, following DR-17's actual last identifier. Two DRs keep the management/
persistence and independently assessable Assurance boundaries distinct. Intent
agreement authorises preparation, not acceptance of these exact representations.

Baseline is current canonical Markdown at Git
`32014bb5843ad7dffdda9ccc404173b758cec690`, with an initially clean working tree. Its pre-ADD
projection reproduces accepted DR-17 specimen SHA-256
`27ef3c237ef7dca9cc7577d8ebbde2ff47930ea6a33249468b36969293c7b5af`. No historical export substitutes for that authority.
Root release remains **1.1.19 / predecessor 1.1.18**; pending candidate preparation
consumes no increment. Existing publications remain byte-identical.

[Discovery evidence](../dokimasia-provenance-add/discovery.json) records the related
canonical inventory, original standing/qualifications, definitions and relationships.
Semantic review covered Dokimasia, Kleio, Mneme, Mnemosyne, Ponos, Praxis, Journey,
Pragma Topic/Management, Ergon, Dokimasia Ergon, AI Assurance Ergon, Context Topic,
InformationUnit (application Class and conceptual BusinessObject), Information
Assertion, ServiceAssurance, current/historical Data Provenance Capture, independent
Assurance/evidence/assessment/conclusion capability and behaviour concepts, Provenance
principles/requirements, REQ-FND-005, finite-assurance/self-AuditEvent and observation/
inference constraints. Current versus historical/candidate standing remains explicit.
Names located subjects and were never used to establish identity equivalence.
Relevant canonical axioms AX-04/05/06/08/09/10/11/14/17 and consistency reasoning are
recorded in the reconciliation matrix.

## Already represented and bounded changes

The six responsibility dispositions in the [reconciliation matrix](../dokimasia-provenance-add/reconciliation.md#responsibility-reconciliation)
separate already adequate assertions, clarification and missing ADDs:

1. Dokimasia's independent Assurance role existed; explicit Provenance framework/
   lifecycle ownership was missing and is added to its existing component definition.
2. Distributed/common execution existed; no equivalent Provenance client library
   component existed. One logical ApplicationComponent represents this distinct
   agreed library responsibility. Kleio's audit library remains separate.
3. Mneme/Mnemosyne information/persistence boundaries existed; explicit application
   to Provenance is clarified without new stores, services, requirements or SLA.
4. Information identity/version/reference/derivation concepts existed. One DataObject
   represents governed Provenance information, with references to existing elements.
   No schema, FHIR mapping, information family or execution-attempt entity is invented.
5. Independent Assurance, Praxis basis, Journey evidence subject, common execution,
   specialised Ergon, findings/outcomes and observation/inference distinctions existed.
   The foundational Provenance-evidence role is made explicit, without duplicating them.
6. Kleio's Audit ownership and evidence input to Dokimasia already existed. Candidate
   clarification preserves that ownership alongside new Provenance ownership.

**New elements:** Provenance — native DataObject, canonicalId
`e282811b51274649816566aac7edeb8a`; Provenance Client Library — native
ApplicationComponent, canonicalId `171f05d188384cedb7d5c493eeaee519`.
Their exact paths/Exchange bindings are retained in
[allocation evidence](../dokimasia-provenance-add/allocated-identities.json).
Each UUIDv4 was allocated once through governed `create` and persisted before
projection. A post-create agent-side read error was recovered by inspecting and
reusing the existing Provenance identity; no second allocation occurred. Local
lexical friendlyNames follow existing rules; formal canonicalName qualifiers
remain explicitly unresolved under the current naming contract.

**Existing definitions amended:** Dokimasia, Kleio, Mneme and Mnemosyne receive
separately qualified append-only candidate sections and provenance contributions.
All prior metadata (apart from those appended contributions/edges), definitions,
classifications, specialisations, source bindings and standing remain intact.
No legacy carrier is rewritten or retired by this ADD.

**New relationships:** two Associations for Dokimasia provision of the library and
Kleio historical-evidence contribution; four Access assertions for Dokimasia lifecycle/
Assurance use, client-library creation/reporting, Mneme management and Mnemosyne durable
persistence; four directed Associations from Provenance to existing InformationUnit,
Pragma, Ergon activity and Context. [Exact relationship evidence](../dokimasia-provenance-add/relationship-additions.json)
records all ten IDs, endpoints, native types, Task selectors and candidate standing.
No old edge is amended. Association makes no Composition/Home/deployment claim;
Access does not define an implementation endpoint, invocation or transport.

**Navigation/projection:** two entries are added under existing Reusable Information
Concepts and Dokimasia Component Architecture. Prior navigation entries, paths and
order are preserved. The generated root Manifest is refreshed; candidate reader,
narrative LaTeX source and XML are evidence, not an accepted baseline publication.
The existing exact-population test fixture changes from 1994/2236 to 1996/2246;
its 462 qualified-source-predicate expectation and all validation guards are retained.
No production tooling or implementation changes.

## Preserved identities, responsibilities and open questions

[Preservation checks](../dokimasia-provenance-add/preservation.json) establish all
**1,994 existing canonical identities/names/types/specialisations/standing/bindings**,
**2,236 existing native and projected relationship assertions** and **1,288 Views**
unchanged. All 1,990 unaffected element files are byte-identical. The original
bodies of the four touched components remain exact prefixes. All prior DR-1–DR-17
text/table rows and acceptance states are unchanged. The maintained presentation,
1379
protected governance/tooling/input/earlier-evidence/publication files (including
204 publication files) remain byte-identical.

No conflicting canonical assertion was identified within this bounded review;
the reconciliation explains existing Kleio durable-evidence language, Provenance/
Audit/Assurance distinctions and Journey's incomplete-provenance qualification.
No supersession or silent conflict resolution was performed. Any later evidenced
conflict requires separate Solution Architect adjudication under AX-17.

All six Task §4.5 questions remain UNRESOLVED: original versus linked new retry
Pragma; execution-attempt representation; synchronous/asynchronous reporting;
publication acknowledgement/recovery; schemas/cardinalities/lifecycle states;
detailed client-library contracts/deployment. They remain explicit in both new
elements and are not prerequisites for this responsibility ADD. Existing evidence-
access/control independence, detailed workflow and assurance data/preservation,
Praxis/Journey/AI-discovery qualifications remain intact. No recursive Dokimasia
assessment or separate own-execution AuditEvent is required. The assured activity
cannot determine, suppress, manufacture or alter its outcome; contributing evidence
confers no such authority.

## Validation and conformance

| Check | Actual result and limit |
| --- | --- |
| Canonical schema, identity, naming, standing, provenance, source binding, navigation | PASS; 1,996 elements, 2,246 relationships, 462 qualified predicates. |
| Native endpoint applicability, containment, XML offline schema/references | PASS; projection allocated zero identities. |
| Deterministic projection and candidate reader/narrative/Manifest generation/staleness | PASS. No protected release publication overwritten. |
| Isolated maintained-input generation | PASS; XML, Manifest and all reader/narrative artefacts reproduce identically without historical docs/scripts/Git/processing. |
| Source links and generated browser DOM/affected native catalogue structure | PASS; agent structural checks, no human visual acceptance claim. |
| Full architecture-tooling mutation/conformance suite | PASS; 87 tests in 358.300 seconds, exit 0. |
| Required Java ArchitectureTest suite | PASS; 90 tests, 0 failures/errors/skips, exit 0. |
| Existing identity/content/relationship/View/unrelated-state preservation | PASS; details above and in preservation evidence. |
| Actual installed Archi 5.10 import/save/export | PASS, exit 0; exact native model/returned XML/execution evidence retained. |
| Strict Archi transport verifier | **FAIL**; exact inherited 575 relationship record language-attribute drift. No guard waived or weakened. |
| Human candidate/structural inspection and acceptance | **PENDING**; successful automated checks supply no acceptance. |
| Original Task timestamp/filename prefix | **UNRESOLVED**; Solution Architect treatment required before closure. |

[Archi transport evidence](../dokimasia-provenance-add/archi-transport/summary.json)
confirms all 1,996 canonical element fields/Property values and all 2,246 native
relationship identities/types/endpoints/text/Property values preserved, with zero
missing/added relationships or Views and zero changed View records. Strict raw
comparison retains the same set of 575 language-changed relationship IDs as prior
DR-17 qualification. Generated labels/organisation-ID omissions and navigation
normalisation remain explicitly recorded in raw evidence; no whole-model fidelity,
transport-loss acceptance or strict PASS is asserted. Returned XML SHA-256:
`d51da17dca64b89791301df8024405e364903a5c25b30d9521ef649297534176`. The no-edit return never replaces the source
projection or maintained template. Initial sandbox display access failed (exit 13);
the same bounded command succeeded with reviewed local-display escalation and
outputs confined to temporary paths. This is execution recovery, not an architecture
change or a bypass of a human checkpoint.

Required checks used finite timeouts; [check evidence](../dokimasia-provenance-add/checks/summary.json)
records commands/results. The environment's default Python lacked dependencies;
the existing tooling virtual environment provided the declared dependencies.
No dependency installation or unrelated remediation was performed. Transient
verification-harness corrections changed no architecture, validator or comparator.
No runtime Provenance-library implementation, delivery protocol, persistence SLA
or assurance-algorithm verification is claimed.

## Changed files and completion boundary

Changed canonical files: the four existing application-component files for
Dokimasia/Kleio/Mneme/Mnemosyne; two newly allocated resources listed above;
`enterprise-architecture/continuum/navigation.json`; generated
`enterprise-architecture/continuum/MANIFEST.md`.
Governance/support: pending `design/review/1.1.19/hcrl-1.1.19-001.pending.md` (only
DR-18/19 rows/sections added); required Task at this iteration's `before/ai-agent-tasks/`;
this Report at `after/ai-agent-reports/`; this candidate's selected evidence under
`after/dokimasia-provenance-add/`; one existing count fixture in
`enterprise-architecture/tooling/tests/test_continuum.py`; the requested working
report at `.junie/reports/dokimasia-provenance-management-and-assurance-add.md`.
The detailed [changed-file inventory](../dokimasia-provenance-add/changed-files.json)
is maintained within this same provenance boundary.

**Scope deviations:** none. No new requirement, ungoverned specialisation, unrelated
architecture, source supersession, implementation, release transition, accepted
baseline publication, staging, commit or push. Git state remains an uncommitted
candidate; earlier Git HEAD does not contain these new resources/evidence.
All exclusions, qualifications and unanswered questions remain visible.

**Required next decision:** Solution Architect review/disposition of DR-18 and
DR-19 and the exact candidate, including its qualified source/graph representation
and affected native/reader entries. This Report supplies no acceptance or closure.
DR-1, DR-18, DR-19 and HCRL 1.1.19-001 remain open at the review boundary.
