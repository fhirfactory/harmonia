# Harmonia 1.1.20 — Pass 3 / Iteration 003: Semantic Rationalisation, First Tranche

**Status:** OFFLINE PEER REVIEW — PROPOSALS ONLY; no acceptance of new semantic changes or repository/Continuum edits.
**Basis:** Candidate 017 / Pass3-002 adjudicated overlay; 101 FC review dispositions remain authoritative for this work. No re-opening of accepted coverage decisions.
**Source catalogue SHA256:** `6a1f50f18a0c99b3b964ec52527e02af11002eb08192a9c98afe1bc513d093b4`. The source was read only; generated inventories and proposals below are separate files.

## 1. Quantitative structural and language audit

- Full parsed population: **24 CT1**, **145 CT2**, **587 CT3**, **2948 Features** (Business 1474; Enterprise 1474).
- Feature cardinalities: **573 CT3s have exactly five Features; 12 have six, one has seven and one (the agreed new collaboration CT3) has four**. Five is a drafting convention, not a target or architectural rule.
- **1322/2948** Features match at least one generic or suspect criterion-expression pattern. This is an automated drafting review, **not** a finding that all 1322 are functionally wrong.
- **No identical normalised CT3 names**; **seven** repeated normalised Feature names across different contexts. Label repetition does not imply semantic equivalence.
- The first CT3 pairing register contains **21** cross-capability semantic candidates, including the 19 earlier flagged pairs; two are proposed as true CT3 consolidation candidates, the others are retained with clarified boundaries or scoped ownership.

### 1.1 Specific wording patterns

| Pattern | Affected Features | Concern |
|---|---:|---|
| Unspecified “quality condition” | 250 | Need observable subject/condition, not a restatement of the Feature name |
| Unspecified “assessment rubric” | 240 | Need observable subject/condition, not a restatement of the Feature name |
| Circular “accessible, accurate evidence of …” | 358 | Need observable subject/condition, not a restatement of the Feature name |
| Generic “changes or disruptions” recovery test | 147 | Need observable subject/condition, not a restatement of the Feature name |
| Generic “positive and negative” acceptance criterion | 143 | Need observable subject/condition, not a restatement of the Feature name |
| Misapplied “reaches and is understood” formula | 108 | Need observable subject/condition, not a restatement of the Feature name |
| Undeclared “context-defined interval” | 37 | Need observable subject/condition, not a restatement of the Feature name |
| Unstated “agreed evaluation basis” | 39 | Need observable subject/condition, not a restatement of the Feature name |
| Malformed “interaction interactions” | 1 | Need observable subject/condition, not a restatement of the Feature name |

These tags overlap. **The proper action is to recover the existing required behaviour and express a falsifiable condition.** Do not delete a Feature solely because it matches a pattern. If no testable distinct behaviour exists, propose consolidation with the neighbouring Feature while preserving the original obligation. Avoid inventing a numerical SLA, performance floor or authority boundary.

## 2. Highest-value CT3 overlap proposals

### P1-01 — B05.03.01 / B05.03.04: consolidate clinical handover acceptance

The base CT3 already requires verified responsibility acceptance, outstanding care obligations, clear accountability and rejected-handover handling. The later CT3 repeats the same business obligation, adding useful distinctions about **receipt not being acceptance**, context sufficiency, and traceability of acceptance time. **Proposal:** one `Clinical Handover Acceptance` CT3, provisionally anchored at `B05.03.01`; seven observable obligation groups rather than the current eleven draft Features. The checklist explicitly names every source Feature and how its requirement is preserved. No deletion authorised yet.

### P1-02 — E07.05.03 / E07.05.06: consolidate qualified disclosure

Both address truthful presentation of materially incomplete information. The later CT3 adds genuinely important qualifications: **unavailable ≠ absent**, existence leakage under restricted disclosure, source staleness and derived information. These complement rather than justify a new independent CT3. **Proposal:** one CT3 provisionally anchored at `E07.05.03`, titled `Qualified Information Disclosure`, with seven distinct obligations in the checklist. Preserve the missing-source escalation path, not merely visible incompleteness.

### P1-03 — E02.05.02 / E02.05.03: separate degradation from restoration

**Do not merge wholesale.** The first owns a verified restoration result; the second should own truthful presentation and controlled behaviour during degraded/unavailable access. Currently E02.05.03 mixes failure detection and restoration, creating double recovery tests. Proposed clarity: (a) `Verified Access Restoration` and (b) `Degraded Access Truthfulness`. Preserve version consistency, recoverability, observable outage, and distinction between absence and unavailability. No Feature migrations applied.

### P1-04 — E01.01.02 / E02.01.02: separate authoritative change from request/disposition

**Keep separate.** `E01.01.02` establishes governed authoritative changes to information, including durability, concurrency, acceptance and rejected-change evidence. `E02.01.02` is a consumer-facing controlled request/disposition contract, not a second owner of persistence. Its Features currently duplicate storage precondition/durability claims. Proposed clearer label: `Governed Change Request & Disposition`; require a truthful disposition referencing authoritative outcome, not independent write authority.

### P1-05 — E06.03.01 / E06.03.03: separate routing decision from physical handoff

**Keep distinct with explicit handoff of authority.** A routing/eligible-destination decision is not a successfully committed Petasos handoff, nor receipt by an Ergon. Keep decision provenance, unsupported destination and parcel fidelity separate from queue/handoff outcome. This **does not adjudicate FC-002**: the unresolved arbitration/composition of Praxis continuation and Mneme Pub/Sub selection into destination-specific instructions stays a design question, not a new independent Router capability.

## 3. Other semantic boundary determinations (proposed)

| Pair | Review proposal | Reason |
|---|---|---|
| `B02` / `B03` | keep distinct scope | These are valid Business capabilities; do not infer Harmonia owns clinical decisions or EMR clinical authoring. |
| `E08.03` / `E08.07` | keep distinct service dedup | Reporting existing evidence does not create or own immutable AuditEvents or Provenance. |
| `E08.03` / `E08.08` | keep distinct service dedup | Consumer visibility is not assembled export/controlled release. |
| `E09.02` / `E09.05` | keep distinct scope | Harmonia is not an analytics warehouse; governed analytical access may be consumed or separately provided. |
| `B05.04.01` / `B05.04.04` | keep distinct | Highly similar title but different objects of concern (goals/decisions versus history/source/currency). |
| `E01.05` / `E01.06` | keep distinct | References may point to content without transferring binary custody to general IU persistence. |
| `E02.03.01` / `E02.03.04` | keep distinct redefine | Both currently discuss conflict and node failure; the first should express usable access, the latter consistency invariants. |
| `E02.04.04` / `E06.08.03` | keep distinct | A matching information consumer is not an assigned or eligible FulfillmentTask performer. |
| `E03.03.02` / `E03.03.05` | keep distinct redefine | Timeouts, framing, ACK modes fit adaptation; slow/malformed peer isolation is a separate resilience condition. |
| `E03.06` / `E04.04` | keep distinct | Standards-native exchange is not fully realised by generic schema validation. |
| `E06.02.04` / `E06.02.05` | keep distinct | Non-Twin work must not be made to masquerade as Twin identity. |
| `E06.08.07` / `E06.09.07` | keep distinct | Redelivery and lost subscriptions are transport/work-offer issues; offline human/world action cannot be inferred from them. |
| `B13.04.02` / `B14.06.01` | keep distinct | Sharing the term safeguard does not erase different business subject and accountability scopes. |
| `E03.06.04` / `E03.06.05` | keep distinct | Similar names are intentionally symmetric roles with separate conformance obligations. |
| `E05.05.01` / `E05.05.02` | keep distinct | Same policy family but different lifecycle and enforcement locations. |
| `E08.08.02` / `E08.08.03` | keep distinct | Two distinct source models, ordering and custodians; generic export function can be reused without collapsing capability. |

**Note:** “keep distinct” is an active recommendation not to manufacture a merge from similar terminology. There is no negative requirement to preserve five Features or any particular Feature count.

## 4. Same-CT3 Feature duplication / awkward wording

Eight original intra-CT3 near-name clusters (nine pair rows including the third Persistent Work Context overlap) were reviewed. The highest-confidence candidates for consolidating redundant Feature *criteria*, not their CT3s, are:

| Existing Features | Treatment | Rationale |
|---|---|---|
| `E03.03.03.F01` / `E03.03.03.F02` | consolidate feature proposed | Both express standards-native representation fidelity; F02 contributes no additional independent assurance condition. |
| `B11.05.01.F01` / `B11.05.01.F02` | consolidate feature proposed | Both demand identification of applicable regulatory obligations. |
| `E07.02.02.F01` / `E07.02.02.F03` | consolidate feature proposed | Confirmation evidence overlaps; F03 wording nonsensically treats explicit user confirmation as communication to a separate recipient. |
| `B02.02.03.F01` / `B02.02.03.F05` | consolidate feature proposed | Both measure timeliness of a qualifying clinical reassessment. |
| `E06.06.02.F01` / `E06.06.02.F03` | consolidate or split test | Both largely test recovering workflow position after interruption. |
| `E07.04.01.F01` / `E07.04.01.F02` | consolidate or split test | User work context and position after interruption appear as the same restoration behaviour. |
| `B12.01.02.F01` / `B12.01.02.F02` | consolidate feature proposed | Both concern evidence supporting comparable investment options. |
| `B08.06.02.F01` / `B08.06.02.F03` | keep but rewrite | Current assignment readiness and currency of prior assessment may be independently testable. |
| `E07.04.01.F01` / `E07.04.01.F05` | consolidate or split test | Restoration of work context after interruption appears repeatedly. |

An especially misleading example is `E07.02.02.F03`, which asks whether “explicit user confirmation reaches and is understood by the intended recipient”. That measures message delivery, not consent/confirmation of intended user action. The illustrative rewrites file shows how to avoid that error without inventing new behaviour.

The seven exact cross-context label repeats are in a separate file. Examples such as `Service Contact Usability` on a Business Feature and an Enterprise Feature may be **deliberate contribution alignment**, not a duplicate to remove.

## 5. Guardrails and impact

1. **Read-only candidate:** The complete Pass3-002 adjudicated overlay is unchanged. No deletions, merges, renames, or additions have been applied.
2. **No reopening of FC acceptance:** FC-042 (known deferred gap), FC-059 (open), FC-101 (parked) remain explicit, and FC-001/002 representation and interaction questions retain their decided standing.
3. **No historical identity trail:** We are rationalising 1.1.20 internal semantics under the agreed 1.1.19 functional-coverage criterion, not migrating predecessor UUID identities.
4. **No accidental scope expansion:** Business Capability modelling does not turn Harmonia into an EMR, clinical service provider, general analytics warehouse, or external system operator.
5. **No premature Service claims:** Preserve distinct functional owners (Mneme, Ponos, Petasos, Themis, Agora/Omilia, Kleio/Dokimasia, etc.) without equating them to newly approved deployment components or claiming the earlier lexical service bridge is verified.
6. **Acceptance sequence:** First adjudicate two CT3 consolidation proposals and boundary clarifications. Only then author a bounded merged candidate with a per-obligation preservation check. The 1,147 flagged wording cases should be processed systematically by CT2, not automatically rewritten into fictional KPIs.

## 6. Questions for Solution Architect (adjudication, not prerequisites to this report)

1. Approve or reject consolidation of `B05.03.04` into `B05.03.01`, preserving the seven obligation groups?
2. Approve or reject consolidation of `E07.05.06` into `E07.05.03`, preserving the seven qualification/disclosure obligation groups?
3. Confirm the **distinct, redefined** boundaries (not mergers) for E02.05, E01.01/E02.01, E06.03?

The workbook provides the complete first-tranche review register and population-level language audit; the CSVs are suitable for automated downstream checks, but no agent tasks have been authored.
