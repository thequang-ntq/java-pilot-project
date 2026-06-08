package com.ntq.demo.service;

import com.ntq.demo.model.request.ProductRequest;
import com.ntq.demo.model.response.ResponseDataModel;

import java.math.BigDecimal;

/**
 * This interface is used to declare functions to handle logic and business for Product Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
public interface ProductService {
	ResponseDataModel<?> getList(int page, String keyword, BigDecimal priceFrom, BigDecimal priceTo);

	ResponseDataModel<?> getById(int productId);

	ResponseDataModel<?> add(ProductRequest request);

	ResponseDataModel<?> update(ProductRequest request);

	ResponseDataModel<?> delete(int productId);
}