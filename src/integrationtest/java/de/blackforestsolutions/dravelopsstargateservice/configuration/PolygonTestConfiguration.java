package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import java.util.Locale;

@Import(ZonedDateTimeConfiguration.class)
@TestConfiguration
public class PolygonTestConfiguration {

    @Value("${test.apitokens[0].text}")
    private String departure;
    @Value("${test.apitokens[0].language}")
    private Locale language;

    @Bean(name = "polygonApiTokenIT")
    @ConfigurationProperties(prefix = "polygon")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setDeparture(departure)
                .setLanguage(language)
                .build();
    }
}
