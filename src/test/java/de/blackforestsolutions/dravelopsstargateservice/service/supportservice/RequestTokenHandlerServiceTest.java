package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.ApiTokenObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RequestTokenHandlerServiceTest {

    private final RequestTokenHandlerService classUnderTest = new RequestTokenHandlerServiceImpl();

    @Test
    void test_mergeApiTokensWith_userRequestToken_and_configuredOtpMapperApiToken_returns_merged_token_for_mapperServceCall() {
        ApiToken userRequestApiToken = getUserRequestToken();
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        ApiToken result = classUnderTest.mergeApiTokensWith(userRequestApiToken, configuredOtpMapperApiToken);

        assertThat(result).isEqualToComparingFieldByField(getOtpMapperApiToken());
    }

    @Test
    void test_mergeApiTokensWith_userRequestToken_optimize_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setOptimize(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeApiTokensWith_userRequestToken_dateTime_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setDateTime(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeApiTokensWith_userRequestToken_departureCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setDepartureCoordinate(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeApiTokensWith_userRequestToken_arrivalCoordinate_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setArrivalCoordinate(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }

    @Test
    void test_mergeApiTokensWith_userRequestToken_language_as_null_and_configuredOtpMapperApiToken_throws_exception() {
        ApiToken.ApiTokenBuilder userRequestApiToken = new ApiToken.ApiTokenBuilder(getUserRequestToken());
        userRequestApiToken.setLanguage(null);
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        assertThrows(NullPointerException.class, () -> classUnderTest.mergeApiTokensWith(userRequestApiToken.build(), configuredOtpMapperApiToken));
    }
}
