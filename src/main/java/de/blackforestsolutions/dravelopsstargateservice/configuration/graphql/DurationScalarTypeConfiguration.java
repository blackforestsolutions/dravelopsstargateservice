package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.time.Duration;

@Slf4j
@SpringBootConfiguration
public class DurationScalarTypeConfiguration {

    private static final String DURATION_SCALAR_NAME = "Duration";

    @Bean
    public GraphQLScalarType duration() {
        Coercing<Duration, Long> coercing = new Coercing<>() {

            @Override
            public Long serialize(Object duration) throws CoercingSerializeException {
                try {
                    return ((Duration) duration).toMinutes();
                } catch (Exception e) {
                    log.error("Parsing duration to long was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public Duration parseValue(Object durationLong) throws CoercingParseValueException {
                return null;
            }

            @Override
            public Duration parseLiteral(Object durationLong) throws CoercingParseLiteralException {
                return null;
            }
        };

        return GraphQLScalarType.newScalar()
                .name(DURATION_SCALAR_NAME)
                .coercing(coercing)
                .description("The duration of a leg in minutes (Int-Type)")
                .build();
    }
}
