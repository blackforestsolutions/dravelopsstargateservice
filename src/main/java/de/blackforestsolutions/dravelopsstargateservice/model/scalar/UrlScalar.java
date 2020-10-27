package de.blackforestsolutions.dravelopsstargateservice.model.scalar;

import graphql.schema.Coercing;
import graphql.schema.CoercingParseLiteralException;
import graphql.schema.CoercingParseValueException;
import graphql.schema.CoercingSerializeException;

import java.net.URL;

public class UrlScalar implements Coercing<URL, String> {

    @Override
    public String serialize(Object url) throws CoercingSerializeException {
        return url.toString();
    }

    @Override
    public URL parseValue(Object urlString) throws CoercingParseValueException {
        return null;
    }

    @Override
    public URL parseLiteral(Object urlString) throws CoercingParseLiteralException {
        return null;
    }
}
