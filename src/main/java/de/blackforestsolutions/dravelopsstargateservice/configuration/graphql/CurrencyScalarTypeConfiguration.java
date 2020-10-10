package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Currency;

@Slf4j
@SpringBootConfiguration
public class CurrencyScalarTypeConfiguration {

    private static final String Currency_SCALAR_NAME = "Currency";

    @Bean
    public GraphQLScalarType currency() {
        Coercing<Currency, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object currency) throws CoercingSerializeException {
                try {
                    return ((Currency) currency).getCurrencyCode();
                } catch (Exception e) {
                    log.error("Parsing currency to string was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public Currency parseValue(Object currencyString) throws CoercingParseValueException {
                return null;
            }

            @Override
            public Currency parseLiteral(Object currencyString) throws CoercingParseLiteralException {
                return null;
            }
        };

        return GraphQLScalarType.newScalar()
                .name(Currency_SCALAR_NAME)
                .coercing(coercing)
                .description("The currency of a price (String-Type)")
                .build();
    }
}
