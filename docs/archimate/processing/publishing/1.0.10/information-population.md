<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.0.10 — initial Information Architecture elements

Deterministic projection: **50 BusinessObjects, 4 DataObjects, 0 Artifacts**.
All 1,308 prior elements, 1,077 relationships and 1,198 Views retain exact content
and identities. No IA semantic relationships or diagram Views were created.
The originating RAD-1.0.9 model remains unchanged at SHA-256
`0f53e81f90a1d62974747e262218612dd70956c3eb65ebf3b2eb2b19af6ec529`.
Current model SHA-256: `b1036f745e8ae3754951cd123b2b1a54a7eaa76c5fa83933abbd11ff2d4153bc`.

The [complete task report](../../../../../.junie/reports/2026-10-19-initial-information-architecture-population.md)
records source authority, exclusions, collisions, decisions still required and
exact changed files. The [governed projection contract](../../../../markdown/governance/initial-information-architecture-projection.md)
records the bounded task decision. Source assertions, standing, source fingerprints,
and immutable resource-owned identities are persisted in
[the capture](../../../../markdown/governance/baseline/information-architecture.json).

The Solution Architect's BO/TOPIC vocabulary adjudication completes canonicalName
on all 54 IA resources using the unchanged local friendlyName components. No
Artifact/ARTEFACT qualifier is authorised. RAD-1.0.10 remains the same unsigned
candidate; the pre-adjudication source and evidence remain preserved in
[completion history](information-population/canonical-name-completion/manifest.json).
Folders supply no semantic ancestry.
Names carry BO/Topic suffixes, without cross-level equivalence. Source-specific
qualifications survive in Documentation; the Domain05 candidate baseline is not
promoted to final Solution Architecture. Human rendering review and IA ingestion,
reconciliation and architect-authored relationship round trips remain outstanding.

| Executed verification | Result |
| --- | --- |
| archi_transport | PASS |
| archimate_tests | PASS |
| architecture_tests | PASS |
| final_model_diagnostic | PASS |
| governance_tests | PASS |
| history | PASS |
| identity | PASS |
| preservation | PASS |
| publication | PASS |
| rads | PASS |
| serialization_roundtrip | EQUIVALENT |

| Native type | Governed Name | canonicalId | canonicalName | Source / selected section |
| --- | --- | --- | --- | --- |
| BusinessObject | ActionableTask BO | c43139289cb649b4836521a96a51604c | BO-ActionableTaskBo | docs/markdown/04-information-architecture/information-families/task-work.md — ### 2.2 ActionableTask — Identifiable Work Instance |
| BusinessObject | ActionableTaskArchetype BO | 1d6d4c3a2df440be94262855fa6564eb | BO-ActionableTaskArchetypeBo | docs/markdown/04-information-architecture/information-families/task-work.md — ### 2.1 ActionableTaskArchetype — Reusable Work Definition |
| BusinessObject | Appointment BO | 9badf0d360b64215bc60e74eec954018 | BO-AppointmentBo | docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md — ### G2-D02 — Appointment and Encounter |
| BusinessObject | AssuredHealthcareService BO | 07a1f83c9b4e462eaef1595c2a50d560 | BO-AssuredHealthcareServiceBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### Stage 5: AssuredHealthcareService (Accountability) |
| BusinessObject | Availability Schedule BO | 96638431fc39486d879d080768a68fe9 | BO-AvailabilityScheduleBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### 5.3 Formal Semantic Model for Service Provision |
| BusinessObject | Care-Place Definition BO | 779f2295b0b648dda9dbd21c4ade3ba0 | BO-CarePlaceDefinitionBo | docs/markdown/04-information-architecture/information-families/healthcare-location.md — ### 3.5 Care-Place Definition |
| BusinessObject | Carer Relationship BO | e95d07cf27c041de955665e7b2af8f52 | BO-CarerRelationshipBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 5.2 Key Information Relationships |
| BusinessObject | Clinical Privilege BO | 75e194f6168945588ed30cc6c78d04cc | BO-ClinicalPrivilegeBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.5 Clinical Privilege |
| BusinessObject | DeliverableHealthcareService BO | 8d41e9fb6a164deb93afb5fc4c671e10 | BO-DeliverableHealthcareServiceBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### Stage 2: DeliverableHealthcareService (Contextualisation / Binding) |
| BusinessObject | Device Association BO | 303f8586953d450d8d82d3d5f76e488a | BO-DeviceAssociationBo | docs/markdown/04-information-architecture/information-families/device.md — ### 3.3 Device Association |
| BusinessObject | Device Definition BO | 2a879d06b17143be8bbfc35cdaab8bdf | BO-DeviceDefinitionBo | docs/markdown/04-information-architecture/information-families/device.md — ### 3.1 Device Definition |
| BusinessObject | Device Instance BO | 25c1b751956446cea17065b6254419d5 | BO-DeviceInstanceBo | docs/markdown/04-information-architecture/information-families/device.md — ### 3.2 Device Instance |
| BusinessObject | Eligibility Rule Set BO | cdb296a83e7e4a17b5e80a9c910c114f | BO-EligibilityRuleSetBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### 5.3 Formal Semantic Model for Service Provision |
| BusinessObject | Encounter BO | fb464349f13b41c4ae9920a743fc6c12 | BO-EncounterBo | docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md — ### G2-D03 — Encounter and HealthcareServiceDelivery |
| BusinessObject | Episode BO | 3927d161556d48f4b2cc57bd46ab785d | BO-EpisodeBo | docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md — ### G2-D01 — Episode |
| BusinessObject | Family Relationship BO | 8adbce49ffdd46a19e95cf40f2aa36fd | BO-FamilyRelationshipBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 5.2 Key Information Relationships |
| BusinessObject | FulfillmentTask BO | 30f4deffcd694f07bf54b2b192695d4f | BO-FulfillmentTaskBo | docs/markdown/04-information-architecture/information-families/task-work.md — ### 2.3 FulfillmentTask — Identifiable Undertaking |
| BusinessObject | Geospatial Address BO | 771b2e814946401bbe6dd6b4fdd3fb42 | BO-GeospatialAddressBo | docs/markdown/04-information-architecture/information-families/healthcare-location.md — ### 3.4 Geospatial Address |
| BusinessObject | Healthcare Location BO | 9ee4df4d76864bb1ac90fe1077c56c6f | BO-HealthcareLocationBo | docs/markdown/04-information-architecture/information-families/healthcare-location.md — ### 3.1 Healthcare Location |
| BusinessObject | Healthcare Organisation BO | dd3ba70128874caeb2d5da419d006c49 | BO-HealthcareOrganisationBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.1 Healthcare Organisation |
| BusinessObject | Healthcare Subject Context BO | e92ea15616ea43c9a1d698e34ed1376b | BO-HealthcareSubjectContextBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.7 Healthcare Subject Context |
| BusinessObject | HealthcareServiceDelivery BO | 4627ee33845744f2ae2e10c84e313755 | BO-HealthcareServiceDeliveryBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### Stage 3: HealthcareServiceDelivery (Fulfilment) |
| BusinessObject | Identifier BO | b3141f382ca04bf4bb214c12300ebbfb | BO-IdentifierBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.3 Identifier |
| BusinessObject | Identity Alias BO | b444496792a24c33b439f9cb59a12d98 | BO-IdentityAliasBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.4 Identity Alias |
| BusinessObject | Identity Correction BO | 7c74034576cf4972bbb6e86e0f0e1e58 | BO-IdentityCorrectionBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.6 Identity Correction |
| BusinessObject | Identity Correlation Graph BO | 8d0a2577d3364dbda96798afe0800200 | BO-IdentityCorrelationGraphBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.5 Identity Correlation Graph |
| BusinessObject | Information Unit BO | 10284034c8534b639284386b064f2c54 | BO-InformationUnitBo | docs/markdown/04-information-architecture/metamodel/information-architecture-metamodel.md — ## 6. Information Unit |
| BusinessObject | Legal Representation BO | 33ddad9f1ce1424bbe0fb9a25efadb9f | BO-LegalRepresentationBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 5.2 Key Information Relationships |
| BusinessObject | Location Classification BO | 3896c910c28646098f0707efe88169ee | BO-LocationClassificationBo | docs/markdown/04-information-architecture/information-families/healthcare-location.md — ### 3.3 Location Classification |
| BusinessObject | Location Identity BO | 5f8299e31ee5410583007473c75d578f | BO-LocationIdentityBo | docs/markdown/04-information-architecture/information-families/healthcare-location.md — ### 3.2 Location Identity & Location Identifier |
| BusinessObject | Metabolē BO | c34b4929f71946708d37dbe6c1da920a | BO-MetabolēBo | docs/markdown/04-information-architecture/patterns/metabole.md — ## 1. Concept and information boundary |
| BusinessObject | OfferedHealthcareService BO | 441b0891822f4add834ea930c3fe58b7 | BO-OfferedHealthcareServiceBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### Stage 1: OfferedHealthcareService (Definition) |
| BusinessObject | Order BO | 2a791e0e1f6b45c0a57fb8233f73faa5 | BO-OrderBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ## 4. Associated Request Mechanism: Order |
| BusinessObject | Organisation Classification BO | 227974cf26054bdea17038ecd6c0aa3a | BO-OrganisationClassificationBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.4 Organisation Classification |
| BusinessObject | Organisation Contact Information BO | cad820b906fa40f28824278fb18b7497 | BO-OrganisationContactInformationBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.5 Organisation Contact Information |
| BusinessObject | Organisation Identity BO | c240be16de76415682237a238b3d3fc8 | BO-OrganisationIdentityBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.2 Organisation Identity & Organisation Identifier |
| BusinessObject | Organisation Profile BO | 04adbfc449bb4b9e9ae6e7fdf8a66bf9 | BO-OrganisationProfileBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.3 Organisation Profile |
| BusinessObject | Organisational Unit BO | 2c884cdd6a4b41a184818e3a7e540897 | BO-OrganisationalUnitBo | docs/markdown/04-information-architecture/information-families/organisation.md — ### 3.6 Organisational Unit |
| BusinessObject | Person BO | 1163ffac87814f319578ef5cb95dcd4d | BO-PersonBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.1 Person |
| BusinessObject | Person Identity BO | 3b72456b5686449f95ad26bfbbae07f3 | BO-PersonIdentityBo | docs/markdown/04-information-architecture/information-families/person-healthcare-subject.md — ### 3.2 Person Identity |
| BusinessObject | Practitioner BO | 4e8f2bd581a5418889813cf7ee414750 | BO-PractitionerBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.1 Practitioner |
| BusinessObject | Practitioner Identity BO | 2f2868f077fc47afbb541925a9801b2c | BO-PractitionerIdentityBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.2 Practitioner Identity & Practitioner Identifier |
| BusinessObject | Practitioner Role Binding BO | cb7ac98f62aa4599ba57263338cb8b3c | BO-PractitionerRoleBindingBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.6 Practitioner Role Binding |
| BusinessObject | Professional Profile BO | 703bb80c062743eeb7ed8dea3ffeaaa0 | BO-ProfessionalProfileBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.4 Professional Profile |
| BusinessObject | Professional Registration BO | 26c72794993044af98b5329b8b14ae89 | BO-ProfessionalRegistrationBo | docs/markdown/04-information-architecture/information-families/practitioner.md — ### 3.3 Professional Registration |
| BusinessObject | Referral Outcome BO | 18ad2e58913b4a938e172df7b03f366a | BO-ReferralOutcomeBo | docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md — ### G2-D04 — Referral Outcome |
| BusinessObject | Referral Progression Event BO | bc70a1055ca34eed8143856816587f67 | BO-ReferralProgressionEventBo | docs/markdown/04-information-architecture/reviews/package2-g2-block1-review.md — ### G2-D04 — Referral Outcome |
| BusinessObject | Search Result Set BO | b683ae87c72a4c16aba948a4b8cb0998 | BO-SearchResultSetBo | docs/markdown/04-information-architecture/patterns/search-results.md — ## 2. Harmonia-Managed Information Search |
| BusinessObject | ServiceOutcome BO | 4fb589629e7f405db44d6a87a2b5e36b | BO-ServiceOutcomeBo | docs/markdown/04-information-architecture/information-families/healthcare-service.md — ### Stage 4: ServiceOutcome (Outcome) |
| BusinessObject | TaskOutcome BO | 08d8ee19a30f418f9d5bc497ced0d1ef | BO-TaskOutcomeBo | docs/markdown/04-information-architecture/information-families/task-work.md — ### 2.4 TaskOutcome — Governed ActionableTask Resolution |
| DataObject | Context Topic | 76ded4866bf443dabc0849d68b215690 | TOPIC-ContextTopic | docs/markdown/05-information-systems-architecture/context-and-persistence-management.md — ## Context — versioned temporal values |
| DataObject | PetasosParcel Topic | f0a88f05a0cb438cb42572ec07fb8513 | TOPIC-PetasosParcelTopic | docs/markdown/05-information-systems-architecture/pathways-journeys-and-handoff.md — ## Persistent handoff without information ownership |
| DataObject | Pragma Topic | 27ad143e7c784e378556af8a57c438f7 | TOPIC-PragmaTopic | docs/markdown/05-information-systems-architecture/context-and-persistence-management.md — ## Pragma and new Content |
| DataObject | Pylai.Diadromí Topic | 978f5c2bd2d343ffaf3a13dff700c258 | TOPIC-PylaiDiadromíTopic | docs/markdown/05-information-systems-architecture/pylai-external-interoperability.md — ## Terma and Diadromí |
