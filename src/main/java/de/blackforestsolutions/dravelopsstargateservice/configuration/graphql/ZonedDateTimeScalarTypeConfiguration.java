package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.language.StringValue;
import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.ZonedDateTime;

@Slf4j
@SpringBootConfiguration
public class ZonedDateTimeScalarTypeConfiguration {

    private static final String ZONED_DATE_TIME_SCALAR_NAME = "ZonedDateTime";

    @Bean
    public GraphQLScalarType zonedDateTime() {
        Coercing<ZonedDateTime, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object zonedDateTime) throws CoercingSerializeException {
                try {
                    return zonedDateTime.toString();
                } catch (Exception e) {
                    log.error("Parsing zonedDateTime to string was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public ZonedDateTime parseValue(Object zonedDateTimeString) throws CoercingParseValueException {
                try {
                    return ZonedDateTime.parse(((StringValue) zonedDateTimeString).getValue());
                } catch (Exception e) {
                    log.error("Parsing string to zonedDateTime was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public ZonedDateTime parseLiteral(Object zonedDateTimeString) throws CoercingParseLiteralException {
                try {
                    return ZonedDateTime.parse(((StringValue) zonedDateTimeString).getValue());
                } catch (Exception e) {
                    log.error("Parsing string to zonedDateTime was not sucessfull: ", e);
                    return null;
                }
            }
        };

        return GraphQLScalarType.newScalar()
                .name(ZONED_DATE_TIME_SCALAR_NAME)
                .coercing(coercing)
                .description("The ZonedDateTime has ISO-8601-Standard. An example is '2011-12-03T10:15:30+01:00'")
                .build();
    }
}
