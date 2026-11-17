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

package net.fhirfactory.harmonia.model.core.workflow.tasks;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.workflow.tasks.datatypes.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.PragmaStatus;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Canonical task instance and workflow execution state entity in the Harmonia integration platform.
 * <p>
 * <b>Pragma (&pi;&rho;&#940;&gamma;&mu;&alpha;)</b> represents a discrete unit of work / task instance.
 * It encapsulates strongly typed input payloads ({@link #getInput()}), accumulating output artifacts
 * ({@link #getOutput()}) produced across single-function Ergon processing stages, and an immutable
 * sequence of execution audit checkpoints ({@link #getCheckpoints()}).
 * <p>
 * Pragma instances are decoupled from transport mechanisms and underlying FHIR representations,
 * while supporting bi-directional mapping to FHIR R5 {@code Task} resources at platform boundaries.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Pragma implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("pragmaId")
    @JsonAlias({"id", "taskId", "pragma_id", "task_id"})
    private String pragmaId;

    @JsonProperty("correlationId")
    @JsonAlias({"correlation_id", "correlation"})
    private String correlationId;

    @JsonProperty("causationId")
    @JsonAlias({"causation_id", "causation"})
    private String causationId;

    @JsonProperty("praxisId")
    @JsonAlias({"sequenceId", "praxis_id", "workflowId"})
    private String praxisId;

    @JsonProperty("status")
    private PragmaStatus status = PragmaStatus.DRAFT;

    @JsonProperty("priority")
    private Integer priority;

    @JsonProperty("priorityCode")
    @JsonAlias({"priority_code"})
    private String priorityCode;

    @JsonProperty("authoredOn")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date authoredOn;

    @JsonProperty("lastModified")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ss.SSSXXX", timezone = "UTC")
    private Date lastModified;

    @JsonProperty("source")
    @JsonAlias({"sourceEndpoint", "source_endpoint", "requester"})
    private String source;

    @JsonProperty("destination")
    @JsonAlias({"destinationEndpoint", "destination_endpoint", "target"})
    private String destination;

    @JsonProperty("input")
    @JsonAlias({"inputs", "taskInputs", "inputPayloads"})
    private List<ErgonPayload> input = new ArrayList<>();

    @JsonProperty("output")
    @JsonAlias({"outputs", "taskOutputs", "outputPayloads"})
    private List<ErgonPayload> output = new ArrayList<>();

    @JsonProperty("checkpoints")
    @JsonAlias({"checkpointList", "auditTrail", "checkpointsList"})
    private List<PragmaCheckpoint> checkpoints = new ArrayList<>();

    @JsonProperty("metadata")
    private Map<String, String> metadata = new LinkedHashMap<>();

    @JsonProperty("originatingPrincipal")
    @JsonAlias({"originating_principal", "principal"})
    private ThemisPrincipal originatingPrincipal;

    @JsonProperty("executingPrincipal")
    @JsonAlias({"executing_principal", "executor"})
    private ThemisPrincipal executingPrincipal;

    @JsonProperty("originatingAuthorities")
    @JsonAlias({"originating_authorities", "authorities"})
    private Set<ThemisAuthority> originatingAuthorities = new HashSet<>();

    @JsonProperty("originatingSecurityContext")
    @JsonAlias({"originating_security_context", "securityContext", "security_context"})
    private ThemisSecurityContext originatingSecurityContext;

    @JsonProperty("policyVersion")
    @JsonAlias({"policy_version"})
    private String policyVersion = "1.0.0";

    /**
     * Default constructor.
     */
    public Pragma() {
        this.pragmaId = UUID.randomUUID().toString();
        this.correlationId = UUID.randomUUID().toString();
        this.authoredOn = new Date();
        this.lastModified = new Date(this.authoredOn.getTime());
        this.status = PragmaStatus.DRAFT;
    }

    /**
     * Parameterized constructor for quick initialization.
     *
     * @param pragmaId  unique task ID
     * @param praxisId  associated workflow identifier
     * @param status    initial task status
     */
    public Pragma(String pragmaId, String praxisId, PragmaStatus status) {
        this();
        if (pragmaId != null) {
            this.pragmaId = pragmaId;
        }
        this.praxisId = praxisId;
        this.status = status != null ? status : PragmaStatus.DRAFT;
    }

    /**
     * Full constructor.
     */
    public Pragma(String pragmaId, String correlationId, String causationId, String praxisId,
                  PragmaStatus status, Integer priority, String priorityCode,
                  Date authoredOn, Date lastModified, String source, String destination,
                  List<ErgonPayload> input, List<ErgonPayload> output,
                  List<PragmaCheckpoint> checkpoints, Map<String, String> metadata) {
        this.pragmaId = pragmaId != null ? pragmaId : UUID.randomUUID().toString();
        this.correlationId = correlationId != null ? correlationId : UUID.randomUUID().toString();
        this.causationId = causationId;
        this.praxisId = praxisId;
        this.status = status != null ? status : PragmaStatus.DRAFT;
        this.priority = priority;
        this.priorityCode = priorityCode;
        this.authoredOn = authoredOn != null ? new Date(authoredOn.getTime()) : new Date();
        this.lastModified = lastModified != null ? new Date(lastModified.getTime()) : new Date(this.authoredOn.getTime());
        this.source = source;
        this.destination = destination;
        if (input != null) input.forEach(p -> this.input.add(new ErgonPayload(p)));
        if (output != null) output.forEach(p -> this.output.add(new ErgonPayload(p)));
        if (checkpoints != null) checkpoints.forEach(cp -> this.checkpoints.add(new PragmaCheckpoint(cp)));
        if (metadata != null) this.metadata.putAll(metadata);
    }

    public Pragma(Pragma other) {
        if (other != null) {
            this.pragmaId = other.pragmaId;
            this.correlationId = other.correlationId;
            this.causationId = other.causationId;
            this.praxisId = other.praxisId;
            this.status = other.status;
            this.priority = other.priority;
            this.priorityCode = other.priorityCode;
            this.authoredOn = other.authoredOn != null ? new Date(other.authoredOn.getTime()) : new Date();
            this.lastModified = other.lastModified != null ? new Date(other.lastModified.getTime()) : new Date();
            this.source = other.source;
            this.destination = other.destination;
            if (other.input != null) other.input.forEach(p -> this.input.add(new ErgonPayload(p)));
            if (other.output != null) other.output.forEach(p -> this.output.add(new ErgonPayload(p)));
            if (other.checkpoints != null) other.checkpoints.forEach(cp -> this.checkpoints.add(new PragmaCheckpoint(cp)));
            if (other.metadata != null) this.metadata.putAll(other.metadata);
            this.originatingPrincipal = other.originatingPrincipal;
            this.executingPrincipal = other.executingPrincipal;
            if (other.originatingAuthorities != null) this.originatingAuthorities.addAll(other.originatingAuthorities);
            this.originatingSecurityContext = other.originatingSecurityContext;
            this.policyVersion = other.policyVersion;
        } else {
            this.pragmaId = UUID.randomUUID().toString();
            this.correlationId = UUID.randomUUID().toString();
            this.authoredOn = new Date();
            this.lastModified = new Date(this.authoredOn.getTime());
            this.status = PragmaStatus.DRAFT;
        }
    }


    // =========================================================================
    // Convenience / Mutation Methods
    // =========================================================================

    /**
     * Appends an input payload to this Pragma, automatically setting its order index if unset.
     *
     * @param payload input payload
     * @return this Pragma instance
     */
    public Pragma addInput(ErgonPayload payload) {
        if (payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(this.input.size());
            }
            this.input.add(payload);
            touch();
        }
        return this;
    }

    /**
     * Appends an output payload to this Pragma, automatically setting its order index if unset.
     *
     * @param payload output payload
     * @return this Pragma instance
     */
    public Pragma addOutput(ErgonPayload payload) {
        if (payload != null) {
            if (payload.getPayloadOrder() == null) {
                payload.setPayloadOrder(this.output.size());
            }
            this.output.add(payload);
            touch();
        }
        return this;
    }

    /**
     * Records an execution checkpoint, updating the Pragma's status and last-modified timestamp.
     *
     * @param checkpoint checkpoint record
     * @return this Pragma instance
     */
    public Pragma addCheckpoint(PragmaCheckpoint checkpoint) {
        if (checkpoint != null) {
            if (checkpoint.getPragmaId() == null) {
                checkpoint.setPragmaId(this.pragmaId);
            }
            if (checkpoint.getPraxisId() == null) {
                checkpoint.setPraxisId(this.praxisId);
            }
            if (checkpoint.getStepIndex() == null) {
                checkpoint.setStepIndex(this.checkpoints.size());
            }
            if (checkpoint.getStatus() != null) {
                this.status = checkpoint.getStatus();
            }
            this.checkpoints.add(checkpoint);
            touch();
        }
        return this;
    }

    /**
     * Returns the most recently recorded checkpoint, if any.
     *
     * @return optional latest checkpoint
     */
    @JsonIgnore
    public Optional<PragmaCheckpoint> getLatestCheckpoint() {
        if (checkpoints.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(checkpoints.get(checkpoints.size() - 1));
    }


    /**
     * Updates the {@link #lastModified} timestamp to current time.
     */
    public void touch() {
        this.lastModified = new Date();
    }

    // =========================================================================
    // Getters and Setters
    // =========================================================================

    /** Returns this task instance's identifier. */
    public String getPragmaId() { return pragmaId; }
    /** Sets this task instance's identifier. */
    public void setPragmaId(String pragmaId) { this.pragmaId = pragmaId; }

    /** Returns the workflow correlation identifier. */
    public String getCorrelationId() { return correlationId; }
    /** Sets the workflow correlation identifier. */
    public void setCorrelationId(String correlationId) { this.correlationId = correlationId; }

    /** Returns the causal predecessor identifier. */
    public String getCausationId() { return causationId; }
    /** Sets the causal predecessor identifier. */
    public void setCausationId(String causationId) { this.causationId = causationId; }

    /** Returns the Praxis definition responsible for this task. */
    public String getPraxisId() { return praxisId; }
    /** Sets the Praxis definition responsible for this task. */
    public void setPraxisId(String praxisId) { this.praxisId = praxisId; }

    /** Returns the current governed task status. */
    public PragmaStatus getStatus() { return status; }
    /** Replaces status, defaulting null to DRAFT, and records the modification time. */
    public void setStatus(PragmaStatus status) { this.status = status != null ? status : PragmaStatus.DRAFT; touch(); }

    /** Returns the task priority value. */
    public Integer getPriority() { return priority; }
    /** Sets the task priority value. */
    public void setPriority(Integer priority) { this.priority = priority; }

    /** Returns the source-system priority code. */
    public String getPriorityCode() { return priorityCode; }
    /** Sets the source-system priority code. */
    public void setPriorityCode(String priorityCode) { this.priorityCode = priorityCode; }

    /** Returns a defensive copy of the authored timestamp. */
    public Date getAuthoredOn() { return authoredOn != null ? new Date(authoredOn.getTime()) : null; }
    /** Stores a defensive copy of the authored timestamp. */
    public void setAuthoredOn(Date authoredOn) { this.authoredOn = authoredOn != null ? new Date(authoredOn.getTime()) : null; }

    /** Returns a defensive copy of the last-modified timestamp. */
    public Date getLastModified() { return lastModified != null ? new Date(lastModified.getTime()) : null; }
    /** Stores a defensive copy of the last-modified timestamp. */
    public void setLastModified(Date lastModified) { this.lastModified = lastModified != null ? new Date(lastModified.getTime()) : null; }

    /** Returns the source identifier or display value. */
    public String getSource() { return source; }
    /** Sets the source identifier or display value. */
    public void setSource(String source) { this.source = source; }

    /** Returns the destination identifier or display value. */
    public String getDestination() { return destination; }
    /** Sets the destination identifier or display value. */
    public void setDestination(String destination) { this.destination = destination; }

    /** Returns the mutable FHIR interchange input list. */
    public List<ErgonPayload> getInput() { return input; }
    /** Replaces input payloads with defensive copies and advances last-modified time. */
    public void setInput(List<ErgonPayload> input) { this.input = input != null ? new ArrayList<>(input) : new ArrayList<>(); touch(); }

    /** Returns the mutable FHIR interchange output list. */
    public List<ErgonPayload> getOutput() { return output; }
    /** Replaces output payloads with defensive copies and advances last-modified time. */
    public void setOutput(List<ErgonPayload> output) { this.output = output != null ? new ArrayList<>(output) : new ArrayList<>(); touch(); }

    /** Returns execution checkpoints in recorded order. */
    public List<PragmaCheckpoint> getCheckpoints() { return checkpoints; }
    /** Replaces checkpoints with defensive copies and advances last-modified time. */
    public void setCheckpoints(List<PragmaCheckpoint> checkpoints) { this.checkpoints = checkpoints != null ? new ArrayList<>(checkpoints) : new ArrayList<>(); touch(); }

    /** Returns task metadata. */
    public Map<String, String> getMetadata() { return metadata; }
    /** Replaces task metadata with a defensive copy. */
    public void setMetadata(Map<String, String> metadata) { this.metadata = metadata != null ? new LinkedHashMap<>(metadata) : new LinkedHashMap<>(); }
    /** Adds one metadata value when the key is non-null. */
    public Pragma addMetadata(String key, String value) { if (key != null) { this.metadata.put(key, value); } return this; }

    /** Returns the originating security principal. */
    public ThemisPrincipal getOriginatingPrincipal() { return originatingPrincipal; }
    /** Sets the originating principal and advances last-modified time. */
    public void setOriginatingPrincipal(ThemisPrincipal originatingPrincipal) { this.originatingPrincipal = originatingPrincipal; touch(); }

    /** Returns the explicitly assigned executing principal. */
    public ThemisPrincipal getExecutingPrincipal() { return executingPrincipal; }
    /** Sets the executing principal and advances last-modified time. */
    public void setExecutingPrincipal(ThemisPrincipal executingPrincipal) { this.executingPrincipal = executingPrincipal; touch(); }

    /** Resolves the originating principal from the explicit field or retained security context. */
    public ThemisPrincipal getInitiatingPrincipal() {
        if (originatingPrincipal != null) return originatingPrincipal;
        if (originatingSecurityContext != null) return originatingSecurityContext.originatingPrincipal();
        return null;
    }

    /** Resolves the executing principal from the explicit field or retained security context. */
    public ThemisPrincipal getEffectiveExecutingPrincipal() {
        if (executingPrincipal != null) return executingPrincipal;
        if (originatingSecurityContext != null) return originatingSecurityContext.executingPrincipal();
        return null;
    }

    /** Resolves the effective Themis security context through the established resolver. */
    public ThemisSecurityContext resolveSecurityContext() {
        return PragmaSecurityContextResolver.resolveSecurityContext(this);
    }

    /** Returns an immutable snapshot of originating authorities. */
    public Set<ThemisAuthority> getOriginatingAuthorities() { return Collections.unmodifiableSet(originatingAuthorities); }
    /** Replaces originating authorities with a defensive copy and advances last-modified time. */
    public void setOriginatingAuthorities(Set<ThemisAuthority> originatingAuthorities) { this.originatingAuthorities = originatingAuthorities != null ? new HashSet<>(originatingAuthorities) : new HashSet<>(); touch(); }
    /** Adds one non-null originating authority and advances last-modified time. */
    public Pragma addOriginatingAuthority(ThemisAuthority authority) { if (authority != null) { this.originatingAuthorities.add(authority); touch(); } return this; }
    /** Adds an authority from its code when non-blank. */
    public Pragma addOriginatingAuthority(String authorityCode) { if (authorityCode != null && !authorityCode.isBlank()) { this.originatingAuthorities.add(ThemisAuthority.of(authorityCode)); touch(); } return this; }

    /** Returns the retained originating security context. */
    public ThemisSecurityContext getOriginatingSecurityContext() { return originatingSecurityContext; }
    /** Sets the retained originating security context and advances last-modified time. */
    public void setOriginatingSecurityContext(ThemisSecurityContext originatingSecurityContext) { this.originatingSecurityContext = originatingSecurityContext; touch(); }

    /** Returns the policy version used to authorize this task. */
    public String getPolicyVersion() { return policyVersion; }
    /** Sets the policy version and advances last-modified time. */
    public void setPolicyVersion(String policyVersion) { this.policyVersion = policyVersion; touch(); }

    /** Compares Pragma instances by their task identity. */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pragma pragma = (Pragma) o;
        return Objects.equals(pragmaId, pragma.pragmaId);
    }

    /** Hashes the task identity used by {@link #equals(Object)}. */
    @Override
    public int hashCode() {
        return Objects.hash(pragmaId);
    }

    /** Returns task identity, status, and payload/checkpoint counts without emitting payload values. */
    @Override
    public String toString() {
        return "Pragma{" +
                "pragmaId='" + pragmaId + '\'' +
                ", correlationId='" + correlationId + '\'' +
                ", praxisId='" + praxisId + '\'' +
                ", status=" + status +
                ", priority=" + priority +
                ", authoredOn=" + authoredOn +
                ", inputSize=" + input.size() +
                ", outputSize=" + output.size() +
                ", checkpointCount=" + checkpoints.size() +
                '}';
    }

}
