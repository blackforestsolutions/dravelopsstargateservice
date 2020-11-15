package de.blackforestsolutions.dravelopsstargateservice.configuration.converter;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeParseException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ZonedDateTimeConverterTest {

    private final ZonedDateTimeConverter classUnderTest = new ZonedDateTimeConverter();

    @BeforeEach
    void init() {
        ReflectionTestUtils.setField(classUnderTest, "timeZone", "Europe/Berlin");
    }

    @Test
    void test_convert_time_with_hour_minute_and_second_returns_zonedDateTime_of_tomorrow() {
        String testData = "13:00:00";

        ZonedDateTime result = classUnderTest.convert(testData);

        assertThat(result.toLocalTime()).isEqualTo(LocalTime.parse(testData));
        assertThat(result.getDayOfMonth()).isEqualTo(LocalDateTime.now().plusDays(1L).getDayOfMonth());
    }

    @Test
    void test_convert_time_with_hour_and_minute_returns_zonedDateTime_of_tomorrow() {
        String testData = "13:00";

        ZonedDateTime result = classUnderTest.convert(testData);

        assertThat(result.toLocalTime()).isEqualTo(LocalTime.parse(testData));
        assertThat(result.getDayOfMonth()).isEqualTo(LocalDateTime.now().plusDays(1L).getDayOfMonth());
    }

    @Test
    void test_convert_time_with_hour_throws_exception() {
        String testData = "13";

        assertThrows(DateTimeParseException.class, () -> classUnderTest.convert(testData));
    }
}
