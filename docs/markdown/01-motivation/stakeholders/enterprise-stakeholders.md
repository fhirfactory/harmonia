# Enterprise Stakeholders

## Overview

Enterprise Stakeholders represent the human actors, clinical groups, delivery organizations, and operational teams that participate actively within the regional health integration ecosystem. They hold direct stakes in the performance, safety, reliability, and governance of the Harmonia Health Integration Environment (HIE).

Harmonia recognizes strictly **six agreed enterprise stakeholders**. External regulatory agencies, standards bodies, and national identifier registrars are treated distinctly as [External Authorities & Regulatory Bodies](external-authorities.md).

---

## 1. Regional Health Network Operator

### Profile & Role
The Regional Health Network Operator is the statutory or corporate governing body responsible for orchestrating, funding, and overseeing healthcare delivery across a defined regional jurisdiction (e.g., a state health department, local hospital network, or integrated care board).

### Core Responsibilities
- Strategic governance and regional healthcare delivery policy.
- Establishing and monitoring cross-facility service level agreements (SLAs) and clinical performance indicators.
- Ensuring statutory compliance with jurisdictional health privacy, clinical governance, and data protection legislation.
- Funding, procuring, and overseeing regional digital health infrastructure and integration platforms.

### Principal Architectural Concerns
- **Network-Wide Availability & Resilience**: The platform must operate continuously without single points of failure across regional public and private hospital networks.
- **Vendor-Independent Longevity**: Avoiding commercial lock-in to proprietary Electronic Medical Record (EMR) vendors, ensuring regional health data remains an enduring asset under public governance.
- **Auditability & Clinical Governance**: Comprehensive, tamper-evident audit trails that prove compliance with regulatory directives and enable forensic review of clinical incidents.
- **Systemic Risk Mitigation**: Minimizing the probability of severe systemic failures, data corruption, or unrecoverable message loss during regional health emergencies.

---

## 2. Healthcare Delivery Organizations

### Profile & Role
Healthcare Delivery Organizations (HDOs) encompass the institutional entities providing clinical care across the region: acute public hospitals, private hospital networks, primary health clinics, specialist outpatient centers, and diagnostic pathology and medical imaging facilities.

### Core Responsibilities
- Providing direct patient care across emergency, inpatient, ambulatory, and diagnostic settings.
- Generating, maintaining, and ingesting operational clinical transactions (admissions, transfers, discharges, orders, results, clinical notes) through local hospital information systems (HIS), patient administration systems (PAS), and laboratory information systems (LIS).
- Maintaining local boundary integration gateways and adhering to regional messaging protocols.

### Principal Architectural Concerns
- **Reliable Ingress & Egress Integration**: Guaranteeing that clinical messages dispatched to Harmonia are reliably accepted and preserved without silent loss or unhandled dropouts.
- **Low Latency & High Throughput**: Fast turnaround times for critical pathology results, radiology reports, and patient admission feeds without queuing delays.
- **Standards-Compliant Boundary Interoperability**: Seamless support for legacy messaging standards (HL7 v2.x via MLLP) alongside modern FHIR RESTful interactions, avoiding disruptive upgrades to legacy hospital software.
- **Clear Demarcation of Responsibility**: Explicit integration boundaries defining where the delivery organization's operational responsibility ends and Harmonia's management begins.

---

## 3. Clinicians & Care Teams

### Profile & Role
Clinicians and Care Teams represent the front-line healthcare workforce: medical practitioners, nurses, clinical specialists, allied health professionals, pharmacists, and multi-disciplinary care coordinators operating at the direct point of care.

### Core Responsibilities
- Diagnosing clinical conditions, formulating treatment plans, and administering patient care.
- Ordering diagnostic investigations, prescribing medications, and documenting clinical findings.
- Reviewing historical health records, previous test results, and discharge summaries to inform clinical decision-making.

### Principal Architectural Concerns
- **Timely Information Availability**: Point-of-care access to complete, up-to-date clinical information without lag or missing historical encounters.
- **Semantic Fidelity & Clinical Context**: Clinical observations, units of measure, reference ranges, and diagnostic notes must be preserved without lossy truncation, confusion of normal/abnormal flags, or distortion of clinical nuance.
- **Elimination of Data Fragmentation**: Synthesized, longitudinal patient views that reduce duplicate diagnostic testing and eliminate dangerous blind spots.
- **Explicit Information Authority**: Clear visual and semantic distinction between authoritative hospital-validated records and preliminary, informational, or anecdotal patient-reported data.

---

## 4. Patients & Care Recipients

### Profile & Role
Patients and Care Recipients are the individual citizens, families, and communities receiving healthcare services across the regional network. They are the ultimate beneficiaries and primary subjects of all health data managed by Harmonia.

### Core Responsibilities
- Engaging with clinicians and healthcare providers throughout episodes of care.
- Providing accurate demographic, personal, and medical history.
- Exercising personal privacy choices and statutory rights regarding information sharing where applicable.

### Principal Architectural Concerns
- **Patient Safety & Identity Protection**: Guaranteeing that personal clinical events are never cross-associated with another individual, preventing severe medication errors, misdiagnoses, and incorrect surgical interventions.
- **Confidentiality & Privacy**: Ensuring sensitive health records (mental health, sexual health, genetic testing) are strictly protected against unauthorized access, eavesdropping, and inadvertent leakage into operational log streams.
- **Continuity of Care**: Seamless information transfer between disparate healthcare facilities so that clinical history follows the patient across the continuum of care.

---

## 5. Healthcare Directory Stewards & Registrars

### Profile & Role
Healthcare Directory Stewards and Registrars are the specialized operational custodians responsible for maintaining authoritative regional registries of healthcare providers, individual clinicians, clinical organizations, and electronic service delivery endpoints.

### Core Responsibilities
- Curating authoritative master directories of healthcare practitioners, qualifications, active credentials, and organizational affiliations.
- Verifying and maintaining electronic service delivery endpoints (secure messaging addresses, FHIR endpoints, MLLP port mappings).
- Managing provider registry updates, deprecations, practitioner relocations, and retirement events.

### Principal Architectural Concerns
- **Secure Endpoint Governance**: Ensuring clinical messages and notifications are routed exclusively to authenticated, currently authorized practitioner endpoints.
- **Accurate Practitioner Role Representation**: Distinguishing between an individual clinician, their legal practitioner role, their accredited specialty, and their specific institutional practice location.
- **Reliable Directory Change Propagation**: Ensuring directory updates (e.g., changes in clinic address or revocation of practitioner credentials) propagate through integration workflows rapidly and deterministically.

---

## 6. Platform Operations & Integration Engineers

### Profile & Role
Platform Operations and Integration Engineers are the systems reliability engineers (SREs), DevOps professionals, integration middleware developers, and technical support teams tasked with running, monitoring, and maintaining the Harmonia platform.

### Core Responsibilities
- Deploying, scaling, and configuring Harmonia runtime services across regional data centers and cloud infrastructure.
- Monitoring system health, message queues, throughput, network latency, and integration error rates.
- Diagnosing integration failures, investigating communication anomalies, and conducting incident retrospectives.

### Principal Architectural Concerns
- **Deep Diagnosability Without PHI Leakage**: Rich operational metrics, traces, and diagnostic logs that provide full visibility into message processing without emitting Protected Health Information (PHI) into non-clinical tooling.
- **Zero Unmonitored Message Loss**: Deterministic verification regarding the progression state of incoming events, ensuring that failed downstream deliveries are immediately flagged rather than lost in silent dead-letter sinks.
- **Observable Multi-Stage Activity State**: Transparent visibility into complex, distributed workflows (such as fan-out distributions) where tasks progress across multiple hops and destination endpoints.
- **Deterministic Failure Semantics**: Clean separation of active operational coordination from authoritative durable state, preventing data corruption during unexpected node crashes or network partitions.
