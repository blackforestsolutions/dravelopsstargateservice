package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    @Override
    public ApiToken mergeAutocompleteAddressesApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getDeparture(), "departure (text) is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "language is not allowed to be null");

        return new ApiToken.ApiTokenBuilder(configuredRequestData)
                .setDeparture(userRequest.getDeparture())
                .setLanguage(userRequest.getLanguage())
                .build();
    }

    @Override
    public ApiToken mergeNearestAddressesApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getRadiusInKilometers(), "radiusInKilometers is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "language is not allowed to be null");

        return new ApiToken.ApiTokenBuilder(configuredRequestData)
                .setArrivalCoordinate(userRequest.getArrivalCoordinate())
                .setRadiusInKilometers(userRequest.getRadiusInKilometers())
                .setLanguage(userRequest.getLanguage())
                .build();
    }

    @Override
    public ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDepartureCoordinate(), "departureCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDateTime(), "dateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getIsArrivalDateTime(), "isArrivalDateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "departureCoordinate is not allowed to be null");

        return new ApiToken.ApiTokenBuilder(configuredRequestData)
                .setArrivalCoordinate(userRequest.getArrivalCoordinate())
                .setDepartureCoordinate(userRequest.getDepartureCoordinate())
                .setDateTime(userRequest.getDateTime())
                .setIsArrivalDateTime(userRequest.getIsArrivalDateTime())
                .setLanguage(userRequest.getLanguage())
                .build();
    }
}
