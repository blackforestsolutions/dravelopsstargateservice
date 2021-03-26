package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@RefreshScope
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

    @RefreshScope
    @Bean
    public ApiToken stationPersistenceTravelPointApiToken() {
        ApiToken apiToken = new ApiToken(getBasePersistenceApiToken());
        apiToken.setPath(stationPersistenceTravelPointPath);
        return apiToken;
    }

    @RefreshScope
    @Bean
    public ApiToken stationPersistencePolygonApiToken() {
        ApiToken apiToken = new ApiToken(getBasePersistenceApiToken());
        apiToken.setPath(stationPersistencePolygonPath);
        return apiToken;
    }

    private ApiToken getBasePersistenceApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setProtocol(stationPersistenceProtocol);
        apiToken.setHost(stationPersistenceHost);
        apiToken.setPort(stationPersistencePort);

        return apiToken;
    }
}
