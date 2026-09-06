package net.fhirfactory.hie.befe.config;

import jakarta.annotation.PreDestroy;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.infinispan.client.hotrod.RemoteCacheManager;
import org.infinispan.client.hotrod.configuration.ClientIntelligence;
import org.infinispan.client.hotrod.configuration.ConfigurationBuilder;
import org.infinispan.commons.marshall.StringMarshaller;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

@ApplicationScoped
public class HotRodClientProducer {

    private static final Logger log = LoggerFactory.getLogger(HotRodClientProducer.class);

    private RemoteCacheManager cacheManager;

    @Produces
    @ApplicationScoped
    public RemoteCacheManager produceRemoteCacheManager() {
        if (cacheManager == null) {
            String host = System.getenv().getOrDefault("INFINISPAN_HOST", "127.0.0.1");
            int port = Integer.parseInt(System.getenv().getOrDefault("INFINISPAN_PORT", "11222"));
            String user = System.getenv().getOrDefault("INFINISPAN_USER", "admin");
            String pass = System.getenv().getOrDefault("INFINISPAN_PASSWORD", "admin");

            log.info("Connecting to Infinispan Hot Rod cluster at {}:{}", host, port);

            ConfigurationBuilder builder = new ConfigurationBuilder();
            builder.clientIntelligence(ClientIntelligence.BASIC)
                    .marshaller(new StringMarshaller(StandardCharsets.UTF_8))
                    .addServer()
                    .host(host)
                    .port(port)
                    .security()
                    .authentication()
                    .enable()
                    .username(user)
                    .password(pass)
                    .realm("default")
                    .saslMechanism("DIGEST-MD5")
                    .connectionPool()
                    .maxActive(50)
                    .socketTimeout(10000)
                    .connectionTimeout(5000);

            try {
                cacheManager = new RemoteCacheManager(builder.build(), true);
                log.info("Hot Rod RemoteCacheManager successfully initialized.");
            } catch (Exception e) {
                log.warn("Failed to connect to Remote Infinispan cluster during startup: {}. Initializing lazy manager.", e.getMessage());
                cacheManager = new RemoteCacheManager(builder.build(), false);
            }
        }
        return cacheManager;
    }

    @PreDestroy
    public void cleanup() {
        if (cacheManager != null && cacheManager.isStarted()) {
            log.info("Stopping RemoteCacheManager");
            cacheManager.stop();
        }
    }
}
