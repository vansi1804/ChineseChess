package com.nvs.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EIndexDescription {

    BEFORE('t'),
    AFTER('s');

    private final char value;

}
