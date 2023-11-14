package com.joseleonardo.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class RequiredObjectIsNullException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	public RequiredObjectIsNullException() {
		super("Não é permitido persistir um objeto nulo!");
	}

	public RequiredObjectIsNullException(String msg) {
		super(msg);
	}
	
}
