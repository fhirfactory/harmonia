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

package net.fhirfactory.harmonia.model.core.workflow.orchestration.factory;

import net.fhirfactory.harmonia.model.core.workflow.orchestration.PraxisDefinition;

import net.fhirfactory.harmonia.model.core.naming.TopicSubscription;

import java.util.*;

/**
 * Fluent builder for constructing {@link PraxisDefinition} instances.
 */
public final class PraxisDefinitionBuilder {

    private String praxisId;
    private String praxisName;
    private String description;
    private String version = "1.0.0";
    private boolean enabled = true;
    private final List<TopicSubscription> topicSubscriptions = new ArrayList<>();
    private final List<String> targetGatewayInstances = new ArrayList<>();
    private final List<String> targetTriggerTypes = new ArrayList<>();
    private boolean matchAllGateways = false;
    private boolean matchAllTriggers = false;
    private final Map<Integer, String> activityIds = new TreeMap<>();

    public PraxisDefinitionBuilder() {
    }

    public PraxisDefinitionBuilder praxisId(String praxisId) {
        this.praxisId = praxisId;
        return this;
    }

    public PraxisDefinitionBuilder praxisName(String praxisName) {
        this.praxisName = praxisName;
        return this;
    }

    public PraxisDefinitionBuilder description(String description) {
        this.description = description;
        return this;
    }

    public PraxisDefinitionBuilder version(String version) {
        this.version = version;
        return this;
    }

    public PraxisDefinitionBuilder enabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public PraxisDefinitionBuilder topicSubscriptions(List<TopicSubscription> topicSubscriptions) {
        this.topicSubscriptions.clear();
        if (topicSubscriptions != null) {
            this.topicSubscriptions.addAll(topicSubscriptions);
        }
        return this;
    }

    public PraxisDefinitionBuilder addTopicSubscription(TopicSubscription subscription) {
        if (subscription != null) {
            this.topicSubscriptions.add(subscription);
        }
        return this;
    }

    public PraxisDefinitionBuilder targetGatewayInstances(List<String> targetGatewayInstances) {
        this.targetGatewayInstances.clear();
        if (targetGatewayInstances != null) {
            this.targetGatewayInstances.addAll(targetGatewayInstances);
        }
        return this;
    }

    public PraxisDefinitionBuilder addTargetGatewayInstance(String gatewayInstanceId) {
        if (gatewayInstanceId != null && !gatewayInstanceId.isBlank()) {
            this.targetGatewayInstances.add(gatewayInstanceId.trim());
        }
        return this;
    }

    public PraxisDefinitionBuilder targetTriggerTypes(List<String> targetTriggerTypes) {
        this.targetTriggerTypes.clear();
        if (targetTriggerTypes != null) {
            this.targetTriggerTypes.addAll(targetTriggerTypes);
        }
        return this;
    }

    public PraxisDefinitionBuilder addTargetTriggerType(String triggerType) {
        if (triggerType != null && !triggerType.isBlank()) {
            this.targetTriggerTypes.add(triggerType.trim());
        }
        return this;
    }

    public PraxisDefinitionBuilder matchAllGateways(boolean matchAllGateways) {
        this.matchAllGateways = matchAllGateways;
        return this;
    }

    public PraxisDefinitionBuilder matchAllTriggers(boolean matchAllTriggers) {
        this.matchAllTriggers = matchAllTriggers;
        return this;
    }

    public PraxisDefinitionBuilder activityIds(Map<Integer, String> activityIds) {
        this.activityIds.clear();
        if (activityIds != null) {
            this.activityIds.putAll(activityIds);
        }
        return this;
    }

    public PraxisDefinitionBuilder addActivity(int sequenceOrder, String ergonId) {
        if (ergonId != null && !ergonId.isBlank()) {
            this.activityIds.put(sequenceOrder, ergonId.trim());
        }
        return this;
    }

    public PraxisDefinitionBuilder addActivity(String ergonId) {
        int nextOrder = this.activityIds.isEmpty() ? 0 : Collections.max(this.activityIds.keySet()) + 1;
        return addActivity(nextOrder, ergonId);
    }

    public PraxisDefinition build() {
        PraxisDefinition def = new PraxisDefinition();
        def.setPraxisId(praxisId != null ? praxisId : UUID.randomUUID().toString());
        def.setPraxisName(praxisName != null ? praxisName : def.getPraxisId());
        def.setDescription(description);
        def.setVersion(version != null ? version : "1.0.0");
        def.setEnabled(enabled);
        if (!topicSubscriptions.isEmpty()) {
            def.setTopicSubscriptions(topicSubscriptions);
        }
        if (!targetGatewayInstances.isEmpty()) {
            def.setTargetGatewayInstances(targetGatewayInstances);
        }
        if (!targetTriggerTypes.isEmpty()) {
            def.setTargetTriggerTypes(targetTriggerTypes);
        }
        def.setMatchAllGateways(matchAllGateways);
        def.setMatchAllTriggers(matchAllTriggers);
        def.setActivityIds(activityIds);
        return def;
    }
}
