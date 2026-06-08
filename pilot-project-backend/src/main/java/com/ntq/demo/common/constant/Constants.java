package com.ntq.demo.common.constant;

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
	public static final int DEFAULT_PAGE_SIZE = 10;

	/**
	 * Result codes
	 */
	public static final int RESULT_CD_SUCCESS = 200;
	public static final int RESULT_CD_FAIL = 500;
	public static final int RESULT_CD_DUPL = 409; //Duplicate
	public static final int RESULT_CD_INVALID = 400; //Validation fail
}
