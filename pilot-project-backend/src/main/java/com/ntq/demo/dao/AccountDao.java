package com.ntq.demo.dao;

import com.ntq.demo.entity.AccountEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is used to declare functions to react with Database for Account Entities
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface AccountDao extends JpaRepository<AccountEntity, Integer> {
	/**
	 * Find by account name when login, optional means that it can be null
	 *
	 * @param accountName
	 * @return Account Entity or null
	 */
	Optional<AccountEntity> findByAccountName(String accountName);

	/**
	 * Check if account name exists when register
	 *
	 * @param accountName
	 * @return true if exists, false if not
	 */
	boolean existsByAccountName(String accountName);

	/**
	 * Find account by google id
	 *
	 * @param googleId
	 * @return AccountEntity
	 */
	Optional<AccountEntity> findByGoogleId(String googleId);
}
