package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.language.StringValue;
import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.util.Locale;

@Slf4j
@SpringBootConfiguration
public class LocaleScalarTypeConfiguration {

    private static final String LOCALE_SCALAR_NAME = "Locale";

    @Bean
    public GraphQLScalarType locale() {
        Coercing<Locale, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object locale) throws CoercingSerializeException {
                try {
                    return ((Locale) locale).getDisplayName();
                } catch (Exception e) {
                    log.error("Parsing locale to string was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public Locale parseValue(Object urlString) throws CoercingParseValueException {
                try {
                    return Locale.forLanguageTag(((StringValue) urlString).getValue());
                } catch (Exception e) {
                    log.error("Parsing string to locale was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public Locale parseLiteral(Object urlString) throws CoercingParseLiteralException {
                try {
                    return Locale.forLanguageTag(((StringValue) urlString).getValue());
                } catch (Exception e) {
                    log.error("Parsing string to locale was not sucessfull: ", e);
                    return null;
                }
            }
        };

        return GraphQLScalarType.newScalar()
                .name(LOCALE_SCALAR_NAME)
                .coercing(coercing)
                .description("language")
                .build();
    }
}
