package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EPiece {
    General("Tướng"),
    Advisor("Sĩ"),
    Elephant("Tượng"),
    Horse("Mã"),
    Chariot("Xe"),
    Cannon("Pháo"),
    Soldier("Binh");

    private final String value;
}
