package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getGermanWatchMuseumTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getGermanyTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class BackendApiServiceTest {

    private final CallService callService = mock(CallService.class);
    private final ExceptionHandlerService exceptionHandlerService = spy(ExceptionHandlerServiceImpl.class);
    private final RequestTokenHandlerServiceImpl requestTokenHandlerService = new RequestTokenHandlerServiceImpl();

    private final BackendApiService classUnderTest = new BackendApiServiceImpl(callService, exceptionHandlerService);

    @BeforeEach
    void init() {
        Journey mockedJourney = getFurtwangenToWaldkirchJourney();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.just(mockedJourney));
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_returns_journeys() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(toJson(journey)).isEqualTo(toJson(getFurtwangenToWaldkirchJourney())))
                .verifyComplete();
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_returns_travelPoints() {
        ApiToken configuredTestToken = getConfiguredPolygonApiToken();
        ApiToken userRequestToken = getTravelPointUserRequestToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(getGermanWatchMuseumTravelPoint(), getGermanyTravelPoint()));

        Flux<TravelPoint> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeTravelPointApiTokensWith, TravelPoint.class);

        StepVerifier.create(result)
                .assertNext(travelPoint -> assertThat(toJson(travelPoint)).isEqualTo(toJson(getGermanWatchMuseumTravelPoint())))
                .assertNext(travelPoint -> assertThat(toJson(travelPoint)).isEqualTo(toJson(getGermanyTravelPoint())))
                .verifyComplete();
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_returns_no_journeys_when_otp_has_no_journeys_found() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.empty());

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_is_executed_correctly_when_journeys_are_returned() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ApiToken> bodyArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class).collectList().block();

        verify(callService, times(1)).postMany(urlArg.capture(), bodyArg.capture(), httpHeadersArg.capture(), eq(Journey.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8084/otp/journeys/get");
        assertThat(bodyArg.getValue()).isEqualToComparingFieldByField(getJourneyUserRequestToken());
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_is_executed_correctly_when_travelPoints_are_returned() {
        ApiToken configuredTestToken = getConfiguredPolygonApiToken();
        ApiToken userRequestToken = getTravelPointUserRequestToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ApiToken> bodyArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeTravelPointApiTokensWith, TravelPoint.class).collectList().block();

        verify(callService, times(1)).postMany(urlArg.capture(), bodyArg.capture(), httpHeadersArg.capture(), eq(TravelPoint.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8083/pelias/travelpoints/get");
        assertThat(bodyArg.getValue()).isEqualToComparingFieldByField(getTravelPointUserRequestToken());
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getManyBy_user_token_with_language_as_null_and_configured_apiToken_returns_failed_call_status() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder userRequestToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestToken.setLanguage(null);
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken.build(), configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_and_host_as_null_returns_failed_call_status() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder configuredTestToken = new ApiToken.ApiTokenBuilder(getConfiguredOtpMapperApiToken());
        ApiToken userRequestToken = getJourneyUserRequestToken();
        configuredTestToken.setHost(null);

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken.build(), requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getManyBy_user_token_as_null_and_configured_apiToken_as_null_returns_failed_call_status_when_exception_is_thrown_outside_of_stream() {

        Flux<Journey> result = classUnderTest.getManyBy(null, null, null, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_apiToken_and_error_by_call_service_returns_failed_call_status() {
        ApiToken configuredTestToken = getConfiguredOtpMapperApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.error(new Exception()));

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }
}
