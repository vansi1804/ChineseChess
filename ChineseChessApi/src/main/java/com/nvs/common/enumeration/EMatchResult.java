package com.nvs.common.enumeration;

import lombok.Getter;

@Getter
public enum EMatchResult {
    WIN(1),
    DRAW(0),
    LOSE(-1);

    private final int value;

    EMatchResult(int value) {
        this.value = value;
    }
}
