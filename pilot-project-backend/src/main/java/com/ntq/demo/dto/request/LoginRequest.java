package com.ntq.demo.dto.request;

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
	@NotBlank(message = "Username is required")
	private String username;

	@NotBlank(message = "Password is required")
	private String password;
}
