package de.blackforestsolutions.dravelopsstargateservice.configuration.scalar;

import de.blackforestsolutions.dravelopsstargateservice.model.scalar.UrlScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class UrlScalarConfiguration {

    @Bean
    public GraphQLScalarType url() {
        return GraphQLScalarType.newScalar()
                .name(GraphQlConfiguration.URL_SCALAR_NAME)
                .coercing(new UrlScalar())
                .build();
    }
}
