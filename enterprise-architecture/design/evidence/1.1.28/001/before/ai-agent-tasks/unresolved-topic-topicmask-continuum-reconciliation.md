Task-Issued-At: UNRESOLVED

Event source: Instruction-entry timestamp unavailable; SA treatment required

# Topic and TopicMask Continuum reconciliation — Task Detail

**Agent** · Codex. **Issuer** · Solution Architect through the current Codex interaction.
**Origin** · `feature/BL-0003-Calliope-Topic-and-TopicMask`, Git
`f1d3c4a896731e05c40b1f21e4bf1b96bdcf3adb`,
tree `1846199b36bbfb07c87746c1367c059698b7120d`, accepted Architecture 1.1.26.
The existing uncommitted roadmap and Backlog Item 0003 revision 000.003 are protected.
**Review locator** · Prospective Architecture 1.1.28/001, conditional on the separate
1.1.27/002 review going first. The target locates this proposal and allocates no release.

## Operative issued instruction

````markdown
# Harmonia — Topic and TopicMask Continuum Reconciliation

## Assignment

**Repository:** `fhirfactory/harmonia`  
**Current branch:** `feature/BL-0003-Calliope-Topic-and-TopicMask`  
**Accepted Architecture baseline:** `1.1.26`  
**Software development target:** `0.1.0-SNAPSHOT`

### Objective

Update the Harmonia Architecture Continuum to reflect the accepted Topic and TopicMask design.

The authoritative design is:

`enterprise-architecture/design/offline-design/DRP-Topic-and-TopicMask.Final.001.001.md`

The design has already undergone review and qualified acceptance through Software HCRL 004, DR-10, with subsequent design-finalisation provenance.

**This assignment is to reconcile the Architecture Continuum with the accepted design, not to redesign Topic or TopicMask.**

Do not reopen previously accepted design decisions without identifying an actual, material contradiction requiring Solution Architect adjudication.

## 1. Verify repository state

Confirm:

- Current branch and HEAD.
- Working-tree changes, including the roadmap and Backlog Item 0003 revision 000.003.
- Accepted Architecture baseline 1.1.26.
- Accepted Software Foundation Reset, HCRL 009.
- The authority and qualifications of the accepted Topic/TopicMask design.

Preserve existing uncommitted work.

Do not modify or republish Architecture 1.1.26.

## 2. Establish the accepted design contract

Read the accepted Topic/TopicMask DRP and its applicable review decisions.

Extract the definitive requirements governing:

- Topic identity and equality.
- TopicEntry structure and ordering.
- Qualified and unqualified naming.
- Canonical name construction.
- Versioned and non-versioned canonical representations.
- Topic hierarchy and ancestry.
- TopicMask identity and equality.
- TopicMask criteria and matching.
- Version-aware matching.
- Exact and inclusive containment.
- Universal matching.
- Builder semantics.
- Logical interchange and serialization.

Distinguish accepted design requirements from historical implementation behaviour.

The exploratory Java implementation and its compatibility obligations have been superseded.

## 3. Reconcile the Architecture Continuum

Inspect the existing Topic and TopicMask architectural elements, their relationships and directly relevant architectural contracts.

Identify every element whose current definition contradicts or incompletely represents the accepted design.

Pay particular attention to the discrepancies recorded as R1 and R2 in:

`docs/backlog/BacklogItem-0003-Calliope-Topic-and-TopicMask.v000.003.md`

### Topic

Bring the canonical architecture into alignment with the accepted design concerning:

- Topic identity based on the ordered qualified path.
- Version independence of Topic identity.
- TopicEntry ordering and sequence semantics.
- Canonical naming and representations.
- Optional entry versions.
- Qualified versus unqualified names.
- Registry-related responsibilities and boundaries.

Do not reintroduce UUID-based runtime Topic identity merely to preserve the earlier Continuum definition.

Preserve the independent identities of architectural elements and InformationUnits.

### TopicMask

Bring the canonical architecture into alignment with the accepted design concerning:

- Structural mask identity and equality.
- Ordered matching criteria.
- Qualifier and value matching.
- Optional version constraints.
- Exact and inclusive containment.
- Explicit universal matching.
- Canonical mask representations.
- Applicable matching and validation semantics.

Do not reintroduce obsolete UUID-based runtime mask identity or superseded selector semantics.

## 4. Architectural scope and boundaries

Preserve the existing accepted architectural responsibilities of Calliope, Mneme, Mnemosyne, Petasos, Themis and the other components.

This activity does not authorise changes to their functional responsibilities.

Where an accepted architectural element references Topic or TopicMask, update its description or relationships only where necessary to accurately reflect the accepted Naming model.

Do not prematurely implement or redesign:

- InformationUnit.
- Pragma or Pragmatosis.
- PetasosParcel.
- Registry services.
- Subscription execution.
- Security policy evaluation.
- Persistence services.

Any necessary downstream architectural implications should be recorded for their respective backlog items.

Avoid broad, speculative changes throughout the Continuum.

## 5. Canonical source and architectural projections

Follow Harmonia's established architecture governance and canonical-source conventions.

The Markdown Architecture Continuum is authoritative.

ArchiMate is a projection of that source, not an independent authority.

Open Exchange XML is an interchange representation.

LaTeX and other publications are derived outputs.

Update the canonical Markdown elements and their directly affected relationships.

Identify any required projection or publication updates.

Do not silently substitute a generated projection for canonical architectural content.

Preserve stable architectural element identifiers wherever the underlying architectural concept remains the same.

## 6. Governance

This change modifies the Architecture Continuum and therefore requires appropriate Architecture Change Review governance.

Prepare the necessary ADD/HCRL evidence and allocate the change to the next appropriate Architecture baseline.

Architecture 1.1.26 remains immutable.

Coordinate with the existing pending Architecture HCRL 1.1.27/002 concerning ArchUnit retirement.

Do not overwrite, repurpose or silently close that review.

Preserve the accepted design's existing qualifications.

Where a qualification becomes resolved through Continuum reconciliation, record the resolution explicitly.

Do not silently discard unrelated qualifications.

## 7. Canonical validation

The previously identified missing Topic/TopicMask retained-input locator must be investigated as part of this activity.

Determine whether the locator can be corrected using the existing authoritative evidence without changing its meaning or provenance.

If the correct target is unambiguous, propose the precise correction.

If not, record the required Solution Architect decision.

Do not fabricate missing evidence or modify historical records to conceal the discrepancy.

Run applicable canonical validation and record its results.

Distinguish pre-existing failures from newly introduced issues.

## 8. Verification

Verify:

1. The updated Topic and TopicMask elements conform to the accepted design.
2. Relevant architectural relationships remain coherent.
3. Architectural element identifiers remain stable unless explicitly authorised otherwise.
4. No unrelated architectural semantics have changed.
5. The accepted 1.1.26 publication remains unchanged.
6. Relevant canonical validation passes, or remaining exceptions are explicitly recorded.
7. The existing software roadmap and Naming backlog candidate are preserved.
8. No Java implementation has been introduced.

Use exception-based review and minimise unnecessary processing of unrelated architectural material.

## 9. Deliverables

Produce:

1. A concise design-to-Continuum reconciliation report.
2. The proposed canonical Markdown changes.
3. Any directly necessary relationship updates.
4. The required Architecture HCRL/ADD candidate.
5. Canonical validation results.
6. The proposed treatment of the missing evidence locator.
7. Any genuine unresolved decisions requiring Solution Architect adjudication.

### Completion boundary

Do not commit or push.

Do not publish a new Architecture release.

Do not implement Java code.

Do not modify the accepted Naming design merely to make it agree with outdated Continuum elements.

Return the proposed changes and governance evidence for Solution Architect review.

**The intended outcome is an Architecture Continuum that accurately represents the already accepted Topic and TopicMask design, allowing Backlog Item 0003 to proceed without conflicting architectural definitions.**
````
