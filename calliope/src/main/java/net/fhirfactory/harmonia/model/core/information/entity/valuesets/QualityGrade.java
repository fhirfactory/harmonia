/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

/**
 * Release-defined qualitative assessment of an InformationUnit's quality.
 *
 * <p>The Integration Architect defines the grading criteria for each Harmonia
 * software release; Calliope only carries the assigned grade. A grade is
 * independent of authoritative standing (AX-06), Themis authorisation,
 * integrity checks, cache state and durable persistence. In particular,
 * {@link #CANONICAL} does not mean "the authoritative copy".</p>
 */
public enum QualityGrade {
    /** Information has not been assured beyond its reported origin. */
    ANECDOTAL,
    /** Information has some supporting assurance but does not meet canonical criteria. */
    INFORMATIONAL,
    /** Information meets the applicable release's canonical quality criteria. */
    CANONICAL
}
