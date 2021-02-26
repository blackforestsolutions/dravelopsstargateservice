package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface BackendApiService {
    <T> Flux<T> getManyBy(ApiToken userRequestToken, ApiToken serviceApiToken, RequestHandlerFunction requestHandlerFunction, Class<T> returnType);

    <T> Flux<T> getManyBy(ApiToken serviceApiToken, Class<T> returnType);

    <T> Mono<T> getOneBy(ApiToken serviceApiToken, Class<T> returnType);
}
