<!-- Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later -->
# Proposed architecture catalogue after human-reviewed BEC reconciliation

Generated from tools/compose.py and the bounded tools/reviewed_bec.py reconciliation. Architectural status remains Proposed. Record standing distinguishes current architecture from preserved superseded and unresolved material.

## HCM-BEC-healthcare-entity-information-enablement — Entity Information Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Intent:** Administer identifying, descriptive and qualified relationship information concerning healthcare participants, organisations, locations, services, materiel and devices.

**Boundary:** Registry information does not establish clinical privilege, service readiness, occupancy or originating national identity authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-care-administration-information-enablement — Care Administration Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Intent:** Administer the information and progression of care access, requests, encounters, records and plans across responsible care systems.

**Boundary:** Information administration does not perform the clinical act, own host booking engines or infer Transfer of Care from referral acceptance.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-care-context-integration-enablement — Care Context Integration Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** superseded

**Definition:** Compose governed information and coordination behaviour for materially different care settings, continuity arrangements and specialist clinical purposes.

Emergency presentation, inpatient rounds and outreach visits need different contextual assemblies of the same underlying information. This frame retains those established setting-specific responsibilities as compositions rather than duplicate records or clinical execution authorities.

**Intent:** Give a care participant the appropriate information context for the particular integration purpose without duplicating general information responsibilities.

**Boundary:** Setting, care mode and specialty are overlapping contextual dimensions. These children are integration responsibilities, not a mutually exclusive taxonomy of patients or clinical care.

**Last allocated structural identity — historical:** CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-operations-coordination-enablement — Health Service Operations Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Intent:** Enable health-service operations through information about flow, care-places, capacity, workforce capability, operational work, logistics and discharge progression.

**Boundary:** Coordination owns only explicitly assigned operational activity. It does not originate clinical clearance, determine clinical work or take workforce and resource governance from their sources.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-governed-health-information-enablement — Shared Information & Coordination Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Intent:** Enable governed shared health-information use, exchange, stewardship, knowledge, collaboration, external work coordination, calendars, presentation and information design.

**Boundary:** Preservation, custody, discoverability and presentation do not confer originating fact authority. Clinical documentation administration and clinical-context use retain their own responsibilities.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-human-collaboration-work-enablement — Human Collaboration & Work Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** superseded

**Definition:** Enable contextual human interaction, collaboration and the definition and progression of governed operational activity.

People discuss information, inspect it, make authorised decisions and undertake work. Those interactions need reusable coordination and temporal context, while entity-relative progression and activity design retain distinct semantics from message exchange and screen rendering.

**Intent:** Allow people and automated participants to interact and progress authorised work with intelligible context and accountability.

**Boundary:** Conversation is not a clinical record, presentation is not authority, and generic work coordination does not determine clinical work or own its outcome.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-independent-service-assurance-enablement — Independent Assurance Enablement

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Intent:** Enable assurance specification and independent review of the assurance service while preserving the distinct Canonical responsibility for assurance against a governed subject.

**Boundary:** Assurance cannot manage, remediate or approve its subject merely because it observes it. Clinical adequacy, financial governance and source-information authority remain outside this mandate.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-practitioner — Practitioner Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Intent:** Administer practitioner identity, professional profile and supplied registration information with its issuing-authority attribution.

**Boundary:** Professional registration and profile representation do not grant clinical or operational privilege.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-practitioner-role — Practitioner Role Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Intent:** Administer practitioner roles, specialties, organisational affiliations, operational capacities and recognised role endpoints.

**Boundary:** Role, specialty, capacity and endpoint information supply context; they do not constitute a privilege determination.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER-ROLE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-provider-relationship — Provider Relationship

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Managing organizational affiliations and multidisciplinary team relationships.

Effective affiliations explain which practitioner or team participates with which organisation. These relationships support collaboration and routing while each participating identity keeps its own responsibility owner.

**Intent:** Preserve which affiliations apply when interpreting provider participation.

**Boundary:** Affiliation records do not transfer the participating organisations or practitioner identities to this capability.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-RELATIONSHIP

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-provider-engagement — Provider Engagement

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Capturing terms of service and clinical privilege agreements.

Engagement information represents the terms and externally granted practice privileges under which a provider participates. This differs from who the practitioner is and from whether a particular operation is authorised now.

**Intent:** Make the externally established terms and privilege standing of provider participation available without granting it.

**Boundary:** Records externally granted service terms and privileges; it does not independently grant those privileges.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PROVIDER-ENGAGEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-healthcare-organisation — Healthcare Organisation Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

**Intent:** Administer identifying, descriptive, structural, location, contact, registered/legal and recognised endpoint information needed to represent and interact with a healthcare organisation.

**Boundary:** Maintains verified legal and operational identity information without corporate management authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-ORGANISATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-organisational-structure — Organisational Structure

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Managing parent-child hierarchies, departments, and corporate relationships.

Departments, divisions and regional networks need qualified structural relationships that can be traversed and understood over time. Structural membership is distinct from physical location and from authority to manage another organisation.

**Intent:** Make organisational relationships navigable without confusing structure with ownership or physical containment.

**Boundary:** Structural relationships do not acquire ownership of every related organisation or service.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATIONAL-STRUCTURE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-organisation-contact — Organisation Contact

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Maintaining verified administrative and clinical communication channels.

Verified facility contact channels make an organisation reachable for appropriate administrative and clinical communication. The contact record says how it may be reached; recipient qualification and disclosure policy govern a particular use.

**Intent:** Provide verified channels for reaching the intended healthcare organisation.

**Boundary:** Contact validity is distinct from clinical recipient eligibility and policy permission.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-ORGANISATION-ADMINISTRATION.CT3-BEC-ORGANISATION-CONTACT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-healthcare-location — Healthcare Location Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

**Intent:** Administer the identity, address, geospatial description and physical containment relationships of healthcare locations.

**Boundary:** Location description does not establish occupancy, cleanliness, service capacity or service readiness.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-HEALTHCARE-LOCATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-location-structure — Location Structure

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Modeling the physical architecture of healthcare campuses.

Campus, building, floor, ward and room relationships explain physical containment. The structure provides navigation and context without determining organisational reporting lines or current operational capacity.

**Intent:** Allow a particular place to be interpreted within its physical surroundings.

**Boundary:** Physical containment differs from organisational structure and operational responsibility.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-LOCATION-STRUCTURE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-place-definition — Care-Place Definition Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Intent:** Administer the stable description, characteristics and physical location of a bed, bay, chair or other care-place.

**Boundary:** Describes a place and its capabilities; operational bed state and readiness remain separately managed.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-LOCATION-ADMINISTRATION.CT3-BEC-CARE-PLACE-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-healthcare-service-definition — Healthcare Service Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

**Intent:** Administer service definitions, provision relationships, defined availability and eligibility information describing offered healthcare services.

**Boundary:** Defined availability is not dynamic operational capacity, collective readiness or authority to accept clinical care.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-HEALTHCARE-SERVICE-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-service-provision-relationship — Service Provision Relationship

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Binding health services to specific organizations, locations, and practitioner roles.

Service provision joins a named offering to the organisations, places and practitioner roles through which it is provided. The mapping is the owned concern; the identity and meaning of each mapped participant remain with its established capability.

**Intent:** Make it clear which provider delivers which service at which place and through which participating roles.

**Boundary:** Owns mapping associations, retaining the owners of each participating entity.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-PROVISION-RELATIONSHIP

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-service-availability-definition — Service Availability Definition

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Modeling regular operating hours, exceptional closures, and emergency availability.

Regular hours, on-call windows and exceptional closures describe when an offering is nominally available. They provide an input to readiness and scheduling without proving that all required dependencies are ready.

**Intent:** Express the operating windows against which a service participation request can be considered.

**Boundary:** Represents operating windows and exceptions; it does not establish staffing, capacity or readiness.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-AVAILABILITY-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-service-eligibility-definition — Service Eligibility Definition

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Capturing referral criteria, catchment boundaries, and clinical eligibility.

Published catchment, referral and clinical eligibility conditions explain the intended audience and access constraints of an offering. Their representation supports responsible assessment without deciding acceptance or clinical priority.

**Intent:** Make the governed intake and catchment conditions available to the party responsible for access decisions.

**Boundary:** Represents governed eligibility criteria; it does not make clinical acceptance or triage decisions.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-SERVICE-ADMINISTRATION.CT3-BEC-SERVICE-ELIGIBILITY-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-health-product-definition — Health Materiel Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

**Intent:** Administer identifying and descriptive information about health materiel referenced in healthcare information and activity.

**Boundary:** Reference representation is distinct from procurement, manufacturing and prescribing.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-HEALTH-PRODUCT-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-product-formulary-catalogue — Health Materiel Catalogue & Formulary Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

**Intent:** Administer health-materiel catalogue and externally approved formulary information, preserving approval and version standing.

**Boundary:** Represents externally approved formularies; it does not acquire formulary approval or dispensing authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-PRODUCT-FORMULARY-CATALOGUE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-product-batch-serial-tracking — Health Materiel Traceability Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

**Intent:** Administer batch/lot, serial, supplier, manufacturer, receipt/delivery and relevant movement/provenance information concerning health materiel.

**Boundary:** Materiel traceability does not make Harmonia a procurement, stock-control or supply-chain manager; specimen movement remains Clinical Logistics Coordination.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-PRODUCT-ADMINISTRATION.CT3-BEC-PRODUCT-BATCH-SERIAL-TRACKING

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-device-definition — Clinical Device Profile Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Intent:** Administer device type, instance, identifying and descriptive information needed to recognise clinical equipment and its stated capabilities.

**Boundary:** Describes device types and capabilities without taking regulatory approval or clinical-use authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-device-instance — Device Instance

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Tracking physical device hardware instances and serial numbers.

An identifiable physical device carries UDI and serial context through its use. The instance record supports association and resolution without originating device observations or making maintenance decisions.

**Intent:** Keep the identity of the actual physical equipment distinguishable from its model and endpoint.

**Boundary:** Identifies physical hardware; serial tracking is distinct from maintenance and live observation content.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-INSTANCE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-device-association — Device Association

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Associating clinical devices with patients, beds, or rooms.

A device can be associated with different patients and care places at different times. Qualified association and its lineage allow observations to be interpreted against the applicable binding rather than the current assignment alone.

**Intent:** Allow a device-originated item to be associated with the correct subject and place at the relevant time.

**Boundary:** Represents qualified device-to-subject/place association without owning those subjects or observations.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-ASSOCIATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-device-connectivity — Device Connectivity

**Type / tier:** capability / CT3

**Reconciliation standing:** superseded

**Definition:** Registering communication protocols and network endpoints for medical devices.

A device communication binding identifies the protocol and endpoint through which its feed can participate. Connectivity metadata supports routing without owning authentication issuance, payload semantics or physiological interpretation.

**Intent:** Make a device feed reachable through its registered communication binding.

**Boundary:** Represents communication bindings; it does not acquire authentication credential issuance or telemetry interpretation.

**Last allocated structural identity — historical:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-CONNECTIVITY

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-access-transaction-enablement — Care Access Transaction Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Coordinate the intake, care-request disposition and appointment information through which a person accesses care.

Coordinate the intake, care-request disposition and appointment information through which a person accesses care. Its constituent abilities distinguish Client-Centred Administration; Referral Administration; Scheduling Administration.

**Intent:** Make an access request and its disposition traceable without confusing a booking with care responsibility or delivery.

**Boundary:** A referral requests care responsibility; an appointment concerns a booking. Neither establishes care delivery or host booking authority.

**Last allocated structural identity — historical:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-ACCESS-TRANSACTION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-fulfilment-administration-enablement — Care Fulfilment Administration Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Maintain distinguishable care-episode, bounded fulfilment and delivered-activity information across performing systems.

Maintain distinguishable care-episode, bounded fulfilment and delivered-activity information across performing systems. Its constituent abilities distinguish Episode & Encounter Administration; Order Administration; Diagnostic Administration; Medication Administration; Procedure Administration.

**Intent:** Maintain the link between a bounded care request, the relevant episode and the externally observed fulfilment outcome.

**Boundary:** Encounter progression, order outcome, diagnostic interpretation, medication events and procedure documentation retain different source authorities.

**Last allocated structural identity — historical:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FULFILMENT-ADMINISTRATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-documentation-communication-enablement — Care Documentation & Communication Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Administer the lifecycle, shared-plan context and directed communication of care documentation.

Administer the lifecycle, shared-plan context and directed communication of care documentation. Its constituent abilities distinguish Clinical Record Administration; Care Plan Administration; Clinical Communication Administration.

**Intent:** Preserve which document or plan applies and what a directed recipient has actually acknowledged.

**Boundary:** This frame does not absorb longitudinal synthesis, collaboration discourse, originating clinical decisions or generic transport.

**Last allocated structural identity — historical:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-DOCUMENTATION-COMMUNICATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-financial-information-enablement — Care Financial Information Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Represent coverage context and convey care-activity evidence to responsible funding and charging systems.

Represent coverage context and convey care-activity evidence to responsible funding and charging systems. Its constituent abilities distinguish Funding & Coverage Administration; Clinical Charging & Claims Administration.

**Intent:** Give responsible financial systems the care-activity and coverage context they need without creating another financial authority.

**Boundary:** Adjacent information participation does not provide financial governance, coverage adjudication or a billing platform.

**Last allocated structural identity — historical:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-FINANCIAL-INFORMATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-longitudinal-community-care-integration — Longitudinal & Community Care Integration

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Support continuity, remote participation and preventive follow-up through appropriately assembled and exchanged care context.

Support continuity, remote participation and preventive follow-up through appropriately assembled and exchanged care context. Its constituent abilities distinguish Primary Care; Community Care; Outreach Care; Remote & Virtual Care; Preventive Care.

**Intent:** Support continuity when care information must follow the person across providers, visits and distance.

**Boundary:** Longitudinal purpose does not make these care contexts exclusive. Outreach location and remote mode remain distinct from source clinical responsibility.

**Last allocated structural identity — historical:** CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-LONGITUDINAL-COMMUNITY-CARE-INTEGRATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-facility-episodic-care-integration — Facility & Episodic Care Integration

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Provide the context assemblies and boundary integration needed for facility presentations and bounded care episodes.

Provide the context assemblies and boundary integration needed for facility presentations and bounded care episodes. Its constituent abilities distinguish Acute Care; Emergency Care; Inpatient Care; Ambulatory Care; Surgical & Procedural Care.

**Intent:** Make presentation- and episode-specific information available to the responsible care team in its applicable context.

**Boundary:** Acute, emergency and inpatient perspectives overlap; each capability owns its named integration purpose rather than a population or clinical workflow.

**Last allocated structural identity — historical:** CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-FACILITY-EPISODIC-CARE-INTEGRATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-specialist-clinical-context-integration — Specialist Clinical Context Integration

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Integrate specialist assessment, investigation and therapeutic information with longitudinal and episode context.

Integrate specialist assessment, investigation and therapeutic information with longitudinal and episode context. Its constituent abilities distinguish Clinical Assessment; Diagnostic Services; Therapeutic Services; Medication Therapy; Allied Health; Rehabilitation.

**Intent:** Keep specialised clinical context connected to the wider record while retaining the specialist source of clinical judgement.

**Boundary:** A clinical specialty is evidence for integration purpose, not authority to invent a specialist engine, clinical decision or duplicated general capture function.

**Last allocated structural identity — historical:** CT1-BEC-CARE-CONTEXT-INTEGRATION-ENABLEMENT.CT2-BEC-SPECIALIST-CLINICAL-CONTEXT-INTEGRATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-setting-operations-enablement — Care Setting Operations Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Coordinate the local flow, session state and capacity semantics of a care-delivery setting.

Coordinate the local flow, session state and capacity semantics of a care-delivery setting. Its constituent abilities distinguish Clinic & Practice Operations; Ward Operations; Theatre Operations; Emergency Department Operations; Outpatient Operations.

**Intent:** Express the actual operating constraints of each setting rather than substituting a generic resource count for capacity.

**Boundary:** Local capacity is established here; aggregate telemetry, bed state and equipment state cannot substitute for it.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-SETTING-OPERATIONS-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-resource-capacity-coordination — Care Resource & Capacity Coordination

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Relate operational care-place and equipment state to the coordinated view of service demand and capacity.

Relate operational care-place and equipment state to the coordinated view of service demand and capacity. Its constituent abilities distinguish Bed & Care-Place Management; Clinical Resource Management; Service Capacity Management.

**Intent:** Make resource-state contributions and coordinated capacity views usable without collapsing their different responsibilities.

**Boundary:** Bed/equipment availability is a contribution to capacity, not its definition. Higher-order capacity consumes local capacity without taking its ownership.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-operational-workforce-coordination — Operational Workforce Coordination

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Make workforce availability, coverage and eligible operational work participation usable in coordination.

Make workforce availability, coverage and eligible operational work participation usable in coordination. Its constituent abilities distinguish Clinical Workforce Management; Clinical Rostering; Mobile Staff Management; On-Call Management; Work Allocation & Dispatch.

**Intent:** Support eligible operational participation using qualified availability and coverage information.

**Boundary:** Source rosters remain separately authoritative; operational dispatch does not acquire clinical-work allocation authority.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OPERATIONAL-WORKFORCE-COORDINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-workforce-qualification-development-enablement — Workforce Qualification & Development Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Represent learning, competence, professional-standing checks and separately governed operational access qualifications.

Represent learning, competence, professional-standing checks and separately governed operational access qualifications. Its constituent abilities distinguish Clinical Training & Education; Clinical Competency Management; Clinical Qualification Management; Clinical Supervision; Credential Management.

**Intent:** Keep professional standing, competence, supervision and operational-access evidence distinguishable when work is considered.

**Boundary:** Clinical qualifications and physical badges remain distinct CT3 responsibilities. Representation or checking does not grant privilege or become an authentication provider.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORKFORCE-QUALIFICATION-DEVELOPMENT-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-care-movement-transition-coordination — Care Movement & Transition Coordination

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Coordinate movement of people/materials and operational departure readiness across care boundaries.

Coordinate movement of people/materials and operational departure readiness across care boundaries. Its constituent abilities distinguish Patient Transport; Clinical Logistics Coordination; Discharge Management.

**Intent:** Coordinate the operational prerequisites and observed milestones of movement and departure without assuming clinical clearance.

**Boundary:** Physical movement, clinical handover, discharge clinical authorisation and care-responsibility transition remain distinct.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-MOVEMENT-TRANSITION-COORDINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-clinical-information-stewardship-enablement — Clinical Information Stewardship Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Maintain governed longitudinal synthesis and the associations needed to use its constituent clinical information safely.

Maintain governed longitudinal synthesis and the associations needed to use its constituent clinical information safely. Its constituent abilities distinguish Patient Clinical Record.

**Intent:** Make the integrated clinical account usable while keeping its source assertions and associated content attributable.

**Boundary:** The synthesis preserves what Harmonia received or managed without becoming the originator of the clinical facts.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-health-information-connectivity-enablement — Health Information Connectivity Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Make governed health information discoverable and exchangeable under explicit internal and external contracts.

Make governed health information discoverable and exchangeable under explicit internal and external contracts. Its constituent abilities distinguish Health Information Exchange; Health Information Access; Health Information Communication.

**Intent:** Let authorised participants find and exchange the information their contract permits.

**Boundary:** Query execution, transaction coordination and boundary protocol mediation are separate responsibilities; none transfers content ownership.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONNECTIVITY-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-semantic-reference-knowledge-enablement — Semantic & Reference Knowledge Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Govern the definitions, terminology and reference content that make healthcare information consistently interpretable.

Govern the definitions, terminology and reference content that make healthcare information consistently interpretable. Its constituent abilities distinguish Clinical Knowledge Services; Information Design Governance.

**Intent:** Keep structural meaning, terminology bindings and approved reference content consistent enough for shared interpretation.

**Boundary:** Definition semantics, mutable approved content and runtime policy decisions retain distinct ownership.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-SEMANTIC-REFERENCE-KNOWLEDGE-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-information-protection-accountability-enablement — Information Protection & Accountability Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Apply information-use policy and preserve the accountability context needed for governed health-information activity.

Apply information-use policy and preserve the accountability context needed for governed health-information activity. Its constituent abilities distinguish Health Information Control.

**Intent:** Make information use both governed and explainable through policy, attributable rules and significant evidence.

**Boundary:** Patient directives remain with Client Privacy; runtime authorisation does not become external authentication or clinical authority.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-secondary-health-information-use-enablement — Health Informatics Management

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Intent:** Establish and govern semantically meaningful health-information assets suitable for downstream research, population-health reporting and service planning in an adjacent analytical environment.

**Boundary:** Harmonia is adjacent to a Big Data/analytical environment. It establishes governed assets and reporting inputs; it does not become that analytical platform.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-collaborative-interaction-enablement — Collaborative Interaction Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Support contextual discussion and human interaction with governed information and activity.

Support contextual discussion and human interaction with governed information and activity. Its constituent abilities distinguish Clinical Collaboration; General Collaboration; Presentation Services.

**Intent:** Give participants a useful discussion and interaction context without turning the communication medium into a clinical authority.

**Boundary:** Collaboration control, collaboration operations and general presentation remain distinct; a discussion or screen is not a new clinical fact.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-COLLABORATIVE-INTERACTION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-operational-activity-temporal-enablement — Operational Activity & Temporal Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** superseded

**Definition:** Progress governed operational work in identifiable activity and temporal contexts.

Progress governed operational work in identifiable activity and temporal contexts. Its constituent abilities distinguish Workflow & Activity Coordination; Calendar Management.

**Intent:** Keep work progression and its timing intelligible across human and synthetic undertakings.

**Boundary:** Work Order is human doing, To Do is human review/update/approval and Task is synthetic work. Projection does not own authoritative bookings or rosters.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-assurance-specification-enablement — Assurance Specification

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Intent:** Model how satisfaction will be assured and maintain applicable, temporally qualified criteria.

**Boundary:** Modelling and criteria stewardship do not approve governing requirements or perform independent conclusion-making.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-independent-assurance-evaluation-enablement — Independent Assurance Evaluation Enablement

**Type / tier:** capability / CT2

**Reconciliation standing:** pending adjudication

**Definition:** Associate trustworthy evidence, assess it and adjudicate independently reportable findings about governed service behaviour.

Associate trustworthy evidence, assess it and adjudicate independently reportable findings about governed service behaviour. Its constituent abilities distinguish Governed Assurance.

**Intent:** Produce findings supported by applicable evidence and criteria while retaining inability to conclude.

**Boundary:** The subject can supply evidence but cannot control its conclusion. Evidence insufficiency remains distinct from execution uncertainty and clinical judgement.

**Last allocated structural identity — historical:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-associated-clinical-binary-content-enablement — Linked Information Curation

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

**Intent:** Curate associations to information and binary content needed to interpret and use a clinical record, retaining custody and source-authority distinctions.

**Boundary:** Association is not physical custody. Persistence authority, source clinical meaning and external repository authority remain separate.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-information-authority-representation — Information Authority Representation

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Represent the source, credibility and qualified authority standing of competing health-information assertions.

A persisted assertion may be authoritative, informational or anecdotal. This ability carries that standing into interpretation without treating transport reliability, freshness or successful commit as proof of credibility.

**Intent:** Keep an assertion technical acceptance separate from the standing of the source that asserted it.

**Boundary:** Classification does not originate clinical facts or universally adjudicate conflicts. Policy determines how qualified assertions are used.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-governed-reference-dataset-administration — Governed Reference Dataset Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

**Intent:** Administer approved runtime reference-dataset content and revisions separately from the reusable models defining its meaning.

**Boundary:** Does not own external ontology authority, execute authorisation policy or establish durable truth independently of the normal persistence boundary.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-significant-activity-audit-capture — Significant Activity Audit Capture

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Capture required security- and information-significant activity as governed, attributable audit evidence.

Mandatory evidence capture has its own policy-governed platform authority and must remain possible when the audited identity is denied an action. The captured fact is distinguished from debug output and the identity whose activity is recorded.

**Intent:** Ensure required significant activity evidence can be captured under the authority specifically assigned to that responsibility.

**Boundary:** The specific intrinsic authority for mandatory audit capture is not generalised to other activities. Capture is not an assurance finding and cannot expose unmasked PHI in diagnostic logs.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-information-lineage-stewardship — Data Provenance Capture

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

**Intent:** Capture meaningful source attribution and transformation lineage for governed information content without transferring originating authority.

**Boundary:** Lineage does not grant originating authority, decide clinical correctness or replace activity audit and independent assurance.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-authorised-research-information-provision — Research Information Curation

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Intent:** Establish and curate governed data assets suitable for explicitly authorised research use, retaining source attribution and applicable release constraints.

**Boundary:** Does not grant ethics approval, recruit trial participants, manage trials or become the authority for derived research conclusions.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-population-health-notification-enablement — Population Health Information Reporting

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Intent:** Establish and provide governed information assets and reporting inputs for population-health purposes, including applicable notification obligations.

**Boundary:** Does not determine population-health findings, create statutory obligations or equate transport delivery with discharge of notification duty.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-POPULATION-HEALTH-NOTIFICATION-ENABLEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-service-planning-information-provision — Service Planning Data Management

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Intent:** Establish and maintain governed information assets suitable for health-service planning, modelling and analysis.

**Boundary:** Observed utilisation, operational capacity and planned capacity are distinct; information provision does not commission services or allocate resources.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-SERVICE-PLANNING-INFORMATION-PROVISION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-governance-requirement-publication-support — Governance Requirement Publication Support

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Represent and publish versioned governing requirements for use by authorised operational and assurance consumers.

Consumers need an identifiable statement of which rule applies and who established it. This proposal fills BC-17 information support without giving the platform authority to make legislation, approve governance requirements or reinterpret their force.

**Intent:** Let operational and assurance consumers identify the governing statement and applicability basis they actually used.

**Boundary:** Governance approval remains externally mandated; operational policy, assurance criteria and requirement authority are not interchangeable.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-GOVERNANCE-REQUIREMENT-PUBLICATION-SUPPORT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-entity-specific-operational-coordination — Entity-Specific Operational Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Coordinate governed operational progression relative to one represented entity, including interested behaviour participation and controlled recovery.

Activity concerning the same patient, practitioner or bed needs a coherent entity-relative frame. Interest fan-out, serial progression, fresh information context and uncertain effects remain distinguishable from generic multi-work-unit progression.

**Intent:** Keep the progression of one represented entity coherent across interested operational behaviours.

**Boundary:** Entity coordination does not own all entity information, determine clinical work or become the source of clinical decisions. No Twin component allocation follows from this proposal.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-governed-operational-activity-design — Governed Operational Activity Design

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Define the information context, expected outcomes, continuation conditions and housekeeping of governed operational activity.

An activity needs a fulfilment definition even when only one bounded execution unit is involved. This capability specifies its semantic envelope while execution and event delivery remain separate responsibilities.

**Intent:** Make the expected information and fulfilment basis of an undertaking explicit before it executes.

**Boundary:** An activity definition is neither an orchestrator nor a choreographer. It does not choose clinical care or dynamically invent execution logic.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BEC-service-offering-readiness-coordination — Service Offering Readiness Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** pending adjudication

**Definition:** Assemble the dependency evidence needed to determine and communicate whether a defined service offering is operationally ready in a particular context.

Collective readiness depends on suitable workforce, spaces, equipment, materials and supporting services. This proposal evaluates their joint availability for an offering without treating one available bed, an operating-hours declaration or aggregate capacity as service readiness.

**Intent:** Distinguish collective service deliverability from nominal availability or readiness of any single dependency.

**Boundary:** Contributing capabilities retain source readiness and resource management. Clinical go/no-go decisions and service governance are not granted by this information-based coordination.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-clinical-finding-interpretation — Clinical Finding Interpretation

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Interpret observed clinical findings and their significance for the individual care concern.

Interpret observed clinical findings and their significance for the individual care concern. The proposed decomposition separates this outcome from the other responsibilities within Clinical Determination.

**Intent:** Make the clinical significance of observations intelligible before diagnostic or response decisions are taken.

**Boundary:** Does not itself establish a diagnosis, prescribe intervention or confer this clinical responsibility on Harmonia.

**Current structural identity:** CT1-INDIVIDUAL-CARE-DELIVERY.CT2-CLINICAL-DETERMINATION.CT3-BC-CLINICAL-FINDING-INTERPRETATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-diagnosis-problem-determination — Diagnosis & Problem Determination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Establish and qualify the clinical problems or diagnoses applicable to an individual.

Establish and qualify the clinical problems or diagnoses applicable to an individual. The proposed decomposition separates this outcome from the other responsibilities within Clinical Determination.

**Intent:** Establish the clinically accountable problem statement that subsequent care decisions address.

**Boundary:** Clinical determination remains with the mandated professional; a coded record is not evidence that Harmonia made the diagnosis.

**Current structural identity:** CT1-INDIVIDUAL-CARE-DELIVERY.CT2-CLINICAL-DETERMINATION.CT3-BC-DIAGNOSIS-PROBLEM-DETERMINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-health-risk-prognostic-determination — Health Risk & Prognostic Determination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Establish clinically meaningful risk and expected-course judgements for an individual.

Establish clinically meaningful risk and expected-course judgements for an individual. The proposed decomposition separates this outcome from the other responsibilities within Clinical Determination.

**Intent:** Make anticipated clinical risk and course explicit for the individual care concern.

**Boundary:** Risk/prognostic judgement is distinct from operational urgency, source-information authority and platform assurance.

**Current structural identity:** CT1-INDIVIDUAL-CARE-DELIVERY.CT2-CLINICAL-DETERMINATION.CT3-BC-HEALTH-RISK-PROGNOSTIC-DETERMINATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-care-goal-definition — Care Goal Definition

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Establish the intended outcomes and priorities against which an individual care response is planned.

Establish the intended outcomes and priorities against which an individual care response is planned. The proposed decomposition separates this outcome from the other responsibilities within Individual Care Planning.

**Intent:** Establish the intended clinical benefit and priorities against which care responses can be considered.

**Boundary:** Goal definition remains distinct from selecting an intervention and from recording an externally agreed care plan.

**Current structural identity:** CT1-INDIVIDUAL-CARE-DELIVERY.CT2-INDIVIDUAL-CARE-PLANNING.CT3-BC-CARE-GOAL-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-care-response-planning — Care Response Planning

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Determine the intended clinical response and its sequencing in relation to agreed care goals.

Determine the intended clinical response and its sequencing in relation to agreed care goals. The proposed decomposition separates this outcome from the other responsibilities within Individual Care Planning.

**Intent:** Make the intended clinical response coherent in relation to the agreed care goals.

**Boundary:** Planning is distinct from intervention delivery and from the operational definition of a synthetic activity.

**Current structural identity:** CT1-INDIVIDUAL-CARE-DELIVERY.CT2-INDIVIDUAL-CARE-PLANNING.CT3-BC-CARE-RESPONSE-PLANNING

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-medication-compounding-preparation — Medication Compounding & Preparation

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Prepare or compound a clinically required medication formulation under the applicable professional and product-governance mandate.

Prepare or compound a clinically required medication formulation under the applicable professional and product-governance mandate. The proposed decomposition separates this outcome from the other responsibilities within Medication & Pharmacy Services.

**Intent:** Provide the required medication formulation when ordinary supply alone does not satisfy the authorised preparation need.

**Boundary:** Preparation differs from dispensing and administration; this proposal assigns no compounding activity to Harmonia.

**Current structural identity:** CT1-DIAGNOSTIC-THERAPEUTIC-CLINICAL-SUPPORT-SERVICES.CT2-MEDICATION-PHARMACY-SERVICES.CT3-BC-MEDICATION-COMPOUNDING-PREPARATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-BC-service-delivery-preparedness-management — Service Delivery Preparedness Management

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Establish whether service offerings can be delivered in their intended operating contexts through the combined standing of their dependencies.

Preparedness asks whether the service as a whole can operate, distinct from scheduling capacity or readiness of a single place. This is a proposed missing business frame for the already identified readiness concern.

**Intent:** Recognise whether the whole service can actually operate in its intended context.

**Boundary:** Does not own the dependencies, their management or clinical execution.

**Current structural identity:** CT1-CARE-ACCESS-COORDINATION.CT2-BC-SERVICE-DELIVERY-PREPAREDNESS-MANAGEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-governed-information-operations — Governed Information Operations

**Type / tier:** enterprise_capability / CT1

**Reconciliation standing:** current

**Definition:** Maintain identifiable, meaningful, context-qualified and retrievable information through valid state progression.

Entity integrity, context, lifecycle, information maintenance, discovery and semantic conformance are constituent reusable abilities; none is an implementation subsystem.

**Intent:** Keep information identity, meaning, context, state and retrieval compatible across diverse healthcare uses.

**Boundary:** Information management and semantic governance do not grant originating domain authority or erase active/durable state separation.

**Current structural identity:** CT1-EC-GOVERNED-INFORMATION-OPERATIONS

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-coordinated-activity-interaction — Coordinated Activity & Interaction

**Type / tier:** enterprise_capability / CT1

**Reconciliation standing:** current

**Definition:** Enable governed interaction, event participation, boundary exchange and accountable activity progression.

Requests, events, work and human interaction are related ways to cause or observe change, with distinct contracts and progression responsibilities.

**Intent:** Make requests, event participation, work and human interaction progress according to their different contracts.

**Boundary:** Exchange is not execution, notification is not outcome, and interaction is not clinical authority.

**Current structural identity:** CT1-EC-COORDINATED-ACTIVITY-INTERACTION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-trust-accountability-assurance — Trust, Accountability & Assurance

**Type / tier:** enterprise_capability / CT1

**Reconciliation standing:** current

**Definition:** Make governed activity permissible, attributable, operationally dependable and independently assessable.

Policy, evidence, resilience and independent assurance contribute to confidence through different responsibilities. Keeping them as peers prevents technical success or recorded evidence from becoming a business assurance conclusion.

**Intent:** Keep permission, attribution, operational integrity and independent confidence distinguishable and usable together.

**Boundary:** Cross-cutting participation does not distribute semantic ownership, mandate centralisation or authorise the assured subject to determine its own conclusion.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-active-information-availability — Active Information Availability

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Maintain responsive, reconstructable active information and coordinate its safe use.

Active representation, observation and convergence support availability while preserving an explicit trust basis for use.

**Intent:** Provide responsive managed use without promoting active representation into durable truth.

**Boundary:** An active generation is not an authoritative version; failure to converge does not establish valid active state.

**Current structural identity:** CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-ACTIVE-INFORMATION-AVAILABILITY

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-authoritative-durable-state-establishment — Authoritative Durable State Establishment

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Atomically establish durable managed state and its authoritative version progression.

Durable acceptance gives managed information an explicit committed state from which applicable active representations can be reconstructed.

**Intent:** Give managed change an explicit committed state and authoritative progression basis.

**Boundary:** Commit is distinct from semantic source authority and from successful active convergence; applications do not gain a raw persistence path.

**Current structural identity:** CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-AUTHORITATIVE-DURABLE-STATE-ESTABLISHMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-binary-content-association-resolution — Binary Content Association & Resolution

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Maintain content associations and custody knowledge sufficient for governed payload resolution, retrieval and assembly.

Document attachments, diagnostic media and operational artefacts share a need to locate usable content while distinguishing logical meaning from physical possession.

**Intent:** Make logical information and its usable payloads consistently associable while retaining custody boundaries.

**Boundary:** No universal binary custody, storage topology or persistence authority is implied.

**Current structural identity:** CT1-EC-GOVERNED-INFORMATION-OPERATIONS.CT2-EC-04.CT3-EC-BINARY-CONTENT-ASSOCIATION-RESOLUTION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-operation-policy-resolution — Operation Policy Resolution

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Resolve effective, temporally applicable permission criteria for an established identity and governed operation.

Permission depends on controlled criteria, role assignments, action and information scope; revocation is distinct from natural expiry.

**Intent:** Determine the effective permitted action under the applicable identity, information and temporal criteria.

**Boundary:** Does not authenticate identity or grant permission from caller-controlled tags or mere role names.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-06.CT3-EC-OPERATION-POLICY-RESOLUTION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-operation-bound-policy-enforcement — Operation-Bound Policy Enforcement

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Enforce resolved policy constraints at the boundary where a governed action is attempted.

Distributed participants apply the applicable effective authorisation and fail closed when authority is absent or untrustworthy.

**Intent:** Ensure an operation cannot proceed merely because its caller omitted or bypassed a voluntary policy check.

**Boundary:** Enforcement participation does not distribute policy ownership or make security context persistent content.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-06.CT3-EC-OPERATION-BOUND-POLICY-ENFORCEMENT

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-information-attribution-lineage — Information Attribution & Lineage

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Preserve source attribution and significant transformation relationships for managed information.

Lineage explains how an assertion reached its present form without turning every infrastructure event into provenance.

**Intent:** Explain the information-significant origin and transformation of an assertion.

**Boundary:** Does not infer clinical credibility from a successful transport or establish independent assurance findings.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-07.CT3-EC-INFORMATION-ATTRIBUTION-LINEAGE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-governed-activity-audit-evidence — Governed Activity Audit Evidence

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Capture required significant activity and decision evidence under its applicable governed capture authority.

Denied actions and successful mutations may both require trustworthy evidence; the audited identity and capture authority remain distinct.

**Intent:** Preserve required significant action evidence under its proper capture authority.

**Boundary:** Mandatory capture authority is specific to the established audit responsibility, not a platform-wide permission shortcut.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-07.CT3-EC-GOVERNED-ACTIVITY-AUDIT-EVIDENCE

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-operational-fulfilment-definition — Operational Fulfilment Definition

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Define the context requirements, expected outcomes and housekeeping of an operational undertaking.

The semantic execution envelope applies to one-step and multi-step activity and stays distinct from dispatch and supervision.

**Intent:** Keep the semantic promise of an undertaking separate from the machinery that executes it.

**Boundary:** A fulfilment definition does not execute, orchestrate or choreograph its activity.

**Current structural identity:** CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-OPERATIONAL-FULFILMENT-DEFINITION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-governed-activity-execution-control — Governed Activity Execution Control

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Instantiate, dispatch, supervise and progress authorised operational undertakings with explicit outcome standing.

Reusable execution control respects work-unit archetype, participation and uncertain effects rather than equating dispatch with completion.

**Intent:** Progress accountable operational work without equating dispatch, execution and successful outcome.

**Boundary:** Does not own underlying clinical-work meaning, clinical decisions or originator business authority.

**Current structural identity:** CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-GOVERNED-ACTIVITY-EXECUTION-CONTROL

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-entity-relative-operational-progression — Entity-Relative Operational Progression

**Type / tier:** enterprise_capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate serial, entity-relative operational inputs and their interested behaviours.

Patient, practitioner and bed concerns recur across different business contexts; each can need a coherent progression frame without one central entity service.

**Intent:** Maintain a coherent operational reference frame as one entity reacts to successive inputs.

**Boundary:** Interest fan-out is distinct from intra-entity concurrency; entity progression does not acquire originating facts.

**Current structural identity:** CT1-EC-COORDINATED-ACTIVITY-INTERACTION.CT2-EC-10.CT3-EC-ENTITY-RELATIVE-OPERATIONAL-PROGRESSION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-EC-information-authority-qualification — Information Authority Qualification

**Type / tier:** enterprise_capability / CT2

**Reconciliation standing:** current

**Definition:** Represent and qualify the source-authority and credibility standing of information independently of transport, persistence and concurrency.

Identity correlation, clinical-history synthesis, reference-content use and assurance evidence all need to distinguish the standing of an assertion from technical acceptance. This recurring ability is insufficiently explicit in the existing fourteen EC definitions and is proposed as a new peer responsibility.

**Intent:** Keep source credibility explicit even when the technical handling of competing assertions is equally valid.

**Boundary:** This is not independent service assurance, a universal conflict arbiter or clinical truth determination. Existing EC-13 governs authority nomenclature; this capability applies and preserves the qualified standing.

**Current structural identity:** CT1-EC-TRUST-ACCOUNTABILITY-ASSURANCE.CT2-EC-INFORMATION-AUTHORITY-QUALIFICATION

**Provenance:** Inferred from bounded responsibilities and their semantic decomposition

**Reviewed reconciliation:** Inferred from bounded responsibilities and their semantic decomposition

## HCM-FT-identity-source-correction-lineage — Identity Source & Correction Lineage

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Retain source-attributed identity assertions and externally decided correction history.

Identifier correlations can change when an upstream authority corrects a relationship. Reviewers must be able to distinguish the source decision, its effective standing and the managed links affected.

**Intent:** Identifier correlations can change when an upstream authority corrects a relationship. Reviewers must be able to distinguish the source decision, its effective standing and the managed links affected.

**Boundary:** No probabilistic matching, golden-record selection or autonomous merge authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-ADMINISTRATION.CT3-BEC-PERSON-IDENTITY.FEAT-STEP2-IDENTITY-SOURCE-CORRECTION-LINEAGE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-provider-directory-discovery-resolution — Provider Directory Discovery & Resolution

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Expose qualified practitioner discovery using identifiers, profile criteria and effective standing.

Routing and care participation need to find a described provider matching stated criteria, beyond merely ingesting a national identifier or updating a profile.

**Intent:** Routing and care participation need to find a described provider matching stated criteria, beyond merely ingesting a national identifier or updating a profile.

**Boundary:** Discovery supplies information; it does not determine privilege or availability.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-PRACTITIONER.FEAT-STEP2-PROVIDER-DIRECTORY-DISCOVERY-RESOLUTION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-device-association-lineage — Device Association Lineage

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Preserve the attributed history and effective basis of device-to-subject/place associations.

A telemetry record needs to be interpreted against the association applicable when it arose, rather than the device current location alone.

**Intent:** A telemetry record needs to be interpreted against the association applicable when it arose, rather than the device current location alone.

**Boundary:** Association history does not interpret physiological observations or prove clinical correctness.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION.FEAT-STEP2-DEVICE-ASSOCIATION-LINEAGE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-referral-destination-coordination — Referral Destination Coordination

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Coordinate qualified referral destination resolution and convey the request under its applicable disposition contract.

Referral intake and status capture leave a distinct routing responsibility: finding the intended service and preserving redirection without turning technical delivery into assumed care responsibility.

**Intent:** Referral intake and status capture leave a distinct routing responsibility: finding the intended service and preserving redirection without turning technical delivery into assumed care responsibility.

**Boundary:** Does not make clinical triage decisions or infer Transfer of Care from acceptance.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION.FEAT-STEP2-REFERRAL-DESTINATION-COORDINATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-encounter-history-retrieval — Encounter History Retrieval

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Expose qualified episode, movement and encounter-history context.

Consumers need to interpret prior care-place and episode relationships, not just the latest encounter state; historical retrieval remains bounded by source attribution and access policy.

**Intent:** Consumers need to interpret prior care-place and episode relationships, not just the latest encounter state; historical retrieval remains bounded by source attribution and access policy.

**Boundary:** A historical view does not retrospectively alter clinical events or acquire longitudinal-record ownership.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT.FEAT-STEP2-ENCOUNTER-HISTORY-RETRIEVAL

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-order-outcome-association — Order Outcome Association

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Associate an externally established outcome with the applicable bounded order and its progression.

Execution completion and returned outcome are different checkpoints. This Feature makes the result link explicit without equating an order acknowledgement with an outcome.

**Intent:** Execution completion and returned outcome are different checkpoints. This Feature makes the result link explicit without equating an order acknowledgement with an outcome.

**Boundary:** Does not interpret diagnostic findings or declare fulfilment from transport delivery alone.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION.FEAT-STEP2-ORDER-OUTCOME-ASSOCIATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-diagnostic-preliminary-corrected-report-handling — Diagnostic Preliminary & Corrected Report Handling

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Receive, qualify and distribute preliminary, corrected and addendum diagnostic information with its relationship to prior reports.

The finalised-report Feature remains unchanged. The broader documented diagnostic behaviour needs a separate frame for information whose preliminary or correction standing is material.

**Intent:** The finalised-report Feature remains unchanged. The broader documented diagnostic behaviour needs a separate frame for information whose preliminary or correction standing is material.

**Boundary:** Does not originate diagnostic interpretation or silently overwrite a previously received report.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT.FEAT-STEP2-DIAGNOSTIC-PRELIMINARY-CORRECTED-REPORT-HANDLING

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-care-goal-plan-progression-representation — Care Goal & Plan Progression Representation

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent externally agreed care goals, participants and plan changes with their effective standing.

Plan ingestion/distribution is complemented by the ability to distinguish the applicable goal and plan context as a shared plan progresses.

**Intent:** Plan ingestion/distribution is complemented by the ability to distinguish the applicable goal and plan context as a shared plan progresses.

**Boundary:** Representing a goal does not choose treatment, determine clinical completion or grant plan approval authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-CARE-PLAN-LIFECYCLE-COORDINATION.FEAT-STEP2-CARE-GOAL-PLAN-PROGRESSION-REPRESENTATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-clinical-information-historical-reconstruction — Clinical Information Historical Reconstruction

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Reconstruct what information Harmonia received, knew or managed at a relevant historical basis.

Current state and historical operational truth answer different questions. Reconstruction retains superseded and corrected knowledge without rewriting the earlier record.

**Intent:** Current state and historical operational truth answer different questions. Reconstruction retains superseded and corrected knowledge without rewriting the earlier record.

**Boundary:** Does not infer that historical information was clinically true or that a present source state applied in the past.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION.FEAT-STEP2-CLINICAL-INFORMATION-HISTORICAL-RECONSTRUCTION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-binary-content-resolution-assembly — Binary Content Resolution & Assembly

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Resolve governed payload associations and retrieve or assemble the content required by a requesting information context.

A logical clinical item may reference externally held or multipart payloads. The Feature supplies a usable content result while retaining custody and missing-content standing.

**Intent:** A logical clinical item may reference externally held or multipart payloads. The Feature supplies a usable content result while retaining custody and missing-content standing.

**Boundary:** No assumption of local copying, shared storage, universal content retention or originating clinical authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FEAT-STEP2-BINARY-CONTENT-RESOLUTION-ASSEMBLY

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-information-authority-source-standing-representation — Information Authority & Source Standing Representation

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Attach and preserve qualified source-authority and credibility standing for managed assertions.

The user of an assertion needs to distinguish authoritative, informational and anecdotal standing even when each was technically valid and durably accepted.

**Intent:** The user of an assertion needs to distinguish authoritative, informational and anecdotal standing even when each was technically valid and durably accepted.

**Boundary:** Authority classification is not a clinical truth judgement or a universal rule for choosing competing assertions.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION.FEAT-STEP2-INFORMATION-AUTHORITY-SOURCE-STANDING-REPRESENTATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-governed-dataset-content-revision — Governed Dataset Content Revision

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Review, accept and expose authorised changes to mutable reference content with effective revision standing.

A model definition can remain stable while approved terminology membership or mapping content changes. This Feature represents that operational governance without making every definition runtime mutable.

**Intent:** A model definition can remain stable while approved terminology membership or mapping content changes. This Feature represents that operational governance without making every definition runtime mutable.

**Boundary:** Does not invent an approval authority or bypass normal durable establishment.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FEAT-STEP2-GOVERNED-DATASET-CONTENT-REVISION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-mandatory-significant-activity-audit-capture — Mandatory Significant Activity Audit Capture

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Capture required significant activity evidence using the specifically governed authoritative audit-capture boundary.

The actor whose action is recorded need not be entitled to persist the audit. Required capture uses its own established authority and failure can block the governed operation.

**Intent:** The actor whose action is recorded need not be entitled to persist the audit. Required capture uses its own established authority and failure can block the governed operation.

**Boundary:** Specific audit authority is not borrowed from the audited identity or generalised to other operations.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.FEAT-STEP2-MANDATORY-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-information-transformation-lineage — Information Transformation Lineage

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Record meaningful source-to-derived information relationships and transformation attribution.

A consumer should be able to explain which source assertions contributed to a managed representation and what significant transformation occurred.

**Intent:** A consumer should be able to explain which source assertions contributed to a managed representation and what significant transformation occurred.

**Boundary:** Routine cache reconstruction, retries and diagnostics do not automatically become permanent lineage.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FEAT-STEP2-INFORMATION-TRANSFORMATION-LINEAGE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-trusted-identity-context-intake — Trusted Identity Context Intake

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Accept and qualify externally authenticated identity context for Harmonia-governed operations.

The boundary between WHO and WHAT remains explicit: an external identity assertion is verified as trusted input before internal authorisation is resolved.

**Intent:** The boundary between WHO and WHAT remains explicit: an external identity assertion is verified as trusted input before internal authorisation is resolved.

**Boundary:** Harmonia does not become an authentication or credential-issuance provider.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-TRUSTED-IDENTITY-CONTEXT-INTAKE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-governed-policy-assignment-revocation — Governed Policy Assignment & Revocation

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Manage approved permission criteria and identity/role assignments, including temporal applicability and revocation.

An assignment becoming inapplicable by expiry differs from an explicit revocation; both must affect the effective authority used for operations.

**Intent:** An assignment becoming inapplicable by expiry differs from an explicit revocation; both must affect the effective authority used for operations.

**Boundary:** Management does not grant unconstrained administrative authority or derive permission from resource tags.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-GOVERNED-POLICY-ASSIGNMENT-REVOCATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-operation-bound-authorisation-enforcement — Operation-Bound Authorisation Enforcement

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Apply effective policy at the governed action boundary and reject actions lacking sufficient authority.

Consistent enforcement is required across mutation, access, execution and administration rather than relying on each caller to voluntarily invoke a check.

**Intent:** Consistent enforcement is required across mutation, access, execution and administration rather than relying on each caller to voluntarily invoke a check.

**Boundary:** Consumes trusted identity and criteria; it does not authenticate or establish clinical privilege from directory roles.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-HEALTH-INFORMATION-CONTROL.FEAT-STEP2-OPERATION-BOUND-AUTHORISATION-ENFORCEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-information-definition-schema-governance — Information Definition & Schema Governance

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Govern structural definitions, data dictionaries and constraint meaning.

Existing definition-governance behaviour needs a distinct Feature frame from general semantic-model authorship and runtime validation.

**Intent:** Existing definition-governance behaviour needs a distinct Feature frame from general semantic-model authorship and runtime validation.

**Boundary:** Governed definitions do not own runtime instances or their clinical facts.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-INFORMATION-DEFINITION-SCHEMA-GOVERNANCE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-terminology-binding-governance — Terminology Binding Governance

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Govern how information definitions are bound to applicable terminology and value sets.

Binding a data element to a vocabulary is distinct from resolving a patient code or changing the runtime membership of a dataset.

**Intent:** Binding a data element to a vocabulary is distinct from resolving a patient code or changing the runtime membership of a dataset.

**Boundary:** Definition of a binding does not take external ontology authority or execute policy.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-TERMINOLOGY-BINDING-GOVERNANCE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-exchange-profile-governance — Exchange Profile Governance

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Govern the semantic and conformance constraints of an interoperability profile.

An external contract must state which representations and extensions are permitted; validation applies that contract but does not define it.

**Intent:** An external contract must state which representations and extensions are permitted; validation applies that contract but does not define it.

**Boundary:** Profile governance does not transmit messages or publish internal operational metadata.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN.FEAT-STEP2-EXCHANGE-PROFILE-GOVERNANCE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-entity-relative-activity-coordination — Entity-Relative Activity Coordination

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Sequence applicable behaviours for an identifiable entity input while preserving its operational reference frame.

Interest matching can select multiple behaviours while the entity progresses one input at a time. Each behaviour uses a fresh managed information context.

**Intent:** Interest matching can select multiple behaviours while the entity progresses one input at a time. Each behaviour uses a fresh managed information context.

**Boundary:** Fan-out is not concurrent execution; clinical decisions and originating information remain with their owners.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FEAT-STEP2-ENTITY-RELATIVE-ACTIVITY-COORDINATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-entity-behaviour-failure-recovery-governance — Entity Behaviour Failure & Recovery Governance

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Apply explicit behaviour-failure dispositions and controlled, authorised resumption for entity-relative processing.

Failure may stop a behaviour, an input or the entity progression and may leave effects uncertain. Resumption must preserve that distinction rather than imply rollback or automatic safe retry.

**Intent:** Failure may stop a behaviour, an input or the entity progression and may leave effects uncertain. Resumption must preserve that distinction rather than imply rollback or automatic safe retry.

**Boundary:** Recovery cannot silently declare no effects, bypass governance or resume a halted entity automatically.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FEAT-STEP2-ENTITY-BEHAVIOUR-FAILURE-RECOVERY-GOVERNANCE

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-operational-activity-context-definition — Operational Activity Context Definition

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Define the anticipated information context and expected fulfilment basis of an operational undertaking.

Even a one-unit activity needs a declared information and outcome frame. The definition is separate from loading values or running the business logic.

**Intent:** Even a one-unit activity needs a declared information and outcome frame. The definition is separate from loading values or running the business logic.

**Boundary:** No dynamically invented resource set or orchestration engine is implied.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FEAT-STEP2-OPERATIONAL-ACTIVITY-CONTEXT-DEFINITION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-activity-outcome-continuation-rules — Activity Outcome & Continuation Rules

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Define how output roles, expected outcomes and continuation conditions are interpreted for an undertaking.

An output may contribute to completion, subsequent activity or retained evidence under different rules. Those meanings need to be specified rather than inferred from a returned payload.

**Intent:** An output may contribute to completion, subsequent activity or retained evidence under different rules. Those meanings need to be specified rather than inferred from a returned payload.

**Boundary:** Completion policy is not business outcome truth; uncertain effects remain explicit.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FEAT-STEP2-ACTIVITY-OUTCOME-CONTINUATION-RULES

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-uncertain-activity-outcome-reconciliation — Uncertain Activity Outcome Reconciliation

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Reconcile indeterminate activity effects through authoritative observation before establishing a safe next disposition.

A timeout or absent response leaves a materially significant outcome unknown. Reconciliation can confirm an effect, establish absence or retain uncertainty without automatically retrying the original work.

**Intent:** A timeout or absent response leaves a materially significant outcome unknown. Reconciliation can confirm an effect, establish absence or retain uncertainty without automatically retrying the original work.

**Boundary:** Execution uncertainty differs from insufficient assurance evidence; a retry does not prove the earlier attempt had no effect.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-STEP2-UNCERTAIN-ACTIVITY-OUTCOME-RECONCILIATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-controlled-administrative-recovery — Controlled Administrative Recovery

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Perform authorised recovery as identifiable governed activity through normal execution and evidence boundaries.

Operators may need to resume, reconcile or repair an operational concern. That is first-class work with permission and outcome standing, rather than direct mutation through a console.

**Intent:** Operators may need to resume, reconcile or repair an operational concern. That is first-class work with permission and outcome standing, rather than direct mutation through a console.

**Boundary:** Administrative interaction grants no raw cache/database or subsystem-internal bypass.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FEAT-STEP2-CONTROLLED-ADMINISTRATIVE-RECOVERY

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-service-readiness-dependency-assessment — Service Readiness Dependency Assessment

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Associate time- and context-qualified dependency evidence to establish a bounded readiness view for an offering.

An offering may be in operating hours but not deliverable because staffing, equipment, materials or supporting services are missing. A readiness view retains insufficiency and the owners of those dependency assertions.

**Intent:** An offering may be in operating hours but not deliverable because staffing, equipment, materials or supporting services are missing. A readiness view retains insufficiency and the owners of those dependency assertions.

**Boundary:** Readiness evidence does not originate clinical clearance or transfer resource management.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.FEAT-STEP2-SERVICE-READINESS-DEPENDENCY-ASSESSMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-authorised-research-release-assembly — Authorised Research Release Assembly

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Assemble and qualify the existing information allowed by a particular authorised research purpose.

The release carries purpose and applicable privacy/consent/use constraints; the requester does not gain unrestricted clinical-record access by having a research role.

**Intent:** The release carries purpose and applicable privacy/consent/use constraints; the requester does not gain unrestricted clinical-record access by having a research role.

**Boundary:** No ethics approval, de-identification guarantee or clinical-trial management is inferred.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-AUTHORISED-RESEARCH-RELEASE-ASSEMBLY

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-population-notification-obligation-tracking — Population Notification Obligation Tracking

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Track the relation between an applicable notification obligation, the supplied source evidence and observed notification disposition.

Sending a required notification and discharging the governing obligation are different assertions. This Feature keeps the explicit contract and indeterminate responses visible.

**Intent:** Sending a required notification and discharging the governing obligation are different assertions. This Feature keeps the explicit contract and indeterminate responses visible.

**Boundary:** Does not define notifiability, author statutory rules or own public-health response.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-POPULATION-NOTIFICATION-OBLIGATION-TRACKING

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-demand-capacity-planning-projection — Demand & Capacity Planning Projection

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Project qualified observed demand, capacity and utilisation for authorised planning consumers.

Comparisons need service, temporal and context qualifiers so that planned future capacity is not confused with present operational capacity.

**Intent:** Comparisons need service, temporal and context qualifiers so that planned future capacity is not confused with present operational capacity.

**Boundary:** Projection does not establish a planning conclusion or allocate services and staff.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION.FEAT-STEP2-DEMAND-CAPACITY-PLANNING-PROJECTION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-governance-requirement-version-publication — Governance Requirement Version Publication

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Publish attributable versions and applicability of externally approved governing requirements.

Operational and assurance consumers need to identify the governing basis they used rather than silently substitute a current rule for a historical one.

**Intent:** Operational and assurance consumers need to identify the governing basis they used rather than silently substitute a current rule for a historical one.

**Boundary:** Publication does not confer requirement approval, authorisation policy or assurance-criteria authority.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-GOVERNANCE-REQUIREMENT-PUBLICATION-SUPPORT.FEAT-STEP2-GOVERNANCE-REQUIREMENT-VERSION-PUBLICATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-collaboration-interaction-capture-qualification — Collaboration Interaction Capture Qualification

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Qualify when a defined collaboration interaction creates a new governed information or activity consequence.

A message may convey existing information or an explicit interaction may derive a new managed consequence. The distinction preserves collaboration-native records while routing governed consequences to their accountable responsibility.

**Intent:** A message may convey existing information or an explicit interaction may derive a new managed consequence. The distinction preserves collaboration-native records while routing governed consequences to their accountable responsibility.

**Boundary:** Conversation presence, display or read receipt does not itself make a clinical decision, approval or final record.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION.FEAT-STEP2-COLLABORATION-INTERACTION-CAPTURE-QUALIFICATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-assurance-disposition-design-modelling — Assurance Disposition & Design Modelling

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Model how satisfaction of an applicable governed concern will be independently established.

This proposal provides a Feature frame for the established design responsibility, including disposition, criteria and evidence expectations, without creating an additional assurance Function.

**Intent:** This proposal provides a Feature frame for the established design responsibility, including disposition, criteria and evidence expectations, without creating an additional assurance Function.

**Boundary:** Modelling is distinct from requirement approval, subject management and evaluation.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-DESIGN.FEAT-STEP2-ASSURANCE-DISPOSITION-DESIGN-MODELLING

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-temporal-assurance-criteria-stewardship — Temporal Assurance Criteria Stewardship

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Manage identifiable assurance criteria and their relevant temporal and version applicability.

Evidence must be evaluated against the criteria applicable to its purpose and time rather than an unqualified current rule.

**Intent:** Evidence must be evaluated against the criteria applicable to its purpose and time rather than an unqualified current rule.

**Boundary:** Criteria stewardship does not approve governing requirements or select a universal result taxonomy.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SPECIFICATION-ENABLEMENT.CT3-BEC-ASSURANCE-CRITERIA-MANAGEMENT.FEAT-STEP2-TEMPORAL-ASSURANCE-CRITERIA-STEWARDSHIP

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-independent-evidence-assessment-adjudication — Independent Evidence Assessment & Adjudication

**Type / tier:** feature / FT

**Reconciliation standing:** pending adjudication

**Definition:** Independently establish assurance context, assess evidence and adjudicate findings under applicable criteria.

The Feature unifies the review frame while retaining the three distinct canonical Functions and the boundary between criterion-level assessment and reportable conclusion.

**Intent:** The Feature unifies the review frame while retaining the three distinct canonical Functions and the boundary between criterion-level assessment and reportable conclusion.

**Boundary:** No new assurance Function or clinical-adequacy mandate is introduced; insufficient evidence establishes neither satisfaction nor non-satisfaction.

**Last allocated structural identity — historical:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-INDEPENDENT-ASSURANCE-EVALUATION-ENABLEMENT.CT3-BEC-GOVERNED-ASSURANCE.FEAT-STEP2-INDEPENDENT-EVIDENCE-ASSESSMENT-ADJUDICATION

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-clinic-session-capacity-management — Clinic Session Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent available, committed and utilised session capacity under clinic-specific constraints.

Clinic capacity depends on session staffing and service mix as well as places; queue progression alone is insufficient.

**Intent:** Clinic capacity depends on session staffing and service mix as well as places; queue progression alone is insufficient.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION.FEAT-STEP2-CLINIC-SESSION-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-ward-care-capacity-management — Ward Care Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent ward care capacity in relation to staffing, care-place suitability and patient dependency.

A clean bed can remain unusable when staffing or patient needs constrain the ward; source clinical facts remain attributed.

**Intent:** A clean bed can remain unusable when staffing or patient needs constrain the ward; source clinical facts remain attributed.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION.FEAT-STEP2-WARD-CARE-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-theatre-session-capacity-management — Theatre Session Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent available and committed procedural-session capacity with its resource dependencies.

Theatre capacity involves session time, team capability, equipment and recovery provision rather than a count of theatres.

**Intent:** Theatre capacity involves session time, team capability, equipment and recovery provision rather than a count of theatres.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION.FEAT-STEP2-THEATRE-SESSION-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-emergency-treatment-capacity-management — Emergency Treatment Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent current emergency treatment capacity under space, staffing and acuity constraints.

Bay state and length of stay contribute evidence but do not alone establish the ability to accept another care demand.

**Intent:** Bay state and length of stay contribute evidence but do not alone establish the ability to accept another care demand.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION.FEAT-STEP2-EMERGENCY-TREATMENT-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-outpatient-appointment-capacity-management — Outpatient Appointment Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent usable outpatient session capacity from booking, staffing and local flow information.

The projection distinguishes scheduled slots from currently usable care capacity without taking source booking authority.

**Intent:** The projection distinguishes scheduled slots from currently usable care capacity without taking source booking authority.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION.FEAT-STEP2-OUTPATIENT-APPOINTMENT-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FT-mobile-service-capacity-management — Mobile Service Capacity Management

**Type / tier:** feature / FT

**Reconciliation standing:** current

**Definition:** Represent mobile service capacity with travel, coverage, work commitment and availability context.

Mobile availability cannot be interpreted as a simple on-duty count because distance, assignments and suitable skills affect deliverability.

**Intent:** Mobile availability cannot be interpreted as a simple on-duty count because distance, assignments and suitable skills affect deliverability.

**Boundary:** Local responsibility retains its capacity meaning; Service Capacity Management consumes it without acquiring ownership or imposing one common capacity model.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION.FEAT-STEP2-MOBILE-SERVICE-CAPACITY-MANAGEMENT

**Provenance:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

**Reviewed reconciliation:** Inferred from capability-specific behaviour, v0.3 inspection and current architectural evidence

## HCM-FN-resolve-associated-binary-content — Resolve Associated Binary Content

**Type / tier:** function / behaviour

**Reconciliation standing:** current

**Definition:** Resolve logical content associations to an authorised, custody-qualified retrieval basis.

Changes an unresolved content request into an identified retrieval basis or an explicit inability to resolve it.

**Intent:** Changes an unresolved content request into an identified retrieval basis or an explicit inability to resolve it.

**Boundary:** Does not assert payload existence, copy all content or establish clinical meaning.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-RESOLVE-ASSOCIATED-BINARY-CONTENT

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-assemble-associated-binary-content — Assemble Associated Binary Content

**Type / tier:** function / behaviour

**Reconciliation standing:** current

**Definition:** Retrieve and assemble the requested logical content from its permitted constituent payloads.

Moves a qualified retrieval request to an assembled result or explicit unavailable/partial standing.

**Intent:** Moves a qualified retrieval request to an assembled result or explicit unavailable/partial standing.

**Boundary:** No storage topology or universal local custody is selected.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.FN-ASSEMBLE-ASSOCIATED-BINARY-CONTENT

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-associated-binary-content-retrieval — Associated Binary Content Retrieval

**Type / tier:** service / behaviour

**Reconciliation standing:** current

**Definition:** Allow information-consuming capabilities to request resolution and retrieval of associated content.

The client contract returns authorised content or explicit incomplete/unavailable standing while retaining custody attribution.

**Intent:** The client contract returns authorised content or explicit incomplete/unavailable standing while retaining custody attribution.

**Boundary:** The client acquires neither physical repository access nor originating clinical authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-ASSOCIATED-CLINICAL-BINARY-CONTENT-ENABLEMENT.SV-ASSOCIATED-BINARY-CONTENT-RETRIEVAL

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-qualify-information-authority-standing — Qualify Information Authority Standing

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Associate source and credibility qualifications with an information assertion under applicable governed semantics.

Moves an unqualified received assertion to explicitly attributed authority standing without equating persistence success to credibility.

**Intent:** Moves an unqualified received assertion to explicitly attributed authority standing without equating persistence success to credibility.

**Boundary:** Does not adjudicate all competing claims or originate clinical truth.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-INFORMATION-STEWARDSHIP-ENABLEMENT.CT3-BEC-INFORMATION-AUTHORITY-REPRESENTATION.FN-QUALIFY-INFORMATION-AUTHORITY-STANDING

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-accept-governed-dataset-revision — Accept Governed Dataset Revision

**Type / tier:** function / behaviour

**Reconciliation standing:** current

**Definition:** Apply an explicitly authorised content revision and its effective standing through governed information-state progression.

Moves a reviewed change to the applicable approved content revision, retaining attribution and uncertainty if durable establishment cannot be confirmed.

**Intent:** Moves a reviewed change to the applicable approved content revision, retaining attribution and uncertainty if durable establishment cannot be confirmed.

**Boundary:** The Function does not invent who approves or become a persistence manager.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.FN-ACCEPT-GOVERNED-DATASET-REVISION

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-governed-reference-dataset-content-query — Governed Reference Dataset Content Query

**Type / tier:** service / behaviour

**Reconciliation standing:** current

**Definition:** Provide authorised consumers with approved content at an identified revision and applicability basis.

Consumers can resolve the membership or mapping content they actually used without treating build-time model definitions as runtime data.

**Intent:** Consumers can resolve the membership or mapping content they actually used without treating build-time model definitions as runtime data.

**Boundary:** Does not grant unrestricted dataset mutation or external terminology authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-GOVERNED-REFERENCE-DATASET-ADMINISTRATION.SV-GOVERNED-REFERENCE-DATASET-CONTENT-QUERY

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-capture-required-significant-audit-evidence — Capture Required Significant Audit Evidence

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Establish required significant activity evidence under the governed authoritative audit-capture responsibility.

Moves an auditable fact to confirmed captured standing or an explicit capture failure affecting the protected operation.

**Intent:** Moves an auditable fact to confirmed captured standing or an explicit capture failure affecting the protected operation.

**Boundary:** Specific capture authority is not borrowed from the originating identity and is not generalised.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.FN-CAPTURE-REQUIRED-SIGNIFICANT-AUDIT-EVIDENCE

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-mandatory-audit-evidence-capture — Mandatory Audit Evidence Capture

**Type / tier:** service / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Allow governed-operation boundaries to require authoritative capture of significant activity evidence.

The client contract distinguishes confirmed capture from inability to capture, allowing required operations to fail closed.

**Intent:** The client contract distinguishes confirmed capture from inability to capture, allowing required operations to fail closed.

**Boundary:** The exposed boundary does not permit callers to assign themselves audit platform authority.

**Last allocated structural identity — historical:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-PROTECTION-ACCOUNTABILITY-ENABLEMENT.CT3-BEC-SIGNIFICANT-ACTIVITY-AUDIT-CAPTURE.SV-MANDATORY-AUDIT-EVIDENCE-CAPTURE

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-record-significant-information-lineage — Record Significant Information Lineage

**Type / tier:** function / behaviour

**Reconciliation standing:** current

**Definition:** Associate a managed assertion with its attributed sources and information-significant transformation.

Changes an unexplained derived representation into an attributable relationship to the source information and transformation basis.

**Intent:** Changes an unexplained derived representation into an attributable relationship to the source information and transformation basis.

**Boundary:** Routine retries, cache mechanics and debug traces are excluded unless information-significant.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-LINEAGE-STEWARDSHIP.FN-RECORD-SIGNIFICANT-INFORMATION-LINEAGE

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-coordinate-entity-relative-input — Coordinate Entity-Relative Input

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Select interested behaviours and progress one entity-relative input with explicit participation and effect standing.

Moves an accepted input through the applicable stable behaviour sequence using fresh managed contexts.

**Intent:** Moves an accepted input through the applicable stable behaviour sequence using fresh managed contexts.

**Boundary:** No concurrent intra-entity progression or implicit clinical authority is introduced.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.FN-COORDINATE-ENTITY-RELATIVE-INPUT

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-entity-operational-input-submission — Entity Operational Input Submission

**Type / tier:** service / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Allow authorised domain coordinators to submit an entity-relative stimulus or requested operational effect.

Acceptance concerns the stated handoff contract; the caller observes completion/effect through separately qualified progression information.

**Intent:** Acceptance concerns the stated handoff contract; the caller observes completion/effect through separately qualified progression information.

**Boundary:** Input acceptance does not establish successful completion or authority over source clinical facts.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-ENTITY-SPECIFIC-OPERATIONAL-COORDINATION.SV-ENTITY-OPERATIONAL-INPUT-SUBMISSION

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-maintain-operational-fulfilment-definition — Maintain Operational Fulfilment Definition

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Establish and revise the declared context needs, expected outcome basis and housekeeping of an operational undertaking.

Changes governed activity design while preserving the applicability of the definition used by an existing undertaking.

**Intent:** Changes governed activity design while preserving the applicability of the definition used by an existing undertaking.

**Boundary:** A definition revision is not a command to run work or a dynamic clinical workflow generator.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-GOVERNED-OPERATIONAL-ACTIVITY-DESIGN.FN-MAINTAIN-OPERATIONAL-FULFILMENT-DEFINITION

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-reconcile-indeterminate-activity-effects — Reconcile Indeterminate Activity Effects

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Inspect authoritative observations to establish, qualify or retain uncertainty about a previously indeterminate activity effect.

Moves an uncertainty concern to a justified disposition while preserving the possibility that the original attempt produced an effect.

**Intent:** Moves an uncertainty concern to a justified disposition while preserving the possibility that the original attempt produced an effect.

**Boundary:** Missing evidence does not establish absence of effect and cannot automatically justify a repeat mutation.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-RECONCILE-INDETERMINATE-ACTIVITY-EFFECTS

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-establish-activity-effect-observation-basis — Establish Activity Effect Observation Basis

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Associate the applicable handoff contract and authoritative observations with an indeterminate operational undertaking.

Changes an unqualified uncertainty concern into a time- and attempt-specific observation basis for reconciliation.

**Intent:** Changes an unqualified uncertainty concern into a time- and attempt-specific observation basis for reconciliation.

**Boundary:** Source observations retain their authority; this is an operational observation basis rather than an independent assurance assessment.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-ESTABLISH-ACTIVITY-EFFECT-OBSERVATION-BASIS

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-determine-reconciled-activity-continuation — Determine Reconciled Activity Continuation

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Establish the permitted operational continuation from reconciled effect standing and applicable authorisation.

Moves a reconciled undertaking to justified continuation, authorised retry or explicitly retained uncertainty.

**Intent:** Moves a reconciled undertaking to justified continuation, authorised retry or explicitly retained uncertainty.

**Boundary:** Does not confer clinical decision authority or infer absence of effects from silence; policy evaluation remains separately owned.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.FN-DETERMINE-RECONCILED-ACTIVITY-CONTINUATION

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-PR-indeterminate-activity-outcome-reconciliation-process — Indeterminate Activity Outcome Reconciliation Process

**Type / tier:** process / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Order the observation, effect qualification and authorised disposition of a materially indeterminate operational outcome.

First inspect the governing handoff and available authoritative state; then reconcile observed effects; finally authorise a safe continuation, retry or retained uncertainty. Each participating Function keeps its responsibility owner.

**Intent:** First inspect the governing handoff and available authoritative state; then reconcile observed effects; finally authorise a safe continuation, retry or retained uncertainty. Each participating Function keeps its responsibility owner.

**Boundary:** Not a universal lifecycle or automatic retry loop. A safe disposition may remain unresolved.

**Last allocated structural identity — historical:** CT1-BEC-HUMAN-COLLABORATION-WORK-ENABLEMENT.CT2-BEC-OPERATIONAL-ACTIVITY-TEMPORAL-ENABLEMENT.CT3-BEC-WORKFLOW-ACTIVITY-COORDINATION.PR-INDETERMINATE-ACTIVITY-OUTCOME-RECONCILIATION-PROCESS

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-assess-service-offering-readiness — Assess Service Offering Readiness

**Type / tier:** function / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Evaluate applicable dependency evidence to establish a context- and time-qualified service readiness view.

Combines qualified dependency standing without converting missing evidence into ready or assuming that an available resource establishes deliverability.

**Intent:** Combines qualified dependency standing without converting missing evidence into ready or assuming that an available resource establishes deliverability.

**Boundary:** Resource ownership and clinical clearance remain external to this coordination.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.FN-ASSESS-SERVICE-OFFERING-READINESS

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-service-offering-readiness-query — Service Offering Readiness Query

**Type / tier:** service / behaviour

**Reconciliation standing:** pending adjudication

**Definition:** Expose the qualified readiness view of a defined offering and operating context to operational planning consumers.

The response identifies relevant evidence basis and insufficiency rather than an unconditional ready flag.

**Intent:** The response identifies relevant evidence basis and insufficiency rather than an unconditional ready flag.

**Boundary:** A readiness answer grants no resource allocation or clinical go/no-go authority.

**Last allocated structural identity — historical:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CARE-RESOURCE-CAPACITY-COORDINATION.CT3-BEC-SERVICE-OFFERING-READINESS-COORDINATION.SV-SERVICE-OFFERING-READINESS-QUERY

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-FN-assemble-purpose-qualified-research-release — Assemble Purpose-Qualified Research Release

**Type / tier:** function / behaviour

**Reconciliation standing:** current

**Definition:** Construct a release containing only existing information permitted for the explicitly authorised research purpose.

Moves a qualified request to a bounded release with source attribution and applicable use constraints.

**Intent:** Moves a qualified request to a bounded release with source attribution and applicable use constraints.

**Boundary:** Does not author ethics approvals, promise de-identification or acquire ownership of research conclusions.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.FN-ASSEMBLE-PURPOSE-QUALIFIED-RESEARCH-RELEASE

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-SV-authorised-research-information-export — Authorised Research Information Export

**Type / tier:** service / behaviour

**Reconciliation standing:** current

**Definition:** Provide an approved research consumer with purpose-qualified existing health information under its release contract.

The service exposes the permitted release and its applicable use basis rather than unrestricted clinical repository access.

**Intent:** The service exposes the permitted release and its applicable use basis rather than unrestricted clinical repository access.

**Boundary:** No new research-governance mandate or universal release format is allocated.

**Current structural identity:** CT1-BEC-SECONDARY-HEALTH-INFORMATION-USE-ENABLEMENT.CT2-BEC-AUTHORISED-RESEARCH-INFORMATION-PROVISION.SV-AUTHORISED-RESEARCH-INFORMATION-EXPORT

**Provenance:** Strongly implied bounded information/work responsibility

**Reviewed reconciliation:** Strongly implied bounded information/work responsibility

## HCM-BEC-reviewed-vendor-liaison — Vendor Liaison

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Maintain the information and liaison arrangements needed to interact with vendors supporting healthcare provision.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-PROVIDER-ADMINISTRATION.CT3-BEC-VENDOR-LIAISON

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-device-utilisation-administration — Device Utilisation Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies.

Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer qualified device-to-subject/place allocation, location, utilisation and operational readiness information, including the time at which an association applies.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-UTILISATION-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-device-oa-m-metadata-management — Device OA&M Metadata Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered.

Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage operational and administrative metadata needed to understand how a clinical device is connected, monitored and administered.

**Boundary:** Connectivity, monitoring and administration metadata do not perform device maintenance, interpret clinical telemetry or determine conformance.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-OA-M-METADATA-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-device-connectivity-compliance-management — Device Connectivity Compliance Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints.

Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage information about whether device connectivity satisfies applicable standards, policies and integration constraints.

**Boundary:** Endpoint registration alone does not establish compliance; conformity concerns applicable standards, policies and integration constraints.

**Current structural identity:** CT1-BEC-HEALTHCARE-ENTITY-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-DEVICE-ADMINISTRATION.CT3-BEC-DEVICE-CONNECTIVITY-COMPLIANCE-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-referral-intake-progression — Referral Intake & Progression

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Receive and correlate referral requests, supporting information, destinations, disposition and progression through responsible care systems.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-REFERRAL-ADMINISTRATION.CT3-BEC-REFERRAL-INTAKE-PROGRESSION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-appointment-booking-administration — Appointment & Booking Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

Administer externally supplied appointment and booking information and communicate changes in their scheduling status. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer externally supplied appointment and booking information and communicate changes in their scheduling status.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-SCHEDULING-ADMINISTRATION.CT3-BEC-APPOINTMENT-BOOKING-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-closed-loop-order-progression — Closed-Loop Order Progression

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association.

Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the traceable progression of an order from request through destination, delivery, acknowledgement, fulfilment and outcome association.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-ORDER-ADMINISTRATION.CT3-BEC-CLOSED-LOOP-ORDER-PROGRESSION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-documentation-lifecycle-administration — Clinical Documentation Lifecycle Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer ingestion, indexing, versioning, supersession and governed availability of clinical documentation.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-RECORD-ADMINISTRATION.CT3-BEC-CLINICAL-DOCUMENTATION-LIFECYCLE-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-care-plan-lifecycle-coordination — Care Plan Lifecycle & Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer received care plans, goals, revisions and cross-participant coordination while retaining clinical planning authority at its source.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CARE-PLAN-ADMINISTRATION.CT3-BEC-CARE-PLAN-LIFECYCLE-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-service-dependency-management — Clinical Service Dependency Management

**Type / tier:** capability / CT1

**Reconciliation standing:** current

**Definition:** Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track operational and infrastructure dependencies necessary for clinical services to function, including care-place and environmental readiness, devices, materiel, forms, workstations, connectivity, infrastructure and basic working materials.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-primary-care — Primary Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for primary care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-PRIMARY-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-acute-care — Acute Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for acute care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-ACUTE-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-emergency-care — Emergency Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for emergency care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-EMERGENCY-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-inpatient-care — Inpatient Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for inpatient care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-INPATIENT-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-diagnostic-services — Diagnostic Services

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for diagnostic services services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-DIAGNOSTIC-SERVICES

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-medication-therapy — Medication Therapy

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for medication therapy services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-MEDICATION-THERAPY

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-preventive-care — Preventive Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for preventive care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-PREVENTIVE-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-community-care — Community Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for community care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-COMMUNITY-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-outreach-care — Outreach Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for outreach care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-OUTREACH-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-remote-virtual-care — Remote & Virtual Care

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Identify and track the operational and infrastructure dependencies necessary for remote & virtual care services to function, retaining each dependency provider's responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CLINICAL-SERVICE-DEPENDENCY-MANAGEMENT.CT2-BEC-REMOTE-VIRTUAL-CARE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-session-queue-coordination — Session & Queue Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate clinic/practice session state, operational queue progression and context-specific session capacity information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINIC-PRACTICE-OPERATIONS.CT3-BEC-SESSION-QUEUE-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-ward-logistics-coordination — Ward Logistics Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate ward patient flow, handover context and ward-specific operational logistics and capacity information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WARD-OPERATIONS.CT3-BEC-WARD-LOGISTICS-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-perioperative-logistics-coordination — Perioperative Logistics Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

Coordinate theatre case progression information and notifications to supporting perioperative operational teams. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate theatre case progression information and notifications to supporting perioperative operational teams.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-THEATRE-OPERATIONS.CT3-BEC-PERIOPERATIVE-LOGISTICS-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-emergency-department-operational-coordination — Emergency Department Operational Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information.

Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate emergency-department bed/bay occupancy, operational waits, length-of-stay milestones and local capacity information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-EMERGENCY-DEPARTMENT-OPERATIONS.CT3-BEC-EMERGENCY-DEPARTMENT-OPERATIONAL-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-outpatient-flow-coordination — Outpatient Flow Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate outpatient arrival, clinic flow, follow-up requirements and context-specific outpatient capacity information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-OUTPATIENT-OPERATIONS.CT3-BEC-OUTPATIENT-FLOW-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-bed-care-place-state-administration — Bed & Care-Place State Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the current operational occupancy, reservation, blocking and readiness states of beds and other care-places.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-STATE-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-bed-care-place-turnover-coordination — Bed & Care-Place Turnover Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate the operational transition of a released care-place through cleaning, preparation and readiness notifications.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-BED-CARE-PLACE-MANAGEMENT.CT3-BEC-BED-CARE-PLACE-TURNOVER-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-service-demand-capacity-coordination — Service Demand & Capacity Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate demand and capacity information across services while retaining the originating responsibilities of context-specific capacity providers.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-CAPACITY-MANAGEMENT.CT3-BEC-SERVICE-DEMAND-CAPACITY-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-service-establishment-support — Service Establishment Support

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Support the information needed to establish a health service in its intended operating context.

Support the information needed to establish a health service in its intended operating context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Support the information needed to establish a health service in its intended operating context.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-SERVICE-ESTABLISHMENT-SUPPORT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-roster-capture-communication — Clinical Roster Capture & Communication

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes.

Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Capture and communicate supplied clinical roster information, its effective periods and subsequent coverage changes.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-ROSTERING.CT3-BEC-CLINICAL-ROSTER-CAPTURE-COMMUNICATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-mobile-worker-dispatch-safety-coordination — Mobile Worker Dispatch & Safety Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context.

Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Exchange externally established mobile work assignments, visit progression and safety check-in information with qualified worker context.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-MOBILE-STAFF-MANAGEMENT.CT3-BEC-MOBILE-WORKER-DISPATCH-SAFETY-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-on-call-availability-contact-resolution — On-Call Availability & Contact Resolution

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time.

Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Resolve currently available on-call providers and their contact paths for the applicable specialty, service and time.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-ON-CALL-MANAGEMENT.CT3-BEC-ON-CALL-AVAILABILITY-CONTACT-RESOLUTION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-operational-work-assignment-dispatch-coordination — Operational Work Assignment & Dispatch Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate qualified operational work assignment, dispatch, acknowledgement, progression and escalation within assigned authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-WORK-ALLOCATION-DISPATCH.CT3-BEC-OPERATIONAL-WORK-ASSIGNMENT-DISPATCH-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-organisation-clinical-certification-compliance-support — Organisation Clinical Certification & Compliance Support

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer supplied organisation clinical certification and conformance evidence for use in service qualification and participation.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-ORGANISATION-CLINICAL-CERTIFICATION-COMPLIANCE-SUPPORT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-provider-clinical-qualification-tracking — Provider Clinical Qualification Tracking

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Track supplied provider clinical qualifications, competency certificates and privilege evidence with effective dates and issuing-authority standing.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-QUALIFICATION-MANAGEMENT.CT3-BEC-PROVIDER-CLINICAL-QUALIFICATION-TRACKING

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-credential-management — Credential Management

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer definitions of clinical-service credentials and their required applicability without granting professional privilege or managing physical badges.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CREDENTIAL-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-service-credential-definition-management — Clinical Service Credential Definition Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.

Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage the definitions of credentials required for participation in a clinical service, distinct from physical badges and authentication credentials.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CREDENTIAL-MANAGEMENT.CT3-BEC-CLINICAL-SERVICE-CREDENTIAL-DEFINITION-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-escalation-path-management — Clinical Escalation Path Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage clinical escalation paths and their applicability, contacts and progression conditions for use by responsible clinical participants.

**Boundary:** Harmonia manages the escalation path; responsible clinical participants perform supervision and retain clinical judgement.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-SUPERVISION.CT3-BEC-CLINICAL-ESCALATION-PATH-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-transfer-coordination — Patient Transfer Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate patient transport requests, dispatch and observed departure, transit and arrival milestones.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-PATIENT-TRANSPORT.CT3-BEC-PATIENT-TRANSFER-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-specimen-metadata-management — Specimen Metadata Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage the identifying, descriptive, custody and relevant handling information needed to interpret a transported specimen.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.CT3-BEC-SPECIMEN-METADATA-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-specimen-movement-traceability — Specimen Movement Traceability

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Maintain attributable pickup, custody, transport-condition and delivery traceability for clinical specimens.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-CLINICAL-LOGISTICS-COORDINATION.CT3-BEC-SPECIMEN-MOVEMENT-TRACEABILITY

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-discharge-progression-coordination — Discharge Progression & Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications.

Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate externally established discharge milestones, multidisciplinary readiness information and departure notifications.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-OPERATIONS-COORDINATION-ENABLEMENT.CT2-BEC-DISCHARGE-MANAGEMENT.CT3-BEC-DISCHARGE-PROGRESSION-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-longitudinal-clinical-record-administration — Longitudinal Clinical Record Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession.

Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer longitudinal record assembly, active clinical summaries and durable received/managed history with source attribution and supersession.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PATIENT-CLINICAL-RECORD.CT3-BEC-LONGITUDINAL-CLINICAL-RECORD-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-hie-requirement-management — HIE Requirement Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange.

Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage the identified information-exchange requirements, interaction contracts and participant obligations applicable to a health-information exchange.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-REQUIREMENT-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-hie-implementation-management — HIE Implementation Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions.

Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage implementation information and progression for established exchange requirements, including submission, retrieval, distribution and syndication interactions.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-IMPLEMENTATION-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-hie-endpoint-state-management — HIE Endpoint State Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information.

Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage recognised exchange endpoint registrations, operational standing and contract-qualified availability information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-EXCHANGE-ENABLEMENT.CT3-BEC-HIE-ENDPOINT-STATE-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-governed-health-information-retrieval — Governed Health Information Retrieval

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations.

Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Retrieve and search health information and apply access-qualified result filtering under established access and consent determinations.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-ACCESS.CT3-BEC-GOVERNED-HEALTH-INFORMATION-RETRIEVAL

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-health-information-communication — Health Information Communication

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable patient notifications, patient task communication, patient self-service and access to a patient health library under applicable information-use controls.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-notification-services — Patient Notification Services

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Enable governed patient notifications concerning health information and applicable care or operational events.

Enable governed patient notifications concerning health information and applicable care or operational events. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable governed patient notifications concerning health information and applicable care or operational events.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-NOTIFICATION-SERVICES

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-task-communication-services — Patient Task Communication Services

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Communicate patient-facing work requests and their reported progression under the applicable work and disclosure contracts.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-TASK-COMMUNICATION-SERVICES

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-self-service — Patient Self-Service

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Enable patients to undertake permitted information-access and administrative self-service interactions.

Enable patients to undertake permitted information-access and administrative self-service interactions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable patients to undertake permitted information-access and administrative self-service interactions.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-SELF-SERVICE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-health-library-access — Patient Health Library Access

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Enable authorised patient access to appropriately governed health knowledge and library information.

Enable authorised patient access to appropriately governed health knowledge and library information. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable authorised patient access to appropriately governed health knowledge and library information.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-COMMUNICATION.CT3-BEC-PATIENT-HEALTH-LIBRARY-ACCESS

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-health-information-restriction-management — Patient Health Information Restriction Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage application of patient health-information restrictions using the client-associated privacy and consent responsibilities retained by Client Privacy Guardrail Management.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-RESTRICTION-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-health-information-filter-management — Patient Health Information Filter Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage patient health-information filtering rules and qualified projections while respecting established client privacy and consent determinations.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-FILTER-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-patient-health-information-disclosure-management — Patient Health Information Disclosure Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage permitted patient health-information disclosure and its applicable recipient, purpose and restriction conditions.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority. Client consent directives remain owned by Client Privacy Guardrail Management (FEAT-EM-09); this capability references and respects those determinations without defining a second consent owner.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-HEALTH-INFORMATION-CONTROL.CT3-BEC-PATIENT-HEALTH-INFORMATION-DISCLOSURE-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-information-asset-stewardship-management — Information Asset Stewardship Management

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage definitions and tags that make governed information assets recognisable and interpretable for their permitted uses.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-information-asset-definition — Information Asset Definition

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Define the identifying, semantic and governed-use characteristics of an information asset.

Define the identifying, semantic and governed-use characteristics of an information asset. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Define the identifying, semantic and governed-use characteristics of an information asset.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT.CT3-BEC-INFORMATION-ASSET-DEFINITION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-information-asset-tag-management — Information Asset Tag Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage attributable tags describing information assets without treating caller-controlled tags as security authority.

Manage attributable tags describing information assets without treating caller-controlled tags as security authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage attributable tags describing information assets without treating caller-controlled tags as security authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-ASSET-STEWARDSHIP-MANAGEMENT.CT3-BEC-INFORMATION-ASSET-TAG-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-information-content-maintainer-management — Information Content Maintainer Management

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Administer information-content maintainer participation, source provenance and governed information products.

Administer information-content maintainer participation, source provenance and governed information products. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer information-content maintainer participation, source provenance and governed information products.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-data-scientist-administration — Data Scientist Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer profiles and qualified participation information for data scientists maintaining governed information content.

Administer profiles and qualified participation information for data scientists maintaining governed information content. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer profiles and qualified participation information for data scientists maintaining governed information content.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-DATA-SCIENTIST-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-information-product-administration — Information Product Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer identifiable governed information products, their purpose, content standing and qualified release/use context.

Administer identifiable governed information products, their purpose, content standing and qualified release/use context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer identifiable governed information products, their purpose, content standing and qualified release/use context.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-CONTENT-MAINTAINER-MANAGEMENT.CT3-BEC-INFORMATION-PRODUCT-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-knowledge-request-administration — Clinical Knowledge Request Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content.

Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer clinical knowledge requests and their qualified resolution to applicable governed knowledge or reference content.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-REQUEST-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-knowledge-artefact-distribution-management — Clinical Knowledge Artefact Distribution Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts.

Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage distribution of governed clinical knowledge artefacts to intended consumers under their applicable use contracts.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-KNOWLEDGE-SERVICES.CT3-BEC-CLINICAL-KNOWLEDGE-ARTEFACT-DISTRIBUTION-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-clinical-context-collaboration — Clinical Context Collaboration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable governed multidisciplinary collaboration with clinical context while preserving the distinction between discourse and authored clinical records.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COLLABORATION.CT3-BEC-CLINICAL-CONTEXT-COLLABORATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-operational-collaboration — Operational Collaboration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

Enable operational team discussion and collaboration without promoting conversational content to clinical record authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Enable operational team discussion and collaboration without promoting conversational content to clinical record authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-GENERAL-COLLABORATION.CT3-BEC-OPERATIONAL-COLLABORATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-workflow-activity-coordination — Workflow & Activity Coordination

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate tasks entering or leaving the Harmonia execution boundary and work undertaken by other systems, with responsibility/destination routing, external status/traceability and governance of the completion bridge.

**Boundary:** External means outside Harmonia execution, potentially inside the same health organisation. Work coordination includes completion responsibilities and traceability; it is not generic interoperability or message routing.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-external-task-coordination — External Task Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.

Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate work entering or leaving the Harmonia execution boundary with the system or actor responsible for undertaking and completing that work.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-EXTERNAL-TASK-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-external-task-status-traceability-management — External Task Status & Traceability Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.

Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage the observable status and attributable traceability of work as it progresses through systems outside the Harmonia execution boundary.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-EXTERNAL-TASK-STATUS-TRACEABILITY-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-activity-routing-management — Activity Routing Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage responsibility and destination routing for activity crossing the Harmonia execution boundary, respecting the intended work and completion responsibility.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-ACTIVITY-ROUTING-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-work-boundary-governance — Work Boundary Governance

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Govern boundary conditions and completion responsibilities of the bridge between Harmonia and another system undertaking work.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-WORKFLOW-ACTIVITY-COORDINATION.CT3-BEC-WORK-BOUNDARY-GOVERNANCE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-operational-clinical-calendar-coordination — Operational & Clinical Calendar Coordination

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Coordinate projections of operational and clinical calendars and correlate temporal events without taking ownership of source bookings or rosters.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-CALENDAR-MANAGEMENT.CT3-BEC-OPERATIONAL-CLINICAL-CALENDAR-COORDINATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-contextual-information-presentation — Contextual Information Presentation

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Present governed longitudinal clinical and operational information in its applicable human interaction context.

Present governed longitudinal clinical and operational information in its applicable human interaction context. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Present governed longitudinal clinical and operational information in its applicable human interaction context.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-PRESENTATION-SERVICES.CT3-BEC-CONTEXTUAL-INFORMATION-PRESENTATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-enterprise-canonical-reusable-model-design — Enterprise Canonical/Reusable Model Design

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance.

Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Design canonical and reusable enterprise information models and semantic bindings under applicable information-design governance.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-DESIGN

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-enterprise-canonical-reusable-model-library-management — Enterprise Canonical/Reusable Model Library Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions.

Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Manage the identifiable library of canonical and reusable enterprise model definitions and their governed revisions.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-LIBRARY-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-enterprise-canonical-reusable-model-utilisation-support — Enterprise Canonical/Reusable Model Utilisation Support

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Support correct utilisation and conformance of canonical and reusable models across their intended consumers.

Support correct utilisation and conformance of canonical and reusable models across their intended consumers. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Support correct utilisation and conformance of canonical and reusable models across their intended consumers.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-CANONICAL-REUSABLE-MODEL-UTILISATION-SUPPORT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-enterprise-reference-data-stewardship — Enterprise Reference Data Stewardship

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration.

Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Steward enterprise reference-data definitions, bindings and applicable governance independently of mutable runtime dataset administration.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-GOVERNED-HEALTH-INFORMATION-ENABLEMENT.CT2-BEC-INFORMATION-DESIGN-GOVERNANCE.CT3-BEC-ENTERPRISE-REFERENCE-DATA-STEWARDSHIP

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-assurance-service-review — Assurance Service Review

**Type / tier:** capability / CT2

**Reconciliation standing:** current

**Definition:** Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Independently review the operation, effectiveness, appropriateness and conformance of the assurance service itself.

**Boundary:** Reviews the assurance service itself. Canonical evidence association, subject assessment, adjudication, findings and conclusions are retained separately pending adjudication.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SERVICE-REVIEW

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-assurance-service-evaluation — Assurance Service Evaluation

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Evaluate the operation, effectiveness, appropriateness and conformance of the assurance service itself through independent review.

**Boundary:** Evaluation concerns operation, effectiveness, appropriateness and conformance of the assurance service; it is not execution of assurance against a subject.

**Current structural identity:** CT1-BEC-INDEPENDENT-SERVICE-ASSURANCE-ENABLEMENT.CT2-BEC-ASSURANCE-SERVICE-REVIEW.CT3-BEC-ASSURANCE-SERVICE-EVALUATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-client-intake-demographic-update — Client Intake & Demographic Update

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established client intake & demographic update information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLIENT-CENTRED-ADMINISTRATION.CT3-BEC-CLIENT-INTAKE-DEMOGRAPHIC-UPDATE

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-encounter-lifecycle-management — Encounter Lifecycle Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority.

Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established encounter lifecycle management information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-EPISODE-ENCOUNTER-ADMINISTRATION.CT3-BEC-ENCOUNTER-LIFECYCLE-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-diagnostic-workflow-management — Diagnostic Workflow Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority.

Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established diagnostic workflow management information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-DIAGNOSTIC-ADMINISTRATION.CT3-BEC-DIAGNOSTIC-WORKFLOW-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-medication-order-dispense-management — Medication Order & Dispense Management

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority.

Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established medication order & dispense management information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-MEDICATION-ADMINISTRATION.CT3-BEC-MEDICATION-ORDER-DISPENSE-MANAGEMENT

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-procedure-surgical-administration — Procedure & Surgical Administration

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established procedure & surgical administration information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-PROCEDURE-ADMINISTRATION.CT3-BEC-PROCEDURE-SURGICAL-ADMINISTRATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

## HCM-BEC-reviewed-secure-clinical-communication — Secure Clinical Communication

**Type / tier:** capability / CT3

**Reconciliation standing:** current

**Definition:** Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

Administer the established secure clinical communication information and coordination responsibility while preserving its source authority. The reviewed hierarchy assigns this enduring ability to its stated business-enabling context.

**Intent:** Administer the established secure clinical communication information and coordination responsibility while preserving its source authority.

**Boundary:** Information administration and coordination preserve the authority of the responsible source, clinical actor and governed domain; they confer no additional real-world management authority.

**Current structural identity:** CT1-BEC-CARE-ADMINISTRATION-INFORMATION-ENABLEMENT.CT2-BEC-CLINICAL-COMMUNICATION-ADMINISTRATION.CT3-BEC-SECURE-CLINICAL-COMMUNICATION

**Provenance:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy

**Reviewed reconciliation:** Reconciliation against the explicit human-reviewed BEC candidate hierarchy
