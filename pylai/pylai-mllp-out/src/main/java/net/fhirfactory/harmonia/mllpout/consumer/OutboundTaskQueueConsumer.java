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

package net.fhirfactory.harmonia.mllpout.consumer;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.jms.BytesMessage;
import jakarta.jms.Connection;
import jakarta.jms.ConnectionFactory;
import jakarta.jms.Message;
import jakarta.jms.MessageConsumer;
import jakarta.jms.MessageListener;
import jakarta.jms.Queue;
import jakarta.jms.Session;
import jakarta.jms.TextMessage;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpRequest;
import net.fhirfactory.harmonia.mllpgateway.model.OutboundMllpResponse;
import net.fhirfactory.harmonia.mllpout.config.MllpOutboundConfig;
import org.apache.camel.CamelContext;
import org.apache.camel.ProducerTemplate;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Artemis JMS consumer listening to the dedicated endpoint queue for this sender instance
 * and routing received tasks into the Apache Camel outbound dispatch pipeline.
 */
@ApplicationScoped
public class OutboundTaskQueueConsumer implements MessageListener {

    private static final Logger LOG = LoggerFactory.getLogger(OutboundTaskQueueConsumer.class);

    @Inject
    private MllpOutboundConfig outboundConfig;

    @Inject
    private ConnectionFactory connectionFactory;

    @Inject
    private net.fhirfactory.harmonia.mllpout.lifecycle.OutboundStateLifecycleManager lifecycleManager;

    private CamelContext camelContext;
    private ProducerTemplate producerTemplate;

    private Connection jmsConnection;
    private Session jmsSession;
    private MessageConsumer jmsConsumer;
    private String queueName;
    private final AtomicBoolean running = new AtomicBoolean(false);
    private final ObjectMapper objectMapper = new ObjectMapper();

    public OutboundTaskQueueConsumer() {
    }

    public OutboundTaskQueueConsumer(MllpOutboundConfig outboundConfig, ConnectionFactory connectionFactory, CamelContext camelContext) {
        this.outboundConfig = outboundConfig;
        this.connectionFactory = connectionFactory;
        this.camelContext = camelContext;
        if (camelContext != null) {
            this.producerTemplate = camelContext.createProducerTemplate();
        }
    }

    /**
     * Starts the queue consumer on the dedicated instance queue.
     */
    public synchronized void start() {
        if (running.get()) {
            return;
        }

        if (outboundConfig == null) {
            outboundConfig = new MllpOutboundConfig();
        }

        queueName = outboundConfig.getDedicatedEventQueueName();
        LOG.info("Starting OutboundTaskQueueConsumer on dedicated queue [{}] for instance [{}]",
                queueName, outboundConfig.getInstanceId());

        if (connectionFactory == null) {
            LOG.warn("JMS ConnectionFactory is not available; queue consumer startup skipped");
            return;
        }

        try {
            jmsConnection = connectionFactory.createConnection();
            jmsConnection.setClientID(outboundConfig.getInstanceId() + "-consumer");
            jmsSession = jmsConnection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = jmsSession.createQueue(queueName);
            jmsConsumer = jmsSession.createConsumer(queue);
            jmsConsumer.setMessageListener(this);
            jmsConnection.start();
            running.set(true);
            LOG.info("OutboundTaskQueueConsumer successfully started on queue [{}]", queueName);
        } catch (Exception e) {
            LOG.warn("Failed to start JMS consumer on queue [{}]: {}", queueName, e.getMessage());
        }
    }

    /**
     * Stops the queue consumer.
     */
    public synchronized void stop() {
        if (!running.get()) {
            return;
        }
        try {
            if (jmsConsumer != null) {
                jmsConsumer.close();
            }
            if (jmsSession != null) {
                jmsSession.close();
            }
            if (jmsConnection != null) {
                jmsConnection.close();
            }
            if (producerTemplate != null) {
                producerTemplate.stop();
            }
            running.set(false);
            LOG.info("OutboundTaskQueueConsumer stopped on queue [{}]", queueName);
        } catch (Exception e) {
            LOG.debug("Error closing JMS consumer resources: {}", e.getMessage());
        }
    }

    @Override
    public void onMessage(Message message) {
        if (message == null) {
            return;
        }

        try {
            String payload = null;
            if (message instanceof TextMessage) {
                payload = ((TextMessage) message).getText();
            } else if (message instanceof BytesMessage) {
                BytesMessage bm = (BytesMessage) message;
                byte[] data = new byte[(int) bm.getBodyLength()];
                bm.readBytes(data);
                payload = new String(data, StandardCharsets.UTF_8);
            }

            if (StringUtils.isBlank(payload)) {
                LOG.warn("Received empty JMS message from queue [{}]", queueName);
                return;
            }

            LOG.debug("Received outbound task message on queue [{}]: length={}", queueName, payload.length());
            OutboundMllpRequest request = parseRequest(payload);

            OutboundMllpResponse response = processOutboundRequest(request);
            LOG.info("Completed outbound dispatch for request {}: success={}, ackCode={}",
                    request.getRequestId(), response.isSuccessful(), response.getAckCode());

        } catch (Exception e) {
            LOG.error("Error processing message from queue [{}]: {}", queueName, e.getMessage(), e);
        }
    }

    /**
     * Dispatches the request through Camel route and returns the response.
     */
    public OutboundMllpResponse processOutboundRequest(OutboundMllpRequest request) {
        if (lifecycleManager != null) {
            lifecycleManager.onDispatchInitiated(request);
        }

        if (producerTemplate == null && camelContext != null) {
            producerTemplate = camelContext.createProducerTemplate();
        }

        OutboundMllpResponse response;
        if (producerTemplate == null) {
            response = OutboundMllpResponse.failure(request.getRequestId(), request.getMessageControlId(),
                    request.getDestinationId(), "Camel ProducerTemplate is unavailable", 0L);
        } else {
            response = producerTemplate.requestBody("direct:mllp-outbound-send", request, OutboundMllpResponse.class);
        }

        if (lifecycleManager != null) {
            response = lifecycleManager.onDispatchCompleted(request, response);
        }

        return response;
    }

    private OutboundMllpRequest parseRequest(String payload) {
        try {
            if (payload.trim().startsWith("{")) {
                return objectMapper.readValue(payload, OutboundMllpRequest.class);
            }
        } catch (Exception e) {
            LOG.debug("JSON deserialization failed, treating as raw HL7 message: {}", e.getMessage());
        }

        String targetEndpoint = outboundConfig != null ? outboundConfig.getTargetEndpointId() : null;
        return new OutboundMllpRequest(payload, targetEndpoint);
    }

    public boolean isRunning() {
        return running.get();
    }

    public String getQueueName() {
        if (queueName != null) {
            return queueName;
        }
        return outboundConfig != null ? outboundConfig.getDedicatedEventQueueName() : null;
    }

    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
        if (camelContext != null) {
            this.producerTemplate = camelContext.createProducerTemplate();
        }
    }

    public void setProducerTemplate(ProducerTemplate producerTemplate) {
        this.producerTemplate = producerTemplate;
    }

    public void setOutboundConfig(MllpOutboundConfig outboundConfig) {
        this.outboundConfig = outboundConfig;
    }

    public net.fhirfactory.harmonia.mllpout.lifecycle.OutboundStateLifecycleManager getLifecycleManager() {
        return lifecycleManager;
    }

    public void setLifecycleManager(net.fhirfactory.harmonia.mllpout.lifecycle.OutboundStateLifecycleManager lifecycleManager) {
        this.lifecycleManager = lifecycleManager;
    }
}
