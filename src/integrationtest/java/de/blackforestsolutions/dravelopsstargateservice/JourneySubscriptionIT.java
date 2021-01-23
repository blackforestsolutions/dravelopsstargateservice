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
public class JourneySubscriptionIT {

    @Value("${test.graphql.subscription.journeys[0].path}")
    private String path;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void test_getJourneysBy_max_parameters_graphql_file_returns_journeys() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource(path);

        assertThat(response.isOk()).isTrue();
        assertThat(response.getRawResponse().getBody()).isNotEmpty();
        assertThat(response.getRawResponse().getBody()).contains("id");
    }
}
