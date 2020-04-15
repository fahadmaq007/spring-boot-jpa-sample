package com.maqs.springboot.sample.controller;

import com.maqs.springboot.sample.exceptions.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

/**
 * Exception Handler for RestFul Services.
 * This is a single place exception handling mechanism 
 * where the Business Exceptions are meant to be notified to the Clients 
 * & rest will be thrown as Internal Server Errors.
 * 
 * @author Maqbool.Ahmed
 * 
 */
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler({ServiceException.class})
	public void handle() {}
}
