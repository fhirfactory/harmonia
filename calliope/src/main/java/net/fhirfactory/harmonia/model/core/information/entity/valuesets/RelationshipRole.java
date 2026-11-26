/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

/**
 * States the semantic role of a typed InformationUnit relationship.
 *
 * <p>Roles are caller-supplied descriptions. Calliope never infers a
 * relationship from Content, identity, or provenance, and never creates the
 * reciprocal link: if one is required it must be supplied explicitly.</p>
 */
public enum RelationshipRole {
    /** The target supplied a basis for this InformationUnit. */
    BASED_ON,
    /** The target is an alternate governed representation of the same information. */
    ALTERNATE_REPRESENTATION,
    /** The target is a larger whole of which this unit is a part. */
    PART_OF,
    /** The target is the source from which this unit was transformed or validated. */
    DERIVED_FROM
}
