package com.ntq.demo.security;

import com.ntq.demo.common.constant.Constants;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.UUID;

/**
 * This class is used to provide JWT token utilities: generate, validate, extract claims
 *
 * @author Quang
 * @since 2026-04-29
 */
@Component
public class JwtTokenProvider {
	private static final Logger LOGGER = LoggerFactory.getLogger(JwtTokenProvider.class);

	@Value("${jwt.secret}")
	private String jwtSecret;

	/**
	 * Generate JWT Access Token from username and role when login success (short live - 1 hour)
	 *
	 * @param username
	 * @param role
	 * @return JWT Token
	 */
	public String generateAccessToken(String username, String role) {
		Date now = new Date();
		Date expiry = new Date(now.getTime() + Constants.JWT_ACCESS_EXPIRATION_MS);

		return Jwts.builder().subject(username).claim("role", role).issuedAt(now).expiration(expiry)
			.signWith(getSecretKey()).compact();
	}

	/**
	 * Generate Refresh Token — long live (7 days)
	 * Random UUID 36 char, no payload needed — DB is source of truth
	 */
	public String generateRefreshToken() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Get username from JWT Access Token
	 * @param token
	 * @return username
	 */
	public String getUsername(String token) {
		return parseClaims(token).getSubject();
	}

	/**
	 * Get role from JWT Access Token
	 * @param token
	 * @return Role
	 */
	public String getRole(String token) {
		return parseClaims(token).get("role", String.class);
	}

	/**
	 * Check JWT Access Token validate
	 * @param token
	 * @return true if valid, false if not
	 */
	public boolean validateToken(String token) {
		try {
			parseClaims(token);
			return true;
		} catch (ExpiredJwtException e) {
			LOGGER.error("JWT token is expired: {}", e.getMessage());
		} catch (UnsupportedJwtException e) {
			LOGGER.error("JWT token is unsupported: {}", e.getMessage());
		} catch (MalformedJwtException e) {
			LOGGER.error("JWT token is malformed: {}", e.getMessage());
		} catch (IllegalArgumentException e) {
			LOGGER.error("JWT token is empty: {}", e.getMessage());
		}
		return false;
	}

	/**
	 * Parse claims from JWT Token: verify signature, decode payload
	 * @param token
	 * @return Claims
	 */
	private Claims parseClaims(String token) {
		return Jwts.parser().verifyWith(getSecretKey()).build().parseSignedClaims(token).getPayload();
	}

	private SecretKey getSecretKey() {
		return Keys.hmacShaKeyFor(jwtSecret.getBytes(StandardCharsets.UTF_8));
	}
}