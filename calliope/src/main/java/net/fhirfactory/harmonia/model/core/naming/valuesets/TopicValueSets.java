/*
 * Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later
 */

package net.fhirfactory.harmonia.model.core.naming.valuesets;

/** Canonical qualifier names and default domain used by the Topic naming model. */
public final class TopicValueSets {

    public static final String DEFAULT_DOMAIN = "Health";
    public static final String QUALIFIER_DOMAIN = "Domain";
    public static final String QUALIFIER_MODEL = "Model";
    public static final String QUALIFIER_MODEL_VERSION = "ModelVersion";
    public static final String QUALIFIER_DATA_ELEMENT = "DataElement";
    public static final String QUALIFIER_DATA_ELEMENT_QUALIFIER = "DataElementQualifier";

    private TopicValueSets() {
    }
}