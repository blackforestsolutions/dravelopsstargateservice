package de.blackforestsolutions.dravelopsstargateservice.configuration;

import de.blackforestsolutions.dravelopsstargateservice.configuration.converter.ZonedDateTimeConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@TestConfiguration
public class ZonedDateTimeConfiguration {

    @Bean
    @ConfigurationPropertiesBinding
    public ZonedDateTimeConverter zonedDateTimeConverter() {
        return new ZonedDateTimeConverter();
    }
}