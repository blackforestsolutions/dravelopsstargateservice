package de.blackforestsolutions.dravelopsstargateservice.configuration.scalar;

import de.blackforestsolutions.dravelopsstargateservice.model.scalar.DistanceScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class DistanceScalarConfiguration {

    @Bean
    public GraphQLScalarType distance() {
        return GraphQLScalarType.newScalar()
                .name(GraphQlConfiguration.DISTANCE_SCALAR_NAME)
                .coercing(new DistanceScalar())
                .build();
    }
}
