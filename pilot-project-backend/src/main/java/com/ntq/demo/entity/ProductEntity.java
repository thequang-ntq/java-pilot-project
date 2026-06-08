package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class is used to declare properties and mapping them with product table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "product_id")
	private Integer productId;

	@Column(name = "product_name", nullable = false, unique = true, length = 50)
	private String productName;

	@Column(name = "quantity", nullable = false)
	private Integer quantity;

	@Column(name = "price", nullable = false, precision = 10, scale = 2)
	private BigDecimal price;

	/**
	 * Brand of this product (owning side, uses brand_id of product table as foreign key).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "brand_id", nullable = false)
	private BrandEntity brand;

	@Column(name = "sale_date", nullable = false)
	private LocalDate saleDate = LocalDate.now();

	@Column(name = "image")
	private String image;

	@Column(name = "description")
	private String description;

	@Column(name = "is_deleted", nullable = false)
	private Boolean isDeleted = false;
}
