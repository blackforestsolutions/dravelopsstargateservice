package de.blackforestsolutions.dravelopsstargateservice.configuration;

import org.junit.jupiter.api.Test;

import static de.blackforestsolutions.dravelopsdatamodel.testutil.TestUtils.getResourceFileAsString;

public class GraphQlConfigurationTest {

    private GraphQlConfiguration classUnderTest = new GraphQlConfiguration();

    @Test
    void test_() {

        classUnderTest.setGraphQlPlaygroundJourneyVariables();
        String result = getResourceFileAsString("json/")

    }
}
