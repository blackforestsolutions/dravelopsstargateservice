package de.blackforestsolutions.dravelopsstargateservice.service.supportservice;

import de.blackforestsolutions.dravelopsdatamodel.util.ApiToken;
import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopstestdatamodel.objectmothers.ApiTokenObjectMother.*;
import static org.assertj.core.api.Assertions.assertThat;

class RequestTokenHandlerServiceTest {

    private final RequestTokenHandlerService classUnderTest = new RequestTokenHandlerServiceImpl();

    @Test
    void test_getRequestApiTokenWith_userRequestToken_and_configuredOtpMapperApiToken_returns_merged_token_for_mapperServceCall() {
        ApiToken userRequestApiToken = getUserRequestToken();
        ApiToken configuredOtpMapperApiToken = getConfiguredOtpMapperApiToken();

        ApiToken result = classUnderTest.getRequestApiTokenWith(userRequestApiToken, configuredOtpMapperApiToken);

        assertThat(result).isEqualToComparingFieldByField(getOtpMapperApiToken());
    }

    @Test
    void test_() {

        ApiToken result = classUnderTest.
    }
}
