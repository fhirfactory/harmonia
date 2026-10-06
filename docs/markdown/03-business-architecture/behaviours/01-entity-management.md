# Entity Management — Capability-Scoped Behaviour

## 1. Contextual Scope & Architectural Intent

Entity Management establishes the canonical master data governance, identity correlation, and structural definitions for all core business entities participating in healthcare delivery: Clients/Patients, Healthcare Practitioners, Healthcare Organisations, Locations, Health Services, and Clinical Devices.

---

## 2. Client Administration

Client Administration governs the identity, demographic context, relationship associations, and privacy directives of persons receiving care.

### 2.1 Person Identity
- **Owning Capability**: `L2: Person Identity` (under `L1: Client Administration`)
- **Key Architectural Semantics**:
  - **Identifier Resolution**: Asks for authoritative detail and verification regarding a specific person identifier within a single domain namespace.
  - **Identifier Correlation**: Discovers, establishes, and maintains governed cross-authority associations between distinct identifier namespaces across the enterprise.
  - **Identity Alias**: Contextual, legal, or alternative identity expressions (e.g., preferred names, maiden names, emergency aliases) by which a person is known. It is conceptually distinct from cross-domain correlation.
- **Functions & Exposed Services**:
  - **Feature: Identifier Resolution**:
    - *Function*: `Resolve Person Identifier` — Validates and retrieves the authoritative demographic record bound to a specific identifier.
    - *Exposed Service*: `Person Identifier Resolution` — Exposed to Service Administration and Service Delivery to disambiguate client identities.
  - **Feature: Cross-Authority Identifier Correlation**:
    - *Function*: `Correlate Person Identifiers` — Evaluates deterministic and probabilistic linkage across multiple local patient identifiers (e.g., Hospital MRN, National IHI, Pathology ID).
    - *Exposed Service*: `Person Identifier Correlation` — Exposed across the HIE to establish patient cross-reference mappings.
  - **Feature: Identity Alias Association**:
    - *Function*: `Maintain Person Identity Aliases` — Records and qualifies alias names, titles, and emergency aliases for a governed person identity.
    - *Exposed Service*: *(Internal Function — accessed via Person Identifier Resolution)*.
  - **Feature: Governed Identity Correction**:
    - *Function*: `Apply Governed Person Identity Correction` — Executes audited merges, unlinks, and demographic rectifications under formal data governance.
    - *Exposed Service*: `Person Identity Correction` — Exposed to authorized HIM/Medical Records administrators.
    - *Governed Process*: **Governed Person Identity Correction Process**.
- **Information Responsibility**: Person Identity Record, Identifier Namespace Bindings, Cross-Authority Correlation Graph, Identity Aliases, Identity Merge/Split Audit Log.

---

### 2.2 Healthcare Subject
- **Owning Capability**: `L2: Healthcare Subject Context`
- **Functions & Exposed Services**:
  - **Feature: Subject Context Binding**:
    - *Function*: `Establish Healthcare Subject Context` — Binds demographic, cultural, and communication requirements to a patient record.
    - *Exposed Service*: `Healthcare Subject Context Resolution` — Provides comprehensive demographic, language, and indigenous status context to clinical applications.
  - **Feature: Demographic Context Governance**:
    - *Function*: `Govern Subject Demographic Context` — Applies validation rules to address changes, date of birth verification, and death notifications.
    - *Exposed Service*: `Demographic Change Distribution` — Emits governed subject demographic updates to subscribed clinical systems.
- **Information Responsibility**: Healthcare Subject Profile, Demographic History, Communication Preferences.

---

### 2.3 Client Relationship
- **Owning Capability**: `L2: Client Relationships & Support Network`
- **Functions & Exposed Services**:
  - **Feature: Next-of-Kin & Guardian Association**:
    - *Function*: `Maintain Client Relationships` — Records links between patients and carers, next of kin, nominated representatives, and legal guardians.
    - *Exposed Service*: `Client Relationship Query` — Discloses verified support network members and contact details to clinical teams.
  - **Feature: Relationship Validity Tracking**:
    - *Function*: `Govern Client Relationship Validity` — Verifies power-of-attorney documents, guardianship orders, and proxy validity periods.
    - *Exposed Service*: `Representative Authority Verification` — Confirms legal decision-making authority for substitute consent.
- **Information Responsibility**: Client Relationship Record, Representative Legal Mandate, Carer Contact Directory.

---

### 2.4 Client Privacy
- **Owning Capability**: `L2: Client Privacy & Consent Directives`
- **Functions & Exposed Services**:
  - **Feature: Consent Directive Evaluation**:
    - *Function*: `Evaluate Client Consent` — Evaluates patient opt-in, opt-out, and general information sharing preferences against proposed exchanges.
    - *Exposed Service*: `Consent Decision Service` — Provides real-time consent evaluation decisions to Health Information Control.
  - **Feature: Confidentiality Flag Enforcement**:
    - *Function*: `Evaluate Client Confidentiality Restrictions` — Evaluates sensitive record flags (e.g., VIP masking, restricted department exclusions).
    - *Exposed Service*: `Confidentiality Restriction Evaluation` — Enforces record-level masking directives across the HIE.
- **Information Responsibility**: Client Consent Directive, Record Masking Rule, Disclosure Restriction Register.

---

## 3. Provider Administration

Provider Administration governs individual healthcare practitioners, their professional qualifications, clinical roles, electronic communication endpoints, and organizational affiliations.

- **Owning Capability**: `L1: Provider Administration`
- **Key Architectural Semantics**:
  - **Practitioner $\neq$ Service Provider**: A *Practitioner* is an individual professional. A *Service Provider* is an organisation or facility offering health services. A Practitioner may act in the *Service Provider* role in private practice.
- **Functions & Exposed Services**:
  - **Feature: National Practitioner Verification**:
    - *Function*: `Verify Practitioner Registration` — Validates national registration, specialty endorsements, and disciplinary sanctions against regulatory registries (e.g., AHPRA).
    - *Exposed Service*: `Practitioner Verification Service` — Discloses verified registration and status to credentialing and clinical systems.
    - *Governed Process*: **Practitioner Verification Process**.
  - **Feature: Practitioner Profile Governance**:
    - *Function*: `Govern Practitioner Profile` — Maintains official names, professional identifiers (e.g., HPI-I, Prescriber Number), and clinical qualifications.
    - *Exposed Service*: `Practitioner Directory Query` — Provides provider directory lookups across the healthcare network.
  - **Feature: Role & Specialty Modeling**:
    - *Function*: `Maintain Practitioner Roles` — Assigns and maintains clinical roles (e.g., Specialist Physician, Registrar, General Practitioner) fulfilled by a practitioner.
    - *Exposed Service*: `Practitioner Role Resolution` — Resolves the specific clinical capacities of a practitioner.
  - **Feature: Electronic Endpoint Binding**:
    - *Function*: `Bind Practitioner Electronic Endpoints` — Associates secure messaging addresses, direct communication endpoints, and notification preferences to practitioner roles.
    - *Exposed Service*: `Practitioner Endpoint Lookup` — Provides secure message routing destinations for clinical communication.
  - **Feature: Practitioner-Organisation Affiliation**:
    - *Function*: `Maintain Practitioner Affiliations` — Tracks employment, admitting rights, and consulting appointments at specific healthcare facilities.
    - *Exposed Service*: `Practitioner Affiliation Resolution` — Discloses organizational ties and practice locations.
  - **Feature: Clinical Privileging Record**:
    - *Function*: `Maintain Clinical Privileges` — Records approved clinical scopes of practice, procedural authorizations, and prescribing privileges.
    - *Exposed Service*: `Clinical Privilege Verification` — Verifies procedural and prescribing authority for clinical ordering systems.
- **Information Responsibility**: Practitioner Master Record, Professional Registration Status, Practitioner Role Bindings, Electronic Communication Endpoints, Scope of Practice Privileges.

---

## 4. Organisation Administration

Organisation Administration maintains the authoritative registry of healthcare organisations, networks, directorates, and administrative subdivisions.

- **Owning Capability**: `L1: Organisation Administration`
- **Functions & Exposed Services**:
  - **Feature: National Organisation Verification**:
    - *Function*: `Verify Healthcare Organisation` — Validates enterprise legal identity, national facility identifiers (e.g., HPI-O), and accreditation status.
    - *Exposed Service*: `Organisation Verification Service` — Provides verified organisation credentials to external and internal partners.
  - **Feature: Organisation Profile Management**:
    - *Function*: `Govern Organisation Profile` — Maintains official trading names, enterprise registration numbers, and governing bodies.
    - *Exposed Service*: `Organisation Directory Query` — Discloses healthcare organisation profiles across the region.
  - **Feature: Hierarchical Structure Navigation**:
    - *Function*: `Maintain Organisational Structure` — Models parent-subsidiary relationships, clinical directorates, departments, and operational divisions.
    - *Exposed Service*: `Organisational Hierarchy Resolution` — Resolves department nesting and reporting hierarchies.
  - **Feature: Directory Contact Management**:
    - *Function*: `Maintain Organisation Contacts` — Manages administrative, clinical, and emergency contact details for facilities.
    - *Exposed Service*: `Organisation Contact Lookup` — Provides official operational contact details.
- **Information Responsibility**: Healthcare Organisation Registry, National Facility Identifier Bindings, Department Hierarchy Graph, Organisation Contact Directory.

---

## 5. Location Administration

Location Administration defines the physical, functional, and geospatial structure of healthcare environments, including sites, buildings, floors, wards, rooms, bays, and bed care-places.

- **Owning Capability**: `L1: Location Administration`
- **Key Architectural Semantics**:
  - **Definition vs. Operational State**: *Location Administration* defines what a physical location or care-place **IS** (its static attributes, type, capacity, hierarchy). *Health Service Operations* owns what is **operationally happening** to that care-place (occupancy, cleaning status, isolation locks, turnover).
- **Functions & Exposed Services**:
  - **Feature: Location Geospatial & Address Resolution**:
    - *Function*: `Maintain Healthcare Location` — Records physical sites, campuses, outpatient clinics, and community health centres.
    - *Exposed Service*: `Location Directory Query` — Exposes location profiles and geospatial addresses.
  - **Feature: Physical Hierarchy Modeling**:
    - *Function*: `Maintain Physical Location Hierarchy` — Models the nested structural containment tree: `Campus` $\to$ `Building` $\to$ `Floor` $\to$ `Ward/Suite` $\to$ `Room/Bay` $\to$ `Bed Care-Place`.
    - *Exposed Service*: `Location Hierarchy Resolution` — Discloses physical containment and navigation paths for clinical facilities.
  - **Feature: Atomic Care-Place Modeling**:
    - *Function*: `Maintain Care-Place Definition` — Defines static bed bays, treatment chairs, surgical tables, and emergency resuscitation cubicles, including equipment capabilities and oxygen/telemetry infrastructure.
    - *Exposed Service*: `Care-Place Specification Lookup` — Discloses care-place physical capabilities to Bed & Care-Place Management.
- **Information Responsibility**: Location Master Registry, Physical Facility Hierarchy Tree, Care-Place Structural Definitions.

---

## 6. Health Service Administration

Health Service Administration governs the catalogue of clinical services offered across the healthcare network and resolves the critical multi-dimensional mapping between Services, Providers, Locations, and Practitioners.

- **Owning Capability**: `L1: Health Service Administration`
- **Key Architectural Semantics**:
  - **Service / Location / Provider Map**: This capability models the multi-way relationship:
    $$\text{Healthcare Service} \longleftrightarrow \text{Service Provider} \longleftrightarrow \text{Healthcare Location} \longleftrightarrow \text{Practitioner Role}$$
  - **Ownership Invariant**: The mapping capability owns the **relationships and association rules**. It does **not** acquire ownership of the Service, Provider, Location, or Practitioner entities being mapped.
  - **Consumer Resolution Scenarios**: Enables clinical and administrative consumers to unambiguously resolve:
    1. *Who provides Service X?*
    2. *Where does Provider Y provide Service X?*
    3. *What Services are delivered at Location Z?*
    4. *Where can Service X be obtained?*
    5. *Which Practitioner Roles participate in Service X?*
- **Functions & Exposed Services**:
  - **Feature: Service Catalogue Publishing**:
    - *Function*: `Maintain Healthcare Service Definition` — Defines clinical service offerings (e.g., Cardiology Outpatient Clinic, MRI Imaging, Acute Stroke Care), specialty classifications, and clinical scope.
    - *Exposed Service*: `Healthcare Service Directory Query` — Exposes published clinical service offerings.
  - **Feature: Service-Location-Provider Binding**:
    - *Function*: `Maintain Service / Location / Provider Map` — Binds health service definitions to delivering organisations, physical clinic locations, and participating practitioner roles.
    - *Exposed Service*: `Service Provision Resolution` — Exposes multi-dimensional resolution queries answering who, where, and when healthcare services are delivered.
  - **Feature: Operating Schedule Specification**:
    - *Function*: `Maintain Service Availability` — Governs regular operating hours, emergency intake availability, and seasonal clinic schedules.
    - *Exposed Service*: `Service Availability Query` — Discloses operational intake hours and open status.
  - **Feature: Eligibility & Catchment Specification**:
    - *Function*: `Maintain Service Eligibility Rules` — Configures catchment criteria, age thresholds, referral requirements, and clinical indications for service access.
    - *Exposed Service*: `Service Eligibility Evaluation` — Evaluates referral eligibility for intake coordinators.
- **Information Responsibility**: Healthcare Service Catalogue, Service / Location / Provider Mapping Matrix, Service Availability Schedules, Service Eligibility Rule Sets.

---

## 7. Clinical Device Administration

Clinical Device Administration governs the master definition, instance identification, and electronic communication endpoints of medical equipment and point-of-care appliances.

- **Owning Capability**: `L1: Clinical Device Administration`
- **Functions & Exposed Services**:
  - **Feature: Device Type Specification**:
    - *Function*: `Maintain Device Definition` — Models medical device models, manufacturers, regulatory classifications (e.g., TGA/FDA classes), and calibration specifications.
    - *Exposed Service*: `Device Definition Query` — Discloses approved medical device models and technical parameters.
  - **Feature: Device Serial Tracking**:
    - *Function*: `Maintain Device Instance Identity` — Tracks physical serial numbers, Unique Device Identifiers (UDI), asset tags, and firmware versions.
    - *Exposed Service*: `Device Instance Resolution` — Resolves physical device instances from scanned barcodes or UDIs.
  - **Feature: Device-Patient/Location Assignment**:
    - *Function*: `Maintain Device Association` — Records active associations between clinical devices, patient encounters, and specific care-places (e.g., Bed 4 Vital Signs Monitor).
    - *Exposed Service*: `Device Association Query` — Discloses active device-to-patient and device-to-location bindings.
  - **Feature: Device Endpoint Registration**:
    - *Function*: `Maintain Device Communication Endpoints` — Manages network telemetry addresses, gateway ports, and protocol bindings for medical devices.
    - *Exposed Service*: `Device Endpoint Resolution` — Exposes network addresses for telemetry routing.
- **Information Responsibility**: Medical Device Type Registry, Physical Device Instance Directory, Device Association Ledger, Device Communication Endpoint Map.
