package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;

@RefreshScope
@SpringBootConfiguration
public class BoxServiceConfiguration {

    @Value("${boxservice.protocol}")
    private String boxServiceProtocol;
    @Value("${boxservice.host}")
    private String boxServiceHost;
    @Value("${boxservice.port}")
    private int boxServicePort;
    @Value("${boxservice.get.autocomplete.addresses.controller.path}")
    private String boxServiceAutocompleteAddressesControllerPath;
    @Value("${boxservice.get.nearest.addresses.controller.path}")
    private String boxServiceNearestAddressesControllerPath;

    @RefreshScope
    @Bean
    public ApiToken autocompleteAddressesBoxServiceApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setProtocol(boxServiceProtocol);
        apiToken.setHost(boxServiceHost);
        apiToken.setPort(boxServicePort);
        apiToken.setPath(boxServiceAutocompleteAddressesControllerPath);

        return apiToken;
    }

    @RefreshScope
    @Bean
    public ApiToken nearestAddressesBoxServiceApiToken() {
        ApiToken apiToken = new ApiToken();

        apiToken.setProtocol(boxServiceProtocol);
        apiToken.setHost(boxServiceHost);
        apiToken.setPort(boxServicePort);
        apiToken.setPath(boxServiceNearestAddressesControllerPath);

        return apiToken;
    }
}
