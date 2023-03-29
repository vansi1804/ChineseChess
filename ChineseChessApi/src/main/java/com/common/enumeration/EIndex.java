package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EIndex {
    Before('t'),
    After('s');
    
    private final char value;
}