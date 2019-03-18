package com.yf.summarize.summarize.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class JacksonUtil {
    private static ObjectMapper mapper = new ObjectMapper();

    // Convert Object to String
    public static String bean2Json(Object obj) throws IOException {
        return mapper.writeValueAsString(obj);
    }

    // Convert string to simple Object
    public static <T> T json2Bean(String jsonStr, Class<T> objClass)
            throws IOException {
        return mapper.readValue(jsonStr, objClass);
    }

    // Convert string to List<Object>
    public static <T> T json2Bean(String jsonStr, Class<?> collectionClass, Class<?>... elementClasses) throws IOException {
        if (jsonStr == null) {
            return null;
        }
        JavaType javaType = mapper.getTypeFactory().constructParametricType(collectionClass, elementClasses);
        return mapper.readValue(jsonStr, javaType);

    }
}
