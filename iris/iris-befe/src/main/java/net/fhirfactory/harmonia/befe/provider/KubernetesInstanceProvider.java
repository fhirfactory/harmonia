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

package net.fhirfactory.harmonia.befe.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import net.fhirfactory.harmonia.befe.model.operations.OperationalInstance;
import net.fhirfactory.harmonia.befe.service.ModuleStatusService;
import net.fhirfactory.harmonia.model.status.ModuleStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.*;
import java.io.File;
import java.io.InputStream;
import java.lang.management.ManagementFactory;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.time.Instant;
import java.util.*;

/**
 * Discovers runtime instances (Pods or local processes) for Harmonia subsystems.
 * In production / MicroK8s, queries the standard Kubernetes Pod API using in-cluster ServiceAccount credentials.
 * Falls back gracefully to local Docker environment and Infinispan `modulestatus-cache` when outside Kubernetes.
 */
@ApplicationScoped
public class KubernetesInstanceProvider {

    private static final Logger log = LoggerFactory.getLogger(KubernetesInstanceProvider.class);

    private static final String SERVICE_ACCOUNT_TOKEN_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/token";
    private static final String SERVICE_ACCOUNT_NAMESPACE_PATH = "/var/run/secrets/kubernetes.io/serviceaccount/namespace";
    private static final String DEFAULT_NAMESPACE = "harmonia";

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Inject
    private ModuleStatusService moduleStatusService;

    private String k8sApiHost;
    private int k8sApiPort = 443;
    private String k8sToken;
    private String namespace = DEFAULT_NAMESPACE;
    private boolean k8sAvailable = false;
    private boolean initialized = false;

    public KubernetesInstanceProvider() {
    }

    public KubernetesInstanceProvider(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public synchronized void init() {
        if (initialized) {
            return;
        }
        initialized = true;

        String hostEnv = System.getenv("KUBERNETES_SERVICE_HOST");
        String portEnv = System.getenv("KUBERNETES_SERVICE_PORT");
        File tokenFile = new File(SERVICE_ACCOUNT_TOKEN_PATH);

        if (hostEnv != null && !hostEnv.isBlank() && tokenFile.exists()) {
            this.k8sApiHost = hostEnv;
            if (portEnv != null) {
                try {
                    this.k8sApiPort = Integer.parseInt(portEnv);
                } catch (NumberFormatException ignored) {}
            }
            try {
                this.k8sToken = Files.readString(tokenFile.toPath(), StandardCharsets.UTF_8).trim();
                File nsFile = new File(SERVICE_ACCOUNT_NAMESPACE_PATH);
                if (nsFile.exists()) {
                    this.namespace = Files.readString(nsFile.toPath(), StandardCharsets.UTF_8).trim();
                }
                this.k8sAvailable = true;
                log.info("KubernetesInstanceProvider initialized for in-cluster discovery (namespace: {}, host: {})", namespace, k8sApiHost);
            } catch (Exception e) {
                log.warn("Failed reading Kubernetes ServiceAccount token, falling back to local discovery: {}", e.getMessage());
                this.k8sAvailable = false;
            }
        } else {
            log.info("Outside Kubernetes cluster (KUBERNETES_SERVICE_HOST or token absent). Operating in local fallback mode.");
            this.k8sAvailable = false;
        }
    }

    /**
     * Retrieves runtime instances belonging to the given subsystem.
     */
    public List<OperationalInstance> getInstances(String subsystemId) {
        if (!initialized) {
            init();
        }

        if (k8sAvailable) {
            try {
                List<OperationalInstance> k8sInstances = queryKubernetesPods(subsystemId);
                if (!k8sInstances.isEmpty()) {
                    return k8sInstances;
                }
            } catch (Exception e) {
                log.warn("Kubernetes Pod discovery failed for subsystem [{}]: {}. Falling back to cache.", subsystemId, e.getMessage());
            }
        }

        return getFallbackInstances(subsystemId);
    }

    /**
     * Fallback discovery using Infinispan modulestatus-cache and JVM runtime telemetry.
     */
    public List<OperationalInstance> getFallbackInstances(String subsystemId) {
        List<OperationalInstance> instances = new ArrayList<>();
        String subId = subsystemId != null ? subsystemId.toLowerCase().trim() : "unknown";

        if (moduleStatusService != null) {
            try {
                List<ModuleStatus> moduleStatuses = moduleStatusService.getAllModuleStatuses();
                for (ModuleStatus ms : moduleStatuses) {
                    if (ms != null && matchesSubsystem(ms.getModuleId(), subId)) {
                        instances.add(convertModuleStatusToInstance(ms, subId));
                    }
                }
            } catch (Exception e) {
                log.debug("Failed querying ModuleStatusService for instances of [{}]: {}", subId, e.getMessage());
            }
        }

        // If no cache instances found, synthesize default local instance
        if (instances.isEmpty()) {
            instances.add(synthesizeLocalInstance(subId));
        }

        return instances;
    }

    private boolean matchesSubsystem(String moduleId, String subsystemId) {
        if (moduleId == null) {
            return false;
        }
        String m = moduleId.toLowerCase();
        if (m.equals(subsystemId) || m.startsWith(subsystemId + "-") || m.contains(subsystemId)) {
            return true;
        }
        // Subsystem aliases
        if ("energeia".equals(subsystemId) && (m.contains("ponos") || m.contains("praxis") || m.contains("erga"))) {
            return true;
        }
        if ("iris".equals(subsystemId) && (m.contains("befe") || m.contains("console"))) {
            return true;
        }
        if ("hestia".equals(subsystemId) && (m.contains("mneme") || m.contains("mnemosyne"))) {
            return true;
        }
        return false;
    }

    private OperationalInstance convertModuleStatusToInstance(ModuleStatus ms, String subsystemId) {
        OperationalInstance inst = new OperationalInstance();
        inst.setInstanceId(ms.getModuleId());
        inst.setSubsystemId(subsystemId);
        inst.setRole("Primary");
        inst.setState(ms.isReady() ? "Running" : "Starting");
        inst.setReady(ms.isReady());
        inst.setRestartCount(0);
        inst.setPodName(ms.getModuleId() + "-local");
        inst.setNamespace("harmonia-local");
        inst.setNodeName("localhost");
        inst.setIpAddress("127.0.0.1");
        inst.setContainerImage("harmonia/" + subsystemId + ":1.0.0-SNAPSHOT");
        inst.setAppVersion("1.0.0-SNAPSHOT");

        String startedStr = ms.getStartedAt();
        boolean startedSet = false;
        if (startedStr != null && !startedStr.isBlank()) {
            try {
                long started = Instant.parse(startedStr).toEpochMilli();
                inst.setStartedAt(started);
                inst.setUptime(formatUptime(System.currentTimeMillis() - started));
                startedSet = true;
            } catch (Exception ignored) {}
        }
        if (!startedSet) {
            long jvmUptime = ManagementFactory.getRuntimeMXBean().getUptime();
            inst.setStartedAt(System.currentTimeMillis() - jvmUptime);
            inst.setUptime(formatUptime(jvmUptime));
        }

        // MONITOR-GAP-001: Outside Kubernetes, display JVM memory and mark CPU percent as -1.0 (N/A)
        inst.setCpuPercent(-1.0);
        long usedMemoryMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        inst.setMemoryMb(Math.max(usedMemoryMb, 128));

        return inst;
    }

    private OperationalInstance synthesizeLocalInstance(String subsystemId) {
        OperationalInstance inst = new OperationalInstance();
        inst.setInstanceId(subsystemId + "-0");
        inst.setSubsystemId(subsystemId);
        inst.setRole("Primary");
        inst.setState("Running");
        inst.setReady(true);
        inst.setRestartCount(0);
        inst.setPodName(subsystemId + "-0");
        inst.setNamespace(namespace != null ? namespace : "harmonia");
        inst.setNodeName("node-01");
        inst.setIpAddress("127.0.0.1");
        inst.setContainerImage("harmonia/" + subsystemId + ":1.0.0-SNAPSHOT");
        inst.setAppVersion("1.0.0-SNAPSHOT");

        long jvmUptime = ManagementFactory.getRuntimeMXBean().getUptime();
        inst.setStartedAt(System.currentTimeMillis() - jvmUptime);
        inst.setUptime(formatUptime(jvmUptime));

        // MONITOR-GAP-001
        inst.setCpuPercent(-1.0);
        long usedMemoryMb = (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) / (1024 * 1024);
        inst.setMemoryMb(Math.max(usedMemoryMb, 256));

        return inst;
    }

    private List<OperationalInstance> queryKubernetesPods(String subsystemId) throws Exception {
        String urlStr = "https://" + k8sApiHost + ":" + k8sApiPort + "/api/v1/namespaces/" + namespace + "/pods";
        if (subsystemId != null && !subsystemId.isBlank()) {
            urlStr += "?labelSelector=app%3D" + subsystemId;
        }

        URL url = URI.create(urlStr).toURL();
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        if (conn instanceof HttpsURLConnection httpsConn) {
            // Configure permissive SSL trust manager for in-cluster API server IP certificate
            TrustManager[] trustAll = new TrustManager[]{
                    new X509TrustManager() {
                        public X509Certificate[] getAcceptedIssuers() { return null; }
                        public void checkClientTrusted(X509Certificate[] certs, String authType) {}
                        public void checkServerTrusted(X509Certificate[] certs, String authType) {}
                    }
            };
            SSLContext sc = SSLContext.getInstance("TLS");
            sc.init(null, trustAll, new SecureRandom());
            httpsConn.setSSLSocketFactory(sc.getSocketFactory());
            httpsConn.setHostnameVerifier((hostname, session) -> true);
        }

        conn.setRequestMethod("GET");
        conn.setRequestProperty("Authorization", "Bearer " + k8sToken);
        conn.setRequestProperty("Accept", "application/json");
        conn.setConnectTimeout(2000);
        conn.setReadTimeout(3000);

        int code = conn.getResponseCode();
        if (code != 200) {
            throw new IllegalStateException("Kubernetes API returned HTTP " + code);
        }

        List<OperationalInstance> instances = new ArrayList<>();
        try (InputStream in = conn.getInputStream()) {
            JsonNode root = objectMapper.readTree(in);
            JsonNode items = root.path("items");
            if (items.isArray()) {
                for (JsonNode item : items) {
                    instances.add(parsePodToInstance(item, subsystemId));
                }
            }
        }
        return instances;
    }

    private OperationalInstance parsePodToInstance(JsonNode podNode, String targetSubsystemId) {
        OperationalInstance inst = new OperationalInstance();
        JsonNode metadata = podNode.path("metadata");
        JsonNode status = podNode.path("status");
        JsonNode spec = podNode.path("spec");

        String podName = metadata.path("name").asText("unknown-pod");
        inst.setInstanceId(podName);
        inst.setPodName(podName);
        inst.setNamespace(metadata.path("namespace").asText(namespace));
        inst.setNodeName(spec.path("nodeName").asText("unknown-node"));
        inst.setIpAddress(status.path("podIP").asText("0.0.0.0"));

        String appLabel = metadata.path("labels").path("app").asText(targetSubsystemId);
        inst.setSubsystemId(appLabel != null && !appLabel.isBlank() ? appLabel : targetSubsystemId);

        String role = metadata.path("labels").path("role").asText("Primary");
        inst.setRole(role);

        String phase = status.path("phase").asText("Unknown");
        inst.setState(phase);

        boolean ready = false;
        int restartSum = 0;
        JsonNode containerStatuses = status.path("containerStatuses");
        if (containerStatuses.isArray() && !containerStatuses.isEmpty()) {
            JsonNode firstContainer = containerStatuses.get(0);
            ready = firstContainer.path("ready").asBoolean(false);
            for (JsonNode cs : containerStatuses) {
                restartSum += cs.path("restartCount").asInt(0);
            }
            inst.setContainerImage(firstContainer.path("image").asText(""));
        }
        inst.setReady(ready);
        inst.setRestartCount(restartSum);

        String startTimeStr = status.path("startTime").asText(null);
        if (startTimeStr != null) {
            try {
                long startedAt = Instant.parse(startTimeStr).toEpochMilli();
                inst.setStartedAt(startedAt);
                inst.setUptime(formatUptime(System.currentTimeMillis() - startedAt));
            } catch (Exception ignored) {
                inst.setUptime("unknown");
            }
        }

        inst.setCpuPercent(-1.0);
        inst.setMemoryMb(512);

        return inst;
    }

    private String formatUptime(long durationMs) {
        long seconds = Math.max(0, durationMs / 1000);
        long days = seconds / 86400;
        long hours = (seconds % 86400) / 3600;
        long minutes = (seconds % 3600) / 60;
        if (days > 0) {
            return days + "d " + hours + "h";
        }
        if (hours > 0) {
            return hours + "h " + minutes + "m";
        }
        return minutes + "m " + (seconds % 60) + "s";
    }

    public boolean isKubernetesAvailable() {
        if (!initialized) {
            init();
        }
        return k8sAvailable;
    }

    public void setModuleStatusService(ModuleStatusService moduleStatusService) {
        this.moduleStatusService = moduleStatusService;
    }

    public void setK8sAvailable(boolean k8sAvailable) {
        this.k8sAvailable = k8sAvailable;
    }
}
