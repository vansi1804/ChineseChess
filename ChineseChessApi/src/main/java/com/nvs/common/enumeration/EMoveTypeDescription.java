package com.nvs.common.enumeration;

import lombok.Getter;

@Getter
public enum EMoveTypeDescription {
    UP(':'),
    ACROSS('-'),
    DOWN('/');

    private final char value;

    EMoveTypeDescription(char value) {
        this.value = value;
    }
}
