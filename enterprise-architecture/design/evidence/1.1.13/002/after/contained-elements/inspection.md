# Actual imported Archi workbench inspection

These paths and extracts come from `published.archimate.gz`, the actual Archi 5.10 save.
Use [the complete inspectable tree](inspection.html) and load the published XML or decompressed native save in Archi.
Agent inspection supplies evidence; Solution Architect inspection and acceptance remain pending.

## Motivation parent Driver

`Motivation → I Motivation → I.1 Drivers → I.1.1 24/7 Availability`

```text
I.1.1 24/7 Availability/
  ◇ 24/7 Availability [archimate:Driver; id-b415475f474d5b3785b517c5935e72d6]
  📁 I.1.1.1 Stakeholder references — Reference navigation/
```

## Enterprise Capabilities CT1

`Strategy → II Strategy → II.1 Capabilities → II.1.1 Enterprise Capabilities → II.1.1.1 [CT1] Coordinated Activity & Interaction`

```text
II.1.1.1 [CT1] Coordinated Activity & Interaction/
  ◇ [CT1] Coordinated Activity & Interaction [archimate:Capability; id-6d2882efaa7756e886bb64a9ca045885]
  📁 II.1.1.1.1 [CT2] Activity & Execution/
    ◇ [CT2] Activity & Execution [id-49d4f69ebeeb5ea782a52172fb3e8a7b]
  📁 II.1.1.1.2 [CT2] Event & Subscription/
    ◇ [CT2] Event & Subscription [id-770ebb3e3bdc5f2bbe77b2bfd55bd74d]
  📁 II.1.1.1.3 [CT2] Interaction & Experience/
    ◇ [CT2] Interaction & Experience [id-82cd1f6a3e2352639e22c542f5cb3a9d]
  📁 II.1.1.1.4 [CT2] Interoperability & Exchange/
    ◇ [CT2] Interoperability & Exchange [id-dbd66e69788450649e184966aa724797]
```

## Leaf CT2

`Strategy → II Strategy → II.1 Capabilities → II.1.1 Enterprise Capabilities → II.1.1.1 [CT1] Coordinated Activity & Interaction → II.1.1.1.2 [CT2] Event & Subscription`

```text
II.1.1.1.2 [CT2] Event & Subscription/
  ◇ [CT2] Event & Subscription [archimate:Capability; id-770ebb3e3bdc5f2bbe77b2bfd55bd74d]
```

## Leaf CT3

`Strategy → II Strategy → II.1 Capabilities → II.1.1 Enterprise Capabilities → II.1.1.1 [CT1] Coordinated Activity & Interaction → II.1.1.1.1 [CT2] Activity & Execution → II.1.1.1.1.1 [CT3] Entity-Relative Operational Progression`

```text
II.1.1.1.1.1 [CT3] Entity-Relative Operational Progression/
  ◇ [CT3] Entity-Relative Operational Progression [archimate:Capability; id-873bd8a6758955b996c204275d26ed4f]
```

## Strategy Business Capabilities CT1

`Strategy → II Strategy → II.1 Capabilities → II.1.2 Business Capabilities → II.1.2.14 [CT1] Individual Care Delivery`

```text
II.1.2.14 [CT1] Individual Care Delivery/
  ◇ [CT1] Individual Care Delivery [archimate:Capability; id-2b13c6665f7b530fb21b07fd3e80e4ce]
  📁 II.1.2.14.1 [CT2] Clinical Determination/
    ◇ [CT2] Clinical Determination [id-e028d7e513fc5729b370465da8bc4ae8]
  📁 II.1.2.14.2 [CT2] Clinical Intervention/
    ◇ [CT2] Clinical Intervention [id-06c36cc9da8c5ad0b22ec96ff99f45aa]
  📁 II.1.2.14.3 [CT2] Individual Care Planning/
    ◇ [CT2] Individual Care Planning [id-e24ae17857c6529cb63c0c8d736ca5e7]
  📁 II.1.2.14.4 [CT2] Individual Care Review/
    ◇ [CT2] Individual Care Review [id-1e0db913564c55f59efd137db390e34f]
  📁 II.1.2.14.5 [CT2] Individual Health Assessment/
    ◇ [CT2] Individual Health Assessment [id-c205b4ba14735b29b4f23bd7531d8750]
  📁 II.1.2.14.6 [CT2] Individual Health Monitoring/
    ◇ [CT2] Individual Health Monitoring [id-30d731a19ab6526386b64c335fe3f7fd]
```

## Application parent Topic

`Application → IV Application → IV.1 Information Architecture → IV.1.1 Topics → IV.1.1.1 [Topic] Care`

```text
IV.1.1.1 [Topic] Care/
  ◇ [Topic] Care [archimate:DataObject; id-395f82a0d8664955b6f542d80de3f5b5]
  📁 IV.1.1.1.1 [Topic] CareLocation/
    ◇ [Topic] CareLocation [id-02f975c032b549d4b7e951d0a427533a]
  📁 IV.1.1.1.2 [Topic] Encounter/
    ◇ [Topic] Encounter [id-bcfd7137c33f4255ad3780826a64b4c5]
  📁 IV.1.1.1.3 [Topic] EncounterContext/
    ◇ [Topic] EncounterContext [id-677d8b45df5a47efa21085a7fbfffe1a]
  📁 IV.1.1.1.4 [Topic] EncounterProgression/
    ◇ [Topic] EncounterProgression [id-e5fda3f50b22419bbac4e39e6cadc416]
  📁 IV.1.1.1.5 [Topic] Episode/
    ◇ [Topic] Episode [id-e2c9fed13a2840a3931c2990bfb76da5]
```

## Application leaf Topic

`Application → IV Application → IV.1 Information Architecture → IV.1.1 Topics → IV.1.1.1 [Topic] Care → IV.1.1.1.2 [Topic] Encounter`

```text
IV.1.1.1.2 [Topic] Encounter/
  ◇ [Topic] Encounter [archimate:DataObject; id-bcfd7137c33f4255ad3780826a64b4c5]
```

## Strategy Business Enabling Capabilities

`Strategy → II Strategy → II.1 Capabilities → II.1.3 Business Enabling Capabilities → II.1.3.1 [CT1] Care Administration Enablement`

```text
II.1.3.1 [CT1] Care Administration Enablement/
  ◇ [CT1] Care Administration Enablement [archimate:Capability; id-fe7b5e3e02bb53f59ab89c27a81e9e18]
  📁 II.1.3.1.1 [CT2] Care Access Transaction Enablement [superseded]/
    ◇ [CT2] Care Access Transaction Enablement [id-957fa549536c5a02b16c2b8d7fed3ad4]
  📁 II.1.3.1.10 [CT2] Diagnostic Administration/
    ◇ [CT2] Diagnostic Administration [id-77908c3422545c70ba9ce082db020c0f]
  📁 II.1.3.1.11 [CT2] Episode & Encounter Administration/
    ◇ [CT2] Episode & Encounter Administration [id-198c918e6b225485b2065dc763be8ce1]
  📁 II.1.3.1.12 [CT2] Funding & Coverage Administration/
    ◇ [CT2] Funding & Coverage Administration [id-c2df74422094583c85e83601696f971f]
  📁 II.1.3.1.13 [CT2] Medication Administration/
    ◇ [CT2] Medication Administration [id-b977d4fbbfbc5943bf2b03b300408b3a]
  📁 II.1.3.1.14 [CT2] Order Administration/
    ◇ [CT2] Order Administration [id-35b56e00f8885f5f97147eca279b8ef6]
  📁 II.1.3.1.15 [CT2] Procedure Administration/
    ◇ [CT2] Procedure Administration [id-4aa8850ccaab516b9d3854b9e82971eb]
  📁 II.1.3.1.16 [CT2] Referral Administration/
    ◇ [CT2] Referral Administration [id-5beae734749a57468bf09956473d2f40]
  … full population available in inspection.html and native-inventory.json.gz
```

## CT1 → CT2 → CT3 canonical placement

`Strategy → II Strategy → II.1 Capabilities → II.1.3 Business Enabling Capabilities → II.1.3.1 [CT1] Care Administration Enablement → II.1.3.1.5 [CT2] Care Plan Administration → II.1.3.1.5.1 [CT3] Care Plan Lifecycle & Coordination`

```text
II.1.3.1.5.1 [CT3] Care Plan Lifecycle & Coordination/
  ◇ [CT3] Care Plan Lifecycle & Coordination [archimate:Capability; id-ab0baa5289245368afb3d9943f0dee5c]
  📁 II.1.3.1.5.1.1 [Feature] Care Goal & Plan Progression Representation/
    ◇ [Feature] Care Goal & Plan Progression Representation [id-3a7f53d246f6577c904c5980bdc3cb81]
  📁 II.1.3.1.5.1.2 [Feature] Care Plan Ingestion & Distribution/
    ◇ [Feature] Care Plan Ingestion & Distribution [id-651b1c005e2352d390d8a34cb2b787d7]
```

## Feature directory at governed Capability ancestry

`Strategy → II Strategy → II.1 Capabilities → II.1.3 Business Enabling Capabilities → II.1.3.9 [CT1] Shared Information & Coordination Enablement → II.1.3.9.9 [CT2] Health Information Control → II.1.3.9.9.4 [Feature] Governed Policy Assignment & Revocation`

```text
II.1.3.9.9.4 [Feature] Governed Policy Assignment & Revocation/
  ◇ [Feature] Governed Policy Assignment & Revocation [archimate:Capability; id-c0ee6896b3d759a9980c2a79ad7c3e53]
```

## Business Behaviour canonical element with explicit governed Home

`Business → III Business → III.4 Business Behaviour → III.4.1 [CT1] Care Administration Enablement — Capability context → III.4.1.1 [CT2] Care Plan Administration — Capability context → III.4.1.1.3 Business Services → III.4.1.1.3.3 Care Plan Ingress`

```text
III.4.1.1.3.3 Care Plan Ingress/
  ◇ Care Plan Ingress [archimate:BusinessService; id-7204348a711e5529a7c4c794c4d66259]
```

## Business Behaviour

`Business → III Business → III.4 Business Behaviour`

```text
III.4 Business Behaviour/
  📁 III.4.1 [CT1] Care Administration Enablement — Capability context/
  📁 III.4.2 [CT1] Entity Information Enablement — Capability context/
  📁 III.4.3 [CT1] Health Informatics Management — Capability context/
  📁 III.4.4 [CT1] Health Service Operations Enablement — Capability context/
  📁 III.4.5 [CT1] Human Collaboration & Work Enablement — Capability context [superseded]/
  📁 III.4.6 [CT1] Independent Assurance Enablement — Capability context/
  📁 III.4.7 [CT1] Shared Information & Coordination Enablement — Capability context/
```

## Business Behaviour deep context

`Business → III Business → III.4 Business Behaviour → III.4.2 [CT1] Entity Information Enablement — Capability context → III.4.2.1 [CT2] Client Administration — Capability context → III.4.2.1.4 [CT3] Person Identity Administration — Capability context → III.4.2.1.4.1 [Feature] Identifier Capture — Capability context → III.4.2.1.4.1.1 Business Services → III.4.2.1.4.1.1.1 [Business Service] Identifier Create`

```text
III.4.2.1.4.1.1.1 [Business Service] Identifier Create/
  ◇ [Business Service] Identifier Create [archimate:BusinessService; id-fddda503b8f0438e995465cd93204326]
```

## Application Information Architecture / Topics

`Application → IV Application → IV.1 Information Architecture → IV.1.1 Topics`

```text
IV.1.1 Topics/
  📁 IV.1.1.1 [Topic] Care/
    ◇ [Topic] Care [id-395f82a0d8664955b6f542d80de3f5b5]
  📁 IV.1.1.10 [Topic] TaskCompletion/
    ◇ [Topic] TaskCompletion [id-ea066123b5304c67ba344bb021d14dca]
  📁 IV.1.1.11 [Topic] Workforce/
    ◇ [Topic] Workforce [id-b931a047f2d24e27ab550368f9f63283]
  📁 IV.1.1.2 [Topic] Coordination/
    ◇ [Topic] Coordination [id-68a83c289478490197dfb2920534da86]
  📁 IV.1.1.3 [Topic] Enterprise/
    ◇ [Topic] Enterprise [id-74f9c5df31f2430ca725c39e285cd9c3]
  📁 IV.1.1.4 [Topic] Equipment/
    ◇ [Topic] Equipment [id-40f54ad3867549dba20c6f197c0c43be]
  📁 IV.1.1.5 [Topic] Individual/
    ◇ [Topic] Individual [id-bd2c303e65434409b69777c403116005]
  📁 IV.1.1.6 [Topic] Ordering/
    ◇ [Topic] Ordering [id-939dda01afdc40eabc1bb723f9da5b7b]
  … full population available in inspection.html and native-inventory.json.gz
```

## Four ValueStreams in native Strategy

`Strategy → III.2.2 Value Streams — Business navigation reference; native Strategy`

```text
III.2.2 Value Streams — Business navigation reference; native Strategy/
  ◇ Distributed Clinical Knowledge → Informed Clinical Collaboration [archimate:ValueStream; id-35185168faa951fd92071753cc98bc93]
  ◇ Multi-Source Clinical Information → Coherent Clinical Picture [archimate:ValueStream; id-d72c559c8ca551de969eb0f6a639a442]
  ◇ Operational Need → Coordinated Activity → Resolved Outcome [archimate:ValueStream; id-fc4621beb95b5af1a1398f683115a449]
  ◇ Unqualified Information → Governed, Consumable Information [archimate:ValueStream; id-c3d5bdd0946b5b7fb9200fae60e3ce82]
```

## Business ValueStream reference context

`Business → III Business → III.2 Products and Value Streams → III.2.2 Value Streams`

```text
III.2.2 Value Streams/
  📁 III.2.2.1 Distributed Clinical Knowledge → Informed Clinical Collaboration — reference; canonical element in Strategy/
  📁 III.2.2.2 Multi-Source Clinical Information → Coherent Clinical Picture — reference; canonical element in Strategy/
  📁 III.2.2.3 Operational Need → Coordinated Activity → Resolved Outcome — reference; canonical element in Strategy/
  📁 III.2.2.4 Unqualified Information → Governed, Consumable Information — reference; canonical element in Strategy/
```

## Cross-Cutting References — Motivation

`Motivation → R Cross-Cutting References — Reference navigation — Motivation index; not decomposition`

```text
R Cross-Cutting References — Reference navigation — Motivation index; not decomposition/
  📁 R.1 Stakeholder Index — Reference navigation/
    ◇ Care Team [id-3b746d99eb9f53d9b33430676fbf3e74]
    ◇ Clinicians & Care Teams [id-fbf5fe11400a57a38279a9aeccb1e22e]
```

## Cross-Cutting References — Strategy

`Strategy → R Cross-Cutting References — Reference navigation — Strategy index; not decomposition`

```text
R Cross-Cutting References — Reference navigation — Strategy index; not decomposition/
  📁 R.2 Historical Capability Identities — Reference navigation/
    ◇ [CT1] Care Access & Coordination [id-f78a7520ea80532b8423fc652b12ff6a]
    ◇ [CT3] Clinical Quality Criteria Management [id-143c6a0eb6675280a374d05aac61fe58]
```

## Historical Capability identities

`Strategy → R Cross-Cutting References — Reference navigation — Strategy index; not decomposition → R.2 Historical Capability Identities — Reference navigation`

```text
R.2 Historical Capability Identities — Reference navigation/
  ◇ [CT1] Care Access & Coordination [archimate:Capability; id-f78a7520ea80532b8423fc652b12ff6a]
  ◇ [CT3] Clinical Quality Criteria Management [archimate:Capability; id-143c6a0eb6675280a374d05aac61fe58]
  ◇ [CT3] Clinical Quality Evidence Management [archimate:Capability; id-3940059fa2735c8fa10020170420ee91]
  ◇ [CT3] Clinical Quality Reporting [archimate:Capability; id-3e15dac60a7851bf820f6269d07b6a37]
  ◇ [CT3] Clinical Quality Review & Assessment Management [archimate:Capability; id-943c0e41cdf55f05ac5fdd126cbcae3d]
  ◇ [CT2] Clinical Quality Management [archimate:Capability; id-e9b2533401765481aa27d5c901bd8fd2]
  ◇ [CT3] Clinical Risk Identification [archimate:Capability; id-aa118374de83598fa0d455e546899188]
  ◇ [CT3] Clinical Risk Mitigation Management [archimate:Capability; id-b09b2931af455e1f816a1e0dd2566cd1]
  … full population available in inspection.html and native-inventory.json.gz
```

## Views — original folders

`Views`

```text
Views/
  📁 0-Motivation/
    ◇ Architectural Axioms — v0.2 source selection [id-be6e7785da7b573aad18b9e05c2796c1]
    ◇ Architecture Axioms [id-9b2c6385858f57dcbca54a46acfe77ba]
  📁 1-Strategy/
  📁 2-BusinessLayer/
  📁 3-ApplicationLayer/
```

## Deep canonical navigation

`Business → III Business → III.4 Business Behaviour → III.4.2 [CT1] Entity Information Enablement — Capability context → III.4.2.1 [CT2] Client Administration — Capability context → III.4.2.1.4 [CT3] Person Identity Administration — Capability context → III.4.2.1.4.1 [Feature] Identifier Capture — Capability context → III.4.2.1.4.1.1 Business Services → III.4.2.1.4.1.1.1 [Business Service] Identifier Create`

```text
III.4.2.1.4.1.1.1 [Business Service] Identifier Create/
  ◇ [Business Service] Identifier Create [archimate:BusinessService; id-fddda503b8f0438e995465cd93204326]
```

## Open the actual native model

```bash
gzip -dc published.archimate.gz > /tmp/harmonia-1.1.13.archimate
```

Open that file in Archi 5.10. The published XML is the deterministic starting projection;
the native save and returned XML are verification controls, not replacement authorities.

Relations remain in the existing native Relations folder. Reference-only folders carry
canonical coordinates/identifiers in their documentation; Archi does not provide live aliases.
