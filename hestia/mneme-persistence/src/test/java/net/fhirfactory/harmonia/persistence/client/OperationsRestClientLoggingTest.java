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

package net.fhirfactory.harmonia.persistence.client;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.read.ListAppender;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import org.junit.jupiter.api.*;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("OperationsRestClient Response and Exception Logging Hardening Tests")
class OperationsRestClientLoggingTest {

    private static final String PHI_MARKER = "PATIENT-PHI-MARKER-92831";
    private static final String SECRET_TOKEN = "TOKEN-SECRET-MARKER-81742";

    private static WireMockServer wireMockServer;
    private OperationsRestClient client;
    private Logger clientLogger;
    private ListAppender<ILoggingEvent> listAppender;

    @BeforeAll
    static void startWireMock() {
        wireMockServer = new WireMockServer(WireMockConfiguration.wireMockConfig().dynamicPort());
        wireMockServer.start();
        WireMock.configureFor("localhost", wireMockServer.port());
    }

    @AfterAll
    static void stopWireMock() {
        if (wireMockServer != null) {
            wireMockServer.stop();
        }
    }

    @BeforeEach
    void setUp() {
        wireMockServer.resetAll();
        client = new OperationsRestClient("http://localhost:" + wireMockServer.port(), 5);

        clientLogger = (Logger) LoggerFactory.getLogger(OperationsRestClient.class);
        clientLogger.setLevel(Level.TRACE);

        listAppender = new ListAppender<>();
        listAppender.start();
        clientLogger.addAppender(listAppender);
    }

    @AfterEach
    void tearDown() {
        if (listAppender != null) {
            listAppender.stop();
            clientLogger.detachAppender(listAppender);
        }
    }

    @Test
    @DisplayName("PUT failure must log operational metadata without response body, PHI marker, or secret token")
    void testSaveResourceJsonFailureSuppressesResponseBodyAndPhi() throws Exception {
        String errorResponseBody = "{\"error\": \"Patient update failed\", \"phi\": \"" + PHI_MARKER
                + "\", \"token\": \"" + SECRET_TOKEN + "\"}";

        stubFor(put(urlEqualTo("/tasksequence/seq-001"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody(errorResponseBody)));

        Boolean success = client.saveResourceJson("tasksequence", "seq-001", "{\"status\":\"active\"}")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(success).isFalse();

        List<ILoggingEvent> errorEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents)
                .as("Exactly one ERROR log event must be emitted on PUT failure")
                .hasSize(1);

        String formattedMessage = errorEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("ERROR log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("ERROR log must contain HTTP operation")
                .contains("operation=PUT")
                .as("ERROR log must contain HTTP status code")
                .contains("status=500")
                .as("ERROR log must contain target URI")
                .contains("/tasksequence/seq-001")
                .as("ERROR log must contain safe failure category")
                .contains("category=SERVER_ERROR_INTERNAL")
                .as("ERROR log must NOT contain raw response body")
                .doesNotContain(errorResponseBody)
                .as("ERROR log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("ERROR log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("DELETE failure must log operational metadata without response body, PHI marker, or secret token")
    void testDeleteResourceFailureSuppressesResponseBodyAndPhi() throws Exception {
        String errorResponseBody = "{\"error\": \"Delete blocked for patient " + PHI_MARKER
                + "\", \"auth\": \"" + SECRET_TOKEN + "\"}";

        stubFor(delete(urlEqualTo("/tasksequence/seq-002"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody(errorResponseBody)));

        Boolean success = client.deleteResource("tasksequence", "seq-002")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(success).isFalse();

        List<ILoggingEvent> errorEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents)
                .as("Exactly one ERROR log event must be emitted on DELETE failure")
                .hasSize(1);

        String formattedMessage = errorEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("ERROR log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("ERROR log must contain HTTP operation")
                .contains("operation=DELETE")
                .as("ERROR log must contain HTTP status code")
                .contains("status=500")
                .as("ERROR log must contain target URI")
                .contains("/tasksequence/seq-002")
                .as("ERROR log must contain safe failure category")
                .contains("category=SERVER_ERROR_INTERNAL")
                .as("ERROR log must NOT contain raw response body")
                .doesNotContain(errorResponseBody)
                .as("ERROR log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("ERROR log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("List resources JSON parse failure must log exception class and category without raw message or PHI")
    void testListResourcesParseFailureSuppressesRawExceptionMessageAndPhi() throws Exception {
        String malformedPayload = "[{\"objectId\": \"seq-003\", \"data\": \"INVALID_JSON_"
                + PHI_MARKER + "_" + SECRET_TOKEN;

        stubFor(get(urlEqualTo("/tasksequence"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody(malformedPayload)));

        List<OperationsRestClient.OperationResourceEntry> result = client.listResources("tasksequence")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(result).isEmpty();

        List<ILoggingEvent> errorEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.ERROR)
                .toList();

        assertThat(errorEvents)
                .as("Exactly one ERROR log event must be emitted on parse failure")
                .hasSize(1);

        String formattedMessage = errorEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("ERROR log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("ERROR log must contain HTTP operation")
                .contains("operation=GET")
                .as("ERROR log must contain HTTP status code")
                .contains("status=200")
                .as("ERROR log must contain exception class name")
                .contains("exception=com.fasterxml.jackson.core.io.JsonEOFException")
                .as("ERROR log must contain safe failure category")
                .contains("category=PAYLOAD_PARSE_FAILURE")
                .as("ERROR log must NOT contain malformed payload text")
                .doesNotContain(malformedPayload)
                .as("ERROR log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("ERROR log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("List resources non-200 HTTP response must log WARN with metadata without response body or PHI")
    void testListResourcesHttpErrorSuppressesResponseBodyAndPhi() throws Exception {
        String errorResponseBody = "{\"error\": \"Service unavailable for patient " + PHI_MARKER
                + "\", \"key\": \"" + SECRET_TOKEN + "\"}";

        stubFor(get(urlEqualTo("/tasksequence"))
                .willReturn(aResponse()
                        .withStatus(503)
                        .withHeader("Content-Type", "application/json")
                        .withBody(errorResponseBody)));

        List<OperationsRestClient.OperationResourceEntry> result = client.listResources("tasksequence")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(result).isEmpty();

        List<ILoggingEvent> warnEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .toList();

        assertThat(warnEvents)
                .as("Exactly one WARN log event must be emitted on HTTP error")
                .hasSize(1);

        String formattedMessage = warnEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("WARN log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("WARN log must contain HTTP operation")
                .contains("operation=GET")
                .as("WARN log must contain HTTP status code")
                .contains("status=503")
                .as("WARN log must contain safe failure category")
                .contains("category=SERVER_ERROR_SERVICE_UNAVAILABLE")
                .as("WARN log must NOT contain raw response body")
                .doesNotContain(errorResponseBody)
                .as("WARN log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("WARN log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("GET resource JSON non-200 HTTP response must log WARN with metadata without response body or PHI")
    void testGetResourceJsonHttpErrorSuppressesResponseBodyAndPhi() throws Exception {
        String errorResponseBody = "{\"error\": \"Resource error for patient " + PHI_MARKER
                + "\", \"token\": \"" + SECRET_TOKEN + "\"}";

        stubFor(get(urlEqualTo("/tasksequence/seq-004"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withHeader("Content-Type", "application/json")
                        .withBody(errorResponseBody)));

        String resourceJson = client.getResourceJson("tasksequence", "seq-004")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(resourceJson).isNull();

        List<ILoggingEvent> warnEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.WARN)
                .toList();

        assertThat(warnEvents)
                .as("Exactly one WARN log event must be emitted on HTTP error")
                .hasSize(1);

        String formattedMessage = warnEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("WARN log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("WARN log must contain HTTP operation")
                .contains("operation=GET")
                .as("WARN log must contain HTTP status code")
                .contains("status=500")
                .as("WARN log must contain safe failure category")
                .contains("category=SERVER_ERROR_INTERNAL")
                .as("WARN log must NOT contain raw response body")
                .doesNotContain(errorResponseBody)
                .as("WARN log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("WARN log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("Readiness check communication failure must log exception class and category without raw message or PHI")
    void testIsServerReadyFailureSuppressesExceptionMessageWithPhi() throws Exception {
        HttpClient mockHttpClient = mock(HttpClient.class);
        String sensitiveExceptionMessage = "Failed connection for " + PHI_MARKER + " with " + SECRET_TOKEN;

        when(mockHttpClient.sendAsync(any(HttpRequest.class), any(HttpResponse.BodyHandler.class)))
                .thenReturn(CompletableFuture.failedFuture(new IOException(sensitiveExceptionMessage)));

        OperationsRestClient clientWithMock = new OperationsRestClient("http://localhost:" + wireMockServer.port(), mockHttpClient);

        Boolean ready = clientWithMock.isServerReady()
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);

        assertThat(ready).isFalse();

        List<ILoggingEvent> debugEvents = listAppender.list.stream()
                .filter(e -> e.getLevel() == Level.DEBUG)
                .toList();

        assertThat(debugEvents)
                .as("DEBUG log event must be emitted on readiness failure")
                .isNotEmpty();

        String formattedMessage = debugEvents.get(0).getFormattedMessage();
        assertThat(formattedMessage)
                .as("DEBUG log must contain target service name")
                .contains(OperationsRestClient.TARGET_SERVICE)
                .as("DEBUG log must contain HTTP operation")
                .contains("operation=GET")
                .as("DEBUG log must contain exception class name")
                .contains("exception=java.io.IOException")
                .as("DEBUG log must contain safe failure category")
                .contains("category=READINESS_CHECK_FAILURE")
                .as("DEBUG log must NOT contain sensitive exception message")
                .doesNotContain(sensitiveExceptionMessage)
                .as("DEBUG log must NOT contain PHI marker")
                .doesNotContain(PHI_MARKER)
                .as("DEBUG log must NOT contain secret token")
                .doesNotContain(SECRET_TOKEN);

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    @Test
    @DisplayName("Categorize HTTP status helper returns accurate, safe categories")
    void testCategorizeHttpStatus() {
        assertThat(OperationsRestClient.categorizeHttpStatus(400)).isEqualTo("CLIENT_ERROR_BAD_REQUEST");
        assertThat(OperationsRestClient.categorizeHttpStatus(401)).isEqualTo("CLIENT_ERROR_UNAUTHORIZED");
        assertThat(OperationsRestClient.categorizeHttpStatus(403)).isEqualTo("CLIENT_ERROR_FORBIDDEN");
        assertThat(OperationsRestClient.categorizeHttpStatus(404)).isEqualTo("CLIENT_ERROR_NOT_FOUND");
        assertThat(OperationsRestClient.categorizeHttpStatus(409)).isEqualTo("CLIENT_ERROR_CONFLICT");
        assertThat(OperationsRestClient.categorizeHttpStatus(410)).isEqualTo("CLIENT_ERROR_GONE");
        assertThat(OperationsRestClient.categorizeHttpStatus(422)).isEqualTo("CLIENT_ERROR_UNPROCESSABLE_ENTITY");
        assertThat(OperationsRestClient.categorizeHttpStatus(429)).isEqualTo("CLIENT_ERROR_TOO_MANY_REQUESTS");
        assertThat(OperationsRestClient.categorizeHttpStatus(499)).isEqualTo("CLIENT_ERROR");

        assertThat(OperationsRestClient.categorizeHttpStatus(500)).isEqualTo("SERVER_ERROR_INTERNAL");
        assertThat(OperationsRestClient.categorizeHttpStatus(502)).isEqualTo("SERVER_ERROR_BAD_GATEWAY");
        assertThat(OperationsRestClient.categorizeHttpStatus(503)).isEqualTo("SERVER_ERROR_SERVICE_UNAVAILABLE");
        assertThat(OperationsRestClient.categorizeHttpStatus(504)).isEqualTo("SERVER_ERROR_GATEWAY_TIMEOUT");
        assertThat(OperationsRestClient.categorizeHttpStatus(599)).isEqualTo("SERVER_ERROR");

        assertThat(OperationsRestClient.categorizeHttpStatus(200)).isEqualTo("HTTP_ERROR");
    }

    @Test
    @DisplayName("Successful PUT, GET, and DELETE emit safe operational logs without payload leakage")
    void testSuccessOperationsLogSafely() throws Exception {
        stubFor(put(urlEqualTo("/tasksequence/seq-005"))
                .willReturn(aResponse().withStatus(200).withBody("{\"saved\":true}")));
        stubFor(get(urlEqualTo("/tasksequence/seq-005"))
                .willReturn(aResponse().withStatus(200).withBody("{\"id\":\"seq-005\"}")));
        stubFor(delete(urlEqualTo("/tasksequence/seq-005"))
                .willReturn(aResponse().withStatus(204)));

        Boolean putSuccess = client.saveResourceJson("tasksequence", "seq-005", "{\"data\":\"test\"}")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        assertThat(putSuccess).isTrue();

        String getPayload = client.getResourceJson("tasksequence", "seq-005")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        assertThat(getPayload).isEqualTo("{\"id\":\"seq-005\"}");

        Boolean deleteSuccess = client.deleteResource("tasksequence", "seq-005")
                .toCompletableFuture()
                .get(5, TimeUnit.SECONDS);
        assertThat(deleteSuccess).isTrue();

        assertNoCapturedLogsContainSensitiveMarkers();
    }

    private void assertNoCapturedLogsContainSensitiveMarkers() {
        for (ILoggingEvent event : listAppender.list) {
            String msg = event.getFormattedMessage();
            assertThat(msg)
                    .as("Log message at level %s must not contain PHI marker", event.getLevel())
                    .doesNotContain(PHI_MARKER);
            assertThat(msg)
                    .as("Log message at level %s must not contain secret token", event.getLevel())
                    .doesNotContain(SECRET_TOKEN);
        }
    }
}
