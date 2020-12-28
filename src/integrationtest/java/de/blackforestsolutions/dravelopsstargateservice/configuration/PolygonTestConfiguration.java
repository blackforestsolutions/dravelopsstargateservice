package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Locale;

@Import(ZonedDateTimeConfiguration.class)
@TestConfiguration
public class PolygonTestConfiguration {

    @Value("${polygon.get.journey.path}")
    private String path;
    @Value("${test.apitokens[0].text}")
    private String departure;
    @Value("${test.apitokens[0].language}")
    private String language;

    @Bean(name = "polygonApiTokenIT")
    @ConfigurationProperties(prefix = "polygon")
    public ApiToken.ApiTokenBuilder apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setPath(path)
                .setDeparture(departure)
                .setLanguage(Locale.forLanguageTag(language));
    }
}
