package com.ntq.demo.service;

import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.model.ResponseDataModel;

/**
 * This interface is used to declare functions to handle logic and business for Brand Entity
 *
 * @author Quang
 * @since 2026-04-29
 */
public interface BrandService {
	ResponseDataModel<?> getList(int page, String keyword);

	ResponseDataModel<?> getById(int brandId);

	ResponseDataModel<?> add(BrandRequest request);

	ResponseDataModel<?> update(int brandId, BrandRequest request);

	ResponseDataModel<?> delete(int brandId);
}