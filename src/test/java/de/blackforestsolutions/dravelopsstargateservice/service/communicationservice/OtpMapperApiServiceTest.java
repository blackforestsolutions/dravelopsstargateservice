package de.blackforestsolutions.dravelopsstargateservice.service.communicationservice;

import com.fasterxml.jackson.core.JsonParseException;
import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Status;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopstestdatamodel.objectmothers.ApiTokenObjectMother.getOtpMapperApiToken;
import static de.blackforestsolutions.dravelopstestdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopstestdatamodel.testutils.TestUtils.toJson;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

class OtpMapperApiServiceTest {

    private final CallService callService = mock(CallService.class);

    private final OtpMapperApiService classUnderTest = new OtpMapperApiServiceImpl(callService);

    @BeforeEach
    void init() {
        Journey mockedJourney = getFurtwangenToWaldkirchJourney();
        when(callService.post(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(Flux.just(toJson(mockedJourney)));
    }

    @Test
    void test_getJourneysBy_apiToken_returns_journeys() {
        ApiToken testApiToken = getOtpMapperApiToken();

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysBy(testApiToken);

        StepVerifier.create(result)
                .assertNext(journey -> {
                    assertThat(journey.getStatus()).isEqualTo(Status.SUCCESS);
                    assertThat(journey.getThrowable()).isNull();
                    assertThat(toJson(journey.getCalledObject())).isEqualTo(toJson(getFurtwangenToWaldkirchJourney()));
                })
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_apiToken_returns_no_journeys_when_otp_has_no_journeys_found() {
        ApiToken testApiToken = getOtpMapperApiToken();
        when(callService.post(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(Flux.empty());

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysBy(testApiToken);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_is_executed_correctly() {
        ApiToken testApiToken = getOtpMapperApiToken();
        ArgumentCaptor<String> urlArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<String> bodyArg = ArgumentCaptor.forClass(String.class);
        ArgumentCaptor<HttpHeaders> httpHeadersArg = ArgumentCaptor.forClass(HttpHeaders.class);

        classUnderTest.getJourneysBy(testApiToken).collectList().block();

        verify(callService, times(1)).post(urlArg.capture(), bodyArg.capture(), httpHeadersArg.capture());
        assertThat(urlArg.getValue()).isEqualTo("http://localhost:8084/otp/journeys/get");
        assertThat(bodyArg.getValue()).isEqualTo(toJson(getOtpMapperApiToken()));
        assertThat(httpHeadersArg.getValue()).isEqualTo(HttpHeaders.EMPTY);
    }

    @Test
    void test_getJourneysBy_apiToken_and_host_as_null_returns_failed_call_status() {
        ApiToken.ApiTokenBuilder testApiToken = new ApiToken.ApiTokenBuilder(getOtpMapperApiToken());
        testApiToken.setHost(null);

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysBy(testApiToken.build());

        StepVerifier.create(result)
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getCalledObject()).isNull();
                    assertThat(error.getThrowable()).isInstanceOf(NullPointerException.class);
                })
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_apiToken_returns_failed_call_status_and_further_journeys_when_one_json_has_an_error() {
        ApiToken testApiToken = getOtpMapperApiToken();
        Journey mockedJourney = getFurtwangenToWaldkirchJourney();
        when(callService.post(anyString(), anyString(), any(HttpHeaders.class)))
                .thenReturn(Flux.just(toJson(mockedJourney), "error"));

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysBy(testApiToken);

        StepVerifier.create(result)
                .assertNext(journey -> {
                    assertThat(journey.getStatus()).isEqualTo(Status.SUCCESS);
                    assertThat(journey.getThrowable()).isNull();
                    assertThat(journey.getCalledObject()).isInstanceOf(Journey.class);
                })
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getCalledObject()).isNull();
                    assertThat(error.getThrowable()).isInstanceOf(JsonParseException.class);
                })
                .verifyComplete();
    }

    @Test
    void test_getJourneysBy_apiToken_as_null_returns_failed_call_status_when_exception_is_thrown_outside_of_stream() {

        Flux<CallStatus<Journey>> result = classUnderTest.getJourneysBy(null);

        StepVerifier.create(result)
                .assertNext(error -> {
                    assertThat(error.getStatus()).isEqualTo(Status.FAILED);
                    assertThat(error.getCalledObject()).isNull();
                    assertThat(error.getThrowable()).isInstanceOf(Exception.class);
                })
                .verifyComplete();
    }
}
