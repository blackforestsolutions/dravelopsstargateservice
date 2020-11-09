package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@TestConfiguration
public class PolygonTestConfiguration {

    @Value("${polygon.protocol}")
    private String protocol;
    @Value("${polygon.host}")
    private String host;
    @Value("${polygon.port}")
    private int port;
    @Value("${polygon.get.journey.path}")
    private String path;
    @Value("${test.apitokens.polygon.text}")
    private String departure;
    @Value("${test.apitokens.polygon.language}")
    private String language;

    @Bean(name = "danke")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(protocol)
                .setHost(host)
                .setPort(port)
                .setPath(path)
                .setDeparture(departure)
                .setLanguage(Locale.forLanguageTag(language))
                .build();
    }
}
