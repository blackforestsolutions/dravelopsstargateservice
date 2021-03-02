package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.DateTimeParsingException;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Locale;

@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final ApiToken routePersistenceApiToken;

    @Autowired
    public JourneyApiServiceImpl(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, ApiToken routePersistenceApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.routePersistenceApiToken = routePersistenceApiToken;
    }

    @Override
    @SuppressWarnings("checkstyle:parameternumber")
    public Flux<Journey> getJourneysBy(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, String dateTime, boolean isArrivalDateTime, String language) {
        ApiToken apiToken = buildRequestApiTokenWith(
                departureLongitude,
                departureLatitude,
                arrivalLongitude,
                arrivalLatitude,
                extractZonedDateTimeFrom(dateTime),
                isArrivalDateTime,
                extractLocaleFrom(language)
        );

        return backendApiService.getManyBy(apiToken, routePersistenceApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);
    }

    @SuppressWarnings("checkstyle:parameternumber")
    private ApiToken buildRequestApiTokenWith(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Locale language) {
        return new ApiToken.ApiTokenBuilder()
                .setDepartureCoordinate(new Point.PointBuilder(departureLongitude, departureLatitude).build())
                .setArrivalCoordinate(new Point.PointBuilder(arrivalLongitude, arrivalLatitude).build())
                .setDateTime(dateTime)
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
