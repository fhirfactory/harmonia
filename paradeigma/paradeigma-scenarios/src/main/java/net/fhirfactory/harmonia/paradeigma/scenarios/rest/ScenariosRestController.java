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

package net.fhirfactory.harmonia.paradeigma.scenarios.rest;

import net.fhirfactory.harmonia.paradeigma.common.model.ExecutionProfile;
import net.fhirfactory.harmonia.paradeigma.scenarios.engine.ParadeigmaScenarioEngine;
import net.fhirfactory.harmonia.paradeigma.scenarios.model.PatientJourneyResult;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/scenarios")
public class ScenariosRestController {

    private final ParadeigmaScenarioEngine engine;

    public ScenariosRestController(ParadeigmaScenarioEngine engine) {
        this.engine = engine;
    }

    @PostMapping("/journey")
    public ResponseEntity<PatientJourneyResult> runSingleJourney() {
        PatientJourneyResult result = engine.runSingleJourney();
        return ResponseEntity.ok(result);
    }

    @PostMapping("/concurrent")
    public ResponseEntity<List<PatientJourneyResult>> runConcurrent(@RequestParam(name = "count", defaultValue = "5") int count,
                                                                   @RequestParam(name = "profile", defaultValue = "TEST") String profileStr) {
        ExecutionProfile profile = ExecutionProfile.fromString(profileStr);
        List<PatientJourneyResult> results = engine.runConcurrentJourneys(count, profile);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("totalJourneys", engine.getTotalJourneys());
        status.put("successfulJourneys", engine.getSuccessfulJourneys());
        status.put("failedJourneys", engine.getFailedJourneys());
        status.put("running", engine.isRunning());
        return ResponseEntity.ok(status);
    }
}
