package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class PolygonConfiguration {

    @Value("${polygonservice.protocol}")
    private String polygonServiceProtocol;
    @Value("${polygonservice.host}")
    private String polygonServiceHost;
    @Value("${polygonservice.port}")
    private int polygonServicePort;
    @Value("${polygonservice.get.journey.path}")
    private String polygonServiceJourneyPath;

    @Bean(name = "polygonServiceApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(polygonServiceProtocol)
                .setHost(polygonServiceHost)
                .setPort(polygonServicePort)
                .setPath(polygonServiceJourneyPath)
                .build();
    }
}
