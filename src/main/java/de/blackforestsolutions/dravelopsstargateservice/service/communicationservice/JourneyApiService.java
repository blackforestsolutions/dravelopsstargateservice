package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import reactor.core.publisher.Mono;

import java.util.List;

public interface JourneyApiService {
    Mono<List<Journey>> retrieveJourneysFromApiService(ApiToken userRequestToken);
}
