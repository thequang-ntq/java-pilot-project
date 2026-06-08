package com.ntq.demo.dto.request;

import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * This class is used to declare properties for product request data from client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductRequest {
	@NotBlank(message = "Product name is required")
	@Size(max = 50, message = "Product name must be at most 50 characters")
	private String productName;

	@NotNull(message = "Quantity is required")
	@Min(value = 0, message = "Quantity must be at least 0")
	private Integer quantity;

	@NotNull(message = "Price is required")
	@DecimalMin(value = "0.0", message = "Price must be at least 0")
	private BigDecimal price;

	@NotNull(message = "Brand is required")
	private Integer brandId;

	@NotNull(message = "Sale date is required")
	private LocalDate saleDate;

	private MultipartFile[] imageFiles;

	private String description;
}