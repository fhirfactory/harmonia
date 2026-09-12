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

package net.fhirfactory.hie.taskprocessors.base;

import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.parser.IParser;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.inject.Inject;
import net.fhirfactory.hie.model.TaskEvent;
import net.fhirfactory.hie.model.task.HieTaskReason;
import net.fhirfactory.hie.model.topic.Topic;
import net.fhirfactory.hie.taskprocessor.cache.TaskCacheService;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.RouteDefinition;
import org.apache.commons.lang3.StringUtils;
import org.hl7.fhir.r5.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.*;

/**
 * Superclass for Task Processing Activities in the HIE Workflow Services.
 * <p>
 * Inherits from Apache Camel {@link RouteBuilder} to define and manage declarative integration routes,
 * activity lifecycle, and execution pipelines for task processing.
 * <p>
 * Implements ingress Task retrieval from cache and egress persistence, child Task creation for discrete outputs,
 * Provenance tracking, and TaskEvent generation for downstream routing.
 */
public abstract class TaskProcessingActivity extends RouteBuilder {

    protected final Logger log = LoggerFactory.getLogger(getClass());

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

    /**
     * Default constructor initializing default identifiers.
     */
    public TaskProcessingActivity() {
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
    public TaskProcessingActivity(String activityId, String activityName) {
        super();
        this.activityId = activityId;
        this.activityName = activityName;
    }

    /**
     * Constructor with CamelContext.
     *
     * @param context CamelContext instance
     */
    public TaskProcessingActivity(CamelContext context) {
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
    public TaskProcessingActivity(CamelContext context, String activityId, String activityName) {
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
     * Subclasses override this method to perform actual business processing.
     *
     * @param exchange Camel Exchange containing the ingress Task in the message body
     * @throws Exception on processing error
     */
    protected void processActivity(Exchange exchange) throws Exception {
        // Default no-op for base classes or overridden in subclasses
    }

    /**
     * 1. Ingress Processing:
     * Retrieves the Task from the cache and sets it as the ingress within the Body
     * of the Camel Message within the Exchange.
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

        Task task = null;
        if (StringUtils.isNotBlank(taskId)) {
            Optional<Task> cachedTaskOpt = getTaskCacheService().getTask(taskId);
            if (cachedTaskOpt.isPresent()) {
                task = cachedTaskOpt.get();
                log.info("[{}] Retrieved Task/{} from cache for ingress", getActivityName(), taskId);
            }
        }

        if (task == null) {
            if (body instanceof Task) {
                task = (Task) body;
                if (StringUtils.isBlank(task.getIdPart()) && StringUtils.isNotBlank(taskId)) {
                    task.setId("Task/" + cleanId(taskId));
                }
            } else if (body instanceof String) {
                String strBody = ((String) body).trim();
                if (strBody.startsWith("{") && strBody.contains("\"resourceType\"") && strBody.contains("\"Task\"")) {
                    try {
                        task = getJsonParser().parseResource(Task.class, strBody);
                    } catch (Exception ignored) {
                    }
                }
            }
        }

        if (task == null) {
            if (StringUtils.isBlank(taskId)) {
                taskId = "TASK-" + UUID.randomUUID().toString().substring(0, 8);
            }
            task = new Task();
            task.setId("Task/" + cleanId(taskId));
            task.setStatus(Task.TaskStatus.REQUESTED);
            task.setAuthoredOn(new Date());
            task.setLastModified(new Date());
            task.setDescription("Task for activity execution: " + getActivityName());
            HieTaskReason.ensureSyntheticTaskReason(task);

            if (body != null && exchange.getMessage().getHeader(HEADER_RAW_MESSAGE) == null) {
                exchange.getMessage().setHeader(HEADER_RAW_MESSAGE, body.toString());
            }

            getTaskCacheService().saveTask(task);
            log.info("[{}] Created baseline Task/{} in cache for ingress", getActivityName(), taskId);
        }

        exchange.setProperty(PROPERTY_INCOMING_TASK, task);
        exchange.getMessage().setBody(task);
        exchange.getMessage().setHeader(HEADER_TASK_ID, task.getIdPart());
    }

    /**
     * 2. Egress Processing:
     * Receives from the actual Body of Exchange coming OUT of the processing done in subclasses a Task resource and:
     * (a) writes the task to the cache,
     * (b) creates a new Task resource for each discrete object contained within the Task.output.payload attribute,
     * (c) creates a new Provenance object for each discrete new Task generated from the Task.output.payload attribute, and
     * (d) creates a new TaskEvent for each discrete new Task generated from the Task.output.payload attribute.
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
        Task processedTask = resolveTaskFromEgressBody(exchange, body);

        if (processedTask == null) {
            log.warn("[{}] Egress could not resolve Task from body or exchange, skipping egress processing", getActivityName());
            return;
        }

        // (a) Write the task to the cache
        processedTask.setLastModified(new Date());
        HieTaskReason.ensureSyntheticTaskReason(processedTask);
        getTaskCacheService().saveTask(processedTask);
        log.info("[{}] Persisted processed Task/{} to cache on egress", getActivityName(), processedTask.getIdPart());

        // (b) Create a new Task resource for each discrete object contained within the Task.output.payload attribute
        List<Task> createdOutgoingTasks = createOutgoingTasks(processedTask);
        exchange.setProperty(PROPERTY_OUTGOING_TASKS, createdOutgoingTasks);

        // (c) Create a new Provenance object for each discrete new Task generated from Task.output
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

        // (d) Create a new TaskEvent for each discrete new Task generated from Task.output
        List<TaskEvent> createdTaskEvents = new ArrayList<>();
        if (!createdOutgoingTasks.isEmpty()) {
            for (Task outgoingTask : createdOutgoingTasks) {
                TaskEvent taskEvent = createTaskEvent(exchange, processedTask, outgoingTask);
                createdTaskEvents.add(taskEvent);
            }
        } else {
            TaskEvent taskEvent = createTaskEvent(exchange, processedTask, null);
            createdTaskEvents.add(taskEvent);
        }
        exchange.setProperty(PROPERTY_TASK_EVENTS, createdTaskEvents);

        TaskEvent primaryOutgoingTaskEvent = !createdTaskEvents.isEmpty() ? createdTaskEvents.get(0) : createTaskEvent(exchange, processedTask, null);

        exchange.getMessage().setBody(primaryOutgoingTaskEvent);
        exchange.getMessage().setHeader(HEADER_TASK_ID, primaryOutgoingTaskEvent.getTaskId());
        exchange.getMessage().setHeader(HEADER_ACTION, primaryOutgoingTaskEvent.getAction());
        exchange.getMessage().setHeader(HEADER_STATUS, primaryOutgoingTaskEvent.getStatus());
        exchange.getMessage().setHeader(HEADER_TASK_PROCESSED, Boolean.TRUE);

        log.info("[{}] Completed egress processing: cached Task/{}, generated {} outgoing tasks, {} Provenance objects, {} TaskEvents (primary [taskId={}])",
                getActivityName(), processedTask.getIdPart(), createdOutgoingTasks.size(),
                createdProvenances.size(), createdTaskEvents.size(), primaryOutgoingTaskEvent.getTaskId());
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
                HieTaskReason.ensureSyntheticTaskReason(outgoingTask);

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
            HieTaskReason.ensureSyntheticTaskReason(outgoingTask);

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
    public List<TaskEvent> createTaskEvents(Exchange exchange, Task incomingTask, List<Task> outgoingTasks) {
        List<TaskEvent> result = new ArrayList<>();
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
    public TaskEvent createTaskEvent(Exchange exchange, Task incomingTask, Task outgoingTask) {
        String taskId = outgoingTask != null ? outgoingTask.getIdPart() : (incomingTask != null ? incomingTask.getIdPart() : "TASK-UNKNOWN");
        TaskEvent taskEvent = new TaskEvent();
        taskEvent.setTaskId(taskId);
        taskEvent.setAction("PROCESS");
        taskEvent.setStatus("requested");
        taskEvent.setSource(getActivityId());
        taskEvent.setTimestamp(new Date());

        String patientDisplay = null;
        if (outgoingTask != null && outgoingTask.hasFor() && outgoingTask.getFor().hasDisplay()) {
            patientDisplay = outgoingTask.getFor().getDisplay();
        } else if (incomingTask != null && incomingTask.hasFor() && incomingTask.getFor().hasDisplay()) {
            patientDisplay = incomingTask.getFor().getDisplay();
        }

        String desc = getActivityName() + " processed Task" + (patientDisplay != null ? " for patient " + patientDisplay : "");
        taskEvent.setDescription(desc);

        if (exchange != null && exchange.getMessage() != null) {
            Topic topic = exchange.getMessage().getHeader(HEADER_TOPIC, Topic.class);
            if (topic != null) {
                taskEvent.setTopic(topic);
            }
            String gw = exchange.getMessage().getHeader(HEADER_GATEWAY_INSTANCE, String.class);
            if (StringUtils.isNotBlank(gw)) taskEvent.setGatewayInstanceId(gw);
            String trigger = exchange.getMessage().getHeader(HEADER_TRIGGER_TYPE, String.class);
            if (StringUtils.isNotBlank(trigger)) taskEvent.setTriggerType(trigger);
            String msgType = exchange.getMessage().getHeader(HEADER_MESSAGE_TYPE, String.class);
            if (StringUtils.isNotBlank(msgType)) taskEvent.setMessageType(msgType);
            String ctrlId = exchange.getMessage().getHeader(HEADER_CONTROL_ID, String.class);
            if (StringUtils.isNotBlank(ctrlId)) taskEvent.setControlId(ctrlId);
        }

        return taskEvent;
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

        if (body instanceof TaskEvent) {
            return cleanId(((TaskEvent) body).getTaskId());
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
            this.objectMapper = new ObjectMapper();
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
