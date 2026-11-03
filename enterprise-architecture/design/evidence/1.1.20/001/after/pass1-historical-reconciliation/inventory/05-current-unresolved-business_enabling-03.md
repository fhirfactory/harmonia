# Pass 1 adjudication inventory — 05-current-unresolved-business_enabling-03

[Review guide](../README.md) · [Complete identity index](../adjudication-inventory.md) · [Dependencies](../dependencies.md)

Recommendations only. Every SA decision is **PENDING**; every incorporation status is **NOT AUTHORISED**. Row references are scoped review locators, not architectural identities. Detailed evidence below each table preserves the original context and the exact standing of comparison targets.

| Reference | Canonical Identity | Name | Element Type | Current Standing | Definition | Architectural Context | Historical Evidence | Current Equivalent | Equivalence Assessment | Recommended Disposition | Recommended Treatment | Rationale | Consequential Changes | Solution Architect Decision | Incorporation Status |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| [P1-431](#p1-431) | f6d8f1310ba543f69e8b57bf05603db3 | [CT3] Operational &amp; Clinical Calendar Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md). 1 explicit incident native relationship(s); see [details](#p1-431). Navigation is independent of containment. | [Source and provenance](#p1-431) · [canonical Markdown](../../../../../../../continuum/strategy/capability/f6d8f1310ba543f69e8b57bf05603db3.md) | No same-instance equivalent established. [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. The source frame [CT2] Calendar Management concerns: Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Calendar Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-170](#p1-170) | 631fdb8b47084008ac9b5de28b26972b | [CT3] Operational Collaboration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md). 1 explicit incident native relationship(s); see [details](#p1-170). Navigation is independent of containment. | [Source and provenance](#p1-170) · [canonical Markdown](../../../../../../../continuum/strategy/capability/631fdb8b47084008ac9b5de28b26972b.md) | No same-instance equivalent established. [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. The source frame [CT2] General Collaboration concerns: General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] General Collaboration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-101](#p1-101) | 3b58935cfa974176989c3c75d76a00fe | [CT3] Operational Work Assignment &amp; Dispatch Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Work Allocation &amp; Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md). 1 explicit incident native relationship(s); see [details](#p1-101). Navigation is independent of containment. | [Source and provenance](#p1-101) · [canonical Markdown](../../../../../../../continuum/strategy/capability/3b58935cfa974176989c3c75d76a00fe.md) | No same-instance equivalent established. [[CT2] Work Allocation &amp; Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. The source frame [CT2] Work Allocation &amp; Dispatch concerns: Work Allocation &amp; Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Work Allocation &amp; Dispatch. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-124](#p1-124) | 4de624e7bf49472f91e3e5c734ee8725 | [CT3] Organisation Clinical Certification &amp; Compliance Support | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md). 1 explicit incident native relationship(s); see [details](#p1-124). Navigation is independent of containment. | [Source and provenance](#p1-124) · [canonical Markdown](../../../../../../../continuum/strategy/capability/4de624e7bf49472f91e3e5c734ee8725.md) | No same-instance equivalent established. [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. The source frame [CT2] Clinical Qualification Administration concerns: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Qualification Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-208](#p1-208) | 799ea278af014168b9b534e5fdadc6d1 | [CT3] Outpatient Flow Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md). 1 explicit incident native relationship(s); see [details](#p1-208). Navigation is independent of containment. | [Source and provenance](#p1-208) · [canonical Markdown](../../../../../../../continuum/strategy/capability/799ea278af014168b9b534e5fdadc6d1.md) | No same-instance equivalent established. [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. The source frame [CT2] Outpatient Operations concerns: Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Outpatient Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-217](#p1-217) | 7f918639e23842e78fa92f1bf47c5a7b | [CT3] Patient Health Information Disclosure Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 1 explicit incident native relationship(s); see [details](#p1-217). Navigation is independent of containment. | [Source and provenance](#p1-217) · [canonical Markdown](../../../../../../../continuum/strategy/capability/7f918639e23842e78fa92f1bf47c5a7b.md) | No same-instance equivalent established. [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-168](#p1-168) | 62f673613c76494eba972cad3e4e8d0b | [CT3] Patient Health Information Filter Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 1 explicit incident native relationship(s); see [details](#p1-168). Navigation is independent of containment. | [Source and provenance](#p1-168) · [canonical Markdown](../../../../../../../continuum/strategy/capability/62f673613c76494eba972cad3e4e8d0b.md) | No same-instance equivalent established. [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-055](#p1-055) | 262366990bda4ac4ace5429269cf6a98 | [CT3] Patient Health Information Restriction Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 1 explicit incident native relationship(s); see [details](#p1-055). Navigation is independent of containment. | [Source and provenance](#p1-055) · [canonical Markdown](../../../../../../../continuum/strategy/capability/262366990bda4ac4ace5429269cf6a98.md) | No same-instance equivalent established. [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-220](#p1-220) | 808e5a3c3f6b496fa329962088f0bee0 | [CT3] Patient Health Library Access | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable authorised patient access to appropriately governed health knowledge and library information. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md). 1 explicit incident native relationship(s); see [details](#p1-220). Navigation is independent of containment. | [Source and provenance](#p1-220) · [canonical Markdown](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md) | No same-instance equivalent established. [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable authorised patient access to appropriately governed health knowledge and library information. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-271](#p1-271) | 9d172cd511b742499c4e7c23f6630a35 | [CT3] Patient Notification Services | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable governed patient notifications concerning health information and applicable care or operational events. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md). 1 explicit incident native relationship(s); see [details](#p1-271). Navigation is independent of containment. | [Source and provenance](#p1-271) · [canonical Markdown](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md) | No same-instance equivalent established. [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable governed patient notifications concerning health information and applicable care or operational events. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-374](#p1-374) | d4956cab8b2b46c396899949c9486a1c | [CT3] Patient Self-Service | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Enable patients to undertake permitted information-access and administrative self-service interactions. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md). 1 explicit incident native relationship(s); see [details](#p1-374). Navigation is independent of containment. | [Source and provenance](#p1-374) · [canonical Markdown](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md) | No same-instance equivalent established. [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Enable patients to undertake permitted information-access and administrative self-service interactions. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-329](#p1-329) | ba8ce1d54ace4bc9aa3521dd954f36cb | [CT3] Patient Task Communication Services | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md). 1 explicit incident native relationship(s); see [details](#p1-329). Navigation is independent of containment. | [Source and provenance](#p1-329) · [canonical Markdown](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md) | No same-instance equivalent established. [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-349](#p1-349) | c7c7aa8e34df4568ae4b91cdde380a11 | [CT3] Patient Transfer Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md). 1 explicit incident native relationship(s); see [details](#p1-349). Navigation is independent of containment. | [Source and provenance](#p1-349) · [canonical Markdown](../../../../../../../continuum/strategy/capability/c7c7aa8e34df4568ae4b91cdde380a11.md) | No same-instance equivalent established. [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. The source frame [CT2] Patient Transport concerns: Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Patient Transport. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-187](#p1-187) | 6b7c08b60b5b48519bfe4b6f993dd6fb | [CT3] Perioperative Logistics Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate theatre case progression information and notifications to supporting perioperative operational teams. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md). 1 explicit incident native relationship(s); see [details](#p1-187). Navigation is independent of containment. | [Source and provenance](#p1-187) · [canonical Markdown](../../../../../../../continuum/strategy/capability/6b7c08b60b5b48519bfe4b6f993dd6fb.md) | No same-instance equivalent established. [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate theatre case progression information and notifications to supporting perioperative operational teams. The source frame [CT2] Theatre Operations concerns: Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Theatre Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-110](#p1-110) | 40759a835f7b456e9257c38b67b0a380 | [CT3] Practitioner Profile Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md). 1 explicit incident native relationship(s); see [details](#p1-110). Navigation is independent of containment. | [Source and provenance](#p1-110) · [canonical Markdown](../../../../../../../continuum/strategy/capability/40759a835f7b456e9257c38b67b0a380.md) | No same-instance equivalent established. [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Professional registration and profile representation do not grant clinical or operational privilege. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-175](#p1-175) | 6651471e67ae46fb8d1c1241c8590be9 | [CT3] Practitioner Role Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md). 1 explicit incident native relationship(s); see [details](#p1-175). Navigation is independent of containment. | [Source and provenance](#p1-175) · [canonical Markdown](../../../../../../../continuum/strategy/capability/6651471e67ae46fb8d1c1241c8590be9.md) | No same-instance equivalent established. [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-106](#p1-106) | 3c4a53dd8c8f468f83ca5d92397dd7e8 | [CT3] Procedure &amp; Surgical Administration | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the established procedure &amp; surgical administration information and coordination responsibility while preserving its source authority. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md). 1 explicit incident native relationship(s); see [details](#p1-106). Navigation is independent of containment. | [Source and provenance](#p1-106) · [canonical Markdown](../../../../../../../continuum/strategy/capability/3c4a53dd8c8f468f83ca5d92397dd7e8.md) | No same-instance equivalent established. [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the established procedure &amp; surgical administration information and coordination responsibility while preserving its source authority. The source frame [CT2] Procedure Administration concerns: Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Procedure Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-244](#p1-244) | 8fd5c78eba3046fa9153fb4efb44e9c8 | [CT3] Provider Clinical Qualification Tracking | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md). 1 explicit incident native relationship(s); see [details](#p1-244). Navigation is independent of containment. | [Source and provenance](#p1-244) · [canonical Markdown](../../../../../../../continuum/strategy/capability/8fd5c78eba3046fa9153fb4efb44e9c8.md) | No same-instance equivalent established. [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. The source frame [CT2] Clinical Qualification Administration concerns: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Qualification Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-370](#p1-370) | d11bdb9464b2476894aa372ab9b929a7 | [CT3] Referral Intake &amp; Progression | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md). 1 explicit incident native relationship(s); see [details](#p1-370). Navigation is independent of containment. | [Source and provenance](#p1-370) · [canonical Markdown](../../../../../../../continuum/strategy/capability/d11bdb9464b2476894aa372ab9b929a7.md) | No same-instance equivalent established. [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. The source frame [CT2] Referral Administration concerns: Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Referral Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-422](#p1-422) | eddb86c7922145439b9dcf0259f4aed5 | [CT3] Secure Clinical Communication | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md). 1 explicit incident native relationship(s); see [details](#p1-422). Navigation is independent of containment. | [Source and provenance](#p1-422) · [canonical Markdown](../../../../../../../continuum/strategy/capability/eddb86c7922145439b9dcf0259f4aed5.md) | No same-instance equivalent established. [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. The source frame [CT2] Clinical Communication Administration concerns: This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Communication Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-254](#p1-254) | 93b5548ce8d04fcd999b97536c1eebf9 | [CT3] Service Demand &amp; Capacity Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md). 1 explicit incident native relationship(s); see [details](#p1-254). Navigation is independent of containment. | [Source and provenance](#p1-254) · [canonical Markdown](../../../../../../../continuum/strategy/capability/93b5548ce8d04fcd999b97536c1eebf9.md) | No same-instance equivalent established. [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. The source frame [CT2] Service Capacity Management concerns: Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Service Capacity Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-046](#p1-046) | 22981a95ca6e466582363706fde8fceb | [CT3] Session &amp; Queue Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinic &amp; Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md). 1 explicit incident native relationship(s); see [details](#p1-046). Navigation is independent of containment. | [Source and provenance](#p1-046) · [canonical Markdown](../../../../../../../continuum/strategy/capability/22981a95ca6e466582363706fde8fceb.md) | No same-instance equivalent established. [[CT2] Clinic &amp; Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. The source frame [CT2] Clinic &amp; Practice Operations concerns: Clinic &amp; Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinic &amp; Practice Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-197](#p1-197) | 738736425b7446fdae47b06c91c4f69c | [CT3] Specimen Metadata Management | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md). 1 explicit incident native relationship(s); see [details](#p1-197). Navigation is independent of containment. | [Source and provenance](#p1-197) · [canonical Markdown](../../../../../../../continuum/strategy/capability/738736425b7446fdae47b06c91c4f69c.md) | No same-instance equivalent established. [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. The source frame [CT2] Clinical Logistics Coordination concerns: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Logistics Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-342](#p1-342) | bfa2e9a84ddd4287897573dc0d917a00 | [CT3] Specimen Movement Traceability | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md). 1 explicit incident native relationship(s); see [details](#p1-342). Navigation is independent of containment. | [Source and provenance](#p1-342) · [canonical Markdown](../../../../../../../continuum/strategy/capability/bfa2e9a84ddd4287897573dc0d917a00.md) | No same-instance equivalent established. [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. The source frame [CT2] Clinical Logistics Coordination concerns: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Logistics Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-174](#p1-174) | 66358bd6698e4050b12342522fbbb923 | [CT3] Vendor Liaison | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md). 1 explicit incident native relationship(s); see [details](#p1-174). Navigation is independent of containment. | [Source and provenance](#p1-174) · [canonical Markdown](../../../../../../../continuum/strategy/capability/66358bd6698e4050b12342522fbbb923.md) | No same-instance equivalent established. [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-084](#p1-084) | 3249647f7b9846c899f982ab339c81f9 | [CT3] Ward Logistics Coordination | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md). 1 explicit incident native relationship(s); see [details](#p1-084). Navigation is independent of containment. | [Source and provenance](#p1-084) · [canonical Markdown](../../../../../../../continuum/strategy/capability/3249647f7b9846c899f982ab339c81f9.md) | No same-instance equivalent established. [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. The source frame [CT2] Ward Operations concerns: Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Ward Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |
| [P1-090](#p1-090) | 35b66a0b38db4568849ecb842ffdcbf3 | [CT3] Work Boundary Governance | Capability | {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. | Source classification: business_enabling; tier CT3. Source frame: [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md). 1 explicit incident native relationship(s); see [details](#p1-090). Navigation is independent of containment. | [Source and provenance](#p1-090) · [canonical Markdown](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | OVERLAPPING | ACCEPT | ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated. | The existing proposal bounds an enduring ability as follows: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. The source frame [CT2] Workflow &amp; Activity Coordination concerns: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions. | SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Workflow &amp; Activity Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented. | PENDING | NOT AUTHORISED |

<a id="p1-431"></a>
## P1-431 — [CT3] Operational & Clinical Calendar Coordination

Canonical identity: `f6d8f1310ba543f69e8b57bf05603db3`; [governed source](../../../../../../../continuum/strategy/capability/f6d8f1310ba543f69e8b57bf05603db3.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

**Overview**

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:calendar-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-13d8cd7496685d96949accc85d515545 | Composition | [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md) | [[CT3] Operational &amp; Clinical Calendar Coordination](../../../../../../../continuum/strategy/capability/f6d8f1310ba543f69e8b57bf05603db3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Calendar Management](../../../../../../../continuum/strategy/capability/7669036f7e6749219412fc64df5f7064.md) — `7669036f7e6749219412fc64df5f7064`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. The source frame [CT2] Calendar Management concerns: Calendar Management projects schedules and correlates events with temporal commitments. It brings rosters, on-call windows, facility calendars and appointment milestones into operational views without replacing their authoritative sources. This distinguishes the temporal context used for coordination from the business decisions that establish the schedule. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Calendar Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/932`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-operational-clinical-calendar-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-d975937d894d521eaf2465cf00b08aaf"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-170"></a>
## P1-170 — [CT3] Operational Collaboration

Canonical identity: `631fdb8b47084008ac9b5de28b26972b`; [governed source](../../../../../../../continuum/strategy/capability/631fdb8b47084008ac9b5de28b26972b.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

**Overview**

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:general-collaboration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-4bcf52de8d995ebaae1bb6f607a62415 | Composition | [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md) | [[CT3] Operational Collaboration](../../../../../../../continuum/strategy/capability/631fdb8b47084008ac9b5de28b26972b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] General Collaboration](../../../../../../../continuum/strategy/capability/3bb740e6db284112b054905c0c6c80be.md) — `3bb740e6db284112b054905c0c6c80be`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. The source frame [CT2] General Collaboration concerns: General Collaboration provides secure operational messaging for departmental, administrative and facility-support participants. It gives non-clinical coordination an interaction context without assuming every discussion requires a patient-linked clinical workspace. Its role is communication enablement rather than ownership of the support activity being discussed. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] General Collaboration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/926`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-operational-collaboration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7a89497d659f50629cd7dd7105c0e411"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-101"></a>
## P1-101 — [CT3] Operational Work Assignment & Dispatch Coordination

Canonical identity: `3b58935cfa974176989c3c75d76a00fe`; [governed source](../../../../../../../continuum/strategy/capability/3b58935cfa974176989c3c75d76a00fe.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

**Overview**

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:work-allocation--dispatch`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-d406de5e8e2d5d84b5796dd4768d1650 | Composition | [[CT2] Work Allocation &amp; Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md) | [[CT3] Operational Work Assignment &amp; Dispatch Coordination](../../../../../../../continuum/strategy/capability/3b58935cfa974176989c3c75d76a00fe.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Work Allocation & Dispatch](../../../../../../../continuum/strategy/capability/152a5252952c405994e9b171234efcb0.md) — `152a5252952c405994e9b171234efcb0`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. The source frame [CT2] Work Allocation & Dispatch concerns: Work Allocation & Dispatch progresses authorised operational work from a recognised requirement through matching, dispatch, acknowledgement and oversight. It distinguishes an instantiated work item from its assignment and from evidence of performance, allowing stalled activity to be identified and escalated. Examples such as cleaning or transport concern real-world support work, not technical message queues. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Work Allocation & Dispatch. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/894`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-operational-work-assignment-dispatch-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-b4d502174176517d89e42e4cd48e1fff"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-124"></a>
## P1-124 — [CT3] Organisation Clinical Certification & Compliance Support

Canonical identity: `4de624e7bf49472f91e3e5c734ee8725`; [governed source](../../../../../../../continuum/strategy/capability/4de624e7bf49472f91e3e5c734ee8725.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

**Overview**

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-qualification-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-ac62936545b75b9694da7f53ec252d12 | Composition | [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) | [[CT3] Organisation Clinical Certification &amp; Compliance Support](../../../../../../../continuum/strategy/capability/4de624e7bf49472f91e3e5c734ee8725.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) — `0511a705d74a4f48a0ce92590ae764fa`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. The source frame [CT2] Clinical Qualification Administration concerns: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Qualification Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/895`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-organisation-clinical-certification-compliance-support`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-5ae6f84e6e865ab78d46b5a33357cb9d"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-208"></a>
## P1-208 — [CT3] Outpatient Flow Coordination

Canonical identity: `799ea278af014168b9b534e5fdadc6d1`; [governed source](../../../../../../../continuum/strategy/capability/799ea278af014168b9b534e5fdadc6d1.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

**Overview**

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:outpatient-operations`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-e4bee0d6103156e2a4bc29952f4e9fe8 | Composition | [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md) | [[CT3] Outpatient Flow Coordination](../../../../../../../continuum/strategy/capability/799ea278af014168b9b534e5fdadc6d1.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Outpatient Operations](../../../../../../../continuum/strategy/capability/21399d52b9624ddfbeb786f1d72e40c3.md) — `21399d52b9624ddfbeb786f1d72e40c3`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. The source frame [CT2] Outpatient Operations concerns: Outpatient Operations integrates arrival events and follow-up requirements generated as clinics progress. It connects a person's operational presence with the relevant service context and makes post-clinic booking or diagnostic needs available for subsequent activity. The responsibility retains outpatient-specific capacity semantics without requiring a separate arrival Function where approved reusable composition suffices. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Outpatient Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/886`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-outpatient-flow-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-4595d9da485357d4b4d5ab830b5ad0a1"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-217"></a>
## P1-217 — [CT3] Patient Health Information Disclosure Management

Canonical identity: `7f918639e23842e78fa92f1bf47c5a7b`; [governed source](../../../../../../../continuum/strategy/capability/7f918639e23842e78fa92f1bf47c5a7b.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

**Overview**

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:health-information-control`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-6073189f20f45f0881f401c678f399ef | Composition | [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | [[CT3] Patient Health Information Disclosure Management](../../../../../../../continuum/strategy/capability/7f918639e23842e78fa92f1bf47c5a7b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/916`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-health-information-disclosure-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-38a9a0f14b5659f4b88fab8c9ae53274"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-168"></a>
## P1-168 — [CT3] Patient Health Information Filter Management

Canonical identity: `62f673613c76494eba972cad3e4e8d0b`; [governed source](../../../../../../../continuum/strategy/capability/62f673613c76494eba972cad3e4e8d0b.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

**Overview**

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:health-information-control`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-8140a99d807f525cbb82628376c1feb1 | Composition | [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | [[CT3] Patient Health Information Filter Management](../../../../../../../continuum/strategy/capability/62f673613c76494eba972cad3e4e8d0b.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/915`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-health-information-filter-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-1174f336b2b158229d94060f3b0f43ed"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-055"></a>
## P1-055 — [CT3] Patient Health Information Restriction Management

Canonical identity: `262366990bda4ac4ace5429269cf6a98`; [governed source](../../../../../../../continuum/strategy/capability/262366990bda4ac4ace5429269cf6a98.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

**Overview**

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:health-information-control`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-f71389e030bd5d6d91919f795714bfff | Composition | [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | [[CT3] Patient Health Information Restriction Management](../../../../../../../continuum/strategy/capability/262366990bda4ac4ace5429269cf6a98.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. The source frame [CT2] Health Information Control concerns: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Control. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/914`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-health-information-restriction-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0c99a409fcad5e30b1c384f25976197f"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-220"></a>
## P1-220 — [CT3] Patient Health Library Access

Canonical identity: `808e5a3c3f6b496fa329962088f0bee0`; [governed source](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable authorised patient access to appropriately governed health knowledge and library information.

**Overview**

Enable authorised patient access to appropriately governed health knowledge and library information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable authorised patient access to appropriately governed health knowledge and library information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-health-information-communication`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-33bb89d1ee40549da4e5a01d464b917f | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Health Library Access](../../../../../../../continuum/strategy/capability/808e5a3c3f6b496fa329962088f0bee0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: [P1-207](05-current-unresolved-business_enabling-01.md#p1-207).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable authorised patient access to appropriately governed health knowledge and library information. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/913`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-health-library-access`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-cb1d28c1e8135785a123e050fa77210a"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-271"></a>
## P1-271 — [CT3] Patient Notification Services

Canonical identity: `9d172cd511b742499c4e7c23f6630a35`; [governed source](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable governed patient notifications concerning health information and applicable care or operational events.

**Overview**

Enable governed patient notifications concerning health information and applicable care or operational events. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable governed patient notifications concerning health information and applicable care or operational events.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-health-information-communication`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-1d6da7e72fed5635a6829ac7ecf17067 | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Notification Services](../../../../../../../continuum/strategy/capability/9d172cd511b742499c4e7c23f6630a35.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: [P1-207](05-current-unresolved-business_enabling-01.md#p1-207).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable governed patient notifications concerning health information and applicable care or operational events. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/910`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-notification-services`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-ac3af58b121552608850d847992c8afa"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-374"></a>
## P1-374 — [CT3] Patient Self-Service

Canonical identity: `d4956cab8b2b46c396899949c9486a1c`; [governed source](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Enable patients to undertake permitted information-access and administrative self-service interactions.

**Overview**

Enable patients to undertake permitted information-access and administrative self-service interactions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Enable patients to undertake permitted information-access and administrative self-service interactions.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-health-information-communication`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-a017b9c0e673560ab71059a2f016d512 | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Self-Service](../../../../../../../continuum/strategy/capability/d4956cab8b2b46c396899949c9486a1c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: [P1-207](05-current-unresolved-business_enabling-01.md#p1-207).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Enable patients to undertake permitted information-access and administrative self-service interactions. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/912`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-self-service`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-446513b2b23f594f80789cda9185d637"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-329"></a>
## P1-329 — [CT3] Patient Task Communication Services

Canonical identity: `ba8ce1d54ace4bc9aa3521dd954f36cb`; [governed source](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

**Overview**

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-health-information-communication`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-646c7d1460a75e23aae69f7da219969e | Composition | [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) | [[CT3] Patient Task Communication Services](../../../../../../../continuum/strategy/capability/ba8ce1d54ace4bc9aa3521dd954f36cb.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: [P1-207](05-current-unresolved-business_enabling-01.md#p1-207).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. The source frame [CT2] Health Information Communication concerns: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Health Information Communication. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/911`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-task-communication-services`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-6f0e40c416e15dfa8c70a216c1a2bb6f"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-349"></a>
## P1-349 — [CT3] Patient Transfer Coordination

Canonical identity: `c7c7aa8e34df4568ae4b91cdde380a11`; [governed source](../../../../../../../continuum/strategy/capability/c7c7aa8e34df4568ae4b91cdde380a11.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

**Overview**

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:patient-transport`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-5e2b49ac0cf95da987c56c8084a5831b | Composition | [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md) | [[CT3] Patient Transfer Coordination](../../../../../../../continuum/strategy/capability/c7c7aa8e34df4568ae4b91cdde380a11.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Patient Transport](../../../../../../../continuum/strategy/capability/6041d7c0cc5d4519aaea584bb4e6d365.md) — `6041d7c0cc5d4519aaea584bb4e6d365`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. The source frame [CT2] Patient Transport concerns: Patient Transport coordinates the operational progression of movement requests, including the patient's applicable mobility constraints. Departure, transit and arrival checkpoints make the physical journey visible to participants. The responsibility supports internal and inter-facility movement without treating arrival as acceptance of clinical responsibility at the destination. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Patient Transport. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/900`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-patient-transfer-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-c67d028a12d457b1bff85ea82df63718"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-187"></a>
## P1-187 — [CT3] Perioperative Logistics Coordination

Canonical identity: `6b7c08b60b5b48519bfe4b6f993dd6fb`; [governed source](../../../../../../../continuum/strategy/capability/6b7c08b60b5b48519bfe4b6f993dd6fb.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

**Overview**

Coordinate theatre case progression information and notifications to supporting perioperative operational teams. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:theatre-operations`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-31ebae897cf25699a507581cc3f7ceba | Composition | [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md) | [[CT3] Perioperative Logistics Coordination](../../../../../../../continuum/strategy/capability/6b7c08b60b5b48519bfe4b6f993dd6fb.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Theatre Operations](../../../../../../../continuum/strategy/capability/4bf7a8260e1444fdac44e97c3557b533.md) — `4bf7a8260e1444fdac44e97c3557b533`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate theatre case progression information and notifications to supporting perioperative operational teams. The source frame [CT2] Theatre Operations concerns: Theatre Operations integrates surgical case progression and the operational consequences relevant to portering, sterilisation and recovery participants. It gives reported milestones an operational coordination context without originating the surgical acts they describe. Theatre-specific capacity remains a business responsibility even when shared information machinery supports several service contexts. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Theatre Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/884`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-perioperative-logistics-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0df9d488a0865c958bf5b6c3557b8976"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-110"></a>
## P1-110 — [CT3] Practitioner Profile Administration

Canonical identity: `40759a835f7b456e9257c38b67b0a380`; [governed source](../../../../../../../continuum/strategy/capability/40759a835f7b456e9257c38b67b0a380.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Overview**

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Intent**

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Boundary**

Professional registration and profile representation do not grant clinical or operational privilege.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Practitioner",
  "architectural_status": "Proposed",
  "definition": "Identifying and verifying individual healthcare professionals.",
  "overview": "Practitioner identity and professional profile information let care participants be recognised across organisations. Registration and profile standing remain attributed to the issuing authority, while discovery makes that described person usable in care integration.",
  "intent": "Allow a described healthcare professional to be reliably recognised and found across participating systems.",
  "boundary": "Professional registration and profile representation do not grant clinical or operational privilege.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "capability:provider-administration"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:provider-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-e1ae1961e5955fd2a802b91a5d24a3d0 | Composition | [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | [[CT3] Practitioner Profile Administration](../../../../../../../continuum/strategy/capability/40759a835f7b456e9257c38b67b0a380.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) — `d6312f85968a4209bafff5ed009297a6`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Professional registration and profile representation do not grant clinical or operational privilege. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/726`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-practitioner`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-4c6e206c1684595b84981d627a110e53"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); recorded line 140; Current corpus evidence; does not promote the derived assertion. Source excerpt: - **2.1 Practitioner** (`Harmonia-Core`)
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-175"></a>
## P1-175 — [CT3] Practitioner Role Administration

Canonical identity: `6651471e67ae46fb8d1c1241c8590be9`; [governed source](../../../../../../../continuum/strategy/capability/6651471e67ae46fb8d1c1241c8590be9.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Overview**

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Intent**

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Boundary**

Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination.

### Earlier retained proposal contribution

This source-record pre-review contribution is historical evidence, not a second current definition.

```json
{
  "name": "Practitioner Role",
  "architectural_status": "Proposed",
  "definition": "Modeling the formal role, specialty, and operational capacity of a practitioner at a given organisation or location.",
  "overview": "The same practitioner may act in different specialties, organisations and operational capacities. Role-specific associations and communication endpoints make that participation intelligible without converting a directory role into permission or professional privilege.",
  "intent": "Make the capacity and organisational context of a practitioner interaction explicit.",
  "boundary": "Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination.",
  "tier": "CT3",
  "category": "business_enabling",
  "parent_key": "capability:provider-administration"
}
```

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:provider-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-7b924b55cb16545f90bab1513dc315e6 | Composition | [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | [[CT3] Practitioner Role Administration](../../../../../../../continuum/strategy/capability/6651471e67ae46fb8d1c1241c8590be9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) — `d6312f85968a4209bafff5ed009297a6`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/727`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:bec-practitioner-role`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-394620b518475a49b9810a7f556b3313"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from bounded responsibilities and their semantic decomposition.

- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md); recorded line 145; Current corpus evidence; does not promote the derived assertion. Source excerpt: - **2.2 Practitioner Role** (`Harmonia-Core`)
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 10; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 1.2 Non-Transfer of Ownership Guardrail

**Reviewed candidate source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-106"></a>
## P1-106 — [CT3] Procedure & Surgical Administration

Canonical identity: `3c4a53dd8c8f468f83ca5d92397dd7e8`; [governed source](../../../../../../../continuum/strategy/capability/3c4a53dd8c8f468f83ca5d92397dd7e8.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:procedure-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-13e0eed934335d8ca74f2ffa7ad1c966 | Composition | [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md) | [[CT3] Procedure &amp; Surgical Administration](../../../../../../../continuum/strategy/capability/3c4a53dd8c8f468f83ca5d92397dd7e8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Procedure Administration](../../../../../../../continuum/strategy/capability/649dd7cc26094e539386a050086dfb91.md) — `649dd7cc26094e539386a050086dfb91`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority. The source frame [CT2] Procedure Administration concerns: Procedure Administration integrates procedural bookings and the documentation of procedures performed. It keeps the scheduled case distinct from operative, anaesthetic and post-procedure information received later. That separation supports continuity across scheduling and clinical documentation without inferring that a booking establishes performance. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Procedure Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/944`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-procedure-surgical-administration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-afd1a07848325b678cdee592358efbb4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-244"></a>
## P1-244 — [CT3] Provider Clinical Qualification Tracking

Canonical identity: `8fd5c78eba3046fa9153fb4efb44e9c8`; [governed source](../../../../../../../continuum/strategy/capability/8fd5c78eba3046fa9153fb4efb44e9c8.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

**Overview**

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-qualification-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-886e419c185b5d4f8fbabdbeee546a4b | Composition | [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) | [[CT3] Provider Clinical Qualification Tracking](../../../../../../../continuum/strategy/capability/8fd5c78eba3046fa9153fb4efb44e9c8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Qualification Administration](../../../../../../../continuum/strategy/capability/0511a705d74a4f48a0ce92590ae764fa.md) — `0511a705d74a4f48a0ce92590ae764fa`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. The source frame [CT2] Clinical Qualification Administration concerns: Administer organisation clinical certification/compliance information and provider qualification evidence with its externally established standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Qualification Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/896`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-provider-clinical-qualification-tracking`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7b77530f0c8a569498954dc0a5fcf798"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-370"></a>
## P1-370 — [CT3] Referral Intake & Progression

Canonical identity: `d11bdb9464b2476894aa372ab9b929a7`; [governed source](../../../../../../../continuum/strategy/capability/d11bdb9464b2476894aa372ab9b929a7.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

**Overview**

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:referral-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-7a77ee43a6685b34948346ddffd916a8 | Composition | [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md) | [[CT3] Referral Intake &amp; Progression](../../../../../../../continuum/strategy/capability/d11bdb9464b2476894aa372ab9b929a7.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Referral Administration](../../../../../../../continuum/strategy/capability/ae0de25600ee416aa24d2a167b64f230.md) — `ae0de25600ee416aa24d2a167b64f230`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. The source frame [CT2] Referral Administration concerns: Referral Administration receives referral material, associates the supporting clinical context and tracks the recipient's explicit disposition and outcome. Its purpose is to keep the request and its progression intelligible across care boundaries. It distinguishes a request for another participant's involvement from an order for bounded fulfilment and from actual acceptance of care responsibility. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Referral Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/866`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-referral-intake-progression`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-f7fc7616aa89520a84e2235872dc93ed"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-422"></a>
## P1-422 — [CT3] Secure Clinical Communication

Canonical identity: `eddb86c7922145439b9dcf0259f4aed5`; [governed source](../../../../../../../continuum/strategy/capability/eddb86c7922145439b9dcf0259f4aed5.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

**Overview**

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-communication-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-58070bcfa27151719c659285296806b9 | Composition | [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md) | [[CT3] Secure Clinical Communication](../../../../../../../continuum/strategy/capability/eddb86c7922145439b9dcf0259f4aed5.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Communication Administration](../../../../../../../continuum/strategy/capability/b38f2c2811724734b5887b6dbaaabbfc.md) — `b38f2c2811724734b5887b6dbaaabbfc`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. The source frame [CT2] Clinical Communication Administration concerns: This capability manages secure clinical communication transactions, delivery and receipt evidence, and the required enduring audit trail. Verified provider and team endpoints support appropriate addressing while transaction correlation makes the communication history intelligible. The responsibility preserves separate meanings for transport delivery, recipient acknowledgement and clinical or business disposition. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Communication Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/945`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-secure-clinical-communication`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-b87005ff372252e1893fbd216a77d525"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-254"></a>
## P1-254 — [CT3] Service Demand & Capacity Coordination

Canonical identity: `93b5548ce8d04fcd999b97536c1eebf9`; [governed source](../../../../../../../continuum/strategy/capability/93b5548ce8d04fcd999b97536c1eebf9.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

**Overview**

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:service-capacity-management`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-eee8b14bef415602abe92d485b001508 | Composition | [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md) | [[CT3] Service Demand &amp; Capacity Coordination](../../../../../../../continuum/strategy/capability/93b5548ce8d04fcd999b97536c1eebf9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Service Capacity Management](../../../../../../../continuum/strategy/capability/d86105bf9c86445394088f65c895d37f.md) — `d86105bf9c86445394088f65c895d37f`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. The source frame [CT2] Service Capacity Management concerns: Service Capacity Management provides a higher-order view by aggregating capacity information across services and facilities. It can consider occupancy, waits and diversion while preserving the service-specific meaning of the underlying capacity. Its purpose is not to erase differences between clinic, ward, theatre, emergency, outpatient and mobile-service operation. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Service Capacity Management. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/889`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-service-demand-capacity-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-4125661f05bc5551a63787e0afe15491"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-046"></a>
## P1-046 — [CT3] Session & Queue Coordination

Canonical identity: `22981a95ca6e466582363706fde8fceb`; [governed source](../../../../../../../continuum/strategy/capability/22981a95ca6e466582363706fde8fceb.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

**Overview**

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinic--practice-operations`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-b06cb3b2497059049e92d5417236825a | Composition | [[CT2] Clinic &amp; Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md) | [[CT3] Session &amp; Queue Coordination](../../../../../../../continuum/strategy/capability/22981a95ca6e466582363706fde8fceb.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinic & Practice Operations](../../../../../../../continuum/strategy/capability/3dffe63e03124efab255204ee4e9a5d9.md) — `3dffe63e03124efab255204ee4e9a5d9`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. The source frame [CT2] Clinic & Practice Operations concerns: Clinic & Practice Operations tracks operating sessions and the operational progression of people through the clinic environment. It makes delays, waiting and completion context visible alongside service-specific capacity responsibilities. This is real-world clinic operation, distinct from the technical queueing machinery that might realise the information flow. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinic & Practice Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/882`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-session-queue-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-5c2d8bf1b2ec5762b2be0bc402aa7ca1"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-197"></a>
## P1-197 — [CT3] Specimen Metadata Management

Canonical identity: `738736425b7446fdae47b06c91c4f69c`; [governed source](../../../../../../../continuum/strategy/capability/738736425b7446fdae47b06c91c4f69c.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

**Overview**

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-logistics-coordination`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-2af8cd96ae125494a5211936a5817aca | Composition | [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) | [[CT3] Specimen Metadata Management](../../../../../../../continuum/strategy/capability/738736425b7446fdae47b06c91c4f69c.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) — `0c27be11b4a44cb19e992309b8146a17`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.
  Boundary: Includes specimen metadata and specimen movement traceability. Supply/materiel movement belongs to Health Materiel Traceability Administration.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. The source frame [CT2] Clinical Logistics Coordination concerns: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Logistics Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/901`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-specimen-metadata-management`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-e58d7bdb27f95fb396623167a677a5ba"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-342"></a>
## P1-342 — [CT3] Specimen Movement Traceability

Canonical identity: `bfa2e9a84ddd4287897573dc0d917a00`; [governed source](../../../../../../../continuum/strategy/capability/bfa2e9a84ddd4287897573dc0d917a00.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

**Overview**

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:clinical-logistics-coordination`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-5511cf0501a054c0ad9745bdc3c082b4 | Composition | [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) | [[CT3] Specimen Movement Traceability](../../../../../../../continuum/strategy/capability/bfa2e9a84ddd4287897573dc0d917a00.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Clinical Logistics Coordination](../../../../../../../continuum/strategy/capability/0c27be11b4a44cb19e992309b8146a17.md) — `0c27be11b4a44cb19e992309b8146a17`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services.
  Boundary: Includes specimen metadata and specimen movement traceability. Supply/materiel movement belongs to Health Materiel Traceability Administration.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. The source frame [CT2] Clinical Logistics Coordination concerns: Coordinate specimen metadata and specimen movement traceability across responsible collection, courier and diagnostic services. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Clinical Logistics Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/902`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-specimen-movement-traceability`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-8c4d5090fb7150469d8e4b10a7551dab"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-174"></a>
## P1-174 — [CT3] Vendor Liaison

Canonical identity: `66358bd6698e4050b12342522fbbb923`; [governed source](../../../../../../../continuum/strategy/capability/66358bd6698e4050b12342522fbbb923.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

**Overview**

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:provider-administration`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-852e76511a44553d960f241314195a06 | Composition | [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) | [[CT3] Vendor Liaison](../../../../../../../continuum/strategy/capability/66358bd6698e4050b12342522fbbb923.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Provider Administration](../../../../../../../continuum/strategy/capability/d6312f85968a4209bafff5ed009297a6.md) — `d6312f85968a4209bafff5ed009297a6`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. The source frame [CT2] Provider Administration concerns: Provider Administration maintains the practitioner, role, affiliation and engagement context needed to interpret healthcare participation. National identifiers, professional profiles, role-specific endpoints and privilege records support different questions: who the practitioner is, in what capacity they act and how they can be reached. Directory representation makes those distinctions usable without replacing the business governance that establishes professional standing. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Provider Administration. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/862`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-vendor-liaison`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-f65c91b69f5a54cfae47b3572ae8bb4e"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-084"></a>
## P1-084 — [CT3] Ward Logistics Coordination

Canonical identity: `3249647f7b9846c899f982ab339c81f9`; [governed source](../../../../../../../continuum/strategy/capability/3249647f7b9846c899f982ab339c81f9.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

**Overview**

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `capability:ward-operations`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-63ae61f37eb45f909a3a3d9ec07edb50 | Composition | [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md) | [[CT3] Ward Logistics Coordination](../../../../../../../continuum/strategy/capability/3249647f7b9846c899f982ab339c81f9.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Ward Operations](../../../../../../../continuum/strategy/capability/f996ecca728f4942a88b034aca818a99.md) — `f996ecca728f4942a88b034aca818a99`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. The source frame [CT2] Ward Operations concerns: Ward Operations coordinates ward occupancy and care-place context and assembles the information needed around shift changes. Its responsibility includes the ward-specific meaning of capacity and patient flow rather than a generic resource counter. It can make pending concerns and tasks visible without becoming the authority managing clinical handover. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Ward Operations. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/883`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-ward-logistics-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-5d528e4fcb5e5092a26ac5fba62d8fe9"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-090"></a>
## P1-090 — [CT3] Work Boundary Governance

Canonical identity: `35b66a0b38db4568849ecb842ffdcbf3`; [governed source](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether this proposal is a distinct part, complementary enablement or an overlapping representation within the existing context; acceptance of a parent does not accept this row.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

**Overview**

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent**

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

**Boundary**

Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

### Classification, source framing and actual relationships

Source category: `business_enabling`; source tier: `CT3`; parent key: `reviewed:bec-workflow-activity-coordination`; owner key: `None`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-a3b5fe2bd6c856fc8a8923e7bd309bc7 | Composition | [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "current", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: [P1-348](05-current-unresolved-business_enabling-01.md#p1-348).

**Assessment and rationale:** OVERLAPPING. The existing proposal bounds an enduring ability as follows: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. The source frame [CT2] Workflow & Activity Coordination concerns: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. This is a broader frame or enabling context, not proof of semantic equivalence to the proposed part. Its explicit boundary is: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Recommend retaining this bounded concept, subject to SA review of the compared scopes and exact graph below. No evidence cited here establishes an additional same-instance equivalent; this is not an exhaustive uniqueness or gap finding. AX-14/17/18 and DR-6 permit layered and partial contributions.

**Proposed treatment:** ACCEPT the existing bounded architectural concept under its present canonical identity; retain or reconcile its current representation only as the SA decides. Preserve its Proposed standing until that decision is incorporated.

**Consequences if authorised:** SA must affirm or amend the existing definition and proposed CT3 framing under [CT2] Workflow & Activity Coordination. Preserve BC/BEC/EC responsibility distinctions. Establish any changed Composition, Aggregation, Realization, Home or responsibility assertion separately; no formal names or identities are invented.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/931`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `reviewed:bec-work-boundary-governance`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-eed78c1571b356ff9a9222054e7feeb4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Placement source qualification:** Proposed structural assertions; existing Canonical semantic standing retained; Reconciliation against the explicit human-reviewed BEC candidate hierarchy.

- [docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json](../../../../../../../../docs/latex-rad/Harmonia-EA-Capability-Map/v0.2-draft/reviewed-bec-hierarchy.json); recorded line 2; Explicit human task decision; reviewed candidate, not RADS acceptance. Source excerpt:  "authority": "User-provided human-adjudicated reviewed candidate BEC hierarchy, 2026-10-16; bounded reconciliation input, not RADS acceptance.",

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

