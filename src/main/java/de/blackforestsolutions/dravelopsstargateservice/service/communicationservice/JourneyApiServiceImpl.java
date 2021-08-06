package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.*;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.DateTimeParsingException;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.GeocodingService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Locale;
import java.util.stream.Collectors;

@Service
public class JourneyApiServiceImpl implements JourneyApiService {

    private static final int ZERO_WALK_STEPS = 0;
    private static final int ZERO_WAYPOINTS = 0;
    private static final String DEPARTURE_LONGITUDE_FIELD = "departureLongitude";
    private static final String DEPARTURE_LATITUDE_FIELD = "departureLatitude";
    private static final String ARRIVAL_LONGITUDE_FIELD = "arrivalLongitude";
    private static final String ARRIVAL_LATITUDE_FIELD = "arrivalLatitude";

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final GeocodingService geocodingService;
    private final ApiToken routePersistenceApiToken;

    @Autowired
    public JourneyApiServiceImpl(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, GeocodingService geocodingService, ApiToken routePersistenceApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.geocodingService = geocodingService;
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

        return backendApiService.getManyBy(apiToken, routePersistenceApiToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class)
                .map(this::convertToJourneyWithWaypoints);
    }

    @SuppressWarnings("checkstyle:parameternumber")
    private ApiToken buildRequestApiTokenWith(double departureLongitude, double departureLatitude, double arrivalLongitude, double arrivalLatitude, ZonedDateTime dateTime, boolean isArrivalDateTime, Locale language) {
        ApiToken apiToken = new ApiToken();

        apiToken.setDepartureCoordinate(geocodingService.extractCoordinateFrom(departureLongitude, departureLatitude, DEPARTURE_LONGITUDE_FIELD, DEPARTURE_LATITUDE_FIELD));
        apiToken.setArrivalCoordinate(geocodingService.extractCoordinateFrom(arrivalLongitude, arrivalLatitude, ARRIVAL_LONGITUDE_FIELD, ARRIVAL_LATITUDE_FIELD));
        apiToken.setDateTime(dateTime);
        apiToken.setIsArrivalDateTime(isArrivalDateTime);
        apiToken.setLanguage(language);

        return apiToken;
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

    private Journey convertToJourneyWithWaypoints(Journey oldJourney) {
        LinkedList<Leg> newLegs = convertToWaypointsLegs(oldJourney.getLegs());
        return new Journey.JourneyBuilder(oldJourney)
                .setLegs(newLegs)
                .build();
    }

    private LinkedList<Leg> convertToWaypointsLegs(LinkedList<Leg> oldLegs) {
        return oldLegs.stream()
                .map(this::convertToLegWithWaypoints)
                .collect(Collectors.toCollection(LinkedList::new));
    }

    private Leg convertToLegWithWaypoints(Leg oldLeg) {
        LinkedList<Point> waypoints = geocodingService.decodePolylineFrom(oldLeg.getPolyline());
        LinkedList<WalkStep> walkSteps = addEndpointToLastWalkStep(oldLeg.getWalkSteps(), waypoints);

        return new Leg.LegBuilder(oldLeg)
                .setWaypoints(waypoints)
                .setWalkSteps(walkSteps)
                .build();
    }

    private LinkedList<WalkStep> addEndpointToLastWalkStep(LinkedList<WalkStep> walkSteps, LinkedList<Point> waypoints) {
        if (walkSteps.size() > ZERO_WALK_STEPS && waypoints.size() > ZERO_WAYPOINTS && !walkSteps.getLast().isDestinationPoint()) {
            WalkStep lastWalkStep = new WalkStep.WalkStepBuilder(walkSteps.removeLast())
                    .setEndPoint(new Point.PointBuilder(waypoints.getLast().getX(), waypoints.getLast().getY()).build())
                    .build();
            walkSteps.addLast(lastWalkStep);
        }
        return walkSteps;
    }
}
