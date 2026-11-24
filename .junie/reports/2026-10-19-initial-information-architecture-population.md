<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Initial Information Architecture element population — 2026-10-19

## Current adjudication outcome — unsigned RAD-1.0.10 completed

**Deterministic projection:** all **54 existing IA canonicalName properties are
now complete**: 50 native BusinessObjects use `BO`; 4 logical DataObjects in
InformationMap use `TOPIC`. The original governed Names, local friendlyNames,
canonicalIds and representation IDs are unchanged. **RAD-1.0.10 remains the same
unsigned candidate**, with predecessor 1.0.9; the release file is byte-identical
to the candidate before this naming completion. This is not another architecture
increment. Human rendering review and candidate sign-off remain pending.

**Governed fact — Solution Architect adjudications recorded in RADS:**

1. [The naming contract](../../docs/markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary)
   authorises `BO` for governed conceptual native BusinessObject and `TOPIC` for
   governed logical native DataObject within InformationMap. Existing PascalCase
   spelling and qualification rules apply unchanged; the Name suffix participates
   in the existing lexical component. Thus `Practitioner BO` → `PractitionerBo`
   → `BO-PractitionerBo`, and `Context Topic` → `ContextTopic` → `TOPIC-ContextTopic`.
   No Artifact/ARTEFACT canonical qualifier is established.
2. [The cross-layer containment rule](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming)
   establishes native Composition **parent → child** as governed semantic containment
   in every layer. Its ancestry is authoritative for contextual friendlyName and
   canonicalName. Folders, Views, geometry, lexical similarity, proximity, Home and
   responsibility cannot supply or manufacture that ancestry. Missing required
   context stops the dependent property until authored containment is ingested.
3. The Solution Architect explicitly accepts the **323 pre-existing Business
   behaviour elements without direct Composition containment** as non-blocking
   modelling debt. Their contextual names are preserved exactly; no Composition
   is inferred, created or migrated. Once explicit Composition exists, normal
   contextual naming applies from that governed ancestry. Acceptance does not
   declare their containment complete or waive the cross-layer rule. Their exact
   identities are recorded in [the preservation proof](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/preservation.json).

**Deterministic projection — local forms:** the same 54 established local
friendlyName components receive only the authorised type prefix. No source
establishes a required IA Composition parent for this population. These local
forms assert neither semantic root status nor a completed containment hierarchy.
No family, folder, Home or View provides qualification. Any later contextual
name depending on architect-authored Composition remains for that authored model
and subsequent ingestion; no IA context or naming hierarchy was invented here.
The original canonicalName-vocabulary stop is resolved for these local forms.

**Preservation:** the whole candidate differs from the pre-completion candidate
only by the **54 canonicalName property additions**, independently checked by
stripping precisely those properties and comparing the complete XML signature.
There are no new identities, elements, semantic relationships, Views, nodes or
connections in this completion. All other element semantics, metadata, existing
relationships, navigation and presentation are exact. The original initial
population still comprises **50 BusinessObjects, 4 DataObjects, 0 Artifacts**;
the complete model still has **1,362 elements, 1,077 relationships and 1,198 Views**.
No IA semantic relationship, including Composition or BO→Topic mapping, was
created in either the original population or this completion. No Technology/
Artefact population or physical naming qualifier was introduced.

**Unresolved ambiguity / Solution Architect decision required:** Endpoint BO
remains unpopulated for both competing Practitioner and Device interpretations;
no merger or distinguishing name was invented. All other original exclusions,
G2 ambiguities and qualified/candidate standing remain unchanged and are documented
in the initial execution record below. No cross-level equivalence was inferred.
The architect will author containment, IA relationships and Views in Archi; the
resulting model must provide future IA ingestion/reconciliation evidence.

This current section supersedes the original report's canonicalName stops,
model/source/export fingerprints, validation outcome and changed-file inventory
where explicitly indicated. The initial execution record is preserved below for
source evidence, all 54 element definitions and all exclusions. Its old hashes,
214-test result and intentional canonicalName absences describe the pre-adjudication
stage only. This report is execution evidence, not architectural authority.

## Current authority, implementation and reproducibility

The authority hierarchy and source definitions in the original record still apply.
AX-04 keeps semantics separate from machinery; AX-14 preserves distinctions;
AX-17 preserves unresolved authority and prevents inferred hierarchy. The supplied
adjudications are recorded in governed RADS. No conflict with those axioms was
identified. The [release policy](../../docs/markdown/governance/architecture-release-policy.md)
records completion within the unsigned candidate without another increment.

The naming contract, modelling strategy, ingestion/publication semantics, mapping
contract, mapping catalogue and initial IA projection contract record the vocabulary
and cross-layer invariant. Earlier Business Home derivation is retained as provenance
for preserved candidate names, subject to the later Composition rule. No global
Business naming migration or IA-specific Home/navigation rule was implemented.

`governed_metadata.py` extends the shared qualifier vocabulary; TOPIC requires
explicit governed InformationMap scope rather than type/folder inference. The
initial population tool calls the existing `qualified_names` derivation, validates
persisted names and permits precisely the authorised source amendment. Publication
again requires complete identity and three-name metadata. Tests reject missing or
drifted canonicalNames, unsupported qualifiers, source amendment drift and protected
model changes. No publication path allocates identities or infers containment.

[The completion manifest](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/manifest.json) retains the original and
completed source hashes and explicit original/current governance amendments.
Original correspondence pins, once-only identity allocation snapshots, parent
verification/final-checks records and original Archi transport evidence were not
rewritten. Changed pre-completion documents/model/capture/report are byte-recoverable
under [before/](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/). Historical validators replay their original
sources; current-authority validation separately verifies authorised live amendments.
[The 68-file census](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/prior-files-preservation.json) records
every unchanged and authorised changed file against the pre-completion snapshot.

| Current or historical evidence | SHA-256 |
| --- | --- |
| Originating RAD-1.0.9, unchanged | `0f53e81f90a1d62974747e262218612dd70956c3eb65ebf3b2eb2b19af6ec529` |
| Pre-completion RAD-1.0.10 candidate, historical | `73a5f4d5356595cfc7dc78926d446923541fe9da36577285f634dcb00ba73094` |
| Completed RAD-1.0.10 candidate, current | `b1036f745e8ae3754951cd123b2b1a54a7eaa76c5fa83933abbd11ff2d4153bc` |
| Original IA source capture, historical | `e021b98318c5e56868c82b65a974551544c822428c8ecd98b4d508df1510d942` |
| Completed IA source capture, current | `4881e06319c806a3e719d60f60ef86d7414cd6f801b6000a1004def794489cfe` |
| Current actual Archi no-edit export, uncompressed XML | `ac7fad3d30b230e894fa12e64e74e77939fc6574dd3336a8ddd7569f70c22b65` |

## Current validation and transport results

[Executed completion checks](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/verification.json) and
[final publication/reproduction checks](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/final-checks.json) retain
commands, statuses and hash-pinned evidence. Commands are bounded; successful
logs are compressed with deterministic gzip timestamps.

| Check | Current outcome |
| --- | --- |
| Full ArchiMate/framework tests | **PASS: 216 tests**, 303.204s, 420s bound. |
| Focused governed metadata tests | **PASS: 25 tests**, 37.903s. |
| Governance Python tests | **PASS: 72 tests**, 31.953s, 120s bound. |
| Required Java architecture tests, JDK 21 / offline Maven | **PASS: 90 tests**, BUILD SUCCESS, 28.304s, 180s bound. |
| Open Exchange bundled XSD, identifiers/endpoints and references | **PASS** in publication and independent preservation. |
| Whole candidate preservation | **PASS**: precisely 54 canonicalName additions; all remaining candidate content exact. |
| Identity/name audit | **PASS**: 1,362 unique valid canonicalIds; all 1,362 displayName/friendlyName/canonicalName values present and match persisted derivation; information Names remain unique. |
| Naming diagnostic | `CONFORMANT_WITH_GOVERNED_DERIVATION`; validates persisted/source-derived names, **does not clear the accepted 323 Composition debt**. |
| Forbidden populations and links | **PASS**: no new IA relationships, Composition, elements, Artifact, ARTEFACT qualifier or candidate/ambiguous promotion. |
| RADS, frozen identity registry, release and historical evidence | **PASS**; release unchanged at 1.0.10 / 1.0.9. |
| Serialization-only round trip | **EQUIVALENT** under the unchanged comparator. |
| Actual Archi 5.10 full-model no-edit import/save/export | Scoped contract **PASS**; all 1,362 canonicalNames and other governed properties, native non-property content, navigation and View structure/geometry preserved. |
| Publication and report/inspection guide `--check` | **PASS**. |
| Independent publication reproduction | **PASS: all 15 generated outputs byte-identical** in independent processes; release unchanged. |
| Prior-file census | All 68 pre-completion files accounted for; original allocation and parent execution/transport evidence unchanged. |
| Whitespace | `git diff --check` **PASS**. |

The actual [Archi transport proof](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/archi-transport-verification.json)
is scoped; the unchanged raw comparator still reports **DIFFERENCES**:
INFORMATION LOSS 1,819; SEMANTIC DIFFERENCE 1,669; SEMANTICALLY EQUIVALENT
NORMALISATION 65; UNEXPLAINED DIFFERENCE 4,871; PRESENTATION-PRESERVING
TRANSFORMATION 0. All 1,669 semantic findings exactly match the originating
RAD-1.0.9 transport findings. The native saved model, actual export, successful
CLI log and raw findings remain beside that proof. No comparator weakening,
whole-model lossless fidelity, human rendering acceptance or architect-authored
IA ingestion/reconciliation proof is claimed.

Earlier completion attempts are retained: the authority-pin guard correctly
rejected governance changes before explicit preserved-authority amendment handling
was installed; an initial focused run exposed helper shadowing and a misplaced
test fixture, subsequently corrected; the first full suite was deliberately
**INTERRUPTED (exit 130)** after stale roundtrip assertions were observed. Its
partial log remains preserved, and the corrected final 216-test run passes.
These were harness/tooling corrections, not semantic model changes. No completion
command timed out or stalled. Original population attempts remain documented below.

## All 54 completed IA metadata records

Each row is a **deterministic projection** of the governed local Name and
adjudicated vocabulary. Every canonicalId, native type, governed Name and
friendlyName in this table is unchanged; only canonicalName was added. Full
source definition evidence and representation IDs remain in the original element
entries below and in the source capture. No row implies containment or a mapping
between abstraction levels.

| Native type | Governed Name | canonicalId, unchanged | friendlyName, unchanged | canonicalName, completed |
| --- | --- | --- | --- | --- |
| BusinessObject | ActionableTask BO | c43139289cb649b4836521a96a51604c | ActionableTaskBo | BO-ActionableTaskBo |
| BusinessObject | ActionableTaskArchetype BO | 1d6d4c3a2df440be94262855fa6564eb | ActionableTaskArchetypeBo | BO-ActionableTaskArchetypeBo |
| BusinessObject | Appointment BO | 9badf0d360b64215bc60e74eec954018 | AppointmentBo | BO-AppointmentBo |
| BusinessObject | AssuredHealthcareService BO | 07a1f83c9b4e462eaef1595c2a50d560 | AssuredHealthcareServiceBo | BO-AssuredHealthcareServiceBo |
| BusinessObject | Availability Schedule BO | 96638431fc39486d879d080768a68fe9 | AvailabilityScheduleBo | BO-AvailabilityScheduleBo |
| BusinessObject | Care-Place Definition BO | 779f2295b0b648dda9dbd21c4ade3ba0 | CarePlaceDefinitionBo | BO-CarePlaceDefinitionBo |
| BusinessObject | Carer Relationship BO | e95d07cf27c041de955665e7b2af8f52 | CarerRelationshipBo | BO-CarerRelationshipBo |
| BusinessObject | Clinical Privilege BO | 75e194f6168945588ed30cc6c78d04cc | ClinicalPrivilegeBo | BO-ClinicalPrivilegeBo |
| BusinessObject | DeliverableHealthcareService BO | 8d41e9fb6a164deb93afb5fc4c671e10 | DeliverableHealthcareServiceBo | BO-DeliverableHealthcareServiceBo |
| BusinessObject | Device Association BO | 303f8586953d450d8d82d3d5f76e488a | DeviceAssociationBo | BO-DeviceAssociationBo |
| BusinessObject | Device Definition BO | 2a879d06b17143be8bbfc35cdaab8bdf | DeviceDefinitionBo | BO-DeviceDefinitionBo |
| BusinessObject | Device Instance BO | 25c1b751956446cea17065b6254419d5 | DeviceInstanceBo | BO-DeviceInstanceBo |
| BusinessObject | Eligibility Rule Set BO | cdb296a83e7e4a17b5e80a9c910c114f | EligibilityRuleSetBo | BO-EligibilityRuleSetBo |
| BusinessObject | Encounter BO | fb464349f13b41c4ae9920a743fc6c12 | EncounterBo | BO-EncounterBo |
| BusinessObject | Episode BO | 3927d161556d48f4b2cc57bd46ab785d | EpisodeBo | BO-EpisodeBo |
| BusinessObject | Family Relationship BO | 8adbce49ffdd46a19e95cf40f2aa36fd | FamilyRelationshipBo | BO-FamilyRelationshipBo |
| BusinessObject | FulfillmentTask BO | 30f4deffcd694f07bf54b2b192695d4f | FulfillmentTaskBo | BO-FulfillmentTaskBo |
| BusinessObject | Geospatial Address BO | 771b2e814946401bbe6dd6b4fdd3fb42 | GeospatialAddressBo | BO-GeospatialAddressBo |
| BusinessObject | Healthcare Location BO | 9ee4df4d76864bb1ac90fe1077c56c6f | HealthcareLocationBo | BO-HealthcareLocationBo |
| BusinessObject | Healthcare Organisation BO | dd3ba70128874caeb2d5da419d006c49 | HealthcareOrganisationBo | BO-HealthcareOrganisationBo |
| BusinessObject | Healthcare Subject Context BO | e92ea15616ea43c9a1d698e34ed1376b | HealthcareSubjectContextBo | BO-HealthcareSubjectContextBo |
| BusinessObject | HealthcareServiceDelivery BO | 4627ee33845744f2ae2e10c84e313755 | HealthcareServiceDeliveryBo | BO-HealthcareServiceDeliveryBo |
| BusinessObject | Identifier BO | b3141f382ca04bf4bb214c12300ebbfb | IdentifierBo | BO-IdentifierBo |
| BusinessObject | Identity Alias BO | b444496792a24c33b439f9cb59a12d98 | IdentityAliasBo | BO-IdentityAliasBo |
| BusinessObject | Identity Correction BO | 7c74034576cf4972bbb6e86e0f0e1e58 | IdentityCorrectionBo | BO-IdentityCorrectionBo |
| BusinessObject | Identity Correlation Graph BO | 8d0a2577d3364dbda96798afe0800200 | IdentityCorrelationGraphBo | BO-IdentityCorrelationGraphBo |
| BusinessObject | Information Unit BO | 10284034c8534b639284386b064f2c54 | InformationUnitBo | BO-InformationUnitBo |
| BusinessObject | Legal Representation BO | 33ddad9f1ce1424bbe0fb9a25efadb9f | LegalRepresentationBo | BO-LegalRepresentationBo |
| BusinessObject | Location Classification BO | 3896c910c28646098f0707efe88169ee | LocationClassificationBo | BO-LocationClassificationBo |
| BusinessObject | Location Identity BO | 5f8299e31ee5410583007473c75d578f | LocationIdentityBo | BO-LocationIdentityBo |
| BusinessObject | Metabolē BO | c34b4929f71946708d37dbe6c1da920a | MetabolēBo | BO-MetabolēBo |
| BusinessObject | OfferedHealthcareService BO | 441b0891822f4add834ea930c3fe58b7 | OfferedHealthcareServiceBo | BO-OfferedHealthcareServiceBo |
| BusinessObject | Order BO | 2a791e0e1f6b45c0a57fb8233f73faa5 | OrderBo | BO-OrderBo |
| BusinessObject | Organisation Classification BO | 227974cf26054bdea17038ecd6c0aa3a | OrganisationClassificationBo | BO-OrganisationClassificationBo |
| BusinessObject | Organisation Contact Information BO | cad820b906fa40f28824278fb18b7497 | OrganisationContactInformationBo | BO-OrganisationContactInformationBo |
| BusinessObject | Organisation Identity BO | c240be16de76415682237a238b3d3fc8 | OrganisationIdentityBo | BO-OrganisationIdentityBo |
| BusinessObject | Organisation Profile BO | 04adbfc449bb4b9e9ae6e7fdf8a66bf9 | OrganisationProfileBo | BO-OrganisationProfileBo |
| BusinessObject | Organisational Unit BO | 2c884cdd6a4b41a184818e3a7e540897 | OrganisationalUnitBo | BO-OrganisationalUnitBo |
| BusinessObject | Person BO | 1163ffac87814f319578ef5cb95dcd4d | PersonBo | BO-PersonBo |
| BusinessObject | Person Identity BO | 3b72456b5686449f95ad26bfbbae07f3 | PersonIdentityBo | BO-PersonIdentityBo |
| BusinessObject | Practitioner BO | 4e8f2bd581a5418889813cf7ee414750 | PractitionerBo | BO-PractitionerBo |
| BusinessObject | Practitioner Identity BO | 2f2868f077fc47afbb541925a9801b2c | PractitionerIdentityBo | BO-PractitionerIdentityBo |
| BusinessObject | Practitioner Role Binding BO | cb7ac98f62aa4599ba57263338cb8b3c | PractitionerRoleBindingBo | BO-PractitionerRoleBindingBo |
| BusinessObject | Professional Profile BO | 703bb80c062743eeb7ed8dea3ffeaaa0 | ProfessionalProfileBo | BO-ProfessionalProfileBo |
| BusinessObject | Professional Registration BO | 26c72794993044af98b5329b8b14ae89 | ProfessionalRegistrationBo | BO-ProfessionalRegistrationBo |
| BusinessObject | Referral Outcome BO | 18ad2e58913b4a938e172df7b03f366a | ReferralOutcomeBo | BO-ReferralOutcomeBo |
| BusinessObject | Referral Progression Event BO | bc70a1055ca34eed8143856816587f67 | ReferralProgressionEventBo | BO-ReferralProgressionEventBo |
| BusinessObject | Search Result Set BO | b683ae87c72a4c16aba948a4b8cb0998 | SearchResultSetBo | BO-SearchResultSetBo |
| BusinessObject | ServiceOutcome BO | 4fb589629e7f405db44d6a87a2b5e36b | ServiceOutcomeBo | BO-ServiceOutcomeBo |
| BusinessObject | TaskOutcome BO | 08d8ee19a30f418f9d5bc497ced0d1ef | TaskOutcomeBo | BO-TaskOutcomeBo |
| DataObject | Context Topic | 76ded4866bf443dabc0849d68b215690 | ContextTopic | TOPIC-ContextTopic |
| DataObject | PetasosParcel Topic | f0a88f05a0cb438cb42572ec07fb8513 | PetasosParcelTopic | TOPIC-PetasosParcelTopic |
| DataObject | Pragma Topic | 27ad143e7c784e378556af8a57c438f7 | PragmaTopic | TOPIC-PragmaTopic |
| DataObject | Pylai.Diadromí Topic | 978f5c2bd2d343ffaf3a13dff700c258 | PylaiDiadromíTopic | TOPIC-PylaiDiadromíTopic |

## Current exact files changed

The complete current change set includes the original population work and this
completion; it is uncommitted. The machine-readable [file inventory](../../docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/changed-files.json)
contains the exact same paths as the following list. The pre-completion census
separately distinguishes completion edits from preserved initial work. There are
no deletions. Original RAD-1.0.9 publication and historical evidence are unchanged.

```text
.junie/reports/2026-10-19-initial-information-architecture-population.md
architecture-release.json
docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml
docs/archimate/exchange/publish/README.md
docs/archimate/framework/Makefile
docs/archimate/framework/README.md
docs/archimate/framework/tests/test_governed_metadata.py
docs/archimate/framework/tests/test_information_population.py
docs/archimate/framework/tests/test_roundtrip.py
docs/archimate/framework/tests/test_view_presentation.py
docs/archimate/framework/tools/diagnose_roundtrip.py
docs/archimate/framework/tools/governed_metadata.py
docs/archimate/framework/tools/home_context.py
docs/archimate/framework/tools/information_population.py
docs/archimate/framework/tools/report_baseline.py
docs/archimate/framework/tools/synchronise_baseline.py
docs/archimate/framework/tools/verify_governance_evidence.py
docs/archimate/framework/tools/view_presentation.py
docs/archimate/processing/publishing/1.0.10/archi-inspection-guide.md
docs/archimate/processing/publishing/1.0.10/information-population.md
docs/archimate/processing/publishing/1.0.10/information-population/allocation/information-architecture.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/allocation/manifest.json
docs/archimate/processing/publishing/1.0.10/information-population/allocation/prior-release.json
docs/archimate/processing/publishing/1.0.10/information-population/before-files.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before-files.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/2026-10-19-initial-information-architecture-population.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/Harmonia-Architecture-1.0.10.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/archi-inspection-guide.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/archimate-ingestion-publication-semantics.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/architecture-modelling-strategy.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/architecture-release-policy.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/architecture-release.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/canonical-identity-and-naming.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/information-architecture.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/information-population.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/initial-information-architecture-projection.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/rad-archimate-mapping-catalogue.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/before/rad-archimate-model-mapping.md.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/changed-files.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/final-checks.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/information-architecture.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/manifest.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-imported.archimate.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/Archi-no-edit-export.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/archi-transport-verification.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/cli-display.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/raw-comparator.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/roundtrip/verify-transport.py
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/archimate-tests-interrupted.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-archimate-tests-final.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-architecture-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-comparator.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-diagnostic.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-focused-corrected.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-focused.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-frozen-identity.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-git-whitespace.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-governance-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-history-final.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-identity.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-independent-reproduction.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-publication-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-publication-corrected.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-publication.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-rads.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-release-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-report-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/canonical-report-generation.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/diagnostic.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/prior-files-preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/validation/serialization.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/verification.json
docs/archimate/processing/publishing/1.0.10/information-population/canonical-name-completion/verify-completion.py
docs/archimate/processing/publishing/1.0.10/information-population/exclusions.json
docs/archimate/processing/publishing/1.0.10/information-population/final-checks.json
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/Archi-imported.archimate.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/Archi-no-edit-export.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/archi-transport-verification.json
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-default-config.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-display.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-sandbox.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/Archi-imported.archimate.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/Archi-no-edit-export.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/cli-display.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/raw-comparator.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/raw-comparator.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/verify-transport.py
docs/archimate/processing/publishing/1.0.10/information-population/validation/archimate-tests-initial.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/archimate-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/architecture-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/diagnostic-final.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/diagnostic-final.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/frozen_identity_registry.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/git_whitespace.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/governance-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/history.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/identity.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/independent-reproduction.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/validation/prior-files-preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/validation/publication-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/publication.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/rads.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/release_check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/report-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/serialization.json
docs/archimate/processing/publishing/1.0.10/information-population/verification.json
docs/archimate/processing/publishing/1.0.10/information-population/verify-preservation.py
docs/archimate/processing/publishing/1.0.10/publication-validation-1.0.10.json
docs/markdown/governance/archimate-ingestion-publication-semantics.md
docs/markdown/governance/architecture-modelling-strategy.md
docs/markdown/governance/architecture-release-policy.md
docs/markdown/governance/baseline/information-architecture.json
docs/markdown/governance/canonical-identity-and-naming.md
docs/markdown/governance/initial-information-architecture-projection.md
docs/markdown/governance/rad-archimate-mapping-catalogue.md
docs/markdown/governance/rad-archimate-model-mapping.md
```

## Stop boundary after completion

Element population, authorised canonicalName completion, governed-rule recording,
applicable validation and reporting are complete. No relationship modelling or
IA ingestion implementation was begun. The architect's future containment/Views,
Endpoint adjudication, accepted Business containment debt and human sign-off remain
explicitly outside this completed projection increment.

## Preserved initial execution record — before naming adjudications

The following original text describes the initial population stage. Its source
definitions/exclusions remain applicable; old naming stops and execution hashes
are historical, with the superseded naming assertions explicitly wrapped below.
The exact original report bytes also remain in the completion snapshot.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Initial Information Architecture element population — 2026-10-19

## Outcome and standing

**Deterministic projection completed:** 50 native `BusinessObject` elements and
4 native `DataObject` elements were added to the current RAD-1.0.9 projection.
The resulting publication is [RAD-1.0.10](../../docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml).
No semantic relationship, IA diagram View, diagram node or connection, Harmonia
specialisation, Technology-layer `Artifact`, or Artefact-suffixed element was created.
The existing 1,308 elements, 1,077 relationships and 1,198 Views retain their exact
content and identities. Every pre-existing XML identity is retained, including
organisation and visual-object identities. Four navigation labels change spelling
as detailed below; existing resources and diagrams are not relocated.


------- Legacy Content - Superseded ------- Start ------

Superseded by: [Canonical naming vocabulary](../../docs/markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary) and [cross-layer Composition invariant](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming); see the current adjudication outcome above.

The result has **explicit property stops**: `canonicalName` is absent on all
54 new resources. Endpoint and other ambiguous/candidate populations remain
unresolved. The population is ready for Solution Architect inspection in Archi;
this report makes no human rendering/sign-off claim.

------- Legacy Content - Superseded ------- Finish ----


The following labels apply throughout this report:

- **Governed fact:** meaning, standing or governance explicitly established in
  repository-held RADS, or a decision explicitly supplied in the task.
- **Deterministic projection:** native representation, suffixed Name, immutable
  identity binding and navigation mechanically derived from those inputs.
- **Unresolved ambiguity:** insufficient or conflicting evidence; no invented
  meaning, independent object boundary, type, merger or name is supplied.
- **Solution Architect decision required:** the unresolved semantic input must
  be adjudicated before the affected property or population can proceed.

This is execution evidence under `.junie/reports`, not architectural authority.
The task establishes an initial element projection, not a new Information
Architecture, completeness declaration, IA ingestion contract or reconciliation
workflow. No later relationship-modelling step was commenced.

## Authority and starting point

The explicit **Establish Initial Information Architecture Element Population**
task supplies the native BusinessObject/DataObject mapping, BO/Topic/Artefact
suffix convention, agreed navigation locations, eligibility gates and prohibition
on relationships and physical population. Its bounded decisions are recorded in
[the governed projection contract](../../docs/markdown/governance/initial-information-architecture-projection.md).
No discovery report was used to establish architectural meaning.

Repository authority applied:

- [AGENTS.md](../../AGENTS.md) and [documentation instructions](../../docs/AGENTS.md):
  RADS authority, preserved historical content, bounded verification and release increment.
- [Architectural Axioms](../../docs/markdown/governance/architectural-axioms.md):
  AX-01/04 preserve information meaning independently of machinery; AX-14
  preserves conceptual/logical/physical distinctions and qualified standing;
  AX-17 prohibits resolving absent/conflicting authority through inference.
  AX-05/06 preserve active/durable state and explicit information authority;
  AX-08/09 preserve evidence/transient-state distinctions; AX-13 preserves the
  external boundary; AX-18 prevents coordination from implying domain authority.
  The projection adds no management, persistence or ownership semantics and
  leaves all source qualifications intact. No conflict was identified in the
  admitted element-only population.
- [Architecture Completion Plan](../../docs/markdown/architecture-completion-plan.md):
  progressive source loading and truthful traceability. Domain05's accepted
  candidate Step-B baseline is not final Solution Architecture; this increment
  does not alter the programme position or declare domain completion.
- [Canonical identity and naming](../../docs/markdown/governance/canonical-identity-and-naming.md),
  [modelling strategy](../../docs/markdown/governance/architecture-modelling-strategy.md),
  [mapping](../../docs/markdown/governance/rad-archimate-model-mapping.md),
  [mapping catalogue](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md),
  [publication plan](../../docs/markdown/governance/rad-archimate-publication-plan.md),
  and [ingestion/publication semantics](../../docs/markdown/governance/archimate-ingestion-publication-semantics.md):
  native semantics, source-owned identity, separate Exchange identity, existing
  lexical naming rules and transport checks. Business Home is not generalised.
- [Release policy](../../docs/markdown/governance/architecture-release-policy.md):
  the root authority advances once to `version=1.0.10`,
  `previous_version=1.0.9`. The prior Home-completion exception is specific to
  that earlier change set; it is not a standing exemption for this new task.
  Repeated generation does not increment the release. No commit is claimed.

The supplied [Home-completion README](../../docs/archimate/processing/publishing/1.0.9/home-context-completion/README.md)
was a navigation aid. The origin is the **current complete** RAD-1.0.9 model,
including its subsequent containment/presentation completion, not an earlier
intermediate Home snapshot:

| Evidence | SHA-256 |
| --- | --- |
| Originating `docs/archimate/exchange/publish/1.0.9/Harmonia-Architecture-1.0.9.xml` | `0f53e81f90a1d62974747e262218612dd70956c3eb65ebf3b2eb2b19af6ec529` |
| Published `docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml` | `73a5f4d5356595cfc7dc78926d446923541fe9da36577285f634dcb00ba73094` |
| Source-owned `docs/markdown/governance/baseline/information-architecture.json` | `e021b98318c5e56868c82b65a974551544c822428c8ecd98b4d508df1510d942` |
| Actual no-edit Archi export, uncompressed XML | `efa0c33891e4253d773c3d017c806e0549cc08923d37fae096fd4e7028d92880` |

The complete selected authoritative sources are listed with fingerprints below.
Each element entry identifies its exact source section and definition evidence.
The [capture](../../docs/markdown/governance/baseline/information-architecture.json)
also retains the full source section, exact selected assertion, standing and hash;
the publisher rejects drift in those inputs.

| Authoritative source | SHA-256 |
| --- | --- |
| [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md) | `5c1646a522d91cdc32610c4b29b81159643b7bb439b4e318428798c4e686adc2` |
| [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md) | `53d37ab3061f507341b02042b59cbfbff23875db258efb8ef3467bc3c2c9d605` |
| [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md) | `ea164a82844b8589b11eef33e75957293239aea5362d1ffcb404e1031736cabd` |
| [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md) | `682648f0da0081bcfc7d4b76f0690598476720bdcde1531d50fe917cc39ef24f` |
| [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md) | `2b38456bb9c260cdc4f9113563cf3ba06e80c6ac2db8ead89a7200dc3af1fb39` |
| [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md) | `86df0038ecefcebd6bac4cb7cf58cdc015fa4c5033ad5468ad7511c693d88f47` |
| [docs/markdown/04-information-architecture/information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md) | `db2047ac5a673b00d908241eb0094e32c9856ed75e2be5b43939a5f940d0cc77` |
| [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md) | `2eadaa3191cea51d9aade7acd99d8a8ee0a2cf114d847a3e87790476d531563f` |
| [docs/markdown/04-information-architecture/patterns/metabole.md](../../docs/markdown/04-information-architecture/patterns/metabole.md) | `a37bd64841d0d54542592f0f160a199de10c616b22360b0d88ed0e815cb9f676` |
| [docs/markdown/04-information-architecture/patterns/search-results.md](../../docs/markdown/04-information-architecture/patterns/search-results.md) | `a48f1ff1df015c17c6736860441ab9e026ace4667c39102c904bfe01c0d5c2d8` |
| [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md) | `fdde6724cadd31a13ec5da19ee855ea3043245536b5079c519f8a3b27b98108f` |
| [docs/markdown/05-information-systems-architecture/context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md) | `4d2f4cd1890ad51e74fb87e75749ee6edb233b8e2e4fd28d9f00c852e21dd986` |
| [docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md) | `a397dee9209f308b7a6a9615ce0a46780faa6acc4724c0e537cf9e08894ab69c` |
| [docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md) | `7d8efac8456b8659b1d57b4657e3372c0f6cb161f9b97dc0c5dc48bba1fc7836` |

## Eligibility and retained qualifications

**Governed fact:** each admitted object has a reviewed independent information
meaning and sufficiently established base name and abstraction. The task maps
conceptual information to native BusinessObject and application/logical
information to native DataObject. Neither headings, family membership, FHIR
registers, database structures nor terminology similarity establish eligibility.

**Deterministic projection:** the initial allowlist contains 50 BOs and four
Topics. Their distribution need not be symmetrical. The five healthcare-service
stages are separately defined concepts; the reified Family/Carer/Legal
relationship information objects are elements without relationship edges.
Information Unit, Metabolē and Search Result Set have independent definitions;
their fields, semantic categories and metamodel kinds are not automatically
instantiated as further objects.

**Governed fact with bounded standing:** G2-D01–D04 explicitly approve Episode,
Appointment, Encounter and Referral Outcome; D04 explicitly retains Referral
Progression Event. Only those approved meanings are admitted from that review.
G2 remains open, and A/B/C/D classifications elsewhere in its candidate package
are not approvals. Order BO relies on established Healthcare Service §4, not
approval of the candidate Order family. TaskOutcome's established conceptual
meaning does not settle its deferred runtime identity/lifecycle. Allocating a
model resource canonicalId does not decide a domain instance identity scheme.

**Governed fact with bounded standing:** Context, Pragma, Pylai.Diadromí and
PetasosParcel have separately adjudicated logical snapshot/envelope/carriage/
handoff-information meanings in their source sections. Those specific meanings
are projected as Topics. Domain05 retains its accepted **candidate Step-B
baseline** standing; detailed representation and final Solution Architecture are
not approved by projection. These are not elements inferred from D-register
membership. Journey's express candidate DataObject standing and the FHIR/resource
inventories remain excluded. No existing native FHIR model is specialised or
redefined.

No BO→Topic, Topic→Artefact, name-equivalence, family, ownership, stewardship,
responsibility, containment or other semantic relationship is asserted. Source
documentation may describe established relationships; no corresponding model
edge is added in this task.

## Identity, names and metadata stops

**Deterministic projection:** 54 new opaque random UUIDv4 values were allocated
once as UUID32 `canonicalId` values and persisted on resource records. The
existing identity machinery separately derives/binds their Exchange
`representationId`; ordinary publication allocates no identities. The original
capture and root release snapshot are preserved in the
[allocation evidence](../../docs/archimate/processing/publishing/1.0.10/information-population/allocation/manifest.json).
No IDs were allocated for excluded candidates. All prior canonical IDs, Exchange
IDs, relationships, Views, nodes, connections and frozen EC bindings survive.

`Name == displayName == <Base Name> BO/Topic`. All 54 information-object Names
are model-wide unique. No `harmoniaType` is added. Existing unqualified lexical
rules produce the source-owned local `friendlyName` shown in each entry;
punctuation does not establish qualification or semantic ancestry.


------- Legacy Content - Superseded ------- Start ------

Superseded by: [Canonical naming vocabulary](../../docs/markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary) and [cross-layer Composition invariant](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming); see the current adjudication outcome above.

**Unresolved ambiguity / Solution Architect decision required:** the existing
formal canonical-name vocabulary has no BusinessObject/DataObject qualifier,
and no IA semantic qualification context is established. `canonicalName` is
therefore absent on all 54 new resources, with an explicit stop in each source
record. The task's BO/Topic suffix decision does not implicitly authorise
BO-/TOPIC-/DATA- canonical qualifiers. No IA hierarchy or Home rule is invented.
The diagnostic correctly reports future naming readiness **NONCONFORMANT**.
Existing complete resource metadata remains intact; the stop is not a waiver
of the naming contract. Any future naming rule/context requires architect
adjudication before completing that property.

------- Legacy Content - Superseded ------- Finish ----


## Navigation and Views

**Governed fact:** the task supplies the following View-tree locations:

```text
2-BusinessLayer
    1-BusinessCapabilities
    2-BusinessEnablingCapabilities
    3-BusinessObjectMap
3-ApplicationLayer
    1-InformationMap
    2-EnterpriseCapabilities
    3-ApplicationMap
    4-MiddlewareMap
4-TechnologyLayer
    1-DataMap
```

**Deterministic projection:** new elements are flat in the native model-resource
tree at **Business → BusinessObjectMap** and **Application → InformationMap**.
The native **Technology & Physical → DataMap** is empty. The new
`Views/2-BusinessLayer/3-BusinessObjectMap` and
`Views/4-TechnologyLayer/1-DataMap` folders and the existing
`Views/3-ApplicationLayer/1-InformationMap` are empty workbench locations.
No IA diagram is designed. Folder references are navigation and assert no
semantic grouping or inferred containment.

Only four existing labels change, retaining their folder identities:

| Existing location/label | Result |
| --- | --- |
| Application / Information Map | Application / InformationMap |
| Views / 2-BusinessLayer / 1-Business Capabilities | Views / 2-BusinessLayer / 1-BusinessCapabilities |
| Views / 2-BusinessLayer / 2-Business Enabling Capabilities | Views / 2-BusinessLayer / 2-BusinessEnablingCapabilities |
| Views / 3-ApplicationLayer / 2-Enterprise Capabilities | Views / 3-ApplicationLayer / 2-EnterpriseCapabilities |

Archi requires its native resource category label **Technology & Physical** to
retain the empty DataMap folder on import/export. This importer convention is
distinct from the agreed `Views/4-TechnologyLayer` location and establishes no
new IA semantic rule. No other existing Business, Application or Technology
organisation is changed.

## Every element created

The following is the complete population. Each entry's type, Name, identity
binding and local friendlyName are **deterministic projection**; its quoted
definition and explicitly stated standing are **governed fact with the source's
qualifications**. The source section in full remains in the capture and element

------- Legacy Content - Superseded ------- Start ------

Superseded by: [Canonical naming vocabulary](../../docs/markdown/governance/canonical-identity-and-naming.md#information-object-canonical-name-vocabulary) and [cross-layer Composition invariant](../../docs/markdown/governance/architecture-modelling-strategy.md#cross-layer-semantic-containment-and-contextual-naming); see the current adjudication outcome above.

Documentation. All entries share the explicit `canonicalName` stop above.

------- Legacy Content - Superseded ------- Finish ----


| Native type | Created | New semantic relationships | New IA diagram Views |
| --- | ---: | ---: | ---: |
| BusinessObject | 50 | 0 | 0 |
| DataObject | 4 | 0 | 0 |
| Artifact | 0 | 0 | 0 |

### 01. ActionableTask BO

Native type: `BusinessObject`. Governed Name/displayName: **ActionableTask BO**.

- canonicalId: `c43139289cb649b4836521a96a51604c`
- Exchange representationId: `id-01e5208d1d15549c812dcedd2c0644a7`
- Local friendlyName: `ActionableTaskBo`

Source: [docs/markdown/04-information-architecture/information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md), **2.2 ActionableTask — Identifiable Work Instance**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **ActionableTask** is an identifiable instance of work that can or should be
> undertaken: the particular thing-to-be-done. It is instantiated from and
> governed by an applicable `ActionableTaskArchetype`.

**Source standing / qualifications:** Bounded adjudicated conceptual Task meaning; section 5 reservations, including runtime TaskOutcome identity/lifecycle, remain unresolved.

### 02. ActionableTaskArchetype BO

Native type: `BusinessObject`. Governed Name/displayName: **ActionableTaskArchetype BO**.

- canonicalId: `1d6d4c3a2df440be94262855fa6564eb`
- Exchange representationId: `id-f45454d186e25fd6ac447be05b4a587e`
- Local friendlyName: `ActionableTaskArchetypeBo`

Source: [docs/markdown/04-information-architecture/information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md), **2.1 ActionableTaskArchetype — Reusable Work Definition**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **ActionableTaskArchetype** is the reusable definition/archetype for a kind of
> actionable work. It describes the meaning and constraints of work that may
> subsequently be instantiated. It SHALL NOT be treated as an instance of work.

**Source standing / qualifications:** Bounded adjudicated conceptual Task meaning; section 5 reservations, including runtime TaskOutcome identity/lifecycle, remain unresolved.

### 03. Appointment BO

Native type: `BusinessObject`. Governed Name/displayName: **Appointment BO**.

- canonicalId: `9badf0d360b64215bc60e74eec954018`
- Exchange representationId: `id-7822a5a67faa552e8ad13755e852ef15`
- Local friendlyName: `AppointmentBo`

Source: [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), **G2-D02 — Appointment and Encounter**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **An Appointment may provide the prospective scheduling context for an Encounter. Where an Encounter occurs without a pre-existing Appointment, such as an emergency or walk-in interaction, an Appointment may be created retrospectively to represent the scheduling/administrative context associated with that Encounter.**

**Source standing / qualifications:** Explicitly APPROVED G2 decision only; G2 remains candidate/open and all residual qualifications remain unresolved.

### 04. AssuredHealthcareService BO

Native type: `BusinessObject`. Governed Name/displayName: **AssuredHealthcareService BO**.

- canonicalId: `07a1f83c9b4e462eaef1595c2a50d560`
- Exchange representationId: `id-f8190710c306583ebf7b4f8d879483ee`
- Local friendlyName: `AssuredHealthcareServiceBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **Stage 5: AssuredHealthcareService (Accountability)**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The formal retrospective quality evaluation, clinical audit, accreditation review, or funding/billing reconciliation assessed against delivered services and outcomes.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 05. Availability Schedule BO

Native type: `BusinessObject`. Governed Name/displayName: **Availability Schedule BO**.

- canonicalId: `96638431fc39486d879d080768a68fe9`
- Exchange representationId: `id-765973bf247b582299f7f5f4235189ab`
- Local friendlyName: `AvailabilityScheduleBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **5.3 Formal Semantic Model for Service Provision**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> - **`Availability Schedule`**: The operating hours, intake periods, and appointment booking windows;

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 06. Care-Place Definition BO

Native type: `BusinessObject`. Governed Name/displayName: **Care-Place Definition BO**.

- canonicalId: `779f2295b0b648dda9dbd21c4ade3ba0`
- Exchange representationId: `id-40fba4cb065f52819995aea0bed06a00`
- Local friendlyName: `CarePlaceDefinitionBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md), **3.5 Care-Place Definition**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The static engineering, physical infrastructure, and architectural specification of a bed bay or clinical treatment point.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 07. Carer Relationship BO

Native type: `BusinessObject`. Governed Name/displayName: **Carer Relationship BO**.

- canonicalId: `e95d07cf27c041de955665e7b2af8f52`
- Exchange representationId: `id-6b1053f5a61952e9a97e6d064d3c596a`
- Local friendlyName: `CarerRelationshipBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **5.2 Key Information Relationships**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> | **Carer Relationship** | `Person` (*Carer / Support*) | `Person` (*Care Recipient*) | *Carer Association* | Nominated primary/secondary carer supporting the individual's daily living and care needs. |

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 08. Clinical Privilege BO

Native type: `BusinessObject`. Governed Name/displayName: **Clinical Privilege BO**.

- canonicalId: `75e194f6168945588ed30cc6c78d04cc`
- Exchange representationId: `id-44f337ee36a254d6aa7f29a7341b166a`
- Local friendlyName: `ClinicalPrivilegeBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.5 Clinical Privilege**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An enterprise-approved scope of clinical practice, procedural authorization, admission entitlement, or prescribing privilege granted by a healthcare organisation.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 09. DeliverableHealthcareService BO

Native type: `BusinessObject`. Governed Name/displayName: **DeliverableHealthcareService BO**.

- canonicalId: `8d41e9fb6a164deb93afb5fc4c671e10`
- Exchange representationId: `id-49cd5bc99ffa5fae998d0a2fa6919a50`
- Local friendlyName: `DeliverableHealthcareServiceBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **Stage 2: DeliverableHealthcareService (Contextualisation / Binding)**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The contextual binding of an `OfferedHealthcareService` to a specific providing organisation, delivery location, participating practitioner roles, operational availability schedules, and eligibility rule sets.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 10. Device Association BO

Native type: `BusinessObject`. Governed Name/displayName: **Device Association BO**.

- canonicalId: `303f8586953d450d8d82d3d5f76e488a`
- Exchange representationId: `id-357c6acd043f58708e08b9a9e741814e`
- Local friendlyName: `DeviceAssociationBo`

Source: [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md), **3.3 Device Association**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> A dynamic, time-bounded association binding a `Device Instance` to a patient, physical care-place, or clinical service.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 11. Device Definition BO

Native type: `BusinessObject`. Governed Name/displayName: **Device Definition BO**.

- canonicalId: `2a879d06b17143be8bbfc35cdaab8bdf`
- Exchange representationId: `id-bbe22ad139ae54b0bdd5da53720d942a`
- Local friendlyName: `DeviceDefinitionBo`

Source: [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md), **3.1 Device Definition**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The governed type, manufacturer catalogue model, regulatory classification, and technical specification of a medical device (e.g. *Infusion Pump Model XYZ*, *12-Lead ECG Monitor Model ABC*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 12. Device Instance BO

Native type: `BusinessObject`. Governed Name/displayName: **Device Instance BO**.

- canonicalId: `25c1b751956446cea17065b6254419d5`
- Exchange representationId: `id-af29f5d403b75648a991cca25b699c9c`
- Local friendlyName: `DeviceInstanceBo`

Source: [docs/markdown/04-information-architecture/information-families/device.md](../../docs/markdown/04-information-architecture/information-families/device.md), **3.2 Device Instance**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An identifiable, physical hardware asset manufactured as an instance of a `Device Definition`.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 13. Eligibility Rule Set BO

Native type: `BusinessObject`. Governed Name/displayName: **Eligibility Rule Set BO**.

- canonicalId: `cdb296a83e7e4a17b5e80a9c910c114f`
- Exchange representationId: `id-653db1787e3b5860807c599af6bf2dac`
- Local friendlyName: `EligibilityRuleSetBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **5.3 Formal Semantic Model for Service Provision**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> - **`Eligibility Rule Set`**: The clinical indications, catchment areas, and patient age/acuity constraints.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 14. Encounter BO

Native type: `BusinessObject`. Governed Name/displayName: **Encounter BO**.

- canonicalId: `fb464349f13b41c4ae9920a743fc6c12`
- Exchange representationId: `id-28e7db48ce415ca2a982e0abd38e2d61`
- Local friendlyName: `EncounterBo`

Source: [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), **G2-D03 — Encounter and HealthcareServiceDelivery**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **An Encounter may provide the care-interaction context within which zero or more Healthcare Service Deliveries occur. A Healthcare Service Delivery may occur without an associated Encounter.**

**Source standing / qualifications:** Explicitly APPROVED G2 decision only; G2 remains candidate/open and all residual qualifications remain unresolved.

### 15. Episode BO

Native type: `BusinessObject`. Governed Name/displayName: **Episode BO**.

- canonicalId: `3927d161556d48f4b2cc57bd46ab785d`
- Exchange representationId: `id-76e17ba541cd5d2db988c503af32cfad`
- Local friendlyName: `EpisodeBo`

Source: [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), **G2-D01 — Episode**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Episode is a longitudinal information context representing the treatment of one or more Problems or Conditions across a related series of Encounters, providing a coherent perspective for assurance, workflow and financial purposes.**

**Source standing / qualifications:** Explicitly APPROVED G2 decision only; G2 remains candidate/open and all residual qualifications remain unresolved.

### 16. Family Relationship BO

Native type: `BusinessObject`. Governed Name/displayName: **Family Relationship BO**.

- canonicalId: `8adbce49ffdd46a19e95cf40f2aa36fd`
- Exchange representationId: `id-f0377a823dc25d5f811b7b936d92119e`
- Local friendlyName: `FamilyRelationshipBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **5.2 Key Information Relationships**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> | **Family Relationship** | `Person` (*Family Member*) | `Person` (*Family Member*) | *Familial Association* (e.g. Parent, Child, Sibling, Spouse) | Biological, legal, or social relationship with optional effective period and verification. |

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 17. FulfillmentTask BO

Native type: `BusinessObject`. Governed Name/displayName: **FulfillmentTask BO**.

- canonicalId: `30f4deffcd694f07bf54b2b192695d4f`
- Exchange representationId: `id-9f79c79ae085520499239a4d0d149a2f`
- Local friendlyName: `FulfillmentTaskBo`

Source: [docs/markdown/04-information-architecture/information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md), **2.3 FulfillmentTask — Identifiable Undertaking**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **FulfillmentTask** represents an identifiable undertaking of an
> `ActionableTask`: doing or attempting to do that particular work. It carries
> the execution-related information necessary to represent the undertaking,
> including, where applicable:

**Source standing / qualifications:** Bounded adjudicated conceptual Task meaning; section 5 reservations, including runtime TaskOutcome identity/lifecycle, remain unresolved.

### 18. Geospatial Address BO

Native type: `BusinessObject`. Governed Name/displayName: **Geospatial Address BO**.

- canonicalId: `771b2e814946401bbe6dd6b4fdd3fb42`
- Exchange representationId: `id-74957499ad6e57dd83efe1b68f7c5bce`
- Local friendlyName: `GeospatialAddressBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md), **3.4 Geospatial Address**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The physical address, geospatial coordinates (latitude/longitude), building elevation, and GIS geofence defining the physical location on Earth.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 19. Healthcare Location BO

Native type: `BusinessObject`. Governed Name/displayName: **Healthcare Location BO**.

- canonicalId: `9ee4df4d76864bb1ac90fe1077c56c6f`
- Exchange representationId: `id-2b0f9a56b0d65f3fbc793090ebea0f8d`
- Local friendlyName: `HealthcareLocationBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md), **3.1 Healthcare Location**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An identifiable physical, geospatial, or functional place where healthcare is delivered, managed, or supported.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 20. Healthcare Organisation BO

Native type: `BusinessObject`. Governed Name/displayName: **Healthcare Organisation BO**.

- canonicalId: `dd3ba70128874caeb2d5da419d006c49`
- Exchange representationId: `id-3fbc97f72c185d82a3d1fef758f27eed`
- Local friendlyName: `HealthcareOrganisationBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.1 Healthcare Organisation**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An identifiable legal, corporate, or administrative organisation operating within the healthcare domain (e.g. Health Service District, Hospital Network, Private Healthcare Provider, Diagnostic Laboratory Enterprise).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 21. Healthcare Subject Context BO

Native type: `BusinessObject`. Governed Name/displayName: **Healthcare Subject Context BO**.

- canonicalId: `e92ea15616ea43c9a1d698e34ed1376b`
- Exchange representationId: `id-3676faa2f7715169839dc9ebc08b9faa`
- Local friendlyName: `HealthcareSubjectContextBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.7 Healthcare Subject Context**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The subject-oriented context in which health information, clinical activity, or care delivery concerns a `Person`.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 22. HealthcareServiceDelivery BO

Native type: `BusinessObject`. Governed Name/displayName: **HealthcareServiceDelivery BO**.

- canonicalId: `4627ee33845744f2ae2e10c84e313755`
- Exchange representationId: `id-cf030f3db0f159888df2760c4786786c`
- Local friendlyName: `HealthcareServiceDeliveryBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **Stage 3: HealthcareServiceDelivery (Fulfilment)**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An Information Concept referring to the real-world execution or performance of a healthcare service for a specific target subject (or facility target) at a concrete point in time. It represents information about the activity rather than becoming the activity or implying that Harmonia performs it.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 23. Identifier BO

Native type: `BusinessObject`. Governed Name/displayName: **Identifier BO**.

- canonicalId: `b3141f382ca04bf4bb214c12300ebbfb`
- Exchange representationId: `id-06079d9b16f15f28bb289eaaa18f39a7`
- Local friendlyName: `IdentifierBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.3 Identifier**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> A distinct identifier token issued within a specific namespace authority to identify a person.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 24. Identity Alias BO

Native type: `BusinessObject`. Governed Name/displayName: **Identity Alias BO**.

- canonicalId: `b444496792a24c33b439f9cb59a12d98`
- Exchange representationId: `id-ccd827c077af5f3a9512e6d99f864935`
- Local friendlyName: `IdentityAliasBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.4 Identity Alias**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> A name, title, or alias by which a person is known in a specific legal, cultural, or operational context.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 25. Identity Correction BO

Native type: `BusinessObject`. Governed Name/displayName: **Identity Correction BO**.

- canonicalId: `7c74034576cf4972bbb6e86e0f0e1e58`
- Exchange representationId: `id-87757aeb9c0d5e7db43f5cd4d92a4672`
- Local friendlyName: `IdentityCorrectionBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.6 Identity Correction**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The audited record and managed consequences of source-established identity corrections, including merge, split/unlink outcomes and demographic rectification within established administrative scope. Harmonia does not adjudicate the originating person-merge decision.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 26. Identity Correlation Graph BO

Native type: `BusinessObject`. Governed Name/displayName: **Identity Correlation Graph BO**.

- canonicalId: `8d0a2577d3364dbda96798afe0800200`
- Exchange representationId: `id-ee2ac92f7f905636916a76bea997e247`
- Local friendlyName: `IdentityCorrelationGraphBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.5 Identity Correlation Graph**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The network of qualified associations linking distinct identifiers originating from different authority namespaces to the same underlying `Person`.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 27. Information Unit BO

Native type: `BusinessObject`. Governed Name/displayName: **Information Unit BO**.

- canonicalId: `10284034c8534b639284386b064f2c54`
- Exchange representationId: `id-0525ac7ff42457dd9339e3ad95255296`
- Local friendlyName: `InformationUnitBo`

Source: [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md), **6. Information Unit**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **An Information Unit is the governed boundary within which Harmonia associates a Governed Representation with its Management Context.**

**Source standing / qualifications:** Established conceptual governed management boundary; no downstream schema or representation mapping.

### 28. Legal Representation BO

Native type: `BusinessObject`. Governed Name/displayName: **Legal Representation BO**.

- canonicalId: `33ddad9f1ce1424bbe0fb9a25efadb9f`
- Exchange representationId: `id-fb0ceb5c213759a688d5ea96b749c812`
- Local friendlyName: `LegalRepresentationBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **5.2 Key Information Relationships**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> | **Legal Representation** | `Person` (*Authorised Representative*) | `Person` (*Represented Subject*) | *Legal Mandate* (e.g. Guardian, Appointed Proxy, Substitute Decision Maker) | Verified statutory or judicial mandate specifying scope of legal authority, validity period, and evidence documentation. |

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 29. Location Classification BO

Native type: `BusinessObject`. Governed Name/displayName: **Location Classification BO**.

- canonicalId: `3896c910c28646098f0707efe88169ee`
- Exchange representationId: `id-c16743d7b1fb5172afa5130095798191`
- Local friendlyName: `LocationClassificationBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md), **3.3 Location Classification**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The structural and functional categorisation of the location (e.g. *Campus*, *Building*, *Floor*, *Ward / Wing*, *Patient Room*, *Care-Place / Bed Bay*, *Operating Theatre*, *Consulting Suite*, *Mobile Unit*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 30. Location Identity BO

Native type: `BusinessObject`. Governed Name/displayName: **Location Identity BO**.

- canonicalId: `5f8299e31ee5410583007473c75d578f`
- Exchange representationId: `id-86e72752878753f2b1ab604f303f1fc6`
- Local friendlyName: `LocationIdentityBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-location.md](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md), **3.2 Location Identity & Location Identifier**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The governed set of enterprise and local identifiers that uniquely distinguish the location within an enterprise or regional jurisdiction.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 31. Metabolē BO

Native type: `BusinessObject`. Governed Name/displayName: **Metabolē BO**.

- canonicalId: `c34b4929f71946708d37dbe6c1da920a`
- Exchange representationId: `id-d867f2dd90255f48bd90c1c0f79492a5`
- Local friendlyName: `MetabolēBo`

Source: [docs/markdown/04-information-architecture/patterns/metabole.md](../../docs/markdown/04-information-architecture/patterns/metabole.md), **1. Concept and information boundary**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Metabolē** is a conceptual information construct representing the bounded
> progression of one or more Information Units through a change or activity.
> A Metabolē incorporates the input information, applicable Context and
> provenance/derivation associated with that progression and accumulates the
> resulting output information.

**Source standing / qualifications:** Adjudicated conceptual IA extension, 2026-10-13; no application manager.

### 32. OfferedHealthcareService BO

Native type: `BusinessObject`. Governed Name/displayName: **OfferedHealthcareService BO**.

- canonicalId: `441b0891822f4add834ea930c3fe58b7`
- Exchange representationId: `id-d4f3a0305c8954ebbda80cef33fc4178`
- Local friendlyName: `OfferedHealthcareServiceBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **Stage 1: OfferedHealthcareService (Definition)**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The canonical service offering defined within a healthcare enterprise or jurisdictional catalogue (e.g. *12-Lead Electrocardiography*, *Acute Inpatient Haemodialysis*, *Wound Debridement Protocol*, *Ward Sanitisation Protocol*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 33. Order BO

Native type: `BusinessObject`. Governed Name/displayName: **Order BO**.

- canonicalId: `2a791e0e1f6b45c0a57fb8233f73faa5`
- Exchange representationId: `id-88dc53e48abd506d9d0433e1f46e72e2`
- Local friendlyName: `OrderBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **4. Associated Request Mechanism: Order**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> Harmonia enforces an essential architectural rule regarding clinical requisitions and service delivery:

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 34. Organisation Classification BO

Native type: `BusinessObject`. Governed Name/displayName: **Organisation Classification BO**.

- canonicalId: `227974cf26054bdea17038ecd6c0aa3a`
- Exchange representationId: `id-d18afdbb120f549eb39194e960859797`
- Local friendlyName: `OrganisationClassificationBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.4 Organisation Classification**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The categorisation of the organisation according to its structural, operational, and clinical mandate (e.g. *Tertiary Hospital*, *Regional Health District*, *Ambulatory Primary Care Clinic*, *Specialist Pathology Service*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 35. Organisation Contact Information BO

Native type: `BusinessObject`. Governed Name/displayName: **Organisation Contact Information BO**.

- canonicalId: `cad820b906fa40f28824278fb18b7497`
- Exchange representationId: `id-fa50c7b9750e5f63ac219c4904f2cc08`
- Local friendlyName: `OrganisationContactInformationBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.5 Organisation Contact Information**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The official administrative communication details, registered physical and postal addresses, corporate telephone/electronic points of contact, and nominated official representatives.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 36. Organisation Identity BO

Native type: `BusinessObject`. Governed Name/displayName: **Organisation Identity BO**.

- canonicalId: `c240be16de76415682237a238b3d3fc8`
- Exchange representationId: `id-0e2aac3ee1785338821600bbaed24b5f`
- Local friendlyName: `OrganisationIdentityBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.2 Organisation Identity & Organisation Identifier**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The governed set of official enterprise identifiers, registry bindings, and external directory identifiers establishing the identity of the organisation.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 37. Organisation Profile BO

Native type: `BusinessObject`. Governed Name/displayName: **Organisation Profile BO**.

- canonicalId: `04adbfc449bb4b9e9ae6e7fdf8a66bf9`
- Exchange representationId: `id-7e853ce10d0050a78fcd7b690d505c7f`
- Local friendlyName: `OrganisationProfileBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.3 Organisation Profile**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The verified set of administrative and corporate attributes describing the organisation, including registered legal name, public trading names, corporate registration numbers, and formal accreditation status.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 38. Organisational Unit BO

Native type: `BusinessObject`. Governed Name/displayName: **Organisational Unit BO**.

- canonicalId: `2c884cdd6a4b41a184818e3a7e540897`
- Exchange representationId: `id-bf12d24b62bb595eaa2325a6a7bea26e`
- Local friendlyName: `OrganisationalUnitBo`

Source: [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md), **3.6 Organisational Unit**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An identifiable internal division, clinical directorate, operational department, or specialty unit within a healthcare organisation (e.g. *Division of Surgery*, *Department of Cardiology*, *Pharmacy Department*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 39. Person BO

Native type: `BusinessObject`. Governed Name/displayName: **Person BO**.

- canonicalId: `1163ffac87814f319578ef5cb95dcd4d`
- Exchange representationId: `id-915f3fef43fc5aeeb89bb30650a10fba`
- Local friendlyName: `PersonBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.1 Person**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The identifiable biological and legal human entity.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 40. Person Identity BO

Native type: `BusinessObject`. Governed Name/displayName: **Person Identity BO**.

- canonicalId: `3b72456b5686449f95ad26bfbbae07f3`
- Exchange representationId: `id-4d30e501991f53e1ab718ceae9cc9599`
- Local friendlyName: `PersonIdentityBo`

Source: [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md), **3.2 Person Identity**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The governed set of identity traits, identifiers, names, and demographic assertions that establish the recognised identity of a `Person`.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 41. Practitioner BO

Native type: `BusinessObject`. Governed Name/displayName: **Practitioner BO**.

- canonicalId: `4e8f2bd581a5418889813cf7ee414750`
- Exchange representationId: `id-87934f3b3f1b53c7abb886d8050defe4`
- Local friendlyName: `PractitionerBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.1 Practitioner**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An identifiable individual healthcare workforce or professional entity.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 42. Practitioner Identity BO

Native type: `BusinessObject`. Governed Name/displayName: **Practitioner Identity BO**.

- canonicalId: `2f2868f077fc47afbb541925a9801b2c`
- Exchange representationId: `id-cbbec381b72c5cef85d842142075b459`
- Local friendlyName: `PractitionerIdentityBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.2 Practitioner Identity & Practitioner Identifier**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The governed set of professional identifiers, credential assertions, and demographic traits establishing the professional identity of a `Practitioner`.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 43. Practitioner Role Binding BO

Native type: `BusinessObject`. Governed Name/displayName: **Practitioner Role Binding BO**.

- canonicalId: `cb7ac98f62aa4599ba57263338cb8b3c`
- Exchange representationId: `id-f30e3f1749b054f7b47be51220f516a5`
- Local friendlyName: `PractitionerRoleBindingBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.6 Practitioner Role Binding**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The contextual binding of a `Practitioner` to a specific clinical capacity, service, facility, or department within an organisation (e.g. *Specialist Physician in Cardiology Department*, *On-Call Registrar*).

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 44. Professional Profile BO

Native type: `BusinessObject`. Governed Name/displayName: **Professional Profile BO**.

- canonicalId: `703bb80c062743eeb7ed8dea3ffeaaa0`
- Exchange representationId: `id-9343d10e09a456508da158f89802d1c7`
- Local friendlyName: `ProfessionalProfileBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.4 Professional Profile**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> The verified set of educational qualifications, clinical competencies, certified subspecialties, and academic credentials possessed by a practitioner.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 45. Professional Registration BO

Native type: `BusinessObject`. Governed Name/displayName: **Professional Registration BO**.

- canonicalId: `26c72794993044af98b5329b8b14ae89`
- Exchange representationId: `id-ec9b517a38d45cf2aa9e7539ee9b253d`
- Local friendlyName: `ProfessionalRegistrationBo`

Source: [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md), **3.3 Professional Registration**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> A formal, externally governed assertion attesting that a practitioner is registered with a statutory registration board or regulatory authority.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 46. Referral Outcome BO

Native type: `BusinessObject`. Governed Name/displayName: **Referral Outcome BO**.

- canonicalId: `18ad2e58913b4a938e172df7b03f366a`
- Exchange representationId: `id-8e3f000b84c25352869c1a2d34f5493e`
- Local friendlyName: `ReferralOutcomeBo`

Source: [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), **G2-D04 — Referral Outcome**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Referral Outcome is the governed information concerning what ultimately became of a Referral, sufficient to support assurance, governance and traceability over the Referral and its progression.**

**Source standing / qualifications:** Explicitly APPROVED G2-D04; no promotion of Referral family/package or independent Referral Disposition.

### 47. Referral Progression Event BO

Native type: `BusinessObject`. Governed Name/displayName: **Referral Progression Event BO**.

- canonicalId: `bc70a1055ca34eed8143856816587f67`
- Exchange representationId: `id-33ce6c9ab8dc56d4a49938ccf113346b`
- Local friendlyName: `ReferralProgressionEventBo`

Source: [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md), **G2-D04 — Referral Outcome**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Referral Progression Event:** Retained as historical information concerning significant occurrences in the administrative/business progression of a Referral. Not every Event represents a Referral Outcome, and the historical progression record is not collapsed into the final/current Outcome.

**Source standing / qualifications:** G2-D04 explicitly retains significant historical progression information; historical describes represented occurrences, not historical architectural standing.

### 48. Search Result Set BO

Native type: `BusinessObject`. Governed Name/displayName: **Search Result Set BO**.

- canonicalId: `b683ae87c72a4c16aba948a4b8cb0998`
- Exchange representationId: `id-e10a6048c177527a9214cd063c4588bd`
- Local friendlyName: `SearchResultSetBo`

Source: [docs/markdown/04-information-architecture/patterns/search-results.md](../../docs/markdown/04-information-architecture/patterns/search-results.md), **2. Harmonia-Managed Information Search**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **A search over Harmonia-managed information produces a governed
> metadata-based Search Result Set referencing the Information Unit Versions
> comprising that result. Search does not, by itself, duplicate or create new
> versions of the returned managed information.**

**Source standing / qualifications:** Adjudicated conceptual managed metadata collection; Search Result Metadata-Set is the same concept, not an additional element.

### 49. ServiceOutcome BO

Native type: `BusinessObject`. Governed Name/displayName: **ServiceOutcome BO**.

- canonicalId: `4fb589629e7f405db44d6a87a2b5e36b`
- Exchange representationId: `id-89bfa28c8be251408ed51a17551aa6ab`
- Local friendlyName: `ServiceOutcomeBo`

Source: [docs/markdown/04-information-architecture/information-families/healthcare-service.md](../../docs/markdown/04-information-architecture/information-families/healthcare-service.md), **Stage 4: ServiceOutcome (Outcome)**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> An Information Concept concerning a result, finding, change, consequence or administrative artefact associated with a service delivery. These information kinds remain distinct; representing them does not confer responsibility for their production, clinical interpretation or originating authority.

**Source standing / qualifications:** Established Package1 conceptual information meaning; residual qualifications in selected section remain unchanged.

### 50. TaskOutcome BO

Native type: `BusinessObject`. Governed Name/displayName: **TaskOutcome BO**.

- canonicalId: `08d8ee19a30f418f9d5bc497ced0d1ef`
- Exchange representationId: `id-894142dc29e756d6b00b40201e324409`
- Local friendlyName: `TaskOutcomeBo`

Source: [docs/markdown/04-information-architecture/information-families/task-work.md](../../docs/markdown/04-information-architecture/information-families/task-work.md), **2.4 TaskOutcome — Governed ActionableTask Resolution**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **TaskOutcome is the governed resolution of an ActionableTask according to
> that ActionableTask's explicitly defined outcome policy.**

**Source standing / qualifications:** Bounded adjudicated conceptual Task meaning; section 5 reservations, including runtime TaskOutcome identity/lifecycle, remain unresolved.

### 51. Context Topic

Native type: `DataObject`. Governed Name/displayName: **Context Topic**.

- canonicalId: `76ded4866bf443dabc0849d68b215690`
- Exchange representationId: `id-584ea943d2c25bb4a1d6f381e24b026f`
- Local friendlyName: `ContextTopic`

Source: [docs/markdown/05-information-systems-architecture/context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md), **Context — versioned temporal values**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Context is a versioned, temporal load-snapshot composed from authoritative
> resource values materialised for a Harmonia activity's anticipated information
> requirements.**

**Source standing / qualifications:** Bounded adjudicated application information meaning; Domain05 remains the accepted candidate Step-B baseline, not final Solution Architecture. Detailed representation is deferred; only this defined logical object is projected.

### 52. PetasosParcel Topic

Native type: `DataObject`. Governed Name/displayName: **PetasosParcel Topic**.

- canonicalId: `f0a88f05a0cb438cb42572ec07fb8513`
- Exchange representationId: `id-99b474f57b075557a25ae229f1185ba6`
- Local friendlyName: `PetasosParcelTopic`

Source: [docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md), **Persistent handoff without information ownership**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> Petasos is Harmonia's durable message/event queue-management component.
> It creates, operates and owns the operational state/status of **purpose-defined
> queues** for inter-Ergon flow and other architecturally established communication,
> notification and event sharing. Every queue carries **PetasosParcel by value**;
> associated information is predominantly by reference to governed information
> managed elsewhere. Parcel is conceptually analogous to FHIR Communication;
> literal equivalence and detailed schema are not established.

**Source standing / qualifications:** Bounded adjudicated application information meaning; Domain05 remains the accepted candidate Step-B baseline, not final Solution Architecture. Detailed representation is deferred; only this defined logical object is projected.

### 53. Pragma Topic

Native type: `DataObject`. Governed Name/displayName: **Pragma Topic**.

- canonicalId: `27ad143e7c784e378556af8a57c438f7`
- Exchange representationId: `id-7e466917d880545b949fe8e07a20cb68`
- Local friendlyName: `PragmaTopic`

Source: [docs/markdown/05-information-systems-architecture/context-and-persistence-management.md](../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md), **Pragma and new Content**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Mneme manages Pragma.** Context is the value-based working environment;
> Pragma is the task/activity information envelope through which inputs and outputs
> are expressed. They are related and distinct.

**Source standing / qualifications:** Bounded adjudicated application information meaning; Domain05 remains the accepted candidate Step-B baseline, not final Solution Architecture. Detailed representation is deferred; only this defined logical object is projected.

### 54. Pylai.Diadromí Topic

Native type: `DataObject`. Governed Name/displayName: **Pylai.Diadromí Topic**.

- canonicalId: `978f5c2bd2d343ffaf3a13dff700c258`
- Exchange representationId: `id-d64929ff4825570e8484be3a85e9de53`
- Local friendlyName: `PylaiDiadromíTopic`

Source: [docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md](../../docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md), **Terma and Diadromí**.

Definition evidence excerpt (the full assertion and section are retained in the capture):

> **Pylai.Terma manages the communication stack; the applicable Harmonia
> execution framework manages the application meaning.**

**Source standing / qualifications:** Bounded adjudicated application information meaning; Domain05 remains the accepted candidate Step-B baseline, not final Solution Architecture. Detailed representation is deferred; only this defined logical object is projected.


## Material considered but excluded and remaining decisions

The following inventory is also retained in
[exclusions.json](../../docs/archimate/processing/publishing/1.0.10/information-population/exclusions.json).
Exclusion is a bounded projection outcome; it does not delete source meaning or
declare that the material can never become an IA object. No semantic merger,
distinguishing name, cross-level mapping or type is inferred to admit it.

### Exclusion 01: Endpoint BO (Practitioner interpretation) and Endpoint BO (Device interpretation)

**unresolved ambiguity / Solution Architect decision required.** Independent definitions concern practitioner/role routing coordinates and device telemetry/communication coordinates. Each produces Endpoint BO. Neither semantic merger nor a distinguishing base name is established. Both excluded, neither allocated.

Source evidence:

- [docs/markdown/04-information-architecture/information-families/practitioner.md#37-endpoint](../../docs/markdown/04-information-architecture/information-families/practitioner.md#37-endpoint)
- [docs/markdown/04-information-architecture/information-families/device.md#34-endpoint](../../docs/markdown/04-information-architecture/information-families/device.md#34-endpoint)

### Exclusion 02: Practitioner Identifier, Organisation Identifier, Location Identifier

**unresolved independent-object boundary / Solution Architect decision required.** Combined Identity & Identifier sections define the governed identity bundles and decompose identifier values/authorities; the diagram and definition independently establish Identity. Traceability-list and heading occurrences alone do not settle separate Identifier object boundaries. No split into two concepts and no merger with the Person Identifier object.

Source evidence:

- [docs/markdown/04-information-architecture/information-families/practitioner.md#32-practitioner-identity--practitioner-identifier](../../docs/markdown/04-information-architecture/information-families/practitioner.md#32-practitioner-identity--practitioner-identifier)
- [docs/markdown/04-information-architecture/information-families/organisation.md#32-organisation-identity--organisation-identifier](../../docs/markdown/04-information-architecture/information-families/organisation.md#32-organisation-identity--organisation-identifier)
- [docs/markdown/04-information-architecture/information-families/healthcare-location.md#32-location-identity--location-identifier](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md#32-location-identity--location-identifier)

### Exclusion 03: Support Person Relationship / Support Person

**unresolved base-name/object boundary / Solution Architect decision required.** Traceability lists Support Person Relationship; the reified-relationship table calls its row Support Person; Business Role usages are distinct. Do not choose or invent a base name or conflate a person/role with relationship information.

Source evidence:

- [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md#2-domain-03-responsibility--traceability](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md#2-domain-03-responsibility--traceability)
- [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md#52-key-information-relationships](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md#52-key-information-relationships)

### Exclusion 04: Demographic Trait Assertion, Communication Preference, Identifier Value, Identifier Authority / Namespace, professional qualifications, specialties and other characteristic-level terms

**not independently established objects in the reviewed evidence.** Traceability entries, examples, characteristics or decomposition values do not alone establish independent object identity/base-name boundaries. Their meaning remains in the parent definitions; no automatic element-per-term conversion.

Source evidence:

- [docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md](../../docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md)
- [docs/markdown/04-information-architecture/information-families/practitioner.md](../../docs/markdown/04-information-architecture/information-families/practitioner.md)
- [docs/markdown/04-information-architecture/information-families/organisation.md](../../docs/markdown/04-information-architecture/information-families/organisation.md)

### Exclusion 05: Information Concept, semantic categories, generic Information Relationship/Assembly/View/Collection metamodel kinds; Identity, Metadata, Content, Context, Version, Active Generation, ActivityContext and other partitions/characteristics

**metamodel/pattern or property; not an automatic information-object instance.** The mapping profile explicitly excludes heading-derived instances and automatic partition objects. The independently defined Information Unit, Metabolē and Search Result Set are admitted separately. No generic class or every field/value is instantiated.

Source evidence:

- [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md)
- [docs/markdown/04-information-architecture/patterns/information-relationships.md](../../docs/markdown/04-information-architecture/patterns/information-relationships.md)
- [docs/markdown/04-information-architecture/patterns/metabole.md](../../docs/markdown/04-information-architecture/patterns/metabole.md)
- [docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md)

### Exclusion 06: Governed Representation and Management Context as additional independent BOs

**unresolved independent-object boundary / Solution Architect decision required.** The mapping permits these where separately defined. The reviewed metamodel establishes their conceptual roles within the Information Unit boundary, but does not settle additional independent projection objects rather than representation/management partitions. Do not infer separate identities, name a generic Context BO or map them to application Context.

Source evidence:

- [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#6-information-unit](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#6-information-unit)
- [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#65-context)
- [docs/markdown/governance/rad-archimate-mapping-catalogue.md](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md)

### Exclusion 07: Task Completion Metadata, Task.Output(s), concurrency controls and modes; ReportedTask; Work Order/To Do/Synthetic Task subclasses

**characteristics/deferred or explicitly non-authorised objects.** The four established Task concepts are admitted. Controls and output/metadata partitions do not establish extra independent object types. ReportedTask is expressly unresolved; business work classifications expressly do not create new conceptual subclasses. Runtime TaskOutcome identity/lifecycle questions are not resolved by assigning the architectural model resource a canonicalId.

Source evidence:

- [docs/markdown/04-information-architecture/information-families/task-work.md#2-established-task-concepts](../../docs/markdown/04-information-architecture/information-families/task-work.md#2-established-task-concepts)
- [docs/markdown/04-information-architecture/information-families/task-work.md#5-explicitly-unresolved-architecture](../../docs/markdown/04-information-architecture/information-families/task-work.md#5-explicitly-unresolved-architecture)

### Exclusion 08: Referral and other unapproved G2 A/B/C/D propositions; Referral Context Assembly, Patient Appointment Schedule View, scheduling milestones, Order candidates, supporting-context/control objects and Responsibility Assumption Assertion

**candidate/unresolved; no promotion.** G2 remains open. A denotes direct derivation within a candidate package, not approval. Only G2-D01–D04 explicitly approved conceptual meanings, including D04's retained Referral Progression Event, are admitted. The Order BO comes from established Package1 Healthcare Service section 4, not approval of the candidate Order family.

Source evidence:

- [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md)
- [docs/markdown/04-information-architecture/information-families/referral.md](../../docs/markdown/04-information-architecture/information-families/referral.md)
- [docs/markdown/04-information-architecture/information-families/appointment-scheduling.md](../../docs/markdown/04-information-architecture/information-families/appointment-scheduling.md)
- [docs/markdown/04-information-architecture/information-families/episode-encounter.md](../../docs/markdown/04-information-architecture/information-families/episode-encounter.md)
- [docs/markdown/04-information-architecture/information-families/order.md](../../docs/markdown/04-information-architecture/information-families/order.md)

### Exclusion 09: Referral Disposition, Referral Finalisation, Referral Closure, Episode Context as a duplicate of Episode

**rejected/superseded or expressly not created.** D04 rejects independent Referral Disposition and does not establish workflow Closure/Finalisation concepts. D01 establishes Episode and supersedes the earlier suggestion; no duplicate historical name is projected.

Source evidence:

- [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md#g2-d01](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md#g2-d01)
- [docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md#g2-d04](../../docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md#g2-d04)

### Exclusion 10: Eight illustrative context assemblies/views and family candidate assemblies; care-place operational state, later clinical/operational families and illustrative healthcare types

**candidate, later-family or illustrative material.** Candidate assembly names and their constituent examples do not become approved objects. A separately established Healthcare Subject Context BO remains independent of the candidate assembly of the same base phrase. Operational-state distinction and future family placement do not authorise a larger initial population.

Source evidence:

- [docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md#2-illustrative-candidate-context-assemblies--views](../../docs/markdown/04-information-architecture/assemblies-views/assemblies-and-views.md#2-illustrative-candidate-context-assemblies--views)
- [docs/markdown/04-information-architecture/information-families/healthcare-location.md#5-architectural-invariant-static-definition-vs-operational-state](../../docs/markdown/04-information-architecture/information-families/healthcare-location.md#5-architectural-invariant-static-definition-vs-operational-state)
- [docs/markdown/04-information-architecture/information-families/README.md](../../docs/markdown/04-information-architecture/information-families/README.md)

### Exclusion 11: Practitioner, PractitionerRole, Organization, Location, HealthcareService, Endpoint, Group, Patient, Encounter, ServiceRequest, Observation, DiagnosticReport, Communication, Consent, AuditEvent and Bundle as additional Topic elements

**candidate logical resource references; no promotion from inventories.** D08/D09/D12 are mixed candidate resource/responsibility inventories. Native FHIR names, clinical persistence responsibility and resource-register membership alone do not establish independently identified logical IA objects under this task. No automatic one-resource-one-DataObject conversion; Healthcare Organisation BO does not establish Organization Topic or a mapping to it.

Source evidence:

- [docs/markdown/05-information-systems-architecture/data-and-persistence.md#coupled-logicalapplication-data-objects](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md#coupled-logicalapplication-data-objects)
- [docs/markdown/governance/rad-archimate-mapping-catalogue.md](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md)

### Exclusion 12: Journey Topic; Praxis/control/Pathway objects; Publishing/Subscription/Pattern/Topic/filter definitions; ErgonEvent, ErgonPayload; access/write/persistence carriers; Stimulus/Effector/BehaviourStimulus/BehaviourEffector and assurance roles

**qualified/candidate logical representation or unresolved independent object/type boundary.** Journey is expressly a candidate Application Data Object despite established observed-progression meaning; its standing is not promoted. Mixed control/interface/behaviour, candidate specialisations, metadata constructs and historical carriers need separate boundary/type adjudication. Register D/C/F keys are navigation, not identities. The admitted Context/Pragma/Diadromí/PetasosParcel objects have bounded adjudicated carriage/snapshot/information meanings, without claiming final schema or Solution Architecture.

Source evidence:

- [docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md#journey-common-observation-and-assurance-subject](../../docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md#journey-common-observation-and-assurance-subject)
- [docs/markdown/05-information-systems-architecture/data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md)
- [docs/markdown/governance/rad-archimate-mapping-catalogue.md](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md)

### Exclusion 13: Criterion, AuthorisationRole, PEP-Criteria, PEP-Context, PractitionerCapacity, authorisation ValueSets and configuration/dataset/security content as additional Topics

**qualified/candidate logical allocation; excluded from the deterministic initial allowlist.** Preserve established security and nomenclature meaning without treating the working DesignNotes, D03/D16 bundles or mapping's conditional DataObject eligibility as accepted independent IA allocation. No object-per-dimension/ValueSet or inferred persistent security-context object is created. A later bounded source/representation review is required.

Source evidence:

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md](../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md)
- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Themis-Authorisation-Model-and-Distributed-Policy-Enforcement.md](../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Themis-Authorisation-Model-and-Distributed-Policy-Enforcement.md)
- [docs/markdown/05-information-systems-architecture/data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md)
- [docs/markdown/governance/rad-archimate-mapping-catalogue.md](../../docs/markdown/governance/rad-archimate-mapping-catalogue.md)

### Exclusion 14: OperationResourceEntity, caches, database/schema/table examples, JPA/persistence structures, archived legacy physical material and every Artefact/Artifact

**historical/implementation or physical population outside scope.** Persistence does not define information meaning. Legacy architectural content is not authoritative merely because implemented. DataMap remains empty; no Technology-layer Artifact, physical information object or Artefact-suffixed element is created. No legacy migration is performed.

Source evidence:

- [docs/markdown/05-information-systems-architecture/data-and-persistence.md](../../docs/markdown/05-information-systems-architecture/data-and-persistence.md)
- [docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#1-foundational-semantic-distinction](../../docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md#1-foundational-semantic-distinction)


The immediate **Solution Architect decisions required** are the two Endpoint
interpretations and their colliding Name; the independent Identifier boundaries;
the Support Person/relationship base name and boundary; whether the generic
Governed Representation/Management Context roles establish additional independent
objects; and applicable canonical-name vocabulary/context. Other excluded bundles
require a separately authorised source/representation review before projection.
No broad new design is proposed here. Candidate G2 and logical-resource material
must retain its standing until separately adjudicated.

## Validation and round-trip evidence

The [verification manifest](../../docs/archimate/processing/publishing/1.0.10/information-population/verification.json)
records executed commands, statuses and hash-pinned logs. Logs are preserved as
gzip files so the repository's global `*.log` ignore rule does not discard them.
The final check/reproduction manifest is separate, avoiding a circular dependency
between generated reports and their own verification evidence.

| Executed check | Result and scope |
| --- | --- |
| Full ArchiMate/framework suite | **PASS**, 214 tests in 307.228s, bounded at 420s. |
| Governance Python suite | **PASS**, 72 tests in 31.430s, bounded at 120s. |
| Required Maven architecture suite | **PASS**, 90 tests, BUILD SUCCESS in 27.839s; offline dependencies and JDK 21, bounded at 180s. |
| Open Exchange bundled XSD / identifier and reference checks | **PASS**, publication and focused tests. |
| Prior model preservation / exact population census | **PASS**: 1,308 original elements, 1,077 relationships and 1,198 Views exact; every pre-existing XML identity retained; only 50 BusinessObjects and 4 DataObjects added. |
| Information Names / forbidden populations | **PASS**: all 54 Names unique; no IA edge endpoints, Artifact, specialisation or ambiguous/candidate promotion beyond the qualified reviewed allowlist. |
| Governed metadata / final-model diagnostic | **PASS** identity audit: 1,362 unique valid UUID32 canonicalIds, no missing/malformed/multiple/duplicate claims or binding conflicts. Future naming readiness remains **NONCONFORMANT** from the 54 explicit canonicalName stops. |
| RADS source validation | **PASS**, established 2,467 unresolved governance items remain unchanged. |
| Frozen identity registry | **PASS**, 14 prior EC bindings retained. |
| Historical governance evidence verifier | **PASS**, prior history is reproduced against original source-release snapshots, with normal patch continuation recorded separately. |
| Release authority check | **PASS**, root 1.0.10 / predecessor 1.0.9, unchanged by generation. |
| XML serialization no-edit round trip | **EQUIVALENT** under the existing comparator. |
| Actual Archi 5.10 no-edit full-model transport | Scoped contract probe **PASS**; raw comparator **DIFFERENCES**, detailed below. |
| Publication `--check` | **PASS**, final published SHA-256 reproduced. |
| Report/inspection-guide `--check` | **PASS**, generated reports exact. |
| Independent-process reproduction | **PASS**, all 15 generated outputs byte-for-byte identical; release authority unchanged. |
| Prior-file hash census | **PASS**, 726 existing files checked; 714 exact, 12 authorised framework/index modifications. Prior publications, captures and evidence remain byte identical. |
| `git diff --check` | **PASS**. |

Exact final-model diagnostic and checks are retained in
[diagnostic-final.json.gz](../../docs/archimate/processing/publishing/1.0.10/information-population/validation/diagnostic-final.json.gz),
[final-checks.json](../../docs/archimate/processing/publishing/1.0.10/information-population/final-checks.json),
[prior-files-preservation.json](../../docs/archimate/processing/publishing/1.0.10/information-population/validation/prior-files-preservation.json),
and [preservation.json](../../docs/archimate/processing/publishing/1.0.10/information-population/validation/preservation.json).

The required Java command actually executed was:

```bash
timeout 180s env JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64 \
  mvn -o test -pl paradeigma/paradeigma-test -am \
  '-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false
```

**Archi no-edit transport:** installed Archi 5.10 imported the complete model,
saved a native model and exported complete Open Exchange without architecture
edits. Execution used isolated configuration/workspace directories under `/tmp`.
The saved native model, no-edit export, CLI logs, unmodified comparator findings
and independent transport probe are retained in
[roundtrip/](../../docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/archi-transport-verification.json).

The transport probe positively establishes exact preservation of all 1,362
canonicalIds, Names/displayNames, friendlyNames and native-type Properties;
the 1,308 existing canonicalNames and the 54 intentional canonicalName absences;
native non-property element/relationship content; existing View identities,
geometry, membership and connections; and resource/View navigation including
empty IA locations. Existing 179 harmoniaType values remain exact; none is added
to the new elements.

The existing Increment 1 raw comparator still reports **DIFFERENCES**:

| Classification | Findings |
| --- | ---: |
| INFORMATION LOSS | 1,819 |
| SEMANTIC DIFFERENCE | 1,669 |
| SEMANTICALLY EQUIVALENT NORMALISATION | 65 |
| UNEXPLAINED DIFFERENCE | 4,871 |
| PRESENTATION-PRESERVING TRANSFORMATION | 0 |

The exact multiset of all 1,669 semantic findings equals the originating
RAD-1.0.9 containment-completion transport findings. The comparator was not
weakened or given a new normalisation. These retained text/property-definition/
organisation and related transport differences preclude a claim of whole-model
lossless equivalence. The scoped transport contracts pass; raw comparator
differences remain visible. This is not architect-authored IA ingestion,
reconciliation or relationship-round-trip proof. Human rendering review and
that later evidence remain pending.

Initial attempts are preserved rather than described as final passes:

- The initial 214-test ArchiMate run had three historical Home-audit evidence-path
  errors after the release advanced. The historical locator was corrected to
  read its recorded originating release. The final full 214-test run passes.
- An initial preservation probe had an XML namespace-prefix harness error;
  its native-type check was corrected. No production architecture changed.
- Initial Archi CLI attempts encountered an unwritable default configuration
  and absent sandbox display access. An isolated display-enabled execution
  subsequently succeeded. Automatic review approved the local display access;
  no user permission request or rejection occurred.
- The first completed Archi probe omitted empty Technology/DataMap because its
  root label was not the native category label. Its evidence remains under
  `roundtrip/initial-native-label/`. The final native-label correction preserves
  every empty map and all other folders.

All potentially blocking commands had explicit bounds and returned control.
There was no TIMEOUT or STALLED final verification. No unrelated architecture,
presentation or historical transport finding was corrected.

## Implementation and canonical documentation assessment

The new source-owned capture is the deterministic allowlist and persistent
identity/name/evidence input. The new projection tool replays it after the full
existing RAD-1.0.9 reconstruction and refuses any change to protected semantics,
metadata or Views. It rejects source drift, collisions, unapproved types, edges,
Artifacts and guessed canonicalName values. It allocates no identities. The
capture is a selection/projection input, not independent authority for redesigning
the source concepts.

Existing publication, metadata, reporting and diagnostic entry points recognise
the bounded overlay and property stop. Historical Home/presentation evidence is
read using its own recorded release, preserving the prior evidence unchanged.
New focused tests cover forbidden edges/types, protected model content,
navigation, ambiguity rejection and publication with identity allocation disabled.
The full test command remains bounded; its 420-second ceiling accommodates the
observed 307-second passing suite. Carry-forward beyond this initial release
explicitly requires review; no future IA workflow is silently established.

**Canonical documentation assessment:** the new governed projection contract
records only the task's authorised mapping/naming/navigation decisions and stops.
The existing conceptual and application source definitions are unchanged. No
legacy architectural meaning was migrated into RADS, so no supersession wrapper
was required. Candidate, historical and unresolved source material retains its
standing. No programme/domain completion status is advanced by this publication.

## Exact files changed

Paths below are repository-relative and include this report, generated outputs,
allocation history, validation and retained initial/final transport evidence.
No existing historical publication, ingestion evidence or source capture is
rewritten. The prior-file hash census checks 726 existing files: 714 are byte
identical and 12 have the explicitly authorised framework/index modifications
listed below; the root release authority is separately incremented.

Modified existing files:

```text
architecture-release.json
docs/archimate/exchange/publish/README.md
docs/archimate/framework/Makefile
docs/archimate/framework/README.md
docs/archimate/framework/tests/test_roundtrip.py
docs/archimate/framework/tests/test_view_presentation.py
docs/archimate/framework/tools/diagnose_roundtrip.py
docs/archimate/framework/tools/governed_metadata.py
docs/archimate/framework/tools/home_context.py
docs/archimate/framework/tools/report_baseline.py
docs/archimate/framework/tools/synchronise_baseline.py
docs/archimate/framework/tools/verify_governance_evidence.py
docs/archimate/framework/tools/view_presentation.py
```

Added files:

```text
.junie/reports/2026-10-19-initial-information-architecture-population.md
docs/archimate/exchange/publish/1.0.10/Harmonia-Architecture-1.0.10.xml
docs/archimate/framework/tests/test_information_population.py
docs/archimate/framework/tools/information_population.py
docs/archimate/processing/publishing/1.0.10/archi-inspection-guide.md
docs/archimate/processing/publishing/1.0.10/information-population.md
docs/archimate/processing/publishing/1.0.10/information-population/allocation/information-architecture.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/allocation/manifest.json
docs/archimate/processing/publishing/1.0.10/information-population/allocation/prior-release.json
docs/archimate/processing/publishing/1.0.10/information-population/before-files.json
docs/archimate/processing/publishing/1.0.10/information-population/exclusions.json
docs/archimate/processing/publishing/1.0.10/information-population/final-checks.json
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/Archi-imported.archimate.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/Archi-no-edit-export.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/archi-transport-verification.json
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-default-config.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-display.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/cli-sandbox.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/Archi-imported.archimate.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/Archi-no-edit-export.xml.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/cli-display.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/initial-native-label/raw-comparator.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/raw-comparator.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/roundtrip/verify-transport.py
docs/archimate/processing/publishing/1.0.10/information-population/validation/archimate-tests-initial.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/archimate-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/architecture-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/diagnostic-final.json.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/diagnostic-final.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/frozen_identity_registry.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/git_whitespace.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/governance-tests.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/history.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/identity.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/independent-reproduction.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/validation/prior-files-preservation.json
docs/archimate/processing/publishing/1.0.10/information-population/validation/publication-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/publication.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/rads.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/release_check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/report-check.log.gz
docs/archimate/processing/publishing/1.0.10/information-population/validation/serialization.json
docs/archimate/processing/publishing/1.0.10/information-population/verification.json
docs/archimate/processing/publishing/1.0.10/information-population/verify-preservation.py
docs/archimate/processing/publishing/1.0.10/publication-validation-1.0.10.json
docs/markdown/governance/baseline/information-architecture.json
docs/markdown/governance/initial-information-architecture-projection.md
```

## Handoff and stop boundary

The Solution Architect can import the published RAD-1.0.10 Exchange model into
Archi and locate the new elements in BusinessObjectMap and InformationMap. The
[inspection guide](../../docs/archimate/processing/publishing/1.0.10/archi-inspection-guide.md)
describes the empty workbench locations and pending decisions.

**Confirmed:** zero IA semantic relationships were created; every existing
relationship is unchanged. Zero Technology/Artefact population was created;
DataMap remains empty. No inferred BO↔Topic equivalence, Home, ownership,
family/grouping or semantic containment was introduced.

**Solution Architect work remains:** inspect the population, adjudicate the
reported ambiguity/property stops, and author/review IA organisation,
relationships and Views. That separately authored model must supply future
ingestion, reconciliation, publication and round-trip evidence. This task stops
at the validated initial element population and report; it does not proceed
into relationship modelling.
