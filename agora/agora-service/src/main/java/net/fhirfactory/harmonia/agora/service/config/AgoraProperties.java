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

package net.fhirfactory.harmonia.agora.service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * Authoritative configuration properties for Agora.
 */
@Configuration
@ConfigurationProperties(prefix = "agora")
public class AgoraProperties {

    private Synapse synapse = new Synapse();
    private Security security = new Security();
    private AppService appservice = new AppService();
    private Matrix matrix = new Matrix();
    private Reconciliation reconciliation = new Reconciliation();

    public Synapse getSynapse() {
        return synapse;
    }

    public void setSynapse(Synapse synapse) {
        this.synapse = synapse;
    }

    public Security getSecurity() {
        return security;
    }

    public void setSecurity(Security security) {
        this.security = security;
    }

    public AppService getAppservice() {
        return appservice;
    }

    public void setAppservice(AppService appservice) {
        this.appservice = appservice;
    }

    public Matrix getMatrix() {
        return matrix;
    }

    public void setMatrix(Matrix matrix) {
        this.matrix = matrix;
    }

    public Reconciliation getReconciliation() {
        return reconciliation;
    }

    public void setReconciliation(Reconciliation reconciliation) {
        this.reconciliation = reconciliation;
    }

    public static class Synapse {
        private String baseUrl = "http://synapse:8008";

        public String getBaseUrl() {
            return baseUrl;
        }

        public void setBaseUrl(String baseUrl) {
            this.baseUrl = baseUrl;
        }
    }

    public static class Security {
        private String hsToken;
        private String asToken;
        private String adminToken;

        public String getHsToken() {
            return hsToken;
        }

        public void setHsToken(String hsToken) {
            this.hsToken = hsToken;
        }

        public String getAsToken() {
            return asToken;
        }

        public void setAsToken(String asToken) {
            this.asToken = asToken;
        }

        public String getAdminToken() {
            return adminToken;
        }

        public void setAdminToken(String adminToken) {
            this.adminToken = adminToken;
        }
    }

    public static class AppService {
        private String id = "harmonia-agora";
        private String url = "http://agora:8092";

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }
    }

    public static class Matrix {
        private String serverName = "harmonia.local";

        public String getServerName() {
            return serverName;
        }

        public void setServerName(String serverName) {
            this.serverName = serverName;
        }
    }

    public static class Reconciliation {
        private String cron = "0 */15 * * * *";

        public String getCron() {
            return cron;
        }

        public void setCron(String cron) {
            this.cron = cron;
        }
    }
}
