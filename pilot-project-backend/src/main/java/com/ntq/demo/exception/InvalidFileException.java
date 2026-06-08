package com.ntq.demo.exception;

/**
 * This class is used to create InvalidFileException for handling invalid file type,
 * throw in ServiceImpl to rollback
 *
 * @author Quang
 * @since 2026-05-28
 */
public class InvalidFileException extends RuntimeException {
	/**
	 * Constructor 1: Message only
	 *
	 * @param message message error
	 */
	public InvalidFileException(String message) {
		super(message);
	}

	/**
	 * Constructor 2: Message + Cause
	 *
	 * @param message message error
	 * @param cause root exception (IOException, etc.)
	 */
	public InvalidFileException(String message, Throwable cause) {
		super(message, cause);
	}
}
