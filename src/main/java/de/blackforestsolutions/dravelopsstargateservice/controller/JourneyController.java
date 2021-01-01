package de.blackforestsolutions.dravelopsstargateservice.controller;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("otp/journeys")
public class JourneyController {

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final ApiToken routePersistenceApiToken;

    @Autowired
    public JourneyController(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, ApiToken routePersistenceApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.routePersistenceApiToken = routePersistenceApiToken;
    }

    @RequestMapping(value = "/get", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Journey> getJourneysBy(@RequestBody ApiToken userRequestToken) {
        return backendApiService.getManyBy(
                userRequestToken,
                routePersistenceApiToken,
                requestTokenHandlerService::mergeJourneyApiTokensWith,
                Journey.class
        );
    }
}
