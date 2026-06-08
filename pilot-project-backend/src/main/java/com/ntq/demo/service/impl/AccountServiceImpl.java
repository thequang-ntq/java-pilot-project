package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.CommonUtil;
import com.ntq.demo.dao.AccountDao;
import com.ntq.demo.entity.AccountEntity;
import com.ntq.demo.model.request.LoginRequest;
import com.ntq.demo.model.request.RegisterRequest;
import com.ntq.demo.model.response.LoginResponse;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.security.JwtTokenProvider;
import com.ntq.demo.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * This class is used to implement functions to handle logic and business for Account Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
public class AccountServiceImpl implements AccountService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountDao AccountDao;

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
			} else {
				String token = jwtTokenProvider.generateToken(
					account.getAccountName(),
					account.getRole().name()
				);
				LoginResponse data = new LoginResponse(
					token,
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

			AccountDao.save(account);

			responseMsg = "Register successful";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when registering";
			LOGGER.error("Error when registering: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}