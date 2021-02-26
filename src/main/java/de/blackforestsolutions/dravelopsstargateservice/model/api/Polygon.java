package de.blackforestsolutions.dravelopsstargateservice.model.api;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.LinkedList;
import java.util.List;

@Getter
public final class Polygon {

    private final List<Point> points;

    private Polygon(PolygonBuilder polygonBuilder) {
        this.points = polygonBuilder.getPoints();
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class PolygonBuilder {

        private List<Point> points = new LinkedList<>();

        public PolygonBuilder() {

        }

        public Polygon build() {
            return new Polygon(this);
        }
    }
}
