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

package net.fhirfactory.harmonia.praxis.config;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import net.fhirfactory.harmonia.themis.api.ThemisService;
import net.fhirfactory.harmonia.themis.core.evaluator.DeterministicPolicyEvaluator;

/**
 * CDI configuration and producer for Themis policy evaluation services in Ponos.
 */
@ApplicationScoped
public class ThemisConfig {

    private final ThemisService themisService = DeterministicPolicyEvaluator.withDefaultPolicies();

    @Produces
    @ApplicationScoped
    public ThemisService produceThemisService() {
        return themisService;
    }
}
