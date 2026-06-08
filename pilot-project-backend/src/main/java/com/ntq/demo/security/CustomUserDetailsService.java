package com.ntq.demo.security;

import com.ntq.demo.dao.AccountDao;
import com.ntq.demo.entity.AccountEntity;
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
 * This class is used to load account from database for Spring Security
 * Called every request to verify account still exists and active in DB
 *
 * @author Quang
 * @since 2026-05-03
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {
	private static final Logger LOGGER = LoggerFactory.getLogger(CustomUserDetailsService.class);

	@Autowired
	private AccountDao AccountDao;

	/**
	 * Load account from DB by account name
	 * Called by JwtAuthenticationFilter every request
	 *
	 * @param accountName from JWT token
	 * @return UserDetails with account info
	 * @throws UsernameNotFoundException if account not found in DB
	 */
	@Override
	public UserDetails loadUserByUsername(String accountName) throws UsernameNotFoundException {
		AccountEntity account = AccountDao.findByAccountName(accountName).orElse(null);

		if (account == null) {
			LOGGER.warn("Account not found in DB: {}", accountName);
			throw new UsernameNotFoundException("Account not found: " + accountName);
		}

		LOGGER.info("Loaded account from DB: {}, role: {}", accountName, account.getRole().name());

		/**
		 * Build UserDetails from AccountEntity
		 * ROLE_ prefix required of Spring Security
		 */
		return new User(
			account.getAccountName(),
			account.getPassword(),
			List.of(new SimpleGrantedAuthority("ROLE_" + account.getRole().name()))
		);
	}
}