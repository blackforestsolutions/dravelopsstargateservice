package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;

public interface RequestTokenHandlerService {
    ApiToken mergeAutocompleteAddressesApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);

    ApiToken mergeNearestAddressesApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);

    ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);
}
