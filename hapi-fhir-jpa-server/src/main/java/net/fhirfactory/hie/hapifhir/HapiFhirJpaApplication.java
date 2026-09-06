package net.fhirfactory.hie.hapifhir;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import javax.sql.DataSource;
import java.sql.Connection;

@SpringBootApplication
public class HapiFhirJpaApplication {

    private static final Logger log = LoggerFactory.getLogger(HapiFhirJpaApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(HapiFhirJpaApplication.class, args);
    }

    @Bean
    ApplicationRunner logDatabaseConnection(DataSource dataSource) {
        return args -> {
            try (Connection connection = dataSource.getConnection()) {
                log.info("HAPI FHIR JPA Server connected to database: {}", connection.getMetaData().getURL());
                log.info("Database product: {} {}", connection.getMetaData().getDatabaseProductName(), connection.getMetaData().getDatabaseProductVersion());
            }
        };
    }
}
