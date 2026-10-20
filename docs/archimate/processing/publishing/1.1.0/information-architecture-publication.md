<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# RAD-1.1.0 Information Architecture publication

The Solution Architect authorised promotion of corrected Run-002, **CR-001–154**,
from RAD-1.0.10 into **RAD-1.1.0** on 2026-10-20. The governed
[release decision](../../../../markdown/governance/baseline/release-1.1.0.json)
pins the accepted resource capture, instructions, candidate, Archi export and
representation-rebinding provenance. Historical specimens remain unchanged.

The release incorporates 32 new Topics, 38 existing BusinessObject → Topic
evolutions, 12 BusinessObject name migrations, 59 Compositions, one Aggregation,
11 new Views and one revised View. All **1,394 elements, 1,137 relationships and
1,209 Views** are retained. The 1,362 established canonical identities remain;
the 32 new identities were already allocated once in the accepted resource
capture. Publication allocates **zero** identities. See
[identity provenance](identity-provenance.json) for every new ID and all 38 rebindings.

**CR-155–158 remain On-Hold / Unresolved and byte-content unchanged:** Context
Topic, Pragma Topic, PetasosParcel Topic and Pylai.Diadromí Topic remain native
DataObjects without inferred specialisation. No Class, Package,
ImplementationObject or other new metamodel construct is introduced.

Shared reconciliation, naming, containment and workspace projectors validate
persisted metadata and regenerate all Element/Relationship navigation. Existing
Views and their navigation retain the reviewed candidate's exact serialization.
The [generation proof](element-generation-proof.json) verifies absent/misleading
input folders; [publication validation](publication-validation-1.1.0.json)
records schema, reference, identity, semantic and presentation checks. Bounded
test results are in [validation](validation/test-results.json); actual final-model
Archi transport is in [roundtrip](roundtrip/archi-transport-verification.json).
Known transport serialization effects remain visible; the native publication
retains the reviewed presentation. Final human rendering inspection remains pending.

Expected differences from the reviewed candidate are the 32 persisted IDs,
their resulting workspace projection IDs, model release metadata and removal of
the candidate-standing marker. **No unexpected architectural differences.**
No documentation-quality sweep or workspace restructuring was performed.

Import [Harmonia-Architecture-1.1.0.xml](../../../exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml).
SHA-256: `4bd64067f4f62abdf58c348db6269ebc7c2edb31a85f069819ea717b982f01a1`.
