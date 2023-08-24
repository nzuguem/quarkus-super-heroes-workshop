package io.quarkus.workshop.superheroes.hero;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

public class TestUtils {

    private TestUtils() {}

    public static Hero jsonToHero(String json) throws JsonProcessingException {
        return JsonMapper.builder()
            .build().readValue(json, Hero.class);
    }
}
