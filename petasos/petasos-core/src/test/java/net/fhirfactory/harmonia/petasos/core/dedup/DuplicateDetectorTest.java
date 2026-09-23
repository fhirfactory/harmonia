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

import org.junit.jupiter.api.Test;

import java.time.Duration;

import static org.assertj.core.api.Assertions.assertThat;

class DuplicateDetectorTest {

    @Test
    void testUniquenessAndDeduplication() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));

        assertThat(detector.isUnique("msg-1")).isTrue();
        assertThat(detector.isUnique("msg-2")).isTrue();

        // Second time msg-1 is seen -> duplicate
        assertThat(detector.isUnique("msg-1")).isFalse();
        assertThat(detector.isDuplicate("msg-1")).isTrue();
        assertThat(detector.isDuplicate("msg-3")).isFalse();
    }

    @Test
    void testDistinctInspectionAndRecording() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));

        // Inspection before recording must NOT record or mark duplicate
        assertThat(detector.isDuplicate("msg-100")).isFalse();
        assertThat(detector.isDuplicate("msg-100")).isFalse();
        assertThat(detector.size()).isEqualTo(0);

        // Record on success
        detector.record("msg-100");
        assertThat(detector.size()).isEqualTo(1);
        assertThat(detector.isDuplicate("msg-100")).isTrue();

        // Distinct ID remains non-duplicate
        assertThat(detector.isDuplicate("msg-101")).isFalse();
    }

    @Test
    void testMarkProcessedAlias() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));

        assertThat(detector.isDuplicate("msg-alias")).isFalse();
        detector.markProcessed("msg-alias");
        assertThat(detector.isDuplicate("msg-alias")).isTrue();
    }

    @Test
    void testRemoveAndRemoveOnFailure() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));

        detector.record("msg-fail-1");
        detector.record("msg-fail-2");
        assertThat(detector.isDuplicate("msg-fail-1")).isTrue();
        assertThat(detector.isDuplicate("msg-fail-2")).isTrue();

        assertThat(detector.remove("msg-fail-1")).isTrue();
        assertThat(detector.isDuplicate("msg-fail-1")).isFalse();

        assertThat(detector.removeOnFailure("msg-fail-2")).isTrue();
        assertThat(detector.isDuplicate("msg-fail-2")).isFalse();

        // Removing non-existent ID returns false
        assertThat(detector.remove("non-existent")).isFalse();
    }

    @Test
    void testNullAndBlankHandling() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));

        assertThat(detector.isDuplicate(null)).isFalse();
        assertThat(detector.isDuplicate("   ")).isFalse();
        assertThat(detector.isUnique(null)).isTrue();
        assertThat(detector.isUnique("   ")).isTrue();

        detector.record(null);
        detector.record("   ");
        assertThat(detector.size()).isEqualTo(0);

        assertThat(detector.remove(null)).isFalse();
        assertThat(detector.remove("   ")).isFalse();
    }

    @Test
    void testWindowDurationExpiry() throws InterruptedException {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMillis(50));

        detector.record("msg-expiring");
        assertThat(detector.isDuplicate("msg-expiring")).isTrue();

        Thread.sleep(70);

        // After window duration expires, isDuplicate should return false
        assertThat(detector.isDuplicate("msg-expiring")).isFalse();
    }

    @Test
    void testMaxSizeEviction() {
        DuplicateDetector detector = new DuplicateDetector(3, Duration.ofMinutes(10));

        detector.isUnique("id-1");
        detector.isUnique("id-2");
        detector.isUnique("id-3");
        assertThat(detector.size()).isEqualTo(3);

        detector.isUnique("id-4");
        // id-1 was evicted
        assertThat(detector.isDuplicate("id-1")).isFalse();
        assertThat(detector.isDuplicate("id-4")).isTrue();
    }

    @Test
    void testClear() {
        DuplicateDetector detector = new DuplicateDetector(100, Duration.ofMinutes(5));
        detector.record("id-1");
        detector.record("id-2");
        assertThat(detector.size()).isEqualTo(2);

        detector.clear();
        assertThat(detector.size()).isEqualTo(0);
        assertThat(detector.isDuplicate("id-1")).isFalse();
    }
}
