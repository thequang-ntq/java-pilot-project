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
	 * Get user's order history (no PRE_ORDER)
	 *
	 * @param account
	 * @param status
	 * @param keyword
	 * @param pageable
	 * @return List Orders of a user with pagination
	 */
	@Query(""" 
		SELECT o FROM OrderEntity o
		WHERE o.account = :account
		AND o.status != 'PRE_ORDER'
		AND (:status IS NULL OR o.status = :status)
		AND (:keyword IS NULL OR
			CAST(o.orderTime  AS string) LIKE CONCAT('%', :keyword, '%') OR
			CAST(o.finishTime AS string) LIKE CONCAT('%', :keyword, '%'))
	""")
	Page<OrderEntity> searchOrdersByAccount(
		@Param("account") AccountEntity account,
		@Param("status")  Status status,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	/**
	 * Get order history of all users for admin (no PRE_ORDER)
	 *
	 * @param status
	 * @param keyword
	 * @param pageable
	 * @return List Order of all users with Pagination
	 */
	@Query("""
		SELECT o FROM OrderEntity o
		WHERE o.status != 'PRE_ORDER'
		AND (:status IS NULL OR o.status = :status)
		AND (:keyword IS NULL OR
			CAST(o.orderTime  AS string) LIKE CONCAT('%', :keyword, '%') OR
			CAST(o.finishTime AS string) LIKE CONCAT('%', :keyword, '%'))
	""")
	Page<OrderEntity> searchAllOrders(
		@Param("status")  Status status,
		@Param("keyword") String keyword,
		Pageable pageable
	);

	/**
	 * Count orders of account, not status mentioned
	 *
	 * @param account
	 * @param status
	 * @return number of orders
	 */
	long countByAccountAndStatusNot(AccountEntity account, OrderEntity.Status status);
}
