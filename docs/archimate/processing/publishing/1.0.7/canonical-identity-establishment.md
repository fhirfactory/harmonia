<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Canonical identity establishment — Architecture Release 1.0.7

Resource-owned identity and governed names are established for **1299 elements**.
Starting release: **1.0.6**. Resulting release: **1.0.7**.
The [contract](../../../markdown/governance/canonical-identity-and-naming.md) governs the metadata.
The Solution Architect's real no-edit Archi 5.10 proof is **AWAITING ARCHI EXPORT**.
No architecture reconciliation or presentation replay has been implemented.

| Measurement | Result |
| --- | ---: |
| Allocated once, UUIDv4 via Python `uuid.uuid4().hex` | 1299 |
| Valid UUID32 Canonical IDs | 1299 |
| Missing / malformed / duplicate / multiple claims | 0 / 0 / 0 / 0 |
| Persisted and validated display, friendly and canonical names | 1299 each |

The existing RADS `architecture.json` and `motivation.json` resources own literal IDs
and names, including retained historical resources. The source history is complete
resource evidence, not a separate identity register. Ordinary publication allocates
zero IDs and rejects missing, mutated, conflicting or drifted metadata.
Legacy short names, structural paths, name aliases and identifier aliases remain
in their original channels. All 14 EC bindings remain historical identifier aliases;
Exchange representation IDs remain distinct. Native Capability and Harmonia Feature
remain separate type concepts, independent of identity.

Exact removal of only the new Properties and restoration of prior model-release
metadata produces the unchanged accepted architecture and presentation signature.
Element representations, relationships/endpoints, Views, nodes, connections,
Documentation, labels, geometry and styles are preserved. The accepted 1.0.6 XML,
verification, reports, recovery fixtures and Increment 1 evidence remain intact.
No unresolved historical crosswalk or Feature assertion was promoted to approval.
Across the retained populations, 213 display names and
209 friendly/canonical paths have repeated values. Their
separate instances keep distinct IDs and standing; names do not establish the
missing historical crosswalks. No artificial context was added to make them unique.

| Check | Result | Evidence |
| --- | --- | --- |
| source | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/source-validation.json` |
| publication | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/publication.log.gz` |
| publication_tests | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/publication-tests.log.gz` |
| identity_release_tests | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/identity-release-tests.log.gz` |
| architecture_tests | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/established-architecture-tests.log.gz` |
| recovery | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/recovery.log.gz` |
| diagnostic | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/established-diagnostic.json` |
| preservation | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/final-preservation.json` |
| core_regeneration | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/core-regeneration.log.gz` |
| focused_identity_tests | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/focused-identity-tests.log.gz` |
| roundtrip_tests | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/roundtrip-tests.log.gz` |
| complete_regeneration | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/complete-regeneration.log.gz` |
| alias_validation | PASS | `.junie/reports/2026-10-18-archimate-canonical-identity-establishment/alias-validation.json` |

Generated output set (14 files):

- `docs/archimate/capability-architecture/synchronised-baseline/Harmonia-Architecture-1.0.7.xml`
- `docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-archi-guide.md`
- `docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-establishment.md`
- `docs/archimate/capability-architecture/synchronised-baseline/canonical-identity-validation.json`
- `docs/markdown/governance/baseline/business-capabilities.md`
- `docs/markdown/governance/baseline/business-enabling-capabilities.md`
- `docs/markdown/governance/baseline/discrepancies.json`
- `docs/markdown/governance/baseline/enterprise-capabilities.md`
- `docs/markdown/governance/baseline/features.md`
- `docs/markdown/governance/baseline/functions.md`
- `docs/markdown/governance/baseline/motivation.md`
- `docs/markdown/governance/baseline/processes.md`
- `docs/markdown/governance/baseline/relationships.md`
- `docs/markdown/governance/baseline/services.md`

Publication SHA-256: `def447de2f522737c8b9c30dd9850a57bfec71eaa01f0820ff49283c40fba4bb`.
See the [current Archi handoff](canonical-identity-archi-guide.md) and
[complete establishment report](../../../../.junie/reports/2026-10-18-archimate-canonical-identity-establishment.md).
Increment 2 remains gated by the actual no-edit export and comparator review.
