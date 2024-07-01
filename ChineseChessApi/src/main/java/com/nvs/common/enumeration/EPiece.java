package com.nvs.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EPiece{

   GENERAL("Tướng", "T.g", 10000),
   GUARD("Sĩ", "S", 20),
   ELEPHANT("Tượng", "T", 40),
   HORSE("Mã", "M", 60),
   CHARIOT("Xe", "X", 120),
   CANNON("Pháo", "P", 70),
   SOLDIER("Binh", "B", 30);

   private final String vietnameseName;

   private final String shortName;

   private final int power;

}
