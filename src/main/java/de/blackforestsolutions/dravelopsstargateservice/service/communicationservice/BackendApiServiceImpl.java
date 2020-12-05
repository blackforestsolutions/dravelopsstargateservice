package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;

@Service
public class BackendApiServiceImpl implements BackendApiService {

    private final CallService callService;
    private final ExceptionHandlerService exceptionHandlerService;

    public BackendApiServiceImpl(CallService callService, ExceptionHandlerService exceptionHandlerService) {
        this.callService = callService;
        this.exceptionHandlerService = exceptionHandlerService;
    }

    @Override
    public <T> Flux<T> getManyBy(ApiToken userRequestToken, ApiToken serviceApiToken, RequestHandlerFunction requestHandlerFunction, Class<T> returnType) {
        try {
            return executeRequestWith(userRequestToken, serviceApiToken, requestHandlerFunction, returnType)
                    .onErrorResume(exceptionHandlerService::handleExceptions);
        } catch (Exception e) {
            return exceptionHandlerService.handleExceptions(e);
        }
    }

    private <T> Flux<T> executeRequestWith(ApiToken userRequestToken, ApiToken serviceApiToken, RequestHandlerFunction requestHandlerFunction, Class<T> returnType) {
        return Mono.just(requestHandlerFunction)
                .map(handlerFunction -> handlerFunction.merge(userRequestToken, serviceApiToken))
                .flatMap(this::getRequestString)
                .flatMapMany(url -> callService.postMany(url, userRequestToken, HttpHeaders.EMPTY, returnType));
    }

    private Mono<String> getRequestString(ApiToken apiToken) {
        URL requestUrl = buildUrlWith(apiToken);
        return Mono.just(requestUrl.toString());
    }
}
