package de.blackforestsolutions.dravelopsstargateservice.objectmothers;

import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.AddressAutocompleteVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.JourneyVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.NearestTravelPointsVariables;


public class GraphQlVariablesObjectMother {

    public static final double TEST_DEPARTURE_LONGITUDE = 8.047d;
    public static final double TEST_DEPARTURE_LATITUDE = 48.013d;
    public static final double TEST_ARRIVAL_LONGITUDE = 7.975d;
    public static final double TEST_ARRIVAL_LATITUDE = 49.073d;
    public static final String TEST_TIME_ZONE = "Europe/Berlin";
    public static final String TEST_TIME = "12:00:00";
    public static final String TEST_WINTER_TIME_UTC = "+01:00";
    public static final String TEST_SUMMER_TIME_UTC = "+02:00";
    public static final boolean TEST_IS_ARRIVAL_DATE_TIME = true;
    public static final String TEST_LANGUAGE = "de";
    public static final String TEST_TEXT = "Freiburg im Breisgau";
    public static final double TEST_RADIUS_IN_KILOMETERS = 1.0d;

    public static JourneyVariables getJourneyVariablesWithWinterTime() {
        JourneyVariables testJourneyVariables = new JourneyVariables();

        testJourneyVariables.setDepartureLongitude(TEST_DEPARTURE_LONGITUDE);
        testJourneyVariables.setDepartureLatitude(TEST_DEPARTURE_LATITUDE);
        testJourneyVariables.setArrivalLongitude(TEST_ARRIVAL_LONGITUDE);
        testJourneyVariables.setArrivalLatitude(TEST_ARRIVAL_LATITUDE);
        testJourneyVariables.setIsArrivalDateTime(TEST_IS_ARRIVAL_DATE_TIME);
        testJourneyVariables.setLanguage(TEST_LANGUAGE);

        return testJourneyVariables;
    }

    public static AddressAutocompleteVariables getAddressAutocompleteVariablesWithNoEmptyFields() {
        AddressAutocompleteVariables testAddressAutocompleteVariables = new AddressAutocompleteVariables();

        testAddressAutocompleteVariables.setText(TEST_TEXT);
        testAddressAutocompleteVariables.setLanguage(TEST_LANGUAGE);

        return testAddressAutocompleteVariables;
    }

    public static NearestTravelPointsVariables getNearestTravelPointsVariablesWithNoEmptyFields() {
        NearestTravelPointsVariables testNearestTravelPointsVariables = new NearestTravelPointsVariables();

        testNearestTravelPointsVariables.setLongitude(TEST_DEPARTURE_LONGITUDE);
        testNearestTravelPointsVariables.setLatitude(TEST_DEPARTURE_LATITUDE);
        testNearestTravelPointsVariables.setRadiusInKilometers(TEST_RADIUS_IN_KILOMETERS);
        testNearestTravelPointsVariables.setLanguage(TEST_LANGUAGE);

        return testNearestTravelPointsVariables;
    }
}
