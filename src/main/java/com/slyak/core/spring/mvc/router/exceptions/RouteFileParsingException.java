package com.slyak.core.spring.mvc.router.exceptions;

import org.springframework.beans.BeansException;

/**
 * Exception: Error while parsing route file
 * @author Brian Clozel
 * @see com.slyak.core.spring.mvc.router.Router
 */
public class RouteFileParsingException extends BeansException {

	public RouteFileParsingException(String msg) {
		super(msg);
	}

	public RouteFileParsingException(String msg, Throwable e) {
		super(msg, e);
	}
}
