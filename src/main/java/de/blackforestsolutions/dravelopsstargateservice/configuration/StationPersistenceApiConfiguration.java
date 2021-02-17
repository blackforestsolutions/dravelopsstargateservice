package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class StationPersistenceApiConfiguration {

    @Value("${stationpersistence.protocol}")
    private String stationPersistenceProtocol;
    @Value("${stationpersistence.host}")
    private String stationPersistenceHost;
    @Value("${stationpersistence.port}")
    private int stationPersistencePort;
    @Value("${stationpersistence.get.journey.path}")
    private String stationPersistenceTravelPointPath;

    @Bean(name = "stationPersistenceApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(stationPersistenceProtocol)
                .setHost(stationPersistenceHost)
                .setPort(stationPersistencePort)
                .setPath(stationPersistenceTravelPointPath)
                .build();
    }
}
