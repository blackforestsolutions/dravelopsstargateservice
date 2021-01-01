package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.DateTimeParsingException;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Point;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Component
public class JourneyResolver implements GraphQLQueryResolver {

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final ApiToken routePersistenceApiToken;

    @Autowired
    public JourneyResolver(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, ApiToken routePersistenceApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.routePersistenceApiToken = routePersistenceApiToken;
    }

    @SuppressWarnings("checkstyle:parameternumber")
    public CompletableFuture<List<Journey>> getJourneysBy(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, String dateTime, boolean isArrivalDateTime, Optimization optimize, String language) {
        ApiToken apiToken = buildRequestApiTokenWith(
                departureLongitude,
                departureLatitude,
                arrivalLongitude,
                arrivalLatitude,
                extractZonedDateTimeFrom(dateTime),
                isArrivalDateTime,
                optimize,
                extractLocaleFrom(language)
        );

        return backendApiService.getManyBy(apiToken, routePersistenceApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class)
                .collectList()
                .toFuture();
    }

    @SuppressWarnings("checkstyle:parameternumber")
    private ApiToken buildRequestApiTokenWith(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Optimization optimize, Locale language) {
        return new ApiToken.ApiTokenBuilder()
                .setDepartureCoordinate(new Point(departureLongitude, departureLatitude))
                .setArrivalCoordinate(new Point(arrivalLongitude, arrivalLatitude))
                .setDateTime(dateTime)
                .setOptimize(optimize)
                .setIsArrivalDateTime(isArrivalDateTime)
                .setLanguage(language)
                .build();
    }

    private ZonedDateTime extractZonedDateTimeFrom(String dateTime) {
        try {
            return ZonedDateTime.parse(dateTime);
        } catch (Exception e) {
            throw new DateTimeParsingException();
        }
    }

    private Locale extractLocaleFrom(String language) {
        return Arrays.stream(Locale.getISOLanguages())
                .filter(isoLanguage -> isoLanguage.equals(language))
                .findFirst()
                .map(Locale::new)
                .orElseThrow(LanguageParsingException::new);
    }

}
