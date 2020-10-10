package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Component
public class JourneyResolver implements GraphQLQueryResolver {

    private final JourneyApiService journeyApiService;

    @Autowired
    public JourneyResolver(JourneyApiService journeyApiService) {
        this.journeyApiService = journeyApiService;
    }

    public CompletableFuture<List<Journey>> getJourneysBy(float departureLongitude, float departureLatitude, float arrivalLongitude, float arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Locale language) {
        return journeyApiService.retrieveJourneysFromApiService(departureLongitude, departureLatitude, arrivalLongitude, arrivalLatitude, dateTime, isArrivalDateTime, language)
                .toFuture();
    }

}
