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

package net.fhirfactory.harmonia.model.core.information.operations;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.access.AuthoritativeCommitOutcome;
import net.fhirfactory.harmonia.model.core.information.operations.factory.OperationalMetadataMaskBuilder;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.naming.factory.HistoricalTopicNames;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Neutral selection construct for operational metadata criteria.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.TopicMask}):
 * <ul>
 *   <li>{@code OperationalMetadataMask} contains or references {@link TopicMask} instances for governed Topic-valued
 *       properties (such as subject identifier and operational context).</li>
 *   <li>Matching evaluates conjunction of defined operational criteria neutrally.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Null-safe matching evaluation.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class OperationalMetadataMask implements Serializable {

    private static final long serialVersionUID = 1L;

    private final UUID id;
    private final TopicMask subjectMask;
    private final TopicMask contextMask;
    private final String operationalRevision;
    private final String activeGeneration;
    private final String lifecycleStage;
    private final AuthoritativeCommitOutcome commitOutcome;

    /**
     * Canonical constructor establishing validated operational metadata mask criteria.
     *
     * @param id                  the mask UUID identity, must not be null
     * @param subjectMask         optional TopicMask for subject identifier
     * @param contextMask         optional TopicMask for operational context Topic
     * @param operationalRevision optional operational revision match string
     * @param activeGeneration    optional active generation match string
     * @param lifecycleStage      optional lifecycle/persistence stage match string
     * @param commitOutcome       optional commit outcome
     */
    @JsonCreator
    public OperationalMetadataMask(
            @JsonProperty("id") UUID id,
            @JsonProperty("subjectMask") TopicMask subjectMask,
            @JsonProperty("contextMask") TopicMask contextMask,
            @JsonProperty("operationalRevision") String operationalRevision,
            @JsonProperty("activeGeneration") String activeGeneration,
            @JsonProperty("lifecycleStage") String lifecycleStage,
            @JsonProperty("commitOutcome") AuthoritativeCommitOutcome commitOutcome) {
        this.id = id != null ? id : UUID.randomUUID();
        this.subjectMask = subjectMask;
        this.contextMask = contextMask;
        this.operationalRevision = sanitize(operationalRevision);
        this.activeGeneration = sanitize(activeGeneration);
        this.lifecycleStage = sanitize(lifecycleStage);
        this.commitOutcome = commitOutcome;
    }

    private static String sanitize(String input) {
        return input != null && !input.isBlank() ? input.trim() : null;
    }

    /**
     * Creates a new builder for constructing {@code OperationalMetadataMask} instances.
     *
     * @return a new Builder instance
     */
    public static OperationalMetadataMaskBuilder builder() {
        return new OperationalMetadataMaskBuilder();
    }

    // =========================================================================
    // Matching Logic
    // =========================================================================

    /**
     * Evaluates whether a candidate {@link OperationalMetadata} instance satisfies this mask.
     *
     * @param metadata candidate OperationalMetadata
     * @return {@code true} if all specified criteria match, {@code false} otherwise
     */
    public boolean matches(OperationalMetadata metadata) {
        if (metadata == null) {
            return false;
        }

        if (subjectMask != null) {
            String subject = metadata.getSubjectIdentifier();
            if (subject == null || !matchesSubject(subjectMask, subject)) {
                return false;
            }
        }

        if (contextMask != null) {
            if (metadata.getOperationalContext() == null || !contextMask.matches(metadata.getOperationalContext())) {
                return false;
            }
        }

        if (operationalRevision != null) {
            if (metadata.getOperationalRevision() == null
                    || !operationalRevision.equals(metadata.getOperationalRevision().value())) {
                return false;
            }
        }

        if (activeGeneration != null) {
            if (metadata.getActiveGeneration() == null
                    || !activeGeneration.equals(metadata.getActiveGeneration())) {
                return false;
            }
        }

        if (lifecycleStage != null) {
            if (metadata.getLifecycleStage() == null
                    || !lifecycleStage.equalsIgnoreCase(metadata.getLifecycleStage())) {
                return false;
            }
        }

        if (commitOutcome != null) {
            if (metadata.getCommitOutcome() != commitOutcome) {
                return false;
            }
        }

        return true;
    }

    /**
     * Converts a persisted historical subject with its old qualifier mapping
     * (Domain, Model, ModelVersion, DataElement, DataElementQualifier, Level&lt;n&gt;),
     * so masks written against historical qualifiers keep their meaning. Text that
     * cannot be converted unambiguously (for example backslash or empty segments)
     * fails closed as a non-match rather than being reinterpreted with generated
     * {@code N<position>} qualifiers.
     */
    private static boolean matchesSubject(TopicMask mask, String subject) {
        try {
            return mask.matches(HistoricalTopicNames.fromName(subject));
        } catch (IllegalArgumentException exception) {
            return false;
        }
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    public UUID getId() {
        return id;
    }

    public Optional<TopicMask> subjectMask() {
        return Optional.ofNullable(subjectMask);
    }

    public TopicMask getSubjectMask() {
        return subjectMask;
    }

    public Optional<TopicMask> contextMask() {
        return Optional.ofNullable(contextMask);
    }

    public TopicMask getContextMask() {
        return contextMask;
    }

    public Optional<String> operationalRevision() {
        return Optional.ofNullable(operationalRevision);
    }

    public String getOperationalRevision() {
        return operationalRevision;
    }

    public Optional<String> activeGeneration() {
        return Optional.ofNullable(activeGeneration);
    }

    public String getActiveGeneration() {
        return activeGeneration;
    }

    public Optional<String> lifecycleStage() {
        return Optional.ofNullable(lifecycleStage);
    }

    public String getLifecycleStage() {
        return lifecycleStage;
    }

    public Optional<AuthoritativeCommitOutcome> commitOutcome() {
        return Optional.ofNullable(commitOutcome);
    }

    public AuthoritativeCommitOutcome getCommitOutcome() {
        return commitOutcome;
    }

    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OperationalMetadataMask that = (OperationalMetadataMask) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(subjectMask, that.subjectMask) &&
                Objects.equals(contextMask, that.contextMask) &&
                Objects.equals(operationalRevision, that.operationalRevision) &&
                Objects.equals(activeGeneration, that.activeGeneration) &&
                Objects.equals(lifecycleStage, that.lifecycleStage) &&
                commitOutcome == that.commitOutcome;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, subjectMask, contextMask, operationalRevision, activeGeneration, lifecycleStage, commitOutcome);
    }

    @Override
    public String toString() {
        return "OperationalMetadataMask{" +
                "id=" + id +
                (subjectMask != null ? ", subjectMask=" + maskSummary(subjectMask) : "") +
                (contextMask != null ? ", contextMask=" + maskSummary(contextMask) : "") +
                (operationalRevision != null ? ", revision='" + operationalRevision + '\'' : "") +
                (lifecycleStage != null ? ", stage='" + lifecycleStage + '\'' : "") +
                '}';
    }

    /** Summarises a mask by kind and depth only, so diagnostics never contain expressions. */
    private static String maskSummary(TopicMask mask) {
        return mask.isUniversal() ? "universal" : "ordinary(depth=" + mask.getEntries().size() + ")";
    }

}
