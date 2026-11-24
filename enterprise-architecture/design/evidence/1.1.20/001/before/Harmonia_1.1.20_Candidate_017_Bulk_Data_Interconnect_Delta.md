# Harmonia 1.1.20 — Candidate 017
## Governed Bulk Data Interconnect: Completeness Expansion

**Standing:** PROPOSED, not governed or accepted. Candidate 016 preserved without modifying existing Capability/Feature identities.

## Principal finding

The export requirements in `DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md` §4 explicitly establish reusable bulk, on-demand and temporally restricted export of log/audit/provenance records. The DRP distinguishes lightweight, non-dereferenced export from domain-specific dereferenced export, and does not require Harmonia to operate external analytics or data-lake infrastructure.

That requirement was not a complete generic Big Data Interconnect capability. It represents one use of an interconnect that should be reusable across independently governed domains.

## Proposed correction

Add **E03.07 — Governed Bulk Data Interconnect** under E03.

Eight CT3 candidate responsibilities, five testable Features each: E03.07.01, E03.07.02, E03.07.03, E03.07.04, E03.07.05, E03.07.06, E03.07.07, E03.07.08. **40 new Features**, no replacement of existing E03 or E08 obligations.

## Boundaries

- **E03.07**: large-population exchange contracts, bulk packaging, flow control, transfer state, reconciliation and interoperable transport.
- **E08.08 / Kleio / Dokimasia**: domain-specific evidence selection, dereferencing, historical consistency, all-or-nothing release where defined.
- **E01.05 / E01.06 / Sylloge**: information references, binary/media references and custody, retrieval and assembly.
- **Mneme/Mnemosyne and source-domain services**: governed information access, selection, authority and source-of-truth semantics.
- **Pylai**: established external interface/transport adaptation responsibility, without automatically allocating a new component or inventing an implementation.
- **Themis**: enforcement; a bulk request does not grant carte blanche access.

## Key architectural guardrails

1. No direct database or raw Information Unit export bypass; use existing authorised information access and respective source-domain contracts.
2. High-volume does not imply large single-object media. Bulk references to media and retrieval of binary bytes are different operations.
3. Receipt, durable acceptance, source-information correctness, destination delivery, completed export and recipient ingestion are separate outcomes.
4. A DRP-required dereferenced evidence export is released only if all required information is assembled and authorised; do not universalise this atomicity to every possible bulk stream.
5. Source version, time criteria, membership, credentials, purpose/destination and manifest contents should be expressed in explicit governed transfer contracts.
6. A downstream data-lake, analytics environment, streaming broker or BI engine is not a universal R1.x/R2.x prerequisite.
7. Imported large populations are a proposed general mechanism, but release requirements and authority remain to be adjudicated. No assumption that R1.x/R2.x supports every import/export mode.
8. Do not automatically turn the ICT Foundation lenses or specific transport products into Enterprise Capabilities.

## Distinct validation examples

- Bulk export of non-dereferenced provenance and AuditEvent records for authorised external analytical ingestion, respecting record identity and time selection.
- Complete dereferenced AuditEvent export coordinated by Kleio: authorised point-in-time assembly and no unauthorised partial release.
- Authorised clinical record extract with versioned information, separately referenced large media and an interpretable transfer manifest.
- Interrupted bulk transfer: controlled restart/reconciliation without duplicate domain commitments or false completion.
- Attempted cross-compartment bulk read: the interconnect cannot bypass local Themis PEP checks merely because a bulk request was accepted.

## Follow-up reconciliation

Enterprise CT2/CT3: E03.07 proposal; existing E08.08, E01.05, E01.06, E02, E09.
Application / Information: preserve Pylai, Mneme, Mnemosyne, Sylloge, Kleio and Dokimasia boundaries; confirm additional source domains during later service-architecture work.
Technology and ops: review ICT lenses for platform, middleware, connectivity, compute, persistence, observability and recovery without selecting products now.

**Net capability inventory change:** +1 Enterprise CT2, +8 Enterprise CT3, +40 Enterprise Features (if the candidate is accepted), resulting in 2,944 proposed combined Features compared with 2,904 in Candidate 016.
