package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsdatamodel.Optimization;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.geo.Point;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.ZonedDateTime;
import java.util.Locale;

import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.retrieveJsonToPojo;
import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class OtpMapperCallServiceIT {

    private final DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken mapperServiceApiToken;

    @Test
    void test_journey_returns_journeys() {
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(mapperServiceApiToken);
        testData.setOptimize(Optimization.QUICK);
        testData.setIsArrivalDateTime(false);
        testData.setDateTime(ZonedDateTime.parse("2020-09-30T13:00:00+02:00"));
        testData.setDepartureCoordinate(new Point(8.209972d, 48.048320d));
        testData.setDeparture("Am Großhausberg 8");
        testData.setArrivalCoordinate(new Point(7.950507d, 48.088204d));
        testData.setArrival("Sick AG");
        testData.setLanguage(Locale.forLanguageTag("de"));

        Flux<String> result = classUnderTest.post(buildUrlWith(testData.build()).toString(), mapper.map(testData.build()).block(), HttpHeaders.EMPTY);

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
        ApiToken.ApiTokenBuilder testData = new ApiToken.ApiTokenBuilder(mapperServiceApiToken);
        testData.setOptimize(Optimization.QUICK);
        testData.setIsArrivalDateTime(false);
        testData.setDateTime(ZonedDateTime.parse("2020-09-30T13:00:00+02:00"));
        testData.setDepartureCoordinate(new Point(2.339506, 48.872404));
        testData.setDeparture("Paris");
        testData.setArrivalCoordinate(new Point(-0.454778d, 51.470203d));
        testData.setArrival("London");
        testData.setLanguage(Locale.forLanguageTag("de"));

        Flux<String> result = classUnderTest.post(buildUrlWith(testData.build()).toString(), mapper.map(testData.build()).block(), HttpHeaders.EMPTY);

        StepVerifier.create(result)
                .expectNextCount(0L)
                .verifyComplete();
    }


}
