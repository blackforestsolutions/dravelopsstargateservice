package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.AddressAutocompleteVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.JourneyVariables;
import de.blackforestsolutions.dravelopsgeneratedcontent.graphql.NearestTravelPointsVariables;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Slf4j
@SpringBootConfiguration
public class GraphQlConfiguration {

    private static final String CLASS_PATH = "classpath";
    private static final String PLAYGROUND_PATH = "playground";
    private static final String VARIABLES_PATH = "variables";

    private static final String JOURNEY_VARIABLES_JSON_FILE = "journey-variables.json";
    private static final String AUTOCOMPLETE_VARIABLES_JSON_FILE = "autocomplete-addresses-variables.json";
    private static final String NEAREST_TRAVEL_POINTS_VARIABLES_JSON_FILE = "nearest-travelpoints-variables.json";

    @Value("${otp.timeZone}")
    private String timeZone;
    @Value("${test.apitokens[0].departureCoordinateLongitude}")
    private Double departureCoordinateLongitude;
    @Value("${test.apitokens[0].departureCoordinateLatitude}")
    private Double departureCoordinateLatitude;
    @Value("${test.apitokens[0].arrivalCoordinateLongitude}")
    private Double arrivalCoordinateLongitude;
    @Value("${test.apitokens[0].arrivalCoordinateLatitude}")
    private Double arrivalCoordinateLatitude;
    @Value("${test.apitokens[0].dateTime}")
    private String time;
    @Value("${test.apitokens[0].language}")
    private Locale language;
    @Value("${test.apitokens[0].isArrivalDateTime}")
    private Boolean isArrivalDateTime;
    @Value("${test.apitokens[0].radiusInKilometers}")
    private Double radiusInKilometers;
    @Value("${test.apitokens[0].departure}")
    private String text;

    @Bean
    public void setGraphQlPlaygroundJourneyVariables() {
        try {
            DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
            File json = ResourceUtils.getFile(buildJsonVariablesPath(JOURNEY_VARIABLES_JSON_FILE));
            mapper.writeValue(json, buildGraphQlPlaygroundJourneyVariables());
            log.info(JOURNEY_VARIABLES_JSON_FILE.concat(" was successfully updated with configurations."));
        } catch (IOException e) {
            log.error("Error while writing JourneyVariables: ", e);
        }
    }

    @Bean
    public void setGraphQlPlaygroundAutocompleteAddressesVariables() {
        try {
            DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
            File json = ResourceUtils.getFile(buildJsonVariablesPath(AUTOCOMPLETE_VARIABLES_JSON_FILE));
            mapper.writeValue(json, buildGraphQlPlaygroundAddressAutocompleteVariables());
            log.info(AUTOCOMPLETE_VARIABLES_JSON_FILE.concat(" was successfully updated with configurations."));
        } catch (IOException e) {
            log.error("Error while writing AutocompleteAddressesVariables: ", e);
        }
    }

    @Bean
    public void setGraphQlPlaygroundNearestTravelPointVariables() {
        try {
            DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
            File json = ResourceUtils.getFile(buildJsonVariablesPath(NEAREST_TRAVEL_POINTS_VARIABLES_JSON_FILE));
            mapper.writeValue(json, buildGraphQlPlaygroundNearestTravelPointVariables());
            log.info(NEAREST_TRAVEL_POINTS_VARIABLES_JSON_FILE.concat(" was successfully updated with configurations."));
        } catch (IOException e) {
            log.error("Error while writing NearestTravelPointVariables: ", e);
        }
    }

    private JourneyVariables buildGraphQlPlaygroundJourneyVariables() {
        JourneyVariables journeyVariables = new JourneyVariables();

        journeyVariables.setDepartureLatitude(departureCoordinateLatitude);
        journeyVariables.setDepartureLongitude(departureCoordinateLongitude);
        journeyVariables.setArrivalLatitude(arrivalCoordinateLatitude);
        journeyVariables.setArrivalLongitude(arrivalCoordinateLongitude);
        journeyVariables.setDateTime(convertTimeToOffsetDateTimeString(time));
        journeyVariables.setIsArrivalDateTime(isArrivalDateTime);
        journeyVariables.setLanguage(language.toString());

        return journeyVariables;
    }

    private AddressAutocompleteVariables buildGraphQlPlaygroundAddressAutocompleteVariables() {
        AddressAutocompleteVariables addressAutocompleteVariables = new AddressAutocompleteVariables();

        addressAutocompleteVariables.setText(text);
        addressAutocompleteVariables.setLanguage(language.toString());

        return addressAutocompleteVariables;
    }

    private NearestTravelPointsVariables buildGraphQlPlaygroundNearestTravelPointVariables() {
        NearestTravelPointsVariables nearestTravelPointsVariables = new NearestTravelPointsVariables();

        nearestTravelPointsVariables.setLongitude(departureCoordinateLongitude);
        nearestTravelPointsVariables.setLatitude(departureCoordinateLatitude);
        nearestTravelPointsVariables.setLanguage(language.toString());
        nearestTravelPointsVariables.setRadiusInKilometers(radiusInKilometers);

        return nearestTravelPointsVariables;
    }

    private String convertTimeToOffsetDateTimeString(String time) {
        return LocalDate.now()
                .atTime(LocalTime.parse(time, DateTimeFormatter.ISO_LOCAL_TIME))
                .atZone(ZoneId.of(timeZone))
                .plusDays(1L)
                .format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    private String buildJsonVariablesPath(String file) {
        return CLASS_PATH
                .concat(":")
                .concat(PLAYGROUND_PATH)
                .concat(File.separator)
                .concat(VARIABLES_PATH)
                .concat(File.separator)
                .concat(file);
    }
}
