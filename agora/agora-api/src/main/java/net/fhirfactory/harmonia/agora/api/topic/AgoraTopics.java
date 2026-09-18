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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.agora.api.topic;

import net.fhirfactory.harmonia.model.topic.Topic;

import java.util.Date;

/**
 * Topic definitions, queue identifiers, and helper methods for Agora Petasos messaging.
 */
public final class AgoraTopics {

    public static final String QUEUE_AGORA_INBOUND = "petasos.queue.agora.inbound";
    public static final String QUEUE_AGORA_OUTBOUND = "petasos.queue.agora.outbound";
    public static final String TOPIC_AGORA_EVENTS = "petasos.topic.agora.events";
    public static final String QUEUE_AGORA_AUDIT = "petasos.queue.agora.audit";

    public static final String TOPIC_DOMAIN = "Health";
    public static final String TOPIC_MODEL = "FHIR";
    public static final String TOPIC_MODEL_VERSION = "R5";
    public static final String DATA_ELEMENT_COMMUNICATION = "Communication";
    public static final String DATA_ELEMENT_TASK = "Task";
    public static final String DEFAULT_SOURCE = "harmonia-agora";

    private AgoraTopics() {
        // Utility class
    }

    /**
     * Builds a canonical Calliope Topic descriptor for an Agora collaboration message.
     *
     * @param dataElement the data element (e.g. Communication, Task)
     * @param qualifier   the data element qualifier (e.g. Inbound, Outbound, Note)
     * @param destination the target destination or subsystem
     * @return a structured Calliope Topic instance
     */
    public static Topic createTopic(String dataElement, String qualifier, String destination) {
        Topic topic = new Topic(TOPIC_DOMAIN, TOPIC_MODEL, TOPIC_MODEL_VERSION, dataElement, qualifier);
        topic.setSource(DEFAULT_SOURCE);
        topic.setDestination(destination);
        topic.setReceivedDate(new Date());
        return topic;
    }

    /**
     * Builds a default topic descriptor for inbound collaboration events from Matrix.
     */
    public static Topic inboundTopic() {
        return createTopic(DATA_ELEMENT_COMMUNICATION, "Inbound", "harmonia-core");
    }

    /**
     * Builds a default topic descriptor for outbound collaboration notifications to Matrix.
     */
    public static Topic outboundTopic() {
        return createTopic(DATA_ELEMENT_TASK, "Outbound", "agora-matrix");
    }
}
