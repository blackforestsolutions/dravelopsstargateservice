package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Locale;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    @Override
    public ApiToken getRequestApiTokenWith(float departureLongitude, float departureLatitude, float arrivalLongitude, float arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Optimization optimize, Locale language) {
        return new ApiToken.ApiTokenBuilder()
                .setDepartureCoordinate(new Point(departureLongitude, departureLatitude))
                .setArrivalCoordinate(new Point(arrivalLongitude, arrivalLatitude))
                .setDateTime(dateTime)
                .setOptimize(optimize)
                .setIsArrivalDateTime(isArrivalDateTime)
                .setLanguage(language)
                .build();
    }

    @Override
    public ApiToken getRequestApiTokenWith(ApiToken userRequest, ApiToken configuredRequestData) {
        ApiToken.ApiTokenBuilder builderCopy = new ApiToken.ApiTokenBuilder(configuredRequestData);
        builderCopy.setArrival(userRequest.getArrival());
        builderCopy.setArrivalCoordinate(userRequest.getArrivalCoordinate());
        builderCopy.setDeparture(userRequest.getDeparture());
        builderCopy.setDepartureCoordinate(userRequest.getDepartureCoordinate());
        builderCopy.setDateTime(userRequest.getDateTime());
        builderCopy.setOptimize(userRequest.getOptimize());
        builderCopy.setIsArrivalDateTime(userRequest.getIsArrivalDateTime());
        builderCopy.setLanguage(userRequest.getLanguage());
        return builderCopy.build();
    }
}
