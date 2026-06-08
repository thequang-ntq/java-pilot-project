package com.ntq.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

/**
 * This class is used to declare properties and mapping them with order table from database
 *
 * @author Quang
 * @since 2026-04-27
 */
@Entity
@Table(name = "`order`")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "order_id")
	private Integer orderId;

	/**
	 * User account of this order (owning side, uses account_id of order table as foreign key).
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "account_id", nullable = false)
	private AccountEntity account;

	@Column(name = "order_time")
	private LocalDateTime orderTime;

	@Column(name = "finish_time")
	private LocalDateTime finishTime;

	@Enumerated(EnumType.STRING)
	@Column(name = "status", nullable = false)
	private Status status = Status.PRE_ORDER;

	/**
	 * Order details of this order (inverse side, mapped by OrderDetail."order").
	 */
	@OneToMany(mappedBy = "order", fetch = FetchType.LAZY)
	private List<OrderDetailEntity> orderDetails;

	public enum Status {
		PRE_ORDER,
		NEW,
		IN_PROGRESS,
		COMPLETED,
		FAILED
	}
}
