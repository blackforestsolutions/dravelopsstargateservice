package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;


@FunctionalInterface
public interface RequestHandlerFunction {

    ApiToken merge(ApiToken userRequestToken, ApiToken serviceApiToken);
}
