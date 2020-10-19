package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsExceptionHandler;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;

@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private static final String PLACEHOLDER = "placeholder";

    private final DravelOpsExceptionHandler dravelOpsExceptionHandler = new DravelOpsExceptionHandler();
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final OtpMapperApiService otpMapperApiService;
    private final ApiToken mapperServiceApiToken;

    @Autowired
    public JourneyApiServiceImpl(RequestTokenHandlerService requestTokenHandlerService, OtpMapperApiService otpMapperApiService, ApiToken mapperServiceApiToken) {
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.otpMapperApiService = otpMapperApiService;
        this.mapperServiceApiToken = mapperServiceApiToken;
    }

    @Override
    public Mono<List<Journey>> retrieveJourneysFromApiService(float departureLongitude, float departureLatitude, float arrivalLongitude, float arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Optimization optimize, Locale language) {
        return Mono.just(PLACEHOLDER)
                .map(placeholder -> requestTokenHandlerService.getRequestApiTokenWith(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude, dateTime, isArrivalDateTime, optimize, language))
                .map(userRequestToken -> requestTokenHandlerService.getRequestApiTokenWith(userRequestToken, mapperServiceApiToken))
                .flatMapMany(otpMapperApiService::getJourneysBy)
                .flatMap(dravelOpsExceptionHandler::handleExceptions)
                .collectList()
                .doOnError(dravelOpsExceptionHandler::handleExceptions);
    }

}
