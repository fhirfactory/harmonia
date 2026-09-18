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

package net.fhirfactory.harmonia.paradeigma.common.generator;

import java.util.List;
import java.util.Random;

/**
 * Seedable pseudo-random number generator wrapper ensuring 100% deterministic reproducibility.
 */
public class SeedRandom {

    private final Random random;
    private final long initialSeed;

    public SeedRandom() {
        this(System.currentTimeMillis());
    }

    public SeedRandom(long seed) {
        this.initialSeed = seed;
        this.random = new Random(seed);
    }

    public long getInitialSeed() {
        return initialSeed;
    }

    public int nextInt(int bound) {
        return random.nextInt(bound);
    }

    public int nextInt(int min, int max) {
        if (min >= max) {
            return min;
        }
        return min + random.nextInt(max - min + 1);
    }

    public long nextLong(long min, long max) {
        if (min >= max) {
            return min;
        }
        return min + (long) (random.nextDouble() * (max - min + 1));
    }

    public double nextDouble() {
        return random.nextDouble();
    }

    public boolean nextBoolean() {
        return random.nextBoolean();
    }

    public boolean nextBoolean(double trueProbability) {
        return random.nextDouble() < trueProbability;
    }

    public <T> T pick(List<T> items) {
        if (items == null || items.isEmpty()) {
            return null;
        }
        return items.get(random.nextInt(items.size()));
    }

    public <T> T pick(T[] items) {
        if (items == null || items.length == 0) {
            return null;
        }
        return items[random.nextInt(items.length)];
    }
}
