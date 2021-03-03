package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.configuration.BoxServiceTestConfiguration;
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

@Import(BoxServiceTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BoxServiceCallServiceIT {

    @Autowired
    private CallService classUnderTest;

    @Autowired
    public ApiToken.ApiTokenBuilder boxServiceApiTokenIT;

    @Test
    void test_travelPoint_returns_travelPoints() {

        Flux<TravelPoint> result = classUnderTest.postMany(buildUrlWith(boxServiceApiTokenIT.build()).toString(), boxServiceApiTokenIT.build(), HttpHeaders.EMPTY, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(travelPoint -> {
                    assertThat(travelPoint.getName()).isNotEmpty();
                    assertThat(travelPoint.getPoint()).isNotNull();
                    return true;
                })
                .verifyComplete();
    }
}