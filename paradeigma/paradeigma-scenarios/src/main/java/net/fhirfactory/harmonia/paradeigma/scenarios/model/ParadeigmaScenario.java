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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.paradeigma.scenarios.model;

import net.fhirfactory.harmonia.paradeigma.common.security.SecurityScenarioContext;

/**
 * Common contract for executable Paradeigma simulation scenarios.
 *
 * @param <R> the scenario execution result type
 */
public interface ParadeigmaScenario<R> {

    String getScenarioId();

    String getName();

    String getDescription();

    long getSeed();

    SecurityScenarioContext getActor();

    ScenarioExpectation getExpectation();

    R execute();
}
