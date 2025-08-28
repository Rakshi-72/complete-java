package com.rakshi.bank.domains.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ConvertBooleanAttributes implements AttributeConverter<Boolean, String> {

    @Override
    public String convertToDatabaseColumn(Boolean attribute) {
        return (attribute != null && attribute) ? "true" : "false";
    }

    @Override
    public Boolean convertToEntityAttribute(String dbData) {
        return "true".equals(dbData) ? Boolean.TRUE : Boolean.FALSE;
    }
}
