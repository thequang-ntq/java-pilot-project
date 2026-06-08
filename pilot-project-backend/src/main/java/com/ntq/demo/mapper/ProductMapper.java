package com.ntq.demo.mapper;

import com.ntq.demo.dto.request.ProductRequest;
import com.ntq.demo.dto.response.ProductResponse;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.entity.ProductEntity;
import org.springframework.stereotype.Component;

/**
 * This class is used to map between ProductEntity and Product DTOs
 *
 * @author Quang
 * @since 2026-05-07
 */
@Component
public class ProductMapper {
	/**
	 * ProductEntity -> ProductResponse (getList)
	 *
	 * @param entity
	 * @return ProductEntity
	 */
	public ProductResponse toResponse(ProductEntity entity) {
		if(entity == null) return null;

		ProductResponse response = new ProductResponse();
		response.setProductId(entity.getProductId());
		response.setProductName(entity.getProductName());
		response.setQuantity(entity.getQuantity());
		response.setPrice(entity.getPrice());
		response.setSaleDate(entity.getSaleDate());
		response.setImage(entity.getImage());
		response.setDescription(entity.getDescription());

		if (entity.getBrand() != null) {
			response.setBrandId(entity.getBrand().getBrandId());
			response.setBrandName(entity.getBrand().getBrandName());
		}
		return response;
	}

	/**
	 * ProductRequest -> ProductEntity (add)
	 *
	 * @param request
	 * @return ProductEntity for Add
	 */
	public ProductEntity toEntity(ProductRequest request, BrandEntity brand) {
		if (request == null) return null;

		ProductEntity entity = new ProductEntity();
		entity.setProductName(request.getProductName());
		entity.setQuantity(request.getQuantity());
		entity.setPrice(request.getPrice());
		entity.setBrand(brand);
		entity.setSaleDate(request.getSaleDate());
		entity.setDescription(request.getDescription());
		return entity;
	}

	/**
	 * Update ProductEntity: get data from ProductRequest
	 *
	 * @param request
	 * @param entity
	 * @param brand
	 */
	public void updateEntity(ProductRequest request, ProductEntity entity, BrandEntity brand) {
		if (request == null || entity == null) return;

		entity.setProductName(request.getProductName());
		entity.setQuantity(request.getQuantity());
		entity.setPrice(request.getPrice());
		entity.setBrand(brand);
		entity.setSaleDate(request.getSaleDate());
		entity.setDescription(request.getDescription());
	}
}
