package de.blackforestsolutions.dravelopsstargateservice.resolver;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.JourneyApiService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.JourneyObjectMother.getFurtwangenToWaldkirchJourney;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class JourneyResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private JourneyApiService journeyApiServiceMock;

    @Test
    void test_getJourneysBy_min_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Mono.just(List.of(getFurtwangenToWaldkirchJourney()))).when(journeyApiServiceMock).retrieveJourneysFromApiService(any(ApiToken.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/bw-get-journeys-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getJourneysBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getJourneysBy").get(0).toPrettyString()).isEqualTo(expectedJourneyJson);
    }

    @Test
    void test_getJourneysBy_no_result_graphql_file_returns_zero_journey() throws IOException {
        doReturn(Mono.just(Collections.emptyList())).when(journeyApiServiceMock).retrieveJourneysFromApiService(any(ApiToken.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/bw-get-journeys-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getJourneysBy").size()).isEqualTo(0);
    }

    @Test
    void test_getJourneysBy_max_parameters_graphql_file_returns_a_correct_journey() throws IOException {
        String expectedJourneyJson = getResourceFileAsString("json/furtwangenToWaldkirchResponse.json");
        doReturn(Mono.just(List.of(getFurtwangenToWaldkirchJourney()))).when(journeyApiServiceMock).retrieveJourneysFromApiService(any(ApiToken.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/bw-get-journeys-max-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getJourneysBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getJourneysBy").get(0).toPrettyString()).isEqualTo(expectedJourneyJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_dateTime_error_returns_error_json_with_dateTimeParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/dateTimeErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-dateTime-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

    @Test
    void test_getJourneysBy_graphql_file_with_language_error_returns_error_json_with_languageParsingException() throws IOException {
        String expectedErrorJson = getResourceFileAsString("json/languageErrorResponse.json");

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-journeys-language-error.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().toPrettyString()).isEqualTo(expectedErrorJson);
    }

}
