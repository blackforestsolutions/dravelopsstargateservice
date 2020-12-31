package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.DateTimeParsingException;
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
import org.springframework.data.geo.Point;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JourneyResolverTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = mock(RequestTokenHandlerServiceImpl.class);
    private final ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

    private final JourneyResolver classUnderTest = new JourneyResolver(backendApiService, requestTokenHandlerService, configuredRoutePersistenceApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.just(getFurtwangenToWaldkirchJourney(), getFurtwangenToWaldkirchJourney()));
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_journey() throws ExecutionException, InterruptedException {
        ApiToken testData = getJourneyUserRequestToken();
        Journey expectedJourney = getFurtwangenToWaldkirchJourney();

        CompletableFuture<List<Journey>> result = classUnderTest.getJourneysBy(
                testData.getDepartureCoordinate().getX(),
                testData.getDepartureCoordinate().getY(),
                testData.getArrivalCoordinate().getX(),
                testData.getArrivalCoordinate().getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getOptimize(),
                testData.getLanguage().toString()
        );

        assertThat(result.get().size()).isEqualTo(2);
        assertThat(toJson(result.get().get(0))).isEqualTo(toJson(expectedJourney));
        assertThat(toJson(result.get().get(1))).isEqualTo(toJson(expectedJourney));
    }

    @Test
    void test_getJourneysBy_userRequestToken_is_executed_in_right_order_and_passes_arguments_correctly() {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredRoutePersistenceApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getOptimize(),
                testData.getLanguage().toString()
        );

        InOrder inOrder = inOrder(backendApiService);
        inOrder.verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredRoutePersistenceApiTokenArg.capture(), any(RequestHandlerFunction.class), eq(Journey.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(getJourneyUserRequestToken());
        assertThat(configuredRoutePersistenceApiTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredRoutePersistenceApiToken());
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        CompletableFuture<List<Journey>> result = classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getOptimize(),
                testData.getLanguage().toString()
        );

        assertThat(result.get().size()).isEqualTo(0);
    }

    @Test
    void test_getJourneysBy_userRequestToken_and_wrong_dateTime_format_throws_dateTimeParsingException() {
        String wrongDateTime = "2020";
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();

        assertThrows(DateTimeParsingException.class, () -> classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                wrongDateTime,
                testData.getIsArrivalDateTime(),
                testData.getOptimize(),
                testData.getLanguage().toString()
        ));
    }

    @Test
    void test_getJourneysBy_userRequestToken_and_wrong_language_format_throws_dateTimeParsingException() {
        String wrongLanguage = "";
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();

        assertThrows(LanguageParsingException.class, () -> classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getOptimize(),
                wrongLanguage
        ));
    }


}
