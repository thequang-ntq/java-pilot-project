package com.ntq.demo.mapper;

import com.ntq.demo.common.util.FileHelper;
import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.dto.response.BrandResponse;
import com.ntq.demo.entity.BrandEntity;
import com.ntq.demo.exception.InvalidFileException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

/**
 * This class is used to map between BrandEntity and Brand DTOs
 *
 * @author Quang
 * @since 2026-05-07
 */
@Component
public class BrandMapper {
	@Value("${parent.folder.images.brand}")
	private String brandLogoFolderPath;

	/**
	 * BrandEntity -> BrandResponse (getList)
	 *
	 * @param brand
	 * @return BrandResponse
	 */
	public BrandResponse toResponse(BrandEntity brand) {
		if (brand == null) return null;

		BrandResponse response = new BrandResponse();
		response.setBrandId(brand.getBrandId());
		response.setBrandName(brand.getBrandName());
		response.setLogo(brand.getLogo());
		response.setDescription(brand.getDescription());
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

		BrandEntity brand = new BrandEntity();
		brand.setBrandName(request.getBrandName());
		/**
		 * Logo image handling
		 */
		MultipartFile[] logoFiles = request.getLogoFiles();
		if (logoFiles != null && logoFiles[0].getSize() > 0) {
			/**
			 * Check if file is image and right type, throw upto ServiceImpl layer to catch
			 */
			if (!FileHelper.isImageFile(logoFiles[0])) {
				throw new InvalidFileException("Invalid image file type");
			}
			String imagePath = FileHelper.editFile(brandLogoFolderPath, logoFiles, null);
			brand.setLogo(imagePath);
		}
		brand.setDescription(request.getDescription());
		return brand;
	}

	/**
	 * Update BrandEntity: get data from BrandRequest
	 *
	 * @param request
	 * @param brand
	 */
	public void updateEntity(BrandRequest request, BrandEntity brand) {
		if (request == null || brand == null) return;

		brand.setBrandName(request.getBrandName());
		/**
		 * Logo image handling
		 * Edit File auto delete old image if brand has new image
		 */
		MultipartFile[] logoFiles = request.getLogoFiles();

		/**
		 * Delete file if user delete in FE
		 */
		if ("true".equals(request.getDeleteLogo())) {
			FileHelper.deleteFile(brand.getLogo());
			brand.setLogo(null);
		}

		/**
		 * Has new file
		 */
		if (logoFiles != null && logoFiles.length > 0 && logoFiles[0].getSize() > 0) {
			/**
			 * Check if file is image and right type, throw upto ServiceImpl layer to catch
			 */
			if (!FileHelper.isImageFile(logoFiles[0])) {
				throw new InvalidFileException("Invalid image file type");
			}
			String imagePath = FileHelper.editFile(brandLogoFolderPath, logoFiles, brand.getLogo());
			brand.setLogo(imagePath);
		}
		brand.setDescription(request.getDescription());
	}
}
