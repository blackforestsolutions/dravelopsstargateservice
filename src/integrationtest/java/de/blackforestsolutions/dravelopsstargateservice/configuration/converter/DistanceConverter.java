package de.blackforestsolutions.dravelopsstargateservice.configuration.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.lang.NonNull;

public class DistanceConverter implements Converter<Double, Distance> {

    @Override
    public Distance convert(@NonNull Double distanceInKilometers) {
        return new Distance(distanceInKilometers, Metrics.KILOMETERS);
    }
}
