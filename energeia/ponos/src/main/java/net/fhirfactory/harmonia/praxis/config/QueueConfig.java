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

package net.fhirfactory.harmonia.praxis.config;

import jakarta.enterprise.context.ApplicationScoped;
import org.apache.commons.lang3.StringUtils;

@ApplicationScoped
public class QueueConfig {

    public static final String DEFAULT_QUEUE_NAME = "task.processing.queue";
    public static final String DEFAULT_EVENT_QUEUE_NAME = "task.event.queue";
    public static final String DEFAULT_EVENT_QUEUE_PREFIX = "task.event.queue";
    public static final String DEFAULT_PROVIDER_REGISTRY_QUEUE = "harmonia.provider.registry.change.request";
    public static final String DEFAULT_GATEWAY_INSTANCE_ID = "mllp-gateway-default";
    public static final String DEFAULT_BROKER_HOST = "0.0.0.0";
    public static final int DEFAULT_BROKER_PORT = 61616;
    public static final String DEFAULT_BROKER_URL = "vm://0";

    public String getQueueName() {
        String env = System.getenv("TASK_QUEUE_NAME");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.queue.name", DEFAULT_QUEUE_NAME);
    }

    public String getEventQueueName() {
        String env = System.getenv("TASK_EVENT_QUEUE_NAME");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.event.queue.name", DEFAULT_EVENT_QUEUE_NAME);
    }

    public String getEventQueuePrefix() {
        String env = System.getenv("TASK_EVENT_QUEUE_PREFIX");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.event.queue.prefix", DEFAULT_EVENT_QUEUE_PREFIX);
    }

    public String getProviderRegistryChangeQueue() {
        String env = System.getenv("PROVIDER_REGISTRY_CHANGE_QUEUE");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("provider.registry.change.queue", DEFAULT_PROVIDER_REGISTRY_QUEUE);
    }

    public String getDedicatedEventQueueName(String gatewayInstanceId) {
        if (StringUtils.isBlank(gatewayInstanceId) || "*".equals(gatewayInstanceId)) {
            return getEventQueueName();
        }
        return getEventQueuePrefix() + "." + gatewayInstanceId.trim();
    }

    public java.util.Set<String> getGatewayEventQueues() {
        java.util.Set<String> queues = new java.util.LinkedHashSet<>();
        queues.add(getEventQueueName());
        queues.add(getDedicatedEventQueueName(DEFAULT_GATEWAY_INSTANCE_ID));

        String customQueues = System.getenv("TASK_EVENT_QUEUES");
        if (StringUtils.isBlank(customQueues)) {
            customQueues = System.getProperty("task.event.queues");
        }
        if (StringUtils.isNotBlank(customQueues)) {
            for (String q : customQueues.split("[,;\\s]+")) {
                if (StringUtils.isNotBlank(q)) {
                    queues.add(q.trim());
                }
            }
        }

        String gateways = System.getenv("TASK_GATEWAY_INSTANCES");
        if (StringUtils.isBlank(gateways)) {
            gateways = System.getProperty("task.gateway.instances");
        }
        if (StringUtils.isNotBlank(gateways)) {
            for (String gw : gateways.split("[,;\\s]+")) {
                if (StringUtils.isNotBlank(gw) && !"*".equals(gw)) {
                    queues.add(getDedicatedEventQueueName(gw));
                }
            }
        }

        return queues;
    }

    public boolean isBrokerEnabled() {
        String env = System.getenv("TASK_BROKER_ENABLED");
        if (StringUtils.isNotBlank(env)) {
            return Boolean.parseBoolean(env.trim());
        }
        return Boolean.parseBoolean(System.getProperty("task.broker.enabled", "true"));
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
            } catch (NumberFormatException ignored) {
            }
        }
        return Integer.getInteger("task.broker.port", DEFAULT_BROKER_PORT);
    }

    public String getBrokerUrl() {
        String env = System.getenv("TASK_BROKER_URL");
        if (StringUtils.isNotBlank(env)) {
            return env.trim();
        }
        return System.getProperty("task.broker.url", DEFAULT_BROKER_URL);
    }
}
