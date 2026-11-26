/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * Release-defined builders for governed InformationUnit vocabulary Topics.
 *
 * <p>{@link net.fhirfactory.harmonia.model.core.information.entity.factory.InformationVocabulary}
 * supplies distinct qualified {@code RAW} container and content schemas and
 * FHIR R4 resource schema Topics using their versioned containment paths. It
 * also builds generic profile Topics beneath their own governing-standard
 * paths, without binding a profile to an underlying resource. Media types belong to dedicated metadata
 * fields and are not built as concepts or schemas. Callers needing other Topics
 * use the Calliope Naming API directly.</p>
 *
 * <p>This package depends on Naming, {@code valuesets} and {@code datatypes};
 * the root package depends on it, never the reverse.</p>
 */
package net.fhirfactory.harmonia.model.core.information.entity.factory;
