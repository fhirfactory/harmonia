<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Report — BL-0005 Profile-Schema Helper Clarification

**Task** · [Profile-Schema Helper Clarification Task](../../before/ai-agent-tasks/UNRESOLVED-bl-0005-profile-schema-helper-clarification-task.md)

**HCRL iteration** · [software-0.1.0 / 011](../../../../../review/software-0.1.0/hcrl-software-0.1.0-011.pending.md), DR-11. The HCRL remains pending.

**Report-Produced-At** · `UNRESOLVED` — the attributable system timestamp of Report emission is not available to the agent; none is inferred.

## Outcome

**CONFIRMED — already satisfied. No source or test code was changed.** Candidate-003 remains the current candidate; its bytes and SHA-256 are unchanged.

## Verification against the implementation

| Component | Finding |
| --- | --- |
| Naming `Topic`, `TopicBuilder`, canonical parsers | Unchanged by BL-0005. They validate only syntax, limits and escaping; no qualifier names, order, depth or repetition are imposed. |
| `ContainerMetadata.of`, `ContentMetadata.of`, `InformationMetadata`, `InformationCoordinates`, `InformationUnit` | Accept any non-null `Topic`; they inspect no Topic entries. The only `getEntries()` use in the IU packages is inside `InformationVocabulary`. |
| `InformationVocabulary.profileSchema` | The only place that applies the structural rules: the base must start with `Domain`, `Owner`, `Standard` and must not contain a `Resource` or `Profile` entry after them. The class Javadoc, method Javadoc and package documentation scope these rules to the helper. The package documentation and `governingStandard` Javadoc tell callers to use the Naming API for other hierarchies. |
| `InformationVocabulary.governingStandard` | A convenience builder for the common prefix only; it does not constrain caller-built Topics. |

## Verification against tests

- `InformationVocabularyTest.profileBaseMustBeAGoverningStandardAndCannotBeResourceBoundOrNested` constrains only `profileSchema` inputs. The rejected bases are built successfully as Topics first, which itself shows the Topic model accepts them.
- `InformationVocabularyTest.profileSchemaPreservesStandardDefinedIntermediateContainment` covers standard-defined intermediate levels.
- `InformationFixtures` builds concept and schema Topics as `System=Harmonia.<qualifier>=<value>`, outside the `Domain → Owner → Standard` shape. The IU test classes use these Topics throughout, so the model already accepts hierarchies the helper would refuse.
- Naming regression tests (`TopicBuilderTest`, `TopicTest`, `TopicCanonicalParserTest` and others) use arbitrary qualifier hierarchies.

## Commands executed

| Command / check | Result |
| --- | --- |
| `mvn -B -ntp -q -pl calliope -am test` | **PASS** · finished ≈ `2026-11-26T21:56Z` (Surefire report write time) · 314 tests, 0 failures, 0 errors, 0 skipped: 91 Naming and 223 InformationUnit tests, the same totals as candidate-003. |
| Scratch probe (`TopicHierarchyProbe`, compiled against `calliope/target/classes`, kept outside the repository and not retained as evidence) | **PASS** · It built three Topic shapes: a profile beneath a `Resource` entry, nested `Profile` entries, and a path not starting with `Domain`. Each round-tripped through `fromCanonicalNameWithVersion` with identity and versions preserved, and each was accepted as concept and schema by `ContentMetadata.of` and `ContainerMetadata.of`. `profileSchema` rejected the first two as bases with `INVALID_INPUT` at `vocabulary.governingStandard`, and the message echoed no values. |
| `mvn -B -ntp verify`, Javadoc, `jdeps` | Not rerun. No code changed; the DR-10 results stand. |
| ArchUnit/Paradeigma selector | **NOT EXECUTED — DEFERRED** under DR-5; not a pass. |

## Qualifications

1. **No permanent test pins the distinction.** The independence of the Topic model is shown indirectly by the fixtures and Naming tests, and directly only by the scratch probe. If the Solution Architect wants a regression guard, a focused test can be added, for example one asserting that a resource-bound profile Topic built through the Naming API is accepted as `contentSchema`. Under the instruction, no test was added.
2. **Draft 002.006 §1.6 wording.** The sentence "Do not nest a profile beneath a `Resource` entry, use an existing profile as a base, or restore the obsolete … path" is phrased as general guidance rather than as the `profileSchema` helper contract. Read with this clarification, it describes the helper contract and the BL-0005 generic profile vocabulary, not a limit on the Topic model. The Draft is left unchanged. A successor Draft clarifying the scope would need Solution Architect direction.

## Implementation Guide Conformance

- **Functional correctness:** confirmed; see the tables above.
- **Defensive behaviour:** the helper still rejects invalid bases and does not echo values; the generic model adds no restriction.
- **Documentation:** the helper-scoped Javadoc and the package/README pointers to the Naming API are already in place.
- **Architectural conformance:** unchanged. There is no new dependency, the Naming API is unchanged and no security or persistence behaviour is involved.
- **Limitations:** qualifications 1 and 2.

No commit, push, merge, release allocation or software acceptance was performed.
