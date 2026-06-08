package com.ntq.demo.mapper;

import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.dto.response.BrandResponse;
import com.ntq.demo.entity.BrandEntity;
import org.springframework.stereotype.Component;

/**
 * This class is used to map between BrandEntity and Brand DTOs
 *
 * @author Quang
 * @since 2026-05-07
 */
@Component
public class BrandMapper {
	/**
	 * BrandEntity -> BrandResponse (getList)
	 *
	 * @param entity
	 * @return BrandResponse
	 */
	public BrandResponse toResponse(BrandEntity entity) {
		if (entity == null) return null;

		BrandResponse response = new BrandResponse();
		response.setBrandId(entity.getBrandId());
		response.setBrandName(entity.getBrandName());
		response.setLogo(entity.getLogo());
		response.setDescription(entity.getDescription());
		return response;
	}

	/**
	 * BrandRequest -> BrandEntity (add)
	 *
	 * @param request
	 * @return BrandEntity for Add
	 */
	public BrandEntity toEntity(BrandRequest request) {
		if (request == null) return null;

		BrandEntity entity = new BrandEntity();
		entity.setBrandName(request.getBrandName());
		entity.setDescription(request.getDescription());
		return entity;
	}

	/**
	 * Update BrandEntity: get data from BrandRequest
	 *
	 * @param request
	 * @param entity
	 */
	public void updateEntity(BrandRequest request, BrandEntity entity) {
		if (request == null || entity == null) return;

		entity.setBrandName(request.getBrandName());
		entity.setDescription(request.getDescription());
	}
}
