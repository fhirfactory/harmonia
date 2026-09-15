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

package net.fhirfactory.harmonia.model.petasos;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PetasosQueueDefinitionTest {

    private final ObjectMapper mapper = new ObjectMapper();

    @Test
    void testConstructorsAndProperties() {
        PetasosQueueDefinition q = new PetasosQueueDefinition("task.processing.queue", "ANYCAST", false, "Task processing queue");
        assertEquals("task.processing.queue", q.getQueueId());
        assertEquals("task.processing.queue", q.getQueueName());
        assertEquals("task.processing.queue", q.getAddress());
        assertEquals("ANYCAST", q.getRoutingType());
        assertFalse(q.isDurable());
        assertTrue(q.isEnabled());
        assertEquals("Task processing queue", q.getDescription());

        PetasosQueueDefinition q2 = new PetasosQueueDefinition("gw-queue", "task.event.queue.pas-gw", "task.event.queue.pas-gw", "ANYCAST", true, "PAS queue", "pas-gw");
        assertEquals("gw-queue", q2.getQueueId());
        assertEquals("task.event.queue.pas-gw", q2.getQueueName());
        assertEquals("task.event.queue.pas-gw", q2.getAddress());
        assertTrue(q2.isDurable());
        assertEquals("pas-gw", q2.getGatewayInstanceId());
    }

    @Test
    void testSerializationAndDeserialization() throws Exception {
        PetasosQueueDefinition original = new PetasosQueueDefinition("q1", "task.test.queue", "task.test.queue", "ANYCAST", false, "Test Queue", "test-gw");
        original.setMaxConsumers(10);
        original.setFilter("HIE_TRIGGER_TYPE = 'A01'");

        String json = mapper.writeValueAsString(original);
        PetasosQueueDefinition deserialized = mapper.readValue(json, PetasosQueueDefinition.class);

        assertEquals(original.getQueueId(), deserialized.getQueueId());
        assertEquals(original.getQueueName(), deserialized.getQueueName());
        assertEquals(original.getAddress(), deserialized.getAddress());
        assertEquals(original.getRoutingType(), deserialized.getRoutingType());
        assertEquals(original.getDescription(), deserialized.getDescription());
        assertEquals(original.getGatewayInstanceId(), deserialized.getGatewayInstanceId());
        assertEquals(10, deserialized.getMaxConsumers());
        assertEquals("HIE_TRIGGER_TYPE = 'A01'", deserialized.getFilter());
        assertEquals(original, deserialized);
    }
}
