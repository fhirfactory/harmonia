<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Report — BL-0005 DR-11 Final Corrections

**Task** · [DR-11 Final Corrections Task](../../before/ai-agent-tasks/UNRESOLVED-bl-0005-dr-11-final-corrections-task.md)

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md), DR-12. The HCRL remains pending for Solution Architect acceptance.

**Report-Produced-At** · `UNRESOLVED` — the attributable system timestamp of Report emission is not available; none is inferred.

## Outcome

Applied the two authorized DR-11 final corrections. Draft 002.006 §1.6 now scopes the `Resource`/`Profile` restrictions to `InformationVocabulary.profileSchema`, and a permanent IU regression test demonstrates that other qualified, versioned Topic paths remain usable. No production source, Naming implementation or accepted behavior outside this scope changed.

## Changes

- **Documentation:** clarified Draft 002.006 §1.6 and its Standing note. The helper's `Domain → Owner → Standard` prefix and its additional `Resource`/`Profile` exclusions are explicitly helper-specific; other valid Topic containment hierarchies remain available to the generic Topic model and IU model. The obsolete `System=Harmonia.Domain=Data...` schema path remains excluded.
- **Regression test:** added `StandardDefinedTopicHierarchyTest.validStandardDefinedTopicsRemainUsableAcrossTheInformationUnitModel`. It constructs three versioned paths through the public Naming API: a resource-bound profile, a nested profile, and a non-`Domain`-rooted path with a repeated qualifier. It checks exact qualified/versioned canonical forms and round-trips; uses the Topics as IU concept, authority, coordinates, container and content terms; checks same-UUID authority revision preserves the prior snapshot; and confirms `profileSchema` still rejects the resource-bound and nested bases with the expected structural failure.
- **HCRL:** recorded the disposition and applied corrections as DR-12, and closed both DR-11 qualifications without advancing iteration 011.

## Verification

| Command / check | Result |
| --- | --- |
| `timeout 120s mvn -B -ntp -pl calliope -am -Dtest=StandardDefinedTopicHierarchyTest -Dsurefire.failIfNoSpecifiedTests=false test` | **PASS** · 1 test, 0 failures, 0 errors, 0 skipped · finished `2026-11-27T09:06:35+11:00`. |
| `timeout 150s mvn -B -ntp -pl calliope -am test` | **PASS** · 315 tests, 0 failures, 0 errors, 0 skipped; 91 Naming and 224 InformationUnit tests · finished `2026-11-27T09:07:14+11:00`. |
| `timeout 300s mvn -B -ntp verify` | **PASS** · all 50 reactor modules succeeded; Calliope's 315 tests passed · finished `2026-11-27T09:07:23+11:00`. Non-fatal empty-JAR warnings were emitted for modules with no packaged sources. |
| Candidate-003 temporary-index round-trip | **PASS** · applied candidate-003 to a temporary index and regenerated its binary diff byte-for-byte; the repository index was not used. |
| Candidate-004 checksum and baseline apply/whitespace check | **PASS** · SHA-256 `0a075d93cbf63d797b5e24c14eb95e6c8bdd755b8a935a11c0c8c78e5111a8ff`; 45-file candidate applies to `HEAD` with `git apply --cached --check --whitespace=error` against a temporary index. |
| Calliope Javadoc and `jdeps` | Not rerun: production sources and dependencies are unchanged by this correction. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under DR-5; not a pass. |

## Implementation Guide Conformance

- **Functional correctness:** the focused test and all 315 Calliope tests pass; the solution-wide Maven verification also passes.
- **Defensive behaviour:** version-capable canonical parsing and exact rendering are checked for all three shapes; the two helper-specific rejection paths are checked by failure category and field. The model accepts only syntactically valid Topics supplied through Naming.
- **Educational documentation:** the test class and method explain why helper-specific validation must not leak into generic Topic use; Draft §1.6 states the same boundary and retains the distinct helper contract.
- **Architectural conformance:** no production code, dependency, Naming behavior or security/persistence boundary changed. The new test exercises existing immutable IU revisions and keeps the helper exclusions intact.
- **Limitations:** the test uses synthetic Topic values to exercise model-level qualified containment and version preservation; it does not assert that the illustrative owner/standard paths are registered by an external standards body. External vocabulary governance remains outside this model-level test.

## Candidate and disposition

The current integrated candidate is [candidate-004](../bl-0005-calliope-informationunit-candidate-004.patch), checksum [sidecar](../bl-0005-calliope-informationunit-candidate-004.patch.sha256). It preserves candidate-003 and contains the existing candidate file set plus the new test and clarified Draft. HCRL 011 remains pending for Solution Architect acceptance; this Report does not accept the implementation, allocate a software release or authorize any commit, push or merge.