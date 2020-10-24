package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.language.StringValue;
import org.junit.jupiter.api.Test;

import java.net.URL;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelProviderObjectMother.getRnvTravelProvider;
import static org.assertj.core.api.Assertions.assertThat;

class UrlScalarTest {

    private final UrlScalar classUnderTest = new UrlScalar();

    @Test
    void test_serialize_url_returns_equivalent_string() {
        URL testUrl = getRnvTravelProvider().getUrl();

        String result = classUnderTest.serialize(testUrl);

        assertThat(result).isEqualTo("http://www.rnv-online.de");
    }

    @Test
    void test_parseValue_string_returns_null() {
        StringValue testString = new StringValue("http://www.rnv-online.de");

        URL result = classUnderTest.parseValue(testString);

        assertThat(result).isNull();
    }

    @Test
    void test_parseLiteral_string_returns_null() {
        StringValue testString = new StringValue("http://www.rnv-online.de");

        URL result = classUnderTest.parseLiteral(testString);

        assertThat(result).isNull();
    }
}
