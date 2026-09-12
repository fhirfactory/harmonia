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

package net.fhirfactory.hie.mllpgateway.messaging;

import jakarta.jms.ConnectionFactory;
import net.fhirfactory.hie.mllpgateway.config.TaskProcessorConfig;
import org.apache.activemq.artemis.jms.client.ActiveMQConnectionFactory;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class JmsConnectionFactoryProducerTest {

    private JmsConnectionFactoryProducer producer;
    private TaskProcessorConfig config;

    @BeforeEach
    void setUp() throws Exception {
        producer = new JmsConnectionFactoryProducer();
        config = new TaskProcessorConfig();

        Field configField = JmsConnectionFactoryProducer.class.getDeclaredField("config");
        configField.setAccessible(true);
        configField.set(producer, config);
    }

    @AfterEach
    void tearDown() {
        if (producer != null) {
            producer.cleanup();
        }
    }

    @Test
    @DisplayName("Should produce ConnectionFactory singleton using config")
    void shouldProduceConnectionFactory() {
        ConnectionFactory cf1 = producer.produceConnectionFactory();
        assertNotNull(cf1);
        assertInstanceOf(ActiveMQConnectionFactory.class, cf1);

        ConnectionFactory cf2 = producer.produceConnectionFactory();
        assertSame(cf1, cf2, "Subsequent calls should return cached ConnectionFactory instance");
    }

    @Test
    @DisplayName("Should cleanup connection factory without throwing exceptions")
    void shouldCleanupGracefully() {
        producer.produceConnectionFactory();
        assertDoesNotThrow(() -> producer.cleanup());
    }
}
