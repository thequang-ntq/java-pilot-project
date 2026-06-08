package com.ntq.demo.service.impl;

import com.ntq.demo.common.constant.Constants;
import com.ntq.demo.dao.AccountDao;
import com.ntq.demo.dao.OrderDetailDao;
import com.ntq.demo.dao.OrderDao;
import com.ntq.demo.dao.ProductDao;
import com.ntq.demo.entity.*;
import com.ntq.demo.model.response.OrderDetailResponse;
import com.ntq.demo.model.response.OrderResponse;
import com.ntq.demo.model.response.PageResponse;
import com.ntq.demo.model.response.ResponseDataModel;
import com.ntq.demo.service.OrderService;
import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used to implement functions to handle logic and business for Order Entities
 *
 * @author Quang
 * @since 2026-04-29
 */
@Service
@Transactional
public class OrderServiceImpl implements OrderService {
	private final Logger LOGGER = LoggerFactory.getLogger(getClass());

	@Autowired
	private AccountDao AccountDao;

	@Autowired
	private ProductDao ProductDao;

	@Autowired
	private OrderDao OrderDao;

	@Autowired
	private OrderDetailDao OrderDetailDao;

	// =====================================================================
	// PRIVATE HELPER
	// =====================================================================

	/**
	 * Get or create pre-order for account
	 * Each account has only 1 pre-order at a time (the cart)
	 */
	private OrderEntity getOrCreateCart(AccountEntity account) {
		OrderEntity cart = OrderDao
			.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);

		if (cart == null) {
			cart = new OrderEntity();
			cart.setAccount(account);
			cart.setStatus(OrderEntity.Status.PRE_ORDER);
			OrderDao.saveAndFlush(cart);
		}
		return cart;
	}

	/**
	 * Map OrderEntity -> OrderResponse DTO
	 */
	private OrderResponse mapToOrderResponse(OrderEntity order) {
		return new OrderResponse(
			order.getOrderId(),
			order.getOrderTime(),
			order.getFinishTime(),
			order.getStatus().name()
		);
	}

	/**
	 * Map OrderDetailEntity -> OrderDetailResponse DTO
	 */
	private OrderDetailResponse mapToOrderDetailResponse(OrderDetailEntity detail) {
		BigDecimal totalPrice = detail.getSalePrice().multiply(BigDecimal.valueOf(detail.getQuantity()));
		return new OrderDetailResponse(
			detail.getProduct().getProductName(),
			detail.getProduct().getImage(),
			detail.getQuantity(),
			detail.getSalePrice(),
			totalPrice
		);
	}

	/**
	 * Build PageResponse of OrderDetailResponse from order
	 */
	private PageResponse<OrderDetailResponse> buildOrderDetailPage(OrderEntity order, int page) {
		Pageable pageable = PageRequest.of(
			page - 1,
			Constants.DEFAULT_PAGE_SIZE,
			Sort.by("id.productId").ascending()
		);

		Page<OrderDetailEntity> detailPage = OrderDetailDao.findByOrder(order, pageable);

		List<OrderDetailResponse> content = detailPage.getContent().stream()
			.map(this::mapToOrderDetailResponse).toList();
		return new PageResponse<>(
			content,
			page,
			detailPage.getTotalPages(),
			detailPage.getTotalElements(),
			Constants.DEFAULT_PAGE_SIZE
		);
	}

	// =====================================================================
	// USER
	// =====================================================================

	@Override
	public ResponseDataModel<PageResponse<OrderDetailResponse>> getCart(String accountName, int page) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			/**
			 * Get cart (pre-order), if not exists return empty cart
			 */
			OrderEntity cart = OrderDao
				.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);

			if (cart == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Cart is empty");
			}

			PageResponse<OrderDetailResponse> data = buildOrderDetailPage(cart, page);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting cart";
			LOGGER.error("Error when getting cart for {}: {}", accountName, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> addToCart(String accountName, int productId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			ProductEntity product = ProductDao.findById(productId).orElse(null);
			if (product == null || product.getIsDeleted()) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Product not found");
			}

			/**
			 * Get or create cart (pre-order)
			 */
			OrderEntity cart = getOrCreateCart(account);

			/**
			 * Check if product already in cart
			 * If yes -> quantity + 1
			 * If no  -> create new order detail with quantity = 1
			 */
			OrderDetailId detailId = new OrderDetailId(cart.getOrderId(), productId);
			OrderDetailEntity detail = OrderDetailDao.findById(detailId).orElse(null);

			if (detail != null) {
				detail.setQuantity(detail.getQuantity() + 1);
				OrderDetailDao.saveAndFlush(detail);
			} else {
				OrderDetailEntity newDetail = new OrderDetailEntity();
				newDetail.setId(detailId);
				newDetail.setOrder(cart);
				newDetail.setProduct(product);
				newDetail.setQuantity(1);
				newDetail.setSalePrice(product.getPrice());
				OrderDetailDao.saveAndFlush(newDetail);
			}

			responseMsg = "Product is added to cart successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when adding to cart";
			LOGGER.error("Error when adding product {} to cart: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> updateCartItem(String accountName, int productId, int quantity) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			OrderEntity cart = OrderDao
				.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);
			if (cart == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Cart not found");
			}

			OrderDetailId detailId = new OrderDetailId(cart.getOrderId(), productId);
			OrderDetailEntity detail = OrderDetailDao.findById(detailId).orElse(null);
			if (detail == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Product not found in cart");
			}

			/**
			 * Update quantity of product in cart
			 * quantity must be >= 1
			 */
			if (quantity < 1) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Quantity must be at least 1");
			}

			detail.setQuantity(quantity);
			OrderDetailDao.saveAndFlush(detail);

			responseMsg = "Cart item is updated successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when updating cart item";
			LOGGER.error("Error when updating cart item {}: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> removeCartItem(String accountName, int productId) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			OrderEntity cart = OrderDao
				.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);
			if (cart == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Cart not found");
			}

			OrderDetailId detailId = new OrderDetailId(cart.getOrderId(), productId);
			OrderDetailEntity detail = OrderDetailDao.findById(detailId).orElse(null);
			if (detail == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Product not found in cart");
			}

			/**
			 * Delete order detail
			 * If it is the last product in cart -> delete order (cart) as well
			 */
			OrderDetailDao.delete(detail);
			OrderDetailDao.flush();

			long remaining = OrderDetailDao.countByOrder(cart);
			if (remaining == 0) {
				OrderDao.delete(cart);
				OrderDao.flush();
			}

			responseMsg = "Product is removed from cart successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when removing cart item";
			LOGGER.error("Error when removing product {} from cart: {}", productId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> clearCart(String accountName) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			OrderEntity cart = OrderDao.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);
			if (cart == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Cart not found");
			}

			/**
			 * Delete all order details -> delete order (cart)
			 */
			OrderDetailDao.deleteByOrder(cart);
			OrderDetailDao.flush();
			OrderDao.delete(cart);
			OrderDao.flush();

			responseMsg = "Cart is cleared successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when clearing cart";
			LOGGER.error("Error when clearing cart for {}: {}", accountName, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> confirmOrder(String accountName) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			OrderEntity cart = OrderDao
				.findByAccountAndStatus(account, OrderEntity.Status.PRE_ORDER).orElse(null);
			if (cart == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Cart is empty");
			}

			/**
			 * Confirm order
			 * Change status from pre-order -> new
			 * Set order time to now
			 */
			cart.setStatus(OrderEntity.Status.NEW);
			cart.setOrderTime(LocalDateTime.now());
			OrderDao.saveAndFlush(cart);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Order is confirmed successfully");
		} catch (Exception e) {
			responseMsg = "Error when confirming order";
			LOGGER.error("Error when confirming order for {}: {}", accountName, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<PageResponse<OrderResponse>> getOrderHistory(String accountName, int page,
			  LocalDate dateFrom, LocalDate dateTo, String status) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			/**
			 * Parse status string -> enum, null if empty (show all instead of search)
			 */
			OrderEntity.Status statusEnum = null;
			if (status != null && !status.isBlank()) {
				statusEnum = OrderEntity.Status.valueOf(status.toUpperCase());
			}

			LocalDateTime from = dateFrom != null
				? dateFrom.atStartOfDay()
				: null;
			LocalDateTime to = dateTo != null
				? dateTo.atTime(23, 59, 59)
				: null;

			Pageable pageable = PageRequest.of(
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				Sort.by("orderId").descending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all orders / Search orders by keyword (order time, finish time) and status of user account
			 */
			Page<OrderEntity> orderPage = OrderDao.searchOrdersByAccount(
				account,
				statusEnum,
				from,
				to,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<OrderResponse> content = orderPage.getContent().stream().map(this::mapToOrderResponse).toList();

			/**
			 * Page Response
			 */
			PageResponse<OrderResponse> data = new PageResponse<>(
				content,
				page,
				orderPage.getTotalPages(),
				orderPage.getTotalElements(),
				Constants.DEFAULT_PAGE_SIZE
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting order history";
			LOGGER.error("Error when getting order history for {}: {}", accountName, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<PageResponse<OrderDetailResponse>> getOrderDetail(String accountName, int orderId, int page) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);
			if (account == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Account not found");
			}

			OrderEntity order = OrderDao.findById(orderId).orElse(null);

			/**
			 * Check order exists and belongs to account
			 */
			if (order == null || !order.getAccount().getAccountId().equals(account.getAccountId())) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Order not found");
			}

			/**
			 * Page Response: List Order Details of an Order
			 */
			PageResponse<OrderDetailResponse> data = buildOrderDetailPage(order, page);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting order detail";
			LOGGER.error("Error when getting order detail {} for {}: {}", orderId, accountName, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	// =====================================================================
	// ADMIN
	// =====================================================================

	@Override
	public ResponseDataModel<PageResponse<OrderResponse>> getAllOrders(int page, LocalDate dateFrom,
			   LocalDate dateTo, String status) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			/**
			 * Parse status string -> enum, null if empty (show all)
			 */
			OrderEntity.Status statusEnum = null;
			if (status != null && !status.isBlank()) {
				statusEnum = OrderEntity.Status.valueOf(status.toUpperCase());
			}

			LocalDateTime from = dateFrom != null
				? dateFrom.atStartOfDay()
				: null;
			LocalDateTime to = dateTo != null
				? dateTo.atTime(23, 59, 59)
				: null;

			Pageable pageable = PageRequest.of(
				page - 1,
				Constants.DEFAULT_PAGE_SIZE,
				Sort.by("orderId").descending()
			);

			/**
			 * JPA page object that is database query result
			 * Get all orders / Search orders by keyword (order time, finish time) and status of all user
			 */
			Page<OrderEntity> orderPage = OrderDao.searchAllOrders(
				statusEnum,
				from,
				to,
				pageable
			);

			/**
			 * Map Entity -> Response DTO
			 */
			List<OrderResponse> content = orderPage.getContent().stream().map(this::mapToOrderResponse).toList();

			/**
			 * Page Response
			 */
			PageResponse<OrderResponse> data = new PageResponse<>(
				content,
				page,
				orderPage.getTotalPages(),
				orderPage.getTotalElements(),
				Constants.DEFAULT_PAGE_SIZE
			);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting all orders";
			LOGGER.error("Error when getting all orders: {}", e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<PageResponse<OrderDetailResponse>> getOrderDetailForAdmin(int orderId, int page) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			OrderEntity order = OrderDao.findById(orderId).orElse(null);
			if (order == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Order not found");
			}

			/**
			 * Page Response: List Order Details of an Order
			 */
			PageResponse<OrderDetailResponse> data = buildOrderDetailPage(order, page);
			return new ResponseDataModel<>(Constants.RESULT_CD_SUCCESS, "Success", data);
		} catch (Exception e) {
			responseMsg = "Error when getting order detail";
			LOGGER.error("Error when getting order detail {}: {}", orderId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}

	@Override
	public ResponseDataModel<Void> updateOrderStatus(int orderId, String status) {
		int responseCode = Constants.RESULT_CD_FAIL;
		String responseMsg = "";

		try {
			OrderEntity order = OrderDao.findById(orderId).orElse(null);
			if (order == null) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Order not found");
			}

			/**
			 * Validate status transition:
			 * PRE_ORDER -> NEW
			 * NEW -> IN_PROGRESS / FAILED
			 * IN_PROGRESS -> COMPLETED / FAILED
			 * COMPLETED / FAILED -> Cannot change
			 */
			OrderEntity.Status currentStatus = order.getStatus();
			OrderEntity.Status newStatus;

			try {
				/**
				 * Get new status by upper case the status that been request to
				 */
				newStatus = OrderEntity.Status.valueOf(status.toUpperCase());
			} catch (IllegalArgumentException e) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID, "Invalid status: " + status);
			}

			boolean validTransition = switch (currentStatus) {
				case PRE_ORDER -> newStatus == OrderEntity.Status.NEW;
				case NEW -> newStatus == OrderEntity.Status.IN_PROGRESS
						|| newStatus == OrderEntity.Status.FAILED;
				case IN_PROGRESS -> newStatus == OrderEntity.Status.COMPLETED
						|| newStatus == OrderEntity.Status.FAILED;
				default -> false; // COMPLETED / FAILED -> Cannot change
			};

			if (!validTransition) {
				return new ResponseDataModel<>(Constants.RESULT_CD_INVALID,
					"Cannot change status from " + currentStatus + " to " + newStatus);
			}

			order.setStatus(newStatus);

			/**
			 * Set finish time when order is completed or failed
			 */
			if (newStatus == OrderEntity.Status.COMPLETED
					|| newStatus == OrderEntity.Status.FAILED) {
				order.setFinishTime(LocalDateTime.now());
			}

			OrderDao.saveAndFlush(order);

			responseMsg = "Order status is updated successfully";
			responseCode = Constants.RESULT_CD_SUCCESS;
		} catch (Exception e) {
			responseMsg = "Error when updating order status";
			LOGGER.error("Error when updating order status {}: {}", orderId, e.getMessage(), e);
		}
		return new ResponseDataModel<>(responseCode, responseMsg);
	}
}