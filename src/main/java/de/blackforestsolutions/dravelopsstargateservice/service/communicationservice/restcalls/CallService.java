package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;

public interface CallService {
    <T> Flux<T> postMany(String url, ApiToken body, HttpHeaders httpHeaders, Class<T> returnType);
}
