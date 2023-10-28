package com.capstone.udrive.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

public class JsonUtils {
    static ObjectMapper objectMapper = new ObjectMapper();

    public static String convertObj2Json(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T convertJson2Obj(String json, Class<T> clazz) {
        return objectMapper.convertValue(json, clazz);
    }

    public static <T> List<T> convertJsonArray2List(String json, Class<T> clazz) {
        return objectMapper.convertValue(json, objectMapper.getTypeFactory().constructCollectionType(List.class, clazz));
    }
}
