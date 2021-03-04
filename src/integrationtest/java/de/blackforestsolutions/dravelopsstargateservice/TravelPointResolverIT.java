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

    @Value("${test.graphql.query.addresses.autocomplete[0].path}")
    private String autocompleteAddressesPath;
    @Value("${test.graphql.query.addresses.nearest[0].path}")
    private String nearestAddressesPath;

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @Test
    void test_getAutocompleteAddressesBy_min_parameters_graphql_file_returns_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource(autocompleteAddressesPath);

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getAutocompleteAddressesBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getAutocompleteAddressesBy").get(0).size()).isGreaterThan(0);
    }

    @Test
    void test_getAutocompleteAddressesBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-autocomplete-addresses-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAutocompleteAddressesBy").size()).isEqualTo(0);
    }

    @Test
    void test_getNearestAddressesBy_min_parameters_graphql_file_returns_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource(nearestAddressesPath);

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getNearestAddressesBy").size()).isEqualTo(1);
        assertThat(response.readTree().get("data").get("getNearestAddressesBy").get(0).size()).isGreaterThan(0);
    }

    @Test
    void test_getNearestAddressesBy_no_result_graphql_file_returns_zero_travelPoints() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-nearest-addresses-query-no-result.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getNearestAddressesBy").size()).isEqualTo(0);
    }

    @Test
    void test_getAllStations_returns_stations() throws IOException {

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-all-stations-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getAllStations").size()).isGreaterThan(0);
    }
}
