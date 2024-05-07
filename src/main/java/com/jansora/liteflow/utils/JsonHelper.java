package com.jansora.liteflow.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Map;

/**
 * @author jansora
 */
@Slf4j
public final class JsonHelper {

    /**
     * deserialize bytes to json node
     *
     * @param bytes bytes of json string
     * @return json node
     * @throws RuntimeException when deserialize occur an error
     */
    public static JsonNode read(byte[] bytes) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readTree(bytes);
        } catch (IOException e) {
            log.error("read json bytes to json node failed, exception", e);
            throw new RuntimeException(String.format("deserialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * deserialize json string to object
     *
     * @param jsonString json string
     * @param clazz      deserialized object class
     * @param <T>        type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> T read(String jsonString, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        try {
            return mapper.readValue(jsonString, clazz);
        } catch (IOException e) {
            log.error("read json {} to {} failed, exception", jsonString, clazz, e);
            throw new RuntimeException(String.format("deserialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * deserialize json string to object
     *
     * @param jsonString json string
     * @param type       deserialized type reference
     * @param <T>        type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> T read(String jsonString, TypeReference<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(jsonString, type);
        } catch (IOException e) {
            log.error("read json {} to {} failed, exception", jsonString, type, e);
            throw new RuntimeException(String.format("deserialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * deserialize json string to object
     *
     * @param bytes bytes of json string
     * @param type  deserialized type reference
     * @param <T>   type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> T read(byte[] bytes, TypeReference<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.readValue(bytes, type);
        } catch (IOException e) {
            log.error("read json bytes to {} failed, exception", type, e);
            throw new RuntimeException(String.format("deserialize occur an error, {%s}", e.getLocalizedMessage()));
        }
    }

    /**
     * deserialize json string to object
     *
     * @param bytes bytes of json string
     * @param clazz deserialized object class
     * @param <T>   type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> T read(byte[] bytes, Class<T> clazz) {
        ObjectMapper mapper = new ObjectMapper().enable(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_USING_DEFAULT_VALUE);
        try {
            return mapper.readValue(bytes, clazz);
        } catch (IOException e) {
            log.error("read json bytes to {} failed, exception", clazz, e);
            throw new RuntimeException(String.format("deserialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * serialize object to json string
     *
     * @param jsonObject json object
     * @return json string
     * @throws RuntimeException when serialize occur an error
     */
    public static String write(Object jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.error("write jsonObject {} failed, exception", jsonObject, e);
            throw new RuntimeException(String.format("serialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * serialize object to pretty json string
     *
     * @param jsonObject json object
     * @return json string
     * @throws RuntimeException when serialize occur an error
     */
    public static String writePretty(Object jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonObject);
        } catch (JsonProcessingException e) {
            log.error("write jsonObject {} failed, exception", jsonObject, e);
            throw new RuntimeException(String.format("serialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * serialize object to bytes of json string
     *
     * @param jsonObject json object
     * @return json string
     * @throws RuntimeException when serialize occur an error
     */
    public static byte[] writeAsByte(Object jsonObject) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsBytes(jsonObject);
        } catch (JsonProcessingException e) {
            log.error("write jsonObject {} failed, exception", jsonObject, e);
            throw new RuntimeException(String.format("serialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * serialize object to another object
     *
     * @param object json object
     * @param type   deserialized type reference
     * @param <T>    type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> T convert(Object object, TypeReference<T> type) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.convertValue(object, type);
        } catch (IllegalArgumentException e) {
            log.error("convert object {} failed, exception", object.getClass(), e);
            throw new RuntimeException(String.format("serialize occur an error, {%s}", e.getMessage()));
        }
    }

    /**
     * serialize object to another object
     *
     * @param object json object
     * @param type   deserialized type reference
     * @param <T>    type of deserialized object
     * @return deserialized object
     * @throws RuntimeException when deserialize occur an error
     */
    public static <T> JsonNode valueToTree(Map<String, T> map) {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.valueToTree(map);
        } catch (IllegalArgumentException e) {
            log.error("convert object {} failed, exception", map.getClass(), e);
            throw new RuntimeException(String.format("serialize occur an error, {%s}", e.getMessage()));
        }
    }
}
