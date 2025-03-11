package br.com.pawloandre.users.exception.handler;

import java.time.LocalDateTime;
import java.util.Locale;

import org.springframework.cglib.proxy.UndeclaredThrowableException;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import br.com.pawloandre.users.exception.BusinessException;
import jakarta.annotation.Resource;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

	@Resource
	private MessageSource messageSource;

	HttpHeaders headers() {
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType((MediaType.APPLICATION_JSON));
		return headers;
	}

	ResponseError responseError(String message, HttpStatus statusCode) {
		ResponseError responseError = new ResponseError(LocalDateTime.now(), message, statusCode.value(), "error");
		return responseError;
	}

	@ExceptionHandler(Exception.class)
	ResponseEntity<Object> hendleGeneral(Exception e, WebRequest request) {
		if (e.getClass().isAssignableFrom(UndeclaredThrowableException.class)) {
			UndeclaredThrowableException exception = (UndeclaredThrowableException) e;
			return handleBusinessException((BusinessException) exception.getUndeclaredThrowable(), request);
		} else {
			String message = messageSource.getMessage("error.server", new Object[] { e.getMessage() }, null);
			ResponseError error = responseError(message, HttpStatus.INTERNAL_SERVER_ERROR);
			return handleExceptionInternal(e, error, headers(), HttpStatus.INTERNAL_SERVER_ERROR, request);
		}
	}

	@ExceptionHandler({ BusinessException.class })
	ResponseEntity<Object> handleBusinessException(BusinessException e, WebRequest request) {
		Locale locale = request.getLocale();
		String message = messageSource.getMessage(e.getCode(), null, locale);
		ResponseError error = responseError(message, HttpStatus.CONFLICT);
		return handleExceptionInternal(e, error, headers(), HttpStatus.CONFLICT, request);
	}

	@ExceptionHandler({ IllegalArgumentException.class })
	ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException e, WebRequest request) {
		ResponseError error = responseError(e.getMessage(), HttpStatus.BAD_REQUEST);
		return handleExceptionInternal(e, error, headers(), HttpStatus.BAD_REQUEST, request);
	}

}