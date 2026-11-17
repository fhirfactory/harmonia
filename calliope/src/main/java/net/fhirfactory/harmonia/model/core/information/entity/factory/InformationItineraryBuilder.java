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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationItinerary;
import net.fhirfactory.harmonia.model.core.naming.Topic;

import java.util.ArrayList;
import java.util.List;

/** Assembles the optional Topic references and participants in an information itinerary. */
public final class InformationItineraryBuilder {

    private Topic sourceEntryComponent;
    private Topic creationComponent;
    private Topic previousComponent;
    private Topic nextComponent;
    private Topic targetExitComponent;
    private final List<Topic> participatingComponents = new ArrayList<>();

    /** Sets the source-entry component. */
    public InformationItineraryBuilder sourceEntryComponent(Topic topic) {
        sourceEntryComponent = topic;
        return this;
    }

    /** Sets the component that created the information. */
    public InformationItineraryBuilder creationComponent(Topic topic) {
        creationComponent = topic;
        return this;
    }

    /** Sets the previous component in the recorded itinerary. */
    public InformationItineraryBuilder previousComponent(Topic topic) {
        previousComponent = topic;
        return this;
    }

    /** Sets the next component in the recorded itinerary. */
    public InformationItineraryBuilder nextComponent(Topic topic) {
        nextComponent = topic;
        return this;
    }

    /** Sets the target-exit component. */
    public InformationItineraryBuilder targetExitComponent(Topic topic) {
        targetExitComponent = topic;
        return this;
    }

    /** Adds one non-null participating component. */
    public InformationItineraryBuilder addParticipatingComponent(Topic topic) {
        if (topic != null) {
            participatingComponents.add(topic);
        }
        return this;
    }

    /** Replaces participating components and rejects malformed null entries. */
    public InformationItineraryBuilder participatingComponents(List<Topic> topics) {
        participatingComponents.clear();
        if (topics != null) {
            for (Topic topic : topics) {
                if (topic == null) {
                    throw new IllegalArgumentException("participatingComponents must not contain null elements");
                }
                participatingComponents.add(topic);
            }
        }
        return this;
    }

    /** Builds an immutable itinerary value. */
    public InformationItinerary build() {
        return new InformationItinerary(sourceEntryComponent, creationComponent, previousComponent, nextComponent,
                targetExitComponent, participatingComponents);
    }
}