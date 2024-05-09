package com.nvs.common.enumeration;

import lombok.Getter;

@Getter
public enum EIndexDescription {
    BEFORE('t'),
    AFTER('s');

    private final char value;

    EIndexDescription(char value) {
        this.value = value;
    }
}
