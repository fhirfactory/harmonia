/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

/**
 * Identifies the kind of referent named by an InformationUnit relationship.
 *
 * <p>The value describes the target boundary only. It does not validate remote
 * existence, grant access, or make the model a relationship-graph manager.
 * Mneme validates local references where applicable; remote references are
 * preserved exactly as supplied (Draft 002.005 §1.4).</p>
 */
public enum RelationshipType {
    /** A resource managed by an external information system. */
    EXTERNAL_SYSTEM,
    /** An external organisation or organisational entity. */
    EXTERNAL_ORGANISATION,
    /** Another InformationUnit, identified by its UUID URN, never by its name. */
    INTERNAL_IU,
    /** Another Harmonia-managed referent that is not an InformationUnit. */
    INTERNAL_OTHER
}
