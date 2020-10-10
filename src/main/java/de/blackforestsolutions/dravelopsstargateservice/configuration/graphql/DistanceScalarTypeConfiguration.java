package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.data.geo.Distance;

@Slf4j
@SpringBootConfiguration
public class DistanceScalarTypeConfiguration {

    private static final String DISTANCE_SCALAR_NAME = "Distance";

    @Bean
    public GraphQLScalarType distance() {
        Coercing<Distance, Double> coercing = new Coercing<>() {
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
        };

        return GraphQLScalarType.newScalar()
                .name(DISTANCE_SCALAR_NAME)
                .coercing(coercing)
                .description("The distance of a leg in metres (Float-Type)")
                .build();
    }
}
