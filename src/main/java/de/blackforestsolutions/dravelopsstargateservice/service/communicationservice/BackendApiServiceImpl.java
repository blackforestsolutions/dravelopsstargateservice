package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
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
public class BackendApiServiceImpl implements BackendApiService {

    private final CallService callService;

    @Autowired
    public BackendApiServiceImpl(CallService callService) {
        this.callService = callService;
    }

    @Override
    public <T> Flux<CallStatus<T>> getManyBy(ApiToken apiToken, Class<T> type) {
        try {
            return executeRequestWith(apiToken, type)
                    .onErrorResume(e -> Flux.just(new CallStatus<>(null, Status.FAILED, e)));
        } catch (Exception e) {
            return Flux.just(new CallStatus<>(null, Status.FAILED, e));
        }
    }

    private <T> Flux<CallStatus<T>> executeRequestWith(ApiToken apiToken, Class<T> type) {
        return Mono.just(apiToken)
                .flatMap(token -> Mono.zip(getRequestString(token), getRequestBody(token)))
                .flatMapMany(request -> callService.post(request.getT1(), request.getT2(), HttpHeaders.EMPTY))
                .flatMap(jsonResponse -> getResponseBody(jsonResponse, type));
    }

    private Mono<String> getRequestString(ApiToken apiToken) {
        URL requestUrl = buildUrlWith(apiToken);
        return Mono.just(requestUrl.toString());
    }

    private Mono<String> getRequestBody(ApiToken apiToken) {
        DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
        return mapper.map(apiToken);
    }

    private <T> Mono<CallStatus<T>> getResponseBody(String json, Class<T> type) {
        DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
        return Mono.just(json)
                .flatMap(j -> mapper.mapJsonToPojo(j, type))
                .map(pojo -> new CallStatus<>(pojo, Status.SUCCESS, null))
                .onErrorResume(error -> Mono.just(new CallStatus<>(null, Status.FAILED, error)));
    }
}
