package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;
import lombok.extern.slf4j.Slf4j;

import java.util.Currency;

@Slf4j
public class CurrencyScalar implements Coercing<Currency, String> {

    @Override
    public String serialize(Object currency) throws CoercingSerializeException {
        try {
            return ((Currency) currency).getCurrencyCode();
        } catch (Exception e) {
            log.error("Parsing currency to string was not sucessfull: ", e);
            return "";
        }
    }

    @Override
    public Currency parseValue(Object currencyString) throws CoercingParseValueException {
        return null;
    }

    @Override
    public Currency parseLiteral(Object currencyString) throws CoercingParseLiteralException {
        return null;
    }
}
