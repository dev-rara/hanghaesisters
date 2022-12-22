package com.team6.hanghaesisters.util;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Converter
@Component
@RequiredArgsConstructor
public class PasswordEncConverter implements AttributeConverter<String, String> {

    private final PasswordEncoder passwordEncoder;

    @Override
    public String convertToDatabaseColumn(String attribute) {
        return passwordEncoder.encode(attribute);
    }

    @Override
    public String convertToEntityAttribute(String dbData) {
        return dbData;
    }
}