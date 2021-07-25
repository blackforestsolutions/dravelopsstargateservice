package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import com.google.maps.internal.PolylineEncoding;
import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.CoordinateParsingException;
import org.springframework.stereotype.Service;

import java.util.LinkedList;
import java.util.stream.Collectors;

import static de.blackforestsolutions.dravelopsstargateservice.configuration.GeocodingConfiguration.*;

@Service
public class GeocodingServiceImpl implements GeocodingService {

    private static final int MULTIPLIER = 10;

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

    @Override
    public LinkedList<Point> decodePolylineFrom(String encodedPolyline) {
        return PolylineEncoding.decode(encodedPolyline)
                .stream()
                .map(latLng -> extractCoordinateWithFixedDecimalPlacesFrom(latLng.lng, latLng.lat))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Point extractCoordinateWithFixedDecimalPlacesFrom(double longitude, double latitude) {
        return new Point.PointBuilder(roundToFixedDecimalPlaces(longitude), roundToFixedDecimalPlaces(latitude))
                .build();
    }

    private double roundToFixedDecimalPlaces(double coordinateHalf) {
        double decimals = Math.pow(MULTIPLIER, NUMBER_OF_DECIMAL_PLACES);
        return Math.round(coordinateHalf * decimals) / decimals;
    }
}
