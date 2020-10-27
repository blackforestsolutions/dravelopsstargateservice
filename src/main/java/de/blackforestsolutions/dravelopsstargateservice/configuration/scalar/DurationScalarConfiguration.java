package de.blackforestsolutions.dravelopsstargateservice.configuration.scalar;

import de.blackforestsolutions.dravelopsstargateservice.model.scalar.DurationScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class DurationScalarConfiguration {

    @Bean
    public GraphQLScalarType duration() {
        return GraphQLScalarType.newScalar()
                .name(GraphQlConfiguration.DURATION_SCALAR_NAME)
                .coercing(new DurationScalar())
                .build();
    }
}
