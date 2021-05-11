package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;
import java.util.Locale;

@TestConfiguration
public class RoutePersistenceApiTestConfiguration {

    @Value("${routepersistence.get.journey.path}")
    private String path;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.departureLatitude}")
    private Double departureLatitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.departureLongitude}")
    private Double departureLongitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.arrivalLatitude}")
    private Double arrivalLatitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.arrivalLongitude}")
    private Double arrivalLongitude;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.dateTime}")
    private String dateTime;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.isArrivalDateTime}")
    private Boolean isArrivalDateTime;
    @Value("${graphql.playground.tabs.JOURNEY_QUERY.variables.language}")
    private Locale language;


    @Bean
    @ConfigurationProperties(prefix = "routepersistence")
    public ApiToken routePersistenceApiTokenIT() {
        ApiToken apiToken = new ApiToken();

        apiToken.setPath(path);
        apiToken.setDepartureCoordinate(new Point.PointBuilder(departureLongitude, departureLatitude).build());
        apiToken.setArrivalCoordinate(new Point.PointBuilder(arrivalLongitude, arrivalLatitude).build());
        apiToken.setDateTime(ZonedDateTime.parse(dateTime));
        apiToken.setIsArrivalDateTime(isArrivalDateTime);
        apiToken.setLanguage(language);

        return apiToken;
    }
}
