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

package net.fhirfactory.harmonia.persistence.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.SecureRandom;

/**
 * Factory utility constructing {@link SSLContext} instances for mutual TLS (mTLS)
 * transport authentication from PKCS12 / JKS keystores and truststores.
 */
public final class SslContextFactory {

    private static final Logger log = LoggerFactory.getLogger(SslContextFactory.class);

    private SslContextFactory() {
        // Utility class
    }

    /**
     * Builds an {@link SSLContext} using the supplied keystore and truststore configurations.
     *
     * @param keyStorePath        path to client keystore (filesystem or classpath, nullable)
     * @param keyStorePassword    password for client keystore (nullable)
     * @param keyStoreType        type of keystore (e.g. "PKCS12", nullable defaults to "PKCS12")
     * @param trustStorePath      path to truststore (filesystem or classpath, nullable)
     * @param trustStorePassword  password for truststore (nullable)
     * @param trustStoreType      type of truststore (e.g. "PKCS12", nullable defaults to "PKCS12")
     * @return initialized {@link SSLContext}
     * @throws GeneralSecurityException if key or trust manager initialization fails
     * @throws IOException              if keystore/truststore files cannot be read
     */
    public static SSLContext createSslContext(
            String keyStorePath,
            String keyStorePassword,
            String keyStoreType,
            String trustStorePath,
            String trustStorePassword,
            String trustStoreType) throws GeneralSecurityException, IOException {

        KeyManager[] keyManagers = null;
        if (keyStorePath != null && !keyStorePath.isBlank()) {
            String type = (keyStoreType != null && !keyStoreType.isBlank()) ? keyStoreType : "PKCS12";
            char[] password = (keyStorePassword != null) ? keyStorePassword.toCharArray() : new char[0];
            KeyStore keyStore = loadKeyStore(keyStorePath, password, type);

            KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
            kmf.init(keyStore, password);
            keyManagers = kmf.getKeyManagers();
            log.debug("Initialized KeyManagers from keystore: {}", keyStorePath);
        }

        TrustManager[] trustManagers = null;
        if (trustStorePath != null && !trustStorePath.isBlank()) {
            String type = (trustStoreType != null && !trustStoreType.isBlank()) ? trustStoreType : "PKCS12";
            char[] password = (trustStorePassword != null) ? trustStorePassword.toCharArray() : new char[0];
            KeyStore trustStore = loadKeyStore(trustStorePath, password, type);

            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(trustStore);
            trustManagers = tmf.getTrustManagers();
            log.debug("Initialized TrustManagers from truststore: {}", trustStorePath);
        }

        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(keyManagers, trustManagers, new SecureRandom());
        return sslContext;
    }

    private static KeyStore loadKeyStore(String path, char[] password, String type)
            throws GeneralSecurityException, IOException {
        KeyStore keyStore = KeyStore.getInstance(type);
        try (InputStream is = openInputStream(path)) {
            keyStore.load(is, password);
        }
        return keyStore;
    }

    private static InputStream openInputStream(String path) throws IOException {
        String cleanPath = path.trim();
        if (cleanPath.startsWith("classpath:")) {
            String resPath = cleanPath.substring("classpath:".length());
            InputStream is = SslContextFactory.class.getResourceAsStream(resPath.startsWith("/") ? resPath : "/" + resPath);
            if (is != null) {
                return is;
            }
            throw new FileNotFoundException("Classpath resource not found: " + path);
        }

        File file = new File(cleanPath);
        if (file.exists() && file.isFile()) {
            return new FileInputStream(file);
        }

        // Fallback: attempt loading from classpath if not found as direct file
        String resPath = cleanPath.startsWith("/") ? cleanPath : "/" + cleanPath;
        InputStream is = SslContextFactory.class.getResourceAsStream(resPath);
        if (is != null) {
            return is;
        }

        throw new FileNotFoundException("Keystore / Truststore file not found at path: " + path);
    }
}
