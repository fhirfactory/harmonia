/* Copyright (c) 2026 Mark Hunter. SPDX-License-Identifier: GPL-3.0-or-later */
package net.fhirfactory.harmonia.model.core.naming;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.Test;

/** Exercises concurrent immutable reuse with mixed match and mismatch outcomes. */
class ConcurrencyTest {

    @Test
    void sharedMaskAndDescriptionsRemainStableAcrossConcurrentOutcomes() throws Exception {
        TopicMask shared = TopicMask.builder()
                .addPatterns("System", "Harmonia")
                .addPatterns("Component", "Calliope*")
                .addPatterns("Release", "Stable", ">=1.2,<2.0")
                .includeContainedElements(true)
                .build();
        Topic matching = Topic.builder().addEntry("System", "Harmonia")
                .addEntry("Component", "Calliope Core")
                .addEntry("Release", "Stable", "1.10")
                .addEntry("Action", "Read").build();
        List<Topic> mismatches = List.of(
                Topic.builder().addEntry("Wrong", "Harmonia").addEntry("Component", "Calliope Core")
                        .addEntry("Release", "Stable", "1.10").build(),
                Topic.builder().addEntry("System", "Harmonia").addEntry("Component", "Calliope Core")
                        .addEntry("Release", "Stable", "2.0").build(),
                Topic.builder().addEntry("System", "Harmonia").addEntry("Component", "Core Calliope")
                        .addEntry("Release", "Stable", "1.10").build(),
                Topic.builder().addEntry("System", "Harmonia").build());

        ExecutorService workers = Executors.newFixedThreadPool(8);
        try {
            List<Callable<Boolean>> tasks = new ArrayList<>();
            for (int worker = 0; worker < 8; worker++) {
                int workerNumber = worker;
                tasks.add(() -> {
                    for (int iteration = 0; iteration < 250; iteration++) {
                        boolean expected = (iteration + workerNumber) % (mismatches.size() + 1) == 0;
                        Topic candidate = expected ? matching
                                : mismatches.get((iteration + workerNumber - 1) % mismatches.size());
                        if (shared.matches(candidate) != expected) {
                            return false;
                        }
                    }
                    return true;
                });
            }
            List<Future<Boolean>> results = workers.invokeAll(tasks, 5, TimeUnit.SECONDS);
            for (Future<Boolean> result : results) {
                assertTrue(result.get(1, TimeUnit.SECONDS));
            }
        } finally {
            workers.shutdownNow();
            assertTrue(workers.awaitTermination(1, TimeUnit.SECONDS));
        }
    }
}