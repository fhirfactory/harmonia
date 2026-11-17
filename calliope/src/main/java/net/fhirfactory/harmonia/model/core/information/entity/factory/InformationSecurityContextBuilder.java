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

package net.fhirfactory.harmonia.model.core.information.entity.factory;

import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationSecurityContext;
import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicMask;
import net.fhirfactory.harmonia.model.core.security.valuesets.FhirConfidentialityEnum;
import net.fhirfactory.harmonia.model.core.security.valuesets.HarmoniaSecurityLabelEnum;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/** Assembles IU-side security requirements using separate typed criteria collections. */
public final class InformationSecurityContextBuilder {

    private final Set<Topic> unitCriteria = new LinkedHashSet<>();
    private final Set<Topic> containerCriteria = new LinkedHashSet<>();
    private final Set<Topic> contentCriteria = new LinkedHashSet<>();
    private final Set<String> actionMasks = new LinkedHashSet<>();
    private final Set<TopicMask> actionTopicMasks = new LinkedHashSet<>();
    private final Set<TopicMask> compartmentMasks = new LinkedHashSet<>();
    private final Set<HarmoniaSecurityLabelEnum> securityLabels = new LinkedHashSet<>();
    private final Map<String, String> attributes = new LinkedHashMap<>();
    private FhirConfidentialityEnum confidentiality;

    /** Adds one governed unit criterion. */
    public InformationSecurityContextBuilder addUnitCriterion(Topic criterion) {
        addIfPresent(unitCriteria, criterion);
        return this;
    }

    /** Replaces unit criteria. */
    public InformationSecurityContextBuilder unitCriteria(Collection<Topic> criteria) {
        replace(unitCriteria, criteria);
        return this;
    }

    /** Adds one container criterion. */
    public InformationSecurityContextBuilder addContainerCriterion(Topic criterion) {
        addIfPresent(containerCriteria, criterion);
        return this;
    }

    /** Replaces container criteria. */
    public InformationSecurityContextBuilder containerCriteria(Collection<Topic> criteria) {
        replace(containerCriteria, criteria);
        return this;
    }

    /** Adds one content criterion. */
    public InformationSecurityContextBuilder addContentCriterion(Topic criterion) {
        addIfPresent(contentCriteria, criterion);
        return this;
    }

    /** Replaces content criteria. */
    public InformationSecurityContextBuilder contentCriteria(Collection<Topic> criteria) {
        replace(contentCriteria, criteria);
        return this;
    }

    /** Adds a string action name to the action allow-list. */
    public InformationSecurityContextBuilder addActionMask(String actionName) {
        String normalized = sanitize(actionName);
        if (normalized != null) {
            actionMasks.add(normalized);
        }
        return this;
    }

    /** Adds a TopicMask action criterion. */
    public InformationSecurityContextBuilder addActionMask(TopicMask actionMask) {
        addIfPresent(actionTopicMasks, actionMask);
        return this;
    }

    /** Replaces string action names. */
    public InformationSecurityContextBuilder actionMasks(Collection<String> actionNames) {
        actionMasks.clear();
        if (actionNames != null) {
            for (String actionName : actionNames) {
                addActionMask(actionName);
            }
        }
        return this;
    }

    /** Replaces TopicMask action criteria. */
    public InformationSecurityContextBuilder actionTopicMasks(Collection<TopicMask> masks) {
        replace(actionTopicMasks, masks);
        return this;
    }

    /** Adds one compartment mask. */
    public InformationSecurityContextBuilder addCompartmentMask(TopicMask mask) {
        addIfPresent(compartmentMasks, mask);
        return this;
    }

    /** Replaces compartment masks. */
    public InformationSecurityContextBuilder compartmentMasks(Collection<TopicMask> masks) {
        replace(compartmentMasks, masks);
        return this;
    }

    /** Adds one security label. */
    public InformationSecurityContextBuilder addSecurityLabel(HarmoniaSecurityLabelEnum label) {
        addIfPresent(securityLabels, label);
        return this;
    }

    /** Replaces security labels. */
    public InformationSecurityContextBuilder securityLabels(Collection<HarmoniaSecurityLabelEnum> labels) {
        replace(securityLabels, labels);
        return this;
    }

    /** Sets the FHIR confidentiality classification. */
    public InformationSecurityContextBuilder confidentiality(FhirConfidentialityEnum confidentiality) {
        this.confidentiality = confidentiality;
        return this;
    }

    /** Replaces string-valued security attributes. */
    public InformationSecurityContextBuilder attributes(Map<String, String> attributes) {
        this.attributes.clear();
        if (attributes != null) {
            this.attributes.putAll(attributes);
        }
        return this;
    }

    /** Adds or removes one attribute. */
    public InformationSecurityContextBuilder attribute(String key, String value) {
        if (key != null) {
            if (value == null) {
                attributes.remove(key);
            } else {
                attributes.put(key, value);
            }
        }
        return this;
    }

    /** Builds an immutable security context. */
    public InformationSecurityContext build() {
        return new InformationSecurityContext(unitCriteria, containerCriteria, contentCriteria, actionMasks,
                actionTopicMasks, compartmentMasks, securityLabels, confidentiality, attributes);
    }

    private static <T> void addIfPresent(Set<T> target, T value) {
        if (value != null) {
            target.add(value);
        }
    }

    private static <T> void replace(Set<T> target, Collection<T> values) {
        target.clear();
        if (values != null) {
            for (T value : values) {
                addIfPresent(target, value);
            }
        }
    }

    private static String sanitize(String value) {
        return value != null && !value.isBlank() ? value.trim() : null;
    }
}