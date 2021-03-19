package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.Point;
import de.blackforestsolutions.dravelopsstargateservice.model.exception.CoordinateParsingException;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsstargateservice.configuration.scalar.GraphQlConfiguration.INVALID_FIELD_NAME;
import static org.assertj.core.api.Assertions.assertThat;
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
}
