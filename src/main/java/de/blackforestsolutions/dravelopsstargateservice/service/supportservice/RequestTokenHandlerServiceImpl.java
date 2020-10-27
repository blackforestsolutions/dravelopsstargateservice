package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    @Override
    public ApiToken mergeApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDepartureCoordinate(), "departureCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDateTime(), "dateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getOptimize(), "optimize is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "departureCoordinate is not allowed to be null");

        ApiToken.ApiTokenBuilder builderCopy = new ApiToken.ApiTokenBuilder(configuredRequestData);
        builderCopy.setArrivalCoordinate(userRequest.getArrivalCoordinate());
        builderCopy.setDepartureCoordinate(userRequest.getDepartureCoordinate());
        builderCopy.setDateTime(userRequest.getDateTime());
        builderCopy.setOptimize(userRequest.getOptimize());
        builderCopy.setIsArrivalDateTime(userRequest.getIsArrivalDateTime());
        builderCopy.setLanguage(userRequest.getLanguage());
        return builderCopy.build();
    }
}
