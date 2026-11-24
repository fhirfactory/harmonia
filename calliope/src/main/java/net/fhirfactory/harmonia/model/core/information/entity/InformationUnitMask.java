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

package net.fhirfactory.harmonia.model.core.information.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationCoordinates;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.factory.InformationUnitMaskBuilder;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;

import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

/**
 * Neutral selection construct evaluating candidate {@link InformationUnit} instances across multiple
 * Topic-valued facets and governed characteristics.
 * <p>
 * <b>Architectural Context &amp; Authority:</b>
 * In accordance with the accepted 1.1.25 Architecture Continuum ({@code model.core.information.TopicMask})
 * and {@code DRP-Information-Unit-Framework.Final.002.md} (§6):
 * <ul>
 *   <li><b>Compositional Multi-Facet Selection:</b> A single InformationUnit contains multiple Topics describing
 *       different facets of the same information (identity, container type, content structure, origin, coordinates).
 *       {@code InformationUnitMask} is therefore conceptually distinct from {@link TopicMask}.</li>
 *   <li>{@link TopicMask} matches a single candidate Topic against a regex pattern.</li>
 *   <li>{@code InformationUnitMask} coordinates {@link TopicMask} evaluations against an IU's specific
 *       Topic-valued properties.</li>
 *   <li><b>Neutrality:</b> Neither mask is an authorisation rule or a subscription state machine; both are neutral
 *       criteria and filtering structures.</li>
 * </ul>
 * <p>
 * <b>Defensive Programming:</b>
 * <ul>
 *   <li>Immutable value object design.</li>
 *   <li>Null-safe evaluation: unspecified criteria match unconditionally.</li>
 * </ul>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public final class InformationUnitMask implements Serializable {

    private static final long serialVersionUID = 1L;

    private static final InformationUnitMask UNRESTRICTED = new InformationUnitMask(
            null, null, null, null, null, null, null, null, null, null);

    private final UUID id;
    private final TopicMask identityMask;
    private final TopicMask containerTypeMask;
    private final TopicMask contentStructureMask;
    private final TopicMask originMask;
    private final TopicMask persistenceGatewayMask;
    private final TopicMask gatewayEndpointMask;
    private final TopicMask informationLocationMask;
    private final TopicMask informationOwnerMask;
    private final String version;

    /**
     * Canonical constructor establishing validated InformationUnit criteria.
     */
    @JsonCreator
    public InformationUnitMask(
            @JsonProperty("id") UUID id,
            @JsonProperty("identityMask") TopicMask identityMask,
            @JsonProperty("containerTypeMask") TopicMask containerTypeMask,
            @JsonProperty("contentStructureMask") TopicMask contentStructureMask,
            @JsonProperty("originMask") TopicMask originMask,
            @JsonProperty("persistenceGatewayMask") TopicMask persistenceGatewayMask,
            @JsonProperty("gatewayEndpointMask") TopicMask gatewayEndpointMask,
            @JsonProperty("informationLocationMask") TopicMask informationLocationMask,
            @JsonProperty("informationOwnerMask") TopicMask informationOwnerMask,
            @JsonProperty("version") String version) {
        this.id = id != null ? id : UUID.randomUUID();
        this.identityMask = identityMask;
        this.containerTypeMask = containerTypeMask;
        this.contentStructureMask = contentStructureMask;
        this.originMask = originMask;
        this.persistenceGatewayMask = persistenceGatewayMask;
        this.gatewayEndpointMask = gatewayEndpointMask;
        this.informationLocationMask = informationLocationMask;
        this.informationOwnerMask = informationOwnerMask;
        this.version = version != null && !version.isBlank() ? version.trim() : null;
    }

    /**
     * Returns an unrestricted mask that matches any candidate Information Unit.
     *
     * @return unrestricted InformationUnitMask
     */
    public static InformationUnitMask unrestricted() {
        return UNRESTRICTED;
    }

    public static InformationUnitMaskBuilder builder() {
        return new InformationUnitMaskBuilder();
    }

    // =========================================================================
    // Matching Evaluation
    // =========================================================================

    /**
     * Evaluates whether a candidate {@link InformationUnit} satisfies all defined criteria in this mask.
     *
     * @param iu candidate Information Unit
     * @return true if all defined criteria match, false otherwise
     */
    public boolean matches(InformationUnit iu) {
        if (iu == null) {
            return false;
        }

        if (identityMask != null) {
            Topic idTopic = iu.identifier().toTopic();
            if (idTopic == null || !identityMask.matches(idTopic)) {
                return false;
            }
        }

        if (containerTypeMask != null) {
            Topic cType = iu.representation().containerMetadata().type();
            if (cType == null || !containerTypeMask.matches(cType)) {
                return false;
            }
        }

        if (contentStructureMask != null) {
            Topic sType = iu.representation().contentMetadata().contentStructureTopic().orElse(null);
            if (sType == null || !contentStructureMask.matches(sType)) {
                return false;
            }
        }

        if (originMask != null) {
            Optional<Topic> origin = iu.metadata().origin();
            if (origin.isEmpty() || !originMask.matches(origin.get())) {
                return false;
            }
        }

        InformationCoordinates coordinates = iu.metadata().coordinates();
        if (persistenceGatewayMask != null) {
            Optional<Topic> gateway = coordinates.persistenceGateway();
            if (gateway.isEmpty() || !persistenceGatewayMask.matches(gateway.get())) {
                return false;
            }
        }

        if (gatewayEndpointMask != null) {
            Optional<Topic> endpoint = coordinates.gatewayEndpoint();
            if (endpoint.isEmpty() || !gatewayEndpointMask.matches(endpoint.get())) {
                return false;
            }
        }

        if (informationLocationMask != null) {
            Optional<Topic> location = coordinates.informationLocation();
            if (location.isEmpty() || !informationLocationMask.matches(location.get())) {
                return false;
            }
        }

        if (informationOwnerMask != null) {
            Optional<Topic> owner = coordinates.informationOwner();
            if (owner.isEmpty() || !informationOwnerMask.matches(owner.get())) {
                return false;
            }
        }

        if (version != null) {
            if (iu.version() == null || !version.equals(iu.version())) {
                return false;
            }
        }

        return true;
    }

    // =========================================================================
    // Accessors
    // =========================================================================

    @JsonProperty("id")
    public UUID id() {
        return id;
    }

    public UUID getId() {
        return id;
    }

    @JsonIgnore
    public String getShortId() {
        return id.toString().substring(0, 8);
    }

    @JsonProperty("identityMask")
    public Optional<TopicMask> identityMask() {
        return Optional.ofNullable(identityMask);
    }

    public TopicMask getIdentityMask() {
        return identityMask;
    }

    @JsonProperty("containerTypeMask")
    public Optional<TopicMask> containerTypeMask() {
        return Optional.ofNullable(containerTypeMask);
    }

    public TopicMask getContainerTypeMask() {
        return containerTypeMask;
    }

    @JsonProperty("contentStructureMask")
    public Optional<TopicMask> contentStructureMask() {
        return Optional.ofNullable(contentStructureMask);
    }

    public TopicMask getContentStructureMask() {
        return contentStructureMask;
    }

    @JsonProperty("originMask")
    public Optional<TopicMask> originMask() {
        return Optional.ofNullable(originMask);
    }

    public TopicMask getOriginMask() {
        return originMask;
    }

    @JsonProperty("persistenceGatewayMask")
    public Optional<TopicMask> persistenceGatewayMask() {
        return Optional.ofNullable(persistenceGatewayMask);
    }

    public TopicMask getPersistenceGatewayMask() {
        return persistenceGatewayMask;
    }

    @JsonProperty("gatewayEndpointMask")
    public Optional<TopicMask> gatewayEndpointMask() {
        return Optional.ofNullable(gatewayEndpointMask);
    }

    public TopicMask getGatewayEndpointMask() {
        return gatewayEndpointMask;
    }

    @JsonProperty("informationLocationMask")
    public Optional<TopicMask> informationLocationMask() {
        return Optional.ofNullable(informationLocationMask);
    }

    public TopicMask getInformationLocationMask() {
        return informationLocationMask;
    }

    @JsonProperty("informationOwnerMask")
    public Optional<TopicMask> informationOwnerMask() {
        return Optional.ofNullable(informationOwnerMask);
    }

    public TopicMask getInformationOwnerMask() {
        return informationOwnerMask;
    }

    @JsonProperty("version")
    public Optional<String> version() {
        return Optional.ofNullable(version);
    }

    public String getVersion() {
        return version;
    }

    // =========================================================================
    // Standard Object Methods
    // =========================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        InformationUnitMask that = (InformationUnitMask) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(identityMask, that.identityMask) &&
                Objects.equals(containerTypeMask, that.containerTypeMask) &&
                Objects.equals(contentStructureMask, that.contentStructureMask) &&
                Objects.equals(originMask, that.originMask) &&
                Objects.equals(persistenceGatewayMask, that.persistenceGatewayMask) &&
                Objects.equals(gatewayEndpointMask, that.gatewayEndpointMask) &&
                Objects.equals(informationLocationMask, that.informationLocationMask) &&
                Objects.equals(informationOwnerMask, that.informationOwnerMask) &&
                Objects.equals(version, that.version);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, identityMask, containerTypeMask, contentStructureMask, originMask,
                persistenceGatewayMask, gatewayEndpointMask, informationLocationMask, informationOwnerMask, version);
    }

    @Override
    public String toString() {
        return "InformationUnitMask{" +
                "id=" + getShortId() +
                (identityMask != null ? ", identityMask=" + maskSummary(identityMask) : "") +
                (containerTypeMask != null ? ", containerMask=" + maskSummary(containerTypeMask) : "") +
                (contentStructureMask != null ? ", contentStructureMask=" + maskSummary(contentStructureMask) : "") +
                (originMask != null ? ", originMask=" + maskSummary(originMask) : "") +
                (version != null ? ", version='" + version + '\'' : "") +
                '}';
    }

    private static String maskSummary(TopicMask mask) {
        return mask.isUniversal() ? "universal" : "ordinary(depth=" + mask.getEntries().size() + ")";
    }

}
