package com.ntq.demo.dao;

import com.ntq.demo.entity.AccountEntity;
import com.ntq.demo.entity.RefreshTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is used to declare functions to react with Database for Refresh Token Entities
 *
 * @author Quang
 * @since 2026-05-05
 */
@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshTokenEntity, Integer> {
	/**
	 * Find by token to verify
	 * @param token
	 * @return token
	 */
	Optional<RefreshTokenEntity> findByToken(String token);

	/**
	 * Delete old token when login again or logout
	 * @param account
	 */
	void deleteByAccount(AccountEntity account);
}
