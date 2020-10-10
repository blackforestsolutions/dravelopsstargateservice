package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.Locale;

@Service
public class RequestTokenHandlerServiceImpl implements RequestTokenHandlerService {

    @Override
    public ApiToken getRequestApiTokenWith(float departureLongitude, float departureLatitude, float arrivalLongitude, float arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Locale language) {
        return new ApiToken.ApiTokenBuilder()
                .setDepartureCoordinate(new Point(departureLongitude, departureLatitude))
                .setArrivalCoordinate(new Point(arrivalLongitude, arrivalLatitude))
                .setDateTime(dateTime)
                .setIsArrivalDateTime(isArrivalDateTime)
                .setLanguage(language)
                .build();
    }

    @Override
    public ApiToken getRequestApiTokenWith(ApiToken request, ApiToken configuredRequestData) {
        ApiToken.ApiTokenBuilder builderCopy = new ApiToken.ApiTokenBuilder(configuredRequestData);
        builderCopy.setArrival(request.getArrival());
        builderCopy.setArrivalCoordinate(request.getArrivalCoordinate());
        builderCopy.setDeparture(request.getDeparture());
        builderCopy.setDepartureCoordinate(request.getDepartureCoordinate());
        builderCopy.setDateTime(request.getDateTime());
        builderCopy.setOptimize(request.getOptimize());
        builderCopy.setIsArrivalDateTime(request.getIsArrivalDateTime());
        builderCopy.setLanguage(request.getLanguage());
        return builderCopy.build();
    }
}
