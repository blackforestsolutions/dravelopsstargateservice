package de.blackforestsolutions.dravelopsstargateservice.model.exception;

import de.blackforestsolutions.dravelopsstargateservice.configuration.scalar.GraphQlConfiguration;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class LanguageParsingException extends RuntimeException implements GraphQLError {

    private static final String INVALID_FIELD = "language";

    public LanguageParsingException() {
        super("Language is represented as a two letter country code. For example 'de', 'en', 'es'...");
    }

    @Override
    public List<SourceLocation> getLocations() {
        return null;
    }

    @Override
    public ErrorClassification getErrorType() {
        return null;
    }

    @Override
    public Map<String, Object> getExtensions() {
        return Collections.singletonMap(GraphQlConfiguration.INVALID_FIELD_NAME, INVALID_FIELD);
    }
}
