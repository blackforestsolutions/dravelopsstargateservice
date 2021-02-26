package de.blackforestsolutions.dravelopsstargateservice.resolver;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import de.blackforestsolutions.dravelopsstargateservice.model.api.Polygon;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiService;
import de.blackforestsolutions.dravelopsstargateservice.service.communicationservice.BackendApiServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InOrder;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.getConfiguredPolygonPersistenceApiToken;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.PolygonObjectMother.getPolygonWithNoEmptyFields;
import static de.blackforestsolutions.dravelopsstargateservice.objectmothers.PolygonObjectMother.getFrontendPolygonWithNoEmptyFields;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class PolygonResolverTest {

    private final BackendApiService backendApiService = mock(BackendApiServiceImpl.class);
    private final ApiToken configuredPolygonApiToken = getConfiguredPolygonPersistenceApiToken();

    private final PolygonResolver classUnderTest = new PolygonResolver(backendApiService, configuredPolygonApiToken);

    @BeforeEach
    void init() {
        when(backendApiService.getOneBy(any(ApiToken.class), eq(org.locationtech.jts.geom.Polygon.class)))
                .thenReturn(Mono.just(getPolygonWithNoEmptyFields()));
    }

    @Test
    void test_getOperatingArea_returns_polygon() throws ExecutionException, InterruptedException {

        CompletableFuture<Polygon> result = classUnderTest.getOperatingArea();

        assertThat(result.get()).isEqualToComparingFieldByFieldRecursively(getFrontendPolygonWithNoEmptyFields());
    }

    @Test
    void test_getOperatingArea_is_executed_in_right_order_and_passes_arguments_correctly() {
        ArgumentCaptor<ApiToken> configuredPolygonApiTokenArg = ArgumentCaptor.forClass(ApiToken.class);

        classUnderTest.getOperatingArea();

        InOrder inOrder = inOrder(backendApiService);
        inOrder.verify(backendApiService, times(1)).getOneBy(configuredPolygonApiTokenArg.capture(), eq(org.locationtech.jts.geom.Polygon.class));
        inOrder.verifyNoMoreInteractions();
        assertThat(configuredPolygonApiTokenArg.getValue()).isEqualToComparingFieldByField(getConfiguredPolygonPersistenceApiToken());
    }

    @Test
    void test_getOperatingArea_returns_an_empty_polygon_when_no_polygon_could_be_calculated_by_our_backend() throws ExecutionException, InterruptedException {
        when(backendApiService.getOneBy(any(ApiToken.class), eq(org.locationtech.jts.geom.Polygon.class)))
                .thenReturn(Mono.empty());

        CompletableFuture<Polygon> result = classUnderTest.getOperatingArea();

        assertThat(result.get()).isEqualToComparingFieldByFieldRecursively(new Polygon.PolygonBuilder().build());
    }
}
