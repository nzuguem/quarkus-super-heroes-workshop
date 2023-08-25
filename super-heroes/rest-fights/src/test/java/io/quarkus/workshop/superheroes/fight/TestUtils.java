package io.quarkus.workshop.superheroes.fight;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class TestUtils {

    private TestUtils() {
    }

    public static Fighters jsonToFighters(String json) throws JsonProcessingException {
        return JsonMapper.builder()
            .build().readValue(json, Fighters.class);
    }
}
