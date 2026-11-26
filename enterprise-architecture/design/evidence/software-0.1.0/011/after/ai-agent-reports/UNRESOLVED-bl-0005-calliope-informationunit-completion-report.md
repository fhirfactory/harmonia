<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Calliope InformationUnit — Implementation Completion Report

Report-Produced-At: UNRESOLVED. The attributable system event time of completed Report emission is not available at sufficient precision. No UTC event time or filename prefix is inferred.
Task-Issued-At: UNRESOLVED. See the [originating Task][task] and the [Resumption Task][task-resume].

**Agent / role** · Junie · bounded software implementation.  
**Software target** · `software-0.1.0` development candidate · HCRL iteration `011` · [pending HCRL][hcrl].  
**Branch / source** · `feature/BL-0005-Calliope-InformationUnit` · `HEAD` `a2e68b9f871102c435dca4cc2583ea69d881dfe1` (uncommitted candidate).  
**Architecture baseline** · `1.1.26`.  
**Preceding Report** · the [stop Report][report-stop], preserved unchanged.

**Disposition** · **CANDIDATE SUBMITTED FOR SOLUTION ARCHITECT REVIEW.** The BL-0005 model is implemented, documented and verified as recorded below. This Report claims no software implementation acceptance, release acceptance, Architecture acceptance or Git-transition authority.

## 1. Authority and scope

The implementation follows [Backlog Item 0005 r000.001][backlog] and [Draft 002.004 §1][draft], as reconciled by the Solution Architect dispositions in the [Resumption Task][task-resume]:

- `informationConcept : Topic` replaces ConceptFDN.
- `CacheLifecycleMetadata` and `PersistenceLifecycleMetadata` are direct constituents of `InformationMetadata`.
- `informationAuthority : Topic` sits on `InformationMetadata`.
- The RAW path is the one supplied by the Solution Architect.
- The vocabulary derives from FHIR R4 resource names and MediaTypes; media formats are never schemas.

HCRL 011 records the superseded earlier contracts (G02 Final.001.000 §2 and HCRL 1.1.25 DR-2) and the obligations preserved (DR-1 to DR-4).

**Candidate** · [patch][patch] · SHA-256 in [`.sha256`][patch-sha]. It covers 40 files: the `calliope/README.md` change, 30 production sources and 9 test sources under `calliope/src/{main,test}/java/net/fhirfactory/harmonia/model/core/information/entity/`. It applies cleanly to `HEAD` (`git apply --check` in a temporary worktree). No Naming source, POM or other module changed. The pre-existing Draft 002.004 edit and the untracked Backlog Item are excluded and untouched.

## 2. Implemented classes and public contracts

| Package | Types and contract |
| --- | --- |
| `information.entity` | `InformationUnit`: an immutable revision snapshot with `getId`, `getInformationConcept`, `getDescription`, `getName` (concept raw name + UUID, no separator), `getQuality`, `getMetadata` and `getRepresentation`. Its controlled changes are `reviseCoordinates`, `withPersistenceLifecycle`, `withCacheLifecycle`, `withOrigin`, `withSecurity`, `withRelationships`, `withDescription` and `deriveSuccessor(UUID, RelationshipRole)`. `InformationUnitBuilder`: caller-confined; validates at `build()`; a failed build preserves its inputs. |
| `…datatypes` | `InformationQuality` (immutable owner, grade and sorted unmodifiable certifications). `InformationMetadata` (six initialised containers plus required `informationAuthority`; structural withers). `PersistenceLifecycleMetadata` (Managed Revision is positive and never regresses; disposition; legal hold). `CacheLifecycleMetadata` (cache-state Topic; opaque bounded Active Generation). `OriginMetadata` (establish-once facts). `InformationCoordinates` (four optional Topics). `SecurityMetadata` (strengthen-only Topic set). `Relationships` and `InformationUnitRelationship` (typed absolute-URI targets; `INTERNAL_IU` requires a `urn:uuid:` target). `InformationRepresentation`, `ContainerMetadata`, `ContentMetadata`, `ContentDetail`/`ContentDetailBuilder`, `Content` (inline xor reference, with defensive byte copies). `InformationModelException` (category and structural field only). `ModelChecks` (shared value-free checks). |
| `…valuesets` | `RelationshipType`, `RelationshipRole`, `QualityGrade`, `InformationFailureCategory`, `FhirR4ResourceType` (146 R4 resource types; `FHIR_R4_VERSION = "4.0.1"`), `MediaTopLevelType` (11 IANA top-level types). |
| `…factory` | `InformationVocabulary`: `rawContainerSchema()`, `rawContainer(Topic)`, `fhirR4ResourceSchema`, `fhirR4ProfileSchema` and `mediaContentConcept` (content concepts only). |

Every package has an educational `package-info.java`. `calliope/README.md` gains a "Calliope InformationUnit" section.

## 3. Significant engineering decisions

All of these are listed for review in [HCRL DR-7][hcrl-dr7].

- **Snapshots.** Every change returns a new snapshot, so earlier revisions and coordinates are preserved by whoever holds them.
- **Coordinate revisions** clear the Managed Revision. Other facts carry forward.
- **`deriveSuccessor`** is the only route to a new representation or quality. It requires a new UUID and adds an `INTERNAL_IU` provenance link. Authority, coordinates and security carry forward, so derivation cannot weaken restrictions.
- **Version-sensitive lifetime rules.** `Topic.equals` ignores entry versions, so origin establish-once checks, security non-weakening checks and value equality all compare the versioned canonical form.
- **Package layering** is `valuesets` ← `datatypes` ← `factory` ← root, with JDK and Naming dependencies only. No logging and no Java serialisation; the model is format-neutral.
- **FHIR R4 enum constants** use the exact resource names (for example `Patient`) so they cannot drift from the standard. The list is pinned by a SHA-256 of the official list.

## 4. Verification (actual commands)

| Command / check | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -am test` | **PASS** · finished `2026-11-26T02:10:37Z` · 318 tests, 0 failures, 0 errors, 0 skipped. This is 91 Naming regression tests (unchanged count from HCRL 010) plus 227 InformationUnit tests. |
| `mvn -B -ntp verify` | **PASS** · finished `2026-11-26T02:10:40Z` · 50 reactor modules SUCCESS. No other module has tests; there is no integration suite to run. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** · 0 Javadoc warnings for Calliope after the documentation corrections. |
| `jdeps --list-deps calliope/target/classes` | **PASS** · `java.base`, `java.xml` (Naming XML codec). |
| ArchUnit selector (AGENTS.md §5) | **UNEXECUTED** · finished `2026-11-26T02:10:41Z` · "No tests to run". **Not a pass**; needs disposition ([HCRL DR-5][hcrl-dr5]). |

**Defects found and corrected during development (not in the candidate):**

- A `SortedSet` compile error; fixed by switching to `NavigableSet`.
- Test expectations that did not account for canonical escaping of `.` inside versions (`4\.0\.1`); expectations corrected.
- An over-broad structure check that flagged an `org.hl7` mention in Javadoc; narrowed to imports.
- A placeholder digest constant; replaced by the value computed independently with `sha256sum`.
- Javadoc "no main description" and "no comment" warnings; all resolved.

One earlier test run was cut short by a broken output pipe and is not counted as a result.

## 5. Acceptance-criteria and Implementation Guide conformance

| Criterion | Test class · relevant methods |
| --- | --- |
| AC-1 identity and name | `InformationUnitIdentityTest` · `nameConcatenatesRawConceptValuesAndUuidWithoutSeparatorQualifiersOrVersions`, `conceptRetainsItsQualifiedPathAndEntryVersions`, `uuidIsStableAcrossRevisionsAndIsTheOnlyIdentity`, `qualifiedConceptIsTheMi05MatchValueForSupportedTopicMasks` |
| AC-2 metadata, relationships, security | `InformationMetadataTest` · `everyContainerIsInitialisedAndTheAuthorityIsExplicit`, `managedRevisionAndActiveGenerationAreIndependentAxes`, `establishedOriginFactsCannotBeReplacedRemovedOrReVersioned`, `securityRequirementsCanOnlyBeStrengthened`. `InformationUnitRelationshipTest` · `everyRoleCanBeUsedWithEveryType` (all 4×4), `urnTargetsRoundTripThroughTheirStringRepresentation`, `internalIuTargetIdentifiesTheUnitByUuid`, `classificationDerivedNameIsInsufficientAsAnInternalIuTarget`, `semanticLinksAreSeparateFromTheContentLocator` |
| AC-3 revisions and transformations | `InformationUnitRevisionTest` · `coordinateChangeCreatesANewRevisionAndPreservesTheEarlierCoordinates`, `revisionPreservesRepresentationQualityOriginSecurityAndRelationships`, `transformationProducesANewIuWithDistinctUuidAndSourceProvenanceEvenWhenEquivalent`, `noPublicOperationReplacesRepresentationQualityConceptOrIdentityUnderTheSameUuid` (reflective API review) |
| AC-4 representation and references | `InformationRepresentationTest` · `contentSchemaCarriesItsVersionThroughTopicEntryVersions`, `containerWithoutDistinctSchemaUsesTheQualifiedRawTopicAndAlwaysHasAConcept`, `inlineTextAndBinaryContentConstructWithoutAnyLocator`, `referenceOnlyContentKeepsTheOpaqueLocatorAndHasNoData`, `bothAndNeitherCarrierStatesAreUnrepresentable`, `inlineBytesAreDefensivelyCopiedInBothDirections`, `providerIntegrityMetadataIsCarriedExactlyAndMatchesTheFixtureBytes` (SHA-256 fixture `017e0aed…bbb62e`) |
| AC-5 isolation | `InformationStructureTest` (4 tests) and the solution-wide `verify`; ArchUnit UNEXECUTED |
| AC-6 InformationQuality | `InformationQualityTest` · `everyGradeIsCarriedIndependentlyOfTheAuthorityReference`, `certificationMapIsCopiedOnInputAndUnmodifiableOnOutput`, `qualityIsStableAcrossACoordinateOwnerRevisionAndItsOwnerIsDistinct`, `subsequentValidationProducesANewIuWithDistinctUuidAndSourceProvenance` |
| Vocabulary | `InformationVocabularyTest` · `rawContainerSchemaIsTheSolutionArchitectsQualifiedPath`, `fhirR4ResourceTypesMatchTheOfficialR4ListExactly`, `everyFhirR4ResourceTypeBuildsAVersionedSchemaTopic` (146 cases), `noVocabularyMethodBuildsASchemaFromAMediaTypeOrFormat` |
| Naming regression | All 15 existing Naming test classes, 91 tests, pass unchanged. `InformationStructureTest.namingRegressionScopeIsUntouchedByTheInformationPackages` |

**Functional correctness.** Verified by the tests above, which use synthetic, PHI-free fixtures.

**Unhappy paths and defensive behaviour.** Every public factory and operation rejects null, blank, malformed, inconsistent or weakening input with `InformationModelException`. The exception carries a category and a structural field label only, and the rejection happens before any state changes. Negative tests:

- `failedBuildNamesTheMissingFieldAndPreservesOtherInputsForRetry` (6 fields, with retry)
- `exceptionMessagesCarryOnlyCategoryAndStructuralField`
- `invalidQualityInputIsRejectedWithoutEchoingValues`
- `relativeMalformedAndMissingTargetsAreRejected`
- `checksumAndAlgorithmMustTravelTogetherAndAreNeverDefaultedSilently`
- `contentDetailValuesAreOptionalValidatedAndPreservedOnFailure`
- `derivationRejectsReuseOfTheSourceUuidAndMissingArguments`
- `rejectedChangesLeaveTheOriginalSnapshotIntact`
- `invalidVocabularyInputIsRejectedSafely`
- the security and origin immutability tests

Collection and buffer immutability is tested for certifications, relationships, security requirements and `Content` bytes. No failure or recovery scenario beyond model-local validation applies, because the model performs no I/O.

**Educational documentation.** Every class has Javadoc explaining its responsibility, invariants and failure behaviour, citing Draft sections and axioms. Each package has a `package-info`, and the README section covers the model end to end. Javadoc builds with 0 warnings.

**Architecture and security boundaries.** The model contains no persistence, cache, Themis, Pylai, logging or Paradeigma code (`InformationStructureTest`). Invariant 7 is respected: `toString()` and exception messages omit payloads, locators, relationship targets, descriptions, owners, certifiers and security labels (asserted in several tests). Themis remains the authority for security changes; the model only blocks structural weakening (Invariant 6). Active cache facts are never authoritative (Invariant 8).

## 6. Known limitations, untested scenarios and qualifications

- **Unverified assumption:** IANA top-level media types as content concepts, and the derived Topic path structure, are Junie's interpretation pending review ([DR-7][hcrl-dr7] item 9).
- **Not implemented, by scope:** Summary, codecs and serialisation, the Mneme operational-revision field, TopicMask-valued security requirements, authority change, Store selection, reference resolution and Themis enforcement.
- **Not run:** concurrency tests for the immutable values. Thread safety is asserted from immutability, not stress-tested.
- **Not verified:** BCP-47 registry validity (shape only) and media-detail plausibility against Content.
- **Separate Architecture-target obligation:** Continuum incorporation of the accepted design ([DR-8][hcrl-dr8]). Software 0.1.0 baseline treatment is still open ([DR-6][hcrl]).

## 7. Deviations from the accepted specification

None intentional. The only additions beyond Draft §1's field list are the Solution Architect-directed `informationAuthority` and the vocabulary value sets. Interpretive choices are recorded in DR-7 for confirmation rather than asserted as the specification.

[task]: ../../before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-implementation-task.md
[task-resume]: ../../before/ai-agent-tasks/UNRESOLVED-bl-0005-calliope-informationunit-resumption-task.md
[report-stop]: UNRESOLVED-bl-0005-calliope-informationunit-implementation-report.md
[patch]: ../bl-0005-calliope-informationunit-candidate.patch
[patch-sha]: ../bl-0005-calliope-informationunit-candidate.patch.sha256
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[hcrl-dr5]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md#dr-5
[hcrl-dr7]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md#dr-7
[hcrl-dr8]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md#dr-8
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0005-Calliope-InformationUnit.v000.001.md
[draft]: ../../../../../offline-design/DRP-Information-Unit-Framework.Draft.002.004.md
