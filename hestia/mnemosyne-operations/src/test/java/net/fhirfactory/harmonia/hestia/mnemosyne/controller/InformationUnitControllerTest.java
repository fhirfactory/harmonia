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

package net.fhirfactory.harmonia.hestia.mnemosyne.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.GovernedPersistenceFramework;
import net.fhirfactory.harmonia.hestia.mnemosyne.framework.PersistenceDurabilityProfile;
import net.fhirfactory.harmonia.hestia.mnemosyne.governance.MnemosyneAuthorizationGate;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitConstructor;
import net.fhirfactory.harmonia.hestia.mnemosyne.information.InformationUnitDisassembler;
import net.fhirfactory.harmonia.hestia.mnemosyne.nonfhir.NonFhirPersistenceService;
import net.fhirfactory.harmonia.model.core.information.access.ResourceKey;
import net.fhirfactory.harmonia.model.core.information.entity.InformationUnit;
import net.fhirfactory.harmonia.model.core.information.entity.InformationMetadata;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.Content;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationRepresentation;
import net.fhirfactory.harmonia.model.core.information.entity.datatypes.InformationUnitIdentifier;
import net.fhirfactory.harmonia.themis.api.model.ThemisAuthorizationDecision;
import net.fhirfactory.harmonia.themis.api.model.ThemisPrincipal;
import net.fhirfactory.harmonia.themis.api.model.ThemisSecurityContext;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

class InformationUnitControllerTest {

    @Test
    void deniedThemisDecisionDoesNotReachPersistence() {
        NonFhirPersistenceService persistenceService = mock(NonFhirPersistenceService.class);
        GovernedPersistenceFramework framework = new GovernedPersistenceFramework(
                persistenceService,
                PersistenceDurabilityProfile.DEVELOPMENT_LOCAL_TRANSACTION);
        InformationUnitController controller = new InformationUnitController(
                new MnemosyneAuthorizationGate(request -> ThemisAuthorizationDecision.defaultDeny("test", "no policy")),
                framework,
                new InformationUnitDisassembler(new ObjectMapper()),
                new InformationUnitConstructor(new ObjectMapper()));
        InformationUnit informationUnit = new InformationUnit(
                InformationUnitIdentifier.random(),
                null,
                InformationMetadata.empty(),
                null,
                new InformationRepresentation(null, null, Content.text("{}")));
        ResourceKey key = ResourceKey.of("InformationUnit", informationUnit.identifier().uuid().toString());
        ThemisSecurityContext context = ThemisSecurityContext.builder()
                .requestingPrincipal(ThemisPrincipal.human("v001-denied"))
                .correlationId("v001-denied-request")
                .build();

        assertThatThrownBy(() -> controller.create(key, informationUnit, context))
                .isInstanceOf(SecurityException.class);
        verify(persistenceService, never()).create(any());
    }
}