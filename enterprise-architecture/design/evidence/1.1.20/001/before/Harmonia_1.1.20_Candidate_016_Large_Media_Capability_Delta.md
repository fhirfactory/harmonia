# Harmonia 1.1.20 — Candidate 016: Large-Media Management & Reference Coverage

**Purpose:** Restore the complete Sylloge-derived binary-content responsibilities into the capability layer, without allowing an implementation component to become the capability taxonomy or altering any existing Candidate 015 definitions.

**Decision status:** New architectural candidate only; no repository modification or governance acceptance.

## Existing Candidate 015 coverage

- `E01.05.01` — Governed Reference Resolution
- `E01.05.02` — Large Content Access by Reference
- `E01.05.03` — Long-Lived Reference Validity
- `E01.05.04` — External Media Reference Governance
- `E02.04.03` — Referenced Information Sharing

**Gap:** These do not independently express the broad Sylloge responsibility for binary registration/metadata, IU associations, physical custody boundaries, acquisition, assembly, lifecycle and technical service integration.

## Proposed Enterprise Capability addition

### E01.06 — Governed Binary & Large-Media Content Management **[NEW C016]**
Manage the identity, metadata, information associations, custody arrangements, resolution, acquisition, retrieval, assembly and lifecycle of formally managed binary or large-media content. This is an **Enterprise Capability**, not a synonym for Sylloge or a storage product. Existing reference and distribution capabilities remain intact.

**Provenance:** Existing Continuum `Harmonia.Sylloge` (canonical component `feea742463b84021b66853d287d8916f`), Domain05 U17 / F31 / D15, and NFR-087; Candidate 015 `E01.05.01`–`E01.05.05` and `E02.04.03`. **Standing:** Candidate 016 proactive completeness expansion, not incorporated into the governed Continuum.

**Business enablement hypotheses:** B10.01 Health Record Stewardship, B10.02 Information Completeness & Provenance Accountability, B10.03 Appropriate Information Access & Sharing, B10.06 Retention, Disclosure & Record Accountability, B10.07 Information Correction & Reconciliation. Additional clinical demand is available through LHR, HIE, diagnostics and provider-directory service journeys; detailed links require review.

**Boundary:** A binary payload, its metadata, the Information Unit that references it, and its physical custodian are distinct. Harmonia may manage a reference to externally held content without copying the bytes. Where Harmonia has custody, suitable media storage is used rather than assuming ordinary IU/database persistence. Sylloge remains the established application responsibility; Mneme/Mnemosyne remain the governed information and persistence boundaries. Agora-native collaboration media remains distinct. References are not automatically publicly dereferenceable.

**Technology interfaces:** ICT Foundation lenses 09 (Platform & Middleware), 11 (Storage & Persistence), 12 (Network & Connectivity), 15 (Observability & Operational Management), 16 (Resilience & Continuity). This does not select object storage, PACS/VNA, DICOM representation, FHIR Binary/DocumentReference operation, or any specific media-server product.

**Release note:** NFR-087 states a *defined size threshold*; the historically discussed 64 KiB boundary should be checked against the exact currently governed release policy rather than silently hardened as a new universally fixed threshold.

#### E01.06.01 — Binary Content Registration & Description **[NEW C016]**
Register content metadata and maintain the information necessary to identify and govern an associated binary payload, without equating the payload with an Information Unit.

- **`E01.06.01.F01` — DescribedContentIdentity:** A registered payload or externally held payload can be distinguished from the associated Information Unit and from other payloads. *PROPOSED — unverified*.
- **`E01.06.01.F02` — ContentFormatDeclared:** The available type, format and representation details are recorded accurately where supplied or established. *PROPOSED — unverified*.
- **`E01.06.01.F03` — ContentProvenanceRetained:** The content registration preserves available source, origin and provenance references. *PROPOSED — unverified*.
- **`E01.06.01.F04` — DescriptiveMetadataBound:** Descriptive metadata can be retrieved without retrieving the full binary content. *PROPOSED — unverified*.
- **`E01.06.01.F05` — RegistrationExceptionsExplicit:** Unsupported, incomplete or inconsistent registration requests have explicit outcomes. *PROPOSED — unverified*.

#### E01.06.02 — Information Unit–Binary Association Management **[NEW C016]**
Associate logical Information Units with zero or more supporting binary payloads while preserving the distinct identity, version and lifecycle semantics of both.

- **`E01.06.02.F01` — AssociationTargetCorrect:** A governed Information Unit association resolves to the intended binary content identity. *PROPOSED — unverified*.
- **`E01.06.02.F02` — MultiplePayloadAssociation:** An information item may explicitly reference multiple associated payloads where its defined semantics require them. *PROPOSED — unverified*.
- **`E01.06.02.F03` — VersionAssociationTraceable:** An association identifies the applicable logical information version and appropriate binary-content version or standing. *PROPOSED — unverified*.
- **`E01.06.02.F04` — RelationshipChangeGoverned:** Creating, changing or retiring an association is authorised and attributable. *PROPOSED — unverified*.
- **`E01.06.02.F05` — AssociationIntegrityDetectable:** Missing, mismatched or contradictory payload associations are detected or explicitly reported. *PROPOSED — unverified*.

#### E01.06.03 — Binary Custody & Source Location Management **[NEW C016]**
Determine which party or technical service holds physical custody and how content can be resolved while preserving external source authority and storage independence.

- **`E01.06.03.F01` — CustodyKindKnown:** Harmonia-managed and externally managed physical custody are distinguishable. *PROPOSED — unverified*.
- **`E01.06.03.F02` — ExternalContentNoForcedCopy:** A valid externally managed item can be governed and referenced without requiring automatic replication into Harmonia. *PROPOSED — unverified*.
- **`E01.06.03.F03` — PhysicalLocationIndependent:** Logical references remain stable through authorised storage-provider or physical-location changes. *PROPOSED — unverified*.
- **`E01.06.03.F04` — CustodyAvailabilityQualified:** Provider unavailability, known absence and broken reference are reported as distinct cases. *PROPOSED — unverified*.
- **`E01.06.03.F05` — SourceAuthorityPreserved:** Binary-content management does not imply ownership of originating clinical facts or the external source repository. *PROPOSED — unverified*.

#### E01.06.04 — Large-Content Ingestion & Custody Establishment **[NEW C016]**
Accept or register large content through appropriate governed custody arrangements without using ordinary structured Information Unit persistence for bulk media bytes.

- **`E01.06.04.F01` — ThresholdPolicyApplied:** Content above the release-defined large-content threshold follows the designated media path. *PROPOSED — unverified*.
- **`E01.06.04.F02` — ReferenceCreatedOnAcceptedIngest:** Where physical custody is accepted, a governed content reference is established and correlated to the accepted payload. *PROPOSED — unverified*.
- **`E01.06.04.F03` — IngestOutcomeUnambiguous:** Accepted, rejected, incomplete and indeterminate acquisition or storage outcomes are distinguishable. *PROPOSED — unverified*.
- **`E01.06.04.F04` — PayloadByteIntegrityChecked:** Accepted physical content can be verified against the applicable integrity evidence. *PROPOSED — unverified*.
- **`E01.06.04.F05` — UncommittedMediaNotPublished:** Uncertain or incomplete content capture is not misrepresented as authoritative available content. *PROPOSED — unverified*.

#### E01.06.05 — Governed Binary Retrieval & Delivery **[NEW C016]**
Supply authorised binary content through bounded access semantics without requiring every consumer or information exchange to carry the payload in-band.

- **`E01.06.05.F01` — AuthorisedRetrievalOnly:** Permitted and denied retrievals respect the applicable effective identity and information-protection rules. *PROPOSED — unverified*.
- **`E01.06.05.F02` — ReferenceBasedAcquisition:** Consumers can acquire the content through a governed reference independently of Information Unit structural data retrieval. *PROPOSED — unverified*.
- **`E01.06.05.F03` — ResourceBoundedTransfer:** Large-content retrieval can be delivered without unbounded application memory demand under a stated performance contract. *PROPOSED — unverified*.
- **`E01.06.05.F04` — AvailabilityFailureQualified:** Missing, inaccessible, corrupted and temporarily unavailable content produce distinguishable outcomes. *PROPOSED — unverified*.
- **`E01.06.05.F05` — TransferIntegrityAssessed:** Received bytes can be verified against the expected governed content identity and integrity evidence. *PROPOSED — unverified*.

#### E01.06.06 — On-Demand Binary Assembly & Presentation **[NEW C016]**
Resolve and assemble the logical content required by a consumer from one or more governed binary payloads when authorised and required.

- **`E01.06.06.F01` — AssemblySourcesTraceable:** The payload sources and transformations contributing to assembled content remain attributable. *PROPOSED — unverified*.
- **`E01.06.06.F02` — MultiPartAssemblyCorrect:** Supported multi-payload content is assembled in the defined order and structure. *PROPOSED — unverified*.
- **`E01.06.06.F03` — SourceAuthorityNotRewritten:** Assembly does not silently change the authoritative status of source information. *PROPOSED — unverified*.
- **`E01.06.06.F04` — PartialAssemblyNotMisrepresented:** Missing or rejected assembly components do not produce a falsely complete result. *PROPOSED — unverified*.
- **`E01.06.06.F05` — AssemblyDisclosureConstrained:** An assembled result is released only when all applicable disclosure obligations are satisfied. *PROPOSED — unverified*.

#### E01.06.07 — Binary Content Lifecycle & Reference Continuity **[NEW C016]**
Maintain consistency of content references, retention and availability across binary-content lifecycle events, without prescribing a particular storage engine.

- **`E01.06.07.F01` — MediaRetentionRespected:** Applicable retention constraints and holds are preserved by the managed content arrangement. *PROPOSED — unverified*.
- **`E01.06.07.F02` — MediaReferenceRetained:** Valid media references remain resolvable or explicitly dispositioned across archive, relocation and provider changes. *PROPOSED — unverified*.
- **`E01.06.07.F03` — MediaVersionSemanticsExplicit:** Replacement, new representation and version changes do not silently rewrite prior source identity. *PROPOSED — unverified*.
- **`E01.06.07.F04` — MediaRetirementGoverned:** Content retirement and deletion are prohibited when retention or external custody constraints disallow them. *PROPOSED — unverified*.
- **`E01.06.07.F05` — MediaRecoveryAccountable:** Recovery/restoration exercises verify consistency between metadata, governed references and available physical payloads. *PROPOSED — unverified*.

#### E01.06.08 — Binary Content Protection & Operational Integration **[NEW C016]**
Apply security and operational obligations to managed binary content and integrate with fit-for-purpose technical storage and external custodians without treating the platform service as the capability itself.

- **`E01.06.08.F01` — ProtectionContextCarried:** Information labels, relevant authorisation context and disclosure constraints accompany applicable media access. *PROPOSED — unverified*.
- **`E01.06.08.F02` — MediaAccessAuditable:** Material retrieval, disclosure and governed lifecycle changes are represented in the applicable audit/provenance framework. *PROPOSED — unverified*.
- **`E01.06.08.F03` — PlatformStorageIntegrationQualified:** Unavailable, degraded and restored media providers are observable through explicit application/technical status interfaces. *PROPOSED — unverified*.
- **`E01.06.08.F04` — NoGenericDatabaseFallback:** Large media is not silently redirected to ordinary structured Information Unit persistence when its configured media facility is unavailable. *PROPOSED — unverified*.
- **`E01.06.08.F05` — TechnicalSubstitutionIndependent:** Changing the underlying repository or storage technology does not redefine governed media identities or consumer contract semantics. *PROPOSED — unverified*.

**Non-duplication:** E01.05 remains the general capability to manage and resolve governed references, and E02.04.03 remains authorised reference-based information sharing. E01.06 covers **the governed binary content itself and its association/custody lifecycle**. E05 retains cross-cutting identity/security obligations; E03 governs whichever explicit external API/media contracts are selected.

**Review scenarios:** external PACS or source unavailable but logical reference retained; metadata exists but binary missing; multipart document assembly missing one part; caller can retrieve IU metadata but not binary bytes; provider storage moves without a logical reference change; content exceeds media threshold; media capture fails after partial transfer; authorised historical content requested after archive; malicious substitution/corruption detected; storage unavailable with no fallback to relational IU store.
