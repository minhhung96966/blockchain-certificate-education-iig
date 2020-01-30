package com.minhhung.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MyResourceNotFoundHandler extends RuntimeException{

     	private static final long serialVersionUID = 1L;

		@ResponseStatus(HttpStatus.NOT_FOUND)
	    public String handleResourceNotFoundException() {
	        return "/404";
	    }
}
