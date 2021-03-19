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
    @Value("${graphql.playground.tabs[0].variables.departureLatitude}")
    private Double departureLatitude;
    @Value("${graphql.playground.tabs[0].variables.departureLongitude}")
    private Double departureLongitude;
    @Value("${graphql.playground.tabs[0].variables.arrivalLatitude}")
    private Double arrivalLatitude;
    @Value("${graphql.playground.tabs[0].variables.arrivalLongitude}")
    private Double arrivalLongitude;
    @Value("${graphql.playground.tabs[0].variables.dateTime}")
    private String dateTime;
    @Value("${graphql.playground.tabs[0].variables.isArrivalDateTime}")
    private Boolean isArrivalDateTime;
    @Value("${graphql.playground.tabs[0].variables.language}")
    private Locale language;


    @Bean
    @ConfigurationProperties(prefix = "routepersistence")
    public ApiToken.ApiTokenBuilder routePersistenceApiTokenIT() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(path)
                .setDepartureCoordinate(new Point.PointBuilder(departureLongitude, departureLatitude).build())
                .setArrivalCoordinate(new Point.PointBuilder(arrivalLongitude, arrivalLatitude).build())
                .setDateTime(ZonedDateTime.parse(dateTime))
                .setIsArrivalDateTime(isArrivalDateTime)
                .setLanguage(language);
    }
}
