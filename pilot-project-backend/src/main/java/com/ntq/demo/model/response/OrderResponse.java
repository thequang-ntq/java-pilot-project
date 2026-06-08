package com.ntq.demo.model.response;

import lombok.*;

import java.time.LocalDateTime;

/**
 * This class is used to declare properties for order response data to client-side (DTO)
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
	private Integer orderId;
	private LocalDateTime orderTime;
	private LocalDateTime finishTime;
	private String status;
}