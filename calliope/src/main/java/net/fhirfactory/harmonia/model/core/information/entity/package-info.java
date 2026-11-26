/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
/**
 * The Calliope InformationUnit model (BL-0005, Draft 002.007 §1).
 *
 * <p>{@link net.fhirfactory.harmonia.model.core.information.entity.InformationUnit}
 * is Harmonia's atomic, individually governed unit of information, built with
 * {@link net.fhirfactory.harmonia.model.core.information.entity.InformationUnitBuilder}.
 * Its two successor operations distinguish a complete clone from a caller-built
 * minimum-metadata derivation through
 * {@link net.fhirfactory.harmonia.model.core.information.entity.MinimumMetadataSuccessorBuilder}.
 * Its constituents live in {@code datatypes}, closed value sets in
 * {@code valuesets}, and governed vocabulary builders in {@code factory}.</p>
 *
 * <p>Package dependencies are one-way:
 * {@code valuesets <- datatypes <- factory}, and this root package may use all
 * three. Every package depends only on the JDK and Calliope Naming. The model
 * is a shared, standalone library: it performs no persistence, caching,
 * authorisation, reference resolution, routing, logging or serialisation.</p>
 */
package net.fhirfactory.harmonia.model.core.information.entity;
