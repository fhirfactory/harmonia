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
     * Checks whether the given ID has been recorded within the sliding window.
     * Does NOT record the ID.
     *
     * @param id message or duplicate detection ID
     * @return true if known duplicate within window, false otherwise
     */
    public synchronized boolean isDuplicate(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        Instant now = Instant.now();
        Instant previous = seenIds.get(id);
        if (previous == null) {
            return false;
        }
        if (Duration.between(previous, now).compareTo(windowDuration) >= 0) {
            seenIds.remove(id);
            return false;
        }
        return true;
    }

    /**
     * Records the given ID as successfully processed at the current timestamp.
     *
     * @param id message or duplicate detection ID
     */
    public synchronized void record(String id) {
        if (id == null || id.isBlank()) {
            return;
        }
        Instant now = Instant.now();
        cleanupExpired(now);
        seenIds.put(id, now);
    }

    /**
     * Alias for {@link #record(String)} to explicitly mark an ID as processed.
     *
     * @param id message or duplicate detection ID
     */
    public synchronized void markProcessed(String id) {
        record(id);
    }

    /**
     * Removes the ID from the seen cache, for example if handling failed.
     *
     * @param id message or duplicate detection ID
     * @return true if the ID was present and removed, false otherwise
     */
    public synchronized boolean remove(String id) {
        if (id == null || id.isBlank()) {
            return false;
        }
        return seenIds.remove(id) != null;
    }

    /**
     * Alias for {@link #remove(String)} to remove an ID on failure.
     *
     * @param id message or duplicate detection ID
     * @return true if the ID was present and removed, false otherwise
     */
    public synchronized boolean removeOnFailure(String id) {
        return remove(id);
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
        if (isDuplicate(id)) {
            return false;
        }
        record(id);
        return true;
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
            }
        }
    }
}
