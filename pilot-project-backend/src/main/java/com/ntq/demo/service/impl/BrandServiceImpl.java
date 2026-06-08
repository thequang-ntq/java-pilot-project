package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.dao.BrandDao;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.model.request.BrandRequest;
import com.ntq.demo.model.response.BrandResponse;
import com.ntq.demo.model.response.PageResponse;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.service.BrandService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

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

	@Value("${parent.folder.images.brand}")
	private String brandLogoFolderPath;

	@Autowired
	private BrandDao BrandDao;

	@Override
	public ResponseDataModel<PageResponse<BrandResponse>> getList(int page, String keyword) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			Pageable pageable = PageRequest.of(
				//JPA index start at 0, but FE start at 1
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				Sort.by("brandId").ascending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all brands that not been deleted / Search brands by keyword (brand name) and not been deleted
			 */
			Page<BrandEntity> brandPage = BrandDao.findByIsDeletedFalseAndBrandNameContainingIgnoreCase(
				keyword == null ? "" : keyword,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<BrandResponse> content = brandPage.getContent().stream()
				.map(b -> new BrandResponse(
					b.getBrandId(),
					b.getBrandName(),
					b.getLogo(),
					b.getDescription(),
					b.getIsDeleted()
				))
				.toList();

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
			responseMsg = "Error when getting brand list";
			LOGGER.error("Error when getting brand list: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<PageResponse<BrandResponse>> getListForAdmin(int page, String keyword) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			Pageable pageable = PageRequest.of(
				//JPA index start at 0, but FE start at 1
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				Sort.by("brandId").ascending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all brands that not been deleted / Search brands by keyword (brand name) and not been deleted
			 */
			Page<BrandEntity> brandPage = BrandDao.findByBrandNameContainingIgnoreCase(
				keyword == null ? "" : keyword,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<BrandResponse> content = brandPage.getContent().stream()
				.map(b -> new BrandResponse(
					b.getBrandId(),
					b.getBrandName(),
					b.getLogo(),
					b.getDescription(),
					b.getIsDeleted()
				))
				.toList();

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
			responseMsg = "Error when getting brand list for admin";
			LOGGER.error("Error when getting brand list for admin: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<BrandResponse> getById(int brandId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			BrandEntity brand = BrandDao.findById(brandId).orElse(null);

			if (brand == null || brand.getIsDeleted()) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Brand not found");
			}

			BrandResponse data = new BrandResponse(
				brand.getBrandId(),
				brand.getBrandName(),
				brand.getLogo(),
				brand.getDescription(),
				brand.getIsDeleted()
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting brand";
			LOGGER.error("Error when getting brand {}: {}", brandId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> add(BrandRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			/**
			 * Check if brand name exists
			 */
			if (BrandDao.existsByBrandName(request.getBrandName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Brand name already exists");
			}

			BrandEntity brand = new BrandEntity();
			brand.setBrandName(request.getBrandName());
			brand.setDescription(request.getDescription());
			brand.setIsDeleted(false);

			/**
			 * Logo image handling
			 */
			MultipartFile[] logoFiles = request.getLogoFiles();
			if (logoFiles != null && logoFiles[0].getSize() > 0) {
				String imagePath = FileHelper.editFile(brandLogoFolderPath, logoFiles, null);
				brand.setLogo(imagePath);
			}

			BrandDao.saveAndFlush(brand);

			responseMsg = "Brand is added successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when adding brand";
			LOGGER.error("Error when adding brand: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> update(int brandId, BrandRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			BrandEntity brand = BrandDao.findById(brandId).orElse(null);

			if (brand == null || brand.getIsDeleted()) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Brand not found");
			}

			/**
			 * Check duplication with other brands in DB
			 * If brand name changed and duplicate with another brand in DB -> error
			 */
			if (!brand.getBrandName().equals(request.getBrandName())
					&& BrandDao.existsByBrandName(request.getBrandName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Brand name already exists");
			}

			brand.setBrandName(request.getBrandName());
			brand.setDescription(request.getDescription());

			/**
			 * Logo image handling
			 * Edit File auto delete old image if brand has new image
			 */
			MultipartFile[] logoFiles = request.getLogoFiles();
			if (logoFiles != null && logoFiles[0].getSize() > 0) {
				String imagePath = FileHelper.editFile(brandLogoFolderPath, logoFiles, brand.getLogo());
				brand.setLogo(imagePath);
			}

			BrandDao.saveAndFlush(brand);

			responseMsg = "Brand is updated successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when updating brand";
			LOGGER.error("Error when updating brand {}: {}", brandId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> delete(int brandId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			BrandEntity brand = BrandDao.findById(brandId).orElse(null);

			if (brand == null || brand.getIsDeleted()) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Brand not found");
			}

			/**
			 * Soft delete brand and all products of that brand
			 */
			brand.setIsDeleted(true);
			brand.getProducts().forEach(p -> p.setIsDeleted(true));

			BrandDao.saveAndFlush(brand);

			responseMsg = "Brand is deleted successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when deleting brand";
			LOGGER.error("Error when deleting brand {}: {}", brandId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}