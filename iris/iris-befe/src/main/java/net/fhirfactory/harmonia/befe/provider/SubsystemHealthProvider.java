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

package net.fhirfactory.harmonia.befe.provider;

import net.fhirfactory.harmonia.befe.model.operations.OperationalHealth;
import net.fhirfactory.harmonia.befe.model.operations.OperationalInstance;
import net.fhirfactory.harmonia.befe.model.operations.OperationalSubsystem;
import net.fhirfactory.harmonia.befe.model.operations.TimeSeries;

import java.util.List;
import java.util.Map;

/**
 * Service Provider Interface (SPI) for Harmonia Subsystem Health Providers.
 * Each Harmonia subsystem (Pylai, Petasos, Energeia, Mneme, Mnemosyne, Calliope, Themis, Agora, Iris)
 * implements this SPI to supply normalized operational health, runtime instances, and time-series metrics.
 */
public interface SubsystemHealthProvider {

    /**
     * Unique identifier for the subsystem (e.g. "petasos", "energeia").
     */
    String getSubsystemId();

    /**
     * Canonical display name of the subsystem (e.g. "Petasos", "Energeia").
     */
    String getSubsystemName();

    /**
     * Operational description of subsystem responsibilities.
     */
    String getDescription();

    /**
     * Subsystem version string.
     */
    String getVersion();

    /**
     * Returns the normalized operational overview and status for this subsystem.
     */
    OperationalSubsystem getSubsystemOverview();

    /**
     * Returns granular operational health metrics and downstream dependency statuses.
     */
    OperationalHealth getOperationalHealth();

    /**
     * Returns discovered runtime instances (Pods or standalone processes) for this subsystem.
     */
    List<OperationalInstance> getInstances();

    /**
     * Returns metric sparklines / time-series data points for the given time window (15m, 1h, 6h, 24h).
     */
    Map<String, TimeSeries> getStatistics(String window);

    /**
     * Indicates whether this subsystem telemetry source is currently reachable.
     */
    boolean isAvailable();
}
