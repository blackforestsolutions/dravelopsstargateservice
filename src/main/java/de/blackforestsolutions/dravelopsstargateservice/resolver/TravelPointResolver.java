package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

@Component
public class TravelPointResolver implements GraphQLQueryResolver {

    private final BackendApiService backendApiService;
    private final RequestTokenHandlerService requestTokenHandlerService;
    private final ApiToken polygonApiToken;

    @Autowired
    public TravelPointResolver(BackendApiService backendApiService, RequestTokenHandlerService requestTokenHandlerService, ApiToken polygonApiToken) {
        this.backendApiService = backendApiService;
        this.requestTokenHandlerService = requestTokenHandlerService;
        this.polygonApiToken = polygonApiToken;
    }

    public CompletableFuture<List<TravelPoint>> getTravelPointsBy(String text, String language) {
        ApiToken apiToken = buildRequestApiTokenWith(text, language);
        return backendApiService.getManyBy(apiToken, polygonApiToken, requestTokenHandlerService::mergeTravelPointApiTokensWith, TravelPoint.class)
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
        Locale locale = Locale.forLanguageTag(language);
        if (locale.toString().length() != 2) {
            throw new LanguageParsingException();
        }
        return locale;
    }
}
