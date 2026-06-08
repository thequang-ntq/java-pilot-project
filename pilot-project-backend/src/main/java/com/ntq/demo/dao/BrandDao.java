package com.ntq.demo.dao;

import com.ntq.demo.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is used to declare functions to react with Database for Brand Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface BrandDao extends JpaRepository<BrandEntity, Integer> {
	/**
	 * List Brands that not deleted and search by brand name (ignore case)
	 *
	 * @param brandName
	 * @param pageable
	 * @return List Brands with pagination
	 */
	Page<BrandEntity> findByIsDeletedFalseAndBrandNameContainingIgnoreCase(String brandName, Pageable pageable);

	/**
	 * List Brands search by brand name (ignore case)
	 * @param brandName
	 * @param pageable
	 * @return List Brands with pagination
	 */
	Page<BrandEntity> findByBrandNameContainingIgnoreCase(String brandName, Pageable pageable);

	/**
	 * Check if brand name exists (Create/Update)
	 *
	 * @param brandName
	 * @return true if exists, false if not
	 */
	boolean existsByBrandName(String brandName);
}
