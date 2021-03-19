package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.GeocodingService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Component
public class TravelPointResolver implements GraphQLQueryResolver {

    private static final String LONGITUDE_FIELD = "longitude";
    private static final String LATITUDE_FIELD = "latitude";

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final GeocodingService geocodingService;
    private final ApiToken autocompleteAddressesBoxServiceApiToken;
    private final ApiToken nearestAddressesBoxServiceApiToken;
    private final ApiToken stationPersistenceTravelPointApiToken;
    private final ApiToken nearestStationsOtpMapperServiceApiToken;

    @Autowired
    public TravelPointResolver(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, GeocodingService geocodingService, ApiToken autocompleteAddressesBoxServiceApiToken, ApiToken nearestAddressesBoxServiceApiToken, ApiToken stationPersistenceTravelPointApiToken, ApiToken nearestStationsOtpMapperServiceApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.geocodingService = geocodingService;
        this.autocompleteAddressesBoxServiceApiToken = autocompleteAddressesBoxServiceApiToken;
        this.nearestAddressesBoxServiceApiToken = nearestAddressesBoxServiceApiToken;
        this.stationPersistenceTravelPointApiToken = stationPersistenceTravelPointApiToken;
        this.nearestStationsOtpMapperServiceApiToken = nearestStationsOtpMapperServiceApiToken;
    }

    public CompletableFuture<List<TravelPoint>> getAutocompleteAddressesBy(String text, String language) {
        ApiToken apiToken = buildAutocompleteRequestApiTokenWith(text, language);
        return backendApiService.getManyBy(apiToken, autocompleteAddressesBoxServiceApiToken, requestTokenHandlerService::mergeAutocompleteAddressesApiTokensWith, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    public CompletableFuture<List<TravelPoint>> getNearestAddressesBy(double longitude, double latitude, double radiusInKilometers, String language) {
        ApiToken apiToken = buildNearestAddressesRequestApiTokenWith(longitude, latitude, radiusInKilometers, language);
        return backendApiService.getManyBy(apiToken, nearestAddressesBoxServiceApiToken, requestTokenHandlerService::mergeNearestAddressesApiTokensWith, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    public CompletableFuture<List<TravelPoint>> getNearestStationsBy(double longitude, double latitude, double radiusInKilometers, String language) {
        ApiToken apiToken = buildNearestAddressesRequestApiTokenWith(longitude, latitude, radiusInKilometers, language);
        return backendApiService.getManyBy(apiToken, nearestStationsOtpMapperServiceApiToken, requestTokenHandlerService::mergeNearestAddressesApiTokensWith, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    public CompletableFuture<List<TravelPoint>> getAllStations() {
        return backendApiService.getManyBy(stationPersistenceTravelPointApiToken, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    private ApiToken buildAutocompleteRequestApiTokenWith(String text, String language) {
        return new ApiToken.ApiTokenBuilder()
                .setDeparture(text)
                .setLanguage(extractLocaleFrom(language))
                .build();
    }

    private ApiToken buildNearestAddressesRequestApiTokenWith(double longitude, double latitude, double radiusInKilometers, String language) {
        return new ApiToken.ApiTokenBuilder()
                .setArrivalCoordinate(geocodingService.extractCoordinateFrom(longitude, latitude, LONGITUDE_FIELD, LATITUDE_FIELD))
                .setRadiusInKilometers(new Distance(radiusInKilometers, Metrics.KILOMETERS))
                .setLanguage(extractLocaleFrom(language))
                .build();
    }

    private Locale extractLocaleFrom(String language) {
        return Arrays.stream(Locale.getISOLanguages())
                .filter(isoLanguage -> isoLanguage.equals(language))
                .findFirst()
                .map(Locale::new)
                .orElseThrow(LanguageParsingException::new);
    }

}
