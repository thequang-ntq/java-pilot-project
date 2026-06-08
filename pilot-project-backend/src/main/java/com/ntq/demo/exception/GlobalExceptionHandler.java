package com.ntq.demo.exception;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.model.ResponseDataModel;
import org.apache.tomcat.util.http.fileupload.impl.FileSizeLimitExceededException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;

import java.util.HashMap;
import java.util.Map;

/**
 * This class is used to handle exceptions globally for all controllers
 *
 * @author Quang
 * @since 2026-05-02
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	/**
	 * Handle InvalidFileException error (invalid file type/save or delete file error)
	 * Spring automatically rollback transaction when InvalidFileException is thrown (because it's
	 * RuntimeException)
	 *
	 * @param ex
	 * @return ResponseDataModel for invalid file type/save or delete file error
	 */
	@ExceptionHandler(InvalidFileException.class)
	public ResponseDataModel<Void> handleInvalidFileException(InvalidFileException ex) {
		LOGGER.warn("Invalid file exception: {}", ex.getMessage());
		return new ResponseDataModel<>(Constants.RESULT_CD_FAIL, ex.getMessage());
	}

	/**
	 * Handle file size exceeded
	 * Triggered when file > 5MB (spring.servlet.multipart.max-file-size)
	 *
	 * @param ex
	 * @return ResponseDataModel for file size exceeded
	 */
	@ExceptionHandler(MultipartException.class)
	public ResponseDataModel<Void> handleMultipartException(MultipartException ex) {
		LOGGER.warn("Multipart exception: {}", ex.getMessage());
		String message = "An error occurred while uploading file";

		/**
		 * Check if it's file size exceeded
		 */
		if (ex.getCause() instanceof FileSizeLimitExceededException) {
			message = "File size exceeds maximum allowed size (5MB)";
		} else if (ex.getMessage().contains("size")) {
			message = "File size exceeds maximum allowed size (5MB)";
		}

		return new ResponseDataModel<>(Constants.RESULT_CD_FAIL, message);
	}

	/**
	 * Handle @Valid validation errors from @RequestBody and @ModelAttribute
	 * Collect all field errors and return them in ResponseDataModel
	 *
	 * @param ex
	 * @return ResponseDataModel for validation error
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseDataModel<Map<String, String>> handleValidationErrors(MethodArgumentNotValidException ex) {
		Map<String, String> errors = new HashMap<>();
		ex.getBindingResult().getFieldErrors().forEach(error ->
			errors.put(error.getField(), error.getDefaultMessage())
		);
		LOGGER.warn("Validation failed: {}", errors);
		return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Validation failed", errors);
	}

	/**
	 * Handle invalid JSON format from @RequestBody
	 *
	 * @param ex
	 * @return ResponseDataModel for invalid JSON format from @RequestBody
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseDataModel<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		LOGGER.warn("Invalid request body format: {}", ex.getMessage());
		return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Invalid request body format");
	}

	/**
	 * Handle all other unexpected exceptions
	 *
	 * @param ex
	 * @return ResponseDataModel for 500 HTTP Status error
	 */
	@ExceptionHandler(Exception.class)
	public ResponseDataModel<Void> handleGeneralException(Exception ex) {
		LOGGER.error("Unexpected error: {}", ex.getMessage(), ex);
		return new ResponseDataModel<>(Constants.RESULT_CD_FAIL, "Internal server error");
	}
}