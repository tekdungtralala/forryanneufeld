package com.upwork.job.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RestResult {

	public Object result;
	public String errorMessage;

	public RestResult(Object result) {
		this.result = result;
	}

	public RestResult(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static RestResult getResult(String errorMessage) {
		RestResult result = new RestResult(errorMessage);
		return result;
	}

	public static RestResult getResult(Object object) {
		RestResult result = new RestResult(object);
		return result;
	}

	public static ResponseEntity generateResp201() {
		return new ResponseEntity<RestResult>(HttpStatus.CREATED);
	}

	public static ResponseEntity generateResp200(Object object) {
		return new ResponseEntity<RestResult>(getResult(object), HttpStatus.OK);
	}

	public static ResponseEntity generateResp201(Object object) {
		return new ResponseEntity<RestResult>(getResult(object), HttpStatus.CREATED);
	}

	public static ResponseEntity generateResp204() {
		return new ResponseEntity<RestResult>(HttpStatus.NO_CONTENT);
	}

	public static ResponseEntity generateResp400(String errorMessage) {
		return new ResponseEntity<RestResult>(getResult(errorMessage), HttpStatus.BAD_REQUEST);
	}

	public static ResponseEntity generateResp403(String errorMessage) {
		return new ResponseEntity<RestResult>(getResult(errorMessage), HttpStatus.FORBIDDEN);
	}

	public static ResponseEntity generateResp404() {
		return new ResponseEntity<RestResult>(HttpStatus.NOT_FOUND);
	}

	public static ResponseEntity generateResp500(String errorMessage) {
		return new ResponseEntity<RestResult>(getResult(errorMessage), HttpStatus.INTERNAL_SERVER_ERROR);
	}
}
