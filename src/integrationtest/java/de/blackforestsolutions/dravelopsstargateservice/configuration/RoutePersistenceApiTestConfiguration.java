package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;

@Import(ZonedDateTimeConfiguration.class)
@TestConfiguration
public class RoutePersistenceApiTestConfiguration {

    @Value("${routepersistence.protocol}")
    private String protocol;
    @Value("${routepersistence.host}")
    private String host;
    @Value("${routepersistence.port}")
    private int port;
    @Value("${routepersistence.get.journey.path}")
    private String path;
    @Value("${test.apitokens[0].departureCoordinateLongitude}")
    private Double departureCoordinateLongitude;
    @Value("${test.apitokens[0].departureCoordinateLatitude}")
    private Double departureCoordinateLatitude;
    @Value("${test.apitokens[0].arrivalCoordinateLongitude}")
    private Double arrivalCoordinateLongitude;
    @Value("${test.apitokens[0].arrivalCoordinateLatitude}")
    private Double arrivalCoordinateLatitude;

    @Bean(name = "routePersistenceApiTokenIT")
    @ConfigurationProperties(prefix = "test.apitokens[0]")
    public ApiToken.ApiTokenBuilder apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setPath(path)
                .setDepartureCoordinate(new Point(departureCoordinateLongitude, departureCoordinateLatitude))
                .setArrivalCoordinate(new Point(arrivalCoordinateLongitude, arrivalCoordinateLatitude));
    }
}
