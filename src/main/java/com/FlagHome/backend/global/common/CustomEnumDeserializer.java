package com.FlagHome.backend.global.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class CustomEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {
    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String value = p.getValueAsString();
        final Class<T> enumClass = getEnumClass(p);

        try {
            return Enum.valueOf(enumClass, value);
        } catch (Exception e) {
            return null;
        }
    }

    private static <T extends Enum<T>> Class<T> getEnumClass(JsonParser p) {
        return (Class<T>) p.getCurrentValue().getClass();
    }
}
