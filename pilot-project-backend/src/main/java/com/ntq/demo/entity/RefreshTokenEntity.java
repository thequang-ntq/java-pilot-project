package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * This class is used to declare properties and mapping them with refresh_tokens table from database
 *
 * @author Quang
 * @since 2026-05-05
 */
@Entity
@Table(name = "refresh_tokens")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RefreshTokenEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "token_id")
	private Integer tokenId;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id", nullable = false)
	private UserEntity user;

	@Column(name = "token", nullable = false, unique = true, length = 50)
	private String token;

	@Column(name = "expired_at", nullable = false)
	private LocalDateTime expiredAt;
}
