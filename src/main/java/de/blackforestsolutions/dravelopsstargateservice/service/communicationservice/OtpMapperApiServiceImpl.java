package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Status;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URL;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;

@Service
public class OtpMapperApiServiceImpl implements OtpMapperApiService {

    private final CallService callService;

    @Autowired
    public OtpMapperApiServiceImpl(CallService callService) {
        this.callService = callService;
    }

    @Override
    public Flux<CallStatus<Journey>> getJourneysBy(ApiToken apiToken) {
        try {
            return executeJourneyRequestWith(apiToken)
                    .onErrorResume(e -> Flux.just(new CallStatus<>(null, Status.FAILED, e)));
        } catch (Exception e) {
            return Flux.just(new CallStatus<>(null, Status.FAILED, e));
        }
    }

    private Flux<CallStatus<Journey>> executeJourneyRequestWith(ApiToken apiToken) {
        return Mono.just(apiToken)
                .flatMap(token -> Mono.zip(
                            getJourneyRequestString(token),
                            getJourneyRequestBody(token)
                        )
                )
                .flatMapMany(request -> callService.post(request.getT1(), request.getT2(), HttpHeaders.EMPTY))
                .flatMap(this::getJourneyResponseBody);
    }

    private Mono<String> getJourneyRequestString(ApiToken apiToken) {
        URL requestUrl = buildUrlWith(apiToken);
        return Mono.just(requestUrl.toString());
    }

    private Mono<String> getJourneyRequestBody(ApiToken apiToken) {
        DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
        return mapper.map(apiToken);
    }

    private Mono<CallStatus<Journey>> getJourneyResponseBody(String json) {
        DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
        return Mono.just(json)
                .flatMap(journeyJson -> mapper.mapJsonToPojo(journeyJson, Journey.class))
                .map(journey -> new CallStatus<>(journey, Status.SUCCESS, null))
                .doOnError(error -> new CallStatus<>(null, Status.FAILED, error));
    }
}
