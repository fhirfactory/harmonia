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

package net.fhirfactory.harmonia.operations.controller;

import net.fhirfactory.harmonia.operations.model.OperationResourceEntity;
import net.fhirfactory.harmonia.operations.service.OperationStorageService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping({"/api/operations", "/operations"})
public class OperationResourceController {

    private final OperationStorageService storageService;

    public OperationResourceController(OperationStorageService storageService) {
        this.storageService = storageService;
    }

    @GetMapping(value = "/{objectType}/{id}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE})
    public ResponseEntity<String> getResource(@PathVariable("objectType") String objectType,
                                              @PathVariable("id") String id) {
        Optional<String> jsonOpt = storageService.getResourceJson(objectType, id);
        return jsonOpt
                .map(json -> ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                        .body(json))
                .orElseGet(() -> ResponseEntity.status(HttpStatus.NOT_FOUND).build());
    }

    @PutMapping(value = "/{objectType}/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE, "*/*"})
    public ResponseEntity<String> putResource(@PathVariable("objectType") String objectType,
                                              @PathVariable("id") String id,
                                              @RequestBody(required = false) String payload) {
        OperationResourceEntity saved = storageService.saveResource(objectType, id, payload != null ? payload : "");
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .body(saved.getDataJson());
    }

    @PostMapping(value = "/{objectType}/{id}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE, "*/*"})
    public ResponseEntity<String> postResource(@PathVariable("objectType") String objectType,
                                               @PathVariable("id") String id,
                                               @RequestBody(required = false) String payload) {
        return putResource(objectType, id, payload);
    }

    @DeleteMapping("/{objectType}/{id}")
    public ResponseEntity<Void> deleteResource(@PathVariable("objectType") String objectType,
                                               @PathVariable("id") String id) {
        boolean deleted = storageService.deleteResource(objectType, id);
        if (deleted) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @RequestMapping(value = "/{objectType}/{id}", method = RequestMethod.HEAD)
    public ResponseEntity<Void> checkResource(@PathVariable("objectType") String objectType,
                                              @PathVariable("id") String id) {
        boolean exists = storageService.containsResource(objectType, id);
        return exists ? ResponseEntity.ok().build() : ResponseEntity.notFound().build();
    }

    @GetMapping(value = "/{objectType}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OperationResourceEntity>> listResourcesByType(@PathVariable("objectType") String objectType) {
        List<OperationResourceEntity> list = storageService.listByObjectType(objectType);
        return ResponseEntity.ok(list);
    }

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<List<OperationResourceEntity>> listAllResources() {
        List<OperationResourceEntity> list = storageService.listAll();
        return ResponseEntity.ok(list);
    }
}
