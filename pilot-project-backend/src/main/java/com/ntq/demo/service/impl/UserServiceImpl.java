package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.CommonUtil;
import com.ntq.demo.repository.UserRepository;
import com.ntq.demo.repository.RefreshTokenRepository;
import com.ntq.demo.entity.UserEntity;
import com.ntq.demo.entity.RefreshTokenEntity;
import com.ntq.demo.dto.request.LoginRequest;
import com.ntq.demo.dto.request.RefreshTokenRequest;
import com.ntq.demo.dto.response.LoginResponse;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.security.JwtTokenProvider;
import com.ntq.demo.service.UserService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * This class is used to implement functions to handle logic and business for User Entity
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
@Transactional
public class UserServiceImpl implements UserService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private UserRepository UserRepository;

	@Autowired
	private RefreshTokenRepository RefreshTokenRepository;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public ResponseDataModel<LoginResponse> login(LoginRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			UserEntity user = UserRepository.findByUsername(request.getUsername()).orElse(null);

			/**
			 * Use BCrypt to compare password instead of MD5 hash comparison
			 */
			if (user == null || !CommonUtil.matchesPassword(request.getPassword(), user.getPassword())) {
				responseMsg = "User name or password is incorrect";
				responseCode = Constants.RESULT_CD_UNAUTHORIZED;
			}
			else {
				/**
				 * Generate access token and refresh token
				 */
				String accessToken = jwtTokenProvider.generateAccessToken(
					user.getUsername(),
					user.getRole().name()
				);
				String refreshToken = jwtTokenProvider.generateRefreshToken();

				/**
				 * Delete old refresh token if exists
				 * Each user only has 1 refresh token at a time
				 */
				RefreshTokenRepository.deleteByUser(user);

				/**
				 * Save new refresh token to DB with expiry time (now plus 7 days)
				 */
				RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
				refreshTokenEntity.setUser(user);
				refreshTokenEntity.setToken(refreshToken);
				refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(
					Constants.JWT_REFRESH_EXPIRATION_MS / 1000));
				RefreshTokenRepository.saveAndFlush(refreshTokenEntity);

				LoginResponse data = new LoginResponse(
					accessToken,
					refreshToken,
					user.getUsername(),
					user.getRole().name()
				);
				return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Login successful", data);
			}
		} catch (Exception e) {
			responseMsg = "Error when logging in";
			LOGGER.error("Error when logging in: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<LoginResponse> refresh(RefreshTokenRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			/**
			 * Find refresh token in DB
			 */
			RefreshTokenEntity refreshTokenEntity = RefreshTokenRepository
				.findByToken(request.getRefreshToken()).orElse(null);

			if (refreshTokenEntity == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_UNAUTHORIZED, "Refresh token not found");
			}

			/**
			 * Check if refresh token is expired
			 */
			if (refreshTokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
				RefreshTokenRepository.delete(refreshTokenEntity);
				RefreshTokenRepository.flush();
				return new ResponseDataModel<>(Constants.RESULT_CD_UNAUTHORIZED,
					"Refresh token is expired, please login again");
			}

			/**
			 * Generate new Access Token from user info in DB, rotate refresh token
			 * Role taken from DB -> always up to date
			 */
			UserEntity user = refreshTokenEntity.getUser();
			RefreshTokenRepository.delete(refreshTokenEntity);
			String newAccessToken = jwtTokenProvider.generateAccessToken(
				user.getUsername(),
				user.getRole().name()
			);
			String newRefreshToken = jwtTokenProvider.generateRefreshToken();
			RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
			newRefreshTokenEntity.setUser(user);
			newRefreshTokenEntity.setToken(newRefreshToken);
			newRefreshTokenEntity.setExpiredAt(
				LocalDateTime.now().plusSeconds(Constants.JWT_REFRESH_EXPIRATION_MS / 1000));
			RefreshTokenRepository.saveAndFlush(newRefreshTokenEntity);

			LOGGER.info("Refresh token rotated for user: {}", user.getUsername());

			LoginResponse data = new LoginResponse(
				newAccessToken,
				newRefreshToken,
				user.getUsername(),
				user.getRole().name()
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Token refreshed successfully", data);
		} catch (Exception e) {
			responseMsg = "Error when refreshing token";
			LOGGER.error("Error when refreshing token: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> logout(String username) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			UserEntity user = UserRepository.findByUsername(username).orElse(null);

			if (user == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "User not found");
			}

			/**
			 * Delete refresh token from DB -> cannot refresh anymore
			 * Access token still valid until expired (1 hour)
			 * but React deletes it from localStorage on logout
			 */
			RefreshTokenRepository.deleteByUser(user);
			RefreshTokenRepository.flush();

			responseMsg = "Logout successful";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when logging out";
			LOGGER.error("Error when logging out: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}