package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.geo.Distance;

@Slf4j
public class DistanceScalar implements Coercing<Distance, Double> {

    @Override
    public Double serialize(Object distance) throws CoercingSerializeException {
        try {
            return ((Distance) distance).getValue();
        } catch (Exception e) {
            log.error("Parsing distance to long was not sucessfull: ", e);
            return null;
        }
    }

    @Override
    public Distance parseValue(Object distanceDouble) throws CoercingParseValueException {
        return null;
    }

    @Override
    public Distance parseLiteral(Object distanceDouble) throws CoercingParseLiteralException {
        return null;
    }

}
