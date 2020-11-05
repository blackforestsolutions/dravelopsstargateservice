package de.blackforestsolutions.dravelopsstargateservice;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("bw-dev")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TravelPointResolverIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void test_getTravelPointsBy_min_parameters_graphql_file_returns_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/bw-get-travelpoints-min-parameters.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getTravelPointsBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getTravelPointsBy").get(0).size()).isGreaterThan(0);
    }

    @Test
    void test_getJourneysBy_no_result_graphql_file_returns_zero_journey() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-travelpoints-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getTravelPointsBy").size()).isEqualTo(0);
    }
}
