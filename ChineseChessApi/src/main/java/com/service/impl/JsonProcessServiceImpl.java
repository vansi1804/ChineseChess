package com.service.impl;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.springframework.stereotype.Service;

import com.config.exception.JsonProcessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.JsonProcessService;

@Service
public class JsonProcessServiceImpl implements JsonProcessService {

    @Override
    public <U> U readValue(String content, Class<U> valueType) {
        try {
            byte[] jsonBytes = content.getBytes(StandardCharsets.UTF_8);
            return new ObjectMapper().readValue(new String(jsonBytes, StandardCharsets.UTF_8), valueType);
        } catch (IOException e) {
            throw new JsonProcessException(e);
        }
    }

}
