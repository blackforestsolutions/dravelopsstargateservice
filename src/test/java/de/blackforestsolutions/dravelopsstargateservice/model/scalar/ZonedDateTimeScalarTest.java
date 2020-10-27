package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.language.StringValue;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.TravelPointObjectMother.getFurtwangenIlbenstreetTravelPoint;
import static org.assertj.core.api.Assertions.assertThat;

class ZonedDateTimeScalarTest {

    private final ZonedDateTimeScalar classUnderTest = new ZonedDateTimeScalar();

    @Test
    void test_serialize_zonedDateTime_returns_time_as_string() {
        ZonedDateTime testZonedDateTime = getFurtwangenIlbenstreetTravelPoint().getArrivalTime();

        String result = classUnderTest.serialize(testZonedDateTime);

        assertThat(result).isEqualTo("2020-09-30T13:20:59+02:00");
    }

    @Test
    void test_serialize_wrong_zonedDateTime_returns_empty_string_when_cast_failed() {
        String wrongZonedDateTimeFormat = "2020";

        String result = classUnderTest.serialize(wrongZonedDateTimeFormat);

        assertThat(result).isEmpty();
    }

    @Test
    void test_parseValue_string_returns_null() {
        StringValue testString = new StringValue("2020-09-30T13:20:59+02:00");

        ZonedDateTime result = classUnderTest.parseValue(testString);

        assertThat(result).isNull();
    }


    @Test
    void test_parseLiteral_string_returns_null() {
        StringValue testString = new StringValue("2020-09-30T13:20:59+02:00");

        ZonedDateTime result = classUnderTest.parseLiteral(testString);

        assertThat(result).isNull();
    }
}
