package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;

import java.time.ZonedDateTime;
import java.util.Locale;

public interface RequestTokenHandlerService {
    ApiToken getRequestApiTokenWith(float departureLongitude, float departureLatitude, float arrivalLongitude, float arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Locale language);

    ApiToken getRequestApiTokenWith(ApiToken request, ApiToken configuredRequestData);
}
