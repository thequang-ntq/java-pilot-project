package com.ntq.demo.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is used to declare properties for brand request data from client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BrandRequest {
	/**
	 * Add -> Null, Edit -> Not null
	 */
	private Integer brandId;

	@NotBlank(message = "Brand name is required")
	@Size(max = 50, message = "Brand name must be at most 50 characters")
	private String brandName;

	private MultipartFile[] logoFiles;

	private String description;
}