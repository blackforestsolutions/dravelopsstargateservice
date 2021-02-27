package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class BoxServiceConfiguration {

    @Value("${boxservice.protocol}")
    private String boxServiceProtocol;
    @Value("${boxservice.host}")
    private String boxServiceHost;
    @Value("${boxservice.port}")
    private int boxServicePort;
    @Value("${boxservice.get.travelpoint.path}")
    private String boxServiceTravelPointPath;

    @Bean(name = "boxServiceApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(boxServiceProtocol)
                .setHost(boxServiceHost)
                .setPort(boxServicePort)
                .setPath(boxServiceTravelPointPath)
                .build();
    }
}
