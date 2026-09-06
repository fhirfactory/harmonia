package net.fhirfactory.hie.hapifhir.config;

import ca.uhn.fhir.context.FhirContext;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FhirServerConfig {

    @Bean
    public FhirContext fhirContext() {
        return FhirContext.forR5();
    }

    @Bean
    public ServletRegistrationBean<JpaRestfulServer> fhirServletRegistration(ApplicationContext applicationContext, FhirContext fhirContext) {
        JpaRestfulServer servlet = new JpaRestfulServer(applicationContext, fhirContext);
        ServletRegistrationBean<JpaRestfulServer> registration = new ServletRegistrationBean<>(servlet, "/fhir/*");
        registration.setName("FhirRestfulServerServlet");
        registration.setLoadOnStartup(1);
        return registration;
    }
}
