package com.ntq.demo.service;

import com.ntq.demo.dto.request.ProductRequest;
import com.ntq.demo.model.ResponseDataModel;

import java.math.BigDecimal;

/**
 * This interface is used to declare functions to handle logic and business for Product Entity
 *
 * @author Quang
 * @since 2026-04-29
 */
public interface ProductService {
	ResponseDataModel<?> getList(int page, String keyword, BigDecimal priceFrom, BigDecimal priceTo);

	ResponseDataModel<?> getById(int productId);

	ResponseDataModel<?> add(ProductRequest request);

	ResponseDataModel<?> update(int productId, ProductRequest request);

	ResponseDataModel<?> delete(int productId);
}