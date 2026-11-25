Report-Produced-At: UNRESOLVED

Event source: completion of this Implementation Report. The attributable system timestamp for the report-emission event is unavailable in the retained interaction context; it is intentionally unresolved rather than inferred.

<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Calliope Naming — Codex Review Corrections Implementation Report

**Agent:** Junie  
**Software target:** `0.1.0-SNAPSHOT`  
**Branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`  
**Backlog authority:** Backlog Item 0003 revision `000.004`  
**Detailed design:** `DRP-Topic-and-TopicMask.Final.003.md`  
**Review iteration:** Software HCRL `software-0.1.0/010` (left unchanged)  
**Task record:** `enterprise-architecture/design/evidence/software-0.1.0/010/before/ai-agent-tasks/unresolved-naming-0003-codex-review-corrections-task.md`  
**Task-Issued-At:** UNRESOLVED, as recorded in the Task Details.

## Result

Implemented the directed corrections as an unaccepted review candidate. The
candidate preserves the accepted Topic and TopicMask semantics, package
organisation, serialization design, XML structure/validation order and bounded
numeric comparison. It does not constitute Software acceptance or close
HCRL `010`.

## Starting source and candidate identity

- Starting branch: `feature/BL-0003-Calliope-Topic-and-TopicMask`.
- Starting `HEAD`: `680e8032289db9e97f2aeefa9783ba48d64f1b53`.
- Starting tree: `514054403a62762d842f620bde90e0da23afd1b9`.
- Candidate patch: `enterprise-architecture/design/evidence/software-0.1.0/010/after/naming-0003-codex-review-corrections-candidate.patch`.
- Candidate tree when that patch is applied to the starting `HEAD`:
  `39ee0140cfee88ed6d5db4d607ddd34e779b8265`.
- Candidate patch SHA-256:
  `a0ff3ba087d0b442e5c466a3d165471537c4ab1d9f5ff5d4868bbe75ec565e0a`.
- The nine-file patch was independently applied to a temporary index and
  reproduced the recorded candidate tree. The real Git index remained clean.
- A separate `/**what` comment change in
  `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/NamingSerialization.java`
  was observed outside the correction scope. It was not edited and is excluded
  from the candidate patch; its origin is not attributed here.
- No accepted design, backlog, HCRL or Architecture-release file was changed.
  No commit, push or release allocation was made.

## Implemented corrections

| Review direction | Implementation and evidence |
| --- | --- |
| Finding 1 — XML 1.0 only | `XmlRecordParser` checks any XML declaration before invoking the parser and rejects a declared version other than `1.0` as `MALFORMED_RECORD` / `XML_RECORD`. No declaration remains accepted under XML's 1.0 default. `NamingXmlCodecTest.xmlInterchangeAcceptsOnlyXmlVersion10` covers Topic and universal TopicMask input, declared 1.0, absent declarations, declared 1.1 and an XML 1.1-only restricted-character reference. Output was already explicitly XML 1.0. |
| Finding 4 — TopicMask construction depth | The `TopicMask` constructor rejects an ordinary entry list above `NamingLimits.MAX_DEPTH` before copying or compiling it, with `LIMIT_EXCEEDED` / `MASK_BUILD` / `DEPTH` and no entry index. `ResourceBoundsTest.maskConstructionRejectsMoreThanSixteenCompletedEntries` exercises direct completed-entry reconstruction. Existing builder and XML checks continue to reject the seventeenth entry before adding/constructing it. |
| Finding 5 — precise failure fields | XML field readers now preserve the known `QUALIFIER`, `VALUE`, `VERSION`, `QUALIFIER_PATTERN`, `VALUE_PATTERN` or `VERSION_CONSTRAINT` kind for nested field content, field attributes, Base64, encoding, bounds and UTF-8 failures. Canonical splitting defers non-structural invalid escapes to the field decoder, which reports the identified field. Structural/order failures remain `XML_RECORD` or `CANONICAL_TEXT` where a field cannot be safely assigned. Regression assertions are in `NamingXmlCodecTest` and `TopicCanonicalParserTest`. |
| XML size/depth boundaries | `ResourceBoundsTest` exercises Topic and universal-mask documents at exactly 256 KiB and one byte over; 16-entry Topic and ordinary-mask documents; 17-entry records of both kinds; and malformed XML padded to exactly 256 KiB, with an over-limit malformed record confirming size rejection precedes parsing. |

Finding 2 (existing XML parsing approach and validation order), Finding 3
(bounded numeric scanning), functional package organisation and Java
serialization received no implementation change.

## Changed-file inventory

The candidate patch changes these nine files (266 insertions, 38 deletions):

- `calliope/README.md`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/NamingXmlCodec.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/TopicMask.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/canonical/CanonicalPathSyntax.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/interchange/NamingXmlFormat.java`
- `calliope/src/main/java/net/fhirfactory/harmonia/model/core/naming/interchange/XmlRecordParser.java`
- `calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/NamingXmlCodecTest.java`
- `calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/ResourceBoundsTest.java`
- `calliope/src/test/java/net/fhirfactory/harmonia/model/core/naming/TopicCanonicalParserTest.java`

## Verification

Environment: OpenJDK 21 (`/usr/lib/jvm/java-21-openjdk-amd64`).

| Command | Outcome |
| --- | --- |
| `mvn -B -ntp -pl calliope -am -Dtest=NamingXmlCodecTest -Dsurefire.failIfNoSpecifiedTests=false test` | PASS: 10 tests in the focused class; zero failures, errors or skips. This is a subset, not an additional unique-test count. |
| `mvn -B -ntp -pl calliope -am test` | PASS: 88 tests across 15 classes; zero failures, errors or skips. |
| `mvn -B -ntp verify` | PASS: full Maven reactor completed with `BUILD SUCCESS`. Calliope's 88-test suite is the same suite reported above, not an additional 88 unique tests. |
| `mvn -B -ntp -pl calliope javadoc:javadoc` | PASS: Javadoc generation succeeded without warnings. |
| `mvn -B -ntp -pl paradeigma/paradeigma-test -am -Dtest="*ArchitectureTest" -Dsurefire.failIfNoSpecifiedTests=false test` | BUILD SUCCESS, but **no architecture tests executed**: `paradeigma-test` has no sources or tests. This is not a PASS of the architecture suite. |
| `jdeps --multi-release 21 -s calliope/target/classes` | PASS: dependencies are `java.base` and `java.xml`. |
| `git diff --check`; candidate-patch replay | PASS: no whitespace errors; replay produced the candidate tree recorded above. |

Test accounting distinguishes the focused 10-test subset from the full
88-test/15-class Calliope execution. The focused run is not added to the 88
unique tests. The full `verify` execution repeated the Calliope suite and is
reported as a separate Maven invocation, not as additional distinct cases.

## Architectural and specification reconciliation

The required XML 1.0-only rule is not explicit in accepted Final.003 §5
(`Logical records and XML encoding`) or Backlog Item 0003 v000.004 §`Canonical
and XML interchange` / AC-6. Those sources require a strict UTF-8 XML envelope
and reject conflicting encodings and malformed records, but do not state which
XML versions are accepted. Before Software acceptance, the Solution Architect
should reconcile the accepted contract to state that input declarations are
optional, XML 1.0 is accepted, and XML 1.1 (and other declared versions) is
rejected. This implementation report and candidate README note the discrepancy;
neither changes the accepted documents.

The depth value itself is already authoritative: Final.003 §6 and Backlog Item
0003 v000.004 §`Resource bounds` set ordinary TopicMask depth to 16 and require
stopping before a seventeenth entry. The correction enforces that existing
invariant at the model constructor boundary for every ordinary construction
path; no new architectural limit is proposed.

Final.003 §6 and Backlog Item 0003 v000.004 §`Public API, validation and
diagnostics` already require the actual field kind when known. The code and
tests now apply that requirement to previously generic XML/canonical failures;
no new diagnostic vocabulary or API is introduced.

## Remaining qualifications

- This is a candidate for independent review, not accepted implementation.
  Software HCRL `software-0.1.0/010` remains unchanged and pending; no findings
  were closed and no acceptance was inferred.
- The XML-version contract discrepancy requires Solution Architect treatment
  in the governed design/backlog record before acceptance. The accepted sources
  were deliberately not edited in this task.
- The architecture-test selector executed no tests. The full reactor verify
  passed, but this report does not claim that the absent ArchUnit suite or any
  separately requested deployment/runtime scenario passed.
- The original Task-Issued-At and this Report-Produced-At remain unresolved;
  no timestamp was inferred from current time, file metadata or build logs.