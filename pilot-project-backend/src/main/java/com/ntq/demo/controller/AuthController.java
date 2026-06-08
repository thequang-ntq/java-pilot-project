package com.ntq.demo.controller;

import com.ntq.demo.model.request.LoginRequest;
import com.ntq.demo.model.request.RegisterRequest;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.service.AccountService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to declare controller related to Account management
 *
 * @author Quang
 * @since 2026-04-30
 */
@RestController
@RequestMapping("/api/auth")
public class AuthController {
	@Autowired
	private AccountService accountService;

	@PostMapping("/login")
	public ResponseDataModel<?> login(@Valid @RequestBody LoginRequest request) {
		return accountService.login(request);
	}

	@PostMapping("/register")
	public ResponseDataModel<?> register(@Valid @RequestBody RegisterRequest request) {
		return accountService.register(request);
	}
}