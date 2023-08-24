package io.quarkus.workshop.superheroes.villain;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class TestUtils {

    private TestUtils() {}

    public static Villain jsonToVillain(String json) throws JsonProcessingException {
        return JsonMapper.builder()
            .build().readValue(json, Villain.class);
    }
}
