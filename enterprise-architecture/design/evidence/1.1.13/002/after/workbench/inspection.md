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

## Strategy Business Capabilities CT1

`Strategy → II Strategy → II.1 Capabilities → II.1.2 Business Capabilities → II.1.2.14 [CT1] Individual Care Delivery`

```text
II.1.2.14 [CT1] Individual Care Delivery/
  ◇ [CT1] Individual Care Delivery [archimate:Capability; id-2b13c6665f7b530fb21b07fd3e80e4ce]
  ◇ [CT2] Clinical Intervention [archimate:Capability; id-06c36cc9da8c5ad0b22ec96ff99f45aa]
  📁 II.1.2.14.1 [CT2] Clinical Determination/
    ◇ [CT2] Clinical Determination [id-e028d7e513fc5729b370465da8bc4ae8]
    ◇ [CT3] Clinical Finding Interpretation [id-94f7aff6888a52d08db90e33e52c3fd3]
  📁 II.1.2.14.3 [CT2] Individual Care Planning/
    ◇ [CT2] Individual Care Planning [id-e24ae17857c6529cb63c0c8d736ca5e7]
    ◇ [CT3] Care Goal Definition [id-c45b599ec97b544d8049f3bbc65859ca]
  📁 II.1.2.14.4 [CT2] Individual Care Review/
    ◇ [CT2] Individual Care Review [id-1e0db913564c55f59efd137db390e34f]
    ◇ [CT3] Care Appropriateness Review [id-dfa6322afd30598fb230e2d13f063d12]
  📁 II.1.2.14.5 [CT2] Individual Health Assessment/
    ◇ [CT2] Individual Health Assessment [id-c205b4ba14735b29b4f23bd7531d8750]
    ◇ [CT3] Clinical Examination [id-abe9e3230f6d5577a13fca453e624a85]
  📁 II.1.2.14.6 [CT2] Individual Health Monitoring/
    ◇ [CT2] Individual Health Monitoring [id-30d731a19ab6526386b64c335fe3f7fd]
    ◇ [CT3] Care Response Monitoring [id-e24ada9f97a351f09911b66311963637]
```

## Application parent Topic

`Application → IV Application → IV.1 Information Architecture → IV.1.1 Topics → IV.1.1.1 [Topic] Care`

```text
IV.1.1.1 [Topic] Care/
  ◇ [Topic] Care [archimate:DataObject; id-395f82a0d8664955b6f542d80de3f5b5]
  ◇ [Topic] CareLocation [archimate:DataObject; id-02f975c032b549d4b7e951d0a427533a]
  ◇ [Topic] Encounter [archimate:DataObject; id-bcfd7137c33f4255ad3780826a64b4c5]
  ◇ [Topic] EncounterContext [archimate:DataObject; id-677d8b45df5a47efa21085a7fbfffe1a]
  ◇ [Topic] EncounterProgression [archimate:DataObject; id-e5fda3f50b22419bbac4e39e6cadc416]
  ◇ [Topic] Episode [archimate:DataObject; id-e2c9fed13a2840a3931c2990bfb76da5]
```

## Strategy Business Enabling Capabilities

`Strategy → II Strategy → II.1 Capabilities → II.1.3 Business Enabling Capabilities → II.1.3.1 [CT1] Care Administration Enablement`

```text
II.1.3.1 [CT1] Care Administration Enablement/
  ◇ [CT1] Care Administration Enablement [archimate:Capability; id-fe7b5e3e02bb53f59ab89c27a81e9e18]
  ◇ [CT2] Care Access Transaction Enablement [archimate:Capability; id-957fa549536c5a02b16c2b8d7fed3ad4]
  ◇ [CT2] Care Documentation & Communication Enablement [archimate:Capability; id-ddefa589dee05b94ab470e11f3b0540c]
  ◇ [CT2] Care Financial Information Enablement [archimate:Capability; id-4ddd19075297571c9aeee1382d935a72]
  ◇ [CT2] Care Fulfilment Administration Enablement [archimate:Capability; id-0ac4ff89dcdc5e7c951d67f06c504dd8]
  ◇ [CT2] Clinical Charging & Claims Administration [archimate:Capability; id-5463239c9ef65cef98c5274ab6e8f6db]
  ◇ [CT2] Funding & Coverage Administration [archimate:Capability; id-c2df74422094583c85e83601696f971f]
  📁 II.1.3.1.10 [CT2] Diagnostic Administration/
    ◇ [CT2] Diagnostic Administration [id-77908c3422545c70ba9ce082db020c0f]
  📁 II.1.3.1.11 [CT2] Episode & Encounter Administration/
    ◇ [CT2] Episode & Encounter Administration [id-198c918e6b225485b2065dc763be8ce1]
  📁 II.1.3.1.13 [CT2] Medication Administration/
    ◇ [CT2] Medication Administration [id-b977d4fbbfbc5943bf2b03b300408b3a]
  📁 II.1.3.1.14 [CT2] Order Administration/
    ◇ [CT2] Order Administration [id-35b56e00f8885f5f97147eca279b8ef6]
  📁 II.1.3.1.15 [CT2] Procedure Administration/
    ◇ [CT2] Procedure Administration [id-4aa8850ccaab516b9d3854b9e82971eb]
  📁 II.1.3.1.16 [CT2] Referral Administration/
    ◇ [CT2] Referral Administration [id-5beae734749a57468bf09956473d2f40]
  📁 II.1.3.1.17 [CT2] Scheduling Administration/
    ◇ [CT2] Scheduling Administration [id-fa4797b3d84052ecbea381b52dfa8b3f]
  📁 II.1.3.1.5 [CT2] Care Plan Administration/
    ◇ [CT2] Care Plan Administration [id-8c082e2d6c605903834291c424722f75]
  … full population available in inspection.html and native-inventory.json.gz
```

## CT1 → CT2 → CT3 canonical placement

`Strategy → II Strategy → II.1 Capabilities → II.1.3 Business Enabling Capabilities → II.1.3.1 [CT1] Care Administration Enablement → II.1.3.1.5 [CT2] Care Plan Administration → II.1.3.1.5.1 [CT3] Care Plan Lifecycle & Coordination`

```text
II.1.3.1.5.1 [CT3] Care Plan Lifecycle & Coordination/
  ◇ [CT3] Care Plan Lifecycle & Coordination [archimate:Capability; id-ab0baa5289245368afb3d9943f0dee5c]
  ◇ [Feature] Care Goal & Plan Progression Representation [archimate:Capability; id-3a7f53d246f6577c904c5980bdc3cb81]
  ◇ [Feature] Care Plan Ingestion & Distribution [archimate:Capability; id-651b1c005e2352d390d8a34cb2b787d7]
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

`Business → III Business → III.4 Business Behaviour → III.4.2 [CT1] Entity Information Enablement — Capability context → III.4.2.1 [CT2] Client Administration — Capability context → III.4.2.1.4 [CT3] Person Identity Administration — Capability context → III.4.2.1.4.1 [Feature] Identifier Capture — Capability context → III.4.2.1.4.1.1 Business Services`

```text
III.4.2.1.4.1.1 Business Services/
  ◇ [Business Service] Identifier Create [archimate:BusinessService; id-fddda503b8f0438e995465cd93204326]
  ◇ [Business Service] Identifier Update [archimate:BusinessService; id-0088d61ccfcf4962b3aab8bc3b96021d]
```

## Application Information Architecture / Topics

`Application → IV Application → IV.1 Information Architecture → IV.1.1 Topics`

```text
IV.1.1 Topics/
  📁 IV.1.1.1 [Topic] Care/
    ◇ [Topic] Care [id-395f82a0d8664955b6f542d80de3f5b5]
    ◇ [Topic] CareLocation [id-02f975c032b549d4b7e951d0a427533a]
  📁 IV.1.1.10 [Topic] TaskCompletion/
    ◇ [Topic] TaskCompletion [id-ea066123b5304c67ba344bb021d14dca]
    ◇ [Topic] ExecutionConcurrency [id-10b5bc11da7d43c999ea4a37e6ae58d1]
  📁 IV.1.1.11 [Topic] Workforce/
    ◇ [Topic] Workforce [id-b931a047f2d24e27ab550368f9f63283]
    ◇ [Topic] CommunicationAddress [id-a60925c7b1db471589f42b064429bb82]
  📁 IV.1.1.2 [Topic] Coordination/
    ◇ [Topic] Coordination [id-68a83c289478490197dfb2920534da86]
    ◇ [Topic] Referral [id-8ca7580325de469586e67ca5b627b8c2]
  📁 IV.1.1.3 [Topic] Enterprise/
    ◇ [Topic] Enterprise [id-74f9c5df31f2430ca725c39e285cd9c3]
    ◇ [Topic] Organisation [id-da6abec4c12f4e689f8e7516dfece8f1]
  📁 IV.1.1.4 [Topic] Equipment/
    ◇ [Topic] Equipment [id-40f54ad3867549dba20c6f197c0c43be]
    ◇ [Topic] Device [id-68ee1eb72f6044ed97960a0aca108823]
  📁 IV.1.1.5 [Topic] Individual/
    ◇ [Topic] Individual [id-bd2c303e65434409b69777c403116005]
    ◇ [Topic] Authority [id-3d77d9bbfd9747c792d456d41f489bae]
  📁 IV.1.1.6 [Topic] Ordering/
    ◇ [Topic] Ordering [id-939dda01afdc40eabc1bb723f9da5b7b]
    ◇ [Topic] Order [id-a025ad6103e24c79b90f39a16b8b15a2]
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

`Business → III Business → III.4 Business Behaviour → III.4.2 [CT1] Entity Information Enablement — Capability context → III.4.2.1 [CT2] Client Administration — Capability context → III.4.2.1.4 [CT3] Person Identity Administration — Capability context → III.4.2.1.4.1 [Feature] Identifier Capture — Capability context → III.4.2.1.4.1.1 Business Services`

```text
III.4.2.1.4.1.1 Business Services/
  ◇ [Business Service] Identifier Create [archimate:BusinessService; id-fddda503b8f0438e995465cd93204326]
  ◇ [Business Service] Identifier Update [archimate:BusinessService; id-0088d61ccfcf4962b3aab8bc3b96021d]
```

## Open the actual native model

```bash
gzip -dc published.archimate.gz > /tmp/harmonia-1.1.13.archimate
```

Open that file in Archi 5.10. The published XML is the deterministic starting projection;
the native save and returned XML are verification controls, not replacement authorities.

Relations remain in the existing native Relations folder. Reference-only folders carry
canonical coordinates/identifiers in their documentation; Archi does not provide live aliases.
