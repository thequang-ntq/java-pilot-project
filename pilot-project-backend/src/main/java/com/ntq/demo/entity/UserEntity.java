package com.ntq.demo.entity;

import com.ntq.demo.model.Role;
import jakarta.persistence.*;
import lombok.*;

/**
 * This class is used to declare properties and mapping them with users table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Integer userId;

	@Column(name = "username", nullable = false, unique = true, length = 50)
	private String username;

	@Column(name = "password", nullable = false)
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "role", nullable = false)
	private Role role = Role.ADMIN;
}
