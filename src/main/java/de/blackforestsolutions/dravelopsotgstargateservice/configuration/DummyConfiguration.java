package de.blackforestsolutions.dravelopsotgstargateservice.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class DummyConfiguration {

    private static final int SERVER_PORT = 8080;

    @Value("${server.port}")
    private int serverPort;

    @Bean
    public boolean dummy() {
        return SERVER_PORT == serverPort;
    }
}
