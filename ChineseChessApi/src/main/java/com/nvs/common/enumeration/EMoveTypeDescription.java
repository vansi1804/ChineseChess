package com.nvs.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EMoveTypeDescription{

   UP(':'), ACROSS('-'), DOWN('/');

   private final char value;

}
