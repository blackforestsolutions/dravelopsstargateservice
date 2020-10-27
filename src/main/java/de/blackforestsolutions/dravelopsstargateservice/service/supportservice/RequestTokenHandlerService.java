package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;

public interface RequestTokenHandlerService {

    ApiToken mergeApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData);
}
