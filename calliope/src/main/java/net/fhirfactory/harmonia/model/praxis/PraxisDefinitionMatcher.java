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

package net.fhirfactory.harmonia.model.praxis;

import net.fhirfactory.harmonia.model.core.naming.Topic;
import net.fhirfactory.harmonia.model.core.naming.TopicSubscription;

import java.util.List;

/**
 * Encapsulates topic subscription and gateway/trigger matching evaluation for {@link PraxisDefinition}.
 */
public final class PraxisDefinitionMatcher {

    private PraxisDefinitionMatcher() {
        // Utility class
    }

    public static boolean matches(PraxisDefinition def, Topic topic) {
        if (def == null || topic == null) {
            return false;
        }
        List<TopicSubscription> subscriptions = def.getTopicSubscriptions();
        if (subscriptions != null && !subscriptions.isEmpty()) {
            for (TopicSubscription sub : subscriptions) {
                if (sub != null && sub.matches(topic)) {
                    return true;
                }
            }
            return false;
        }
        return matchesGatewayAndTrigger(def, null, topic.getCompositeTrigger());
    }

    public static boolean matches(PraxisDefinition def, String gatewayInstanceId, String triggerType) {
        if (def == null) {
            return false;
        }
        return matchesGatewayAndTrigger(def, gatewayInstanceId, triggerType);
    }

    public static boolean matchesGatewayAndTrigger(PraxisDefinition def, String gatewayInstanceId, String triggerType) {
        boolean gatewayMatches = def.isMatchAllGateways();
        List<String> targetGatewayInstances = def.getTargetGatewayInstances();
        if (!gatewayMatches) {
            if (gatewayInstanceId == null || "*".equals(gatewayInstanceId)
                    || targetGatewayInstances == null || targetGatewayInstances.isEmpty() || targetGatewayInstances.contains("*")) {
                gatewayMatches = true;
            } else {
                for (String targetGw : targetGatewayInstances) {
                    if (targetGw != null && (targetGw.equalsIgnoreCase(gatewayInstanceId) || "*".equals(targetGw))) {
                        gatewayMatches = true;
                        break;
                    }
                }
            }
        }

        boolean triggerMatches = def.isMatchAllTriggers();
        List<String> targetTriggerTypes = def.getTargetTriggerTypes();
        if (!triggerMatches) {
            if (triggerType == null || "*".equals(triggerType)
                    || targetTriggerTypes == null || targetTriggerTypes.isEmpty() || targetTriggerTypes.contains("*")) {
                triggerMatches = true;
            } else {
                String normalizedTrigger = triggerType.contains("^")
                        ? triggerType.substring(triggerType.indexOf('^') + 1)
                        : triggerType;
                for (String targetTrig : targetTriggerTypes) {
                    if (targetTrig != null) {
                        String normalizedTarget = targetTrig.contains("^")
                                ? targetTrig.substring(targetTrig.indexOf('^') + 1)
                                : targetTrig;
                        if (targetTrig.equalsIgnoreCase(triggerType)
                                || normalizedTarget.equalsIgnoreCase(normalizedTrigger)
                                || "*".equals(targetTrig)) {
                            triggerMatches = true;
                            break;
                        }
                    }
                }
            }
        }

        return gatewayMatches && triggerMatches;
    }
}
