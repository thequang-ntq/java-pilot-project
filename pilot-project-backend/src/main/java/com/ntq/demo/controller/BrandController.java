package com.ntq.demo.controller;

import com.ntq.demo.dto.request.BrandRequest;
import com.ntq.demo.model.ResponseDataModel;
import com.ntq.demo.service.BrandService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to declare controller related to Brand management
 *
 * @author Quang
 * @since 2026-04-30
 */
@RestController
@RequestMapping("/api")
public class BrandController {

	@Autowired
	private BrandService brandService;

	/**
	 * GET /api/brands?page=1&keyword=apple
	 * Public - Guest, Admin can see
	 */
	@GetMapping("/brands")
	public ResponseDataModel<?> getList(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String keyword) {
		return brandService.getList(page, keyword);
	}

	/**
	 * GET /api/brands/1
	 * Public
	 */
	@GetMapping("/brands/{id}")
	public ResponseDataModel<?> getById(@PathVariable int id) {
		return brandService.getById(id);
	}

	/**
	 * POST /api/brands
	 * Admin only - add brand
	 */
	@PostMapping("/brands")
	public ResponseDataModel<?> add(@Valid @ModelAttribute BrandRequest request) {
		return brandService.add(request);
	}

	/**
	 * PUT /api/brands/1
	 * Admin only - update brand
	 */
	@PutMapping("/brands/{id}")
	public ResponseDataModel<?> update(@PathVariable int id, @Valid @ModelAttribute BrandRequest request) {
		return brandService.update(id, request);
	}

	/**
	 * DELETE /api/brands/1
	 * Admin only - delete brand + products in brand
	 */
	@DeleteMapping("/brands/{id}")
	public ResponseDataModel<?> delete(@PathVariable int id) {
		return brandService.delete(id);
	}
}