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

package net.fhirfactory.harmonia.praxis.messaging;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessageBuilder;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.Task;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

/**
 * Ponos message producer component binding through the Petasos client facade (ArtemisPetasos)
 * rather than embedding Artemis broker server configuration.
 */
@ApplicationScoped
public class ArtemisPonosProducer {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPonosProducer.class);

    @Inject
    private Petasos petasos;

    @Inject
    private QueueConfig queueConfig;

    @Inject
    private FhirContext fhirContext;

    private ObjectMapper objectMapper = new ObjectMapper();

    public ArtemisPonosProducer() {
    }

    public ArtemisPonosProducer(Petasos petasos, QueueConfig queueConfig) {
        this.petasos = petasos;
        this.queueConfig = queueConfig;
    }

    public void sendTask(Task task) throws Exception {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        IParser parser = (fhirContext != null ? fhirContext : FhirContext.forR5()).newJsonParser().setPrettyPrint(false);
        String json = parser.encodeResourceToString(task);
        sendTaskJson(json);
    }

    public void sendTaskJson(String jsonPayload) throws Exception {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("Payload cannot be null");
        }
        String queueName = queueConfig != null ? queueConfig.getQueueName() : QueueConfig.DEFAULT_QUEUE_NAME;
        PetasosMessage message = PetasosMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .destination(PetasosDestination.queue(queueName))
                .messageType("Task")
                .payload(jsonPayload)
                .build();
        ensurePetasos().send(PetasosDestination.queue(queueName), message);
        log.info("Sent Task payload via Petasos Client Facade to named queue [{}]", queueName);
    }

    public void sendTaskEvent(ErgonEvent event) throws Exception {
        if (event == null) {
            throw new IllegalArgumentException("TaskEvent cannot be null");
        }
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
        }
        String json = objectMapper.writeValueAsString(event);
        sendTaskEvent(event, json);
    }

    public void sendTaskEventJson(String jsonPayload) throws Exception {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("TaskEvent payload cannot be null");
        }
        ErgonEvent event = null;
        try {
            if (objectMapper == null) objectMapper = new ObjectMapper();
            event = objectMapper.readValue(jsonPayload, ErgonEvent.class);
        } catch (Exception ignored) {}
        sendTaskEvent(event, jsonPayload);
    }

    public void sendTaskEvent(ErgonEvent event, String jsonPayload) throws Exception {
        if (jsonPayload == null) {
            throw new IllegalArgumentException("TaskEvent payload cannot be null");
        }
        String queueName;
        if (event != null && StringUtils.isNotBlank(event.getGatewayInstanceId()) && queueConfig != null) {
            queueName = queueConfig.getDedicatedEventQueueName(event.getGatewayInstanceId());
        } else if (queueConfig != null) {
            queueName = queueConfig.getEventQueueName();
        } else {
            queueName = QueueConfig.DEFAULT_EVENT_QUEUE_NAME;
        }

        PetasosMessageBuilder builder = PetasosMessage.builder()
                .messageId(UUID.randomUUID().toString())
                .destination(PetasosDestination.queue(queueName))
                .messageType("TaskEvent")
                .payload(jsonPayload);

        if (event != null) {
            if (StringUtils.isNotBlank(event.getGatewayInstanceId())) {
                builder.header("HIE_GATEWAY_INSTANCE", event.getGatewayInstanceId());
            }
            if (StringUtils.isNotBlank(event.getTriggerType())) {
                builder.header("HIE_TRIGGER_TYPE", event.getTriggerType());
            }
            if (StringUtils.isNotBlank(event.getMessageType())) {
                builder.header("HIE_MESSAGE_TYPE", event.getMessageType());
            }
            if (StringUtils.isNotBlank(event.getTaskId())) {
                builder.header("HIE_TASK_ID", event.getTaskId());
            }
            if (StringUtils.isNotBlank(event.getControlId())) {
                builder.header("HIE_CONTROL_ID", event.getControlId());
            }
            if (StringUtils.isNotBlank(event.getAction())) {
                builder.header("HIE_ACTION", event.getAction());
            }
            if (StringUtils.isNotBlank(event.getStatus())) {
                builder.header("HIE_STATUS", event.getStatus());
            }
            if (event.getTopic() != null) {
                Topic t = event.getTopic();
                if (t.getDomain() != null) builder.header("HIE_TOPIC_DOMAIN", t.getDomain());
                if (t.getModel() != null) builder.header("HIE_TOPIC_MODEL", t.getModel());
                if (t.getDataElement() != null) builder.header("HIE_TOPIC_DATA_ELEMENT", t.getDataElement());
                if (t.getDataElementQualifier() != null) builder.header("HIE_TOPIC_QUALIFIER", t.getDataElementQualifier());
                builder.header("HIE_TOPIC", t.toTopicString());
            }
        }

        ensurePetasos().send(PetasosDestination.queue(queueName), builder.build());
        log.info("Sent TaskEvent payload via Petasos Client Facade to named event queue [{}]", queueName);
    }

    private Petasos ensurePetasos() {
        if (petasos == null) {
            String brokerUrl = queueConfig != null ? queueConfig.getBrokerUrl() : QueueConfig.DEFAULT_BROKER_URL;
            String username = queueConfig != null ? queueConfig.getBrokerUsername() : "admin";
            String password = queueConfig != null ? queueConfig.getBrokerPassword() : "adminPassword";
            boolean haEnabled = queueConfig != null ? queueConfig.isHaEnabled() : true;
            PetasosConfig config = PetasosConfig.builder()
                    .addBrokerUrl(brokerUrl)
                    .username(username)
                    .password(password)
                    .haEnabled(haEnabled)
                    .build();
            petasos = ArtemisPetasos.create(config);
        }
        return petasos;
    }

    public Petasos getPetasos() {
        return petasos;
    }

    public void setPetasos(Petasos petasos) {
        this.petasos = petasos;
    }

    public QueueConfig getQueueConfig() {
        return queueConfig;
    }

    public void setQueueConfig(QueueConfig queueConfig) {
        this.queueConfig = queueConfig;
    }

    public FhirContext getFhirContext() {
        return fhirContext;
    }

    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }
}
