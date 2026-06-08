package com.ntq.demo.model.response;

import lombok.*;

/**
 * This class is used to declare properties for brand response data to client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandResponse {
	private Integer brandId;
	private String brandName;
	private String logo;
	private String description;
	private Boolean isDeleted;
}