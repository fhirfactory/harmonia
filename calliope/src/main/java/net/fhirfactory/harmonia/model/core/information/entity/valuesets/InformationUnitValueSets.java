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

package net.fhirfactory.harmonia.model.core.information.entity.valuesets;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.factory.TopicFactory;

/** Canonical Topic and string values used by the Information Unit model. */
public final class InformationUnitValueSets {

    public static final Topic TYPE_FHIR_BUNDLE = TopicFactory.fromName("Harmonia.Container.FHIR.Bundle");
    public static final Topic TYPE_HL7_MESSAGE = TopicFactory.fromName("Harmonia.Container.HL7.Message");
    public static final Topic TYPE_DOCUMENT = TopicFactory.fromName("Harmonia.Container.Document");
    public static final Topic TYPE_PRAGMA = TopicFactory.fromName("Harmonia.Container.Pragma");
    public static final Topic TYPE_EVENT = TopicFactory.fromName("Harmonia.Container.Event");

    public static final Topic TYPE_RELATES_TO = TopicFactory.fromName("Harmonia.Relationship.RelatesTo");
    public static final Topic TYPE_PART_OF = TopicFactory.fromName("Harmonia.Relationship.PartOf");
    public static final Topic TYPE_BASED_ON = TopicFactory.fromName("Harmonia.Relationship.BasedOn");
    public static final Topic TYPE_DERIVED_FROM = TopicFactory.fromName("Harmonia.Relationship.DerivedFrom");
    public static final Topic TYPE_REPLACES = TopicFactory.fromName("Harmonia.Relationship.Replaces");

    public static final String RELATES_TO = "relatesTo";
    public static final String PART_OF = "partOf";
    public static final String BASED_ON = "basedOn";
    public static final String DERIVED_FROM = "derivedFrom";
    public static final String REPLACES = "replaces";

    public static final Topic DISPOSITION_ACTIVE = TopicFactory.fromName("Harmonia.Lifecycle.Active");
    public static final Topic DISPOSITION_RETIRED = TopicFactory.fromName("Harmonia.Lifecycle.Retired");
    public static final Topic DISPOSITION_ARCHIVED = TopicFactory.fromName("Harmonia.Lifecycle.Archived");
    public static final Topic DISPOSITION_HOLD = TopicFactory.fromName("Harmonia.Lifecycle.LegalHold");

    private InformationUnitValueSets() {
    }
}