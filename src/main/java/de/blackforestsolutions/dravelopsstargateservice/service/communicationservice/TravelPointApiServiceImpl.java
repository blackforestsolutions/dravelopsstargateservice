package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class TravelPointApiServiceImpl implements TravelPointApiService {

    private final ExceptionHandlerService exceptionHandlerService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final BackendApiService backendApiService;
    private final ApiToken polygonServiceApiToken;

    @Autowired
    public TravelPointApiServiceImpl(ExceptionHandlerService exceptionHandlerService, RequestTokenHandlerService requestTokenHandlerService, BackendApiService backendApiService, ApiToken polygonServiceApiToken) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.backendApiService = backendApiService;
        this.polygonServiceApiToken = polygonServiceApiToken;
    }

    @Override
    public Mono<List<TravelPoint>> retrieveJourneysFromApiService(ApiToken userRequestToken) {
        return Mono.just(userRequestToken)
                .map(token -> requestTokenHandlerService.mergeTravelPointApiTokensWith(token, polygonServiceApiToken))
                .flatMapMany(apiToken -> backendApiService.getManyBy(apiToken, TravelPoint.class))
                .flatMap(exceptionHandlerService::handleExceptions)
                .onErrorResume(exceptionHandlerService::handleExceptions)
                .collectList();
    }
}
