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
}
