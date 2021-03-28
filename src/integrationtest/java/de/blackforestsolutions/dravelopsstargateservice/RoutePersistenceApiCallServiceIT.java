package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.configuration.RoutePersistenceApiTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(RoutePersistenceApiTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class RoutePersistenceApiCallServiceIT {

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken routePersistenceApiTokenIT;

    @Test
    void test_journey_returns_journeys() {
        String testUrl = buildUrlWith(routePersistenceApiTokenIT).toString();

        Flux<Journey> result = classUnderTest.postMany(testUrl, routePersistenceApiTokenIT, HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(journey -> true, journey -> assertThat(journey.getLegs().size()).isGreaterThan(0))
                .verifyComplete();
    }


    @Test
    void test_journey_without_being_inside_area_returns_no_journeys() {
        ApiToken testData = new ApiToken(routePersistenceApiTokenIT);
        testData.setDepartureCoordinate(new Point.PointBuilder(0.0d, 0.0d).build());
        testData.setDeparture("middlepoint of earth");
        testData.setArrivalCoordinate(new Point.PointBuilder(0.1d, 0.1d).build());
        testData.setArrival("middlepoint of earth");
        String testUrl = buildUrlWith(testData).toString();

        Flux<Journey> result = classUnderTest.postMany(testUrl, testData, HttpHeaders.EMPTY, Journey.class);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
