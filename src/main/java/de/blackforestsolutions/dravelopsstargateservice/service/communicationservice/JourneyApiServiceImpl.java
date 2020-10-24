package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private final ExceptionHandlerService exceptionHandlerService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final OtpMapperApiService otpMapperApiService;
    private final ApiToken mapperServiceApiToken;

    @Autowired
    public JourneyApiServiceImpl(ExceptionHandlerService exceptionHandlerService, RequestTokenHandlerService requestTokenHandlerService, OtpMapperApiService otpMapperApiService, ApiToken mapperServiceApiToken) {
        this.exceptionHandlerService = exceptionHandlerService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.otpMapperApiService = otpMapperApiService;
        this.mapperServiceApiToken = mapperServiceApiToken;
    }

    @Override
    public Mono<List<Journey>> retrieveJourneysFromApiService(ApiToken userRequestToken) {
        return Mono.just(userRequestToken)
                .map(token -> requestTokenHandlerService.mergeApiTokensWith(token, mapperServiceApiToken))
                .flatMapMany(otpMapperApiService::getJourneysBy)
                .flatMap(exceptionHandlerService::handleExceptions)
                .onErrorResume(exceptionHandlerService::handleExceptions)
                .collectList();
    }

}
