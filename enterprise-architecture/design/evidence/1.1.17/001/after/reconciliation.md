<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Iteration 001 complete source correspondence

Baseline RAD-1.1.16 → prepared RAD-1.1.17, HCRL 001. Source edition and checksum are pinned in [before/baseline.json](../before/baseline.json). The accepted descriptions remain in the canonical resources; this table avoids reproducing the complete specification.

150 active statements: four reused/extended and 146 created. All 13 groups and 69 subgroups are created as native Grouping. The [semantic reconciliation notes](reconciliation-notes.md) explain reuse and important non-identity distinctions.

| Source entry | Accepted name | Native type / specialisation | Action | Primary subject | Canonical resource |
| --- | --- | --- | --- | --- | --- |
| NFR-001 | Conformance to Governed Architecture | Principle «ArchitecturalAxiom» | created | G01.02 | [45eb197073df406ea4080f9665ca12ab](../../../../../continuum/motivation/principle/architectural-axiom/45eb197073df406ea4080f9665ca12ab.md) |
| NFR-002 | Architectural Decision Traceability | Constraint «Mandate» | created | G01.01 | [ae28fe841f6b476da4393c5e3c6a2eab](../../../../../continuum/motivation/constraint/mandate/ae28fe841f6b476da4393c5e3c6a2eab.md) |
| NFR-003 | Implementation Reconciliation | Constraint «Mandate» | created | G01.01 | [5acccfa8cf634669a26bb31010926720](../../../../../continuum/motivation/constraint/mandate/5acccfa8cf634669a26bb31010926720.md) |
| NFR-004 | Stable Architectural Identity | Principle «ArchitecturalAxiom» | created | G01.03 | [86ecebbf65fc46af9763c96840342823](../../../../../continuum/motivation/principle/architectural-axiom/86ecebbf65fc46af9763c96840342823.md) |
| NFR-005 | Authoritative Projection Semantics | Principle «ArchitecturalAxiom» | created | G01.03 | [d8ae43c850a646d485e6aba6c41a88e2](../../../../../continuum/motivation/principle/architectural-axiom/d8ae43c850a646d485e6aba6c41a88e2.md) |
| NFR-006 | Governed Architecture Review | Constraint «Mandate» | created | G01.04 | [d30a62f256ae44599841362118a84ec8](../../../../../continuum/motivation/constraint/mandate/d30a62f256ae44599841362118a84ec8.md) |
| NFR-007 | Independent Architecture Verification | Principle «ArchitecturalAxiom» | created | G01.02 | [82d29abf230d478aaf42ecb73ecbd01e](../../../../../continuum/motivation/principle/architectural-axiom/82d29abf230d478aaf42ecb73ecbd01e.md) |
| NFR-008 | Defined Service Contracts | Principle «ArchitecturalAxiom» | created | G01.03 | [b7d07bb4ec064272898d2f0189cffefd](../../../../../continuum/motivation/principle/architectural-axiom/b7d07bb4ec064272898d2f0189cffefd.md) |
| NFR-009 | Service Boundary Encapsulation | Principle «ArchitecturalAxiom» | created | G01.03 | [376363587388401194ff967b0fe80a5a](../../../../../continuum/motivation/principle/architectural-axiom/376363587388401194ff967b0fe80a5a.md) |
| NFR-010 | Component Substitutability | CourseOfAction «ImplementationGuideline» | created | G01.03 | [d4864b46c56e455189e418f3eaffec5c](../../../../../continuum/strategy/course-of-action/implementation-guideline/d4864b46c56e455189e418f3eaffec5c.md) |
| NFR-011 | Independent Component Evolution | CourseOfAction «ImplementationGuideline» | created | G01.03 | [c532bca066db4c419ccff5c52899af2a](../../../../../continuum/strategy/course-of-action/implementation-guideline/c532bca066db4c419ccff5c52899af2a.md) |
| NFR-012 | Explicit Component Dependencies | CourseOfAction «ImplementationGuideline» | created | G01.03 | [2c21d1a175fb4c1486f6a91830838cde](../../../../../continuum/strategy/course-of-action/implementation-guideline/2c21d1a175fb4c1486f6a91830838cde.md) |
| NFR-013 | Separation of Responsibilities | CourseOfAction «ImplementationGuideline» | created | G01.03 | [8983e2a7c2814c9c99aa1f81eaabe9bd](../../../../../continuum/strategy/course-of-action/implementation-guideline/8983e2a7c2814c9c99aa1f81eaabe9bd.md) |
| NFR-014 | Governed Information Identity | Principle «ArchitecturalAxiom» | created | G02.01 | [c2e7ee7597e64275b3523befd72c0987](../../../../../continuum/motivation/principle/architectural-axiom/c2e7ee7597e64275b3523befd72c0987.md) |
| NFR-015 | Governed Information Versioning | Principle «ArchitecturalAxiom» | created | G02.03 | [2bd0132f072b42e9bf71bf483eb2deec](../../../../../continuum/motivation/principle/architectural-axiom/2bd0132f072b42e9bf71bf483eb2deec.md) |
| NFR-016 | Designated Information Immutability | CourseOfAction «ImplementationGuideline» | created | G02.04 | [f9ac3424852f4c30b5b152c7a007c704](../../../../../continuum/strategy/course-of-action/implementation-guideline/f9ac3424852f4c30b5b152c7a007c704.md) |
| NFR-017 | Governed Information Modification | CourseOfAction «ImplementationGuideline» | created | G02.04 | [d08bf41d719c43e3bc1a54bd79d95741](../../../../../continuum/strategy/course-of-action/implementation-guideline/d08bf41d719c43e3bc1a54bd79d95741.md) |
| NFR-018 | Concurrent Information Consistency | CourseOfAction «ImplementationGuideline» | created | G02.04 | [0b0be077a4d449e29d72048634e276e4](../../../../../continuum/strategy/course-of-action/implementation-guideline/0b0be077a4d449e29d72048634e276e4.md) |
| NFR-019 | Information Provenance | Requirement «AssuranceRequirement» | reused and extended | G02.02 | [3e45e60affb44bf384d54de26d3add6a](../../../../../continuum/motivation/requirement/assurance-requirement/3e45e60affb44bf384d54de26d3add6a.md) |
| NFR-020 | Transformation Semantic Integrity | CourseOfAction «ImplementationGuideline» | created | G02.04 | [45c752faef034d3eb04ef6580f1dcc97](../../../../../continuum/strategy/course-of-action/implementation-guideline/45c752faef034d3eb04ef6580f1dcc97.md) |
| NFR-021 | Information Reference Resolution | CourseOfAction «ImplementationGuideline» | created | G02.04 | [b5c3dcfd6ece40fdb6c8694924db2ae6](../../../../../continuum/strategy/course-of-action/implementation-guideline/b5c3dcfd6ece40fdb6c8694924db2ae6.md) |
| NFR-022 | Distinct Execution Contexts | CourseOfAction «ImplementationGuideline» | created | G02.04 | [d0af3f7b7f9947008b3b066eefe73617](../../../../../continuum/strategy/course-of-action/implementation-guideline/d0af3f7b7f9947008b3b066eefe73617.md) |
| NFR-023 | Immutable Information Protection | Constraint «Mandate» | created | G02.02 | [4a25d9c8aaa9418fbddb5bff7302c46d](../../../../../continuum/motivation/constraint/mandate/4a25d9c8aaa9418fbddb5bff7302c46d.md) |
| NFR-024 | Ergon Execution Isolation | CourseOfAction «ImplementationGuideline» | created | G06.03 | [4332542fe3c54af9ba07220d255de9c2](../../../../../continuum/strategy/course-of-action/implementation-guideline/4332542fe3c54af9ba07220d255de9c2.md) |
| NFR-025 | Concurrency Policy Conformance | Principle «ArchitecturalAxiom» | created | G06.03 | [7e3b05a2e3e547cbbd26b1e181af0d10](../../../../../continuum/motivation/principle/architectural-axiom/7e3b05a2e3e547cbbd26b1e181af0d10.md) |
| NFR-026 | Execution Outcome Attribution | Requirement «AssuranceRequirement» | created | G06.06 | [79177908b67243a4a5f86fa180f5fd22](../../../../../continuum/motivation/requirement/assurance-requirement/79177908b67243a4a5f86fa180f5fd22.md) |
| NFR-027 | Task Independence | CourseOfAction «ImplementationGuideline» | created | G06.05 | [80fe052799944843917044749288f297](../../../../../continuum/strategy/course-of-action/implementation-guideline/80fe052799944843917044749288f297.md) |
| NFR-028 | Concurrent Task State Integrity | Principle «ArchitecturalAxiom» | created | G07.01 | [26f3759e1d014a7cae5f265ea6530000](../../../../../continuum/motivation/principle/architectural-axiom/26f3759e1d014a7cae5f265ea6530000.md) |
| NFR-029 | Accepted Work Durability | Constraint «Mandate» | created | G07.05 | [eabd38371b6940289d9a4bf8e9773942](../../../../../continuum/motivation/constraint/mandate/eabd38371b6940289d9a4bf8e9773942.md) |
| NFR-030 | Controlled Processing Recovery | Constraint «Mandate» | created | G07.03 | [014a4549398d49ee81eadb7108598f48](../../../../../continuum/motivation/constraint/mandate/014a4549398d49ee81eadb7108598f48.md) |
| NFR-031 | Repeated Execution Safety | CourseOfAction «ImplementationGuideline» | created | G07.03 | [be3bd95296c2427a870980a0e12adb41](../../../../../continuum/strategy/course-of-action/implementation-guideline/be3bd95296c2427a870980a0e12adb41.md) |
| NFR-032 | Failure Containment | CourseOfAction «ImplementationGuideline» | created | G07.02 | [93e40f54c22846538d91dc901ee34633](../../../../../continuum/strategy/course-of-action/implementation-guideline/93e40f54c22846538d91dc901ee34633.md) |
| NFR-033 | Interrupted Processing Recovery | CourseOfAction «ImplementationGuideline» | created | G07.02 | [6ca05ae8b3b34c1493ecdb48cfda1979](../../../../../continuum/strategy/course-of-action/implementation-guideline/6ca05ae8b3b34c1493ecdb48cfda1979.md) |
| NFR-034 | Failure Observability | Principle «ArchitecturalAxiom» | created | G09.02 | [2ed20179a98d44a68880958155e47923](../../../../../continuum/motivation/principle/architectural-axiom/2ed20179a98d44a68880958155e47923.md) |
| NFR-035 | Governed Information Authorisation | Constraint «Mandate» | created | G03.03 | [8812fcd52d3f4afcbeef41b60bda5874](../../../../../continuum/motivation/constraint/mandate/8812fcd52d3f4afcbeef41b60bda5874.md) |
| NFR-036 | Authorisation Compartment Assessment | CourseOfAction «ImplementationGuideline» | created | G03.01 | [a471a150c7d24210a991e96ed82e27a2](../../../../../continuum/strategy/course-of-action/implementation-guideline/a471a150c7d24210a991e96ed82e27a2.md) |
| NFR-037 | Authenticated Identity | Constraint «Mandate» | created | G03.03 | [72b6d13c85944e2d93efc6c5f5a53d82](../../../../../continuum/motivation/constraint/mandate/72b6d13c85944e2d93efc6c5f5a53d82.md) |
| NFR-038 | Authorisation Boundary Enforcement | Constraint «Mandate» | created | G03.04 | [e04672ef7f2341b68c09a0348742a861](../../../../../continuum/motivation/constraint/mandate/e04672ef7f2341b68c09a0348742a861.md) |
| NFR-039 | Auditable Security Decisions | Requirement «AssuranceRequirement» | created | G03.06 | [92ef574eb9b945dfaf6cf2b1b670595b](../../../../../continuum/motivation/requirement/assurance-requirement/92ef574eb9b945dfaf6cf2b1b670595b.md) |
| NFR-040 | Provider-Side Security Enforcement | Constraint «Mandate» | created | G03.04 | [d93b50d0abe04c47bf09759627785745](../../../../../continuum/motivation/constraint/mandate/d93b50d0abe04c47bf09759627785745.md) |
| NFR-041 | Execution Context Confidentiality | CourseOfAction «ImplementationGuideline» | created | G03.04 | [ddc28f789d69433686ef40561fec5750](../../../../../continuum/strategy/course-of-action/implementation-guideline/ddc28f789d69433686ef40561fec5750.md) |
| NFR-042 | Security Information Integrity | Principle «ArchitecturalAxiom» | created | G03.04 | [52083b57c01d4262bf965afa96489136](../../../../../continuum/motivation/principle/architectural-axiom/52083b57c01d4262bf965afa96489136.md) |
| NFR-043 | Governed Audit Generation | Constraint «Mandate» | created | G04.01 | [d388ee6fb1d24ca886b2802cfb42c30e](../../../../../continuum/motivation/constraint/mandate/d388ee6fb1d24ca886b2802cfb42c30e.md) |
| NFR-044 | Audit Context Identification | Requirement «AssuranceRequirement» | created | G04.02 | [de466c998be34fae87a2be41983cbcdd](../../../../../continuum/motivation/requirement/assurance-requirement/de466c998be34fae87a2be41983cbcdd.md) |
| NFR-045 | Audit Record Protection | Constraint «Mandate» | created | G04.03 | [de15359269cf45adb646a435c18306f8](../../../../../continuum/motivation/constraint/mandate/de15359269cf45adb646a435c18306f8.md) |
| NFR-046 | Audit Retention | Requirement «AssuranceRequirement» | created | G04.04 | [44b1a897fd1a41c097a1138bfee9cea6](../../../../../continuum/motivation/requirement/assurance-requirement/44b1a897fd1a41c097a1138bfee9cea6.md) |
| NFR-047 | Cross-Component Execution Traceability | Requirement «AssuranceRequirement» | created | G04.04 | [8014fbaa48b5435785fced72aa748da6](../../../../../continuum/motivation/requirement/assurance-requirement/8014fbaa48b5435785fced72aa748da6.md) |
| NFR-048 | Audit Noninterference | Requirement «AssuranceRequirement» | created | G04.01 | [ccea3a4dbd494cbe99c525ff2e87db26](../../../../../continuum/motivation/requirement/assurance-requirement/ccea3a4dbd494cbe99c525ff2e87db26.md) |
| NFR-049 | Published Interaction Contracts | CourseOfAction «ImplementationGuideline» | created | G05.04 | [b21633d8a1554688baf8704264c63608](../../../../../continuum/strategy/course-of-action/implementation-guideline/b21633d8a1554688baf8704264c63608.md) |
| NFR-050 | Exchange Semantic Preservation | Principle «ArchitecturalAxiom» | created | G05.02 | [602eb50793f448d8bdbc92d41f851877](../../../../../continuum/motivation/principle/architectural-axiom/602eb50793f448d8bdbc92d41f851877.md) |
| NFR-051 | Interoperability Encapsulation | Principle «ArchitecturalAxiom» | created | G05.03 | [e9e0b4f82b6544e7ba87651c578d82c2](../../../../../continuum/motivation/principle/architectural-axiom/e9e0b4f82b6544e7ba87651c578d82c2.md) |
| NFR-052 | Governed Exchange Representation | Constraint «Mandate» | created | G05.03 | [e9d06b3fe6b744cc86bd2a136e02165a](../../../../../continuum/motivation/constraint/mandate/e9d06b3fe6b744cc86bd2a136e02165a.md) |
| NFR-053 | Exchange Identity and Provenance | Requirement «AssuranceRequirement» | created | G05.04 | [5579b7ce9df540359b7ba36f807b52c5](../../../../../continuum/motivation/requirement/assurance-requirement/5579b7ce9df540359b7ba36f807b52c5.md) |
| NFR-054 | Contract Evolution Compatibility | Principle | created | G05.04 | [0da415a142c640d9ab1c73d770df8ea3](../../../../../continuum/motivation/principle/0da415a142c640d9ab1c73d770df8ea3.md) |
| NFR-055 | Native Operational Observability | Constraint «Mandate» | created | G09.01 | [151906f8acad4519b7f9204cce9fc758](../../../../../continuum/motivation/constraint/mandate/151906f8acad4519b7f9204cce9fc758.md) |
| NFR-056 | Dependency Health Monitoring | Constraint «Mandate» | created | G09.01 | [c5c8a76fbcbb466bb94f27b4863fb84c](../../../../../continuum/motivation/constraint/mandate/c5c8a76fbcbb466bb94f27b4863fb84c.md) |
| NFR-057 | Operational Failure Diagnostics | Requirement «AssuranceRequirement» | reused and extended | G09.02 | [b5d3447a05b249a8af154136fc36c4d9](../../../../../continuum/motivation/requirement/assurance-requirement/b5d3447a05b249a8af154136fc36c4d9.md) |
| NFR-058 | Cross-Service Activity Correlation | Requirement «AssuranceRequirement» | reused and extended | G09.04 | [f2e504e7d6374e41a81303265adda6de](../../../../../continuum/motivation/requirement/assurance-requirement/f2e504e7d6374e41a81303265adda6de.md) |
| NFR-059 | Component and Dependency Health Distinction | Requirement «AssuranceRequirement» | reused and extended | G09.06 | [4c02fec3af264b878ee3e0cfaecbf55f](../../../../../continuum/motivation/requirement/assurance-requirement/4c02fec3af264b878ee3e0cfaecbf55f.md) |
| NFR-060 | Concurrent Execution Units | Constraint «Mandate» | created | G06.03 | [12466ce2701146c1b52a9c68c2fb426c](../../../../../continuum/motivation/constraint/mandate/12466ce2701146c1b52a9c68c2fb426c.md) |
| NFR-061 | Elastic Processing Capacity | Constraint «Mandate» | created | G08.03 | [ea33d8db942f4b5b9e322d74cbfe975b](../../../../../continuum/motivation/constraint/mandate/ea33d8db942f4b5b9e322d74cbfe975b.md) |
| NFR-062 | Concurrent Service Processing | Constraint «Mandate» | created | G06.03 | [338a4cb1c2444ec2aa7703fcd9f28247](../../../../../continuum/motivation/constraint/mandate/338a4cb1c2444ec2aa7703fcd9f28247.md) |
| NFR-063 | Event Ingestion Capacity | Requirement «NonFunctionalRequirement» | created | G08.01 | [75420022c1a7463e91ca48f229a78194](../../../../../continuum/motivation/requirement/non-functional-requirement/75420022c1a7463e91ca48f229a78194.md) |
| NFR-064 | Resource Contention Integrity | Principle «ArchitecturalAxiom» | created | G08.04 | [db8d320dc4c0454080f8c0f3c4e52801](../../../../../continuum/motivation/principle/architectural-axiom/db8d320dc4c0454080f8c0f3c4e52801.md) |
| NFR-065 | Governed Search Performance | Requirement «NonFunctionalRequirement» | created | G08.02 | [b29075c844cf403fb1322b4d1aba8803](../../../../../continuum/motivation/requirement/non-functional-requirement/b29075c844cf403fb1322b4d1aba8803.md) |
| NFR-066 | Event Processing Completion | Requirement «NonFunctionalRequirement» | created | G08.01 | [d1380eec700a492b810354e0798c8146](../../../../../continuum/motivation/requirement/non-functional-requirement/d1380eec700a492b810354e0798c8146.md) |
| NFR-067 | Accepted Activity Durable Recording | Principle «ArchitecturalAxiom» | created | G07.05 | [6ee491b8c08d41868c3a7652fb794756](../../../../../continuum/motivation/principle/architectural-axiom/6ee491b8c08d41868c3a7652fb794756.md) |
| NFR-068 | Concurrent Practitioner Workload | Requirement «NonFunctionalRequirement» | created | G08.04 | [e77f2e780a974dde99815ff63158941c](../../../../../continuum/motivation/requirement/non-functional-requirement/e77f2e780a974dde99815ff63158941c.md) |
| NFR-069 | Concurrent OA&M Workload | Requirement «NonFunctionalRequirement» | created | G08.04 | [5220295ae5d144468066ae70683e0f19](../../../../../continuum/motivation/requirement/non-functional-requirement/5220295ae5d144468066ae70683e0f19.md) |
| NFR-070 | R1.x Accessibility Scope | Requirement «FunctionalRequirement» | created | G12.02 | [8369e22bc3414ae49a1987095549ef03](../../../../../continuum/motivation/requirement/functional-requirement/8369e22bc3414ae49a1987095549ef03.md) |
| NFR-071 | R1.x User Authentication | Requirement «FunctionalRequirement» | created | G03.01 | [9683182663cd40428d806ed2d8685511](../../../../../continuum/motivation/requirement/functional-requirement/9683182663cd40428d806ed2d8685511.md) |
| NFR-072 | Software-Defined Deployment | CourseOfAction «ImplementationGuideline» | created | G10.02 | [1ad8342e9ffd43138b27be348e861a77](../../../../../continuum/strategy/course-of-action/implementation-guideline/1ad8342e9ffd43138b27be348e861a77.md) |
| NFR-073 | R1.x Container Deployment | CourseOfAction «ImplementationGuideline» | created | G10.06 | [536bbcae0de5495a8f4d2e994cdc2232](../../../../../continuum/strategy/course-of-action/implementation-guideline/536bbcae0de5495a8f4d2e994cdc2232.md) |
| NFR-074 | R2.x Deployment Platforms | CourseOfAction «ImplementationGuideline» | created | G10.06 | [e1521c49b740487e935fa9ecd00675a0](../../../../../continuum/strategy/course-of-action/implementation-guideline/e1521c49b740487e935fa9ecd00675a0.md) |
| NFR-075 | Native Operational Logging | Principle «ArchitecturalAxiom» | created | G09.05 | [9d094b1b42644ab5aa2071a9460778e2](../../../../../continuum/motivation/principle/architectural-axiom/9d094b1b42644ab5aa2071a9460778e2.md) |
| NFR-076 | Operational Log PHI Protection | Constraint «Mandate» | created | G03.04 | [ab99a48c29c14a1883e820baf98b643a](../../../../../continuum/motivation/constraint/mandate/ab99a48c29c14a1883e820baf98b643a.md) |
| NFR-077 | Encryption at Rest | Constraint «Mandate» | created | G03.04 | [4522bf0326334607a4a6177c819e61b6](../../../../../continuum/motivation/constraint/mandate/4522bf0326334607a4a6177c819e61b6.md) |
| NFR-078 | Internal Encryption in Transit | Constraint «Mandate» | created | G03.04 | [4b540b55db1b4c1197482d8f29d70e45](../../../../../continuum/motivation/constraint/mandate/4b540b55db1b4c1197482d8f29d70e45.md) |
| NFR-079 | Standard Key Management | CourseOfAction «ImplementationGuideline» | created | G03.05 | [bbedb1ff13e94a88ac7ec60b8e40092e](../../../../../continuum/strategy/course-of-action/implementation-guideline/bbedb1ff13e94a88ac7ec60b8e40092e.md) |
| NFR-080 | Responsive Interface Layouts | Principle | created | G12.01 | [2e276eda09a94670bee0619468947895](../../../../../continuum/motivation/principle/2e276eda09a94670bee0619468947895.md) |
| NFR-081 | User Interaction Responsiveness | Requirement «NonFunctionalRequirement» | created | G12.03 | [0f07f89a3a854bfdac4d4fc423feefca](../../../../../continuum/motivation/requirement/non-functional-requirement/0f07f89a3a854bfdac4d4fc423feefca.md) |
| NFR-082 | Contextual Navigation | Principle | created | G12.04 | [39d698ff446d48ab9358844666fcafe5](../../../../../continuum/motivation/principle/39d698ff446d48ab9358844666fcafe5.md) |
| NFR-083 | Hard Shell Hard Body Security | Constraint «Mandate» | created | G03.04 | [fb7783088c894106a017912467a9e40d](../../../../../continuum/motivation/constraint/mandate/fb7783088c894106a017912467a9e40d.md) |
| NFR-084 | Source-Proximate Audit Generation | CourseOfAction «ImplementationGuideline» | created | G04.01 | [427d41b9416943d4a362470a965baea5](../../../../../continuum/strategy/course-of-action/implementation-guideline/427d41b9416943d4a362470a965baea5.md) |
| NFR-085 | Reference-Based Audit Records | Principle «ArchitecturalAxiom» | created | G04.03 | [9202f9b1adf84d4882a507a2d7280710](../../../../../continuum/motivation/principle/architectural-axiom/9202f9b1adf84d4882a507a2d7280710.md) |
| NFR-086 | Independent Audit Service | Principle «ArchitecturalAxiom» | created | G04.01 | [41fa01556ade4f22a35288b072113767](../../../../../continuum/motivation/principle/architectural-axiom/41fa01556ade4f22a35288b072113767.md) |
| NFR-087 | Large Information Object Storage | Principle «ArchitecturalAxiom» | created | G02.04 | [06ac0b10e0614c5cb33499c1e6c4edc7](../../../../../continuum/motivation/principle/architectural-axiom/06ac0b10e0614c5cb33499c1e6c4edc7.md) |
| NFR-088 | Configurable MLLP Timeouts | CourseOfAction «ImplementationGuideline» | created | G05.01 | [267142b7a01a49c686458b9fc1c8d7b2](../../../../../continuum/strategy/course-of-action/implementation-guideline/267142b7a01a49c686458b9fc1c8d7b2.md) |
| NFR-089 | Configurable MLLP Acknowledgements | CourseOfAction «ImplementationGuideline» | created | G05.02 | [ab4206c7312449569abaa818b3a8727a](../../../../../continuum/strategy/course-of-action/implementation-guideline/ab4206c7312449569abaa818b3a8727a.md) |
| NFR-090 | Robust MLLP Connection Management | Principle | created | G05.01 | [e7b7d94a94194ef28a12051a550d3f3f](../../../../../continuum/motivation/principle/e7b7d94a94194ef28a12051a550d3f3f.md) |
| NFR-091 | Governed HL7 v2 Tolerance | Principle | created | G05.03 | [0968fc163ae54514bc1f02471fe6d866](../../../../../continuum/motivation/principle/0968fc163ae54514bc1f02471fe6d866.md) |
| NFR-092 | External API Infrastructure Assumptions | CourseOfAction «ImplementationGuideline» | created | G05.01 | [a0c7aa62c0ff498c91dcd6af011170ce](../../../../../continuum/strategy/course-of-action/implementation-guideline/a0c7aa62c0ff498c91dcd6af011170ce.md) |
| NFR-093 | Operational State Reporting | Principle «ArchitecturalAxiom» | created | G09.01 | [d7e3f0388d0043259585d1e4c5c948d6](../../../../../continuum/motivation/principle/architectural-axiom/d7e3f0388d0043259585d1e4c5c948d6.md) |
| NFR-094 | Single-Machine Failure Recovery | Requirement «AssuranceRequirement» | created | G07.03 | [0d13da9390a3438ca887a4293bb034b9](../../../../../continuum/motivation/requirement/assurance-requirement/0d13da9390a3438ca887a4293bb034b9.md) |
| NFR-095 | Timezone Correctness | Principle | created | G12.05 | [3da1fba95a00438db09c342dfba8b693](../../../../../continuum/motivation/principle/3da1fba95a00438db09c342dfba8b693.md) |
| NFR-096 | Target User Expertise | Principle | created | G12.01 | [1c0b0e15434d47b1b08ad86e087a45d1](../../../../../continuum/motivation/principle/1c0b0e15434d47b1b08ad86e087a45d1.md) |
| NFR-097 | Coherent Workflow Scheduler | Principle «ArchitecturalAxiom» | created | G06.02 | [99adfc9b5af049748df91755d395c247](../../../../../continuum/motivation/principle/architectural-axiom/99adfc9b5af049748df91755d395c247.md) |
| NFR-098 | Coherent Calendar Service | Principle «ArchitecturalAxiom» | created | G06.02 | [a9b1d031fa844057aa030f09e4fa6b01](../../../../../continuum/motivation/principle/architectural-axiom/a9b1d031fa844057aa030f09e4fa6b01.md) |
| NFR-099 | Structured Information Encapsulation | Principle | created | G05.03 | [6973441f2d62448188af9bfce5ad9d9f](../../../../../continuum/motivation/principle/6973441f2d62448188af9bfce5ad9d9f.md) |
| NFR-100 | Harmonia FOSS Licensing | Constraint «Mandate» | created | G01.03 | [40880a1432ab4848ab5278839620a406](../../../../../continuum/motivation/constraint/mandate/40880a1432ab4848ab5278839620a406.md) |
| NFR-101 | FOSS Technology Dependencies | Constraint «Mandate» | created | G01.03 | [13c56fcda53c406494561c883f32fc1c](../../../../../continuum/motivation/constraint/mandate/13c56fcda53c406494561c883f32fc1c.md) |
| NFR-102 | Jakarta EE Preference | CourseOfAction «ImplementationGuideline» | created | G01.03 | [17ae70b671b5451a812df11d59ff6e1c](../../../../../continuum/strategy/course-of-action/implementation-guideline/17ae70b671b5451a812df11d59ff6e1c.md) |
| NFR-103 | Unbounded Clinical Information Retention | Principle «ArchitecturalAxiom» | created | G02.03 | [4c270732fecf4e79bb83c938861ce05b](../../../../../continuum/motivation/principle/architectural-axiom/4c270732fecf4e79bb83c938861ce05b.md) |
| NFR-104 | R1.x Internationalisation | Principle | created | G12.05 | [920606fdc7c14a27b7e39a08105c77b6](../../../../../continuum/motivation/principle/920606fdc7c14a27b7e39a08105c77b6.md) |
| NFR-105 | Operational Availability | Requirement «NonFunctionalRequirement» | created | G07.05 | [65ac0367b7f241149ad1839032004039](../../../../../continuum/motivation/requirement/non-functional-requirement/65ac0367b7f241149ad1839032004039.md) |
| NFR-106 | Disaster Recovery Consistency | Constraint «Mandate» | created | G07.04 | [81fdb5611a0f4338ae85478781032dd7](../../../../../continuum/motivation/constraint/mandate/81fdb5611a0f4338ae85478781032dd7.md) |
| NFR-107 | Component Recovery Consistency | Principle «ArchitecturalAxiom» | created | G07.03 | [545465b03f794a509c932bd3126fbe57](../../../../../continuum/motivation/principle/architectural-axiom/545465b03f794a509c932bd3126fbe57.md) |
| NFR-108 | Backup and Replication | Constraint «Mandate» | created | G07.04 | [8731f88e202a4f5dacb408e5a1d474a8](../../../../../continuum/motivation/constraint/mandate/8731f88e202a4f5dacb408e5a1d474a8.md) |
| NFR-109 | Planned Restart Preservation | Principle «ArchitecturalAxiom» | created | G07.03 | [0ce24ee595a445e5b1a95e54aa9f1eac](../../../../../continuum/motivation/principle/architectural-axiom/0ce24ee595a445e5b1a95e54aa9f1eac.md) |
| NFR-110 | Degraded Mode Continuity | Principle | created | G07.05 | [51191ca802f54efb8554d20423199cd0](../../../../../continuum/motivation/principle/51191ca802f54efb8554d20423199cd0.md) |
| NFR-111 | Controlled Capacity Admission | Constraint «Mandate» | created | G08.04 | [fb30eb9b8a3c41eda3a8b5edfee95ac1](../../../../../continuum/motivation/constraint/mandate/fb30eb9b8a3c41eda3a8b5edfee95ac1.md) |
| NFR-112 | Processing Resource Isolation | Principle | created | G08.04 | [41ce95e00e064d809ad4bda818705510](../../../../../continuum/motivation/principle/41ce95e00e064d809ad4bda818705510.md) |
| NFR-113 | Configurable Resource Limits | CourseOfAction «ImplementationGuideline» | created | G08.04 | [4621a5cb0dbe445488439ac03398a8b5](../../../../../continuum/strategy/course-of-action/implementation-guideline/4621a5cb0dbe445488439ac03398a8b5.md) |
| NFR-114 | Detailed Performance Metrics | CourseOfAction «ImplementationGuideline» | created | G09.01 | [247ae4b498944e5f80c638fc958a3f43](../../../../../continuum/strategy/course-of-action/implementation-guideline/247ae4b498944e5f80c638fc958a3f43.md) |
| NFR-115 | Cross-Component Resource Contention | Principle | created | G08.04 | [5bd9cc19d41e47d49978afaeea8343ed](../../../../../continuum/motivation/principle/5bd9cc19d41e47d49978afaeea8343ed.md) |
| NFR-117 | Information Archival and Retrieval | Principle «ArchitecturalAxiom» | created | G02.03 | [2429db4dbecc4a03a66f5189d76a6c38](../../../../../continuum/motivation/principle/architectural-axiom/2429db4dbecc4a03a66f5189d76a6c38.md) |
| NFR-118 | Configurable Retention and Disposal | Principle | created | G02.03 | [b1a46fce85b4448fba55987af146d61e](../../../../../continuum/motivation/principle/b1a46fce85b4448fba55987af146d61e.md) |
| NFR-119 | Information Integrity Assessment | Constraint «Mandate» | created | G02.02 | [6316761bfd414032a8d889c7ba34131d](../../../../../continuum/motivation/constraint/mandate/6316761bfd414032a8d889c7ba34131d.md) |
| NFR-121 | Migration Information Integrity | Constraint «Mandate» | created | G02.02 | [22583ccb89dc4cf683c710760bc4b1f2](../../../../../continuum/motivation/constraint/mandate/22583ccb89dc4cf683c710760bc4b1f2.md) |
| NFR-122 | Information Model Migration Planning | CourseOfAction «ImplementationGuideline» | created | G02.03 | [4a2cd91fab0c42e98b09a017aefa66de](../../../../../continuum/strategy/course-of-action/implementation-guideline/4a2cd91fab0c42e98b09a017aefa66de.md) |
| NFR-123 | Cryptographic Key Rotation | Principle «ArchitecturalAxiom» | created | G03.05 | [6df99d4979a942fe833e1c43317b385b](../../../../../continuum/motivation/principle/architectural-axiom/6df99d4979a942fe833e1c43317b385b.md) |
| NFR-124 | Credential and Grant Revocation | Principle «ArchitecturalAxiom» | created | G03.03 | [370d9a6e0e00402aba9720580a2e365e](../../../../../continuum/motivation/principle/architectural-axiom/370d9a6e0e00402aba9720580a2e365e.md) |
| NFR-125 | Secrets Protection | Constraint «Mandate» | created | G03.04 | [4547bd339ff143c988391159e3407f20](../../../../../continuum/motivation/constraint/mandate/4547bd339ff143c988391159e3407f20.md) |
| NFR-126 | Configurable Session Timeouts | CourseOfAction «ImplementationGuideline» | created | G03.01 | [886896239b2e40b0b3bda53e922a2ab6](../../../../../continuum/strategy/course-of-action/implementation-guideline/886896239b2e40b0b3bda53e922a2ab6.md) |
| NFR-127 | Security Anomaly Reporting | Principle «ArchitecturalAxiom» | created | G03.06 | [859f884874094c96a6ace15e17131a4c](../../../../../continuum/motivation/principle/architectural-axiom/859f884874094c96a6ace15e17131a4c.md) |
| NFR-129 | Governed DevSecOps Deployment | Constraint «Mandate» | created | G10.02 | [54eb7e671c094ebcbb5503af36c7892d](../../../../../continuum/motivation/constraint/mandate/54eb7e671c094ebcbb5503af36c7892d.md) |
| NFR-130 | Environment-Specific Configuration | Principle | created | G10.03 | [48e5415eff454e3e9e987506999e62f2](../../../../../continuum/motivation/principle/48e5415eff454e3e9e987506999e62f2.md) |
| NFR-131 | Adjacent Release Coexistence | Principle «ArchitecturalAxiom» | created | G10.05 | [3ce5568f2ded498386fd6fcfb142f89a](../../../../../continuum/motivation/principle/architectural-axiom/3ce5568f2ded498386fd6fcfb142f89a.md) |
| NFR-132 | Three-Generation Release Compatibility | Principle «ArchitecturalAxiom» | created | G10.05 | [33d348c2936b401aa567d8fa531147ea](../../../../../continuum/motivation/principle/architectural-axiom/33d348c2936b401aa567d8fa531147ea.md) |
| NFR-133 | Representative Deployment Test Harness | CourseOfAction «ImplementationGuideline» | created | G11.06 | [13ce74b78d624dd09c4401fafc911f67](../../../../../continuum/strategy/course-of-action/implementation-guideline/13ce74b78d624dd09c4401fafc911f67.md) |
| NFR-135 | Governed External Interaction Retry | Principle «ArchitecturalAxiom» | created | G05.05 | [49c13eb367b44565a011a77b90108cb8](../../../../../continuum/motivation/principle/architectural-axiom/49c13eb367b44565a011a77b90108cb8.md) |
| NFR-136 | Duplicate Message Handling | Requirement «FunctionalRequirement» | created | G06.04 | [76a67526fcdf40999f375a8cac54d5ef](../../../../../continuum/motivation/requirement/functional-requirement/76a67526fcdf40999f375a8cac54d5ef.md) |
| NFR-137 | Message Processing Ordering | Requirement «FunctionalRequirement» | created | G13.06 | [3c11adccd7194276b787416368c13858](../../../../../continuum/motivation/requirement/functional-requirement/3c11adccd7194276b787416368c13858.md) |
| NFR-138 | Interaction Contract Versioning | CourseOfAction «ImplementationGuideline» | created | G05.04 | [71d7a55f3d8c4b12836601da142cb304](../../../../../continuum/strategy/course-of-action/implementation-guideline/71d7a55f3d8c4b12836601da142cb304.md) |
| NFR-139 | External Connection Configuration | Principle «ArchitecturalAxiom» | created | G05.01 | [f24e63c49135411bbfbc06f9385aeea5](../../../../../continuum/motivation/principle/architectural-axiom/f24e63c49135411bbfbc06f9385aeea5.md) |
| NFR-141 | Configurable Operational Alert Thresholds | CourseOfAction «ImplementationGuideline» | created | G09.01 | [587db702f9134c5b8292f894dbcba53d](../../../../../continuum/strategy/course-of-action/implementation-guideline/587db702f9134c5b8292f894dbcba53d.md) |
| NFR-142 | Governed OA&M Information | Principle «ArchitecturalAxiom» | created | G09.05 | [43593f7a9566410ebc1aa30dbe935848](../../../../../continuum/motivation/principle/architectural-axiom/43593f7a9566410ebc1aa30dbe935848.md) |
| NFR-143 | Manual Processing Intervention | Requirement «FunctionalRequirement» | created | G06.02 | [911511665a8a4135b7f2a37f5f79ad36](../../../../../continuum/motivation/requirement/functional-requirement/911511665a8a4135b7f2a37f5f79ad36.md) |
| NFR-145 | Root Cause Investigation | Principle | created | G09.02 | [9cdbf642158c4c37a6f3f10a6dd9ebfe](../../../../../continuum/motivation/principle/9cdbf642158c4c37a6f3f10a6dd9ebfe.md) |
| NFR-146 | Controlled Component Shutdown and Restart | Constraint «Mandate» | created | G09.03 | [96f7bfa320b049d2b76fcc35a1926883](../../../../../continuum/motivation/constraint/mandate/96f7bfa320b049d2b76fcc35a1926883.md) |
| NFR-147 | Automated Interaction Contract Verification | CourseOfAction «ImplementationGuideline» | created | G11.02 | [a6c8cc8f5647461cb210f88ced90cc57](../../../../../continuum/strategy/course-of-action/implementation-guideline/a6c8cc8f5647461cb210f88ced90cc57.md) |
| NFR-148 | Governed Reference Test Harness | Constraint «Mandate» | created | G11.06 | [5ce1ef8baaf84848979ff6c9ccf6ca91](../../../../../continuum/motivation/constraint/mandate/5ce1ef8baaf84848979ff6c9ccf6ca91.md) |
| NFR-149 | Repeatable Performance Testing | Principle | created | G11.04 | [1c2eb4d9aef4425288528907fed7ebc2](../../../../../continuum/motivation/principle/1c2eb4d9aef4425288528907fed7ebc2.md) |
| NFR-150 | Controlled Resilience Testing | Constraint «Mandate» | created | G11.05 | [f2d38e7beef84b61a769de4ca7f48951](../../../../../continuum/motivation/constraint/mandate/f2d38e7beef84b61a769de4ca7f48951.md) |
| NFR-151 | Processing Outcome Evidence | Requirement «AssuranceRequirement» | created | G06.06 | [8ac5880576ad4f8fa9612f231178a06c](../../../../../continuum/motivation/requirement/assurance-requirement/8ac5880576ad4f8fa9612f231178a06c.md) |
| NFR-152 | Architecture-Implementation Alignment | Goal | created | G01.04 | [adf5bd2a409b44099253fbed486d736f](../../../../../continuum/motivation/goal/adf5bd2a409b44099253fbed486d736f.md) |
| NFR-153 | Distributed Temporal Consistency | Principle «ArchitecturalAxiom» | created | G13.02 | [ed3e8d7923d14f8cbc6f08d3673248f2](../../../../../continuum/motivation/principle/architectural-axiom/ed3e8d7923d14f8cbc6f08d3673248f2.md) |
| NFR-154 | External Information Sovereignty | Principle «ArchitecturalAxiom» | created | G02.05 | [aa179ef5bdf44b4eab3f2c855a8798d2](../../../../../continuum/motivation/principle/architectural-axiom/aa179ef5bdf44b4eab3f2c855a8798d2.md) |
| NFR-155 | Information Residency Governance | Principle «ArchitecturalAxiom» | created | G02.05 | [349f526a401e4e299b854cbfedad0fca](../../../../../continuum/motivation/principle/architectural-axiom/349f526a401e4e299b854cbfedad0fca.md) |
| NFR-156 | External Dependency Resilience | Goal | created | G07.05 | [150b3b1eea9b484b89ddc609bf550485](../../../../../continuum/motivation/goal/150b3b1eea9b484b89ddc609bf550485.md) |

## Subject group correspondence

Every subgroup retains its source concern, including explicitly unspecified concerns. Its canonical navigation cross-references account for all primary allocations; 219 source-supported Compositions independently preserve subject containment.

| Source group | Name | Kind | Canonical resource |
| --- | --- | --- | --- |
| G01 | Architectural Governance | group | [03cfbc2cac494fb9a15106c2c571d384](../../../../../continuum/grouping/03cfbc2cac494fb9a15106c2c571d384.md) |
| G01.01 | Architecture Process | subgroup | [2ee0a6e46b47413d91cfb71ee3100e4b](../../../../../continuum/grouping/2ee0a6e46b47413d91cfb71ee3100e4b.md) |
| G01.02 | Architecture Vision | subgroup | [6d6086134162468cac129040a139d8d9](../../../../../continuum/grouping/6d6086134162468cac129040a139d8d9.md) |
| G01.03 | Implementation Strategy | subgroup | [8bf2a60efafc4caf89ed9744adfb628d](../../../../../continuum/grouping/8bf2a60efafc4caf89ed9744adfb628d.md) |
| G01.04 | Architecture Conformance | subgroup | [5989495f1e224d5797054a8b6c0364a1](../../../../../continuum/grouping/5989495f1e224d5797054a8b6c0364a1.md) |
| G02 | Information Management | group | [ef296738e68e4206a507dc3c811b3fe1](../../../../../continuum/grouping/ef296738e68e4206a507dc3c811b3fe1.md) |
| G02.01 | Information Identity | subgroup | [819aeed91d0544c3b7cc52e69aa46d72](../../../../../continuum/grouping/819aeed91d0544c3b7cc52e69aa46d72.md) |
| G02.02 | Information Integrity | subgroup | [eb1b0526b898448688a322cce7ea6e74](../../../../../continuum/grouping/eb1b0526b898448688a322cce7ea6e74.md) |
| G02.03 | Information Lifecycle | subgroup | [b23f7fa8cb1b46569f63bde1aa5fbf1f](../../../../../continuum/grouping/b23f7fa8cb1b46569f63bde1aa5fbf1f.md) |
| G02.04 | Information Persistence | subgroup | [8b7fd22a55434e5ca3e7ba87afb020d6](../../../../../continuum/grouping/8b7fd22a55434e5ca3e7ba87afb020d6.md) |
| G02.05 | Information Sovereignty and Residency | subgroup | [f4af2b5206264a6fa4c5040324ad272c](../../../../../continuum/grouping/f4af2b5206264a6fa4c5040324ad272c.md) |
| G03 | Security and Authorisation | group | [a762424d13254eaa9467946d9d7355f8](../../../../../continuum/grouping/a762424d13254eaa9467946d9d7355f8.md) |
| G03.01 | Authentication | subgroup | [f29ffa95c89d470b8a32e1369696327f](../../../../../continuum/grouping/f29ffa95c89d470b8a32e1369696327f.md) |
| G03.02 | Authorisation | subgroup | [4648cf5a1f8e4333978c13efae10eaa5](../../../../../continuum/grouping/4648cf5a1f8e4333978c13efae10eaa5.md) |
| G03.03 | Authorisation Enforcement | subgroup | [fe8916b03b084e82af587ac5a900349b](../../../../../continuum/grouping/fe8916b03b084e82af587ac5a900349b.md) |
| G03.04 | Information Protection | subgroup | [d2f8fcaac9984a12beab502d1944ea25](../../../../../continuum/grouping/d2f8fcaac9984a12beab502d1944ea25.md) |
| G03.05 | Security Administration | subgroup | [84693ef7bb4a4ced9634737c6bd5a36d](../../../../../continuum/grouping/84693ef7bb4a4ced9634737c6bd5a36d.md) |
| G03.06 | Security Monitoring | subgroup | [bb3adbc3c2fb43e6a18a876c4e897c9c](../../../../../continuum/grouping/bb3adbc3c2fb43e6a18a876c4e897c9c.md) |
| G04 | Audit and Accountability | group | [abc27ddff6d644d9945db0cfd7488f07](../../../../../continuum/grouping/abc27ddff6d644d9945db0cfd7488f07.md) |
| G04.01 | Audit Generation | subgroup | [40416671099a46678ac5dfc3d0d615cf](../../../../../continuum/grouping/40416671099a46678ac5dfc3d0d615cf.md) |
| G04.02 | Audit Integrity | subgroup | [c102ede2b9e249b2a2ddc9f19e0cbd7f](../../../../../continuum/grouping/c102ede2b9e249b2a2ddc9f19e0cbd7f.md) |
| G04.03 | Audit Management | subgroup | [de791c4976f84676bf8545b1cfb8bf41](../../../../../continuum/grouping/de791c4976f84676bf8545b1cfb8bf41.md) |
| G04.04 | Accountability and Traceability | subgroup | [38d3dfcd23894f43b59d00c782170e63](../../../../../continuum/grouping/38d3dfcd23894f43b59d00c782170e63.md) |
| G05 | Interoperability and Integration | group | [098d2e468cd14029b43ec6dbd07374fd](../../../../../continuum/grouping/098d2e468cd14029b43ec6dbd07374fd.md) |
| G05.01 | External Connectivity | subgroup | [0b9e18356e9e4dcca51007f03f7b81b4](../../../../../continuum/grouping/0b9e18356e9e4dcca51007f03f7b81b4.md) |
| G05.02 | Interoperability Standards | subgroup | [a44091225174403ebd2c5b75ba690a09](../../../../../continuum/grouping/a44091225174403ebd2c5b75ba690a09.md) |
| G05.03 | Information Exchange | subgroup | [bda335658c29473d9ba20352242c83f8](../../../../../continuum/grouping/bda335658c29473d9ba20352242c83f8.md) |
| G05.04 | Interaction Contracts | subgroup | [b3c19413e4b447adb5f1d86912db4052](../../../../../continuum/grouping/b3c19413e4b447adb5f1d86912db4052.md) |
| G05.05 | Interaction Reliability | subgroup | [1d7c50343cca49eeaeccf7c49c3c008b](../../../../../continuum/grouping/1d7c50343cca49eeaeccf7c49c3c008b.md) |
| G06 | Processing and Workflow | group | [afb9686c9f1c48159d92a518a7d17452](../../../../../continuum/grouping/afb9686c9f1c48159d92a518a7d17452.md) |
| G06.01 | Task Management | subgroup | [0b6d5f58354040189b3a9a30a9b913d9](../../../../../continuum/grouping/0b6d5f58354040189b3a9a30a9b913d9.md) |
| G06.02 | Task Completion | subgroup | [2d89b3a1e74e42d28a9f0a3dfb2c4ecb](../../../../../continuum/grouping/2d89b3a1e74e42d28a9f0a3dfb2c4ecb.md) |
| G06.03 | Concurrency Management | subgroup | [9536aaf4114b4c35b8a795383d442f64](../../../../../continuum/grouping/9536aaf4114b4c35b8a795383d442f64.md) |
| G06.04 | Message Processing | subgroup | [5dde6ebecd094084b325ba59603bb919](../../../../../continuum/grouping/5dde6ebecd094084b325ba59603bb919.md) |
| G06.05 | Processing State Management | subgroup | [5e8ddc78f9554243aeb051f77595cc88](../../../../../continuum/grouping/5e8ddc78f9554243aeb051f77595cc88.md) |
| G06.06 | Processing Outcomes | subgroup | [f40da9359c7545d2ad0eef6c3a5bf645](../../../../../continuum/grouping/f40da9359c7545d2ad0eef6c3a5bf645.md) |
| G07 | Resilience and Recovery | group | [914b8be39ecc4fb599e6bc0004c355c0](../../../../../continuum/grouping/914b8be39ecc4fb599e6bc0004c355c0.md) |
| G07.01 | Fault Tolerance | subgroup | [0a59e0ebb33c494e939148d30e42f49c](../../../../../continuum/grouping/0a59e0ebb33c494e939148d30e42f49c.md) |
| G07.02 | Failure Detection and Handling | subgroup | [e9804b6dea5c4e1db80be1925463786f](../../../../../continuum/grouping/e9804b6dea5c4e1db80be1925463786f.md) |
| G07.03 | Operational Recovery | subgroup | [8609b046777c4a8ab4d5cafb30e34d04](../../../../../continuum/grouping/8609b046777c4a8ab4d5cafb30e34d04.md) |
| G07.04 | Disaster Recovery | subgroup | [49966f8f01e244b4926ce4dc4c41fb47](../../../../../continuum/grouping/49966f8f01e244b4926ce4dc4c41fb47.md) |
| G07.05 | Continuity of Operations | subgroup | [41900f15d1d2431295a4b9dc1aa752c0](../../../../../continuum/grouping/41900f15d1d2431295a4b9dc1aa752c0.md) |
| G08 | Performance and Capacity | group | [8034148e63e6457f90e8cae1d4eb4453](../../../../../continuum/grouping/8034148e63e6457f90e8cae1d4eb4453.md) |
| G08.01 | Throughput | subgroup | [cdcfbe8d106844109bb8ded51dea7a8a](../../../../../continuum/grouping/cdcfbe8d106844109bb8ded51dea7a8a.md) |
| G08.02 | Responsiveness | subgroup | [47d8e097526248349dd6dbbd318b1ef4](../../../../../continuum/grouping/47d8e097526248349dd6dbbd318b1ef4.md) |
| G08.03 | Scalability | subgroup | [9cf94c0ac09141a3b909a6a1051bff97](../../../../../continuum/grouping/9cf94c0ac09141a3b909a6a1051bff97.md) |
| G08.04 | Resource Management | subgroup | [0e5030d90c134f1ea445257f0b6e788e](../../../../../continuum/grouping/0e5030d90c134f1ea445257f0b6e788e.md) |
| G08.05 | Capacity Planning | subgroup | [f5df30b02e8a4d60bf44e8af2da4d5ab](../../../../../continuum/grouping/f5df30b02e8a4d60bf44e8af2da4d5ab.md) |
| G09 | Operations and Assurance | group | [16ebd2840f2246a4a9ec9991947f5db2](../../../../../continuum/grouping/16ebd2840f2246a4a9ec9991947f5db2.md) |
| G09.01 | Operational Monitoring | subgroup | [cc0a0cc2678c4c7db20ad015a75cc1de](../../../../../continuum/grouping/cc0a0cc2678c4c7db20ad015a75cc1de.md) |
| G09.02 | Operational Diagnostics | subgroup | [45cb08d300b74853b456f333572a2bf6](../../../../../continuum/grouping/45cb08d300b74853b456f333572a2bf6.md) |
| G09.03 | Operational Control | subgroup | [f0402eea7b5a49f9b61fbd90e336b2de](../../../../../continuum/grouping/f0402eea7b5a49f9b61fbd90e336b2de.md) |
| G09.04 | Operational Reporting | subgroup | [b92ea843bd6342879b6a3be255743e31](../../../../../continuum/grouping/b92ea843bd6342879b6a3be255743e31.md) |
| G09.05 | Operational Information Management | subgroup | [da5b4238bd6945cf9b467ff3feb9d76b](../../../../../continuum/grouping/da5b4238bd6945cf9b467ff3feb9d76b.md) |
| G09.06 | Operational Assurance | subgroup | [315ba7bbe4624223a97a7e7e6d9cc252](../../../../../continuum/grouping/315ba7bbe4624223a97a7e7e6d9cc252.md) |
| G10 | Deployment and Release Management | group | [4e5f729c97394b898918591f4d81a69a](../../../../../continuum/grouping/4e5f729c97394b898918591f4d81a69a.md) |
| G10.01 | Build Management | subgroup | [3b56e60b042f429b8bf0efdb477c637e](../../../../../continuum/grouping/3b56e60b042f429b8bf0efdb477c637e.md) |
| G10.02 | Deployment Management | subgroup | [36264352a7f745109deaac2025877690](../../../../../continuum/grouping/36264352a7f745109deaac2025877690.md) |
| G10.03 | Configuration Management | subgroup | [ef476f07cd9e4ba6bdac99dc6e940baa](../../../../../continuum/grouping/ef476f07cd9e4ba6bdac99dc6e940baa.md) |
| G10.04 | Release Management | subgroup | [6b4df7dee51344b3b8c4c6eaace50d72](../../../../../continuum/grouping/6b4df7dee51344b3b8c4c6eaace50d72.md) |
| G10.05 | Release Compatibility | subgroup | [7f9cadb36d764b22840748a9608f9a37](../../../../../continuum/grouping/7f9cadb36d764b22840748a9608f9a37.md) |
| G10.06 | Deployment Environments | subgroup | [1b4044525d474363bafb462e575582b9](../../../../../continuum/grouping/1b4044525d474363bafb462e575582b9.md) |
| G11 | Verification and Testing | group | [d0fa8cfaf5044359a5f6a1e91375429e](../../../../../continuum/grouping/d0fa8cfaf5044359a5f6a1e91375429e.md) |
| G11.01 | Functional Verification | subgroup | [ab65501a8e3542839707ae31cd0b8efd](../../../../../continuum/grouping/ab65501a8e3542839707ae31cd0b8efd.md) |
| G11.02 | Integration Verification | subgroup | [e8c7f8a36ff248cd9115d7a382324808](../../../../../continuum/grouping/e8c7f8a36ff248cd9115d7a382324808.md) |
| G11.03 | Regression Testing | subgroup | [35df8a5d8b1e495ba4cf79a5d4a47863](../../../../../continuum/grouping/35df8a5d8b1e495ba4cf79a5d4a47863.md) |
| G11.04 | Performance and Capacity Testing | subgroup | [cd453ee30502406db647a0c5b4011739](../../../../../continuum/grouping/cd453ee30502406db647a0c5b4011739.md) |
| G11.05 | Resilience Testing | subgroup | [d3fb5abc58af4347ab52cdc26e9b14f1](../../../../../continuum/grouping/d3fb5abc58af4347ab52cdc26e9b14f1.md) |
| G11.06 | Test Governance | subgroup | [7eceab6d8a9741708d438a7760103c69](../../../../../continuum/grouping/7eceab6d8a9741708d438a7760103c69.md) |
| G12 | User Experience | group | [1c57a3d9080443178f6e9159d0591c77](../../../../../continuum/grouping/1c57a3d9080443178f6e9159d0591c77.md) |
| G12.01 | Usability | subgroup | [58c3577b30fd4748836a1d52e07c8a98](../../../../../continuum/grouping/58c3577b30fd4748836a1d52e07c8a98.md) |
| G12.02 | Accessibility | subgroup | [f814f4598722425cb3e7f45c32c11c90](../../../../../continuum/grouping/f814f4598722425cb3e7f45c32c11c90.md) |
| G12.03 | User Interface Responsiveness | subgroup | [1f441cdb8fa342cca0afe5b3b65b368d](../../../../../continuum/grouping/1f441cdb8fa342cca0afe5b3b65b368d.md) |
| G12.04 | Navigation and Interaction | subgroup | [9f192036bee542ad883b89729a35bbfb](../../../../../continuum/grouping/9f192036bee542ad883b89729a35bbfb.md) |
| G12.05 | Localisation and Internationalisation | subgroup | [77cd076a5bdf463a93fe330698d9be19](../../../../../continuum/grouping/77cd076a5bdf463a93fe330698d9be19.md) |
| G13 | Distributed Systems and Temporal Management | group | [c77bba93c0c843959b30d9e1195fb68e](../../../../../continuum/grouping/c77bba93c0c843959b30d9e1195fb68e.md) |
| G13.01 | Distributed Coordination | subgroup | [114a3866dffc4a4cb6aabef39b48fadf](../../../../../continuum/grouping/114a3866dffc4a4cb6aabef39b48fadf.md) |
| G13.02 | Temporal Consistency | subgroup | [0f1014c80b0a473abf8c37845e0ed2fa](../../../../../continuum/grouping/0f1014c80b0a473abf8c37845e0ed2fa.md) |
| G13.03 | Event Ordering and Causality | subgroup | [432d52cc3de5443896a4559f215ece9f](../../../../../continuum/grouping/432d52cc3de5443896a4559f215ece9f.md) |
| G13.04 | Event Horizon Management | subgroup | [22fce0d656ef4df89d2a35a85fa92aa1](../../../../../continuum/grouping/22fce0d656ef4df89d2a35a85fa92aa1.md) |
| G13.05 | Distributed State Consistency | subgroup | [71b9830ce1a543548334647cbbaec7f1](../../../../../continuum/grouping/71b9830ce1a543548334647cbbaec7f1.md) |
| G13.06 | Task Ordering | subgroup | [0528b2ac76c548ae861ebcb1ef9cf3d9](../../../../../continuum/grouping/0528b2ac76c548ae861ebcb1ef9cf3d9.md) |

## Reserved identifiers

`NFR-116`, `NFR-120`, `NFR-128`, `NFR-134`, `NFR-140` and `NFR-144` have no active statement or canonical source binding. No existing architectural identity is retired by these source reservations.

The [relationship accounting](relationships.json) records 219 subject-containment Compositions and three explicit joint-workload Associations. No other relationships are created or modified. Existing relationships and all 1,216 authored Views remain unchanged.
