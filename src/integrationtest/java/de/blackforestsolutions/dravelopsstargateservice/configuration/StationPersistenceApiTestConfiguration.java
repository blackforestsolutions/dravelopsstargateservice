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
    public ApiToken.ApiTokenBuilder stationPersistenceTravelPointApiTokenIT() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(stationPersistenceTravelPointPath);
    }

    @Bean
    @ConfigurationProperties(prefix = "stationpersistence")
    public ApiToken.ApiTokenBuilder stationPersistencePolygonApiTokenIT() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(stationPersistencePolygonPath);
    }


}
