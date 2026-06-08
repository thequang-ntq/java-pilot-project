package com.ntq.demo.repository;

import com.ntq.demo.entity.BrandEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * This interface is used to declare functions to react with Database for Brand Entity
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface BrandRepository extends JpaRepository<BrandEntity, Integer> {
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
