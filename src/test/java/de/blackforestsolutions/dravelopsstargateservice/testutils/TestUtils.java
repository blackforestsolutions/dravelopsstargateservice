package de.blackforestsolutions.dravelopsstargateservice.testutils;

import de.blackforestsolutions.dravelopsdatamodel.util.DravelOpsJsonMapper;

public class TestUtils {

    /**
     * NEVER USE IN PRODUCTIVE CODE!
     * Parse the given json into object of type pojo
     *
     * @param json the given json
     * @param pojo the class the json has to be parsed
     * @param <T> type of the class the json has to be parsed
     * @return object
     */
    public static <T> T retrieveJsonToPojo(String json, Class<T> pojo) {
        DravelOpsJsonMapper mapper = new DravelOpsJsonMapper();
        return mapper.mapJsonToPojo(json, pojo).block();
    }
}
