package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Point;

public interface GeocodingService {
    Point extractCoordinateFrom(double longitude, double latitude, String longitudeErrorField, String latitudeErrorField);
}
