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

package net.fhirfactory.harmonia.paradeigma.scenarios.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerRequest;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.scenarios.config.ScenarioEngineConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

@Component
public class SimulatorRestClient {

    private static final Logger log = LoggerFactory.getLogger(SimulatorRestClient.class);

    private final ScenarioEngineConfig config;
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public SimulatorRestClient(ScenarioEngineConfig config) {
        this.config = config;
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(5))
                .build();
        this.objectMapper = new ObjectMapper().registerModule(new JavaTimeModule());
    }

    public ManualTriggerResponse pasRegisterPatient(PatientProfile patient) throws Exception {
        String url = config.getPasBaseUrl() + "/api/pas/patients/register";
        String body = patient != null ? objectMapper.writeValueAsString(patient) : "{}";
        return post(url, body);
    }

    public ManualTriggerResponse pasAdmitPatient(String patientId) throws Exception {
        String url = config.getPasBaseUrl() + "/api/pas/patients/" + patientId + "/admit";
        return post(url, "{}");
    }

    public ManualTriggerResponse pasTransferPatient(String patientId, String newWard, String newRoom, String newBed) throws Exception {
        String url = config.getPasBaseUrl() + "/api/pas/patients/" + patientId + "/transfer";
        ManualTriggerRequest req = new ManualTriggerRequest();
        req.setNewWard(newWard);
        req.setNewRoom(newRoom);
        req.setNewBed(newBed);
        return post(url, objectMapper.writeValueAsString(req));
    }

    public ManualTriggerResponse pasUpdatePatient(String patientId) throws Exception {
        String url = config.getPasBaseUrl() + "/api/pas/patients/" + patientId + "/update";
        return post(url, "{}");
    }

    public ManualTriggerResponse pasDischargePatient(String patientId) throws Exception {
        String url = config.getPasBaseUrl() + "/api/pas/patients/" + patientId + "/discharge";
        return post(url, "{}");
    }

    public ManualTriggerResponse emrPlaceLabOrder(String testCode) throws Exception {
        String url = config.getEmrBaseUrl() + "/api/emr/orders/laboratory";
        ManualTriggerRequest req = new ManualTriggerRequest();
        req.setTestCode(testCode);
        return post(url, objectMapper.writeValueAsString(req));
    }

    public ManualTriggerResponse emrPlaceImagingOrder(String studyCode) throws Exception {
        String url = config.getEmrBaseUrl() + "/api/emr/orders/imaging";
        ManualTriggerRequest req = new ManualTriggerRequest();
        req.setTestCode(studyCode);
        return post(url, objectMapper.writeValueAsString(req));
    }

    public ManualTriggerResponse lmsProduceResult(String orderNumber, String testCode) throws Exception {
        String url = config.getLmsBaseUrl() + "/api/lms/results/laboratory";
        ManualTriggerRequest req = new ManualTriggerRequest();
        req.setOrderNumber(orderNumber);
        req.setTestCode(testCode);
        return post(url, objectMapper.writeValueAsString(req));
    }

    public ManualTriggerResponse rispacProduceReport(String orderNumber, String studyCode) throws Exception {
        String url = config.getRispacBaseUrl() + "/api/rispac/results/imaging";
        ManualTriggerRequest req = new ManualTriggerRequest();
        req.setOrderNumber(orderNumber);
        req.setTestCode(studyCode);
        return post(url, objectMapper.writeValueAsString(req));
    }

    public SimulatorStatusDto getSimulatorStatus(String systemName) {
        String baseUrl = switch (systemName.toUpperCase()) {
            case "PAS" -> config.getPasBaseUrl() + "/api/pas/status";
            case "EMR" -> config.getEmrBaseUrl() + "/api/emr/status";
            case "LMS" -> config.getLmsBaseUrl() + "/api/lms/status";
            case "RISPAC", "RIS-PAC" -> config.getRispacBaseUrl() + "/api/rispac/status";
            default -> null;
        };
        if (baseUrl == null) return null;

        try {
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .timeout(Duration.ofSeconds(3))
                    .GET()
                    .build();
            HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() == 200) {
                return objectMapper.readValue(response.body(), SimulatorStatusDto.class);
            }
        } catch (Exception e) {
            log.warn("Could not fetch status for simulator {}: {}", systemName, e.getMessage());
        }
        return null;
    }

    private ManualTriggerResponse post(String url, String jsonBody) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .timeout(Duration.ofSeconds(10))
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        if (response.statusCode() >= 200 && response.statusCode() < 300) {
            return objectMapper.readValue(response.body(), ManualTriggerResponse.class);
        } else {
            return ManualTriggerResponse.failure("HTTP_" + response.statusCode(), response.body());
        }
    }
}
