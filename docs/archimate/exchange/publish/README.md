<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Published exchange packages

Each `<release>/` directory contains the finished governed Harmonia → Archi
exchange package. Keep it lean: currently the Open Exchange XML alone. Reports,
validation results, source history, inspection guides and verification evidence
belong in [processing/publishing](../../processing/publishing/).

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
