package com.ntq.demo.service;

import com.ntq.demo.model.response.ResponseDataModel;

/**
 * This interface is used to declare functions to handle logic and business for Order Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
public interface OrderService {
	// ===================== USER =====================
	ResponseDataModel<?> getCart(String accountName, int page);

	ResponseDataModel<?> addToCart(String accountName, int productId);

	ResponseDataModel<?> updateCartItem(String accountName, int productId, int quantity);

	ResponseDataModel<?> removeCartItem(String accountName, int productId);

	ResponseDataModel<?> clearCart(String accountName);

	ResponseDataModel<?> confirmOrder(String accountName);

	ResponseDataModel<?> getOrderHistory(String accountName, int page, String keyword, String status);

	ResponseDataModel<?> getOrderDetail(String accountName, int orderId, int page);

	// ===================== ADMIN =====================
	ResponseDataModel<?> getAllOrders(int page, String keyword, String status);

	ResponseDataModel<?> getOrderDetailForAdmin(int orderId, int page);

	ResponseDataModel<?> updateOrderStatus(int orderId, String status);
}