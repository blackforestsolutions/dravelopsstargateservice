package de.blackforestsolutions.dravelopsstargateservice;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TravelPointResolverIT {

    @Value("${test.graphql.query.travelpoints[0].path}")
    private String path;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void test_getTravelPointsBy_min_parameters_graphql_file_returns_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource(path);

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getTravelPointsBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getTravelPointsBy").get(0).size()).isGreaterThan(0);
    }

    @Test
    void test_getTravelPointsBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-travelpoints-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getTravelPointsBy").size()).isEqualTo(0);
    }
}
