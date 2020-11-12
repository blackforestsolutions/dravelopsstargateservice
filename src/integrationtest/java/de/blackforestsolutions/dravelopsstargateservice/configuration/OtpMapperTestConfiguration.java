package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.Locale;

@Import(ZonedDateTimeConfiguration.class)
@TestConfiguration
public class OtpMapperTestConfiguration {

//    @Value("${otpmapper.get.journey.path}")
//    private String path;
    @Value("${test.apitokens[0].optimize}")
    private Optimization optimize;
    @Value("${test.apitokens[0].isArrivalDateTime}")
    private boolean isArrivalDateTime;
    @Value("${test.apitokens[0].dateTime}")
    private String dateTime;
    @Value("${test.apitokens[0].departureCoordinateLongitude}")
    private Double departureCoordinateLongitude;
    @Value("${test.apitokens[0].departureCoordinateLatitude}")
    private Double departureCoordinateLatitude;
    @Value("${test.apitokens[0].arrivalCoordinateLongitude}")
    private Double arrivalCoordinateLongitude;
    @Value("${test.apitokens[0].arrivalCoordinateLatitude}")
    private Double arrivalCoordinateLatitude;
    @Value("${test.apitokens[0].language}")
    private String language;

    @Bean(name = "otpMapperApiToken")
    @ConfigurationProperties(prefix = "otpmapper")
    public ApiToken.ApiTokenBuilder apiToken() {
        return new ApiToken.ApiTokenBuilder()
//                .setPath(path)
                .setOptimize(optimize)
                .setIsArrivalDateTime(isArrivalDateTime)
                .setDateTime(ZonedDateTime.parse(dateTime))
                .setDepartureCoordinate(new Point(departureCoordinateLongitude, departureCoordinateLatitude))
                .setArrivalCoordinate(new Point(arrivalCoordinateLongitude, arrivalCoordinateLatitude))
                .setLanguage(Locale.forLanguageTag(language));
    }

}
