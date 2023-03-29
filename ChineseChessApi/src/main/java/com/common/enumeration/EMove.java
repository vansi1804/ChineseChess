package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMove {
    Up(':'),
    Across('-'),
    Down('/');

    private final char value;
}
