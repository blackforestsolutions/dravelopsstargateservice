package de.blackforestsolutions.dravelopsstargateservice.exceptionhandling;

import de.blackforestsolutions.dravelopsdatamodel.CallStatus;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Status;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getJourneyWithEmptyFields;
import static org.assertj.core.api.Assertions.assertThat;

public class ExceptionHandlerServiceTest {


    private final ExceptionHandlerService classUnderTest = new ExceptionHandlerServiceImpl();

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_null_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, null, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_null_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), null, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_null_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), null, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_null_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, null, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_success_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.SUCCESS, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_success_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), Status.SUCCESS, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .assertNext(journey -> assertThat(journey).isEqualToComparingFieldByField(getJourneyWithEmptyFields()))
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_success_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), Status.SUCCESS, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_success_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.SUCCESS, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_failed_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.FAILED, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_failed_exception_as_null_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), Status.FAILED, null);

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_status_as_failed_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(getJourneyWithEmptyFields(), Status.FAILED, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }

    @Test
    void test_handleExceptions_with_calledObject_as_null_status_as_failed_exception_returns_emptyMono() {
        CallStatus<Journey> testData = new CallStatus<>(null, Status.FAILED, new Exception());

        Mono<Journey> result = classUnderTest.handleExceptions(testData);

        StepVerifier.create(result)
                .expectNextCount(0)
                .verifyComplete();
    }
}
