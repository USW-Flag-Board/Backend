package com.Flaground.backend.global.common;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.lang.reflect.Field;

public class CustomEnumDeserializer<T extends Enum<T>> extends JsonDeserializer<T> {

    @Override
    public T deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String value = p.getValueAsString();
        final Class<?> dtoClass = p.getCurrentValue().getClass();
        final String fieldName = p.currentName();

        try {
            final Class<T> enumClass = getEnumClass(dtoClass, fieldName);
            return Enum.valueOf(enumClass, value);
        } catch (IllegalArgumentException | NoSuchFieldException e) {
            return null;
        }
    }

    private static <T extends Enum<T>> Class<T> getEnumClass(Class<?> dtoClass, String fieldName) throws NoSuchFieldException {
        Field field = dtoClass.getDeclaredField(fieldName);
        return (Class<T>) field.getType();
    }
}