<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Published exchange packages

Each `<release>/` directory contains a governed exchange representation,
currently the Open Exchange XML alone. [Architecture Change Provenance](../../../markdown/governance/architecture-change-provenance.md)
defines durable evidence of the authorised transition; a processing tree is not
that evidence package. Reports, raw validation results and recovery records
remain working/historical material in [processing/publishing](../../processing/publishing/)
while needed. The historical release links below retain their recorded standing;
installation does not migrate their evidence, change their bytes or permit cleanup.

------- Legacy Content - Superseded ------- Start ------

Superseded by: [Architecture Change Provenance](../../../markdown/governance/architecture-change-provenance.md#transition-verification-versus-historical-machinery-reproduction) and [Change Review closure](../../../markdown/governance/archimate-ingestion-change-review.md#processing-record-and-audit-evidence).
Only the retention/evidentiary meaning of the following unchanged
guidance is superseded; operational and historical details remain useful.

---------------------------------------------------------

Each `<release>/` directory contains the finished governed Harmonia → Archi
exchange package. Keep it lean: currently the Open Exchange XML alone. Reports,
validation results, source history, inspection guides and verification evidence
belong in [processing/publishing](../../processing/publishing/).

------- Legacy Content - Superseded ------- Finish ----

- [RAD-1.1.0](1.1.0/Harmonia-Architecture-1.1.0.xml): current governed baseline,
  promoting corrected Run-002 CR-001–154; 1,394 elements, 1,137 relationships
  and 1,209 preserved Views. CR-155–158 remain unresolved and unchanged.
  [Release authority and scope](../../../markdown/governance/baseline/release-1.1.0.json),
  [publication evidence](../../processing/publishing/1.1.0/information-architecture-publication.md)
  and [final Archi inspection](../../processing/publishing/1.1.0/archi-inspection-guide.md).
- [RAD-1.0.10](1.0.10/Harmonia-Architecture-1.0.10.xml): initial IA element population,
  50 native BusinessObjects and 4 native DataObjects; [publication evidence](../../processing/publishing/1.0.10/information-population.md)
  and [Archi handoff](../../processing/publishing/1.0.10/archi-inspection-guide.md).
  No IA semantic relationships, diagrams or physical objects. The same candidate
  now completes local BO/TOPIC canonicalNames under the [adjudication](../../../markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary).
  Composition ancestry governs contextual names; no IA containment is created.
  Human rendering review remains pending.

  ------- Legacy Content - Superseded ------- Start ------
  Superseded by: [IA naming adjudication](../../../markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary).

  No IA semantic relationships, diagrams or physical objects; canonicalName remains
  explicitly stopped on the 54 new elements. Human rendering review remains pending.

  ------- Legacy Content - Superseded ------- Finish ----
- [RAD-1.0.9](1.0.9/Harmonia-Architecture-1.0.9.xml): preserved originating model; authorised run-001/run-002
  reconciliation; [publication and validation](../../processing/publishing/1.0.9/reconciliation-publication.md)
  and [inspection guide](../../processing/publishing/1.0.9/archi-inspection-guide.md).
  Human rendering acceptance remains pending.
- [Release 1.0.8](1.0.8/Harmonia-Architecture-1.0.8.xml): originating structure/tooling
  publication; [inspection guide](../../processing/publishing/1.0.8/archi-inspection-1.0.8.md).
- [Release 1.0.7](1.0.7/Harmonia-Architecture-1.0.7.xml): frozen accepted identity-bearing baseline.
- [Release 1.0.6](1.0.6/Harmonia-Architecture-Baseline.xml): frozen earlier baseline.
- [Release 1.0.4](1.0.4/Harmonia-Capability-Architecture.xml): frozen navigation publication.

These representations are generated from the governed Harmonia Markdown RAD and
projection metadata. They establish no independent architecture authority.
The root [release authority](../../../../architecture-release.json) determines
the current release; publication never increments it.

Internal governance/tooling evidence is held outside both architecture publication
namespaces. The [1.0.14 evidence archive](../../governance-tooling/1.0.14/README.md)
retains the original normalisation model, reports and validation records. Its
manifest explicitly classifies it as non-RAD; it is not an architecture release
or an authoring baseline.

Release 1.0.8 retains 1.0.7 architecture and presentation; the XML changes only
one model-level release value. The preserved [real Archi 5.10 acceptance evidence](../../../../.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md)
remains the empirical identity/naming proof. Historical recovery outputs remain
with their processing evidence. Use the [location map](../../framework/historical-locations.json)
for historical locators. Incoming exports belong in [ingest](../ingest/README.md).
