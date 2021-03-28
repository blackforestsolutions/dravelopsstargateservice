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

        ApiToken autocompleteAddressToken = new ApiToken(configuredRequestData);

        autocompleteAddressToken.setDeparture(userRequest.getDeparture());
        autocompleteAddressToken.setLanguage(userRequest.getLanguage());

        return autocompleteAddressToken;
    }

    @Override
    public ApiToken mergeNearestAddressesApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getRadiusInKilometers(), "radiusInKilometers is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "language is not allowed to be null");

        ApiToken nearestAddressesToken = new ApiToken(configuredRequestData);

        nearestAddressesToken.setArrivalCoordinate(userRequest.getArrivalCoordinate());
        nearestAddressesToken.setRadiusInKilometers(userRequest.getRadiusInKilometers());
        nearestAddressesToken.setLanguage(userRequest.getLanguage());

        return nearestAddressesToken;
    }

    @Override
    public ApiToken mergeJourneyApiTokensWith(ApiToken userRequest, ApiToken configuredRequestData) {
        Objects.requireNonNull(userRequest.getArrivalCoordinate(), "arrivalCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDepartureCoordinate(), "departureCoordinate is not allowed to be null");
        Objects.requireNonNull(userRequest.getDateTime(), "dateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getIsArrivalDateTime(), "isArrivalDateTime is not allowed to be null");
        Objects.requireNonNull(userRequest.getLanguage(), "departureCoordinate is not allowed to be null");

        ApiToken journeyApiToken = new ApiToken(configuredRequestData);

        journeyApiToken.setArrivalCoordinate(userRequest.getArrivalCoordinate());
        journeyApiToken.setDepartureCoordinate(userRequest.getDepartureCoordinate());
        journeyApiToken.setDateTime(userRequest.getDateTime());
        journeyApiToken.setIsArrivalDateTime(userRequest.getIsArrivalDateTime());
        journeyApiToken.setLanguage(userRequest.getLanguage());

        return journeyApiToken;
    }
}
