Report-Produced-At: 2026-11-24T09:12:29.596696Z

Event source: architecture_review_evidence.emit_record system UTC clock immediately after completed initial Report emission; field/filename synchronization is provenance maintenance.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Independent Naming implementation review — Backlog Item 0003

**Standing** · Completed independent engineering and proportionate architectural review. **Recommendation · Changes requested before implementation acceptance.** The reviewed bytes remain a Software `0.1.0-SNAPSHOT` candidate.

**Review boundary** · `software-0.1.0/010`. [Operative Codex Task][task] · [Pending Decision Review][hcrl] · [Junie Task][junie-task] · [Implementation Report][junie-report].

**Authority** · [Backlog Item 0003, revision 000.003][backlog] with the approved RE2/J, diagnostics and interim structural verification qualifications, [Naming DRP Final.001.001][design] as accepted under [Software HCRL 004 DR-10][design-review], the [Implementation Guide][guide], [Software Development Praxis][praxis] and the [reset disposition][foundation]. Architecture `1.1.26` governs. [Architecture 1.1.28/001 DR-6][architecture] bounds standalone implementation and retains incorporation and verification for dependent acceptance. [Architecture 1.1.27/002][archunit] remains a separate pending amendment. The reset supersedes the exploratory Java candidate and preserves the qualified design.

AX-04 applies to RE2/J and XML machinery, AX-07 to the neutral selector's authority boundary, AX-10 and AX-11 to bounded failure and work, AX-12 to the interface and its supporting machinery, AX-14 to strict identity, presence and error outcomes, and AX-17 to open dispositions. Review found an independent Calliope model with format-free value objects and direct RE2/J use. The divergences below concern already governed requirements. [Canonical axiom locators][axioms]

## Candidate integrity

| Check | Observed result |
| --- | --- |
| Git state | Branch `feature/BL-0003-Calliope-Topic-and-TopicMask` at `f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb` with a clean index and `0/0` divergence from the local tracking ref. Tree `1846199b36bbfb07c87746c1367c059698b7120d` also matches accepted development foundation commit `7d5c195ad7782ab0b39032308a2eb8f72c678da5`. HCRL 009 establishes the foundation with no prior accepted software release. |
| Patch | [Retained candidate patch][patch] SHA-256 `70ad92b2503e89b15ff150e89562723b23ca80c1f7cc94648863e7f5a553f97a`. `git apply --check --cached --whitespace=error` passed. I applied it to a temporary `git archive HEAD` extraction. All 33 paths matched the live candidate bytes and the Report's path inventory. |
| Actual change | 23 production Java files, seven JUnit classes, two XML fixtures and Calliope POM, 3,477 inserted lines. The patch introduces no other module, downstream consumer, project graph, plugin, accepted Continuum or release-identity changes. |
| Dependencies | The POM diff adds exactly direct compile `com.google.re2j:re2j:1.8` and test `org.junit.jupiter:junit-jupiter:5.10.2`. JDK 21 `jdeps -verbose:package` on the built Calliope JAR shows `java.base`, `java.xml` and RE2/J as its only external package dependencies. It shows no Themis, higher-layer, logging or test library dependency. [Calliope POM][pom] |
| Additional workspace work | The 19 paths in Junie's pre-implementation unrelated inventory are still present outside the patch. The accepted backlog, retained reviewed bytes and approved assignment source match their stated SHA-256 values. I recorded current hashes for the pre-existing workspace work and Junie artefacts before review and compared them afterwards. The Junie Task records the baseline paths without byte digests for most unrelated files, so historical byte equality for those files cannot be independently established. |
| Report correspondence | Commit/tree, patch, POM scope, source paths and focused count correspond. The 72-test reactor count and several coverage claims differ from the evidence below. Junie's Report production time is explicitly unresolved. |

## Findings and required treatment

### 1. High — RE2/J lexical safeguards diverge from the pinned engine

**Security and robustness, functional correctness, AC-4, AC-8 and AC-9.** `ExpressionSafeguard.guardComplexity` treats every `[` inside a class as nested, pairs backslashes while already quoted and treats braces after `\x` as potential counted repeats. RE2/J permits a literal opening bracket inside a class and ends `\Q` quoting at its first `\E` substring. Independent small probes confirmed that a 257-repeat case and a 17-group case following a literal opening bracket in a class were accepted. A 257-repeat case following an adjacent backslash and raw quote terminator was also accepted. Each should fail the relevant pre-compilation guard. Independently, RE2/J compiles `\x{2603}` and matches U+2603. Naming rejects it as `LIMIT_EXCEEDED` even though the braces belong to a Unicode escape. [Safeguard scanner][regex]

The native cap does not cover these discrepancies. The accepted-count probes had small compiled programs and the valid hex escape has no counted repetition. Correct the limited lexical treatment of ordinary and POSIX classes, a leading caret or literal closing bracket, raw quote termination and recognized braced escapes. Use pinned RE2/J to adjudicate syntax and retain all numeric thresholds. Add regressions for active syntax beside literals, canonical and XML reconstruction with those expressions, and below/at/above guard boundaries. Return for design disposition if faithful guarding would require a second parser or a change in dialect or bounds.

### 2. High — XML reconstruction supplies a qualifier that was absent from the record

**Functional correctness and architectural conformance, AC-6 through AC-8, DRP §§1–2 and 5, AX-14.** The reader of completed Topic records passes an empty `<qualifier/>` straight to `TopicBuilder.addEntry`, which deliberately generates qualifiers for assembly callers. An independently authored record with empty qualifier and value `LAB` returns `N0=LAB`. The builder is behaving as specified. The reader accepts invalid completed data and can create a different valid identity. [XML reader][xml] · [Topic assembly][topic-builder]

Make XML reconstruction use strict completed-entry validation and construction. Reject empty or missing qualifiers before any builder defaults. Preserve ordered field content and the distinction between missing Topic version, invalid explicit empty Topic version and valid present-empty mask version expression.

### 3. Medium — XML record grammar admits extra content and contradictory encoding

**Defensive and interchange conformance, AC-6 and AC-8.** `childElements` uses Java `isBlank()`. A universal record carrying U+2003 content decodes as an unrestricted selector even though the content is outside XML's structural whitespace set. The reader decodes physical bytes as UTF-8 and passes a `StringReader` to JAXP. The parser accepts a declaration of UTF-16 in a physically UTF-8 record. The independent probe confirmed both outcomes. [XML decoding and grammar][xml]

Define and enforce XML whitespace at element-only positions and enforce the UTF-8 envelope including declaration consistency. Preserve the 256 KiB check, strict UTF-8 decoding and hardened parser settings. Include negative fixtures and verify they produce the safe record category. Parser setup, suppressed SAX details, DTD and external resolution restrictions worked for the reviewed JDK 21 provider and test cases.

### 4. Medium — Canonical depth is checked after segment materialization

**Resource proportionality, AC-9.** `CanonicalPathSyntax.splitEntries` collects all delimiter-separated substrings and snapshots the list before either parser enforces a maximum of 16. The byte ceiling bounds input size. It still permits up to 65,536 minimal ordinary-mask segments to be collected on a rejected call. Both parsers can detect the seventeenth entry during scanning and bound temporary segment storage to 16. A regression should establish early rejection and preserved safe metadata. [Shared scanner][syntax] · [Topic parser][topic-parse] · [Mask parser][mask-parse]

### 5. Medium — Safe failure metadata loses boundary and entry meaning

**Defensive correctness and diagnosability, AC-8, AC-12 and AC-13.** A malformed qualifier inside XML is reported as `TOPIC_BUILD / QUALIFIER / 0`. Invalid XML mask regex is `MASK_BUILD / QUALIFIER_REGEX / null`. The decode operations know the XML read operation and entry index. `TopicMaskBuilder` similarly sends all regex and literal helper failures through entry factories that omit its next position. A negative Topic index is stored as the entry index, and failed ancestor navigation is labelled `TOPIC_BUILD`. These results compromise category-based handling even though exception text is payload-free. [XML reader][xml] · [Mask builder][mask-builder] · [Topic builder][topic-builder] · [Topic navigation][topic]

Propagate read operation, field kind and valid entry index through one strict reconstruction path, and use the known position at builder rejection. Document how navigation and invalid index arguments are represented. Verify that a handling boundary can recognize validation ERROR intent from the typed rejection using local capture. Actual application ERROR and TRACE delivery stays under the accepted standalone deferral.

### 6. High — Internal diagnostics and adapter plumbing expand the public contract

**Public API, package containment and cohesion, AC-11 and AC-13, DRP §§6 and 8, AX-12.** The only producer and sink for `NamingDiagnostic` are package-local to root Naming. Its public constructor and record methods, and the public severity and match-exit enums, follow from splitting the producer, record and enums across packages. There is no consumer contract for them. The record also documents a bounded depth but its constructor accepts values above 16. The XML split also turns the exception's internal factory and resource constants into globally accessible Java members. Public builder constructors duplicate the accepted static entry points. Candidate comments present some of this machinery as deliberate API. [Package inventory and API review](#package-containment-and-api-review)

Require disposition of the supported surface before freezing the first canonical increment. Keep the six model types, the validation exception and its deliberate safe metadata, and the four supported XML operations. Co-locate match-only diagnostic collaborators with the evaluator and make them package-private. Prefer package-private builder construction. For XML, the simplest option is to co-locate its single codec with Naming's strict construction and limits while keeping codec and model responsibilities in separate classes. Retaining the `interchange` package is appropriate if review establishes a small intentional cross-package contract and explicitly justifies its exposed members. Hiding this behind another public facade or access framework would add indirection. Move shared internal resource constants with their actual collaborators and remove the unused public `MAX_FIELD_EVALUATIONS` constant or derive an internal structural assertion.

### 7. High — Integrated verification scope and test accounting need disposition

**Evidence and acceptance, AC-11 and AC-12, Software Development Praxis §6.** The 50-module reactor built successfully. It executed 36 distinct tests in Calliope in seven classes. The eight log summaries contain those seven class counts and a 36-test aggregate. Summing all eight yields 72 without an additional test execution. The original Junie logs are absent from the iteration evidence, so the counting mechanism is inferred from the reproduced output. The 72-test claim has no independent support. The Report's specific claim of a native program-size test is also unsupported by the actual three safeguard test methods. [Verification and test quality](#verification-and-test-quality)

Correct the verification record for the reviewed candidate and explicitly account for the absent cross-module regression and integration scope. The scoped ArchUnit and shared logger qualifications apply. Neither supplies a general integrated readiness disposition. Any dependent acceptance needs the architecture incorporation and verification established by DR-6, the relevant Solution Architect dispositions, and a revised exact candidate with checks mapped to the affected requirements.

### 8. Medium — Educational documentation misstates or omits supported behavior

**Implementation Guide §§4 and 7.5, AC-13.** `Topic.toString` calls raw versioned canonical text suitable for “diagnostic display.” Topic payload may contain sensitive text, and mask `toString` can contain raw expressions. Describe these as contract renderings and direct operational diagnostics to safe fields. The `TopicMaskEntry.of` and `TopicMaskBuilder.addRegex` three-argument docs describe a present version expression but omit `null` as unconstrained. `TopicMask.isIncludeContainedElements` omits the universal-kind exception, and the two `Topic.fromCanonicalName*` methods omit categorized fail conditions. `TopicBuilder.addEntry(int, String, String)` implies adds never grow depth, though inserting a new position can grow it. `TopicMask.isOrdinary` refers to an enum while the type stores a boolean. The model classes should explain Naming's selector boundary, builder confinement, validation outcomes and caller responsibilities. Keep public documentation focused on the deliberately supported API and relate XML format, validated presence and configured limits to the accepted design. [Topic][topic] · [TopicBuilder][topic-builder] · [TopicMask][mask] · [TopicMaskEntry][mask-entry] · [TopicMaskBuilder][mask-builder]

## AC-1–AC-13 conformity

| Criterion | Assessment and evidence |
| --- | --- |
| AC-1 Identity, ordering and hierarchy | Conforms in reviewed cases. Ordered literal qualifiers and values give injective canonical identity, hash and `String.compareTo` ordering with version excluded. Tests and independent hash/sorted collections confirm equal version variants and prefix/navigation semantics. |
| AC-2 Canonical names | Conforms for reviewed grammar and projections. Literal delimiters and backslashes are escaped in qualified projections. Display and raw projections preserve text. Separate parsers share only outer syntax. Depth materialization is tracked under AC-9. |
| AC-3 Version semantics | Conforms for reviewed Topic and Mask data. Null and empty Topic versions are absent. Nonempty values survive navigation/canonical reconstruction. Three structural mask version states stay distinct, and versioned matching distinguishes equal Topics. |
| AC-4 Matching | Partial. Positional full-field checks, flags, containment, explicit universal, null and short circuit verified. A valid supported expression is rejected and limit enforcement is incomplete. Finding 1. |
| AC-5 Builders | Construction and recovery conform in reviewed cases. Out-of-order assembly, initial validation before mode selection, replacement, snapshot and failed operations are correct. Failure context is incomplete. Finding 5. |
| AC-6 XML interchange | Fails strict reconstruction. Deterministic positive fixtures, kind and expression encoding were verified. Invalid completed qualifier and unexpected envelope/content are accepted. Findings 2–3. |
| AC-7 Lossless reconstruction | Positive Topic and mask fixtures preserve ordered original fields, flags, line endings and absence. Reconstruction conformance remains partial because of Findings 1–3, including supported regex syntax that cannot reach construction. |
| AC-8 Malformed and adversarial input | Fails full conformance. DTD, malformed UTF-8/Base64, canonical separators, invalid Unicode and a number of invalid patterns are rejected. Evaluation and sink failures fail closed in the tested paths. Findings 1–3 and 5 remain. |
| AC-9 Bounds and resources | Fails full conformance. Byte and several field bounds, full-field count, compilation once, independent 1,024/1,025 program boundary and expansion boundary verified. Active syntax can miss pre-compilation checks and canonical allocation outruns depth. Findings 1 and 4. |
| AC-10 Concurrency | Conforms for checked immutable inputs and local matchers. The independent mixed-outcome workload exercised successful, absent version, mismatched version and wrong-depth calls through one compiled mask. Published collections resist mutation and snapshot changes. |
| AC-11 Structure | Partial. Approved dependency scope, class size and neutral model ownership pass. Package and public surface require correction or explicit disposition. Material limitations in structural JUnit assertions are documented below. Finding 6. |
| AC-12 Implementation Guide and acceptance evidence | Incomplete. Recovery, logging deferral, test scope and outcomes can be traced. Confirmed defects and required integrated verification or disposition remain. Findings 1–7. |
| AC-13 Educational documentation | Partial. Classes and contracts have documentation and significant safeguards are described. Missing or misleading failure and public API documentation requires correction. Finding 8. |

## Package containment and API review

### Actual package hierarchy

| Package relative to `net.fhirfactory.harmonia.model.core.naming` | Production elements | Responsibility, dependencies and assessment |
| --- | --- | --- |
| `naming` | `Topic`, `TopicEntry`, `TopicBuilder`, `TopicMask`, `TopicMaskEntry`, `TopicMaskBuilder`, `NamingValidationException`, `CanonicalPathSyntax`, `TopicCanonicalParser`, `MaskExpressionParser`, `TopicFieldRules`, `ExpressionSafeguard`, `MaskEvaluator`, `FieldEvaluator`, `MatchDiagnosticSink` | Cohesive core for identity, matching, assembly, canonical representations and shared validation. The six principal abstractions are discoverable. The last eight helpers/interfaces are package-private. Direct RE2/J use is internal. Keep canonical parsing, validation and evaluator collaborators here, where they can use completed-domain construction and compiled fields without public access. Root depends on `datatypes` and `valuesets` for its own diagnostics. |
| `naming.datatypes` | `NamingDiagnostic` | Contains one comparison-exit record used by root matching and its tests. It has no supported downstream contract. The record depends on root concerns via `valuesets` and root matching has to import a public record for its package-local sink. Co-locate the record with the evaluator. This placement would leave `datatypes` with no independent responsibility in this increment. |
| `naming.valuesets` | `DiagnosticSeverity`, `MatchExitCategory`, `NamingFailureCategory`, `NamingFieldKind`, `NamingLimits`, `NamingOperation` | The safe validation vocabulary is a cohesive caller-facing set. Match-only symbols and engine/resource parameters have different consumers and widen this package gratuitously. Keep the justified validation enums together. Move the comparison-only enums with the local diagnostic and keep limits at their enforcement boundary. `valuesets` has no reverse dependency on core. |
| `naming.interchange` | `NamingXmlCodec` and its private nested `StrictErrorHandler` | XML is a meaningful responsibility with a one-way dependency on the domain. No XML classes, annotations or wire representations appear in the domain. The current adapter reaches builders, public constants and a public exception factory because Java subpackages have independent visibility. Strict completed-object reconstruction and a narrower surface justify evaluating co-location as described in Finding 6. Keep XML parsing/writing isolated in the codec even if it shares a package with the model. |

### Every public production type and member

Category 1 is accepted Naming contract, 2 is necessary supporting public contract, 3 is technical public exposure due to package organisation and 4 is unnecessary exposure. Review covered 15 public top-level types and eight package-private types. All public APIs here are Java-level interfaces available to downstream modules.

| Type | Public constructor and method/field disposition |
| --- | --- |
| `Topic` (1) | No public constructor. Retain `builder`, `fromCanonicalName`, `fromCanonicalNameWithVersion`, `getEntries`, `getCanonicalName`, `getCanonicalNameWithVersion`, `getName`, `getRelativeName`, `getFriendlyName`, `getDisplayName`, `getDepth`, `getParent`, `getAncestor`, `isAncestorOf`, and `compareTo(Topic)`. `equals`, `hashCode` and `toString` implement the accepted semantics. `compareTo(Object)` is the Java `Comparable` bridge. |
| `TopicEntry` (1) | No public constructor. Retain `getQualifier`, `getValue` and `getVersion`. `equals` and `hashCode` support complete record comparison including version, as required for interchange evidence. |
| `TopicBuilder` (1) | Retain both sequential and both indexed `addEntry` overloads and `build`. Public zero-argument constructor is category 4. `Topic.builder()` is the accepted public entry point. |
| `TopicMask` (1) | No public constructor. Retain `builder`, `all`, `fromCanonicalExpression`, `isUniversal`, `getEntries`, `isIncludeContainedElements`, `getCanonicalExpression`, `matches(Topic)`, `equals`, `hashCode` and `toString`. The diagnostic/evaluator overloads are package-private. |
| `TopicMaskEntry` (1) | No public constructor. Retain both `of` overloads, `forValue`, `forQualifier`, `any`, both `ofLiterals` overloads, `forLiteralValue`, `forLiteralQualifier`, `getQualifierRegex`, `getValueRegex` and `getVersionRegex`. The equality/hash overrides support structural comparison of ordered criteria and logical values. |
| `TopicMaskBuilder` (1) | Retain `add`, both `addRegex` overloads, `addForValue`, `addForQualifier`, `addAny`, both `addLiterals` overloads, `addForLiteralValue`, `addForLiteralQualifier`, `includeContainedElements` and `build`. Public zero-argument constructor is category 4. `TopicMask.builder()` is the accepted public entry point. |
| `NamingValidationException` (2) | No public constructor. Keep `getCategory`, `getOperation`, `getFieldKind` and `getEntryIndex` for accepted safe failure handling. `create` is category 3, called publicly only for the XML adapter. It also allows callers to fabricate incomplete metadata. Make construction internal. |
| `NamingXmlCodec` (2) | No public constructor. All four operations (`readTopic`, `writeTopic`, `readTopicMask`, `writeTopicMask`) are justified for the approved XML consumer contract. Signatures use bytes and domain types. `StrictErrorHandler`'s public Java override declarations (`warning`, `error`, `fatalError`) implement `ErrorHandler` on a private class and do not widen the Naming surface. |
| `NamingDiagnostic` (3) | Hide the canonical constructor and all record accessors (`severity`, `category`, `operation`, `depth`, `entryIndex`, `fieldKind`) with the record itself. Its compiler-generated `equals`, `hashCode` and `toString` share that disposition. Its public signatures unnecessarily expose match-only severity and exit enums. |
| `DiagnosticSeverity`, `MatchExitCategory` (3) | Hide both enum types, their constants (`TRACE`, `ERROR` and all nine match exit categories), and the compiler-generated `values`/`valueOf` access with the local comparison diagnostic. |
| `NamingFailureCategory`, `NamingFieldKind` (2) | Retain their validation vocabulary and incidental compiler-generated enum access (`values`, `valueOf`). Each is exposed by the accepted failure type. Public metadata must remain safe and stable. |
| `NamingOperation` (2) | Retain build, parse and XML read/write operation metadata and generated enum methods. `MASK_MATCH` only labels a private comparison exit and is category 4 on the public validation enum. A comparison-only record requires no caller-facing operation value. |
| `NamingLimits` (3/4) | No public constructor. `MAX_DEPTH`, `MAX_EXPRESSION_CODE_POINTS` and `MAX_XML_RECORD_UTF8_BYTES` cross into XML and are category 3. The three Topic field maxima, two canonical byte maxima, group depth, repeat bound, expansion, native program size and field evaluation maxima are category 4 as public fields. Hide their declarations while documenting the approved behavior. |

No public domain method passes compiled RE2/J, parser, DOM, mutable builder storage or evaluation state. The exception's three public enum return types are deliberate validation support. The comparison record and its signatures are the material leak. [Source hierarchy][source]

## Complexity proportionality

- Keep separate domain-specific parsers around the shared outer scanner. They enforce materially different literal and RegEx contracts. Bound segment materialization, perform well-formedness checks once at the appropriate boundary and reuse the decoded string. The raw slice record can represent presence using a nullable middle slice, eliminating its redundant boolean. A single type parameter or generic parser framework would add little value here. [Outer scanner][syntax]
- The builder state and immutable products are clear responsibilities. TopicBuilder's `ArrayList`, `TreeMap`, staged record and repeated full validation implement one depth-16 path. A single bounded positional collection used by both add modes and one completed-entry validation path would reduce duplicate storage and field passes. Preserve first-success mode selection, replacement, continuity and published snapshots. MaskBuilder's own capacity guard avoids unnecessary compilation at full depth. Its helper calls should share position-aware construction. [Topic builder][topic-builder] · [Mask builder][mask-builder]
- Keep `MaskEvaluator` separate from value identity. The two functional interfaces are small local seams with a demonstrated testing purpose. Allow an absent optional local sink, so public matching does not construct a diagnostic solely to hand it to a no-op. Remove the evaluator's unreachable null-mask branch and simplify `isOrdinary` and the duplicate containment accessor where the universal case has already returned. Keep injection, first-failure categories and the fail-closed result. [Evaluator][evaluate] · [Mask][mask]
- Keep RE2/J's direct compiled patterns and instruction limit. The conservative saturating estimate is proportionate to the accepted qualification. The scanner is bounded and has no expression tree, expansion or second engine. Its lexical state nevertheless has to agree with RE2/J on the governed cases. Remove the always-true empty-expression switch from shared validation and leave RE2/J as syntax authority. [Safeguard][regex] · [Validation][field-rules]
- XML decoding, output and parser hardening are one codec responsibility with specialized operations. At 466 physical lines, review duplication while fixing read-side strictness. A single strict entry-reading path and a shared UTF-8 decoding routine would reduce divergence. Extract a package-private reader or text helper only if the responsibilities justify it. Keep the byte cap, secure feature settings, depth limit and safe failure mapping. [Codec][xml]

## Verification and test quality

### Executed checks

I used an isolated copy reconstructed from the exact source tree and retained patch. Environment was Linux x86-64, UTF-8, OpenJDK `21.0.13.1`, Maven `3.9.12` and the existing local Maven cache. I used offline Maven to avoid dependency resolution changing the test environment. Commands used `JAVA_HOME=/usr/lib/jvm/java-21-openjdk-amd64` and its `bin` first on `PATH`.

| Command or check | Outcome |
| --- | --- |
| `timeout --signal=TERM --kill-after=10s 150s mvn -o -B -ntp -pl calliope -am test` | **PASS**, 2 modules, 36 Calliope tests, 0 failures/errors/skips. Maven elapsed 4.347 s. |
| `timeout --signal=TERM --kill-after=10s 360s mvn -o -B -ntp verify` | **PASS**, 50 modules, 36 distinct tests in seven Calliope classes, 0 failures/errors/skips. Maven elapsed 4.578 s. |
| `timeout --signal=TERM --kill-after=10s 360s mvn -o -B -ntp clean verify` | **PASS**, independent clean build of all 50 modules, 36 tests all in Calliope, 0 failures/errors/skips. Maven elapsed 5.148 s. Seven Surefire XML reports and no other test reports. Empty-JAR warnings appeared in reset modules. |
| Bounded standalone Java reviewer probes against the fresh Calliope classes and RE2/J 1.8 | Completed under 15 s. 29 checks agreed with their expectations and eight expectations failed, grouped in Findings 1–3 and 5. The first harness invocation stopped on a reviewer mistake in the 4,096-unit test fixture. I corrected its length and exercised both sides below the native cap. No candidate bytes changed. |
| Architecture validator against the exact reconstructed source | **FAIL** on missing `enterprise-architecture/design/evidence/1.1.25/001/before/accepted-input/DRP-Topic-and-TopicMask.Final.001.000.md`, the pre-existing locator recorded in HCRL 009 DR-2 and 1.1.28/001 DR-5. The initial help invocation with system Python could not import `jsonschema`. An offline retry with the existing EA dependency cache used Python `3.14.4`, `PYTHONPATH=/tmp/harmonia-f01-004.Eophds/ea-deps`, and `timeout --signal=TERM --kill-after=5s 90s python3 enterprise-architecture/tooling/architecture_continuum.py validate`. It returned 1 in under three seconds. The test was read-only. |
| Retired ArchUnit | Unexecuted under the accepted standalone qualification. Exact source has no tests in the retired location. Interim Naming structural test, dependency/bytecode inspection and production/test diff review were performed. |

The existing positive maximum-depth JUnit workload logged 1,000 comparisons with 48 checks per call in 63,609,532 ns (focused), 50,921,930 ns (reactor) and 53,144,801 ns (clean reactor). A separate maximum-depth workload with all Topic fields at 64/256/64 code points and a wrong version only at the last position completed 128 positive/late-mismatch pairs, 48 checks per call, in 102,003,704 ns. Mask plus Topic builder setup took 8,873,844 ns. These are observations on a local JDK workload. The compiled-program and field-count bounds constitute the reproducible gates.

Independent confirmation included version-independent hash/sorted collections, actual retained navigation version, valid indexed replacement, Unicode byte measurement, literal quoting at the 512-code-point boundary, well-formed supplementary input and unsupported syntax. RE2/J reported 1,024 instructions for a noncapturing group of 14 `a` characters repeated 73 times and 1,025 after appending a literal. Naming accepted the former and rejected the latter. The expansion test used 26 literal `b` characters followed by `a{128}` for 4,096 units. Appending `a` rejected on the conservative bound even with a native program below 1,024. Separate 256/257 counted repetition and 16/17 group checks agree on ordinary syntax. The mixed-outcome test ran 128 tasks across eight worker threads with a bounded join. Fault injection confirmed a single qualifier check on first mismatch and that evaluation and diagnostic exceptions in the same invocation yield false.

Independent XML records asserted fully decoded qualifier, value and version fields and deterministic write bytes with ampersand, angle brackets, supplementary text and a separate decomposed value. A hand-authored mask confirmed raw CR/LF, absent and present-empty constraint, containment and positive matching after reconstruction. A separately authored universal fixture confirmed exact bytes and explicit logical kind. Noncanonical Base64, invalid decoded UTF-8, DTD and a universal mask carrying a containment flag were rejected. Findings 2–3 and 5 document the failed expectations.

### Candidate assertions and remaining coverage

| Suite | Actual assertion coverage and limitations |
| --- | --- |
| `TopicTest` (6) | Identity versus versions, hashes, strict parent/ancestor, order, display and composed/decomposed preservation. Parent assertion compares version-insensitive Topic equality. Add a direct parent version assertion, identity duplicates in both collection types, value ordering within the same qualifier and current-depth navigation. |
| `TopicBuilderTest` (6) | Failed initial add, one direction of mixed mode, gap recovery, invalid replacement, immutable sequential snapshot, fields and depth. Successful indexed replacement with the three-argument overload, negative index, reverse mode failure, and generated/explicit qualifier equivalence in matching and interchange need coverage. Add low surrogate, paragraph separator and exact plus over-limit escaped field cases with stable categories. |
| `TopicCanonicalParserTest` (3) | Independent escaped canonical string, raw fields, malformed delimiters and ASCII byte ceilings. Over-cap tests are malformed strings, suitable for precedence of the byte guard. Add byte-vs-UTF-16 tests with supplementary text, dangling escape, null, and evidence of allocation stopping at depth before accumulating excess segments. |
| `TopicMaskTest` (11) | Positional matching, flags, version-dependent outcomes, universal accessors, literals, snapshot isolation and structural check count. Successful all-true tasks cannot demonstrate correct mixed concurrent outcomes. Untimed `invokeAll` blocks before timed `Future.get`. Use timed joins and varied expectations. Add broad ordinary round-trip, containment equality, null third regex equivalence, literal versions, excess depth, explicit field-order/first-mismatch counters, throwing-sink mismatch/failure cases and late mismatch at maximum fields. The existing injected counter verifies orchestration. The timing fixture only checks successful wildcard comparisons on short fields. |
| `ExpressionSafeguardTest` (3) | Basic nested groups, counted/estimated rejection, quoted/class literals, unsupported look-ahead and expression length. No test distinguishes native instruction rejection or exact expansion acceptance. Add independent RE2/J comparison for class, quote and braced-escape cases, program boundary with lexical headroom, exact 4,096 and over-limit cases, supplementary code points, quoted literal length and unsupported backreferences. |
| `NamingXmlCodecTest` (6) | Two useful independent fixtures, byte-stable output, CR/LF equality, unsafe structure and XML byte ceiling. Versioned canonical Topic comparison avoids the version-insensitive `Topic.equals` weakness but direct field assertions are stronger. The universal assertion is a codec self-round-trip. Add an independent universal fixture, positive recompiled matching, strict qualifiers/encoding/whitespace, unknown/missing kind, malformed UTF-8, invalid decoded fields, malformed Base64 padding, entry depth and boundary failure metadata. |
| `NamingStructureTest` (1, structural) | Counts the actual files and checks three textual banned references in this run. POM assertions match independent substrings and cannot establish complete coordinates, scope or absence of extra dependencies. The loop also accepts an empty existing source directory. Assert a nonempty inventory, parse actual dependency tuples, and verify the reviewed public/package boundary at the level useful for the first increment. Current source and bytecode inspection independently establish the actual dependency scope and class line counts. |

These runs establish reactor build and packaging health and the executed Calliope functional/defensive tests and structural check. There is no executed cross-module application regression, live service, deployment, container, messaging or distributed recovery verification on this reset candidate. XML exercises real local interchange and no downstream module integration. Consumer authority, IU/legacy mapping work and operational logger evidence are outside this standalone assignment as specified. Record required integration and conformance treatment at HCRL disposition. Architecture incorporation and software release closure remain governed by their separate dispositions. [Verification scope][backlog] · [Software Development Praxis §6][praxis] · [DR-6][architecture]

## Documentation and line limit

All 23 production Java files satisfy the 500-physical-line cap counting documentation and declarations. `NamingXmlCodec` is largest at 466, then `TopicMaskEntry` at 241 and `Topic` at 222. Classes, functional interfaces and operations are generally purpose-documented. Candidate docs already explain ordering, immutable snapshots, local matching, RE2/J dialect, limits, XML Base64 and recovery. Apply Finding 8 and align validated metadata, strict interchange and visibility documentation with the resulting implementation. No line-limit exception is required. [Implementation Guide][guide]

## Acceptance recommendation and resulting state

**Changes requested.** Correct the strict XML and regex defects, honor the early depth constraint, preserve truthful bounded failure context, and resolve the public API and package placement before acceptance. Bring regression and documentation evidence into correspondence with the corrected exact candidate. Obtain a recorded Solution Architect disposition of required unexecuted coverage and verify the dependent Architecture reconciliation under DR-6. The subsequent software acceptance decision must bind the exact tested candidate and release scope. [Pending Decision Review][hcrl]

Review deliverables are the Codex Task, this Report and the pending software review record in iteration `010`. The original candidate and Junie evidence remain byte-identical to review entry. Branch, HEAD, index and unrelated working paths were preserved. There was no commit, push, merge, tag, publication, deployment or release allocation.

[task]: ../../before/ai-agent-tasks/20261124T090642Z-naming-0003-independent-review.md
[hcrl]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-010.pending.md
[junie-task]: ../../before/ai-agent-tasks/20261124T082329Z-naming-0003-implementation-task.md
[junie-report]: unresolved-naming-0003-implementation-report.md
[patch]: ../naming-0003-candidate.patch
[backlog]: ../../../../../../../docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md
[design]: ../../../../../offline-design/DRP-Topic-and-TopicMask.Final.001.001.md
[design-review]: ../../../../../review/software-0.1.0/hcrl-software-0.1.0-004.pending.md#dr-10
[guide]: ../../../../../../../docs/implementation/Harmonia-Implementation-Guide-Philosophy.md
[praxis]: ../../../../../../governance/software-development-praxis.md#6-solution-wide-candidate-submission-and-evidence
[foundation]: ../../../../../register/software-0.1.0/hcrl-software-0.1.0-009.complete.md
[architecture]: ../../../../../review/1.1.28/hcrl-1.1.28-001.pending.md#dr-6
[archunit]: ../../../../../review/1.1.27/hcrl-1.1.27-002.pending.md
[axioms]: ../../../../../../governance/architectural-axioms.md
[pom]: ../../../../../../../calliope/pom.xml
[source]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/
[topic]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/Topic.java
[topic-builder]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicBuilder.java
[topic-parse]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicCanonicalParser.java
[mask]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMask.java
[mask-entry]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMaskEntry.java
[mask-builder]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMaskBuilder.java
[mask-parse]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/MaskExpressionParser.java
[regex]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/ExpressionSafeguard.java
[evaluate]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/MaskEvaluator.java
[field-rules]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicFieldRules.java
[syntax]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/CanonicalPathSyntax.java
[xml]: ../../../../../../../calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/interchange/NamingXmlCodec.java
