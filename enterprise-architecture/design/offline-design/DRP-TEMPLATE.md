# Design Review Proposal (DRP)

**DRP:** DRP-<identifier>  
**Title:** <short architectural proposition>  
**Status:** Draft  
**Prepared for:** Architecture Review  
**Prepared by:** <author / architecture peer>  
**Solution Architect:** Mark Hunter  
**Date:** <date>

## 1. Feature Realisation

State the Feature(s) whose realisation is the primary architectural purpose of this DRP. A DRP should normally realise one or more Features.

### 1.1 New or Updated Feature Details

Identify existing Feature(s), or define/refine the Feature and its Enterprise Capability context where no adequate Feature exists. Map the Enterprise Capability/Feature context to the applicable Business and Business-Enabling capabilities.

If no defensible Feature and architectural placement can be established, reconsider why the DRP exists.

### 1.2 Associated Requirements

Identify applicable existing Requirements and explicitly author requirements exposed by the design that were previously implicit. Requirements architecture remains the responsibility of the architects; it is not delegated to implementation.

### 1.3 Associated Strategy and Motivation

Identify applicable Axioms, Courses of Action, Principles, Constraints, Goals/Outcomes and other Strategy/Motivation elements. Add/refine missing architecture where the DRP exposes a genuine gap.

## 2. Proposal

State what is proposed to become architecture, not how the Technical Lead should implement it.

## 3. Design Context

Describe the context, existing architecture, discoveries and gap being resolved.

## 4. Architectural Definition

Define the architectural responsibilities, behaviours, information semantics, interactions, boundaries and invariants needed to realise the Feature(s).

## 5. Explicitly Not Prescribed

Identify solution-design choices deliberately left to the Technical Lead/Lead Developer.

## 6. Evidence and Rationale

Distinguish governed architecture, historical discovery evidence, implementation evidence and architectural judgement.

## 7. Relationship to Current Architecture

State what this extends, clarifies, confirms, supersedes or leaves unchanged.

## 8. Conformance Expectations

Describe architectural conformance/non-conformance.

## 9. Open Questions / Matters for Review

List genuine unresolved architectural matters requiring Solution Architect adjudication.

## 10. Continuum Update

### 10.1 Motivation Updates
Requirements, Axioms, Principles, Constraints, Goals/Outcomes and their relationships.

### 10.2 Strategy Updates
Enterprise Capabilities, Features, Courses of Action and mappings to Business/Business-Enabling capabilities.

### 10.3 Business Layer — Information Architecture Updates
Business Objects, Topics and semantic/containment relationships.

### 10.4 Business Layer — Feature-Scoped Function, Service and Process Updates
Business behaviour needed to express/realise the Feature(s).

### 10.5 Application Layer — Information Architecture Updates
Application-level information contracts/concepts that are architecturally significant; do not prescribe implementation class/DTO/schema decomposition.

### 10.6 Application Layer — Feature-Scoped Enterprise Capability Realisation
Application Functions, Services, Processes and Interactions that realise/support the Feature(s).

### 10.7 Application Layer — Structural / Component Updates
Architecturally significant Application Components/structure and assignment/realisation relationships. Do not prescribe internal implementation decomposition.

### 10.8 Cross-Layer and Cross-Feature Relationships
Explicitly connect realisation to Feature/Capability/Requirement/Axiom/CoA/information architecture using the semantically correct relationships.

### 10.9 Explicitly Unaffected / No-Change Architecture
Identify useful no-change boundaries and architecture referenced only as context.

## 11. Review Decision

**Decision:** Pending  
**Decision authority:** Solution Architect / Architecture Review  
**Decision notes:** (To be completed during review.)

## 12. Post-Decision Actions

Record only actions arising from the accepted/rejected/amended proposal.

## Appendix A — Design Provenance

Informative rather than normative; record principal design sources and lineage.
