package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMatchResult {
  WIN(true),
  DRAW(null),
  LOSE(false);

  private final Boolean value;
}
