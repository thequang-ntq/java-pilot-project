package com.ntq.demo.model.response;

import lombok.*;

import java.math.BigDecimal;

/**
 * This class is used to declare properties for order detail response data to client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailResponse {
	private String productName;
	private String image;
	private Integer quantity;
	/**
	 * Unit price
	 */
	private BigDecimal salePrice;
	/**
	 * Quantity * Sale price
	 */
	private BigDecimal totalPrice;
}