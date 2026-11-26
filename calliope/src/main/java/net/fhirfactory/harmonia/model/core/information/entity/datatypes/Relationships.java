/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;

/**
 * The optional internal and external semantic links held in InformationMetadata.
 *
 * <p>The single ordered {@code relationships} list carries every link type
 * (Draft 002.005 §1.4). It may be empty, which is the initialised default.
 * The creator or an authorised updater may supply or change links; Calliope
 * expresses that as a new immutable value and leaves authorisation to the
 * calling Harmonia component. Order is preserved and duplicates are kept as
 * supplied, because the model does not interpret relationship meaning.</p>
 */
public final class Relationships {
    private static final Relationships NONE = new Relationships(List.of());

    private final List<InformationUnitRelationship> relationships;

    private Relationships(List<InformationUnitRelationship> relationships) {
        this.relationships = relationships;
    }

    /**
     * Returns the initialised empty value.
     *
     * @return the initialised empty value
     */
    public static Relationships none() {
        return NONE;
    }

    /**
     * Creates an independent snapshot of the supplied links.
     *
     * @param relationships ordered links; may be empty, must not contain null
     * @return an immutable value unaffected by later changes to the supplied collection
     * @throws InformationModelException {@code MISSING_INPUT} for a null collection or element
     */
    public static Relationships of(Collection<InformationUnitRelationship> relationships) {
        ModelChecks.required(relationships, "relationships");
        List<InformationUnitRelationship> copy = new ArrayList<>(relationships.size());
        for (InformationUnitRelationship relationship : relationships) {
            if (relationship == null) {
                throw InformationModelException.of(InformationFailureCategory.MISSING_INPUT, "relationships");
            }
            copy.add(relationship);
        }
        return new Relationships(List.copyOf(copy));
    }

    /**
     * Returns a new value with one link appended.
     *
     * @param relationship link to append
     * @return a new value; this value is unchanged
     * @throws InformationModelException {@code MISSING_INPUT} when null
     */
    public Relationships with(InformationUnitRelationship relationship) {
        ModelChecks.required(relationship, "relationships");
        List<InformationUnitRelationship> copy = new ArrayList<>(relationships);
        copy.add(relationship);
        return new Relationships(List.copyOf(copy));
    }

    /**
     * Returns the unmodifiable ordered list of links.
     *
     * @return the unmodifiable ordered list of links
     */
    public List<InformationUnitRelationship> getRelationships() {
        return relationships;
    }

    @Override
    public boolean equals(Object other) {
        return this == other || other instanceof Relationships that && relationships.equals(that.relationships);
    }

    @Override
    public int hashCode() {
        return relationships.hashCode();
    }

    /** Shows the count only; targets can be sensitive. */
    @Override
    public String toString() {
        return "Relationships{count=" + relationships.size() + "}";
    }
}
