package com.ntq.demo.service;

import com.ntq.demo.dto.request.LoginRequest;
import com.ntq.demo.dto.request.RefreshTokenRequest;
import com.ntq.demo.model.ResponseDataModel;

/**
 * This interface is used to declare functions to handle logic and business for User Entity
 *
 * @author Quang
 * @since 2026-04-29
 */
public interface UserService {
	ResponseDataModel<?> login(LoginRequest request);

	ResponseDataModel<?> refresh(RefreshTokenRequest request);

	ResponseDataModel<?> logout(String accountName);
}