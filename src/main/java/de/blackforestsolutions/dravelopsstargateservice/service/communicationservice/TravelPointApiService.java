package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import reactor.core.publisher.Mono;

import java.util.List;

public interface TravelPointApiService {
    Mono<List<TravelPoint>> retrieveJourneysFromApiService(ApiToken userRequestToken);
}
