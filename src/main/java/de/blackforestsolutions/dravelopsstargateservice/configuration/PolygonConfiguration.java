package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class PolygonConfiguration {

    @Value("${polygon.protocol}")
    private String polygonProtocol;
    @Value("${polygon.host}")
    private String polygonHost;
    @Value("${polygon.port}")
    private int polygonPort;
    @Value("${polygon.get.journey.path}")
    private String polygonJourneyPath;

    @Bean(name = "polygonApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(polygonProtocol)
                .setHost(polygonHost)
                .setPort(polygonPort)
                .setPath(polygonJourneyPath)
                .build();
    }
}
