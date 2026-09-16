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

package net.fhirfactory.harmonia.paradeigma.rispac.rest;

import net.fhirfactory.harmonia.paradeigma.common.model.OrderProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerRequest;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorConfigDto;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacResultWorker;
import net.fhirfactory.harmonia.paradeigma.rispac.service.RispacService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/rispac")
public class RispacRestController {

    private final RispacService rispacService;
    private final RispacResultWorker resultWorker;

    public RispacRestController(RispacService rispacService, RispacResultWorker resultWorker) {
        this.rispacService = rispacService;
        this.resultWorker = resultWorker;
    }

    @GetMapping("/status")
    public ResponseEntity<SimulatorStatusDto> getStatus() {
        return ResponseEntity.ok(rispacService.getStatus());
    }

    @GetMapping("/config")
    public ResponseEntity<SimulatorConfigDto> getConfig() {
        SimulatorConfigDto dto = new SimulatorConfigDto();
        dto.setFixedIntervalMs(rispacService.getConfig().getReportingDelayMs());
        dto.setProfile(rispacService.getConfig().getProfile());
        dto.setFaultInjection(rispacService.getConfig().getFaultInjection());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/config")
    public ResponseEntity<SimulatorConfigDto> updateConfig(@RequestBody SimulatorConfigDto newConfig) {
        if (newConfig != null) {
            if (newConfig.getFixedIntervalMs() > 0) {
                rispacService.getConfig().setReportingDelayMs(newConfig.getFixedIntervalMs());
            }
            if (newConfig.getProfile() != null) {
                rispacService.getConfig().setProfile(newConfig.getProfile());
            }
            if (newConfig.getFaultInjection() != null) {
                rispacService.getConfig().setFaultInjection(newConfig.getFaultInjection());
            }
        }
        return getConfig();
    }

    @PostMapping("/results/imaging")
    public ResponseEntity<ManualTriggerResponse> triggerImagingReport(@RequestBody(required = false) ManualTriggerRequest req) {
        String placerId = req != null ? req.getOrderNumber() : null;
        String studyCode = req != null ? req.getTestCode() : "XR_CHEST";
        ManualTriggerResponse response = rispacService.produceImagingReport(placerId, studyCode);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/orders")
    public ResponseEntity<List<OrderProfile>> getReceivedOrders() {
        return ResponseEntity.ok(rispacService.getReceivedOrders());
    }
}
