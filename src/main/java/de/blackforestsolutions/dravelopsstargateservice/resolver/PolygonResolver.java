package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.api.Polygon;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import lombok.extern.slf4j.Slf4j;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Slf4j
@Component
public class PolygonResolver implements GraphQLQueryResolver {

    private final BackendApiService backendApiService;
    private final ApiToken stationPersistencePolygonApiToken;

    @Autowired
    public PolygonResolver(BackendApiService backendApiService, ApiToken stationPersistencePolygonApiToken) {
        this.backendApiService = backendApiService;
        this.stationPersistencePolygonApiToken = stationPersistencePolygonApiToken;
    }

    public CompletableFuture<Polygon> getOperatingArea() {
        return backendApiService.getOneBy(stationPersistencePolygonApiToken, org.locationtech.jts.geom.Polygon.class)
                .map(this::mapToGraphQlPolygon)
                .switchIfEmpty(Mono.just(new Polygon.PolygonBuilder().setPoints(Collections.emptyList()).build()))
                .toFuture();
    }

    private Polygon mapToGraphQlPolygon(org.locationtech.jts.geom.Polygon backendPolygon) {
        return new Polygon.PolygonBuilder()
                .setPoints(mapCoordinatesToPoints(backendPolygon.getExteriorRing().getCoordinates()))
                .build();
    }

    private List<Point> mapCoordinatesToPoints(Coordinate[] coordinates) {
        return Arrays.stream(coordinates)
                .map(coordinate -> new Point.PointBuilder(coordinate.getX(), coordinate.getY()).build())
                .collect(Collectors.toList());
    }

}
