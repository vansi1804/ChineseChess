package com.exception;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ExceptionCustom extends RuntimeException {
	private Object entity;
	private Object errorMessage;
	private Object field;
	private Object value;

	public ExceptionCustom(String msg) {
		super(msg);
	}

	@Override
	public String toString() {
		return entity + " " + errorMessage + " " + field + " " + value + "\n";
	}

}
