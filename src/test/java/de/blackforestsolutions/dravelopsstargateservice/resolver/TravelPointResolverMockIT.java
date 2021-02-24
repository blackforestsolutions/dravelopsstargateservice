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
import reactor.core.publisher.Flux;

import java.io.IOException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.*;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getTribergStationStreetTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class TravelPointResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getAddressesBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/travelPointResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-addresses-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getAddressesBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-addresses-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAddressesBy").size()).isEqualTo(0);
    }

    @Test
    void test_getAddressesBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/travelPointResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("customer/bw-get-travelpoints-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getAddressesBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getAddressesBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getAddressesBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/travelPointLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-addresses-query-language-error.graphql");

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
