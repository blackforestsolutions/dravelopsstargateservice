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
    @Value("${boxservice.autocomplete.addresses.controller.path}")
    private String boxServiceAutocompleteAddressesControllerPath;
    @Value("${boxservice.nearest.addresses.controller.path}")
    private String boxServiceNearestAddressesControllerPath;

    @Bean
    public ApiToken autocompleteAddressesBoxServiceApiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(boxServiceProtocol)
                .setHost(boxServiceHost)
                .setPort(boxServicePort)
                .setPath(boxServiceAutocompleteAddressesControllerPath)
                .build();
    }

    @Bean
    public ApiToken nearestAddressesBoxServiceApiToken() {
        return new ApiToken.ApiTokenBuilder()
                .setProtocol(boxServiceProtocol)
                .setHost(boxServiceHost)
                .setPort(boxServicePort)
                .setPath(boxServiceNearestAddressesControllerPath)
                .build();
    }
}
