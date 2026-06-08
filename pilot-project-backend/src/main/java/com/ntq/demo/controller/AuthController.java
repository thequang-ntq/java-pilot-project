package com.ntq.demo.controller;

import com.ntq.demo.dto.request.LoginRequest;
import com.ntq.demo.dto.request.RefreshTokenRequest;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to declare controller related to User management
 *
 * @author Quang
 * @since 2026-04-30
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private UserService userService;

	/**
	 * POST /api/auth/login
	 * Public
	 */
	@PostMapping("/login")
	public ResponseDataModel<?> login(@Valid @RequestBody LoginRequest request) {
		return userService.login(request);
	}

	/**
	 * POST /api/auth/refresh
	 * Public
	 */
	@PostMapping("/refresh")
	public ResponseDataModel<?> refresh(@Valid @RequestBody RefreshTokenRequest request) {
		return userService.refresh(request);
	}

	/**
	 * POST /api/auth/logout
	 * Public
	 */
	@PostMapping("/logout")
	public ResponseDataModel<?> logout(@AuthenticationPrincipal String username) {
		return userService.logout(username);
	}
}