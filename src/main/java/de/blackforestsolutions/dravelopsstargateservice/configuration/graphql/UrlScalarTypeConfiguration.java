package de.blackforestsolutions.dravelopsstargateservice.configuration.graphql;

import graphql.schema.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.context.annotation.Bean;

import java.net.URL;

@Slf4j
@SpringBootConfiguration
public class UrlScalarTypeConfiguration {

    private static final String URL_SCALAR_NAME = "URL";

    @Bean
    public GraphQLScalarType url() {
        Coercing<URL, String> coercing = new Coercing<>() {

            @Override
            public String serialize(Object url) throws CoercingSerializeException {
                try {
                    return url.toString();
                } catch (Exception e) {
                    log.error("Parsing url to string was not sucessfull: ", e);
                    return null;
                }
            }

            @Override
            public URL parseValue(Object urlString) throws CoercingParseValueException {
                return null;
            }

            @Override
            public URL parseLiteral(Object urlString) throws CoercingParseLiteralException {
                return null;
            }
        };

        return GraphQLScalarType.newScalar()
                .name(URL_SCALAR_NAME)
                .coercing(coercing)
                .description("The URL of a TravelProvider")
                .build();
    }
}
