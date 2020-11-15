package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsstargateservice.configuration.OtpMapperTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(OtpMapperTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OtpMapperCallServiceIT {

    private final DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken otpMapperApiToken;

    @Test
    void test_journey_returns_journeys() {

        Flux<Journey> result = classUnderTest.postMany(buildUrlWith(otpMapperApiToken).toString(), otpMapperApiToken, HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(journey -> true, journey -> assertThat(journey.getLegs().size()).isGreaterThan(0))
                .verifyComplete();
    }


    @Test
    void test_journey_without_being_inside_area_returns_no_journeys() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(otpMapperApiToken);
        testData.setDepartureCoordinate(new Point(0.0d, 0.0d));
        testData.setDeparture("Mittelpunkt der Erde");
        testData.setArrivalCoordinate(new Point(0.1d, 0.1d));
        testData.setArrival("Mittelpunkt der Erde");

        Flux<Journey> result = classUnderTest.postMany(buildUrlWith(testData.build()).toString(), testData.build(), HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
