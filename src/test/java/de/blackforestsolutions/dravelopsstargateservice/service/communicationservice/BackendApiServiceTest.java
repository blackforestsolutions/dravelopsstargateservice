package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerService;
import de.blackforestsolutions.dravelopsstargateservice.exceptionhandling.ExceptionHandlerServiceImpl;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import de.blackforestsolutions.dravelopsstargateservice.service.supportservice.RequestTokenHandlerServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.PolygonObjectMother.getPolygonWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.*;
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

        TravelPoint mockedTravelPoint = getBadDuerkheimTravelPoint();
        when(callService.getMany(anyString(), any(HttpHeaders.class), eq(TravelPoint.class)))
                .thenReturn(Flux.just(mockedTravelPoint));

        Polygon mockedPolygon = getPolygonWithNoEmptyFields();
        when((callService.getOne(anyString(), any(HttpHeaders.class), eq(Polygon.class))))
                .thenReturn(Mono.just(mockedPolygon));
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_returns_journeys() {
        ApiToken configuredTestToken = getConfiguredRoutePersistenceApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByFieldRecursively(getFurtwangenToWaldkirchJourney()))
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
                .assertNext(travelPoint -> assertThat(travelPoint).isEqualToComparingFieldByFieldRecursively(getGermanWatchMuseumTravelPoint()))
                .assertNext(travelPoint -> assertThat(travelPoint).isEqualToComparingFieldByFieldRecursively(getGermanyTravelPoint()))
                .verifyComplete();
    }

    @Test
    void test_getManyBy_user_token_and_configured_apiToken_returns_no_journeys_when_otp_has_no_journeys_found() {
        ApiToken configuredTestToken = getConfiguredRoutePersistenceApiToken();
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
        ApiToken configuredTestToken = getConfiguredRoutePersistenceApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<ApiToken> bodyArg = ArgumentCaptor.forClass(ApiToken.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class).collectList().block();

        verify(callService, times(1)).postMany(urlArg.capture(), bodyArg.capture(), httpHeadersArg.capture(), eq(Journey.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8088/otp/journeys/get");
        assertThat(bodyArg.getValue()).isEqualToComparingFieldByFieldRecursively(getJourneyUserRequestToken());
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
        ApiToken configuredTestToken = getConfiguredRoutePersistenceApiToken();

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
        ApiToken.ApiTokenBuilder configuredTestToken = new ApiToken.ApiTokenBuilder(getConfiguredRoutePersistenceApiToken());
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

        Flux<Journey> result = classUnderTest.getManyBy(null, null, null, null);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_apiToken_and_error_by_call_service_returns_failed_call_status() {
        ApiToken configuredTestToken = getConfiguredRoutePersistenceApiToken();
        ApiToken userRequestToken = getJourneyUserRequestToken();
        when(callService.postMany(anyString(), any(ApiToken.class), any(HttpHeaders.class), eq(Journey.class)))
                .thenReturn(Flux.error(new Exception()));

        Flux<Journey> result = classUnderTest.getManyBy(userRequestToken, configuredTestToken, requestTokenHandlerService::mergeJourneyApiTokensWith, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_configured_apiToken_returns_travelPoints() {
        ApiToken configuredTestToken = getConfiguredTravelPointPersistenceApiToken();

        Flux<TravelPoint> result = classUnderTest.getManyBy(configuredTestToken, TravelPoint.class);

        StepVerifier.create(result)
                .assertNext(travelPoint -> assertThat(travelPoint).isEqualToComparingFieldByFieldRecursively(getBadDuerkheimTravelPoint()))
                .verifyComplete();
    }

    @Test
    void test_getManyBy_configured_apiToken_returns_no_travelPoints_when_nothing_found() {
        ApiToken configuredTestToken = getConfiguredTravelPointPersistenceApiToken();
        when(callService.getMany(anyString(), any(HttpHeaders.class), eq(TravelPoint.class)))
                .thenReturn(Flux.empty());

        Flux<TravelPoint> result = classUnderTest.getManyBy(configuredTestToken, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_configured_apiToken_is_executed_correctly_when_travelPoints_are_returned() {
        ApiToken configuredTestToken = getConfiguredTravelPointPersistenceApiToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getManyBy(configuredTestToken, TravelPoint.class).collectList().block();

        verify(callService, times(1)).getMany(urlArg.capture(), httpHeadersArg.capture(), eq(TravelPoint.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8086/travelpoints/get");
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getManyBy_configured_apiToken_and_host_as_null_returns_failed_call_status() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder configuredTestToken = new ApiToken.ApiTokenBuilder(getConfiguredTravelPointPersistenceApiToken());
        configuredTestToken.setHost(null);

        Flux<TravelPoint> result = classUnderTest.getManyBy(configuredTestToken.build(), TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleExceptions(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getManyBy_configured_apiToken_as_null_returns_no_results_when_exception_is_thrown_outside_of_stream() {

        Flux<TravelPoint> result = classUnderTest.getManyBy(null, null);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getManyBy_configured_apiToken_and_error_by_call_service_returns_no_travelPoint() {
        ApiToken configuredTestToken = getConfiguredTravelPointPersistenceApiToken();
        when(callService.getMany(anyString(), any(HttpHeaders.class), eq(TravelPoint.class)))
                .thenReturn(Flux.error(new Exception()));

        Flux<TravelPoint> result = classUnderTest.getManyBy(configuredTestToken, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getOneBy_configured_apiToken_returns_polygon() {
        ApiToken configuredTestToken = getConfiguredPolygonPersistenceApiToken();

        Mono<Polygon> result = classUnderTest.getOneBy(configuredTestToken, Polygon.class);

        StepVerifier.create(result)
                .assertNext(polygon -> assertThat(polygon).isEqualToComparingFieldByFieldRecursively(getPolygonWithNoEmptyFields()))
                .verifyComplete();
    }

    @Test
    void test_getOneBy_configured_apiToken_returns_no_polygon_when_backend_could_not_calculate_one() {
        ApiToken configuredTestToken = getConfiguredPolygonPersistenceApiToken();
        when(callService.getOne(anyString(), any(HttpHeaders.class), eq(Polygon.class)))
                .thenReturn(Mono.empty());

        Mono<Polygon> result = classUnderTest.getOneBy(configuredTestToken, Polygon.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getOneBy_configured_apiToken_is_executed_correctly_with_right_arguments() {
        ApiToken configuredTestToken = getConfiguredPolygonPersistenceApiToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getOneBy(configuredTestToken, Polygon.class).block();

        verify(callService, times(1)).getOne(urlArg.capture(), httpHeadersArg.capture(), eq(Polygon.class));
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8086/geocoding/get/operatingPolygon");
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getOneBy_configured_apiToken_and_host_as_null_returns_no_polygon_when_exception_is_thrown() {
        ArgumentCaptor<Throwable> exceptionArg = ArgumentCaptor.forClass(Throwable.class);
        ApiToken.ApiTokenBuilder configuredTestToken = new ApiToken.ApiTokenBuilder(getConfiguredPolygonPersistenceApiToken());
        configuredTestToken.setHost(null);

        Mono<Polygon> result = classUnderTest.getOneBy(configuredTestToken.build(), Polygon.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
        verify(exceptionHandlerService, times(1)).handleException(exceptionArg.capture());
        assertThat(exceptionArg.getValue()).isInstanceOf(NullPointerException.class);
    }

    @Test
    void test_getOneBy_configured_apiToken_as_null_returns_no_polygon_when_expcetion_thrown_outside_of_stream() {

        Mono<Polygon> result = classUnderTest.getOneBy(null, null);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getOneBy_configured_apiToken_and_error_by_callService_returns_no_polygon() {
        ApiToken configuredTestToken = getConfiguredPolygonPersistenceApiToken();
        when(callService.getOne(anyString(), any(HttpHeaders.class), eq(Polygon.class)))
                .thenReturn(Mono.error(new Exception()));

        Mono<Polygon> result = classUnderTest.getOneBy(configuredTestToken, Polygon.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
