package de.blackforestsolutions.dravelopsstargateservice;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PolygonResolverIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void test_getOperatingArea_with_graphql_file_returns_polygon() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-polygon-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getOperatingArea")).isNotNull();
        assertThat(response.readTree().findValues("points").size()).isGreaterThan(0);
    }
}
