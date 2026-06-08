package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.mapper.ProductMapper;
import com.ntq.demo.repository.BrandRepository;
import com.ntq.demo.repository.ProductRepository;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.entity.ProductEntity;
import com.ntq.demo.dto.request.ProductRequest;
import com.ntq.demo.model.PageResponse;
import com.ntq.demo.dto.response.ProductResponse;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.service.ProductService;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;

/**
 * This class is used to implement functions to handle logic and business for Product Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
@Transactional
public class ProductServiceImpl implements ProductService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Value("${parent.folder.images.product}")
	private String productImageFolderPath;

	@Autowired
	private ProductRepository ProductRepository;

	@Autowired
	private BrandRepository BrandRepository;

	@Autowired
	private ProductMapper productMapper;

	@Override
	public ResponseDataModel<PageResponse<ProductResponse>> getList(int page, String keyword, BigDecimal priceFrom, BigDecimal priceTo) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			Pageable pageable = PageRequest.of(
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				Sort.by("productId").ascending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all products / Search products by keyword (product name / brand name) and price range
			 */
			Page<ProductEntity> productPage = ProductRepository.searchProducts(
				keyword == null ? "" : keyword,
				priceFrom,
				priceTo,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<ProductResponse> content = productPage.getContent().stream()
				.map(productMapper::toResponse).toList();

			/**
			 * Page Response
			 */
			PageResponse<ProductResponse> data = new PageResponse<>(
				content,
				page,
				productPage.getTotalPages(),
				productPage.getTotalElements(),
				Constants.DEFAULT_PAGE_SIZE
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting product list";
			LOGGER.error("Error when getting product list: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<ProductResponse> getById(int productId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			ProductEntity product = ProductRepository.findById(productId).orElse(null);

			if (product == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Product not found");
			}

			ProductResponse data = new ProductResponse(
				product.getProductId(),
				product.getProductName(),
				product.getQuantity(),
				product.getPrice(),
				product.getBrand().getBrandId(),
				product.getBrand().getBrandName(),
				product.getSaleDate(),
				product.getImage(),
				product.getDescription()
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting product";
			LOGGER.error("Error when getting product {}: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> add(ProductRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			/**
			 * Check if product name exists
			 */
			if (ProductRepository.existsByProductName(request.getProductName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Product name already exists");
			}

			/**
			 * Check if brand exists
			 */
			BrandEntity brand = BrandRepository.findById(request.getBrandId()).orElse(null);
			if (brand == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Brand not found");
			}

			ProductEntity product = productMapper.toEntity(request, brand);

			/**
			 * Product image handling
			 */
			MultipartFile[] imageFiles = request.getImageFiles();
			if (imageFiles != null && imageFiles[0].getSize() > 0) {
				String imagePath = FileHelper.editFile(productImageFolderPath, imageFiles, null);
				product.setImage(imagePath);
			}

			ProductRepository.saveAndFlush(product);

			responseMsg = "Product is added successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when adding product";
			LOGGER.error("Error when adding product: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> update(int productId, ProductRequest request) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			ProductEntity product = ProductRepository.findById(productId).orElse(null);

			if (product == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Product not found");
			}

			/**
			 * Check duplication with other products in DB
			 * If product name changed and duplicate with another product -> error
			 */
			if (!product.getProductName().equals(request.getProductName())
					&& ProductRepository.existsByProductName(request.getProductName())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_DUPL, "Product name already exists");
			}

			/**
			 * Check if brand exists
			 */
			BrandEntity brand = BrandRepository.findById(request.getBrandId()).orElse(null);
			if (brand == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Brand not found");
			}

			productMapper.updateEntity(request, product, brand);

			/**
			 * Product image handling
			 * Edit File auto delete old image if product has new image
			 */
			MultipartFile[] imageFiles = request.getImageFiles();
			if (imageFiles != null && imageFiles[0].getSize() > 0) {
				String imagePath = FileHelper.editFile(productImageFolderPath, imageFiles, product.getImage());
				product.setImage(imagePath);
			}

			ProductRepository.saveAndFlush(product);

			responseMsg = "Product is updated successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when updating product";
			LOGGER.error("Error when updating product {}: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> delete(int productId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			ProductEntity product = ProductRepository.findById(productId).orElse(null);

			if (product == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_NOT_FOUND, "Product not found");
			}

			String image = product.getImage();

			/**
			 * Delete product
			 */
			ProductRepository.delete(product);
			ProductRepository.flush();

			FileHelper.deleteFile(image);

			responseMsg = "Product is deleted successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when deleting product";
			LOGGER.error("Error when deleting product {}: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}