package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.language.IntValue;
import org.junit.jupiter.api.Test;

import java.math.BigInteger;
import java.time.Duration;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.LegObjectMother.getGrosshausbergToFurtwangenIlbenstreetLeg;
import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.WaypointsObjectMother.getExampleWaypoints;
import static org.assertj.core.api.Assertions.assertThat;

class DurationScalarTest {

    private final DurationScalar classUnderTest = new DurationScalar();

    @Test
    void test_serialize_duration_returns_correct_duration_in_minutes() {
        Duration testDuration = getGrosshausbergToFurtwangenIlbenstreetLeg(getExampleWaypoints()).getDelayInMinutes();

        long result = classUnderTest.serialize(testDuration);

        assertThat(result).isEqualTo(testDuration.toMinutes());
    }

    @Test
    void test_serialize_duration_returns_null_when_cast_failed() {
        String wrongDurationType = "2L";

        Long result = classUnderTest.serialize(wrongDurationType);

        assertThat(result).isNull();
    }

    @Test
    void test_parseValue_integer_returns_null() {
        IntValue testInteger = new IntValue(new BigInteger("2"));

        Duration result = classUnderTest.parseValue(testInteger);

        assertThat(result).isNull();
    }

    @Test
    void test_parseLiteral_integer_returns_null() {
        IntValue testInteger = new IntValue(new BigInteger("2"));

        Duration result = classUnderTest.parseLiteral(testInteger);

        assertThat(result).isNull();
    }


}
