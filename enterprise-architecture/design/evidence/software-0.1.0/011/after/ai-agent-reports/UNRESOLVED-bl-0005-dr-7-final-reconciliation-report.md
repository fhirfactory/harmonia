<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Report — BL-0005 HCRL 011, DR-7 Final Reconciliation

**Task** · [DR-7 Final-Reconciliation Task](../../before/ai-agent-tasks/UNRESOLVED-bl-0005-dr-7-final-reconciliation-task.md)

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md), DR-13. HCRL 011 remains pending for Solution Architect acceptance.

**Report-Produced-At** · `UNRESOLVED` — the attributable system timestamp of completed Report emission was unavailable. No time is inferred from file metadata, commits or the current clock.

**Candidate-005** · [Patch](../bl-0005-calliope-informationunit-candidate-005.patch) · SHA-256 `4d056a5711986ac0d5d37dd71ec7cc228d9f6c651a222add7ef09573bac460b9` ([sidecar](../bl-0005-calliope-informationunit-candidate-005.patch.sha256)).

## Outcome

Applied the adjudicated DR-7 corrections to the Calliope InformationUnit implementation, focused tests and successor documentation. Candidate-005 is submitted for Solution Architect review; this Report does not accept the implementation, close HCRL 011, allocate a software release, or authorize commit, push or merge.

## Changes

- **Successor derivation:** removed the single `deriveSuccessor()` API. `deriveSuccessorAsClone(UUID, RelationshipRole)` returns a complete new IU, copying source data and associated metadata, preserving existing relationships and appending source provenance. It resets persistence and cache lifecycle metadata. It rejects the source UUID and prevents an inherited `INTERNAL_IU` link from targeting the successor itself.
- **Minimum-metadata derivation:** `deriveSuccessorWithMinimumMetadata(UUID, RelationshipRole)` returns `MinimumMetadataSuccessorBuilder`. The new IU inherits only source `OriginMetadata` and `SecurityMetadata`, receives only source provenance, and starts with fresh persistence/cache lifecycle metadata. The caller must provide concept, description, quality, authority and representation; coordinates default to unplaced and are optional. The builder does not expose overrides for inherited security, origin or provenance, and failed builds preserve its accepted inputs for retry.
- **Accepted existing semantics:** origin Topics remain immutable including versions; descriptions remain revisionable; `InformationQuality.owner` remains required, non-blank and lifetime-immutable. Existing UTF-8, absolute-reference, dimensions, duration, language-tag and checksum/algorithm validation remains unchanged.
- **RAW ContentSchema:** added `InformationVocabulary.rawContentSchema()` returning the exact path `Domain=ICT.Owner=fhirfactory.Standard=harmonia.Concept=InformationUnit.ContentSchema=RAW`. The RAW container schema remains distinct and unchanged. Tests keep `contentConcept` and `contentSchema` mandatory.
- **Documentation and governance evidence:** added Draft 002.007 and Backlog Item 0005 revision 000.004; predecessors remain unchanged. Updated Calliope README/package documentation and HCRL 011 DR-13. The candidate patch excludes Task/Report/HCRL evidence; those are separately linked iteration records.

## Implementation Guide Conformance

### Functional correctness

- `InformationUnitRevisionTest.cloneSuccessorCopiesDataAndMetadataPreservesLinksAndAddsProvenance` checks a new UUID, copied IU fields and metadata, preserved relationships plus source provenance, fresh lifecycle state, source immutability and equivalence to rebuilding the produced snapshot.
- `InformationUnitRevisionTest.minimumMetadataSuccessorInheritsOnlyOriginAndSecurityAndUsesCallerValues` checks caller-supplied values, inheritance of only origin/security, provenance-only relationships, fresh lifecycle state and preservation of source relationships/revision.
- `InformationUnitRevisionTest.derivationRejectsReusedIdsMissingArgumentsAndCloneSelfReferences` checks missing arguments, reused source IDs and clone self-reference protection.
- `InformationUnitRevisionTest.minimumMetadataBuilderReportsEachMissingFieldAndRetainsInputsAfterFailure` checks each required builder field in turn, correction/retry after failed builds, and absence of public setters that could override inherited metadata.
- `InformationQualityTest.subsequentValidationProducesANewIuWithDistinctUuidAndSourceProvenance` verifies the validation workflow uses minimum-metadata derivation and defaults coordinates to unplaced.
- `InformationVocabularyTest.rawContentSchemaHasItsOwnGovernedPathAndRemainsMandatory` checks the exact RAW ContentSchema path, its distinction from the container schema and mandatory `contentConcept`/`contentSchema` inputs.
- Existing `InformationMetadataTest.establishedOriginFactsCannotBeReplacedRemovedOrReVersioned` retains coverage of origin idempotence and version immutability. Existing `InformationRepresentationTest` methods `inlineTextAndBinaryContentConstructWithoutAnyLocator`, `referenceOnlyContentKeepsTheOpaqueLocatorAndHasNoData`, `checksumAndAlgorithmMustTravelTogetherAndAreNeverDefaultedSilently`, and `contentDetailValuesAreOptionalValidatedAndPreservedOnFailure` cover the accepted content validation rules.

### Defensive behaviour and unhappy paths

The successor APIs reject null IDs/roles and reused source IDs with stable model categories and field labels. Clone construction additionally fails closed if preserving an existing internal relationship would make it self-referential. The minimum builder validates every caller-required field at `build()` and remains reusable after rejection. Its inherited origin, security and source provenance are fixed; the immutable source object and its lifecycle/relationship state remain untouched. Existing defensive byte and collection handling is unchanged and remains covered by the representation and metadata tests.

### Educational documentation quality

`InformationUnit` Javadoc describes the two successor intentions and their metadata boundaries. `MinimumMetadataSuccessorBuilder` documents required/optional inputs, fixed inherited fields, lifecycle initialization, caller confinement and retry semantics. The new Draft, Backlog revision and Calliope README explain clone versus minimum-metadata derivation, RAW container versus RAW ContentSchema, and the accepted immutability and validation decisions.

### Architectural and security-boundary compliance

The change stays in Calliope's model and vocabulary responsibilities. It uses existing Java/JDK and Naming types, adds no dependency, JPMS module, persistence path, Themis-Krites service, security-policy decision or external protocol. The new service is not implemented; security adjudication remains outside Calliope. Naming sources and behavior remain unchanged. `jdeps` reports only `java.base` and `java.xml`; `InformationStructureTest` passes. The ArchUnit/Paradeigma requirement remains retained and is **NOT EXECUTED — DEFERRED** under DR-5, not a PASS.

### Limitations, engineering choices and verification qualification

- The derivation methods accept a caller-selected `RelationshipRole`, consistent with the relationship model. Reusing the source UUID is rejected by both methods. Clone derivation also rejects a target UUID already present in an inherited `INTERNAL_IU` relationship because preserving that link would otherwise create a self-reference. These are bounded model-integrity safeguards, not new authorization decisions.
- The test suite was not executed against the pre-change implementation before production edits. Therefore no pre-change failing-test output is claimed; the reported tests establish post-change behavior only.
- Calliope still does not persist data, operate Mneme/Mnemosyne, resolve external references, verify provider bytes, implement Themis-Krites or evaluate security policy. Integration behavior for those components is outside this bounded correction.
- HCRL DR-5's ArchUnit selector remains deferred; HCRL DR-6's first accepted Software 0.1.0 baseline and DR-8's Architecture-target Continuum incorporation remain open. No release allocation or Git transition was performed.
- Task-Issued-At and Report-Produced-At remain unresolved under the provenance rules; no timestamps are guessed.

## Verification

| Command / check | Result |
| --- | --- |
| `timeout 150s mvn -B -ntp -pl calliope -am -Dtest=InformationUnitRevisionTest,InformationQualityTest,InformationVocabularyTest -Dsurefire.failIfNoSpecifiedTests=false test` | **PASS** — 173 tests; 0 failures, 0 errors, 0 skipped. |
| `timeout 150s mvn -B -ntp -pl calliope -am test` | **PASS** — 318 tests; 0 failures, 0 errors, 0 skipped: 91 Naming and 227 InformationUnit tests. The final reactor run includes the lifecycle-reset assertions for revision, disposition and legal hold. |
| `timeout 300s mvn -B -ntp verify` | **PASS** — all 50 reactor modules succeeded after the lifecycle-reset assertions; Calliope's 318 tests passed. Maven emitted non-fatal warnings for empty JARs. |
| `timeout 180s mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** — Javadoc generated successfully with no warnings. |
| `jdeps --list-deps calliope/target/classes` | **PASS** — dependencies are `java.base` and `java.xml`. |
| Candidate-005 SHA-256 and baseline/whitespace check | **PASS** — checksum `4d056a5711986ac0d5d37dd71ec7cc228d9f6c651a222add7ef09573bac460b9`; candidate applies to `HEAD` using a temporary index with `--check --whitespace=error`. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under HCRL DR-5; retained, not retired, and not a pass. |

**Disposition** · Candidate-005 is submitted for Solution Architect review. HCRL 011 remains pending and is not closed.