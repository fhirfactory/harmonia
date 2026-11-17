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
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program. If not, see <https://www.gnu.org/licenses/>.
 */

package net.fhirfactory.harmonia.model.core.workflow.tasks.factory;

import net.fhirfactory.harmonia.model.core.workflow.tasks.ErgonPayload;
import net.fhirfactory.harmonia.model.core.workflow.tasks.Pragma;
import net.fhirfactory.harmonia.model.core.workflow.tasks.datatypes.PragmaCheckpoint;
import net.fhirfactory.harmonia.model.core.workflow.tasks.valuesets.PragmaStatus;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthority;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;

import java.time.Instant;
import java.util.*;

/**
 * Fluent builder for creating {@link Pragma} workflow task instances.
 */
public final class PragmaBuilder {

    private String pragmaId;
    private String correlationId;
    private String causationId;
    private String praxisId;
    private PragmaStatus status = PragmaStatus.DRAFT;
    private Integer priority;
    private String priorityCode;
    private Date authoredOn = new Date();
    private Date lastModified = new Date();
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

    public PragmaBuilder() {
    }

    public PragmaBuilder pragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
        return this;
    }

    public PragmaBuilder correlationId(String correlationId) {
        this.correlationId = correlationId;
        return this;
    }

    public PragmaBuilder causationId(String causationId) {
        this.causationId = causationId;
        return this;
    }

    public PragmaBuilder praxisId(String praxisId) {
        this.praxisId = praxisId;
        return this;
    }

    public PragmaBuilder status(PragmaStatus status) {
        if (status != null) {
            this.status = status;
        }
        return this;
    }

    public PragmaBuilder source(String source) {
        this.source = source;
        return this;
    }

    public PragmaBuilder destination(String destination) {
        this.destination = destination;
        return this;
    }

    public PragmaBuilder priority(Integer priority) {
        this.priority = priority;
        return this;
    }

    public PragmaBuilder priorityCode(String priorityCode) {
        this.priorityCode = priorityCode;
        return this;
    }

    public PragmaBuilder addMetadata(String key, String value) {
        if (key != null) {
            this.metadata.put(key, value);
        }
        return this;
    }

    public PragmaBuilder metadata(Map<String, String> metadata) {
        this.metadata.clear();
        if (metadata != null) {
            this.metadata.putAll(metadata);
        }
        return this;
    }

    public PragmaBuilder putMetadata(String key, String value) {
        return addMetadata(key, value);
    }

    public PragmaBuilder authoredOn(Date authoredOn) {
        if (authoredOn != null) {
            this.authoredOn = authoredOn;
        }
        return this;
    }

    public PragmaBuilder authoredOn(Instant authoredOn) {
        if (authoredOn != null) {
            this.authoredOn = Date.from(authoredOn);
        }
        return this;
    }

    public PragmaBuilder lastModified(Date lastModified) {
        if (lastModified != null) {
            this.lastModified = lastModified;
        }
        return this;
    }

    public PragmaBuilder lastModified(Instant lastModified) {
        if (lastModified != null) {
            this.lastModified = Date.from(lastModified);
        }
        return this;
    }

    public PragmaBuilder input(List<ErgonPayload> input) {
        this.input.clear();
        if (input != null) {
            this.input.addAll(input);
        }
        return this;
    }

    public PragmaBuilder addInput(ErgonPayload inputPayload) {
        if (inputPayload != null) {
            this.input.add(inputPayload);
        }
        return this;
    }

    public PragmaBuilder output(List<ErgonPayload> output) {
        this.output.clear();
        if (output != null) {
            this.output.addAll(output);
        }
        return this;
    }

    public PragmaBuilder addOutput(ErgonPayload outputPayload) {
        if (outputPayload != null) {
            this.output.add(outputPayload);
        }
        return this;
    }

    public PragmaBuilder checkpoints(List<PragmaCheckpoint> checkpoints) {
        this.checkpoints.clear();
        if (checkpoints != null) {
            this.checkpoints.addAll(checkpoints);
        }
        return this;
    }

    public PragmaBuilder addCheckpoint(PragmaCheckpoint checkpoint) {
        if (checkpoint != null) {
            this.checkpoints.add(checkpoint);
        }
        return this;
    }

    public PragmaBuilder originatingPrincipal(ThemisPrincipal originatingPrincipal) {
        this.originatingPrincipal = originatingPrincipal;
        return this;
    }

    public PragmaBuilder executingPrincipal(ThemisPrincipal executingPrincipal) {
        this.executingPrincipal = executingPrincipal;
        return this;
    }

    public PragmaBuilder originatingAuthorities(Set<ThemisAuthority> originatingAuthorities) {
        this.originatingAuthorities.clear();
        if (originatingAuthorities != null) {
            this.originatingAuthorities.addAll(originatingAuthorities);
        }
        return this;
    }

    public PragmaBuilder addOriginatingAuthority(ThemisAuthority authority) {
        if (authority != null) {
            this.originatingAuthorities.add(authority);
        }
        return this;
    }

    public PragmaBuilder addOriginatingAuthority(String authorityCode) {
        if (authorityCode != null && !authorityCode.isBlank()) {
            this.originatingAuthorities.add(ThemisAuthority.of(authorityCode));
        }
        return this;
    }

    public PragmaBuilder originatingSecurityContext(ThemisSecurityContext originatingSecurityContext) {
        this.originatingSecurityContext = originatingSecurityContext;
        return this;
    }

    public PragmaBuilder policyVersion(String policyVersion) {
        this.policyVersion = policyVersion;
        return this;
    }

    public Pragma build() {
        Pragma pragma = new Pragma();
        pragma.setPragmaId(pragmaId != null ? pragmaId : UUID.randomUUID().toString());
        pragma.setCorrelationId(correlationId);
        pragma.setCausationId(causationId);
        pragma.setPraxisId(praxisId);
        pragma.setStatus(status != null ? status : PragmaStatus.DRAFT);
        if (priority != null) pragma.setPriority(priority);
        if (priorityCode != null) pragma.setPriorityCode(priorityCode);
        if (source != null) pragma.setSource(source);
        if (destination != null) pragma.setDestination(destination);
        pragma.setAuthoredOn(authoredOn != null ? authoredOn : new Date());
        pragma.setLastModified(lastModified != null ? lastModified : new Date());
        pragma.setInput(input);
        pragma.setOutput(output);
        pragma.setCheckpoints(checkpoints);
        pragma.setMetadata(metadata);
        pragma.setOriginatingPrincipal(originatingPrincipal);
        pragma.setExecutingPrincipal(executingPrincipal);
        pragma.setOriginatingAuthorities(originatingAuthorities);
        pragma.setOriginatingSecurityContext(originatingSecurityContext);
        pragma.setPolicyVersion(policyVersion);
        return pragma;
    }
}
