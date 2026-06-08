package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to declare properties and mapping them with account table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "account")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "account_id")
	private Integer accountId;

	@Column(name = "account_name", nullable = false, unique = true, length = 50)
	private String accountName;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role = Role.USER;

	@Column(name = "email", unique = true)
	private String email;

	@Column(name = "google_id", unique = true)
	private String googleId;

	@Enumerated(EnumType.STRING)
	@Column(name = "auth_type", nullable = false)
	private AuthType authType = AuthType.LOCAL;

	public enum Role {
		ADMIN,
		USER
	}

	public enum AuthType {
		LOCAL,
		GOOGLE
	}
}
