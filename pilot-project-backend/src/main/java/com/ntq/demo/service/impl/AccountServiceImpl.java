package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.CommonUtil;
import com.ntq.demo.dao.AccountDao;
import com.ntq.demo.dao.RefreshTokenDao;
import com.ntq.demo.entity.AccountEntity;
import com.ntq.demo.entity.RefreshTokenEntity;
import com.ntq.demo.model.request.LoginRequest;
import com.ntq.demo.model.request.RefreshTokenRequest;
import com.ntq.demo.model.request.RegisterRequest;
import com.ntq.demo.model.response.LoginResponse;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.security.JwtTokenProvider;
import com.ntq.demo.service.AccountService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

/**
 * This class is used to implement functions to handle logic and business for Account Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
@Transactional
public class AccountServiceImpl implements AccountService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountDao AccountDao;

	@Autowired
	private RefreshTokenDao RefreshTokenDao;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Override
	public ResponseDataModel<LoginResponse> login(LoginRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(request.getAccountName()).orElse(null);

			/**
			 * Use BCrypt to compare password instead of MD5 hash comparison
			 */
			if (account == null || !CommonUtil.matchesPassword(request.getPassword(), account.getPassword())) {
				responseMsg = "Account name or password is incorrect";
				responseCode = Constants.RESULT_CD_INVALID;
			}
			else if (account.getAuthType().equals(AccountEntity.AuthType.GOOGLE)) {
				responseMsg = "Please login by Google by clicking the button";
				responseCode = Constants.RESULT_CD_INVALID;
			}
			else {
				/**
				 * Generate access token and refresh token
				 */
				String accessToken = jwtTokenProvider.generateAccessToken(
					account.getAccountName(),
					account.getRole().name()
				);
				String refreshToken = jwtTokenProvider.generateRefreshToken();

				/**
				 * Delete old refresh token if exists
				 * Each account only has 1 refresh token at a time
				 */
				RefreshTokenDao.deleteByAccount(account);

				/**
				 * Save new refresh token to DB with expiry time (now plus 7 days)
				 */
				RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
				refreshTokenEntity.setAccount(account);
				refreshTokenEntity.setToken(refreshToken);
				refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(
					Constants.JWT_REFRESH_EXPIRATION_MS / 1000));
				RefreshTokenDao.saveAndFlush(refreshTokenEntity);

				LoginResponse data = new LoginResponse(
					accessToken,
					refreshToken,
					account.getAccountName(),
					account.getRole().name()
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
	public ResponseDataModel<Void> register(RegisterRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			/**
			 * Check if password and confirmPassword is the same
			 */
			if (!request.getPassword().equals(request.getConfirmPassword())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Passwords do not match");
			}

			/**
			 * Check if account name exists
			 */
			if (AccountDao.existsByAccountName(request.getAccountName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Account name already exists");
			}

			/**
			 * Create new account, default role USER, hash BCrypt for password
			 */
			AccountEntity account = new AccountEntity();
			account.setAccountName(request.getAccountName());
			/**
			 * Encode password with BCrypt before saving to DB
			 */
			account.setPassword(CommonUtil.encodePassword(request.getPassword()));
			account.setRole(AccountEntity.Role.USER);

			AccountDao.saveAndFlush(account);

			responseMsg = "Register successful";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when registering";
			LOGGER.error("Error when registering: {}", e.getMessage(), e);
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
			RefreshTokenEntity refreshTokenEntity = RefreshTokenDao
				.findByToken(request.getRefreshToken()).orElse(null);

			if (refreshTokenEntity == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Refresh token not found");
			}

			/**
			 * Check if refresh token is expired
			 */
			if (refreshTokenEntity.getExpiredAt().isBefore(LocalDateTime.now())) {
				RefreshTokenDao.delete(refreshTokenEntity);
				RefreshTokenDao.flush();
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID,
					"Refresh token is expired, please login again");
			}

			/**
			 * Generate new Access Token from account info in DB, rotate refresh token
			 * Role taken from DB -> always up to date
			 */
			AccountEntity account = refreshTokenEntity.getAccount();
			RefreshTokenDao.delete(refreshTokenEntity);
			String newAccessToken = jwtTokenProvider.generateAccessToken(
				account.getAccountName(),
				account.getRole().name()
			);
			String newRefreshToken = jwtTokenProvider.generateRefreshToken();
			RefreshTokenEntity newRefreshTokenEntity = new RefreshTokenEntity();
			newRefreshTokenEntity.setAccount(account);
			newRefreshTokenEntity.setToken(newRefreshToken);
			newRefreshTokenEntity.setExpiredAt(
				LocalDateTime.now().plusSeconds(Constants.JWT_REFRESH_EXPIRATION_MS / 1000));
			RefreshTokenDao.saveAndFlush(newRefreshTokenEntity);

			LOGGER.info("Refresh token rotated for account: {}", account.getAccountName());

			LoginResponse data = new LoginResponse(
				newAccessToken,
				newRefreshToken,
				account.getAccountName(),
				account.getRole().name()
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Token refreshed successfully", data);
		} catch (Exception e) {
			responseMsg = "Error when refreshing token";
			LOGGER.error("Error when refreshing token: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> logout(String accountName) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);

			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			/**
			 * Delete refresh token from DB -> cannot refresh anymore
			 * Access token still valid until expired (1 hour)
			 * but React deletes it from localStorage on logout
			 */
			RefreshTokenDao.deleteByAccount(account);
			RefreshTokenDao.flush();

			responseMsg = "Logout successful";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when logging out";
			LOGGER.error("Error when logging out: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}