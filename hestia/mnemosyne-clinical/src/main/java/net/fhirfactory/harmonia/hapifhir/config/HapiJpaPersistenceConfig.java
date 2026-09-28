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

package net.fhirfactory.harmonia.hapifhir.config;

import ca.uhn.fhir.batch2.jobs.config.Batch2JobsConfig;
import ca.uhn.fhir.jpa.api.config.JpaStorageSettings;
import ca.uhn.fhir.jpa.api.config.ThreadPoolFactoryConfig;
import ca.uhn.fhir.jpa.batch2.JpaBatch2Config;
import ca.uhn.fhir.jpa.config.HapiFhirLocalContainerEntityManagerFactoryBean;
import ca.uhn.fhir.jpa.config.HapiJpaConfig;
import ca.uhn.fhir.jpa.config.r5.JpaR5Config;
import ca.uhn.fhir.jpa.model.config.PartitionSettings;
import ca.uhn.fhir.jpa.subscription.channel.config.SubscriptionChannelConfig;
import ca.uhn.fhir.jpa.subscription.submit.config.SubscriptionSubmitterConfig;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateProperties;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateSettings;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Import({
    JpaR5Config.class,
    HapiJpaConfig.class,
    JpaBatch2Config.class,
    Batch2JobsConfig.class,
    ThreadPoolFactoryConfig.class,
    SubscriptionChannelConfig.class,
    SubscriptionSubmitterConfig.class
})
@EntityScan(basePackages = {
    "ca.uhn.fhir.jpa.model.entity",
    "ca.uhn.fhir.jpa.entity",
    "net.fhirfactory.harmonia.hapifhir.model"
})
@EnableJpaRepositories(basePackages = {
    "net.fhirfactory.harmonia.hapifhir.repository"
})
public class HapiJpaPersistenceConfig {

    @Bean
    public JpaStorageSettings jpaStorageSettings() {
        JpaStorageSettings storageSettings = new JpaStorageSettings();
        storageSettings.setResourceClientIdStrategy(JpaStorageSettings.ClientIdStrategyEnum.ANY);
        return storageSettings;
    }

    @Bean
    public PartitionSettings partitionSettings() {
        return new PartitionSettings();
    }

    @Bean(name = "entityManagerFactory")
    @Primary
    public HapiFhirLocalContainerEntityManagerFactoryBean entityManagerFactory(
            ConfigurableListableBeanFactory beanFactory,
            DataSource dataSource,
            JpaProperties jpaProperties,
            HibernateProperties hibernateProperties) {
        HapiFhirLocalContainerEntityManagerFactoryBean factory = new HapiFhirLocalContainerEntityManagerFactoryBean(beanFactory);
        factory.setDataSource(dataSource);
        factory.setPackagesToScan(
            "ca.uhn.fhir.jpa.model.entity",
            "ca.uhn.fhir.jpa.entity",
            "net.fhirfactory.harmonia.hapifhir.model"
        );
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(jpaProperties.isGenerateDdl());
        vendorAdapter.setShowSql(jpaProperties.isShowSql());
        if (jpaProperties.getDatabasePlatform() != null) {
            vendorAdapter.setDatabasePlatform(jpaProperties.getDatabasePlatform());
        }
        factory.setJpaVendorAdapter(vendorAdapter);
        Map<String, Object> jpaPropertyMap = new HashMap<>(jpaProperties.getProperties());
        jpaPropertyMap.putAll(hibernateProperties.determineHibernateProperties(
            jpaProperties.getProperties(), new HibernateSettings()));
        factory.setJpaPropertyMap(jpaPropertyMap);
        return factory;
    }
}
