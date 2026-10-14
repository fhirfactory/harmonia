<!-- Copyright (c) 2026 Mark Hunter. Licensed under GPL-3.0-or-later. -->

# B2.1 — Individual Care Delivery ↔ Health Information Access

**B2.1 — Individual Care Delivery ↔ Health Information Access: PASS**

## 1. Authority and bounded result

This record reconciles the explicitly agreed Domain05 Step B B2.1 decisions,
2026-10-13. The [Domain02 Business Capability catalogue](../../02-strategy/capabilities/business-capabilities.md#01-individual-care-delivery)
defines Individual Care Delivery and its six L2 capabilities. The established
[Health Information Access responsibility](../../03-business-architecture/behaviours/05-intrinsic-enablement.md#23-health-information-access)
defines the enabling anchor. The [Step B method](step-b-method.md#capability-enablement-semantics)
governs relationship semantics and Health Information Access scope.

PASS applies only to the L1 relationship and six L2 relationships below. It is
not a runtime verification, a declaration of exhaustive capability coverage or
an application/component allocation. This record establishes no additional
capability decomposition, Feature, Service, Function or Information mapping.
**B2.2 is not commenced.** No other Business Capability or Business Enabling
Capability is mapped by this reconciliation.

## 2. L1 relationship and rationale

`Individual Care Delivery enabled-by Health Information Access`

Individual Care Delivery requires governed access to relevant longitudinal and
current health information to support clinical assessment, diagnosis, treatment
decisions, interventions and monitoring. Health Information Access enables
access to that information but does not determine its clinical significance or
perform the clinical decision-making of Individual Care Delivery.

## 3. L2 relationships and rationales

These are material capability-enablement relationships. They do not transfer
clinical accountability or require each runtime exercise to retrieve information
through Health Information Access. The L2 labels identify the agreed Domain02
decomposition; no additional structural Canonical ID scheme is established.

### 01.01 Individual Health Assessment

`Individual Health Assessment enabled-by Health Information Access`

Individual Health Assessment requires governed access to relevant existing and
current health information so that available clinical, health and contextual
information can be considered when establishing an understanding of the
individual's health state. Health Information Access enables retrieval of that
information but does not perform the assessment or establish the assessed findings.

Health Information Access does not own every mechanism by which assessment
information is acquired. Direct examination, observation, history-taking or
measurement may establish assessment information independently.

### 01.02 Clinical Determination

`Clinical Determination enabled-by Health Information Access`

Clinical Determination requires governed access to relevant current and
historical health information where that information is necessary to interpret
assessed findings and establish clinically meaningful conclusions about the
individual's health state, condition, risk or prognosis. Health Information
Access enables retrieval of that information but does not interpret its clinical
significance or establish the clinical determination.

### 01.03 Individual Care Planning

`Individual Care Planning enabled-by Health Information Access`

Individual Care Planning requires governed access to relevant current and
historical health and care information where that information is necessary to
establish an appropriate intended clinical response, including care objectives,
interventions and monitoring. Health Information Access enables retrieval of
that information but does not establish the care intent, select the clinical
response or perform the planning.

Information concerning operational availability of services, resources, staff,
facilities or appointments is not Health Information Access merely because it
influences care planning.

### 01.04 Clinical Intervention

`Clinical Intervention enabled-by Health Information Access`

Clinical Intervention requires governed access to relevant health and care
information where that information is necessary to safely and appropriately
perform an intended clinical action, including information concerning the
individual's current health state, established care intent and clinically
relevant history. Health Information Access enables retrieval of that information
but does not authorise, select or perform the clinical intervention.

Access to information describing an intended intervention does not confer
authority to perform that intervention.

### 01.05 Individual Health Monitoring

`Individual Health Monitoring enabled-by Health Information Access`

Individual Health Monitoring requires governed access to relevant current and
historical health information where that information is necessary to establish
change, stability or deviation in an individual's health state over time. Health
Information Access enables access to prior, baseline and current health
information required for comparison, but does not perform the monitoring or
interpret the clinical significance of an identified change.

### 01.06 Individual Care Review

`Individual Care Review enabled-by Health Information Access`

Individual Care Review requires governed access to relevant health and care
information in order to evaluate delivered care and observed health progression
against the intended care objectives. Health Information Access enables access
to the care intent, relevant care history and health outcomes required for that
evaluation, but does not perform the review or establish the continuing
appropriateness of the care intent.

## 4. Preserved boundaries

The [Domain02 decomposition and context rules](../../02-strategy/capabilities/business-capabilities.md#capability-decomposition-and-educational-documentation)
apply: the six established children are now explicitly reconciled as BC CT2s.
The current catalogue establishes the stated assessment, monitoring and review
CT3s; the other branches retain their recorded uncertainty/stopping points.
Under the
[fixed Capability model](../../03-business-architecture/metamodel/business-architecture-metamodel.md#11-capability-decomposition-and-terminology),
subsequently required Harmonia Features must first have meaningful **Business
Enabling CT3** frames established. BC decomposition does not terminate in Features
and this B2.1 support trace establishes no ancestry or new CT3 enablement edge.
None may be invented for structural completeness. Retained L1/L2 display labels
alone do not allocate CT tiers. Examples are not subordinate Capabilities, and care
setting, delivery mode, temporal pattern and organisational
arrangement do not themselves define different business abilities. Acute,
Planned/Scheduled and Longitudinal Care are not the ICD L2 decomposition.

[B2-R07](step-b-method.md#b2-r07) distinguishes individual clinical / health /
care information from operational / workforce / financial / corporate /
service-resource information. No exhaustive edge-case taxonomy or allocation to
other enabling capabilities is introduced.

`enabled-by` does not mean that Health Information Access interprets, judges,
plans, authorises or performs clinical care. It expresses material enablement,
not a mandatory runtime dependency. B2.1 PASS does not change retained candidate
deferrals, declare B1 PASS or authorise subsequent Step B mappings.
