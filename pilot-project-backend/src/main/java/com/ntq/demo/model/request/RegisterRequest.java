package com.ntq.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

/**
 * This class is used to declare properties for register request data from client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
	@NotBlank(message = "Account name is required")
	@Size(min = 3, max = 50, message = "Account name must be between 3 and 50 characters")
	private String accountName;

	@NotBlank(message = "Password is required")
	@Size(min = 6, message = "Password must be at least 6 characters")
	private String password;

	@NotBlank(message = "Confirm password is required")
	private String confirmPassword;
}