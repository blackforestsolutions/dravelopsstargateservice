package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

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

    @Bean(name = "routePersistenceApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(routePersistenceProtocol)
                .setHost(routePersistenceHost)
                .setPort(routePersistencePort)
                .setPath(routePersistenceJourneyPath)
                .build();
    }


}
