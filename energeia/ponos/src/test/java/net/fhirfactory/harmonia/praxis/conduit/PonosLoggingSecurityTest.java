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

package net.fhirfactory.harmonia.praxis.conduit;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import net.fhirfactory.harmonia.model.pragma.Pragma;
import net.fhirfactory.harmonia.petasos.api.message.PetasosMessage;
import net.fhirfactory.harmonia.petasos.api.consumer.PetasosMessageContext;
import org.apache.camel.CamelContext;
import org.apache.camel.Exchange;
import org.apache.camel.impl.DefaultCamelContext;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;

@ExtendWith(MockitoExtension.class)
class PonosLoggingSecurityTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_MARKER = "TOKEN-SECRET-MARKER-81742";

    @Mock
    private PetasosMessage petasosMessage;

    @Mock
    private Exchange exchange;

    private ListAppender<ILoggingEvent> conduitAppender;
    private Logger conduitLogger;
    
    private CamelContext camelContext;

    @BeforeEach
    void setUp() {
        conduitLogger = (Logger) LoggerFactory.getLogger(PetasosQueueToExchangeConduit.class);
        conduitAppender = new ListAppender<>();
        conduitAppender.start();
        conduitLogger.addAppender(conduitAppender);

        camelContext = new DefaultCamelContext();
    }

    @AfterEach
    void tearDown() {
        if (conduitLogger != null && conduitAppender != null) {
            conduitLogger.detachAppender(conduitAppender);
            conduitAppender.stop();
        }
    }

    @Test
    @DisplayName("PetasosQueueToExchangeConduit sanitizes dispatch exception logging and suppresses markers")
    void testConduitLoggingSuppressesPhiAndSecrets() {
        PetasosQueueToExchangeConduit conduit = new PetasosQueueToExchangeConduit();
        PragmaWorkflowDispatcher mockDispatcher = mock(PragmaWorkflowDispatcher.class);
        conduit.setWorkflowDispatcher(mockDispatcher);

        when(petasosMessage.getMessageId()).thenReturn("msg-ponos-001");
        
        // Simulate error path by forcing dispatcher to throw
        RuntimeException e = new RuntimeException("Crash with " + PHI_MARKER);
        when(mockDispatcher.dispatchPragma(any())).thenThrow(e);

        conduit.handleIncomingPetasosMessage(petasosMessage, mock(PetasosMessageContext.class));

        List<ILoggingEvent> events = conduitAppender.list;
        assertThat(events).isNotEmpty();

        for (ILoggingEvent event : events) {
            assertThat(event.getFormattedMessage())
                    .doesNotContain(PHI_MARKER)
                    .doesNotContain(SECRET_MARKER);
            if (event.getLevel() == Level.ERROR) {
                assertThat(event.getFormattedMessage())
                        .contains("category=DISPATCH_FAILURE")
                        .contains("exception=java.lang.RuntimeException")
                        .contains("msg-ponos-001");
            }
        }
    }

}
