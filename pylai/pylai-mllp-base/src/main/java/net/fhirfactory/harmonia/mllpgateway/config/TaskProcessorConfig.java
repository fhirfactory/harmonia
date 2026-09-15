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

package net.fhirfactory.harmonia.mllpgateway.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class TaskProcessorConfig {

    public static final String DEFAULT_EVENT_QUEUE_PREFIX = "task.event.queue";
    public static final String DEFAULT_GATEWAY_INSTANCE_ID = "mllp-gateway-default";
    public static final String DEFAULT_EVENT_QUEUE_NAME = DEFAULT_EVENT_QUEUE_PREFIX + "." + DEFAULT_GATEWAY_INSTANCE_ID;
    public static final String DEFAULT_BROKER_HOST = "127.0.0.1";
    public static final int DEFAULT_BROKER_PORT = 61616;
    public static final String DEFAULT_BROKER_URL = "tcp://127.0.0.1:61616";
    public static final String DEFAULT_TASK_PROCESSOR_URL = "http://127.0.0.1:8080/api/queue/event";

    public String getGatewayInstanceId() {
        String env = System.getenv("MLLP_GATEWAY_INSTANCE_ID");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("mllp.gateway.instance.id", DEFAULT_GATEWAY_INSTANCE_ID);
    }

    public String getEventQueuePrefix() {
        String env = System.getenv("MLLP_EVENT_QUEUE_PREFIX");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("mllp.event.queue.prefix", DEFAULT_EVENT_QUEUE_PREFIX);
    }

    public String getDedicatedEventQueueName(String gatewayInstanceId) {
        String prefix = getEventQueuePrefix();
        String gwId = StringUtils.isNotBlank(gatewayInstanceId) ? gatewayInstanceId.trim() : DEFAULT_GATEWAY_INSTANCE_ID;
        return prefix + "." + gwId;
    }

    public String getEventQueueName() {
        String env = System.getenv("TASK_EVENT_QUEUE_NAME");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        String prop = System.getProperty("task.event.queue.name");
        if (StringUtils.isNotBlank(prop)) {
            return prop.trim();
        }
        return getDedicatedEventQueueName(getGatewayInstanceId());
    }

    public String getBrokerUrl() {
        String env = System.getenv("TASK_BROKER_URL");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        String host = getBrokerHost();
        int port = getBrokerPort();
        return System.getProperty("task.broker.url", "tcp://" + host + ":" + port);
    }

    public String getBrokerHost() {
        String env = System.getenv("TASK_BROKER_HOST");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.broker.host", DEFAULT_BROKER_HOST);
    }

    public int getBrokerPort() {
        String env = System.getenv("TASK_BROKER_PORT");
        if (StringUtils.isNotBlank(env)) {
            try {
                return Integer.parseInt(env.trim());
            } catch (NumberFormatException ignored) {}
        }
        String prop = System.getProperty("task.broker.port");
        if (StringUtils.isNotBlank(prop)) {
            try {
                return Integer.parseInt(prop.trim());
            } catch (NumberFormatException ignored) {}
        }
        return DEFAULT_BROKER_PORT;
    }

    public String getBrokerUsername() {
        String env = System.getenv("TASK_BROKER_USER");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.broker.user", "admin");
    }

    public String getBrokerPassword() {
        String env = System.getenv("TASK_BROKER_PASSWORD");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.broker.password", "admin");
    }

    public String getTaskProcessorUrl() {
        String env = System.getenv("TASK_PROCESSOR_URL");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.processor.url", DEFAULT_TASK_PROCESSOR_URL);
    }

    public boolean isEventProducerEnabled() {
        String env = System.getenv("TASK_EVENT_PRODUCER_ENABLED");
        if (StringUtils.isNotBlank(env)) {
            return Boolean.parseBoolean(env.trim());
        }
        return Boolean.parseBoolean(System.getProperty("task.event.producer.enabled", "true"));
    }
}
