package ru.improve.unboundedSound.util;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IntegerToBooleanConverter implements AttributeConverter<Boolean, Integer> {

    public static final int TRUE = 1;

    public static final int FALSE = 0;

    @Override
    public Integer convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? TRUE : FALSE;
    }

    @Override
    public Boolean convertToEntityAttribute(Integer dbData) {
        return dbData != null && dbData != FALSE;
    }
}
