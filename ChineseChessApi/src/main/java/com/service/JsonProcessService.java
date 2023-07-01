package com.service;

public interface JsonProcessService{

    <U> U readValue(String content, Class<U> valueType);
    
}
