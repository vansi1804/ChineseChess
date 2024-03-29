package com.config.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionCustomize extends RuntimeException {

	private final Object errors;

    public ExceptionCustomize() {
		super();
        this.errors = null;
	}
    

	public ExceptionCustomize(String msg, Object errors) {
		super(msg);
		this.errors = errors;
	}
    
}
