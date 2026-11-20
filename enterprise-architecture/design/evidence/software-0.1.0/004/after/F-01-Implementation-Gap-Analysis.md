<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# F-01 implementation qualifications and acceptance obligations — iteration 004

**Standing** · Future implementation and acceptance evidence for [Draft.001.003][draft]. All obligations below remain open.

The [iteration 003 gap analysis][previous] is the inspected-code baseline. [F-01.75–F-01.81][decisions] settle the design direction. The branch's existing tests exercise the pre-redesign implementation. Its current behaviour and known gaps remain relevant to compatibility. The [DRP acceptance matrix][acceptance] specifies the complete behavioural suite.

| Area | New or refined proof required |
| --- | --- |
| **F-01.75 · APIs** | Test canonical Topic rejection of `Role:=LAB`, omitted-colon round trip and builder/API `null`/empty equivalence. Test the separate mask parser's valid present empty RegEx. Assert all naming accessors with escaped metacharacters, version-preserving valid ancestor depths, invalid depths, null ancestry, separate universal interchange and ordinary-only accessor rejection. |
| **R1 · Topic references** | Inventory legacy UUIDs, stored and transported references, aliases, registry-scoped uniqueness and order assumptions. Demonstrate explicit reference resolution or migration and collision handling, including persisted references to generated versus supplied `N<position>`. Replay representative fixtures to prove the mapping while allowing distinct typed paths with the same unqualified name. The pre-redesign factory's inferred qualifiers and defaults require specific compatibility treatment if consumers rely on them. |
| **R2 · TopicMask migration** | Inventory legacy inputs, including persisted identifiers and both criterion bases. Document adapter mapping and verify old versus new selection scope with exact, descendant, deliberately unrestricted, name-based, ambiguous and invalid inputs. Check supported and unsupported legacy RegEx and remediation on the pinned RE2/J version. Include negative no-widening regression cases for information masks, Themis policy consumers and routing/subscription eligibility at their own boundaries. Retain deliberate errors, including unknown or ambiguous criteria, as failed conversions. |
| **R3 · IU designation** | Obtain an attributable decision on the qualifier sequence and mapping for each boundary. Verify established unqualified spelling and UUID encoding, the distinct qualified canonical form, preservation or explicit migration of old references, and independent IU UUID and version concepts. Include persisted, transport, messaging, matching and reference fixtures. |
| **R4 · Architectural correspondence** | Demonstrate explicit mapping where a runtime Topic has an architectural counterpart. Preserve existing model identities and historical references. Verify the runtime model and interchange do not introduce architectural ID fields. Any needed Continuum amendments use ADD/HCRL. |
| **R5 · Allocation and limits** | Confirm the implementing Maven module, its direct RE2/J dependency/version and supported features, compilation/complexity safeguards and no Java RegEx fallback. Verify logging abstraction availability, Calliope boundary compatibility, exact software-release bounds/error mapping and Topic/TopicMask independence from Themis. Preserve the independent `themis-api` inquiry. |
| **R6 · Logging** | Exercise normal match/mismatch, depth mismatch, invalid construction and evaluation error with logging healthy, failing, saturated and recovering. Verify synchronous Boolean and false on failure, safe and conditional TRACE without remote synchronous delivery, independent console output, bounded producer blocking against applicable budgets, and ERROR delivery through the existing acknowledgement/recovery path without intentional silent discard. Record measured latencies, receipt boundaries and unknown delivery states. Return conflicting or unmet reliability/latency requirements for disposition. |

The integrated implementation submission needs the applicable component, solution-wide regression/integration and architectural conformance evidence under the [Software Development Praxis][software]. Compatibility against actual persisted or deployed inputs, runtime RE2/J, and logging delivery remain unverified. A future acceptance request must disclose missing evidence and its requested disposition. No software implementation acceptance request is made for this design activity.

[draft]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md
[acceptance]: ../../../../offline-design/DRP-Topic-and-TopicMask.draft.001.003.md#10-mandatory-behavioural-acceptance
[previous]: ../../003/after/F-01-Implementation-Gap-Analysis.md
[decisions]: ../before/F-01-Topic-TopicMask-Adjudications-Continuation-004.md
[software]: ../../../../../governance/software-development-praxis.md#6-solution-wide-candidate-submission-and-evidence
