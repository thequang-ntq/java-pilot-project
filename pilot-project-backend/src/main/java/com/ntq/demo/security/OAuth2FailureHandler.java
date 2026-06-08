package com.ntq.demo.security;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.model.response.ResponseDataModel;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * This class is used to handle OAuth2 login failure
 * Called when Google authentication fails or user denies permission
 *
 * @author Quang
 * @since 2026-05-06
 */
@Component
public class OAuth2FailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private static final Logger LOGGER = LoggerFactory.getLogger(OAuth2FailureHandler.class);

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException {
		LOGGER.error("OAuth2 authentication failed: {}", exception.getMessage(), exception);

		ResponseDataModel<Void> errorResponse = new ResponseDataModel<>(
			Constants.RESULT_CD_FAIL,
			"OAuth2 authentication failed"
		);

		response.setContentType("application/json");
		response.setCharacterEncoding("UTF-8");
		response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
		response.getWriter().write(new ObjectMapper().writeValueAsString(errorResponse));
	}
}