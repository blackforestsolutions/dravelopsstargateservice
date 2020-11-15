package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.http.codec.json.Jackson2JsonDecoder;
import org.springframework.http.codec.json.Jackson2JsonEncoder;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@Slf4j
@SpringBootConfiguration
public class WebClientConfiguration {

    private static final int BIT_HIGHER_UNIT = 1024;

    @Value("${webclient.maxBufferSizeMb}")
    private int maxBufferSizeMb;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .exchangeStrategies(exchangeStrategies())
                .build();
    }

    private ExchangeStrategies exchangeStrategies() {
        return ExchangeStrategies.builder()
                .codecs(configurer -> {
                    configurer.defaultCodecs().maxInMemorySize(maxBufferSizeMb * BIT_HIGHER_UNIT * BIT_HIGHER_UNIT);
                    configurer.defaultCodecs().jackson2JsonEncoder(new Jackson2JsonEncoder(new DravelOpsJsonMapper()));
                    configurer.defaultCodecs().jackson2JsonDecoder(new Jackson2JsonDecoder(new DravelOpsJsonMapper()));
                })
                .build();
    }
}
