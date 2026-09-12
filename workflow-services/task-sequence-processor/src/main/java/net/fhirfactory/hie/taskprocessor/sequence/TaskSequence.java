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

package net.fhirfactory.hie.taskprocessor.sequence;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.enterprise.context.Dependent;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;

import java.util.List;
import java.util.Map;

/**
 * Encapsulates a sequence of {@link TaskProcessingActivity} instances in the HIE Task Sequence Processor.
 * <p>
 * Backwards-compatible subclass extending {@link TaskSequenceImplementation}.
 * For new components, use {@link TaskSequenceDefinition} for data/persisted representations
 * and {@link TaskSequenceImplementation} for Camel/CDI runtime execution.
 */
@Dependent
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSequence extends TaskSequenceImplementation {

    private static final long serialVersionUID = 1L;

    /**
     * Default constructor.
     */
    public TaskSequence() {
        super();
    }

    /**
     * Constructor with sequence identifier and name.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     */
    public TaskSequence(String sequenceId, String sequenceName) {
        super(sequenceId, sequenceName);
    }

    /**
     * Constructor initializing from a {@link TaskSequenceDefinition}.
     *
     * @param definition TaskSequenceDefinition to copy properties from
     */
    public TaskSequence(TaskSequenceDefinition definition) {
        super(definition);
    }

    /**
     * Constructor initializing with an array of activities.
     *
     * @param activities Array of task processing activities
     */
    public TaskSequence(TaskProcessingActivity[] activities) {
        super(activities);
    }

    /**
     * Constructor initializing with a map of activities.
     *
     * @param activities Map of order index to task processing activity
     */
    public TaskSequence(Map<Integer, TaskProcessingActivity> activities) {
        super(activities);
    }

    /**
     * Constructor initializing with sequence identification and an array of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Array of task processing activities
     */
    public TaskSequence(String sequenceId, String sequenceName, TaskProcessingActivity[] activities) {
        super(sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with sequence identification and a map of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Map of order index to task processing activity
     */
    public TaskSequence(String sequenceId, String sequenceName, Map<Integer, TaskProcessingActivity> activities) {
        super(sequenceId, sequenceName, activities);
    }

    /**
     * Constructor initializing with sequence identification and a list of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   List of task processing activities
     */
    public TaskSequence(String sequenceId, String sequenceName, List<TaskProcessingActivity> activities) {
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
    public TaskSequence(CamelContext camelContext, String sequenceId, String sequenceName, TaskProcessingActivity[] activities) {
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
    public TaskSequence(CamelContext camelContext, String sequenceId, String sequenceName, Map<Integer, TaskProcessingActivity> activities) {
        super(camelContext, sequenceId, sequenceName, activities);
    }
}
