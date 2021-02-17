package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.configuration.StationPersistenceApiTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(StationPersistenceApiTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StationPersistenceApiCallServiceIT {

    @Autowired
    private ApiToken.ApiTokenBuilder stationPersistenceApiTokenIT;

    @Autowired
    private CallService classUnderTest;

    @Test
    void test_station_persistence_api_returns_all_travelPoints() {
        ApiToken testData = stationPersistenceApiTokenIT.build();

        Flux<TravelPoint> result = classUnderTest.getMany(buildUrlWith(testData).toString(), HttpHeaders.EMPTY, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(travelPoint -> true, travelPoint -> {
                    assertThat(travelPoint.getName()).isNotEmpty();
                    assertThat(travelPoint.getPoint()).isNotNull();
                    assertThat(travelPoint.getArrivalTime()).isNull();
                    assertThat(travelPoint.getDepartureTime()).isNull();
                    assertThat(travelPoint.getPlatform()).isNotNull();
                })
                .verifyComplete();
    }
}
