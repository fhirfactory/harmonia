# Pass 1 adjudication inventory — 02-pending-features

[Review guide](../README.md) · [Complete identity index](../adjudication-inventory.md) · [Dependencies](../dependencies.md)

Recommendations only. Every SA decision is **PENDING**; every incorporation status is **NOT AUTHORISED**. Row references are scoped review locators, not architectural identities. Detailed evidence below each table preserves the original context and the exact standing of comparison targets.

| Reference | Canonical Identity | Name | Element Type | Current Standing | Definition | Architectural Context | Historical Evidence | Current Equivalent | Equivalence Assessment | Recommended Disposition | Recommended Treatment | Rationale | Consequential Changes | Solution Architect Decision | Incorporation Status |
| --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- | --- |
| [P1-089](#p1-089) | 34c8309244c142ec8772dcbb11ecc4ef | [Feature] Activity Outcome &amp; Continuation Rules | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Governed Operational Activity Design](../../../../../../../continuum/strategy/capability/3a96040821c14fe38830411dab4f8453.md). 0 explicit incident native relationship(s); see [details](#p1-089). Navigation is independent of containment. | [Source and provenance](#p1-089) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/34c8309244c142ec8772dcbb11ecc4ef.md) | No same-instance equivalent established. [[CT3] Operational Fulfilment Definition](../../../../../../../continuum/strategy/capability/2b52c0de17b146da962f303e99f90a4a.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the bounded activity-design concept and two distinct Features, subject to reconciliation of BEC versus EC abstraction. | The proposed BEC activity envelope specifies context, expected fulfilment and continuation; the EC definition supplies reusable operational machinery, while Work Boundary Governance concerns another system undertaking work. Similar fulfilment terms do not establish the same architectural responsibility. AX-04/14/18 apply. | SA must decide whether BEC design is independently required or represented through existing EC enablement; establish current CT2 and explicit CT3 frames if retained. Do not allocate an orchestrator or dynamic engine. | PENDING | NOT AUTHORISED |
| [P1-044](#p1-044) | 21d0601a852043a4b0b1d4fe841108d2 | [Feature] Controlled Administrative Recovery | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 0 explicit incident native relationship(s); see [details](#p1-044). Navigation is independent of containment. | [Source and provenance](#p1-044) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/21d0601a852043a4b0b1d4fe841108d2.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |
| [P1-338](#p1-338) | bdfaeebab74e4d4d96a5e3799a3aa07b | [Feature] Entity Behaviour Failure &amp; Recovery Governance | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Entity-Specific Operational Coordination](../../../../../../../continuum/strategy/capability/9f0c1429b64f4f88982fbbda6eaec233.md). 0 explicit incident native relationship(s); see [details](#p1-338). Navigation is independent of containment. | [Source and provenance](#p1-338) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/bdfaeebab74e4d4d96a5e3799a3aa07b.md) | No same-instance equivalent established. [[CT3] Entity-Relative Operational Progression](../../../../../../../continuum/strategy/capability/69c82474fb534152b151c92f706197d0.md); [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | OVERLAPPING | ACCEPT | ACCEPT entity-relative coordination and its separate progression/recovery Features; preserve identities and refrain from assigning a Twin component. | Entity-relative interested-behaviour progression and controlled recovery differ from the external work bridge. The EC progression capability is a plausible enabling responsibility, not proof of the same BEC identity. Serial entity progression, fan-out and uncertain effects remain separate. AX-14/15/16/18 apply. | Resolve BEC/EC abstraction, current CT2 frame and CT3-to-Feature containment explicitly; preserve halt/recovery dispositions and fresh information context. | PENDING | NOT AUTHORISED |
| [P1-326](#p1-326) | b9960d7de097464ea6de94929ca48c89 | [Feature] Entity-Relative Activity Coordination | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Entity-Specific Operational Coordination](../../../../../../../continuum/strategy/capability/9f0c1429b64f4f88982fbbda6eaec233.md). 1 explicit incident native relationship(s); see [details](#p1-326). Navigation is independent of containment. | [Source and provenance](#p1-326) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/b9960d7de097464ea6de94929ca48c89.md) | No same-instance equivalent established. [[CT3] Entity-Relative Operational Progression](../../../../../../../continuum/strategy/capability/69c82474fb534152b151c92f706197d0.md); [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) | OVERLAPPING | ACCEPT | ACCEPT entity-relative coordination and its separate progression/recovery Features; preserve identities and refrain from assigning a Twin component. | Entity-relative interested-behaviour progression and controlled recovery differ from the external work bridge. The EC progression capability is a plausible enabling responsibility, not proof of the same BEC identity. Serial entity progression, fan-out and uncertain effects remain separate. AX-14/15/16/18 apply. | Resolve BEC/EC abstraction, current CT2 frame and CT3-to-Feature containment explicitly; preserve halt/recovery dispositions and fresh information context. | PENDING | NOT AUTHORISED |
| [P1-001](#p1-001) | 001a067a4b3b46bebfe945f1bef541ce | [Feature] Governance Requirement Version Publication | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Publish attributable versions and applicability of externally approved governing requirements. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Governance Requirement Publication Support](../../../../../../../continuum/strategy/capability/7e780751d50e4ffe942f8443b7cb6567.md). 0 explicit incident native relationship(s); see [details](#p1-001). Navigation is independent of containment. | [Source and provenance](#p1-001) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/001a067a4b3b46bebfe945f1bef541ce.md) | No same-instance equivalent established. [[CT3] Policy / Obligation Governance](../../../../../../../continuum/strategy/capability/73c6e5b36bdf4bf0ad2b8b50c9b9a00b.md); [[CT2] Information Design Governance](../../../../../../../continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md); [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) | DISTINCT | ACCEPT | ACCEPT the bounded publication concept and Feature without granting rule approval or policy ownership. | Publication of attributable versions/applicability of externally approved requirements supports consumers. Authoring/approving obligations, information-model design and assurance-criteria specification answer different authority questions. Shared governance subject matter supplies no equivalent or parent. AX-06/14/17/18 apply. | SA must supply current CT2 placement and explicit CT3 framing; retain requirement-authority, permission-policy and assurance-criteria distinctions. | PENDING | NOT AUTHORISED |
| [P1-378](#p1-378) | d6c271ae95ea44449c5e22665d1d9bc2 | [Feature] Governed Policy Assignment &amp; Revocation | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 0 explicit incident native relationship(s); see [details](#p1-378). Navigation is independent of containment. | [Source and provenance](#p1-378) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/d6c271ae95ea44449c5e22665d1d9bc2.md) | No same-instance equivalent established. [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md); [[CT2] Policy &amp; Control](../../../../../../../continuum/strategy/capability/0c5d986cc948421a8bebcab3b74d108f.md); [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | OVERLAPPING | ACCEPT | ACCEPT bounded policy-assignment/revocation meaning while preserving source identity and each authority boundary. | Assignment, temporal applicability, expiry and explicit revocation concern effective permission administration. Policy resolution consumes that basis; patient-specific restrictions and client consent retain independent ownership. Shared policy subject matter does not make these the same responsibility. AX-07/14/18 apply. | Resolve CT3 framing explicitly rather than treating the source CT2 owner as a legal Feature parent; retain expiry/revocation distinctions and Themis policy authority. | PENDING | NOT AUTHORISED |
| [P1-099](#p1-099) | 3aedee8f78484b81955866bcf16b8a6d | [Feature] Independent Evidence Assessment &amp; Adjudication | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Governed Assurance](../../../../../../../continuum/strategy/capability/da1a983d4dcf4d5bb772b8f69bc08426.md). 3 explicit incident native relationship(s); see [details](#p1-099). Navigation is independent of containment. | [Source and provenance](#p1-099) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/3aedee8f78484b81955866bcf16b8a6d.md) | No same-instance equivalent established. [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md); [[CT2] Assurance Evaluation](../../../../../../../continuum/strategy/capability/d1b20e0fb91e4bd1950ad57b84e2cafd.md); [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) | OVERLAPPING | ACCEPT | ACCEPT the subject-assurance concept; retain existing independent identities pending a coordinated CT2/CT3/Feature representation decision. Do not reinterpret it as recursive self-assurance. | Assurance of a governed subject uses context, evidence assessment and adjudication. Review of the assurance service has a different subject; EC Assurance Evaluation enables reasoning about evidence without acquiring the BEC owner role. Existing approved Functions establish meaningful subject-assurance responsibility while classification and Feature decomposition remain qualified. Insufficiency is not failure or clinical judgement. AX-14/15/17/18 and DR-19 apply. | Resolve CT2 placement relative to Independent Assurance Enablement, CT3 relevance and the Feature decomposition without adding Functions or changing the approved assurance boundary. | PENDING | NOT AUTHORISED |
| [P1-191](#p1-191) | 70a444156cd246cb97608a782e4bdea8 | [Feature] Information Authority &amp; Source Standing Representation | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Attach and preserve qualified source-authority and credibility standing for managed assertions. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Information Authority Representation](../../../../../../../continuum/strategy/capability/8c850faafb334f97a81a0b4cd8fdb65f.md). 1 explicit incident native relationship(s); see [details](#p1-191). Navigation is independent of containment. | [Source and provenance](#p1-191) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/70a444156cd246cb97608a782e4bdea8.md) | No same-instance equivalent established. [[CT2] Information Authority Qualification](../../../../../../../continuum/strategy/capability/cbba9984dd404deabed41fdcc96088b1.md); [[CT3] Information Asset Tag Management](../../../../../../../continuum/strategy/capability/df27c3ba6e164e56af7bff4e1eb1954a.md); [[CT3] Information Attribution &amp; Lineage](../../../../../../../continuum/strategy/capability/8bc66c9bc1024a68a1bfd010b4dd1b79.md) | OVERLAPPING | ACCEPT | ACCEPT explicit information-authority representation and its Feature; evaluate the BEC/EC distinction before retaining overlapping representations. | Source credibility and authoritative/informational/anecdotal standing are required by AX-06 and independent of persistence, freshness, tags and lineage. The current proposed EC qualification responsibility is a strong enabling comparison; it is not adjudicated equivalence to this BEC representation. AX-05/06/14/17 apply. | Resolve the superseded stewardship parent, current CT2/CT3 frame and Feature containment; preserve policy-dependent use rather than a universal winner rule. | PENDING | NOT AUTHORISED |
| [P1-320](#p1-320) | b69936757c4446a7a1b57a455aae7a5f | [Feature] Mandatory Significant Activity Audit Capture | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Significant Activity Audit Capture](../../../../../../../continuum/strategy/capability/d903948169eb4785b0f03d916c842a8a.md). 1 explicit incident native relationship(s); see [details](#p1-320). Navigation is independent of containment. | [Source and provenance](#p1-320) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/b69936757c4446a7a1b57a455aae7a5f.md) | No same-instance equivalent established. [[CT3] Governed Activity Audit Evidence](../../../../../../../continuum/strategy/capability/c8d93ddc5aa84960964b78345fff1b29.md); [[CT2] Provenance &amp; Traceability](../../../../../../../continuum/strategy/capability/68a0755aa4e14a509ac5e6c0bbcff3e7.md); [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) | OVERLAPPING | ACCEPT | ACCEPT the mandatory-capture concept and its bounded Feature; assess whether the separate BEC representation adds a responsibility beyond EC audit evidence. | Mandatory authoritative activity-audit capture is explicitly required by AX-07. EC audit evidence overlaps its enabling purpose; data provenance preserves information origin/transformation and does not replace activity audit. Neither generic traceability nor disclosure supplies an adjudicated BEC identity. AX-07/08/14 apply; DR-18/19 boundaries remain controlling. | Select a current BEC CT2/CT3 frame or explicitly reconcile representation after SA decision. Preserve Kleio intrinsic capture authority, Themis policy evaluation, non-PHI diagnostics and independence from the audited identity. | PENDING | NOT AUTHORISED |
| [P1-013](#p1-013) | 08e37326908a49988dfb787f68df840a | [Feature] Non-PHI Compliance Auditing | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Generate tamper-evident, verifiable audit records of security-significant events without emitting unmasked PHI. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 2 explicit incident native relationship(s); see [details](#p1-013). Navigation is independent of containment. | [Source and provenance](#p1-013) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/08e37326908a49988dfb787f68df840a.md) | No same-instance equivalent established. [[CT3] Governed Activity Audit Evidence](../../../../../../../continuum/strategy/capability/c8d93ddc5aa84960964b78345fff1b29.md); [[Feature] Mandatory Significant Activity Audit Capture](../../../../../../../continuum/strategy/capability/feature/b69936757c4446a7a1b57a455aae7a5f.md); [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) | OVERLAPPING | ACCEPT | ACCEPT security-audit meaning; ask SA whether the two audit Features are complementary constraints or duplicate representations before consolidation. | Tamper-evident non-PHI security-event records overlap mandatory audit capture but the mandatory Feature additionally specifies capture authority independent of the audited actor. Content lineage and assurance findings remain separate. AX-07/08/14 and DR-18/19 apply. | Establish a lawful CT3 frame and decide audit Feature granularity; preserve non-PHI diagnostic constraints, meaningful evidence and mandatory capture authority. | PENDING | NOT AUTHORISED |
| [P1-103](#p1-103) | 3b7dcee7d58d4d1c960dc92abd58b4c3 | [Feature] Operation-Bound Authorisation Enforcement | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Apply effective policy at the governed action boundary and reject actions lacking sufficient authority. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 0 explicit incident native relationship(s); see [details](#p1-103). Navigation is independent of containment. | [Source and provenance](#p1-103) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/3b7dcee7d58d4d1c960dc92abd58b4c3.md) | No same-instance equivalent established. [[CT3] Operation-Bound Policy Enforcement](../../../../../../../continuum/strategy/capability/cb32b32c657445b3b9b27bebc069ace9.md); [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md); [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md) | OVERLAPPING | ACCEPT | ACCEPT operation-bound enforcement as a Feature; retain its existing identity without inventing a patient-specific owner. | AX-07 requires enforcement at each governed operation. The EC enforcement ability is a strong enabling comparison; patient-specific restriction/disclosure control is narrower, while policy resolution determines the applicable decision. No patient CT3 frame follows from those overlaps. | SA must decide whether to frame the Feature under the existing proposed EC enforcement CT3 or another explicitly governed CT3; preserve Themis policy ownership and separate resolution/enforcement. | PENDING | NOT AUTHORISED |
| [P1-143](#p1-143) | 58dcd59389664b7d83d14b536d7d03d0 | [Feature] Operational Activity Context Definition | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Define the anticipated information context and expected fulfilment basis of an operational undertaking. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Governed Operational Activity Design](../../../../../../../continuum/strategy/capability/3a96040821c14fe38830411dab4f8453.md). 1 explicit incident native relationship(s); see [details](#p1-143). Navigation is independent of containment. | [Source and provenance](#p1-143) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/58dcd59389664b7d83d14b536d7d03d0.md) | No same-instance equivalent established. [[CT3] Operational Fulfilment Definition](../../../../../../../continuum/strategy/capability/2b52c0de17b146da962f303e99f90a4a.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the bounded activity-design concept and two distinct Features, subject to reconciliation of BEC versus EC abstraction. | The proposed BEC activity envelope specifies context, expected fulfilment and continuation; the EC definition supplies reusable operational machinery, while Work Boundary Governance concerns another system undertaking work. Similar fulfilment terms do not establish the same architectural responsibility. AX-04/14/18 apply. | SA must decide whether BEC design is independently required or represented through existing EC enablement; establish current CT2 and explicit CT3 frames if retained. Do not allocate an orchestrator or dynamic engine. | PENDING | NOT AUTHORISED |
| [P1-066](#p1-066) | 2b071c4fb10c4f61afecd18d09387214 | [Feature] Security Context Propagation | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Propagate tamper-evident, attributable security context across asynchronous processing boundaries. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 0 explicit incident native relationship(s); see [details](#p1-066). Navigation is independent of containment. | [Source and provenance](#p1-066) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/2b071c4fb10c4f61afecd18d09387214.md) | No same-instance equivalent established. [[CT3] Operation-Bound Policy Enforcement](../../../../../../../continuum/strategy/capability/cb32b32c657445b3b9b27bebc069ace9.md); [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md); [[CT2] Policy &amp; Control](../../../../../../../continuum/strategy/capability/0c5d986cc948421a8bebcab3b74d108f.md) | DISTINCT | ACCEPT | ACCEPT security-context propagation with its own existing Feature identity; preserve the explicit absence of an inferred consent Function association. | Propagation preserves established tamper-evident, attributable security context across asynchronous boundaries. It does not evaluate consent or resolve the effective policy decision. AX-07 distinguishes operational context from persistent information; AX-14/16 preserve that boundary. | Resolve lawful CT3 framing and independently review any Realization/Home change; no automatic persistent security-context payload or policy ownership transfer. | PENDING | NOT AUTHORISED |
| [P1-123](#p1-123) | 4d917929d5cf43218049a84cf46662b0 | [Feature] Service Readiness Dependency Assessment | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Service Offering Readiness Coordination](../../../../../../../continuum/strategy/capability/0caecaba5d3e4781b8671b12c13ee283.md). 1 explicit incident native relationship(s); see [details](#p1-123). Navigation is independent of containment. | [Source and provenance](#p1-123) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/4d917929d5cf43218049a84cf46662b0.md) | No same-instance equivalent established. [[CT3] Health Service Operational Readiness](../../../../../../../continuum/strategy/capability/cc91eb4017034939ab82b152bddf7b1c.md); [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md); [[CT2] Service Delivery Preparedness Management](../../../../../../../continuum/strategy/capability/9665d894c4c9438999d8cc10afae695d.md) | OVERLAPPING | ACCEPT | ACCEPT collective readiness as a bounded concept; coordinate all existing readiness identities and distinguish evidence assembly from determination. | Collective, context-qualified deliverability is broader than dependency tracking, operating hours, an empty bed or aggregate capacity. The current Candidate readiness concept is a serious equivalence candidate across BC/BEC layers, but authority to determine readiness is not established by that similarity. AX-14/17/18 apply. | Resolve BC/BEC responsibilities and decision authority before selecting current CT2 and CT3-to-Feature containment. No new site/session identities or readiness engine. | PENDING | NOT AUTHORISED |
| [P1-078](#p1-078) | 3192ba1541ff41458d176184119c30bc | [Feature] Standards-Based Boundary Exchange | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Provide standards-conformant boundary exchange for healthcare messaging and resource representations. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Health Information Communication](../../../../../../../continuum/strategy/capability/9794b2aff43443b495a19aefe0efa67b.md). 2 explicit incident native relationship(s); see [details](#p1-078). Navigation is independent of containment. | [Source and provenance](#p1-078) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/3192ba1541ff41458d176184119c30bc.md) | No same-instance equivalent established. [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md); [[CT2] Health Information Exchange Enablement](../../../../../../../continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md); [[Feature] Exchange Profile Governance](../../../../../../../continuum/strategy/capability/feature/5e59d8f2e23e4fb5b59a5bd1dd3f31fa.md) | DISTINCT | ACCEPT | ACCEPT the boundary-exchange concept under the existing identity; retain the Feature separately from patient-facing communication. No same-name merge. | The historical responsibility mediates standards-facing protocols/technical transmission; the current CT2 with the same communication label covers patient notifications, tasks, self-service and library access. Exchange-profile governance defines a contract rather than performing transmission. AX-02/13/14 preserve these distinctions. | Resolve the absent CT3 definition from the preserved historical explanation only after SA review; select a lawful CT2 frame and establish any CT3-to-Feature Composition explicitly. Preserve separation from profile governance and clinical message purpose. | PENDING | NOT AUTHORISED |
| [P1-213](#p1-213) | 7cf9dd3aeeb84a83b2445f897db9bd4e | [Feature] Synthetic Task Orchestration | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Execute non-human, automated platform activity units and sequence orchestration. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 2 explicit incident native relationship(s); see [details](#p1-213). Navigation is independent of containment. | [Source and provenance](#p1-213) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/7cf9dd3aeeb84a83b2445f897db9bd4e.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |
| [P1-094](#p1-094) | 377a40e8f2d64840a10dd55f87a9d005 | [Feature] To Do Decision &amp; Approval Coordination | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Coordinate human clinical review items, sign-offs, and approval workflows. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 2 explicit incident native relationship(s); see [details](#p1-094). Navigation is independent of containment. | [Source and provenance](#p1-094) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/377a40e8f2d64840a10dd55f87a9d005.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |
| [P1-075](#p1-075) | 2ef8f67907c34cb5a6a37fe7181434e2 | [Feature] Trusted Identity Context Intake | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Accept and qualify externally authenticated identity context for Harmonia-governed operations. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md). 0 explicit incident native relationship(s); see [details](#p1-075). Navigation is independent of containment. | [Source and provenance](#p1-075) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/2ef8f67907c34cb5a6a37fe7181434e2.md) | No same-instance equivalent established. [[CT3] Identity Identification &amp; Authentication](../../../../../../../continuum/strategy/capability/ba4ad62548984d63ad75da3561566431.md); [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md); [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) | DISTINCT | ACCEPT | ACCEPT trusted identity-context intake as the bounded Feature while preserving external authentication authority. | Trusted intake verifies an externally authenticated identity context; deciding what operations it may perform and administering patient-information restrictions answer different questions. The broader identity/authentication EC responsibility does not authorise a new Harmonia authentication issuer. AX-07/14/18 apply. | Select an explicit CT3 frame consistent with trusted intake; do not broaden the Feature into credential issuance or professional privilege determination. | PENDING | NOT AUTHORISED |
| [P1-418](#p1-418) | eb06ecc956404d0397701ceafb5fbd46 | [Feature] Uncertain Activity Outcome Reconciliation | Feature | {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"} | Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 3 explicit incident native relationship(s); see [details](#p1-418). Navigation is independent of containment. | [Source and provenance](#p1-418) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/eb06ecc956404d0397701ceafb5fbd46.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |
| [P1-156](#p1-156) | 5ca494e85869419c867618fd9648d03a | [Feature] Work Order Progression | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Manage execution states and dispatch of human operational work orders. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 2 explicit incident native relationship(s); see [details](#p1-156). Navigation is independent of containment. | [Source and provenance](#p1-156) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/5ca494e85869419c867618fd9648d03a.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |
| [P1-421](#p1-421) | ec55d06e393e4988a84c6157b719bd40 | [Feature] Workflow Timeout &amp; Escalation Oversight | Feature | {"semantic": "Canonical", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"} | Supervise operational deadlines, executing automated timeout notifications and supervisory escalations. | Source classification: not recorded in historical reference; tier FT. Source frame: [[CT3] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/94c683c7588f475e973b58cb226345db.md). 2 explicit incident native relationship(s); see [details](#p1-421). Navigation is independent of containment. | [Source and provenance](#p1-421) · [canonical Markdown](../../../../../../../continuum/strategy/capability/feature/ec55d06e393e4988a84c6157b719bd40.md) | No same-instance equivalent established. [[CT2] Workflow &amp; Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md); [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md); [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) | OVERLAPPING | ACCEPT | ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution. | Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply. | Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass. | PENDING | NOT AUTHORISED |

<a id="p1-089"></a>
## P1-089 — [Feature] Activity Outcome & Continuation Rules

Canonical identity: `34c8309244c142ec8772dcbb11ecc4ef`; [governed source](../../../../../../../continuum/strategy/capability/feature/34c8309244c142ec8772dcbb11ecc4ef.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** The old temporal-enablement parent is superseded; resolve abstraction and placement together.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking.

**Overview**

An output may contribute to completion, subsequent activity or retained evidence under different rules. Those meanings need to be specified rather than inferred from a returned payload.

**Boundary**

Completion policy is not business outcome truth; uncertain effects remain explicit.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-governed-operational-activity-design`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Operational Fulfilment Definition](../../../../../../../continuum/strategy/capability/2b52c0de17b146da962f303e99f90a4a.md) — `2b52c0de17b146da962f303e99f90a4a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Define the context requirements, expected outcomes and housekeeping of an operational undertaking.
  Boundary: A fulfilment definition does not execute, orchestrate or choreograph its activity.
  Independent adjudication: P1-068 (preserved source locator `05-current-unresolved-enterprise.md#p1-068`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. The proposed BEC activity envelope specifies context, expected fulfilment and continuation; the EC definition supplies reusable operational machinery, while Work Boundary Governance concerns another system undertaking work. Similar fulfilment terms do not establish the same architectural responsibility. AX-04/14/18 apply.

**Proposed treatment:** ACCEPT the bounded activity-design concept and two distinct Features, subject to reconciliation of BEC versus EC abstraction.

**Consequences if authorised:** SA must decide whether BEC design is independently required or represented through existing EC enablement; establish current CT2 and explicit CT3 frames if retained. Do not allocate an orchestrator or dynamic engine.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/824`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-activity-outcome-continuation-rules`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-339280e5161858089a433918445c4836"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/04-information-architecture/information-families/task-work.md](../../../../../../../../docs/markdown/04-information-architecture/information-families/task-work.md); recorded line 7; Current corpus evidence; does not promote the derived assertion. Source excerpt: `ActionableTaskArchetype`, `ActionableTask`, `FulfillmentTask` and `TaskOutcome`
- [docs/markdown/05-information-systems-architecture/context-and-persistence-management.md](../../../../../../../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md); recorded line 52; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## Context — versioned temporal values

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-044"></a>
## P1-044 — [Feature] Controlled Administrative Recovery

Canonical identity: `21d0601a852043a4b0b1d4fe841108d2`; [governed source](../../../../../../../continuum/strategy/capability/feature/21d0601a852043a4b0b1d4fe841108d2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries.

**Overview**

Operators may need to resume, reconcile or repair an operational concern. That is first-class work with permission and outcome standing, rather than direct mutation through a console.

**Boundary**

Administrative interaction grants no raw cache/database or subsystem-internal bypass.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/826`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-controlled-administrative-recovery`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-14f2b94d4e9551c6b3d19b26aa56aa16"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md); recorded line 73; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## 3. Twin-relative execution
- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md); recorded line 33; Current corpus evidence; does not promote the derived assertion. Source excerpt: Identity.Authentication is a **Separately Provided Capability (SEP)**. Harmonia's primary authentication pattern is standards-based OIDC/OAuth integration with an externally managed Identity Provider.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-338"></a>
## P1-338 — [Feature] Entity Behaviour Failure & Recovery Governance

Canonical identity: `bdfaeebab74e4d4d96a5e3799a3aa07b`; [governed source](../../../../../../../continuum/strategy/capability/feature/bdfaeebab74e4d4d96a5e3799a3aa07b.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Current BEC destination is unadjudicated; resolve with internal activity progression without merging the three identities.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing.

**Overview**

Failure may stop a behaviour, an input or the entity progression and may leave effects uncertain. Resumption must preserve that distinction rather than imply rollback or automatic safe retry.

**Boundary**

Recovery cannot silently declare no effects, bypass governance or resume a halted entity automatically.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-entity-specific-operational-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Entity-Relative Operational Progression](../../../../../../../continuum/strategy/capability/69c82474fb534152b151c92f706197d0.md) — `69c82474fb534152b151c92f706197d0`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate serial, entity-relative operational inputs and their interested behaviours.
  Boundary: Interest fan-out is distinct from intra-entity concurrency; entity progression does not acquire originating facts.
  Independent adjudication: P1-183 (preserved source locator `05-current-unresolved-enterprise.md#p1-183`).
- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).

**Assessment and rationale:** OVERLAPPING. Entity-relative interested-behaviour progression and controlled recovery differ from the external work bridge. The EC progression capability is a plausible enabling responsibility, not proof of the same BEC identity. Serial entity progression, fan-out and uncertain effects remain separate. AX-14/15/16/18 apply.

**Proposed treatment:** ACCEPT entity-relative coordination and its separate progression/recovery Features; preserve identities and refrain from assigning a Twin component.

**Consequences if authorised:** Resolve BEC/EC abstraction, current CT2 frame and CT3-to-Feature containment explicitly; preserve halt/recovery dispositions and fresh information context.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/822`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-entity-behaviour-failure-recovery-governance`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-337f8c9d1cb75bd19fc27f6b3cacb703"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md); recorded line 73; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## 3. Twin-relative execution

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-326"></a>
## P1-326 — [Feature] Entity-Relative Activity Coordination

Canonical identity: `b9960d7de097464ea6de94929ca48c89`; [governed source](../../../../../../../continuum/strategy/capability/feature/b9960d7de097464ea6de94929ca48c89.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Current BEC destination is unadjudicated; resolve with internal activity progression without merging the three identities.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Internal entity-relative progression is not the reviewed external work bridge; a current BEC destination has not been adjudicated.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame.

**Overview**

Interest matching can select multiple behaviours while the entity progresses one input at a time. Each behaviour uses a fresh managed information context.

**Boundary**

Fan-out is not concurrent execution; clinical decisions and originating information remain with their owners.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-entity-specific-operational-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-e4e0e14897a45fa59d1117026c6321c2 | Realization | [Coordinate Entity-Relative Input](../../../../../../../continuum/business/business-function/78417d5c46cc406f95e6a41d22e3f143.md) | [[Feature] Entity-Relative Activity Coordination](../../../../../../../continuum/strategy/capability/feature/b9960d7de097464ea6de94929ca48c89.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Entity-Relative Operational Progression](../../../../../../../continuum/strategy/capability/69c82474fb534152b151c92f706197d0.md) — `69c82474fb534152b151c92f706197d0`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate serial, entity-relative operational inputs and their interested behaviours.
  Boundary: Interest fan-out is distinct from intra-entity concurrency; entity progression does not acquire originating facts.
  Independent adjudication: P1-183 (preserved source locator `05-current-unresolved-enterprise.md#p1-183`).
- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).

**Assessment and rationale:** OVERLAPPING. Entity-relative interested-behaviour progression and controlled recovery differ from the external work bridge. The EC progression capability is a plausible enabling responsibility, not proof of the same BEC identity. Serial entity progression, fan-out and uncertain effects remain separate. AX-14/15/16/18 apply.

**Proposed treatment:** ACCEPT entity-relative coordination and its separate progression/recovery Features; preserve identities and refrain from assigning a Twin component.

**Consequences if authorised:** Resolve BEC/EC abstraction, current CT2 frame and CT3-to-Feature containment explicitly; preserve halt/recovery dispositions and fresh information context.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/821`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-entity-relative-activity-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-690bcf58be875ffc83126a9d288b2df5"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md); recorded line 73; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## 3. Twin-relative execution

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-001"></a>
## P1-001 — [Feature] Governance Requirement Version Publication

Canonical identity: `001a067a4b3b46bebfe945f1bef541ce`; [governed source](../../../../../../../continuum/strategy/capability/feature/001a067a4b3b46bebfe945f1bef541ce.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** A current architectural Home is unestablished; do not choose a convenient governance or assurance grouping.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Requirement publication/authority is not assurance-service review or information-model design; the reviewed hierarchy does not allocate a home.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Publish attributable versions and applicability of externally approved governing requirements.

**Overview**

Operational and assurance consumers need to identify the governing basis they used rather than silently substitute a current rule for a historical one.

**Boundary**

Publication does not confer requirement approval, authorisation policy or assurance-criteria authority.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-governance-requirement-publication-support`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Policy / Obligation Governance](../../../../../../../continuum/strategy/capability/73c6e5b36bdf4bf0ad2b8b50c9b9a00b.md) — `73c6e5b36bdf4bf0ad2b8b50c9b9a00b`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Identify, interpret, establish and maintain authoritative legislative, regulatory, policy, standards, guideline, contractual and other obligations, attributable and traceable to source authority.
  Boundary: Interpreting obligations does not acquire the regulator's authority or determine operational means.
- [[CT2] Information Design Governance](../../../../../../../continuum/strategy/capability/f39ed9ebcc724857a4bf7a2b8e045c7a.md) — `f39ed9ebcc724857a4bf7a2b8e045c7a`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Information Design Governance maintains canonical definitions, semantic bindings, exchange profiles and the conformance rules applied to representations. It makes meaning and constraints available across regional information flows so that an accepted technical format is not mistaken for semantic correctness. Governance and verification remain distinguishable from the clinical facts represented.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[CT2] Assurance Specification](../../../../../../../continuum/strategy/capability/b81775d89d774cf8ba61731d13c67a54.md) — `b81775d89d774cf8ba61731d13c67a54`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.
  Boundary: Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.
  Independent adjudication: P1-322 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-322`).

**Assessment and rationale:** DISTINCT. Publication of attributable versions/applicability of externally approved requirements supports consumers. Authoring/approving obligations, information-model design and assurance-criteria specification answer different authority questions. Shared governance subject matter supplies no equivalent or parent. AX-06/14/17/18 apply.

**Proposed treatment:** ACCEPT the bounded publication concept and Feature without granting rule approval or policy ownership.

**Consequences if authorised:** SA must supply current CT2 placement and explicit CT3 framing; retain requirement-authority, permission-policy and assurance-criteria distinctions.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/831`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-governance-requirement-version-publication`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-012a49f6e1ce5f4094fd4870dfd281d4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-capabilities.md); recorded line 993; Current corpus evidence; does not promote the derived assertion. Source excerpt: #### 17. Health Service Governance
- [docs/markdown/03-business-architecture/behaviours/health-service-assurance.md](../../../../../../../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md); recorded line 9; Current corpus evidence; does not promote the derived assertion. Source excerpt: The approved human review decisions on 2026-10-08 extend the previous bounded derivation with **[Service Assurance Modeller](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-assurance-modeller)** and **[Service Guardian](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-guardian)** Business Roles and exactly five Business Functions: **Design Service Assurance**, **Manage Assurance Criteria**, **Establish Assurance Context**, **Assess Assurance Evidence** and **Adjudicate Assurance Assessment**. Their capability ownership and Role performance are documented in §3. The assurance Role previously named Guardian is now Service Guardian; legal/personal-welfare Guardian remains unchanged. These naming and Function-decomposition decisions are resolved; further decomposition is not approved.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-378"></a>
## P1-378 — [Feature] Governed Policy Assignment & Revocation

Canonical identity: `d6c271ae95ea44449c5e22665d1d9bc2`; [governed source](../../../../../../../continuum/strategy/capability/feature/d6c271ae95ea44449c5e22665d1d9bc2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide administration versus resolution and do not infer client-consent ownership from general permission administration.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation.

**Overview**

An assignment becoming inapplicable by expiry differs from an explicit revocation; both must affect the effective authority used for operations.

**Boundary**

Management does not grant unconstrained administrative authority or derive permission from resource tags.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-control`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md) — `7eca9fc4d2244b0facc83376e57ebbc4`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Resolve effective, temporally applicable permission criteria for an established identity and governed operation.
  Boundary: Does not authenticate identity or grant permission from caller-controlled tags or mere role names.
  Independent adjudication: P1-216 (preserved source locator `05-current-unresolved-enterprise.md#p1-216`).
- [[CT2] Policy & Control](../../../../../../../continuum/strategy/capability/0c5d986cc948421a8bebcab3b74d108f.md) — `0c5d986cc948421a8bebcab3b74d108f`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules.
- **Functional Responsibilities**:
  - Executing impartial, default-deny policy evaluations for all integration transactions.
  - Enforcing Role-Based and Attribute-Based Access Control (RBAC/ABAC) policies.
  - Evaluating patient consent directives and privacy restrictions at boundary and query checkpoints.
  - Enforcing statutory information disclosure boundaries (e.g., mental health, VIP records).
- **Collaborative Nature**: Evaluated across boundary gateways, internal bus dispatch, storage interfaces, and presentation layers.
  Boundary: Participation at multiple enforcement points does not distribute policy ownership or turn operational security context into persistent clinical content.
- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Assessment and rationale:** OVERLAPPING. Assignment, temporal applicability, expiry and explicit revocation concern effective permission administration. Policy resolution consumes that basis; patient-specific restrictions and client consent retain independent ownership. Shared policy subject matter does not make these the same responsibility. AX-07/14/18 apply.

**Proposed treatment:** ACCEPT bounded policy-assignment/revocation meaning while preserving source identity and each authority boundary.

**Consequences if authorised:** Resolve CT3 framing explicitly rather than treating the source CT2 owner as a legal Feature parent; retain expiry/revocation distinctions and Themis policy authority.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/816`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-governed-policy-assignment-revocation`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-c0ee6896b3d759a9980c2a79ad7c3e53"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md); recorded line 33; Current corpus evidence; does not promote the derived assertion. Source excerpt: Identity.Authentication is a **Separately Provided Capability (SEP)**. Harmonia's primary authentication pattern is standards-based OIDC/OAuth integration with an externally managed Identity Provider.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-099"></a>
## P1-099 — [Feature] Independent Evidence Assessment & Adjudication

Canonical identity: `3aedee8f78484b81955866bcf16b8a6d`; [governed source](../../../../../../../continuum/strategy/capability/feature/3aedee8f78484b81955866bcf16b8a6d.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide whether the historical CT2 grouping remains useful, and distinguish subject assurance, assurance-service review and existing macro Health Service Assurance.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical assurance against a governed subject is not independent review of the assurance service itself.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria.

**Overview**

The Feature unifies the review frame while retaining the three distinct canonical Functions and the boundary between criterion-level assessment and reportable conclusion.

**Boundary**

No new assurance Function or clinical-adequacy mandate is introduced; insufficient evidence establishes neither satisfaction nor non-satisfaction.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:governed-assurance`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-4cf297cf60b95bef85d6eafb87ba33ae | Realization | [Assess Assurance Evidence](../../../../../../../continuum/business/business-function/e1f6402d47c0455b95b45a1a4d393484.md) | [[Feature] Independent Evidence Assessment &amp; Adjudication](../../../../../../../continuum/strategy/capability/feature/3aedee8f78484b81955866bcf16b8a6d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-508333bcdf1e5e8bb1bbbbe77dccf43f | Realization | [Adjudicate Assurance Assessment](../../../../../../../continuum/business/business-function/7d7cd216415d4b839ea3edfbf1538110.md) | [[Feature] Independent Evidence Assessment &amp; Adjudication](../../../../../../../continuum/strategy/capability/feature/3aedee8f78484b81955866bcf16b8a6d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-bb0f1436adb458dab1dc9f945c502615 | Realization | [Establish Assurance Context](../../../../../../../continuum/business/business-function/ffffe352e28c4e878d986b0e15ddff15.md) | [[Feature] Independent Evidence Assessment &amp; Adjudication](../../../../../../../continuum/strategy/capability/feature/3aedee8f78484b81955866bcf16b8a6d.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Assurance Service Evaluation](../../../../../../../continuum/strategy/capability/039343518db542d48cde6afab84a3e1e.md) — `039343518db542d48cde6afab84a3e1e`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.
  Boundary: Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.
  Independent adjudication: P1-006 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-006`).
- [[CT2] Assurance Evaluation](../../../../../../../continuum/strategy/capability/d1b20e0fb91e4bd1950ad57b84e2cafd.md) — `d1b20e0fb91e4bd1950ad57b84e2cafd`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Determine what justified confidence the applicable evidence supports.
  Boundary: Insufficient evidence establishes neither assurance nor non-assurance. Operational response remains with the activity's responsible capability, and the existing assurance BEC ownership is unchanged.
- [[CT2] Assurance Service Review](../../../../../../../continuum/strategy/capability/b1a791458d644d4794895e4fa42ec83c.md) — `b1a791458d644d4794895e4fa42ec83c`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.
  Boundary: Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.
  Independent adjudication: P1-306 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-306`).

**Assessment and rationale:** OVERLAPPING. Assurance of a governed subject uses context, evidence assessment and adjudication. Review of the assurance service has a different subject; EC Assurance Evaluation enables reasoning about evidence without acquiring the BEC owner role. Existing approved Functions establish meaningful subject-assurance responsibility while classification and Feature decomposition remain qualified. Insufficiency is not failure or clinical judgement. AX-14/15/17/18 and DR-19 apply.

**Proposed treatment:** ACCEPT the subject-assurance concept; retain existing independent identities pending a coordinated CT2/CT3/Feature representation decision. Do not reinterpret it as recursive self-assurance.

**Consequences if authorised:** Resolve CT2 placement relative to Independent Assurance Enablement, CT3 relevance and the Feature decomposition without adding Functions or changing the approved assurance boundary.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/835`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-independent-evidence-assessment-adjudication`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-74297fcf6597537fb193578f0eefca8d"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/03-business-architecture/behaviours/health-service-assurance.md](../../../../../../../../docs/markdown/03-business-architecture/behaviours/health-service-assurance.md); recorded line 9; Current corpus evidence; does not promote the derived assertion. Source excerpt: The approved human review decisions on 2026-10-08 extend the previous bounded derivation with **[Service Assurance Modeller](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-assurance-modeller)** and **[Service Guardian](../../../../../../../../docs/markdown/03-business-architecture/actors-roles/roles.md#service-guardian)** Business Roles and exactly five Business Functions: **Design Service Assurance**, **Manage Assurance Criteria**, **Establish Assurance Context**, **Assess Assurance Evidence** and **Adjudicate Assurance Assessment**. Their capability ownership and Role performance are documented in §3. The assurance Role previously named Guardian is now Service Guardian; legal/personal-welfare Guardian remains unchanged. These naming and Function-decomposition decisions are resolved; further decomposition is not approved.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-191"></a>
## P1-191 — [Feature] Information Authority & Source Standing Representation

Canonical identity: `70a444156cd246cb97608a782e4bdea8`; [governed source](../../../../../../../continuum/strategy/capability/feature/70a444156cd246cb97608a782e4bdea8.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide BEC versus EC representation, not whether successful technical acceptance establishes credibility.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Source credibility and authority standing require an explicit home; information-asset tags alone do not establish that responsibility.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Attach and preserve qualified source-authority and credibility standing for managed assertions.

**Overview**

The user of an assertion needs to distinguish authoritative, informational and anecdotal standing even when each was technically valid and durably accepted.

**Boundary**

Authority classification is not a clinical truth judgement or a universal rule for choosing competing assertions.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-information-authority-representation`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-422618c4399b5a26aa3cee1db8934bca | Realization | [Qualify Information Authority Standing](../../../../../../../continuum/business/business-function/f5b57bbba47a4e86bd2f79f700d6977a.md) | [[Feature] Information Authority &amp; Source Standing Representation](../../../../../../../continuum/strategy/capability/feature/70a444156cd246cb97608a782e4bdea8.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Information Authority Qualification](../../../../../../../continuum/strategy/capability/cbba9984dd404deabed41fdcc96088b1.md) — `cbba9984dd404deabed41fdcc96088b1`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Represent and qualify the source-authority and credibility standing of information independently of transport, persistence and concurrency.
  Boundary: This is not independent service assurance, a universal conflict arbiter or clinical truth determination. Existing EC-13 governs authority nomenclature; this capability applies and preserves the qualified standing.
  Independent adjudication: P1-357 (preserved source locator `05-current-unresolved-enterprise.md#p1-357`).
- [[CT3] Information Asset Tag Management](../../../../../../../continuum/strategy/capability/df27c3ba6e164e56af7bff4e1eb1954a.md) — `df27c3ba6e164e56af7bff4e1eb1954a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Manage attributable tags describing information assets without treating caller-controlled tags as security authority.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-394 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-394`).
- [[CT3] Information Attribution & Lineage](../../../../../../../continuum/strategy/capability/8bc66c9bc1024a68a1bfd010b4dd1b79.md) — `8bc66c9bc1024a68a1bfd010b4dd1b79`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Preserve source attribution and significant transformation relationships for managed information.
  Boundary: Does not infer clinical credibility from a successful transport or establish independent assurance findings.
  Independent adjudication: P1-237 (preserved source locator `05-current-unresolved-enterprise.md#p1-237`).

**Assessment and rationale:** OVERLAPPING. Source credibility and authoritative/informational/anecdotal standing are required by AX-06 and independent of persistence, freshness, tags and lineage. The current proposed EC qualification responsibility is a strong enabling comparison; it is not adjudicated equivalence to this BEC representation. AX-05/06/14/17 apply.

**Proposed treatment:** ACCEPT explicit information-authority representation and its Feature; evaluate the BEC/EC distinction before retaining overlapping representations.

**Consequences if authorised:** Resolve the superseded stewardship parent, current CT2/CT3 frame and Feature containment; preserve policy-dependent use rather than a universal winner rule.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/811`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-information-authority-source-standing-representation`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-15b2dbb248135673b3bfa3dbe53a03fc"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 303; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### AX-06 --- Information Authority Is Explicit

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-320"></a>
## P1-320 — [Feature] Mandatory Significant Activity Audit Capture

Canonical identity: `b69936757c4446a7a1b57a455aae7a5f`; [governed source](../../../../../../../continuum/strategy/capability/feature/b69936757c4446a7a1b57a455aae7a5f.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Resolve BEC/EC representation and audit/provenance/assurance boundaries; no generalised authority or capture bypass follows.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: The reviewed hierarchy does not allocate general mandatory authoritative audit capture; disclosure or provenance alone is not an equivalent responsibility.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary.

**Overview**

The actor whose action is recorded need not be entitled to persist the audit. Required capture uses its own established authority and failure can block the governed operation.

**Boundary**

Specific audit authority is not borrowed from the audited identity or generalised to other operations.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-significant-activity-audit-capture`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-8418ccf814935bcbbd9588a4aa1cfbda | Realization | [Capture Required Significant Audit Evidence](../../../../../../../continuum/business/business-function/c8e614257cf14bd1878df894455f21fb.md) | [[Feature] Mandatory Significant Activity Audit Capture](../../../../../../../continuum/strategy/capability/feature/b69936757c4446a7a1b57a455aae7a5f.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Governed Activity Audit Evidence](../../../../../../../continuum/strategy/capability/c8d93ddc5aa84960964b78345fff1b29.md) — `c8d93ddc5aa84960964b78345fff1b29`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture required significant activity and decision evidence under its applicable governed capture authority.
  Boundary: Mandatory capture authority is specific to the established audit responsibility, not a platform-wide permission shortcut.
  Independent adjudication: P1-351 (preserved source locator `05-current-unresolved-enterprise.md#p1-351`).
- [[CT2] Provenance & Traceability](../../../../../../../continuum/strategy/capability/68a0755aa4e14a509ac5e6c0bbcff3e7.md) — `68a0755aa4e14a509ac5e6c0bbcff3e7`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Preserve required origin attribution, meaningful transformation history, significant execution checkpoints, tamper-evident audit evidence and end-to-end traceability. Harmonia SHALL preserve provenance, audit and operational evidence where required by governing obligations, architectural significance, accountability, reconstruction, security or assurance needs. Routine technical activity SHALL NOT automatically require durable evidentiary recording.
- **Functional Responsibilities**:
  - Recording attributable, verifiable origin metadata for information sources, authors, and timestamps.
  - Tracking information-significant transformation history and required mapping checkpoints as information crosses system seams.
  - Capturing non-PHI operational and security audit trails for regulatory compliance (HIPAA, GDPR).
  - Supporting end-to-end transaction tracing across asynchronous processing pipelines.
- **Collaborative Nature**: Participating elements preserve required provenance and audit context according to policy. Telemetry, operational facts, provenance, audit and assurance evidence remain distinct. Being evidence is contextual, not an intrinsic property of every recorded fact; preservation does not establish an assurance conclusion.
  Boundary: Telemetry, provenance, audit and assurance evidence remain distinct. Recording evidence neither establishes an assurance conclusion nor automatically requires retention of routine technical activity.
- [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) — `44a7937520a54945b06b5fd4a2bbd870`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.
  Boundary: Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.
  Independent adjudication: P1-114 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-114`).

**Assessment and rationale:** OVERLAPPING. Mandatory authoritative activity-audit capture is explicitly required by AX-07. EC audit evidence overlaps its enabling purpose; data provenance preserves information origin/transformation and does not replace activity audit. Neither generic traceability nor disclosure supplies an adjudicated BEC identity. AX-07/08/14 apply; DR-18/19 boundaries remain controlling.

**Proposed treatment:** ACCEPT the mandatory-capture concept and its bounded Feature; assess whether the separate BEC representation adds a responsibility beyond EC audit evidence.

**Consequences if authorised:** Select a current BEC CT2/CT3 frame or explicitly reconcile representation after SA decision. Preserve Kleio intrinsic capture authority, Themis policy evaluation, non-PHI diagnostics and independence from the audited identity.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/813`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-mandatory-significant-activity-audit-capture`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-a07c25a127ca5917984623d75092c14d"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/governance/architectural-axioms.md](../../../../../../../../docs/markdown/governance/architectural-axioms.md); recorded line 379; Current corpus evidence; does not promote the derived assertion. Source excerpt: #### Kleio mandatory authoritative audit capture

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-013"></a>
## P1-013 — [Feature] Non-PHI Compliance Auditing

Canonical identity: `08e37326908a49988dfb787f68df840a`; [governed source](../../../../../../../continuum/strategy/capability/feature/08e37326908a49988dfb787f68df840a.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Do not assume equivalence to Mandatory Significant Activity Audit Capture or treat provenance as an audit replacement.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Generate tamper-evident, verifiable audit records of security-significant events without emitting unmasked PHI.

---

## History

### Feature Overview — predecessor explanation

Security-significant audit evidence makes governed actions attributable and verifiable while respecting PHI restrictions. The record concerns meaningful security facts under policy rather than routine implementation events, and recording it does not independently establish an assurance conclusion.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-control`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-b579f1cd09505e089e91222ca8a1690b | Realization | [Record Security-Significant Activity](../../../../../../../continuum/business/business-function/6177ed0d53e5407ab1bc4b721619ed44.md) | [[Feature] Non-PHI Compliance Auditing](../../../../../../../continuum/strategy/capability/feature/08e37326908a49988dfb787f68df840a.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-c087df1d9a975e47b50cdd3b6eb3acc0 | Association | [Security Audit Ingress](../../../../../../../continuum/business/business-service/43c4a2aa7b4548ec8fdc2844e3d666b6.md) | [[Feature] Non-PHI Compliance Auditing](../../../../../../../continuum/strategy/capability/feature/08e37326908a49988dfb787f68df840a.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Governed Activity Audit Evidence](../../../../../../../continuum/strategy/capability/c8d93ddc5aa84960964b78345fff1b29.md) — `c8d93ddc5aa84960964b78345fff1b29`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture required significant activity and decision evidence under its applicable governed capture authority.
  Boundary: Mandatory capture authority is specific to the established audit responsibility, not a platform-wide permission shortcut.
  Independent adjudication: P1-351 (preserved source locator `05-current-unresolved-enterprise.md#p1-351`).
- [[Feature] Mandatory Significant Activity Audit Capture](../../../../../../../continuum/strategy/capability/feature/b69936757c4446a7a1b57a455aae7a5f.md) — `b69936757c4446a7a1b57a455aae7a5f`; {"semantic": "Proposed", "currentness": "pending_adjudication", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary.
  Boundary: Specific audit authority is not borrowed from the audited identity or generalised to other operations.
  Independent adjudication: P1-320 (preserved source locator `02-pending-features.md#p1-320`).
- [[CT3] Data Provenance Capture](../../../../../../../continuum/strategy/capability/44a7937520a54945b06b5fd4a2bbd870.md) — `44a7937520a54945b06b5fd4a2bbd870`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.
  Boundary: Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.
  Independent adjudication: P1-114 (preserved source locator `05-current-unresolved-business_enabling-02.md#p1-114`).

**Assessment and rationale:** OVERLAPPING. Tamper-evident non-PHI security-event records overlap mandatory audit capture but the mandatory Feature additionally specifies capture authority independent of the audited actor. Content lineage and assurance findings remain separate. AX-07/08/14 and DR-18/19 apply.

**Proposed treatment:** ACCEPT security-audit meaning; ask SA whether the two audit Features are complementary constraints or duplicate representations before consolidation.

**Consequences if authorised:** Establish a lawful CT3 frame and decide audit Feature granularity; preserve non-PHI diagnostic constraints, meaningful evidence and mandatory capture authority.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/386`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:non-phi-compliance-auditing`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-134a61003723533683cb931b9231dd3f"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `5-health-information-control-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-103"></a>
## P1-103 — [Feature] Operation-Bound Authorisation Enforcement

Canonical identity: `3b7dcee7d58d4d1c960dc92abd58b4c3`; [governed source](../../../../../../../continuum/strategy/capability/feature/3b7dcee7d58d4d1c960dc92abd58b4c3.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** The source currently frames this Feature at CT2 Health Information Control; CT2 is not an authorised CT3-to-Feature frame.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Apply effective policy at the governed action boundary and reject actions lacking sufficient authority.

**Overview**

Consistent enforcement is required across mutation, access, execution and administration rather than relying on each caller to voluntarily invoke a check.

**Boundary**

Consumes trusted identity and criteria; it does not authenticate or establish clinical privilege from directory roles.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-control`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Operation-Bound Policy Enforcement](../../../../../../../continuum/strategy/capability/cb32b32c657445b3b9b27bebc069ace9.md) — `cb32b32c657445b3b9b27bebc069ace9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enforce resolved policy constraints at the boundary where a governed action is attempted.
  Boundary: Enforcement participation does not distribute policy ownership or make security context persistent content.
  Independent adjudication: P1-356 (preserved source locator `05-current-unresolved-enterprise.md#p1-356`).
- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.
- [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md) — `7eca9fc4d2244b0facc83376e57ebbc4`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Resolve effective, temporally applicable permission criteria for an established identity and governed operation.
  Boundary: Does not authenticate identity or grant permission from caller-controlled tags or mere role names.
  Independent adjudication: P1-216 (preserved source locator `05-current-unresolved-enterprise.md#p1-216`).

**Assessment and rationale:** OVERLAPPING. AX-07 requires enforcement at each governed operation. The EC enforcement ability is a strong enabling comparison; patient-specific restriction/disclosure control is narrower, while policy resolution determines the applicable decision. No patient CT3 frame follows from those overlaps.

**Proposed treatment:** ACCEPT operation-bound enforcement as a Feature; retain its existing identity without inventing a patient-specific owner.

**Consequences if authorised:** SA must decide whether to frame the Feature under the existing proposed EC enforcement CT3 or another explicitly governed CT3; preserve Themis policy ownership and separate resolution/enforcement.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/817`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-operation-bound-authorisation-enforcement`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-edcdc7b12cc25092aee8b7c197674755"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md); recorded line 33; Current corpus evidence; does not promote the derived assertion. Source excerpt: Identity.Authentication is a **Separately Provided Capability (SEP)**. Harmonia's primary authentication pattern is standards-based OIDC/OAuth integration with an externally managed Identity Provider.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-143"></a>
## P1-143 — [Feature] Operational Activity Context Definition

Canonical identity: `58dcd59389664b7d83d14b536d7d03d0`; [governed source](../../../../../../../continuum/strategy/capability/feature/58dcd59389664b7d83d14b536d7d03d0.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** The old temporal-enablement parent is superseded; resolve abstraction and placement together.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Internal execution-envelope design is distinct from governance of work across an external execution boundary.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Define the anticipated information context and expected fulfilment basis of an operational undertaking.

**Overview**

Even a one-unit activity needs a declared information and outcome frame. The definition is separate from loading values or running the business logic.

**Boundary**

No dynamically invented resource set or orchestration engine is implied.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-governed-operational-activity-design`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-61dad9256b7357bcb6ca2f0f6f9f5e89 | Realization | [Maintain Operational Fulfilment Definition](../../../../../../../continuum/business/business-function/9f7085926221436bb672e52e59bca512.md) | [[Feature] Operational Activity Context Definition](../../../../../../../continuum/strategy/capability/feature/58dcd59389664b7d83d14b536d7d03d0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Operational Fulfilment Definition](../../../../../../../continuum/strategy/capability/2b52c0de17b146da962f303e99f90a4a.md) — `2b52c0de17b146da962f303e99f90a4a`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Define the context requirements, expected outcomes and housekeeping of an operational undertaking.
  Boundary: A fulfilment definition does not execute, orchestrate or choreograph its activity.
  Independent adjudication: P1-068 (preserved source locator `05-current-unresolved-enterprise.md#p1-068`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. The proposed BEC activity envelope specifies context, expected fulfilment and continuation; the EC definition supplies reusable operational machinery, while Work Boundary Governance concerns another system undertaking work. Similar fulfilment terms do not establish the same architectural responsibility. AX-04/14/18 apply.

**Proposed treatment:** ACCEPT the bounded activity-design concept and two distinct Features, subject to reconciliation of BEC versus EC abstraction.

**Consequences if authorised:** SA must decide whether BEC design is independently required or represented through existing EC enablement; establish current CT2 and explicit CT3 frames if retained. Do not allocate an orchestrator or dynamic engine.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/823`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-operational-activity-context-definition`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-25d8c4b3871058aa99f090a7ee1e2231"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/context-and-persistence-management.md](../../../../../../../../docs/markdown/05-information-systems-architecture/context-and-persistence-management.md); recorded line 52; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## Context — versioned temporal values
- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Digital-Twin-Behaviour-and-Execution-Semantics.md); recorded line 73; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## 3. Twin-relative execution

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-066"></a>
## P1-066 — [Feature] Security Context Propagation

Canonical identity: `2b071c4fb10c4f61afecd18d09387214`; [governed source](../../../../../../../continuum/strategy/capability/feature/2b071c4fb10c4f61afecd18d09387214.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** The CT2 source owner is insufficient Feature framing, and topic overlap cannot establish Function association.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Propagate tamper-evident, attributable security context across asynchronous processing boundaries.

---

## History

### Feature Overview — predecessor explanation

Security-context propagation carries attributable operational authority across asynchronous boundaries. Its responsibility is preservation of established context, distinct from evaluating a consent constraint; shared security subject matter is insufficient to assign that Function to this Feature.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-control`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Operation-Bound Policy Enforcement](../../../../../../../continuum/strategy/capability/cb32b32c657445b3b9b27bebc069ace9.md) — `cb32b32c657445b3b9b27bebc069ace9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enforce resolved policy constraints at the boundary where a governed action is attempted.
  Boundary: Enforcement participation does not distribute policy ownership or make security context persistent content.
  Independent adjudication: P1-356 (preserved source locator `05-current-unresolved-enterprise.md#p1-356`).
- [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md) — `7eca9fc4d2244b0facc83376e57ebbc4`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Resolve effective, temporally applicable permission criteria for an established identity and governed operation.
  Boundary: Does not authenticate identity or grant permission from caller-controlled tags or mere role names.
  Independent adjudication: P1-216 (preserved source locator `05-current-unresolved-enterprise.md#p1-216`).
- [[CT2] Policy & Control](../../../../../../../continuum/strategy/capability/0c5d986cc948421a8bebcab3b74d108f.md) — `0c5d986cc948421a8bebcab3b74d108f`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: - **Architectural Scope**: Evaluate and enforce security, consent, privacy, and regulatory policies including authority, access control, and information handling rules.
- **Functional Responsibilities**:
  - Executing impartial, default-deny policy evaluations for all integration transactions.
  - Enforcing Role-Based and Attribute-Based Access Control (RBAC/ABAC) policies.
  - Evaluating patient consent directives and privacy restrictions at boundary and query checkpoints.
  - Enforcing statutory information disclosure boundaries (e.g., mental health, VIP records).
- **Collaborative Nature**: Evaluated across boundary gateways, internal bus dispatch, storage interfaces, and presentation layers.
  Boundary: Participation at multiple enforcement points does not distribute policy ownership or turn operational security context into persistent clinical content.

**Assessment and rationale:** DISTINCT. Propagation preserves established tamper-evident, attributable security context across asynchronous boundaries. It does not evaluate consent or resolve the effective policy decision. AX-07 distinguishes operational context from persistent information; AX-14/16 preserve that boundary.

**Proposed treatment:** ACCEPT security-context propagation with its own existing Feature identity; preserve the explicit absence of an inferred consent Function association.

**Consequences if authorised:** Resolve lawful CT3 framing and independently review any Realization/Home change; no automatic persistent security-context payload or policy ownership transfer.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/385`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:security-context-propagation`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-7e3672f150a85990ba602078b3393205"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `5-health-information-control-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-123"></a>
## P1-123 — [Feature] Service Readiness Dependency Assessment

Canonical identity: `4d917929d5cf43218049a84cf46662b0`; [governed source](../../../../../../../continuum/strategy/capability/feature/4d917929d5cf43218049a84cf46662b0.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** SA must determine semantic equivalence versus layered enablement and who may establish readiness; dependency tracking alone supplies neither decision.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: source proposal determines collective readiness; reviewed scope establishes dependency tracking only. Evidence may be reused, but determination authority is not silently transferred.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering.

**Overview**

An offering may be in operating hours but not deliverable because staffing, equipment, materials or supporting services are missing. A readiness view retains insufficiency and the owners of those dependency assertions.

**Boundary**

Readiness evidence does not originate clinical clearance or transfer resource management.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `proposal:bec-service-offering-readiness-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-8ef442d225e55f00b4eb94244a6b9d47 | Realization | [Assess Service Offering Readiness](../../../../../../../continuum/business/business-function/a7eb76c1ff5b450b9835ac19662c8148.md) | [[Feature] Service Readiness Dependency Assessment](../../../../../../../continuum/strategy/capability/feature/4d917929d5cf43218049a84cf46662b0.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Health Service Operational Readiness](../../../../../../../continuum/strategy/capability/cc91eb4017034939ab82b152bddf7b1c.md) — `cc91eb4017034939ab82b152bddf7b1c`; {"semantic": "Candidate", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Candidate", "ancestry_status": "proposed"}. Definition: Definition unresolved in the baseline. No definition has been synthesised from the name.
  Boundary: Tier, parent and CT1 ancestry remain unresolved. Site-level and session-level exercises do not establish separate capabilities, and no convenient parent is allocated here.
  Independent adjudication: P1-359 (preserved source locator `05-current-unresolved-business.md#p1-359`).
- [[CT1] Clinical Service Dependency Management](../../../../../../../continuum/strategy/capability/232db9b8b47c450cb0aa9acaf539c09d.md) — `232db9b8b47c450cb0aa9acaf539c09d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-048 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-048`).
- [[CT2] Service Delivery Preparedness Management](../../../../../../../continuum/strategy/capability/9665d894c4c9438999d8cc10afae695d.md) — `9665d894c4c9438999d8cc10afae695d`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Establish whether service offerings can be delivered in their intended operating contexts through the combined standing of their dependencies.
  Boundary: Does not own the dependencies, their management or clinical execution.
  Independent adjudication: P1-260 (preserved source locator `05-current-unresolved-business.md#p1-260`).

**Assessment and rationale:** OVERLAPPING. Collective, context-qualified deliverability is broader than dependency tracking, operating hours, an empty bed or aggregate capacity. The current Candidate readiness concept is a serious equivalence candidate across BC/BEC layers, but authority to determine readiness is not established by that similarity. AX-14/17/18 apply.

**Proposed treatment:** ACCEPT collective readiness as a bounded concept; coordinate all existing readiness identities and distinguish evidence assembly from determination.

**Consequences if authorised:** Resolve BC/BEC responsibilities and decision authority before selecting current CT2 and CT3-to-Feature containment. No new site/session identities or readiness engine.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/827`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-service-readiness-dependency-assessment`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-eea28ef3bab358869d143da0c838865a"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/02-strategy/capabilities/business-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-capabilities.md); recorded line 403; Current corpus evidence; does not promote the derived assertion. Source excerpt: **Boundary:** Available staff, equipment or an unoccupied bed does not establish usable service capacity. Available for service coverage ≠ deployed to a care requirement ≠ assigned operational work. Care Resource Deployment consumes availability, role, qualification, engagement and privilege context without changing authoritative rosters, qualifications, employment or privilege records. It establishes no Harmonia authority over clinical work, enterprise assets or hospital-wide logistics. Operational capacity is distinct from strategic/tactical capacity planning and from collective service readiness (OI-BC-002).
- [docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md](../../../../../../../../docs/markdown/03-business-architecture/information-responsibility/information-responsibility.md); recorded line 34; Current corpus evidence; does not promote the derived assertion. Source excerpt: [Context-specific Capacity Management](../../../../../../../../docs/markdown/03-business-architecture/behaviours/04-health-service-operations.md#capacity-management-responsibility-boundary) establishes/manages capacity meaning for Clinic & Practice Operations, Ward Operations, Theatre Operations, Emergency Department Operations, Outpatient Operations and Mobile Staff Management. Their local information responsibilities express available, committed and utilised capacity without prescribing a shared formal Information Object or identical model. Bed/care-place and equipment state contribute; they do not alone establish service capacity. Service Capacity Management consumes/coordinates those capacity states; Work Allocation & Dispatch and Discharge Management may consume them without acquiring their ownership. Exact provider contracts remain unestablished.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-078"></a>
## P1-078 — [Feature] Standards-Based Boundary Exchange

Canonical identity: `3192ba1541ff41458d176184119c30bc`; [governed source](../../../../../../../continuum/strategy/capability/feature/3192ba1541ff41458d176184119c30bc.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Boundary protocol mediation has no adjudicated current BEC parent; determine its relation to Exchange Enablement without assuming equivalence.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical boundary protocol mediation and technical transmission are distinct from reviewed patient-facing communication.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Provide standards-conformant boundary exchange for healthcare messaging and resource representations.

---

## History

### Feature Overview — predecessor explanation

Boundary exchange preserves the standards contract understood by the external participant. Harmonia's internal management metadata must not leak into that contract, and publication must construct an allowed external representation without destructively changing the internally managed information.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-communication`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-571b9c6045b05bd682516a79de253fe0 | Realization | [Mediate Standards-Based Health Information Communication](../../../../../../../continuum/business/business-function/6c14589215f744a2b76605a2023b04fb.md) | [[Feature] Standards-Based Boundary Exchange](../../../../../../../continuum/strategy/capability/feature/3192ba1541ff41458d176184119c30bc.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-eff76991fd7452e69d54ea26b0d8d536 | Association | [Standards Communication Gateway Service](../../../../../../../continuum/business/business-service/80e239faf1bd476e973eb602b3730380.md) | [[Feature] Standards-Based Boundary Exchange](../../../../../../../continuum/strategy/capability/feature/3192ba1541ff41458d176184119c30bc.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Health Information Communication](../../../../../../../continuum/strategy/capability/7925605bf60f4b288e826020409a09d9.md) — `7925605bf60f4b288e826020409a09d9`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-207 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-207`).
- [[CT2] Health Information Exchange Enablement](../../../../../../../continuum/strategy/capability/a61a15908ed544e6acf3e9cab63a0267.md) — `a61a15908ed544e6acf3e9cab63a0267`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: As a BEC, Health Information Exchange provides reusable information-movement enablement through submission, retrieval mediation, addressed distribution and syndication. The models distinguish sending to a participant, requesting information, deliberately distributing to named recipients and publishing changes to matching subscribers. Their common responsibility is the exchange context, including routing and delivery state, rather than the business meaning of every payload.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity.
- [[Feature] Exchange Profile Governance](../../../../../../../continuum/strategy/capability/feature/5e59d8f2e23e4fb5b59a5bd1dd3f31fa.md) — `5e59d8f2e23e4fb5b59a5bd1dd3f31fa`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern the semantic and conformance constraints of an interoperability profile.
  Boundary: Profile governance does not transmit messages or publish internal operational metadata.
  Independent adjudication: P1-158 (preserved source locator `05-current-unresolved-features.md#p1-158`).

**Assessment and rationale:** DISTINCT. The historical responsibility mediates standards-facing protocols/technical transmission; the current CT2 with the same communication label covers patient notifications, tasks, self-service and library access. Exchange-profile governance defines a contract rather than performing transmission. AX-02/13/14 preserve these distinctions.

**Proposed treatment:** ACCEPT the boundary-exchange concept under the existing identity; retain the Feature separately from patient-facing communication. No same-name merge.

**Consequences if authorised:** Resolve the absent CT3 definition from the preserved historical explanation only after SA review; select a lawful CT2 frame and establish any CT3-to-Feature Composition explicitly. Preserve separation from profile governance and clinical message purpose.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/383`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:standards-based-boundary-exchange`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-0a5c88a49f2a518ebcf9b6f0315c092d"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `4-health-information-communication-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-213"></a>
## P1-213 — [Feature] Synthetic Task Orchestration

Canonical identity: `7cf9dd3aeeb84a83b2445f897db9bd4e`; [governed source](../../../../../../../continuum/strategy/capability/feature/7cf9dd3aeeb84a83b2445f897db9bd4e.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Execute non-human, automated platform activity units and sequence orchestration.

---

## History

### Feature Overview — predecessor explanation

Synthetic Task orchestration governs non-human executable work and its sequencing. This technical work meaning remains distinct from human Work Orders, clinical worklists and external systems' generic task terminology; incomplete stalled/failed Process paths are not supplied by convention.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-8b02917dd6d85f19a71a657d0ffb5f85 | Association | [Synthetic Task Execution Service](../../../../../../../continuum/business/business-service/57317da133ba4e29bde3997921f92e2e.md) | [[Feature] Synthetic Task Orchestration](../../../../../../../continuum/strategy/capability/feature/7cf9dd3aeeb84a83b2445f897db9bd4e.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |
| id-f85b4f4908a958a8a3dec5d3a7b3a054 | Realization | [Coordinate Synthetic Task](../../../../../../../continuum/business/business-function/6cb88c7e689648b1bd0a4d1e26b12f9b.md) | [[Feature] Synthetic Task Orchestration](../../../../../../../continuum/strategy/capability/feature/7cf9dd3aeeb84a83b2445f897db9bd4e.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/395`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:synthetic-task-orchestration`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-a230f00f87e65345adb868f557cee471"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `9-workflow--activity-coordination-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-094"></a>
## P1-094 — [Feature] To Do Decision & Approval Coordination

Canonical identity: `377a40e8f2d64840a10dd55f87a9d005`; [governed source](../../../../../../../continuum/strategy/capability/feature/377a40e8f2d64840a10dd55f87a9d005.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Coordinate human clinical review items, sign-offs, and approval workflows.

---

## History

### Feature Overview — predecessor explanation

To Do coordination supports human review, sign-off and decision activity. Its progression can expose whether the review item has been handled while leaving the meaning and authority of the professional or governance decision with the responsible participant.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-1b6d7bc73ea350e7b87e0b41c1f01534 | Realization | [Coordinate To Do](../../../../../../../continuum/business/business-function/a4c9a281578d493eb876eda9fb73cac1.md) | [[Feature] To Do Decision &amp; Approval Coordination](../../../../../../../continuum/strategy/capability/feature/377a40e8f2d64840a10dd55f87a9d005.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-99c5b1c5c57f5aca9632989fab446224 | Association | [To Do Management Service](../../../../../../../continuum/business/business-service/6bc69c5517024c43b1ab701aac5e39b7.md) | [[Feature] To Do Decision &amp; Approval Coordination](../../../../../../../continuum/strategy/capability/feature/377a40e8f2d64840a10dd55f87a9d005.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/394`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:to-do-decision--approval-coordination`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-522c663fad0151a597da85ce9ef313b7"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `9-workflow--activity-coordination-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-075"></a>
## P1-075 — [Feature] Trusted Identity Context Intake

Canonical identity: `2ef8f67907c34cb5a6a37fe7181434e2`; [governed source](../../../../../../../continuum/strategy/capability/feature/2ef8f67907c34cb5a6a37fe7181434e2.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Resolve WHO versus WHAT and legal Feature containment; current source CT2 Health Information Control supplies no CT3 frame.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: general platform security/evidence Feature has no adjudicated patient-specific CT3 frame; established Health Information Control responsibility is preserved.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Accept and qualify externally authenticated identity context for Harmonia-governed operations.

**Overview**

The boundary between WHO and WHAT remains explicit: an external identity assertion is verified as trusted input before internal authorisation is resolved.

**Boundary**

Harmonia does not become an authentication or credential-issuance provider.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:health-information-control`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  / . This is reading organisation, not semantic containment.

No explicit incident native relationship is recorded. Source prose, naming or navigation does not supply missing Composition, Aggregation, Realization or Home.

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT3] Identity Identification & Authentication](../../../../../../../continuum/strategy/capability/ba4ad62548984d63ad75da3561566431.md) — `ba4ad62548984d63ad75da3561566431`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Established", "structural_id_status": "Established", "name_status": "Canonical", "ancestry_status": "established"}. Definition: Identify an actor and authenticate the asserted identity.
  Boundary: Authentication neither establishes every required identity-quality condition nor authorises the requested activity.
- [[CT3] Operation Policy Resolution](../../../../../../../continuum/strategy/capability/7eca9fc4d2244b0facc83376e57ebbc4.md) — `7eca9fc4d2244b0facc83376e57ebbc4`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Resolve effective, temporally applicable permission criteria for an established identity and governed operation.
  Boundary: Does not authenticate identity or grant permission from caller-controlled tags or mere role names.
  Independent adjudication: P1-216 (preserved source locator `05-current-unresolved-enterprise.md#p1-216`).
- [[CT2] Health Information Control](../../../../../../../continuum/strategy/capability/44cc0e17ad534675bf26dce6e46a68ff.md) — `44cc0e17ad534675bf26dce6e46a68ff`; {"semantic": "Canonical", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Canonical", "ancestry_status": "proposed"}. Definition: Govern patient health-information restrictions, filtering and disclosure using established information-access authority, client privacy/consent determinations and applicable security controls.
  Boundary: Information administration does not confer authority over the represented person, organisation, service, place, device or clinical activity. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Assessment and rationale:** DISTINCT. Trusted intake verifies an externally authenticated identity context; deciding what operations it may perform and administering patient-information restrictions answer different questions. The broader identity/authentication EC responsibility does not authorise a new Harmonia authentication issuer. AX-07/14/18 apply.

**Proposed treatment:** ACCEPT trusted identity-context intake as the bounded Feature while preserving external authentication authority.

**Consequences if authorised:** Select an explicit CT3 frame consistent with trusted intake; do not broaden the Feature into credential issuance or professional privilege determination.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/815`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-trusted-identity-context-intake`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-9caab2bbf6365d30999ddb7592dfc543"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md](../../../../../../../../docs/markdown/05-information-systems-architecture/design-notes/DesignNote-Identity-Authentication-and-Authorisation-Architecture.md); recorded line 33; Current corpus evidence; does not promote the derived assertion. Source excerpt: Identity.Authentication is a **Separately Provided Capability (SEP)**. Harmonia's primary authentication pattern is standards-based OIDC/OAuth integration with an externally managed Identity Provider.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-418"></a>
## P1-418 — [Feature] Uncertain Activity Outcome Reconciliation

Canonical identity: `eb06ecc956404d0397701ceafb5fbd46`; [governed source](../../../../../../../continuum/strategy/capability/feature/eb06ecc956404d0397701ceafb5fbd46.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition.

**Overview**

A timeout or absent response leaves a materially significant outcome unknown. Reconciliation can confirm an effect, establish absence or retain uncertainty without automatically retrying the original work.

**Boundary**

Execution uncertainty differs from insufficient assurance evidence; a retry does not prove the earlier attempt had no effect.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-96cc0004be6b591a9cffb4f8a24726c0 | Realization | [Reconcile Indeterminate Activity Effects](../../../../../../../continuum/business/business-function/eace49fe7b304ad786d460ccef622378.md) | [[Feature] Uncertain Activity Outcome Reconciliation](../../../../../../../continuum/strategy/capability/feature/eb06ecc956404d0397701ceafb5fbd46.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-e4f54b04acbe5729800dccbf730a7feb | Realization | [Determine Reconciled Activity Continuation](../../../../../../../continuum/business/business-function/2e326eb5a06641048ad8a919723c4c51.md) | [[Feature] Uncertain Activity Outcome Reconciliation](../../../../../../../continuum/strategy/capability/feature/eb06ecc956404d0397701ceafb5fbd46.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-ee24644ffb8557fb80bbf76756b8e119 | Realization | [Establish Activity Effect Observation Basis](../../../../../../../continuum/business/business-function/4038dd6a1baa4856afc76d7adfa6a457.md) | [[Feature] Uncertain Activity Outcome Reconciliation](../../../../../../../continuum/strategy/capability/feature/eb06ecc956404d0397701ceafb5fbd46.md) | {"v0.2 / Assertion Status": "Proposed", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/825`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `proposal:feature-uncertain-activity-outcome-reconciliation`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-00706dca11295d44b01a89f0e66c83d2"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.

**Original synthesis source qualification:** Proposed only; cited source standing is not inherited; Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence.

- [docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md](../../../../../../../../docs/markdown/01-motivation/requirements-constraints/foundational-requirements.md); recorded line 76; Current corpus evidence; does not promote the derived assertion. Source excerpt: ## REQ-FND-004: Explicit Indeterminate Outcome
- [docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md](../../../../../../../../docs/markdown/03-business-architecture/behaviours/05-intrinsic-enablement.md); recorded line 170; Current corpus evidence; does not promote the derived assertion. Source excerpt: ### 2.9 Workflow & Activity Coordination

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-156"></a>
## P1-156 — [Feature] Work Order Progression

Canonical identity: `5ca494e85869419c867618fd9648d03a`; [governed source](../../../../../../../continuum/strategy/capability/feature/5ca494e85869419c867618fd9648d03a.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Manage execution states and dispatch of human operational work orders.

---

## History

### Feature Overview — predecessor explanation

Work Order progression coordinates human operational doing as a distinct work meaning. Dispatch, acceptance and completion describe the undertaking without making the workflow engine the authority over clinical work or treating a receipt as performance.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-271b374d07c452c08f997d774c8db130 | Association | [Work Order Coordination Service](../../../../../../../continuum/business/business-service/8a1987ed11d44598b325359287e503f0.md) | [[Feature] Work Order Progression](../../../../../../../continuum/strategy/capability/feature/5ca494e85869419c867618fd9648d03a.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |
| id-53a920b773fe5d9da4aea4487274f53b | Realization | [Coordinate Work Order](../../../../../../../continuum/business/business-function/0161308bad6945b5bcfdeea5519341cd.md) | [[Feature] Work Order Progression](../../../../../../../continuum/strategy/capability/feature/5ca494e85869419c867618fd9648d03a.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/393`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:work-order-progression`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-c8c5184814d85707844e56c072d9459c"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `9-workflow--activity-coordination-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

<a id="p1-421"></a>
## P1-421 — [Feature] Workflow Timeout & Escalation Oversight

Canonical identity: `ec55d06e393e4988a84c6157b719bd40`; [governed source](../../../../../../../continuum/strategy/capability/feature/ec55d06e393e4988a84c6157b719bd40.md).

**Recommendation:** ACCEPT. **SA decision:** PENDING. **Incorporation:** NOT AUTHORISED.

**SA judgement required:** Decide internal versus external scope and source contribution treatment before changing parentage, Home, Realization or coverage.

**Preserved originating unresolved note:** HUMAN ADJUDICATION REQUIRED: Canonical internal Work Order, To Do and synthetic Task progression is broader than the reviewed external execution-boundary bridge.

### Existing definition and architectural explanation

The following is retained source wording, with links rebased only for navigation. Its original qualifications remain binding.

Supervise operational deadlines, executing automated timeout notifications and supervisory escalations.

---

## History

### Feature Overview — predecessor explanation

Timeout oversight observes deadlines and initiates the established coordination response to stalled activity. It is management of progression rather than an assurance conclusion, and escalation does not grant the coordinator authority over the clinical meaning of the underlying work.

### Classification, source framing and actual relationships

Source category: `not recorded in historical reference`; source tier: `FT`; parent key: `None`; owner key: `capability:workflow--activity-coordination`. These source keys are provenance locators, not canonical IDs or newly asserted parent relationships.

Navigation locator: Strategy / Capabilities / Business Enabling Capabilities /  /  /  / . This is reading organisation, not semantic containment.

| Relationship ID | Type / specialisation | Source | Target | Standing |
| --- | --- | --- | --- | --- |
| id-342fc89d07dd510b8a1fbd6cb7e32326 | Realization | [Supervise Activity Timeout / Escalation](../../../../../../../continuum/business/business-function/cbf6bfd71d5d4ea9923f10f288ff24dd.md) | [[Feature] Workflow Timeout &amp; Escalation Oversight](../../../../../../../continuum/strategy/capability/feature/ec55d06e393e4988a84c6157b719bd40.md) | {"v0.2 / Assertion Status": "established", "v0.2 / Record State": "preserved historical assertion", "v0.2 / Architecture Identity Status": "Unresolved — representation ID only"} |
| id-bb481c2ec7ca5185932bb1b31c0acbff | Association | [Activity Escalation Telemetry](../../../../../../../continuum/business/business-service/f6c353c9349d47daadd59e57821ad03b.md) | [[Feature] Workflow Timeout &amp; Escalation Oversight](../../../../../../../continuum/strategy/capability/feature/ec55d06e393e4988a84c6157b719bd40.md) | {"semantic": "established", "currentness": "preserved historical assertion"} |

### Current comparison evidence

The comparison set includes qualified current records and independently pending historical alternatives; the standing of each is explicit. Each comparison is a potential equivalent, broader frame, enabling responsibility or overlapping concept as explained in the row; listing it is not an equivalence or coverage decision.

- [[CT2] Workflow & Activity Coordination](../../../../../../../continuum/strategy/capability/c724f3d38bfa4567983a11ad22095d61.md) — `c724f3d38bfa4567983a11ad22095d61`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.
  Boundary: External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.
  Independent adjudication: P1-348 (preserved source locator `05-current-unresolved-business_enabling-01.md#p1-348`).
- [[CT3] Governed Activity Execution Control](../../../../../../../continuum/strategy/capability/1e351cdfdc1045e384c3bd768eed8e80.md) — `1e351cdfdc1045e384c3bd768eed8e80`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.
  Boundary: Does not own underlying clinical-work meaning, clinical decisions or originator business authority.
  Independent adjudication: P1-039 (preserved source locator `05-current-unresolved-enterprise.md#p1-039`).
- [[CT3] Work Boundary Governance](../../../../../../../continuum/strategy/capability/35b66a0b38db4568849ecb842ffdcbf3.md) — `35b66a0b38db4568849ecb842ffdcbf3`; {"semantic": "Proposed", "currentness": "current", "placement_status": "Proposed", "structural_id_status": "Proposed", "name_status": "Proposed", "ancestry_status": "proposed"}. Definition: Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.
  Boundary: Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.
  Independent adjudication: P1-090 (preserved source locator `05-current-unresolved-business_enabling-03.md#p1-090`).

**Assessment and rationale:** OVERLAPPING. Internal Work Orders, To Dos and Synthetic Tasks retain distinct work meanings. The reviewed CT2 concerns the external execution bridge; EC execution control provides a reusable platform ability. Neither establishes equivalence to the BEC responsibility or its individual Features. The current external CT2 also carries an internal-registry source contribution: this is a visible scope tension, not authority to widen its definition. AX-10/14/15/16/18 apply.

**Proposed treatment:** ACCEPT the internal progression concept and each separately bounded Feature; retain independent identities. Coordinate clarification of the external CT2 source contribution.

**Consequences if authorised:** Adjudicate a current BEC frame, missing CT3 definition and explicit CT3-to-Feature containment; preserve human/non-human work distinctions, authorised recovery and indeterminate effects. Do not repair stalled/failed Process paths in this pass.

### Attributable source evidence

The canonical file owns current meaning. Historical sources below retain their original standing and recorded edition; source location and successful incorporation confer no additional acceptance. Entry-line selectors refer to the recorded revision, which may differ from today’s supersession-wrapped file.

- [docs/markdown/governance/baseline/architecture.json](../../../../../../../../docs/markdown/governance/baseline/architecture.json) — selector `/elements/396`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: governed source resource. Source key: `feature:workflow-timeout--escalation-oversight`.
- [docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml](../../../../../../../../docs/archimate/exchange/publish/1.1.0/Harmonia-Architecture-1.1.0.xml) — selector `element[@identifier="id-bef177231eed51078d4244176d3aced4"]`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: accepted representation binding and presentation; not an additional architecture source.
- [docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md](../../../../../../../../docs/markdown/02-strategy/capabilities/business-enabling-capabilities.md) — selector `9-workflow--activity-coordination-harmonia-core`; revision `aadcd55252fa7965be8a4321832ecc6d5b97c6e0`; role: original source locator; recorded edition.

**Remaining source/property qualifications:** `{}`; source unresolved: `[]`. These are preserved, not repaired.

[Back to row table](#) · [Identity index](../adjudication-inventory.md)

