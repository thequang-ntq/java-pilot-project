package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * This class is used to declare properties and mapping them with brands table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "brands")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "brand_id")
	private Integer brandId;

	@Column(name = "brand_name", nullable = false, unique = true, length = 50)
	private String brandName;

	@Column(name = "Logo")
	private String logo;

	@Column(name = "description")
	private String description;

	/**
	 * Products of this brand (inverse side, mapped by ProductEntity."brand").
	 */
	@OneToMany(mappedBy = "brand", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ProductEntity> products;
}
