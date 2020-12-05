package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import reactor.core.publisher.Flux;

public interface BackendApiService {
    <T> Flux<T> getManyBy(ApiToken userRequestToken, ApiToken serviceApiToken, RequestHandlerFunction requestHandlerFunction, Class<T> returnType);
}
