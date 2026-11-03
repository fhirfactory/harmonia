# Pass 1 adjudication inventory — 05-current-unresolved-business_enabling-01

[Review guide](../README.md) · [Complete identity index](../adjudication-inventory.md) · [Dependencies](../dependencies.md)

Recommendations only. Every SA decision is **PENDING**; every incorporation status is **NOT AUTHORISED**. Row references are scoped review locators, not architectural identities. Detailed evidence below each table preserves the original context and the exact standing of comparison targets.

| Reference | Canonical Identity | Name | Element Type | Current Standing | Definition | Architectural Context | Historical Evidence | Current Equivalent | Equivalence Assessment | Recommended Disposition | Recommended Treatment | Rationale | Consequential Changes | Solution Architect Decision | Incorporation Status |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| [P1-062](#p1-062) | 28df27d55c514a37995145ab46945f78 | [CT1] Care Administration Enablement | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems. | Source classification: business_enabling; tier CT1. Source frame: not established. 17 explicit incident native relationship(s); see [details](#p1-062). Navigation is independent of containment. | [Source and provenance](#p1-062) · [canonical Markdown](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | No same-instance equivalent established. [[CT2] Episode &amp; Encounter Administration](../../../../../../../continuum/strategy/capability/61d7067aee0540259b3daca4287d66d2.md); [[CT2] Diagnostic Administration](../../../../../../../continuum/strategy/capability/3cc720ba2e8b4d7a94bb349391d61895.md); [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md); [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md); [[CT2] Clinical Charging &amp; Claims Administration](../../../../../../../continuum/strategy/capability/af78ab7c89454a608cd4d34ec7703331.md); [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md); [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md); [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md); [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md); [[CT2] Medication Administration](../../../../../../../continuum/strategy/capability/c907e2a95629435b9951941ad4e2ebe9.md); [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md); [[CT2] Funding &amp; Coverage Administration](../../../../../../../continuum/strategy/capability/640432d413cf47928ea2b3d221f92313.md); [[CT2] Order Administration](../../../../../../../continuum/strategy/capability/c3c5abd7987f4433a46cbfed8167914c.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems. Its explicit boundary is: Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-048](#p1-048) | 232db9b8b47c450cb0aa9acaf539c09d | [CT1] Clinical Service Dependency Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. | Source classification: business_enabling; tier CT1. Source frame: not established. 16 explicit incident native relationship(s); see [details](#p1-048). Navigation is independent of containment. | [Source and provenance](#p1-048) · [canonical Markdown](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | No same-instance equivalent established. [[CT2] Allied Health](../../../../../../../continuum/strategy/capability/63cd714a3e2a4354aa5383b189fea566.md); [[CT2] Therapeutic Services](../../../../../../../continuum/strategy/capability/b512b6d326004cb7b91a627fe6dbd8be.md); [[CT2] Clinical Assessment](../../../../../../../continuum/strategy/capability/a7d84c95e48c453797419813ca80a66c.md); [[CT2] Remote &amp; Virtual Care](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md); [[CT2] Preventive Care](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md); [[CT2] Diagnostic Services](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md); [[CT2] Ambulatory Care](../../../../../../../continuum/strategy/capability/f74d7c3a39c540c4b462576e8f09fc58.md); [[CT2] Outreach Care](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md); [[CT2] Medication Therapy](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md); [[CT2] Emergency Care](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md); [[CT2] Community Care](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md); [[CT2] Surgical &amp; Procedural Care](../../../../../../../continuum/strategy/capability/c9a26d1aa1014545b9ae759b663c807b.md); [[CT2] Acute Care](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md); [[CT2] Inpatient Care](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md); [[CT2] Rehabilitation](../../../../../../../continuum/strategy/capability/99ed51abcc5e4e8ba3b3d0161b17544a.md); [[CT2] Primary Care](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-316](#p1-316) | b5a92874f8ac4509a30af5253590edc1 | [CT1] Entity Information Enablement | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices. | Source classification: business_enabling; tier CT1. Source frame: not established. 7 explicit incident native relationship(s); see [details](#p1-316). Navigation is independent of containment. | [Source and provenance](#p1-316) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | No same-instance equivalent established. [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md); [[CT2] Client Administration](../../../../../../../continuum/strategy/capability/f0885472cced4bccbcce1568bf0360dc.md); [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md); [[CT2] Health Materiel Administration](../../../../../../../continuum/strategy/capability/e7081070a9f8444f81c1d8fce37ef664.md); [[CT2] Health Service Administration](../../../../../../../continuum/strategy/capability/ba68a52913a0499d8377553bddfa74a4.md); [[CT2] Organisation Administration](../../../../../../../continuum/strategy/capability/14fbdc5ce3dd4c2092c5397dab3ed743.md); [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices. Its explicit boundary is: Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-291](#p1-291) | a790b5f2c5a1497fa6ec09d7824da32e | [CT1] Health Informatics Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. | Source classification: business_enabling; tier CT1. Source frame: not established. 4 explicit incident native relationship(s); see [details](#p1-291). Navigation is independent of containment. | [Source and provenance](#p1-291) · [canonical Markdown](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | No same-instance equivalent established. [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md); [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md); [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md); [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. Its explicit boundary is: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-304](#p1-304) | b13ee2c5a4ef4fd6be8a44795696dfc2 | [CT1] Health Service Operations Enablement | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. | Source classification: business_enabling; tier CT1. Source frame: not established. 26 explicit incident native relationship(s); see [details](#p1-304). Navigation is independent of containment. | [Source and provenance](#p1-304) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | No same-instance equivalent established. [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md); [[CT2] Clinical Competency Administration](../../../../../../../continuum/strategy/capability/8a99f4a6e5054d6c997bacec04805c01.md); [[CT2] Clinical Training &amp; Education](../../../../../../../continuum/strategy/capability/d016f35ffbc345d99d7281e968c14ddb.md); [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md); [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md); [[CT2] Clinic &amp; Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md); [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md); [[CT2] Service Establishment Support](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md); [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md); [[CT2] On-Call Management](../../../../../../../continuum/strategy/capability/b16cef8f2bfd4b92948ec75cbcfa7b9a.md); [[CT2] Emergency Department Operations](../../../../../../../continuum/strategy/capability/24a4b5815faa4d369bc756a6e97d3e9e.md); [[CT2] Clinical Workforce Capability Monitoring](../../../../../../../continuum/strategy/capability/367b3e5643f44fa785f093db8bb9a6e8.md); [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md); [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md); [[CT2] Mobile Staff Management](../../../../../../../continuum/strategy/capability/79a4a48fddf74c69acf7e16404d06ea5.md); [[CT2] Discharge Management](../../../../../../../continuum/strategy/capability/ec5e8d6732764aa489c2dd7630fc8fef.md); [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md); [[CT2] Clinical Rostering](../../../../../../../continuum/strategy/capability/192ca88a289544dcbf6adaf643f39969.md); [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md); [[CT2] Work Allocation &amp; Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md); [[CT2] Credential Management](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. Its explicit boundary is: Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-166](#p1-166) | 616682b612d244429e656b040e002d56 | [CT1] Independent Assurance Enablement | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. | Source classification: business_enabling; tier CT1. Source frame: not established. 3 explicit incident native relationship(s); see [details](#p1-166). Navigation is independent of containment. | [Source and provenance](#p1-166) · [canonical Markdown](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | No same-instance equivalent established. [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md); [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. Its explicit boundary is: Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-002](#p1-002) | 0062fcacae1740988b6be2bda2aa1674 | [CT1] Shared Information &amp; Coordination Enablement | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. | Source classification: business_enabling; tier CT1. Source frame: not established. 19 explicit incident native relationship(s); see [details](#p1-002). Navigation is independent of containment. | [Source and provenance](#p1-002) · [canonical Markdown](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | No same-instance equivalent established. [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md); [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md); [[CT2] Clinical Knowledge Services](../../../../../../../continuum/strategy/capability/bbd2bd21859f40e39963bdbe7a851686.md); [[CT2] Health Information Exchange Enablement](../../../../../../../continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md); [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md); [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md); [[CT2] Information Design Governance](../../../../../../../continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md); [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md); [[CT2] Presentation Services](../../../../../../../continuum/strategy/capability/e9b6ba1917ba44cca7466444d0836c98.md); [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md); [[CT2] Patient Clinical Record](../../../../../../../continuum/strategy/capability/5478878e0e6e4d34a8d5023ac6ac2d54.md); [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md); [[CT2] Health Information Access](../../../../../../../continuum/strategy/capability/cc253aea3c5f489da0502df74dde1669.md); [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. Its explicit boundary is: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-269](#p1-269) | 9bb988c526404522b83aeed5069e0eb5 | [CT2] Acute Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 2 explicit incident native relationship(s); see [details](#p1-269). Navigation is independent of containment. | [Source and provenance](#p1-269) · [canonical Markdown](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-306](#p1-306) | b1a791458d644d4794895e4fa42ec83c | [CT2] Assurance Service Review | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md). 2 explicit incident native relationship(s); see [details](#p1-306). Navigation is independent of containment. | [Source and provenance](#p1-306) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | No same-instance equivalent established. [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md); [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. The source frame [CT1] Independent Assurance Enablement concerns: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Independent Assurance Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-322](#p1-322) | b81775d89d774cf8ba61731d13c67a54 | [CT2] Assurance Specification | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Model how satisfaction will be assured and maintain applicable, temporally qualified criteria. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md). 3 explicit incident native relationship(s); see [details](#p1-322). Navigation is independent of containment. | [Source and provenance](#p1-322) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | No same-instance equivalent established. [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md); [[CT3] Assurance Criteria Administration](../../../../../../../continuum/strategy/capability/1bfd71f24321428c8871b587d413f47e.md); [[CT3] Assurance Design](../../../../../../../continuum/strategy/capability/71c8a612b88f4ecba049e94d506bd0c2.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Model how satisfaction will be assured and maintain applicable, temporally qualified criteria. The source frame [CT1] Independent Assurance Enablement concerns: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Independent Assurance Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-255](#p1-255) | 942aa78cad004ef99de6b3aae8ddd014 | [CT2] Community Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-255). Navigation is independent of containment. | [Source and provenance](#p1-255) · [canonical Markdown](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-045](#p1-045) | 21eed4fc8c924496b16c6ddaa32f3d01 | [CT2] Credential Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md). 2 explicit incident native relationship(s); see [details](#p1-045). Navigation is independent of containment. | [Source and provenance](#p1-045) · [canonical Markdown](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) | No same-instance equivalent established. [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md); [[CT3] Clinical Service Credential Definition Management](../../../../../../../continuum/strategy/capability/2e5c97f51e7e48d1b865ac416dd8099d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. The source frame [CT1] Health Service Operations Enablement concerns: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Service Operations Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-324](#p1-324) | b825ebab42f444d8b738b4c1f489f785 | [CT2] Diagnostic Services | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-324). Navigation is independent of containment. | [Source and provenance](#p1-324) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-295](#p1-295) | aa088f0647ad4b78966fe28fa80af674 | [CT2] Emergency Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 2 explicit incident native relationship(s); see [details](#p1-295). Navigation is independent of containment. | [Source and provenance](#p1-295) · [canonical Markdown](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-207](#p1-207) | 7925605bf60f4b288e826020409a09d9 | [CT2] Health Information Communication | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md). 5 explicit incident native relationship(s); see [details](#p1-207). Navigation is independent of containment. | [Source and provenance](#p1-207) · [canonical Markdown](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | No same-instance equivalent established. [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md); [[CT3] Patient Notification Services](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md); [[CT3] Patient Health Library Access](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md); [[CT3] Patient Task Communication Services](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md); [[CT3] Patient Self-Service](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. The source frame [CT1] Shared Information &amp; Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information &amp; Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-153](#p1-153) | 5bac15316d8d4f4db6e7a253f1acfbe3 | [CT2] Information Asset Stewardship Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md). 3 explicit incident native relationship(s); see [details](#p1-153). Navigation is independent of containment. | [Source and provenance](#p1-153) · [canonical Markdown](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) | No same-instance equivalent established. [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md); [[CT3] Information Asset Definition](../../../../../../../continuum/strategy/capability/05c58b2ccfcc45e9a6c70d9a569c4f61.md); [[CT3] Information Asset Tag Management](../../../../../../../continuum/strategy/capability/df27c3ba6e164e56af7bff4e1eb1954a.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. The source frame [CT1] Shared Information &amp; Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information &amp; Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-323](#p1-323) | b81a1ab42af24c2e9896b8f3f78d291a | [CT2] Information Content Maintainer Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer information-content maintainer participation, source provenance and governed information products. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md). 4 explicit incident native relationship(s); see [details](#p1-323). Navigation is independent of containment. | [Source and provenance](#p1-323) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | No same-instance equivalent established. [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md); [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md); [[CT3] Data Scientist Administration](../../../../../../../continuum/strategy/capability/3bb1a11fba234717bf40c9e6452f401a.md); [[CT3] Information Product Administration](../../../../../../../continuum/strategy/capability/b5a6a31855834ea58870ebb6c51ed4b0.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer information-content maintainer participation, source provenance and governed information products. The source frame [CT1] Shared Information &amp; Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information &amp; Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-310](#p1-310) | b39fb49c01b545a5b51d3313531efb1b | [CT2] Inpatient Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 2 explicit incident native relationship(s); see [details](#p1-310). Navigation is independent of containment. | [Source and provenance](#p1-310) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-331](#p1-331) | bb04996164c94e0c827a020432f4e498 | [CT2] Medication Therapy | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-331). Navigation is independent of containment. | [Source and provenance](#p1-331) · [canonical Markdown](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-328](#p1-328) | b9ddbe2dd6a7485ba9cd3482215ea09e | [CT2] Outreach Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-328). Navigation is independent of containment. | [Source and provenance](#p1-328) · [canonical Markdown](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-043](#p1-043) | 2135b45eab034370b0063b20e821985d | [CT2] Population Health Information Reporting | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md). 6 explicit incident native relationship(s); see [details](#p1-043). Navigation is independent of containment. | [Source and provenance](#p1-043) · [canonical Markdown](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | No same-instance equivalent established. [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md); [[Feature] Population Notification Obligation Tracking](../../../../../../../continuum/strategy/capability/feature/62f6a91a10684876b7d08b536700f12f.md); [[CT2] Population Health Surveillance](../../../../../../../continuum/strategy/capability/83e568fa8af14d7791a7253765ea390f.md); [[CT2] Operational Assurance](../../../../../../../continuum/strategy/capability/938cd44380ab431fa8dae13c702d83c1.md); [[CT2] Event &amp; Subscription](../../../../../../../continuum/strategy/capability/b768b129971e4bf5bccc03d33d2fcb4e.md); [[CT2] Interoperability &amp; Exchange](../../../../../../../continuum/strategy/capability/0f2e5222874949d8b0cce234941ecb20.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-397](#p1-397) | dfdc4f0273ba4910848b3b65c9ef6ab2 | [CT2] Preventive Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-397). Navigation is independent of containment. | [Source and provenance](#p1-397) · [canonical Markdown](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-369](#p1-369) | d118673d77d74c8cb8cec24f03adddd8 | [CT2] Primary Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 2 explicit incident native relationship(s); see [details](#p1-369). Navigation is independent of containment. | [Source and provenance](#p1-369) · [canonical Markdown](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-077](#p1-077) | 30dff22d40b64da29ede7c41673db085 | [CT2] Remote &amp; Virtual Care | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Identify and track the operational and infrastructure dependencies necessary for remote &amp; virtual care services to function, retaining each dependency provider's responsibility. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md). 1 explicit incident native relationship(s); see [details](#p1-077). Navigation is independent of containment. | [Source and provenance](#p1-077) · [canonical Markdown](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md) | No same-instance equivalent established. [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for remote &amp; virtual care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-126](#p1-126) | 4f5b8cec90324c2989cdc30e80bc8cb9 | [CT2] Research Information Curation | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md). 3 explicit incident native relationship(s); see [details](#p1-126). Navigation is independent of containment. | [Source and provenance](#p1-126) · [canonical Markdown](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | No same-instance equivalent established. [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md); [[Feature] Authorised Research Release Assembly](../../../../../../../continuum/strategy/capability/feature/dcebc2f2b5b7494a8e80a02402664078.md); [[CT2] Research Information Support](../../../../../../../continuum/strategy/capability/ef18f999eef3488ba584d263a551732b.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-041](#p1-041) | 20e67a41e7f44398b0f0cf3d379d8b9c | [CT2] Service Establishment Support | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Support the information needed to establish a health service in its intended operating context. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md). 1 explicit incident native relationship(s); see [details](#p1-041). Navigation is independent of containment. | [Source and provenance](#p1-041) · [canonical Markdown](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md) | No same-instance equivalent established. [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Support the information needed to establish a health service in its intended operating context. The source frame [CT1] Health Service Operations Enablement concerns: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Service Operations Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-116](#p1-116) | 4873cd4b960d45d8b93560d5c8b98500 | [CT2] Service Planning Data Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Establish and maintain governed information assets suitable for health-service planning, modelling and analysis. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md). 3 explicit incident native relationship(s); see [details](#p1-116). Navigation is independent of containment. | [Source and provenance](#p1-116) · [canonical Markdown](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) | No same-instance equivalent established. [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md); [[CT2] Health-System Planning](../../../../../../../continuum/strategy/capability/9ca4df07eceb4150a0190cc488270ce2.md); [[Feature] Demand &amp; Capacity Planning Projection](../../../../../../../continuum/strategy/capability/feature/4f97baa3aed24b8385bf3ecda4eab622.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Establish and maintain governed information assets suitable for health-service planning, modelling and analysis. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-348](#p1-348) | c724f3d38bfa4567983a11ad22095d61 | [CT2] Workflow &amp; Activity Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. | Source classification: business_enabling; tier CT2. Source frame: [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md). 6 explicit incident native relationship(s); see [details](#p1-348). Navigation is independent of containment. | [Source and provenance](#p1-348) · [canonical Markdown](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | No same-instance equivalent established. [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md); [[CT3] Activity Routing Management](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md); [[CT3] External Task Status &amp; Traceability Management](../../../../../../../continuum/strategy/capability/b25bb04a8aa94dcfac105a646878ccfc.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md); [[CT3] External Task Coordination](../../../../../../../continuum/strategy/capability/ff3447c929724245a13ee864874b5561.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. The source frame [CT1] Shared Information &amp; Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information &amp; Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-059](#p1-059) | 27411c143e6e41ffba1af139fbbcd7e2 | [CT3] Activity Routing Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md). 1 explicit incident native relationship(s); see [details](#p1-059). Navigation is independent of containment. | [Source and provenance](#p1-059) · [canonical Markdown](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. The source frame [CT2] Workflow &amp; Activity Coordination concerns: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Workflow &amp; Activity Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-211](#p1-211) | 7b3ba9bcac564dd4a7a08631581db2ca | [CT3] Appointment &amp; Booking Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer externally supplied appointment and booking information and communicate changes in their scheduling status. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md). 1 explicit incident native relationship(s); see [details](#p1-211). Navigation is independent of containment. | [Source and provenance](#p1-211) · [canonical Markdown](../../../../../../../continuum/strategy/capability/7b3ba9bcac564dd4a7a08631581db2ca.md) | No same-instance equivalent established. [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer externally supplied appointment and booking information and communicate changes in their scheduling status. The source frame [CT2] Scheduling Administration concerns: Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Scheduling Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-006](#p1-006) | 039343518db542d48cde6afab84a3e1e | [CT3] Assurance Service Evaluation | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md). 1 explicit incident native relationship(s); see [details](#p1-006). Navigation is independent of containment. | [Source and provenance](#p1-006) · [canonical Markdown](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) | No same-instance equivalent established. [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. The source frame [CT2] Assurance Service Review concerns: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Assurance Service Review. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-057](#p1-057) | 26a9383d7bb146a19141558d232843de | [CT3] Bed &amp; Care-Place State Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md). 1 explicit incident native relationship(s); see [details](#p1-057). Navigation is independent of containment. | [Source and provenance](#p1-057) · [canonical Markdown](../../../../../../../continuum/strategy/capability/26a9383d7bb146a19141558d232843de.md) | No same-instance equivalent established. [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. The source frame [CT2] Bed &amp; Care-Place Management concerns: Bed &amp; Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Bed &amp; Care-Place Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-108](#p1-108) | 3c9464f9eae1415d8fa041bd1f3da4f2 | [CT3] Bed &amp; Care-Place Turnover Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md). 1 explicit incident native relationship(s); see [details](#p1-108). Navigation is independent of containment. | [Source and provenance](#p1-108) · [canonical Markdown](../../../../../../../continuum/strategy/capability/3c9464f9eae1415d8fa041bd1f3da4f2.md) | No same-instance equivalent established. [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. The source frame [CT2] Bed &amp; Care-Place Management concerns: Bed &amp; Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Bed &amp; Care-Place Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-122](#p1-122) | 4cb7db65392f4fab801d7794af4d4b29 | [CT3] Care Plan Lifecycle &amp; Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md). 1 explicit incident native relationship(s); see [details](#p1-122). Navigation is independent of containment. | [Source and provenance](#p1-122) · [canonical Markdown](../../../../../../../continuum/strategy/capability/4cb7db65392f4fab801d7794af4d4b29.md) | No same-instance equivalent established. [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. The source frame [CT2] Care Plan Administration concerns: Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Care Plan Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-167](#p1-167) | 629b493c7d9844baa25c029a87ed04f6 | [CT3] Care-Place Definition Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md). 1 explicit incident native relationship(s); see [details](#p1-167). Navigation is independent of containment. | [Source and provenance](#p1-167) · [canonical Markdown](../../../../../../../continuum/strategy/capability/629b493c7d9844baa25c029a87ed04f6.md) | No same-instance equivalent established. [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place. The source frame [CT2] Location Administration concerns: Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Describes a place and its capabilities; operational bed state and readiness remain separately managed. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Location Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-429](#p1-429) | f51f8d6b7556424987c76d9a7465c67c | [CT3] Client Intake &amp; Demographic Update | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the established client intake &amp; demographic update information and coordination responsibility while preserving its source authority. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md). 1 explicit incident native relationship(s); see [details](#p1-429). Navigation is independent of containment. | [Source and provenance](#p1-429) · [canonical Markdown](../../../../../../../continuum/strategy/capability/f51f8d6b7556424987c76d9a7465c67c.md) | No same-instance equivalent established. [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the established client intake &amp; demographic update information and coordination responsibility while preserving its source authority. The source frame [CT2] Client-Centred Administration concerns: Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Client-Centred Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-424](#p1-424) | f11617eed6ff4d6fa237644ec95b28b2 | [CT3] Clinical Context Collaboration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md). 1 explicit incident native relationship(s); see [details](#p1-424). Navigation is independent of containment. | [Source and provenance](#p1-424) · [canonical Markdown](../../../../../../../continuum/strategy/capability/f11617eed6ff4d6fa237644ec95b28b2.md) | No same-instance equivalent established. [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. The source frame [CT2] Clinical Collaboration concerns: Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Collaboration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-368](#p1-368) | d0a1ba80c790476ea6042290eb7ea09e | [CT3] Clinical Device Profile Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md). 1 explicit incident native relationship(s); see [details](#p1-368). Navigation is independent of containment. | [Source and provenance](#p1-368) · [canonical Markdown](../../../../../../../continuum/strategy/capability/d0a1ba80c790476ea6042290eb7ea09e.md) | No same-instance equivalent established. [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities. The source frame [CT2] Clinical Device Administration concerns: Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Describes device types and capabilities without taking regulatory approval or clinical-use authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Device Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-395](#p1-395) | df683100067b47e785037c27cfd17a3c | [CT3] Clinical Documentation Lifecycle Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md). 1 explicit incident native relationship(s); see [details](#p1-395). Navigation is independent of containment. | [Source and provenance](#p1-395) · [canonical Markdown](../../../../../../../continuum/strategy/capability/df683100067b47e785037c27cfd17a3c.md) | No same-instance equivalent established. [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. The source frame [CT2] Clinical Record Administration concerns: Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Record Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-360](#p1-360) | cca86a32ba8c4dda84caeaa248e9f444 | [CT3] Clinical Escalation Path Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md). 1 explicit incident native relationship(s); see [details](#p1-360). Navigation is independent of containment. | [Source and provenance](#p1-360) · [canonical Markdown](../../../../../../../continuum/strategy/capability/cca86a32ba8c4dda84caeaa248e9f444.md) | No same-instance equivalent established. [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. The source frame [CT2] Clinical Supervision concerns: Enable responsible clinical participants to identify and use the applicable clinical escalation path. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Supervision. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |

<a id="p1-062"></a>
## P1-062 — [CT1] Care Administration Enablement

Canonical identity: `28df27d55c514a37995145ab46945f78`; [governed source](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Overview**

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Intent**

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Boundary**

Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Care Administration Information Enablement",
  "architectural_status": "Proposed",
  "definition": "Receive, correlate and communicate the administrative requests, episodes, fulfilment records and documents through which care is arranged and recorded.",
  "overview": "Referrals, bookings, encounters, orders and clinical documents have different progression and acknowledgement meanings. Their common ability is to make care administration intelligible across organisations without making those transactions interchangeable.",
  "intent": "Keep the requested, accepted, performed and recorded aspects of care administration distinguishable as information crosses systems.",
  "boundary": "Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance.",
  "tier": "CT1",
  "category": "business_enabling"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-0ff026d172245933a65bfb576771c552 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Episode &amp; Encounter Administration](../../../../../../../continuum/strategy/capability/61d7067aee0540259b3daca4287d66d2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-243b6e37b771560c9b95ddc234e45b0a | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Diagnostic Administration](../../../../../../../continuum/strategy/capability/3cc720ba2e8b4d7a94bb349391d61895.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-371c90009eea5b24a69e5ad07cbc8b87 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-3d3dadbc8cc659c0bafee65dc980a009 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-6236459728eb58fc8b76e4ebeaa710a2 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Clinical Charging &amp; Claims Administration](../../../../../../../continuum/strategy/capability/af78ab7c89454a608cd4d34ec7703331.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-677a2c2c85b156789fbc4333cbf0c87d | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-958822b3fad8502daaaecd1e60128205 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-97365a5002535a30bb477718755cc2db | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Care Financial Information Enablement](../../../../../../../continuum/strategy/capability/59a756b89c604e3e97ee5dede2d6ed89.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a818dfea85e35a3cab7f84309d604766 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Care Fulfilment Administration Enablement](../../../../../../../continuum/strategy/capability/9f0f4379c1954524ba5e691d0c7d95ab.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a8e96c967238574391d0ce2e944598a7 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-ae3668c1170b542f8672bd2d4e4b94ae | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-b23acadd5432516896eba26d03840690 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Medication Administration](../../../../../../../continuum/strategy/capability/c907e2a95629435b9951941ad4e2ebe9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-bc9dcf00b2795ff8bf6a4f2116abe3c0 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c8cf4ab80069578a9cd29d97a64817ba | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Care Documentation &amp; Communication Enablement](../../../../../../../continuum/strategy/capability/5ad2e381ec484d1d9062a65eee842fa7.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d5a892665b035138936bb527acebeb34 | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Funding &amp; Coverage Administration](../../../../../../../continuum/strategy/capability/640432d413cf47928ea2b3d221f92313.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-def0a301eaf9549e9618fd95c316d3df | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Order Administration](../../../../../../../continuum/strategy/capability/c3c5abd7987f4433a46cbfed8167914c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-efdf1abf796252299ee349805eef136b | Composition | [[CT1] Care Administration Enablement](../../../../../../../continuum/strategy/capability/28df27d55c514a37995145ab46945f78.md) | [[CT2] Care Access Transaction Enablement](../../../../../../../continuum/strategy/capability/b65973b576154d3a96cf458bf33df53a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Episode & Encounter Administration](../../../../../../../continuum/strategy/capability/61d7067aee0540259b3daca4287d66d2.md) — `61d7067aee0540259b3daca4287d66d2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: This capability maintains identifiable encounter contexts and their governed progression, including association with care teams and locations. It enables information from an admission, visit or community contact to be understood within the relevant episode of activity. Encounter progression is an administrative and operational account, separate from the clinical acts that occur during it.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Diagnostic Administration](../../../../../../../continuum/strategy/capability/3cc720ba2e8b4d7a94bb349391d61895.md) — `3cc720ba2e8b4d7a94bb349391d61895`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Diagnostic Administration correlates requests with specimens and encounters and integrates diagnostic reports into their request context. It connects the laboratory or imaging contribution with the participants expecting an outcome, preserving report identity and standing as updates occur. Its enabling role differs from performing the diagnostic procedure or professionally interpreting evidence.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md) — `649dd7cc26094e539386a050086dfb91`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md) — `ae0de25600ee416aa24d2a167b64f230`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Charging & Claims Administration](../../../../../../../continuum/strategy/capability/af78ab7c89454a608cd4d34ec7703331.md) — `af78ab7c89454a608cd4d34ec7703331`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: This capability supplies clinical activity summaries and charging records to billing contexts. It preserves a route from activity information to the systems responsible for financial processing without treating the export as a completed bill or claim. No Feature decomposition or comprehensive revenue-cycle responsibility is established.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md) — `35fb3744c900470f9895056246a13fb1`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md) — `8752b7a937754b2faa296300c6a48911`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md) — `ce9e7e7b22054cefb707a650d5be7219`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md) — `b38f2c2811724734b5887b6dbaaabbfc`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Medication Administration](../../../../../../../continuum/strategy/capability/c907e2a95629435b9951941ad4e2ebe9.md) — `c907e2a95629435b9951941ad4e2ebe9`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: In this enabling catalogue, Medication Administration integrates medication orders, dispensing evidence and point-of-care administration records. It preserves the distinct events through which intended therapy becomes supplied and, where reported, administered. This administrative integration supports a coherent medication account across systems without itself dispensing or administering a medicine.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md) — `cf06eec5699f406fa30a00d2f76e3098`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Funding & Coverage Administration](../../../../../../../continuum/strategy/capability/640432d413cf47928ea2b3d221f92313.md) — `640432d413cf47928ea2b3d221f92313`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Funding & Coverage Administration represents externally supplied insurance coverage and eligibility flags needed as care context. It allows those administrative conditions to be available without implying that Harmonia determines entitlements. The established Adjacent standing and absence of Features bound the current responsibility to this information contribution.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Order Administration](../../../../../../../continuum/strategy/capability/c3c5abd7987f4433a46cbfed8167914c.md) — `c3c5abd7987f4433a46cbfed8167914c`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Order Administration manages the closed loop around a request for defined fulfilment. It receives the request, resolves and routes to a destination, distinguishes delivery from business disposition, tracks progress and associates the eventual outcome. This makes the difference between an order sent and an order fulfilled visible throughout the requester's interaction.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems. Its explicit boundary is: Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/720`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-care-administration-information-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-fe7b5e3e02bb53f59ab89c27a81e9e18"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail
- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 668; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### AX-14 — Semantic Distinctions Are Preserved

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-048"></a>
## P1-048 — [CT1] Clinical Service Dependency Management

Canonical identity: `232db9b8b47c450cb0aa9acaf539c09d`; [governed source](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

**Overview**

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-043aea128eb850fdaa02f14f76ff870e | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Allied Health](../../../../../../../continuum/strategy/capability/63cd714a3e2a4354aa5383b189fea566.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-17935c9735b15bebb0ddcb2c69b47d0c | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Therapeutic Services](../../../../../../../continuum/strategy/capability/b512b6d326004cb7b91a627fe6dbd8be.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-1ea900ddfb2c5a938dc191a60f532b15 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Clinical Assessment](../../../../../../../continuum/strategy/capability/a7d84c95e48c453797419813ca80a66c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-3551f41796b657b2ab75877ded20695f | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Remote &amp; Virtual Care](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-43cce9d2c8b4518db3b2473ee119bbb7 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Preventive Care](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-445690e23e845885ae1e0e89cc4a3fcc | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Diagnostic Services](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-6becc39029d35ed8b2e15aa61ec50eea | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Ambulatory Care](../../../../../../../continuum/strategy/capability/f74d7c3a39c540c4b462576e8f09fc58.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-7b21a12993ee5494ae0c4ab99817413b | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Outreach Care](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a72519a45a0750da967ba829c7f8d2db | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Medication Therapy](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-b79c26e8964658f083ee2f5a9de41c56 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Emergency Care](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c32d3d347681509e94a346636bcf85f4 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Community Care](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c66319b6fedc5864b0ff0b3ab1bb4d8c | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Surgical &amp; Procedural Care](../../../../../../../continuum/strategy/capability/c9a26d1aa1014545b9ae759b663c807b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c9586e2870c6516aa39063a4ffc255c6 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Acute Care](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d5cfc47123f959c1a477f229106a3ceb | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Inpatient Care](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f7114c80d2ac571d88c5c1a4d7425046 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Rehabilitation](../../../../../../../continuum/strategy/capability/99ed51abcc5e4e8ba3b3d0161b17544a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f888507b858e5f41908fc81ae950747d | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Primary Care](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Allied Health](../../../../../../../continuum/strategy/capability/63cd714a3e2a4354aa5383b189fea566.md) — `63cd714a3e2a4354aa5383b189fea566`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for allied health services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Therapeutic Services](../../../../../../../continuum/strategy/capability/b512b6d326004cb7b91a627fe6dbd8be.md) — `b512b6d326004cb7b91a627fe6dbd8be`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for therapeutic services services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Assessment](../../../../../../../continuum/strategy/capability/a7d84c95e48c453797419813ca80a66c.md) — `a7d84c95e48c453797419813ca80a66c`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for clinical assessment services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Remote & Virtual Care](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md) — `30dff22d40b64da29ede7c41673db085`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-077 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-077`).
- [[CT2] Preventive Care](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md) — `dfdc4f0273ba4910848b3b65c9ef6ab2`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-397 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-397`).
- [[CT2] Diagnostic Services](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md) — `b825ebab42f444d8b738b4c1f489f785`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-324 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-324`).
- [[CT2] Ambulatory Care](../../../../../../../continuum/strategy/capability/f74d7c3a39c540c4b462576e8f09fc58.md) — `f74d7c3a39c540c4b462576e8f09fc58`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for ambulatory care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Outreach Care](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md) — `b9ddbe2dd6a7485ba9cd3482215ea09e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-328 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-328`).
- [[CT2] Medication Therapy](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md) — `bb04996164c94e0c827a020432f4e498`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-331 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-331`).
- [[CT2] Emergency Care](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md) — `aa088f0647ad4b78966fe28fa80af674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-295 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-295`).
- [[CT2] Community Care](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md) — `942aa78cad004ef99de6b3aae8ddd014`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-255 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-255`).
- [[CT2] Surgical & Procedural Care](../../../../../../../continuum/strategy/capability/c9a26d1aa1014545b9ae759b663c807b.md) — `c9a26d1aa1014545b9ae759b663c807b`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for surgical & procedural care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Acute Care](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md) — `9bb988c526404522b83aeed5069e0eb5`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-269 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-269`).
- [[CT2] Inpatient Care](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md) — `b39fb49c01b545a5b51d3313531efb1b`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-310 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-310`).
- [[CT2] Rehabilitation](../../../../../../../continuum/strategy/capability/99ed51abcc5e4e8ba3b3d0161b17544a.md) — `99ed51abcc5e4e8ba3b3d0161b17544a`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for rehabilitation services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Primary Care](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) — `d118673d77d74c8cb8cec24f03adddd8`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-369 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-369`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/871`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-clinical-service-dependency-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7afbcec0122951079c2bb647251be323"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-316"></a>
## P1-316 — [CT1] Entity Information Enablement

Canonical identity: `b5a92874f8ac4509a30af5253590edc1`; [governed source](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Overview**

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Intent**

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Boundary**

Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Healthcare Entity Information Enablement",
  "architectural_status": "Proposed",
  "definition": "Maintain the identity, descriptive structure and qualified relationships of the participants, places, services, products and devices referenced in healthcare activity.",
  "overview": "A care transaction needs to identify its subject, provider, service and place before it can be interpreted safely. This frame brings together those relatively enduring descriptions while keeping each kind of entity and relationship accountable within its own boundary.",
  "intent": "Make the participant, place and service references in a care concern consistently interpretable across organisational boundaries.",
  "boundary": "Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority.",
  "tier": "CT1",
  "category": "business_enabling"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-05fb0b3f26d8545a8caf8fcb8a92b2e3 | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-3fc7839fceda5f4193e58dd14415a0ab | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Client Administration](../../../../../../../continuum/strategy/capability/f0885472cced4bccbcce1568bf0360dc.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-b297858a4ab352cb942b88df159109ed | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-be1babf8397d51b3878b271f8775d711 | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Health Materiel Administration](../../../../../../../continuum/strategy/capability/e7081070a9f8444f81c1d8fce37ef664.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c099301d131c594580c840a6363997a3 | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Health Service Administration](../../../../../../../continuum/strategy/capability/ba68a52913a0499d8377553bddfa74a4.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c97120325e3e5a1b99a11eeec1e5c89e | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Organisation Administration](../../../../../../../continuum/strategy/capability/14fbdc5ce3dd4c2092c5397dab3ed743.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f7deddb1eb2751aa96ba6a490b18003e | Composition | [[CT1] Entity Information Enablement](../../../../../../../continuum/strategy/capability/b5a92874f8ac4509a30af5253590edc1.md) | [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) — `d6312f85968a4209bafff5ed009297a6`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Client Administration](../../../../../../../continuum/strategy/capability/f0885472cced4bccbcce1568bf0360dc.md) — `f0885472cced4bccbcce1568bf0360dc`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Client Administration provides the representation and demographic context needed to recognise individuals receiving care. It brings identity, healthcare-subject context, relationships and privacy into the same administrative frame while preserving their different responsibilities. The established partial chain to Person Identity is retained; the catalogue's other contextual descriptions do not supply a complete capability hierarchy.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md) — `c410005667444f99a221483bf577fad2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Health Materiel Administration](../../../../../../../continuum/strategy/capability/e7081070a9f8444f81c1d8fce37ef664.md) — `e7081070a9f8444f81c1d8fce37ef664`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Health Product Administration retains the reference context for medications, implants, blood products and consumables. Product definitions, formularies and batch or serial information help explain what is being referenced without assigning a supply-chain implementation to Harmonia. No Feature decomposition is established for this responsibility in the present baseline.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Health Service Administration](../../../../../../../continuum/strategy/capability/ba68a52913a0499d8377553bddfa74a4.md) — `ba68a52913a0499d8377553bddfa74a4`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Health Service Administration represents identifiable services and the conditions under which organisations, locations and practitioner roles provide them. Catalogue publishing, service-location-provider association, operating schedules and eligibility definitions allow a service to be understood as more than a location or specialty label. The service context remains material wherever activity meaning or accountability depends on it.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Organisation Administration](../../../../../../../continuum/strategy/capability/14fbdc5ce3dd4c2092c5397dab3ed743.md) — `14fbdc5ce3dd4c2092c5397dab3ed743`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Organisation Administration maintains the identity, profiles, structural relationships and contact context of participating healthcare organisations. It enables a legal or operational entity to be distinguished from a department, network relationship or communication channel. This supports consistent interpretation of providers and destinations across information flows rather than allowing each interface to create an incompatible organisational account.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) — `31bcd6fda0d14cc8b992cdca39a3cbed`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices. Its explicit boundary is: Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/719`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-healthcare-entity-information-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-4726f9b8f6015e019238b5a8cc596c7c"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail
- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 668; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### AX-14 — Semantic Distinctions Are Preserved

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-291"></a>
## P1-291 — [CT1] Health Informatics Management

Canonical identity: `a790b5f2c5a1497fa6ec09d7824da32e`; [governed source](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Overview**

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Intent**

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Boundary**

Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Secondary Health Information Use Enablement",
  "architectural_status": "Proposed",
  "definition": "Provide existing health information for authorised research, required population notification and service planning purposes.",
  "overview": "Provide existing health information for authorised research, required population notification and service planning purposes. Its constituent abilities distinguish .",
  "intent": "Preserve the particular authorised purpose and obligation when clinical information is used beyond individual care delivery.",
  "boundary": "Each purpose has different release criteria. This grouping does not originate research approval, public-health decisions or planning authority.",
  "tier": "CT2",
  "category": "business_enabling",
  "parent_key": "proposal:bec-governed-health-information-enablement"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-0417eabe7e4452cc814cc035b17053e8 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-2ff13a06ac0555eaa8e520306db3dc7a | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-df70babccbeb5977897dd2421f46d135 | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-fc7b33bb093f56d491bf7174adc9e842 | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Shared Information & Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) — `0062fcacae1740988b6be2bda2aa1674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.
  Boundary: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.
  Independent adjudication: P1-002 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-002`).
- [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) — `4873cd4b960d45d8b93560d5c8b98500`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.
  Boundary: Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.
  Independent adjudication: P1-116 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-116`).
- [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) — `2135b45eab034370b0063b20e821985d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.
  Boundary: Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.
  Independent adjudication: P1-043 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-043`).
- [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) — `4f5b8cec90324c2989cdc30e80bc8cb9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.
  Boundary: Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.
  Independent adjudication: P1-126 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-126`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. Its explicit boundary is: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/763`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-secondary-health-information-use-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-dc7f84d80543529cab8e2ccc86a0428c"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-304"></a>
## P1-304 — [CT1] Health Service Operations Enablement

Canonical identity: `b13ee2c5a4ef4fd6be8a44795696dfc2`; [governed source](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Overview**

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Intent**

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Boundary**

Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Care Operations Coordination Enablement",
  "architectural_status": "Proposed",
  "definition": "Represent and coordinate care-setting flow, operational resources, workforce participation, movement and service-delivery preparedness.",
  "overview": "Operational coordination connects available resources with actual care-setting constraints and observable work progression. Bed state, local capacity, aggregate capacity, transport and discharge remain separate abilities even when they exchange the same milestones.",
  "intent": "Make operational constraints and progression visible enough to coordinate the care-delivery responsibilities actually assigned to Harmonia.",
  "boundary": "Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.",
  "tier": "CT1",
  "category": "business_enabling"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-00bb91295bb45912bb57a7f8ca2c3ac8 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-17f9d5116b7a5293be59a58af15b77df | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Competency Administration](../../../../../../../continuum/strategy/capability/8a99f4a6e5054d6c997bacec04805c01.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-30c640b5c00f58e9b64174415cc26efd | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Training &amp; Education](../../../../../../../continuum/strategy/capability/d016f35ffbc345d99d7281e968c14ddb.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-320ffe30d9c05809a2734f8388a1df5b | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-34e77a9793715ec5858a067ca3831952 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-377be234fffe51fd92677bd95aef452a | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinic &amp; Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-398ca80f48ed5a3099a15e45349e5b65 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Operational Workforce Coordination](../../../../../../../continuum/strategy/capability/66e72222f2064a23adbdb0311092918b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-3eaf49b58fc35634b4dacbdd55974027 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-489049abd29459d59385476753d9fecf | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Service Establishment Support](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-59df38bb3e6b5b40a251777b93fa486c | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-5ae522c3b073561fb50a0a6108f95dbf | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] On-Call Management](../../../../../../../continuum/strategy/capability/b16cef8f2bfd4b92948ec75cbcfa7b9a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-63b93c19fc6355daadb2e4a1fdb29017 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Emergency Department Operations](../../../../../../../continuum/strategy/capability/24a4b5815faa4d369bc756a6e97d3e9e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-6d1df70784335fed8d27fea078c07a89 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Workforce Qualification &amp; Development Enablement](../../../../../../../continuum/strategy/capability/d24a1f4c4f674c45a95c317998ff4186.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-78a557aef2b85390b756c365782aea6e | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Workforce Capability Monitoring](../../../../../../../continuum/strategy/capability/367b3e5643f44fa785f093db8bb9a6e8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-846df338b9a458aa91b7b2a0d247162f | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-8865da6932a95fb68965a5e96b4158a5 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-9905ba8b55dc5ae8b3e4932aed0a1100 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Care Resource &amp; Capacity Coordination](../../../../../../../continuum/strategy/capability/96f71cb8fa6844d7aa7e9f16759a3a62.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-9deea1af7f4053488b2fe3f520acb47b | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Mobile Staff Management](../../../../../../../continuum/strategy/capability/79a4a48fddf74c69acf7e16404d06ea5.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a102efceedf1574f9e329f905b850238 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Discharge Management](../../../../../../../continuum/strategy/capability/ec5e8d6732764aa489c2dd7630fc8fef.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a6fbe348e1c05f41bdd8d009684200cf | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c687ad2b813f5528bb7412010351ce84 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Rostering](../../../../../../../continuum/strategy/capability/192ca88a289544dcbf6adaf643f39969.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d376141049955ac09fcca1cef5a3e99d | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Care Movement &amp; Transition Coordination](../../../../../../../continuum/strategy/capability/59b9331fa3c04ef09bf963ad8a5d0d71.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-dc2897327e875ef2b1dccc8d0cc0cc07 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Care Setting Operations Enablement](../../../../../../../continuum/strategy/capability/a03741e6e98642bea44680d23fdcb434.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-de277a4b6d0350559cde8fe47df35362 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-e8eaf177a6485043aceec3c43d9188bb | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Work Allocation &amp; Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f5f81df65e5d539ca9b42b93e894fa35 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Credential Management](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md) — `4bf7a8260e1444fdac44e97c3557b533`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Competency Administration](../../../../../../../continuum/strategy/capability/8a99f4a6e5054d6c997bacec04805c01.md) — `8a99f4a6e5054d6c997bacec04805c01`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer information about what clinical capability an organisation can provide and what a practitioner is competent to perform.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Training & Education](../../../../../../../continuum/strategy/capability/d016f35ffbc345d99d7281e968c14ddb.md) — `d016f35ffbc345d99d7281e968c14ddb`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Training & Education remains a Reference context for developing practitioners' knowledge and skills. The catalogue recognises its enterprise significance without assigning training delivery or a Feature decomposition to Harmonia. Its separation from qualification checks prevents a represented certificate or training record from being mistaken for the education activity itself.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Bed & Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) — `f117ccec0bfa4f738e6ddb646b3534a2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md) — `f996ecca728f4942a88b034aca818a99`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinic & Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md) — `3dffe63e03124efab255204ee4e9a5d9`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) — `0c27be11b4a44cb19e992309b8146a17`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.
  Boundary: Includes specimen metadata and specimen movement traceability. Supply/materiel movement belongs to Health Materiel Traceability Administration.
- [[CT2] Service Establishment Support](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md) — `20e67a41e7f44398b0f0cf3d379d8b9c`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Support the information needed to establish a health service in its intended operating context.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-041 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-041`).
- [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md) — `d86105bf9c86445394088f65c895d37f`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] On-Call Management](../../../../../../../continuum/strategy/capability/b16cef8f2bfd4b92948ec75cbcfa7b9a.md) — `b16cef8f2bfd4b92948ec75cbcfa7b9a`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: On-Call Management resolves the active provider for a specialty, service or hospital at the relevant time. It turns changing coverage information into usable routing context so that operational communication can reach the intended capacity. This differs from authoring the roster or deciding who is professionally qualified for the requested activity.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Emergency Department Operations](../../../../../../../continuum/strategy/capability/24a4b5815faa4d369bc756a6e97d3e9e.md) — `24a4b5815faa4d369bc756a6e97d3e9e`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: This capability maintains the operational context of emergency places, waits and length-of-stay milestones. It supports understanding of occupancy and impediments in the department while keeping capacity's emergency-service meaning intact. Its concern differs from Emergency Care's clinical presentation context, even though both can refer to the same patient encounter.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Workforce Capability Monitoring](../../../../../../../continuum/strategy/capability/367b3e5643f44fa785f093db8bb9a6e8.md) — `367b3e5643f44fa785f093db8bb9a6e8`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Monitor supplied information about the clinical capability available from the workforce without acquiring employment or clinical allocation authority.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) — `0511a705d74a4f48a0ce92590ae764fa`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md) — `21399d52b9624ddfbeb786f1d72e40c3`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Mobile Staff Management](../../../../../../../continuum/strategy/capability/79a4a48fddf74c69acf7e16404d06ea5.md) — `79a4a48fddf74c69acf7e16404d06ea5`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Mobile Staff Management integrates with external business mobile-work mechanisms and receives visit progression and safety milestones. Its established dispatch Feature exchanges assignment, dispatch and status information with the relevant worker context. This is an information integration boundary around mobile activity, rather than a platform claim to decide the clinician's work.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Discharge Management](../../../../../../../continuum/strategy/capability/ec5e8d6732764aa489c2dd7630fc8fef.md) — `ec5e8d6732764aa489c2dd7630fc8fef`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Discharge Management coordinates the reported milestones needed around discharge, including medication supply, transport and home-care confirmations. It makes readiness and impediments visible across disciplines and communicates departure information when physical exit occurs. The responsibility preserves a distinction between gathering confirmations and making the clinical decision that discharge is appropriate.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md) — `6041d7c0cc5d4519aaea584bb4e6d365`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Rostering](../../../../../../../continuum/strategy/capability/192ca88a289544dcbf6adaf643f39969.md) — `192ca88a289544dcbf6adaf643f39969`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Rostering provides the Adjacent context of planned workforce availability. It is distinct from consuming roster projections for operational views or resolving who is currently on call. The present architecture retains the context without establishing a Harmonia Feature decomposition for authoring or managing the authoritative roster.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md) — `f697b1ac79b544749315f31819b17969`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Enable responsible clinical participants to identify and use the applicable clinical escalation path.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Work Allocation & Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md) — `152a5252952c405994e9b171234efcb0`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Credential Management](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) — `21eed4fc8c924496b16c6ddaa32f3d01`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-045 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-045`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. Its explicit boundary is: Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/722`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-care-operations-coordination-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7d2f4319495b59248bd13f7b256a1c32"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail
- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 668; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### AX-14 — Semantic Distinctions Are Preserved

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-166"></a>
## P1-166 — [CT1] Independent Assurance Enablement

Canonical identity: `616682b612d244429e656b040e002d56`; [governed source](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Overview**

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Intent**

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Boundary**

Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Independent Service Assurance Enablement",
  "architectural_status": "Proposed",
  "definition": "Specify how governed service behaviour is assured and independently establish what applicable evidence demonstrates.",
  "overview": "The frame separates design and criteria stewardship from the evaluation activity that produces findings. It preserves the established three assurance capabilities and five Functions without turning observation or operational health into an assurance conclusion.",
  "intent": "Establish justified confidence in governed service behaviour without giving the subject control of its own assurance conclusion.",
  "boundary": "Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.",
  "tier": "CT1",
  "category": "business_enabling"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-0a0a84e48f6b5ade89ca66d0c8bfe2b2 | Composition | [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-bdcbe5a38efb51e5bbe177fa18dab4bb | Composition | [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f5611ff476f156649f9362462b646492 | Composition | [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | [[CT2] Independent Assurance Evaluation Enablement](../../../../../../../continuum/strategy/capability/7fa5112efb724b90a17a03c7c19c516a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) — `b81775d89d774cf8ba61731d13c67a54`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.
  Boundary: Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.
  Independent adjudication: P1-322 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-322`).
- [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) — `b1a791458d644d4794895e4fa42ec83c`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.
  Boundary: Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.
  Independent adjudication: P1-306 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-306`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. Its explicit boundary is: Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/725`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-independent-service-assurance-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7045e58c19235175b54f5d054232b883"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail
- [docs/markdown/03-business-architecture/behaviours/health-service-assurance.md](../../../../../../../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md); recorded line 9; Current corpus evidence; does not promote the derived assertion. Source excerpt: The approved human review decisions on 2026-10-08 extend the previous bounded derivation with **[Service Assurance Modeller](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-assurance-modeller)** and **[Service Guardian](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-guardian)** Business Roles and exactly five Business Functions: **Design Service Assurance**, **Manage Assurance Criteria**, **Establish Assurance Context**, **Assess Assurance Evidence** and **Adjudicate Assurance Assessment**. Their capability ownership and Role performance are documented in §3. The assurance Role previously named Guardian is now Service Guardian; legal/personal-welfare Guardian remains unchanged. These naming and Function-decomposition decisions are resolved; further decomposition is not approved.

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-002"></a>
## P1-002 — [CT1] Shared Information & Coordination Enablement

Canonical identity: `0062fcacae1740988b6be2bda2aa1674`; [governed source](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Overview**

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Intent**

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Boundary**

Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Governed Health Information Enablement",
  "architectural_status": "Proposed",
  "definition": "Preserve, find, exchange, qualify and protect health information and the governed definitions needed to interpret it.",
  "overview": "This frame makes a managed record usable over time and across authorised purposes. It separates clinical synthesis, binary-content association, external exchange, semantic definitions, privacy and evidence so that none becomes an undifferentiated information utility.",
  "intent": "Keep managed health information usable and attributable across time, boundaries and authorised purposes.",
  "boundary": "Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.",
  "tier": "CT1",
  "category": "business_enabling"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT1`; parent key: `None`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-0417eabe7e4452cc814cc035b17053e8 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-131413fff1355dd79fb4135c299d4bce | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-2a581d15b2a056758371846e78a97981 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Connectivity Enablement](../../../../../../../continuum/strategy/capability/6a1030483dfc4ae6af6998b587129d1c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-2c38d0dd86b05054971561b058752f9e | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Clinical Knowledge Services](../../../../../../../continuum/strategy/capability/bbd2bd21859f40e39963bdbe7a851686.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-409ee31a8ed0538b8b3a3e155d906813 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Exchange Enablement](../../../../../../../continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-49b2166892d353eeb7d871ca92e01ef4 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-609edc10eab4594a9628f22624cca4c0 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-635a9ec5c8d9566da49c0d0c2f41e49a | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-6acf7bca1c975ff6adcbbb9954f0e9ad | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Design Governance](../../../../../../../continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-70f298f48f3958b892c2f08a504241c2 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-74f96ce74a5a52a4a06d3a7caa4bd85e | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Presentation Services](../../../../../../../continuum/strategy/capability/e9b6ba1917ba44cca7466444d0836c98.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-766c607772eb55fca764b3021e34b431 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-857b5bd449555742914786cbc2ec555d | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Semantic &amp; Reference Knowledge Enablement](../../../../../../../continuum/strategy/capability/92a71292710c41b29775d19b98c197d6.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-92c4838415785f30bb9261555ad963e9 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Clinical Information Stewardship Enablement](../../../../../../../continuum/strategy/capability/3811c758ff2140c39a801bf89f75098a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a75fd5c5ea1c593199b62f4fc1a314fa | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Patient Clinical Record](../../../../../../../continuum/strategy/capability/5478878e0e6e4d34a8d5023ac6ac2d54.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-b02c62ac49de5f74b21ab40f91919704 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c99f0a1cf0a05c1b9ff882c4734b2f69 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Access](../../../../../../../continuum/strategy/capability/cc253aea3c5f489da0502df74dde1669.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-e71182df476b57e2ba04e2c089cf52eb | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Protection &amp; Accountability Enablement](../../../../../../../continuum/strategy/capability/e922cba7cf1d4ebeb9abf6472a3803ce.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-ed2061e01b1658c38afda6501b1ec374 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) — `a790b5f2c5a1497fa6ec09d7824da32e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.
  Boundary: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.
  Independent adjudication: P1-291 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-291`).
- [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md) — `3bb740e6db284112b054905c0c6c80be`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Clinical Knowledge Services](../../../../../../../continuum/strategy/capability/bbd2bd21859f40e39963bdbe7a851686.md) — `bbd2bd21859f40e39963bdbe7a851686`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Administer clinical knowledge requests, distribute governed knowledge artefacts and administer approved reference datasets.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Health Information Exchange Enablement](../../../../../../../continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md) — `a61a15908ed544e6acf3e9cab63a0267`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) — `5bac15316d8d4f4db6e7a253f1acfbe3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-153 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-153`).
- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md) — `e6429225463a44d39f82e490e254e5d4`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Information Design Governance](../../../../../../../continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md) — `f39ed9ebcc724857a4bf7a2b8e045c7a`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md) — `7669036f7e6749219412fc64df5f7064`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Presentation Services](../../../../../../../continuum/strategy/capability/e9b6ba1917ba44cca7466444d0836c98.md) — `e9b6ba1917ba44cca7466444d0836c98`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Presentation Services supplies contextual human interaction through clinical timelines, directory views and operational dashboards. It makes governed information usable by people while consuming the identity, security and clinical context established elsewhere. The responsibility is the interaction and presentation experience rather than a new authority for the information shown.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) — `b81a1ab42af24c2e9896b8f3f78d291a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer information-content maintainer participation, source provenance and governed information products.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-323 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-323`).
- [[CT2] Patient Clinical Record](../../../../../../../continuum/strategy/capability/5478878e0e6e4d34a8d5023ac6ac2d54.md) — `5478878e0e6e4d34a8d5023ac6ac2d54`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Patient Clinical Record provides a governed longitudinal representation across encounters, providers and source systems. Assembly supplies the timeline, active access supports current care context, and durable preservation retains the history of what Harmonia received, knew or managed. These contributions are complementary: a responsive working view must not become the authority for durable history.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.
- [[CT2] Health Information Access](../../../../../../../continuum/strategy/capability/cc253aea3c5f489da0502df74dde1669.md) — `cc253aea3c5f489da0502df74dde1669`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Health Information Access enables authorised discovery and retrieval across clinical information. It combines semantic search and discrete retrieval with access-qualified filtering so that a discoverable item is not automatically disclosable. It consumes established policy and consent determinations rather than making the search interface an alternative security authority.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-207 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-207`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. Its explicit boundary is: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT1 framing. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/723`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-governed-health-information-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-a5127b36f8a25c748c68426668173fcd"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail
- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 668; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### AX-14 — Semantic Distinctions Are Preserved

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-269"></a>
## P1-269 — [CT2] Acute Care

Canonical identity: `9bb988c526404522b83aeed5069e0eb5`; [governed source](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-37fa0df5fb9f5c6e9b7a8b15312b9858 | Association | [Consent Enforcement Service](../../../../../../../continuum/business/business-service/67bf392d2dd6464292619e259cc37f8e.md) | [[CT2] Acute Care](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md) | {"semantic": "Established Business Architecture dependency; source exclusions retained", "currentness": "current"} |
| id-c9586e2870c6516aa39063a4ffc255c6 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Acute Care](../../../../../../../continuum/strategy/capability/9bb988c526404522b83aeed5069e0eb5.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/873`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-acute-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-36aa4edb4b1557c7bb8fde88fa158486"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-306"></a>
## P1-306 — [CT2] Assurance Service Review

Canonical identity: `b1a791458d644d4794895e4fa42ec83c`; [governed source](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

**Overview**

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

**Boundary**

Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-independent-service-assurance-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-6edda368e62358899be44b9e8c29974d | Composition | [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-bdcbe5a38efb51e5bbe177fa18dab4bb | Composition | [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) — `616682b612d244429e656b040e002d56`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.
  Boundary: Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.
  Independent adjudication: P1-166 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-166`).
- [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) — `039343518db542d48cde6afab84a3e1e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.
  Boundary: Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.
  Independent adjudication: P1-006 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-006`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. The source frame [CT1] Independent Assurance Enablement concerns: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Independent Assurance Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/938`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-assurance-service-review`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-8a7d47abbe015a309139a0ee2c1250d4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-322"></a>
## P1-322 — [CT2] Assurance Specification

Canonical identity: `b81775d89d774cf8ba61731d13c67a54`; [governed source](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Overview**

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Intent**

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Boundary**

Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Assurance Specification Enablement",
  "architectural_status": "Proposed",
  "definition": "Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.",
  "overview": "Model how satisfaction will be assured and maintain applicable, temporally qualified criteria. Its constituent abilities distinguish Assurance Design; Assurance Criteria Management.",
  "intent": "Make the intended basis of assurance explicit before evidence is assessed.",
  "boundary": "Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.",
  "tier": "CT2",
  "category": "business_enabling",
  "parent_key": "proposal:bec-independent-service-assurance-enablement"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-independent-service-assurance-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-0a0a84e48f6b5ade89ca66d0c8bfe2b2 | Composition | [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) | [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d9152aa8dd6255c3a1e1d38813811dc0 | Composition | [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | [[CT3] Assurance Criteria Administration](../../../../../../../continuum/strategy/capability/1bfd71f24321428c8871b587d413f47e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f99eedf511cb5ce696630acda2d1f1b3 | Composition | [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | [[CT3] Assurance Design](../../../../../../../continuum/strategy/capability/71c8a612b88f4ecba049e94d506bd0c2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Independent Assurance Enablement](../../../../../../../continuum/strategy/capability/616682b612d244429e656b040e002d56.md) — `616682b612d244429e656b040e002d56`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.
  Boundary: Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.
  Independent adjudication: P1-166 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-166`).
- [[CT3] Assurance Criteria Administration](../../../../../../../continuum/strategy/capability/1bfd71f24321428c8871b587d413f47e.md) — `1bfd71f24321428c8871b587d413f47e`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: The capability to establish and manage reusable, governed and temporally identifiable assurance criteria through which subjects may be evaluated.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT3] Assurance Design](../../../../../../../continuum/strategy/capability/71c8a612b88f4ecba049e94d506bd0c2.md) — `71c8a612b88f4ecba049e94d506bd0c2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: The capability to determine and define how satisfaction of a governed requirement, constraint or expected behaviour will be assured, including its assurance disposition, applicable criteria, required evidence and evaluation expectations.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Model how satisfaction will be assured and maintain applicable, temporally qualified criteria. The source frame [CT1] Independent Assurance Enablement concerns: Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Independent Assurance Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/766`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-assurance-specification-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-e18e1674a30c5c97a40ef75faea25663"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md](../../../../../../../../docs/markdown/03-business-architecture/metamodel/business-architecture-metamodel.md); recorded line 49; Current corpus evidence; does not promote the derived assertion. Source excerpt: Harmonia SHALL use the fixed three-tier Capability decomposition:
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-255"></a>
## P1-255 — [CT2] Community Care

Canonical identity: `942aa78cad004ef99de6b3aae8ddd014`; [governed source](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-c32d3d347681509e94a346636bcf85f4 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Community Care](../../../../../../../continuum/strategy/capability/942aa78cad004ef99de6b3aae8ddd014.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/879`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-community-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-a12191bde58c547b954e263c5e49ba3e"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-045"></a>
## P1-045 — [CT2] Credential Management

Canonical identity: `21eed4fc8c924496b16c6ddaa32f3d01`; [governed source](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

**Overview**

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-care-operations-coordination-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-7c988086a7ab55ba88ecd598cc989479 | Composition | [[CT2] Credential Management](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) | [[CT3] Clinical Service Credential Definition Management](../../../../../../../continuum/strategy/capability/2e5c97f51e7e48d1b865ac416dd8099d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-f5f81df65e5d539ca9b42b93e894fa35 | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Credential Management](../../../../../../../continuum/strategy/capability/21eed4fc8c924496b16c6ddaa32f3d01.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) — `b13ee2c5a4ef4fd6be8a44795696dfc2`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.
  Boundary: Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.
  Independent adjudication: P1-304 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-304`).
- [[CT3] Clinical Service Credential Definition Management](../../../../../../../continuum/strategy/capability/2e5c97f51e7e48d1b865ac416dd8099d.md) — `2e5c97f51e7e48d1b865ac416dd8099d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-073 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-073`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. The source frame [CT1] Health Service Operations Enablement concerns: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Service Operations Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/897`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-credential-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-ecc5bfe7e27b54dfb59023a05879f1b0"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-324"></a>
## P1-324 — [CT2] Diagnostic Services

Canonical identity: `b825ebab42f444d8b738b4c1f489f785`; [governed source](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-445690e23e845885ae1e0e89cc4a3fcc | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Diagnostic Services](../../../../../../../continuum/strategy/capability/b825ebab42f444d8b738b4c1f489f785.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/876`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-diagnostic-services`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-52c91baf111d50f7bd4b24fcc80ab1be"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-295"></a>
## P1-295 — [CT2] Emergency Care

Canonical identity: `aa088f0647ad4b78966fe28fa80af674`; [governed source](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-b79c26e8964658f083ee2f5a9de41c56 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Emergency Care](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-de70be974c975e3e9a1eae0a4882a156 | Association | [Person Identifier Correlation](../../../../../../../continuum/business/business-service/71220901361e42f686faf7d852f57e45.md) | [[CT2] Emergency Care](../../../../../../../continuum/strategy/capability/aa088f0647ad4b78966fe28fa80af674.md) | {"semantic": "Established Business Architecture dependency; source exclusions retained", "currentness": "current"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/874`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-emergency-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0bdbebb9b36f5b2b8d5816dbc27aeeb3"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-207"></a>
## P1-207 — [CT2] Health Information Communication

Canonical identity: `7925605bf60f4b288e826020409a09d9`; [governed source](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

**Overview**

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-governed-health-information-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-1d6da7e72fed5635a6829ac7ecf17067 | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Notification Services](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-33bb89d1ee40549da4e5a01d464b917f | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Health Library Access](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-646c7d1460a75e23aae69f7da219969e | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Task Communication Services](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a017b9c0e673560ab71059a2f016d512 | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Self-Service](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-ed2061e01b1658c38afda6501b1ec374 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Shared Information & Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) — `0062fcacae1740988b6be2bda2aa1674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.
  Boundary: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.
  Independent adjudication: P1-002 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-002`).
- [[CT3] Patient Notification Services](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md) — `9d172cd511b742499c4e7c23f6630a35`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed patient notifications concerning health information and applicable care or operational events.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-271 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-271`).
- [[CT3] Patient Health Library Access](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md) — `808e5a3c3f6b496fa329962088f0bee0`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable authorised patient access to appropriately governed health knowledge and library information.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-220 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-220`).
- [[CT3] Patient Task Communication Services](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md) — `ba8ce1d54ace4bc9aa3521dd954f36cb`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-329 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-329`).
- [[CT3] Patient Self-Service](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md) — `d4956cab8b2b46c396899949c9486a1c`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patients to undertake permitted information-access and administrative self-service interactions.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-374 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-374`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. The source frame [CT1] Shared Information & Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information & Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/909`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-health-information-communication`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-5004f4019d1059108356b6ee7cfcaa8e"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-153"></a>
## P1-153 — [CT2] Information Asset Stewardship Management

Canonical identity: `5bac15316d8d4f4db6e7a253f1acfbe3`; [governed source](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

**Overview**

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-governed-health-information-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-31344016c6e8524db8d0adab97c50055 | Composition | [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) | [[CT3] Information Asset Definition](../../../../../../../continuum/strategy/capability/05c58b2ccfcc45e9a6c70d9a569c4f61.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-49b2166892d353eeb7d871ca92e01ef4 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d4dc14a69f825107ab15329dac917bf1 | Composition | [[CT2] Information Asset Stewardship Management](../../../../../../../continuum/strategy/capability/5bac15316d8d4f4db6e7a253f1acfbe3.md) | [[CT3] Information Asset Tag Management](../../../../../../../continuum/strategy/capability/df27c3ba6e164e56af7bff4e1eb1954a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Shared Information & Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) — `0062fcacae1740988b6be2bda2aa1674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.
  Boundary: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.
  Independent adjudication: P1-002 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-002`).
- [[CT3] Information Asset Definition](../../../../../../../continuum/strategy/capability/05c58b2ccfcc45e9a6c70d9a569c4f61.md) — `05c58b2ccfcc45e9a6c70d9a569c4f61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Define the identifying, semantic and governed-use characteristics of an information asset.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-008 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-008`).
- [[CT3] Information Asset Tag Management](../../../../../../../continuum/strategy/capability/df27c3ba6e164e56af7bff4e1eb1954a.md) — `df27c3ba6e164e56af7bff4e1eb1954a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage attributable tags describing information assets without treating caller-controlled tags as security authority.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-394 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-394`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. The source frame [CT1] Shared Information & Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information & Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/917`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-information-asset-stewardship-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0e93a43b398a503d804b87204f939a09"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-323"></a>
## P1-323 — [CT2] Information Content Maintainer Management

Canonical identity: `b81a1ab42af24c2e9896b8f3f78d291a`; [governed source](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer information-content maintainer participation, source provenance and governed information products.

**Overview**

Administer information-content maintainer participation, source provenance and governed information products. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer information-content maintainer participation, source provenance and governed information products.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-governed-health-information-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-766c607772eb55fca764b3021e34b431 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-8987c7c0846d53259f9b97e50861d77a | Composition | [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-8fd34b94672f5541b4066e0e363c5f31 | Composition | [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | [[CT3] Data Scientist Administration](../../../../../../../continuum/strategy/capability/3bb1a11fba234717bf40c9e6452f401a.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-ee35b93d41045db48e4657e0368814d9 | Composition | [[CT2] Information Content Maintainer Management](../../../../../../../continuum/strategy/capability/b81a1ab42af24c2e9896b8f3f78d291a.md) | [[CT3] Information Product Administration](../../../../../../../continuum/strategy/capability/b5a6a31855834ea58870ebb6c51ed4b0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Shared Information & Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) — `0062fcacae1740988b6be2bda2aa1674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.
  Boundary: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.
  Independent adjudication: P1-002 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-002`).
- [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) — `44a7937520a54945b06b5fd4a2bbd870`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.
  Boundary: Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.
  Independent adjudication: P1-114 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-114`).
- [[CT3] Data Scientist Administration](../../../../../../../continuum/strategy/capability/3bb1a11fba234717bf40c9e6452f401a.md) — `3bb1a11fba234717bf40c9e6452f401a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer profiles and qualified participation information for data scientists maintaining governed information content.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-104 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-104`).
- [[CT3] Information Product Administration](../../../../../../../continuum/strategy/capability/b5a6a31855834ea58870ebb6c51ed4b0.md) — `b5a6a31855834ea58870ebb6c51ed4b0`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer identifiable governed information products, their purpose, content standing and qualified release/use context.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-314 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-314`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer information-content maintainer participation, source provenance and governed information products. The source frame [CT1] Shared Information & Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information & Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/920`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-information-content-maintainer-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-f4e98809bf18585aac90aa1597b89bb0"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-310"></a>
## P1-310 — [CT2] Inpatient Care

Canonical identity: `b39fb49c01b545a5b51d3313531efb1b`; [governed source](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-8f72ac58af7955bfac6f0acf831bfd4b | Association | [Discharge Readiness Telemetry](../../../../../../../continuum/business/business-service/03d01a9b024c4497badf3fe011a8dd20.md) | [[CT2] Inpatient Care](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md) | {"semantic": "Established Business Architecture dependency; source exclusions retained", "currentness": "current"} |
| id-d5cfc47123f959c1a477f229106a3ceb | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Inpatient Care](../../../../../../../continuum/strategy/capability/b39fb49c01b545a5b51d3313531efb1b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/875`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-inpatient-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-052abff4e30c5d5eaa4071331c4af500"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-331"></a>
## P1-331 — [CT2] Medication Therapy

Canonical identity: `bb04996164c94e0c827a020432f4e498`; [governed source](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-a72519a45a0750da967ba829c7f8d2db | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Medication Therapy](../../../../../../../continuum/strategy/capability/bb04996164c94e0c827a020432f4e498.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/877`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-medication-therapy`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-6c80b51346315f2fa3d9111d871deba1"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-328"></a>
## P1-328 — [CT2] Outreach Care

Canonical identity: `b9ddbe2dd6a7485ba9cd3482215ea09e`; [governed source](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-7b21a12993ee5494ae0c4ab99817413b | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Outreach Care](../../../../../../../continuum/strategy/capability/b9ddbe2dd6a7485ba9cd3482215ea09e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/880`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-outreach-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-00c20496abf655fa974ae887a695102a"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-043"></a>
## P1-043 — [CT2] Population Health Information Reporting

Canonical identity: `2135b45eab034370b0063b20e821985d`; [governed source](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Overview**

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Intent**

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Boundary**

Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Population Health Notification Enablement",
  "architectural_status": "Proposed",
  "definition": "Prepare, convey and track required population-health notifications from applicable governed source information.",
  "overview": "A reportable event and the obligation to notify are different from sending a message. This capability preserves the established obligation and observes the notification outcome under its contract without deciding public-health response.",
  "intent": "Keep the applicable notification duty and the observed communication disposition traceable to each other.",
  "boundary": "Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "proposal:bec-secondary-health-information-use-enablement"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-secondary-health-information-use-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-028a6c6895375c1ab108237b62dc7328 | Association | [[Feature] Population Notification Obligation Tracking](../../../../../../../continuum/strategy/capability/feature/62f6a91a10684876b7d08b536700f12f.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-17605478605e545fa36ecf518fb9f5a0 | Serving | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | [[CT2] Population Health Surveillance](../../../../../../../continuum/strategy/capability/83e568fa8af14d7791a7253765ea390f.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-3d50721b497a553dafcf838da779ad20 | Serving | [[CT2] Operational Assurance](../../../../../../../continuum/strategy/capability/938cd44380ab431fa8dae13c702d83c1.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-506d798d257150e0aaa4f1adb622ca53 | Serving | [[CT2] Event &amp; Subscription](../../../../../../../continuum/strategy/capability/b768b129971e4bf5bccc03d33d2fcb4e.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-9260966dba105df986885f9c01c9e2e9 | Serving | [[CT2] Interoperability &amp; Exchange](../../../../../../../continuum/strategy/capability/0f2e5222874949d8b0cce234941ecb20.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-df70babccbeb5977897dd2421f46d135 | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Population Health Information Reporting](../../../../../../../continuum/strategy/capability/2135b45eab034370b0063b20e821985d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

Original qualified predicates, exact targets, native mappings and their qualifications are retained in [reconciliation.json](../reconciliation.json) for this row; mapped relationships above retain their actual standing. Unestablished predicate existence is not promoted.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) — `a790b5f2c5a1497fa6ec09d7824da32e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.
  Boundary: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.
  Independent adjudication: P1-291 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-291`).
- [[Feature] Population Notification Obligation Tracking](../../../../../../../continuum/strategy/capability/feature/62f6a91a10684876b7d08b536700f12f.md) — `62f6a91a10684876b7d08b536700f12f`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Track the relation between an applicable notification obligation, the supplied source evidence and observed notification disposition.
  Boundary: Does not define notifiability, author statutory rules or own public-health response.
  Independent adjudication: P1-169 (preserved source locator `05-current-unresolved-features.md#p1-169`).
- [[CT2] Population Health Surveillance](../../../../../../../continuum/strategy/capability/83e568fa8af14d7791a7253765ea390f.md) — `83e568fa8af14d7791a7253765ea390f`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Understand population-health state and manage organised surveillance, significant events and applicable notification.
  Boundary: Notification management does not define the obligation or own the operational response. Surveillance evidence can inform other abilities without establishing their findings.
- [[CT2] Operational Assurance](../../../../../../../continuum/strategy/capability/938cd44380ab431fa8dae13c702d83c1.md) — `938cd44380ab431fa8dae13c702d83c1`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Ensure concurrency integrity, system resilience, duplicate detection, exception handling, automated recovery, and operational telemetry across platform operations.
- **Functional Responsibilities**:
  - Enforcing sliding-window duplicate message detection and idempotent suppression.
  - Guaranteeing ingress dual-write safety (durable acceptance before emitting positive application ACK).
  - Managing structured failure recovery, exception escalation, and dead-letter isolation.
  - Emitting platform telemetry, operational health metrics, and queue processing metrics.
- **Collaborative Nature**: Realized collaboratively across boundary gateways, messaging infrastructure, execution workers, and storage layers.
  Boundary: Operational health is distinct from independent assessment of a governed subject. Assurance-specific criteria applicability, adjudication and conclusions belong to the established EC-14 contribution.
- [[CT2] Event & Subscription](../../../../../../../continuum/strategy/capability/b768b129971e4bf5bccc03d33d2fcb4e.md) — `b768b129971e4bf5bccc03d33d2fcb4e`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Publish, ingest, correlate, filter, and syndicate events and subscription-driven changes across the enterprise landscape.
- **Functional Responsibilities**:
  - Ingesting operational and clinical event triggers from external systems and internal state changes.
  - Managing topic- and criteria-based event subscriptions for internal and external consumers.
  - Evaluating dynamic subscription filters to match event payloads with interested subscriber criteria.
  - Delivering asynchronous event notifications reliably to registered subscribers.
- **Decoupling Note**: Defined strictly in terms of event publishing, criteria matching, and notification delivery; entirely independent of broker topics, partitions, or queues.
  Boundary: Receiving a notification does not determine the consumer's business response; topics and queues do not define the capability.
- [[CT2] Interoperability & Exchange](../../../../../../../continuum/strategy/capability/0f2e5222874949d8b0cce234941ecb20.md) — `0f2e5222874949d8b0cce234941ecb20`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Resolve destinations, transform schemas, route, transport, deliver, acknowledge, and coordinate standards-based information exchange across enterprise boundaries.
- **Functional Responsibilities**:
  - Providing multi-protocol boundary adaptation for healthcare standards (HL7 v2, FHIR REST, DICOM, secure messaging).
  - Executing bidirectional structural and semantic transformations between legacy formats and canonical representations.
  - Ensuring reliable addressed delivery and fan-out distribution to external clinical endpoints.
  - Processing and distinguishing technical delivery acknowledgements from business acknowledgements.
- **Boundary Rule**: Egress terminates Harmonia management of the emitted representation (Axiom AX-13).
  Boundary: Egress ends Harmonia management of the emitted representation. Retained delivery evidence does not extend control over an external recipient or transfer payload authority.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/774`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-population-health-notification-enablement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-1ba1dd139e615a7da6f08eef1caa56be"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `source-qualified relationship assertions whose from-key binds to canonicalId 2135b45eab034370b0063b20e821985d`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: Preserved non-native predicates, not new graph relationships.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-capabilities.md); recorded line 611; Current corpus evidence; does not promote the derived assertion. Source excerpt: | Notifiable Health Event Management | Manage formal notification applicability, required information, recipient, status, submission, correction/supplementation and applicable follow-up. |
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-397"></a>
## P1-397 — [CT2] Preventive Care

Canonical identity: `dfdc4f0273ba4910848b3b65c9ef6ab2`; [governed source](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-43cce9d2c8b4518db3b2473ee119bbb7 | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Preventive Care](../../../../../../../continuum/strategy/capability/dfdc4f0273ba4910848b3b65c9ef6ab2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/878`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-preventive-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-2bc56e5d43a356dda381e7a34377af89"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-369"></a>
## P1-369 — [CT2] Primary Care

Canonical identity: `d118673d77d74c8cb8cec24f03adddd8`; [governed source](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-566b50bd797a52298d385f41a5d34536 | Association | [Longitudinal Clinical Record Query](../../../../../../../continuum/business/business-service/df10431b93bf47dfae2b8ec7b3e6552b.md) | [[CT2] Primary Care](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) | {"semantic": "Established Business Architecture dependency; source exclusions retained", "currentness": "current"} |
| id-f888507b858e5f41908fc81ae950747d | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Primary Care](../../../../../../../continuum/strategy/capability/d118673d77d74c8cb8cec24f03adddd8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/872`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-primary-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-351cfc95a8c85684a3ff908f6df282eb"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-077"></a>
## P1-077 — [CT2] Remote & Virtual Care

Canonical identity: `30dff22d40b64da29ede7c41673db085`; [governed source](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

**Overview**

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `reviewed:bec-clinical-service-dependency-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-3551f41796b657b2ab75877ded20695f | Composition | [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) | [[CT2] Remote &amp; Virtual Care](../../../../../../../continuum/strategy/capability/30dff22d40b64da29ede7c41673db085.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility. The source frame [CT1] Clinical Service Dependency Management concerns: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Clinical Service Dependency Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/881`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-remote-virtual-care`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-3bc2433424425db093822bd2db4dd8c7"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-126"></a>
## P1-126 — [CT2] Research Information Curation

Canonical identity: `4f5b8cec90324c2989cdc30e80bc8cb9`; [governed source](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Overview**

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Intent**

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Boundary**

Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Authorised Research Information Provision",
  "architectural_status": "Proposed",
  "definition": "Assemble and release existing health information for an explicitly authorised research purpose under applicable use constraints.",
  "overview": "Research support must retain the purpose, applicable approvals and release qualification of supplied information. It reuses governed retrieval and privacy controls rather than claiming a clinical-trial or research-governance platform.",
  "intent": "Supply the permitted existing information for a particular authorised research use.",
  "boundary": "Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "proposal:bec-secondary-health-information-use-enablement"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-secondary-health-information-use-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-755ba67ebed151468513b19627c351b0 | Association | [[Feature] Authorised Research Release Assembly](../../../../../../../continuum/strategy/capability/feature/dcebc2f2b5b7494a8e80a02402664078.md) | [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-a1dc68d6cb2651ba875a1917e6cbe43d | Serving | [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | [[CT2] Research Information Support](../../../../../../../continuum/strategy/capability/ef18f999eef3488ba584d263a551732b.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-fc7b33bb093f56d491bf7174adc9e842 | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Research Information Curation](../../../../../../../continuum/strategy/capability/4f5b8cec90324c2989cdc30e80bc8cb9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) — `a790b5f2c5a1497fa6ec09d7824da32e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.
  Boundary: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.
  Independent adjudication: P1-291 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-291`).
- [[Feature] Authorised Research Release Assembly](../../../../../../../continuum/strategy/capability/feature/dcebc2f2b5b7494a8e80a02402664078.md) — `dcebc2f2b5b7494a8e80a02402664078`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Assemble and qualify the existing information allowed by a particular authorised research purpose.
  Boundary: No ethics approval, de-identification guarantee or clinical-trial management is inferred.
  Independent adjudication: P1-390 (preserved source locator `05-current-unresolved-features.md#p1-390`).
- [[CT2] Research Information Support](../../../../../../../continuum/strategy/capability/ef18f999eef3488ba584d263a551732b.md) — `ef18f999eef3488ba584d263a551732b`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Provide the existing health information needed for authorised research within the deliberately bounded R1/R2 responsibility.
  Boundary: Research governance, trial oversight and specialist analysis remain unmodelled and are not absorbed into provision.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/773`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-authorised-research-information-provision`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-2bb6a6fd2f5f55db940b8d0793706993"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-capabilities.md); recorded line 842; Current corpus evidence; does not promote the derived assertion. Source excerpt: | Governed Research Information Provision | Provide existing health information for an authorised research purpose under applicable ethics, governance, privacy, consent, security and information-use constraints. |
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-041"></a>
## P1-041 — [CT2] Service Establishment Support

Canonical identity: `20e67a41e7f44398b0f0cf3d379d8b9c`; [governed source](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Support the information needed to establish a health service in its intended operating context.

**Overview**

Support the information needed to establish a health service in its intended operating context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Support the information needed to establish a health service in its intended operating context.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-care-operations-coordination-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-489049abd29459d59385476753d9fecf | Composition | [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) | [[CT2] Service Establishment Support](../../../../../../../continuum/strategy/capability/20e67a41e7f44398b0f0cf3d379d8b9c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Service Operations Enablement](../../../../../../../continuum/strategy/capability/b13ee2c5a4ef4fd6be8a44795696dfc2.md) — `b13ee2c5a4ef4fd6be8a44795696dfc2`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.
  Boundary: Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.
  Independent adjudication: P1-304 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-304`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Support the information needed to establish a health service in its intended operating context. The source frame [CT1] Health Service Operations Enablement concerns: Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Service Operations Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/890`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-service-establishment-support`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-1ae0ff5d6b3b54deb3f9a0832045bcf5"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-116"></a>
## P1-116 — [CT2] Service Planning Data Management

Canonical identity: `4873cd4b960d45d8b93560d5c8b98500`; [governed source](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Overview**

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Intent**

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Boundary**

Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Service Planning Information Provision",
  "architectural_status": "Proposed",
  "definition": "Supply qualified demand, utilisation and capacity evidence to responsible health-service planning activities.",
  "overview": "Planners need comparable time- and service-qualified observations from multiple operating settings. This ability prepares those projections while leaving the interpretation of required future capacity and distribution with the planning authority.",
  "intent": "Give planners qualified observed evidence without predetermining the planning judgement.",
  "boundary": "Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "proposal:bec-secondary-health-information-use-enablement"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-secondary-health-information-use-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-03e6fb9ad87656a48930d9bf7377daa8 | Serving | [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) | [[CT2] Health-System Planning](../../../../../../../continuum/strategy/capability/9ca4df07eceb4150a0190cc488270ce2.md) | {"semantic": "Proposed", "currentness": "current"} |
| id-2ff13a06ac0555eaa8e520306db3dc7a | Composition | [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) | [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-6bc506cfd4f35efe9fa8e069b2775d17 | Association | [[Feature] Demand &amp; Capacity Planning Projection](../../../../../../../continuum/strategy/capability/feature/4f97baa3aed24b8385bf3ecda4eab622.md) | [[CT2] Service Planning Data Management](../../../../../../../continuum/strategy/capability/4873cd4b960d45d8b93560d5c8b98500.md) | {"semantic": "Proposed", "currentness": "current"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Health Informatics Management](../../../../../../../continuum/strategy/capability/a790b5f2c5a1497fa6ec09d7824da32e.md) — `a790b5f2c5a1497fa6ec09d7824da32e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.
  Boundary: Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.
  Independent adjudication: P1-291 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-291`).
- [[CT2] Health-System Planning](../../../../../../../continuum/strategy/capability/9ca4df07eceb4150a0190cc488270ce2.md) — `9ca4df07eceb4150a0190cc488270ce2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Assess health-service demand and plan the capacity and footprint required to meet it.
  Boundary: Plans do not establish available resources or operational allocations. Detailed commissioning, capital, funding and workforce planning remain deliberately unmodelled.
- [[Feature] Demand & Capacity Planning Projection](../../../../../../../continuum/strategy/capability/feature/4f97baa3aed24b8385bf3ecda4eab622.md) — `4f97baa3aed24b8385bf3ecda4eab622`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Project qualified observed demand, capacity and utilisation for authorised planning consumers.
  Boundary: Projection does not establish a planning conclusion or allocate services and staff.
  Independent adjudication: P1-127 (preserved source locator `05-current-unresolved-features.md#p1-127`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Establish and maintain governed information assets suitable for health-service planning, modelling and analysis. The source frame [CT1] Health Informatics Management concerns: Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Health Informatics Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/775`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-service-planning-information-provision`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-95802b6c6b505351b4d3d57f8aed2753"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-capabilities.md); recorded line 626; Current corpus evidence; does not promote the derived assertion. Source excerpt: | Health Service Demand Assessment | Assess current/expected health-service demand across a population, geography, service domain or health-system context. |
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 34; Current corpus evidence; does not promote the derived assertion. Source excerpt: [Context-specific Capacity Management](../../../../../../../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md#capacity-management-responsibility-boundary) establishes/manages capacity meaning for Clinic & Practice Operations, Ward Operations, Theatre Operations, Emergency Department Operations, Outpatient Operations and Mobile Staff Management. Their local information responsibilities express available, committed and utilised capacity without prescribing a shared formal Information Object or identical model. Bed/care-place and equipment state contribute; they do not alone establish service capacity. Service Capacity Management consumes/coordinates those capacity states; Work Allocation & Dispatch and Discharge Management may consume them without acquiring their ownership. Exact provider contracts remain unestablished.

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-348"></a>
## P1-348 — [CT2] Workflow & Activity Coordination

Canonical identity: `c724f3d38bfa4567983a11ad22095d61`; [governed source](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

**Overview**

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

**Boundary**

External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT2`; parent key: `proposal:bec-governed-health-information-enablement`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-2751e5d9f9fe54e899cf9acf1d14fe77 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] Activity Routing Management](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-609edc10eab4594a9628f22624cca4c0 | Composition | [[CT1] Shared Information &amp; Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-77fb28cc16985dcfa83038441c331ed1 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] External Task Status &amp; Traceability Management](../../../../../../../continuum/strategy/capability/b25bb04a8aa94dcfac105a646878ccfc.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-a3b5fe2bd6c856fc8a8923e7bd309bc7 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-d5028176c1285bd199cbbf4f17d8707e | Association | [Policy Evaluation &amp; Authorisation Service](../../../../../../../continuum/business/business-service/a025ac002b6a461abaac07fcb87f411f.md) | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | {"semantic": "Established Business Architecture dependency; source exclusions retained", "currentness": "current"} |
| id-e2434718dd765bb7a5070c30fe9d4f33 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] External Task Coordination](../../../../../../../continuum/strategy/capability/ff3447c929724245a13ee864874b5561.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT1] Shared Information & Coordination Enablement](../../../../../../../continuum/strategy/capability/0062fcacae1740988b6be2bda2aa1674.md) — `0062fcacae1740988b6be2bda2aa1674`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.
  Boundary: Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.
  Independent adjudication: P1-002 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-002`).
- [[CT3] Activity Routing Management](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md) — `27411c143e6e41ffba1af139fbbcd7e2`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-059 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-059`).
- [[CT3] External Task Status & Traceability Management](../../../../../../../continuum/strategy/capability/b25bb04a8aa94dcfac105a646878ccfc.md) — `b25bb04a8aa94dcfac105a646878ccfc`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-307 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-307`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).
- [[CT3] External Task Coordination](../../../../../../../continuum/strategy/capability/ff3447c929724245a13ee864874b5561.md) — `ff3447c929724245a13ee864874b5561`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-436 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-436`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. The source frame [CT1] Shared Information & Coordination Enablement concerns: Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT2 framing under [CT1] Shared Information & Coordination Enablement. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/927`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-workflow-activity-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-36d21ea7e98b5e1bbbddad25ba53cd4e"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md) — selector `information responsibility row 34`; revision `798344f80eb7fd3998e17f4f040cf7dabe260388`; role: existing capability responsibility contribution; no new asset identity.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-059"></a>
## P1-059 — [CT3] Activity Routing Management

Canonical identity: `27411c143e6e41ffba1af139fbbcd7e2`; [governed source](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

**Overview**

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-workflow-activity-coordination`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-2751e5d9f9fe54e899cf9acf1d14fe77 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] Activity Routing Management](../../../../../../../continuum/strategy/capability/27411c143e6e41ffba1af139fbbcd7e2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. The source frame [CT2] Workflow & Activity Coordination concerns: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Workflow & Activity Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/930`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-activity-routing-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-96e9160ac5bc532faed556f17a714f99"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-211"></a>
## P1-211 — [CT3] Appointment & Booking Administration

Canonical identity: `7b3ba9bcac564dd4a7a08631581db2ca`; [governed source](../../../../../../../continuum/strategy/capability/7b3ba9bcac564dd4a7a08631581db2ca.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

**Overview**

Administer externally supplied appointment and booking information and communicate changes in their scheduling status. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:scheduling-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-a8acfa5129a35caf950d219d92dc1e25 | Composition | [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md) | [[CT3] Appointment &amp; Booking Administration](../../../../../../../continuum/strategy/capability/7b3ba9bcac564dd4a7a08631581db2ca.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Scheduling Administration](../../../../../../../continuum/strategy/capability/35fb3744c900470f9895056246a13fb1.md) — `35fb3744c900470f9895056246a13fb1`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer externally supplied appointment and booking information and communicate changes in their scheduling status. The source frame [CT2] Scheduling Administration concerns: Scheduling Administration integrates appointment information established by departmental scheduling systems. It receives scheduled events and propagates cancellation, rescheduling and attendance changes so that care-coordination contexts do not diverge. Its concern is the coherence of scheduling information across participants rather than originating every booking decision. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Scheduling Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/867`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-appointment-booking-administration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-75679799ab975e8088f69be6f0f414f0"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-006"></a>
## P1-006 — [CT3] Assurance Service Evaluation

Canonical identity: `039343518db542d48cde6afab84a3e1e`; [governed source](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

**Overview**

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

**Boundary**

Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-assurance-service-review`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-6edda368e62358899be44b9e8c29974d | Composition | [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) — `b1a791458d644d4794895e4fa42ec83c`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.
  Boundary: Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.
  Independent adjudication: P1-306 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-306`).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. The source frame [CT2] Assurance Service Review concerns: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Assurance Service Review. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/939`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-assurance-service-evaluation`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0bcc748a066653819c90f63952059b9a"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-057"></a>
## P1-057 — [CT3] Bed & Care-Place State Administration

Canonical identity: `26a9383d7bb146a19141558d232843de`; [governed source](../../../../../../../continuum/strategy/capability/26a9383d7bb146a19141558d232843de.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

**Overview**

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:bed--care-place-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-a86ab90caf975b33845c936c073f05a8 | Composition | [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) | [[CT3] Bed &amp; Care-Place State Administration](../../../../../../../continuum/strategy/capability/26a9383d7bb146a19141558d232843de.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Bed & Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) — `f117ccec0bfa4f738e6ddb646b3534a2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. The source frame [CT2] Bed & Care-Place Management concerns: Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Bed & Care-Place Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/887`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-bed-care-place-state-administration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-315b46c620d151868c9f84a0ed45ccd2"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-108"></a>
## P1-108 — [CT3] Bed & Care-Place Turnover Coordination

Canonical identity: `3c9464f9eae1415d8fa041bd1f3da4f2`; [governed source](../../../../../../../continuum/strategy/capability/3c9464f9eae1415d8fa041bd1f3da4f2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

**Overview**

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:bed--care-place-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-41979b1db3e753679aadf89db7d98521 | Composition | [[CT2] Bed &amp; Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) | [[CT3] Bed &amp; Care-Place Turnover Coordination](../../../../../../../continuum/strategy/capability/3c9464f9eae1415d8fa041bd1f3da4f2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Bed & Care-Place Management](../../../../../../../continuum/strategy/capability/f117ccec0bfa4f738e6ddb646b3534a2.md) — `f117ccec0bfa4f738e6ddb646b3534a2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. The source frame [CT2] Bed & Care-Place Management concerns: Bed & Care-Place Management maintains operational place state and the turnover progression that makes a place usable again. Vacancy, reservation, cleaning and readiness must remain distinguishable so that an empty bed is not automatically treated as available for allocation. Notifications expose meaningful readiness changes to the responsible operational participants. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Bed & Care-Place Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/888`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-bed-care-place-turnover-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-f498770b9a45551990afc0c14336212f"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-122"></a>
## P1-122 — [CT3] Care Plan Lifecycle & Coordination

Canonical identity: `4cb7db65392f4fab801d7794af4d4b29`; [governed source](../../../../../../../continuum/strategy/capability/4cb7db65392f4fab801d7794af4d4b29.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

**Overview**

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:care-plan-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-6e20779c45ea544a98ae0150a8eba6b1 | Composition | [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md) | [[CT3] Care Plan Lifecycle &amp; Coordination](../../../../../../../continuum/strategy/capability/4cb7db65392f4fab801d7794af4d4b29.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Care Plan Administration](../../../../../../../continuum/strategy/capability/cf06eec5699f406fa30a00d2f76e3098.md) — `cf06eec5699f406fa30a00d2f76e3098`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. The source frame [CT2] Care Plan Administration concerns: Care Plan Administration receives care plans and coordinates the distribution of updates to authorised team members. It supports shared understanding of established clinical intent across participating organisations. The responsibility concerns the plan as managed information, distinct from deciding the intended course of care or performing the actions expressed by it. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Care Plan Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/870`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-care-plan-lifecycle-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-ab0baa5289245368afb3d9943f0dee5c"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-167"></a>
## P1-167 — [CT3] Care-Place Definition Administration

Canonical identity: `629b493c7d9844baa25c029a87ed04f6`; [governed source](../../../../../../../continuum/strategy/capability/629b493c7d9844baa25c029a87ed04f6.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Overview**

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Intent**

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Boundary**

Describes a place and its capabilities; operational bed state and readiness remain separately managed.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Care-Place Definition",
  "architectural_status": "Proposed",
  "definition": "Modeling the atomic care delivery places (beds, bays, treatment chairs, surgical tables).",
  "overview": "A particular bed, bay, treatment chair or procedural place has stable physical characteristics relevant to care. Those descriptive capabilities remain separate from occupancy, cleaning state and suitability for a current patient.",
  "intent": "Describe the stable characteristics that distinguish one care place from another.",
  "boundary": "Describes a place and its capabilities; operational bed state and readiness remain separately managed.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "capability:location-administration"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:location-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-f7ef6d56b05a5e2cb1c4becd4e756ee7 | Composition | [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md) | [[CT3] Care-Place Definition Administration](../../../../../../../continuum/strategy/capability/629b493c7d9844baa25c029a87ed04f6.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Location Administration](../../../../../../../continuum/strategy/capability/c410005667444f99a221483bf577fad2.md) — `c410005667444f99a221483bf577fad2`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place. The source frame [CT2] Location Administration concerns: Location Administration represents the places and physical relationships relevant to healthcare activity, from facilities to particular care places. Address and geospatial context answer where a place is; hierarchy explains its surroundings; care-place definition records the local characteristics relevant to care. These are stable descriptive responsibilities, distinct from the changing operational state of a bed or ward. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Describes a place and its capabilities; operational bed state and readiness remain separately managed. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Location Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/735`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-care-place-definition`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-6406fe2dfdf65a79980a8fbd6881f251"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); recorded line 185; Current corpus evidence; does not promote the derived assertion. Source excerpt: - **4.3 Care-Place Definition** (`Harmonia-Core`)
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-429"></a>
## P1-429 — [CT3] Client Intake & Demographic Update

Canonical identity: `f51f8d6b7556424987c76d9a7465c67c`; [governed source](../../../../../../../continuum/strategy/capability/f51f8d6b7556424987c76d9a7465c67c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:client-centred-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-5f43371dad72578c9ae5eb122081f050 | Composition | [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md) | [[CT3] Client Intake &amp; Demographic Update](../../../../../../../continuum/strategy/capability/f51f8d6b7556424987c76d9a7465c67c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Client-Centred Administration](../../../../../../../continuum/strategy/capability/8752b7a937754b2faa296300c6a48911.md) — `8752b7a937754b2faa296300c6a48911`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority. The source frame [CT2] Client-Centred Administration concerns: Client-Centred Administration receives demographic and registration changes and makes verified updates available to participating clinical systems. It connects administrative events from external PAS environments with the client context used across care. The enabling responsibility concerns integration of changes, rather than independently deciding the authoritative demographic truth whenever a source sends an update. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Client-Centred Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/940`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-client-intake-demographic-update`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-714b37e59d095316bb94389ebbc14e33"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-424"></a>
## P1-424 — [CT3] Clinical Context Collaboration

Canonical identity: `f11617eed6ff4d6fa237644ec95b28b2`; [governed source](../../../../../../../continuum/strategy/capability/f11617eed6ff4d6fa237644ec95b28b2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

**Overview**

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-collaboration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-898f3e90614b5b9ea007953a8ab503b1 | Composition | [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md) | [[CT3] Clinical Context Collaboration](../../../../../../../continuum/strategy/capability/f11617eed6ff4d6fa237644ec95b28b2.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Collaboration](../../../../../../../continuum/strategy/capability/e6429225463a44d39f82e490e254e5d4.md) — `e6429225463a44d39f82e490e254e5d4`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. The source frame [CT2] Clinical Collaboration concerns: Clinical Collaboration provides secure multidisciplinary spaces bound to clinical context, including governed access to longitudinal information during discussion. Its purpose is contextual conversation between care participants rather than a separate source of authoritative clinical facts. A statement in a conversation remains discourse until explicitly authored through the governed record boundary. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Collaboration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/925`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-clinical-context-collaboration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-97b0afca9c9e513d8205eb624be06169"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-368"></a>
## P1-368 — [CT3] Clinical Device Profile Administration

Canonical identity: `d0a1ba80c790476ea6042290eb7ea09e`; [governed source](../../../../../../../continuum/strategy/capability/d0a1ba80c790476ea6042290eb7ea09e.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Overview**

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Intent**

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Boundary**

Describes device types and capabilities without taking regulatory approval or clinical-use authority.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Device Definition",
  "architectural_status": "Proposed",
  "definition": "Specifying medical equipment types, models, and regulatory classifications.",
  "overview": "A device type has a model, regulatory description and stated capabilities used to interpret equipment context. This type-level responsibility differs from identifying the actual physical instance that produced information.",
  "intent": "Make the stated type and capability of a device understandable before its instance is used as an information source.",
  "boundary": "Describes device types and capabilities without taking regulatory approval or clinical-use authority.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "capability:clinical-device-administration"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-device-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-36502a9fd3ed5a2f920fb90bda1fcfb4 | Composition | [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) | [[CT3] Clinical Device Profile Administration](../../../../../../../continuum/strategy/capability/d0a1ba80c790476ea6042290eb7ea09e.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Device Administration](../../../../../../../continuum/strategy/capability/31bcd6fda0d14cc8b992cdca39a3cbed.md) — `31bcd6fda0d14cc8b992cdca39a3cbed`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities. The source frame [CT2] Clinical Device Administration concerns: Clinical Device Administration represents device types, identifiable instances, patient or location associations and communication endpoints. This lets device-originated information be interpreted in relation to the actual device and care context, rather than merely a network address. Type, instance, assignment and connectivity each answer a different question about the source of device data. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Describes device types and capabilities without taking regulatory approval or clinical-use authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Device Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/743`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-device-definition`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-66b395be368b5464932a1d028bac18a4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); recorded line 218; Current corpus evidence; does not promote the derived assertion. Source excerpt: - **7.1 Device Definition** (`Harmonia-Relevant`)
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-395"></a>
## P1-395 — [CT3] Clinical Documentation Lifecycle Administration

Canonical identity: `df683100067b47e785037c27cfd17a3c`; [governed source](../../../../../../../continuum/strategy/capability/df683100067b47e785037c27cfd17a3c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

**Overview**

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-record-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-f37b5c0b280b5c998ca2d713802949a2 | Composition | [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md) | [[CT3] Clinical Documentation Lifecycle Administration](../../../../../../../continuum/strategy/capability/df683100067b47e785037c27cfd17a3c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Record Administration](../../../../../../../continuum/strategy/capability/ce9e7e7b22054cefb707a650d5be7219.md) — `ce9e7e7b22054cefb707a650d5be7219`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. The source frame [CT2] Clinical Record Administration concerns: Clinical Record Administration manages clinical documents as governed information with authorship, classification, versions and supersession history. It distinguishes a new document from an amendment or withdrawal and maintains metadata for discovery. Its purpose is to preserve an intelligible documentary record across change, rather than merely accumulate files. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Record Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/869`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-clinical-documentation-lifecycle-administration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-31701b65e27a544297e8b3807896433e"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-360"></a>
## P1-360 — [CT3] Clinical Escalation Path Management

Canonical identity: `cca86a32ba8c4dda84caeaa248e9f444`; [governed source](../../../../../../../continuum/strategy/capability/cca86a32ba8c4dda84caeaa248e9f444.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

**Overview**

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

**Boundary**

Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-supervision`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-7c7ad1d9a3e1567c9bf32c2185ccc2bf | Composition | [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md) | [[CT3] Clinical Escalation Path Management](../../../../../../../continuum/strategy/capability/cca86a32ba8c4dda84caeaa248e9f444.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Supervision](../../../../../../../continuum/strategy/capability/f697b1ac79b544749315f31819b17969.md) — `f697b1ac79b544749315f31819b17969`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Enable responsible clinical participants to identify and use the applicable clinical escalation path.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. The source frame [CT2] Clinical Supervision concerns: Enable responsible clinical participants to identify and use the applicable clinical escalation path. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Supervision. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/899`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-clinical-escalation-path-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-e77accf68d2a5f63b25c3c257d97feec"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

