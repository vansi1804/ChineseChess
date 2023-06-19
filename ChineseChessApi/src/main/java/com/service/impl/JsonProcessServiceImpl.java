package com.service.impl;

import org.springframework.stereotype.Service;

import com.exception.JsonProcessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.service.JsonProcessService;

@Service
public class JsonProcessServiceImpl implements JsonProcessService {

    @Override
    public <U> U readValue(String content, Class<U> valueType) {
        try {
            return new ObjectMapper().readValue(content, valueType);
        } catch (JsonProcessingException e) {
            throw new JsonProcessException(e);
        }
    }
}
