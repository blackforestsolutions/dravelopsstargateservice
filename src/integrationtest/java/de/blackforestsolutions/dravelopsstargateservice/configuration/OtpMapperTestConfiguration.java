package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Point;

import java.time.ZonedDateTime;
import java.util.Locale;

@TestConfiguration
public class OtpMapperTestConfiguration {

    @Value("${otpmapper.protocol}")
    private String protocol;
    @Value("${otpmapper.host}")
    private String host;
    @Value("${otpmapper.port}")
    private int port;
    @Value("${otpmapper.get.journey.path}")
    private String path;
    @Value("${test.apitokens.otpmapper.optimize}")
    private Optimization optimize;
    @Value("${test.apitokens.otpmapper.isArrivalDateTime}")
    private boolean isArrivalDateTime;
    @Value("${test.apitokens.otpmapper.dateTime}")
    private String dateTime;
    @Value("${test.apitokens.otpmapper.departureCoordinateLongitude}")
    private Double departureCoordinateLongitude;
    @Value("${test.apitokens.otpmapper.departureCoordinateLatitude}")
    private Double departureCoordinateLatitude;
    @Value("${test.apitokens.otpmapper.arrivalCoordinateLongitude}")
    private Double arrivalCoordinateLongitude;
    @Value("${test.apitokens.otpmapper.arrivalCoordinateLatitude}")
    private Double arrivalCoordinateLatitude;
    @Value("${test.apitokens.otpmapper.language}")
    private String language;

    @Bean(name = "otpMapperApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setPath(path)
                .setOptimize(Optimization.QUICK)
                .setIsArrivalDateTime(isArrivalDateTime)
                .setDateTime(ZonedDateTime.parse(dateTime))
                .setDepartureCoordinate(new Point(departureCoordinateLongitude, departureCoordinateLatitude))
                .setArrivalCoordinate(new Point(arrivalCoordinateLongitude, arrivalCoordinateLatitude))
                .setLanguage(Locale.forLanguageTag(language))
                .build();
    }
}
