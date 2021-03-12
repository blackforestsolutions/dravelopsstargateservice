package de.blackforestsolutions.dravelopsstargateservice.resolver;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import de.blackforestsolutions.dravelopsdatamodel.TravelPoint;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.RequestHandlerFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.Metrics;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getTribergStationStreetTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class TravelPointResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getAutocompleteAddressesBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/autocompleteAddressesResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(null)))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-autocomplete-addresses-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getAutocompleteAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getAutocompleteAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getAutocompleteAddressesBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-autocomplete-addresses-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAutocompleteAddressesBy").size()).isEqualTo(0);
    }

    @Test
    void test_getAutocompleteAddressesBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/autocompleteAddressesResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(null)))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-autocomplete-addresses-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getAutocompleteAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getAutocompleteAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getAutocompleteAddressesBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/autocompleteAddressesLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-autocomplete-addresses-query-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getNearestAddressesBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/nearestResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(new Distance(0.0d, Metrics.KILOMETERS))))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-addresses-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getNearestAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getNearestAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getNearestAddressesBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-addresses-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getNearestAddressesBy").size()).isEqualTo(0);
    }

    @Test
    void test_getNearestAddressesBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/nearestResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(new Distance(0.0d, Metrics.KILOMETERS))))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-addresses-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getNearestAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getNearestAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getNearestAddressesBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/nearestAddressesLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-addresses-query-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getNearestStationsBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/nearestResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(new Distance(0.0d, Metrics.KILOMETERS))))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-stations-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getNearestStationsBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getNearestStationsBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getNearestStationsBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-stations-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getNearestStationsBy").size()).isEqualTo(0);
    }

    @Test
    void test_getNearestStationsBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/nearestResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint(new Distance(0.0d, Metrics.KILOMETERS))))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-stations-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getNearestStationsBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getNearestStationsBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getNearestStationsBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/nearestStationsLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-stations-query-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getAllStations_with_graphql_file_returns_correct_travelPoints() throws IOException {
        String expectedTravelPointsJson = getResourceFileAsString("json/getAllStationsResponse.json");
        doReturn(Flux.just(
                getBadDuerkheimTravelPoint(),
                getNeckarauTrainStationTravelPoint(),
                getKarlsbaderStreetTravelPoint(),
                getFurtwangenBirkeTravelPoint(),
                getTribergStationStreetTravelPoint())
        ).when(backendApiService).getManyBy(any(ApiToken.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-all-stations-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAllStations").toPrettyString()).isEqualTo(expectedTravelPointsJson);
    }

    @Test
    void test_getAllStations_with_graphql_file_returns_an_empty_array_when_no_results_are_emitted_by_backend() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-all-stations-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAllStations").toPrettyString()).isEqualTo("[ ]");
    }
}
