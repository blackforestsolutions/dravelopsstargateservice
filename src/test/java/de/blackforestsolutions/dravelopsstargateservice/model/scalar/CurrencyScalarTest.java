package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.language.StringValue;
import org.junit.jupiter.api.Test;

import java.util.Currency;
import java.util.Locale;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.PriceObjectMother.getFurtwangenToWaldkirchPrice;
import static org.assertj.core.api.Assertions.assertThat;

class CurrencyScalarTest {

    private final CurrencyScalar classUnderTest = new CurrencyScalar();

    @Test
    void test_serialize_german_currency_returns_correct_currencyCode() {
        Currency testCurrency = getFurtwangenToWaldkirchPrice().getCurrencyCode();

        String result = classUnderTest.serialize(testCurrency);

        assertThat(result).isEqualTo("EUR");
    }

    @Test
    void test_serialize_us_currency_returns_correct_currencyCode() {
        Currency testCurrency = Currency.getInstance(Locale.US);

        String result = classUnderTest.serialize(testCurrency);

        assertThat(result).isEqualTo("USD");
    }

    @Test
    void test_serialize_german_currency_returns_empty_string_when_cast_failed() {
        String wrongCurrencyType = "EUR";

        String result = classUnderTest.serialize(wrongCurrencyType);

        assertThat(result).isEmpty();
    }

    @Test
    void test_parseValue_string_returns_null() {
        StringValue testString = new StringValue("EUR");

        Currency result = classUnderTest.parseValue(testString);

        assertThat(result).isNull();
    }

    @Test
    void test_parseLiteral_string_returns_null() {
        StringValue testString = new StringValue("EUR");

        Currency result = classUnderTest.parseLiteral(testString);

        assertThat(result).isNull();
    }
}
