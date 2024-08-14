package com.nvs.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public abstract class BaseExceptionCustomize extends RuntimeException {

  protected Object errors;

  public BaseExceptionCustomize() {
    super();
    this.errors = null;
  }

  public BaseExceptionCustomize(String msg) {
    super(msg);
    this.errors = null;
  }

  public BaseExceptionCustomize(String msg, Object errors) {
    super(msg);
    this.errors = errors;
  }
}
