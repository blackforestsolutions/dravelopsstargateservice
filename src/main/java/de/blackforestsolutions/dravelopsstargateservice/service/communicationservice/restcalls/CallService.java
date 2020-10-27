package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls;

import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

public interface CallService {
    Flux<String> post(String url, String body, HttpHeaders request);
}
