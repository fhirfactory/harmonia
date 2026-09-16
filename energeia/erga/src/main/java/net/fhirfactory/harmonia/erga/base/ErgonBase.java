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

package net.fhirfactory.harmonia.erga.base;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.model.ergon.ErgonReasonEnum;
import net.fhirfactory.harmonia.model.ergon.ErgonEvent;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.model.pragma.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.pragma.PragmaFhirConverter;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.model.security.ErgonSecurityDefinition;
import net.fhirfactory.harmonia.model.security.FhirSecurityTagManager;
import net.fhirfactory.harmonia.model.topic.Topic;
import net.fhirfactory.harmonia.praxis.cache.TaskCacheService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Superclass for modular single-responsibility Task Processing Activities (Erga) in Harmonia.
 * <p>
 * <b>Ergon</b> (&epsilon;&#7984;&rho;&gamma;&omicron;&nu;): A single, modular task action encapsulated within an Apache Camel
 * route that fulfills a discrete function (mapping, extracting, normalizing, or enriching data).
 * Each Ergon takes in a {@link Pragma}, processes data from {@link Pragma#getInput()}, and appends results to {@link Pragma#getOutput()}.
 * <p>
 * Inherits from Apache Camel {@link RouteBuilder} to define declarative integration routes,
 * lifecycle management, state checkpointing, and execution pipelines.
 */
public abstract class ErgonBase extends RouteBuilder {

    protected final Logger log = LoggerFactory.getLogger(getClass());

    public static final String HEADER_PRAGMA_ID = "HIE_PRAGMA_ID";
    public static final String HEADER_TASK_ID = "HIE_TASK_ID";
    public static final String HEADER_ACTION = "HIE_ACTION";
    public static final String HEADER_STATUS = "HIE_STATUS";
    public static final String HEADER_TASK_PROCESSED = "HIE_TASK_PROCESSED";
    public static final String HEADER_RAW_MESSAGE = "HIE_RAW_MESSAGE";
    public static final String HEADER_GATEWAY_INSTANCE = "HIE_GATEWAY_INSTANCE";
    public static final String HEADER_TRIGGER_TYPE = "HIE_TRIGGER_TYPE";
    public static final String HEADER_MESSAGE_TYPE = "HIE_MESSAGE_TYPE";
    public static final String HEADER_CONTROL_ID = "HIE_CONTROL_ID";
    public static final String HEADER_TOPIC = "HIE_TOPIC";

    public static final String PROPERTY_PRAGMA = "HIE_PRAGMA";
    public static final String PROPERTY_INCOMING_TASK = "HIE_INCOMING_TASK";
    public static final String PROPERTY_PROVENANCE = "HIE_PROVENANCE";
    public static final String PROPERTY_PROVENANCES = "HIE_PROVENANCES";
    public static final String PROPERTY_OUTGOING_TASKS = "HIE_OUTGOING_TASKS";
    public static final String PROPERTY_TASK_EVENTS = "HIE_TASK_EVENTS";

    @Inject
    private TaskCacheService taskCacheService;

    @Inject
    private FhirContext fhirContext;

    private ObjectMapper objectMapper;

    private String activityId;
    private String activityName;
    private String activityDescription;
    private String version = "1.0.0";
    private String inputEndpoint;
    private String outputEndpoint;
    private String errorEndpoint;
    private boolean enabled = true;
    private ErgonSecurityDefinition securityDefinition;

    /**
     * Default constructor initializing default identifiers.
     */
    public ErgonBase() {
        super();
        this.activityName = getClass().getSimpleName();
        this.activityId = getClass().getName();
    }

    /**
     * Constructor with activity identification.
     *
     * @param activityId   Unique identifier of the task processing activity
     * @param activityName Human-readable activity name
     */
    public ErgonBase(String activityId, String activityName) {
        super();
        this.activityId = activityId;
        this.activityName = activityName;
    }

    /**
     * Constructor with CamelContext.
     *
     * @param context CamelContext instance
     */
    public ErgonBase(CamelContext context) {
        super(context);
        this.activityName = getClass().getSimpleName();
        this.activityId = getClass().getName();
    }

    /**
     * Constructor with CamelContext and activity metadata.
     *
     * @param context      CamelContext instance
     * @param activityId   Unique identifier of the task processing activity
     * @param activityName Human-readable activity name
     */
    public ErgonBase(CamelContext context, String activityId, String activityName) {
        super(context);
        this.activityId = activityId;
        this.activityName = activityName;
    }

    /**
     * Template method configuring the Camel routes for this activity.
     * Calls {@link #configureActivity()} if this activity is enabled.
     *
     * @throws Exception if route configuration fails
     */
    @Override
    public void configure() throws Exception {
        if (!enabled) {
            log.warn("TaskProcessingActivity [{}] (id={}) is disabled, skipping route configuration",
                    activityName, activityId);
            return;
        }

        log.info("Configuring TaskProcessingActivity [{}] (id={}, version={})",
                activityName, activityId, version);

        configureActivity();
    }

    /**
     * Subclasses implement or override this method to define their specific Camel route definitions.
     * By default, establishes an ingress -> processActivity -> egress pipeline.
     *
     * @throws Exception if route building fails
     */
    protected void configureActivity() throws Exception {
        String input = getInputEndpoint() != null && !getInputEndpoint().isBlank()
                ? getInputEndpoint()
                : "direct:" + generateRouteId() + "-in";
        String output = getOutputEndpoint();

        log.info("Configuring {} route: {} -> {}", getActivityName(), input, (output != null && !output.isBlank()) ? output : "[terminal]");

        RouteDefinition route = from(input)
                .routeId(generateRouteId())
                .routeDescription(getActivityDescription() != null ? getActivityDescription() : getActivityName())
                .log(LoggingLevel.INFO, log.getName(), getActivityName() + " processing message on exchange ${exchangeId}")
                .process(this::processIngress)
                .process(this::processActivity)
                .process(this::processEgress)
                .log(LoggingLevel.INFO, log.getName(), getActivityName() + " successfully processed exchange ${exchangeId}");

        if (output != null && !output.isBlank()) {
            route.to(output);
        }
    }

    /**
     * Core processing lifecycle step. Extracts {@link Pragma} from exchange, invokes
     * {@link #processErgon(Pragma, Exchange)}, and ensures the updated Pragma is retained
     * in the exchange body for subsequent stages.
     *
     * @param exchange Camel Exchange containing the ingress Pragma in the message body
     * @throws Exception on processing error
     */
    protected void processActivity(Exchange exchange) throws Exception {
        Pragma pragma = extractPragmaFromExchange(exchange);
        if (pragma == null) {
            log.warn("[{}] processActivity found no Pragma in exchange", getActivityName());
            return;
        }
        processErgon(pragma, exchange);
        exchange.getMessage().setBody(pragma);
        exchange.setProperty(PROPERTY_PRAGMA, pragma);
    }

    /**
     * Contract method for subclass Ergon activities.
     * Subclasses inspect relevant inputs from {@link Pragma#getInput()}, execute single-responsibility
     * processing logic, and append produced artifacts to {@link Pragma#getOutput()}.
     *
     * @param pragma   canonical Pragma domain instance
     * @param exchange Camel Exchange for headers and routing context
     * @throws Exception on processing or mapping error
     */
    protected void processErgon(Pragma pragma, Exchange exchange) throws Exception {
        // Default hook for backwards compatibility with legacy processActivity subclasses
    }

    /**
     * 1. Ingress Processing:
     * Resolves the {@link Pragma} task instance (from body, cache, or raw message payload),
     * records an ingress checkpoint, and sets the Pragma as the Exchange Message Body and property.
     *
     * @param exchange Camel Exchange
     * @throws Exception on ingress retrieval failure
     */
    public void processIngress(Exchange exchange) throws Exception {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("[{}] Ingress received null exchange or message", getActivityName());
            return;
        }

        Object body = exchange.getMessage().getBody();
        String taskId = extractTaskId(exchange, body);
        Pragma pragma = resolvePragmaFromIngress(exchange, body);

        if (pragma == null) {
            String pragmaId = StringUtils.isNotBlank(taskId) ? cleanId(taskId) : UUID.randomUUID().toString();
            pragma = new Pragma(pragmaId, getActivityId(), PragmaStatus.IN_PROGRESS);
            pragma.setAuthoredOn(new Date());
            pragma.setLastModified(new Date());

            if (body != null) {
                Topic container = exchange.getMessage().getHeader(HEADER_TOPIC, Topic.class);
                if (container == null) {
                    container = new Topic("Health", "HL7", "2.4", "RAW", "INGRESS");
                }
                Topic content = new Topic("Health", "Payload", "1.0", "RawMessage", null);
                ErgonPayload inputPayload = ErgonPayload.fromJson(0, container, content, body.toString());
                pragma.addInput(inputPayload);

                if (exchange.getMessage().getHeader(HEADER_RAW_MESSAGE) == null) {
                    exchange.getMessage().setHeader(HEADER_RAW_MESSAGE, body.toString());
                }
            }
            getTaskCacheService().savePragma(pragma);
            log.info("[{}] Created baseline Pragma/{} in cache for ingress", getActivityName(), pragmaId);
        } else if (StringUtils.isNotBlank(taskId) && StringUtils.isBlank(pragma.getPragmaId())) {
            pragma.setPragmaId(cleanId(taskId));
        }

        // Add Ingress Checkpoint
        pragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId(pragma.getPragmaId())
                .ergonId(getActivityId())
                .stageName("INGRESS")
                .status(PragmaStatus.IN_PROGRESS)
                .statusMessage(getActivityName() + " received task for execution")
                .build());

        Task fhirTask = PragmaFhirConverter.toFhirTask(pragma);

        exchange.setProperty(PROPERTY_PRAGMA, pragma);
        exchange.setProperty(PROPERTY_INCOMING_TASK, fhirTask);
        exchange.getMessage().setBody(pragma);
        exchange.getMessage().setHeader(HEADER_PRAGMA_ID, pragma.getPragmaId());
        exchange.getMessage().setHeader(HEADER_TASK_ID, pragma.getPragmaId());
    }

    /**
     * 2. Egress Processing:
     * Receives the processed {@link Pragma} coming OUT of subclass processing and:
     * (a) persists the Pragma state and checkpoint snapshot to the cache,
     * (b) converts to FHIR Task/Provenance/ErgonEvent for backwards compatibility,
     * (c) retains the updated Pragma in the Exchange Message Body or property for downstream pipeline chaining.
     *
     * @param exchange Camel Exchange
     * @throws Exception on egress processing failure
     */
    public void processEgress(Exchange exchange) throws Exception {
        if (exchange == null || exchange.getMessage() == null) {
            log.warn("[{}] Egress received null exchange or message", getActivityName());
            return;
        }

        Object body = exchange.getMessage().getBody();
        Pragma processedPragma = resolvePragmaFromEgress(exchange, body);

        if (processedPragma == null) {
            log.warn("[{}] Egress could not resolve Pragma from body or exchange, skipping egress processing", getActivityName());
            return;
        }

        processedPragma.touch();

        // Record Egress Checkpoint
        processedPragma.addCheckpoint(PragmaCheckpoint.builder()
                .pragmaId(processedPragma.getPragmaId())
                .ergonId(getActivityId())
                .stageName("EGRESS")
                .status(processedPragma.getStatus() != null ? processedPragma.getStatus() : PragmaStatus.IN_PROGRESS)
                .statusMessage(getActivityName() + " successfully completed processing with " + processedPragma.getOutput().size() + " outputs")
                .build());

        // (a) Write the Pragma to cache
        getTaskCacheService().savePragma(processedPragma);

        // Convert to FHIR Task for compatibility
        Task processedTask = PragmaFhirConverter.toFhirTask(processedPragma);
        ErgonReasonEnum.ensureSyntheticTaskReason(processedTask);
        FhirSecurityTagManager.applyDefaultSecurityTag(processedTask);
        getTaskCacheService().saveTask(processedTask);
        log.info("[{}] Persisted processed Pragma/{} (Task/{}) to cache on egress",
                getActivityName(), processedPragma.getPragmaId(), processedTask.getIdPart());

        // (b) Create a new Task resource for each discrete object contained within the Task.output.payload attribute
        List<Task> createdOutgoingTasks = createOutgoingTasks(processedTask);
        exchange.setProperty(PROPERTY_OUTGOING_TASKS, createdOutgoingTasks);

        // (c) Create Provenance objects
        List<Provenance> createdProvenances = new ArrayList<>();
        if (!createdOutgoingTasks.isEmpty()) {
            for (Task outgoingTask : createdOutgoingTasks) {
                Provenance provenance = createProvenance(processedTask, outgoingTask);
                getTaskCacheService().saveProvenance(provenance);
                createdProvenances.add(provenance);
            }
        } else {
            Provenance provenance = createProvenance(processedTask, (Task) null);
            getTaskCacheService().saveProvenance(provenance);
            createdProvenances.add(provenance);
        }
        exchange.setProperty(PROPERTY_PROVENANCES, createdProvenances);
        if (!createdProvenances.isEmpty()) {
            exchange.setProperty(PROPERTY_PROVENANCE, createdProvenances.get(0));
        }

        // (d) Create TaskEvents
        List<ErgonEvent> createdErgonEvents = new ArrayList<>();
        if (!createdOutgoingTasks.isEmpty()) {
            for (Task outgoingTask : createdOutgoingTasks) {
                ErgonEvent ergonEvent = createTaskEvent(exchange, processedTask, outgoingTask);
                createdErgonEvents.add(ergonEvent);
            }
        } else {
            ErgonEvent ergonEvent = createTaskEvent(exchange, processedTask, null);
            createdErgonEvents.add(ergonEvent);
        }
        exchange.setProperty(PROPERTY_TASK_EVENTS, createdErgonEvents);

        ErgonEvent primaryOutgoingErgonEvent = !createdErgonEvents.isEmpty() ? createdErgonEvents.get(0) : createTaskEvent(exchange, processedTask, null);

        exchange.setProperty(PROPERTY_PRAGMA, processedPragma);
        exchange.setProperty(PROPERTY_INCOMING_TASK, processedTask);
        exchange.getMessage().setBody(primaryOutgoingErgonEvent);
        exchange.getMessage().setHeader(HEADER_PRAGMA_ID, processedPragma.getPragmaId());
        exchange.getMessage().setHeader(HEADER_TASK_ID, primaryOutgoingErgonEvent.getTaskId());
        exchange.getMessage().setHeader(HEADER_ACTION, primaryOutgoingErgonEvent.getAction());
        exchange.getMessage().setHeader(HEADER_STATUS, primaryOutgoingErgonEvent.getStatus());
        exchange.getMessage().setHeader(HEADER_TASK_PROCESSED, Boolean.TRUE);

        log.info("[{}] Completed egress processing: cached Pragma/{}, generated {} outputs, {} outgoing tasks",
                getActivityName(), processedPragma.getPragmaId(), processedPragma.getOutput().size(), createdOutgoingTasks.size());
    }

    /**
     * Resolves Pragma instance from Ingress payload or context.
     */
    protected Pragma resolvePragmaFromIngress(Exchange exchange, Object body) {
        if (body instanceof Pragma) {
            return (Pragma) body;
        }

        Object incPragma = exchange != null ? exchange.getProperty(PROPERTY_PRAGMA) : null;
        if (incPragma instanceof Pragma) {
            return (Pragma) incPragma;
        }

        if (body instanceof ErgonEvent) {
            ErgonEvent event = (ErgonEvent) body;
            if (StringUtils.isNotBlank(event.getTaskId())) {
                String tId = event.getTaskId();
                Optional<Pragma> cachedPragmaOpt = getTaskCacheService().getPragma(tId);
                if (cachedPragmaOpt.isPresent()) {
                    return cachedPragmaOpt.get();
                }
                if (tId.contains("-out-")) {
                    String parentId = tId.substring(0, tId.indexOf("-out-"));
                    Optional<Pragma> parentPragmaOpt = getTaskCacheService().getPragma(parentId);
                    if (parentPragmaOpt.isPresent()) {
                        return parentPragmaOpt.get();
                    }
                }
                Optional<Task> cachedTaskOpt = getTaskCacheService().getTask(tId);
                if (cachedTaskOpt.isPresent()) {
                    return PragmaFhirConverter.fromFhirTask(cachedTaskOpt.get());
                }
            }
        }

        if (body instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) body);
        }

        String taskId = extractTaskId(exchange, body);
        if (StringUtils.isNotBlank(taskId)) {
            Optional<Pragma> cachedPragmaOpt = getTaskCacheService().getPragma(taskId);
            if (cachedPragmaOpt.isPresent()) {
                return cachedPragmaOpt.get();
            }
            if (taskId.contains("-out-")) {
                String parentId = taskId.substring(0, taskId.indexOf("-out-"));
                Optional<Pragma> parentPragmaOpt = getTaskCacheService().getPragma(parentId);
                if (parentPragmaOpt.isPresent()) {
                    return parentPragmaOpt.get();
                }
            }
            Optional<Task> cachedTaskOpt = getTaskCacheService().getTask(taskId);
            if (cachedTaskOpt.isPresent()) {
                return PragmaFhirConverter.fromFhirTask(cachedTaskOpt.get());
            }
        }

        if (body instanceof String) {
            String strBody = ((String) body).trim();
            if (strBody.startsWith("{")) {
                if (strBody.contains("\"pragmaId\"") || strBody.contains("\"checkpoints\"")) {
                    try {
                        return getObjectMapper().readValue(strBody, Pragma.class);
                    } catch (Exception ignored) {
                    }
                } else if (strBody.contains("\"resourceType\"") && strBody.contains("\"Task\"")) {
                    try {
                        Task task = getJsonParser().parseResource(Task.class, strBody);
                        return PragmaFhirConverter.fromFhirTask(task);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        Object incTask = exchange != null ? exchange.getProperty(PROPERTY_INCOMING_TASK) : null;
        if (incTask instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) incTask);
        }

        return null;
    }

    /**
     * Resolves Pragma instance from Egress payload or exchange property.
     */
    protected Pragma resolvePragmaFromEgress(Exchange exchange, Object body) {
        if (body instanceof Pragma) {
            return (Pragma) body;
        }

        if (body instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) body);
        }

        Object incPragma = exchange.getProperty(PROPERTY_PRAGMA);
        if (incPragma instanceof Pragma) {
            return (Pragma) incPragma;
        }

        Object incTask = exchange.getProperty(PROPERTY_INCOMING_TASK);
        if (incTask instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) incTask);
        }

        return resolvePragmaFromIngress(exchange, body);
    }

    /**
     * Helper to extract active {@link Pragma} from exchange body or properties.
     */
    public Pragma extractPragmaFromExchange(Exchange exchange) {
        if (exchange == null) {
            return null;
        }
        Object body = exchange.getMessage() != null ? exchange.getMessage().getBody() : null;
        if (body instanceof Pragma) {
            return (Pragma) body;
        }
        if (body instanceof ErgonEvent) {
            ErgonEvent event = (ErgonEvent) body;
            if (StringUtils.isNotBlank(event.getTaskId())) {
                Optional<Pragma> pOpt = getTaskCacheService().getPragma(event.getTaskId());
                if (pOpt.isPresent()) return pOpt.get();
            }
        }
        Object prop = exchange.getProperty(PROPERTY_PRAGMA);
        if (prop instanceof Pragma) {
            return (Pragma) prop;
        }
        if (body instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) body);
        }
        Object taskProp = exchange.getProperty(PROPERTY_INCOMING_TASK);
        if (taskProp instanceof Task) {
            return PragmaFhirConverter.fromFhirTask((Task) taskProp);
        }
        return null;
    }

    /**
     * Appends an output payload to the given Pragma.
     */
    public void addOutputPayload(Pragma pragma, Topic container, Topic content, Object data) {
        if (pragma == null || data == null) {
            return;
        }
        ErgonPayload outputPayload;
        if (data instanceof IBaseResource) {
            outputPayload = ErgonPayload.fromFhirResource(pragma.getOutput().size(), container, content, (IBaseResource) data);
        } else if (data instanceof Reference) {
            outputPayload = ErgonPayload.fromFhirResource(pragma.getOutput().size(), container, content, (Reference) data);
        } else if (data instanceof String) {
            outputPayload = ErgonPayload.fromJson(pragma.getOutput().size(), container, content, (String) data);
        } else {
            outputPayload = ErgonPayload.fromJsonObject(pragma.getOutput().size(), container, content, data);
        }
        pragma.addOutput(outputPayload);
    }

    /**
     * Creates new outgoing Task resources for each discrete object in Task.output.
     *
     * @param processedTask parent processed Task
     * @return list of newly created outgoing Task resources
     */
    public List<Task> createOutgoingTasks(Task processedTask) {
        List<Task> createdTasks = new ArrayList<>();
        if (processedTask == null) {
            return createdTasks;
        }

        List<Task.TaskOutputComponent> outputs = processedTask.getOutput();
        if (outputs != null && !outputs.isEmpty()) {
            for (int i = 0; i < outputs.size(); i++) {
                Task.TaskOutputComponent outputComp = outputs.get(i);
                Task outgoingTask = new Task();
                String childTaskId = processedTask.getIdPart() + "-out-" + (i + 1);
                outgoingTask.setId("Task/" + childTaskId);
                outgoingTask.setStatus(Task.TaskStatus.REQUESTED);
                outgoingTask.setAuthoredOn(new Date());
                outgoingTask.setLastModified(new Date());

                if (processedTask.hasFor()) {
                    outgoingTask.setFor(processedTask.getFor());
                }
                if (processedTask.hasFocus()) {
                    outgoingTask.setFocus(processedTask.getFocus());
                }
                if (processedTask.hasGroupIdentifier()) {
                    outgoingTask.setGroupIdentifier(processedTask.getGroupIdentifier());
                }
                outgoingTask.addPartOf(new Reference("Task/" + processedTask.getIdPart()).setType("Task").setDisplay("Parent Task"));

                if (outputComp.hasValue()) {
                    Task.TaskInputComponent inputComp = outgoingTask.addInput();
                    if (outputComp.hasType()) {
                        inputComp.setType(outputComp.getType());
                    }
                    inputComp.setValue(outputComp.getValue());

                    if (outputComp.getValue() instanceof Reference) {
                        Reference ref = (Reference) outputComp.getValue();
                        String refStr = ref.getReference();
                        if (refStr != null && refStr.startsWith("#")) {
                            String containedId = refStr.substring(1);
                            for (Resource res : processedTask.getContained()) {
                                if (containedId.equals(res.getIdPart()) || containedId.equals(res.getId())) {
                                    outgoingTask.addContained(res);
                                }
                            }
                        }
                    }
                }

                for (Resource res : processedTask.getContained()) {
                    boolean alreadyContained = outgoingTask.getContained().stream()
                            .anyMatch(c -> Objects.equals(c.getIdPart(), res.getIdPart()) && Objects.equals(c.fhirType(), res.fhirType()));
                    if (!alreadyContained) {
                        outgoingTask.addContained(res);
                    }
                }

                String outputDesc = outputComp.hasType() && outputComp.getType().hasText()
                        ? outputComp.getType().getText() : "discrete-output-" + (i + 1);
                outgoingTask.setDescription("Outgoing Task created by " + getActivityName() + " for " + outputDesc);
                ErgonReasonEnum.ensureSyntheticTaskReason(outgoingTask);
                FhirSecurityTagManager.applyDefaultSecurityTag(outgoingTask);

                getTaskCacheService().saveTask(outgoingTask);
                createdTasks.add(outgoingTask);
                log.info("[{}] Created discrete outgoing Task/{} from Task/{} output [{}]",
                        getActivityName(), childTaskId, processedTask.getIdPart(), outputDesc);
            }
        } else {
            Task outgoingTask = new Task();
            String childTaskId = processedTask.getIdPart() + "-out-1";
            outgoingTask.setId("Task/" + childTaskId);
            outgoingTask.setStatus(Task.TaskStatus.REQUESTED);
            outgoingTask.setAuthoredOn(new Date());
            outgoingTask.setLastModified(new Date());

            if (processedTask.hasFor()) {
                outgoingTask.setFor(processedTask.getFor());
            }
            if (processedTask.hasFocus()) {
                outgoingTask.setFocus(processedTask.getFocus());
            }
            if (processedTask.hasGroupIdentifier()) {
                outgoingTask.setGroupIdentifier(processedTask.getGroupIdentifier());
            }
            outgoingTask.addPartOf(new Reference("Task/" + processedTask.getIdPart()).setType("Task").setDisplay("Parent Task"));

            for (Resource res : processedTask.getContained()) {
                outgoingTask.addContained(res);
            }

            outgoingTask.setDescription("Outgoing Task created by " + getActivityName() + " following task processing");
            ErgonReasonEnum.ensureSyntheticTaskReason(outgoingTask);
            FhirSecurityTagManager.applyDefaultSecurityTag(outgoingTask);

            getTaskCacheService().saveTask(outgoingTask);
            createdTasks.add(outgoingTask);
        }

        return createdTasks;
    }

    /**
     * Creates a FHIR Provenance resource linking incoming Task and a single created outgoing Task.
     *
     * @param incomingTask source incoming Task
     * @param outgoingTask generated outgoing Task
     * @return constructed Provenance resource
     */
    public Provenance createProvenance(Task incomingTask, Task outgoingTask) {
        Provenance provenance = new Provenance();
        String outId = outgoingTask != null && outgoingTask.getIdPart() != null
                ? outgoingTask.getIdPart()
                : (incomingTask != null && incomingTask.getIdPart() != null ? incomingTask.getIdPart() : UUID.randomUUID().toString());
        String provId = "prov-" + outId + "-" + System.currentTimeMillis();
        provenance.setId("Provenance/" + provId);
        provenance.setRecorded(new Date());

        if (outgoingTask != null) {
            Reference targetRef = new Reference("Task/" + outgoingTask.getIdPart()).setType("Task");
            if (outgoingTask.hasDescription()) {
                targetRef.setDisplay(outgoingTask.getDescription());
            }
            provenance.addTarget(targetRef);
        }

        if (incomingTask != null) {
            Provenance.ProvenanceEntityComponent entity = provenance.addEntity();
            entity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
            entity.setWhat(new Reference("Task/" + incomingTask.getIdPart()).setType("Task").setDisplay("Source Incoming Task"));

            if (incomingTask.hasFor()) {
                provenance.setPatient(incomingTask.getFor());
            }
        }

        CodeableConcept activityConcept = new CodeableConcept();
        activityConcept.setText(getActivityName());
        activityConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "TRANSFORM", "Transform"));
        provenance.setActivity(activityConcept);

        Provenance.ProvenanceAgentComponent agent = provenance.addAgent();
        agent.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler")));
        agent.setWho(new Reference("Device/" + getActivityId()).setDisplay(getActivityName()));

        FhirSecurityTagManager.applyDefaultSecurityTag(provenance);
        return provenance;
    }

    /**
     * Creates FHIR Provenance resources for each created outgoing Task.
     *
     * @param incomingTask  source incoming Task
     * @param outgoingTasks list of generated outgoing Tasks
     * @return list of constructed Provenance resources
     */
    public List<Provenance> createProvenances(Task incomingTask, List<Task> outgoingTasks) {
        List<Provenance> result = new ArrayList<>();
        if (outgoingTasks != null && !outgoingTasks.isEmpty()) {
            for (Task outTask : outgoingTasks) {
                result.add(createProvenance(incomingTask, outTask));
            }
        } else {
            result.add(createProvenance(incomingTask, (Task) null));
        }
        return result;
    }

    /**
     * Creates a FHIR Provenance resource linking incoming Task and created outgoing Tasks.
     *
     * @param incomingTask  source incoming Task
     * @param outgoingTasks list of generated outgoing Tasks
     * @return constructed Provenance resource
     */
    public Provenance createProvenance(Task incomingTask, List<Task> outgoingTasks) {
        Provenance provenance = new Provenance();
        String provId = "prov-" + (incomingTask != null && incomingTask.getIdPart() != null ? incomingTask.getIdPart() : UUID.randomUUID().toString())
                + "-" + System.currentTimeMillis();
        provenance.setId("Provenance/" + provId);
        provenance.setRecorded(new Date());

        if (outgoingTasks != null) {
            for (Task outTask : outgoingTasks) {
                Reference targetRef = new Reference("Task/" + outTask.getIdPart()).setType("Task");
                if (outTask.hasDescription()) {
                    targetRef.setDisplay(outTask.getDescription());
                }
                provenance.addTarget(targetRef);
            }
        }

        if (incomingTask != null) {
            Provenance.ProvenanceEntityComponent entity = provenance.addEntity();
            entity.setRole(Provenance.ProvenanceEntityRole.SOURCE);
            entity.setWhat(new Reference("Task/" + incomingTask.getIdPart()).setType("Task").setDisplay("Source Incoming Task"));

            if (incomingTask.hasFor()) {
                provenance.setPatient(incomingTask.getFor());
            }
        }

        CodeableConcept activityConcept = new CodeableConcept();
        activityConcept.setText(getActivityName());
        activityConcept.addCoding(new Coding("http://terminology.hl7.org/CodeSystem/v3-DataOperation", "TRANSFORM", "Transform"));
        provenance.setActivity(activityConcept);

        Provenance.ProvenanceAgentComponent agent = provenance.addAgent();
        agent.setType(new CodeableConcept().addCoding(new Coding("http://terminology.hl7.org/CodeSystem/provenance-participant-type", "assembler", "Assembler")));
        agent.setWho(new Reference("Device/" + getActivityId()).setDisplay(getActivityName()));

        return provenance;
    }

    /**
     * Constructs TaskEvents for each generated outgoing Task.
     *
     * @param exchange      Camel Exchange
     * @param incomingTask  source incoming Task
     * @param outgoingTasks list of generated outgoing Tasks
     * @return list of constructed TaskEvents
     */
    public List<ErgonEvent> createTaskEvents(Exchange exchange, Task incomingTask, List<Task> outgoingTasks) {
        List<ErgonEvent> result = new ArrayList<>();
        if (outgoingTasks != null && !outgoingTasks.isEmpty()) {
            for (Task outTask : outgoingTasks) {
                result.add(createTaskEvent(exchange, incomingTask, outTask));
            }
        } else {
            result.add(createTaskEvent(exchange, incomingTask, null));
        }
        return result;
    }

    /**
     * Constructs a TaskEvent notification for downstream route routing.
     *
     * @param exchange     Camel Exchange
     * @param incomingTask source incoming Task
     * @param outgoingTask target primary outgoing Task
     * @return constructed TaskEvent
     */
    public ErgonEvent createTaskEvent(Exchange exchange, Task incomingTask, Task outgoingTask) {
        String taskId = outgoingTask != null ? outgoingTask.getIdPart() : (incomingTask != null ? incomingTask.getIdPart() : "TASK-UNKNOWN");
        ErgonEvent ergonEvent = new ErgonEvent();
        ergonEvent.setTaskId(taskId);
        ergonEvent.setAction("PROCESS");
        ergonEvent.setStatus("requested");
        ergonEvent.setSource(getActivityId());
        ergonEvent.setTimestamp(new Date());

        String patientDisplay = null;
        if (outgoingTask != null && outgoingTask.hasFor() && outgoingTask.getFor().hasDisplay()) {
            patientDisplay = outgoingTask.getFor().getDisplay();
        } else if (incomingTask != null && incomingTask.hasFor() && incomingTask.getFor().hasDisplay()) {
            patientDisplay = incomingTask.getFor().getDisplay();
        }

        String desc = getActivityName() + " processed Task" + (patientDisplay != null ? " for patient " + patientDisplay : "");
        ergonEvent.setDescription(desc);

        if (exchange != null && exchange.getMessage() != null) {
            Topic topic = exchange.getMessage().getHeader(HEADER_TOPIC, Topic.class);
            if (topic != null) {
                ergonEvent.setTopic(topic);
            }
            String gw = exchange.getMessage().getHeader(HEADER_GATEWAY_INSTANCE, String.class);
            if (StringUtils.isNotBlank(gw)) ergonEvent.setGatewayInstanceId(gw);
            String trigger = exchange.getMessage().getHeader(HEADER_TRIGGER_TYPE, String.class);
            if (StringUtils.isNotBlank(trigger)) ergonEvent.setTriggerType(trigger);
            String msgType = exchange.getMessage().getHeader(HEADER_MESSAGE_TYPE, String.class);
            if (StringUtils.isNotBlank(msgType)) ergonEvent.setMessageType(msgType);
            String ctrlId = exchange.getMessage().getHeader(HEADER_CONTROL_ID, String.class);
            if (StringUtils.isNotBlank(ctrlId)) ergonEvent.setControlId(ctrlId);
        }

        return ergonEvent;
    }

    /**
     * Resolves the Task from egress message body or exchange context.
     */
    private Task resolveTaskFromEgressBody(Exchange exchange, Object body) {
        if (body instanceof Task) {
            return (Task) body;
        }

        if (body instanceof String) {
            String str = ((String) body).trim();
            if (str.startsWith("{") && str.contains("\"resourceType\"") && str.contains("\"Task\"")) {
                try {
                    return getJsonParser().parseResource(Task.class, str);
                } catch (Exception ignored) {
                }
            }
        }

        String taskId = exchange.getMessage().getHeader(HEADER_TASK_ID, String.class);
        if (StringUtils.isNotBlank(taskId)) {
            Optional<Task> cached = getTaskCacheService().getTask(taskId);
            if (cached.isPresent()) {
                return cached.get();
            }
        }

        Object incoming = exchange.getProperty(PROPERTY_INCOMING_TASK);
        if (incoming instanceof Task) {
            return (Task) incoming;
        }

        return null;
    }

    /**
     * Extracts Task ID from exchange headers or message body.
     */
    private String extractTaskId(Exchange exchange, Object body) {
        if (exchange != null && exchange.getMessage() != null) {
            String headerId = exchange.getMessage().getHeader(HEADER_TASK_ID, String.class);
            if (StringUtils.isNotBlank(headerId)) {
                return cleanId(headerId);
            }
            String taskIdLower = exchange.getMessage().getHeader("taskId", String.class);
            if (StringUtils.isNotBlank(taskIdLower)) {
                return cleanId(taskIdLower);
            }
        }

        if (body instanceof ErgonEvent) {
            return cleanId(((ErgonEvent) body).getTaskId());
        }

        if (body instanceof Task) {
            return cleanId(((Task) body).getIdPart());
        }

        if (body instanceof byte[]) {
            body = new String((byte[]) body, StandardCharsets.UTF_8);
        }

        if (body instanceof String) {
            String strBody = ((String) body).trim();
            if (strBody.startsWith("{")) {
                try {
                    JsonNode node = getObjectMapper().readTree(strBody);
                    if (node.has("taskId")) {
                        return cleanId(node.get("taskId").asText());
                    }
                    if (node.has("id") && "Task".equalsIgnoreCase(node.path("resourceType").asText())) {
                        return cleanId(node.get("id").asText());
                    }
                } catch (Exception ignored) {
                }
            }
        }

        if (exchange != null && exchange.getMessage() != null) {
            String controlId = exchange.getMessage().getHeader(HEADER_CONTROL_ID, String.class);
            if (StringUtils.isNotBlank(controlId)) {
                return cleanId(controlId);
            }
        }

        return null;
    }

    public TaskCacheService getTaskCacheService() {
        if (this.taskCacheService == null) {
            this.taskCacheService = new TaskCacheService(null, getFhirContext());
        }
        return this.taskCacheService;
    }

    public void setTaskCacheService(TaskCacheService taskCacheService) {
        this.taskCacheService = taskCacheService;
    }

    public FhirContext getFhirContext() {
        if (this.fhirContext == null) {
            this.fhirContext = FhirContext.forR5();
        }
        return this.fhirContext;
    }

    public void setFhirContext(FhirContext fhirContext) {
        this.fhirContext = fhirContext;
    }

    public IParser getJsonParser() {
        return getFhirContext().newJsonParser().setPrettyPrint(true);
    }

    public ObjectMapper getObjectMapper() {
        if (this.objectMapper == null) {
            this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
        }
        return this.objectMapper;
    }

    public void setObjectMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    /**
     * Helper to establish standard logging and metadata on a route definition.
     *
     * @param routeDefinition Camel RouteDefinition
     * @return decorated RouteDefinition
     */
    protected RouteDefinition applyStandardActivityDecorations(RouteDefinition routeDefinition) {
        return routeDefinition
                .routeId(generateRouteId())
                .routeDescription(activityDescription != null ? activityDescription : activityName)
                .log(LoggingLevel.DEBUG, log.getName(), "Starting execution of activity [" + activityName + "] on exchange ${exchangeId}");
    }

    /**
     * Generates a default route ID based on the activity identifier.
     *
     * @return route identifier string
     */
    public String generateRouteId() {
        if (activityId != null && !activityId.isBlank()) {
            return "activity-" + activityId.toLowerCase().replaceAll("[^a-z0-9-_]", "-");
        }
        return "activity-" + getClass().getSimpleName().toLowerCase();
    }

    protected String cleanId(String id) {
        if (id == null) return "";
        String clean = id.trim();
        if (clean.startsWith("#")) {
            clean = clean.substring(1);
        }
        if (clean.startsWith("Task/")) {
            clean = clean.substring("Task/".length());
        }
        if (clean.startsWith("Patient/")) {
            clean = clean.substring("Patient/".length());
        }
        return clean;
    }

    // Getters and Setters

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getActivityName() {
        return activityName;
    }

    public void setActivityName(String activityName) {
        this.activityName = activityName;
    }

    public String getActivityDescription() {
        return activityDescription;
    }

    public void setActivityDescription(String activityDescription) {
        this.activityDescription = activityDescription;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getInputEndpoint() {
        return inputEndpoint;
    }

    public void setInputEndpoint(String inputEndpoint) {
        this.inputEndpoint = inputEndpoint;
    }

    public String getOutputEndpoint() {
        return outputEndpoint;
    }

    public void setOutputEndpoint(String outputEndpoint) {
        this.outputEndpoint = outputEndpoint;
    }

    public String getErrorEndpoint() {
        return errorEndpoint;
    }

    public void setErrorEndpoint(String errorEndpoint) {
        this.errorEndpoint = errorEndpoint;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ErgonSecurityDefinition getSecurityDefinition() {
        return securityDefinition;
    }

    public void setSecurityDefinition(ErgonSecurityDefinition securityDefinition) {
        this.securityDefinition = securityDefinition;
    }

    @Override
    public String toString() {
        return "TaskProcessingActivity{" +
                "activityId='" + activityId + '\'' +
                ", activityName='" + activityName + '\'' +
                ", version='" + version + '\'' +
                ", enabled=" + enabled +
                '}';
    }
}
