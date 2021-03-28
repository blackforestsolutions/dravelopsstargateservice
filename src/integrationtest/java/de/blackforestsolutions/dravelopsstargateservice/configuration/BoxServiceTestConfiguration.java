package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.util.Locale;

@TestConfiguration
public class BoxServiceTestConfiguration {

    /**
     * boxServiceAutocompleteAddressesApiToken
     */
    @Value("${boxservice.get.autocomplete.addresses.controller.path}")
    private String autocompleteAddressesPath;
    @Value("${graphql.playground.tabs[2].variables.text}")
    private String text;
    @Value("${graphql.playground.tabs[2].variables.language}")
    private Locale language;

    /**
     * boxServiceNearestAddressesApiToken
     */
    @Value("${boxservice.get.nearest.addresses.controller.path}")
    private String nearestAddressesPath;
    @Value("${graphql.playground.tabs[3].variables.longitude}")
    private Double longitude;
    @Value("${graphql.playground.tabs[3].variables.latitude}")
    private Double latitude;
    @Value("${graphql.playground.tabs[3].variables.radiusInKilometers}")
    private Double radiusInKilometers;


    @Bean
    @ConfigurationProperties(prefix = "boxservice")
    public ApiToken boxServiceAutocompleteAddressesApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setPath(autocompleteAddressesPath);
        apiToken.setDeparture(text);
        apiToken.setLanguage(language);

        return apiToken;
    }

    @Bean
    @ConfigurationProperties(prefix = "boxservice")
    public ApiToken boxServiceNearestAddressesApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setPath(nearestAddressesPath);
        apiToken.setLanguage(language);
        apiToken.setArrivalCoordinate(new Point.PointBuilder(longitude, latitude).build());
        apiToken.setRadiusInKilometers(new Distance(radiusInKilometers, Metrics.KILOMETERS));

        return apiToken;
    }
}
