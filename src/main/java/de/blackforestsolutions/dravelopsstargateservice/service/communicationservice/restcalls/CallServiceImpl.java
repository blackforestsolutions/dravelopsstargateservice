package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CallServiceImpl implements CallService {

    private final WebClient webClient;

    @Autowired
    public CallServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public <T> Flux<T> postMany(String url, ApiToken body, HttpHeaders httpHeaders, Class<T> returnType) {
        return webClient
                .post()
                .uri(url)
                .body(Mono.just(body), ApiToken.class)
                .headers(headers -> httpHeaders.forEach(headers::addAll))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(returnType);
    }
}
