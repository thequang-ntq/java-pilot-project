package com.ntq.demo.common.constant;

import java.util.Set;

/**
 * This class is used to declare constant for project
 *
 * @author Quang
 * @since 2026-04-27
 */
public class Constants {
	/**
	 * JWT
	 */
	public static final long JWT_ACCESS_EXPIRATION_MS = 600000L; //10 minutes
	public static final long JWT_REFRESH_EXPIRATION_MS = 604800000L; //1 week
	public static final String JWT_HEADER = "Authorization";
	public static final String JWT_PREFIX = "Bearer ";

	/**
	 * Pagination
	 */
	public static final int DEFAULT_PAGE_SIZE = 5;

	/**
	 * Result codes
	 */
	public static final int RESULT_CD_SUCCESS = 200;

	public static final int RESULT_CD_INVALID = 400; // Validation fail (@Valid in Controller)
	public static final int RESULT_CD_UNAUTHORIZED = 401; // Not login, access/refresh token expired/invalid
	public static final int RESULT_CD_FORBIDDEN = 403; // Wrong role (authorization)
	public static final int RESULT_CD_NOT_FOUND = 404; // Not found
	public static final int RESULT_CD_DUPL = 409; // Duplicated
	public static final int RESULT_CD_FAIL = 500; // Internal server error

	/**
	 * Allowed image types (MIME Type): png, jpg, jpeg, webp, gif
	 */
	public static final Set<String> ALLOWED_IMAGE_TYPES = Set.of(
		"image/png",
		"image/jpeg", // jpg & jpeg has the same MIME type
		"image/webp",
		"image/gif"
	);
}
