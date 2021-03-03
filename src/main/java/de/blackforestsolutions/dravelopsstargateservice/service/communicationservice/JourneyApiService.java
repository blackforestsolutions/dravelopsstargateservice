package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import reactor.core.publisher.Flux;

public interface JourneyApiService {
    @SuppressWarnings("checkstyle:parameternumber")
    Flux<Journey> getJourneysBy(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, String dateTime, boolean isArrivalDateTime, String language);
}
