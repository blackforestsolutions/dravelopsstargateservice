package de.blackforestsolutions.dravelopsstargateservice.objectmothers;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.api.Polygon;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.PolygonObjectMother.getPolygonWithNoEmptyFields;

public class PolygonObjectMother {

    public static Polygon getFrontendPolygonWithNoEmptyFields() {
        return new Polygon.PolygonBuilder()
                .setPoints(mapBackendCoordinatesToFrontendPoints())
                .build();
    }

    private static List<Point> mapBackendCoordinatesToFrontendPoints() {
        return Arrays.stream(getPolygonWithNoEmptyFields().getExteriorRing().getCoordinates())
                .map(coordinate -> new Point.PointBuilder(coordinate.getX(), coordinate.getY()).build())
                .collect(Collectors.toList());
    }
}
