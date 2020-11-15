package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;


@FunctionalInterface
public interface RequestHandlerFunction {

    ApiToken merge(ApiToken userRequestToken, ApiToken serviceApiToken);
}
