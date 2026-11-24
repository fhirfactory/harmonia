# Harmonia Motivation View Reconciliation — Candidate 1.1.18-001-003

## Governance and scope

- Input: `Harmonia-Enterprise-Continuum-1.1.18-001-002.xml` (unchanged).
- Output: `Harmonia-Enterprise-Continuum-1.1.18-001-003.xml`.
- **Candidate only.** The Solution Architect agreed the 23 exception dispositions in discussion; this does not modify or claim acceptance of canonical Markdown authority.
- The original 23 low-confidence G01.02 placements were removed from that view; replacements appear in their reconciled subject Views.
- Native ArchiMate `Principle` represents `ArchitecturalAxiom`; native `Constraint` represents Harmonia `Mandate` where specified.
- No new semantic relationships were invented. Existing relationships to superseded historical elements were preserved pending explicit review.
- Existing element canonical identity and source paths were preserved. Renamed elements retain prior canonical-name metadata pending governed Markdown reconciliation; do not treat that metadata as updated authority.

## Reconciliation dispositions

| # | Previous element | Candidate element / status | Subject Views |
|---:|---|---|---|
| 1 | Authority Forwarding Requires FORWARD Authorisation | Constrained Authorisation Decision Forwarding | G03.02,G03.03 |
| 2 | CST-EXT-003: Mandated External Interoperability Contracts | External Interface Contract Precedence | G05.04,G05.02 |
| 3 | Harmonisation Reuses the Common Execution Framework | Unified Information and Execution Framework | G06.01,G02.03,G01.03 |
| 4 | Internal Mneme Responsibilities Are Not Automatically Exposed Services | Mneme Internal Service Boundary | G05.04,G03.03 |
| 5 | Mneme Is Insulated from Ponos Implementation Internals | Mneme Is Insulated from Ponos Implementation Internals | Historical only |
| 6 | Negative Criteria Require a Demonstrated Business Requirement | Positive Authorisation Criteria Only | G03.02,G03.03 |
| 7 | PetasosParcel Is Transported by Value | PetasosParcel Transport by Value | G06.04,G05.04 |
| 8 | R1 and R2 Observation Does Not Authorise Autonomous Adaptation | Assurance AI Restricted to Observation and Reporting | G09.06,G01.04 |
| 9 | Runtime Vocabulary Membership Is Authoritative | Release-Governed ValueSet Membership | G02.03,G10.04 |
| 10 | Separate Models and DataSets Lifecycles | Independent Model and DataSet Lifecycles | G02.03,G10.05 |
| 11 | Durable Clinical Information Independence | Enduring Clinical Information Availability | G02.03,G01.02,G07.05 |
| 12 | Patient Safety & Identity Integrity | Patient Safety & Identity Integrity | G02.01,G01.02 |
| 13 | Durable Acceptance and Preservation of Clinical Events | Durable Acceptance and Preservation of Clinical Events | G02.04,G05.03,G07.05 |
| 14 | Vendor-Independent Longitudinal Clinical Information Coherence | Vendor-Independent Longitudinal Clinical Information Coherence | G02.02,G05.03,G01.02 |
| 15 | AX-03 Native Standards Models Remain Native | AX-03 Native Standards Models Remain Native | G05.02,G02.02 |
| 16 | AX-07 Security Is Intrinsic to Managed Operations | AX-07 Security Is Intrinsic to Managed Operations | G03.03,G02.02,G01.01 |
| 17 | AX-17 Architectural Authority and Explicit Uncertainty | AX-17 Architectural Authority and Explicit Uncertainty | G01.01,G01.04 |
| 18 | AX-18 Integration and Coordination Do Not Confer Domain Authority | AX-18 Integration and Coordination Do Not Confer Domain Authority | G01.01,G02.02,G05.03 |
| 19 | Development Representations of Runtime Governed Datasets | Development Representations of Runtime Governed Datasets | G02.03,G10.04,G06.01,G11.03 |
| 20 | Efficient Materialisation of Anticipated Context Resources | Efficient Materialisation of Anticipated Context Resources | G06.01,G02.04,G08.04 |
| 21 | Preserve Necessary Evidence When Historical Resolution Is Unavailable | Preserve Necessary Evidence When Historical Resolution Is Unavailable | G01.01,G01.04 |
| 22 | REQ-FND-003: Subject Referential Integrity | REQ-FND-003: Subject Referential Integrity | G01.01,G01.04 |
| 23 | Platform Operations & Integration Engineers | Platform Operations & Integration Engineers | Historical only |

## Newly proposed candidate elements

- **Distributed Authorisation Enforcement** (Principle, ArchitecturalAxiom): Exception 1; new candidate axiom.
- **Passport Propagation / Availability** (Principle, ArchitecturalAxiom): Exception 1; new candidate axiom.
- **Exclusive PetasosParcel Routing** (Constraint, Mandate): Exception 7; new candidate Mandate; native ArchiMate Constraint.
- **Platform Operations & OA&M Personnel** (Stakeholder).
- **Integration & Solution Engineers** (Stakeholder).

## Unresolved / review qualifications

- Exception 8: AI-generated corrective recommendations within Assurance remain undecided.
- Exception 14: federation versus internal holdings remains undecided.
- Exception 19: independently deploying Calliope releases remains an objective for investigation, not an accepted solution.
- Exception 23: reallocate existing relationships from the superseded combined stakeholder only after semantic review.
- Exception 5: superseded Mneme/Ponos constraint remains historical; no active subject placement.
- Subject View assignments are visual navigation, not newly authored semantic relationships.
- Before governed ADD/UPDATE: reconcile candidate wording, identity, specialisation, source paths, and supersession with canonical Markdown RAD and verify Archi import/round-trip.

## Validation

- XML parses successfully.
- XML identifiers unique.
- All diagram node element references resolve.
- 23 reviewed exceptions, five newly proposed elements (three axiom/mandate plus two stakeholders).
