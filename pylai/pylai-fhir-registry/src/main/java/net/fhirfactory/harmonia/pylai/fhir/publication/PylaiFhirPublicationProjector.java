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

package net.fhirfactory.harmonia.pylai.fhir.publication;

import org.hl7.fhir.instance.model.api.IBaseResource;
import org.hl7.fhir.r5.model.BackboneElement;
import org.hl7.fhir.r5.model.Base;
import org.hl7.fhir.r5.model.Bundle;
import org.hl7.fhir.r5.model.DomainResource;
import org.hl7.fhir.r5.model.Element;
import org.hl7.fhir.r5.model.Extension;
import org.hl7.fhir.r5.model.Meta;
import org.hl7.fhir.r5.model.Property;
import org.hl7.fhir.r5.model.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Objects;
import java.util.Set;

/**
 * Dedicated, fail-closed, non-destructive publication boundary projector.
 * <p>
 * Projects internally managed FHIR resources into clean external representations governed by
 * an explicit {@link FhirPublicationPolicy}. Ensures that Harmonia-private operational metadata
 * (e.g., authoritative persistence versions, Mneme active-state tokens, Praxis execution IDs,
 * checkpoint extensions, and operational security labels) are stripped before egress while preserving
 * standard FHIR metadata (versionId, lastUpdated, standard profiles) and clinical security tags.
 */
@Component
public class PylaiFhirPublicationProjector {

    private static final Logger log = LoggerFactory.getLogger(PylaiFhirPublicationProjector.class);

    private final FhirPublicationPolicy policy;

    public PylaiFhirPublicationProjector() {
        this(FhirPublicationPolicy.defaultPolicy());
    }

    @Autowired
    public PylaiFhirPublicationProjector(FhirPublicationPolicy policy) {
        this.policy = Objects.requireNonNull(policy, "policy must not be null");
    }

    public FhirPublicationPolicy getPolicy() {
        return policy;
    }

    /**
     * Projects any FHIR resource for external publication.
     * <p>
     * Non-destructive: clones the resource using {@code Resource.copy()} to guarantee
     * in-memory and cached source instances are never mutated.
     *
     * @param <T> resource type
     * @param source the source FHIR resource
     * @return a projected, publication-sanitized deep copy of the resource, or null if input is null
     */
    @SuppressWarnings("unchecked")
    public <T extends IBaseResource> T projectForPublication(T source) {
        if (source == null) {
            return null;
        }
        if (source instanceof Bundle bundle) {
            return (T) projectBundle(bundle);
        }
        if (source instanceof Resource resource) {
            return (T) projectResource(resource);
        }
        return source;
    }

    /**
     * Projects a FHIR {@link Bundle} by deep-copying and projecting all constituent resources and entries.
     *
     * @param sourceBundle the source Bundle
     * @return a projected deep copy of the Bundle
     */
    public Bundle projectBundle(Bundle sourceBundle) {
        if (sourceBundle == null) {
            return null;
        }
        Bundle copy = sourceBundle.copy();
        if (copy.hasMeta()) {
            sanitizeMeta(copy.getMeta());
        }
        sanitizeExtensions(copy, Collections.newSetFromMap(new IdentityHashMap<>()));

        if (copy.hasEntry()) {
            for (Bundle.BundleEntryComponent entry : copy.getEntry()) {
                if (entry != null) {
                    sanitizeExtensions(entry, Collections.newSetFromMap(new IdentityHashMap<>()));
                    if (entry.hasResource() && entry.getResource() != null) {
                        entry.setResource(projectResource(entry.getResource()));
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Projects an individual FHIR {@link Resource} by deep-copying and sanitizing metadata and extensions.
     *
     * @param <R> resource type
     * @param sourceResource the source Resource
     * @return a projected deep copy of the Resource
     */
    @SuppressWarnings("unchecked")
    public <R extends Resource> R projectResource(R sourceResource) {
        if (sourceResource == null) {
            return null;
        }
        R copy = (R) sourceResource.copy();
        if (copy.hasMeta()) {
            sanitizeMeta(copy.getMeta());
        }
        sanitizeExtensions(copy, Collections.newSetFromMap(new IdentityHashMap<>()));
        return copy;
    }

    private void sanitizeMeta(Meta meta) {
        if (meta == null) {
            return;
        }
        if (meta.hasExtension()) {
            meta.getExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
        }
        if (meta.hasSecurity()) {
            meta.getSecurity().removeIf(coding -> coding == null || !policy.isSecurityLabelPermitted(coding));
        }
        if (meta.hasProfile()) {
            meta.getProfile().removeIf(profile -> profile == null || !policy.isProfilePermitted(profile.getValue()));
        }
        if (meta.hasTag()) {
            meta.getTag().removeIf(tag -> tag == null || !policy.isTagPermitted(tag));
        }
    }

    private void sanitizeExtensions(Base base, Set<Base> visited) {
        if (base == null || !visited.add(base)) {
            return;
        }

        if (base instanceof DomainResource dr) {
            if (dr.hasExtension()) {
                dr.getExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
            }
            if (dr.hasModifierExtension()) {
                dr.getModifierExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
            }
        } else if (base instanceof BackboneElement be) {
            if (be.hasExtension()) {
                be.getExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
            }
            if (be.hasModifierExtension()) {
                be.getModifierExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
            }
        } else if (base instanceof Element elem) {
            if (elem.hasExtension()) {
                elem.getExtension().removeIf(ext -> ext == null || !policy.isExtensionPermitted(ext.getUrl()));
            }
        } else if (base instanceof Extension ext) {
            if (ext.hasExtension()) {
                ext.getExtension().removeIf(child -> child == null || !policy.isExtensionPermitted(child.getUrl()));
            }
        }

        if (base instanceof Resource res) {
            if (res.hasMeta()) {
                sanitizeMeta(res.getMeta());
            }
        }

        for (Property property : base.children()) {
            if (property != null && property.hasValues()) {
                for (Base child : property.getValues()) {
                    if (child != null) {
                        sanitizeExtensions(child, visited);
                    }
                }
            }
        }
    }
}
