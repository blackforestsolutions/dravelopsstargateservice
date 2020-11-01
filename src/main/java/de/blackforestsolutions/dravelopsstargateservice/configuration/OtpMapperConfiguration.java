package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class OtpMapperConfiguration {

    @Value("${mapperservice.protocol}")
    private String mapperServiceProtocol;
    @Value("${mapperservice.host}")
    private String mapperServiceHost;
    @Value("${mapperservice.port}")
    private int mapperServicePort;
    @Value("${mapperservice.get.journey.path}")
    private String mapperServiceJourneyPath;

    @Bean(name = "mapperServiceApiToken")
    public ApiToken apiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(mapperServiceProtocol)
                .setHost(mapperServiceHost)
                .setPort(mapperServicePort)
                .setPath(mapperServiceJourneyPath)
                .build();
    }


}
