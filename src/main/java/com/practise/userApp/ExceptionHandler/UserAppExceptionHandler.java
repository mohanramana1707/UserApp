package com.practise.userApp.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

@RestControllerAdvice
public class UserAppExceptionHandler {
	
//====================================	 controller EXCEPTION HANDLING  ======================================================
	
	@ResponseStatus(HttpStatus.BAD_REQUEST)  //  e: handling 400- BAD REQUESTs 
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public Map<String,String> handleMethodArgumentNotValidException(MethodArgumentNotValidException exception){  // the exception will show all the invalid exception in json
		
		 final Map<String,String> errorDetails= new HashMap<>();
		 exception.getBindingResult().getFieldErrors().forEach(
				 err-> errorDetails.put(err.getField(),err.getDefaultMessage())
				 );
		 
		 return errorDetails;
	}
	
	
	
	@ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)  //  e: handling 405 -method not allowed 
	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public Map<String,String> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException exception){  // this will give the single msg of all the complete details
		
		 final Map<String,String> errorDetails= new HashMap<>();
		 errorDetails.put("errorMessage", exception.toString());  
		 
		 return errorDetails;
	}
	
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)  //  e: handling 500 -all other exception
	@ExceptionHandler(InternalServerError.class)
	public Map<String,String>handleException (InternalServerError exception){  // this will give the single msg of all the complete details
		
		 final Map<String,String> errorDetails= new HashMap<>();
		 errorDetails.put("errorMessage", exception.toString());  
		 
		 return errorDetails;
	}


}
