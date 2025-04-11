package com.zbinyds.localmessage.util;

import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;

/**
 * @author zbinyds@126.com
 * @since 2025-04-05 12:01
 */
@SuppressWarnings("unused")
public class JsonUtil {
    private static final ObjectMapper JSON_MAPPER = new ObjectMapper();

    public static String toJson(Object value) {
        try {
            return JSON_MAPPER.writeValueAsString(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static String toJsonPretty(Object value) {
        try {
            return JSON_MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(value);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> List<T> fromJsonToList(String str, Class<T> listType) {
        JavaType javaType = JSON_MAPPER.getTypeFactory().constructParametricType(List.class, listType);
        return fromJson(str, javaType);
    }

    public static <T> T fromJson(String content, Class<T> valueType) {
        try {
            return JSON_MAPPER.readValue(content, valueType);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

    public static <T> T fromJson(String content, JavaType valueType) {
        try {
            return JSON_MAPPER.readValue(content, valueType);
        } catch (Exception e) {
            throw new UnsupportedOperationException(e);
        }
    }

}
