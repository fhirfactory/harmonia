# Strategic Resources

## Overview & Metamodel Reference

In enterprise architecture, the **Resources** layer identifies the foundational assets—tangible or intangible—owned, controlled, or relied upon by an organisation to realise its capabilities and implement its courses of action.

Harmonia references the resource concepts established in **ArchiMate® 3.2** while applying explicit qualification to preserve architectural clarity and prevent the inflation of implementation artefacts into strategy assets.

---

## 1. ArchiMate 3.2 Semantics & Harmonia Qualification

### ArchiMate 3.2 Resource Definition
According to the ArchiMate 3.2 specification:
> *"A resource represents an asset owned or controlled by an individual or organization."*

In ArchiMate, resources are positioned at the Strategy layer alongside **Capabilities** and **Courses of Action**, serving as the assets that capabilities require to function.

### Harmonia's Explanatory Qualification
In complex healthcare integration environments, applying the ArchiMate resource definition without qualification inevitably results in "metamodel theatre"—creating diagram boxes for every database, schema, framework, software library, or message stream.

Harmonia strictly qualifies the concept:

> **A resource belongs in Strategy only where the asset itself possesses enduring strategic significance in enabling Harmonia's capabilities or courses of action, and where its absence would materially impair Harmonia's architectural mission.**

Under this qualification:
- An asset is **not** a Strategic Resource merely because it can be drawn as a box on an architecture diagram.
- Architecture clarity takes strict precedence over metamodel formalism.
- Assets are classified by their authentic architectural nature, not promoted to Strategy for visibility.

---

## 2. The Strategic Significance Test

To adjudicate whether a candidate asset qualifies as a Domain 02 Strategic Resource, Harmonia applies a formal qualification test:

> **The Strategic Significance Test:**  
> *"If this asset disappeared or became unavailable, would Harmonia's strategic ability to realise its intended capabilities or Courses of Action materially change?"*

### Adjudication Decision Tree

```text
Is the asset an enduring external or foundational asset upon which Harmonia depends?
                     │
        ┌────────────┴────────────┐
       YES                        NO
        │                         │
Does its absence alter     Is it managed health information?
Harmonia's strategic               │
ability to realise                 ├── YES ──► Domain 04 (Information Architecture)
capabilities/COAs?                 │
        │                  Is it an internal software construct/schema?
   ┌────┴────┐                     │
  YES        NO                    ├── YES ──► Domain 05 (Application Architecture)
   │         │                     │
   │    Is it an external  Is it operational audit/evidence data?
   │    legal/privacy rule?        │
   │         │                     ├── YES ──► Domain 08 (Security Architecture)
   │         ├── YES ──► Domain 01 │
   │         │          (Motivation)Is it a simulation/test harness?
   │         │                     │
   │         └── NO  ──► Relegate  └── YES ──► Domain 10 (Testing Architecture)
   ▼
ADMIT as Domain 02
Strategic Resource
```

1. **Strategic Resources (Domain 02)**: External normative standards, jurisdictional directory/identifier specifications, and clinical terminologies upon which Harmonia depends to deliver interoperability and semantic governance.
2. **Managed Information (Domain 04)**: Data that Harmonia ingests, harmonises, correlates, and manages (e.g., patient longitudinal records, practitioner graphs). Information managed *by* Harmonia is an outcome of capability execution, not an enabling strategic resource.
3. **Application Constructs (Domain 05)**: Internal execution envelopes, task structures, and software library artefacts (e.g., Pragma task envelopes, messaging contracts).
4. **Security & Audit Records (Domain 08 / 04)**: Provenance logs, security assertions, and audit trails generated through execution.
5. **Verification Testbeds (Domain 10)**: Synthetic personas, simulation harnesses, and test suites used for offline verification.
6. **External Constraints (Domain 01)**: Legislative acts, privacy principles, and regulatory mandates that define governing boundaries rather than enabling operational assets.

---

## 3. Directory Structure & Navigation

The detailed catalogue of adjudicated strategic resources and formal disposition records is documented in:

- [Strategic Resources Catalogue & Candidate Adjudication](strategic-resources.md): Full evaluation of admitted strategic resources and formal relegation dispositions for candidate assets.

To navigate across adjacent Domain 02 Strategy areas:
- [Domain 02 Strategy Overview](../README.md)
- [Enterprise Capabilities](../capabilities/enterprise-capabilities.md)
- [Strategic Courses of Action](../courses-of-action/index.md)
- [Strategic Logical Component Responsibilities](../strategic-views/logical-component-responsibilities.md)
