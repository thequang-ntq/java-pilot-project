package com.ntq.demo.model;

import lombok.*;

import java.util.List;

/**
 * This class is used to generate pagination information for tables
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PageResponse<T> {
	/**
	 * List data
	 */
	private List<T> content;
	/**
	 * Current page (Begin at 0)
	 */
	private int currentPage;
	/**
	 * Total number of pages
	 */
	private int totalPages;
	/**
	 * Total number of records
	 */
	private long totalElements;
	/**
	 * Number of records each page
	 */
	private int pageSize;
}