package de.blackforestsolutions.dravelopsstargateservice.subscription;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiService;
import graphql.kickstart.tools.GraphQLSubscriptionResolver;
import org.reactivestreams.Publisher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class JourneySubscription implements GraphQLSubscriptionResolver {

    private final JourneyApiService journeyApiService;

    @Autowired
    public JourneySubscription(JourneyApiService journeyApiService) {
        this.journeyApiService = journeyApiService;
    }

    @SuppressWarnings("checkstyle:parameternumber")
    public Publisher<Journey> getJourneysBy(
            double departureLongitude,
            double departureLatitude,
            double arrivalLongitude,
            double arrivalLatitude,
            String dateTime,
            boolean isArrivalDateTime,
            String language
    ) {
        return journeyApiService.getJourneysBy(
                departureLongitude,
                departureLatitude,
                arrivalLongitude,
                arrivalLatitude,
                dateTime,
                isArrivalDateTime,
                language
        );
    }
}
