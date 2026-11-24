# Harmonia 1.1.20 — Pass 3 / 005: NR-001 Contextual Semantic Duplication Review

> **Status:** Offline, read-only semantic review proposals. No change to Candidate 017, SR decisions, or governed Continuum.

## Purpose

The governing test is not whether `friendlyName` labels are globally unique. The qualified hierarchy `CT1.CT2.CT3.Feature` supplies naming context and helps establish architectural identity. Two *different* paths can nevertheless express the **same architectural obligation**, behaviour, or business outcome; different containment does not legitimate redundant obligations. Conversely, the same vocabulary or common underlying service does not imply duplicate responsibility.

## Baseline and screening

- Input: `Harmonia_1.1.20_Candidate_017_Pass3_002_Adjudicated_Overlay.md` — 587 CT3s, 2,948 Features; unmodified.
- The analysis examined all 2,948 Features (4,343,878 unordered pairs in the total comparison universe). The source names, qualified parent paths, CT3 descriptions, and Feature criteria were used as screening evidence.
- Lexical similarity is a **shortlisting tool, not a duplicate determination**. Normalisation included token boundaries, punctuation, and selected close semantic word forms. Boilerplate measurement scaffolding was downweighted in the outcome comparison.
- 148 cross-CT3 pairs met the selected composite similarity threshold of 0.40; the screening queue records these without asserting they are duplicates.
- 28 selected cross-CT3 comparisons have explicit, reviewable classifications: 7 likely repeated obligations, 11 ownership-boundary questions, 9 distinct by context, and 1 intentional Business-to-Enterprise contribution.
- The remaining screening candidates **have not** been semantically adjudicated. There is no claim that this heuristic scan exhausts all possible semantic overlaps, especially those expressed with very different vocabulary.
- The original FR-001–FR-009 same-CT3 comparisons still require review: NR-001 contextual qualification does not resolve repeated obligations within one CT3. The 1,322 wording-quality flags remain independent from duplication analysis.

## P1 — Likely repeated Feature obligations (subject to Solution Architect adjudication)

The following are candidate **Feature-level** redundancies. A consolidated Feature obligation can be referenced, specialised or realised by more than one retained CT3; no parent CT3 is proposed for automatic deletion.

| Review ID | Two different qualified paths | Why repeated |
|---|---|---|
| XD-001 | `Governed Information Custody.Governed Information Persistence.Durable Information Acceptance.PersistenceBeforeAcknowledgement` ↔ `Governed Information Custody.Governed Information Persistence.Independent Persistence Custody **[NEW C012]**.DurableBeforeAcknowledgement` | Both establish that durable storage evidence must precede acknowledgement; independent persistence boundary does not make the ordering obligation different. |
| XD-002 | `Diagnostic Investigation.Diagnostic Findings Reporting.Critical Finding Communication.Unacknowledged Finding Escalation` ↔ `Diagnostic Investigation.Diagnostic Findings Reporting.Critical Result Receipt & Follow-Up Confirmation **[NEW C014]**.UnacknowledgedResultEscalated` | Both make escalation of an unacknowledged critical result the observable outcome; the CT3s differ in communication versus receipt follow-up. |
| XD-003 | `Identity, Trust & Information Protection.Digital Identity & Authentication.Trusted System Interaction.Expired Trust Rejection` ↔ `Identity, Trust & Information Protection.Digital Identity & Authentication.Machine-to-Machine Trust Continuity **[NEW C012]**.ExpiredTrustRejected` | Both assert rejection of expired/revoked machine trust; different labels do not establish different outcomes. |
| XD-004 | `Information Access, Discovery & Distribution.Distributed Information Availability.Distributed Access.Access Recovery after Node Loss` ↔ `Information Access, Discovery & Distribution.Distributed Information Availability.Distributed Access Consistency **[NEW C012]**.NodeFailureAccessContinuity` | Both test truthful access continuity on node failure, including permitted access versus explicit degradation; E02.03.04 has additional consistency responsibilities not expressed by this Feature. |
| XD-005 | `Digital Interoperability & Service Interaction.Exchange Compatibility & Conformance.Exchange Conformance Evidence.Repeatable Conformance Test` ↔ `Digital Interoperability & Service Interaction.Exchange Compatibility & Conformance.Conformance Evidence for External Interfaces **[NEW C012]**.ConformanceSuiteRepeatable` | Both require a repeatable/rerunnable exchange-interface conformance suite; one adds release qualification but not necessarily a different outcome. |
| XD-006 | `Governed Information Custody.Governed Information Reference Management.Large Content Access by Reference.Media Content Integrity` ↔ `Governed Information Custody.Governed Information Reference Management.External Media Reference Governance **[NEW C012]**.MediaIntegrityVerified` | Both ask whether retrieved large media matches governing integrity evidence, despite placement in access-by-reference versus external-reference governance. |
| XD-007 | `Information Presentation & Human Interaction.Longitudinal Information Viewing.Longitudinal Health Record Viewing.Historical Version Accessibility` ↔ `Information Presentation & Human Interaction.Longitudinal Information Viewing.Cross-Domain Longitudinal Correlation View **[NEW C012]**.HistoricalVersionAccessible` | Both describe the ability to access an earlier information state from a longitudinal presentation; the cross-domain CT3 may have other distinct correlation obligations. |

## P2 — Possible shared obligation requiring boundary adjudication

| Review ID | Feature pair | Key question |
|---|---|---|
| XD-008 | `E06.02.03.F04` / `E06.02.05.F01` | Twin-work isolation and Twin queue isolation may express the same scope/ownership safeguard, though queue-specific enforcement could be narrower. |
| XD-009 | `E06.02.03.F05` / `E06.02.05.F04` | Both address recovery of accepted Twin work; one is generic continuity and the other specifies restoration after Twin-host failure. |
| XD-010 | `B07.03.01.F04` / `B07.03.02.F02` | Both expose unmet demand; one derives information in demand assessment and the other needs it during capacity allocation. |
| XD-011 | `E04.01.02.F05` / `E04.01.03.F02` | Both refer to historical model availability; one promises model lookup, the other historic Information Unit interpretability under the original ontology. |
| XD-012 | `E06.03.02.F04` / `E06.03.03.F02` | Route-decision visibility and traceability could repeat the same evidence requirement at consumer-versus-routing boundaries. |
| XD-013 | `E08.06.02.F01` / `E08.06.04.F03` | The invariant of no lost accepted work appears in preservation and disaster-recovery proof, though the latter may have a stricter catastrophic-recovery assurance context. |
| XD-014 | `E08.08.03.F05` / `E08.08.04.F01` | Both introduce complete-package release gating; provenance assembly applies a particular instance of the general all-or-nothing governed release obligation. |
| XD-015 | `E01.02.01.F03` / `E01.02.03.F03` | Both mention version identity preservation; retrieval must resolve a precise identity, while immutable custody ensures the identity remains stable. |
| XD-016 | `E03.04.01.F04` / `E03.04.05.F04` | Both specify acknowledgement correlation; second applies across transport/application/commitment levels. |
| XD-017 | `E06.08.01.F03` / `E06.08.05.F03` | Both prohibit treating publication or delivery as work acceptance, but apply that rule at different state boundaries. |
| XD-018 | `B04.03.03.F02` / `B04.03.04.F01` | Correct delivery to a responsible recipient differs from knowing the intended recipient, despite similar terminology. |

## P3 — Qualifying context supplies distinct responsibility

| Review ID | Feature pair | Retain distinction |
|---|---|---|
| XD-019 | `E05.05.01.F01` / `E05.05.02.F01` | Protected information exchange is transit security; protected information custody is protection at rest/retrieval (SR-018). |
| XD-020 | `E01.01.02.F03` / `E02.01.02.F03` | Persistence-side information change preconditions differ from controlled consumer change requests (SR-002). |
| XD-021 | `E04.04.01.F01` / `E04.04.02.F01` | The generic-looking conformance outcome is model conformance versus terminology conformance, each with different source contracts. |
| XD-022 | `B12.04.04.F01` / `E08.06.02.F01` | Business continuity obligation versus Enterprise mechanism for preserving accepted work; similar outcomes can be intentional Business-to-Enterprise realisation. |
| XD-023 | `E04.02.02.F02` / `E04.02.04.F01` | Access to approved versioned value-set membership differs from guaranteeing release-stable terminology publication. |
| XD-024 | `E01.04.01.F03` / `E01.04.04.F02` | Preserving information under a retention hold versus rejecting its disposal despite the hold are separate lifecycle checkpoints. |
| XD-025 | `B05.04.01.F01` / `B05.04.04.F01` | The previously accepted SR-009 keeps longitudinal care intent and longitudinal care context as separate responsibilities. |
| XD-026 | `E05.05.01.F02` / `E05.05.02.F02` | Channel encryption for exchange versus encryption of data in custody; accepted SR-018. |
| XD-027 | `E06.02.04.F02` / `E06.02.05.F04` | Non-Digital-Twin progression versus Digital Twin continuation/recovery; SR-014 preserves specialisation. |
| XD-028 | `E02.03.01.F04` / `E02.03.02.F05` | Distributed read availability differs from reference permission across nodes. |

## Guardrails and governing decisions

1. **NR-001 is accepted.** Short `friendlyName` labels remain valid; interpret them in their containment context. A unique path does not prove a unique responsibility.
2. **SR-001–SR-019 are accepted** as previously adjudicated. No inferred duplicate overrides their explicit separations of responsibility. In particular, distinguish client versus server, capture versus isolation, general function versus specialisation, and information in transit versus at rest.
3. The proposed duplication labels are **not approval** to remove, merge, rename or renumber any Feature. A repeated assertion may be removed from one acceptance test while its parent CT3 remains valid and distinct.
4. Business and Enterprise capabilities may intentionally express the same outcome at different architectural layers; preserve the contribution relation rather than interpreting it as redundant capability.
5. The source catalogue remains unmodified; no governed architecture change, code-agent task, publication, commit or push was performed.
6. FC-042 remains deferred, FC-059 open, FC-101 parked. No previously accepted FC adjudication is reopened by this scan.

## Suggested adjudication sequence

Begin with `XD-001` through `XD-007` (P1 repeated obligations), while retaining each involved CT3 unless a separate architectural decision establishes that the *whole* CT3 is redundant. Then review `XD-008` through `XD-018` only where independent outcome or ownership is genuinely unclear. Keep FR-001–FR-009 and Feature wording quality as distinct workstreams.

## Review artefacts

- `...NR001_Contextual_Semantic_Review.xlsx`: extends the prior Pass3/004 review workbook with the NR001 decision/evidence and screening tabs; preserves existing SR/FR registers.
- `...NR001_Cross_Path_Review.csv`: 28 manually classified comparisons with full qualified paths, CT3 descriptions and criteria.
- `...NR001_Screening_Queue.csv`: 148 machine-shortlisted cross-CT3 comparisons; unscreened results are not called duplicates.
- Source `Candidate_017_Pass3_002_Adjudicated_Overlay.md`: unmodified reference candidate.
