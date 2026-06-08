package com.ntq.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * This class is used to declare properties for login request data from client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
	@NotBlank(message = "Account name is required")
	private String accountName;

	@NotBlank(message = "Password is required")
	private String password;
}
