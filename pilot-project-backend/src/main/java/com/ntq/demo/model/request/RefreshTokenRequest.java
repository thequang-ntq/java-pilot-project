package com.ntq.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * This class is used to declare properties for refresh token request data from client-side (DTO)
 *
 * @author Quang
 * @since 2026-05-05
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenRequest {
	@NotBlank(message = "Refresh token is required")
	private String refreshToken;
}
