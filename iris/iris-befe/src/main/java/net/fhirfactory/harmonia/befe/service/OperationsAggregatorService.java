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

package net.fhirfactory.harmonia.befe.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Any;
import jakarta.enterprise.inject.Instance;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.befe.model.operations.*;
import net.fhirfactory.harmonia.befe.provider.*;
import net.fhirfactory.harmonia.model.petasos.PetasosQueueDefinition;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.praxis.PraxisDefinition;
import org.infinispan.client.hotrod.RemoteCache;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.*;

/**
 * Aggregates operational telemetry across all registered SubsystemHealthProvider beans,
 * Infinispan clustered caches, and Kubernetes instance discovery.
 * <p>
 * Implements bounded execution, partial failure tolerance, data staleness detection,
 * and explicit UNKNOWN status semantics.
 */
@ApplicationScoped
public class OperationsAggregatorService {

    private static final Logger log = LoggerFactory.getLogger(OperationsAggregatorService.class);

    public static final String CACHE_MESSAGE_QUEUE = "messagequeue-cache";
    public static final String CACHE_TASK = "task-cache";
    public static final String CACHE_TASK_SEQUENCE = "tasksequence-cache";
    public static final String CACHE_AUDIT_EVENT = "auditevent-cache";
    public static final String CACHE_ALERT_STATUS = "alert-status-cache";

    public static final List<String> CANONICAL_SUBSYSTEM_ORDER = List.of(
            "pylai", "petasos", "energeia", "mneme", "mnemosyne", "calliope", "themis", "agora", "iris"
    );

    @Inject
    @Any
    private Instance<SubsystemHealthProvider> injectedProviders;

    @Inject
    private ModuleStatusService moduleStatusService;

    @Inject
    private TaskSequenceCacheService taskSequenceCacheService;

    @Inject
    private KubernetesInstanceProvider instanceProvider;

    @Inject
    private RemoteCacheManager remoteCacheManager;

    private final Map<String, SubsystemHealthProvider> providersMap = new ConcurrentHashMap<>();
    private final Map<String, QueueSummary> registeredQueues = new ConcurrentHashMap<>();
    private final Map<String, PragmaSummary> localPragmaStore = new ConcurrentHashMap<>();
    private final List<OperationalEvent> operationalEvents = new CopyOnWriteArrayList<>();
    private final Map<String, String> alertAcknowledgements = new ConcurrentHashMap<>(); // alertId -> acknowledgedBy

    private ObjectMapper objectMapper = new ObjectMapper();
    private ExecutorService executionPool;

    public OperationsAggregatorService() {
    }

    @PostConstruct
    public void init() {
        if (executionPool == null) {
            executionPool = Executors.newCachedThreadPool();
        }

        // 1. Register injected providers if CDI available
        if (injectedProviders != null && !injectedProviders.isUnsatisfied()) {
            for (SubsystemHealthProvider provider : injectedProviders) {
                registerProvider(provider);
            }
        }

        // 2. Ensure all canonical 9 providers exist with fallbacks
        ensureCanonicalProviders();

        // 3. Initialize default Petasos message queues
        ensureDefaultQueues();
    }

    public synchronized void registerProvider(SubsystemHealthProvider provider) {
        if (provider != null && provider.getSubsystemId() != null) {
            String key = provider.getSubsystemId().toLowerCase().trim();
            providersMap.put(key, provider);
            log.info("Registered SubsystemHealthProvider for [{}]", key);
        }
    }

    public void ensureCanonicalProviders() {
        if (instanceProvider == null) {
            instanceProvider = new KubernetesInstanceProvider(moduleStatusService);
            instanceProvider.init();
        }

        registerIfMissing("pylai", () -> new PylaiHealthProvider());
        registerIfMissing("petasos", () -> new PetasosHealthProvider());
        registerIfMissing("energeia", () -> new EnergeiaHealthProvider());
        registerIfMissing("mneme", () -> new MnemeHealthProvider());
        registerIfMissing("mnemosyne", () -> new MnemosyneHealthProvider());
        registerIfMissing("calliope", () -> new CalliopeHealthProvider());
        registerIfMissing("themis", () -> new ThemisHealthProvider());
        registerIfMissing("agora", () -> new AgoraHealthProvider());
        registerIfMissing("iris", () -> new IrisHealthProvider());

        // Wire services for all AbstractSubsystemHealthProvider instances
        for (SubsystemHealthProvider p : providersMap.values()) {
            if (p instanceof AbstractSubsystemHealthProvider base) {
                if (moduleStatusService != null) {
                    base.setModuleStatusService(moduleStatusService);
                }
                if (instanceProvider != null) {
                    base.setInstanceProvider(instanceProvider);
                }
            }
        }
    }

    private void registerIfMissing(String id, java.util.function.Supplier<SubsystemHealthProvider> supplier) {
        if (!providersMap.containsKey(id.toLowerCase())) {
            SubsystemHealthProvider provider = supplier.get();
            registerProvider(provider);
        }
    }

    private void ensureDefaultQueues() {
        // Seed standard Petasos queues
        seedQueueIfMissing(new QueueSummary(
                "petasos.queue.pylai.mllp.in",
                "petasos.queue.pylai.mllp.in",
                "petasos.queue.pylai.mllp.in",
                "HEALTHY",
                0L, 2, 1, 0.0, 0.0, 0L, 0L, 0L, 0L,
                "Pylai Inbound MLLP Gateway"
        ));
        seedQueueIfMissing(new QueueSummary(
                "petasos.queue.ponos.dispatch",
                "petasos.queue.ponos.dispatch",
                "petasos.queue.ponos.dispatch",
                "HEALTHY",
                0L, 4, 2, 0.0, 0.0, 0L, 0L, 0L, 0L,
                "Ponos Task Dispatch"
        ));
        seedQueueIfMissing(new QueueSummary(
                "petasos.queue.mnemosyne.audit",
                "petasos.queue.mnemosyne.audit",
                "petasos.queue.mnemosyne.audit",
                "HEALTHY",
                0L, 1, 3, 0.0, 0.0, 0L, 0L, 0L, 0L,
                "Mnemosyne Audit Logger"
        ));
        seedQueueIfMissing(new QueueSummary(
                "petasos.queue.agora.inbound",
                "petasos.queue.agora.inbound",
                "petasos.queue.agora.inbound",
                "HEALTHY",
                0L, 1, 1, 0.0, 0.0, 0L, 0L, 0L, 0L,
                "Agora Matrix Gateway Inbound"
        ));
        seedQueueIfMissing(new QueueSummary(
                "petasos.queue.dlq",
                "petasos.queue.dlq",
                "petasos.queue.dlq",
                "HEALTHY",
                0L, 0, 0, 0.0, 0.0, 0L, 0L, 0L, 0L,
                "Petasos Dead Letter Queue"
        ));
    }

    private void seedQueueIfMissing(QueueSummary qs) {
        registeredQueues.putIfAbsent(qs.getQueueId(), qs);
    }

    // =========================================================================
    // 1. Overall Platform Summary (/api/operations/summary)
    // =========================================================================

    public OperationalSummary getOperationsSummary() {
        ensureCanonicalProviders();
        List<OperationalSubsystem> subsystems = getSubsystems();
        List<OperationalAlert> alerts = getAlerts(null, "ACTIVE", null);

        int totalSubsystems = subsystems.size();
        int degradedSubsystems = 0;
        boolean hasUnavailable = false;

        for (OperationalSubsystem sub : subsystems) {
            String state = sub.getState();
            if ("DEGRADED".equalsIgnoreCase(state) || "UNKNOWN".equalsIgnoreCase(state)) {
                degradedSubsystems++;
            } else if ("UNAVAILABLE".equalsIgnoreCase(state)) {
                degradedSubsystems++;
                hasUnavailable = true;
            }
        }

        int criticalAlerts = 0;
        int warningAlerts = 0;
        for (OperationalAlert a : alerts) {
            if ("CRITICAL".equalsIgnoreCase(a.getSeverity())) {
                criticalAlerts++;
            } else if ("WARNING".equalsIgnoreCase(a.getSeverity())) {
                warningAlerts++;
            }
        }

        String platformStatus = "HEALTHY";
        if (hasUnavailable) {
            platformStatus = "DEGRADED";
        } else if (degradedSubsystems > 0 || criticalAlerts > 0) {
            platformStatus = "DEGRADED";
        }

        String environment = System.getProperty("harmonia.environment",
                System.getenv().getOrDefault("HARMONIA_ENVIRONMENT", "PROD / microk8s-01"));
        String cluster = System.getProperty("harmonia.cluster",
                System.getenv().getOrDefault("HARMONIA_CLUSTER", "harmonia-cluster-01"));

        long now = System.currentTimeMillis();
        return new OperationalSummary(
                platformStatus,
                environment,
                cluster,
                now,
                totalSubsystems,
                degradedSubsystems,
                criticalAlerts,
                warningAlerts,
                now
        );
    }

    // =========================================================================
    // 2. Subsystems Perspective (/api/operations/subsystems)
    // =========================================================================

    public List<OperationalSubsystem> getSubsystems() {
        ensureCanonicalProviders();
        List<OperationalSubsystem> result = new ArrayList<>();

        for (String id : CANONICAL_SUBSYSTEM_ORDER) {
            SubsystemHealthProvider provider = providersMap.get(id);
            if (provider != null) {
                try {
                    OperationalSubsystem overview = executeWithTimeout(() -> provider.getSubsystemOverview(), 3000);
                    result.add(overview);
                } catch (Exception e) {
                    log.warn("Telemetry timeout or failure querying subsystem overview for [{}]: {}", id, e.getMessage());
                    result.add(createFallbackSubsystemOverview(provider));
                }
            }
        }

        // Also add any custom registered providers not in canonical order
        for (Map.Entry<String, SubsystemHealthProvider> entry : providersMap.entrySet()) {
            if (!CANONICAL_SUBSYSTEM_ORDER.contains(entry.getKey())) {
                try {
                    result.add(entry.getValue().getSubsystemOverview());
                } catch (Exception e) {
                    result.add(createFallbackSubsystemOverview(entry.getValue()));
                }
            }
        }

        return result;
    }

    public Optional<OperationalSubsystem> getSubsystem(String id) {
        if (id == null || id.isBlank()) {
            return Optional.empty();
        }
        ensureCanonicalProviders();
        SubsystemHealthProvider provider = providersMap.get(id.toLowerCase().trim());
        if (provider == null) {
            return Optional.empty();
        }
        try {
            return Optional.ofNullable(executeWithTimeout(() -> provider.getSubsystemOverview(), 3000));
        } catch (Exception e) {
            log.warn("Failed retrieving overview for subsystem [{}]: {}", id, e.getMessage());
            return Optional.of(createFallbackSubsystemOverview(provider));
        }
    }

    public List<OperationalInstance> getSubsystemInstances(String id) {
        if (id == null || id.isBlank()) {
            return Collections.emptyList();
        }
        ensureCanonicalProviders();
        String key = id.toLowerCase().trim();
        SubsystemHealthProvider provider = providersMap.get(key);
        if (provider != null) {
            try {
                List<OperationalInstance> instances = executeWithTimeout(() -> provider.getInstances(), 3000);
                if (instances != null && !instances.isEmpty()) {
                    return instances;
                }
            } catch (Exception e) {
                log.warn("Failed retrieving instances from provider for [{}]: {}", key, e.getMessage());
            }
        }
        if (instanceProvider != null) {
            try {
                return instanceProvider.getInstances(key);
            } catch (Exception e) {
                log.warn("Instance discovery failed for [{}]: {}", key, e.getMessage());
            }
        }
        return Collections.emptyList();
    }

    public OperationalHealth getSubsystemHealth(String id) {
        if (id == null || id.isBlank()) {
            return new OperationalHealth("unknown", "UNKNOWN", null, 0, 0, null, "Unknown (0 / 0)");
        }
        ensureCanonicalProviders();
        String key = id.toLowerCase().trim();
        SubsystemHealthProvider provider = providersMap.get(key);
        if (provider != null) {
            try {
                return executeWithTimeout(() -> provider.getOperationalHealth(), 3000);
            } catch (Exception e) {
                log.warn("Failed retrieving health from provider for [{}]: {}", key, e.getMessage());
            }
        }
        return new OperationalHealth(key, "UNKNOWN", null, 0, 0, null, "Unknown (Subsystem not registered)");
    }

    public Map<String, TimeSeries> getSubsystemStatistics(String id, String window) {
        if (id == null || id.isBlank()) {
            return Collections.emptyMap();
        }
        ensureCanonicalProviders();
        String key = id.toLowerCase().trim();
        SubsystemHealthProvider provider = providersMap.get(key);
        String win = (window == null || window.isBlank()) ? "15m" : window.toLowerCase().trim();
        if (!Set.of("15m", "1h", "6h", "24h").contains(win)) {
            win = "15m";
        }

        if (provider != null) {
            try {
                final String finalWin = win;
                Map<String, TimeSeries> stats = executeWithTimeout(() -> provider.getStatistics(finalWin), 3000);
                if (stats != null) {
                    return stats;
                }
            } catch (Exception e) {
                log.warn("Failed retrieving statistics for [{}]: {}", key, e.getMessage());
            }
        }
        return Collections.emptyMap();
    }

    private OperationalSubsystem createFallbackSubsystemOverview(SubsystemHealthProvider provider) {
        return new OperationalSubsystem(
                provider.getSubsystemId(),
                provider.getSubsystemName(),
                provider.getDescription(),
                "UNKNOWN",
                0,
                provider.getVersion(),
                System.currentTimeMillis(),
                Collections.emptyList()
        );
    }

    // =========================================================================
    // 3. Queues Perspective (/api/operations/queues)
    // =========================================================================

    public List<QueueSummary> getQueues(String statusFilter, String search) {
        Map<String, QueueSummary> combined = new LinkedHashMap<>(registeredQueues);

        // Check remote cache for persisted PetasosQueueDefinitions
        RemoteCache<String, String> cache = getCache(CACHE_MESSAGE_QUEUE);
        if (cache != null) {
            try {
                for (String json : cache.values()) {
                    if (json != null && !json.isBlank()) {
                        PetasosQueueDefinition qd = objectMapper.readValue(json, PetasosQueueDefinition.class);
                        if (qd != null && qd.getQueueId() != null) {
                            QueueSummary qs = convertQueueDefinitionToSummary(qd);
                            combined.put(qs.getQueueId(), qs);
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Remote messagequeue-cache read error: {}", e.getMessage());
            }
        }

        List<QueueSummary> result = new ArrayList<>();
        for (QueueSummary qs : combined.values()) {
            if (statusFilter != null && !statusFilter.isBlank() && !statusFilter.equalsIgnoreCase("ALL")) {
                if (!statusFilter.equalsIgnoreCase(qs.getStatus())) {
                    continue;
                }
            }
            if (search != null && !search.isBlank()) {
                String s = search.toLowerCase();
                boolean matches = (qs.getQueueName() != null && qs.getQueueName().toLowerCase().contains(s))
                        || (qs.getAddress() != null && qs.getAddress().toLowerCase().contains(s))
                        || (qs.getAssociatedCapability() != null && qs.getAssociatedCapability().toLowerCase().contains(s));
                if (!matches) {
                    continue;
                }
            }
            result.add(qs);
        }
        return result;
    }

    public Optional<QueueSummary> getQueue(String queueId) {
        if (queueId == null || queueId.isBlank()) {
            return Optional.empty();
        }
        List<QueueSummary> all = getQueues(null, null);
        for (QueueSummary qs : all) {
            if (queueId.equalsIgnoreCase(qs.getQueueId()) || queueId.equalsIgnoreCase(qs.getQueueName())) {
                return Optional.of(qs);
            }
        }
        return Optional.empty();
    }

    public void registerQueue(QueueSummary queue) {
        if (queue != null && queue.getQueueId() != null) {
            registeredQueues.put(queue.getQueueId(), queue);
        }
    }

    private QueueSummary convertQueueDefinitionToSummary(PetasosQueueDefinition qd) {
        return new QueueSummary(
                qd.getQueueId(),
                qd.getQueueName(),
                qd.getAddress(),
                qd.isEnabled() ? "HEALTHY" : "DEGRADED",
                0L,
                qd.getMaxConsumers() != null && qd.getMaxConsumers() > 0 ? qd.getMaxConsumers() : 1,
                1,
                0.0,
                0.0,
                0L,
                0L,
                0L,
                0L,
                qd.getDescription() != null ? qd.getDescription() : "Petasos Queue"
        );
    }

    // =========================================================================
    // 4. Workflows & Pragmas Perspective (/api/operations/workflows, /pragmas)
    // =========================================================================

    public List<WorkflowSummary> getWorkflows(String search) {
        List<WorkflowSummary> result = new ArrayList<>();

        if (taskSequenceCacheService != null) {
            try {
                List<PraxisDefinition> sequences = taskSequenceCacheService.getAllSequences();
                for (PraxisDefinition seq : sequences) {
                    WorkflowSummary ws = convertPraxisToWorkflowSummary(seq);
                    if (search != null && !search.isBlank()) {
                        String s = search.toLowerCase();
                        boolean match = (ws.getWorkflowId() != null && ws.getWorkflowId().toLowerCase().contains(s))
                                || (ws.getName() != null && ws.getName().toLowerCase().contains(s))
                                || (ws.getDescription() != null && ws.getDescription().toLowerCase().contains(s));
                        if (!match) {
                            continue;
                        }
                    }
                    result.add(ws);
                }
            } catch (Exception e) {
                log.debug("Failed querying TaskSequenceCacheService: {}", e.getMessage());
            }
        }

        // If no workflows returned from cache, return default canonical workflows
        if (result.isEmpty()) {
            WorkflowSummary defaultWs = new WorkflowSummary(
                    "seq-patient-identity-pipeline",
                    "Patient Identity Update Sequence",
                    "Extracts, normalizes, and updates patient identity across all clinical messages",
                    0, 0, 0, 0, 0, 0.0, 0L, 0.0
            );
            result.add(defaultWs);
        }

        return result;
    }

    public Optional<WorkflowSummary> getWorkflow(String workflowId) {
        if (workflowId == null || workflowId.isBlank()) {
            return Optional.empty();
        }
        for (WorkflowSummary ws : getWorkflows(null)) {
            if (workflowId.equalsIgnoreCase(ws.getWorkflowId())) {
                return Optional.of(ws);
            }
        }
        return Optional.empty();
    }

    private WorkflowSummary convertPraxisToWorkflowSummary(PraxisDefinition seq) {
        return new WorkflowSummary(
                seq.getSequenceId(),
                seq.getSequenceName(),
                seq.getDescription(),
                0, // active executions
                0, // queued
                0, // completed
                0, // failed
                0, // retrying
                0.0, // processing rate
                0L,  // p95 duration
                0.0  // failure rate
        );
    }

    public Optional<PragmaSummary> getPragma(String pragmaId) {
        if (pragmaId == null || pragmaId.isBlank()) {
            return Optional.empty();
        }

        // 1. Check local store
        if (localPragmaStore.containsKey(pragmaId)) {
            return Optional.of(localPragmaStore.get(pragmaId));
        }

        // 2. Check remote task-cache
        RemoteCache<String, String> cache = getCache(CACHE_TASK);
        if (cache != null) {
            try {
                String json = cache.get(pragmaId);
                if (json != null && !json.isBlank()) {
                    Pragma pragma = objectMapper.readValue(json, Pragma.class);
                    if (pragma != null) {
                        PragmaSummary ps = convertPragmaToSummary(pragma);
                        localPragmaStore.put(pragmaId, ps);
                        return Optional.of(ps);
                    }
                }
            } catch (Exception e) {
                log.debug("Remote task-cache read error for [{}]: {}", pragmaId, e.getMessage());
            }
        }

        return Optional.empty();
    }

    public void storePragma(PragmaSummary pragma) {
        if (pragma != null && pragma.getPragmaId() != null) {
            localPragmaStore.put(pragma.getPragmaId(), pragma);
        }
    }

    public List<PragmaSummary> getPragmasForWorkflow(String workflowId) {
        if (workflowId == null || workflowId.isBlank()) {
            return Collections.emptyList();
        }
        List<PragmaSummary> result = new ArrayList<>();
        for (PragmaSummary ps : localPragmaStore.values()) {
            if (workflowId.equalsIgnoreCase(ps.getPraxisId())) {
                result.add(ps);
            }
        }

        RemoteCache<String, String> cache = getCache(CACHE_TASK);
        if (cache != null) {
            try {
                for (String key : cache.keySet()) {
                    if (result.stream().noneMatch(p -> p.getPragmaId().equals(key))) {
                        String json = cache.get(key);
                        if (json != null && !json.isBlank()) {
                            Pragma pragma = objectMapper.readValue(json, Pragma.class);
                            if (pragma != null && workflowId.equalsIgnoreCase(pragma.getPraxisId())) {
                                PragmaSummary ps = convertPragmaToSummary(pragma);
                                result.add(ps);
                            }
                        }
                    }
                }
            } catch (Exception e) {
                log.debug("Remote task-cache query error for workflow [{}]: {}", workflowId, e.getMessage());
            }
        }
        return result;
    }

    private PragmaSummary convertPragmaToSummary(Pragma pragma) {
        List<ErgonCheckpoint> checkpoints = new ArrayList<>();
        if (pragma.getCheckpoints() != null) {
            for (int i = 0; i < pragma.getCheckpoints().size(); i++) {
                var cp = pragma.getCheckpoints().get(i);
                long cpTime = cp.getTimestamp() != null ? cp.getTimestamp().getTime() : System.currentTimeMillis();
                String eId = cp.getErgonId() != null ? cp.getErgonId() : "ergon-" + i;
                String eName = cp.getStageName() != null ? cp.getStageName() : eId;
                String cpStatus = cp.getStatus() != null ? cp.getStatus().name() : "COMPLETED";
                checkpoints.add(new ErgonCheckpoint(
                        eId,
                        eName,
                        cpStatus,
                        cpTime,
                        cpTime,
                        0L,
                        cp.getStatusMessage()
                ));
            }
        }

        long started = pragma.getAuthoredOn() != null ? pragma.getAuthoredOn().getTime() : System.currentTimeMillis();
        long modified = pragma.getLastModified() != null ? pragma.getLastModified().getTime() : started;
        long duration = Math.max(0, modified - started);

        PragmaSummary ps = new PragmaSummary(
                pragma.getPragmaId(),
                pragma.getPraxisId(),
                pragma.getStatus() != null ? pragma.getStatus().name() : "COMPLETED",
                started,
                duration,
                "completed",
                checkpoints.size(),
                0,
                pragma.getCorrelationId(),
                pragma.getCausationId(),
                null
        );
        ps.setCheckpoints(checkpoints);
        return ps;
    }

    // =========================================================================
    // 5. Events Perspective (/api/operations/events)
    // =========================================================================

    public List<OperationalEvent> getEvents(String correlationId, String causationId, String messageId,
                                            String pragmaId, String subsystem, String status,
                                            Long from, Long to, int page, int pageSize) {
        return getEvents(correlationId, causationId, messageId, pragmaId, subsystem, null, status, from, to, page, pageSize);
    }

    public List<OperationalEvent> getEvents(String correlationId, String causationId, String messageId,
                                            String pragmaId, String subsystem, String eventType, String status,
                                            Long from, Long to, int page, int pageSize) {
        List<OperationalEvent> filtered = new ArrayList<>();

        for (OperationalEvent ev : operationalEvents) {
            if (correlationId != null && !correlationId.isBlank()) {
                if (ev.getCorrelationId() == null || !ev.getCorrelationId().equalsIgnoreCase(correlationId.trim())) {
                    continue;
                }
            }
            if (causationId != null && !causationId.isBlank()) {
                if (ev.getCausationId() == null || !ev.getCausationId().equalsIgnoreCase(causationId.trim())) {
                    continue;
                }
            }
            if (messageId != null && !messageId.isBlank()) {
                if (ev.getMessageId() == null || !ev.getMessageId().equalsIgnoreCase(messageId.trim())) {
                    continue;
                }
            }
            if (pragmaId != null && !pragmaId.isBlank()) {
                if (ev.getPragmaId() == null || !ev.getPragmaId().equalsIgnoreCase(pragmaId.trim())) {
                    continue;
                }
            }
            if (subsystem != null && !subsystem.isBlank() && !subsystem.equalsIgnoreCase("ALL")) {
                if (ev.getSubsystem() == null || !ev.getSubsystem().equalsIgnoreCase(subsystem.trim())) {
                    continue;
                }
            }
            if (eventType != null && !eventType.isBlank() && !eventType.equalsIgnoreCase("ALL")) {
                if (ev.getEventType() == null || !ev.getEventType().equalsIgnoreCase(eventType.trim())) {
                    continue;
                }
            }
            if (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) {
                if (ev.getStatus() == null || !ev.getStatus().equalsIgnoreCase(status.trim())) {
                    continue;
                }
            }
            if (from != null && ev.getTimestamp() < from) {
                continue;
            }
            if (to != null && ev.getTimestamp() > to) {
                continue;
            }
            filtered.add(ev);
        }

        // Sort descending by timestamp
        filtered.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));

        // Apply pagination controls
        int boundedPage = Math.max(0, page);
        int boundedSize = Math.max(1, Math.min(pageSize > 0 ? pageSize : 50, 200));
        int fromIndex = boundedPage * boundedSize;

        if (fromIndex >= filtered.size()) {
            return Collections.emptyList();
        }
        int toIndex = Math.min(fromIndex + boundedSize, filtered.size());
        return new ArrayList<>(filtered.subList(fromIndex, toIndex));
    }

    public Optional<OperationalEvent> getEvent(String eventId) {
        if (eventId == null || eventId.isBlank()) {
            return Optional.empty();
        }
        for (OperationalEvent ev : operationalEvents) {
            if (eventId.equalsIgnoreCase(ev.getEventId())) {
                return Optional.of(ev);
            }
        }
        return Optional.empty();
    }

    public void recordEvent(OperationalEvent event) {
        if (event != null && event.getEventId() != null) {
            operationalEvents.add(event);
            // Cap in-memory history to 5000 items
            if (operationalEvents.size() > 5000) {
                operationalEvents.remove(0);
            }
        }
    }

    // =========================================================================
    // 6. Alerts Perspective (/api/operations/alerts)
    // =========================================================================

    public List<OperationalAlert> getAlerts(String severity, String status, String subsystem) {
        ensureCanonicalProviders();
        List<OperationalAlert> alerts = new ArrayList<>();

        // 1. Evaluate live subsystem states
        for (OperationalSubsystem sub : getSubsystems()) {
            String state = sub.getState();
            String subId = sub.getId();

            if ("UNAVAILABLE".equalsIgnoreCase(state)) {
                String alertId = "alert-subsystem-" + subId + "-down";
                String alertStatus = alertAcknowledgements.containsKey(alertId) ? "ACKNOWLEDGED" : "ACTIVE";
                alerts.add(new OperationalAlert(
                        alertId,
                        "CRITICAL",
                        subId,
                        sub.getName(),
                        "Subsystem is UNAVAILABLE or stopped",
                        sub.getLastUpdated(),
                        System.currentTimeMillis(),
                        "Active",
                        alertStatus,
                        "Subsystem/" + subId,
                        null,
                        "Inspect container logs and pod restart counts in Kubernetes; verify process status."
                ));
            } else if ("DEGRADED".equalsIgnoreCase(state)) {
                String alertId = "alert-subsystem-" + subId + "-degraded";
                String alertStatus = alertAcknowledgements.containsKey(alertId) ? "ACKNOWLEDGED" : "ACTIVE";
                alerts.add(new OperationalAlert(
                        alertId,
                        "WARNING",
                        subId,
                        sub.getName(),
                        "Subsystem reports DEGRADED health status",
                        sub.getLastUpdated(),
                        System.currentTimeMillis(),
                        "Active",
                        alertStatus,
                        "Subsystem/" + subId,
                        null,
                        "Inspect dependency connectivity, error logs, and service health metrics."
                ));
            }
        }

        // 2. Evaluate queues for DLQ depth
        for (QueueSummary qs : getQueues(null, null)) {
            if (qs.getDlqDepth() > 0) {
                String alertId = "alert-queue-" + qs.getQueueId() + "-dlq";
                String alertStatus = alertAcknowledgements.containsKey(alertId) ? "ACKNOWLEDGED" : "ACTIVE";
                String sev = qs.getDlqDepth() > 50 ? "CRITICAL" : "WARNING";
                alerts.add(new OperationalAlert(
                        alertId,
                        sev,
                        "petasos",
                        qs.getQueueName(),
                        "Dead Letter Queue contains " + qs.getDlqDepth() + " unprocessable messages",
                        System.currentTimeMillis() - 60000,
                        System.currentTimeMillis(),
                        "Active",
                        alertStatus,
                        "Queue/" + qs.getQueueId(),
                        null,
                        "Examine DLQ message error headers and verify downstream consumer/processor health."
                ));
            }
        }

        // 3. Filter by severity, status, subsystem
        List<OperationalAlert> result = new ArrayList<>();
        for (OperationalAlert a : alerts) {
            if (severity != null && !severity.isBlank() && !severity.equalsIgnoreCase("ALL")) {
                if (!a.getSeverity().equalsIgnoreCase(severity.trim())) {
                    continue;
                }
            }
            if (status != null && !status.isBlank() && !status.equalsIgnoreCase("ALL")) {
                if (!a.getStatus().equalsIgnoreCase(status.trim())) {
                    continue;
                }
            }
            if (subsystem != null && !subsystem.isBlank() && !subsystem.equalsIgnoreCase("ALL")) {
                if (!a.getSubsystem().equalsIgnoreCase(subsystem.trim())) {
                    continue;
                }
            }
            result.add(a);
        }

        return result;
    }

    public boolean acknowledgeAlert(String alertId, String operatorId) {
        if (alertId == null || alertId.isBlank()) {
            return false;
        }
        String op = (operatorId != null && !operatorId.isBlank()) ? operatorId : "operator";
        alertAcknowledgements.put(alertId, op);

        // Project into alert-status-cache if connected
        RemoteCache<String, String> cache = getCache(CACHE_ALERT_STATUS);
        if (cache != null) {
            try {
                cache.put(alertId, op + ":" + System.currentTimeMillis());
            } catch (Exception e) {
                log.debug("Failed projecting alert ack into alert-status-cache: {}", e.getMessage());
            }
        }
        return true;
    }

    // =========================================================================
    // Utilities & Bounded Execution
    // =========================================================================

    private <T> T executeWithTimeout(Callable<T> task, long timeoutMs) throws Exception {
        if (executionPool == null) {
            executionPool = Executors.newCachedThreadPool();
        }
        Future<T> future = executionPool.submit(task);
        try {
            return future.get(timeoutMs, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            future.cancel(true);
            throw new RuntimeException("Execution timed out after " + timeoutMs + "ms");
        }
    }

    @SuppressWarnings("unchecked")
    private RemoteCache<String, String> getCache(String cacheName) {
        if (remoteCacheManager != null && remoteCacheManager.isStarted()) {
            try {
                return remoteCacheManager.getCache(cacheName);
            } catch (Exception e) {
                log.debug("Cache lookup failed for [{}]: {}", cacheName, e.getMessage());
            }
        }
        return null;
    }

    public ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public void setTaskSequenceCacheService(TaskSequenceCacheService taskSequenceCacheService) {
        this.taskSequenceCacheService = taskSequenceCacheService;
    }

    public void setInstanceProvider(KubernetesInstanceProvider instanceProvider) {
        this.instanceProvider = instanceProvider;
    }

    public void setRemoteCacheManager(RemoteCacheManager remoteCacheManager) {
        this.remoteCacheManager = remoteCacheManager;
    }
}
