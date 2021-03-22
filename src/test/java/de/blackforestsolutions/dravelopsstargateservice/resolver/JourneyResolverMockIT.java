package de.blackforestsolutions.dravelopsstargateservice.resolver;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsdatamodel.Journey;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.RequestHandlerFunction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Flux;

import java.io.IOException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class JourneyResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getJourneysBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Flux.just(getFurtwangenToWaldkirchJourney()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getJourneysBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getJourneysBy").get(0).toPrettyString()).isEqualTo(expectedJourneyJson);
    }

    @Test
    void test_getJourneysBy_no_result_graphql_file_returns_zero_journey() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getJourneysBy").size()).isEqualTo(0);
    }

    @Test
    void test_getJourneysBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Flux.just(getFurtwangenToWaldkirchJourney()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getJourneysBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getJourneysBy").get(0).toPrettyString()).isEqualTo(expectedJourneyJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_dateTime_error_returns_error_json_with_dateTimeParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/queryDateTimeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-dateTime-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/journeyLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_departureLatitude_error_returns_error_json_with_coordinateParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/journeyDepartureLatitudeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-departure-latitude-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_departureLongitude_error_returns_error_json_with_coordinateParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/journeyDepartureLongitudeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-departure-longitude-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_arrivalLatitude_error_returns_error_json_with_coordinateParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/journeyArrivalLatitudeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-arrival-latitude-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_arrivalLongitude_error_returns_error_json_with_coordinateParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/journeyArrivalLongitudeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-query-arrival-longitude-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

}
