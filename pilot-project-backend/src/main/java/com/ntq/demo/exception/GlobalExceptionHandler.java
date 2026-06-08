package com.ntq.demo.exception;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.model.ResponseDataModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

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
	 * Handle @Valid validation errors from @RequestBody and @ModelAttribute
	 * Collect all field errors and return them in ResponseDataModel
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
	 */
	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseDataModel<Void> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
		LOGGER.warn("Invalid request body format: {}", ex.getMessage());
		return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Invalid request body format");
	}

	/**
	 * Handle all other unexpected exceptions
	 */
	@ExceptionHandler(Exception.class)
	public ResponseDataModel<Void> handleGeneralException(Exception ex) {
		LOGGER.error("Unexpected error: {}", ex.getMessage(), ex);
		return new ResponseDataModel<>(Constants.RESULT_CD_FAIL, "Internal server error");
	}
}