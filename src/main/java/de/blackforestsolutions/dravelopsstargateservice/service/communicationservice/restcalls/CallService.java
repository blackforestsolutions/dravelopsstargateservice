package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CallService {
    <T> Flux<T> postMany(String url, ApiToken body, HttpHeaders httpHeaders, Class<T> returnType);

    <T> Flux<T> getMany(String url, HttpHeaders httpHeaders, Class<T> returnType);

    <T> Mono<T> getOne(String url, HttpHeaders httpHeaders, Class<T> returnType);
}
