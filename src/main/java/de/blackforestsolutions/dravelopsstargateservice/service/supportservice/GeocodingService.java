package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Point;

import java.util.LinkedList;

public interface GeocodingService {
    Point extractCoordinateFrom(double longitude, double latitude, String longitudeErrorField, String latitudeErrorField);

    LinkedList<Point> decodePolylineFrom(String encodedPolyline);
}
