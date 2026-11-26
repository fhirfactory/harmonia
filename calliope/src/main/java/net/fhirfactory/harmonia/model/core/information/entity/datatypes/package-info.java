/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Immutable constituent values of a Calliope InformationUnit.
 *
 * <p>Draft 002.005 §1 composes the InformationUnit from three constituents,
 * all modelled here as dependent values that have no identity of their own:</p>
 * <pre>
 * InformationQuality          owner, grade, certifications (lifetime-immutable)
 * InformationMetadata         PersistenceLifecycleMetadata, CacheLifecycleMetadata,
 *                             OriginMetadata, InformationCoordinates, SecurityMetadata,
 *                             Relationships, informationAuthority
 * InformationRepresentation   ContainerMetadata, ContentMetadata (+ ContentDetail), Content
 * </pre>
 *
 * <p>Every type is immutable and safe to share between threads. Collections
 * and byte arrays are copied on input and never exposed mutably. Controlled
 * changes return new values and enforce their structural rules (establish-once
 * origin and non-regressing Managed Revision). Local revisions retain the
 * originating Managed Revision as the expected next-write revision. Security
 * values are immutable, while Themis-Krites adjudicates proposed changes
 * outside Calliope. Every rejection raises {@link
 * net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationModelException}
 * before any state changes, and neither exceptions nor {@code toString()}
 * reveal payloads, locators, descriptions or other potentially sensitive
 * values.</p>
 *
 * <p>The package depends only on the JDK, Calliope Naming and the sibling
 * {@code valuesets} package. It deliberately contains no persistence, cache,
 * authorisation, reference-resolution or serialisation behaviour: those
 * belong to Mnemosyne, Mneme, Themis and the interchange contracts.</p>
 */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;
