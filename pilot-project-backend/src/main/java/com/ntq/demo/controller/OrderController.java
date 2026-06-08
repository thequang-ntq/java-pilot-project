package com.ntq.demo.controller;

import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

/**
 * This class is used to declare controller related to Order management
 *
 * @author Quang
 * @since 2026-04-30
 */
@RestController
@RequestMapping("/api")
public class OrderController {
	@Autowired
	private OrderService orderService;

	// =====================================================================
	// USER - CART
	// =====================================================================

	/**
	 * GET /api/cart?page=1
	 * Get user's cart
	 * @AuthenticationPrincipal get accountName from JWT token has been set in SecurityContext
	 */
	@GetMapping("/cart")
	public ResponseDataModel<?> getCart(
			@AuthenticationPrincipal String accountName,
			@RequestParam(defaultValue = "1") int page) {
		return orderService.getCart(accountName, page);
	}

	/**
	 * POST /api/cart/products/1
	 * Add product into cart
	 */
	@PostMapping("/cart/products/{productId}")
	public ResponseDataModel<?> addToCart(
			@AuthenticationPrincipal String accountName,
			@PathVariable int productId) {
		return orderService.addToCart(accountName, productId);
	}

	/**
	 * PUT /api/cart/products/1?quantity=3
	 * Update product quantity in cart
	 */
	@PutMapping("/cart/products/{productId}")
	public ResponseDataModel<?> updateCartItem(
			@AuthenticationPrincipal String accountName,
			@PathVariable int productId,
			@RequestParam int quantity) {
		return orderService.updateCartItem(accountName, productId, quantity);
	}

	/**
	 * DELETE /api/cart/products/1
	 * Delete 1 product from cart
	 */
	@DeleteMapping("/cart/products/{productId}")
	public ResponseDataModel<?> removeCartItem(
			@AuthenticationPrincipal String accountName,
			@PathVariable int productId) {
		return orderService.removeCartItem(accountName, productId);
	}

	/**
	 * DELETE /api/cart
	 * Delete cart
	 */
	@DeleteMapping("/cart")
	public ResponseDataModel<?> clearCart(
			@AuthenticationPrincipal String accountName) {
		return orderService.clearCart(accountName);
	}

	/**
	 * POST /api/cart/confirm
	 * Confirm order -> status: PRE_ORDER -> NEW
	 */
	@PostMapping("/cart/confirm")
	public ResponseDataModel<?> confirmOrder(
			@AuthenticationPrincipal String accountName) {
		return orderService.confirmOrder(accountName);
	}

	// =====================================================================
	// USER - ORDER HISTORY
	// =====================================================================

	/**
	 * GET /api/orders?page=1&keyword=&status=NEW
	 * Order history of user
	 */
	@GetMapping("/orders")
	public ResponseDataModel<?> getOrderHistory(
			@AuthenticationPrincipal String accountName,
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "") String status) {
		return orderService.getOrderHistory(accountName, page, keyword, status);
	}

	/**
	 * GET /api/orders/1?page=1
	 * List order details of order of user
	 */
	@GetMapping("/orders/{orderId}")
	public ResponseDataModel<?> getOrderDetail(
			@AuthenticationPrincipal String accountName,
			@PathVariable int orderId,
			@RequestParam(defaultValue = "1") int page) {
		return orderService.getOrderDetail(accountName, orderId, page);
	}

	// =====================================================================
	// ADMIN - ORDER MANAGEMENT
	// =====================================================================

	/**
	 * GET /api/admin/orders?page=1&keyword=&status=NEW
	 * All orders
	 */
	@GetMapping("/admin/orders")
	public ResponseDataModel<?> getAllOrders(
			@RequestParam(defaultValue = "1") int page,
			@RequestParam(defaultValue = "") String keyword,
			@RequestParam(defaultValue = "") String status) {
		return orderService.getAllOrders(page, keyword, status);
	}

	/**
	 * GET /api/admin/orders/1?page=1
	 * List order details of order for admin
	 */
	@GetMapping("/admin/orders/{orderId}")
	public ResponseDataModel<?> getOrderDetailForAdmin(
			@PathVariable int orderId,
			@RequestParam(defaultValue = "1") int page) {
		return orderService.getOrderDetailForAdmin(orderId, page);
	}

	/**
	 * PUT /api/admin/orders/1/status?status=IN_PROGRESS
	 * Update order status
	 */
	@PutMapping("/admin/orders/{orderId}/status")
	public ResponseDataModel<?> updateOrderStatus(
			@PathVariable int orderId,
			@RequestParam String status) {
		return orderService.updateOrderStatus(orderId, status);
	}
}