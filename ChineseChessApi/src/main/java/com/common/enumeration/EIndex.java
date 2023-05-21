package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EIndex {
    BEFORE('t'),   
    AFTER('s');
    
    private final char value;
}