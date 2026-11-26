/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Collection;
import java.util.Collections;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.naming.Topic;

/**
 * The security requirements that travel with an InformationUnit.
 *
 * <p>Each requirement is a qualified Topic naming a security criterion (for
 * example a sensitivity or handling label). Calliope stores and carries these
 * requirements without deciding their policy meaning. Themis-Krites is the
 * external authority that adjudicates every proposed change, including
 * strengthening, weakening and replacement. This value is immutable, but it
 * does not impose an add-only lifecycle rule (Draft 002.005 §1.4).</p>
 *
 * <p>Security requirements are independent of coordinates and of quality.
 * An empty set is the initialised default and means "no IU-specific
 * requirement recorded", not "unrestricted": the default-deny decision still
 * belongs to Themis.</p>
 */
public final class SecurityMetadata {
    private static final SecurityMetadata NONE = new SecurityMetadata(Collections.emptyNavigableSet());

    private final NavigableSet<Topic> requirements;

    private SecurityMetadata(NavigableSet<Topic> requirements) {
        this.requirements = requirements;
    }

    /**
     * Returns the initialised value with no recorded requirements.
     *
     * @return the initialised value with no recorded requirements
     */
    public static SecurityMetadata none() {
        return NONE;
    }

    /**
     * Creates security metadata from a collection of requirement Topics.
     *
     * @param requirements requirement Topics; may be empty, no null elements
     * @return an immutable value independent of the supplied collection
     * @throws InformationModelException {@code MISSING_INPUT} for null input or element;
     *         {@code INCONSISTENT_INPUT} when the same requirement appears with different versions
     */
    public static SecurityMetadata of(Collection<Topic> requirements) {
        ModelChecks.required(requirements, "security.requirements");
        return NONE.withAdded(requirements);
    }

    /**
     * Returns a new proposal containing these requirements plus the additional ones.
     *
     * <p>Re-stating an existing requirement with the same entry versions is a
     * no-op. A second version of the same Topic identity in one value is
     * structurally ambiguous and is rejected; replacement should be expressed
     * as a separate proposed value for Krites to adjudicate.</p>
     *
     * @param additional requirement Topics to add
     * @return a new immutable proposal; this value is unchanged
     * @throws InformationModelException {@code MISSING_INPUT} for null input or element;
     *         {@code INCONSISTENT_INPUT} for a conflicting version of an existing requirement
     */
    public SecurityMetadata withAdded(Collection<Topic> additional) {
        ModelChecks.required(additional, "security.requirements");
        TreeSet<Topic> combined = new TreeSet<>(requirements);
        for (Topic requirement : additional) {
            ModelChecks.required(requirement, "security.requirements");
            Topic existing = combined.ceiling(requirement);
            if (existing != null && existing.equals(requirement)) {
                if (!ModelChecks.sameVersionedTopic(existing, requirement)) {
                    throw InformationModelException.of(InformationFailureCategory.INCONSISTENT_INPUT,
                            "security.requirements");
                }
            } else {
                combined.add(requirement);
            }
        }
        return new SecurityMetadata(Collections.unmodifiableNavigableSet(combined));
    }

    /**
     * Returns a new proposal with the specified existing requirements removed.
     *
     * <p>This operation does not determine whether removal is permitted. The
     * caller must submit the resulting proposal to Themis-Krites for
     * adjudication. Each target must match both Topic identity and its entry
     * versions when the target identity exists, so an accidental version
     * mismatch cannot silently remove a different requirement.</p>
     *
     * @param removed requirements to remove
     * @return a new immutable proposal; this value is unchanged
     * @throws InformationModelException {@code MISSING_INPUT} for null input or element;
     *         {@code INCONSISTENT_INPUT} when a target has the same identity but a different version
     */
    public SecurityMetadata withRemoved(Collection<Topic> removed) {
        ModelChecks.required(removed, "security.requirements");
        TreeSet<Topic> remaining = new TreeSet<>(requirements);
        for (Topic requirement : removed) {
            ModelChecks.required(requirement, "security.requirements");
            Topic existing = remaining.ceiling(requirement);
            if (existing != null && existing.equals(requirement)) {
                if (!ModelChecks.sameVersionedTopic(existing, requirement)) {
                    throw InformationModelException.of(InformationFailureCategory.INCONSISTENT_INPUT,
                            "security.requirements");
                }
                remaining.remove(existing);
            }
        }
        return new SecurityMetadata(Collections.unmodifiableNavigableSet(remaining));
    }

    /**
     * Returns the unmodifiable, ordered set of requirement Topics.
     *
     * @return the unmodifiable, ordered set of requirement Topics
     */
    public Set<Topic> getRequirements() {
        return requirements;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof SecurityMetadata that) || requirements.size() != that.requirements.size()) {
            return false;
        }
        for (Topic requirement : requirements) {
            Topic counterpart = that.requirements.contains(requirement) ? that.requirements.ceiling(requirement) : null;
            if (!ModelChecks.sameVersionedTopic(requirement, counterpart)) {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode() {
        return requirements.hashCode();
    }

    /** Shows the count only; requirement labels can reveal sensitivity. */
    @Override
    public String toString() {
        return "SecurityMetadata{requirements=" + requirements.size() + "}";
    }
}
