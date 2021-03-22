package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.DateTimeParsingException;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.GeocodingService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.GeocodingServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredRoutePersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getJourneyUserRequestToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JourneyApiServiceTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = mock(RequestTokenHandlerServiceImpl.class);
    private final GeocodingService geocodingService = mock(GeocodingServiceImpl.class);
    private final ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

    private final JourneyApiService classUnderTest = new JourneyApiServiceImpl(backendApiService, requestTokenHandlerService, geocodingService, configuredRoutePersistenceApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.just(getFurtwangenToWaldkirchJourney(), getFurtwangenToWaldkirchJourney()));

        when(geocodingService.extractCoordinateFrom(anyDouble(), anyDouble(), anyString(), anyString()))
                .thenReturn(getJourneyUserRequestToken().getDepartureCoordinate())
                .thenReturn(getJourneyUserRequestToken().getArrivalCoordinate());
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_journey() {
        ApiToken testData = getJourneyUserRequestToken();
        Journey expectedJourney = getFurtwangenToWaldkirchJourney();

        Flux<Journey> result = classUnderTest.getJourneysBy(
                testData.getDepartureCoordinate().getX(),
                testData.getDepartureCoordinate().getY(),
                testData.getArrivalCoordinate().getX(),
                testData.getArrivalCoordinate().getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(expectedJourney))
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(expectedJourney))
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_userRequestToken_is_executed_in_right_order_and_passes_arguments_correctly() {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredRoutePersistenceApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<Double> longitudeArg = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> latitudeArg = ArgumentCaptor.forClass(Double.class);

        classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        InOrder inOrder = inOrder(geocodingService, backendApiService);
        inOrder.verify(geocodingService, times(2)).extractCoordinateFrom(longitudeArg.capture(), latitudeArg.capture(), anyString(), anyString());
        inOrder.verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredRoutePersistenceApiTokenArg.capture(), any(RequestHandlerFunction.class), eq(Journey.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(longitudeArg.getAllValues().size()).isEqualTo(2);
        assertThat(longitudeArg.getAllValues().get(0)).isEqualTo(getJourneyUserRequestToken().getDepartureCoordinate().getX());
        assertThat(longitudeArg.getAllValues().get(1)).isEqualTo(getJourneyUserRequestToken().getArrivalCoordinate().getX());
        assertThat(latitudeArg.getAllValues().size()).isEqualTo(2);
        assertThat(latitudeArg.getAllValues().get(0)).isEqualTo(getJourneyUserRequestToken().getDepartureCoordinate().getY());
        assertThat(latitudeArg.getAllValues().get(1)).isEqualTo(getJourneyUserRequestToken().getArrivalCoordinate().getY());
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByFieldRecursively(getJourneyUserRequestToken());
        assertThat(configuredRoutePersistenceApiTokenArg.getValue()).isEqualToComparingFieldByFieldRecursively(getConfiguredRoutePersistenceApiToken());
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
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
                wrongLanguage
        ));
    }
}
