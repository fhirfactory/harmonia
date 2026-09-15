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

package net.fhirfactory.harmonia.mllpgateway.messaging;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import net.fhirfactory.harmonia.mllpgateway.config.TaskProcessorConfig;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JmsConnectionFactoryProducer {

    private static final Logger log = LoggerFactory.getLogger(JmsConnectionFactoryProducer.class);

    @Inject
    private TaskProcessorConfig config;

    private ActiveMQConnectionFactory connectionFactory;

    @Produces
    @ApplicationScoped
    public ConnectionFactory produceConnectionFactory() {
        if (connectionFactory == null) {
            String brokerUrl = config != null ? config.getBrokerUrl() : TaskProcessorConfig.DEFAULT_BROKER_URL;
            String user = config != null ? config.getBrokerUsername() : "admin";
            String pass = config != null ? config.getBrokerPassword() : "admin";
            log.info("Initializing ActiveMQConnectionFactory for MLLP Gateway connecting to {}", brokerUrl);
            try {
                connectionFactory = new ActiveMQConnectionFactory(brokerUrl, user, pass);
            } catch (Exception e) {
                log.warn("Could not create ActiveMQConnectionFactory for [{}]: {}", brokerUrl, e.getMessage());
            }
        }
        return connectionFactory;
    }

    @PreDestroy
    public void cleanup() {
        if (connectionFactory != null) {
            try {
                connectionFactory.close();
            } catch (Exception e) {
                log.debug("Error closing connection factory: {}", e.getMessage());
            }
        }
    }
}
