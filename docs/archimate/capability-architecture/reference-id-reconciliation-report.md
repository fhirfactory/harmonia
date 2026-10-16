<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Reference-ID reconciliation of the proven 228-capability Exchange baseline

16 October 2026. **Report only: no XML, documentation fields, IDs, relationships,
View definitions, repository semantic records or identity locks were changed.**

## Finding and enrichment decision

**14 capabilities are eligible for documentation enrichment by exact existing
Reference ID: EC-01 through EC-14. The other 214 are not yet eligible.** Every
eligible match resolves to one explicit definition section in the current governed
Enterprise Capability catalogue. Every baseline capability is assessed individually
in Appendix A; the exact eligible source descriptions are in Appendix C.

The 214 Business/Business Enabling elements have no Reference ID, Canonical ID,
ID Alias, source locator or other repository identity binding in the Exchange
baseline. Their Exchange UUIDs identify the external model's objects, but no
repository-held UUID crosswalk was found. A name, hierarchy position, ordinal,
tier, release-scope annotation or similar-looking UUID cannot supply that binding.
No capability-name similarity was used to establish any mapping.

“Not eligible” means no identity-backed governed description can safely be
selected from the evidence, not that a corresponding business meaning necessarily
does not exist in Harmonia. No enrichment is performed or authorised by this report.

## 1. Scope, authority and audit method

The input is the unchanged [proven v2.1a Exchange fixture](baseline/Harmonia-Capability-Architecture-v2.1a.xml), with
228 capabilities and 189 Composition relationships. It has **zero existing
capability `<documentation>` fields**. The augmented four-View publication is
also preserved unchanged. The current checkout is
`6f1d0961b9b8720f18a628d54671d3a83e20eebb`;
existing uncommitted publication work is not treated as architectural approval.

Authority is [AX-17](../../markdown/governance/architectural-axioms.md#ax-17),
[AGENTS.md §§1–1.2](../../../AGENTS.md), the current canonical Strategy catalogues,
the [Business Architecture identity rules](../../markdown/03-business-architecture/metamodel/business-architecture-metamodel.md#8-architectural-element-identity-and-canonical-identification),
and [Domain03 traceability §1.1](../../markdown/04-information-architecture/traceability/domain03-traceability.md#11-traceability-principles).
Existing responsibility is preserved; unestablished correspondence is not completed
through names, hierarchy or proposed publication decisions.

The [RADS reference candidate capture](../../markdown/03-business-architecture/reference-model/business-architecture.json) and its
[authority/uncertainty contract](../../markdown/03-business-architecture/reference-model/README.md)
were used to navigate to explicit source assertions, not as an independent grant
of authority. All 15 source fingerprints and all cited capability source-line
locators agree with the current source files. The existing RADS validator returns
PASS, invalid=0, with 2,467 permitted unresolved dimensions/relationships; that
result does not resolve architectural gaps or approve the candidate capture.

The governed comparison inventory is **190 established BCs + 76 established BEC
contexts + 14 Enterprise capabilities = 280 established capability identities**.
It includes Domain03's independently declared operational Credential Management
responsibility. Three candidate BC concerns are listed separately and are not
promoted. The 69 enabling catalogue descriptions whose standalone identity remains
unresolved are not counted as established capabilities. Contextual views, natural
regions, Features, Functions, application components and ICT Foundation lenses
are not peers of these three capability families and are outside this inventory.

The XML's literal `prop-reference-id` values were compared to explicit governed
catalogue codes; no IDs were synthesised or normalised. Each of the 14 matching
EC sections has one existing Architectural Scope description. Only after these
ID matches were established were names compared. For non-ID-bearing elements,
Appendices A/B include *literal, case-sensitive, same-category name equality*
solely as navigation evidence. Equality neither establishes a mapping nor permits
description copying. No fuzzy matching, guessed alias, inferred rename, parent
propagation or UUID reconstruction was used.

Existing LaTeX/JSON publication `HCM-BC-*`/`HCM-BEC-*` Reference IDs and its
Canonical/Proposed/Candidate labels cannot retrospectively bind this external
XML. Under AGENTS.md §1.2, that publication is not architectural authority over
RADS. Its model, identity lock and proposed reconciliation remain unchanged.

| Exchange layer | Elements | Existing Reference IDs | Exact governed ID matches / eligible | Unresolved (no Reference ID) |
| --- | --- | --- | --- | --- |
| Business Capability | 52 | 0 | 0 | 52 |
| Business Enabling Capability | 162 | 0 | 0 | 162 |
| Enterprise Capability | 14 | 14 | 14 | 0 |
| Total | 228 | 14 | 14 | 214 |

## 2. Exact Reference-ID matches and name differences

All fourteen Reference IDs have exactly one governed catalogue definition.
The XML display name prepends the ID and a space; the catalogue's capability
name omits that prefix (its heading writes `EC-NN: Name`). Thus all 14 raw name
strings differ in formatting, with **zero additional name differences**. These
findings do not propose a rename or ID change.

| Reference ID | Exchange element ID | Exchange display name | Governed capability name | Definition / scope line | Eligible |
| --- | --- | --- | --- | --- | --- |
| EC-01 | `id-d53727d908365757a293b0896b2c769b` | EC-01 Managed Entity & Relationship | Managed Entity & Relationship | [EC catalogue:104](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-01-managed-entity--relationship) | Yes — exact ID |
| EC-02 | `id-06e038737c5355edbc0591e08c594ab0` | EC-02 Context Management | Context Management | [EC catalogue:113](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-02-context-management) | Yes — exact ID |
| EC-03 | `id-5891936941f65d20afad86b0697879c7` | EC-03 Managed State & Lifecycle | Managed State & Lifecycle | [EC catalogue:122](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-03-managed-state--lifecycle) | Yes — exact ID |
| EC-04 | `id-9e79bc474aed516c8c6752ab81b63d6b` | EC-04 Information Management | Information Management | [EC catalogue:131](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-04-information-management) | Yes — exact ID |
| EC-05 | `id-e11f02764fe55ff29c0782db21198040` | EC-05 Search & Discovery | Search & Discovery | [EC catalogue:140](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-05-search--discovery) | Yes — exact ID |
| EC-06 | `id-a6f6d30d8fce5ac78f60f7aefa71b28e` | EC-06 Policy & Control | Policy & Control | [EC catalogue:149](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-06-policy--control) | Yes — exact ID |
| EC-07 | `id-d71457435a085e749ffb1e1a3b551228` | EC-07 Provenance & Traceability | Provenance & Traceability | [EC catalogue:158](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-07-provenance--traceability) | Yes — exact ID |
| EC-08 | `id-dbd66e69788450649e184966aa724797` | EC-08 Interoperability & Exchange | Interoperability & Exchange | [EC catalogue:167](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-08-interoperability--exchange) | Yes — exact ID |
| EC-09 | `id-770ebb3e3bdc5f2bbe77b2bfd55bd74d` | EC-09 Event & Subscription | Event & Subscription | [EC catalogue:176](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-09-event--subscription) | Yes — exact ID |
| EC-10 | `id-49d4f69ebeeb5ea782a52172fb3e8a7b` | EC-10 Activity & Execution | Activity & Execution | [EC catalogue:185](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-10-activity--execution) | Yes — exact ID |
| EC-11 | `id-82cd1f6a3e2352639e22c542f5cb3a9d` | EC-11 Interaction & Experience | Interaction & Experience | [EC catalogue:194](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-11-interaction--experience) | Yes — exact ID |
| EC-12 | `id-9bbd142f818e50d39a3d0429f74b72b4` | EC-12 Operational Assurance | Operational Assurance | [EC catalogue:203](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-12-operational-assurance) | Yes — exact ID |
| EC-13 | `id-7d6f635ffb6f5c6181bde1d07b859662` | EC-13 Semantic Governance & Conformance | Semantic Governance & Conformance | [EC catalogue:216](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-13-semantic-governance--conformance) | Yes — exact ID |
| EC-14 | `id-4dbf578f52bd5b3b9301b9807ce60f30` | EC-14 Service Guardian | Service Guardian | [EC catalogue:226](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-14-service-guardian) | Yes — exact ID |

Appendix C quotes each exact existing Architectural Scope text and identifies
the complete governed section. These existing descriptions can be selected by ID
without changing semantic identity. Any later enrichment must retain applicable
responsibilities and exclusions when using longer descriptions, especially the
EC-07 evidence qualification, EC-08 egress boundary, EC-12/EC-14 boundary, and
EC-14's independence, anti-responsibilities and no-component-allocation rule.
This is an eligibility finding, not an implementation change.

## 3. Capabilities without an identifiable governed definition

| Decision | Number | Meaning |
| --- | --- | --- |
| E | 14 | Eligible: exact existing Reference ID selects one governed definition. |
| U | 111 | No Reference ID; an equal name exists in the same-category established inventory. Identity correspondence and description selection remain unverified. |
| G | 103 | No Reference ID; no literally equal name occurs in the same-category established inventory. No governed description was identifiable without an inferred correspondence. |

| Layer | U: equal-name navigation evidence only | G: no equal-name inventory entry | Total without ID-backed description |
| --- | --- | --- | --- |
| Business | 46 | 6 | 52 |
| Business Enabling | 65 | 97 | 162 |
| Total | 111 | 103 | 214 |

**No governed definition can presently be attached by identity for any of the
214 U/G cases.** The 103 G cases are the bounded “no corresponding governed
definition identified” list in Appendix A. This is not proof that no differently
named governed concept could correspond. Conversely, the 111 U cases do not become
safe matches merely because a definition/responsibility source has the same label.
All 214 would require additional explicit identity/lineage evidence or adjudication.

The six BC G cases are Clinical Finding Interpretation, Diagnosis & Problem
Determination, Health Risk & Prognostic Determination, Care Goal Definition, Care
Response Planning and Care Intent Appropriateness Review. Their absence from the
established BC inventory must not be repaired using illustrative activities or
provisional CT3 taxonomy. The 97 BEC G cases include new broad frames and renamed,
consolidated or decomposed presentation entries; their semantic equivalence is
not inferred from the reviewed working publication.

## 4. Repository capabilities not represented by a verified Exchange mapping

All 14 governed Enterprise capabilities are represented by exact ID. **None of
the 190 BC or 76 BEC governed identities is represented by a verified identity
mapping** in this baseline; all 266 are enumerated in Appendix B. This does not
claim that all 266 are physically absent: 214 unidentified BC/BEC objects may
represent some of them, but the correspondence is not established.

Of those 266 repository capabilities, 155 have no literally equal label in the
same Exchange layer (144 BC, 11 BEC); 111 have an equal label only. These are
navigation comparisons, not an authoritative absence/equivalence judgement.
An identity-level list of exactly which repository capabilities are absent cannot
be completed safely until the 214 object identities have explicit cross-references.

A count does establish a lower bound: **at least 138 of the 190 established BC
identities cannot be separately represented by only 52 BC objects**, under a
one-capability/one-semantic-identity interpretation. The available evidence cannot
name those 138 definitively. No analogous absence bound follows for 76 BEC
identities against 162 BEC objects. A single XML capability must not be silently
interpreted as several governed capabilities to erase the BC count discrepancy.

The eleven governed BEC labels with no literal counterpart are:

| Governed BEC name | Navigation key (not a Reference ID) | Source |
| --- | --- | --- |
| Health Product Administration | `capability:health-product-administration` | [enabling:208](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-health-product-administration) |
| Person Identity | `capability:person-identity` | [enabling:115](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) |
| Healthcare Subject | `capability:healthcare-subject` | [enabling:122](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) |
| Client Relationship | `capability:client-relationship` | [enabling:127](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) |
| Client Privacy | `capability:client-privacy` | [enabling:132](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) |
| Clinical Resource Management | `capability:clinical-resource-management` | [enabling:512](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-resource-management-harmonia-relevant) |
| Clinical Workforce Management | `capability:clinical-workforce-management` | [enabling:456](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) |
| Clinical Competency Management | `capability:clinical-competency-management` | [enabling:462](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) |
| Clinical Qualification Management | `capability:clinical-qualification-management` | [enabling:539](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#16-clinical-qualification-management-harmonia-relevant) |
| Assurance Criteria Management | `capability:assurance-criteria-management` | [enabling:686](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#assurance-criteria-management) |
| Governed Assurance | `capability:governed-assurance` | [enabling:694](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance) |

No mapping is approved from these to differently named XML elements. For example,
Person Identity cannot be equated to Person Identity Administration, Health Product
Administration to Health Materiel Administration, or Governed Assurance to Assurance
Service Evaluation merely from their wording or working-publication lineage.

## 5. ID conflicts and inference risks

| Check | Finding |
| --- | --- |
| Exchange semantic identifiers | 417 unique identifiers (228 elements + 189 relationships); no duplicate semantic ID. |
| Existing Exchange Reference IDs | 14 distinct non-empty values, each used once; no duplicate Reference ID. |
| Governed definitions for those 14 IDs | One canonical Enterprise definition per code; no conflicting EC code allocation found in the governed comparison sources. |
| BC/BEC governed complete IDs and ID Aliases | The 266 established capture records have no allocated Canonical ID, local capability code or ID Alias; missing allocations remain unresolved. |
| Exchange UUID-to-repository crosswalk | No literal Exchange element ID occurs in current RADS, the existing LaTeX/JSON publication or scripts. No lineage crosswalk found. |
| Missing-ID cases | 214 mappings cannot be conflict-checked by Reference ID; this is missing evidence, not proof of an ID collision or conflict-free mapping. |

There are **zero positively identified ID conflicts among the 14 exact matches**.
The following unresolved issues must not be treated as safe fallback mappings:

- **Bare-name ambiguity:** RADS has distinct BC and BEC identities both called
  Health Information Exchange; the [canonical conflict statement](../../markdown/02-strategy/capabilities/business-capabilities.md#L1225)
  explicitly preserves them without merging or authorising a BEC rename. This is
  a name/governance conflict, not an EC Reference-ID collision. Layer/name context
  may help navigation but does not supply the missing Exchange identity binding.
- **Credential boundary:** [Credential Management](../../markdown/03-business-architecture/behaviours/04-health-service-operations.md#L238)
  governs operational physical badge/access responsibility and explicitly remains
  distinct from Clinical Qualification Management. Wording cannot transfer either
  description to a different responsibility.
- **Unapproved ancestry:** the external CT1/CT2/CT3 structure and scope annotations
  cannot allocate unresolved RADS BEC tiers or turn the five contextual views into
  capabilities. Even a same-name node at a plausible tier is not an ID match.
- **Renames, consolidation and splits:** a changed label, a publication alias or
  several predecessor names do not prove that one XML object retains one particular
  governed identity. Copying a predecessor's description could change its meaning.
- **UUID/name derivation:** no external allocation algorithm or governed crosswalk
  was supplied. Deriving a slug, guessing a UUID namespace or matching diagram
  position would create a new assertion, not recover authorised identity.
- **Candidate concerns:** the three BC records in Appendix D retain candidate or
  unresolved standing and cannot supply an approved definition just because a
  publication proposes a completed hierarchy or identifier.

## 6. Verification and unchanged artefacts

The audit checks partition all 228 baseline capabilities into exactly 14 E,
111 U and 103 G records, resolve each E Reference ID uniquely, verify the 14 name
prefix differences, and account for all 266 established BC/BEC inventory records
and three separate candidate concerns. It verifies all 15 capture source hashes
and capability source locators and confirms that no semantic identity join uses
names. The existing RADS validation completed PASS with no invalid records.

The repository-required architecture suite completed PASS: **90 tests, zero
failures/errors/skips** (`mvn test -pl paradeigma/paradeigma-test -am
'-Dtest=*ArchitectureTest' -Dsurefire.failIfNoSpecifiedTests=false`, bounded to
300 seconds). Its run log is
`/tmp/harmonia-reference-reconciliation-architecture.log`.

Before/after SHA-256 comparison establishes that both Exchange XML files below
are unchanged. The report is the sole repository file added/changed by this task.

| Unchanged XML artefact | SHA-256 |
| --- | --- |
| [Harmonia-Capability-Architecture.xml](Harmonia-Capability-Architecture.xml) | `ee6c3bfb2ebd5fb590a2b5790fdace59a7e16ba708a6131884cca6b23b7dfc0b` |
| [Harmonia-Capability-Architecture-v2.1a.xml](baseline/Harmonia-Capability-Architecture-v2.1a.xml) | `dbcf7b41ad7909fe27c9babaefa6e7c07810f8518558c672732f394f910efa80` |

## Appendix A — all 228 baseline capability decisions

E = exact Reference-ID match, eligible. U = missing Reference ID with literal
same-category name evidence only, ineligible. G = missing Reference ID and no
literal same-category established inventory name, ineligible. Source links in U
rows are **navigation-only candidates**; they are not crosswalks. No generated
Reference ID is assigned to any missing-ID element. All 103 G records are shown
explicitly below, rather than treated as accepted aliases.

### Business Capability

| No. | Exchange element ID | Exchange capability name | XML tier | Existing Reference ID | Decision | Evidence / limit |
| --- | --- | --- | --- | --- | --- | --- |
| 1 | `id-9f17f836d6e05d95a778c4cc4e052996` | Individual Care Delivery | CT1 | Absent | U | Equal-name evidence only: [business:152](../../markdown/02-strategy/capabilities/business-capabilities.md#01-individual-care-delivery) |
| 2 | `id-62a4e116f5a951a9ad5a7b2f730072c8` | Individual Health Assessment | CT2 | Absent | U | Equal-name evidence only: [business:320](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) |
| 3 | `id-cbafdc119f6a513bb3fea09f88339fbe` | Health History Assessment | CT3 | Absent | U | Equal-name evidence only: [business:326](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) |
| 4 | `id-7f4dfd1742555c729739020b88325e4d` | Clinical Examination | CT3 | Absent | U | Equal-name evidence only: [business:327](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) |
| 5 | `id-ecc73fa3f4385a05adc08b66033b1ade` | Health Status Measurement | CT3 | Absent | U | Equal-name evidence only: [business:328](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) |
| 6 | `id-4daf3c8ca8155d48b1a88636f06d9be8` | Clinical Determination | CT2 | Absent | U | Equal-name evidence only: [business:186](../../markdown/02-strategy/capabilities/business-capabilities.md#0102-clinical-determination) |
| 7 | `id-cb308101abb15933bef59fdf6bb5a06c` | Clinical Finding Interpretation | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 8 | `id-ce5fb9aa138d51d6882fdbf003ff995b` | Diagnosis & Problem Determination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 9 | `id-d6ad1a59674a5776866343b73fd01be2` | Health Risk & Prognostic Determination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 10 | `id-ed8ad37da4e05146ad1598281861e454` | Individual Care Planning | CT2 | Absent | U | Equal-name evidence only: [business:211](../../markdown/02-strategy/capabilities/business-capabilities.md#0103-individual-care-planning) |
| 11 | `id-23b9fc6a502e5d6e9bcbf65d6e4f8345` | Care Goal Definition | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 12 | `id-d04b07bddef25664a8411627c1915b8d` | Care Response Planning | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 13 | `id-88c88cae931a551b930f455b82c63aa5` | Clinical Intervention | CT2 | Absent | U | Equal-name evidence only: [business:237](../../markdown/02-strategy/capabilities/business-capabilities.md#0104-clinical-intervention) |
| 14 | `id-67610b928dbe5690a2f089ecddd9963d` | Individual Health Monitoring | CT2 | Absent | U | Equal-name evidence only: [business:336](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) |
| 15 | `id-2b3a5a4111ae527bbbc6c5e84de3ad8a` | Health State Monitoring | CT3 | Absent | U | Equal-name evidence only: [business:342](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) |
| 16 | `id-a4b1b25b3893529195ccb230921de502` | Care Response Monitoring | CT3 | Absent | U | Equal-name evidence only: [business:343](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) |
| 17 | `id-6018c5b1f4ca53149a178ecfe6927ced` | Individual Care Review | CT2 | Absent | U | Equal-name evidence only: [business:351](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-care-review) |
| 18 | `id-3d3aae478cd35e3c87f246e6d5611452` | Care Intent Appropriateness Review | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 19 | `id-961ab14e4a355e7c9033ac9d149a198a` | Care Effectiveness Review | CT3 | Absent | U | Equal-name evidence only: [business:358](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-care-review) |
| 20 | `id-f78a7520ea80532b8423fc652b12ff6a` | Care Access & Coordination | CT1 | Absent | U | Equal-name evidence only: [business:366](../../markdown/02-strategy/capabilities/business-capabilities.md#02-care-access--coordination) |
| 21 | `id-236d2d03222355b7a740fe7a2a19ff22` | Health Rights, Advocacy & Participation | CT1 | Absent | U | Equal-name evidence only: [business:441](../../markdown/02-strategy/capabilities/business-capabilities.md#03-health-rights-advocacy--participation) |
| 22 | `id-8f7e1834c1285f8e9074a8f3639c9f04` | Diagnostic, Therapeutic & Clinical Support Services | CT1 | Absent | U | Equal-name evidence only: [business:451](../../markdown/02-strategy/capabilities/business-capabilities.md#04-diagnostic-therapeutic--clinical-support-services) |
| 23 | `id-8789275fcf145364aaac9cfbe25bbce6` | Health Products & Clinical Technology | CT1 | Absent | U | Equal-name evidence only: [business:498](../../markdown/02-strategy/capabilities/business-capabilities.md#05-health-products--clinical-technology) |
| 24 | `id-e31496b3af1450f6ad7f931218f14b60` | Clinical Quality, Safety & Improvement | CT1 | Absent | U | Equal-name evidence only: [business:526](../../markdown/02-strategy/capabilities/business-capabilities.md#06-clinical-quality-safety--improvement) |
| 25 | `id-a0a61a36bcd45bf28f83c5ec1c6c5579` | Clinical Safety Management | CT2 | Absent | U | Equal-name evidence only: [business:534](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) |
| 26 | `id-aa118374de83598fa0d455e546899188` | Clinical Risk Identification | CT3 | Absent | U | Equal-name evidence only: [business:540](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) |
| 27 | `id-b09b2931af455e1f816a1e0dd2566cd1` | Clinical Risk Mitigation Management | CT3 | Absent | U | Equal-name evidence only: [business:541](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) |
| 28 | `id-495b46bdf1f05f93a7f948a5e95d7663` | Clinical Risk Response Management | CT3 | Absent | U | Equal-name evidence only: [business:542](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) |
| 29 | `id-e9b2533401765481aa27d5c901bd8fd2` | Clinical Quality Management | CT2 | Absent | U | Equal-name evidence only: [business:550](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) |
| 30 | `id-143c6a0eb6675280a374d05aac61fe58` | Clinical Quality Criteria Management | CT3 | Absent | U | Equal-name evidence only: [business:556](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) |
| 31 | `id-3940059fa2735c8fa10020170420ee91` | Clinical Quality Evidence Management | CT3 | Absent | U | Equal-name evidence only: [business:557](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) |
| 32 | `id-943c0e41cdf55f05ac5fdd126cbcae3d` | Clinical Quality Review & Assessment Management | CT3 | Absent | U | Equal-name evidence only: [business:558](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) |
| 33 | `id-3e15dac60a7851bf820f6269d07b6a37` | Clinical Quality Reporting | CT3 | Absent | U | Equal-name evidence only: [business:559](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) |
| 34 | `id-0efaf106654e5f19bf85139a3a93a4fb` | Community Health & Wellbeing | CT1 | Absent | U | Equal-name evidence only: [business:567](../../markdown/02-strategy/capabilities/business-capabilities.md#07-community-health--wellbeing) |
| 35 | `id-63f5840bf3bf5ba5828c7d2b13795221` | Preventative Health | CT2 | Absent | U | Equal-name evidence only: [business:575](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 36 | `id-ea890856698a55b686338449e9b99984` | Preventative Health Problem Identification | CT3 | Absent | U | Equal-name evidence only: [business:581](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 37 | `id-48544f17a07c53839f82a273cce0d86e` | Preventative Health Programme Management | CT3 | Absent | U | Equal-name evidence only: [business:582](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 38 | `id-c9caa2cf828a5c6f92cc681ec086e060` | Preventative Health Education Services | CT3 | Absent | U | Equal-name evidence only: [business:583](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 39 | `id-c31871a53f5255b08d5210ea58ce2683` | Preventative Health Programme Monitoring | CT3 | Absent | U | Equal-name evidence only: [business:584](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 40 | `id-1703ff7fa038538e9aa18358f56d18b0` | Preventative Health Reporting | CT3 | Absent | U | Equal-name evidence only: [business:585](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 41 | `id-abb6bc8768f151f7889ffeb2382f7d01` | Preventative Health Cohort Management | CT3 | Absent | U | Equal-name evidence only: [business:586](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) |
| 42 | `id-f8a453be4c9450b9958ae54ca6630f30` | Population Health & Health-System Planning | CT1 | Absent | U | Equal-name evidence only: [business:594](../../markdown/02-strategy/capabilities/business-capabilities.md#08-population-health--health-system-planning) |
| 43 | `id-1441d728bfb35d509cf6f8a39ed3f504` | Health Information & Knowledge Management | CT1 | Absent | U | Equal-name evidence only: [business:640](../../markdown/02-strategy/capabilities/business-capabilities.md#09-health-information--knowledge-management) |
| 44 | `id-f485d54cc43454ae9896ccbb5398f586` | Standards, Semantics & Reference Governance | CT1 | Absent | U | Equal-name evidence only: [business:687](../../markdown/02-strategy/capabilities/business-capabilities.md#10-standards-semantics--reference-governance) |
| 45 | `id-19f88e8f2c8856658cdbe0e57d83aa79` | Connected Health Services | CT1 | Absent | U | Equal-name evidence only: [business:734](../../markdown/02-strategy/capabilities/business-capabilities.md#11-connected-health-services) |
| 46 | `id-0b2a8f3deee5540aa21ed3a451769cd0` | Security, Privacy & Digital Trust | CT1 | Absent | U | Equal-name evidence only: [business:762](../../markdown/02-strategy/capabilities/business-capabilities.md#12-security-privacy--digital-trust) |
| 47 | `id-30c9efe556ad537e8bcf6af838d48792` | Health Research & Innovation | CT1 | Absent | U | Equal-name evidence only: [business:828](../../markdown/02-strategy/capabilities/business-capabilities.md#13-health-research--innovation) |
| 48 | `id-395b9859599a57e485ed1bf82210881d` | Health Service Direction & Stewardship | CT1 | Absent | U | Equal-name evidence only: [business:868](../../markdown/02-strategy/capabilities/business-capabilities.md#14-health-service-direction--stewardship) |
| 49 | `id-86c2aeb7054d55b1bb087ac44fbfe993` | Workforce & Organisational Capability | CT1 | Absent | U | Equal-name evidence only: [business:910](../../markdown/02-strategy/capabilities/business-capabilities.md#15-workforce--organisational-capability) |
| 50 | `id-fe62aa38cb26553ba0ff061f642a8979` | Corporate Resources & Enterprise Services | CT1 | Absent | U | Equal-name evidence only: [business:952](../../markdown/02-strategy/capabilities/business-capabilities.md#16-corporate-resources--enterprise-services) |
| 51 | `id-fc4efeb712585dd4857f288f033767c3` | Health Service Governance | CT1 | Absent | U | Equal-name evidence only: [business:997](../../markdown/02-strategy/capabilities/business-capabilities.md#17-health-service-governance) |
| 52 | `id-ba1236cba7d65592984a7f53f137293c` | Health Service Assurance | CT1 | Absent | U | Equal-name evidence only: [business:1037](../../markdown/02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) |

### Business Enabling Capability

| No. | Exchange element ID | Exchange capability name | XML tier | Existing Reference ID | Decision | Evidence / limit |
| --- | --- | --- | --- | --- | --- | --- |
| 53 | `id-0d159c312270578dbbc6f6f886aa6d10` | Entity Information Enablement | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 54 | `id-ff7557da73fd5a98a3f122fa6433c82f` | Client Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:112](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) |
| 55 | `id-2cb3c9646bc85fa3b1565c767a7febc7` | Person Identity Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 56 | `id-22aae6e7573c5fccb607f6e94ba0093f` | Healthcare Subject Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 57 | `id-dfdd87cee92a5664a788b9e3e4125793` | Client Relationship Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 58 | `id-5fead29652395512b50adf3cfddfb13f` | Client Privacy Guardrail Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 59 | `id-b12071ab88dc573b9ab955165b063211` | Provider Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:137](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-provider-administration) |
| 60 | `id-6b457e430448545eb286494c9570da07` | Practitioner Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 61 | `id-38731210bb295216a82be56c5ab740d6` | Practitioner Role Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 62 | `id-efe6217f4808594b80ec8a521762973a` | Vendor Liaison | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 63 | `id-dcfd4c1988fd519ba4fc6679ad0bd0d1` | Organisation Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:158](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-organisation-administration) |
| 64 | `id-23f9ff8b179358d0ac2f9df5cc5f51bd` | Healthcare Organisation Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 65 | `id-6d78c985724f5ec3a7d7d7311d2c60fe` | Location Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:174](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-location-administration) |
| 66 | `id-137082c437f45d3cb76d5c1d2fbe8c7e` | Healthcare Location Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 67 | `id-dc3e456c573955f19bfc97699588ff53` | Care-Place Definition Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 68 | `id-decc03bc70e15e95861abae202a677a1` | Health Service Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:189](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-health-service-administration) |
| 69 | `id-e3fa1979104e5832826677da087a3fd6` | Healthcare Service Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 70 | `id-6f5eb49acff458288ab91a3efabbcf54` | Health Materiel Administration | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 71 | `id-ccd1b46897965a9d9c1925310a4a756f` | Health Materiel Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 72 | `id-bb3df556a66459769a572091579ae6f9` | Health Materiel Catalogue & Formulary Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 73 | `id-21d5725b83e759028652c16d0f87a62a` | Health Materiel Traceability Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 74 | `id-6c5c2b29afba51408d02a1959fa680b2` | Clinical Device Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:215](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-device-administration) |
| 75 | `id-246263f4a13356ff96ee37e3ca454b26` | Clinical Device Profile Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 76 | `id-54cae5c64b96599f93a28c49bd4c136a` | Device Utilisation Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 77 | `id-9a121fe2709e56df98d689359a793dea` | Device OA&M Metadata Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 78 | `id-ecc645e49b5a567ba2e3843c564d708c` | Device Connectivity Compliance Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 79 | `id-47a8d188ce4c516f9865b6523723e1f3` | Care Administration Enablement | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 80 | `id-a7f5492d52235705a4354970d8dabf62` | Client-Centred Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:279](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-centred-administration-harmonia-relevant) |
| 81 | `id-0f0ebf04da8e5f5784bea0f593591996` | Referral Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:284](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-referral-administration-harmonia-relevant) |
| 82 | `id-b39ff7009d93582c889c9443263212ac` | Referral Intake & Progression | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 83 | `id-87aa79f1e50459ef8826a46449a73600` | Scheduling Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:290](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-scheduling-administration-harmonia-relevant) |
| 84 | `id-64b9899afc645f7190e31357a35f65dd` | Appointment & Booking Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 85 | `id-3caf2ebccbbe5caf9096e29855953186` | Episode & Encounter Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:295](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-episode--encounter-administration-harmonia-core) |
| 86 | `id-f84d4877352f5247adf6d6539b19019d` | Order Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:301](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core) |
| 87 | `id-e6673042d37d58c7851c775235aee004` | Closed-Loop Order Progression | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 88 | `id-2dda57f0b2de58bdb1d54a9e3b08747c` | Diagnostic Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:308](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-diagnostic-administration-harmonia-core) |
| 89 | `id-baa0e64c54175bba86f21e766140414b` | Medication Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:316](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-medication-administration-harmonia-relevant) |
| 90 | `id-653effd194c05bec9ce8e248f5272ff8` | Procedure Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:322](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-procedure-administration-harmonia-relevant) |
| 91 | `id-48014b9acfa152b38e5b100233156e19` | Clinical Record Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:327](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#9-clinical-record-administration-harmonia-core) |
| 92 | `id-85f2fb44c7c85c0c9c7c8398073caa9c` | Clinical Documentation Lifecycle Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 93 | `id-14ca90e0382a539ea11b61e33dc44336` | Care Plan Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:333](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-care-plan-administration-harmonia-relevant) |
| 94 | `id-02ffa3292036539b8eea9a249dc89083` | Care Plan Lifecycle & Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 95 | `id-36dc392f62815be1a97a7bb14799df15` | Clinical Communication Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:337](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-clinical-communication-administration-harmonia-core) |
| 96 | `id-24f0d184ab385eb9a2fa8d8bc39ec06e` | Funding & Coverage Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:343](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-funding--coverage-administration-adjacent) |
| 97 | `id-740d5118859d5f269f7dbdfed7de3447` | Clinical Charging & Claims Administration | CT2 | Absent | U | Equal-name evidence only: [enabling:346](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#13-clinical-charging--claims-administration-adjacent) |
| 98 | `id-bfc17f5b03af5af399503331f1fb164c` | Clinical Service Dependency Management | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 99 | `id-08a53c1d0c1f508c9bd64ac5b4ec7637` | Clinical Assessment | CT2 | Absent | U | Equal-name evidence only: [enabling:361](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 100 | `id-d94d9c26f8ef51bc8ff43fd695bf2f7f` | Primary Care | CT2 | Absent | U | Equal-name evidence only: [enabling:389](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-primary-care-harmonia-relevant) |
| 101 | `id-b00661d292e7536ab049285478d9bacb` | Acute Care | CT2 | Absent | U | Equal-name evidence only: [enabling:394](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-acute-care-harmonia-relevant) |
| 102 | `id-7291ef30d6005123b057c0538c79b12e` | Emergency Care | CT2 | Absent | U | Equal-name evidence only: [enabling:399](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-emergency-care-harmonia-relevant) |
| 103 | `id-2cb060f64bee537d9c9e3696ff37cc19` | Inpatient Care | CT2 | Absent | U | Equal-name evidence only: [enabling:404](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-inpatient-care-harmonia-relevant) |
| 104 | `id-352c8e395f5359748d90c8de19187fd9` | Ambulatory Care | CT2 | Absent | U | Equal-name evidence only: [enabling:366](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 105 | `id-cd305a8616a151f3945b5cd6bbfd197d` | Surgical & Procedural Care | CT2 | Absent | U | Equal-name evidence only: [enabling:367](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 106 | `id-d57ad5619be15d78beac5ac06a087209` | Diagnostic Services | CT2 | Absent | U | Equal-name evidence only: [enabling:409](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-diagnostic-services-harmonia-relevant) |
| 107 | `id-2331925a475f51cea884fb63a59f8115` | Therapeutic Services | CT2 | Absent | U | Equal-name evidence only: [enabling:369](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 108 | `id-3b661002cbf252c8832f33793a70961e` | Medication Therapy | CT2 | Absent | U | Equal-name evidence only: [enabling:414](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-medication-therapy-harmonia-relevant) |
| 109 | `id-311a6024f0d653c1825dcc76b50c295f` | Allied Health | CT2 | Absent | U | Equal-name evidence only: [enabling:371](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 110 | `id-99057a12f02a510f8d4888436b221c48` | Preventive Care | CT2 | Absent | U | Equal-name evidence only: [enabling:419](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-preventive-care-harmonia-relevant) |
| 111 | `id-6dd0835babf85d66ad74bd714f3ee2a1` | Rehabilitation | CT2 | Absent | U | Equal-name evidence only: [enabling:373](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) |
| 112 | `id-3fa8a3bab1b955a48260a22ac167a560` | Community Care | CT2 | Absent | U | Equal-name evidence only: [enabling:424](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#14-community-care-harmonia-relevant) |
| 113 | `id-b817a50ccfb250ee9535efdc7b6babf0` | Outreach Care | CT2 | Absent | U | Equal-name evidence only: [enabling:429](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#15-outreach-care-harmonia-relevant) |
| 114 | `id-0308c16e830754728ef94c1c8108cf81` | Remote & Virtual Care | CT2 | Absent | U | Equal-name evidence only: [enabling:434](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#16-remote--virtual-care-harmonia-relevant) |
| 115 | `id-c63acb99ac005ecab94e0f8b31ce007f` | Health Service Operations Enablement | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 116 | `id-126dd25cd89653de8ae301c194eb2f4a` | Clinic & Practice Operations | CT2 | Absent | U | Equal-name evidence only: [enabling:481](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-clinic--practice-operations-harmonia-relevant) |
| 117 | `id-8c965b1d0d8859c59a9ebfa3034522a1` | Session & Queue Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 118 | `id-963f3158331d5d1898f2426bf07fa426` | Ward Operations | CT2 | Absent | U | Equal-name evidence only: [enabling:486](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-ward-operations-harmonia-relevant) |
| 119 | `id-3a7155fd762452ce949bcd777c8c9595` | Ward Logistics Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 120 | `id-c6390d844ced5832b12f25cc3924de73` | Theatre Operations | CT2 | Absent | U | Equal-name evidence only: [enabling:491](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-theatre-operations-harmonia-relevant) |
| 121 | `id-10893dccc3055d09b5efa3abe205645f` | Perioperative Logistics Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 122 | `id-fa58f7ef35f45c41ba91f08689bd299a` | Emergency Department Operations | CT2 | Absent | U | Equal-name evidence only: [enabling:496](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-emergency-department-operations-harmonia-relevant) |
| 123 | `id-d5be8e3ad9485b0b82e72038035dc383` | Emergency Department Operational Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 124 | `id-e94fc1de50725fc89bacd15e29c735e5` | Outpatient Operations | CT2 | Absent | U | Equal-name evidence only: [enabling:501](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-outpatient-operations-harmonia-relevant) |
| 125 | `id-559bdd8011945098a39423836ab08a99` | Outpatient Flow Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 126 | `id-6f3591fe97ba51bba76293048509cdd3` | Bed & Care-Place Management | CT2 | Absent | U | Equal-name evidence only: [enabling:506](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-bed--care-place-management-harmonia-core) |
| 127 | `id-67560ad6bae75e998cee483a16b067b6` | Bed & Care-Place State Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 128 | `id-a7145900de2e5627a79503de59bb462e` | Bed & Care-Place Turnover Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 129 | `id-122cf0d3f0055bea8cc85ff72d4d23ee` | Service Capacity Management | CT2 | Absent | U | Equal-name evidence only: [enabling:516](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-service-capacity-management-harmonia-relevant) |
| 130 | `id-3fe3864d76ed5ccd94514b4cd1a331c1` | Service Demand & Capacity Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 131 | `id-d47de4c452e955809f9b93d33395c419` | Service Establishment Support | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 132 | `id-dd26c85334f95d46888c873677c3129e` | Clinical Workforce Capability Monitoring | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 133 | `id-8ecaf6c96820562691a86a7a7b1dbd26` | Clinical Rostering | CT2 | Absent | U | Equal-name evidence only: [enabling:457](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) |
| 134 | `id-5ca32b26e8b256b19c1c465bb75ce7ce` | Clinical Roster Capture & Communication | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 135 | `id-859c08b9ae82509bb2b1fea5d5f6d121` | Mobile Staff Management | CT2 | Absent | U | Equal-name evidence only: [enabling:520](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-mobile-staff-management-harmonia-relevant) |
| 136 | `id-a69cb5bcd9c658af9a69bf2b775f091a` | Mobile Worker Dispatch & Safety Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 137 | `id-16fe8798e48657769fd3999afbc8cdf6` | On-Call Management | CT2 | Absent | U | Equal-name evidence only: [enabling:526](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-on-call-management-harmonia-relevant) |
| 138 | `id-a434090c4ae25508abcc66dd61210f71` | On-Call Availability & Contact Resolution | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 139 | `id-1df926232bc156348ba13340e2facd93` | Work Allocation & Dispatch | CT2 | Absent | U | Equal-name evidence only: [enabling:530](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#13-work-allocation--dispatch-harmonia-core) |
| 140 | `id-99c1e2176d3f53b0942bf80e2befda71` | Operational Work Assignment & Dispatch Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 141 | `id-4701f92969965a7390095f36e1a8c5da` | Clinical Training & Education | CT2 | Absent | U | Equal-name evidence only: [enabling:461](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) |
| 142 | `id-d8818559ec0550c99acef678cd553252` | Clinical Competency Administration | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 143 | `id-1e4490f462e45d7ab8b5f5eca2aa2e89` | Clinical Qualification Administration | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 144 | `id-79c21f44dcd05defaf42392391526d56` | Organisation Clinical Certification & Compliance Support | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 145 | `id-03dea8a9dba557338b7d0b0569a2991f` | Provider Clinical Qualification Tracking | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 146 | `id-5652c0a06d095f17a63623a23b6a3e8a` | Credential Management | CT2 | Absent | U | Equal-name evidence only: [hso:239](../../markdown/03-business-architecture/behaviours/04-health-service-operations.md#212-credential-management) |
| 147 | `id-816cf3f31494502287046e5fd304a2f1` | Clinical Service Credential Definition Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 148 | `id-f58d9424b3cd5e4f9a2ee2399ef447cc` | Clinical Supervision | CT2 | Absent | U | Equal-name evidence only: [enabling:464](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) |
| 149 | `id-7399f412e27e5eb98942140ef0ecd355` | Clinical Escalation Path Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 150 | `id-9aed294f7bd255d0b5383748a17077a8` | Patient Transport | CT2 | Absent | U | Equal-name evidence only: [enabling:543](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#18-patient-transport-harmonia-core) |
| 151 | `id-cd8216b770fe5ca1acf76fe3641ed62c` | Patient Transfer Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 152 | `id-9b3dfe55c10c5e0f87bf9d2c029e7431` | Clinical Logistics Coordination | CT2 | Absent | U | Equal-name evidence only: [enabling:548](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#19-clinical-logistics-coordination-harmonia-relevant) |
| 153 | `id-07adcaeb8cba5cc489e4818134e393b4` | Specimen Metadata Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 154 | `id-ec4c7acdd0585187b5106a3515050171` | Specimen Movement Traceability | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 155 | `id-b2f798c4fba25d01bcef0468cd2e03f9` | Discharge Management | CT2 | Absent | U | Equal-name evidence only: [enabling:552](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#20-discharge-management-harmonia-core) |
| 156 | `id-1274a8f0c2b45b74af0405eba3be2b87` | Discharge Progression & Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 157 | `id-f97db18cfd4854abbe14020229fac298` | Shared Information & Coordination Enablement | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 158 | `id-19b2fcb11e915c7c95937cf0f0cc3c26` | Patient Clinical Record | CT2 | Absent | U | Equal-name evidence only: [enabling:608](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-patient-clinical-record-harmonia-core) |
| 159 | `id-947f9f3132e755909239d54b9bddcb05` | Longitudinal Clinical Record Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 160 | `id-4afe7a0eea9d5371a7e0882e4ce086dc` | Linked Information Curation | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 161 | `id-b53ad897fec858d8a79678166d038341` | Health Information Exchange | CT2 | Absent | U | Equal-name evidence only: [enabling:614](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-health-information-exchange-harmonia-core) |
| 162 | `id-1ffb7d0e95b15e81a4fcd3b62dec6db9` | HIE Requirement Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 163 | `id-7aaa8d8c77955de9966132c939773d57` | HIE Implementation Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 164 | `id-9fe0dded960259fe8b3ff544a7d80c40` | HIE Endpoint State Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 165 | `id-26445b6da97f54dd96ffcb6c16bfb368` | Health Information Access | CT2 | Absent | U | Equal-name evidence only: [enabling:621](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-health-information-access-harmonia-core) |
| 166 | `id-76eb10a929005533963a7fe2500442ec` | Governed Health Information Retrieval | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 167 | `id-bfa17ee995d254028c02831a58562c6f` | Health Information Communication | CT2 | Absent | U | Equal-name evidence only: [enabling:626](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-health-information-communication-harmonia-core) |
| 168 | `id-6d55717492415c51ba369e6577d479d2` | Patient Notification Services | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 169 | `id-c48eff1ae94a567286b9a42776d49b3e` | Patient Task Communication Services | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 170 | `id-78160df0e80a58b4a003b6c508b71b1c` | Patient Self-Service | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 171 | `id-f381159ee8b85830a9e8683ddbe7e4bc` | Patient Health Library Access | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 172 | `id-e08ec9af139752abb94248fca3720623` | Health Information Control | CT2 | Absent | U | Equal-name evidence only: [enabling:630](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-health-information-control-harmonia-core) |
| 173 | `id-c8fbaaea6bb95acf956f9cfda8abffec` | Patient Health Information Restriction Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 174 | `id-873631d924bd52b1ab9c6fa2f43c4dff` | Patient Health Information Filter Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 175 | `id-4095dc622a8e5a14bbab403719cb83fe` | Patient Health Information Disclosure Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 176 | `id-db77a56d0232501396424b873ce383fb` | Information Asset Stewardship Management | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 177 | `id-a6e161fdb0b75c5ba83f5180c2f94d06` | Information Asset Definition | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 178 | `id-af8c47acb1fd57378691f03c7eed42c1` | Information Asset Tag Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 179 | `id-e457171963d55dfd80140f132e175278` | Information Content Maintainer Management | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 180 | `id-3199bd09ec665f8d9462b5b31c4ee5ed` | Data Scientist Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 181 | `id-5417235e38b558ff9ff1bef509b0e998` | Data Provenance Capture | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 182 | `id-f50bdfb2f6c652a18c4fc4eb5a739230` | Information Product Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 183 | `id-9c9dbab4b5ca5755a5683e5f5f8f426f` | Clinical Knowledge Services | CT2 | Absent | U | Equal-name evidence only: [enabling:636](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-clinical-knowledge-services-harmonia-relevant) |
| 184 | `id-d21dd2e217595cd2bc8835f1197a630f` | Clinical Knowledge Request Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 185 | `id-3047363b34085941a122267f063bd8a4` | Clinical Knowledge Artefact Distribution Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 186 | `id-d4d202241d2f573899138b0dd79e1090` | Governed Reference Dataset Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 187 | `id-738afeaabb865edbb077876659b2495f` | Clinical Collaboration | CT2 | Absent | U | Equal-name evidence only: [enabling:641](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-collaboration-harmonia-core) |
| 188 | `id-1e0e938bb1fd5fd3bbfc1d8284a383cf` | Clinical Context Collaboration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 189 | `id-912d23c11441584a9cd4d6f9d055bc4c` | General Collaboration | CT2 | Absent | U | Equal-name evidence only: [enabling:647](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-general-collaboration-harmonia-relevant) |
| 190 | `id-9c170b8d957f59e68e8342cd06b5760f` | Operational Collaboration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 191 | `id-f32f886a9494596eacc820ad92af6056` | Workflow & Activity Coordination | CT2 | Absent | U | Equal-name evidence only: [enabling:651](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#9-workflow--activity-coordination-harmonia-core) |
| 192 | `id-7fdefa4d9eb6509d8e0e286dcec9725c` | External Task Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 193 | `id-72c554c466f95421a4f653686013794e` | External Task Status & Traceability Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 194 | `id-2c72d4bed06c54e68a70e97e880a44a6` | Activity Routing Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 195 | `id-ce3a63920278532ab5e4693638eda19f` | Work Boundary Governance | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 196 | `id-b94dea7ef8875d63b2618585b0d74cda` | Calendar Management | CT2 | Absent | U | Equal-name evidence only: [enabling:658](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-calendar-management-harmonia-core) |
| 197 | `id-3fff7bec706c577dbffa92c12c30e505` | Operational & Clinical Calendar Coordination | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 198 | `id-8c3ad242b1ee5d50ab08ba18551e1394` | Presentation Services | CT2 | Absent | U | Equal-name evidence only: [enabling:663](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-presentation-services-harmonia-core) |
| 199 | `id-dc18eee312cf53878adeef4e072f2bba` | Contextual Information Presentation | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 200 | `id-e4ba5bed506e52a087ec1c5a5e30e5e5` | Information Design Governance | CT2 | Absent | U | Equal-name evidence only: [enabling:667](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-information-design-governance-harmonia-core) |
| 201 | `id-0c1f12c37f965051891bc815736a46ef` | Enterprise Canonical/Reusable Model Design | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 202 | `id-afa3800b85ab5b919adbb7ea034c57e5` | Enterprise Canonical/Reusable Model Library Management | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 203 | `id-6645311b0b525345bc3c18d4575bb6a1` | Enterprise Canonical/Reusable Model Utilisation Support | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 204 | `id-4e8b1e2f71a35dd8ba248773aa0ad152` | Enterprise Reference Data Stewardship | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 205 | `id-1632c993befa50348f82d0ccc31150b1` | Health Informatics Management | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 206 | `id-3295535332ff5eac800088ec806863e3` | Research Information Curation | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 207 | `id-847a3dd1d65a59a28fafc18388794bbc` | Population Health Information Reporting | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 208 | `id-39f1b41cce0557e4bbe89a95d159fe00` | Service Planning Data Management | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 209 | `id-07329024afc551218062e1f00ea0d1f3` | Independent Assurance Enablement | CT1 | Absent | G | No literal same-category established inventory entry; no mapping |
| 210 | `id-925e627ba8945dd692317c25c9638647` | Assurance Specification | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 211 | `id-e4fa9b97aca051739a6f689daf76e8b2` | Assurance Design | CT3 | Absent | U | Equal-name evidence only: [enabling:680](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#assurance-design) |
| 212 | `id-b4605e9469835b8eb4a82e56179fc409` | Assurance Criteria Administration | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |
| 213 | `id-af50dc4fa7eb5328aa0a69b0beee3d1d` | Assurance Service Review | CT2 | Absent | G | No literal same-category established inventory entry; no mapping |
| 214 | `id-c63e45819fcd5297b177de67d8b6bb3a` | Assurance Service Evaluation | CT3 | Absent | G | No literal same-category established inventory entry; no mapping |

### Enterprise Capability

| No. | Exchange element ID | Exchange capability name | XML tier | Existing Reference ID | Decision | Evidence / limit |
| --- | --- | --- | --- | --- | --- | --- |
| 215 | `id-d53727d908365757a293b0896b2c769b` | EC-01 Managed Entity & Relationship | EC | EC-01 | E | [Governed EC-01](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-01-managed-entity--relationship) |
| 216 | `id-06e038737c5355edbc0591e08c594ab0` | EC-02 Context Management | EC | EC-02 | E | [Governed EC-02](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-02-context-management) |
| 217 | `id-5891936941f65d20afad86b0697879c7` | EC-03 Managed State & Lifecycle | EC | EC-03 | E | [Governed EC-03](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-03-managed-state--lifecycle) |
| 218 | `id-9e79bc474aed516c8c6752ab81b63d6b` | EC-04 Information Management | EC | EC-04 | E | [Governed EC-04](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-04-information-management) |
| 219 | `id-e11f02764fe55ff29c0782db21198040` | EC-05 Search & Discovery | EC | EC-05 | E | [Governed EC-05](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-05-search--discovery) |
| 220 | `id-a6f6d30d8fce5ac78f60f7aefa71b28e` | EC-06 Policy & Control | EC | EC-06 | E | [Governed EC-06](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-06-policy--control) |
| 221 | `id-d71457435a085e749ffb1e1a3b551228` | EC-07 Provenance & Traceability | EC | EC-07 | E | [Governed EC-07](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-07-provenance--traceability) |
| 222 | `id-dbd66e69788450649e184966aa724797` | EC-08 Interoperability & Exchange | EC | EC-08 | E | [Governed EC-08](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-08-interoperability--exchange) |
| 223 | `id-770ebb3e3bdc5f2bbe77b2bfd55bd74d` | EC-09 Event & Subscription | EC | EC-09 | E | [Governed EC-09](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-09-event--subscription) |
| 224 | `id-49d4f69ebeeb5ea782a52172fb3e8a7b` | EC-10 Activity & Execution | EC | EC-10 | E | [Governed EC-10](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-10-activity--execution) |
| 225 | `id-82cd1f6a3e2352639e22c542f5cb3a9d` | EC-11 Interaction & Experience | EC | EC-11 | E | [Governed EC-11](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-11-interaction--experience) |
| 226 | `id-9bbd142f818e50d39a3d0429f74b72b4` | EC-12 Operational Assurance | EC | EC-12 | E | [Governed EC-12](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-12-operational-assurance) |
| 227 | `id-7d6f635ffb6f5c6181bde1d07b859662` | EC-13 Semantic Governance & Conformance | EC | EC-13 | E | [Governed EC-13](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-13-semantic-governance--conformance) |
| 228 | `id-4dbf578f52bd5b3b9301b9807ce60f30` | EC-14 Service Guardian | EC | EC-14 | E | [Governed EC-14](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-14-service-guardian) |

## Appendix B — governed BC/BEC identities without a verified Exchange mapping

All entries below are established identity/responsibility assertions located in
RADS via exact source evidence. The reference-capture key is shown solely to
locate the record; it is **neither an allocated Canonical ID nor an Exchange
Reference ID**. “Equal label only” is unresolved correspondence, not representation.
“No equal label” is not proof of absence under another label. Source evidence
remains authoritative; a candidate capture summary is not a replacement definition.

### 190 established Business Capabilities

| No. | Navigation key | Governed name | Governed tier | Source assertion | Exchange representation |
| --- | --- | --- | --- | --- | --- |
| 1 | `capability:individual-care-delivery` | Individual Care Delivery | CT1 | [business:152](../../markdown/02-strategy/capabilities/business-capabilities.md#01-individual-care-delivery) | Equal label only; identity unresolved |
| 2 | `capability:individual-health-assessment` | Individual Health Assessment | CT2 | [business:320](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) | Equal label only; identity unresolved |
| 3 | `capability:clinical-determination` | Clinical Determination | CT2 | [business:186](../../markdown/02-strategy/capabilities/business-capabilities.md#0102-clinical-determination) | Equal label only; identity unresolved |
| 4 | `capability:individual-care-planning` | Individual Care Planning | CT2 | [business:211](../../markdown/02-strategy/capabilities/business-capabilities.md#0103-individual-care-planning) | Equal label only; identity unresolved |
| 5 | `capability:clinical-intervention` | Clinical Intervention | CT2 | [business:237](../../markdown/02-strategy/capabilities/business-capabilities.md#0104-clinical-intervention) | Equal label only; identity unresolved |
| 6 | `capability:individual-health-monitoring` | Individual Health Monitoring | CT2 | [business:336](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) | Equal label only; identity unresolved |
| 7 | `capability:individual-care-review` | Individual Care Review | CT2 | [business:351](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-care-review) | Equal label only; identity unresolved |
| 8 | `capability:care-access--coordination` | Care Access & Coordination | CT1 | [business:366](../../markdown/02-strategy/capabilities/business-capabilities.md#02-care-access--coordination) | Equal label only; identity unresolved |
| 9 | `capability:health-rights-advocacy--participation` | Health Rights, Advocacy & Participation | CT1 | [business:441](../../markdown/02-strategy/capabilities/business-capabilities.md#03-health-rights-advocacy--participation) | Equal label only; identity unresolved |
| 10 | `capability:diagnostic-therapeutic--clinical-support-services` | Diagnostic, Therapeutic & Clinical Support Services | CT1 | [business:451](../../markdown/02-strategy/capabilities/business-capabilities.md#04-diagnostic-therapeutic--clinical-support-services) | Equal label only; identity unresolved |
| 11 | `capability:health-products--clinical-technology` | Health Products & Clinical Technology | CT1 | [business:498](../../markdown/02-strategy/capabilities/business-capabilities.md#05-health-products--clinical-technology) | Equal label only; identity unresolved |
| 12 | `capability:clinical-quality-safety--improvement` | Clinical Quality, Safety & Improvement | CT1 | [business:526](../../markdown/02-strategy/capabilities/business-capabilities.md#06-clinical-quality-safety--improvement) | Equal label only; identity unresolved |
| 13 | `capability:community-health--wellbeing` | Community Health & Wellbeing | CT1 | [business:567](../../markdown/02-strategy/capabilities/business-capabilities.md#07-community-health--wellbeing) | Equal label only; identity unresolved |
| 14 | `capability:population-health--health-system-planning` | Population Health & Health-System Planning | CT1 | [business:594](../../markdown/02-strategy/capabilities/business-capabilities.md#08-population-health--health-system-planning) | Equal label only; identity unresolved |
| 15 | `capability:health-information--knowledge-management` | Health Information & Knowledge Management | CT1 | [business:640](../../markdown/02-strategy/capabilities/business-capabilities.md#09-health-information--knowledge-management) | Equal label only; identity unresolved |
| 16 | `capability:standards-semantics--reference-governance` | Standards, Semantics & Reference Governance | CT1 | [business:687](../../markdown/02-strategy/capabilities/business-capabilities.md#10-standards-semantics--reference-governance) | Equal label only; identity unresolved |
| 17 | `capability:connected-health-services` | Connected Health Services | CT1 | [business:734](../../markdown/02-strategy/capabilities/business-capabilities.md#11-connected-health-services) | Equal label only; identity unresolved |
| 18 | `capability:security-privacy--digital-trust` | Security, Privacy & Digital Trust | CT1 | [business:762](../../markdown/02-strategy/capabilities/business-capabilities.md#12-security-privacy--digital-trust) | Equal label only; identity unresolved |
| 19 | `capability:health-research--innovation` | Health Research & Innovation | CT1 | [business:828](../../markdown/02-strategy/capabilities/business-capabilities.md#13-health-research--innovation) | Equal label only; identity unresolved |
| 20 | `capability:health-service-direction--stewardship` | Health Service Direction & Stewardship | CT1 | [business:868](../../markdown/02-strategy/capabilities/business-capabilities.md#14-health-service-direction--stewardship) | Equal label only; identity unresolved |
| 21 | `capability:workforce--organisational-capability` | Workforce & Organisational Capability | CT1 | [business:910](../../markdown/02-strategy/capabilities/business-capabilities.md#15-workforce--organisational-capability) | Equal label only; identity unresolved |
| 22 | `capability:corporate-resources--enterprise-services` | Corporate Resources & Enterprise Services | CT1 | [business:952](../../markdown/02-strategy/capabilities/business-capabilities.md#16-corporate-resources--enterprise-services) | Equal label only; identity unresolved |
| 23 | `capability:health-service-governance` | Health Service Governance | CT1 | [business:997](../../markdown/02-strategy/capabilities/business-capabilities.md#17-health-service-governance) | Equal label only; identity unresolved |
| 24 | `capability:health-service-assurance` | Health Service Assurance | CT1 | [business:1037](../../markdown/02-strategy/capabilities/business-capabilities.md#18-health-service-assurance) | Equal label only; identity unresolved |
| 25 | `business-capability:health-history-assessment` | Health History Assessment | CT3 | [business:326](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) | Equal label only; identity unresolved |
| 26 | `business-capability:clinical-examination` | Clinical Examination | CT3 | [business:327](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) | Equal label only; identity unresolved |
| 27 | `business-capability:health-status-measurement` | Health Status Measurement | CT3 | [business:328](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-assessment) | Equal label only; identity unresolved |
| 28 | `business-capability:health-state-monitoring` | Health State Monitoring | CT3 | [business:342](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) | Equal label only; identity unresolved |
| 29 | `business-capability:care-response-monitoring` | Care Response Monitoring | CT3 | [business:343](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-health-monitoring) | Equal label only; identity unresolved |
| 30 | `business-capability:care-appropriateness-review` | Care Appropriateness Review | CT3 | [business:357](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-care-review) | No equal label; representation unverified |
| 31 | `business-capability:care-effectiveness-review` | Care Effectiveness Review | CT3 | [business:358](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--individual-care-review) | Equal label only; identity unresolved |
| 32 | `business-capability:care-access-management` | Care Access Management | CT2 | [business:374](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-access-management) | No equal label; representation unverified |
| 33 | `business-capability:care-intake-management` | Care Intake Management | CT3 | [business:380](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-access-management) | No equal label; representation unverified |
| 34 | `business-capability:care-referral-management` | Care Referral Management | CT3 | [business:381](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-access-management) | No equal label; representation unverified |
| 35 | `business-capability:care-eligibility-determination` | Care Eligibility Determination | CT3 | [business:382](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-access-management) | No equal label; representation unverified |
| 36 | `business-capability:care-scheduling--capacity-management` | Care Scheduling & Capacity Management | CT2 | [business:390](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 37 | `business-capability:care-appointment-management` | Care Appointment Management | CT3 | [business:396](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 38 | `business-capability:care-place-allocation` | Care-Place Allocation | CT3 | [business:397](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 39 | `business-capability:care-place-readiness-management` | Care-Place Readiness Management | CT3 | [business:398](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 40 | `business-capability:care-resource-deployment` | Care Resource Deployment | CT3 | [business:399](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 41 | `business-capability:operational-care-flow-coordination` | Operational Care Flow Coordination | CT3 | [business:400](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 42 | `business-capability:operational-care-capacity-management` | Operational Care Capacity Management | CT3 | [business:401](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-scheduling--capacity-management) | No equal label; representation unverified |
| 43 | `business-capability:care-coordination--transition` | Care Coordination & Transition | CT2 | [business:409](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-coordination--transition) | No equal label; representation unverified |
| 44 | `business-capability:care-team-coordination` | Care-Team Coordination | CT3 | [business:415](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-coordination--transition) | No equal label; representation unverified |
| 45 | `business-capability:care-responsibility-transition` | Care Responsibility Transition | CT3 | [business:416](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-coordination--transition) | No equal label; representation unverified |
| 46 | `business-capability:care-discharge-coordination` | Care Discharge Coordination | CT3 | [business:417](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-coordination--transition) | No equal label; representation unverified |
| 47 | `business-capability:care-support-logistics-management` | Care Support Logistics Management | CT2 | [business:425](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-support-logistics-management) | No equal label; representation unverified |
| 48 | `business-capability:patient-movement-coordination` | Patient Movement Coordination | CT3 | [business:431](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-support-logistics-management) | No equal label; representation unverified |
| 49 | `business-capability:clinical-material-logistics-coordination` | Clinical Material Logistics Coordination | CT3 | [business:432](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-support-logistics-management) | No equal label; representation unverified |
| 50 | `business-capability:care-support-work-coordination` | Care Support Work Coordination | CT3 | [business:433](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--care-support-logistics-management) | No equal label; representation unverified |
| 51 | `business-capability:diagnostic-support-provision` | Diagnostic Support Provision | CT2 | [business:461](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 52 | `business-capability:diagnostic-service-appointment-management` | Diagnostic Service Appointment Management | CT3 | [business:467](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 53 | `business-capability:diagnostic-service-delivery` | Diagnostic Service Delivery | CT3 | [business:468](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 54 | `business-capability:diagnostic-specimen-management` | Diagnostic Specimen Management | CT3 | [business:469](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 55 | `business-capability:diagnostic-data-management` | Diagnostic Data Management | CT3 | [business:470](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 56 | `business-capability:diagnostic-interpretation` | Diagnostic Interpretation | CT3 | [business:471](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 57 | `business-capability:diagnostic-report-management` | Diagnostic Report Management | CT3 | [business:472](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 58 | `business-capability:diagnostic-report-distribution` | Diagnostic Report Distribution | CT3 | [business:473](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--diagnostic-support-provision) | No equal label; representation unverified |
| 59 | `business-capability:medication--pharmacy-services` | Medication & Pharmacy Services | CT2 | [business:481](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--medication--pharmacy-services) | No equal label; representation unverified |
| 60 | `business-capability:prescription-medication-dispensing` | Prescription Medication Dispensing | CT3 | [business:487](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--medication--pharmacy-services) | No equal label; representation unverified |
| 61 | `business-capability:non-prescription-medication-provision` | Non-Prescription Medication Provision | CT3 | [business:488](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--medication--pharmacy-services) | No equal label; representation unverified |
| 62 | `business-capability:medication-advice` | Medication Advice | CT3 | [business:489](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--medication--pharmacy-services) | No equal label; representation unverified |
| 63 | `business-capability:medication-dose-administration-aid-provision` | Medication Dose Administration Aid Provision | CT3 | [business:490](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--medication--pharmacy-services) | No equal label; representation unverified |
| 64 | `business-capability:clinical-technology-management` | Clinical Technology Management | CT2 | [business:506](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 65 | `business-capability:clinical-device-inventory-management` | Clinical Device Inventory Management | CT3 | [business:512](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 66 | `business-capability:clinical-device-feature--release-management` | Clinical Device Feature & Release Management | CT3 | [business:513](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 67 | `business-capability:clinical-device-loan--deployment-management` | Clinical Device Loan & Deployment Management | CT3 | [business:514](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 68 | `business-capability:clinical-device-utilisation-support` | Clinical Device Utilisation Support | CT3 | [business:515](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 69 | `business-capability:clinical-device-integration-provision` | Clinical Device Integration Provision | CT3 | [business:516](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 70 | `business-capability:clinical-device-data-integration` | Clinical Device Data Integration | CT3 | [business:517](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 71 | `business-capability:clinical-device-lifecycle-management` | Clinical Device Lifecycle Management | CT3 | [business:518](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-technology-management) | No equal label; representation unverified |
| 72 | `business-capability:clinical-safety-management` | Clinical Safety Management | CT2 | [business:534](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) | Equal label only; identity unresolved |
| 73 | `business-capability:clinical-risk-identification` | Clinical Risk Identification | CT3 | [business:540](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) | Equal label only; identity unresolved |
| 74 | `business-capability:clinical-risk-mitigation-management` | Clinical Risk Mitigation Management | CT3 | [business:541](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) | Equal label only; identity unresolved |
| 75 | `business-capability:clinical-risk-response-management` | Clinical Risk Response Management | CT3 | [business:542](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-safety-management) | Equal label only; identity unresolved |
| 76 | `business-capability:clinical-quality-management` | Clinical Quality Management | CT2 | [business:550](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) | Equal label only; identity unresolved |
| 77 | `business-capability:clinical-quality-criteria-management` | Clinical Quality Criteria Management | CT3 | [business:556](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) | Equal label only; identity unresolved |
| 78 | `business-capability:clinical-quality-evidence-management` | Clinical Quality Evidence Management | CT3 | [business:557](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) | Equal label only; identity unresolved |
| 79 | `business-capability:clinical-quality-review--assessment-management` | Clinical Quality Review & Assessment Management | CT3 | [business:558](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) | Equal label only; identity unresolved |
| 80 | `business-capability:clinical-quality-reporting` | Clinical Quality Reporting | CT3 | [business:559](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-quality-management) | Equal label only; identity unresolved |
| 81 | `business-capability:preventative-health` | Preventative Health | CT2 | [business:575](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 82 | `business-capability:preventative-health-problem-identification` | Preventative Health Problem Identification | CT3 | [business:581](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 83 | `business-capability:preventative-health-programme-management` | Preventative Health Programme Management | CT3 | [business:582](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 84 | `business-capability:preventative-health-education-services` | Preventative Health Education Services | CT3 | [business:583](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 85 | `business-capability:preventative-health-programme-monitoring` | Preventative Health Programme Monitoring | CT3 | [business:584](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 86 | `business-capability:preventative-health-reporting` | Preventative Health Reporting | CT3 | [business:585](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 87 | `business-capability:preventative-health-cohort-management` | Preventative Health Cohort Management | CT3 | [business:586](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--preventative-health) | Equal label only; identity unresolved |
| 88 | `business-capability:population-health-surveillance` | Population Health Surveillance | CT2 | [business:602](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 89 | `business-capability:population-health-longitudinal-assessment` | Population Health Longitudinal Assessment | CT3 | [business:608](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 90 | `business-capability:population-health-surveillance-management` | Population Health Surveillance Management | CT3 | [business:609](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 91 | `business-capability:population-health-event-identification` | Population Health Event Identification | CT3 | [business:610](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 92 | `business-capability:notifiable-health-event-management` | Notifiable Health Event Management | CT3 | [business:611](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 93 | `business-capability:population-health-reporting` | Population Health Reporting | CT3 | [business:612](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--population-health-surveillance) | No equal label; representation unverified |
| 94 | `business-capability:health-system-planning` | Health-System Planning | CT2 | [business:620](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-system-planning) | No equal label; representation unverified |
| 95 | `business-capability:health-service-demand-assessment` | Health Service Demand Assessment | CT3 | [business:626](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-system-planning) | No equal label; representation unverified |
| 96 | `business-capability:health-service-capacity-planning` | Health Service Capacity Planning | CT3 | [business:627](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-system-planning) | No equal label; representation unverified |
| 97 | `business-capability:health-service-distribution-planning` | Health Service Distribution Planning | CT3 | [business:628](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-system-planning) | No equal label; representation unverified |
| 98 | `business-capability:health-information-management` | Health Information Management | CT2 | [business:648](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 99 | `business-capability:health-information-data-management` | Health Information Data Management | CT3 | [business:654](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 100 | `business-capability:health-information-structure-management` | Health Information Structure Management | CT3 | [business:655](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 101 | `business-capability:health-information-lifecycle-management` | Health Information Lifecycle Management | CT3 | [business:656](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 102 | `business-capability:health-information-catalogue-and-index-management` | Health Information Catalogue and Index Management | CT3 | [business:657](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 103 | `business-capability:health-information-compartmentalisation` | Health Information Compartmentalisation | CT3 | [business:658](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 104 | `business-capability:health-information-data-correlation` | Health Information Data Correlation | CT3 | [business:659](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 105 | `business-capability:health-information-data-retrieval` | Health Information Data Retrieval | CT3 | [business:660](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-management) | No equal label; representation unverified |
| 106 | `business-capability:clinical-knowledge-management` | Clinical Knowledge Management | CT2 | [business:668](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 107 | `business-capability:clinical-knowledge-context-identification` | Clinical Knowledge Context Identification | CT3 | [business:674](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 108 | `business-capability:clinical-knowledge-context-extraction` | Clinical Knowledge Context Extraction | CT3 | [business:675](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 109 | `business-capability:clinical-knowledge-information-formulation` | Clinical Knowledge Information Formulation | CT3 | [business:676](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 110 | `business-capability:clinical-knowledge-concept-association` | Clinical Knowledge Concept Association | CT3 | [business:677](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 111 | `business-capability:clinical-knowledge-review--approval-management` | Clinical Knowledge Review & Approval Management | CT3 | [business:678](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 112 | `business-capability:clinical-knowledge-publication--sharing` | Clinical Knowledge Publication & Sharing | CT3 | [business:679](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-knowledge-management) | No equal label; representation unverified |
| 113 | `business-capability:health-information-standards-governance` | Health Information Standards Governance | CT2 | [business:695](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 114 | `business-capability:information-model-discovery` | Information Model Discovery | CT3 | [business:701](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 115 | `business-capability:information-model-adoption` | Information Model Adoption | CT3 | [business:702](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 116 | `business-capability:information-model-education` | Information Model Education | CT3 | [business:703](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 117 | `business-capability:information-model-compliance` | Information Model Compliance | CT3 | [business:704](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 118 | `business-capability:information-model-governance-oversight` | Information Model Governance Oversight | CT3 | [business:705](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 119 | `business-capability:standards-adoption` | Standards Adoption | CT3 | [business:706](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 120 | `business-capability:standards-impact-identification` | Standards Impact Identification | CT3 | [business:707](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-standards-governance) | No equal label; representation unverified |
| 121 | `business-capability:clinical-semantics-governance` | Clinical Semantics Governance | CT2 | [business:715](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 122 | `business-capability:terminology-governance` | Terminology Governance | CT3 | [business:721](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 123 | `business-capability:terminology-utilisation-oversight` | Terminology Utilisation Oversight | CT3 | [business:722](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 124 | `business-capability:terminology-localisation` | Terminology Localisation | CT3 | [business:723](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 125 | `business-capability:classification-governance` | Classification Governance | CT3 | [business:724](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 126 | `business-capability:classification-utilisation-oversight` | Classification Utilisation Oversight | CT3 | [business:725](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 127 | `business-capability:semantic-mapping-governance` | Semantic Mapping Governance | CT3 | [business:726](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--clinical-semantics-governance) | No equal label; representation unverified |
| 128 | `business-capability:health-information-exchange` | Health Information Exchange | CT2 | [business:742](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-exchange) | No equal label; representation unverified |
| 129 | `business-capability:digital-communication-channel` | Digital Communication Channel | CT2 | [business:752](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-communication-channel) | No equal label; representation unverified |
| 130 | `business-capability:digital-identity--access-governance` | Digital Identity & Access Governance | CT2 | [business:770](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 131 | `business-capability:identity-assurance` | Identity Assurance | CT3 | [business:776](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 132 | `business-capability:identity-modelling--interoperability` | Identity Modelling & Interoperability | CT3 | [business:777](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 133 | `business-capability:identity-qualification-control` | Identity Qualification Control | CT3 | [business:778](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 134 | `business-capability:identity-identification--authentication` | Identity Identification & Authentication | CT3 | [business:779](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 135 | `business-capability:access-control-context-management` | Access Control Context Management | CT3 | [business:780](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 136 | `business-capability:access-control-assurance--reporting` | Access Control Assurance & Reporting | CT3 | [business:781](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 137 | `business-capability:access-control-collaboration` | Access Control Collaboration | CT3 | [business:782](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 138 | `business-capability:access-control-enforcement` | Access Control Enforcement | CT3 | [business:783](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-identity--access-governance) | No equal label; representation unverified |
| 139 | `business-capability:health-information-protection` | Health Information Protection | CT2 | [business:791](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 140 | `business-capability:health-privacy-safeguard-management` | Health Privacy Safeguard Management | CT3 | [business:797](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 141 | `business-capability:health-information-safeguarding` | Health Information Safeguarding | CT3 | [business:798](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 142 | `business-capability:health-information-access-monitoring` | Health Information Access Monitoring | CT3 | [business:799](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 143 | `business-capability:health-information-compartment-assurance` | Health Information Compartment Assurance | CT3 | [business:800](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 144 | `business-capability:health-information-classification-assurance` | Health Information Classification Assurance | CT3 | [business:801](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-information-protection) | No equal label; representation unverified |
| 145 | `business-capability:digital-accountability--trust` | Digital Accountability & Trust | CT2 | [business:809](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-accountability--trust) | No equal label; representation unverified |
| 146 | `business-capability:governed-activity-attribution` | Governed Activity Attribution | CT3 | [business:815](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-accountability--trust) | No equal label; representation unverified |
| 147 | `business-capability:security--information-use-accountability` | Security & Information-Use Accountability | CT3 | [business:816](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--digital-accountability--trust) | No equal label; representation unverified |
| 148 | `business-capability:research-information-support` | Research Information Support | CT2 | [business:836](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--research-information-support) | No equal label; representation unverified |
| 149 | `business-capability:governed-research-information-provision` | Governed Research Information Provision | CT3 | [business:842](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--research-information-support) | No equal label; representation unverified |
| 150 | `business-capability:health-innovation` | Health Innovation | CT2 | [business:850](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-innovation) | No equal label; representation unverified |
| 151 | `business-capability:health-innovation-evaluation` | Health Innovation Evaluation | CT3 | [business:856](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-innovation) | No equal label; representation unverified |
| 152 | `business-capability:health-service-modelling` | Health Service Modelling | CT2 | [business:876](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-modelling) | No equal label; representation unverified |
| 153 | `business-capability:health-service-need-identification` | Health Service Need Identification | CT3 | [business:882](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-modelling) | No equal label; representation unverified |
| 154 | `business-capability:health-service-capability-identification` | Health Service Capability Identification | CT3 | [business:883](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-modelling) | No equal label; representation unverified |
| 155 | `business-capability:health-service-implementation-support` | Health Service Implementation Support | CT3 | [business:884](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-modelling) | No equal label; representation unverified |
| 156 | `business-capability:health-service-continuous-improvement` | Health Service Continuous Improvement | CT3 | [business:885](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-modelling) | No equal label; representation unverified |
| 157 | `business-capability:health-service-alignment` | Health Service Alignment | CT2 | [business:893](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-alignment) | No equal label; representation unverified |
| 158 | `business-capability:health-service-co-dependency-management` | Health Service Co-Dependency Management | CT3 | [business:899](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-alignment) | No equal label; representation unverified |
| 159 | `business-capability:health-service-resource-identification` | Health Service Resource Identification | CT3 | [business:900](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-alignment) | No equal label; representation unverified |
| 160 | `business-capability:health-service-financial-management` | Health Service Financial Management | CT3 | [business:901](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-alignment) | No equal label; representation unverified |
| 161 | `business-capability:health-service-implementation-planning` | Health Service Implementation Planning | CT3 | [business:902](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-alignment) | No equal label; representation unverified |
| 162 | `business-capability:health-workforce-management` | Health Workforce Management | CT2 | [business:920](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-workforce-management) | No equal label; representation unverified |
| 163 | `business-capability:practitioner-qualification-management` | Practitioner Qualification Management | CT3 | [business:926](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-workforce-management) | No equal label; representation unverified |
| 164 | `business-capability:practitioner-engagement--privilege-management` | Practitioner Engagement & Privilege Management | CT3 | [business:927](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-workforce-management) | No equal label; representation unverified |
| 165 | `business-capability:workforce-service-coverage-management` | Workforce Service Coverage Management | CT3 | [business:928](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-workforce-management) | No equal label; representation unverified |
| 166 | `business-capability:healthcare-organisation--service-structure-management` | Healthcare Organisation & Service Structure Management | CT2 | [business:936](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--healthcare-organisation--service-structure-management) | No equal label; representation unverified |
| 167 | `business-capability:healthcare-organisation-identity--structure-management` | Healthcare Organisation Identity & Structure Management | CT3 | [business:942](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--healthcare-organisation--service-structure-management) | No equal label; representation unverified |
| 168 | `business-capability:healthcare-location-definition-management` | Healthcare Location Definition Management | CT3 | [business:943](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--healthcare-organisation--service-structure-management) | No equal label; representation unverified |
| 169 | `business-capability:healthcare-service-offering-management` | Healthcare Service Offering Management | CT3 | [business:944](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--healthcare-organisation--service-structure-management) | No equal label; representation unverified |
| 170 | `business-capability:financial--commercial-services` | Financial & Commercial Services | CT2 | [business:960](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--financial--commercial-services) | No equal label; representation unverified |
| 171 | `business-capability:clinical-activity-charging-information-provision` | Clinical Activity Charging Information Provision | CT3 | [business:966](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--financial--commercial-services) | No equal label; representation unverified |
| 172 | `business-capability:corporate-support-services` | Corporate Support Services | CT2 | [business:974](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 173 | `business-capability:back-office-services` | Back-Office Services | CT3 | [business:980](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 174 | `business-capability:facilities-management` | Facilities Management | CT3 | [business:981](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 175 | `business-capability:it-infrastructure-management` | IT Infrastructure Management | CT3 | [business:982](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 176 | `business-capability:communications--publicity` | Communications & Publicity | CT3 | [business:983](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 177 | `business-capability:people-management` | People Management | CT3 | [business:984](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 178 | `business-capability:operational-oversight` | Operational Oversight | CT3 | [business:985](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--corporate-support-services) | No equal label; representation unverified |
| 179 | `business-capability:governance-framework-management` | Governance Framework Management | CT2 | [business:1007](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--governance-framework-management) | No equal label; representation unverified |
| 180 | `business-capability:policy--obligation-governance` | Policy / Obligation Governance | CT3 | [business:1013](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--governance-framework-management) | No equal label; representation unverified |
| 181 | `business-capability:authority--constraint-governance` | Authority & Constraint Governance | CT3 | [business:1014](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--governance-framework-management) | No equal label; representation unverified |
| 182 | `business-capability:compliance-governance` | Compliance Governance | CT2 | [business:1022](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--compliance-governance) | No equal label; representation unverified |
| 183 | `business-capability:compliance-assessment-definition` | Compliance Assessment Definition | CT3 | [business:1028](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--compliance-governance) | No equal label; representation unverified |
| 184 | `business-capability:governance-reporting--checkpoint-definition` | Governance Reporting & Checkpoint Definition | CT3 | [business:1029](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--compliance-governance) | No equal label; representation unverified |
| 185 | `business-capability:health-service-assurance-specification` | Health Service Assurance Specification | CT2 | [business:1047](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-assurance-specification) | No equal label; representation unverified |
| 186 | `business-capability:assurance-approach-definition` | Assurance Approach Definition | CT3 | [business:1053](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-assurance-specification) | No equal label; representation unverified |
| 187 | `business-capability:assurance-evaluation-criteria-governance` | Assurance Evaluation Criteria Governance | CT3 | [business:1054](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--health-service-assurance-specification) | No equal label; representation unverified |
| 188 | `business-capability:assurance-evaluation` | Assurance Evaluation | CT2 | [business:1062](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--assurance-evaluation) | No equal label; representation unverified |
| 189 | `business-capability:assurance-evidence-assessment` | Assurance Evidence Assessment | CT3 | [business:1068](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--assurance-evaluation) | No equal label; representation unverified |
| 190 | `business-capability:assurance-conclusion-determination` | Assurance Conclusion Determination | CT3 | [business:1069](../../markdown/02-strategy/capabilities/business-capabilities.md#ct2--assurance-evaluation) | No equal label; representation unverified |

### 76 established Business Enabling Capability contexts

| No. | Navigation key | Governed name | Governed tier | Source assertion | Exchange representation |
| --- | --- | --- | --- | --- | --- |
| 1 | `capability:client-administration` | Client Administration | Unresolved | [enabling:112](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) | Equal label only; identity unresolved |
| 2 | `capability:provider-administration` | Provider Administration | Unresolved | [enabling:137](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-provider-administration) | Equal label only; identity unresolved |
| 3 | `capability:organisation-administration` | Organisation Administration | Unresolved | [enabling:158](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-organisation-administration) | Equal label only; identity unresolved |
| 4 | `capability:location-administration` | Location Administration | Unresolved | [enabling:174](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-location-administration) | Equal label only; identity unresolved |
| 5 | `capability:health-service-administration` | Health Service Administration | Unresolved | [enabling:189](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-health-service-administration) | Equal label only; identity unresolved |
| 6 | `capability:health-product-administration` | Health Product Administration | Unresolved | [enabling:208](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-health-product-administration) | No equal label; representation unverified |
| 7 | `capability:clinical-device-administration` | Clinical Device Administration | Unresolved | [enabling:215](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-device-administration) | Equal label only; identity unresolved |
| 8 | `capability:person-identity` | Person Identity | Unresolved | [enabling:115](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) | No equal label; representation unverified |
| 9 | `capability:healthcare-subject` | Healthcare Subject | Unresolved | [enabling:122](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) | No equal label; representation unverified |
| 10 | `capability:client-relationship` | Client Relationship | Unresolved | [enabling:127](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) | No equal label; representation unverified |
| 11 | `capability:client-privacy` | Client Privacy | Unresolved | [enabling:132](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-administration) | No equal label; representation unverified |
| 12 | `capability:client-centred-administration` | Client-Centred Administration | Unresolved | [enabling:279](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-client-centred-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 13 | `capability:referral-administration` | Referral Administration | Unresolved | [enabling:284](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-referral-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 14 | `capability:scheduling-administration` | Scheduling Administration | Unresolved | [enabling:290](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-scheduling-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 15 | `capability:episode--encounter-administration` | Episode & Encounter Administration | Unresolved | [enabling:295](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-episode--encounter-administration-harmonia-core) | Equal label only; identity unresolved |
| 16 | `capability:order-administration` | Order Administration | Unresolved | [enabling:301](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-order-administration-harmonia-core) | Equal label only; identity unresolved |
| 17 | `capability:diagnostic-administration` | Diagnostic Administration | Unresolved | [enabling:308](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-diagnostic-administration-harmonia-core) | Equal label only; identity unresolved |
| 18 | `capability:medication-administration` | Medication Administration | Unresolved | [enabling:316](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-medication-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 19 | `capability:procedure-administration` | Procedure Administration | Unresolved | [enabling:322](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-procedure-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 20 | `capability:clinical-record-administration` | Clinical Record Administration | Unresolved | [enabling:327](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#9-clinical-record-administration-harmonia-core) | Equal label only; identity unresolved |
| 21 | `capability:care-plan-administration` | Care Plan Administration | Unresolved | [enabling:333](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-care-plan-administration-harmonia-relevant) | Equal label only; identity unresolved |
| 22 | `capability:clinical-communication-administration` | Clinical Communication Administration | Unresolved | [enabling:337](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-clinical-communication-administration-harmonia-core) | Equal label only; identity unresolved |
| 23 | `capability:funding--coverage-administration` | Funding & Coverage Administration | Unresolved | [enabling:343](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-funding--coverage-administration-adjacent) | Equal label only; identity unresolved |
| 24 | `capability:clinical-charging--claims-administration` | Clinical Charging & Claims Administration | Unresolved | [enabling:346](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#13-clinical-charging--claims-administration-adjacent) | Equal label only; identity unresolved |
| 25 | `capability:clinical-assessment` | Clinical Assessment | Unresolved | [enabling:361](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 26 | `capability:primary-care` | Primary Care | Unresolved | [enabling:389](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-primary-care-harmonia-relevant) | Equal label only; identity unresolved |
| 27 | `capability:acute-care` | Acute Care | Unresolved | [enabling:394](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-acute-care-harmonia-relevant) | Equal label only; identity unresolved |
| 28 | `capability:emergency-care` | Emergency Care | Unresolved | [enabling:399](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-emergency-care-harmonia-relevant) | Equal label only; identity unresolved |
| 29 | `capability:inpatient-care` | Inpatient Care | Unresolved | [enabling:404](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-inpatient-care-harmonia-relevant) | Equal label only; identity unresolved |
| 30 | `capability:ambulatory-care` | Ambulatory Care | Unresolved | [enabling:366](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 31 | `capability:surgical--procedural-care` | Surgical & Procedural Care | Unresolved | [enabling:367](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 32 | `capability:diagnostic-services` | Diagnostic Services | Unresolved | [enabling:409](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-diagnostic-services-harmonia-relevant) | Equal label only; identity unresolved |
| 33 | `capability:therapeutic-services` | Therapeutic Services | Unresolved | [enabling:369](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 34 | `capability:medication-therapy` | Medication Therapy | Unresolved | [enabling:414](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-medication-therapy-harmonia-relevant) | Equal label only; identity unresolved |
| 35 | `capability:allied-health` | Allied Health | Unresolved | [enabling:371](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 36 | `capability:preventive-care` | Preventive Care | Unresolved | [enabling:419](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-preventive-care-harmonia-relevant) | Equal label only; identity unresolved |
| 37 | `capability:rehabilitation` | Rehabilitation | Unresolved | [enabling:373](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-3-service-delivery) | Equal label only; identity unresolved |
| 38 | `capability:community-care` | Community Care | Unresolved | [enabling:424](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#14-community-care-harmonia-relevant) | Equal label only; identity unresolved |
| 39 | `capability:outreach-care` | Outreach Care | Unresolved | [enabling:429](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#15-outreach-care-harmonia-relevant) | Equal label only; identity unresolved |
| 40 | `capability:remote--virtual-care` | Remote & Virtual Care | Unresolved | [enabling:434](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#16-remote--virtual-care-harmonia-relevant) | Equal label only; identity unresolved |
| 41 | `capability:clinic--practice-operations` | Clinic & Practice Operations | Unresolved | [enabling:481](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-clinic--practice-operations-harmonia-relevant) | Equal label only; identity unresolved |
| 42 | `capability:ward-operations` | Ward Operations | Unresolved | [enabling:486](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-ward-operations-harmonia-relevant) | Equal label only; identity unresolved |
| 43 | `capability:theatre-operations` | Theatre Operations | Unresolved | [enabling:491](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-theatre-operations-harmonia-relevant) | Equal label only; identity unresolved |
| 44 | `capability:emergency-department-operations` | Emergency Department Operations | Unresolved | [enabling:496](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-emergency-department-operations-harmonia-relevant) | Equal label only; identity unresolved |
| 45 | `capability:outpatient-operations` | Outpatient Operations | Unresolved | [enabling:501](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-outpatient-operations-harmonia-relevant) | Equal label only; identity unresolved |
| 46 | `capability:bed--care-place-management` | Bed & Care-Place Management | Unresolved | [enabling:506](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-bed--care-place-management-harmonia-core) | Equal label only; identity unresolved |
| 47 | `capability:clinical-resource-management` | Clinical Resource Management | Unresolved | [enabling:512](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-resource-management-harmonia-relevant) | No equal label; representation unverified |
| 48 | `capability:service-capacity-management` | Service Capacity Management | Unresolved | [enabling:516](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-service-capacity-management-harmonia-relevant) | Equal label only; identity unresolved |
| 49 | `capability:clinical-workforce-management` | Clinical Workforce Management | Unresolved | [enabling:456](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) | No equal label; representation unverified |
| 50 | `capability:clinical-rostering` | Clinical Rostering | Unresolved | [enabling:457](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) | Equal label only; identity unresolved |
| 51 | `capability:mobile-staff-management` | Mobile Staff Management | Unresolved | [enabling:520](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-mobile-staff-management-harmonia-relevant) | Equal label only; identity unresolved |
| 52 | `capability:on-call-management` | On-Call Management | Unresolved | [enabling:526](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-on-call-management-harmonia-relevant) | Equal label only; identity unresolved |
| 53 | `capability:work-allocation--dispatch` | Work Allocation & Dispatch | Unresolved | [enabling:530](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#13-work-allocation--dispatch-harmonia-core) | Equal label only; identity unresolved |
| 54 | `capability:clinical-training--education` | Clinical Training & Education | Unresolved | [enabling:461](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) | Equal label only; identity unresolved |
| 55 | `capability:clinical-competency-management` | Clinical Competency Management | Unresolved | [enabling:462](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) | No equal label; representation unverified |
| 56 | `capability:clinical-qualification-management` | Clinical Qualification Management | Unresolved | [enabling:539](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#16-clinical-qualification-management-harmonia-relevant) | No equal label; representation unverified |
| 57 | `capability:clinical-supervision` | Clinical Supervision | Unresolved | [enabling:464](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#view-4-health-service-operations) | Equal label only; identity unresolved |
| 58 | `capability:patient-transport` | Patient Transport | Unresolved | [enabling:543](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#18-patient-transport-harmonia-core) | Equal label only; identity unresolved |
| 59 | `capability:clinical-logistics-coordination` | Clinical Logistics Coordination | Unresolved | [enabling:548](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#19-clinical-logistics-coordination-harmonia-relevant) | Equal label only; identity unresolved |
| 60 | `capability:discharge-management` | Discharge Management | Unresolved | [enabling:552](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#20-discharge-management-harmonia-core) | Equal label only; identity unresolved |
| 61 | `capability:patient-clinical-record` | Patient Clinical Record | Unresolved | [enabling:608](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#1-patient-clinical-record-harmonia-core) | Equal label only; identity unresolved |
| 62 | `capability:health-information-exchange` | Health Information Exchange | Unresolved | [enabling:614](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#2-health-information-exchange-harmonia-core) | Equal label only; identity unresolved |
| 63 | `capability:health-information-access` | Health Information Access | Unresolved | [enabling:621](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#3-health-information-access-harmonia-core) | Equal label only; identity unresolved |
| 64 | `capability:health-information-communication` | Health Information Communication | Unresolved | [enabling:626](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#4-health-information-communication-harmonia-core) | Equal label only; identity unresolved |
| 65 | `capability:health-information-control` | Health Information Control | Unresolved | [enabling:630](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#5-health-information-control-harmonia-core) | Equal label only; identity unresolved |
| 66 | `capability:clinical-knowledge-services` | Clinical Knowledge Services | Unresolved | [enabling:636](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#6-clinical-knowledge-services-harmonia-relevant) | Equal label only; identity unresolved |
| 67 | `capability:clinical-collaboration` | Clinical Collaboration | Unresolved | [enabling:641](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#7-clinical-collaboration-harmonia-core) | Equal label only; identity unresolved |
| 68 | `capability:general-collaboration` | General Collaboration | Unresolved | [enabling:647](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#8-general-collaboration-harmonia-relevant) | Equal label only; identity unresolved |
| 69 | `capability:workflow--activity-coordination` | Workflow & Activity Coordination | Unresolved | [enabling:651](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#9-workflow--activity-coordination-harmonia-core) | Equal label only; identity unresolved |
| 70 | `capability:calendar-management` | Calendar Management | Unresolved | [enabling:658](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#10-calendar-management-harmonia-core) | Equal label only; identity unresolved |
| 71 | `capability:presentation-services` | Presentation Services | Unresolved | [enabling:663](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#11-presentation-services-harmonia-core) | Equal label only; identity unresolved |
| 72 | `capability:information-design-governance` | Information Design Governance | Unresolved | [enabling:667](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#12-information-design-governance-harmonia-core) | Equal label only; identity unresolved |
| 73 | `capability:assurance-design` | Assurance Design | Unresolved | [enabling:680](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#assurance-design) | Equal label only; identity unresolved |
| 74 | `capability:assurance-criteria-management` | Assurance Criteria Management | Unresolved | [enabling:686](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#assurance-criteria-management) | No equal label; representation unverified |
| 75 | `capability:governed-assurance` | Governed Assurance | Unresolved | [enabling:694](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md#governed-assurance) | No equal label; representation unverified |
| 76 | `capability:credential-management` | Credential Management | Unresolved | [hso:239](../../markdown/03-business-architecture/behaviours/04-health-service-operations.md#212-credential-management) | Equal label only; identity unresolved |

## Appendix C — exact governed descriptions eligible by existing Reference ID

The following Architectural Scope text is quoted directly from the current
canonical catalogue, without paraphrase. The source section also governs its
responsibilities, boundaries and qualifications. Selection is by the explicit
Reference ID, not by matching the quoted heading's name. These are report excerpts;
no `<documentation>` value has been written.

### EC-01 — Managed Entity & Relationship

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-01-managed-entity--relationship) at line 104; full governed section lines 103–111.

> Identify, classify, relate, and maintain the structural integrity of managed healthcare and administrative entities throughout the enterprise landscape.

### EC-02 — Context Management

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-02-context-management) at line 113; full governed section lines 112–120.

> Establish, validate, propagate, and preserve the execution, clinical, security, and administrative context within which information and activities have meaning.

### EC-03 — Managed State & Lifecycle

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-03-managed-state--lifecycle) at line 122; full governed section lines 121–129.

> Govern the current and historical state, effective temporal periods, state transition validations, and lifecycle progression of managed integration artifacts and entities.

### EC-04 — Information Management

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-04-information-management) at line 131; full governed section lines 130–138.

> Validate, represent, persist, retrieve, version, and manage governed clinical, administrative, and operational information assets.

### EC-05 — Search & Discovery

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-05-search--discovery) at line 140; full governed section lines 139–147.

> Index, search, resolve, filter, and discover information assets, entities, directory endpoints, and healthcare services across the enterprise.

### EC-06 — Policy & Control

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-06-policy--control) at line 149; full governed section lines 148–156.

> Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules.

### EC-07 — Provenance & Traceability

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-07-provenance--traceability) at line 158; full governed section lines 157–165.

> Preserve required origin attribution, meaningful transformation history, significant execution checkpoints, tamper-evident audit evidence and end-to-end traceability. Harmonia SHALL preserve provenance, audit and operational evidence where required by governing obligations, architectural significance, accountability, reconstruction, security or assurance needs. Routine technical activity SHALL NOT automatically require durable evidentiary recording.

### EC-08 — Interoperability & Exchange

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-08-interoperability--exchange) at line 167; full governed section lines 166–174.

> Resolve destinations, transform schemas, route, transport, deliver, acknowledge, and coordinate standards-based information exchange across enterprise boundaries.

### EC-09 — Event & Subscription

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-09-event--subscription) at line 176; full governed section lines 175–183.

> Publish, ingest, correlate, filter, and syndicate events and subscription-driven changes across the enterprise landscape.

### EC-10 — Activity & Execution

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-10-activity--execution) at line 185; full governed section lines 184–192.

> Define, instantiate, match, assign, dispatch, supervise, and progress coordinated operational and clinical activity units.

### EC-11 — Interaction & Experience

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-11-interaction--experience) at line 194; full governed section lines 193–201.

> Provide contextual interaction between human actors, Harmonia, and enterprise presentation services, including visualization, alerting, and collaboration integration.

### EC-12 — Operational Assurance

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-12-operational-assurance) at line 203; full governed section lines 202–214.

> Ensure concurrency integrity, system resilience, duplicate detection, exception handling, automated recovery, and operational telemetry across platform operations.

### EC-13 — Semantic Governance & Conformance

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-13-semantic-governance--conformance) at line 216; full governed section lines 215–223.

> Govern canonical models, schemas, terminologies, value sets, mapping tables, constraint rules, and semantic conformance validation across the enterprise.

### EC-14 — Service Guardian

**Eligible.** [Exact scope text](../../markdown/02-strategy/capabilities/enterprise-capabilities.md#ec-14-service-guardian) at line 226; full governed section lines 224–255.

> Service Guardian provides the reusable capability to independently establish whether governed service behaviour, state or outcomes remain within defined assurance expectations by associating applicable criteria and trustworthy evidence with a governed subject, assessing that evidence, adjudicating the assessment, and establishing reportable assurance findings and conclusions.

## Appendix D — candidate repository concerns, excluded from approved enrichment

| Navigation key | Candidate concern | Source | Limit |
| --- | --- | --- | --- |
| `business-capability:health-service-operational-readiness` | Health Service Operational Readiness | [business:1201](../../markdown/02-strategy/capabilities/business-capabilities.md#oi-bc-002--health-service-operational-readiness-placement) | Candidate/unresolved; no established Exchange identity mapping or approved description selection |
| `business-capability:clinical-device-maintenance-management` | Clinical Device Maintenance Management | [business:1216](../../markdown/02-strategy/capabilities/business-capabilities.md#other-preserved-uncertainties-and-canonical-conflicts) | Candidate/unresolved; no established Exchange identity mapping or approved description selection |
| `business-capability:health-information-sharing-programme-management` | Health Information Sharing Programme Management | [business:1222](../../markdown/02-strategy/capabilities/business-capabilities.md#other-preserved-uncertainties-and-canonical-conflicts) | Candidate/unresolved; no established Exchange identity mapping or approved description selection |

## Appendix E — source snapshot and reproducibility

These hashes fix the current source snapshot for the report; none was refreshed
or written into existing model files. The 15 source fingerprints already stored
in the reference capture match this snapshot. Capability-source locators were
checked against exact line text. The XML ID search used literal matching over
`docs/markdown`, `docs/latex-rad` and `scripts`; the new Exchange publication's
self-references were excluded so they cannot serve as their own identity authority.

| Source | SHA-256 |
| --- | --- |
| [AGENTS.md](../../../AGENTS.md) | `14850e24269cc44d96d91c30064fa0bf423031e78ec2fc7638b0e3d8a5ff0aca` |
| [docs/markdown/governance/architectural-axioms.md](../../markdown/governance/architectural-axioms.md) | `d0544fcb437b1ae43ed5fda6318ab4ab69a2e4ec614f73b34e8227b659b040c2` |
| [docs/markdown/04-information-architecture/traceability/domain03-traceability.md](../../markdown/04-information-architecture/traceability/domain03-traceability.md) | `221fa30288570fa58c554c40236a91be92d07aaf1348ecd123e3e0bbe649af08` |
| [docs/markdown/03-business-architecture/reference-model/business-architecture.json](../../markdown/03-business-architecture/reference-model/business-architecture.json) | `e7f3c8f52e58ad944434167dcb54d623d95d7f80c8c82813153b8566aa939dee` |
| [docs/markdown/02-strategy/capabilities/enterprise-capabilities.md](../../markdown/02-strategy/capabilities/enterprise-capabilities.md) | `4b887f8486827d26649b76d5d8ce3ef37ab9d11e861dbc6915ceb6202d92b2fb` |
| [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../markdown/02-strategy/capabilities/business-capabilities.md) | `27d86f8a5efe789083c464cf3f5026d6ddab86ad8d207d94dd57807fd9cdd2c0` |
| [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../markdown/02-strategy/capabilities/business-enabling-capabilities.md) | `8a7229be3343e027ab87c5421dec514d3803b80da254157be4e9d8c981010d49` |
| [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../markdown/03-business-architecture/metamodel/business-architecture-metamodel.md) | `3b0906eb6d51aea98e17b3f6b31250e68c2aa7320a1989f2386449909ce8f19a` |
| [docs/markdown/04-information-architecture/reviews/package2-g1-review.md](../../markdown/04-information-architecture/reviews/package2-g1-review.md) | `d5fde989273d26aa0823c66f50b7dd28e1eeaca180ed097b535c044c4a2d4b38` |
| [docs/markdown/02-strategy/capability-maps/capability-tier-model.md](../../markdown/02-strategy/capability-maps/capability-tier-model.md) | `186d9f1c6b10ce462db3214a423602f4ae9b69ce872d23b150dc68225c8b098a` |
| [docs/markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md](../../markdown/02-strategy/reviews/business-capability-five-region-reconciliation.md) | `a46cfdb769cd27dc55a38a7ebf165a267f322ad6590910862d11e68de2b00e5d` |
| [docs/markdown/02-strategy/capability-maps/health-service-assurance-derivation.md](../../markdown/02-strategy/capability-maps/health-service-assurance-derivation.md) | `76bfe725b77fdc65409acf073cc516c193b49c1371fe758f4dae02192deda6d6` |
| [docs/markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md](../../markdown/05-information-systems-architecture/traceability/b2-1-individual-care-delivery-health-information-access.md) | `1e3b59e0b5d1168e75244528540f1c64084391f72551ba9fd375a70a83016ab1` |
| [docs/markdown/03-business-architecture/processes/business-processes.md](../../markdown/03-business-architecture/processes/business-processes.md) | `d7fd20fa50c3f41eb8a1d23b83ee6057eeb2f3225157f8677bdfdb3b478e23c2` |
| [docs/markdown/03-business-architecture/behaviours/health-service-assurance.md](../../markdown/03-business-architecture/behaviours/health-service-assurance.md) | `fa976de7417f4979c95f3d44cd2cbed979407744053918cc903e0d1059e4c5bb` |
| [docs/markdown/03-business-architecture/behaviours/01-entity-management.md](../../markdown/03-business-architecture/behaviours/01-entity-management.md) | `15903e877addfbbc5fdc256377f48d52b7d7ac18c7b4f040baa8e8361666c69e` |
| [docs/markdown/03-business-architecture/behaviours/02-service-administration.md](../../markdown/03-business-architecture/behaviours/02-service-administration.md) | `7a26c3ad1d0581f6b5321b6089b99299e891256d5a739d5c074de306d9321878` |
| [docs/markdown/03-business-architecture/behaviours/03-service-delivery.md](../../markdown/03-business-architecture/behaviours/03-service-delivery.md) | `9da2e94a389c6d075f13ddf5aaa1d1e0c54477fc973fb9763c93a6e95e5a86c9` |
| [docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md](../../markdown/03-business-architecture/behaviours/04-health-service-operations.md) | `9de0e4725c772f96dc1a37ed9f668876cd7c3eca8aff56ae7f1865f39d54035d` |
| [docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md](../../markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md) | `80c699f9a5903c280c8a05fc87062b24a1c6ea2dea8843a92c5b53281d37ac8a` |

The work stops at this reconciliation. The 14 E cases are eligible for a later
explicit documentation-enrichment task; the 214 U/G cases remain stopped for
identity evidence. No semantic reconciliation, identifier allocation, renaming,
hierarchy change, new capability definition, XML enrichment or regeneration is
performed here.
