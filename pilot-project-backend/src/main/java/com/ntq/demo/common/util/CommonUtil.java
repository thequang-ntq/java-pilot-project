package com.ntq.demo.common.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is used to implement common functions for project
 *
 * @author Quang
 * @since 2026-04-28
 */
public class CommonUtil {
	private static final BCryptPasswordEncoder BCRYPT = new BCryptPasswordEncoder();

	/**
	 * Encode password with BCrypt
	 *
	 * @param rawPassword
	 * @return encoded password
	 */
	public static String encodePassword(String rawPassword) {
		return BCRYPT.encode(rawPassword);
	}

	/**
	 * Compare raw password with BCrypt hash from DB
	 *
	 * @param rawPassword
	 * @param encodedPassword
	 * @return true if equal, false if not
	 */
	public static boolean matchesPassword(String rawPassword, String encodedPassword) {
		return BCRYPT.matches(rawPassword, encodedPassword);
	}
}
