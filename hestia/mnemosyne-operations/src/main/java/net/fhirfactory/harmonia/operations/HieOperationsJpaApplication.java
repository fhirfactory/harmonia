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

package net.fhirfactory.harmonia.operations;

import net.fhirfactory.harmonia.model.status.ModuleStatus;
import net.fhirfactory.harmonia.operations.service.OperationStorageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;

@SpringBootApplication
public class HieOperationsJpaApplication {

    private static final Logger log = LoggerFactory.getLogger(HieOperationsJpaApplication.class);

    private final OperationStorageService storageService;

    public HieOperationsJpaApplication(OperationStorageService storageService) {
        this.storageService = storageService;
    }

    public static void main(String[] args) {
        SpringApplication.run(HieOperationsJpaApplication.class, args);
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        try {
            if (storageService != null) {
                ModuleStatus status = ModuleStatus.ready("mnemosyne-operations", "Mnemosyne Operations JPA Server", "DATA_SERVICE");
                storageService.saveResource("modulestatus", "mnemosyne-operations", status.toJson());
                log.info("Registered mnemosyne-operations status in operational database");
            }
        } catch (Exception e) {
            log.warn("Could not register mnemosyne-operations status on startup: {}", e.getMessage());
        }
    }
}
