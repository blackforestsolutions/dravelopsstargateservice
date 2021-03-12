package de.blackforestsolutions.dravelopsstargateservice.resolver;

import com.graphql.spring.boot.test.GraphQLResponse;
import com.graphql.spring.boot.test.GraphQLTestTemplate;
import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import org.junit.jupiter.api.Test;
import org.locationtech.jts.geom.Polygon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.annotation.DirtiesContext;
import reactor.core.publisher.Mono;

import java.io.IOException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.PolygonObjectMother.getPolygonWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doReturn;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_CLASS)
class PolygonResolverMockIT {

    @Autowired
    private GraphQLTestTemplate graphQLTestTemplate;

    @MockBean
    private BackendApiService backendApiService;

    @Test
    void test_getOperatingArea_returns_a_correct_polygon() throws IOException {
        String expectedPolygonJson = getResourceFileAsString("json/getOperatingAreaResponse.json");
        doReturn(Mono.just(getPolygonWithNoEmptyFields()))
                .when(backendApiService).getOneBy(any(ApiToken.class), eq(Polygon.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-polygon-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().findValues("getOperatingArea")).isNotNull();
        assertThat(response.readTree().get("data").get("getOperatingArea").toPrettyString()).isEqualTo(expectedPolygonJson);
    }

    @Test
    void test_getOperatingArea_with_no_result_from_backend_returns_an_empty_polygon() throws IOException {
        String expectedPolygonJson = getResourceFileAsString("json/getOperatingAreaNoResponse.json");
        doReturn(Mono.empty()).when(backendApiService).getOneBy(any(ApiToken.class), eq(Polygon.class));

        GraphQLResponse response = graphQLTestTemplate.postForResource("graphql/get-polygon-query.graphql");

        assertThat(response.isOk()).isTrue();
        assertThat(response.readTree().get("data").get("getOperatingArea")).isNotNull();
        assertThat(response.readTree().get("data").get("getOperatingArea").toPrettyString()).isEqualTo(expectedPolygonJson);
    }

}
