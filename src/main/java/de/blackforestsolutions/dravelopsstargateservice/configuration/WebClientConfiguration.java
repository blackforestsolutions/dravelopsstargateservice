package de.blackforestsolutions.dravelopsstargateservice.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
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
                .exchangeStrategies(ExchangeStrategies.builder()
                        .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(maxBufferSizeMb * BIT_HIGHER_UNIT * BIT_HIGHER_UNIT))
                        .build()
                )
//                .filter(logRequest())
//                .filter(logResponse())
                .build();
    }

//    private ExchangeFilterFunction logRequest() {
//        return (clientRequest, next) -> {
//            log.info("Request: {} {}", clientRequest.method(), clientRequest.url());
//            clientRequest.headers()
//                    .forEach((name, values) -> values.forEach(value -> log.info("{}={}", name, value)));
//            log.info("Body: ", clientRequest.body());
//            return next.exchange(clientRequest);
//        };
//    }
//
//    private ExchangeFilterFunction logResponse() {
//        return ExchangeFilterFunction.ofResponseProcessor(clientResponse -> {
//            log.info("Response: {}", clientResponse.headers().asHttpHeaders().get("property-header"));
//            return Mono.just(clientResponse);
//        });
//    }
}
