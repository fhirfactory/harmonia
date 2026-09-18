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

package net.fhirfactory.harmonia.paradeigma.lms.rest;

import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerRequest;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorConfigDto;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsResultWorker;
import net.fhirfactory.harmonia.paradeigma.lms.service.LmsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/lms")
public class LmsRestController {

    private final LmsService lmsService;
    private final LmsResultWorker resultWorker;

    public LmsRestController(LmsService lmsService, LmsResultWorker resultWorker) {
        this.lmsService = lmsService;
        this.resultWorker = resultWorker;
    }

    @GetMapping("/status")
    public ResponseEntity<SimulatorStatusDto> getStatus() {
        return ResponseEntity.ok(lmsService.getStatus());
    }

    @GetMapping("/config")
    public ResponseEntity<SimulatorConfigDto> getConfig() {
        SimulatorConfigDto dto = new SimulatorConfigDto();
        dto.setFixedIntervalMs(lmsService.getConfig().getResultDelayMs());
        dto.setProfile(lmsService.getConfig().getProfile());
        dto.setFaultInjection(lmsService.getConfig().getFaultInjection());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/config")
    public ResponseEntity<SimulatorConfigDto> updateConfig(@RequestBody SimulatorConfigDto newConfig) {
        if (newConfig != null) {
            if (newConfig.getFixedIntervalMs() > 0) {
                lmsService.getConfig().setResultDelayMs(newConfig.getFixedIntervalMs());
            }
            if (newConfig.getProfile() != null) {
                lmsService.getConfig().setProfile(newConfig.getProfile());
            }
            if (newConfig.getFaultInjection() != null) {
                lmsService.getConfig().setFaultInjection(newConfig.getFaultInjection());
            }
        }
        return getConfig();
    }

    @PostMapping("/results/laboratory")
    public ResponseEntity<ManualTriggerResponse> triggerLaboratoryResult(@RequestBody(required = false) ManualTriggerRequest req) {
        String placerId = req != null ? req.getOrderNumber() : null;
        String testCode = req != null ? req.getTestCode() : "CBC";
        ManualTriggerResponse response = lmsService.produceLabResult(placerId, testCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderProfile>> getReceivedOrders() {
        return ResponseEntity.ok(lmsService.getReceivedOrders());
    }
}
