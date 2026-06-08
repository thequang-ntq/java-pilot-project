package com.ntq.demo.dao;

import com.ntq.demo.entity.OrderDetailEntity;
import com.ntq.demo.entity.OrderDetailId;
import com.ntq.demo.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is used to declare functions to react with Database for Order Detail Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface OrderDetailDao extends JpaRepository<OrderDetailEntity, OrderDetailId> {

	/**
	 * Get List order details of an Order
	 *
	 * @param order
	 * @param pageable
	 * @return List order details of an Order with Pagination
	 */
	Page<OrderDetailEntity> findByOrder(OrderEntity order, Pageable pageable);

	/**
	 * Count all products in an order (Delete Cart)
	 *
	 * @param order
	 * @return number of products in an order
	 */
	long countByOrder(OrderEntity order);

	/**
	 * Delete all products in an order (Delete Cart)
	 *
	 * @param order
	 */
	void deleteByOrder(OrderEntity order);
}