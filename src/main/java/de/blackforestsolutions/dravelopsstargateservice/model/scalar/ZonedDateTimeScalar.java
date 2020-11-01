package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class ZonedDateTimeScalar implements Coercing<ZonedDateTime, String> {

    @Override
    public String serialize(Object zonedDateTime) throws CoercingSerializeException {
        try {
            return ((ZonedDateTime) zonedDateTime).format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        } catch (Exception e) {
            log.error("Parsing ZonedDateTime to string was not sucessfull: ", e);
            return "";
        }
    }

    @Override
    public ZonedDateTime parseValue(Object zonedDateTimeString) throws CoercingParseValueException {
        return null;
    }

    @Override
    public ZonedDateTime parseLiteral(Object zonedDateTimeString) throws CoercingParseLiteralException {
        return null;
    }
}
