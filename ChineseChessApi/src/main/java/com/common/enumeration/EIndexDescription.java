package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EIndexDescription {

    BEFORE("t"),   
    AFTER("s");
    
    private final String value;
    
}