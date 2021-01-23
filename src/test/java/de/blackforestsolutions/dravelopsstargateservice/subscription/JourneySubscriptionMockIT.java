package de.blackforestsolutions.dravelopsstargateservice.subscription;

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
import reactor.core.publisher.Flux;

import java.io.IOException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.apache.commons.lang3.StringUtils.deleteWhitespace;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JourneySubscriptionMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getJourneysBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Flux.just(getFurtwangenToWaldkirchJourney()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-subscription-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(deleteWhitespace(response.getRawResponse().getBody())).containsOnlyOnce(deleteWhitespace(expectedJourneyJson));
    }

    @Test
    void test_getJourneysBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Flux.just(getFurtwangenToWaldkirchJourney()))
                .when(backendApiService).getManyBy(any(ApiToken.class), any(ApiToken.class), any(RequestHandlerFunction.class), eq(Journey.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("customer/bw-get-journeys-subscription-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(deleteWhitespace(response.getRawResponse().getBody())).containsOnlyOnce(deleteWhitespace(expectedJourneyJson));
    }

    @Test
    void test_getJourneysBy_graphql_file_with_dateTime_error_returns_error_json_with_dateTimeParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/subscriptionDateTimeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-subscription-dateTime-error.graphql");

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
}
