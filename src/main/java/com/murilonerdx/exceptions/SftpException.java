package com.murilonerdx.exceptions;

public class SftpException extends Exception {
	public SftpException(String message) {
		super(message);
	}
	public SftpException(String message, Throwable cause) {
		super(message, cause);
	}
}
