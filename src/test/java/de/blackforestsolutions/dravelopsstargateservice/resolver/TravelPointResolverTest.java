package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.LanguageParsingException;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.TravelPointApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.TravelPointApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getTravelPointUserRequestToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getGermanyTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TravelPointResolverTest {

    private final TravelPointApiService travelPointApiService = mock(TravelPointApiServiceImpl.class);

    private final TravelPointResolver classUnderTest = new TravelPointResolver(travelPointApiService);

    @BeforeEach
    void init() {
        when(travelPointApiService.retrieveTravelPointsFromApiService(any(ApiToken.class)))
                .thenReturn(Mono.just(List.of(getGermanyTravelPoint(), getGermanyTravelPoint())));
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

        classUnderTest.getTravelPointsBy(testData.getDeparture(), testData.getLanguage().toString());


        InOrder inOrder = inOrder(travelPointApiService);
        inOrder.verify(travelPointApiService, times(1)).retrieveTravelPointsFromApiService(userRequestArg.capture());
        inOrder.verifyNoMoreInteractions();
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(getTravelPointUserRequestToken());
    }

    @Test
    void test_getTravelPointsBy_userRequestToken_returns_an_empty_list_when_no_results_are_in_response_list() throws ExecutionException, InterruptedException {
        ApiToken testData = getTravelPointUserRequestToken();
        when(travelPointApiService.retrieveTravelPointsFromApiService(any(ApiToken.class)))
                .thenReturn(Mono.just(Collections.emptyList()));

        CompletableFuture<List<TravelPoint>> result = classUnderTest.getTravelPointsBy(testData.getDeparture(), testData.getLanguage().toString());

        assertThat(result.get().size()).isEqualTo(0);
    }

    @Test
    void test_getTravelPointsBy_userRequestToken_and_wrong_language_format_throws_dateTimeParsingException() {
        String wrongLanguage = "";
        ApiToken testData = getTravelPointUserRequestToken();

        assertThrows(LanguageParsingException.class, () -> classUnderTest.getTravelPointsBy(testData.getDeparture(), wrongLanguage));
    }
}
