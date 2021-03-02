package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.ApiToken;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestTokenHandlerServiceTest {

    private final RequestTokenHandlerService classUnderTest = new RequestTokenHandlerServiceImpl();

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_and_configuredRoutePersistenceApiToken_returns_merged_token_for_mapperServceCall() {
        ApiToken userRequestApiToken = getJourneyUserRequestToken();
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        ApiToken result = classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken, configuredRoutePersistenceApiToken);

        assertThat(result).isEqualToComparingFieldByFieldRecursively(getRoutePersistenceApiToken());
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_dateTime_as_null_and_configuredRoutePersistenceApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestApiToken.setDateTime(null);
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredRoutePersistenceApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_departureCoordinate_as_null_and_configuredRoutePersistenceApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestApiToken.setDepartureCoordinate(null);
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredRoutePersistenceApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_arrivalCoordinate_as_null_and_configuredRoutePersistenceApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestApiToken.setArrivalCoordinate(null);
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredRoutePersistenceApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_language_as_null_and_configuredRoutePersistenceApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestApiToken.setLanguage(null);
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredRoutePersistenceApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_isArrivalDateTime_as_null_and_configuredRoutePersistenceApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getJourneyUserRequestToken());
        userRequestApiToken.setIsArrivalDateTime(null);
        ApiToken configuredRoutePersistenceApiToken = getConfiguredRoutePersistenceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredRoutePersistenceApiToken));
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_and_configuredPolygonApiToken_returns_merged_token() {
        ApiToken userRequestApiToken = getTravelPointUserRequestToken();
        ApiToken configuredBoxServiceApiToken = getConfiguredBoxServiceApiToken();

        ApiToken result = classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken, configuredBoxServiceApiToken);

        assertThat(result).isEqualToComparingFieldByField(getBoxServiceApiToken());
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_departure_as_null_and_configuredPolygonApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getTravelPointUserRequestToken());
        userRequestApiToken.setDeparture(null);
        ApiToken configuredBoxServiceApiToken = getConfiguredBoxServiceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken.build(), configuredBoxServiceApiToken));
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_language_as_null_and_configuredPolygonApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getTravelPointUserRequestToken());
        userRequestApiToken.setLanguage(null);
        ApiToken configuredBoxServiceApiToken = getConfiguredBoxServiceApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken.build(), configuredBoxServiceApiToken));
    }

}
