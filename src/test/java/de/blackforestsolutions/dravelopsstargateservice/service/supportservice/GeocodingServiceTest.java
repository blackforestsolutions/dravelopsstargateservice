package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.CoordinateParsingException;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.WaypointsObjectMother.getFurtwangenExampleWaypoints;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.WaypointsObjectMother.getMannheimUniversityToMannheimBerlinerPlatzWaypoints;
import static de.blackforestsolutions.dravelopsstargateservice.configuration.scalar.GraphQlConfiguration.INVALID_FIELD_NAME;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GeocodingServiceTest {

    private final GeocodingService classUnderTest = new GeocodingServiceImpl();


    @Test
    void test_extractCoordinateFrom_correct_positive_longitude_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = 180.0d;
        double testLatitude = 0.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        Point result = classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField);

        assertThat(result).isEqualToComparingFieldByField(new Point.PointBuilder(testLongitude, testLatitude).build());
    }

    @Test
    void test_extractCoordinateFrom_wrong_positive_longitude_latitude_and_errorFields_throws_exception() {
        double testLongitude = 180.1d;
        double testLatitude = 0.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        CoordinateParsingException exception = assertThrows(CoordinateParsingException.class, () -> classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField));
        assertThat(exception.getExtensions().size()).isEqualTo(1);
        assertThat(exception.getExtensions().get(INVALID_FIELD_NAME)).isEqualTo(testLongitudeErrorField);
    }

    @Test
    void test_extractCoordinateFrom_correct_negative_longitude_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = -180.0d;
        double testLatitude = 0.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        Point result = classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField);

        assertThat(result).isEqualToComparingFieldByField(new Point.PointBuilder(testLongitude, testLatitude).build());
    }

    @Test
    void test_extractCoordinateFrom_wrong_negative_longitude_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = -180.1d;
        double testLatitude = 0.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        CoordinateParsingException exception = assertThrows(CoordinateParsingException.class, () -> classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField));
        assertThat(exception.getExtensions().size()).isEqualTo(1);
        assertThat(exception.getExtensions().get(INVALID_FIELD_NAME)).isEqualTo(testLongitudeErrorField);
    }

    @Test
    void test_extractCoordinateFrom_longitude_correct_positive_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = 0.0d;
        double testLatitude = 90.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        Point result = classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField);

        assertThat(result).isEqualToComparingFieldByField(new Point.PointBuilder(testLongitude, testLatitude).build());
    }

    @Test
    void test_extractCoordinateFrom_longitude_wrong_positive_latitude_and_errorFields_throws_exception() {
        double testLongitude = 0.0d;
        double testLatitude = 90.1d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        CoordinateParsingException exception = assertThrows(CoordinateParsingException.class, () -> classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField));
        assertThat(exception.getExtensions().size()).isEqualTo(1);
        assertThat(exception.getExtensions().get(INVALID_FIELD_NAME)).isEqualTo(testLatitudeErrorField);
    }

    @Test
    void test_extractCoordinateFrom_longitude_correct_negative_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = 0.0d;
        double testLatitude = -90.0d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        Point result = classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField);

        assertThat(result).isEqualToComparingFieldByField(new Point.PointBuilder(testLongitude, testLatitude).build());
    }

    @Test
    void test_extractCoordinateFrom_longitude_wrong_negative_latitude_and_errorFields_returns_correct_point() {
        double testLongitude = 0.0d;
        double testLatitude = -90.1d;
        String testLongitudeErrorField = "longitude";
        String testLatitudeErrorField = "latitude";

        CoordinateParsingException exception = assertThrows(CoordinateParsingException.class, () -> classUnderTest.extractCoordinateFrom(testLongitude, testLatitude, testLongitudeErrorField, testLatitudeErrorField));
        assertThat(exception.getExtensions().size()).isEqualTo(1);
        assertThat(exception.getExtensions().get(INVALID_FIELD_NAME)).isEqualTo(testLatitudeErrorField);
    }

    @Test
    void test_decodePolylineFrom_encoded_polyline_in_furtwangen_return_correct_track() {
        String encodedPolylineTestData = "mtodHyhpo@@HVbDPVHlABAl@QRIDN|@Sd@Gx@Ob@EDp@FfAD~@@JDd@Fd@@JPt@v@{@LM";
        LinkedList<Point> expectedTrack = getFurtwangenExampleWaypoints();

        LinkedList<Point> result = classUnderTest.decodePolylineFrom(encodedPolylineTestData);

        assertThat(result).extracting(
                Point::getX,
                Point::getY
        ).containsExactly(
                tuple(expectedTrack.get(0).getX(), expectedTrack.get(0).getY()),
                tuple(expectedTrack.get(1).getX(), expectedTrack.get(1).getY()),
                tuple(expectedTrack.get(2).getX(), expectedTrack.get(2).getY()),
                tuple(expectedTrack.get(3).getX(), expectedTrack.get(3).getY()),
                tuple(expectedTrack.get(4).getX(), expectedTrack.get(4).getY()),
                tuple(expectedTrack.get(5).getX(), expectedTrack.get(5).getY()),
                tuple(expectedTrack.get(6).getX(), expectedTrack.get(6).getY()),
                tuple(expectedTrack.get(7).getX(), expectedTrack.get(7).getY()),
                tuple(expectedTrack.get(8).getX(), expectedTrack.get(8).getY()),
                tuple(expectedTrack.get(9).getX(), expectedTrack.get(9).getY()),
                tuple(expectedTrack.get(10).getX(), expectedTrack.get(10).getY()),
                tuple(expectedTrack.get(11).getX(), expectedTrack.get(11).getY()),
                tuple(expectedTrack.get(12).getX(), expectedTrack.get(12).getY()),
                tuple(expectedTrack.get(13).getX(), expectedTrack.get(13).getY()),
                tuple(expectedTrack.get(14).getX(), expectedTrack.get(14).getY()),
                tuple(expectedTrack.get(15).getX(), expectedTrack.get(15).getY()),
                tuple(expectedTrack.get(16).getX(), expectedTrack.get(16).getY()),
                tuple(expectedTrack.get(17).getX(), expectedTrack.get(17).getY()),
                tuple(expectedTrack.get(18).getX(), expectedTrack.get(18).getY()),
                tuple(expectedTrack.get(19).getX(), expectedTrack.get(19).getY()),
                tuple(expectedTrack.get(20).getX(), expectedTrack.get(20).getY()),
                tuple(expectedTrack.get(21).getX(), expectedTrack.get(21).getY()),
                tuple(expectedTrack.get(22).getX(), expectedTrack.get(22).getY())
        );
    }

    @Test
    void test_decodePolylineFrom_encoded_polyline_return_correct_track() {
        String encodedPolylineTestData = "l`fdPnvl{U";
        Point expectedPoint = new Point.PointBuilder(-120.0012d, -89.984230d).build();

        LinkedList<Point> result = classUnderTest.decodePolylineFrom(encodedPolylineTestData);

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0)).isEqualToComparingFieldByFieldRecursively(expectedPoint);
    }

    @Test
    void test_decodePolylineFrom_polyline_returns_track_between_mannheim_university_and_mannheim_berlin_place() {
        String encodedPolylineTestData = "sn_mH{~sr@i@x\\zJnp@";
        LinkedList<Point> expectedTrack = getMannheimUniversityToMannheimBerlinerPlatzWaypoints();

        LinkedList<Point> result = classUnderTest.decodePolylineFrom(encodedPolylineTestData);

        assertThat(result).extracting(
                Point::getX,
                Point::getY
        ).containsExactly(
                tuple(expectedTrack.get(0).getX(), expectedTrack.get(0).getY()),
                tuple(expectedTrack.get(1).getX(), expectedTrack.get(1).getY()),
                tuple(expectedTrack.get(2).getX(), expectedTrack.get(2).getY())
        );
    }
}
