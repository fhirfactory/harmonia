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

package net.fhirfactory.harmonia.paradeigma.emr.rest;

import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerRequest;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorConfigDto;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.emr.scheduler.EmrScheduler;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrPatientManager;
import net.fhirfactory.harmonia.paradeigma.emr.service.EmrService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/emr")
public class EmrRestController {

    private final EmrService emrService;
    private final EmrPatientManager patientManager;
    private final EmrScheduler scheduler;

    public EmrRestController(EmrService emrService, EmrPatientManager patientManager, EmrScheduler scheduler) {
        this.emrService = emrService;
        this.patientManager = patientManager;
        this.scheduler = scheduler;
    }

    @GetMapping("/status")
    public ResponseEntity<SimulatorStatusDto> getStatus() {
        return ResponseEntity.ok(emrService.getStatus());
    }

    @GetMapping("/config")
    public ResponseEntity<SimulatorConfigDto> getConfig() {
        SimulatorConfigDto dto = new SimulatorConfigDto();
        dto.setTimerEnabled(emrService.getConfig().isTimerEnabled());
        dto.setTimerMode(emrService.getConfig().getTimerMode());
        dto.setFixedIntervalMs(emrService.getConfig().getFixedIntervalMs());
        dto.setMinIntervalMs(emrService.getConfig().getMinIntervalMs());
        dto.setMaxIntervalMs(emrService.getConfig().getMaxIntervalMs());
        dto.setProfile(emrService.getConfig().getProfile());
        dto.setFaultInjection(emrService.getConfig().getFaultInjection());
        return ResponseEntity.ok(dto);
    }

    @PostMapping("/config")
    public ResponseEntity<SimulatorConfigDto> updateConfig(@RequestBody SimulatorConfigDto newConfig) {
        if (newConfig != null) {
            scheduler.updateConfig(
                    newConfig.isTimerEnabled(),
                    newConfig.getTimerMode(),
                    newConfig.getFixedIntervalMs(),
                    newConfig.getMinIntervalMs(),
                    newConfig.getMaxIntervalMs()
            );
            if (newConfig.getProfile() != null) {
                emrService.getConfig().setProfile(newConfig.getProfile());
            }
            if (newConfig.getFaultInjection() != null) {
                emrService.getConfig().setFaultInjection(newConfig.getFaultInjection());
            }
        }
        return getConfig();
    }

    @PostMapping("/orders/laboratory")
    public ResponseEntity<ManualTriggerResponse> placeLaboratoryOrder(@RequestBody(required = false) ManualTriggerRequest req) {
        String test = (req != null && req.getTestCode() != null) ? req.getTestCode() : "CBC";
        ManualTriggerResponse response = emrService.placeLabOrder(test);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/orders/imaging")
    public ResponseEntity<ManualTriggerResponse> placeImagingOrder(@RequestBody(required = false) ManualTriggerRequest req) {
        String study = (req != null && req.getTestCode() != null) ? req.getTestCode() : "XR_CHEST";
        ManualTriggerResponse response = emrService.placeImagingOrder(study);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/patients")
    public ResponseEntity<List<PatientProfile>> getPatients() {
        return ResponseEntity.ok(patientManager.getAllPatients());
    }
}
