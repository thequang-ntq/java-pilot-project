package com.ntq.demo.service;

import com.ntq.demo.model.request.LoginRequest;
import com.ntq.demo.model.request.RegisterRequest;
import com.ntq.demo.model.response.ResponseDataModel;

/**
 * This interface is used to declare functions to handle logic and business for Account Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
public interface AccountService {
	ResponseDataModel<?> login(LoginRequest request);

	ResponseDataModel<?> register(RegisterRequest request);
}