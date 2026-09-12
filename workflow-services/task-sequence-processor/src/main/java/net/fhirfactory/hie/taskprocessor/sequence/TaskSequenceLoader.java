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

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.sequence.TaskSequenceDefinition;
import net.fhirfactory.hie.taskprocessor.service.TaskSequenceService;
import net.fhirfactory.hie.taskprocessors.base.TaskProcessingActivity;
import net.fhirfactory.hie.taskprocessors.infrastructure.MessageQueueToExchangeConduit;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * Loads persisted {@link TaskSequenceDefinition} objects from {@link TaskSequenceService},
 * resolves and structures the configured {@link TaskProcessingActivity} instances from CDI,
 * and dynamically registers sequence consumer routes in the Apache Camel runtime.
 */
@ApplicationScoped
public class TaskSequenceLoader {

    private static final Logger log = LoggerFactory.getLogger(TaskSequenceLoader.class);

    @Inject
    private TaskSequenceService sequenceService;

    @Inject
    private TaskSequenceDefaultSeeder sequenceSeeder;

    @Inject
    private net.fhirfactory.hie.taskprocessor.cache.ClusterReadinessService readinessService;

    @Inject
    @Any
    private Instance<TaskProcessingActivity> availableActivities;

    private final List<TaskSequence> loadedSequences = new ArrayList<>();

    public TaskSequenceLoader() {
    }

    public TaskSequenceLoader(TaskSequenceService sequenceService) {
        this.sequenceService = sequenceService;
        this.sequenceSeeder = new TaskSequenceDefaultSeeder(sequenceService);
    }

    public TaskSequenceLoader(TaskSequenceService sequenceService, Instance<TaskProcessingActivity> availableActivities) {
        this.sequenceService = sequenceService;
        this.availableActivities = availableActivities;
        this.sequenceSeeder = new TaskSequenceDefaultSeeder(sequenceService);
    }

    public TaskSequenceLoader(TaskSequenceService sequenceService, TaskSequenceDefaultSeeder sequenceSeeder, Instance<TaskProcessingActivity> availableActivities) {
        this.sequenceService = sequenceService;
        this.sequenceSeeder = sequenceSeeder;
        this.availableActivities = availableActivities;
    }

    public TaskSequenceLoader(TaskSequenceService sequenceService, TaskSequenceDefaultSeeder sequenceSeeder, Instance<TaskProcessingActivity> availableActivities, net.fhirfactory.hie.taskprocessor.cache.ClusterReadinessService readinessService) {
        this.sequenceService = sequenceService;
        this.sequenceSeeder = sequenceSeeder;
        this.availableActivities = availableActivities;
        this.readinessService = readinessService;
    }

    /**
     * Loads persisted sequence configurations, structures their activity pipelines, and registers Camel routes.
     *
     * @param camelContext CamelContext to register routes into
     * @return list of loaded and registered {@link TaskSequence} instances
     * @throws Exception if route registration fails
     */
    public synchronized List<TaskSequence> loadAndRegisterSequences(CamelContext camelContext) throws Exception {
        Objects.requireNonNull(camelContext, "CamelContext must not be null");

        log.info("TaskSequenceLoader initializing sequences into CamelContext...");

        // 0. Verify cluster readiness if readinessService is available
        if (readinessService != null) {
            readinessService.waitForCacheReady(TaskSequenceService.SEQUENCE_CACHE_NAME, 2);
        }

        // 1. Discover available TaskProcessingActivity instances from CDI
        Map<String, TaskProcessingActivity> activityIndex = indexAvailableActivities();
        log.info("Discovered {} available TaskProcessingActivity bean(s) in CDI context", activityIndex.size());

        // 2. Fetch persisted sequences from Infinispan cache / JPA store
        List<TaskSequenceDefinition> persistedSequences = sequenceService != null ? sequenceService.getAll() : Collections.emptyList();
        log.info("Pulled {} TaskSequenceDefinition(s) from cache/JPA store on startup", persistedSequences.size());

        // 3. Seed defaults if cache is empty
        if (persistedSequences.isEmpty() && sequenceService != null) {
            log.info("No persisted TaskSequence definitions found in cache. Seeding default sequences...");
            if (sequenceSeeder == null) {
                sequenceSeeder = new TaskSequenceDefaultSeeder(sequenceService);
            }
            List<TaskSequence> seeded = sequenceSeeder.seedDefaultSequences(activityIndex);
            persistedSequences = new ArrayList<>(seeded);
        }

        loadedSequences.clear();

        // 4. Structure and register each sequence
        for (TaskSequenceDefinition def : persistedSequences) {
            if (def == null) {
                continue;
            }

            if (!def.isEnabled()) {
                log.info("TaskSequence [{}] is disabled. Skipping route registration.", def.getSequenceId());
                continue;
            }

            TaskSequence seq = def instanceof TaskSequence ? (TaskSequence) def : new TaskSequence(def);

            // Resolve activities for this sequence
            Map<Integer, TaskProcessingActivity> structuredActivities = resolveActivitiesForSequence(seq, activityIndex);
            if (!structuredActivities.isEmpty()) {
                seq.setActivities(structuredActivities);
                seq.configureChainedEndpoints();
                seq.registerRoutes(camelContext);
            } else {
                log.warn("TaskSequence [{}] has no resolved activities. Registering empty pipeline.", seq.getSequenceId());
            }

            // Register pipeline consumer route
            RouteBuilder pipelineRoute = seq.createSequencePipelineRoute();
            if (pipelineRoute != null) {
                camelContext.addRoutes(pipelineRoute);
                log.info("Successfully registered Camel pipeline route for TaskSequence [{}] (gateways={}, triggers={}, activities={})",
                        seq.getSequenceId(), seq.getTargetGatewayInstances(), seq.getTargetTriggerTypes(), seq.getActivityCount());
            }

            loadedSequences.add(seq);
        }

        log.info("TaskSequenceLoader completed. Registered {} active TaskSequence pipeline(s).", loadedSequences.size());
        return Collections.unmodifiableList(loadedSequences);
    }

    /**
     * Indexes available CDI activities by ID and class name for fast lookup.
     */
    public Map<String, TaskProcessingActivity> indexAvailableActivities() {
        Map<String, TaskProcessingActivity> map = new LinkedHashMap<>();
        if (availableActivities != null && !availableActivities.isUnsatisfied()) {
            for (TaskProcessingActivity activity : availableActivities) {
                if (activity != null) {
                    if (activity.getActivityId() != null) {
                        map.put(activity.getActivityId(), activity);
                    }
                    map.put(activity.getClass().getName(), activity);
                    map.put(activity.getClass().getSimpleName(), activity);
                    if (activity instanceof MessageQueueToExchangeConduit) {
                        map.put("message-queue-to-exchange", activity);
                        map.put("message-queue-conduit", activity);
                        map.put("message-queue-to-exchange-conduit", activity);
                        map.put("messageq-to-exchange", activity);
                    }
                }
            }
        }
        return map;
    }

    /**
     * Resolves matching activities for a sequence definition from the available activity map.
     */
    public Map<Integer, TaskProcessingActivity> resolveActivitiesForSequence(TaskSequenceDefinition sequence,
                                                                             Map<String, TaskProcessingActivity> activityIndex) {
        Map<Integer, TaskProcessingActivity> resolved = new TreeMap<>();
        Map<Integer, String> activityIds = sequence.getActivityIds();
        Map<Integer, String> classNames = sequence.getActivityClassNames();

        if (activityIds != null && !activityIds.isEmpty()) {
            new TreeMap<>(activityIds).forEach((order, id) -> {
                TaskProcessingActivity match = activityIndex.get(id);
                if (match != null) {
                    resolved.put(order, match);
                } else {
                    log.warn("TaskSequence [{}] references activity ID [{}] at order [{}] which is not found in CDI context.",
                            sequence.getSequenceId(), id, order);
                }
            });
        } else if (classNames != null && !classNames.isEmpty()) {
            new TreeMap<>(classNames).forEach((order, className) -> {
                TaskProcessingActivity match = activityIndex.get(className);
                if (match != null) {
                    resolved.put(order, match);
                } else {
                    log.warn("TaskSequence [{}] references activity class [{}] at order [{}] which is not found in CDI context.",
                            sequence.getSequenceId(), className, order);
                }
            });
        } else if (!activityIndex.isEmpty()) {
            // Default fallback: attach all unique discovered activities
            int order = 0;
            for (TaskProcessingActivity act : new LinkedHashSet<>(activityIndex.values())) {
                resolved.put(order++, act);
            }
        }

        return resolved;
    }

    /**
     * Seeds initial standard default TaskSequence configurations in Infinispan cache using {@link TaskSequenceDefaultSeeder}.
     */
    public List<TaskSequence> seedDefaultSequences(Map<String, TaskProcessingActivity> activityIndex) {
        if (sequenceSeeder == null) {
            sequenceSeeder = new TaskSequenceDefaultSeeder(sequenceService);
        }
        return sequenceSeeder.seedDefaultSequences(activityIndex);
    }

    /**
     * Reloads and re-registers all TaskSequence pipelines into CamelContext from the data store.
     *
     * @param camelContext the active CamelContext
     * @return updated list of active {@link TaskSequence} instances
     * @throws Exception if reload or route registration fails
     */
    public synchronized List<TaskSequence> reloadSequences(CamelContext camelContext) throws Exception {
        log.info("TaskSequenceLoader reload requested. Reloading sequences from data store...");
        return loadAndRegisterSequences(camelContext);
    }

    /**
     * Validates all TaskSequence definitions against available CDI activities and configuration rules.
     *
     * @return validation report map
     */
    public Map<String, Object> validateSequences() {
        Map<String, Object> report = new HashMap<>();
        Map<String, TaskProcessingActivity> activityIndex = indexAvailableActivities();

        List<TaskSequenceDefinition> sequences = sequenceService != null
                ? sequenceService.getAll()
                : Collections.emptyList();

        List<Map<String, Object>> sequenceReports = new ArrayList<>();
        int validCount = 0;
        int invalidCount = 0;

        for (TaskSequenceDefinition def : sequences) {
            Map<String, Object> sRep = new HashMap<>();
            sRep.put("sequenceId", def.getSequenceId());
            sRep.put("sequenceName", def.getSequenceName());
            sRep.put("enabled", def.isEnabled());
            sRep.put("sourceQueueName", def.getSourceQueueName());
            sRep.put("targetGateways", def.getTargetGatewayInstances());
            sRep.put("targetTriggers", def.getTargetTriggerTypes());

            List<String> missingActivities = new ArrayList<>();
            Map<Integer, String> actIds = def.getActivityIds();
            if (actIds != null && !actIds.isEmpty()) {
                for (String actId : actIds.values()) {
                    if (!activityIndex.containsKey(actId)) {
                        missingActivities.add(actId);
                    }
                }
            }

            boolean isValid = def.getSequenceId() != null && !def.getSequenceId().isBlank() && missingActivities.isEmpty();
            sRep.put("valid", isValid);
            if (isValid) {
                validCount++;
            } else {
                invalidCount++;
                if (def.getSequenceId() == null || def.getSequenceId().isBlank()) {
                    sRep.put("error", "Sequence ID is missing or blank");
                } else {
                    sRep.put("missingActivities", missingActivities);
                    sRep.put("error", "Missing CDI activity bean(s): " + missingActivities);
                }
            }
            sequenceReports.add(sRep);
        }

        report.put("totalSequences", sequences.size());
        report.put("validSequences", validCount);
        report.put("invalidSequences", invalidCount);
        report.put("availableCdiActivities", activityIndex.keySet());
        report.put("sequences", sequenceReports);
        return report;
    }

    public List<TaskSequence> getLoadedSequences() {
        return Collections.unmodifiableList(loadedSequences);
    }

    /**
     * Returns the list of entry point endpoints for all currently loaded and enabled TaskSequences.
     * Used by the sequence dispatcher to multicast/route incoming TaskEvents.
     */
    public List<String> getSequencePipelineEndpoints() {
        List<String> endpoints = new ArrayList<>();
        for (TaskSequence seq : loadedSequences) {
            if (seq != null && seq.isEnabled()) {
                String ep = seq.getPipelineInputEndpoint();
                if (ep != null && !ep.isBlank() && !endpoints.contains(ep)) {
                    endpoints.add(ep);
                }
            }
        }
        if (endpoints.isEmpty()) {
            return Collections.emptyList();
        }
        return endpoints;
    }

    /**
     * Collects all specific dedicated gateway queue names referenced by loaded sequences.
     */
    public Set<String> getAllTargetGatewayQueues(String eventQueuePrefix) {
        String prefix = eventQueuePrefix != null ? eventQueuePrefix : "task.event.queue";
        Set<String> queues = new LinkedHashSet<>();
        for (TaskSequence seq : loadedSequences) {
            if (seq != null && seq.isEnabled()) {
                if (seq.getSourceQueueName() != null && !seq.getSourceQueueName().isBlank()) {
                    String sq = seq.getSourceQueueName();
                    if (sq.startsWith("jms:queue:")) sq = sq.substring("jms:queue:".length());
                    queues.add(sq);
                }
                List<String> gateways = seq.getTargetGatewayInstances();
                if (gateways != null) {
                    for (String gw : gateways) {
                        if (gw != null && !gw.isBlank() && !"*".equals(gw)) {
                            queues.add(prefix + "." + gw.trim());
                        }
                    }
                }
            }
        }
        return queues;
    }

    public TaskSequenceService getSequenceService() {
        return sequenceService;
    }

    public void setSequenceService(TaskSequenceService sequenceService) {
        this.sequenceService = sequenceService;
        if (this.sequenceSeeder != null) {
            this.sequenceSeeder.setSequenceService(sequenceService);
        }
    }

    public TaskSequenceDefaultSeeder getSequenceSeeder() {
        return sequenceSeeder;
    }

    public void setSequenceSeeder(TaskSequenceDefaultSeeder sequenceSeeder) {
        this.sequenceSeeder = sequenceSeeder;
    }

    public void setAvailableActivities(Instance<TaskProcessingActivity> availableActivities) {
        this.availableActivities = availableActivities;
    }

    public net.fhirfactory.hie.taskprocessor.cache.ClusterReadinessService getReadinessService() {
        return readinessService;
    }

    public void setReadinessService(net.fhirfactory.hie.taskprocessor.cache.ClusterReadinessService readinessService) {
        this.readinessService = readinessService;
    }
}
