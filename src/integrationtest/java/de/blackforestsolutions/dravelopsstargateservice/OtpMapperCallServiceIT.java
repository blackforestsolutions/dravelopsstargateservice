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

import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.retrieveJsonToPojo;
import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(OtpMapperTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OtpMapperCallServiceIT {

    private final DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken hallo;

    @Test
    void test_journey_returns_journeys() {

        Flux<String> result = classUnderTest.post(buildUrlWith(hallo).toString(), mapper.map(hallo).block(), HttpHeaders.EMPTY);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(journey -> {
                    Journey actualJourney = retrieveJsonToPojo(journey, Journey.class);
                    assertThat(actualJourney.getLegs().size()).isGreaterThan(0);
                    return true;
                })
                .verifyComplete();
    }


    @Test
    void test_journey_without_being_inside_area_returns_no_journeys() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(hallo);
        testData.setDepartureCoordinate(new Point(0.0d, 0.0d));
        testData.setDeparture("Mittelpunkt der Erde");
        testData.setArrivalCoordinate(new Point(0.1d, 0.1d));
        testData.setArrival("Mittelpunkt der Erde");

        Flux<String> result = classUnderTest.post(buildUrlWith(testData.build()).toString(), mapper.map(testData.build()).block(), HttpHeaders.EMPTY);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
