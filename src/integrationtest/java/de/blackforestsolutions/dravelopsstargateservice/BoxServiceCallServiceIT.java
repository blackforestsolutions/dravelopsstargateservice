package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.configuration.BoxServiceTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.geo.Metrics;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static de.blackforestsolutions.dravelopsstargateservice.configuration.GeocodingConfiguration.*;
import static org.assertj.core.api.Assertions.assertThat;

@Import(BoxServiceTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
public class BoxServiceCallServiceIT {

    @Autowired
    private CallService classUnderTest;

    @Autowired
    private ApiToken.ApiTokenBuilder boxServiceAutocompleteAddressesApiToken;

    @Autowired
    private ApiToken.ApiTokenBuilder boxServiceNearestAddressesApiToken;

    @Test
    void test_getAutocompleteAddressesBy_apiToken_returns_travelPoints() {

        Flux<TravelPoint> result = classUnderTest.postMany(buildUrlWith(boxServiceAutocompleteAddressesApiToken.build()).toString(), boxServiceAutocompleteAddressesApiToken.build(), HttpHeaders.EMPTY, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(travelPoint -> {
                    assertThat(travelPoint).isNotNull();
                    assertThat(travelPoint.getName()).isNotEmpty();
                    assertThat(travelPoint.getPoint()).isNotNull();
                    assertThat(travelPoint.getPoint().getX()).isGreaterThanOrEqualTo(MIN_WGS_84_LONGITUDE);
                    assertThat(travelPoint.getPoint().getX()).isLessThanOrEqualTo(MAX_WGS_84_LONGITUDE);
                    assertThat(travelPoint.getPoint().getY()).isGreaterThanOrEqualTo(MIN_WGS_84_LATITUDE);
                    assertThat(travelPoint.getPoint().getY()).isLessThanOrEqualTo(MAX_WGS_84_LATITUDE);
                    assertThat(travelPoint.getPlatform()).isEmpty();
                    assertThat(travelPoint.getArrivalTime()).isNull();
                    assertThat(travelPoint.getDepartureTime()).isNull();
                    return true;
                })
                .verifyComplete();
    }

    @Test
    void test_getNearestAddressesBy_apiToken_returns_travelPoints() {

        Flux<TravelPoint> result = classUnderTest.postMany(buildUrlWith(boxServiceNearestAddressesApiToken.build()).toString(), boxServiceNearestAddressesApiToken.build(), HttpHeaders.EMPTY, TravelPoint.class);

        StepVerifier.create(result)
                .expectNextCount(1L)
                .thenConsumeWhile(travelPoint -> {
                    assertThat(travelPoint).isNotNull();
                    assertThat(travelPoint.getName()).isNotEmpty();
                    assertThat(travelPoint.getPoint()).isNotNull();
                    assertThat(travelPoint.getPoint().getX()).isGreaterThanOrEqualTo(MIN_WGS_84_LONGITUDE);
                    assertThat(travelPoint.getPoint().getX()).isLessThanOrEqualTo(MAX_WGS_84_LONGITUDE);
                    assertThat(travelPoint.getPoint().getY()).isGreaterThanOrEqualTo(MIN_WGS_84_LATITUDE);
                    assertThat(travelPoint.getPoint().getY()).isLessThanOrEqualTo(MAX_WGS_84_LATITUDE);
                    assertThat(travelPoint.getDistanceInKilometers()).isNotNull();
                    assertThat(travelPoint.getDistanceInKilometers().getValue()).isGreaterThanOrEqualTo(MIN_DISTANCE_IN_KILOMETERS_TO_POINT);
                    assertThat(travelPoint.getDistanceInKilometers().getMetric()).isEqualTo(Metrics.KILOMETERS);
                    assertThat(travelPoint.getDepartureTime()).isNull();
                    assertThat(travelPoint.getArrivalTime()).isNull();
                    assertThat(travelPoint.getPlatform()).isEmpty();
                    return true;
                })
                .verifyComplete();
    }
}
