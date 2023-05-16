package com.exception;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = true)
public class ExceptionCustom extends RuntimeException {
	private final Map<String, Object> errors;
	
	public ExceptionCustom(Map<String, Object> errors, String msg) {
		super(msg);
		this.errors = errors;
	}
}
