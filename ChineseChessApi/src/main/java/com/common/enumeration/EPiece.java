package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EPiece {

    General("Tướng","Tg", 10000),
    Guard("Sĩ","S", 20),
    Elephant("Tượng","T", 40),
    Horse("Mã","M", 60),
    Chariot("Xe","X", 120),
    Cannon("Pháo","P", 70),
    Soldier("Binh","B", 30);

    private final String fullName;
    
    private final String shortName;

    private final int power;

}
