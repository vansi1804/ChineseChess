package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EPiece {
    General("Tướng","Tg"),
    Advisor("Sĩ","S"),
    Elephant("Tượng","T"),
    Horse("Mã","M"),
    Chariot("Xe","X"),
    Cannon("Pháo","P"),
    Soldier("Binh","B");

    private final String fullNameValue;
    private final String shortNameValue;
}
