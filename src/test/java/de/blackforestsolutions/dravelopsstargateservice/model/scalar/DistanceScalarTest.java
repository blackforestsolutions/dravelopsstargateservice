package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.language.FloatValue;
import org.junit.jupiter.api.Test;
import org.springframework.data.geo.Distance;

import java.math.BigDecimal;

import static de.blackforestsolutions.dravelopsdatamodel.objectmothers.LegObjectMother.getGrosshausbergToFurtwangenIlbenstreetLeg;
import static org.assertj.core.api.Assertions.assertThat;

class DistanceScalarTest {

    private final DistanceScalar classUnderTest = new DistanceScalar();

    @Test
    void test_serialize_distance_returns_correct_distance_in_kilometers() {
        Distance testDistance = getGrosshausbergToFurtwangenIlbenstreetLeg().getDistanceInKilometers();

        double result = classUnderTest.serialize(testDistance);

        assertThat(result).isEqualTo(0.977d);
    }

    @Test
    void test_serialize_distance_returns_null_when_cast_failed() {
        String wrongDistanceType = "0.456d";

        Double result = classUnderTest.serialize(wrongDistanceType);

        assertThat(result).isNull();
    }

    @Test
    void test_parseValue_float_returns_null() {
        FloatValue testFloat = new FloatValue(new BigDecimal("0.456"));

        Distance result = classUnderTest.parseValue(testFloat);

        assertThat(result).isNull();
    }

    @Test
    void test_parseLiteral_float_returns_null() {
        FloatValue testFloat = new FloatValue(new BigDecimal("0.456"));

        Distance result = classUnderTest.parseLiteral(testFloat);

        assertThat(result).isNull();
    }
}
