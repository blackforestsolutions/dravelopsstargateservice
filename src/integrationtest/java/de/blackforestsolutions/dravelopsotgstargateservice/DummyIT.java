package de.blackforestsolutions.dravelopsotgstargateservice;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class DummyIT {

    private final Dummy classUnderTest = new Dummy();

    @Test
    void test_returnTwo_returns_two() {

        int result = classUnderTest.returnTwo();

        assertThat(result).isEqualTo(2);
    }
}
