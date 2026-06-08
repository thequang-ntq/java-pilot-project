package com.ntq.demo.security;

import com.ntq.demo.repository.UserRepository;
import com.ntq.demo.entity.UserEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * This class is used to load user from database for Spring Security
 * Called every request to verify user still exists and active in DB
 *
 * @author Quang
 * @since 2026-05-03
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private UserRepository UserRepository;

	/**
	 * Load user from DB by username
	 * Called by JwtAuthenticationFilter every request
	 *
	 * @param username from JWT token
	 * @return UserDetails with user info
	 * @throws UsernameNotFoundException if user not found in DB
	 */
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		UserEntity user = UserRepository.findByUsername(username).orElse(null);

		if (user == null) {
			LOGGER.warn("User not found in DB: {}", username);
			throw new UsernameNotFoundException("User not found: " + username);
		}

		LOGGER.info("Loaded user from DB: {}, role: {}", username, user.getRole().name());

		/**
		 * Build UserDetails from userEntity
		 * ROLE_ prefix required of Spring Security
		 */
		return new User(
			user.getUsername(),
			null,
			List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole().name()))
		);
	}
}