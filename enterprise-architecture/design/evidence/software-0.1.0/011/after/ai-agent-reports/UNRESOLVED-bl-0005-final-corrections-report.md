<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# BL-0005 Final Corrections — Report

Report-Produced-At: UNRESOLVED. The attributable system timestamp for completed report emission is unavailable at sufficient precision. No filename timestamp is inferred.

**Task** · [Final-corrections Task Details][task]  
**Target** · `software-0.1.0` · HCRL iteration `011`  
**HCRL** · [Pending review record][hcrl]  
**Candidate** · [Final-correction patch][patch] · SHA-256 `96283de013212e5b2407b033aefc67b23e2000f558575ea2895e02877659c951` ([checksum file][patch-sha])  
**Disposition** · Corrected implementation candidate submitted for Solution Architect review. HCRL 011 remains pending; this report does not accept the implementation or allocate a release.

## Result

Restored a generic, standard-neutral profile-schema capability and its tests. A profile is built beneath its own caller-supplied governing organisation and standard, with optional standard-defined intermediate containment and versions preserved on the entries they describe. The model neither binds profiles to FHIR nor implies a relationship to an underlying resource, and it does not restore the obsolete Harmonia data-schema path or introduce AU Core-specific vocabulary.

Recorded the Solution Architect's ArchUnit/Paradeigma disposition in HCRL 011 DR-5 and DR-10: the requirement is retained, not retired; execution is **NOT EXECUTED — DEFERRED** until Harmonia begins implementing complete subsystems. For BL-0005, the available JUnit, Maven verification and `jdeps` evidence is sufficient. `AGENTS.md` was not changed.

## Implemented contracts

- `InformationVocabulary.governingStandard(domain, owner, standard, standardVersion)` builds the common `Domain → Owner → Standard` prefix, with the optional version attached to the `Standard` entry.
- `InformationVocabulary.profileSchema(governingStandard, profile, profileVersion)` appends a versioned or unversioned `Profile` entry, preserving the base Topic's intermediate entries and their versions.
- Profile bases must begin with the exact ordered qualifiers `Domain`, `Owner`, `Standard`. A `Resource` or existing `Profile` entry below that prefix is rejected, preventing resource-bound and nested profiles. Input failures are converted to value-free `InformationModelException` diagnostics.
- Updated `InformationVocabulary` and package Javadocs, `calliope/README.md`, Draft 002.006 and Backlog Item 0005 revision 000.003. Draft 002.004/002.005 and Backlog revisions 000.001/000.002 remain preserved.

## Implementation Guide Conformance

### Functional correctness

`InformationVocabularyTest` has 155 passing cases, including all 146 FHIR R4 resource schema paths and five profile-specific tests:

- `genericProfileSchemasUseTheirGoverningOwnerAndStandard` checks the caller's organisation/standard path, the profile version, direct parent and absence of a Resource entry.
- `profileSchemaPreservesStandardDefinedIntermediateContainment` checks that intermediate entries and their versions survive profile construction.
- `profileAndStandardVersionsRemainDataRatherThanTopicIdentity` checks version-insensitive Topic identity and version-preserving canonical rendering.
- `invalidGenericProfileAndGoverningStandardInputsAreRejectedSafely` covers missing and blank required values and invalid non-ASCII standard/profile versions.
- `profileBaseMustBeAGoverningStandardAndCannotBeResourceBoundOrNested` rejects wrong qualifier order, an incomplete prefix, a FHIR resource schema base and an existing profile base.

The full Calliope test run passed 314 cases: 91 Naming regression tests and 223 InformationUnit tests, with no failures, errors or skips. The five new profile cases account for the increase from 309 to 314; no Naming test or behavior was changed.

### Defensive programming and unhappy paths

Validation occurs before a profile Topic is returned. Null, blank, malformed version, wrong-order, too-short, resource-bound and nested-profile inputs fail with stable categories and structural field names; caller values are not copied into the model exception. The builder preserves the input Topic and all standard-defined intermediate entries and versions. Tests establish both valid construction and rejection behavior.

### Educational documentation quality

The public methods and factory package documentation explain ownership, the common hierarchy, optional version placement, intermediate containment, resource/profile separation, and the lack of an implicit resource relationship. The Calliope README and Draft 002.006 provide a generic path example and distinguish governing-standard structure from content encoding. Backlog Item revision 000.003 carries the corresponding observable acceptance criteria and verification disposition.

### Architectural and security-boundary compliance

The change remains a Calliope vocabulary construction concern. It uses the existing Naming API and JDK; no AU Core terms, organisation-specific hierarchy, new module, JPMS, persistence, authorization or resource-resolution behavior was introduced. `jdeps` reports only `java.base` and `java.xml` (`java.xml` is used by existing Naming XML support). `InformationStructureTest` passed its source-level structure checks. The ArchUnit/Paradeigma suite was not executed under the explicit deferral and is not reported as a pass; its standing requirement remains in `AGENTS.md`.

### Limitations and unresolved matters

- Generic Topic construction verifies path shape and Naming validity, not that a caller's organisation or standard is authoritative or that a profile name/version is registered by that standard. Such vocabulary authority remains with the profile's governing organisation.
- The base guard rejects `Resource` and `Profile` qualifiers below `Standard` to enforce the accepted independence and non-nesting rules. It is not a general validator for every possible standard ontology.
- The ArchUnit/Paradeigma requirement remains deferred until complete-subsystem work; this report does not remove or waive it.
- HCRL DR-6's first accepted Software 0.1.0 baseline/predecessor decision, DR-8's Architecture-target Continuum incorporation, and remaining DR-7 engineering decisions are outside this correction and remain pending. No commit, push, merge, software acceptance or release allocation was performed.
- Task-Issued-At and Report-Produced-At remain unresolved under the evidence provenance rules; no timestamp was guessed.

## Verification

| Command | Result |
| --- | --- |
| `mvn -B -ntp -pl calliope -Dtest=InformationVocabularyTest test` | **PASS** — 155 tests, 0 failures, 0 errors, 0 skipped. |
| `mvn -B -ntp -pl calliope -am test` | **PASS** — 314 tests, 0 failures, 0 errors, 0 skipped; 91 Naming and 223 InformationUnit tests. |
| `mvn -B -ntp verify` | **PASS** — all 50 reactor modules succeeded. Other modules contain no tests or sources in this checkout; Maven emitted non-fatal empty-JAR warnings for empty modules. |
| `mvn -B -ntp -pl calliope -DskipTests javadoc:javadoc` | **PASS** — Javadoc generation completed without warnings. |
| `jdeps --list-deps calliope/target/classes` | **PASS** — dependencies are `java.base` and `java.xml`. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** — retained under HCRL DR-5/DR-10 until complete-subsystem implementation; not a pass. |

[task]: ../../before/ai-agent-tasks/UNRESOLVED-bl-0005-final-corrections-task.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md
[patch]: ../bl-0005-calliope-informationunit-candidate-003.patch
[patch-sha]: ../bl-0005-calliope-informationunit-candidate-003.patch.sha256