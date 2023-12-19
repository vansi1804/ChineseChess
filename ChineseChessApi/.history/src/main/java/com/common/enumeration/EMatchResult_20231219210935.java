
package com.common.enumeration;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum EMatchResult {
  WIN(1),
  DRAW(0),
  LOSE(-1);

  private final int value;
}
