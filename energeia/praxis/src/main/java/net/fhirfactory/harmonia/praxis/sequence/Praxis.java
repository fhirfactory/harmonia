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

package net.fhirfactory.harmonia.praxis.sequence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
import net.fhirfactory.harmonia.erga.base.ErgonBase;
import org.apache.camel.CamelContext;

import java.util.List;
import java.util.Map;

/**
 * Encapsulates a sequence of {@link ErgonBase} instances in the HIE Task Sequence Processor.
 * <p>
 * Backwards-compatible subclass extending {@link PraxisImplementation}.
 * For new components, use {@link PraxisDefinition} for data/persisted representations
 * and {@link PraxisImplementation} for Camel/CDI runtime execution.
 */
@Dependent
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Praxis extends PraxisImplementation {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public Praxis() {
        super();
    }

    /**
     * Constructor with sequence identifier and name.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     */
    public Praxis(String sequenceId, String sequenceName) {
        super(sequenceId, sequenceName);
    }

    /**
     * Constructor initializing from a {@link PraxisDefinition}.
     *
     * @param definition PraxisDefinition to copy properties from
     */
    public Praxis(PraxisDefinition definition) {
        super(definition);
    }

    /**
     * Constructor initializing with an array of activities.
     *
     * @param activities Array of task processing activities
     */
    public Praxis(ErgonBase[] activities) {
        super(activities);
    }

    /**
     * Constructor initializing with a map of activities.
     *
     * @param activities Map of order index to task processing activity
     */
    public Praxis(Map<Integer, ErgonBase> activities) {
        super(activities);
    }

    /**
     * Constructor initializing with sequence identification and an array of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Array of task processing activities
     */
    public Praxis(String sequenceId, String sequenceName, ErgonBase[] activities) {
        super(sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with sequence identification and a map of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Map of order index to task processing activity
     */
    public Praxis(String sequenceId, String sequenceName, Map<Integer, ErgonBase> activities) {
        super(sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with sequence identification and a list of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   List of task processing activities
     */
    public Praxis(String sequenceId, String sequenceName, List<ErgonBase> activities) {
        super(sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with CamelContext, identification, and an array of activities.
     *
     * @param camelContext CamelContext instance
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Array of task processing activities
     */
    public Praxis(CamelContext camelContext, String sequenceId, String sequenceName, ErgonBase[] activities) {
        super(camelContext, sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with CamelContext, identification, and a map of activities.
     *
     * @param camelContext CamelContext instance
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Map of order index to task processing activity
     */
    public Praxis(CamelContext camelContext, String sequenceId, String sequenceName, Map<Integer, ErgonBase> activities) {
        super(camelContext, sequenceId, sequenceName, activities);
    }
}
