package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import reactor.core.publisher.Flux;

public interface BackendApiService {
    <T> Flux<CallStatus<T>> getManyBy(ApiToken apiToken, Class<T> type);
}
