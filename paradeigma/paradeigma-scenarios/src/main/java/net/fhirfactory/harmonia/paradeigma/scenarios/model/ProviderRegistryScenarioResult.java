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

package net.fhirfactory.harmonia.paradeigma.scenarios.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import net.fhirfactory.harmonia.model.pragma.PragmaStatus;
import net.fhirfactory.harmonia.themis.api.model.ThemisDecision;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Detailed execution report and result for a simulated Provider Registry scenario.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProviderRegistryScenarioResult {

    private String scenarioId;
    private String scenarioName;
    private long seed;
    private boolean success;
    private int httpStatusCode;
    private String pragmaId;
    private PragmaStatus pragmaStatus;
    private ThemisDecision securityDecision;
    private String resultingVersion;
    private String correlationId;
    private long durationMs;
    private String errorMessage;
    private String operationOutcomeCode;
    private boolean phiDiagnosticLogged;
    private boolean secretsProtected;
    private LocalDateTime executedAt;
    private final List<ScenarioExecutionStep> steps = new ArrayList<>();

    public ProviderRegistryScenarioResult() {
        this.executedAt = LocalDateTime.now();
    }

    public ProviderRegistryScenarioResult(String scenarioId, String scenarioName, long seed) {
        this.scenarioId = scenarioId;
        this.scenarioName = scenarioName;
        this.seed = seed;
        this.executedAt = LocalDateTime.now();
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

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getHttpStatusCode() {
        return httpStatusCode;
    }

    public void setHttpStatusCode(int httpStatusCode) {
        this.httpStatusCode = httpStatusCode;
    }

    public String getPragmaId() {
        return pragmaId;
    }

    public void setPragmaId(String pragmaId) {
        this.pragmaId = pragmaId;
    }

    public PragmaStatus getPragmaStatus() {
        return pragmaStatus;
    }

    public void setPragmaStatus(PragmaStatus pragmaStatus) {
        this.pragmaStatus = pragmaStatus;
    }

    public ThemisDecision getSecurityDecision() {
        return securityDecision;
    }

    public void setSecurityDecision(ThemisDecision securityDecision) {
        this.securityDecision = securityDecision;
    }

    public String getResultingVersion() {
        return resultingVersion;
    }

    public void setResultingVersion(String resultingVersion) {
        this.resultingVersion = resultingVersion;
    }

    public String getCorrelationId() {
        return correlationId;
    }

    public void setCorrelationId(String correlationId) {
        this.correlationId = correlationId;
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

    public String getOperationOutcomeCode() {
        return operationOutcomeCode;
    }

    public void setOperationOutcomeCode(String operationOutcomeCode) {
        this.operationOutcomeCode = operationOutcomeCode;
    }

    public boolean isPhiDiagnosticLogged() {
        return phiDiagnosticLogged;
    }

    public void setPhiDiagnosticLogged(boolean phiDiagnosticLogged) {
        this.phiDiagnosticLogged = phiDiagnosticLogged;
    }

    public boolean isSecretsProtected() {
        return secretsProtected;
    }

    public void setSecretsProtected(boolean secretsProtected) {
        this.secretsProtected = secretsProtected;
    }

    public LocalDateTime getExecutedAt() {
        return executedAt;
    }

    public void setExecutedAt(LocalDateTime executedAt) {
        this.executedAt = executedAt;
    }

    public List<ScenarioExecutionStep> getSteps() {
        return Collections.unmodifiableList(steps);
    }

    public void addStep(ScenarioExecutionStep step) {
        if (step != null) {
            this.steps.add(step);
        }
    }
}
