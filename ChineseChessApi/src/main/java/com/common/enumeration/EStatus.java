package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EStatus {
    Active("Hoạt động"),
    Locked("Khóa");

    private final String value;
}
