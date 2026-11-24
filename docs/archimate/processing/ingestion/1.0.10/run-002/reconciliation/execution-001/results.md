<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Run-002 accepted reconciliation result

**CR-001–CR-154: Accept / Resolved** for the authorised bounded reconciliation.
**CR-155–CR-158: On-Hold / Unresolved**, unchanged. The Solution Architect's
2026-10-20 [acceptance](decision.json) is recorded separately from the
[issued revision-2 instructions](reviewed-instructions.md),
[current review](../../change-review.md) and [processing outcomes](processing-records.json).
The accepted candidate was reviewed in Archi. Codex applied only its accepted scope
and governed identity/metadata/navigation consequences. No RAD release or exchange
publication was created; root authority remains **1.0.10 / predecessor 1.0.9**.

The [reconciled complete model](artifacts/reconciled.xml) is an **accepted-scope,
unpublished processing result**. Its newly allocated identities require a subsequent
actual Archi import/export proof before publication; the earlier candidate's export
does not prove transport of these new IDs. No new export-provenance assertion or
whole-XML equivalence claim is inferred from the architect's bounded acceptance.

| Reconciled scope | Established result |
| --- | --- |
| New Topics, CR-001–032 | 32 UUIDv4 Canonical IDs allocated once and persisted in the governed resource source. |
| Existing instances, CR-033–070 | 38 BusinessObject → DataObject/Topic evolutions; original Canonical IDs retained, old/new Exchange bindings and names retained as history. |
| Retained BusinessObjects, CR-071–082 | 12 governed name migrations, retaining identity and authored content. |
| Relationships, CR-083–142 | 59 Composition and one Aggregation preserved exactly, including native type, identity, endpoints, direction, Name/absence, specialisation, Documentation and Properties. |
| Views, CR-143–154 | 11 additions and one existing InformationMap revision captured as authored. All 1,209 complete Views and their navigation match the reviewed candidate's serialization exactly. |
| Held classification findings | Context Topic, Pragma Topic, PetasosParcel Topic and Pylai.Diadromí Topic remain unchanged native DataObjects without inferred Topic specialisation. |
| Complete model | 1,394 unique Canonical IDs: 1,362 established identities retained plus 32 new persisted identities. All 1,137 relationships and 1,209 Views retained. |
| Derived workspace | All 1,394 elements and 1,137 relationships regenerated through shared projectors; each of 993 containment participants has its own folder/direct self-reference. Relationship navigation follows its actual source. |

The [accepted RADS source](../../../../../../../markdown/governance/baseline/accepted-ingestion-1.0.10.json)
owns the resource IDs and persisted metadata. The
[readable Information Architecture amendment](../../../../../../../markdown/governance/baseline/information-architecture.md)
records every accepted resource definition, name, relationship and View. Original
source captures, the baseline README and frozen publishers retain their exact
historical inputs. This amendment changes only the identified instances; it does
not adopt missing conceptual counterparts, domain completion, runtime ownership,
physical allocation or general inference rules.

The [execution plan](plan.json), [establishment manifest](artifacts/source-manifest.json),
[containment](artifacts/resolved-containment.json),
[navigation plan](artifacts/workspace-projection.json),
[generation proof](artifacts/element-generation-proof.json) and
[validation](artifacts/validation.json) separate decisions, identities, authored
semantics, derived navigation and presentation under AX-04/14/15/17. Care → Encounter
retains parent/self → contained-leaf-folder/self structure. Equipment →
CommunicationAddress remains Aggregation; Workforce → CommunicationAddress supplies
the independent Composition ancestry. Absent and misleading input folder cases
regenerate the same workspace without changing protected content.

| Verification | Result |
| --- | --- |
| Complete-model offline XSD/identifiers/references, deterministic metadata/navigation and exact semantic/View preservation | **PASS** — [model validation](artifacts/validation.json). |
| Read-only reconciliation replay from persisted source | **PASS** — [replay log](validation/replay-check.log); zero identities allocated by replay. |
| Focused reconciliation regressions | **PASS** — nine tests, [log](validation/focused-tests.log). |
| Required Maven architecture suite | **PASS** — 90 tests, zero failures/errors/skips, 21.459 seconds, [log](validation/architecture-tests.log). |
| Complete framework suite | **PASS** — 326 tests, 345.403 seconds, [log](validation/framework-tests.log). |
| Frozen Run-002 isolated reproduction | **PASS** — original candidate, metadata, navigation and containment byte-identical, [log](validation/frozen-run-002-replay.log). |
| Independent source/history/full-model/current-review audit | **PASS** — [read-only verifier](validation/verify-execution.py), [audit output](validation/independent-audit.log); original review/inputs, allocated IDs, metadata, all semantic relationships/Views, 993 self-folders, current outcomes and local links verified. |

The first broad test attempt stopped on the baseline README's frozen authority
fingerprint. Its temporary index paragraph was removed and the README restored
byte-for-byte before retry. The initial result and fixture-development findings
remain visible in [development notes](validation/development-notes.md); no historical
pin or test outcome is rewritten.

The [revision-1 review](before/change-review-revision-001.md),
[before manifest](before/manifest.json) and complete hash-verified snapshots retain
the initial On-Hold decisions, draft directives and original Outcomes. The
revision-2 instruction copy and revision-3 review snapshot preserve subsequent
history. Verbatim review snapshots retain relative links in their original review
directory context; the current review supplies live navigation. The historical
[review manifest](../../validation/review-manifest.json) and original review
verifier remain unchanged; the current execution manifest/audit verifies the
original review against its preserved revision-1 text.

The original [transport report](../../roundtrip-results.md) remains immutable.
Its observed losses of 4,717 organisation-item IDs, 215 sibling-order contexts
and one Property-definition language attribute are still recorded. The accepted
candidate seeds reconciliation, preserving its authored View navigation rather
than incorporating those export losses. Residual treatment and final identity-bearing
transport remain publication concerns; this result creates no release transition.
