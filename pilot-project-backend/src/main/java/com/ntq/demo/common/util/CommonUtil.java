package com.ntq.demo.common.util;

import com.ntq.demo.common.constant.Constants;
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
	 * Sanitize input string
	 *
	 * @param str Input string
	 * @return Sanitized string
	 */
	public static String sanitize(String str) {
		if (str == null || str.isBlank()) {
			return "";
		}
		String sanitized = str.replaceAll(Constants.DANGEROUS_CHARS_REGEX, "");
		sanitized = sanitized.replaceAll("\\s+", " ");
		return sanitized.trim();
	}

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
