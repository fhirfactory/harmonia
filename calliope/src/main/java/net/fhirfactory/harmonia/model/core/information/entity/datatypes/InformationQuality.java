/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.datatypes;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.InformationFailureCategory;
import net.fhirfactory.harmonia.model.core.information.entity.valuesets.QualityGrade;

/**
 * The lifetime-immutable quality assessment of one InformationUnit.
 *
 * <p>Draft 002.005 §1.5 makes quality a distinct IU constituent with three
 * properties, all fixed for the IU's whole lifetime:</p>
 * <ul>
 *   <li>{@code owner} - the party responsible for this quality assessment.
 *       It is <em>not</em> {@code InformationCoordinates.InformationOwner},
 *       which describes governed logical ownership and may change by revision;</li>
 *   <li>{@code grade} - assigned under the Integration Architect's release
 *       criteria. {@code CANONICAL} is a quality grade, independent of
 *       authoritative standing (AX-06); the authority reference lives in
 *       {@link InformationMetadata#getInformationAuthority()};</li>
 *   <li>{@code certifications} - certifier to certification.</li>
 * </ul>
 *
 * <p>There are deliberately no "with" methods and the InformationUnit has no
 * quality-changing operation: a subsequent validation produces a new IU with
 * a new UUID and provenance linking it to the source IU. The certification map
 * is copied on input and exposed as an unmodifiable sorted view, so neither the
 * caller's original map nor the returned map can change this value.</p>
 */
public final class InformationQuality {
    private final String owner;
    private final QualityGrade grade;
    private final SortedMap<String, String> certifications;

    private InformationQuality(String owner, QualityGrade grade, SortedMap<String, String> certifications) {
        this.owner = owner;
        this.grade = grade;
        this.certifications = certifications;
    }

    /**
     * Creates a quality assessment.
     *
     * @param owner non-blank identifier of the party responsible for the assessment
     * @param grade assigned grade
     * @param certifications certifier-to-certification entries; may be empty, never null
     * @return an immutable value independent of the supplied map
     * @throws InformationModelException {@code MISSING_INPUT} for a null argument, key or value;
     *         {@code INVALID_INPUT} for a blank owner, certifier or certification
     */
    public static InformationQuality of(String owner, QualityGrade grade, Map<String, String> certifications) {
        ModelChecks.requiredText(owner, "quality.owner");
        ModelChecks.required(grade, "quality.grade");
        ModelChecks.required(certifications, "quality.certifications");
        SortedMap<String, String> copy = new TreeMap<>();
        for (Map.Entry<String, String> entry : certifications.entrySet()) {
            String certifier = entry.getKey();
            String certification = entry.getValue();
            if (certifier == null || certification == null) {
                throw InformationModelException.of(InformationFailureCategory.MISSING_INPUT,
                        "quality.certifications");
            }
            if (certifier.isBlank() || certification.isBlank()) {
                throw InformationModelException.of(InformationFailureCategory.INVALID_INPUT,
                        "quality.certifications");
            }
            copy.put(certifier, certification);
        }
        return new InformationQuality(owner, grade, Collections.unmodifiableSortedMap(copy));
    }

    /**
     * Returns the party responsible for the quality assessment.
     *
     * @return the party responsible for the quality assessment
     */
    public String getOwner() {
        return owner;
    }

    /**
     * Returns the assigned release-defined grade.
     *
     * @return the assigned release-defined grade
     */
    public QualityGrade getGrade() {
        return grade;
    }

    /**
     * Returns an unmodifiable, certifier-ordered view of the certifications.
     *
     * @return an unmodifiable, certifier-ordered view of the certifications
     */
    public Map<String, String> getCertifications() {
        return certifications;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof InformationQuality that)) {
            return false;
        }
        return owner.equals(that.owner) && grade == that.grade && certifications.equals(that.certifications);
    }

    @Override
    public int hashCode() {
        return Objects.hash(owner, grade, certifications);
    }

    /** Shows the grade and certification count; owner and certifier names are omitted. */
    @Override
    public String toString() {
        return "InformationQuality{grade=" + grade + ", certifications=" + certifications.size() + "}";
    }
}
