package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.CoordinateParsingException;
import org.springframework.stereotype.Service;

import static de.blackforestsolutions.dravelopsstargateservice.configuration.GeocodingConfiguration.*;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    @Override
    public Point extractCoordinateFrom(double longitude, double latitude, String longitudeErrorField, String latitudeErrorField) {
        if (longitude < MIN_WGS_84_LONGITUDE || longitude > MAX_WGS_84_LONGITUDE) {
            throw new CoordinateParsingException(longitudeErrorField);
        }
        if (latitude < MIN_WGS_84_LATITUDE || latitude > MAX_WGS_84_LATITUDE) {
            throw new CoordinateParsingException(latitudeErrorField);
        }
        return new Point.PointBuilder(longitude, latitude).build();
    }
}
