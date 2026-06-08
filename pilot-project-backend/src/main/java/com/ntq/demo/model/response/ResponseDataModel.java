package com.ntq.demo.model.response;

import lombok.*;

/**
 * This class is used to declare properties for data that response to client-side when call RestAPI
 *
 * @author Quang
 * @since 2026-04-28
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ResponseDataModel<T> {
	private int responseCode;
	private String responseMsg;
	private T data;

	/**
	 * Success/Failed response (No data)
	 *
	 * @param responseCode
	 * @param responseMsg
	 */
	public ResponseDataModel(int responseCode, String responseMsg) {
		this.responseCode = responseCode;
		this.responseMsg = responseMsg;
	}
}