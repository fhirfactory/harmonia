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

package net.fhirfactory.harmonia.model.pragma;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.ergon.ErgonPayload;
import net.fhirfactory.harmonia.model.topic.Topic;
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
        if (input != null) {
            for (ErgonPayload payload : input) {
                this.input.add(new ErgonPayload(payload));
            }
        }
        if (output != null) {
            for (ErgonPayload payload : output) {
                this.output.add(new ErgonPayload(payload));
            }
        }
        if (checkpoints != null) {
            for (PragmaCheckpoint cp : checkpoints) {
                this.checkpoints.add(new PragmaCheckpoint(cp));
            }
        }
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
    }

    /**
     * Copy constructor.
     *
     * @param other Pragma instance to copy
     */
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
            if (other.input != null) {
                for (ErgonPayload payload : other.input) {
                    this.input.add(new ErgonPayload(payload));
                }
            }
            if (other.output != null) {
                for (ErgonPayload payload : other.output) {
                    this.output.add(new ErgonPayload(payload));
                }
            }
            if (other.checkpoints != null) {
                for (PragmaCheckpoint cp : other.checkpoints) {
                    this.checkpoints.add(new PragmaCheckpoint(cp));
                }
            }
            if (other.metadata != null) {
                this.metadata.putAll(other.metadata);
            }
            this.originatingPrincipal = other.originatingPrincipal;
            this.executingPrincipal = other.executingPrincipal;
            if (other.originatingAuthorities != null) {
                this.originatingAuthorities.addAll(other.originatingAuthorities);
            }
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
    // Static Builder & Factory Methods
    // =========================================================================

    public static Builder builder() {
        return new Builder();
    }

    public static Pragma newDraft() {
        return new Pragma();
    }

    public static Pragma newRequested(String praxisId) {
        Pragma pragma = new Pragma();
        pragma.setPraxisId(praxisId);
        pragma.setStatus(PragmaStatus.REQUESTED);
        return pragma;
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
     * Finds the first input payload matching the given container and content topics.
     *
     * @param container container topic (optional)
     * @param content   content topic (optional)
     * @return optional matching ErgonPayload
     */
    public Optional<ErgonPayload> findInput(Topic container, Topic content) {
        return input.stream()
                .filter(p -> (container == null || Objects.equals(p.getPayloadContainer(), container)))
                .filter(p -> (content == null || Objects.equals(p.getPayloadContent(), content)))
                .findFirst();
    }

    /**
     * Finds the first output payload matching the given container and content topics.
     *
     * @param container container topic (optional)
     * @param content   content topic (optional)
     * @return optional matching ErgonPayload
     */
    public Optional<ErgonPayload> findOutput(Topic container, Topic content) {
        return output.stream()
                .filter(p -> (container == null || Objects.equals(p.getPayloadContainer(), container)))
                .filter(p -> (content == null || Objects.equals(p.getPayloadContent(), content)))
                .findFirst();
    }

    /**
     * Finds an input payload by sequential order index.
     *
     * @param order 0-based order
     * @return optional ErgonPayload
     */
    public Optional<ErgonPayload> findInputByOrder(int order) {
        return input.stream()
                .filter(p -> p.getPayloadOrder() != null && p.getPayloadOrder() == order)
                .findFirst();
    }

    /**
     * Finds an output payload by sequential order index.
     *
     * @param order 0-based order
     * @return optional ErgonPayload
     */
    public Optional<ErgonPayload> findOutputByOrder(int order) {
        return output.stream()
                .filter(p -> p.getPayloadOrder() != null && p.getPayloadOrder() == order)
                .findFirst();
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

    public String getPragmaId() {
        return pragmaId;
    }

    public void setPragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public String getCausationId() {
        return causationId;
    }

    public void setCausationId(String causationId) {
        this.causationId = causationId;
    }

    public String getPraxisId() {
        return praxisId;
    }

    public void setPraxisId(String praxisId) {
        this.praxisId = praxisId;
    }

    public PragmaStatus getStatus() {
        return status;
    }

    public void setStatus(PragmaStatus status) {
        this.status = status != null ? status : PragmaStatus.DRAFT;
        touch();
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public String getPriorityCode() {
        return priorityCode;
    }

    public void setPriorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
    }

    public Date getAuthoredOn() {
        return authoredOn != null ? new Date(authoredOn.getTime()) : null;
    }

    public void setAuthoredOn(Date authoredOn) {
        this.authoredOn = authoredOn != null ? new Date(authoredOn.getTime()) : null;
    }

    public Date getLastModified() {
        return lastModified != null ? new Date(lastModified.getTime()) : null;
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = lastModified != null ? new Date(lastModified.getTime()) : null;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public List<ErgonPayload> getInput() {
        return input;
    }

    public void setInput(List<ErgonPayload> input) {
        this.input = input != null ? new ArrayList<>(input) : new ArrayList<>();
        touch();
    }

    public List<ErgonPayload> getOutput() {
        return output;
    }

    public void setOutput(List<ErgonPayload> output) {
        this.output = output != null ? new ArrayList<>(output) : new ArrayList<>();
        touch();
    }

    public List<PragmaCheckpoint> getCheckpoints() {
        return checkpoints;
    }

    public void setCheckpoints(List<PragmaCheckpoint> checkpoints) {
        this.checkpoints = checkpoints != null ? new ArrayList<>(checkpoints) : new ArrayList<>();
        touch();
    }

    public Map<String, String> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, String> metadata) {
        this.metadata = metadata != null ? new LinkedHashMap<>(metadata) : new LinkedHashMap<>();
    }

    public Pragma addMetadata(String key, String value) {
        if (key != null) {
            this.metadata.put(key, value);
        }
        return this;
    }

    public ThemisPrincipal getOriginatingPrincipal() {
        return originatingPrincipal;
    }

    public void setOriginatingPrincipal(ThemisPrincipal originatingPrincipal) {
        this.originatingPrincipal = originatingPrincipal;
        touch();
    }

    public ThemisPrincipal getExecutingPrincipal() {
        return executingPrincipal;
    }

    public void setExecutingPrincipal(ThemisPrincipal executingPrincipal) {
        this.executingPrincipal = executingPrincipal;
        touch();
    }

    /**
     * Resolves the initiating (human/requesting) principal, falling back to the originating security context.
     *
     * @return initiating principal, or null
     */
    public ThemisPrincipal getInitiatingPrincipal() {
        if (originatingPrincipal != null) {
            return originatingPrincipal;
        }
        if (originatingSecurityContext != null) {
            return originatingSecurityContext.originatingPrincipal();
        }
        return null;
    }

    /**
     * Resolves the effective executing (process/worker) principal, falling back to the originating security context.
     *
     * @return executing principal, or null
     */
    public ThemisPrincipal getEffectiveExecutingPrincipal() {
        if (executingPrincipal != null) {
            return executingPrincipal;
        }
        if (originatingSecurityContext != null) {
            return originatingSecurityContext.executingPrincipal();
        }
        return null;
    }

    /**
     * Resolves canonical {@link ThemisSecurityContext} from this Pragma's security and correlation fields.
     *
     * @return resolved ThemisSecurityContext, or null
     */
    public ThemisSecurityContext resolveSecurityContext() {
        if (originatingSecurityContext != null) {
            ThemisSecurityContext.Builder b = originatingSecurityContext.toBuilder();
            if (originatingPrincipal != null && originatingSecurityContext.requestingPrincipal() == null) {
                b.originatingPrincipal(originatingPrincipal);
            }
            if (executingPrincipal != null && originatingSecurityContext.executingPrincipal() == null) {
                b.executingPrincipal(executingPrincipal);
            }
            if (!originatingAuthorities.isEmpty() && originatingSecurityContext.authorities().isEmpty()) {
                b.authorities(originatingAuthorities);
            }
            if (correlationId != null && originatingSecurityContext.correlationId() == null) {
                b.correlationId(correlationId);
            }
            if (causationId != null && originatingSecurityContext.causationId() == null) {
                b.causationId(causationId);
            }
            return b.build();
        }
        if (originatingPrincipal != null || executingPrincipal != null || !originatingAuthorities.isEmpty()) {
            return ThemisSecurityContext.builder()
                    .originatingPrincipal(originatingPrincipal)
                    .executingPrincipal(executingPrincipal)
                    .authorities(originatingAuthorities)
                    .correlationId(correlationId)
                    .causationId(causationId)
                    .build();
        }
        return null;
    }

    public Set<ThemisAuthority> getOriginatingAuthorities() {
        return Collections.unmodifiableSet(originatingAuthorities);
    }

    public void setOriginatingAuthorities(Set<ThemisAuthority> originatingAuthorities) {
        this.originatingAuthorities = originatingAuthorities != null ? new HashSet<>(originatingAuthorities) : new HashSet<>();
        touch();
    }

    public Pragma addOriginatingAuthority(ThemisAuthority authority) {
        if (authority != null) {
            this.originatingAuthorities.add(authority);
            touch();
        }
        return this;
    }

    public Pragma addOriginatingAuthority(String authorityCode) {
        if (authorityCode != null && !authorityCode.isBlank()) {
            this.originatingAuthorities.add(ThemisAuthority.of(authorityCode));
            touch();
        }
        return this;
    }

    public ThemisSecurityContext getOriginatingSecurityContext() {
        return originatingSecurityContext;
    }

    public void setOriginatingSecurityContext(ThemisSecurityContext originatingSecurityContext) {
        this.originatingSecurityContext = originatingSecurityContext;
        touch();
    }

    public String getPolicyVersion() {
        return policyVersion;
    }

    public void setPolicyVersion(String policyVersion) {
        this.policyVersion = policyVersion;
        touch();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Pragma pragma = (Pragma) o;
        return Objects.equals(pragmaId, pragma.pragmaId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(pragmaId);
    }

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

    // =========================================================================
    // Builder
    // =========================================================================

    public static class Builder {
        private String pragmaId;
        private String correlationId;
        private String causationId;
        private String praxisId;
        private PragmaStatus status = PragmaStatus.DRAFT;
        private Integer priority;
        private String priorityCode;
        private Date authoredOn;
        private Date lastModified;
        private String source;
        private String destination;
        private final List<ErgonPayload> input = new ArrayList<>();
        private final List<ErgonPayload> output = new ArrayList<>();
        private final List<PragmaCheckpoint> checkpoints = new ArrayList<>();
        private final Map<String, String> metadata = new LinkedHashMap<>();
        private ThemisPrincipal originatingPrincipal;
        private ThemisPrincipal executingPrincipal;
        private final Set<ThemisAuthority> originatingAuthorities = new HashSet<>();
        private ThemisSecurityContext originatingSecurityContext;
        private String policyVersion = "1.0.0";

        public Builder pragmaId(String pragmaId) {
            this.pragmaId = pragmaId;
            return this;
        }

        public Builder correlationId(String correlationId) {
            this.correlationId = correlationId;
            return this;
        }

        public Builder causationId(String causationId) {
            this.causationId = causationId;
            return this;
        }

        public Builder praxisId(String praxisId) {
            this.praxisId = praxisId;
            return this;
        }

        public Builder status(PragmaStatus status) {
            this.status = status;
            return this;
        }

        public Builder priority(Integer priority) {
            this.priority = priority;
            return this;
        }

        public Builder priorityCode(String priorityCode) {
            this.priorityCode = priorityCode;
            return this;
        }

        public Builder authoredOn(Date authoredOn) {
            this.authoredOn = authoredOn;
            return this;
        }

        public Builder lastModified(Date lastModified) {
            this.lastModified = lastModified;
            return this;
        }

        public Builder source(String source) {
            this.source = source;
            return this;
        }

        public Builder destination(String destination) {
            this.destination = destination;
            return this;
        }

        public Builder addInput(ErgonPayload payload) {
            if (payload != null) {
                this.input.add(payload);
            }
            return this;
        }

        public Builder addOutput(ErgonPayload payload) {
            if (payload != null) {
                this.output.add(payload);
            }
            return this;
        }

        public Builder addCheckpoint(PragmaCheckpoint checkpoint) {
            if (checkpoint != null) {
                this.checkpoints.add(checkpoint);
            }
            return this;
        }

        public Builder metadata(Map<String, String> metadata) {
            if (metadata != null) {
                this.metadata.putAll(metadata);
            }
            return this;
        }

        public Builder addMetadata(String key, String value) {
            if (key != null) {
                this.metadata.put(key, value);
            }
            return this;
        }

        public Builder originatingPrincipal(ThemisPrincipal originatingPrincipal) {
            this.originatingPrincipal = originatingPrincipal;
            return this;
        }

        public Builder executingPrincipal(ThemisPrincipal executingPrincipal) {
            this.executingPrincipal = executingPrincipal;
            return this;
        }

        public Builder originatingAuthorities(Set<ThemisAuthority> originatingAuthorities) {
            if (originatingAuthorities != null) {
                this.originatingAuthorities.addAll(originatingAuthorities);
            }
            return this;
        }

        public Builder addOriginatingAuthority(ThemisAuthority authority) {
            if (authority != null) {
                this.originatingAuthorities.add(authority);
            }
            return this;
        }

        public Builder addOriginatingAuthority(String authorityCode) {
            if (authorityCode != null && !authorityCode.isBlank()) {
                this.originatingAuthorities.add(ThemisAuthority.of(authorityCode));
            }
            return this;
        }

        public Builder originatingSecurityContext(ThemisSecurityContext originatingSecurityContext) {
            this.originatingSecurityContext = originatingSecurityContext;
            return this;
        }

        public Builder policyVersion(String policyVersion) {
            this.policyVersion = policyVersion;
            return this;
        }

        public Pragma build() {
            Pragma pragma = new Pragma(pragmaId, correlationId, causationId, praxisId, status,
                    priority, priorityCode, authoredOn, lastModified, source, destination,
                    input, output, checkpoints, metadata);
            pragma.setOriginatingPrincipal(originatingPrincipal);
            pragma.setExecutingPrincipal(executingPrincipal);
            pragma.setOriginatingAuthorities(originatingAuthorities);
            pragma.setOriginatingSecurityContext(originatingSecurityContext);
            if (policyVersion != null) {
                pragma.setPolicyVersion(policyVersion);
            }
            return pragma;
        }
    }
}
