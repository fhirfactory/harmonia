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

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import jakarta.inject.Inject;
import jakarta.jms.ConnectionFactory;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationScoped
public class JmsConnectionFactoryProducer {

    private static final Logger log = LoggerFactory.getLogger(JmsConnectionFactoryProducer.class);

    @Inject
    private QueueConfig queueConfig;

    private ActiveMQConnectionFactory connectionFactory;
    private ArtemisPetasos petasos;

    @Produces
    @ApplicationScoped
    public ConnectionFactory produceConnectionFactory() {
        if (connectionFactory == null) {
            String brokerUrl = queueConfig != null ? queueConfig.getBrokerUrl() : QueueConfig.DEFAULT_BROKER_URL;
            String username = queueConfig != null ? queueConfig.getBrokerUsername() : "admin";
            String password = queueConfig != null ? queueConfig.getBrokerPassword() : "adminPassword";
            log.info("Initializing ActiveMQConnectionFactory connecting to {}", brokerUrl);
            connectionFactory = new ActiveMQConnectionFactory(brokerUrl, username, password);
        }
        return connectionFactory;
    }

    @Produces
    @ApplicationScoped
    public Petasos producePetasos() {
        if (petasos == null) {
            String brokerUrl = queueConfig != null ? queueConfig.getBrokerUrl() : QueueConfig.DEFAULT_BROKER_URL;
            String username = queueConfig != null ? queueConfig.getBrokerUsername() : "admin";
            String password = queueConfig != null ? queueConfig.getBrokerPassword() : "adminPassword";
            log.info("Initializing ArtemisPetasos connecting to {}", brokerUrl);
            PetasosConfig config = PetasosConfig.builder()
                    .addBrokerUrl(brokerUrl)
                    .username(username)
                    .password(password)
                    .build();
            petasos = ArtemisPetasos.create(config);
        }
        return petasos;
    }

    @PreDestroy
    public void cleanup() {
        if (petasos != null) {
            try {
                petasos.close();
            } catch (Exception e) {
                log.debug("Error closing Petasos instance: {}", e.getMessage());
            }
        }
        if (connectionFactory != null) {
            try {
                connectionFactory.close();
            } catch (Exception e) {
                log.debug("Error closing connection factory: {}", e.getMessage());
            }
        }
    }
}
