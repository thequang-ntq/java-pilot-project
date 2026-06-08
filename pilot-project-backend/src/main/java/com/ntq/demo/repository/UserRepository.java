package com.ntq.demo.repository;

import com.ntq.demo.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * This interface is used to declare functions to react with Database for User Entity
 *
 * @author Quang
 * @since 2026-04-27
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {
	/**
	 * Find by username when login, optional means that it can be null
	 *
	 * @param username
	 * @return User Entity or null
	 */
	Optional<UserEntity> findByUsername(String username);
}
