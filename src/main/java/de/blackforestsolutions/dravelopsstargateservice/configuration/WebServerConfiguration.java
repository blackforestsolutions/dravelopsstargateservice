package de.blackforestsolutions.dravelopsstargateservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootConfiguration
public class WebServerConfiguration {

    @Bean
    @Primary
    public ObjectMapper objectMapper() {
        return new DravelOpsJsonMapper();
    }
}
