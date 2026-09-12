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

package net.fhirfactory.hie.taskprocessor.service;

import net.fhirfactory.hie.model.queue.MessageQueueDefinition;
import net.fhirfactory.hie.taskprocessor.config.QueueConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class MessageQueueServiceTest {

    private MessageQueueService service;
    private QueueConfig queueConfig;

    @BeforeEach
    void setUp() {
        queueConfig = new QueueConfig();
        service = new MessageQueueService(null, queueConfig);
        service.init();
    }

    @Test
    void testSeedAndGetAll() {
        List<MessageQueueDefinition> queues = service.getAll();
        assertNotNull(queues);
        assertFalse(queues.isEmpty());
        assertTrue(queues.stream().anyMatch(q -> "task.processing.queue".equals(q.getQueueName())));
        assertTrue(queues.stream().anyMatch(q -> "task.event.queue".equals(q.getQueueName())));
    }

    @Test
    void testSaveAndGetById() {
        MessageQueueDefinition q = new MessageQueueDefinition("custom.test.queue", "ANYCAST", true, "Custom Test Queue");
        service.save(q);

        Optional<MessageQueueDefinition> retrieved = service.getById("custom.test.queue");
        assertTrue(retrieved.isPresent());
        assertEquals("custom.test.queue", retrieved.get().getQueueName());
        assertTrue(retrieved.get().isDurable());
    }

    @Test
    void testDelete() {
        MessageQueueDefinition q = new MessageQueueDefinition("to.delete.queue");
        service.save(q);

        assertTrue(service.getById("to.delete.queue").isPresent());
        boolean deleted = service.delete("to.delete.queue");
        assertTrue(deleted);
        assertFalse(service.getById("to.delete.queue").isPresent());
    }
}
