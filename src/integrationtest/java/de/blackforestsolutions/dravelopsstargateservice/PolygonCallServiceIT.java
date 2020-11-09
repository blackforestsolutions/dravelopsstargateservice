package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsstargateservice.configuration.PolygonTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.retrieveJsonToPojo;
import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(PolygonTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PolygonCallServiceIT {

    private final DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();

    @Autowired
    private CallService classUnderTest;

    @Autowired
    public ApiToken polygonApiTokenIT;

    @Test
    void test_travelPoint_returns_travelPoints() {
        String testBody = mapper.map(polygonApiTokenIT).block();

        Flux<String> result = classUnderTest.post(buildUrlWith(polygonApiTokenIT).toString(), testBody, HttpHeaders.EMPTY);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(travelPoint -> {
                    TravelPoint actualTravelPoint = retrieveJsonToPojo(travelPoint, TravelPoint.class);
                    assertThat(actualTravelPoint.getName()).isNotEmpty();
                    assertThat(actualTravelPoint.getPoint()).isNotNull();
                    return true;
                })
                .verifyComplete();
    }
}
