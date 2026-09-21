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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.petasos.api.config.PetasosConfig;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageHandler;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosSubscription;
import net.fhirfactory.harmonia.petasos.api.destination.PetasosDestination;
import net.fhirfactory.harmonia.petasos.artemis.ArtemisPetasos;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Ponos message consumer component binding through the Petasos client facade (ArtemisPetasos)
 * rather than embedding Artemis broker server configuration.
 */
@ApplicationScoped
public class ArtemisPonosConsumer implements AutoCloseable {

    private static final Logger log = LoggerFactory.getLogger(ArtemisPonosConsumer.class);

    @Inject
    private Petasos petasos;

    @Inject
    private QueueConfig queueConfig;

    private final Map<String, PetasosSubscription> activeSubscriptions = new ConcurrentHashMap<>();

    public ArtemisPonosConsumer() {
    }

    public ArtemisPonosConsumer(Petasos petasos, QueueConfig queueConfig) {
        this.petasos = petasos;
        this.queueConfig = queueConfig;
    }

    public synchronized PetasosSubscription subscribe(String queueName, PetasosMessageHandler handler) {
        if (activeSubscriptions.containsKey(queueName)) {
            log.warn("Subscription already exists for queue [{}], unsubscribing previous subscription", queueName);
            unsubscribe(queueName);
        }
        log.info("Subscribing Ponos consumer to queue [{}] via Petasos Client Facade", queueName);
        PetasosSubscription subscription = ensurePetasos().receive(PetasosDestination.queue(queueName), handler);
        activeSubscriptions.put(queueName, subscription);
        return subscription;
    }

    public synchronized void unsubscribe(String queueName) {
        PetasosSubscription subscription = activeSubscriptions.remove(queueName);
        if (subscription != null) {
            try {
                subscription.unsubscribe();
                log.info("Unsubscribed Ponos consumer from queue [{}]", queueName);
            } catch (Exception e) {
                log.warn("Error unsubscribing from queue [{}]: {}", queueName, e.getMessage());
            }
        }
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

    public Map<String, PetasosSubscription> getActiveSubscriptions() {
        return activeSubscriptions;
    }

    @Override
    public synchronized void close() {
        for (Map.Entry<String, PetasosSubscription> entry : activeSubscriptions.entrySet()) {
            try {
                entry.getValue().unsubscribe();
            } catch (Exception ignored) {
            }
        }
        activeSubscriptions.clear();
    }
}
