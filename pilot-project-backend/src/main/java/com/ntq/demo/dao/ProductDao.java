package com.ntq.demo.dao;

import com.ntq.demo.entity.ProductEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * This interface is used to declare functions to react with Database for Product Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface ProductDao extends JpaRepository<ProductEntity, Integer> {
	/**
	 * Search by not deleted + (product_name or brand_name) + price range + is_deleted
	 *
	 * @param keyword
	 * @param priceFrom
	 * @param priceTo
	 * @param pageable
	 * @return List Products with Pagination
	 */
	@Query(""" 
		SELECT p FROM ProductEntity p
		JOIN FETCH p.brand b
		WHERE p.isDeleted = false
		AND b.isDeleted = false
		AND (:keyword IS NULL OR
			LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) OR
			LOWER(b.brandName)   LIKE LOWER(CONCAT('%', :keyword, '%')))
		AND (:priceFrom IS NULL OR p.price >= :priceFrom)
		AND (:priceTo   IS NULL OR p.price <= :priceTo)
	""")
	Page<ProductEntity> searchProducts(
		@Param("keyword") String keyword,
		@Param("priceFrom") BigDecimal priceFrom,
		@Param("priceTo") BigDecimal priceTo,
		Pageable pageable
	);

	/**
	 * Check if product name exists (Create/Update)
	 *
	 * @param productName
	 * @return true if exists, false if not
	 */
	boolean existsByProductName(String productName);
}
