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

package net.fhirfactory.harmonia.mllpout.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;

/**
 * Configuration provider for an MLLP Outbound Gateway instance.
 * Supports instance identification, endpoint pinning, and dedicated Petasos Artemis queue resolution.
 */
@ApplicationScoped
public class MllpOutboundConfig implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_INSTANCE_ID = "mllp-sender-default";
    public static final String DEFAULT_QUEUE_PREFIX = "petasos.queue.mllp.outbound";
    public static final String DEFAULT_BROKER_URL = "tcp://localhost:61616";
    public static final int DEFAULT_REST_PORT = 8087;

    private String instanceId;
    private String targetEndpointId;
    private String queuePrefix;
    private String brokerUrl;
    private String brokerUsername;
    private String brokerPassword;
    private int restPort;
    private int concurrency = 1;

    public MllpOutboundConfig() {
        this.instanceId = getEnvOrProperty("MLLP_OUTBOUND_INSTANCE_ID", DEFAULT_INSTANCE_ID);
        this.targetEndpointId = getEnvOrProperty("MLLP_OUTBOUND_TARGET_ENDPOINT_ID", null);
        this.queuePrefix = getEnvOrProperty("MLLP_OUTBOUND_QUEUE_PREFIX", DEFAULT_QUEUE_PREFIX);
        this.brokerUrl = getEnvOrProperty("PETASOS_BROKER_URL",
                getEnvOrProperty("petasos.broker.url",
                        getEnvOrProperty("TASK_PROCESSOR_BROKER_URL",
                                getEnvOrProperty("BROKER_URL", DEFAULT_BROKER_URL))));
        this.brokerUsername = getEnvOrProperty("PETASOS_BROKER_USER",
                getEnvOrProperty("petasos.broker.user",
                        getEnvOrProperty("ARTEMIS_USER",
                                getEnvOrProperty("TASK_PROCESSOR_BROKER_USERNAME",
                                        getEnvOrProperty("BROKER_USERNAME", "admin")))));
        this.brokerPassword = getEnvOrProperty("PETASOS_BROKER_PASSWORD",
                getEnvOrProperty("petasos.broker.password",
                        getEnvOrProperty("ARTEMIS_PASSWORD",
                                getEnvOrProperty("TASK_PROCESSOR_BROKER_PASSWORD",
                                        getEnvOrProperty("BROKER_PASSWORD", "adminPassword")))));
        this.restPort = Integer.parseInt(getEnvOrProperty("MLLP_OUTBOUND_PORT", String.valueOf(DEFAULT_REST_PORT)));
        this.concurrency = Integer.parseInt(getEnvOrProperty("MLLP_OUTBOUND_CONCURRENCY", "1"));
    }

    public MllpOutboundConfig(String instanceId, String targetEndpointId) {
        this.instanceId = instanceId;
        this.targetEndpointId = targetEndpointId;
        this.queuePrefix = DEFAULT_QUEUE_PREFIX;
        this.brokerUrl = DEFAULT_BROKER_URL;
        this.brokerUsername = "admin";
        this.brokerPassword = "admin";
        this.restPort = DEFAULT_REST_PORT;
    }

    private String getEnvOrProperty(String key, String defaultValue) {
        String sysProp = System.getProperty(key);
        if (sysProp != null && !sysProp.isBlank()) {
            return sysProp.trim();
        }
        String envVal = System.getenv(key);
        if (envVal != null && !envVal.isBlank()) {
            return envVal.trim();
        }
        return defaultValue;
    }

    /**
     * Resolves the dedicated Petasos queue assigned to this outbound sender instance.
     */
    public String getDedicatedEventQueueName() {
        if (StringUtils.isNotBlank(targetEndpointId)) {
            return getDedicatedEventQueueName(targetEndpointId);
        }
        String prefix = StringUtils.isNotBlank(queuePrefix) ? queuePrefix.trim() : DEFAULT_QUEUE_PREFIX;
        String id = StringUtils.isNotBlank(instanceId) ? instanceId.trim().toLowerCase() : "default";
        return prefix + "." + id;
    }

    /**
     * Resolves a dedicated Petasos queue for an explicit target endpoint.
     */
    public String getDedicatedEventQueueName(String endpointId) {
        String prefix = StringUtils.isNotBlank(queuePrefix) ? queuePrefix.trim() : DEFAULT_QUEUE_PREFIX;
        String id = StringUtils.isNotBlank(endpointId) ? endpointId.trim().toLowerCase() : "default";
        return prefix + "." + id;
    }

    public String getInstanceId() {
        return instanceId;
    }

    public void setInstanceId(String instanceId) {
        this.instanceId = instanceId;
    }

    public String getTargetEndpointId() {
        return targetEndpointId;
    }

    public void setTargetEndpointId(String targetEndpointId) {
        this.targetEndpointId = targetEndpointId;
    }

    public String getQueuePrefix() {
        return queuePrefix;
    }

    public void setQueuePrefix(String queuePrefix) {
        this.queuePrefix = queuePrefix;
    }

    public String getBrokerUrl() {
        return brokerUrl;
    }

    public void setBrokerUrl(String brokerUrl) {
        this.brokerUrl = brokerUrl;
    }

    public String getBrokerUsername() {
        return brokerUsername;
    }

    public void setBrokerUsername(String brokerUsername) {
        this.brokerUsername = brokerUsername;
    }

    public String getBrokerPassword() {
        return brokerPassword;
    }

    public void setBrokerPassword(String brokerPassword) {
        this.brokerPassword = brokerPassword;
    }

    public int getRestPort() {
        return restPort;
    }

    public void setRestPort(int restPort) {
        this.restPort = restPort;
    }

    public int getConcurrency() {
        return concurrency;
    }

    public void setConcurrency(int concurrency) {
        this.concurrency = concurrency;
    }
}
