package com.murilonerdx.exceptions;

public class FileTransferException extends RuntimeException {
	public FileTransferException(String message, Throwable cause) {
		super(message, cause);
	}
}