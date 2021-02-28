package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Component
public class TravelPointResolver implements GraphQLQueryResolver {

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final ApiToken boxServiceApiToken;
    private final ApiToken stationPersistenceTravelPointApiToken;

    @Autowired
    public TravelPointResolver(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, ApiToken boxServiceApiToken, ApiToken stationPersistenceTravelPointApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.boxServiceApiToken = boxServiceApiToken;
        this.stationPersistenceTravelPointApiToken = stationPersistenceTravelPointApiToken;
    }

    public CompletableFuture<List<TravelPoint>> getAddressesBy(String text, String language) {
        ApiToken apiToken = buildRequestApiTokenWith(text, language);
        return backendApiService.getManyBy(apiToken, boxServiceApiToken, requestTokenHandlerService::mergeTravelPointApiTokensWith, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    public CompletableFuture<List<TravelPoint>> getAllStations() {
        return backendApiService.getManyBy(stationPersistenceTravelPointApiToken, TravelPoint.class)
                .collectList()
                .toFuture();
    }

    private ApiToken buildRequestApiTokenWith(String text, String language) {
        return new ApiToken.ApiTokenBuilder()
                .setDeparture(text)
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
