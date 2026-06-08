package com.ntq.demo.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

/**
 * This class is used to declare id for order_detail entity
 *
 * @author Quang
 * @since 2026-04-27
 */
@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class OrderDetailId implements Serializable {
	private Integer orderId;
	private Integer productId;
}
