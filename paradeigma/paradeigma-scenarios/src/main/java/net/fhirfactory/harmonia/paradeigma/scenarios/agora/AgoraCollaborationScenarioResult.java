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

package net.fhirfactory.harmonia.paradeigma.scenarios.agora;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.agora.api.model.AgoraRoomType;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.ScenarioExecutionStep;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Result model capturing execution details and verification metrics of an Agora collaboration scenario.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgoraCollaborationScenarioResult {

    private String scenarioId;
    private String scenarioName;
    private long seed;
    private String correlationId;
    private ThemisDecision securityDecision;
    private boolean success;
    private long durationMs;
    private String errorMessage;

    // Patient Collaboration Space
    private String patientId;
    private String patientSpaceId;
    private Map<AgoraRoomType, String> patientChildRoomIds = new HashMap<>();

    // Practitioner Space & Role
    private String practitionerId;
    private String practitionerRoleId;
    private String practitionerSpaceId;
    private String practitionerRoleRoomId;

    // Application Service Ingestion & Deduplication
    private String duplicateTxnId;
    private boolean duplicateAcknowledged;

    // Messaging
    private String messageTxnId;
    private boolean messageDelivered;

    // Membership Reconciliation & Drift
    private boolean driftDetected;
    private boolean driftResolved;
    private Set<String> kickedUserIds = new HashSet<>();
    private Set<String> invitedUserIds = new HashSet<>();
    private Set<String> retainedUserIds = new HashSet<>();

    // Steps
    private List<ScenarioExecutionStep> steps = new ArrayList<>();

    public AgoraCollaborationScenarioResult() {
    }

    public AgoraCollaborationScenarioResult(String scenarioId, String scenarioName, long seed) {
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
        this.seed = seed;
    }

    public void addStep(ScenarioExecutionStep step) {
        if (steps == null) {
            steps = new ArrayList<>();
        }
        steps.add(step);
    }

    public String getScenarioId() {
        return scenarioId;
    }

    public void setScenarioId(String scenarioId) {
        this.scenarioId = scenarioId;
    }

    public String getScenarioName() {
        return scenarioName;
    }

    public void setScenarioName(String scenarioName) {
        this.scenarioName = scenarioName;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(long seed) {
        this.seed = seed;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
    }

    public ThemisDecision getSecurityDecision() {
        return securityDecision;
    }

    public void setSecurityDecision(ThemisDecision securityDecision) {
        this.securityDecision = securityDecision;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(long durationMs) {
        this.durationMs = durationMs;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getPatientId() {
        return patientId;
    }

    public void setPatientId(String patientId) {
        this.patientId = patientId;
    }

    public String getPatientSpaceId() {
        return patientSpaceId;
    }

    public void setPatientSpaceId(String patientSpaceId) {
        this.patientSpaceId = patientSpaceId;
    }

    public Map<AgoraRoomType, String> getPatientChildRoomIds() {
        return patientChildRoomIds;
    }

    public void setPatientChildRoomIds(Map<AgoraRoomType, String> patientChildRoomIds) {
        this.patientChildRoomIds = patientChildRoomIds;
    }

    public String getPractitionerId() {
        return practitionerId;
    }

    public void setPractitionerId(String practitionerId) {
        this.practitionerId = practitionerId;
    }

    public String getPractitionerRoleId() {
        return practitionerRoleId;
    }

    public void setPractitionerRoleId(String practitionerRoleId) {
        this.practitionerRoleId = practitionerRoleId;
    }

    public String getPractitionerSpaceId() {
        return practitionerSpaceId;
    }

    public void setPractitionerSpaceId(String practitionerSpaceId) {
        this.practitionerSpaceId = practitionerSpaceId;
    }

    public String getPractitionerRoleRoomId() {
        return practitionerRoleRoomId;
    }

    public void setPractitionerRoleRoomId(String practitionerRoleRoomId) {
        this.practitionerRoleRoomId = practitionerRoleRoomId;
    }

    public String getDuplicateTxnId() {
        return duplicateTxnId;
    }

    public void setDuplicateTxnId(String duplicateTxnId) {
        this.duplicateTxnId = duplicateTxnId;
    }

    public boolean isDuplicateAcknowledged() {
        return duplicateAcknowledged;
    }

    public void setDuplicateAcknowledged(boolean duplicateAcknowledged) {
        this.duplicateAcknowledged = duplicateAcknowledged;
    }

    public String getMessageTxnId() {
        return messageTxnId;
    }

    public void setMessageTxnId(String messageTxnId) {
        this.messageTxnId = messageTxnId;
    }

    public boolean isMessageDelivered() {
        return messageDelivered;
    }

    public void setMessageDelivered(boolean messageDelivered) {
        this.messageDelivered = messageDelivered;
    }

    public boolean isDriftDetected() {
        return driftDetected;
    }

    public void setDriftDetected(boolean driftDetected) {
        this.driftDetected = driftDetected;
    }

    public boolean isDriftResolved() {
        return driftResolved;
    }

    public void setDriftResolved(boolean driftResolved) {
        this.driftResolved = driftResolved;
    }

    public Set<String> getKickedUserIds() {
        return kickedUserIds;
    }

    public void setKickedUserIds(Set<String> kickedUserIds) {
        this.kickedUserIds = kickedUserIds;
    }

    public Set<String> getInvitedUserIds() {
        return invitedUserIds;
    }

    public void setInvitedUserIds(Set<String> invitedUserIds) {
        this.invitedUserIds = invitedUserIds;
    }

    public Set<String> getRetainedUserIds() {
        return retainedUserIds;
    }

    public void setRetainedUserIds(Set<String> retainedUserIds) {
        this.retainedUserIds = retainedUserIds;
    }

    public List<ScenarioExecutionStep> getSteps() {
        return steps;
    }

    public void setSteps(List<ScenarioExecutionStep> steps) {
        this.steps = steps;
    }
}
