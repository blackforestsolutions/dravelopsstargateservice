package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestTokenHandlerServiceTest {

    private final RequestTokenHandlerService classUnderTest = new RequestTokenHandlerServiceImpl();

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_and_configuredOtpMapperApiToken_returns_merged_token_for_mapperServceCall() {
        ApiToken userRequestApiToken = getUserRequestToken();
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        ApiToken result = classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken, configuredOtpMapperApiToken);

        assertThat(result).isEqualToComparingFieldByField(getOtpMapperApiToken());
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_optimize_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setOptimize(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_dateTime_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setDateTime(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_departureCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setDepartureCoordinate(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_arrivalCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setArrivalCoordinate(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeJourneyApiTokensWith_userRequestToken_language_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setLanguage(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeJourneyApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_and_configuredPolygonApiToken_returns_merged_token() {
        ApiToken userRequestApiToken = getTravelPointUserRequestToken();
        ApiToken configuredPeliasApiToken = getConfiguredPolygonApiToken();

        ApiToken result = classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken, configuredPeliasApiToken);

        assertThat(result).isEqualToComparingFieldByField(getPolygonApiToken());
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_departure_as_null_and_configuredPolygonApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getTravelPointUserRequestToken());
        userRequestApiToken.setDeparture(null);
        ApiToken configuredPeliasApiToken = getConfiguredPolygonApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken.build(), configuredPeliasApiToken));
    }

    @Test
    void test_mergeTravelPointApiTokensWith_userRequestToken_language_as_null_and_configuredPolygonApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getTravelPointUserRequestToken());
        userRequestApiToken.setLanguage(null);
        ApiToken configuredPeliasApiToken = getConfiguredPolygonApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeTravelPointApiTokensWith(userRequestApiToken.build(), configuredPeliasApiToken));
    }

}
