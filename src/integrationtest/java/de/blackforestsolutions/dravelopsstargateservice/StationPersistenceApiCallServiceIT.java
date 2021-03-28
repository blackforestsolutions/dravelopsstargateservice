package de.blackforestsolutions.dravelopsstargateservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsstargateservice.configuration.StationPersistenceApiTestConfiguration;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.restcalls.CallService;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsHttpCallBuilder.buildUrlWith;
import static org.assertj.core.api.Assertions.assertThat;

@Import(StationPersistenceApiTestConfiguration.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class StationPersistenceApiCallServiceIT {

    private static final int MIN_POLYGON_POINTS = 3;

    @Autowired
    private ApiToken stationPersistenceTravelPointApiTokenIT;

    @Autowired
    private ApiToken stationPersistencePolygonApiTokenIT;

    @Autowired
    private CallService classUnderTest;

    @Test
    void test_station_persistence_api_returns_all_travelPoints() {
        String testUrl = buildUrlWith(stationPersistencePolygonApiTokenIT).toString();

        Flux<TravelPoint> result = classUnderTest.getMany(testUrl, HttpHeaders.EMPTY, TravelPoint.class);

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

    @Test
    void test_station_persistence_api_returns_polygon() {
        String testUrl = buildUrlWith(stationPersistencePolygonApiTokenIT).toString();

        Mono<Polygon> result = classUnderTest.getOne(testUrl, HttpHeaders.EMPTY, Polygon.class);

        StepVerifier.create(result)
                .assertNext(polygon -> {
                    assertThat(polygon.getExteriorRing().getCoordinates().length).isGreaterThanOrEqualTo(MIN_POLYGON_POINTS);
                    assertThat(polygon.getExteriorRing().getCoordinates()).allMatch(coordinate -> coordinate.getX() >= -180.0d);
                    assertThat(polygon.getExteriorRing().getCoordinates()).allMatch(coordinate -> coordinate.getX() <= 180.0d);
                    assertThat(polygon.getExteriorRing().getCoordinates()).allMatch(coordinate -> coordinate.getY() >= -90.0d);
                    assertThat(polygon.getExteriorRing().getCoordinates()).allMatch(coordinate -> coordinate.getY() <= 90.0d);
                })
                .verifyComplete();
    }
}
