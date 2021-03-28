package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;


@TestConfiguration
public class StationPersistenceApiTestConfiguration {

    @Value("${stationpersistence.get.travelpoint.path}")
    private String stationPersistenceTravelPointPath;
    @Value("${stationpersistence.get.polygon.path}")
    private String stationPersistencePolygonPath;


    @Bean
    @ConfigurationProperties(prefix = "stationpersistence")
    public ApiToken stationPersistenceTravelPointApiTokenIT() {
        ApiToken apiToken = new ApiToken();
        apiToken.setPath(stationPersistenceTravelPointPath);
        return apiToken;
    }

    @Bean
    @ConfigurationProperties(prefix = "stationpersistence")
    public ApiToken stationPersistencePolygonApiTokenIT() {
        ApiToken apiToken = new ApiToken();
        apiToken.setPath(stationPersistencePolygonPath);
        return apiToken;
    }


}
