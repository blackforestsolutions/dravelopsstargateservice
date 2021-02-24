package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
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

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class TravelPointResolverTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = mock(RequestTokenHandlerServiceImpl.class);
    private final ApiToken configuredPolygonApiToken = getConfiguredPolygonApiToken();
    private final ApiToken configuredStationApiToken = getConfiguredTravelPointPersistenceApiToken();

    private final TravelPointResolver classUnderTest = new TravelPointResolver(backendApiService, requestTokenHandlerService, configuredPolygonApiToken, configuredStationApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(getGermanyTravelPoint(), getGermanyTravelPoint()));

        when(backendApiService.getManyBy(any(ApiToken.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(
                        getBadDuerkheimTravelPoint(),
                        getNeckarauTrainStationTravelPoint(),
                        getKarlsbaderStreetTravelPoint(),
                        getFurtwangenBirkeTravelPoint(),
                        getTribergStationStreetTravelPoint()
                ));
    }

    @Test
    void test_getAddressesBy_userRequestToken_returns_travelPoints() throws ExecutionException, InterruptedException {
        ApiToken testData = getTravelPointUserRequestToken();
        TravelPoint expectedTravelPoint = getGermanyTravelPoint();

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getAddressesBy(testData.getDeparture(), testData.getLanguage().toString());

        assertThat(result.get().size()).isEqualTo(2);
        assertThat(result.get().get(0)).isEqualToComparingFieldByFieldRecursively(expectedTravelPoint);
        assertThat(result.get().get(1)).isEqualToComparingFieldByFieldRecursively(expectedTravelPoint);
    }

    @Test
    void test_getAddressesBy_userRequestToken_is_executed_in_right_order_and_passes_arguments_correctly() {
        ApiToken testData = getTravelPointUserRequestToken();
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredPolygonApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.getAddressesBy(testData.getDeparture(), testData.getLanguage().toString());


        InOrder inOrder = inOrder(backendApiService);
        inOrder.verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredPolygonApiTokenArg.capture(), any(RequestHandlerFunction.class), eq(TravelPoint.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(getTravelPointUserRequestToken());
        assertThat(configuredPolygonApiTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredPolygonApiToken());
    }

    @Test
    void test_getAddressesBy_userRequestToken_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        ApiToken testData = getTravelPointUserRequestToken();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class)))
                .thenReturn(Flux.empty());

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getAddressesBy(testData.getDeparture(), testData.getLanguage().toString());

        assertThat(result.get().size()).isEqualTo(0);
    }

    @Test
    void test_getAddressesBy_userRequestToken_and_wrong_language_format_throws_dateTimeParsingException() {
        String wrongLanguage = "";
        ApiToken testData = getTravelPointUserRequestToken();

        assertThrows(LanguageParsingException.class, () -> classUnderTest.getAddressesBy(testData.getDeparture(), wrongLanguage));
    }

    @Test
    void test_getAllStations_returns_correct_stations() throws ExecutionException, InterruptedException {

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getAllStations();

        assertThat(result.get().size()).isEqualTo(5);
        assertThat(result.get().get(0)).isEqualToComparingFieldByFieldRecursively(getBadDuerkheimTravelPoint());
        assertThat(result.get().get(1)).isEqualToComparingFieldByFieldRecursively(getNeckarauTrainStationTravelPoint());
        assertThat(result.get().get(2)).isEqualToComparingFieldByFieldRecursively(getKarlsbaderStreetTravelPoint());
        assertThat(result.get().get(3)).isEqualToComparingFieldByFieldRecursively(getFurtwangenBirkeTravelPoint());
        assertThat(result.get().get(4)).isEqualToComparingFieldByFieldRecursively(getTribergStationStreetTravelPoint());
    }

    @Test
    void test_getAllStations_is_executed_in_right_order_and_passes_arguments_correctly() {
        ArgumentCaptor<ApiToken> configuredStationPersistenceApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.getAllStations();

        InOrder inOrder = inOrder(backendApiService);
        inOrder.verify(backendApiService, times(1)).getManyBy(configuredStationPersistenceApiTokenArg.capture(), eq(TravelPoint.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(configuredStationPersistenceApiTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredTravelPointPersistenceApiToken());
    }

    @Test
    void test_getAllStations_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        when(backendApiService.getManyBy(any(ApiToken.class), eq(TravelPoint.class)))
                .thenReturn(Flux.empty());

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getAllStations();

        assertThat(result.get().size()).isEqualTo(0);
    }
}
