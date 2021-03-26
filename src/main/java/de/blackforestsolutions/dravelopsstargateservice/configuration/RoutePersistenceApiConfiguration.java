package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@RefreshScope
@SpringBootConfiguration
public class RoutePersistenceApiConfiguration {

    @Value("${routepersistence.protocol}")
    private String routePersistenceProtocol;
    @Value("${routepersistence.host}")
    private String routePersistenceHost;
    @Value("${routepersistence.port}")
    private int routePersistencePort;
    @Value("${routepersistence.get.journey.path}")
    private String routePersistenceJourneyPath;

    @RefreshScope
    @Bean
    public ApiToken routePersistenceApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setProtocol(routePersistenceProtocol);
        apiToken.setHost(routePersistenceHost);
        apiToken.setPort(routePersistencePort);
        apiToken.setPath(routePersistenceJourneyPath);

        return apiToken;
    }


}
