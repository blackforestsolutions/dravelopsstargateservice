package de.blackforestsolutions.dravelopsstargateservice.configuration.scalar;

import de.blackforestsolutions.dravelopsstargateservice.model.scalar.ZonedDateTimeScalar;
import graphql.schema.GraphQLScalarType;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

@SpringBootConfiguration
public class ZonedDateTimeScalarConfiguration {

    @Bean
    public GraphQLScalarType zonedDateTime() {
        return GraphQLScalarType.newScalar()
                .name(GraphQlConfiguration.ZONED_DATE_TIME_SCALAR_NAME)
                .coercing(new ZonedDateTimeScalar())
                .build();
    }
}
