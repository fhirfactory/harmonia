/*
 * Copyright (c) 2026 Mark Hunter
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.alignment;

import org.hl7.fhir.instance.model.api.IBaseResource;

/**
 * Boundary contract for evaluating semantic relationships between FHIR resource instances
 * and performing fluent, destructive merges of compatible variances.
 * <p>
 * Calliope reports the semantic relationship between resource instances via {@link AlignmentAssessment};
 * Mneme determines the processing consequences.
 * <p>
 * Implementations of {@link #mergeResource(IBaseResource, IBaseResource)} must apply compatible variance
 * destructively and directly in-place to {@code currentActiveResource}, returning the identical
 * reference instance ({@code merged == currentActiveResource}).
 */
public interface ResourceAlignmentPort {

    /**
     * Evaluates the semantic relationship between the current active resource and a variant resource.
     *
     * @param currentActiveResource the current active representation
     * @param variantResource the variant representation to compare
     * @param <T> the resource type
     * @return the relationship-oriented alignment assessment
     */
    <T extends IBaseResource> AlignmentAssessment checkAlignment(T currentActiveResource, T variantResource);

    /**
     * Applies compatible variance from {@code variantResource} destructively and in-place into
     * {@code currentActiveResource}, returning the identical reference.
     *
     * @param currentActiveResource the current active resource being mutated in place
     * @param variantResource the variant resource providing compatible variance
     * @param <T> the resource type
     * @return the exact same updated {@code currentActiveResource} instance
     */
    <T extends IBaseResource> T mergeResource(T currentActiveResource, T variantResource);
}
