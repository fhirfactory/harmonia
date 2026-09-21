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

import net.fhirfactory.harmonia.petasos.api.Petasos;
import net.fhirfactory.harmonia.praxis.config.QueueConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.assertj.core.api.Assertions.assertThat;

class JmsConnectionFactoryProducerTest {

    private JmsConnectionFactoryProducer producer;

    @AfterEach
    void tearDown() {
        if (producer != null) {
            producer.cleanup();
        }
    }

    @Test
    void testProducePetasosPropagatesHaDisabled() throws Exception {
        QueueConfig queueConfig = new QueueConfig();
        queueConfig.setBrokerUrl("tcp://localhost:61616");
        queueConfig.setHaEnabled(false);

        producer = new JmsConnectionFactoryProducer();
        setField(producer, "queueConfig", queueConfig);

        Petasos petasos = producer.producePetasos();
        assertThat(petasos).isNotNull();
        assertThat(petasos.getConfig().isHaEnabled()).isFalse();
        assertThat(petasos.getConfig().getUsername()).isEqualTo("admin");
    }

    @Test
    void testProducePetasosPropagatesHaEnabled() throws Exception {
        QueueConfig queueConfig = new QueueConfig();
        queueConfig.setBrokerUrl("tcp://localhost:61616");
        queueConfig.setHaEnabled(true);

        producer = new JmsConnectionFactoryProducer();
        setField(producer, "queueConfig", queueConfig);

        Petasos petasos = producer.producePetasos();
        assertThat(petasos).isNotNull();
        assertThat(petasos.getConfig().isHaEnabled()).isTrue();
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }
}
