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

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.Dependent;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.model.topic.TopicSubscription;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import org.apache.camel.CamelContext;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;

/**
 * Encapsulates the runtime implementation and execution orchestration of a Task Sequence.
 * <p>
 * Extends {@link TaskSequenceDefinition} with Apache Camel route orchestration,
 * CDI dependency injection discovery of {@link TaskProcessingActivity} beans,
 * runtime activity lifecycle operations, endpoint chaining, and pipeline execution.
 */
@Dependent
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class TaskSequenceImplementation extends TaskSequenceDefinition {

    private static final long serialVersionUID = 1L;
    private static final Logger log = LoggerFactory.getLogger(TaskSequenceImplementation.class);

    @JsonIgnore
    private Map<Integer, TaskProcessingActivity> activities = new TreeMap<>();

    @JsonIgnore
    private CamelContext camelContext;

    @Inject
    @Any
    @JsonIgnore
    private Instance<TaskProcessingActivity> injectedActivities;

    /**
     * Default constructor.
     */
    public TaskSequenceImplementation() {
        super();
    }

    /**
     * Constructor with sequence identifier and name.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     */
    public TaskSequenceImplementation(String sequenceId, String sequenceName) {
        super(sequenceId, sequenceName);
    }

    /**
     * Constructor initializing from a {@link TaskSequenceDefinition}.
     *
     * @param definition TaskSequenceDefinition to copy properties from
     */
    public TaskSequenceImplementation(TaskSequenceDefinition definition) {
        super(definition);
    }

    /**
     * Constructor initializing with an array of activities.
     *
     * @param activities Array of task processing activities
     */
    public TaskSequenceImplementation(TaskProcessingActivity[] activities) {
        this();
        setActivities(activities);
    }

    /**
     * Constructor initializing with a map of activities.
     *
     * @param activities Map of order index to task processing activity
     */
    public TaskSequenceImplementation(Map<Integer, TaskProcessingActivity> activities) {
        this();
        setActivities(activities);
    }

    /**
     * Constructor initializing with sequence identification and an array of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Array of task processing activities
     */
    public TaskSequenceImplementation(String sequenceId, String sequenceName, TaskProcessingActivity[] activities) {
        super(sequenceId, sequenceName);
        setActivities(activities);
    }

    /**
     * Constructor initializing with sequence identification and a map of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Map of order index to task processing activity
     */
    public TaskSequenceImplementation(String sequenceId, String sequenceName, Map<Integer, TaskProcessingActivity> activities) {
        super(sequenceId, sequenceName);
        setActivities(activities);
    }

    /**
     * Constructor initializing with sequence identification and a list of activities.
     *
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   List of task processing activities
     */
    public TaskSequenceImplementation(String sequenceId, String sequenceName, List<TaskProcessingActivity> activities) {
        super(sequenceId, sequenceName);
        setActivities(activities);
    }

    /**
     * Constructor initializing with CamelContext, identification, and an array of activities.
     *
     * @param camelContext CamelContext instance
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Array of task processing activities
     */
    public TaskSequenceImplementation(CamelContext camelContext, String sequenceId, String sequenceName, TaskProcessingActivity[] activities) {
        super(sequenceId, sequenceName);
        this.camelContext = camelContext;
        setActivities(activities);
    }

    /**
     * Constructor initializing with CamelContext, identification, and a map of activities.
     *
     * @param camelContext CamelContext instance
     * @param sequenceId   Unique sequence identifier
     * @param sequenceName Human-readable sequence name
     * @param activities   Map of order index to task processing activity
     */
    public TaskSequenceImplementation(CamelContext camelContext, String sequenceId, String sequenceName, Map<Integer, TaskProcessingActivity> activities) {
        super(sequenceId, sequenceName);
        this.camelContext = camelContext;
        setActivities(activities);
    }

    /**
     * CDI PostConstruct hook to populate activities from CDI injection if not already configured.
     */
    @PostConstruct
    public void init() {
        if ((this.activities == null || this.activities.isEmpty()) && injectedActivities != null && !injectedActivities.isUnsatisfied()) {
            Map<Integer, TaskProcessingActivity> discovered = new TreeMap<>();
            int order = 0;
            for (TaskProcessingActivity activity : injectedActivities) {
                if (activity != null) {
                    discovered.put(order++, activity);
                }
            }
            if (!discovered.isEmpty()) {
                log.info("Discovered {} injected TaskProcessingActivity instance(s) for sequence [{}]",
                        discovered.size(), getSequenceName());
                this.activities = discovered;
                syncActivityIds();
            }
        }
    }

    /**
     * Synchronizes activity identifiers and class names with the configured activity map.
     */
    public void syncActivityIds() {
        if (activities != null && !activities.isEmpty()) {
            Map<Integer, String> ids = new TreeMap<>();
            Map<Integer, String> classes = new TreeMap<>();
            for (Map.Entry<Integer, TaskProcessingActivity> entry : new TreeMap<>(activities).entrySet()) {
                TaskProcessingActivity act = entry.getValue();
                if (act != null) {
                    if (act.getActivityId() != null) {
                        ids.put(entry.getKey(), act.getActivityId());
                    }
                    classes.put(entry.getKey(), act.getClass().getName());
                }
            }
            setActivityIds(ids);
            setActivityClassNames(classes);
        }
    }

    /**
     * Converts this implementation instance into a pure {@link TaskSequenceDefinition}.
     *
     * @return a new TaskSequenceDefinition instance containing definition metadata
     */
    public TaskSequenceDefinition toDefinition() {
        syncActivityIds();
        return new TaskSequenceDefinition(this);
    }

    // =========================================================================
    // Activity Map Management
    // =========================================================================

    /**
     * Gets the current map of order index to task processing activities.
     *
     * @return a map of order index to {@link TaskProcessingActivity}
     */
    @JsonIgnore
    public Map<Integer, TaskProcessingActivity> getActivities() {
        if (activities == null) {
            return new TreeMap<>();
        }
        return new TreeMap<>(activities);
    }

    /**
     * Sets the map of order index to task processing activities.
     *
     * @param activities Map of order index to activities
     */
    public void setActivities(Map<Integer, TaskProcessingActivity> activities) {
        if (activities == null) {
            this.activities = new TreeMap<>();
            setActivityIds(new TreeMap<>());
            setActivityClassNames(new TreeMap<>());
        } else {
            this.activities = new TreeMap<>(activities);
            syncActivityIds();
        }
    }

    /**
     * Sets the activities from an array, mapping each element to its array index (order entry).
     *
     * @param activities Array of activities
     */
    public void setActivities(TaskProcessingActivity[] activities) {
        this.activities = new TreeMap<>();
        if (activities != null) {
            for (int i = 0; i < activities.length; i++) {
                if (activities[i] != null) {
                    this.activities.put(i, activities[i]);
                }
            }
            syncActivityIds();
        } else {
            setActivityIds(new TreeMap<>());
            setActivityClassNames(new TreeMap<>());
        }
    }

    /**
     * Sets the activities from a list, mapping each element to its list index (order entry).
     *
     * @param activityList List of activities
     */
    public void setActivities(List<TaskProcessingActivity> activityList) {
        this.activities = new TreeMap<>();
        if (activityList != null) {
            for (int i = 0; i < activityList.size(); i++) {
                if (activityList.get(i) != null) {
                    this.activities.put(i, activityList.get(i));
                }
            }
            syncActivityIds();
        } else {
            setActivityIds(new TreeMap<>());
            setActivityClassNames(new TreeMap<>());
        }
    }

    /**
     * Returns the activities in sequence order as an unmodifiable list view.
     *
     * @return List of {@link TaskProcessingActivity} ordered by key
     */
    @JsonIgnore
    public List<TaskProcessingActivity> getActivityList() {
        if (activities == null || activities.isEmpty()) {
            return Collections.emptyList();
        }
        List<TaskProcessingActivity> ordered = new ArrayList<>();
        for (TaskProcessingActivity act : new TreeMap<>(activities).values()) {
            if (act != null) {
                ordered.add(act);
            }
        }
        return Collections.unmodifiableList(ordered);
    }

    /**
     * Appends a task processing activity to the sequence map with the next sequential order entry.
     *
     * @param activity TaskProcessingActivity to append
     */
    public synchronized void addActivity(TaskProcessingActivity activity) {
        Objects.requireNonNull(activity, "TaskProcessingActivity must not be null");
        int nextOrder = 0;
        if (activities != null && !activities.isEmpty()) {
            nextOrder = Collections.max(activities.keySet()) + 1;
        }
        addActivity(nextOrder, activity);
    }

    /**
     * Adds an activity at a specified order entry in the sequence map.
     *
     * @param order    Order index entry
     * @param activity Activity to insert
     */
    public synchronized void addActivity(int order, TaskProcessingActivity activity) {
        Objects.requireNonNull(activity, "TaskProcessingActivity must not be null");
        if (this.activities == null) {
            this.activities = new TreeMap<>();
        }
        this.activities.put(order, activity);
        syncActivityIds();
    }

    /**
     * Appends multiple task processing activities to the sequence map.
     *
     * @param newActivities Varargs of activities to append
     */
    public synchronized void addActivities(TaskProcessingActivity... newActivities) {
        if (newActivities == null || newActivities.length == 0) {
            return;
        }
        for (TaskProcessingActivity act : newActivities) {
            if (act != null) {
                addActivity(act);
            }
        }
    }

    /**
     * Removes the specified activity from the sequence map.
     *
     * @param activity Activity to remove
     * @return true if an instance was removed, false otherwise
     */
    public synchronized boolean removeActivity(TaskProcessingActivity activity) {
        if (activity == null || activities == null || activities.isEmpty()) {
            return false;
        }
        Integer foundKey = null;
        for (Map.Entry<Integer, TaskProcessingActivity> entry : activities.entrySet()) {
            if (Objects.equals(entry.getValue(), activity)) {
                foundKey = entry.getKey();
                break;
            }
        }
        if (foundKey != null) {
            activities.remove(foundKey);
            if (activities.isEmpty()) {
                setActivityIds(new TreeMap<>());
                setActivityClassNames(new TreeMap<>());
            } else {
                syncActivityIds();
            }
            return true;
        }
        return false;
    }

    /**
     * Removes an activity at the specified order entry.
     *
     * @param order Order entry index of the activity to remove
     * @return the removed TaskProcessingActivity, or null if not found
     */
    public synchronized TaskProcessingActivity removeActivity(int order) {
        if (activities == null || !activities.containsKey(order)) {
            return null;
        }
        TaskProcessingActivity removed = activities.remove(order);
        if (activities.isEmpty()) {
            setActivityIds(new TreeMap<>());
            setActivityClassNames(new TreeMap<>());
        } else {
            syncActivityIds();
        }
        return removed;
    }

    /**
     * Removes an activity with matching activityId from the sequence map.
     *
     * @param activityId Unique identifier of activity to remove
     * @return true if an activity was removed, false otherwise
     */
    public synchronized boolean removeActivityById(String activityId) {
        if (activityId == null || activities == null || activities.isEmpty()) {
            return false;
        }
        Integer foundKey = null;
        for (Map.Entry<Integer, TaskProcessingActivity> entry : activities.entrySet()) {
            if (entry.getValue() != null && activityId.equals(entry.getValue().getActivityId())) {
                foundKey = entry.getKey();
                break;
            }
        }
        if (foundKey != null) {
            activities.remove(foundKey);
            if (activities.isEmpty()) {
                setActivityIds(new TreeMap<>());
                setActivityClassNames(new TreeMap<>());
            } else {
                syncActivityIds();
            }
            return true;
        }
        return false;
    }

    /**
     * Retrieves an activity by its order entry key.
     *
     * @param order Order index of activity
     * @return TaskProcessingActivity at order, or null if not found
     */
    public TaskProcessingActivity getActivity(int order) {
        if (activities == null) {
            return null;
        }
        return activities.get(order);
    }

    /**
     * Finds an activity by its activity ID across all map entries.
     *
     * @param activityId Unique activity ID
     * @return matching {@link TaskProcessingActivity} or null if not found
     */
    public TaskProcessingActivity getActivityById(String activityId) {
        if (activityId == null || activities == null) {
            return null;
        }
        for (TaskProcessingActivity activity : activities.values()) {
            if (activity != null && activityId.equals(activity.getActivityId())) {
                return activity;
            }
        }
        return null;
    }

    /**
     * Checks if the sequence contains an activity with the given activity ID.
     *
     * @param activityId Activity ID to check
     * @return true if present, false otherwise
     */
    public boolean containsActivity(String activityId) {
        return getActivityById(activityId) != null;
    }

    /**
     * Returns the total count of activities in the sequence map.
     *
     * @return activity count
     */
    public int getActivityCount() {
        return activities != null ? activities.size() : 0;
    }

    /**
     * Synonym for {@link #getActivityCount()}.
     *
     * @return activity count
     */
    public int size() {
        return getActivityCount();
    }

    /**
     * Checks if the sequence has no activities.
     *
     * @return true if empty, false otherwise
     */
    public boolean isEmpty() {
        return getActivityCount() == 0;
    }

    /**
     * Clears all activities from the sequence.
     */
    public synchronized void clearActivities() {
        if (this.activities != null) {
            this.activities.clear();
        } else {
            this.activities = new TreeMap<>();
        }
        setActivityIds(new TreeMap<>());
        setActivityClassNames(new TreeMap<>());
    }

    /**
     * Returns a list of all enabled activities in the sequence ordered by key.
     *
     * @return List of enabled activities
     */
    @JsonIgnore
    public List<TaskProcessingActivity> getEnabledActivities() {
        if (activities == null || activities.isEmpty()) {
            return Collections.emptyList();
        }
        List<TaskProcessingActivity> enabledList = new ArrayList<>();
        for (TaskProcessingActivity activity : new TreeMap<>(activities).values()) {
            if (activity != null && activity.isEnabled()) {
                enabledList.add(activity);
            }
        }
        return enabledList;
    }

    // =========================================================================
    // Camel Integration & Route Orchestration
    // =========================================================================

    /**
     * Registers all activity routes within the provided CamelContext in sequential order.
     *
     * @param context CamelContext to register routes with
     * @throws Exception if route registration fails
     */
    public void registerRoutes(CamelContext context) throws Exception {
        Objects.requireNonNull(context, "CamelContext must not be null");
        if (!isEnabled()) {
            log.warn("TaskSequenceImplementation [{}] (id={}) is disabled, skipping route registration", getSequenceName(), getSequenceId());
            return;
        }

        log.info("Registering routes for TaskSequenceImplementation [{}] (id={}, {} activities) into CamelContext",
                getSequenceName(), getSequenceId(), getActivityCount());

        for (TaskProcessingActivity activity : getActivityList()) {
            if (activity != null && activity.isEnabled()) {
                log.debug("Adding route for activity [{}] (id={}) to CamelContext",
                        activity.getActivityName(), activity.getActivityId());
                context.addRoutes(activity);
            }
        }
    }

    /**
     * Registers all activity routes in the internal CamelContext if set.
     *
     * @throws Exception if route registration fails
     */
    public void registerRoutes() throws Exception {
        if (camelContext == null) {
            throw new IllegalStateException("CamelContext is not set on TaskSequenceImplementation [" + getSequenceId() + "]");
        }
        registerRoutes(camelContext);
    }

    /**
     * Automatically configures chained direct endpoints connecting sequential activities.
     * <p>
     * For activity i, outputEndpoint connects to direct endpoint of activity i+1 inputEndpoint.
     */
    public void configureChainedEndpoints() {
        List<TaskProcessingActivity> ordered = getActivityList();
        if (ordered.isEmpty()) {
            return;
        }

        String prefix = "direct:seq-" + (getSequenceId() != null ? getSequenceId().toLowerCase().replaceAll("[^a-z0-9-_]", "-") : "default");

        for (int i = 0; i < ordered.size(); i++) {
            TaskProcessingActivity current = ordered.get(i);
            if (current == null) {
                continue;
            }

            if (i == 0) {
                if (current.getInputEndpoint() == null) {
                    if (current instanceof net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit) {
                        net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit conduit =
                                (net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit) current;
                        if (getSourceQueueName() != null && !getSourceQueueName().isBlank()) {
                            String sq = getSourceQueueName().startsWith("jms:") || getSourceQueueName().startsWith("direct:")
                                    ? getSourceQueueName()
                                    : "jms:queue:" + getSourceQueueName();
                            current.setInputEndpoint(sq);
                        } else if (getInputEndpoint() != null && !getInputEndpoint().isBlank()) {
                            current.setInputEndpoint(getInputEndpoint());
                        } else {
                            current.setInputEndpoint(conduit.resolveInputEndpoint());
                        }
                    } else {
                        current.setInputEndpoint(prefix + "-step-1");
                    }
                }
            }

            if (i < ordered.size() - 1) {
                String intermediateEndpoint = prefix + "-step-" + (i + 2);
                current.setOutputEndpoint(intermediateEndpoint);
                TaskProcessingActivity next = ordered.get(i + 1);
                if (next != null) {
                    next.setInputEndpoint(intermediateEndpoint);
                }
            } else {
                if (getOutputEndpoint() != null && !getOutputEndpoint().isBlank()) {
                    current.setOutputEndpoint(getOutputEndpoint());
                } else {
                    current.setOutputEndpoint(null);
                }
            }

            if (current.getErrorEndpoint() == null && getErrorEndpoint() != null) {
                current.setErrorEndpoint(getErrorEndpoint());
            }
        }
    }

    /**
     * Gets the standard pipeline entry point direct endpoint URI for this sequence.
     */
    @JsonIgnore
    public String getPipelineInputEndpoint() {
        return "direct:pipeline-" + generateSequenceRouteId() + "-in";
    }

    /**
     * Builds a Camel {@link RouteBuilder} establishing a pipeline orchestrating the sequential flow of all activities.
     *
     * @return RouteBuilder for the sequence pipeline
     */
    public RouteBuilder createSequencePipelineRoute() {
        return new RouteBuilder() {
            @Override
            public void configure() {
                List<TaskProcessingActivity> ordered = getActivityList();
                if (!isEnabled() || ordered.isEmpty()) {
                    return;
                }

                String pipelineInput = getPipelineInputEndpoint();
                String firstActivityInput = ordered.get(0) != null && ordered.get(0).getInputEndpoint() != null
                        ? ordered.get(0).getInputEndpoint()
                        : (getInputEndpoint() != null && !getInputEndpoint().isBlank() ? getInputEndpoint() : "direct:seq-" + generateSequenceRouteId() + "-step-0");

                // Main sequence pipeline route with topic subscription & gateway/trigger filtering
                from(pipelineInput)
                        .routeId("sequence-pipeline-" + generateSequenceRouteId())
                        .routeDescription(getSequenceDescription() != null ? getSequenceDescription() : getSequenceName())
                        .filter(exchange -> {
                            Topic topic = exchange.getMessage().getHeader("HIE_TOPIC", Topic.class);
                            if (topic != null) {
                                return matches(topic);
                            }
                            String gw = exchange.getMessage().getHeader("HIE_GATEWAY_INSTANCE", String.class);
                            String trig = exchange.getMessage().getHeader("HIE_TRIGGER_TYPE", String.class);
                            Object body = exchange.getMessage().getBody();
                            if (body instanceof TaskEvent) {
                                TaskEvent te = (TaskEvent) body;
                                if (te.getTopic() != null) {
                                    return matches(te.getTopic());
                                }
                                if (gw == null) gw = te.getGatewayInstanceId();
                                if (trig == null) trig = te.getTriggerType();
                            } else if (body instanceof String) {
                                String strBody = ((String) body).trim();
                                if (strBody.startsWith("{")) {
                                    try {
                                        com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
                                        com.fasterxml.jackson.databind.JsonNode node = mapper.readTree(strBody);
                                        if (node.has("topic")) {
                                            Topic t = mapper.treeToValue(node.get("topic"), Topic.class);
                                            if (t != null) {
                                                return matches(t);
                                            }
                                        }
                                        if (gw == null && node.has("gatewayInstanceId")) {
                                            gw = node.get("gatewayInstanceId").asText();
                                        }
                                        if (trig == null && node.has("triggerType")) {
                                            trig = node.get("triggerType").asText();
                                        }
                                    } catch (Exception ignored) {
                                    }
                                }
                            }
                            return matches(gw, trig);
                        })
                        .log(LoggingLevel.INFO, log.getName(), "Executing TaskSequenceImplementation [" + getSequenceName() + "] pipeline on exchange ${exchangeId}")
                        .to(firstActivityInput)
                        .log(LoggingLevel.INFO, log.getName(), "Completed TaskSequenceImplementation [" + getSequenceName() + "] pipeline execution on exchange ${exchangeId}");

                // Optional bridge from custom inputEndpoint (e.g. for direct unit test injection)
                if (getInputEndpoint() != null && !getInputEndpoint().isBlank()
                        && !getInputEndpoint().equals(pipelineInput)
                        && !getInputEndpoint().equals(firstActivityInput)) {
                    from(getInputEndpoint())
                            .routeId("sequence-input-bridge-" + generateSequenceRouteId())
                            .to(pipelineInput);
                }

                // Optional bridge from custom dedicated sourceQueueName
                if (getSourceQueueName() != null && !getSourceQueueName().isBlank()) {
                    String sq = getSourceQueueName().startsWith("jms:") || getSourceQueueName().startsWith("direct:")
                            ? getSourceQueueName()
                            : "jms:queue:" + getSourceQueueName();
                    from(sq)
                            .routeId("sequence-source-queue-" + generateSequenceRouteId())
                            .to(pipelineInput);
                }
            }
        };
    }

    /**
     * Validates configuration of the task sequence implementation and its contained activities.
     *
     * @return true if valid, false otherwise
     */
    @Override
    public boolean validate() {
        if (!super.validate()) {
            return false;
        }
        if (activities != null) {
            for (Map.Entry<Integer, TaskProcessingActivity> entry : activities.entrySet()) {
                if (entry.getValue() == null) {
                    log.warn("TaskSequenceImplementation [{}] validation failed: activity at order {} is null", getSequenceId(), entry.getKey());
                    return false;
                }
            }
        }
        return true;
    }

    @JsonIgnore
    public CamelContext getCamelContext() {
        return camelContext;
    }

    @JsonIgnore
    public void setCamelContext(CamelContext camelContext) {
        this.camelContext = camelContext;
    }

    @JsonIgnore
    public void setInjectedActivities(Instance<TaskProcessingActivity> injectedActivities) {
        this.injectedActivities = injectedActivities;
    }

    @Override
    public String toString() {
        return "TaskSequenceImplementation{" +
                "sequenceId='" + getSequenceId() + '\'' +
                ", sequenceName='" + getSequenceName() + '\'' +
                ", version='" + getVersion() + '\'' +
                ", enabled=" + isEnabled() +
                ", targetGateways=" + getTargetGatewayInstances() +
                ", targetTriggers=" + getTargetTriggerTypes() +
                ", activityCount=" + (activities != null ? activities.size() : 0) +
                '}';
    }
}
