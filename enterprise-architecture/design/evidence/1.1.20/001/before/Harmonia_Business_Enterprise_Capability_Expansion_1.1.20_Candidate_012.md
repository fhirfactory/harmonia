# Harmonia 1.1.20 — Proactive Business-to-Enterprise Capability Expansion
**Candidate 012 — designed for completeness-first architectural review, not accepted incorporation.**  
**Source:** `fhirfactory/harmonia` / `Capability-Map-Consolidation` / pinned commit `fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374`.  
**Authority:** Governed Continuum and agreed DRP thinking have equal evidentiary standing. Existing names/identities are not automatically retired or changed.  
**Action:** Proactively expand detected capability gaps, including justified CT2 additions; new semantics remain review candidates. No Codex task, repository change or migration authorisation.  

## Executive summary
The design rule has changed: **do not leave known or strongly inferred capability gaps as a list of questions when we can draft a defensible, scope-relevant candidate now.** Err towards a broad working target, making overreach visible and correctable rather than risking a second wholesale reconstruction. This is not a directive to overclaim implementation coverage.

| Layer | Candidate 006 | Candidate 012 | Candidate additions |
|---|---:|---:|---:|
| Business CT1 | 15 | 15 | 0 |
| Business CT2 | 86 | 86 | 0 |
| Business CT3 | 258 | 274 | 16 |
| Business Features | 1,299 | 1,379 | 80 |
| Enterprise CT1 | 9 | 9 | 0 |
| Enterprise CT2 | 47 | 51 | 4 |
| Enterprise CT3 | 139 | 222 | 83 |
| Enterprise Features | 700 | 1,115 | 415 |

**All 397 original CT3s and all 1,999 original Features from Candidate 006 remain intact.** This file describes additions; the paired JSON contains the *full* combined Business and Enterprise hierarchies. All added CT3s have five distinct candidate Features, each with a specific assessment criterion.

## Architectural rules and review standing
1. **Stakeholder Journey → Business value → Information Systems ability → granular Service promise → eventual Information Systems organisation.** This is the reasoning order, not an assertion of one-to-one mapping or of automatic delivery.
2. **No facet-derived taxonomy.** Names such as Mneme, Themis, Dokimasia, Artemis and Praxis inform and test what is needed; a Capability does not inherit their current component split.
3. **The Continuum and DRPs both constrain the candidate.** The DRPs express agreed thinking while explicit open questions stay unresolved. Existing governed identities and source evidence remain intact.
4. **Propose changes proactively.** If source evidence or a Journey demonstrates a plausible omission, model it now as `PROPOSED`, with evidence and business demand. Do not treat number of features as an adequacy metric on its own.
5. **Existing and proposed items remain distinguishable.** The original Candidate 006 elements are exact preserved copies; new codes are *review locators*, not UUIDs or approved canonical names.
6. **No inherited satisfaction.** An apparently complete map is not proof that a Service, Feature, Journey stage or Business outcome is implemented or tested.
7. **Retrieve → Check → Update → ProcessSomething → Commit** is a test lens, not a mandatory lifecycle; `ProcessSomething` is explicitly not `ArchiMate::Process`, and execution success does not establish authoritative commitment.
8. **Scope versus delivery.** Cross-sector, research, participant and funding support remains a legitimate architectural scope; model presence does not commit a particular Harmonia R1/R2 implementation.

## Four additional Enterprise CT2 responsibilities
| Review ref | Enterprise CT1 | CT2 | Reason a separate responsibility is defensible |
|---|---|---|---|
| `E05.06` | Identity, Trust & Information Protection | **Authorisation Policy & Effective Authority Management** | Define and maintain authoritative policy and effective authorisation representations independently of login, distribution and local enforcement. |
| `E06.07` | Digital Work, Events & Temporal Coordination | **Work Acceptance & Outcome Reconciliation** | Distinguish work acceptance, execution outcome, durable information commitment and the reconciliation of uncertain transitions. |
| `E08.07` | Operational Observability, Assurance & Resilience | **Audit, Provenance & Assurance Evidence Recording** | Create and preserve distinct immutable audit and provenance evidence and accountable assurance observations without conflating operational logs. |
| `E08.08` | Operational Observability, Assurance & Resilience | **Evidence Collation, Export & Controlled Release** | Select, collate, validate and release audit, provenance and assurance evidence while preserving provenance, completeness and authorisation. |

**Why the new CT2s?** Policy/Passport *authority* cannot be reduced to policy *determination* or *propagation*; recording immutable audit/provenance evidence is not the same as reporting it; dereferenced assembly and authorised *release* is distinct from evidence custody; and accepting work is not the same as completing an Ergon or committing Information Unit state. All four are review proposals, not statements that four new code modules must exist.

## Detailed Enterprise Capability extensions
### E01 — Governed Information Custody
The system can hold, preserve, recover and account for governed information.

#### E01.01 — Governed Information Persistence
**E01.01.04 — Authoritative Commitment Outcome**  
Report a uniquely interpretable acceptance, rejection, absence or indeterminate disposition for an attempted durable change.  
*Candidate Business demand:* `B10.01` Health Record Stewardship, `B10.07` Information Correction & Reconciliation, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `MN`, `PERSIST`, `IU`; *review findings:* R06, R16. **Standing: Proposed, not verified.**

- `E01.01.04.F01` **AcceptedStateVerified** — test: Every reported committed result is read back with the same governed identity and version.
- `E01.01.04.F02` **RejectedStateUnchanged** — test: Rejected changes leave the authoritative version and integrity state unchanged.
- `E01.01.04.F03` **OutcomeUncertaintyExplicit** — test: Interrupted commit tests return an indeterminate outcome rather than invented success.
- `E01.01.04.F04` **ReplayCorrelationPreserved** — test: Repeated requests are correlated to the original disposition without false duplicate acceptance.
- `E01.01.04.F05` **CommitEvidenceResolvable** — test: The commit response carries enough references to retrieve authoritative disposition evidence.

**E01.01.05 — Independent Persistence Custody**  
Provide governed durable custody without binding authoritative identity or lifecycle to a storage provider or cache.  
*Candidate Business demand:* `B10.01` Health Record Stewardship, `B10.06` Retention, Disclosure & Record Accountability.  
*Source context:* `PERSIST`, `DIST`, `IU`; *review findings:* R05, R20. **Standing: Proposed, not verified.**

- `E01.01.05.F01` **ProviderIdentityIndependence** — test: Replacing a provider leaves governed identity and version references resolvable.
- `E01.01.05.F02` **DurableBeforeAcknowledgement** — test: No durable-acceptance acknowledgement precedes required persistence evidence.
- `E01.01.05.F03` **UnknownProviderOutcomeReported** — test: Provider timeouts produce a bounded uncertain outcome with reconciliation route.
- `E01.01.05.F04` **DomainInstanceIsolation** — test: A failure in one persistence domain does not silently change another domain state.
- `E01.01.05.F05` **CustodyEvidenceTraceable** — test: Stored unit, version and provider event can be correlated without exposing provider IDs as authoritative.

#### E01.02 — Information Version & History Management
**E01.02.04 — Versioned Correction & Qualification**  
Correct or qualify governed information while preserving prior versions, references and immutable history constraints.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B10.07` Information Correction & Reconciliation, `B11.03` Clinical Governance & Practice Assurance.  
*Source context:* `IU`, `MN`, `PERSIST`; *review findings:* R05, R09. **Standing: Proposed, not verified.**

- `E01.02.04.F01` **PriorVersionRetained** — test: Correction leaves previous immutable versions retrievable.
- `E01.02.04.F02` **CorrectionLineageLinked** — test: Every correction identifies its antecedent and reason where required.
- `E01.02.04.F03` **HistoricalReferenceStable** — test: References to an earlier version continue to resolve to that earlier version.
- `E01.02.04.F04` **ConflictingCorrectionDetected** — test: Competing updates identify version or state conflict without silent overwrite.
- `E01.02.04.F05` **QualifiedRecordVisible** — test: Affected readers can distinguish original content from later correction or qualification.

#### E01.03 — Information Integrity Management
**E01.03.04 — Cross-Representation Integrity**  
Preserve identity, relationship and validity assertions across representation changes or independent derived Information Units.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B10.05` Health Knowledge Stewardship, `B13.05` Health Research Execution.  
*Source context:* `IU`, `REF`, `MODEL`; *review findings:* R05, R09. **Standing: Proposed, not verified.**

- `E01.03.04.F01` **RepresentationBindingValid** — test: Exactly one governed representation is associated with the Information Unit being tested.
- `E01.03.04.F02` **DerivationLineageIntact** — test: Separately governed derivatives retain a traceable source relationship.
- `E01.03.04.F03` **MetadataDomainSeparation** — test: Container, content and Information Unit metadata remain distinguishable in tests.
- `E01.03.04.F04` **IntegrityMismatchReported** — test: Corrupted content or incompatible metadata produces an attributable exception.
- `E01.03.04.F05` **NativeRepresentationTraceable** — test: Preserved native content remains interpretable with its source representation definition.

**E01.03.05 — Custody & Reference Consistency Assessment**  
Assess whether references, relationships and authoritative custody agree after changes, migration or partial failure.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B10.07` Information Correction & Reconciliation, `B12.04` Enterprise Risk & Resilience.  
*Source context:* `IU`, `DIST`, `RES`; *review findings:* R05, R20, R24. **Standing: Proposed, not verified.**

- `E01.03.05.F01` **OrphanReferenceDetected** — test: Broken governed references are detected in a sampled migration.
- `E01.03.05.F02` **RelationshipEndpointsVerified** — test: Relationship endpoints resolve to existing governed identities or explicit tombstone states.
- `E01.03.05.F03` **ReplicaDivergenceReported** — test: Detected divergent authoritative copies are reported without silent resolution.
- `E01.03.05.F04` **IntegrityRepairTraceable** — test: Corrective action retains the original exception and remediation evidence.
- `E01.03.05.F05` **AssessmentRepeatable** — test: Repeated assessment of an unchanged snapshot yields equivalent findings.

#### E01.04 — Information Retention & Recovery
**E01.04.05 — Policy-Governed Retire & Retrieval**  
Enforce retention and retirement decisions without destroying information that must remain recoverable or independently evidenced.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `IU`, `PERSIST`, `RES`; *review findings:* R05, R24. **Standing: Proposed, not verified.**

- `E01.04.05.F01` **RetentionEligibilityEnforced** — test: Disallowed retirement requests are rejected with reason.
- `E01.04.05.F02` **LegalHoldPreserved** — test: A protected or held information version remains available through retention tests.
- `E01.04.05.F03` **RetirementStateVisible** — test: Readers can distinguish retained, retired and unavailable dispositions.
- `E01.04.05.F04` **ArchiveVersionIntegrity** — test: Archived versions round-trip without altered identity, timestamps or content hashes.
- `E01.04.05.F05` **RecoveryExceptionsReported** — test: Unrecoverable archival states produce explicit exceptions rather than empty results.

#### E01.05 — Governed Information Reference Management
**E01.05.04 — External Media Reference Governance**  
Maintain durable, controlled references to large content managed outside ordinary Information Unit persistence.  
*Candidate Business demand:* `B10.01` Health Record Stewardship, `B10.06` Retention, Disclosure & Record Accountability, `B09.02` Clinical Equipment Readiness.  
*Source context:* `IU`, `NFR`, `REF`; *review findings:* R20. **Standing: Proposed, not verified.**

- `E01.05.04.F01` **MediaNotStoredInOrdinaryStore** — test: Large-payload scenarios use authorised media custody rather than generic relational IU storage.
- `E01.05.04.F02` **MediaReferenceSurvivesRelocation** — test: A media reference remains resolvable when storage location changes.
- `E01.05.04.F03` **MediaAccessAuthorised** — test: Denied requesters cannot dereference restricted media.
- `E01.05.04.F04` **MediaIntegrityVerified** — test: Retrieved media matches the governed integrity evidence.
- `E01.05.04.F05` **BrokenReferenceDistinguished** — test: Missing media is reported distinctly from unauthorised access or transient failure.

**E01.05.05 — Information Coordinates & Addressability**  
Resolve governed references using identity, context and temporal coordinates without coupling callers to provider-native addresses.  
*Candidate Business demand:* `B10.01` Health Record Stewardship, `B10.07` Information Correction & Reconciliation, `B02.01` Clinical Context Establishment.  
*Source context:* `REF`, `IU`; *review findings:* R05, R09. **Standing: Proposed, not verified.**

- `E01.05.05.F01` **CoordinateResolutionDeterministic** — test: Identical stable coordinates select the same governed identity and version.
- `E01.05.05.F02` **TemporalReferenceUnambiguous** — test: Historic-state coordinates resolve to the intended point-in-time version.
- `E01.05.05.F03` **UnauthorisedCoordinatesProtected** — test: Reference enumeration does not disclose restricted subject characteristics.
- `E01.05.05.F04` **MovedIdentityPreserved** — test: Relocation or sharding does not change governed coordinates.
- `E01.05.05.F05` **MissingCoordinateExplained** — test: Invalid, ambiguous and absent reference outcomes remain distinguishable.

### E02 — Information Access, Discovery & Distribution
The system can make information available to appropriate consumers at the time and scope required.

#### E02.01 — Governed Information Access
**E02.01.04 — Authorised Version-Specific Retrieval**  
Allow a consumer to retrieve a precisely identified governed version with explicit authority and absence outcomes.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B10.03` Appropriate Information Access & Sharing, `B10.07` Information Correction & Reconciliation.  
*Source context:* `MN`, `IU`, `SEC`; *review findings:* R05, R09. **Standing: Proposed, not verified.**

- `E02.01.04.F01` **VersionSelectionCorrect** — test: Specified historical reference returns only the designated version.
- `E02.01.04.F02` **DenialNotAbsence** — test: Unauthorised retrieval cannot be mistaken for absence of information.
- `E02.01.04.F03` **ReadEvidenceAttributed** — test: Disclosures requiring audit are correlated with requesting authority and source.
- `E02.01.04.F04` **RepresentationMeaningPreserved** — test: Returned data retains sufficient model context for interpretation.
- `E02.01.04.F05` **StaleReadQualified** — test: Where currentness is not assured the response exposes its temporal/availability qualification.

**E02.01.05 — Purpose-Limited Disclosure**  
Make authorised information available only in the requested and permitted purpose, content and temporal scope.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B10.03` Appropriate Information Access & Sharing, `B10.04` Consent, Preference & Representation Stewardship, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `SEC`, `TH`, `IU`; *review findings:* R02, R22. **Standing: Proposed, not verified.**

- `E02.01.05.F01` **PurposeConstraintApplied** — test: Purpose-incompatible information is not disclosed.
- `E02.01.05.F02` **CompartmentFilterEnforced** — test: Compartment-restricted content does not escape filtered responses.
- `E02.01.05.F03` **DelegatedScopeValidated** — test: Representative permissions are checked against applicable current authority.
- `E02.01.05.F04` **NoUnauthorisedMetadataLeak** — test: Denied results expose no protected presence/identity hints.
- `E02.01.05.F05` **DisclosureDecisionAttributable** — test: Permit and deny decisions can be examined with effective authority context.

#### E02.02 — Information Discovery & Search
**E02.02.04 — Governed Search Completeness Qualification**  
Describe which authoritative search domain, versions and filters were actually evaluated.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B10.03` Appropriate Information Access & Sharing, `B13.05` Health Research Execution.  
*Source context:* `MN`, `IU`, `RESULT`; *review findings:* R09, R18. **Standing: Proposed, not verified.**

- `E02.02.04.F01` **SearchScopeDeclared** — test: Results identify source scope and temporal cut-off.
- `E02.02.04.F02` **SearchNotCacheOnly** — test: Complete authoritative searches are not satisfied from incomplete active-cache state.
- `E02.02.04.F03` **ResultVersionsPinned** — test: Persisted result sets retain version references used during the search.
- `E02.02.04.F04` **SearchDenialNotMissing** — test: Search restriction is distinguishable from no matching authoritative content.
- `E02.02.04.F05` **PartialSearchExposed** — test: Partial failures or inaccessible partitions are reported rather than implied complete.

**E02.02.05 — Cross-Domain Discovery & Correlation**  
Discover information across healthcare domains using governed identifiers while avoiding identity conflation.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B05.04` Longitudinal Care Coordination, `B13.05` Health Research Execution.  
*Source context:* `IU`, `REF`, `MODEL`; *review findings:* R09. **Standing: Proposed, not verified.**

- `E02.02.05.F01` **SubjectCorrelationQualified** — test: Cross-domain correlations report matching confidence and provenance.
- `E02.02.05.F02` **DifferentIdentityTypesPreserved** — test: Person, episode and IU identities remain distinguishable.
- `E02.02.05.F03` **DuplicateCandidatesExposed** — test: Ambiguous matches are visible rather than silently merged.
- `E02.02.05.F04` **TemporalCorrelationCorrect** — test: Cross-domain results retain applicable occurrence and recording times.
- `E02.02.05.F05` **UnauthorisedSourcesNotExposed** — test: Search federation does not disclose unauthorised source existence.

#### E02.03 — Distributed Information Availability
**E02.03.04 — Distributed Access Consistency**  
Preserve consumer-visible access and update semantics across concurrent nodes and partitions.  
*Candidate Business demand:* `B05.06` Community & Cross-Organisation Continuity, `B10.01` Health Record Stewardship, `B12.04` Enterprise Risk & Resilience.  
*Source context:* `DIST`, `RES`, `NFR`; *review findings:* R10, R21. **Standing: Proposed, not verified.**

- `E02.03.04.F01` **ConcurrentVersionSafety** — test: Concurrent updates obey declared version policy without silent lost changes.
- `E02.03.04.F02` **NodeFailureAccessContinuity** — test: Permitted reads remain available or degrade explicitly after one node loss.
- `E02.03.04.F03` **PartitionOutcomeExplicit** — test: Partition uncertainty never appears as unqualified authoritative success.
- `E02.03.04.F04` **ReplicaCurrencyQualified** — test: Observed replica freshness is exposed where material.
- `E02.03.04.F05` **RecoveryNoIdentityChange** — test: After recovery no governed reference changes identity or version unexpectedly.

#### E02.04 — Information Distribution & Notification
**E02.04.04 — Governed Subscription Matching**  
Select interested consumers based on governed information and event characteristics without making internal metadata a public API.  
*Candidate Business demand:* `B05.06` Community & Cross-Organisation Continuity, `B10.03` Appropriate Information Access & Sharing, `B13.01` Population Health Surveillance.  
*Source context:* `IU`, `REF`, `SEC`; *review findings:* R05, R18. **Standing: Proposed, not verified.**

- `E02.04.04.F01` **SubscriptionMatchCorrect** — test: Authorized subscribers receive events matching agreed criteria.
- `E02.04.04.F02` **NoRestrictedDelivery** — test: Subscriber filters never override local information disclosure requirements.
- `E02.04.04.F03` **ChangeNotificationCorrelation** — test: Notifications identify the changed governed unit and relevant version.
- `E02.04.04.F04` **WithdrawalStopsDelivery** — test: Revoked subscription or authority stops further protected delivery.
- `E02.04.04.F05` **DeliveryQualificationKnown** — test: Delayed, duplicate or failed notices are distinguishable where delivery is tracked.

**E02.04.05 — Selective Publication & Withdrawn Information**  
Make new, superseded and withdrawn information availability visible without modifying immutable historic versions.  
*Candidate Business demand:* `B05.04` Longitudinal Care Coordination, `B10.03` Appropriate Information Access & Sharing, `B10.07` Information Correction & Reconciliation.  
*Source context:* `IU`, `SEC`; *review findings:* R05, R18. **Standing: Proposed, not verified.**

- `E02.04.05.F01` **PublicationScopeApplied** — test: Only authorised target consumers see published material.
- `E02.04.05.F02` **SupersessionTraceable** — test: Receivers can locate the subsequent governed version.
- `E02.04.05.F03` **WithdrawalCommunicated** — test: Impacted consumers receive a qualified withdrawal notification where required.
- `E02.04.05.F04` **HistoricViewPreserved** — test: Withdrawal leaves required historical version references interpretable.
- `E02.04.05.F05` **DuplicateNoticeHandled** — test: Repeated notifications do not create conflicting authoritative change records.

#### E02.05 — Information Access Continuity
**E02.05.03 — Degraded Access & Restoration**  
Report and restore governed access following outage without promising unverified data freshness or availability.  
*Candidate Business demand:* `B05.06` Community & Cross-Organisation Continuity, `B12.04` Enterprise Risk & Resilience, `B10.01` Health Record Stewardship.  
*Source context:* `RES`, `DIST`, `OPS`; *review findings:* R10, R24. **Standing: Proposed, not verified.**

- `E02.05.03.F01` **AccessDegradationVisible** — test: Users and consumers can distinguish degraded and normal access modes.
- `E02.05.03.F02` **RestoredVersionVerified** — test: Recovered access returns an authoritative version after consistency checks.
- `E02.05.03.F03` **HIEAvailabilityProtected** — test: Accepted incoming clinical content remains retrievable after relevant failure.
- `E02.05.03.F04` **RecoveryTimeEvidence** — test: Recovery sequence and relevant timing are recorded for conformance assessment.
- `E02.05.03.F05` **NeverInventedEmptyResult** — test: Temporary inaccessibility is not reported as no record found.

### E03 — Digital Interoperability & Service Interaction
The system can communicate with external/internal services using understandable, governed contracts.

#### E03.01 — External Information Exchange
**E03.01.04 — External Acceptance versus Receipt**  
Separate transport-level receipt, validation, governed acceptance and clinical responsibility acceptance.  
*Candidate Business demand:* `B04.03` Diagnostic Findings Reporting, `B05.03` Care Responsibility Transfer, `B10.01` Health Record Stewardship.  
*Source context:* `EXCH`, `MN`, `NFR`; *review findings:* R16. **Standing: Proposed, not verified.**

- `E03.01.04.F01` **TransportReceiptClassified** — test: Transport receipt is never presented as authoritative durable acceptance.
- `E03.01.04.F02` **ValidationRejectionExplicit** — test: Invalid incoming content has a distinct actionable rejection result.
- `E03.01.04.F03` **DurableAcceptanceCorrelated** — test: Positive authoritative acceptance corresponds to retrieved governed content.
- `E03.01.04.F04` **DuplicateSubmissionHandled** — test: Repeat submissions preserve idempotency or explicit duplicate meaning.
- `E03.01.04.F05` **ClinicalResponsibilityNotInferred** — test: Technical exchange responses do not assert clinical handover acceptance.

#### E03.02 — Service Contract Exposure & Consumption
**E03.02.04 — Consumer-Specific Contract Negotiation**  
Expose stable service contracts and their compatible versions without binding consumers to implementation topology.  
*Candidate Business demand:* `B07.02` Service Provider & Network Participation, `B10.05` Health Knowledge Stewardship, `B12.05` Health Service Readiness & Mobilisation.  
*Source context:* `MODEL`, `EXCH`, `NFR`; *review findings:* R13. **Standing: Proposed, not verified.**

- `E03.02.04.F01` **ContractVersionDeclared** — test: Consumer sees the agreed contract and version.
- `E03.02.04.F02` **UnsupportedContractRejected** — test: Unsupported protocol/version is rejected with a bounded explanation.
- `E03.02.04.F03` **ContractChangeTraceable** — test: A contract change has a traceable publication and compatibility history.
- `E03.02.04.F04` **ImplementationTopologyHidden** — test: Client-facing contract is unchanged by internal provider replacement.
- `E03.02.04.F05` **ContractAuthnRequired** — test: Protected contracts cannot be invoked through anonymous fallback.

**E03.02.05 — Service Invocation Evidence & Accountability**  
Correlate service calls, consumers and outcomes without treating invocation as completed business work.  
*Candidate Business demand:* `B05.03` Care Responsibility Transfer, `B10.02` Information Completeness & Provenance Accountability, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `AUD`, `NFR`, `TH`; *review findings:* R16, R18. **Standing: Proposed, not verified.**

- `E03.02.05.F01` **InvocationIdentityCorrelated** — test: Service interaction references requester and execution context.
- `E03.02.05.F02` **OutcomeDistinctFromRequest** — test: Accepted invocation is not reported as completed processing.
- `E03.02.05.F03` **CrossBoundaryTraceCoherent** — test: Distributed spans can be connected without logging protected content by default.
- `E03.02.05.F04` **AuthorisationEnforced** — test: Contract consumption revalidates applicable effective authority.
- `E03.02.05.F05` **AuditableRejectionRecorded** — test: Mandatory denied or rejected attempts contribute the required audit evidence.

#### E03.03 — Protocol & Format Mediation
**E03.03.04 — Standards-Native Content Preservation**  
Retain incoming native content and the governed representation relationship when standards require preservation.  
*Candidate Business demand:* `B10.01` Health Record Stewardship, `B04.03` Diagnostic Findings Reporting, `B13.05` Health Research Execution.  
*Source context:* `IU`, `EXCH`, `MODEL`; *review findings:* R09, R13. **Standing: Proposed, not verified.**

- `E03.03.04.F01` **NativePayloadRetained** — test: Original standard-native representation is retrievable when governed preservation applies.
- `E03.03.04.F02` **DerivedMappingLinked** — test: Any mapped Information Unit refers to its native source provenance.
- `E03.03.04.F03` **UnknownFieldsReported** — test: Unmapped or unsupported attributes receive explicit handling qualifications.
- `E03.03.04.F04` **RoundTripLossMeasured** — test: Known semantic loss is detected and reported in controlled round trips.
- `E03.03.04.F05` **NoFakeStandardsConformance** — test: Conformance reporting distinguishes native compliance and local mapping acceptance.

**E03.03.05 — Protocol Boundary Isolation**  
Adapt network and message protocols without exposing transport implementation as a business-domain service contract.  
*Candidate Business demand:* `B07.02` Service Provider & Network Participation, `B05.06` Community & Cross-Organisation Continuity.  
*Source context:* `NFR`, `EXCH`; *review findings:* R16. **Standing: Proposed, not verified.**

- `E03.03.05.F01` **ConfigurableBoundaryTimeouts** — test: MLLP and equivalent boundary timeout policies are tested.
- `E03.03.05.F02` **SlowPeerIsolation** — test: One slow or malformed peer does not block unrelated exchanges.
- `E03.03.05.F03` **AcknowledgementModeConformance** — test: Configured protocol acknowledgement modes match the applicable standard.
- `E03.03.05.F04` **BoundaryErrorAttribution** — test: A transport fault can be correlated to the affected interaction.
- `E03.03.05.F05` **MalformedPayloadContained** — test: Failed format handling does not create authoritative clinical information.

#### E03.04 — Interaction Acknowledgement & Error Handling
**E03.04.04 — End-to-End Exchange State Reconciliation**  
Make uncertain exchange results discoverable after retries, lost acknowledgements or boundary failure.  
*Candidate Business demand:* `B04.03` Diagnostic Findings Reporting, `B05.02` Referral & Transfer Coordination, `B10.01` Health Record Stewardship.  
*Source context:* `EXCH`, `MN`, `PERSIST`; *review findings:* R16, R06. **Standing: Proposed, not verified.**

- `E03.04.04.F01` **UnknownDispositionQuery** — test: A sender can obtain or reconcile an initially unknown interaction outcome.
- `E03.04.04.F02` **RetrySafeCorrelation** — test: Replays do not create unexplained duplicate authoritative content.
- `E03.04.04.F03` **ResponseCommitConsistency** — test: Positive business acceptance has durable downstream evidence where promised.
- `E03.04.04.F04` **FailureStageIdentifiable** — test: Failure can be attributed to receipt, validation, acceptance or publication stage.
- `E03.04.04.F05` **OutOfOrderReplyHandled** — test: Late acknowledgement does not override a newer authoritative result.

#### E03.05 — Exchange Compatibility & Conformance
**E03.05.04 — Conformance Evidence for External Interfaces**  
Demonstrate that external interfaces honour authoritative standards, declared deviations and compatible releases.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B11.05` Regulatory Conformance & Accreditation, `B07.02` Service Provider & Network Participation.  
*Source context:* `MODEL`, `NFR`, `EXCH`; *review findings:* R13. **Standing: Proposed, not verified.**

- `E03.05.04.F01` **KnownDeviationRecorded** — test: Nonconforming external input has a retained deviation record.
- `E03.05.04.F02` **ConformanceSuiteRepeatable** — test: A release can rerun a bounded interface conformance suite.
- `E03.05.04.F03` **ExternalContractPrecedence** — test: Authoritative external contract is respected over internal preference.
- `E03.05.04.F04` **VersionCompatibilityReported** — test: Supported and unsupported contract versions are explicit.
- `E03.05.04.F05` **FailureEvidenceReproducible** — test: A rejected exchange can be recreated using protected representative evidence.

### E04 — Semantic Meaning & Model Governance
The system can preserve, interpret, transform and evolve the meaning of managed information.

#### E04.01 — Information Model Definition & Resolution
**E04.01.03 — Ontology-Tied Model Resolution**  
Resolve governed information models under the ontology and version applicable when the data was created or used.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B13.06` Evidence Translation & Practice Improvement.  
*Source context:* `MODEL`, `IU`, `REF`; *review findings:* R13, R09. **Standing: Proposed, not verified.**

- `E04.01.03.F01` **OntologyVersionPinned** — test: Model resolution includes associated ontology release identity.
- `E04.01.03.F02` **HistoricalModelAvailable** — test: A historic Information Unit remains interpretable after model release.
- `E04.01.03.F03` **UnknownDefinitionQualified** — test: Unknown Topic or class version is not silently replaced by latest.
- `E04.01.03.F04` **IndependentReleaseCorrelated** — test: Dependent component acceptance identifies the exact published model release.
- `E04.01.03.F05` **DefinitionIntegrityVerifiable** — test: Resolved model metadata matches its governed release evidence.

**E04.01.04 — Governed Topic Identity & Namespace**  
Preserve meaning and canonical identity of Topics and named references across authoring, distribution and runtime.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B10.07` Information Correction & Reconciliation.  
*Source context:* `REF`, `MODEL`; *review findings:* R05, R09. **Standing: Proposed, not verified.**

- `E04.01.04.F01` **TopicIdentityStable** — test: Accepted Topic identities survive rename and reclassification.
- `E04.01.04.F02` **CanonicalNameDerived** — test: Canonical names reflect governed Composition ancestry where defined.
- `E04.01.04.F03` **AliasAmbiguityExposed** — test: Conflicting aliases cannot silently select an incorrect Topic.
- `E04.01.04.F04` **RuntimeDefinitionImmutable** — test: Accepted runtime Topic definitions cannot mutate without release.
- `E04.01.04.F05` **HierarchyMatchingConformant** — test: Containment matching uses the governed scope and Topic definition.

#### E04.02 — Terminology & Value Set Management
**E04.02.04 — Terminology Publication Stability**  
Prevent unreviewed dynamic terminology changes from altering operational meaning within an accepted release.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B04.04` Diagnostic Quality & Safety Assurance, `B13.06` Evidence Translation & Practice Improvement.  
*Source context:* `MODEL`, `NFR`; *review findings:* R13. **Standing: Proposed, not verified.**

- `E04.02.04.F01` **ValueSetVersionStable** — test: Repeated lookup within a release yields the same value-set definition.
- `E04.02.04.F02` **UnapprovedUpdateBlocked** — test: Runtime terminology changes bypassing release governance are rejected.
- `E04.02.04.F03` **DeprecatedTermQualified** — test: Deprecated concepts remain distinguishable from currently approved concepts.
- `E04.02.04.F04` **TerminologyRollbackTraceable** — test: A rollback retains release and validation evidence.
- `E04.02.04.F05` **DependentRegressionsMeasured** — test: Terminology changes trigger bounded conformance regression suites.

#### E04.03 — Semantic Transformation
**E04.03.03 — Loss-Aware Semantic Transformation**  
Transform representations while exposing provenance, semantic degradation and incompatibility.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B04.03` Diagnostic Findings Reporting, `B10.05` Health Knowledge Stewardship.  
*Source context:* `MODEL`, `IU`, `EXCH`; *review findings:* R09, R13. **Standing: Proposed, not verified.**

- `E04.03.03.F01` **UnmappedConceptsReported** — test: No unrepresentable source semantics vanish without qualification.
- `E04.03.03.F02` **TransformationVersionPinned** — test: Mapping rules and versions are recoverable for every governed derivative.
- `E04.03.03.F03` **MeaningEquivalentWhereClaimed** — test: Equivalence claims pass reference examples and adverse tests.
- `E04.03.03.F04` **OriginalRepresentationRetrievable** — test: Original preserved information remains referenceable.
- `E04.03.03.F05` **TransformationProvenanceLinked** — test: Output IU is linked to source IU and transform evidence.

#### E04.04 — Semantic Validation
**E04.04.04 — Context-Dependent Rule Evaluation**  
Evaluate model, terminology and contextual invariants without confusing semantic validity with clinical judgement.  
*Candidate Business demand:* `B04.04` Diagnostic Quality & Safety Assurance, `B02.04` Diagnostic & Clinical Reasoning, `B10.07` Information Correction & Reconciliation.  
*Source context:* `MODEL`, `REF`, `SEC`; *review findings:* R09, R13. **Standing: Proposed, not verified.**

- `E04.04.04.F01` **ContextRulesVersioned** — test: Rule results cite the applicable semantic release.
- `E04.04.04.F02` **UnknownContextReported** — test: Missing context yields an indeterminate or incomplete result rather than false validation.
- `E04.04.04.F03` **RuleConflictDetected** — test: Contradictory constraints produce an attributable finding.
- `E04.04.04.F04` **DataNotClinicalDecision** — test: A semantically valid result does not assert clinical correctness.
- `E04.04.04.F05` **ValidationEvidenceRepeatable** — test: Running the same rules against an unchanged snapshot repeats the outcome.

#### E04.05 — Model Change & Release Governance
**E04.05.04 — Controlled Semantic Change Impact**  
Determine whether revised definitions affect stored meaning, clients, transformations or accepted behaviour.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B11.05` Regulatory Conformance & Accreditation, `B13.06` Evidence Translation & Practice Improvement.  
*Source context:* `MODEL`, `IU`, `NFR`; *review findings:* R13. **Standing: Proposed, not verified.**

- `E04.05.04.F01` **DependencyImpactEnumerated** — test: Affected contracts, topics and transformations are identified before release.
- `E04.05.04.F02` **MigrationNeedExplicit** — test: Model changes requiring data migration are distinguished from compatible extensions.
- `E04.05.04.F03` **RegressionEvidenceLinked** — test: Accepted release records associate test evidence and exceptions.
- `E04.05.04.F04` **RollbackCompatibilityKnown** — test: Rollback viability and information state risk are classified.
- `E04.05.04.F05` **IndependentReleaseGateEnforced** — test: Dependent release activation is gated where architecturally required.

**E04.05.05 — Reference Dataset Release & Reproducibility**  
Provide repeatable access to versioned datasets and terminology references needed for conformance and clinical information.  
*Candidate Business demand:* `B10.05` Health Knowledge Stewardship, `B13.05` Health Research Execution, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `MODEL`, `NFR`; *review findings:* R13, R23. **Standing: Proposed, not verified.**

- `E04.05.05.F01` **DatasetSnapshotReproducible** — test: A named dataset version yields identical governed content on repeat.
- `E04.05.05.F02` **LatestVersusPinnedExplicit** — test: Consumers can distinguish latest from snapshot-selection semantics.
- `E04.05.05.F03` **ReferenceSourceAttributable** — test: Published reference data retains source and acceptance provenance.
- `E04.05.05.F04` **UnauthorisedRefreshBlocked** — test: Changes without release approval do not alter accepted dataset content.
- `E04.05.05.F05` **RegressionSetAvailable** — test: A prior release can be tested against its associated reference dataset.

### E05 — Identity, Trust & Information Protection
The system can establish, propagate and enforce appropriate digital trust and access boundaries.

#### E05.01 — Digital Identity & Authentication
**E05.01.04 — Machine-to-Machine Trust Continuity**  
Authenticate services and workloads without conflating workload identity with human business authority.  
*Candidate Business demand:* `B07.02` Service Provider & Network Participation, `B12.04` Enterprise Risk & Resilience, `B10.03` Appropriate Information Access & Sharing.  
*Source context:* `TH`, `NFR`; *review findings:* R02, R22. **Standing: Proposed, not verified.**

- `E05.01.04.F01` **WorkloadIdentityBound** — test: Calls carry verifiable workload identity independent of user identity.
- `E05.01.04.F02` **DelegatedAuthorityExplicit** — test: Machine identity alone does not imply human purpose-scoped permission.
- `E05.01.04.F03` **CredentialRotationContinuity** — test: Credential rollover does not require ungoverned anonymous fallback.
- `E05.01.04.F04` **ExpiredTrustRejected** — test: Old or revoked technical identities fail protected interactions.
- `E05.01.04.F05` **TrustEventAccountable** — test: Trust failures carry suitable diagnostic/audit correlation.

#### E05.02 — Contextual Authorisation Determination
**E05.02.03 — Subject-Specific Required Criteria**  
Evaluate protected-subject requirements separately from the effective authority presented by the requester.  
*Candidate Business demand:* `B10.03` Appropriate Information Access & Sharing, `B10.04` Consent, Preference & Representation Stewardship, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `IU`, `REF`; *review findings:* R22. **Standing: Proposed, not verified.**

- `E05.02.03.F01` **CriteriaProviderEncapsulated** — test: Evaluation does not require consumers to parse protected-object internals.
- `E05.02.03.F02` **ActionMaskIndependent** — test: Action limitations are applied independently of compartment restrictions.
- `E05.02.03.F03` **CompartmentMaskCorrect** — test: Classification/compartment conditions alter outcome as defined.
- `E05.02.03.F04` **TimeContextApplied** — test: Temporal and contextual changes can revoke permission to an unchanged subject.
- `E05.02.03.F05` **DeniedCriteriaExplainedSafely** — test: Rejection exposes safe reason classes without protected criteria leakage.

#### E05.03 — Local Access Policy Enforcement
**E05.03.04 — Local PEP Decision Integrity**  
Require independently enforced authorisation at protected service, information and workflow boundaries.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B10.03` Appropriate Information Access & Sharing, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `SEC`; *review findings:* R02, R22. **Standing: Proposed, not verified.**

- `E05.03.04.F01` **EveryProtectedBoundaryChecks** — test: Selected protected boundaries demonstrably invoke local enforcement.
- `E05.03.04.F02` **InheritedScopeHonoured** — test: Contained-scope inheritance never exceeds approved bounds.
- `E05.03.04.F03` **ForwardScopeConstrained** — test: Forwarded authority remains limited by applicable originating criteria.
- `E05.03.04.F04` **FailureDefaultsDeny** — test: Evaluation failure or missing context cannot produce permit.
- `E05.03.04.F05` **PEPDecisionEvidence** — test: Permit/deny result is attributable without disclosing credentials or unnecessary PHI.

#### E05.04 — Security Context & Passport Propagation
**E05.04.03 — Operational Passport Currency**  
Make current effective authority available to distributed consumers while preventing use of invalid or expired manifestations.  
*Candidate Business demand:* `B10.03` Appropriate Information Access & Sharing, `B10.04` Consent, Preference & Representation Stewardship, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `SEC`, `MN`; *review findings:* R01, R02, R22. **Standing: Proposed, not verified.**

- `E05.04.03.F01` **ExpiredPassportDenied** — test: An expired Passport never authorises a protected action.
- `E05.04.03.F02` **InvalidationPropagated** — test: Revocation reaches subscribed enforcement points under defined recovery tests.
- `E05.04.03.F03` **DisconnectedConsumerFailsSafe** — test: Loss of policy currency does not create unchecked authority.
- `E05.04.03.F04` **OperationalCopyDistinguished** — test: Operational Passport storage is not mistaken for authoritative policy state.
- `E05.04.03.F05` **PassportFreshnessAssessable** — test: Consumers can assess currency against governing policy epoch or equivalent contract.

#### E05.05 — Information Protection & Security Evidence
**E05.05.05 — Security Label & Isolation Assurance**  
Preserve information classifications and isolation constraints across storage, processing, references and distributed exchange.  
*Candidate Business demand:* `B10.03` Appropriate Information Access & Sharing, `B10.06` Retention, Disclosure & Record Accountability, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `IU`, `SEC`, `TH`; *review findings:* R22, R05. **Standing: Proposed, not verified.**

- `E05.05.05.F01` **LabelPropagationComplete** — test: Governed information derivatives retain applicable security labels.
- `E05.05.05.F02` **IsolationCrossingBlocked** — test: Incompatible isolation contexts cannot exchange protected data.
- `E05.05.05.F03` **NoBypassViaSearch** — test: Search metadata cannot reveal restricted existence or content.
- `E05.05.05.F04` **ReclassificationAccountable** — test: Changes in classification produce attributable governed events.
- `E05.05.05.F05` **RecoveryPreservesProtection** — test: Restored data retains original enforceable security constraints.

**E05.05.06 — Protected Release Integrity**  
Withhold sensitive information products unless the entire requested content set is authorised and safely releasable.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.05` Regulatory Conformance & Accreditation, `B13.04` Research Governance & Participant Protection.  
*Source context:* `PROV`, `TH`, `SEC`; *review findings:* R04, R23. **Standing: Proposed, not verified.**

- `E05.05.06.F01` **CompleteSetAuthorisation** — test: Each requested protected item passes release-time checks.
- `E05.05.06.F02` **NoPartialReleaseOnDenial** — test: Any denied required item prevents publication of the assembled set.
- `E05.05.06.F03` **StagedInformationNotExposed** — test: In-progress assembly cannot be accessed through ungoverned paths.
- `E05.05.06.F04` **ReleaseDecisionRecorded** — test: Published and rejected outcomes retain attributable decision evidence.
- `E05.05.06.F05` **AuthorityChangeRechecked** — test: Changes between assembly and release cannot silently leave stale permission.

#### E05.06 — Authorisation Policy & Effective Authority Management
**E05.06.01 — Authoritative Policy Definition**  
Maintain a versioned, reviewable policy model for protected subjects and information use.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B10.03` Appropriate Information Access & Sharing, `B10.04` Consent, Preference & Representation Stewardship, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `SEC`; *review findings:* R01, R22. **Standing: Proposed, not verified.**

- `E05.06.01.F01` **PolicyVersionAuthoritative** — test: Every effective policy selection refers to an approved authoritative version.
- `E05.06.01.F02` **PolicyChangeAccountable** — test: Authorised policy changes record actor, reason and effective point.
- `E05.06.01.F03` **InvalidPolicyRejected** — test: Contradictory or malformed conditions cannot be silently activated.
- `E05.06.01.F04` **PolicyScopeDiscriminated** — test: Subject, compartment, action and contextual limits remain independently evaluable.
- `E05.06.01.F05` **OldPolicyRecoverable** — test: A prior policy definition remains available for historical decision assessment.

**E05.06.02 — Effective Passport Construction**  
Construct and preserve an effective authority representation when policy or identity circumstances change, independently of routine login.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B10.04` Consent, Preference & Representation Stewardship, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `REF`; *review findings:* R01, R02. **Standing: Proposed, not verified.**

- `E05.06.02.F01` **PassportChangeDriven** — test: Relevant authoritative changes cause affected Passport recalculation.
- `E05.06.02.F02` **LoginNoForcedCompilation** — test: Ordinary valid login retrieves suitable preconstructed effective authority.
- `E05.06.02.F03` **PassportSourceTraceable** — test: Constructed authority references input policy and identity versions.
- `E05.06.02.F04` **PassportCriteriaComplete** — test: RequiredCriteria dimensions are evaluated for supported protected subjects.
- `E05.06.02.F05` **PassportBuildFailureSafe** — test: Construction failure cannot create an over-permissive operational representation.

**E05.06.03 — Policy Change Impact & Revalidation**  
Identify affected authority grants and revalidate operational Passports after a governing change.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B10.03` Appropriate Information Access & Sharing, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `SEC`; *review findings:* R01, R02. **Standing: Proposed, not verified.**

- `E05.06.03.F01` **AffectedPassportSetFound** — test: Change impact identifies all affected effective authorisations in test population.
- `E05.06.03.F02` **RevocationBeforeContinuedGrant** — test: Stale effective authority cannot grant access after the governing change.
- `E05.06.03.F03` **RevalidationOutcomeVisible** — test: Completed, failed and pending revalidations have distinct statuses.
- `E05.06.03.F04` **RecoveryOfDistributionGap** — test: Disconnected consumers reconcile effective authority on rejoin.
- `E05.06.03.F05` **NoUncontrolledPrivilegeExpansion** — test: Revalidation never broadens authority without a valid governing change.

**E05.06.04 — Policy Administration & Evidence**  
Enable authorised policy maintenance, review and accountable release without bypassing policy authority.  
*Candidate Business demand:* `B10.04` Consent, Preference & Representation Stewardship, `B11.05` Regulatory Conformance & Accreditation, `B12.03` Enterprise Performance & Accountability.  
*Source context:* `TH`, `AUD`; *review findings:* R01, R22. **Standing: Proposed, not verified.**

- `E05.06.04.F01` **AdministrativeActorVerified** — test: Every change action is attributable to authorised administration context.
- `E05.06.04.F02` **SeparationOfAuthorityApplied** — test: Administrative access does not imply authority to bypass policy acceptance.
- `E05.06.04.F03` **PolicyReleaseReviewed** — test: New policy state has required approval and release evidence.
- `E05.06.04.F04` **ChangeImpactExplained** — test: Affected subjects and permitted actions are identified before activation.
- `E05.06.04.F05` **RejectedAdminChangeRecorded** — test: Mandatory rejected attempts produce audit evidence without sensitive leakage.

### E06 — Digital Work, Events & Temporal Coordination
The system can carry forward and account for work, events and time-dependent execution.

#### E06.01 — Task Representation & State Management
**E06.01.04 — Task Obligation & Outcome Semantics**  
Represent actionable task, fulfillment, reporting and outcome relationships without using transport status as task completion.  
*Candidate Business demand:* `B05.01` Multidisciplinary Care Coordination, `B07.04` Service Delivery Coordination, `B11.02` Incident Reporting & Learning.  
*Source context:* `PRAGMA`, `TASK`; *review findings:* R07, R18. **Standing: Proposed, not verified.**

- `E06.01.04.F01` **TaskTypeDistinguished** — test: Actionable, fulfillment and reported task representations remain distinguishable.
- `E06.01.04.F02` **OutcomeModeApplied** — test: Aggregate/collection/derived outcomes follow the designated contract.
- `E06.01.04.F03` **CompletionPolicyApplied** — test: FirstToFinish, AssignedToFinish and Aggregate behave as specified.
- `E06.01.04.F04` **TaskIdentityPreserved** — test: Task lineage survives restart and repartitioning.
- `E06.01.04.F05` **UndeliveredNotComplete** — test: Queued or accepted transport never alone indicates task fulfillment.

#### E06.02 — Workflow & Work Progression
**E06.02.04 — Non-Digital-Twin Workflow Progression**  
Coordinate and observe non-Twin workflow obligations without equating them to individual work execution or parcel routing.  
*Candidate Business demand:* `B05.01` Multidisciplinary Care Coordination, `B07.04` Service Delivery Coordination, `B11.02` Incident Reporting & Learning.  
*Source context:* `TASK`, `WORK`, `PRAGMA`; *review findings:* R08. **Standing: Proposed, not verified.**

- `E06.02.04.F01` **WorkflowStepOrderPreserved** — test: Required predecessor and successor constraints are observed.
- `E06.02.04.F02` **NonTwinWorkRecognised** — test: Non-Twin workflow can be expressed without fake Twin identity.
- `E06.02.04.F03` **FailureStatePreserved** — test: Unfinished workflow state remains inspectable across execution loss.
- `E06.02.04.F04` **HumanActionGateRespected** — test: Required clinical or administrative human decision cannot be silently automated.
- `E06.02.04.F05` **WorkflowCompletionEvidence** — test: The declared completion condition is evidenced independently of transport receipt.

**E06.02.05 — Digital Twin Workflow Coordination**  
Coordinate Twin-scoped work while preserving Twin-local ownership of execution and ordered activity.  
*Candidate Business demand:* `B03.03` Clinical Observation & Response, `B05.01` Multidisciplinary Care Coordination, `B07.04` Service Delivery Coordination.  
*Source context:* `TASK`, `PRAGMA`, `WORK`; *review findings:* R08. **Standing: Proposed, not verified.**

- `E06.02.05.F01` **TwinQueueIsolation** — test: One Twin cannot consume another Twin work queue in conflict with governing scope.
- `E06.02.05.F02` **TwinOrderMaintained** — test: Ordered Twin actions obey selected sequencing policy.
- `E06.02.05.F03` **TwinFailureVisible** — test: Unavailable Twin state is not reported as finished work.
- `E06.02.05.F04` **TwinWorkRecoverable** — test: Accepted obligations can be reconstructed after Twin-host failure.
- `E06.02.05.F05` **CrossTwinCorrelation** — test: Multi-Twin interactions remain correlated without falsely merging their identities.

#### E06.03 — Event & Parcel Routing
**E06.03.03 — Exclusive Parcel Routing Decision**  
Determine a governed PetasosParcel route without taking ownership of queue delivery, work execution or payload interpretation.  
*Candidate Business demand:* `B07.04` Service Delivery Coordination, `B05.06` Community & Cross-Organisation Continuity, `B12.04` Enterprise Risk & Resilience.  
*Source context:* `PARCEL`, `PRAGMA`; *review findings:* R08. **Standing: Proposed, not verified.**

- `E06.03.03.F01` **SingleRoutingAuthority** — test: Each parcel destination decision has one responsible routing authority.
- `E06.03.03.F02` **RouteDecisionTraceable** — test: Route rules and destination choices are attributable.
- `E06.03.03.F03` **UnavailableDestinationHandled** — test: No valid route produces an explicit exception, not disappearance.
- `E06.03.03.F04` **RoutingNoPayloadMutation** — test: Routing does not silently transform the parcel state or governed payload meaning.
- `E06.03.03.F05` **NoDeliveryOwnershipLeak** — test: Routing decision is not represented as evidence of successful delivery.

**E06.03.04 — Parcel State & Metadata Preservation**  
Preserve by-value parcel envelope semantics and required metadata while routing through distributed components.  
*Candidate Business demand:* `B07.04` Service Delivery Coordination, `B10.02` Information Completeness & Provenance Accountability.  
*Source context:* `PARCEL`, `PRAGMA`, `IU`; *review findings:* R07, R08. **Standing: Proposed, not verified.**

- `E06.03.04.F01` **EnvelopeValueIntegrity** — test: Parcel value and metadata are preserved through the route.
- `E06.03.04.F02` **ParcelScopeInterComponent** — test: Cross-component route follows declared parcel-domain scope.
- `E06.03.04.F03` **CorrelationIdPersistent** — test: Retry and reroute retain identity/correlation information.
- `E06.03.04.F04` **SecurityContextCarriesSafely** — test: Passport snapshot constraints propagate without credentials.
- `E06.03.04.F05` **ParcelNotInformationUnit** — test: Parcel transfer state cannot redefine durable IU identity/version state.

#### E06.04 — Delivery Reliability & Recovery
**E06.04.04 — Backpressure & Capacity Admission**  
Maintain accepted work obligations while responding safely to overload and delivery congestion.  
*Candidate Business demand:* `B07.03` Demand, Capacity & Scheduling, `B12.04` Enterprise Risk & Resilience, `B07.04` Service Delivery Coordination.  
*Source context:* `PARCEL`, `NFR`, `RES-W`; *review findings:* R21. **Standing: Proposed, not verified.**

- `E06.04.04.F01` **OverCapacityExplicitRejection** — test: Refused new work receives a clear capacity disposition before acceptance.
- `E06.04.04.F02` **AcceptedQueueNotDropped** — test: Accepted parcels remain traceable when downstream consumers stall.
- `E06.04.04.F03` **BackpressureSignalVisible** — test: Upstream capacity signals are measurable and interpretable.
- `E06.04.04.F04` **RetryLoadBounded** — test: Retries obey configured limits without amplifying an outage uncontrolled.
- `E06.04.04.F05` **ServiceRecoveryDrainsQueue** — test: Recovered receivers process preserved accepted work according to policy.

#### E06.05 — Temporal Execution & Scheduling
**E06.05.04 — Central Time & Scheduling Authority**  
Provide a consistent scheduling/calendar context and temporal evaluation for distributed work without component-local competing calendars.  
*Candidate Business demand:* `B07.03` Demand, Capacity & Scheduling, `B05.05` Follow-Up & Ongoing Care Arrangement, `B12.05` Health Service Readiness & Mobilisation.  
*Source context:* `NFR`, `TIME`, `PRAGMA`; *review findings:* R17. **Standing: Proposed, not verified.**

- `E06.05.04.F01` **SingleSchedulingAuthority** — test: Scheduled execution derives from the governed scheduler responsibility.
- `E06.05.04.F02` **TimezoneBoundaryCorrect** — test: Cross-timezone transitions produce correct due instants.
- `E06.05.04.F03` **LateExecutionQualified** — test: Delayed work retains its original scheduled and actual execution times.
- `E06.05.04.F04` **ClockSkewDetected** — test: Material component clock differences are reported as temporal conditions.
- `E06.05.04.F05` **CalendarUpdateAccountable** — test: Changes in configured schedule/calendar carry release or governance evidence.

#### E06.06 — Work Context & Checkpoint Preservation
**E06.06.04 — Safe Work Reconstruction Boundary**  
Reconstruct missing active working context from durable references and accepted progress, never resuming in a failed Ergon.  
*Candidate Business demand:* `B07.04` Service Delivery Coordination, `B12.04` Enterprise Risk & Resilience, `B10.01` Health Record Stewardship.  
*Source context:* `RES-W`, `WORK`, `PRAGMATON`; *review findings:* R07, R24. **Standing: Proposed, not verified.**

- `E06.06.04.F01` **NoMidErgonResume** — test: Post-failure execution begins at a governed restart boundary.
- `E06.06.04.F02` **ContextRehydratedFromAuthority** — test: Reconstruction uses authoritative versions and durable transitions.
- `E06.06.04.F03` **LostCacheNotLostWork** — test: Active cache failure does not erase accepted obligations.
- `E06.06.04.F04` **RecoveredInputsVersioned** — test: Reconstituted input references identify their intended versions.
- `E06.06.04.F05` **RestartExceptionVisible** — test: Unreconstructable context is reported with correlated failure evidence.

**E06.06.05 — Pragmaton Context Integrity**  
Establish and maintain bounded working information without allowing active state to masquerade as authoritative information.  
*Candidate Business demand:* `B07.04` Service Delivery Coordination, `B10.01` Health Record Stewardship.  
*Source context:* `PRAGMATON`, `WORK`, `MN`; *review findings:* R06, R07. **Standing: Proposed, not verified.**

- `E06.06.05.F01` **ContextScopeBounded** — test: An Ergon receives only its expected and explicitly authorised ad-hoc working context.
- `E06.06.05.F02` **WorkingStateNotAuthoritative** — test: Intermediate mutation cannot be mistaken for durable version progression.
- `E06.06.05.F03` **OutputNewVersusChanged** — test: Genuinely new outputs remain distinct from changes to loaded inputs.
- `E06.06.05.F04` **ContextAuthorityPreserved** — test: Requester and executor authority and information labels survive contextualisation.
- `E06.06.05.F05` **ContextReleaseAccountable** — test: Required context disposal does not silently discard accepted authoritative information.

#### E06.07 — Work Acceptance & Outcome Reconciliation
**E06.07.01 — Durable Work Acceptance**  
Accept externally or internally submitted work with a durable obligation independent of Information Unit commitment.  
*Candidate Business demand:* `B07.04` Service Delivery Coordination, `B11.02` Incident Reporting & Learning, `B12.04` Enterprise Risk & Resilience.  
*Source context:* `PARCEL`, `PRAGMA`, `NFR`; *review findings:* R06, R07, R21. **Standing: Proposed, not verified.**

- `E06.07.01.F01` **AcceptedWorkRecordedBeforeAck** — test: An acceptance acknowledgment corresponds to a recoverable work obligation.
- `E06.07.01.F02` **RejectedWorkNotQueued** — test: Capacity or authority rejection leaves no silently accepted obligation.
- `E06.07.01.F03` **DuplicateWorkCorrelated** — test: Retries correlate to the original accepted work identity.
- `E06.07.01.F04` **WorkAcceptanceNotIUCommit** — test: Acceptance status does not assert authoritative information version progression.
- `E06.07.01.F05` **PendingWorkSurvivesRestart** — test: Accepted but unfulfilled work reappears after planned or unplanned restart.

**E06.07.02 — Execution Outcome Reconciliation**  
Reconcile produced work outcomes with accepted work obligations and subsequent authoritative information commitment.  
*Candidate Business demand:* `B05.03` Care Responsibility Transfer, `B07.04` Service Delivery Coordination, `B11.02` Incident Reporting & Learning.  
*Source context:* `PRAGMA`, `MN`, `RES-W`; *review findings:* R06, R07. **Standing: Proposed, not verified.**

- `E06.07.02.F01` **ExecutionResultNotCommit** — test: Completed Ergon outcome remains distinct from durable information acceptance.
- `E06.07.02.F02` **OutcomeLineageComplete** — test: Task, input, execution and output correlation survives recovery.
- `E06.07.02.F03` **UnknownCommitReconcilable** — test: Indeterminate write can be resolved without reexecuting clinical business logic blindly.
- `E06.07.02.F04` **FailureBetweenStagesVisible** — test: A failed commit after successful processing is visible as incomplete disposition.
- `E06.07.02.F05` **NoDuplicateOutcomePromotion** — test: Replay does not create multiple accepted outcomes for a single governed obligation.

**E06.07.03 — Staged Change & Commit Coordination**  
Support Retrieve, Check, Update, ProcessSomething and Commit as distinct service intentions with independently assessable outcomes.  
*Candidate Business demand:* `B10.07` Information Correction & Reconciliation, `B05.03` Care Responsibility Transfer, `B11.03` Clinical Governance & Practice Assurance.  
*Source context:* `MN`, `WORK`, `PERSIST`; *review findings:* R06, R07. **Standing: Proposed, not verified.**

- `E06.07.03.F01` **RetrievalVersionKnown** — test: Work begins with a known governed source version where needed.
- `E06.07.03.F02` **CheckDoesNotCommit** — test: Validation changes no authoritative state without an explicit authorised mutation.
- `E06.07.03.F03` **UpdateRemainsProvisional** — test: Working-state updates do not imply durable commitment.
- `E06.07.03.F04` **ProcessProducesQualifiedOutcome** — test: Execution output is separately inspectable before commitment where applicable.
- `E06.07.03.F05` **CommitDispositionAuthoritative** — test: Final accepted state can be verified and differentiated from pre-commit stages.

### E07 — Information Presentation & Human Interaction
The system can expose governed information and work context to people in useful, controlled ways.

#### E07.01 — Contextual Information Presentation
**E07.01.06 — Human Role & Context Sensitive Presentation**  
Present care, participant, administrative and operational information in their own appropriate conceptual and decision contexts.  
*Candidate Business demand:* `B01.04` Informed Participation & Choice, `B05.01` Multidisciplinary Care Coordination, `B14.01` Personal Goal & Preference Support, `B07.04` Service Delivery Coordination.  
*Source context:* `PRESENT`, `TH`, `JOURNEY`; *review findings:* R11, R18. **Standing: Proposed, not verified.**

- `E07.01.06.F01` **AudienceContextAppropriate** — test: Patient and participant interfaces are assessed against distinct journeys.
- `E07.01.06.F02` **ClinicalAndNonclinicalSeparated** — test: Clinical judgements are not silently inferred from participant support preferences.
- `E07.01.06.F03` **RestrictedFieldsOmitted** — test: Presentation respects field and compartment restrictions.
- `E07.01.06.F04` **SourceAndCurrencyVisible** — test: Display includes source and relevant age of information.
- `E07.01.06.F05` **ScenarioMeaningValidated** — test: Users can identify the correct intended action in representative domain scenarios.

**E07.01.07 — Operational Assurance Presentation**  
Support OA&M users with status, exceptions and service readiness without exposing clinical content unnecessarily.  
*Candidate Business demand:* `B07.06` Service Performance & Reporting, `B12.04` Enterprise Risk & Resilience, `B12.05` Health Service Readiness & Mobilisation.  
*Source context:* `OPS`, `LOG`, `PRESENT`; *review findings:* R14, R24. **Standing: Proposed, not verified.**

- `E07.01.07.F01` **ReadinessStatusGrounded** — test: UI readiness state corresponds to observed component and dependency health.
- `E07.01.07.F02` **IncidentDrilldownAvailable** — test: An incident can be traced to correlated diagnostics.
- `E07.01.07.F03` **NoDefaultClinicalPayload** — test: OA&M displays avoid PHI unless separately authorised and necessary.
- `E07.01.07.F04` **RecoveryProgressVisible** — test: Restoration state differentiates in-progress and verified recovered.
- `E07.01.07.F05` **OperatorActionsAudited** — test: Operational actions with material consequences are attributable.

#### E07.02 — Governed User Interaction
**E07.02.05 — Staged Change Review & Confirmation**  
Allow a user to review, check, adjust and explicitly submit a change without collapsing those intentions into commitment.  
*Candidate Business demand:* `B01.04` Informed Participation & Choice, `B10.07` Information Correction & Reconciliation, `B14.03` Choice & Support Arrangement.  
*Source context:* `PRESENT`, `MN`, `TH`; *review findings:* R06, R11. **Standing: Proposed, not verified.**

- `E07.02.05.F01` **ReviewDoesNotMutate** — test: Review interaction does not commit authoritative changes.
- `E07.02.05.F02` **ValidationFindingsActionable** — test: Check results identify correctable conditions in consumer language.
- `E07.02.05.F03` **PreparedChangeDiffVisible** — test: A user can see intended differences before submission.
- `E07.02.05.F04` **CommitConsentExplicit** — test: Where required, authoritative submission requires a distinct confirmation.
- `E07.02.05.F05` **UnknownCommitNotSuccess** — test: An uncertain commitment cannot be displayed as confirmed success.

**E07.02.06 — Supported & Represented Participation**  
Allow appropriately authorised assistance and representation while recording the participant or patient’s own preferences and authority limits.  
*Candidate Business demand:* `B01.05` Representation & Carer Participation, `B14.01` Personal Goal & Preference Support, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `TH`, `PRESENT`, `JOURNEY`; *review findings:* R11, R22. **Standing: Proposed, not verified.**

- `E07.02.06.F01` **RepresentativeScopeVisible** — test: The effective scope and duration of a representative role can be inspected.
- `E07.02.06.F02` **SupportedVoiceRecorded** — test: Participant preference is recorded separately from representative instruction.
- `E07.02.06.F03` **AuthorityRevocationImmediate** — test: Expired or revoked representation is not permitted to continue.
- `E07.02.06.F04` **DisagreementNotSilenced** — test: Conflicting preferences and instructions are distinguishable for escalation.
- `E07.02.06.F05` **DecisionEvidenceAttributable** — test: The person, representative and reviewer actions remain separately attributable.

**E07.02.07 — Accessibility & Language Scope Transparency**  
Express actual accessibility and language support without silently asserting unapproved release commitments.  
*Candidate Business demand:* `B01.04` Informed Participation & Choice, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `PRESENT`, `NFR`, `JOURNEY`; *review findings:* R11. **Standing: Proposed, not verified.**

- `E07.02.07.F01` **SupportedLocaleDeclared** — test: Available release-language support is explicit.
- `E07.02.07.F02` **UnsupportedNeedEscalatable** — test: A user can obtain a bounded alternative when needed support is absent.
- `E07.02.07.F03` **NoFalseWCAGClaim** — test: Testing does not assert WCAG conformance when it is outside the release baseline.
- `E07.02.07.F04` **CriticalContentLegible** — test: Known accessibility-critical scenarios receive targeted review against actual contract.
- `E07.02.07.F05` **HelpContextRelevant** — test: Service guidance is expressed in the current user participation context.

#### E07.03 — Longitudinal Information Viewing
**E07.03.04 — Cross-Domain Longitudinal Correlation View**  
Display a person’s timeline across encounters, providers and representations without conflating occurrence, documentation and custody time.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B05.04` Longitudinal Care Coordination, `B14.04` Ongoing Support Coordination.  
*Source context:* `IU`, `REF`, `PRESENT`; *review findings:* R09, R11. **Standing: Proposed, not verified.**

- `E07.03.04.F01` **TimeAxisDiscriminated** — test: Occurrence and recorded/persisted times can be distinguished.
- `E07.03.04.F02` **SourceSystemRetained** — test: Cross-provider timeline entries show source lineage.
- `E07.03.04.F03` **HistoricalVersionAccessible** — test: A relevant past state can be retrieved without current overwrite.
- `E07.03.04.F04` **UncertainCorrelationMarked** — test: Probable but unconfirmed identity matches are qualified.
- `E07.03.04.F05` **RestrictedTimelineFiltered** — test: Protected records do not leak through timeline metadata.

#### E07.04 — User-Context Continuity
**E07.04.04 — Safe Interrupted Session Continuation**  
Restore a user’s work and navigation context after interruption without implying uncommitted actions were accepted.  
*Candidate Business demand:* `B01.04` Informed Participation & Choice, `B05.01` Multidisciplinary Care Coordination, `B14.04` Ongoing Support Coordination.  
*Source context:* `PRAGMATON`, `PRESENT`, `TH`; *review findings:* R06, R11. **Standing: Proposed, not verified.**

- `E07.04.04.F01` **UncommittedChangesLabelled** — test: Recovered drafts are clearly not authoritative.
- `E07.04.04.F02` **AuthorityRecheckedOnResume** — test: Session restoration does not bypass new authorisation conditions.
- `E07.04.04.F03` **OriginalContextIdentifiable** — test: Restored work preserves source unit/version identifiers.
- `E07.04.04.F04` **InterruptedActionDisambiguated** — test: The UI identifies whether submission outcome is pending, failed or committed.
- `E07.04.04.F05` **SafeContextSwitch** — test: Switching patient/participant context cannot carry prior restricted content across.

**E07.04.05 — Review-to-Action Context Preservation**  
Maintain the identity, source version and authority of information as a consumer moves from finding to action.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B05.03` Care Responsibility Transfer, `B10.07` Information Correction & Reconciliation.  
*Source context:* `PRESENT`, `REF`, `TH`; *review findings:* R06, R18. **Standing: Proposed, not verified.**

- `E07.04.05.F01` **SelectedObjectStable** — test: A selected item remains bound to the intended governed identity.
- `E07.04.05.F02` **VersionChangeAlerted** — test: Concurrent updates trigger visible context-change qualification.
- `E07.04.05.F03` **NoCrossPersonLeakage** — test: Switching subjects cannot reuse the previous subject information.
- `E07.04.05.F04` **ActionTargetConfirmed** — test: High-impact actions identify the actual target object and effective role.
- `E07.04.05.F05` **AuditContextCorrelatable** — test: User action evidence links to the viewed source when required.

#### E07.05 — Information Presentation Accountability
**E07.05.05 — Decision Provenance Presentation**  
Make visible which human and automated inputs influenced a displayed conclusion without assigning decision authority to advice.  
*Candidate Business demand:* `B02.04` Diagnostic & Clinical Reasoning, `B11.03` Clinical Governance & Practice Assurance, `B14.06` Participant Safeguarding & Supported Decision Participation.  
*Source context:* `PROV`, `ASSURE`, `PRESENT`; *review findings:* R15, R18. **Standing: Proposed, not verified.**

- `E07.05.05.F01` **EvidenceLinksUsable** — test: A displayed recommendation links to retrievable authorised evidence.
- `E07.05.05.F02` **AutomatedAdviceLabelled** — test: AI/automated observations are recognisable as non-authoritative.
- `E07.05.05.F03` **DecisionOwnerDistinct** — test: The human or authorised decision owner is distinguished from advice origin.
- `E07.05.05.F04` **DataGapsDisplayed** — test: Material gaps and outdated inputs are explicit.
- `E07.05.05.F05` **RecommendationReproducible** — test: Available provenance permits review of the basis used for advice.

**E07.05.06 — Incomplete & Qualified Information Disclosure**  
Distinguish absent, unavailable, restricted, stale and partially derived information in human-facing presentation.  
*Candidate Business demand:* `B02.01` Clinical Context Establishment, `B10.03` Appropriate Information Access & Sharing, `B14.02` Support Needs & Circumstances Assessment.  
*Source context:* `PRESENT`, `MN`, `IU`; *review findings:* R09, R11. **Standing: Proposed, not verified.**

- `E07.05.06.F01` **UnavailableNotAbsent** — test: Temporary inaccessibility is not displayed as an absent clinical fact.
- `E07.05.06.F02` **PermissionNotExistence** — test: Restriction notices do not leak protected existence details.
- `E07.05.06.F03` **StalenessVisible** — test: Known source currency is shown where relevant.
- `E07.05.06.F04` **DerivedQualificationShown** — test: Inferred and transformed information is visually distinguishable from native source.
- `E07.05.06.F05` **PartialResultCaveat** — test: Material incompleteness is shown before a consequential user decision.

### E08 — Operational Observability, Assurance & Resilience
The system can reveal its operational state, preserve evidence, support controlled response and recover service.

#### E08.01 — Operational Observability
**E08.01.04 — Dependency & Readiness Truthfulness**  
Represent dependency availability, degraded status and recovery readiness across service domains.  
*Candidate Business demand:* `B07.06` Service Performance & Reporting, `B12.04` Enterprise Risk & Resilience, `B12.05` Health Service Readiness & Mobilisation.  
*Source context:* `OPS`, `NFR`; *review findings:* R24. **Standing: Proposed, not verified.**

- `E08.01.04.F01` **CriticalDependencyKnown** — test: Each in-scope component declares material execution dependencies.
- `E08.01.04.F02` **DependencyFaultPropagated** — test: Unhealthy dependency affects readiness as required.
- `E08.01.04.F03` **NoFalseHealthyOnIsolation** — test: Partitioned service is not reported healthy when unable to meet its contract.
- `E08.01.04.F04` **RestorationVerified** — test: Green status requires bounded functional recovery evidence where applicable.
- `E08.01.04.F05` **OperationalViewTimely** — test: Material readiness changes reach operational observers within the defined target.

#### E08.02 — Operational Logging & Evidence Collection
**E08.02.04 — Operational Log Satellite & Receipt**  
Capture local operational events through in-JVM reception, buffering and forwarding with explicit server-receipt semantics.  
*Candidate Business demand:* `B07.06` Service Performance & Reporting, `B12.04` Enterprise Risk & Resilience.  
*Source context:* `LOG`, `OPS`; *review findings:* R14. **Standing: Proposed, not verified.**

- `E08.02.04.F01` **LocalReceiverPresent** — test: Required application components can emit standard log events.
- `E08.02.04.F02` **BufferPressureObserved** — test: Buffer saturation is measured and reported.
- `E08.02.04.F03` **ServerReceiptAccurate** — test: Satellite ACK refers to server receipt, not persistent storage.
- `E08.02.04.F04` **PHIControlsApplied** — test: Logging paths apply approved data-minimisation/handling requirements.
- `E08.02.04.F05` **SatelliteFailureQualification** — test: JVM loss and unpersisted event exposure are not falsely claimed lossless.

**E08.02.05 — Log Server & Persistence Independence**  
Operate receiving/forwarding/log storage boundaries without conflating operational log custody with Mnemosyne or audit custody.  
*Candidate Business demand:* `B12.04` Enterprise Risk & Resilience, `B12.03` Enterprise Performance & Accountability.  
*Source context:* `LOG`, `OPS`; *review findings:* R14. **Standing: Proposed, not verified.**

- `E08.02.05.F01` **LogServerAckDistinct** — test: Receipt ACK remains distinct from durable log storage acknowledgement.
- `E08.02.05.F02` **LogStoreIndependent** — test: Log-Persistence is not represented as a Mnemosyne PersistenceServiceInstance.
- `E08.02.05.F03` **BackpressureHandled** — test: Log transport congestion is observable and controlled.
- `E08.02.05.F04` **LogRecoveryQualified** — test: Post-failure log completeness claims reflect the actual receipt/durability model.
- `E08.02.05.F05` **FOSSDeploymentConformant** — test: Chosen logging infrastructure licensing remains within mandated FOSS bounds.

#### E08.04 — Conformance & Assurance Support
**E08.04.04 — Evidence-Based Architectural Conformance**  
Compare release state against governed capabilities, service obligations, NFRs and integrity tests without mistaking test presence for satisfaction.  
*Candidate Business demand:* `B11.03` Clinical Governance & Practice Assurance, `B11.05` Regulatory Conformance & Accreditation, `B12.03` Enterprise Performance & Accountability.  
*Source context:* `NFR`, `ASSURE`, `JOURNEY`; *review findings:* R15, R18. **Standing: Proposed, not verified.**

- `E08.04.04.F01` **EveryRequirementDispositioned** — test: Applicable requirements have independently evidenced pass/fail/qualified statuses.
- `E08.04.04.F02` **CapabilityClaimTraceable** — test: Claims point to tests and exact architecture versions.
- `E08.04.04.F03` **UnmeasuredFeatureUnclaimed** — test: No Feature is labelled satisfied solely from a mapped CT2 or Service.
- `E08.04.04.F04` **ExceptionsInReleaseReport** — test: Deferred and failed checks remain visible in assurance publication.
- `E08.04.04.F05` **ReproductionEvidenceAvailable** — test: A reviewer can repeat a documented reference test without unavailable private state.

#### E08.06 — Service Continuity & Recovery
**E08.06.04 — Disaster Recovery Consistency Proof**  
Demonstrate that information, accepted work and operational services recover coherently after bounded and catastrophic failure.  
*Candidate Business demand:* `B12.04` Enterprise Risk & Resilience, `B12.05` Health Service Readiness & Mobilisation, `B10.01` Health Record Stewardship.  
*Source context:* `RES`, `RES-W`, `NFR`; *review findings:* R24, R21. **Standing: Proposed, not verified.**

- `E08.06.04.F01` **SingleNodeFailureTransparent** — test: Accepted work remains recoverable across tested single-node loss.
- `E08.06.04.F02` **DisasterRecoveryPointKnown** — test: Recovered authoritative state has known consistent recovery point.
- `E08.06.04.F03` **AcceptedWorkNotLost** — test: Persisted accepted obligations are neither silently lost nor falsely completed.
- `E08.06.04.F04` **RecoveryIdentityUnchanged** — test: Restoration preserves canonical information and task identities.
- `E08.06.04.F05` **RecoveryEvidenceReproducible** — test: Fault injection and restoration outcomes are independently documented.

#### E08.07 — Audit, Provenance & Assurance Evidence Recording
**E08.07.01 — Mandatory Audit Contribution**  
Capture auditable actions close to their source without making each source the authoritative AuditEvent custodian.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B10.06` Retention, Disclosure & Record Accountability, `B11.02` Incident Reporting & Learning.  
*Source context:* `AUD`, `TH`, `PROV`; *review findings:* R03, R14. **Standing: Proposed, not verified.**

- `E08.07.01.F01` **SourceAuditContributionPresent** — test: Mandatory covered actions generate required contributions.
- `E08.07.01.F02` **AuditNotOperationalLog** — test: A log message alone cannot satisfy mandatory AuditEvent contribution.
- `E08.07.01.F03` **EventActorContextCorrelated** — test: Subject, action, identity and relevant Task/IU references are retained.
- `E08.07.01.F04` **AuditFailureReported** — test: Failure to meet mandatory audit obligations is never silent.
- `E08.07.01.F05` **SensitiveEvidenceProtected** — test: Audit contribution avoids unnecessary PHI leakage while retaining required accountability.

**E08.07.02 — Immutable AuditEvent Record Management**  
Maintain append-only governed AuditEvent records, including correction by subsequent records and independent access history.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.03` Clinical Governance & Practice Assurance, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `AUD`, `PROV`; *review findings:* R03. **Standing: Proposed, not verified.**

- `E08.07.02.F01` **NoInPlaceAuditMutation** — test: Attempted alteration of stored AuditEvent is rejected.
- `E08.07.02.F02` **QualificationIsNewRecord** — test: A correction creates attributable new record linked to predecessor.
- `E08.07.02.F03` **AuditRecordReadSeparated** — test: Reading an AuditEvent does not create a recursive new AuditEvent.
- `E08.07.02.F04` **AccessHistoryIndependent** — test: Accesses to audit records are captured through the agreed separate history mechanism.
- `E08.07.02.F05` **AuditRecordRecoverable** — test: Immutable records survive required recovery and retention tests.

**E08.07.03 — Chronological Provenance Evidence**  
Preserve ordered provenance evidence with temporal interpretation independent of recording order.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B10.07` Information Correction & Reconciliation, `B11.03` Clinical Governance & Practice Assurance.  
*Source context:* `PROV`, `IU`, `REF`; *review findings:* R03. **Standing: Proposed, not verified.**

- `E08.07.03.F01` **ChronologicalOrderPreserved** — test: Evidence list order matches represented activity chronology in reference scenarios.
- `E08.07.03.F02` **RecordedTimeNotSubstituted** — test: Late evidence insertion does not silently redefine occurrence order.
- `E08.07.03.F03` **EvidenceReferencesValid** — test: Referenced evidence has stable identity and role semantics.
- `E08.07.03.F04` **ImmutableProvenanceProtected** — test: Recorded Provenance and its evidence order cannot be edited in place.
- `E08.07.03.F05` **IncompleteLineageQualified** — test: Unresolved evidence is explicitly identified without inventing a complete chain.

**E08.07.04 — Assurance Evidence Collection & Classification**  
Collect and classify conformance observations, test results and exceptions without confounding audit or provenance authority.  
*Candidate Business demand:* `B11.03` Clinical Governance & Practice Assurance, `B11.05` Regulatory Conformance & Accreditation, `B12.03` Enterprise Performance & Accountability.  
*Source context:* `ASSURE`, `NFR`, `PROV`; *review findings:* R03, R15. **Standing: Proposed, not verified.**

- `E08.07.04.F01` **EvidenceTypeDistinguished** — test: Assurance, operational log, audit and provenance types remain separately identifiable.
- `E08.07.04.F02` **ObservationSourceKnown** — test: Collected findings reference the tested system/version and source.
- `E08.07.04.F03` **AssuranceFindingRepeatable** — test: A known test observation can be reproduced or its limits explained.
- `E08.07.04.F04` **QualificationNotDiscarded** — test: Adverse or partial findings remain visible within release evidence.
- `E08.07.04.F05` **NoAutomatedApproval** — test: AI assurance output does not automatically confer release acceptance.

#### E08.08 — Evidence Collation, Export & Controlled Release
**E08.08.01 — Non-Dereferenced Record Export**  
Export governed raw logging, AuditEvent and Provenance records and their references without resolving underlying evidence payloads.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.05` Regulatory Conformance & Accreditation, `B12.03` Enterprise Performance & Accountability.  
*Source context:* `PROV`, `LOG`, `TH`; *review findings:* R04, R14. **Standing: Proposed, not verified.**

- `E08.08.01.F01` **RawRecordsRemainRaw** — test: Export does not silently dereference protected underlying information.
- `E08.08.01.F02` **TemporalSelectionExplicit** — test: Occurrence-time and persistence-time filtering are distinguished or left unresolved.
- `E08.08.01.F03` **RecordIntegrityInExport** — test: Exported record identities and version/hash evidence survive round trip.
- `E08.08.01.F04` **BulkAndDemandSelection** — test: Bulk and on-demand record selections produce traceable result scopes.
- `E08.08.01.F05` **ExportAuthorityEnforced** — test: Unauthorised record selection fails without leaking restricted content.

**E08.08.02 — Dereferenced Audit Evidence Assembly**  
Assemble authorised explanatory packages for selected AuditEvents while preserving underlying event immutability.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.03` Clinical Governance & Practice Assurance, `B11.05` Regulatory Conformance & Accreditation.  
*Source context:* `PROV`, `TH`, `ASSURE`; *review findings:* R04. **Standing: Proposed, not verified.**

- `E08.08.02.F01` **EventSetPreciselySelected** — test: Requested AuditEvent set and selection criteria are recoverable.
- `E08.08.02.F02` **ReferencedEvidenceGathered** — test: Required evidence follows governed reference resolution rules.
- `E08.08.02.F03` **UnresolvedEvidenceDeclared** — test: Missing/invalid evidence triggers explicit bounded outcome.
- `E08.08.02.F04` **NoPartialOnAuthorisationFailure** — test: Any denied required item prevents entire assembled release.
- `E08.08.02.F05` **AuditPackageInterpretable** — test: An authorised recipient can understand the export without presumed database access.

**E08.08.03 — Dereferenced Provenance Evidence Assembly**  
Assemble authorised provenance chains and contextual evidence without confusing their ordering or source identity.  
*Candidate Business demand:* `B10.02` Information Completeness & Provenance Accountability, `B11.05` Regulatory Conformance & Accreditation, `B13.05` Health Research Execution.  
*Source context:* `PROV`, `REF`, `TH`; *review findings:* R04, R23. **Standing: Proposed, not verified.**

- `E08.08.03.F01` **ProvenanceSetPrecise** — test: Selected lineage roots and temporal context are explicit.
- `E08.08.03.F02` **ChronologyPreservedInExport** — test: Evidence remains in agreed chronological semantic order.
- `E08.08.03.F03` **RecursionPolicyBounded** — test: Recursive expansion follows the declared traversal constraints.
- `E08.08.03.F04` **MissingEvidenceQualified** — test: Unavailable but relevant evidence is not silently invented.
- `E08.08.03.F05` **CompleteReleaseGate** — test: No assembled package is released before whole-set authorisation succeeds.

**E08.08.04 — All-or-Nothing Governed Release**  
Withhold incomplete or partly unauthorised evidence products and account for staging, refusal and disposition.  
*Candidate Business demand:* `B10.06` Retention, Disclosure & Record Accountability, `B11.05` Regulatory Conformance & Accreditation, `B13.04` Research Governance & Participant Protection.  
*Source context:* `PROV`, `TH`, `SEC`; *review findings:* R04, R23. **Standing: Proposed, not verified.**

- `E08.08.04.F01` **ReleaseCompletenessVerified** — test: Released set contains every required requested element or a justified scoped exclusion.
- `E08.08.04.F02` **NoPartialReleaseAfterDenial** — test: Any mandatory denied item prevents any partial output delivery.
- `E08.08.04.F03` **StagedPayloadProtected** — test: Intermediate assembled information is never publicly addressable.
- `E08.08.04.F04` **ReleaseTimeAuthorityCurrent** — test: Authorisation is valid at actual release boundary.
- `E08.08.04.F05` **FailureNoSensitiveLeak** — test: Failure status excludes protected evidence content while identifying remedial path.

### E09 — Information Insight & Evidence Use
The system can organise information for queries, analysis and decision-supporting evidence without acquiring the decision authority itself.

#### E09.01 — Information Set & Result Organisation
**E09.01.04 — Stable Versioned Information Set**  
Define a governed collection of IU version references with reproducible selection and work-context preservation.  
*Candidate Business demand:* `B13.05` Health Research Execution, `B10.02` Information Completeness & Provenance Accountability, `B11.03` Clinical Governance & Practice Assurance.  
*Source context:* `IU`, `RESULT`, `PRAGMATON`; *review findings:* R23, R09. **Standing: Proposed, not verified.**

- `E09.01.04.F01` **IUVersionRefsOnly** — test: Search result sets retain governed IU version references rather than uncontrolled payload copies.
- `E09.01.04.F02` **SetIdentityRepeatable** — test: A fixed set reference reproduces identical membership.
- `E09.01.04.F03` **SelectionProvenanceKnown** — test: Query criteria, requester and time context can be inspected.
- `E09.01.04.F04` **SetContextDurablyPreserved** — test: Relevant Ergon context can recover result-set membership.
- `E09.01.04.F05` **RestrictedMembersNotLeaked** — test: Results are re-evaluated for applicable access before disclosure.

#### E09.02 — Information Aggregation & Analysis
**E09.02.03 — Temporal & Provenance Aware Aggregation**  
Aggregate information while retaining source versions, definition changes and temporal applicability.  
*Candidate Business demand:* `B13.01` Population Health Surveillance, `B11.04` Service Quality & Outcome Evaluation, `B15.05` Funding Outcomes & Equity Review.  
*Source context:* `IU`, `PROV`, `INSIGHT`; *review findings:* R23. **Standing: Proposed, not verified.**

- `E09.02.03.F01` **SourceVersionTraceable** — test: Reported aggregate can identify contributing governed versions.
- `E09.02.03.F02` **TemporalWindowCorrect** — test: The declared occurrence/recording time basis is applied consistently.
- `E09.02.03.F03` **DefinitionChangeQualified** — test: Model changes affecting comparability are declared.
- `E09.02.03.F04` **RestrictedContributionControlled** — test: Security limits apply to aggregate disclosure or suppressions.
- `E09.02.03.F05` **CalculationReproducible** — test: A fixed dataset and transformation definition reproduce an aggregate.

**E09.02.04 — Population Insight Without Identity Leakage**  
Produce allowed population-level metrics with defensible suppression, scope and data quality checks.  
*Candidate Business demand:* `B13.01` Population Health Surveillance, `B13.03` Public Health Protection & Response, `B15.05` Funding Outcomes & Equity Review.  
*Source context:* `INSIGHT`, `TH`, `IU`; *review findings:* R12, R23. **Standing: Proposed, not verified.**

- `E09.02.04.F01` **SuppressionPolicyEnforced** — test: Disallowed small-cell or sensitive breakdown release is blocked where policy applies.
- `E09.02.04.F02` **PopulationDenominatorKnown** — test: Reported rates state inclusion criteria and denominator version.
- `E09.02.04.F03` **MissingDataBiasFlagged** — test: Material incompleteness is visible rather than hidden.
- `E09.02.04.F04` **TimeComparisonComparable** — test: Series identify source and definition changes.
- `E09.02.04.F05` **NoIndividualDisclosure** — test: Aggregate pathways do not reveal protected individual records.

#### E09.03 — Operational & Business Information Reporting
**E09.03.04 — Controlled Business Evidence Reporting**  
Provide accountable business and service reports without implying operation of the underlying business transaction engines.  
*Candidate Business demand:* `B07.06` Service Performance & Reporting, `B12.03` Enterprise Performance & Accountability, `B15.04` Claims & Funding Accountability.  
*Source context:* `INSIGHT`, `PROV`, `TH`; *review findings:* R12, R23. **Standing: Proposed, not verified.**

- `E09.03.04.F01` **ReportingSourceKnown** — test: Report measures link to governed sources and calculation definitions.
- `E09.03.04.F02` **NoImplicitClaimsAdjudication** — test: A funding report is not represented as a claims decision service.
- `E09.03.04.F03` **ReportScopeAuthorised** — test: Recipient receives only data within authorised purpose.
- `E09.03.04.F04` **ReportQualificationVisible** — test: Known coverage and completeness limitations are explicit.
- `E09.03.04.F05` **ReportVersionReproducible** — test: A historical report can be reconstructed from pinned inputs.

**E09.03.05 — Commissioning & Funding Accountability Data**  
Expose trusted information supporting commissioning and funding review without presuming an internal payer platform.  
*Candidate Business demand:* `B15.01` Health Needs-Based Commissioning, `B15.04` Claims & Funding Accountability, `B15.05` Funding Outcomes & Equity Review.  
*Source context:* `INSIGHT`, `TH`, `PROV`; *review findings:* R12. **Standing: Proposed, not verified.**

- `E09.03.05.F01` **CommissioningContextTraceable** — test: Recommendations identify source population/service need evidence.
- `E09.03.05.F02` **EntitlementNotDigitalGrant** — test: Funding eligibility is distinguished from information access permission.
- `E09.03.05.F03` **ClaimsStatusNotAdjudication** — test: Status reports do not assert payment/claims processing authority.
- `E09.03.05.F04` **FundingReportReproducible** — test: Versioned inputs support independent audit of reported amount/status.
- `E09.03.05.F05` **ScopeOfSolutionExplicit** — test: Unimplemented or external financial functions remain documented as dependencies.

#### E09.04 — Evidence & Insight Presentation
**E09.04.03 — Advisory Evidence & Confidence Presentation**  
Expose recommendations and interpretations as qualified guidance with evidence and without unapproved decision authority.  
*Candidate Business demand:* `B02.04` Diagnostic & Clinical Reasoning, `B11.04` Service Quality & Outcome Evaluation, `B13.06` Evidence Translation & Practice Improvement.  
*Source context:* `ASSURE`, `INSIGHT`, `PROV`; *review findings:* R15. **Standing: Proposed, not verified.**

- `E09.04.03.F01` **AdviceNotAuthoritative** — test: AI recommendations never write an authoritative business decision without authorised action.
- `E09.04.03.F02` **ConfidenceAndLimitsShown** — test: Known uncertainty and model limitations accompany advice.
- `E09.04.03.F03` **SourceEvidenceAvailable** — test: Recommendations link to available relevant evidence.
- `E09.04.03.F04` **HumanDecisionAttributable** — test: Acceptance/rejection by an authorised decision maker is separate and auditable.
- `E09.04.03.F05` **AdverseAdviceDetected** — test: Tests include plausible but unsupported recommendations and require appropriate qualification.

**E09.04.04 — Journey Outcome & Integrity Evidence**  
Correlate stakeholder Journey value stages with separately evidenced business outcomes and IT contributions.  
*Candidate Business demand:* `B11.04` Service Quality & Outcome Evaluation, `B12.03` Enterprise Performance & Accountability, `B14.05` Independent Living & Community Participation.  
*Source context:* `JOURNEY`, `ASSURE`, `PROV`; *review findings:* R18. **Standing: Proposed, not verified.**

- `E09.04.04.F01` **StageValueIndependentlyTested** — test: Each scoped Journey stage has a value assertion not merely a technical call.
- `E09.04.04.F02` **ContributionNotDelivery** — test: An EC Service test is not reported as business-outcome fulfilment.
- `E09.04.04.F03` **StageExceptionBounded** — test: Failure and alternative paths carry explicit expected outcomes.
- `E09.04.04.F04` **EvidenceAcrossStagesCorrelated** — test: Identity, authority, output and commitment can be traced end to end.
- `E09.04.04.F05` **UnverifiedStageVisible** — test: Missing or untested Journey stage cannot be silently marked covered.

#### E09.05 — Governed Analytical Access
**E09.05.04 — Research Purpose & Data Release Controls**  
Enforce approved research purpose, participant protections and evidence access without assuming a whole research solution is in Harmonia.  
*Candidate Business demand:* `B13.04` Research Governance & Participant Protection, `B13.05` Health Research Execution, `B10.03` Appropriate Information Access & Sharing.  
*Source context:* `TH`, `PROV`, `INSIGHT`; *review findings:* R23, R12. **Standing: Proposed, not verified.**

- `E09.05.04.F01` **ApprovedPurposeVerified** — test: Research information retrieval checks permitted purpose and applicable grant.
- `E09.05.04.F02` **ParticipantRestrictionsHonoured** — test: Consent, restrictions and representation conditions are applied.
- `E09.05.04.F03` **DataSnapshotPinned** — test: Study dataset references preserve a reproducible authorised version.
- `E09.05.04.F04` **ExtractAllOrNothingWhereRequired** — test: High-risk export checks reject unauthorised partial release.
- `E09.05.04.F05` **ResearchScopeQualified** — test: Capabilities are labelled contribution/optional where a full research runtime is not committed.

## Detailed Business Capability extensions
### B01 — Care Participation & Access
**B01.05.04 — Representation Authority Accountability** — Establish and review what a carer, advocate or responsible person can legitimately decide or access, without replacing the person’s own voice.  
*Candidate Enterprise contribution:* `E05.06` Authorisation Policy & Effective Authority Management, `E05.02` Contextual Authorisation Determination, `E07.02` Governed User Interaction. *Finding:* R01, R11.  
- `B01.05.04.F01` **RepresentationBasisKnown** — test: Authority basis is documented and current.
- `B01.05.04.F02` **ScopeOfAssistanceClear** — test: Permitted support differs explicitly from permitted decision-making.
- `B01.05.04.F03` **RevokedAuthorityHonoured** — test: Expired authority no longer permits representative action.
- `B01.05.04.F04` **PersonPreferenceRecorded** — test: Represented person’s wishes remain separately visible.
- `B01.05.04.F05` **DisputedAuthorityEscalated** — test: Disputed representation triggers a documented review path.

### B02 — Clinical Assessment & Determination
**B02.01.04 — Clinically Meaningful Information Currency** — Enable clinicians to identify whether information is current, historic, disputed, incomplete or unavailable when establishing clinical context.  
*Candidate Enterprise contribution:* `E02.01` Governed Information Access, `E07.05` Information Presentation Accountability, `E01.02` Information Version & History Management. *Finding:* R09, R11.  
- `B02.01.04.F01` **HistoricalStateQualified** — test: Clinician distinguishes historic state from current presentation.
- `B02.01.04.F02` **UnavailableNotAssumedAbsent** — test: Service outage is not mistaken for negative clinical history.
- `B02.01.04.F03` **DisputedFactsVisible** — test: Relevant unresolved corrections are clearly identified.
- `B02.01.04.F04` **SourceAndTimeKnown** — test: Material facts include source, occurrence and documentation time.
- `B02.01.04.F05` **CriticalContextIntegrity** — test: Missing critical context is flagged before consequential clinical decisions.

### B04 — Diagnostic Investigation
**B04.04.04 — Diagnostic Information Release Accountability** — Determine that diagnostic results are validly released and traceable without equating transport receipt with clinical responsibility transfer.  
*Candidate Enterprise contribution:* `E03.01` External Information Exchange, `E03.04` Interaction Acknowledgement & Error Handling, `E08.07` Audit, Provenance & Assurance Evidence Recording. *Finding:* R16.  
- `B04.04.04.F01` **ResultVerifiedBeforeRelease** — test: Published diagnostic result has required verification.
- `B04.04.04.F02` **ReleaseIdentityKnown** — test: Accountable releaser and release instant can be established.
- `B04.04.04.F03` **DeliveryNotClinicalAcceptance** — test: Technical delivery is distinguished from clinician review.
- `B04.04.04.F04` **CorrectionHistoryPreserved** — test: Corrected results retain historical traceability.
- `B04.04.04.F05` **CriticalResultEscalationTracked** — test: Urgent findings have a traceable acknowledgement/escalation path.

### B05 — Care Coordination & Continuity
**B05.03.04 — Handover Acceptance & Evidence** — Establish whether receiving responsibility was actually accepted and whether outstanding obligations remain accounted for.  
*Candidate Enterprise contribution:* `E06.07` Work Acceptance & Outcome Reconciliation, `E03.04` Interaction Acknowledgement & Error Handling, `E08.07` Audit, Provenance & Assurance Evidence Recording. *Finding:* R07, R16.  
- `B05.03.04.F01` **ReceivingPartyAgreed** — test: Handover identifies who accepted clinical responsibility.
- `B05.03.04.F02` **UnacceptedHandoverVisible** — test: No receipt notification is treated as acceptance without required evidence.
- `B05.03.04.F03` **OutstandingWorkOwned** — test: Each unresolved obligation has accountable next owner.
- `B05.03.04.F04` **TransferredContextSufficient** — test: Required care context is available to receiver.
- `B05.03.04.F05` **AcceptanceTimeTraceable** — test: Timing of acceptance and any delay is independently evidenced.

### B07 — Health Service Provision
**B07.04.04 — Digital Service Handoff Accountability** — Coordinate business activity across service boundaries while knowing what has been requested, executed, accepted and committed.  
*Candidate Enterprise contribution:* `E06.01` Task Representation & State Management, `E06.07` Work Acceptance & Outcome Reconciliation, `E08.03` Audit & Provenance Reporting. *Finding:* R06, R07.  
- `B07.04.04.F01` **SubmittedRequestTraceable** — test: A business request has an identifiable digital work obligation.
- `B07.04.04.F02` **ExecutionOutcomeKnown** — test: Work execution results are visible independently of persistence.
- `B07.04.04.F03` **CommitOutcomeKnown** — test: Authoritative acceptance result can be established.
- `B07.04.04.F04` **UncertainHandoffReconciled** — test: Timeout/unknown states have accountable reconciliation.
- `B07.04.04.F05` **NoDuplicateObligation** — test: Retries do not create unaccounted duplicate business demands.

### B08 — Workforce & Professional Practice
**B08.03.04 — Effective Clinical Privilege Evidence** — Ensure professional privileges for a particular care context are valid and reviewable independently of technical authentication.  
*Candidate Enterprise contribution:* `E05.02` Contextual Authorisation Determination, `E05.06` Authorisation Policy & Effective Authority Management, `E07.05` Information Presentation Accountability. *Finding:* R22.  
- `B08.03.04.F01` **PrivilegeForServiceValidated** — test: Actual service and setting privileges are checked.
- `B08.03.04.F02` **TechnicalLoginNotEnough** — test: Successful login alone does not imply clinical privilege.
- `B08.03.04.F03` **PrivilegeExpiryHonoured** — test: Expired/suspended authority cannot be used as current evidence.
- `B08.03.04.F04` **ExceptionalAuthorisationLogged** — test: Emergency or exceptional authority is separately accountable.
- `B08.03.04.F05` **PrivilegeSourceTraceable** — test: The organisation and acceptance basis of privileges can be reviewed.

### B10 — Health Information Stewardship
**B10.02.04 — Provenance Evidence Stewardship** — Ensure that evidence about information origin, transformations and accountability remains identifiable, interpretable and protected.  
*Candidate Enterprise contribution:* `E08.07` Audit, Provenance & Assurance Evidence Recording, `E08.08` Evidence Collation, Export & Controlled Release, `E01.03` Information Integrity Management. *Finding:* R03, R04.  
- `B10.02.04.F01` **ProvenanceChronologyFaithful** — test: Evidence chronology matches represented event order.
- `B10.02.04.F02` **SourceIdentityRetained** — test: Every governed derivative has attributable source identity where required.
- `B10.02.04.F03` **IncompleteProvenanceQualified** — test: Missing evidence does not silently imply full lineage.
- `B10.02.04.F04` **ProvenanceCorrectionAdditive** — test: Qualifications are recorded without altering immutable history.
- `B10.02.04.F05` **ReviewAccessControlled** — test: Only authorised reviewers can obtain protected evidence.

**B10.03.04 — Disclosure Authority Revalidation** — Ensure sharing decisions reflect current consent, policy, compartment and representative authority.  
*Candidate Enterprise contribution:* `E05.06` Authorisation Policy & Effective Authority Management, `E05.03` Local Access Policy Enforcement, `E02.01` Governed Information Access. *Finding:* R01, R02, R22.  
- `B10.03.04.F01` **RevokedPermissionHonoured** — test: Withdrawn sharing authority prevents subsequent disclosure.
- `B10.03.04.F02` **ChangedPolicyReassessed** — test: Previously granted access is reconsidered when policy changes.
- `B10.03.04.F03` **RecipientPurposeVerified** — test: Sharing is limited to intended lawful/authorised purpose.
- `B10.03.04.F04` **NoMetadataDisclosure** — test: Restricted records cannot be discovered through unprivileged metadata.
- `B10.03.04.F05` **DenialAccountable** — test: A defensible record of an authorisation refusal is available where required.

**B10.06.04 — Comprehensive Evidence Disclosure** — Release requested evidentiary information only when complete, valid and authorisable as an integrated disclosure.  
*Candidate Enterprise contribution:* `E08.08` Evidence Collation, Export & Controlled Release, `E05.05` Information Protection & Security Evidence, `E09.03` Operational & Business Information Reporting. *Finding:* R04, R23.  
- `B10.06.04.F01` **RequestedSetComplete** — test: The approved requested evidence set is fully assembled before release.
- `B10.06.04.F02` **NoPartialEvidenceRelease** — test: Any mandatory authorisation denial prevents release of the whole set.
- `B10.06.04.F03` **RawVsInterpretedDistinct** — test: A record export differs clearly from dereferenced explanatory evidence.
- `B10.06.04.F04` **DisclosureBasisRetained** — test: Purpose, authority and release decision remain attributable.
- `B10.06.04.F05` **IncompleteRequestQualified** — test: Missing essential evidence yields refusal/exception rather than misleading completeness.

**B10.07.04 — Correction Commitment Accountability** — Distinguish working correction, review, execution and authoritative acceptance while preserving immutable prior versions.  
*Candidate Enterprise contribution:* `E01.01` Governed Information Persistence, `E06.07` Work Acceptance & Outcome Reconciliation, `E07.02` Governed User Interaction. *Finding:* R06, R09.  
- `B10.07.04.F01` **PreparedCorrectionNotFinal** — test: Uncommitted drafts are not treated as authoritative.
- `B10.07.04.F02` **ValidationSeparate** — test: Quality checks produce findings before acceptance.
- `B10.07.04.F03` **CommittedVersionIdentified** — test: Accepted correction has a resolvable new governed version.
- `B10.07.04.F04` **RejectedCorrectionSafe** — test: Rejected action does not alter previous authoritative content.
- `B10.07.04.F05` **ConflictResolutionTraceable** — test: Competing corrections retain decision evidence and prior history.

### B11 — Safety, Quality & Accountability
**B11.03.04 — Auditable Clinical Governance Evidence** — Obtain adequate, immutable and interpretable evidence to review clinical governance and material practice events.  
*Candidate Enterprise contribution:* `E08.07` Audit, Provenance & Assurance Evidence Recording, `E08.08` Evidence Collation, Export & Controlled Release, `E09.04` Evidence & Insight Presentation. *Finding:* R03, R04.  
- `B11.03.04.F01` **MandatoryActionsEvidenced** — test: Relevant clinical activity generates required audit evidence.
- `B11.03.04.F02` **GovernanceEvidenceImmutable** — test: Previously accepted audit/provenance records cannot be rewritten.
- `B11.03.04.F03` **EvidenceCorrelatable** — test: Clinician, subject, event, decision and information version can be related.
- `B11.03.04.F04` **ReviewPackageAuthorised** — test: Evidence assemblies are disclosed only after whole-set authorisation.
- `B11.03.04.F05` **AdverseFindingVisible** — test: Qualified or contrary evidence is not suppressed from authorised review.

**B11.05.04 — Evidence-Based Accreditation Verification** — Demonstrate conformance through repeatable observations without asserting success solely from a complete-looking model.  
*Candidate Enterprise contribution:* `E08.04` Conformance & Assurance Support, `E08.07` Audit, Provenance & Assurance Evidence Recording, `E09.04` Evidence & Insight Presentation. *Finding:* R18.  
- `B11.05.04.F01` **RequirementToEvidenceTrace** — test: A compliance claim links to explicit tested requirement and version.
- `B11.05.04.F02` **UnverifiedNotCompliant** — test: Missing evidence cannot be recorded as passing conformance.
- `B11.05.04.F03` **ExceptionDisclosed** — test: Known qualification and deferral remain visible in review.
- `B11.05.04.F04` **RepeatableTestAvailable** — test: Assurance checks can be rerun with controlled inputs.
- `B11.05.04.F05` **ReviewerAuthorityEstablished** — test: The approval and evidence-review roles are identifiable.

### B12 — Enterprise Direction & Readiness
**B12.04.04 — Accepted Service Obligation Continuity** — Ensure business-critical accepted obligations survive interruption and their completion or failure can be accounted for.  
*Candidate Enterprise contribution:* `E06.04` Delivery Reliability & Recovery, `E06.07` Work Acceptance & Outcome Reconciliation, `E08.06` Service Continuity & Recovery. *Finding:* R21, R24.  
- `B12.04.04.F01` **AcceptedWorkNotLost** — test: Accepted work remains recoverable after bounded failure.
- `B12.04.04.F02` **OverloadRefusalDeclared** — test: Refused work is not reported as accepted.
- `B12.04.04.F03` **RecoveryStatusAccurate** — test: Operators know the actual recovery/unfinished-work state.
- `B12.04.04.F04` **NoUncontrolledDuplicateWork** — test: Replay does not silently duplicate business obligations.
- `B12.04.04.F05` **CriticalServiceRestored** — test: Declared critical service continuity is verified by representative operation.

### B13 — Population Health & Learning
**B13.04.04 — Research Information Release Accountability** — Protect participants and authorised research purposes through governed, reproducible information release.  
*Candidate Enterprise contribution:* `E09.05` Governed Analytical Access, `E08.08` Evidence Collation, Export & Controlled Release, `E05.06` Authorisation Policy & Effective Authority Management. *Finding:* R23.  
- `B13.04.04.F01` **StudyPurposeApproved** — test: Research request is aligned with approved purpose and authority.
- `B13.04.04.F02` **ParticipantRestrictionApplied** — test: Applicable participant restrictions are verified.
- `B13.04.04.F03` **DatasetVersionKnown** — test: Provided research information is identified as a stable snapshot.
- `B13.04.04.F04` **NoPartialRestrictedRelease** — test: Required but unauthorised data prevents improper extract release.
- `B13.04.04.F05` **DisclosureTrailReviewable** — test: Released evidence includes selection and authority provenance.

### B14 — Participation, Independence & Support
**B14.06.04 — Participant Decision Authority Transparency** — Protect a participant’s own decisions while keeping supporters, representatives and automated advice in their proper roles.  
*Candidate Enterprise contribution:* `E07.02` Governed User Interaction, `E05.06` Authorisation Policy & Effective Authority Management, `E09.04` Evidence & Insight Presentation. *Finding:* R11, R15, R22.  
- `B14.06.04.F01` **ParticipantVoicePrimary** — test: Participant goals and choices are recorded distinctly from supporters.
- `B14.06.04.F02` **SupporterAuthorityLimited** — test: Supporter may not exceed verified decision scope.
- `B14.06.04.F03` **AIAdviceNotDecision** — test: Automated recommendations do not become authoritative participant decisions.
- `B14.06.04.F04` **DisagreementResolutionAvailable** — test: Conflict between participant and representative has accountable escalation.
- `B14.06.04.F05` **DecisionEvidenceAccessible** — test: Participant or authorised reviewer can examine decision basis.

### B15 — Health System Funding & Commissioning
**B15.04.04 — Funding Information Accountability Boundary** — Support trustworthy reporting and reconciliation while identifying where claims adjudication and payment occur outside the digital contribution.  
*Candidate Enterprise contribution:* `E09.03` Operational & Business Information Reporting, `E09.04` Evidence & Insight Presentation, `E08.08` Evidence Collation, Export & Controlled Release. *Finding:* R12.  
- `B15.04.04.F01` **FinancialSourceDefined** — test: Reported amounts link to qualified source systems and data.
- `B15.04.04.F02` **PaymentStatusNotInvented** — test: An information report does not assert actual remittance without source evidence.
- `B15.04.04.F03` **ClaimsAuthorityKnown** — test: Decision-making responsibility for claims is identifiable.
- `B15.04.04.F04` **ReconciliationExceptionVisible** — test: Unmatched or disputed funding records remain explicit.
- `B15.04.04.F05` **DataDisclosurePermitted** — test: Funding and person-level protected data obey applicable access constraints.

## Findings R01–R24 — drafted change, not satisfaction
The status below means the finding now has a concrete candidate design response. It does **not** mean the issue is resolved or accepted.
| Finding | Priority | Candidate EC CT3 additions | Candidate BC CT3 additions | Standing |
|---|---|---|---|---|
| R01 | HIGH | `E05.06.01`, `E05.06.02`, `E05.06.03`, `E05.06.04`, `E05.04.03` | `B01.05.04`, `B10.03.04` | Proposed / not tested |
| R02 | HIGH | `E02.01.05`, `E05.06.02`, `E05.06.03`, `E05.04.03`, `E05.03.04`, `E05.01.04` | `B10.03.04` | Proposed / not tested |
| R03 | HIGH | `E08.07.01`, `E08.07.02`, `E08.07.03`, `E08.07.04` | `B10.02.04`, `B11.03.04` | Proposed / not tested |
| R04 | HIGH | `E05.05.06`, `E08.08.01`, `E08.08.02`, `E08.08.03`, `E08.08.04` | `B10.02.04`, `B10.06.04`, `B11.03.04` | Proposed / not tested |
| R05 | HIGH | `E01.01.05`, `E01.02.04`, `E01.03.04`, `E01.04.05`, `E01.05.05`, `E01.03.05`, `E02.01.04`, `E02.04.04`, `E02.04.05`, `E04.01.04`, `E05.05.05` | — | Proposed / not tested |
| R06 | HIGH | `E01.01.04`, `E03.04.04`, `E06.07.01`, `E06.07.02`, `E06.07.03`, `E06.06.05`, `E07.02.05`, `E07.04.04`, `E07.04.05` | `B07.04.04`, `B10.07.04` | Proposed / not tested |
| R07 | HIGH | `E06.01.04`, `E06.07.01`, `E06.07.02`, `E06.07.03`, `E06.03.04`, `E06.06.04`, `E06.06.05` | `B05.03.04`, `B07.04.04` | Proposed / not tested |
| R08 | HIGH | `E06.02.04`, `E06.02.05`, `E06.03.03`, `E06.03.04` | — | Proposed / not tested |
| R09 | HIGH | `E01.02.04`, `E01.03.04`, `E01.05.05`, `E02.01.04`, `E02.02.04`, `E02.02.05`, `E03.03.04`, `E04.01.03`, `E04.03.03`, `E04.04.04`, `E04.01.04`, `E07.03.04`, `E07.05.06`, `E09.01.04` | `B02.01.04`, `B10.07.04` | Proposed / not tested |
| R10 | MEDIUM | `E02.03.04`, `E02.05.03` | — | Proposed / not tested |
| R11 | HIGH | `E07.01.06`, `E07.02.05`, `E07.02.06`, `E07.03.04`, `E07.04.04`, `E07.02.07`, `E07.05.06` | `B01.05.04`, `B02.01.04`, `B14.06.04` | Proposed / not tested |
| R12 | MEDIUM | `E09.03.04`, `E09.05.04`, `E09.02.04`, `E09.03.05` | `B15.04.04` | Proposed / not tested |
| R13 | HIGH | `E03.02.04`, `E03.03.04`, `E03.05.04`, `E04.01.03`, `E04.02.04`, `E04.03.03`, `E04.04.04`, `E04.05.04`, `E04.05.05` | — | Proposed / not tested |
| R14 | MEDIUM | `E07.01.07`, `E08.07.01`, `E08.08.01`, `E08.02.04`, `E08.02.05` | — | Proposed / not tested |
| R15 | MEDIUM | `E07.05.05`, `E08.07.04`, `E08.04.04`, `E09.04.03` | `B14.06.04` | Proposed / not tested |
| R16 | MEDIUM | `E01.01.04`, `E03.01.04`, `E03.03.05`, `E03.04.04`, `E03.02.05` | `B04.04.04`, `B05.03.04` | Proposed / not tested |
| R17 | MEDIUM | `E06.05.04` | — | Proposed / not tested |
| R18 | HIGH | `E02.02.04`, `E02.04.04`, `E02.04.05`, `E03.02.05`, `E06.01.04`, `E07.01.06`, `E07.05.05`, `E07.04.05`, `E08.04.04`, `E09.04.04` | `B11.05.04` | Proposed / not tested |
| R19 | HIGH | — | — | Proposed / not tested |
| R20 | HIGH | `E01.01.05`, `E01.05.04`, `E01.03.05` | — | Proposed / not tested |
| R21 | MEDIUM | `E02.03.04`, `E06.07.01`, `E06.04.04`, `E08.06.04` | `B12.04.04` | Proposed / not tested |
| R22 | MEDIUM | `E02.01.05`, `E05.06.01`, `E05.06.04`, `E05.04.03`, `E05.03.04`, `E05.02.03`, `E05.01.04`, `E05.05.05`, `E07.02.06` | `B08.03.04`, `B10.03.04`, `B14.06.04` | Proposed / not tested |
| R23 | MEDIUM | `E04.05.05`, `E05.05.06`, `E08.08.03`, `E08.08.04`, `E09.01.04`, `E09.02.03`, `E09.03.04`, `E09.05.04`, `E09.02.04` | `B10.06.04`, `B13.04.04` | Proposed / not tested |
| R24 | MEDIUM | `E01.04.05`, `E01.03.05`, `E02.05.03`, `E06.06.04`, `E07.01.07`, `E08.06.04`, `E08.01.04` | `B12.04.04` | Proposed / not tested |

**R19 is a governance and migration identity-preservation control**, not an omitted consumer capability. It is met *in this candidate construction* by copying every original Candidate 006 element unchanged and allocating no governed UUIDs. It must later be verified independently against the Continuum during migration. No synthetic “Preserve UUID Capability” has been invented.

## Business-to-Enterprise reconciliation
The 353 Candidate 011 CT2 contribution hypotheses are preserved *unchanged*. This iteration adds 242 proposed links from new Enterprise CT3s to applicable Business CT2 demand and 48 review links from added Business CT3s to Enterprise CT2s. These are not accepted ArchiMate relationships and cannot prove Feature satisfaction.

**Interpretation:** Several digital capabilities legitimately contribute to a Business Capability, and Business outcomes can depend on enterprise services delivered outside Harmonia. The crosswalk files are evidence-backed review candidates, not automatic coverage certificates.

## Journeys and independent integrity tests
The 13 Stakeholders, 11 reusable Journey definitions, 29 Journeys and 179 stage hypotheses from Candidate 003/011 are retained. A supplementary stage register lists additional Enterprise CT3s that *may* be relevant to a particular stage based on its already-proposed Business CT2 anchor. The stage associations are discovery candidates, not established value-stage coverage.

| Domain | Adverse probe | Critical assertion |
|---|---|---|
| E01 | Commit timeout after execution, compare stored version to uncertain response | No false positive commit; original version preserved; evidence resolves disposition |
| E02 | Node loss, restricted historic lookup, incomplete cache and search federation | Absence, denial and outage distinct; stable version references and search scope disclosed |
| E03 | MLLP late ACK, replay, native-FHIR preservation and external contract deviation | Transport receipt and durable acceptance independently evidenced |
| E04 | Concurrent terminology/model release with historic clinical content | Historic definitions resolve; unsupported updates gated; interpretation unambiguous |
| E05 | Policy revocation during in-flight distributed authorised action | Invalidated Passport cannot grant; policy, distribution and local enforcement independently traceable |
| E06 | Accept queue work, run Ergon, fail between ProcessSomething and Commit | Work survives; execution result is not commit; replay/reconstruction safe |
| E07 | Patient-to-participant context switch with uncommitted change and restricted source | No cross-context leakage; draft and committed state distinguishable |
| E08 | Request mixed authorised/unauthorised provenance evidence while log server fails | No partial export; immutable source history; log loss semantics correctly qualified |
| E09 | Produce population report from changed model and partially restricted data | Reproducible source versions; restrictions enforced; advisory output not given authority |

### Five-stage service-intent stress scenarios
The columns express distinguishable user intentions; N/A stages demonstrate that the model is **not mandatory**. `ProcessSomething` remains conceptual shorthand, not an ArchiMate process name or a literal Service name.
| Scenario | Business CT2 | Retrieve | Check | Update | ProcessSomething | Commit |
|---|---|---|---|---|---|---|
| Information correction | `B10.07` | `E02.01.04` | `E01.03.05` | `E01.02.04` | `E06.07.03` | `E01.01.04` |
| Clinical handover | `B05.03` | `E02.01.04` | `E07.05.04` | `E06.01.04` | `E06.02.04` | `E06.07.02` |
| External diagnostic receipt | `B04.04` | `E03.03.04` | `E03.05.04` | `E04.03.03` | `E03.01.04` | `E01.01.04` |
| Authorisation revocation | `B10.03` | `E05.06.02` | `E05.02.03` | `E05.06.01` | `E05.06.03` | `E05.04.03` |
| Dereferenced evidence export | `B10.06` | `E08.08.02` | `E05.05.05` | `N/A` | `E08.08.03` | `E08.08.04` |
| Participant supported decision | `B14.06` | `E07.01.06` | `E05.02.03` | `E07.02.05` | `E07.02.06` | `E01.01.04` |
| Accepted work after node loss | `B12.04` | `E06.07.01` | `E06.06.05` | `E06.06.04` | `E06.02.04` | `E06.07.02` |
| Research disclosure | `B13.04` | `E09.01.04` | `E09.05.03` | `N/A` | `E08.08.03` | `E08.08.04` |

- **Information correction:** A prepared correction, business processing and durable commit have separately observable outcomes.
- **Clinical handover:** Receiving clinical responsibility acceptance must not be inferred from a technical receipt or data commit.
- **External diagnostic receipt:** External receipt, model validity and authoritative information acceptance have distinct proofs.
- **Authorisation revocation:** The change in authoritative policy and the cessation of operational grants must both be evidenced.
- **Dereferenced evidence export:** No write is needed to assemble a report; controlled release is distinct from commit of an IU version.
- **Participant supported decision:** A represented decision does not suppress participant preference or turn AI advice into authority.
- **Accepted work after node loss:** Recover accepted obligation; never silently resume inside a failed Ergon or claim commit from execution.
- **Research disclosure:** Purpose restrictions and complete-set authorisation are checked before evidence release; analysis need not mutate source.

## Source and evidence register
Evidence keys are *context references*, not proof that a source literally named each candidate feature. Definitive semantic correspondence with individual Continuum elements remains a separate review activity. All sources are read-only references to the pinned repository snapshot.
| Key | Architectural intent | Source |
|---|---|---|
| `IU` | Information Unit primacy, identity, metadata, versioning, disclosure and custody | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) |
| `REF` | Topic and InformationCoordinates, reference semantics and secure evaluation | [DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Topic-Identity-InformationCoordinates-and-Information-Security.001.md) |
| `MN` | GovernedReader, GovernedWriter, GovernedSearch and interaction semantics | [DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-to-Mnemosyne-Interaction.draft.002.md) |
| `MM` | Governed read/write/search, authoritative persistence boundary and uncertain outcomes | [DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Mneme-to-Mnemosyne.draft.002.md) |
| `PERSIST` | GovernedPersistenceFramework and independently provided persistence authority | [DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Authoritative-Persistence-Service-Model.draft.002.md) |
| `RES` | Authoritative persistence recovery, availability and failure qualification | [DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Availability-Resilience-Disaster-Recovery.draft.002.md) |
| `DIST` | Distributed and partitioned persistence without semantic identity loss | [DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mnemosyne-Data-Persistence-Sharding.draft.002.md) |
| `SEC` | Governed access, contextualisation as disclosure, local authority | [DRP-Mneme-Security.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Security.draft.002.md) |
| `TH` | Policy definition, RequiredCriteria, Passport lifecycle and local PEP | [DRP-Themis-Policy-Passport-Enforcement.draft.001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Themis-Policy-Passport-Enforcement.draft.001.md) |
| `PROV` | Immutable provenance, chronological evidence, dereferenced export | [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) |
| `AUD` | Audit contribution distinct from authoritative AuditEvent ownership | [DRP-Mneme-Audit.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Audit.draft.002.md) |
| `LOG` | In-JVM satellite, rsyslog Log-Server, separate Log-Persistence, receipt ACK semantics | [DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Harmonia-Logging-Middleware-Strategy-rsyslog.md) |
| `PRAGMA` | Pragma identity, Input/Output and progression separate from parcel state | [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) |
| `PRAGMATON` | Active work context distinguishable from authoritative durable information | [DRP-Mneme-Pragmaton-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Model.draft.002.md) |
| `WORK` | Work execution receives and returns context without owning persistence | [DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Pragmaton.draft.002.md) |
| `RES-W` | Work reconstruction; never resume inside a failed Ergon | [DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragmaton-Availability-Resilience-Recovery.draft.002.md) |
| `PARCEL` | Durable work acceptance separate from authoritative IU persistence | [DRP-Interaction-Petasos-to-Mneme.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Petasos-to-Mneme.draft.002.md) |
| `RESULT` | Adhoc governed information acquisition and result handling | [DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Interaction-Ergon-to-Mneme-Adhoc-Information-Access.draft.002.md) |
| `ASSURE` | Assurance and evidence boundaries; dereferencing owned by Kleio/Dokimasia | [DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Logging-AuditEvent-Provenance-Governance-and-Export.md) |
| `EXCH` | Exchange use of governed native and mapped information representations | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) |
| `MODEL` | Interpretation and model-independent IU semantics; Calliope separation | [DRP-Information-Unit-Framework.draft.003.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Information-Unit-Framework.draft.003.md) |
| `OPS` | Operations, observability and dependency health constraints | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) |
| `NFR` | NFR-derived Motivation obligations and quality constraints | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) |
| `TIME` | Temporal, scheduling and time-coordinate obligations | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) |
| `PRESENT` | User experience, source, currency and presentation constraints | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) |
| `INSIGHT` | Governed information, reporting and assurance constraints | [Harmoni-NFR-Derived Architetural Elements - Iteration 001.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/Harmoni-NFR-Derived%20Architetural%20Elements%20-%20Iteration%20001.md) |
| `JOURNEY` | Candidate 003 Stakeholder and Journey demand (candidate; not an accepted DRP) | Stakeholder/Journey Candidate 003 (conversation artifact) |
| `TASK` | Task envelope and work semantics, without a complete task service specification | [DRP-Mneme-Pragma-Model.draft.002.md](https://github.com/fhirfactory/harmonia/blob/fecb465cab9c8c5f6e9a6035a1f7c6780e7ca374/enterprise-architecture/design/offline-design/DRP-Mneme-Pragma-Model.draft.002.md) |

## Preservation, non-equivalence, and limits
- Business clinical privilege ≠ machine authentication; human clinical handover acceptance ≠ technical receipt; specimen transport ≠ PetasosParcel routing; disability participant goals ≠ clinical patient state.
- Operational logging ≠ immutable AuditEvent ≠ chronological Provenance ≠ assurance observations. Shared infrastructure technologies do not imply shared service owner or persistence instance.
- Mnemosyne authoritative durability ≠ Mneme active working information ≠ Petasos work acceptance ≠ Ergon execution completion. An indeterminate outcome cannot be promoted to success.
- InformationUnit is a foundational governed information concept; its metadata structure is not a universal subsystem-specific API. Larger media remains reference-governed, not relationally stored by default.
- Themis Passport is derived operational authorisation, not credentials. Authoritative policy lives independently from operational distribution and PEP enforcement.
- No new named Application Components, canonical UUIDs, Application Services, formal Home relationships or implementation targets have been created by Candidate 012.
- Proposed new CT2s and Feature names may overlap existing capabilities or finer services: review *semantic equivalence and value* before adopting them. Broad scope is intentional; claims of completeness or conformance are not.
- The full 147 existing Service identities and 174 candidate Services in Candidate 010 remain a separate, unmodified service-design input. They must be revisited after CT3/Feature reconciliation rather than mechanically multiplied by 495 new Features.

## Suggested design acceptance test (not migration authorisation)
Review whether every new item has a meaningful consumer value, a distinct testable aspect, plausible business demand and source provenance; whether explicit DRP contradictions remain visible; whether important Stakeholder Journey stages can fail independently without being marked “covered”; and whether the Enterprise structure remains solution-independent. **Only the Solution Architect may accept or amend the proposed architecture.**