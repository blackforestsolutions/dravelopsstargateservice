package de.blackforestsolutions.dravelopsstargateservice.configuration.scalar;

import de.blackforestsolutions.dravelopsstargateservice.model.scalar.CurrencyScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class CurrencyScalarConfiguration {

    @Bean
    public GraphQLScalarType currency() {
        return GraphQLScalarType.newScalar()
                .name(GraphQlConfiguration.CURRENCY_SCALAR_NAME)
                .coercing(new CurrencyScalar())
                .build();
    }

}
