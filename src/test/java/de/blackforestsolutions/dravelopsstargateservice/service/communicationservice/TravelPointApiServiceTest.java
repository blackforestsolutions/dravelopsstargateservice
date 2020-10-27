package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Status;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getGermanyTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class TravelPointApiServiceTest {

    private final ExceptionHandlerService exceptionHandlerService = spy(ExceptionHandlerServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = mock(RequestTokenHandlerServiceImpl.class);
    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final ApiToken polygonServiceApiToken = getConfiguredPolygonApiToken();

    private final TravelPointApiService classUnderTest = new TravelPointApiServiceImpl(exceptionHandlerService, requestTokenHandlerService, backendApiService, polygonServiceApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getManyBy(any(ApiToken.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(
                        new CallStatus<>(getGermanyTravelPoint(), Status.SUCCESS, null),
                        new CallStatus<>(null, Status.FAILED, new Exception())
                ));

        when(requestTokenHandlerService.mergeTravelPointApiTokensWith(any(ApiToken.class), any(ApiToken.class)))
                .thenReturn(getPolygonApiToken());
    }

    @Test
    void test_retrieveTravelPointsFromApiService_with_userRequestToken_returns_correct_travelPoint_list() {
        ApiToken testData = getTravelPointUserRequestToken();

        Mono<List<TravelPoint>> result = classUnderTest.retrieveTravelPointsFromApiService(testData);

        StepVerifier.create(result)
                .assertNext(travelPoints -> {
                    assertThat(travelPoints.size()).isEqualTo(1);
                    assertThat(toJson(travelPoints.get(0))).isEqualTo(toJson(getGermanyTravelPoint()));
                })
                .verifyComplete();
    }

    @Test
    void test_retrieveTravelPointsFromApiService_with_userApiToken_requestTokenHandler_exceptionHandler_and_apiService_is_executed_correctly() {
        ApiToken testData = getTravelPointUserRequestToken();
        ArgumentCaptor<ApiToken> userRequestTokenArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredTokenArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> mergedTokenArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<CallStatus> callStatusArg = ArgumentCaptor.forClass(CallStatus.class);

        classUnderTest.retrieveTravelPointsFromApiService(testData).block();

        InOrder inOrder = inOrder(requestTokenHandlerService, exceptionHandlerService, backendApiService);
        inOrder.verify(requestTokenHandlerService, times(1)).mergeTravelPointApiTokensWith(userRequestTokenArg.capture(), configuredTokenArg.capture());
        inOrder.verify(backendApiService, times(1)).getManyBy(mergedTokenArg.capture(), eq(TravelPoint.class));
        inOrder.verify(exceptionHandlerService, times(2)).handleExceptions(callStatusArg.capture());
        inOrder.verifyNoMoreInteractions();
        assertThat(userRequestTokenArg.getValue()).isEqualToComparingFieldByField(getTravelPointUserRequestToken());
        assertThat(configuredTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredPolygonApiToken());
        assertThat(mergedTokenArg.getValue()).isEqualToComparingFieldByField(getPolygonApiToken());
        assertThat(callStatusArg.getAllValues().size()).isEqualTo(2);
        assertThat(callStatusArg.getAllValues().get(0).getStatus()).isEqualTo(Status.SUCCESS);
        assertThat(callStatusArg.getAllValues().get(0).getThrowable()).isNull();
        assertThat(callStatusArg.getAllValues().get(0).getCalledObject()).isInstanceOf(TravelPoint.class);
        assertThat(callStatusArg.getAllValues().get(1).getStatus()).isEqualTo(Status.FAILED);
        assertThat(callStatusArg.getAllValues().get(1).getCalledObject()).isNull();
        assertThat(callStatusArg.getAllValues().get(1).getThrowable()).isInstanceOf(Exception.class);
    }

    @Test
    void test_retrieveTravelPointsFromApiService_with_userRequestToken_and_thrown_exception_by_mocked_service_returns_zero_travelPoints() {
        ApiToken testData = getTravelPointUserRequestToken();
        when(requestTokenHandlerService.mergeTravelPointApiTokensWith(any(ApiToken.class), any(ApiToken.class)))
                .thenThrow(new NullPointerException());

        Mono<List<TravelPoint>> result = classUnderTest.retrieveTravelPointsFromApiService(testData);

        StepVerifier.create(result)
                .assertNext(travelPoints -> assertThat(travelPoints.size()).isEqualTo(0))
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(Throwable.class));
    }

    @Test
    void test_retrieveTravelPointsFromApiService_with_userRequestToken_and_error_stream_by_service_returns_zero_travelPoints() {
        ApiToken testData = getTravelPointUserRequestToken();
        when(backendApiService.getManyBy(any(ApiToken.class), eq(TravelPoint.class)))
                .thenReturn(Flux.error(new Exception()));

        Mono<List<TravelPoint>> result = classUnderTest.retrieveTravelPointsFromApiService(testData);

        StepVerifier.create(result)
                .assertNext(travelPoints -> assertThat(travelPoints.size()).isEqualTo(0))
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(Throwable.class));
    }

    @Test
    void test_retrieveTravelPointsFromApiService_with_userRequestToken_returns_zero_travelPoints_when_apiService_failed() {
        ApiToken testData = getTravelPointUserRequestToken();
        when(backendApiService.getManyBy(any(ApiToken.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(new CallStatus<>(null, Status.FAILED, new Exception())));

        Mono<List<TravelPoint>> result = classUnderTest.retrieveTravelPointsFromApiService(testData);

        StepVerifier.create(result)
                .assertNext(travelPoints -> assertThat(travelPoints.size()).isEqualTo(0))
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(any(CallStatus.class));
    }
}
