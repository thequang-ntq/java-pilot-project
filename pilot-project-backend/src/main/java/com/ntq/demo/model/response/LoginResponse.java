package com.ntq.demo.model.response;

import lombok.*;

/**
 * This class is used to declare properties for login response data to client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
	/**
	 * JWT Access Token and refresh token
	 */
	private String accessToken;
	private String refreshToken;
	private String accountName;
	private String role;
}