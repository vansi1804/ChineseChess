package com.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExceptionCustom extends RuntimeException {
	private String entity;
	private String errorMessage;
	private String field;
	private Object value;

	public ExceptionCustom(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return String.format(entity + " " + errorMessage + " " + field + " " + value);
	}

}
