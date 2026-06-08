package com.ntq.demo.dao;

import com.ntq.demo.entity.AccountEntity;
import com.ntq.demo.entity.OrderEntity;
import com.ntq.demo.entity.OrderEntity.Status;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

/**
 * This interface is used to declare functions to react with Database for Order Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface OrderDao extends JpaRepository<OrderEntity, Integer> {
	/**
	 * Get user's cart (PRE_ORDER order)
	 *
	 * @param account
	 * @param status
	 * @return OrderEntity with PRE_ORDER status and belong to user account
	 */
	Optional<OrderEntity> findByAccountAndStatus (AccountEntity account, Status status);

	/**
	 * Get user's order history (no PRE_ORDER), search by status, dateFrom, dateTo of orderTime
	 *
	 * @param account
	 * @param status
	 * @param dateFrom
	 * @param dateTo
	 * @param pageable
	 * @return List Orders of a user with pagination
	 */
	@Query(""" 
		SELECT o FROM OrderEntity o
		WHERE o.account = :account
		AND o.status != 'PRE_ORDER'
		AND (:status IS NULL OR o.status = :status)
		AND (:dateFrom IS NULL OR o.orderTime >= :dateFrom)
		AND (:dateTo IS NULL OR o.orderTime <= :dateTo)
	""")
	Page<OrderEntity> searchOrdersByAccount(
		@Param("account") AccountEntity account,
		@Param("status") Status status,
		@Param("dateFrom") LocalDateTime dateFrom,
		@Param("dateTo") LocalDateTime dateTo,
		Pageable pageable
	);

	/**
	 * Get order history of all users for admin (no PRE_ORDER)
	 *
	 * @param status
	 * @param dateFrom
	 * @param dateTo
	 * @param pageable
	 * @return List Order of all users with Pagination
	 */
	@Query("""
		SELECT o FROM OrderEntity o
		WHERE o.status != 'PRE_ORDER'
		AND (:status IS NULL OR o.status = :status)
		AND (:dateFrom IS NULL OR o.orderTime >= :dateFrom)
		AND (:dateTo IS NULL OR o.orderTime <= :dateTo)
	""")
	Page<OrderEntity> searchAllOrders(
		@Param("status") Status status,
		@Param("dateFrom") LocalDateTime dateFrom,
		@Param("dateTo") LocalDateTime dateTo,
		Pageable pageable
	);
}
