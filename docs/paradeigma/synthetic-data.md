# Paradeigma Synthetic Data Generation & Identifiers `[IMPLEMENTED]`

This document details the deterministic pseudorandom generation framework, clinical persona models, Australian digital health identifier formats, and interconnected Provider Registry topology graph builders in `paradeigma-common`.

---

## 1. Deterministic Pseudorandom Generation (`SeedRandom`) `[IMPLEMENTED]`

To guarantee that tests and scenarios are 100% reproducible across CI/CD and developer environments, Paradeigma avoids standard non-deterministic random libraries in favor of `SeedRandom`:

```java
// Identical seed produces bit-for-bit identical HL7 and FHIR resources
long seed = 42L;
SyntheticPatientGenerator patientGen = new SyntheticPatientGenerator(seed);
Patient patient1 = patientGen.generatePatient("pat-001");

SyntheticPatientGenerator patientGen2 = new SyntheticPatientGenerator(seed);
Patient patient2 = patientGen2.generatePatient("pat-001");

// Byte-for-byte identical output
assertThat(patient1.equalsDeep(patient2)).isTrue();
```

---

## 2. Generator Inventory (`paradeigma-common`) `[IMPLEMENTED]`

| Generator Class | Target Domain | Generated Payload / Structures |
| :--- | :--- | :--- |
| **`SyntheticPatientGenerator`** | Patient Demographics & Encounters | First/last names, DOB, biological sex, phone, address, MRN (`MRN-<seed>`), Medicare number (`2...`). |
| **`SyntheticOrderGenerator`** | Diagnostic Orders | Placer Order Number (`ORD-<seed>`), order type (`LAB`, `IMAGING`), clinical priority, ordering clinician. |
| **`SyntheticResultGenerator`** | Pathology & Radiology Results | Filler Order Number, observation timestamps, numeric observation values, reference intervals, diagnostic narrative impressions. |
| **`PractitionerGenerator`** | Healthcare Providers | Deterministic names, qualifications, Australian `HPI-I` (`800361...`), and `AHPRA` registration numbers. |
| **`PractitionerRoleGenerator`** | Clinical Roles & Specialties | `PractitionerRole` records with SNOMED CT specialty codes, multi-target references to `Organization`, `Location`, `Endpoint`. |
| **`OrganizationGenerator`** | Health Services & Facilities | Hospital network names, Australian `HPI-O` (`800362...`), Australian Business Numbers (`ABN`), physical addresses. |
| **`LocationGenerator`** | Wards, Clinics & Rooms | Physical bed locations, wings, ward identifiers, and managing organization references. |
| **`HealthcareServiceGenerator`**| Specialized Clinical Services | Clinical services (Emergency, Cardiology, Pathology), opening hours, and contact details. |
| **`EndpointGenerator`** | Integration Endpoints | FHIR REST, MLLP, and Direct Secure Messaging communication endpoints. |
| **`GroupGenerator`** | Clinical Panels & Committees | Specialist panels, clinical governance committees, and multidisciplinary care teams. |

---

## 3. Canonical Australian Digital Health Identifiers `[IMPLEMENTED]`

All synthetic personas and provider structures conform to official Australian Digital Health Agency specifications:

### 3.1 Healthcare Provider Identifier - Individual (HPI-I)
- **Format**: 16-digit numeric starting with prefix `800361`.
- **System URI**: `http://ns.electronichealth.net.au/id/hi/hpii/1.0`
- **Luhn Checksum**: Verified via standard Luhn modulus algorithm.
- **Example**: `8003610000000001`

### 3.2 Healthcare Provider Identifier - Organisation (HPI-O)
- **Format**: 16-digit numeric starting with prefix `800362`.
- **System URI**: `http://ns.electronichealth.net.au/id/hi/hpio/1.0`
- **Example**: `8003620000000002`

### 3.3 AHPRA Registration Number
- **Format**: 3-letter profession prefix followed by 10 digits.
- **System URI**: `http://hl7.org.au/id/ahpra-registration-number`
- **Example**: `MED0001234567` (Medical Practitioner), `NMW0007654321` (Nursing & Midwifery)

### 3.4 Australian Medicare Number
- **Format**: 10-digit primary number + 1-digit individual reference number (IRN).
- **System URI**: `http://ns.electronichealth.net.au/id/medicare-number`
- **Example**: `2123456781-1`

### 3.5 SNOMED CT Specialty Codes
- **Clinical Specialties**: Mapped to official SNOMED CT AU concept identifiers:
  - `394579002`: Cardiology
  - `394582007`: Dermatology
  - `394583002`: Endocrinology
  - `394585009`: Obstetrics and Gynaecology
  - `394801008`: Trauma and Orthopaedic Surgery

---

## 4. Structured Clinical Observation Catalog `[IMPLEMENTED]`

`SyntheticResultGenerator` provides calibrated clinical reference ranges and panic thresholds:

### 4.1 Full Blood Count (Order Code: `CBC`)

| Analyte / Test | Observation Code (LOINC) | Normal Reference Range | Panic Low Threshold | Panic High Threshold | Unit |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Hemoglobin (`Hb`)** | `718-7` | `130.0 – 180.0` | `< 70.0` | `> 200.0` | `g/L` |
| **White Blood Cells (`WBC`)** | `6690-2` | `4.0 – 11.0` | `< 2.0` | `> 30.0` | `x10^9/L` |
| **Platelets (`Plt`)** | `777-3` | `150.0 – 450.0` | `< 50.0` | `> 1000.0` | `x10^9/L` |
| **Hematocrit (`Hct`)** | `4544-3` | `0.40 – 0.52` | `< 0.20` | `> 0.60` | `L/L` |

### 4.2 Electrolytes, Urea & Creatinine (Order Code: `ELEC`)

| Analyte / Test | Observation Code (LOINC) | Normal Reference Range | Panic Low Threshold | Panic High Threshold | Unit |
| :--- | :--- | :--- | :--- | :--- | :--- |
| **Sodium (`Na`)** | `2951-2` | `135.0 – 145.0` | `< 120.0` | `> 160.0` | `mmol/L` |
| **Potassium (`K`)** | `2823-3` | `3.5 – 5.0` | `< 2.8` | `> 6.2` | `mmol/L` |
| **Chloride (`Cl`)** | `2075-0` | `95.0 – 110.0` | `< 80.0` | `> 125.0` | `mmol/L` |
| **Bicarbonate (`HCO3`)** | `1963-8` | `22.0 – 32.0` | `< 10.0` | `> 40.0` | `mmol/L` |
| **Creatinine** | `2160-0` | `60.0 – 110.0` | N/A | `> 350.0` | `umol/L` |

---

## 5. Interconnected Provider Topology Graph Builders `[IMPLEMENTED]`

`SyntheticProviderRegistryGenerator` provides high-level builders that construct complete, referentially sound FHIR R5 topologies:

1. **Solo Practitioner Graph**: Generates a single `Practitioner`, an `Organization`, and a linking `PractitionerRole`.
2. **Specialist with Multiple Roles Graph**: Generates a single specialist `Practitioner` linked to multiple distinct `Organization` entities and `Location` records through separate roles (e.g. Visiting Medical Officer across public and private hospitals).
3. **Healthcare Organization with Locations & Endpoints**: Generates an acute hospital network topology including base hospital, outpatient clinics, surgical suites, and MLLP/REST `Endpoint` resources.
4. **Connected Provider Network**: Complete enterprise graph comprising `Organization`, `Location`, `Endpoint`, `HealthcareService`, `Practitioner`, `PractitionerRole`, and `Group`.
5. **Invalid Reference Graph (Negative Testing)**: Deliberately generates dangling references to non-existent organizations or locations to assert that Harmonia's referential integrity engine correctly rejects the transaction.
