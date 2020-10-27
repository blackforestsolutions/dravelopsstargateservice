package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;

import java.time.Duration;

@Slf4j
public class DurationScalar implements Coercing<Duration, Long> {

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
}
