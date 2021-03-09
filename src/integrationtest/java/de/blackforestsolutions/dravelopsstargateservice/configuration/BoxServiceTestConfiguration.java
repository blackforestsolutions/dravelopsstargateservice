package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;

import java.util.Locale;

@Import(ConverterConfiguration.class)
@TestConfiguration
public class BoxServiceTestConfiguration {

    @Value("${boxservice.get.autocomplete.addresses.controller.path}")
    private String autocompleteAddressesPath;
    @Value("${boxservice.get.nearest.addresses.controller.path}")
    private String nearestAddressesPath;
    @Value("${test.apitokens[0].text}")
    private String departure;
    @Value("${test.apitokens[0].language}")
    private Locale language;
    @Value("${test.apitokens[0].arrivalCoordinateLongitude}")
    private Double arrivalCoordinateLongitude;
    @Value("${test.apitokens[0].arrivalCoordinateLatitude}")
    private Double arrivalCoordinateLatitude;
    @Value("${test.apitokens[0].radiusInKilometers}")
    private Double radiusInKilometers;

    @Bean
    @ConfigurationProperties(prefix = "boxservice")
    public ApiToken.ApiTokenBuilder boxServiceAutocompleteAddressesApiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(autocompleteAddressesPath)
                .setDeparture(departure)
                .setLanguage(language);
    }

    @Bean
    @ConfigurationProperties(prefix = "boxservice")
    public ApiToken.ApiTokenBuilder boxServiceNearestAddressesApiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(nearestAddressesPath)
                .setArrivalCoordinate(new Point.PointBuilder(arrivalCoordinateLongitude, arrivalCoordinateLatitude).build())
                .setRadiusInKilometers(new Distance(radiusInKilometers, Metrics.KILOMETERS))
                .setLanguage(language);
    }
}
