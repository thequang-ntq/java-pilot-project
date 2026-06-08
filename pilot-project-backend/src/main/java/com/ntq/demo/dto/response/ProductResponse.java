package com.ntq.demo.dto.response;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class is used to declare properties for product response data to client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponse {
	private Integer productId;
	private String productName;
	private Integer quantity;
	private BigDecimal price;
	private Integer brandId;
	/**
	 * Get from brand, not expose BrandEntity
	 */
	private String brandName;
	private LocalDate saleDate;
	private String image;
	private String description;
}