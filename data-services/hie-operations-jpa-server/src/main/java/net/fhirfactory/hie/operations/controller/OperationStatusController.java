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

package net.fhirfactory.hie.operations.controller;

import net.fhirfactory.hie.operations.service.OperationStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

/**
 * Controller providing readiness, liveness, and status probes for clients connecting
 * to the HIE Operations JPA Server.
 */
@RestController
public class OperationStatusController {

    private final OperationStorageService storageService;
    private final Instant startTime = Instant.now();

    public OperationStatusController(OperationStorageService storageService) {
        this.storageService = storageService;
    }

    @RequestMapping(value = {"/ready", "/api/operations/ready", "/operations/ready"}, method = {RequestMethod.GET, RequestMethod.HEAD})
    public ResponseEntity<Map<String, Object>> getReadiness() {
        Map<String, Object> response = new HashMap<>();
        response.put("ready", true);
        response.put("status", "UP");
        response.put("server", "hie-operations-jpa-server");
        response.put("database", "ready");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(response);
    }

    @GetMapping(value = {"/status", "/api/operations/status", "/operations/status"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getStatus() {
        Map<String, Object> response = new HashMap<>();
        response.put("ready", true);
        response.put("status", "UP");
        response.put("server", "hie-operations-jpa-server");
        response.put("startTime", startTime.toString());
        response.put("timestamp", Instant.now().toString());
        response.put("totalResources", storageService.listAll().size());

        return ResponseEntity.ok(response);
    }

    @GetMapping(value = {"/health", "/api/operations/health", "/operations/health"}, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Map<String, Object>> getHealth() {
        Map<String, Object> response = new HashMap<>();
        response.put("status", "UP");
        response.put("server", "hie-operations-jpa-server");
        response.put("database", "UP");
        response.put("timestamp", Instant.now().toString());

        return ResponseEntity.ok(response);
    }
}
