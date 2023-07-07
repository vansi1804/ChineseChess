package com.config.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionCustomize extends RuntimeException {

	private final Object errors;

	public ExceptionCustomize(String msg, Object errors) {
		super(msg);
		this.errors = errors;
	}
    
}
