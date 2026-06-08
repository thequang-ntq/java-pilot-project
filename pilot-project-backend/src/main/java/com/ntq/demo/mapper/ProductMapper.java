package com.ntq.demo.mapper;

import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.dto.request.ProductRequest;
import com.ntq.demo.dto.response.ProductResponse;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.entity.ProductEntity;
import com.ntq.demo.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is used to map between ProductEntity and Product DTOs
 *
 * @author Quang
 * @since 2026-05-07
 */
@Component
public class ProductMapper {
	@Value("${parent.folder.images.product}")
	private String productImageFolderPath;

	/**
	 * ProductEntity -> ProductResponse (getList)
	 *
	 * @param product
	 * @return ProductEntity
	 */
	public ProductResponse toResponse(ProductEntity product) {
		if(product == null) return null;

		ProductResponse response = new ProductResponse();
		response.setProductId(product.getProductId());
		response.setProductName(product.getProductName());
		response.setQuantity(product.getQuantity());
		response.setPrice(product.getPrice());
		response.setSaleDate(product.getSaleDate());
		response.setImage(product.getImage());
		response.setDescription(product.getDescription());

		if (product.getBrand() != null) {
			response.setBrandId(product.getBrand().getBrandId());
			response.setBrandName(product.getBrand().getBrandName());
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
		if (request == null || brand == null) return null;

		ProductEntity product = new ProductEntity();
		product.setProductName(request.getProductName());
		product.setQuantity(request.getQuantity());
		product.setPrice(request.getPrice());
		product.setBrand(brand);
		product.setSaleDate(request.getSaleDate());
		/**
		 * Product image handling
		 */
		MultipartFile[] imageFiles = request.getImageFiles();
		if (imageFiles != null && imageFiles[0].getSize() > 0) {
			/**
			 * Check if file is image and right type, throw upto ServiceImpl layer to catch
			 */
			if (!FileHelper.isImageFile(imageFiles[0])) {
				throw new InvalidFileException("Invalid image file type");
			}
			String imagePath = FileHelper.editFile(productImageFolderPath, imageFiles, null);
			product.setImage(imagePath);
		}
		product.setDescription(request.getDescription());
		return product;
	}

	/**
	 * Update ProductEntity: get data from ProductRequest
	 *
	 * @param request
	 * @param product
	 * @param brand
	 */
	public void updateEntity(ProductRequest request, ProductEntity product, BrandEntity brand) {
		if (request == null || product == null || brand == null) return;

		product.setProductName(request.getProductName());
		product.setQuantity(request.getQuantity());
		product.setPrice(request.getPrice());
		product.setBrand(brand);
		product.setSaleDate(request.getSaleDate());
		/**
		 * Product image handling
		 * Edit File auto delete old image if product has new image
		 */
		MultipartFile[] imageFiles = request.getImageFiles();

		/**
		 * Delete file if user delete in FE
		 */
		if ("true".equals(request.getDeleteImage())) {
			FileHelper.deleteFile(product.getImage());
			product.setImage(null);
		}

		/**
		 * Has new file
		 */
		if (imageFiles != null && imageFiles.length > 0 && imageFiles[0].getSize() > 0) {
			/**
			 * Check if file is image and right type, throw upto ServiceImpl layer to catch
			 */
			if (!FileHelper.isImageFile(imageFiles[0])) {
				throw new InvalidFileException("Invalid image file type");
			}
			String imagePath = FileHelper.editFile(productImageFolderPath, imageFiles, product.getImage());
			product.setImage(imagePath);
		}
		product.setDescription(request.getDescription());
	}
}
