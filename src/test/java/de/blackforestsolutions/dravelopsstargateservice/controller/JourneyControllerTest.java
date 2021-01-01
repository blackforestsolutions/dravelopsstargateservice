package de.blackforestsolutions.dravelopsstargateservice.controller;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.RequestHandlerFunction;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredRoutePersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getJourneyUserRequestToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.UUIDObjectMother.TEST_UUID_1;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class JourneyControllerTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final RequestTokenHandlerService requestTokenHandlerService = new RequestTokenHandlerServiceImpl();
    private final ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

    private final JourneyController classUnderTest = new JourneyController(backendApiService, requestTokenHandlerService, configuredRoutePersistenceApiToken);

    @Test
    void test_getJourneysBy_userRequestToken_configuredToken_requestHandlerFunction_returns_journeys_when_journeys_by_backend_are_found() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        ArgumentCaptor<ApiToken> userRequestArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<ApiToken> configuredTokenCapture = ArgumentCaptor.forClass(ApiToken.class);
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.just(getJourneyWithEmptyFields(TEST_UUID_1)));

        Flux<Journey> result = classUnderTest.getJourneysBy(testData);

        verify(backendApiService, times(1)).getManyBy(userRequestArg.capture(), configuredTokenCapture.capture(), any(RequestHandlerFunction.class), eq(Journey.class));
        StepVerifier.create(result)
                .assertNext(journey -> assertThat(toJson(journey)).isEqualTo(toJson(getJourneyWithEmptyFields(TEST_UUID_1))))
                .verifyComplete();
        assertThat(userRequestArg.getValue()).isEqualToComparingFieldByField(testData.build());
        assertThat(configuredTokenCapture.getValue()).isEqualToComparingFieldByField(configuredRoutePersistenceApiToken);
    }

    @Test
    void test_getJourneysBy_userRequestToken_configuredToken_requestHandlerFunction_returns_zero_journeys_when_no_journeys_by_backend_are_found() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        when(backendApiService.getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.getJourneysBy(testData);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
