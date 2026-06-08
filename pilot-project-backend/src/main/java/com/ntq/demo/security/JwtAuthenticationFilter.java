package com.ntq.demo.security;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.service.impl.UserDetailsServiceImpl;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * This class is used to filter and validate JWT token, get UserDetails, set SecurityContext for every request
 *
 * @author Quang
 * @since 2026-04-29
 */
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

	@Autowired
	private JwtTokenProvider jwtTokenProvider;

	@Autowired
	private UserDetailsServiceImpl userDetailsServiceImpl;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
		throws ServletException, IOException {
		String token = extractToken(request);

		if (token != null && jwtTokenProvider.validateToken(token)) {
			try {
				/**
				 * Step 1: Extract username and role from JWT token
				 */
				String username = jwtTokenProvider.getUsername(token);
				String role = jwtTokenProvider.getRole(token);

				/**
				 * Step 2: Load user from DB to verify still exists
				 * If user deleted after token issued -> null -> skip
				 * This is the key difference vs only reading from JWT:
				 * DB is source of truth, not the token
				 */
				UserDetails userDetails = null;
				try {
					userDetails = userDetailsServiceImpl.loadUserByUsername(username);
				} catch (Exception e) {
					LOGGER.warn("User {} not found in DB or disabled, skip authentication", username);
				}

				/**
				 * Step 3: Only set SecurityContext if user exists in DB
				 * userDetails != null -> user still valid in DB
				 * Use role from DB (userDetails.getAuthorities()) not from token
				 * -> role changed in DB -> reflected immediately
				 */
				if (userDetails != null) {
					UsernamePasswordAuthenticationToken authentication =
						new UsernamePasswordAuthenticationToken(
							userDetails.getUsername(),
							null,
							userDetails.getAuthorities()
						);

					SecurityContextHolder.getContext().setAuthentication(authentication);
					LOGGER.info("Authenticated user: {}, role: {}", username, role);
				}
			} catch (Exception e) {
				LOGGER.error("Cannot set authentication: {}", e.getMessage());
				SecurityContextHolder.clearContext();
			}
		}

		filterChain.doFilter(request, response);
	}

	/**
	 * Read JWT Token from Authorization header
	 * Header format: "Bearer <token>"
	 *
	 * @param request
	 * @return String JWT Token
	 */
	private String extractToken(HttpServletRequest request) {
		String header = request.getHeader(Constants.JWT_HEADER);
		if (header != null && header.startsWith(Constants.JWT_PREFIX)) {
			return header.substring(Constants.JWT_PREFIX.length());
		}
		return null;
	}
}