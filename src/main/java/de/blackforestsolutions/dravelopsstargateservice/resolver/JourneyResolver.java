package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class JourneyResolver implements GraphQLQueryResolver {

    private final JourneyApiService journeyApiService;

    @Autowired
    public JourneyResolver(JourneyApiService journeyApiService) {
        this.journeyApiService = journeyApiService;
    }

    @SuppressWarnings("checkstyle:parameternumber")
    public CompletableFuture<List<Journey>> getJourneysBy(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, String dateTime, boolean isArrivalDateTime, String language) {
        return journeyApiService.getJourneysBy(
                departureLongitude,
                departureLatitude,
                arrivalLongitude,
                arrivalLatitude,
                dateTime,
                isArrivalDateTime,
                language
        ).collectList().toFuture();
    }

}
