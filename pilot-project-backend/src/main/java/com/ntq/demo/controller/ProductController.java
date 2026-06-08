package com.ntq.demo.controller;

import com.ntq.demo.model.request.ProductRequest;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

/**
 * This class is used to declare controller related to Product management
 *
 * @author Quang
 * @since 2026-04-30
 */
@RestController
@RequestMapping("/api")
public class ProductController {
	@Autowired
	private ProductService productService;

	/**
	 * GET /api/products?page=1&keyword=iphone&priceFrom=1000000&priceTo=5000000
	 * Public
	 */
	@GetMapping("/products")
	public ResponseDataModel<?> getList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(required = false) BigDecimal priceFrom,
			@RequestParam(required = false) BigDecimal priceTo) {
		return productService.getList(page, keyword, priceFrom, priceTo);
	}

	/**
	 * GET /api/products/1
	 * Public
	 */
	@GetMapping("/products/{id}")
	public ResponseDataModel<?> getById(@PathVariable int id) {
		return productService.getById(id);
	}

	/**
	 * POST /api/admin/products
	 * Admin only - add
	 */
	@PostMapping("/admin/products")
	public ResponseDataModel<?> add(@Valid @ModelAttribute ProductRequest request) {
		return productService.add(request);
	}

	/**
	 * PUT /api/admin/products
	 * Admin only - update
	 */
	@PutMapping("/admin/products")
	public ResponseDataModel<?> update(@Valid @ModelAttribute ProductRequest request) {
		return productService.update(request);
	}

	/**
	 * DELETE /api/admin/products/1
	 * Admin only - soft delete
	 */
	@DeleteMapping("/admin/products/{id}")
	public ResponseDataModel<?> delete(@PathVariable int id) {
		return productService.delete(id);
	}
}