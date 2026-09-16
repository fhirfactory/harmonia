# Architectural Security Gap Analysis

This document tracks identified architectural security gaps across Harmonia, their associated risk levels, proposed and implemented mitigations, and priority status.

---

## Gap Tracking Matrix

| ID | Component | Description & Current Behaviour | Security Risk | Proposed Mitigation | Status | Priority |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **GAP-01** | `pylai-fhir-registry` | `FhirSecurityInterceptor` previously inspected raw header string literals without policy decoupling. | Bypass or inconsistent policy application across gateways. | Replaced with `ThemisService` evaluating deterministic default-deny policies. | **RESOLVED** | High |
| **GAP-02** | `mnemosyne-clinical` | `FhirStorageService` previously lacked authorization hooks, trusting all internal callers. | Lateral movement / direct repository write bypass. | Embedded Themis persistence authorization gates into `FhirStorageService`. | **RESOLVED** | High |
| **GAP-03** | `energeia-ponos` | Asynchronous task dispatcher previously lacked execution claims validation. | Privilege escalation where background workers amplify submitter rights. | Dual-authority evaluation in `PragmaWorkflowDispatcher` and `ErgonSecurityDefinition`. | **RESOLVED** | High |
| **GAP-04** | `calliope` / `pragma` | Task messages across queues previously lacked immutable caller identity context. | Inability to audit or re-evaluate caller privileges at execution time. | Extended `Pragma` with immutable originating context and FHIR Task extensions. | **RESOLVED** | High |
| **GAP-05** | Service-to-Service | Internal services lacked explicit controlled identities and authorities. | Implicit trust assumptions between internal microservices. | Established `HarmoniaServiceIdentities` with least-privilege authority mappings. | **RESOLVED** | Medium |
| **GAP-06** | PKI Message Signatures | Artemis queue message envelopes rely on broker security without cryptographic payload signing. | Message tampering in compromised broker environments. | Introduce asymmetric PKI envelope signing in subsequent release iteration. | **PLANNED** | Medium |
| **GAP-07** | ABAC Dynamic Policy UI | Policy management is configuration and bootstrap driven without dynamic UI administration. | Slower turnaround for runtime rule adjustments. | Design administrative policy management APIs and UI in Agora/Themis Phase 2. | **PLANNED** | Low |
