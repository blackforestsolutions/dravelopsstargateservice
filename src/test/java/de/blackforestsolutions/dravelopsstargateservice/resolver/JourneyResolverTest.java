package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getJourneyUserRequestToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchWaypointsJourney;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class JourneyResolverTest {

    private final JourneyApiService journeyApiService = mock(JourneyApiServiceImpl.class);

    private final JourneyResolver classUnderTest = new JourneyResolver(journeyApiService);

    @BeforeEach
    void init() {
        when(journeyApiService.getJourneysBy(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyString(),
                anyBoolean(),
                anyString())
        ).thenReturn(Flux.just(getFurtwangenToWaldkirchWaypointsJourney(), getFurtwangenToWaldkirchWaypointsJourney()));
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_journey() throws ExecutionException, InterruptedException {
        ApiToken testData = getJourneyUserRequestToken();
        Journey expectedJourney = getFurtwangenToWaldkirchWaypointsJourney();

        CompletableFuture<List<Journey>> result = classUnderTest.getJourneysBy(
                testData.getDepartureCoordinate().getX(),
                testData.getDepartureCoordinate().getY(),
                testData.getArrivalCoordinate().getX(),
                testData.getArrivalCoordinate().getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        assertThat(result.get().size()).isEqualTo(2);
        assertThat(result.get().get(0)).isEqualToComparingFieldByFieldRecursively(expectedJourney);
        assertThat(result.get().get(1)).isEqualToComparingFieldByFieldRecursively(expectedJourney);
    }

    @Test
    void test_getJourneysBy_userRequestToken_is_executed_in_right_order_and_passes_arguments_correctly() {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        ArgumentCaptor<Double> departureLongitudeArg = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> departureLatitudeArg = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> arrivalLongitudeArg = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> arrivalLatitudeArg = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<String> dateTimeArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<Boolean> isArrivalDateTimeArg = ArgumentCaptor.forClass(Boolean.class);
        ArgumentCaptor<String> languageArg = ArgumentCaptor.forClass(String.class);

        classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        InOrder inOrder = inOrder(journeyApiService);
        inOrder.verify(journeyApiService, times(1)).getJourneysBy(
                departureLongitudeArg.capture(),
                departureLatitudeArg.capture(),
                arrivalLongitudeArg.capture(),
                arrivalLatitudeArg.capture(),
                dateTimeArg.capture(),
                isArrivalDateTimeArg.capture(),
                languageArg.capture()
        );
        inOrder.verifyNoMoreInteractions();
        assertThat(departureLongitudeArg.getValue()).isEqualTo(testData.getDepartureCoordinate().getX());
        assertThat(departureLatitudeArg.getValue()).isEqualTo(testData.getDepartureCoordinate().getY());
        assertThat(arrivalLongitudeArg.getValue()).isEqualTo(testData.getArrivalCoordinate().getX());
        assertThat(arrivalLatitudeArg.getValue()).isEqualTo(testData.getArrivalCoordinate().getY());
        assertThat(dateTimeArg.getValue()).isEqualTo(testData.getDateTime().toString());
        assertThat(isArrivalDateTimeArg.getValue()).isEqualTo(testData.getIsArrivalDateTime());
        assertThat(languageArg.getValue()).isEqualTo(testData.getLanguage().toString());
    }

    @Test
    void test_getJourneysBy_userRequestToken_returns_an_empty_list_when_no_results_are_available() throws ExecutionException, InterruptedException {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        when(journeyApiService.getJourneysBy(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyString(),
                anyBoolean(),
                anyString())
        ).thenReturn(Flux.empty());

        CompletableFuture<List<Journey>> result = classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        );

        assertThat(result.get().size()).isEqualTo(0);
    }

    @Test
    void test_getJourneysBy_userRequestToken_throws_exception_from_journeyApiService() {
        ApiToken testData = getJourneyUserRequestToken();
        Point testDeparture = testData.getDepartureCoordinate();
        Point testArrival = testData.getArrivalCoordinate();
        when(journeyApiService.getJourneysBy(
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyDouble(),
                anyString(),
                anyBoolean(),
                anyString())
        ).thenThrow(new LanguageParsingException());

        assertThrows(LanguageParsingException.class, () -> classUnderTest.getJourneysBy(
                testDeparture.getX(),
                testDeparture.getY(),
                testArrival.getX(),
                testArrival.getY(),
                testData.getDateTime().toString(),
                testData.getIsArrivalDateTime(),
                testData.getLanguage().toString()
        ));
    }


}
