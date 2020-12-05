package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.RequestHandlerFunction;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredPolygonApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getTravelPointUserRequestToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getGermanyTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TravelPointResolverTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = mock(RequestTokenHandlerServiceImpl.class);
    private final ApiToken configuredPolygonApiToken = getConfiguredPolygonApiToken();

    private final TravelPointResolver classUnderTest = new TravelPointResolver(backendApiService, requestTokenHandlerService, configuredPolygonApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(getGermanyTravelPoint(), getGermanyTravelPoint()));
    }

    @Test
    void test_getTravelPointsBy_userRequestToken_returns_travelPoints() throws ExecutionException, InterruptedException {
        ApiToken testData = getTravelPointUserRequestToken();
        TravelPoint expectedTravelPoint = getGermanyTravelPoint();

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getTravelPointsBy(testData.getDeparture(), testData.getLanguage().toString());

        assertThat(result.get().size()).isEqualTo(2);
        assertThat(toJson(result.get().get(0))).isEqualTo(toJson(expectedTravelPoint));
        assertThat(toJson(result.get().get(1))).isEqualTo(toJson(expectedTravelPoint));
    }

    @Test
    void test_getTravelPointsBy_userRequestToken_is_executed_in_right_order_and_passes_arguments_correctly() {
        ApiToken testData = getTravelPointUserRequestToken();
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredPolygonApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.getTravelPointsBy(testData.getDeparture(), testData.getLanguage().toString());


        InOrder inOrder = inOrder(backendApiService);
        inOrder.verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredPolygonApiTokenArg.capture(), any(RequestHandlerFunction.class), eq(TravelPoint.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(getTravelPointUserRequestToken());
        assertThat(configuredPolygonApiTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredPolygonApiToken());
    }

    @Test
    void test_getTravelPointsBy_userRequestToken_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        ApiToken testData = getTravelPointUserRequestToken();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class)))
                .thenReturn(Flux.empty());

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getTravelPointsBy(testData.getDeparture(), testData.getLanguage().toString());

        assertThat(result.get().size()).isEqualTo(0);
    }

    @Test
    void test_getTravelPointsByo_userRequestToken_and_wrong_language_format_throws_dateTimeParsingException() {
        String wrongLanguage = "";
        ApiToken testData = getTravelPointUserRequestToken();

        assertThrows(LanguageParsingException.class, () -> classUnderTest.getTravelPointsBy(testData.getDeparture(), wrongLanguage));
    }
}
