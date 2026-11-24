<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->

# Preserved 1.1.19 baseline → carried candidate difference assessment

Accepted baseline: `9d4b6e27d69bf8999144ca474acc0e6264f0a2de9d42a8ee41f042cdf6095d09`.
Complete preserved candidate: `ca70443bf8bec1e4546b45c9fb454c9925b87325a79532b2145e14256d79bb7b`.

The maintained comparator was run twice with byte-identical results. Matching uses
existing identities, not labels. Every difference is carried as a governed candidate
change; original scoped acceptance, standing, provenance and review qualifications
remain intact. This assessment supplies no new architectural acceptance.

| Kind | Added | Removed | Modified | Unchanged |
| --- | ---: | ---: | ---: | ---: |
| elements | 2 | 0 | 251 | 1743 |
| relationships | 11 | 0 | 0 | 2235 |
| views | 3 | 0 | 12 | 1273 |

[Complete identity/provenance assessment](difference-assessment.json) · [Maintained architectural delta](architectural-delta.json) · [Raw comparison](raw-comparison.json)

The source candidate and original evidence remain at their originating locations.
No removed record is hidden; each group reports its complete removal list. View
content and View navigation are assessed separately; the full XML preserves all geometry.

## Elements — added

| Existing identity | Before name | Candidate name |
| --- | --- | --- |
| `171f05d188384cedb7d5c493eeaee519` | (unnamed) | Provenance Client Library |
| `e282811b51274649816566aac7edeb8a` | (unnamed) | Provenance |

## Elements — removed

None.

## Elements — modified

| Existing identity | Before name | Candidate name |
| --- | --- | --- |
| `da5b4238bd6945cf9b467ff3feb9d76b` | Operational Information Management | Operational Information Management |
| `0085c4fe23cf4b76829fae3369c9fbb1` | Interoperability-and-Integration.Information-Exchange.Assessments | Information Exchange - Assessments |
| `f6985401675b47c0a1c480aa156aaa0c` | Deployment-and-Release-Management.Configuration-Management.Principles | Configuration Management - Principles |
| `5f25a2e8082d486d8f2b87aec6667e33` | Deployment-and-Release-Management.Configuration-Management.Constraints | Configuration Management - Constraints |
| `58c5443983854dd98cd72f2dd795c063` | Demographic and Contact Management | Demographic and Contact Management |
| `10284034c8534b639284386b064f2c54` | [BusinessObject] Information Unit | [BusinessObject] Information Unit |
| `bbb7438e78604308abba93afc14f4e2e` | REQ-FND-005: Independent Assurance of Governed Activity | REQ-FND-005: Independent Assurance of Governed Activity |
| `5e7bdebc353e49259113adf2a5b93aad` | Distributed-Systems-and-Temporal-Management.Task-Ordering.Requirements | Task Ordering - Requirements |
| `84693ef7bb4a4ced9634737c6bd5a36d` | Security Administration | Security Administration |
| `1cdc1ee62ec548998a8db6ee23b59ca1` | Security-and-Authorisation.Information-Protection.Principles | Information Protection - Principles |
| `cd453ee30502406db647a0c5b4011739` | Performance and Capacity Testing | Performance and Capacity Testing |
| `52508b8b7aec4b1cbaa570576442d4c9` | Consent Communication and History | Consent Communication and History |
| `ad16265c76114f4eaddf75b0be2d4755` | Provider Lifecycle and Administration | Provider Lifecycle and Administration |
| `5de2ae2c3b794c2d9f3a14efd51d564f` | AX-07 Security Is Intrinsic to Managed Operations | [ArchitecturalAxiom] AX-07 Security Is Intrinsic to Managed Operations |
| `d3fb5abc58af4347ab52cdc26e9b14f1` | Resilience Testing | Resilience Testing |
| `31812051912e4dd99b07125d86ca2c11` | Deployment-and-Release-Management.Release-Compatibility.Principles | Release Compatibility - Principles |
| `5280c9c5e5ac43fe8fd314fd74bb6e69` | Processing-and-Workflow.Task-Completion.Requirements | Task Completion - Requirements |
| `f5df30b02e8a4d60bf44e8af2da4d5ab` | Capacity Planning | Capacity Planning |
| `d733a698d2c4477db4a4289144417e3f` | Information-Management.Information-Persistence.Assessments | Information Persistence - Assessments |
| `392c07b6a5ba4fc7bd5c19910437da98` | Representatives and Delegated Consent | Representatives and Delegated Consent |
| `9020808d1ff9466db08ad2f8c9a97542` | Interoperability-and-Integration.Interoperability-Standards.Assessments | Interoperability Standards - Assessments |
| `1af8a56358f7429fabfb57be66542a99` | Mneme | Mneme |
| `9fec9301e759460880557df71baa19c0` | Audit-and-Accountability.Audit-Generation.Requirements | Audit Generation - Requirements |
| `8d58b9333a374b9cbe936f34919fc122` | Performance-and-Capacity.Resource-Management.Principles | Resource Management - Principles |
| `3b56e60b042f429b8bf0efdb477c637e` | Build Management | Build Management |
| `5e8ddc78f9554243aeb051f77595cc88` | Processing State Management | Processing State Management |
| `3189d80ed4c24efe9bfc6b28ab263b51` | Operations-and-Assurance.Operational-Assurance.Requirements | Operational Assurance - Requirements |
| `a5228e483703437ba7617034e4ea207f` | Information-Management.Information-Persistence.Principles | Information Persistence - Principles |
| `ee9bc6c96d944f2ea8bbaaeffb31db28` | Resilience-and-Recovery.Failure-Detection-and-Handling.Constraints | Failure Detection And Handling - Constraints |
| `80aa94b4210047a0a14563f408da1694` | Information-Management.Information-Integrity.Constraints | Information Integrity - Constraints |
| `202ebe880cc8456bb5e9f1389cc26fe8` | Security-and-Authorisation.Authorisation-Enforcement.Constraints | Authorisation Enforcement - Constraints |
| `b47210a43fe147d4a3f2fbc081321063` | Architectural-Governance.Architecture-Conformance.Goals | Architecture Conformance - Goals |
| `e321b3c9558745fcaf58fe96ec8eca05` | Operations-and-Assurance.Operational-Monitoring.Goals | Operational Monitoring - Goals |
| `315ba7bbe4624223a97a7e7e6d9cc252` | Operational Assurance | Operational Assurance |
| `1b4044525d474363bafb462e575582b9` | Deployment Environments | Deployment Environments |
| `73a9255ce3cd4be89c2f5f65868c5f79` | Operations-and-Assurance.Operational-Monitoring.Principles | Operational Monitoring - Principles |
| `5fcada54e6df40a8983335a90e4a9f02` | Identity Registration and Verification | Identity Registration and Verification |
| `1c57a3d9080443178f6e9159d0591c77` | User Experience | User Experience |
| `9536aaf4114b4c35b8a795383d442f64` | Concurrency Management | Concurrency Management |
| `a4fbf334ea6e48028e39eb7f74ced2bb` | Resilience-and-Recovery.Continuity-of-Operations.Goals | Continuity of Operations - Goals |
| `a44091225174403ebd2c5b75ba690a09` | Interoperability Standards | Interoperability Standards |
| `2f97190ae5734e8caee48d581e1a6ab2` | Identity Access and Disclosure | Identity Access and Disclosure |
| `af4f93db10cb47c282600babe51048e1` | Deployment-and-Release-Management.Deployment-Management.Constraints | Deployment Management - Constraints |
| `ba2e95cc11374757bd0945148da43b07` | Identity Quality and Oversight | Identity Quality and Oversight |
| `6d8848b700df4f6eacc0347993ac541a` | PHI Leakage through Operational Logging | PHI Leakage through Operational Logging |
| `3ba98c0c1170458f8c02fa76694fee67` | Distributed Clinical Knowledge → Informed Clinical Collaboration | Distributed Clinical Knowledge → Informed Clinical Collaboration |
| `58c3577b30fd4748836a1d52e07c8a98` | Usability | Usability |
| `ba0e7f67e2e7496d9df8f6617ee39ec3` | Reference Data Management | Reference Data Management |
| `88d1c3fd640448c2b35933d2b03b8151` | Information-Management.Information-Integrity.Principles | Information Integrity - Principles |
| `0b6d5f58354040189b3a9a30a9b913d9` | Task Management | Task Management |
| `47d8e097526248349dd6dbbd318b1ef4` | Responsiveness | Responsiveness |
| `55441c2677144a9da1c6348e633eaac8` | Interoperability-and-Integration.Interaction-Contracts.Requirements | Interaction Contracts - Requirements |
| `bb3adbc3c2fb43e6a18a876c4e897c9c` | Security Monitoring | Security Monitoring |
| `793ed649804a4bf19b5fa92bac3e4c29` | Operations-and-Assurance.Operational-Assurance.Constraints | Operational Assurance - Constraints |
| `582e15cf43f94ce6b1c128361ec31780` | Verification-and-Testing.Performance-and-Capacity-Testing.Principles | Performance and Capacity Testing - Principles |
| `50a4cb6b01c94904a0991175ab09004c` | Deployment-and-Release-Management.Deployment-Management.Principles | Deployment Management - Principles |
| `bc45121331a04af7a54e790bd4a7ea6e` | Interoperability-and-Integration.Interoperability-Standards.Drivers | Interoperability Standards - Drivers |
| `d903a1d46d6e4a3c84d247789e43aa41` | Performance-and-Capacity.Resource-Management.Constraints | Resource Management - Constraints |
| `901d49996c9545bb99cb0e0e7c043c81` | Resilience-and-Recovery.Operational-Recovery.Principles | Operational Recovery - Principles |
| `08ffff9f57aa412b80e36ab154136ad0` | Interoperability-and-Integration.Information-Exchange.Principles | Information Exchange - Principles |
| `899d34ec491948b68aad09e507dea6d1` | Themis | Themis |
| `e576768ae5cd4522b760c36d2b364be2` | Processing-and-Workflow.Message-Processing.Requirements | Message Processing - Requirements |
| `25a15d4bea2d418d918e3a7e01d9fcbf` | Affiliations, Roles and Practice Locations | Affiliations, Roles and Practice Locations |
| `ef296738e68e4206a507dc3c811b3fe1` | Information Management | Information Management |
| `819aeed91d0544c3b7cc52e69aa46d72` | Information Identity | Information Identity |
| `8e39da9fc2d443efa3cc5d0311fcaf79` | Distributed-Systems-and-Temporal-Management.Temporal-Consistency.Principles | Temporal Consistency - Principles |
| `f40da9359c7545d2ad0eef6c3a5bf645` | Processing Outcomes | Processing Outcomes |
| `8b27323cbaba400698842f77e0d28c0d` | Terminology and Reference Data | Terminology and Reference Data |
| `afb9686c9f1c48159d92a518a7d17452` | Processing and Workflow | Processing-and-Workflow |
| `432d52cc3de5443896a4559f215ece9f` | Event Ordering and Causality | Event Ordering and Causality |
| `1a235d13959e477ea52e0f201215fbe9` | Resilience-and-Recovery.Operational-Recovery.Requirements | Operational Recovery - Requirements |
| `d8d1f0aeff354b2696d3d6475c44c36e` | Interoperability-and-Integration.Interaction-Contracts.Principles | Interaction Contracts - Principles |
| `9f192036bee542ad883b89729a35bbfb` | Navigation and Interaction | Navigation and Interaction |
| `de791c4976f84676bf8545b1cfb8bf41` | Audit Management | Audit Management |
| `9c613c6963e9451e945d3934a20bdd53` | Audit-and-Accountability.Audit-Management.Principles | Audit Management - Principles |
| `b01782f63be44bc9ab2f6ebbf84b37f2` | Architectural-Governance.Architecture-Vision.Goals | Architecture Vision - Goals |
| `c445fc55a6e547ae9b1a5bb43cad1a8e` | Provider Directory Discovery | Provider Directory Discovery |
| `b05d68295ed141d6815aa56c8a3d3b39` | Processing-and-Workflow.Processing-Outcomes.Outcomes | Processing Outcomes - Outcomes |
| `de7e90f46127455a9ec9ad3b9b48ba54` | AX-16 Operational Activity & Entity State Progress Together | AX-16 Operational Activity & Entity State Progress Together |
| `43ea78d2aa5e4e0eb4e0c80a6ddb22dd` | Performance-and-Capacity.Responsiveness.Requirements | Responsiveness - Requirements |
| `114a3866dffc4a4cb6aabef39b48fadf` | Distributed Coordination | Distributed Coordination |
| `49966f8f01e244b4926ce4dc4c41fb47` | Disaster Recovery | Disaster Recovery |
| `df0b38c3ba9c49ea8dfe3f032ad2a329` | Resilience-and-Recovery.Operational-Recovery.Constraints | Operational Recovery - Constraints |
| `a762424d13254eaa9467946d9d7355f8` | Security and Authorisation | Security-and-Management |
| `4e0bcac2a001411c8802b5df30e6bb38` | Terminology Release and Versioning | Terminology Release and Versioning |
| `9c7c9de7ffc0457d87cf52548bca70ca` | Information-Management.Information-Identity.Constraints | Information Identity - Constraints |
| `5989495f1e224d5797054a8b6c0364a1` | Architecture Conformance | Architecture Conformance |
| `5120fbb391e24b3f941d8a5cc347a1fd` | User-Experience.Navigation-and-Interaction.Principles | Navigation and Interaction - Principles |
| `f85948e2b4e145a4b56ab369c1a70c22` | Security-and-Authorisation.Authorisation-Enforcement.Principles | Authorisation Enforcement - Principles |
| `db0cac8c53b7404b8f2c17a542db7604` | Interoperability-and-Integration.Interoperability-Standards.Principles | Interoperability Standards - Principles |
| `41040498bcaf469686edbee208b9b349` | Interoperability-and-Integration.Interaction-Reliability.Principles | Interaction Reliability - Principles |
| `8bf2a60efafc4caf89ed9744adfb628d` | Implementation Strategy | Implementation Strategy |
| `f71f084dd9154318b86ca22f0fe8c36d` | User-Experience.Localisation-and-Internationalisation.Principles | Localisation and Internationalisation - Principles |
| `ef476f07cd9e4ba6bdac99dc6e940baa` | Configuration Management | Configuration Management |
| `d931781bb22243fdb892937b88141cf4` | Information-Management.Information-Identity.Principles | Information Identity - Principles |
| `63807867e2d147528542a914952b3e3e` | Operations-and-Assurance.Operational-Information-Management.Principles | Operational Information Management - Principles |
| `08d8ee19a30f418f9d5bc497ced0d1ef` | [Topic] TaskOutcome | [Topic] TaskOutcome |
| `36264352a7f745109deaac2025877690` | Deployment Management | Deployment Management |
| `aba77f43a26440dfb5e7cd67934e7ebd` | Resilience-and-Recovery.Continuity-of-Operations.Principles | Continuity of Operations - Principles |
| `709d34e55e7b4c7a9e010f5465f76473` | Resilience-and-Recovery.Continuity-of-Operations.Requirements | Continuity of Operations - Requirements |
| `8655d81cd41647449c3b1a10ffd453b3` | Terminology Mapping and Validation | Terminology Mapping and Validation |
| `75519ed8f7de4a619cc9065f525d703a` | Demonstrable Protection and Accountable Handling of Health Information (O3) | Demonstrable Protection and Accountable Handling of Health Information (O3) |
| `41900f15d1d2431295a4b9dc1aa752c0` | Continuity of Operations | Continuity of Operations |
| `7b2cabb4089d414d8ad700ef5f7932b7` | Directory Verification and Quality | Directory Verification and Quality |
| `f814f4598722425cb3e7f45c32c11c90` | Accessibility | Accessibility |
| `77cd076a5bdf463a93fe330698d9be19` | Localisation and Internationalisation | Localisation and Internationalisation |
| `38d3dfcd23894f43b59d00c782170e63` | Accountability and Traceability | Accountability and Traceability |
| `3566f7776b8741699f48139e1c6b8ec8` | Resilience-and-Recovery.Fault-Tolerance.Principles | Fault Tolerance - Principles |
| `45cb08d300b74853b456f333572a2bf6` | Operational Diagnostics | Operational Diagnostics |
| `f6b8d4c19f9649a0bdde6727182855b6` | Information-Management.Information-Sovereignty-and-Residency.Principles | Information Sovereignty and Residency - Principles |
| `16ebd2840f2246a4a9ec9991947f5db2` | Operations and Assurance | Operations and Assurance |
| `f9035f49dbab4676b23ab9e4f45678f9` | Security-and-Authorisation.Security-Administration.Constraints | Security Administration - Constraints |
| `25bc65e773d64ecf9b71ae4f92ed57e1` | Exceptional and Emergency Consent | Exceptional and Emergency Consent |
| `66ec039ab4f042a8b4817ca1dafaedfe` | Security-and-Authorisation.Information-Protection.Stakeholders | Information Protection - Stakeholders |
| `d2876dff4f5444e3b71bdeba487b2d6b` | Directory Information Maintenance | Directory Information Maintenance |
| `40416671099a46678ac5dfc3d0d615cf` | Audit Generation | Audit Generation |
| `b83d63bb553c4642b26bbc41f66445b3` | Terminology Definition and Maintenance | Terminology Definition and Maintenance |
| `b8cafe3cf5904d2bb9a9767260fddf78` | AX-18 Integration and Coordination Do Not Confer Domain Authority | [ArchitecturalAxiom] AX-18 Integration and Coordination Do Not Confer Domain Authority |
| `20326d2f4f2e44dcbe12db38deea02d0` | Performance-and-Capacity.Throughput.Requirements | Throughput - Requirements |
| `f1b3b73329ae46b9b5105fb8ff236be5` | Implicit Perimeter Trust | Implicit Perimeter Trust |
| `7985e647bf9e4ca3ba70faf9e3e4ae02` | Mnemosyne | Mnemosyne |
| `1d7c50343cca49eeaeccf7c49c3c008b` | Interaction Reliability | Interaction Reliability |
| `416367b1da96412299df199827b3541f` | Processing-and-Workflow.Concurrency-Management.Principles | Concurrency Management - Principles |
| `011207c2c69f42d7a5c9c7504ffbc951` | Interoperability-and-Integration.External-Connectivity.Principles | External Connectivity - Principles |
| `b23f7fa8cb1b46569f63bde1aa5fbf1f` | Information Lifecycle | Information Lifecycle |
| `549a5d805b3f40d49437ce704f923bf0` | Architectural-Governance.Implementation-Strategy.Constraints | Implementation Strategy - Constraints |
| `a58e87c95bd24c16a1006b217aeac691` | Provider Identity and Registration | Provider Identity and Registration |
| `477d6408de0642fc875ce79d8e9dee86` | Processing-and-Workflow.Processing-Outcomes.Requirements | Processing Outcomes - Requirements |
| `c5f38ca4adc24f03a2b885938186e509` | Verification-and-Testing.Test-Governance.Constraints | Test Governance - Constraints |
| `4f80f28b3dad490abe942e5bb37e1a80` | Architectural-Governance.Architecture-Vision.Stakeholders | Architecture Vision - Stakeholders |
| `f7d9c92661ed44af99516400635041bf` | Resilience-and-Recovery.Disaster-Recovery.Constraints | Disaster Recovery - Constraints |
| `f14ca9b3213d4c3db3c5d479b4cc938e` | Information-Management.Information-Lifecycle.Principles | Information Lifecycle - Principles |
| `cdcfbe8d106844109bb8ded51dea7a8a` | Throughput | Throughput |
| `5a4a41f263424c1aac14102b64f95bd0` | Processing-and-Workflow.Processing-Outcomes.Principles | Processing Outcomes - Principles |
| `0233415953ed487fbbe407ebb469e7bc` | Provider Directory | Provider Directory |
| `0f1014c80b0a473abf8c37845e0ed2fa` | Temporal Consistency | Temporal Consistency |
| `b558d26d626b4276bd5ec38b8d42213a` | Operations-and-Assurance.Operational-Monitoring.Constraints | Operational Monitoring - Constraints |
| `c18ef04adb54413d89bd9434a344504f` | Audit-and-Accountability.Audit-Management.Constraints | Audit Management - Constraints |
| `b91ec2110aaf4d44bd043bb787fbd910` | Identity Matching and Reconciliation | Identity Matching and Reconciliation |
| `2423108223e14d27a7bf59610034297a` | Security-and-Authorisation.Security-Monitoring.Principles | Security Monitoring - Principles |
| `e8c7f8a36ff248cd9115d7a382324808` | Integration Verification | Integration Verification |
| `a49c1e5afd224d12bae33fac366a1d37` | Security-and-Authorisation.Information-Protection.Constraints | Information Protection - Constraints |
| `76b2a8e5551e4645a048f48e4820a0bc` | Information-Management.Information-Persistence.Constraints | Information Persistence - Constraints |
| `23408174240a47f690d23dee589e4ddc` | User-Experience.Accessibility.Requirements | Accessibility - Requirements |
| `3d87dbf35e0a43a3b5e12cbc6b947763` | Consent Verification and Decisions | Consent Verification and Decisions |
| `b1997b3f14724bb983c06963fa7b4ea6` | Security-and-Authorisation.Security-Administration.Assessments | Security Administration - Assessments |
| `e84139e878c740ab81c558cbc7ec6548` | Security-and-Authorisation.Information-Protection.Requirements | Information Protection - Requirements |
| `b12837a817d14919b5c33b35e3bb3d5a` | Audit-and-Accountability.Accountability-and-Traceability.Requirements | Accountability and Traceability - Requirements |
| `f29ffa95c89d470b8a32e1369696327f` | Authentication | Authentication |
| `4cc994e1de714cc8915dc965d2694808` | Interoperability-and-Integration.External-Connectivity.Assessments | External Connectivity - Assessments |
| `f5838796139b4157b91b53dfd56ea0a2` | Interoperability-and-Integration.Information-Exchange.Outcomes | Information Exchange - Outcomes |
| `4e5f729c97394b898918591f4d81a69a` | Deployment and Release Management | Deployment and Release Management |
| `d56a556f29ff4ba78634e58d103c786e` | Audit-and-Accountability.Audit-Generation.Principles | Audit Generation - Principles |
| `801386f27cda458b8ce811f133d917e9` | Identity Maintenance and Status | Identity Maintenance and Status |
| `2a791e0e1f6b45c0a57fb8233f73faa5` | [Topic] Order | [Topic] Order |
| `c0ef85ab954f447fb244149dfcc44aad` | Security-and-Authorisation.Authorisation.Constraints | Authorisation - Constraints |
| `6fceb4d8857a4601b736a33e9bec140a` | Architectural-Governance.Architecture-Process.Constraints | Architecture Process - Constraints |
| `99436452c53a49b89fa6bc2863557572` | Operations-and-Assurance.Operational-Diagnostics.Goals | Operational Diagnostics - Goals |
| `549eaf0604764c01814641872404017f` | Processing-and-Workflow.Message-Processing-View.Assessments | Message Processing View - Assessments |
| `f812d4f33612403986c174602b358a2f` | Resilience-and-Recovery.Continuity-of-Operations.Drivers | Continuity of Operations - Drivers |
| `b6ff7b82c59e4125868902e34d89630d` | Security-and-Authorisation.Information-Protection.Outcomes | Information Protection - Outcomes |
| `b92ea843bd6342879b6a3be255743e31` | Operational Reporting | Operational Reporting |
| `fd9d0fa7117a4dc3b45cfed0e7be614b` | Provider and Workforce Management | Provider and Workforce Management |
| `5c36fbab326d484a9f7877b7fb6a2ff9` | Processing-and-Workflow.Task-Completion.Principles | Task Completion - Principles |
| `71b9830ce1a543548334647cbbaec7f1` | Distributed State Consistency | Distributed State Consistency |
| `c189826cd7024780abd89fa4f067947a` | Security-and-Authorisation.Security-Administration.Principles | Security Administration - Principles |
| `400c5c54a23a44588cc473ed10ead4a8` | Reference Data Quality and Governance | Reference Data Quality and Governance |
| `e5d75469d336405a8f187368a7dc0353` | Information-Management.Information-Persistence.Goals | Information Persistence - Goals |
| `46dd1f4490ae47cc854ce70ff600cb47` | Architectural-Governance.Architecture-Conformance.Constraints | ArchitectureConformance - Constraints |
| `fe8916b03b084e82af587ac5a900349b` | Authorisation Enforcement | Authorisation Enforcement |
| `c77bba93c0c843959b30d9e1195fb68e` | Distributed Systems and Temporal Management | Distributed Systems and Temporal Management |
| `8609b046777c4a8ab4d5cafb30e34d04` | Operational Recovery | Operational Recovery |
| `445d396942f549e5891086bd72fa5b44` | Operations-and-Assurance.Operational-Control.Constraints | Operational Control - Constraints |
| `8882573df7ce43c6bc0f66da0a676139` | AX-17 Architectural Authority and Explicit Uncertainty | [ArchitecturalAxiom] AX-17 Architectural Authority and Explicit Uncertainty |
| `eef79a8840434bd6b6fd37ab4f8ff75d` | Performance-and-Capacity.Resource-Management.Requirements | Resource Management - Requirements |
| `93adee1dcb7348dd8bc070e7a53264f6` | Architectural-Governance.Implementation-Strategy.Principles | Implementation Strategy - Principles |
| `884f06591f6c4dcf8e7f94e4b31269c1` | Consent Capture and Maintenance | Consent Capture and Maintenance |
| `a47808600e8f4d7c83756f4ce77b498e` | Provider Profile Maintenance | Provider Profile Maintenance |
| `b3c19413e4b447adb5f1d86912db4052` | Interaction Contracts | Interaction Contracts |
| `2d89b3a1e74e42d28a9f0a3dfb2c4ecb` | Task Completion | Task Completion |
| `fb464349f13b41c4ae9920a743fc6c12` | [Topic] Encounter | [Topic] Encounter |
| `914b8be39ecc4fb599e6bc0004c355c0` | Resilience and Recovery | Resilience and Recovery |
| `de3ff3302e504029b5b5f3f18fe9c380` | Audit-and-Accountability.Audit-Integrity.Requirements | Audit Integrity - Requirements |
| `8034148e63e6457f90e8cae1d4eb4453` | Performance and Capacity | Performance and Capacity |
| `d90a28dcf8294c89a7a06022a3617aba` | Processing-and-Workflow.Concurrency-Management.Constraints | Concurrency Management - Constraints |
| `e9804b6dea5c4e1db80be1925463786f` | Failure Detection and Handling | Failure Detection and Handling |
| `0a59e0ebb33c494e939148d30e42f49c` | Fault Tolerance | Fault Tolerance |
| `02e036f18bab400bac261007e39457ee` | Unqualified Information → Governed, Consumable Information | Unqualified Information → Governed, Consumable Information |
| `028a5b5f4b0e458a91489cac0513955d` | Performance-and-Capacity.Scalability.Constraints | Scalability - Constraints |
| `eabbd522676448fcb075f7d4b5ef4434` | Architectural-Governance.Architecture-Vision.Requirements | Architecture Vision - Requirements |
| `7eceab6d8a9741708d438a7760103c69` | Test Governance | Test Governance |
| `03cfbc2cac494fb9a15106c2c571d384` | Architectural Governance | Architectural Governance |
| `2ad6333e5858456dba8aefeec71773a5` | Credentials and Professional Standing | Credentials and Professional Standing |
| `805280b737e14b72ba401393c1f4a271` | Operations-and-Assurance.Operational-Diagnostics.Drivers | Operational Diagnostics - Drivers |
| `60fcf91dfa7445999dfa8e039d993415` | Security-and-Authorisation.Security-Monitoring.Requirements | Security Monitoring - Requirements |
| `35df8a5d8b1e495ba4cf79a5d4a47863` | Regression Testing | Regression Testing |
| `dc8500b4e7eb4923884f54db99d10a88` | Architectural-Governance.Architecture-Vision.Drivers | Architecture Vision - Drivers |
| `4648cf5a1f8e4333978c13efae10eaa5` | Authorisation | Authorisation |
| `54d5ba1ccaae46c8b372f7ccfca99741` | Architectural-Governance.Architecture-Vision.Constraints | Architecture Vision - Constraints |
| `0528b2ac76c548ae861ebcb1ef9cf3d9` | Task Ordering | Task Ordering |
| `7f9cadb36d764b22840748a9608f9a37` | Release Compatibility | Release Compatibility |
| `d739c75dffe2453081735710b285f03b` | Dokimasia | Dokimasia |
| `098d2e468cd14029b43ec6dbd07374fd` | Interoperability and Integration | Interoperability and Integration |
| `f4af2b5206264a6fa4c5040324ad272c` | Information Sovereignty and Residency | Information Sovereignty and Residency |
| `67bfa710070c4618b3ba834da62cc63d` | Operations-and-Assurance.Operational-Diagnostics.Stakeholders | Operational Diagnostics - Stakeholders |
| `ab65501a8e3542839707ae31cd0b8efd` | Functional Verification | Functional Verification |
| `fefda110e2274884ad58906733606efd` | Kleio | Kleio |
| `9cf94c0ac09141a3b909a6a1051bff97` | Scalability | Scalability |
| `a9deca9adbb443208d6f08dbe4505717` | Patient Identity and Demographics | Patient Identity and Demographics |
| `0b9e18356e9e4dcca51007f03f7b81b4` | External Connectivity | External Connectivity |
| `493e4a05fe76496f82caf26e68f15edd` | User-Experience.Usability.Principles | Usability - Principles |
| `1dbf34b452b6497f914c704e037aec24` | Statutory Health Information Privacy & Protection | Statutory Health Information Privacy & Protection |
| `1eb3b965c7334b899133282bc0500a9d` | Operations-and-Assurance.Operational-Diagnostics.Principles | Operational Diagnostics - Principles |
| `bda335658c29473d9ba20352242c83f8` | Information Exchange | Information Exchange |
| `4643d0b24fe7420b8e3fd0f4a2f4b1de` | User-Experience.User-Interface-Responsiveness.Requirements | User Interface Responsiveness - Requirements |
| `b683ae87c72a4c16aba948a4b8cb0998` | [BusinessObject] Search Result Set | [BusinessObject] Search Result Set |
| `22fce0d656ef4df89d2a35a85fa92aa1` | Event Horizon Management | Event Horizon Management |
| `9badf0d360b64215bc60e74eec954018` | [Topic] Appointment | [Topic] Appointment |
| `99e7cebde18640df841ac7f5c0c996be` | Processing-and-Workflow.Message-Processing.Constraints | Message Processing - Constraints |
| `f147597cd5e04696887257c6316eedf7` | Operations-and-Assurance.Operational-Information-Management.Assessments | Operational Information Management - Assessments |
| `9a5487f0dd7b42bca1f791aad1257849` | Security-and-Authorisation.Authorisation-Enforcement.Requirements | Authorisation Enforcement - Requirements |
| `3b06c335246c4307adb5801e05f2cef4` | Processing-and-Workflow.Task-Completion.Stakeholders | Task Completion - Stakeholders |
| `d0fa8cfaf5044359a5f6a1e91375429e` | Verification and Testing | Verification and Testing |
| `aeb3b98e54124d94b4a68ab18de60514` | Audit-and-Accountability.Audit-Generation.Constraints | Audit Generation - Constraints |
| `3f830af6837d4f89bcd042039572bcdc` | Interoperability-and-Integration.External-Connectivity.Drivers | External Connectivity - Drivers |
| `0e5030d90c134f1ea445257f0b6e788e` | Resource Management | Resource Management |
| `5dde6ebecd094084b325ba59603bb919` | Message Processing | Message Processing |
| `c102ede2b9e249b2a2ddc9f19e0cbd7f` | Audit Integrity | Audit Integrity |
| `d2f8fcaac9984a12beab502d1944ea25` | Information Protection | Information Protection |
| `6b4df7dee51344b3b8c4c6eaace50d72` | Release Management | Release Management |
| `b6a20afe29624f73906e5e852d6f2fbf` | Directory Publication and Visibility | Directory Publication and Visibility |
| `2ee0a6e46b47413d91cfb71ee3100e4b` | Architecture Process | Architecture Process |
| `87694e76de1d4b93af63a16a9f5de128` | Architectural-Governance.Architecture-Vision.Principles | Architecture Vision - Principles |
| `0caf2fb84f944a6ab3872feb92b9338d` | Security-and-Authorisation.Information-Protection.Assessments | Information Protection - Assessments |
| `980b414d73614bd48bc47a6aaa718a51` | Security-and-Authorisation.Security-Administration.Stakeholders | Security Administration - Stakeholders |
| `8b7fd22a55434e5ca3e7ba87afb020d6` | Information Persistence | Information Persistence |
| `4df45b11a8c64144acea7c240a2c93d1` | Interoperability-and-Integration.Information-Exchange.Constraints | Information Exchange - Constraints |
| `e1ed88f315a84116a77ead21d8fe1792` | Directory Synchronisation | Directory Synchronisation |
| `eb1b0526b898448688a322cce7ea6e74` | Information Integrity | Information Integrity |
| `ce45a338a1fc4d11a8d64ce898119f94` | Security-and-Authorisation.Information-Protection.Drivers | Information Protection - Drivers |
| `5e78ed0ddec14bb4b081082c66c75ad2` | Resilience-and-Recovery.Continuity-of-Operations.Constraints | Continuity of Operations - Constraints |
| `abca473311ea40aca58756d859077ff7` | Consent and Information Sharing | Consent and Information Sharing |
| `395d77eea25d40b0b74321dc7aabd283` | Security-and-Authorisation.Authentication.Requirements | Authentication - Requirements |
| `1de844b879754fd7a21a29f2df3df97b` | Interoperability-and-Integration.Information-Exchange.Requirements | Information Exchange - Requirements |
| `1f441cdb8fa342cca0afe5b3b65b368d` | User Interface Responsiveness | User Interface Responsiveness |
| `f0402eea7b5a49f9b61fbd90e336b2de` | Operational Control | Operational Control |
| `e48346674b0740c19f4a13cc50a7ae66` | Consent Scope and Preferences | Consent Scope and Preferences |
| `82837737622a46059703e99a8920533e` | Verification-and-Testing.Resilience-Testing.Constraints | Resilience Testing - Constraints |
| `6d6086134162468cac129040a139d8d9` | Architecture Vision | Architecture Vision |
| `abc27ddff6d644d9945db0cfd7488f07` | Audit and Accountability | Audit and Accountability |
| `cc0a0cc2678c4c7db20ad015a75cc1de` | Operational Monitoring | Operational Monitoring |

## Relationships — added

| Existing identity | Before name | Candidate name |
| --- | --- | --- |
| `id-0077a1c0c70c4c50a9b90e7f91a1cf8c` | (unnamed) | References relevant Pragma instances |
| `id-45281b5e60ee44aba4aa848d479e306b` | (unnamed) | Provides reusable Provenance Client Library |
| `id-47cd0528b7464d7ca6c7ad2a9e98d759` | (unnamed) | Creates and reports Provenance for originating components and activities |
| `id-47d6e2bd19d342d982c0c195ca4916ce` | (unnamed) | References execution activities performed by Ergon |
| `id-48b5618f63b24ea5abbbcd2cd58d70f2` | (unnamed) | Manages governed Provenance information |
| `id-6bf9674e35c04cd88c11a80cdf777a17` | (unnamed) | References relevant InformationUnits |
| `id-8590dea2bc74433dbc99f17927cdc092` | (unnamed) | (unnamed) |
| `id-86502907d244442aad6470652bc3fd19` | (unnamed) | Manages Provenance lifecycle and uses it as foundational Assurance evidence |
| `id-cfb102b2ad0c47cdad7a6b2031afa877` | (unnamed) | Historical audit evidence may contribute to independent Assurance |
| `id-f8b0d09b13764f7298735519bb998d26` | (unnamed) | Establishes authoritative durable persistence of Provenance |
| `id-f9ee3e4f6faf4c61b97ce7d1494d86a5` | (unnamed) | References associated execution Context |

## Relationships — removed

None.

## Relationships — modified

None.

## Views — added

| Existing identity | Before name | Candidate name |
| --- | --- | --- |
| `id-36a82d51b7cb4611a65ace4b06488484` | (unnamed) | information-management-view |
| `id-7095a65ca0be48fe8b8eecad63e01815` | (unnamed) | security-and-management-view |
| `id-93b3ce60b18a4bf2b931779f1dde91e2` | (unnamed) | architecture-governance-view |

## Views — removed

None.

## Views — modified

| Existing identity | Before name | Candidate name |
| --- | --- | --- |
| `id-25907c1ad9c84890ba36f0ce47e09283` | Information-Persistence-View | Information-Persistence-View |
| `id-6429b82599df4027a745d84cfacf587c` | Information-Sovereignty-and-Residency-View | Information-Sovereignty-and-Residency-View |
| `id-6fc97b179aaa48b3aeb4696e8f3b2227` | Implementation-Strategy-View | Implementation-Strategy-View |
| `id-702144c188c44a4292b338927ac17ad6` | Architecture-Vision-View | Architecture-Vision-View |
| `id-7c0d291a29f34feab7be4d9f29a08027` | Architecture-Conformance-View | Architecture-Conformance-View |
| `id-81cfffb25890400d9292449a8a375366` | Architecture-Process-View | Architecture-Process-View |
| `id-9873a9236c5a453bb1f2426cd94ec3a1` | Authorisation-View | Authorisation-View |
| `id-abcc22053a464d7aa1f05b41e0fade01` | Information-Integrity-View | Information-Integrity-View |
| `id-bfbf78c6cb8448118468197fc9269ad9` | Information-Lifecycle-View | Information-Lifecycle-View |
| `id-da484996370e48e5979e58f7196b0f98` | Authorisation-Enforcement-View | Authorisation-Enforcement-View |
| `id-e541e847576142029495840acd9250e2` | Authentication-View | Authentication-View |
| `id-ee264a765fb547859c50a3dc74e1dfdf` | Information-Identity-View | Information-Identity-View |
