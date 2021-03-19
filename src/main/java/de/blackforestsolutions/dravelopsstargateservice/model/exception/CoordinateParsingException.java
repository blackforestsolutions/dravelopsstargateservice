package de.blackforestsolutions.dravelopsstargateservice.model.exception;

import de.blackforestsolutions.dravelopsstargateservice.configuration.scalar.GraphQlConfiguration;
import graphql.ErrorClassification;
import graphql.GraphQLError;
import graphql.language.SourceLocation;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class CoordinateParsingException extends RuntimeException implements GraphQLError {

    private final String invalidField;

    public CoordinateParsingException(String invalidField) {
        super("Coordinates use WGS-84 coordinate system. Permissible longitudes are between -180.0 to 180.0 degrees and permissible latitudes are between -90.0 to 90 degrees");
        this.invalidField = invalidField;
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
        return Collections.singletonMap(GraphQlConfiguration.INVALID_FIELD_NAME, invalidField);
    }
}
