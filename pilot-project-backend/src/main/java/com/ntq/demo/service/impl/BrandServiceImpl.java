package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.entity.ProductEntity;
import com.ntq.demo.exception.InvalidFileException;
import com.ntq.demo.mapper.BrandMapper;
import com.ntq.demo.repository.BrandRepository;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.dto.response.BrandResponse;
import com.ntq.demo.model.PageResponse;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.service.BrandService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is used to implement functions to handle logic and business for Brand Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
@Transactional
public class BrandServiceImpl implements BrandService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private BrandRepository BrandRepository;

	@Autowired
	private BrandMapper brandMapper;

	@Override
	public ResponseDataModel<PageResponse<BrandResponse>> getList(int page, String keyword, boolean isSortByName) {
		try {
			/**
			 * Add LIMIT, OFFSET, ORDER BY in Repository method (SQL query)
			 */
			Pageable pageable = PageRequest.of(
				/**
				 * JPA index start at 0, but FE start at 1
				 */
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				isSortByName ? Sort.by("brandName").ascending() : Sort.by("brandId").ascending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all brands / Search brands by keyword (brand name) -> pagination by pageable
			 */
			Page<BrandEntity> brandPage = BrandRepository.findByBrandNameContainingIgnoreCase(
				keyword == null ? "" : keyword,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<BrandResponse> content = brandPage.getContent().stream()
				.map(brandMapper::toResponse).toList();

			/**
			 * Page Response
			 */
			PageResponse<BrandResponse> data = new PageResponse<>(
				content,
				page,
				brandPage.getTotalPages(),
				brandPage.getTotalElements(),
				Constants.DEFAULT_PAGE_SIZE
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			LOGGER.error("Error when getting brand list: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public ResponseDataModel<BrandResponse> getById(int brandId) {
		try {
			BrandEntity brand = BrandRepository.findById(brandId).orElse(null);

			if (brand == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Brand not found");
			}

			BrandResponse data = new BrandResponse(
				brand.getBrandId(),
				brand.getBrandName(),
				brand.getLogo(),
				brand.getDescription()
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			LOGGER.error("Error when getting brand {}: {}", brandId, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public ResponseDataModel<BrandResponse> add(BrandRequest request) {
		try {
			/**
			 * Check if brand name exists
			 */
			if (BrandRepository.existsByBrandName(request.getBrandName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Brand name already exists");
			}

			BrandEntity brand = brandMapper.toEntity(request);
			BrandEntity savedBrand = BrandRepository.saveAndFlush(brand);
			BrandResponse data = brandMapper.toResponse(savedBrand);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS,
				"Brand is added successfully", data);
		} catch (Exception e) {
			LOGGER.error("Error when adding brand: {}", e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public ResponseDataModel<BrandResponse> update(int brandId, BrandRequest request) {
		try {
			BrandEntity brand = BrandRepository.findById(brandId).orElse(null);

			if (brand == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Brand not found");
			}

			/**
			 * Check duplication with other brands in DB
			 * If brand name changed and duplicate with another brand in DB -> error
			 */
			if (!brand.getBrandName().equals(request.getBrandName())
					&& BrandRepository.existsByBrandName(request.getBrandName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Brand name already exists");
			}

			brandMapper.updateEntity(request, brand);
			BrandEntity updatedBrand = BrandRepository.saveAndFlush(brand);
			BrandResponse data = brandMapper.toResponse(updatedBrand);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS,
				"Brand is updated successfully", data);
		} catch (Exception e) {
			LOGGER.error("Error when updating brand {}: {}", brandId, e.getMessage(), e);
			throw e;
		}
	}

	@Override
	public ResponseDataModel<Void> delete(int brandId) {
		try {
			BrandEntity brand = BrandRepository.findById(brandId).orElse(null);

			if (brand == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Brand not found");
			}

			/**
			 * Save brand logo path and all product image paths before delete
			 */
			String logo = brand.getLogo();
			List<String> productImages = brand.getProducts()
				.stream()
				.map(ProductEntity::getImage)
				.filter(image -> image != null && !image.isBlank())
				.toList();

			/**
			 * Delete all products that in brand, then delete brand, using orphanRemoval in BrandEntity
			 */
			BrandRepository.delete(brand);
			BrandRepository.flush();

			/**
			 * Delete logo and images
			 */
			FileHelper.deleteFile(logo);
			productImages.forEach(FileHelper::deleteFile);

			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS,
				"Brand and products in brand is deleted successfully");
		} catch (Exception e) {
			LOGGER.error("Error when deleting brand {}: {}", brandId, e.getMessage(), e);
			throw e;
		}
	}
}