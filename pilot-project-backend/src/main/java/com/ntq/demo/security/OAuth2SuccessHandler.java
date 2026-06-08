package com.ntq.demo.security;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.CommonUtil;
import com.ntq.demo.dao.AccountDao;
import com.ntq.demo.dao.RefreshTokenDao;
import com.ntq.demo.entity.AccountEntity;
import com.ntq.demo.entity.RefreshTokenEntity;
import com.ntq.demo.model.response.LoginResponse;
import com.ntq.demo.model.response.ResponseDataModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * This class is used to handle OAuth2 login success
 * Called after Google authenticates user successfully
 * Creates or finds account in DB, generates JWT tokens, redirects to React
 *
 * @author Quang
 * @since 2026-05-06
 */
@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2SuccessHandler.class);

	@Autowired
	private AccountDao AccountDao;

	@Autowired
	private RefreshTokenDao RefreshTokenDao;

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private ObjectMapper objectMapper;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException {
		try {
			/**
			 * Step 1: Get user info from Google
			 * OAuth2User contains attribute from Google
			 */
			OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();

			String googleId = oAuth2User.getAttribute("sub"); // Google unique ID
			String email = oAuth2User.getAttribute("email"); // Email Google

			LOGGER.info("OAuth2 login success - googleId: {}, email: {}", googleId, email);

			/**
			 * Step 2: Find account by googleId
			 * Create a new one if it isn't exists
			 * Otherwise, use that account
			 */
			AccountEntity account = AccountDao.findByGoogleId(googleId).orElse(null);

			if (account == null) {
				/**
				 * Create new account
				 * account_name = email (unique)
				 * password = BCrypt(UUID)
				 * role = USER
				 */
				account = new AccountEntity();
				account.setAccountName(email);
				account.setEmail(email);
				account.setGoogleId(googleId);
				account.setPassword(CommonUtil.encodePassword(UUID.randomUUID().toString()));
				account.setRole(AccountEntity.Role.USER);
				account.setAuthType(AccountEntity.AuthType.GOOGLE);
				AccountDao.saveAndFlush(account);
				LOGGER.info("Created new GOOGLE account: {}", email);
			}

			/**
			 * Step 3: Generate JWT Access Token + Refresh Token
			 * Same as when login username/password
			 */
			String accessToken = jwtTokenProvider.generateAccessToken(
				account.getAccountName(),
				account.getRole().name()
			);
			String refreshToken = jwtTokenProvider.generateRefreshToken();

			/**
			 * Step 4: Save refresh token into DB
			 */
			RefreshTokenDao.deleteByAccount(account);

			RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity();
			refreshTokenEntity.setAccount(account);
			refreshTokenEntity.setToken(refreshToken);
			refreshTokenEntity.setExpiredAt(LocalDateTime.now().plusSeconds(
				Constants.JWT_REFRESH_EXPIRATION_MS / 1000));
			RefreshTokenDao.saveAndFlush(refreshTokenEntity);

			/**
			 * Step 5: ResponseDataModel with LoginResponse
			 */
			LoginResponse loginResponse = new LoginResponse(
				accessToken,
				refreshToken,
				account.getAccountName(),
				account.getRole().name()
			);

			ResponseDataModel<LoginResponse> responseDataModel = new ResponseDataModel<>(
				Constants.RESULT_CD_SUCCESS,
				"OAuth2 login successful",
				loginResponse
			);

			/**
			 * Write JSON into response body (same as ServiceImpl, @RestController)
			 */
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_OK);
			response.getWriter().write(objectMapper.writeValueAsString(responseDataModel));
		} catch (Exception e) {
			LOGGER.error("OAuth2 login failed: {}", e.getMessage(), e);

			ResponseDataModel<Void> errorResponse = new ResponseDataModel<>(
				Constants.RESULT_CD_FAIL,
				"OAuth2 login failed"
			);

			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			response.getWriter().write(objectMapper.writeValueAsString(errorResponse));
		}
	}
}