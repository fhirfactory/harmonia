<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Implementation Review Corrections — Report

Report-Produced-At: UNRESOLVED. The attributable system timestamp for completed report emission is unavailable at sufficient precision. No filename timestamp is inferred.

**Task** · [Implementation-review corrections Task Details][task]  
**Target** · `software-0.1.0` · HCRL iteration `011`  
**HCRL** · [Pending review record][hcrl]  
**Candidate** · [Corrected patch][patch] · SHA-256 `0865131541da822c159a7468334c522edb7cc284af18322bd346f9e32797874d` ([checksum file][patch-sha])  
**Disposition** · Implementation candidate is ready for Solution Architect review. HCRL 011 remains pending; this report does not accept the implementation or allocate a release.

## Result

Applied the accepted implementation-review corrections while preserving the previous InformationUnit candidate and Naming behavior wherever unaffected. Updated the implementation, focused tests and educational documentation, and created successor versions of the DRP and Backlog Item. The earlier Draft 002.004 and Backlog Item 0005 revision 000.001 remain unchanged and are preserved as historical sources.

## Implemented contracts

- **Managed Revision:** `InformationMetadata.forCoordinateRevision` and `InformationUnit.reviseCoordinates` now retain the originating Managed Revision as the expected revision for the next governed write. Mnemosyne establishes the successor after successful persistence; no persistence integration is implemented here.
- **Information authority:** required `InformationMetadata.informationAuthority : Topic` remains independent of `InformationQuality`. `InformationMetadata.forAuthorityRevision` and `InformationUnit.reviseInformationAuthority` create a new same-UUID snapshot when the versioned authority Topic changes; prior snapshots remain unchanged and the expected Managed Revision is retained. An identical versioned Topic is idempotent.
- **Security boundary:** `SecurityMetadata` remains immutable and retains only structural checks. `withAdded`, `withRemoved` and `InformationMetadata.withSecurity` construct values/proposals without an add-only rule or policy classification. Themis-Krites is documented as the authority for adjudicating strengthening, weakening and replacement. No Krites service, security-policy engine or JPMS was introduced.
- **Media types:** `ContainerMetadata.containerMediaType` and `ContentMetadata.contentMediaType` are required strings validated as case-insensitive IANA-style media type identifiers and stored in normalized lowercase. They remain separate from content concepts and schema Topics. Media types no longer produce content-concept Topics.
- **Vocabulary:** `InformationVocabulary.rawContainerSchema()` uses exactly `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.InformationContainer=RAW`; RAW means no additional Calliope container structure. All 146 `FhirR4ResourceType` values build `Domain=Health.Owner=HealthLevelSevenInternational.Standard=FHIR.Release:4.0.1=R4.Resource=<name>`; `R4` is the release designation, `4.0.1` the version, and resource entries are unversioned. The common containment hierarchy is `Domain → Owner → Standard`, with subordinate containment and versioning defined by each standard.
- **Documents:** `calliope/README.md`, package/class Javadocs, Draft 002.005 and Backlog Item 0005 revision 000.002 describe these contracts. Draft 002.004 and Backlog Item revision 000.001 were not edited.

## Implementation Guide Conformance

### Functional correctness

Focused tests cover the changed public contracts:

- `InformationUnitRevisionTest.coordinateChangeCreatesANewRevisionAndPreservesTheEarlierCoordinates` verifies same-UUID coordinate snapshots retain the origin revision and the expected revision can be advanced by the persistence-boundary value update.
- `InformationUnitRevisionTest.authorityChangeCreatesANewSnapshotUnderTheSameUuidAndRetainsExpectedRevision` verifies authority replacement and version change, preserved old snapshot, same UUID, expected revision retention, idempotent re-statement and null rejection.
- `InformationMetadataTest.securityChangesAreProposalsForKritesRatherThanCalliopePolicyDecisions` verifies strengthening, weakening, replacement, immutability, version-conflict handling and rejection of null without embedding authorization decisions in Calliope.
- `InformationRepresentationTest.mediaTypesAreNormalisedAndRemainSeparateFromConceptAndSchema` and `mediaTypeInputsAreRequiredAndValidatedWithoutEchoingTheirValues` verify normalization, field separation and negative inputs (blank/missing syntax, invalid slash structure, parameters, unsupported top-level token, invalid characters and overlong subtype).
- `InformationVocabularyTest.rawContainerSchemaIsTheSolutionArchitectsQualifiedPath` and the parameterized `everyFhirR4ResourceTypeBuildsTheVersionedSchemaTopic` verify the exact RAW path and all 146 FHIR R4 resource paths, release designation/version placement and unversioned resource entries. The vocabulary test also guards against restoring media-concept builders.
- Existing identity, relationship, representation, quality, defensive-structure and Naming tests continue to pass.

### Defensive programming and unhappy paths

The model copies and exposes collections/buffers defensively as before. Required media-type fields reject null and malformed values using value-free `InformationModelException` diagnostics; upper- and lowercase spellings normalize consistently. `SecurityMetadata` returns immutable proposal values and rejects conflicting versions of the same Topic identity without silently replacing or removing a different version. Authority and coordinate revisions do not mutate existing snapshots. Tests check these outcomes and the builder/model negative paths.

### Educational documentation quality

The Calliope README and Javadocs explain expected-revision semantics, same-UUID revision snapshots, separation of authority from quality, the Themis-Krites boundary, and the difference between concepts, schemas, media types and containment paths. Package documentation identifies module responsibilities and dependency direction. The revised DRP explains the model and its architectural limits; the Backlog successor makes the observable acceptance checks explicit.

### Architectural and security-boundary compliance

The implementation remains in Calliope's standalone model packages and uses only the JDK and existing Calliope Naming types. `jdeps` reports `java.base` and `java.xml`; the latter is the existing Naming XML codec. No persistence/cache operation, external reference lookup, authorization engine, Krites service, new runtime boundary, module descriptor or JPMS metadata was added. The candidate preserves the existing Themis default-deny obligation; Calliope does not decide security policy. The Architecture-target incorporation of the accepted IU design and Themis-Krites boundary remains a separate obligation in HCRL DR-8.

## Verification

| Command | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** — 309 tests, 0 failures, 0 errors, 0 skipped; 91 Naming regression tests and 218 InformationUnit tests. |
| `mvn -B -ntp verify` | **PASS** — all 50 reactor modules succeeded. The configured unit tests are in Calliope; no separate integration tests were configured or executed. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** — Javadoc generation succeeded; no warnings appeared in the command output. |
| `jdeps --list-deps calliope/target/classes` | **PASS** — `java.base`, `java.xml`. |
| `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | **UNEXECUTED** — command completed, but the selector reported “No tests to run”; it is not a pass. |

## Limitations and unresolved matters

- Media-type validation checks restricted-name syntax, supported top-level type and length, but does not check whether a subtype is currently listed in the live IANA registry.
- Krites adjudication is an external architectural responsibility; neither the Krites service nor an integration test is part of BL-0005. The model cannot prove that a caller obtained a Krites disposition.
- The tests verify model value transitions, not a successful Mneme/Mnemosyne persistence handshake or the actor that sets the successor Managed Revision.
- The ArchUnit architecture selector remains UNEXECUTED because no tests were discovered. Its treatment is still required; no waiver is assumed.
- HCRL DR-6's first accepted Software 0.1.0 baseline/predecessor decision and DR-8's Architecture-target Continuum incorporation remain unresolved. Other engineering choices preserved in DR-7 remain for Solution Architect review.
- Task-Issued-At and Report-Produced-At are UNRESOLVED under the evidence provenance rules; no timestamp was guessed. HCRL 011 remains open for Solution Architect review. No commit, push, merge, software acceptance or release allocation was performed.

[task]: ../../before/ai-agent-tasks/UNRESOLVED-bl-0005-implementation-review-corrections-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[patch]: ../bl-0005-calliope-informationunit-candidate-002.patch
[patch-sha]: ../bl-0005-calliope-informationunit-candidate-002.patch.sha256