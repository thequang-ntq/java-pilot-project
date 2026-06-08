package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * This class is used to declare properties and mapping them with order_detail table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "order_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderDetailEntity {
	@EmbeddedId
	private OrderDetailId id; //Composite PK

	/**
	 * Order of this order detail (owning side, uses order_id of order detail table as foreign key).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("orderId") //map to field orderId in OrderDetailId
	@JoinColumn(name = "order_id")
	private OrderEntity order;

	/**
	 * Product of this order detail (owning side, uses product_id of order detail table as foreign key).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("productId") //map to field productId in OrderDetailId
	@JoinColumn(name = "product_id")
	private ProductEntity product;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "sale_price", nullable = false, precision = 10, scale = 2)
	private BigDecimal salePrice;
}
