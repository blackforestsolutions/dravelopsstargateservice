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
    @Value("${stationpersistence.get.travelpoint.path}")
    private String stationPersistenceTravelPointPath;
    @Value("${stationpersistence.get.polygon.path}")
    private String stationPersistencePolygonPath;

    @Bean(name = "stationPersistenceTravelPointApiToken")
    public ApiToken travelPointControllerApiToken() {
        return getBasePersistenceApiToken()
                .setPath(stationPersistenceTravelPointPath)
                .build();
    }

    @Bean(name = "stationPersistencePolygonApiToken")
    public ApiToken polygonControllerApiToken() {
        return getBasePersistenceApiToken()
                .setPath(stationPersistencePolygonPath)
                .build();
    }

    private ApiToken.ApiTokenBuilder getBasePersistenceApiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(stationPersistenceProtocol)
                .setHost(stationPersistenceHost)
                .setPort(stationPersistencePort);
    }
}
