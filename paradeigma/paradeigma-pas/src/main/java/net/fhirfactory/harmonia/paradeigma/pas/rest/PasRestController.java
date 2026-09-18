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

package net.fhirfactory.harmonia.paradeigma.pas.rest;

import net.fhirfactory.harmonia.paradeigma.common.model.PatientProfile;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerRequest;
import net.fhirfactory.harmonia.paradeigma.common.rest.ManualTriggerResponse;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorConfigDto;
import net.fhirfactory.harmonia.paradeigma.common.rest.SimulatorStatusDto;
import net.fhirfactory.harmonia.paradeigma.pas.scheduler.PasScheduler;
import net.fhirfactory.harmonia.paradeigma.pas.service.PasPatientLifecycleManager;
import net.fhirfactory.harmonia.paradeigma.pas.service.PasService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/pas")
public class PasRestController {

    private final PasService pasService;
    private final PasPatientLifecycleManager lifecycleManager;
    private final PasScheduler scheduler;

    public PasRestController(PasService pasService,
                             PasPatientLifecycleManager lifecycleManager,
                             PasScheduler scheduler) {
        this.pasService = pasService;
        this.lifecycleManager = lifecycleManager;
        this.scheduler = scheduler;
    }

    @GetMapping("/status")
    public ResponseEntity<SimulatorStatusDto> getStatus() {
        return ResponseEntity.ok(pasService.getStatus());
    }

    @GetMapping("/config")
    public ResponseEntity<SimulatorConfigDto> getConfig() {
        SimulatorConfigDto dto = new SimulatorConfigDto();
        dto.setTimerEnabled(pasService.getConfig().isTimerEnabled());
        dto.setTimerMode(pasService.getConfig().getTimerMode());
        dto.setFixedIntervalMs(pasService.getConfig().getFixedIntervalMs());
        dto.setMinIntervalMs(pasService.getConfig().getMinIntervalMs());
        dto.setMaxIntervalMs(pasService.getConfig().getMaxIntervalMs());
        dto.setProfile(pasService.getConfig().getProfile());
        dto.setFaultInjection(pasService.getConfig().getFaultInjection());
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
                pasService.getConfig().setProfile(newConfig.getProfile());
            }
            if (newConfig.getFaultInjection() != null) {
                pasService.getConfig().setFaultInjection(newConfig.getFaultInjection());
            }
        }
        return getConfig();
    }

    @PostMapping("/patients/register")
    public ResponseEntity<ManualTriggerResponse> registerPatient(@RequestBody(required = false) PatientProfile customPatient) {
        String adtA04 = lifecycleManager.registerPatient(customPatient);
        ManualTriggerResponse response = pasService.sendAdtMessage(adtA04);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patients/{id}/admit")
    public ResponseEntity<ManualTriggerResponse> admitPatient(@PathVariable("id") String patientId) {
        String adtA01 = lifecycleManager.admitPatient(patientId);
        ManualTriggerResponse response = pasService.sendAdtMessage(adtA01);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patients/{id}/transfer")
    public ResponseEntity<ManualTriggerResponse> transferPatient(@PathVariable("id") String patientId,
                                                                 @RequestBody(required = false) ManualTriggerRequest req) {
        String ward = req != null ? req.getNewWard() : "WARD-4B";
        String room = req != null ? req.getNewRoom() : "402";
        String bed = req != null ? req.getNewBed() : "A";

        String adtA02 = lifecycleManager.transferPatient(patientId, ward, room, bed);
        ManualTriggerResponse response = pasService.sendAdtMessage(adtA02);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patients/{id}/update")
    public ResponseEntity<ManualTriggerResponse> updatePatient(@PathVariable("id") String patientId) {
        String adtA08 = lifecycleManager.updatePatient(patientId);
        ManualTriggerResponse response = pasService.sendAdtMessage(adtA08);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/patients/{id}/discharge")
    public ResponseEntity<ManualTriggerResponse> dischargePatient(@PathVariable("id") String patientId) {
        String adtA03 = lifecycleManager.dischargePatient(patientId);
        ManualTriggerResponse response = pasService.sendAdtMessage(adtA03);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/trigger-next")
    public ResponseEntity<ManualTriggerResponse> triggerNext() {
        ManualTriggerResponse response = pasService.triggerNextLifecycleEvent();
        return ResponseEntity.ok(response);
    }
}
