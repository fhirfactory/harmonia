# Person and Healthcare Subject Information Family

## 1. Purpose & Semantic Boundary

The **Person and Healthcare Subject** information family formalises the semantic models for human identity, identifier management, cross-authority identity correlation, governed identity corrections, healthcare subject context, and personal support/representation networks.

This family establishes the core human entities and contextual subject profiles that participate across all clinical, diagnostic, operational, and administrative workflows in Harmonia.

```mermaid
graph TD
    subgraph PersonIdentityModel ["Person Identity & Subject Context"]
        P["Person<br/>(Human Entity)"]
        PI["Person Identity<br/>(Governed Identity Information)"]
        ID["Identifier<br/>(Value + Authority / Namespace)"]
        ALIAS["Identity Alias<br/>(Governed Name & Alias Assertions)"]
        CORR["Identity Correlation Graph<br/>(Cross-Authority Identifier Linkage)"]
        CORR_AUDIT["Identity Correction<br/>(Governed Merge / Split / Rectification)"]
        SUBJ_CTX["Healthcare Subject Context<br/>(Contextual Subject Profile & Preferences)"]
    end

    subgraph Relationships ["Support Network & Representation"]
        REL_FAM["Family Relationship<br/>(Familial Association)"]
        REL_REP["Legal Representation<br/>(Authorised Representative / Guardian)"]
        REL_CARER["Carer Relationship<br/>(Nominated Carer / Support Role)"]
    end

    P -->|"identified by"| PI
    PI -->|"asserts"| ID
    PI -->|"known by"| ALIAS
    PI -->|"correlated via"| CORR
    PI -->|"corrected via"| CORR_AUDIT
    P -->|"participates as"| SUBJ_CTX
    P -.->|"source of"| REL_FAM
    P -.->|"source of"| REL_REP
    P -.->|"source of"| REL_CARER
```

---

## 2. Domain 03 Responsibility & Traceability

This information family derives directly from Domain 03 Business Information Responsibilities:

| Domain 03 Capability | Domain 03 Function / Feature | Domain 03 Information Responsibility | Realised Domain 04 Concepts |
| :--- | :--- | :--- | :--- |
| **`L2: Person Identity`** *(under `L1: Client Administration`)* | `Resolve Person Identifier`, `Correlate Person Identifiers`, `Maintain Person Identity Aliases`, `Apply Governed Person Identity Correction` | `Person Identity & Identifier Correlation Graph`, `Identity Aliases`, `Identity Merge/Split Audit Log` | `Person`, `Person Identity`, `Identifier`, `Identity Alias`, `Identity Correlation Graph`, `Identity Correction` |
| **`L2: Healthcare Subject Context`** *(under `L1: Client Administration`)* | `Establish Healthcare Subject Context`, `Govern Subject Demographic Context` | `Healthcare Subject Profile`, `Demographic History`, `Communication Preferences` | `Healthcare Subject Context`, `Demographic Trait Assertion`, `Communication Preference` |
| **`L2: Client Relationships & Support Network`** *(under `L1: Client Administration`)* | `Maintain Client Relationships`, `Govern Client Relationship Validity` | `Client Support Network & Legal Mandates`, `Representative Legal Mandate`, `Carer Contact Directory` | `Family Relationship`, `Carer Relationship`, `Legal Representation`, `Support Person Relationship` |

---

## 3. Principal Information Concepts

### 3.1 Person
- **Semantic Classification**: `Entity`
- **Definition**: The identifiable biological and legal human entity.
- **Architectural Scope**: Represents the enduring human being independently of any specific clinical encounter, healthcare facility, or operational role. A `Person` exists prior to and outside of any interaction with the healthcare system.

### 3.2 Person Identity
- **Semantic Classification**: `Assertion / Identity Bundle`
- **Definition**: The governed set of identity traits, identifiers, names, and demographic assertions that establish the recognised identity of a `Person`.
- **Architectural Scope**: Governed by the `Person Identity` capability. Acts as the governed identity anchor linking identifiers, aliases, and correlation graphs.

### 3.3 Identifier
- **Semantic Classification**: `Assertion`
- **Definition**: A distinct identifier token issued within a specific namespace authority to identify a person.
- **Key Semantic Decomposition**:
  - `Identifier Value`: The lexical string or token representing the identity code.
  - `Identifier Authority / Namespace`: The issuing organisation, national scheme, or local domain establishing the uniqueness and validity of the value.
- **Architectural Rule**: An identifier is NEVER modelled as an unqualified, opaque string. It is always qualified by its originating authority/namespace.

### 3.4 Identity Alias
- **Semantic Classification**: `Assertion`
- **Definition**: A name, title, or alias by which a person is known in a specific legal, cultural, or operational context.
- **Key Characteristics**: Alias type (e.g. *Legal Name*, *Preferred Name*, *Former Name*, *Emergency Alias*), name components, effective period, and verification status.

### 3.5 Identity Correlation Graph
- **Semantic Classification**: `Relationship / Correlation Graph`
- **Definition**: The network of qualified associations linking distinct identifiers originating from different authority namespaces to the same underlying `Person`.
- **Key Characteristics**: Linkage type (e.g. *Deterministic Match*, *Probabilistic Match*, *Manually Verified*), match confidence score, verification authority, and effective period.
- **Authority Invariant**: Cross-authority correlation links distinct identifier namespaces without merging or transferring their originating authorities, and without implying destructive consolidation into a single authoritative source identity.

### 3.6 Identity Correction
- **Semantic Classification**: `Activity / Governance Record`
- **Definition**: The formal, audited rectification of identity errors, encompassing person record merges, split operations, and demographic corrections.
- **Key Characteristics**: Correction type (*Merge*, *Split*, *Rectification*), target identities, rationale, authorizing officer, and timestamp.
- **Governance Invariant**: Identity corrections preserve full provenance and audit logs. They do not destructively overwrite historical assertions or break historical provenance chains. Source authorities remain distinct and intact.

### 3.7 Healthcare Subject Context
- **Semantic Classification**: `Contextual Binding`
- **Definition**: The subject-oriented context in which health information, clinical activity, or care delivery concerns a `Person`.
- **Key Characteristics**: Demographic history, language/interpreter requirements, cultural and spiritual preferences, communication preferences, and vital status assertions (e.g., verified birth date, death notification).
- **Architectural Scope**: Captures the individual's profile as a consumer of health services without assuming active patient status in a specific facility.

---

## 4. Canonical Role Taxonomy & Semantic Demarcations

### 4.1 Strict Conceptual Distinctions
Harmonia enforces a strict three-way conceptual demarcation:

$$\text{Person} \neq \text{Patient [Business Role]} \neq \text{Healthcare Subject Context}$$

1. **`Person`**: The enduring human entity.
2. **`Healthcare Subject Context`**: The contextual profile of the person as a subject of health care. `Healthcare Subject` is **NOT** a Domain 03 Business Role.
3. **`Patient`**: A Domain 03 **Business Role** assumed by a `Person` when actively receiving care within a specific healthcare collaboration or encounter. Not every Person is currently a Patient.

### 4.2 Separation of Relationship Roles from Business Roles
- Roles within personal and family relationships (e.g. *Parent*, *Child*, *Sibling*, *Nominated Carer*, *Guardian*, *Authorised Proxy*) are **Relationship Roles** belonging to specific Information Relationships.
- They SHALL NOT be conflated with or used to create Domain 03 **Business Roles** (Guardrail 8).

### 4.3 Distinct Authority & Relationship Types
Harmonia strictly prohibits inferring one type of relationship or authority from another. The following remain independent semantic assertions:
- **Care Team Membership** $\neq$ **Caring Responsibility**;
- **Caring Responsibility** $\neq$ **Advocacy / Support**;
- **Advocacy** $\neq$ **Legal Representation / Guardianship**;
- **Legal Representation** $\neq$ **Information Access Authority**;
- **Information Access Authority** $\neq$ **Consent Directive Authority**;
- **Consent Directive Authority** $\neq$ **Clinical Decision-Making Authority**.

---

## 5. Personal Relationships & Containment Guardrail

### 5.1 Person Containment Rule
In accordance with the frozen Domain 04 foundational guardrail (Guardrail 9):

> **Person-oriented concepts SHALL NOT use recursive containment merely to represent familial, social, care, representation or authority relationships. Those semantics SHALL use appropriate Information Relationships or Collections.**

### 5.2 Key Information Relationships

| Relationship | Source & Role | Target & Role | Type / Qualification | Governed Evidence & Semantics |
| :--- | :--- | :--- | :--- | :--- |
| **Family Relationship** | `Person` (*Family Member*) | `Person` (*Family Member*) | *Familial Association* (e.g. Parent, Child, Sibling, Spouse) | Biological, legal, or social relationship with optional effective period and verification. |
| **Carer Relationship** | `Person` (*Carer / Support*) | `Person` (*Care Recipient*) | *Carer Association* | Nominated primary/secondary carer supporting the individual's daily living and care needs. |
| **Legal Representation** | `Person` (*Authorised Representative*) | `Person` (*Represented Subject*) | *Legal Mandate* (e.g. Guardian, Appointed Proxy, Substitute Decision Maker) | Verified statutory or judicial mandate specifying scope of legal authority, validity period, and evidence documentation. |
| **Support Person** | `Person` (*Nominated Support*) | `Person` (*Supported Subject*) | *Informal Support* | Nominated contact or advocate for communication and emotional support. |

---

## 6. Assertion-Level Governance & Provenance

1. **Federated Identifier Authorities**: Each `Identifier` retains its link to its originating authority (e.g., national identifier scheme, hospital medical record administration, pathology laboratory). Harmonia does not become the originating authority by holding or correlating identifiers.
2. **Demographic Assertions & Provenance**: Individual demographic assertions (e.g., date of birth, home address, indigenous status) carry their own source provenance, assertion timestamp, and verification status.
3. **Audit Continuity for Merges/Splits**: When two person identities are merged under governed administration, both historical identifiers and assertion records remain linked through the `Identity Correction` audit log, allowing historical reconstruction of data states at any prior point in time.

---

## 7. Candidate Information Assembly Participation

The concepts in this family participate in several downstream candidate Information Assemblies without transferring originating authority:

1. **Healthcare Subject Context Assembly**: Combines `Person Identity`, `Healthcare Subject Context`, active `Identity Aliases`, communication preferences, and primary contact details.
2. **Client Support Network Assembly**: Aggregates verified `Carer Relationships`, `Legal Representation` mandates, and emergency contacts for a subject.
3. **Encounter Context Assembly**: Embeds the `Healthcare Subject Context` into the operational admission/encounter context.
4. **Longitudinal Clinical Record Assembly**: Organises clinical history, observations, and care activities around the governed `Person Identity`.
