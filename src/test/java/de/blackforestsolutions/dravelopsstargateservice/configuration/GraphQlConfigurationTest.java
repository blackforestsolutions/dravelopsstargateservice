package de.blackforestsolutions.dravelopsstargateservice.configuration;

import com.fasterxml.jackson.core.JsonProcessingException;
import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.AddressAutocompleteVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.JourneyVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.NearestTravelPointsVariables;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Locale;

import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static de.blackforestsolutions.dravelopsstargateservice.objectmothers.GraphQlVariablesObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;

public class GraphQlConfigurationTest {

    private final GraphQlConfiguration classUnderTest = new GraphQlConfiguration();

    @BeforeEach
    public void init() {
        ReflectionTestUtils.setField(classUnderTest, "timeZone", TEST_TIME_ZONE);
        ReflectionTestUtils.setField(classUnderTest, "departureCoordinateLongitude", TEST_DEPARTURE_LONGITUDE);
        ReflectionTestUtils.setField(classUnderTest, "departureCoordinateLatitude", TEST_DEPARTURE_LATITUDE);
        ReflectionTestUtils.setField(classUnderTest, "arrivalCoordinateLongitude", TEST_ARRIVAL_LONGITUDE);
        ReflectionTestUtils.setField(classUnderTest, "arrivalCoordinateLatitude", TEST_ARRIVAL_LATITUDE);
        ReflectionTestUtils.setField(classUnderTest, "time", TEST_TIME);
        ReflectionTestUtils.setField(classUnderTest, "language", new Locale(TEST_LANGUAGE));
        ReflectionTestUtils.setField(classUnderTest, "isArrivalDateTime", TEST_IS_ARRIVAL_DATE_TIME);
        ReflectionTestUtils.setField(classUnderTest, "radiusInKilometers", TEST_RADIUS_IN_KILOMETERS);
        ReflectionTestUtils.setField(classUnderTest, "text", TEST_TEXT);
    }

    @Test
    void test_setGraphQlPlaygroundJourneyVariables_with_mocked_configs_return_correct_journey_variables() throws JsonProcessingException {
        JourneyVariables expected = getJourneyVariablesWithWinterTime();

        classUnderTest.setGraphQlPlaygroundJourneyVariables();
        String result = getResourceFileAsString("playground/variables/journey-variables.json");

        assertThat(new DravelOpsJsonMapper().readValue(result, JourneyVariables.class)).isEqualToIgnoringGivenFields(expected, "dateTime");
    }

    @Test
    void test_setGraphQlPlaygroundJourneyVariables_with_mocked_configs_returns_correct_date_Time() throws JsonProcessingException {
        ZonedDateTime expectedDateAndOffset = LocalDateTime.now().atZone(ZoneId.of(TEST_TIME_ZONE)).plusDays(1L);
        JourneyVariables expected = getJourneyVariablesWithWinterTime();

        classUnderTest.setGraphQlPlaygroundJourneyVariables();
        String result = getResourceFileAsString("playground/variables/journey-variables.json");

        assertThat(new DravelOpsJsonMapper().readValue(result, JourneyVariables.class).getDateTime()).contains(TEST_TIME);
        assertThat(new DravelOpsJsonMapper().readValue(result, JourneyVariables.class).getDateTime()).contains(expectedDateAndOffset.toLocalDate().toString());
        if (expectedDateAndOffset.getZone().getRules().isDaylightSavings(expectedDateAndOffset.toInstant())) {
            assertThat(new DravelOpsJsonMapper().readValue(result, JourneyVariables.class).getDateTime()).contains(TEST_SUMMER_TIME_UTC);
        } else {
            assertThat(new DravelOpsJsonMapper().readValue(result, JourneyVariables.class).getDateTime()).contains(TEST_WINTER_TIME_UTC);
        }
    }

    @Test
    void test_setGraphQlPlaygroundNearestTravelPointVariables_with_mocked_configs_returns_correct_nearest_travel_points() throws JsonProcessingException {
        NearestTravelPointsVariables expected = getNearestTravelPointsVariablesWithNoEmptyFields();

        classUnderTest.setGraphQlPlaygroundNearestTravelPointVariables();
        String result = getResourceFileAsString("playground/variables/nearest-travelpoints-variables.json");

        assertThat(new DravelOpsJsonMapper().readValue(result, NearestTravelPointsVariables.class)).isEqualToComparingFieldByField(expected);
    }

    @Test
    void test_setGraphQlPlaygroundAutocompleteAddressesVariables_with_mocked_configs_returns_autocomplete_addresses() throws JsonProcessingException {
        AddressAutocompleteVariables expected = getAddressAutocompleteVariablesWithNoEmptyFields();

        classUnderTest.setGraphQlPlaygroundAutocompleteAddressesVariables();
        String result = getResourceFileAsString("playground/variables/autocomplete-addresses-variables.json");

        assertThat(new DravelOpsJsonMapper().readValue(result, AddressAutocompleteVariables.class)).isEqualToComparingFieldByField(expected);
    }
}
