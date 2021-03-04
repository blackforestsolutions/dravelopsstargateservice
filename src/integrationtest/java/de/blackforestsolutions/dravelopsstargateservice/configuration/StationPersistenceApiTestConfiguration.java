package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

@Import(ConverterConfiguration.class)
@TestConfiguration
public class StationPersistenceApiTestConfiguration {

    @Value("${stationpersistence.protocol}")
    private String protocol;
    @Value("${stationpersistence.host}")
    private String host;
    @Value("${stationpersistence.port}")
    private int port;
    @Value("${stationpersistence.get.travelpoint.path}")
    private String stationPersistenceTravelPointPath;
    @Value("${stationpersistence.get.polygon.path}")
    private String stationPersistencePolygonPath;

    @Bean
    public ApiToken.ApiTokenBuilder stationPersistenceTravelPointApiTokenIT() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setPath(stationPersistenceTravelPointPath);
    }

    @Bean
    public ApiToken.ApiTokenBuilder stationPersistencePolygonApiTokenIT() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setPath(stationPersistencePolygonPath);
    }


}
