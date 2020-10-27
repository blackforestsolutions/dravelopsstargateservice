package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

@Service
public class CallServiceImpl implements CallService {

    private final WebClient webClient;

    @Autowired
    public CallServiceImpl(WebClient webClient) {
        this.webClient = webClient;
    }

    @Override
    public Flux<String> post(String url, String body, HttpHeaders httpHeaders) {
        return webClient
                .post()
                .uri(url)
                .bodyValue(body)
                .headers(headers -> httpHeaders.forEach(headers::addAll))
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
    }
}
