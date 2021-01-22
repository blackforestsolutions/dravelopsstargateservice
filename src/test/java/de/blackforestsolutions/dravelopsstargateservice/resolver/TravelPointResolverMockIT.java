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

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getStuttgarterStreetOneTravelPoint;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TravelPointResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getTravelPointsBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/travelPointResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-travelpoints-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getTravelPointsBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getTravelPointsBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getTravelPointsBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {
        doReturn(Flux.empty()).when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-travelpoints-query-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getTravelPointsBy").size()).isEqualTo(0);
    }

    @Test
    void test_getTravelPointsBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedTravelPointJson = getResourceFileAsString("json/travelPointResponse.json");
        doReturn(Flux.just(getStuttgarterStreetOneTravelPoint()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(TravelPoint.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("customer/bw-get-travelpoints-query-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getTravelPointsBy").size()).isGreaterThan(0);
        assertThat(response.readTree().get("data").get("getTravelPointsBy").get(0).toPrettyString()).isEqualTo(expectedTravelPointJson);
    }

    @Test
    void test_getTravelPointsBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/travelPointLanguageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-travelpoints-query-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }
}
