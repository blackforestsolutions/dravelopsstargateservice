package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;

public interface RequestTokenHandlerService {
    ApiToken mergeTravelPointApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);

    ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);
}
