package de.blackforestsolutions.dravelopsstargateservice.model.exception;

import de.blackforestsolutions.dravelopsstargateservice.configuration.scalar.GraphQlConfiguration;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DateTimeParsingException extends RuntimeException implements GraphQLError {

    private static final String INVALID_FIELD = "dateTime";

    public DateTimeParsingException() {
        super("Times are reprensented as ISO 8601 strings. For example '2011-12-03T10:15:30+01:00'");
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
