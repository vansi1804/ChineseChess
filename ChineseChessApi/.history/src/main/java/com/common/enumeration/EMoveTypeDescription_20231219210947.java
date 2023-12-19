package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMoveTypeDescription {
  UP(':'),
  ACROSS('-'),
  DOWN('/');

  private final char value;
}
