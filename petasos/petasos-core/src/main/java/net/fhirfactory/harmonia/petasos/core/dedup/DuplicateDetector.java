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

package net.fhirfactory.harmonia.petasos.core.dedup;

import java.time.Duration;
import java.time.Instant;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * In-memory sliding window cache for client-side message deduplication.
 * <p>
 * Supplements Artemis broker-side duplicate ID cache by protecting consumers
 * against repeated processing of identical message or duplicate detection IDs.
 */
public final class DuplicateDetector {

    private final int maxEntries;
    private final Duration windowDuration;
    private final Map<String, Instant> seenIds;

    public DuplicateDetector(int maxEntries, Duration windowDuration) {
        this.maxEntries = maxEntries > 0 ? maxEntries : 10000;
        this.windowDuration = windowDuration != null ? windowDuration : Duration.ofHours(1);
        this.seenIds = new LinkedHashMap<>(128, 0.75f, true) {
            @Override
            protected boolean removeEldestEntry(Map.Entry<String, Instant> eldest) {
                return size() > DuplicateDetector.this.maxEntries;
            }
        };
    }

    public DuplicateDetector() {
        this(10000, Duration.ofHours(1));
    }

    /**
     * Checks whether the given message or duplicate ID has been seen within the sliding window.
     * If not seen, records the ID and returns true (unique). If seen, returns false (duplicate).
     *
     * @param id message or duplicate detection ID
     * @return true if first time seen (unique), false if duplicate
     */
    public synchronized boolean isUnique(String id) {
        if (id == null || id.isBlank()) {
            return true;
        }

        Instant now = Instant.now();
        cleanupExpired(now);

        Instant previous = seenIds.get(id);
        if (previous != null) {
            if (Duration.between(previous, now).compareTo(windowDuration) < 0) {
                return false; // Duplicate within window
            }
        }

        seenIds.put(id, now);
        return true;
    }

    /**
     * Checks if ID is known duplicate without recording it.
     */
    public synchronized boolean isDuplicate(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        Instant previous = seenIds.get(id);
        if (previous == null) {
            return false;
        }
        return Duration.between(previous, Instant.now()).compareTo(windowDuration) < 0;
    }

    public synchronized void clear() {
        seenIds.clear();
    }

    public synchronized int size() {
        return seenIds.size();
    }

    private void cleanupExpired(Instant now) {
        if (seenIds.size() < 100) {
            return;
        }
        Iterator<Map.Entry<String, Instant>> it = seenIds.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Instant> entry = it.next();
            if (Duration.between(entry.getValue(), now).compareTo(windowDuration) >= 0) {
                it.remove();
            } else {
                break; // Since access-ordered, older entries come first
            }
        }
    }
}
