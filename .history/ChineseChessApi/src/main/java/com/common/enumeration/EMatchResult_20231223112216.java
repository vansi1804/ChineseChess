package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMatchResult {
  WIN(true),
  DRAW(0),
  LOSE(-1);

  private final Boolean value;
}
